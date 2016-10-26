package cn.xcom.helper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import cn.xcom.helper.bean.DictionaryList;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtils;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——特卖
 *
 */
public class Sale1Fragment extends Fragment implements View.OnClickListener {
    private String TAG = "SaleFragment";
    private Context mContext;
    private PopupWindow popupWindow;
    private RelativeLayout rl_classification, rl_release;
    private ListView listView, lv_group;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Front> addlist = new ArrayList<>();
    private SaleAdapter saleAdapter;
    private GroupAdapter groupAdapter;
    private List <DictionaryList>addAllList=new ArrayList<>();
    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
          swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sale, container, false);
        listView = (ListView) view.findViewById(R.id.lv_fragment_listView);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorTheme);
        //swip.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorTextWhite));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 handler.removeCallbacks(runnable);
                 handler.postDelayed(runnable, 1000);
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
                                 ToastUtils.showToast(mContext, "刷新成功");


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
             }
         });
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
                intent.putExtra("judge","我是fragemnt");
                startActivity(intent);
                break;
        }

    }
//获取字典并弹出popuwindow
    private void showWindow(View parent) {
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
            popupWindow = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        popupWindow.showAsDropDown(parent);
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        clickPopuwindow();

    }
//popuwindow的点击事件,同时刷新listview
   public void clickPopuwindow(){
       final List aList=new ArrayList();
       lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view,
                                   int position, long id) {

               DictionaryList dictionaryList = addAllList.get(position);
               if (dictionaryList.getName().equals("全部")) {
                   aList.addAll(addlist);
               } else {
                   for (int i = 0; i < addlist.size(); i++) {
                       Front front = addlist.get(i);
                       if (dictionaryList.getId().equals(front.getType())) {
                           Log.d("=== 数据", front.getId());
                           aList.add(front);
                       } else if (dictionaryList.getName().equals("其他") && front.getType().length() <= 0) {
                           Log.d("=== 其他", front.getType().toString());
                           aList.add(front);
                       }
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