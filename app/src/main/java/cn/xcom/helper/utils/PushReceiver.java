package cn.xcom.helper.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.activity.BillActivity;
import cn.xcom.helper.activity.ChatActivity;
import cn.xcom.helper.activity.HomeActivity;
import cn.xcom.helper.activity.InsureActivity;
import cn.xcom.helper.activity.LoginActivity;
import cn.xcom.helper.activity.MyOrderActivity;
import cn.xcom.helper.activity.OrderTakingActivity;
import cn.xcom.helper.activity.WalletActivity;
import cn.xcom.helper.bean.OrderHelper;
import cn.xcom.helper.bean.UserInfo;

/**
 * Created by Administrator on 2016/9/8.
 */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private static final String JPUSHMESSAGE = "JPUSHMESSAGE";
    private static final String JPUSHHOMEWORK = "JPUSHHOMEWORK";
    private static final String JPUSHTRUST = "JPUSHTRUST";
    private static final String JPUSHNOTICE = "JPUSHNOTICE";
    private static final String JPUSHDAIJIE = "JPUSHDAIJIE";
    private static final String JPUSHLEAVE = "JPUSHLEAVE";
    private static final String JPUSHACTIVITY = "JPUSHACTIVITY";
    private static final String JPUSHCOMMENT = "JPUSHCOMMENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String type = bundle.getString(JPushInterface.EXTRA_EXTRA);

        if (type == null) return;
        String key = "";
        String v = "";
        try {
            JSONObject jsonObject = new JSONObject(type);
            key = jsonObject.getString("key");
            v = jsonObject.getString("v");
        } catch (JSONException e) {
            Log.i(TAG, "JSONException" + type);
            e.printStackTrace();
        }
        Log.i(TAG, "[PushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));


        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.i(TAG, "[PushReceiver] 接收Registeration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "[PushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "[PushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.i(TAG, "[PushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            switch (key){

                case "loginFromOther":
                    String title = "您的账号再异地登录";
                    String message ="您需要重新登陆";
                    popLogOutDialog(title,message);
                    break;

                case "prohibitVisit":
                    title = "封号";
                    message = "您的账号因为言论已被封停";
                    popLogOutDialog(title,message);
                    break;

            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.i(TAG, "[PushReceiver] 用户点击打开了通知");
            openNotification(context, key, v);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.i(TAG, "[PushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.i(TAG, "[PushReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.i(TAG, "[PushReceiver] Unhandled intent - " + intent.getAction());
        }
    }


    private void openNotification(Context context, String key, String value) {
        Intent intent;
        switch (key) {
            case "newTask":
                intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from", "push");
                context.startActivity(intent);
                break;
            case "newMessage":
                intent = new Intent(context, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value);
                context.startActivity(intent);
                break;
            case "sendTaskType":
                intent = new Intent(context, BillActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case "acceptTaskType":
                intent = new Intent(context, OrderTakingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case "myWallet":
                intent = new Intent(context, WalletActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case "buyOrderType":
                intent = new Intent(context, MyOrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("order_type", OrderHelper.BuyerOrder);
                context.startActivity(intent);
                break;
            case "businessOrderType":
                intent = new Intent(context, MyOrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("order_type", OrderHelper.SellerOrder);
                context.startActivity(intent);
                break;
            case "loginFromOther":

                break;
            case "certificationType":
                intent = new Intent(context, InsureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case "prohibitVisit":

                break;
        }


    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


    /**
     * 接受推送弹出提示框
     *
     * @param title
     * @param message
     */
    private void popLogOutDialog(String title, String message) {
        List<Activity> activities = HelperApplication.getInstance().getActivities();
        if(activities.size()==0){
            return;
        }
        final Activity activity = activities.get(activities.size()-1);
        if (activity==null){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserInfo userInfo = new UserInfo();
                userInfo.clearDataExceptPhone(activity);
                SPUtils.clear(activity);
                JPushInterface.stopPush(activity);
                activity.startActivity(new Intent(activity, LoginActivity.class));
                HelperApplication.getInstance().onTerminate();
            }
        });
        builder.show();

    }

}
