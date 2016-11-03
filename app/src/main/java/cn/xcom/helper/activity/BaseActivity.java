package cn.xcom.helper.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.utils.ToastUtil;

/**
 * Created by zhuchongkun on 16/5/27.
 * 基础页
 */
public class BaseActivity extends FragmentActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*IntentFilter filter = new IntentFilter("com.USER_ACTION");
        this.registerReceiver(new Receiver(), filter);*/
        //添加ctivity集合
        HelperApplication.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏

    }

    /**
     * 接受推送通知并通知页面添加小红点
     */
   /* public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Recevier1", "接收到:");
            String key = SPUtils.get(context, "push", "").toString();
            String state = SPUtils.get(context,"pushstate","").toString();
            String title = "";
            String message = "";
            switch (key){
                case "newTask":
                    title = "所在城市有新的任务";
                    if(state.equals("1")){
                        message = "是否去抢单？";
                    }
                    popDialog(context,title,message);
                    break;
                case "newMessage":
                    title = "您有新的留言消息";
                    message ="是否立即查看？";
                    popDialog(context,title,message);
                    break;
                case "sendTaskType":
                    title = "您的任务状态改变";
                    if(state.equals("1")){
                        message = "已被抢";
                    }
                    if(state.equals("2")){
                        message = "对方已上门";
                    }
                    if(state.equals("3")){
                        message = "对方已申请付款";
                    }
                    if(state.equals("4")){
                        message = "对方已上门";
                    }
                    if(state.equals("5")){
                        message = "对方已评价";
                    }
                    popDialog(context,title,message);
                    break;
                case "acceptTaskType":
                    title = "您接的任务状态改变";
                    if(state.equals("1")){
                        message = "对方已同意您接单";
                    }
                    if(state.equals("2")){
                        message = "对方已付款";
                    }
                    if(state.equals("3")){
                        message = "对方已评价";
                    }
                    if(state.equals("-1")){
                        message = "对方已取消";
                    }
                    popDialog(context,title,message);
                    break;
                case "myWallet":
                    title = "钱包数据已更新";
                    message = "是否立即查看？";
                    popDialog(context,title,message);
                    break;
                case "buyOrderType":
                    title = "您购买的商品订单状态已更新";
                    if(state.equals("1")){
                        message = "商家已接单";
                    }
                    if(state.equals("2")){
                        message = "商家已发货";
                    }
                    if(state.equals("3")){
                        message = "商品已送达/订单已消费";
                    }
                    if(state.equals("4")){
                        message = "商家回复评论";
                    }
                    popDialog(context,title,message);
                    break;
                case "businessOrderType":
                    title = "您发布的商品订单状态已更新";
                    if(state.equals("1")){
                        message = "有人购买您的商品";
                    }
                    if(state.equals("2")){
                        message = "对方已取消订单";
                    }
                    if(state.equals("3")){
                        message = "对方已付款";
                    }
                    if(state.equals("4")){
                        message = "订单已消费";
                    }
                    if(state.equals("5")){
                        message = "对方已评论";
                    }
                    popDialog(context,title,message);
                    break;
                case "loginFromOther":
                    title = "有人登陆您的账号";
                    message ="您需要重新登陆";
                    Intent loginIntent = new Intent(context,HomeActivity.class);
                    HelperApplication.getInstance().flag = "true";
                    startActivity(loginIntent);
                    break;
                case "certificationType":
                    title = "认证状态更新";
                    if(state.equals("1")){
                        message = "您已通过身份认证";
                    }
                    if(state.equals("2")){
                        message = "您未通过身份认证";
                    }
                    if(state.equals("3")){
                        message = "您已通过投保认证";
                    }
                    if(state.equals("4")){
                        message = "您未通过投保认证";
                    }
                    popDialog(context,title,message);
                    break;
                case "prohibitVisit":
                    title = "封号";
                    message = "封号";
                    popDialog(context,title,message);
                    break;
            }
        }

    }*/

    /**
     * 接受推送弹出提示框
     * @param context
     * @param title
     * @param message
     */
    private void popDialog(final Context context, String title, String message) {
        AlertView mAlertView = new AlertView(title, message, "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    ToastUtil.showShort(context,"确定");
                }
            }
        }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {

            }
        });
        mAlertView.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        HelperApplication.getInstance().finishActivity(this);
        super.onDestroy();
    }
}
