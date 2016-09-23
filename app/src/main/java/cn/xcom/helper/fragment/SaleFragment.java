package cn.xcom.helper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.ReleaseActivity;
import cn.xcom.helper.adapter.GroupAdapter;
import cn.xcom.helper.adapter.SaleAdapter;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.entity.DictionaryList;
import cn.xcom.helper.entity.Front;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.wheel.widget.TosGallery;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——特卖
 *
 */
public class SaleFragment extends Fragment implements View.OnClickListener {
    private String TAG = "SaleFragment";
    private Context mContext;
    private PopupWindow popupWindow;
    private RelativeLayout rl_classification, rl_release;
    private ListView listView, lv_group;
    private View view;
    private List<Front> addlist = new ArrayList<>();
    private SaleAdapter saleAdapter;
    private GroupAdapter groupAdapter;
    private List <DictionaryList>addAllList=new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sale, container, false);
        listView = (ListView) view.findViewById(R.id.lv_fragment_listView);
        String url = NetConstant.GOODSLIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("=====显示111",""+s);
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        String jsonObject1 = jsonObject.getString("data");
                        Gson gson = new Gson();
                        addlist = gson.fromJson(jsonObject1,
                                new TypeToken<ArrayList<Front>>() {
                                }.getType());
                        Log.e("========fragment", "" + addlist.size());
                        saleAdapter = new SaleAdapter(addlist, mContext);
                        listView.setAdapter(saleAdapter);
                        saleAdapter.notifyDataSetChanged();



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(mContext, "网络连接错误，请检查您的网络");
            }
        });
        SingleVolleyRequest.getInstance(getContext()).addToRequestQueue(request);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent1=new Intent(getActivity(),SaleDetailActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("item",addlist.get(position));
//                intent1.putExtras(bundle);
//                Log.i("---", position + "");
//               // intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent1);
//            }
//        });
        return view;
    }
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initView();
    }
    private void initView() {
        rl_classification = (RelativeLayout) getView().findViewById(R.id.rl_fragment_sale_classification);
        rl_classification.setOnClickListener(this);
        rl_release = (RelativeLayout) getView().findViewById(R.id.rl_fragment_sale_release);
        rl_release.setOnClickListener(this);
        DictionaryList dictionaryList=new DictionaryList();
        dictionaryList.setId("1000");
        dictionaryList.setName("全部");
        addAllList.add(dictionaryList);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment_sale_classification:
                showWindow(v);
                break;
            case R.id.rl_fragment_sale_release:
               Intent intent=new Intent(getActivity(), ReleaseActivity.class);
                startActivity(intent);
                break;
        }

    }
//获取字典并弹出popuwindow
    private void showWindow(View parent) {

        if (popupWindow == null) {
            Log.d("=====显示", "" + "=================");
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.group_list, null);
            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            String url1=NetConstant.DICTIONARYS_LIST;
            StringPostRequest request=new StringPostRequest(url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        Log.d("=====显示", "" + s);
                        JSONObject jsonObject=new JSONObject(s);
                        String state = jsonObject.getString("status");
                        if (state.equals("success")){
                            String jsonObject1 = jsonObject.getString("data");
                            Gson gson = new Gson();
                          List<DictionaryList>  groups=gson.fromJson(jsonObject1,
                                    new TypeToken<ArrayList<DictionaryList>>() {
                                    }.getType());
                            addAllList.addAll(groups);
                            Log.d("=====显示后面",""+addAllList.size());
                            groupAdapter = new GroupAdapter(mContext, addAllList);
                            lv_group.setAdapter(groupAdapter);
                            groupAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            request.putValue("type","3");
            SingleVolleyRequest.getInstance(getContext()).addToRequestQueue(request);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, TosGallery.LayoutParams.MATCH_PARENT, TosGallery.LayoutParams.WRAP_CONTENT);
        }
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);


        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
//        ColorDrawable cd = new ColorDrawable(0x000000);
//        popupWindow.setBackgroundDrawable(cd);
        //  popupWindow.setBackgroundDrawable(new ColorDrawable());
        // backgroundAlpha();
//        WindowManager windowManager =
//                (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //      显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
//        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
//                - popupWindow.getWidth()/2 ;
//        Log.i("coder", "xPos:" + xPos);
        popupWindow.showAsDropDown(parent, -100, 0);
        clickPopuwindow();

    }
//popuwindow的点击事件,同时刷新listview
   public void clickPopuwindow(){
       final List aList=new ArrayList();
       lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view,
                                   int position, long id) {

              DictionaryList dictionaryList=addAllList.get(position);
                if (dictionaryList.getName().equals("全部")){
                   aList.addAll(addlist);
                }else{
                    for (int i=0;i<addlist.size();i++){
                        Front front = addlist.get(i);
                        if (dictionaryList.getId().equals(front.getType())){
                            Log.d("=== 数据",front.getId());
                            aList.add(front);
                        }else if (dictionaryList.getName().equals("其他")&&front.getType().length()<=0){
                            Log.d("=== 其他",front.getType().toString());
                            aList.add(front);
                        }
// else if (!front.getType().contains(dictionaryList.getId())){
//                            Toast.makeText(getContext(),
//                                    "您需要的还未上架，请等待....", Toast.LENGTH_SHORT)
//                                    .show();
//                        }

                    }
                }

               saleAdapter = new SaleAdapter(aList, mContext);
               listView.setAdapter(saleAdapter);
               saleAdapter.notifyDataSetChanged();

//               Toast.makeText(getContext(),
//                       "您点击的位置是"+position, Toast.LENGTH_SHORT)
//                       .show();

               if (popupWindow != null) {
                   popupWindow.dismiss();
               }
           }
       });
   }

}