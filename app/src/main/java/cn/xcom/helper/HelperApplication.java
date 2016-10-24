package cn.xcom.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.xcom.helper.activity.HomeActivity;
import cn.xcom.helper.bean.TaskType;
import cn.xcom.helper.utils.SPUtils;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ToolUtil;

/**
 * Created by zhuchongkun on 16/5/27.
 */
public class HelperApplication extends Application {
    private Context mContext;
    private static HelperApplication instance;
    //全局变量，选择的任务分类
    private List<TaskType> taskTypes;

    //切换城市信息
    public double mCurrentLocLat = 0;
    public double mCurrentLocLon = 0;
    public String status = "";
    public String mDistrict = "";

    //实时定位坐标和地址
    public double mLocLat, mLocLon;
    public String mLocAddress;

    //是否强退标识
    public String flag = "false";

    /**
     * 打开的activity
     **/
    private List<Activity> activities = new ArrayList<Activity>();

    //当前的activity
    public Activity mActivity;

    // login user name
    public final String PREF_USERNAME = "username";
    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";
    /**
     * 把全国的省市区的信息以json的格式保存
     */
    public static JSONObject mCityJson;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        taskTypes = new ArrayList<>();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        this.registerReceiver(new Receiver(), filter);

        //初始化极光
        setPush();
        //初始化地图
        SDKInitializer.initialize(mContext);
        //初始化ImageLoader
        initImageLoader(mContext);
        //初始化城市数据
        initJsonData();

    }

     public class Receiver extends BroadcastReceiver {

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
                    popDialog(title,message);
                    break;
                case "newMessage":
                    title = "您有新的留言消息";
                    message ="是否立即查看？";
                    popDialog(title,message);
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
                    popDialog(title,message);
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
                    popDialog(title,message);
                    break;
                case "myWallet":
                    title = "钱包数据已更新";
                    message = "是否立即查看？";
                    popDialog(title,message);
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
                    popDialog(title,message);
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
                    popDialog(title,message);
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
                    popDialog(title,message);
                    break;
                case "prohibitVisit":
                    title = "封号";
                    message = "封号";
                    popDialog(title,message);
                    break;
            }
        }

    }

    /**
     * 接受推送弹出提示框
     *
     * @param title
     * @param message
     */
    private void popDialog(String title, String message) {
        final Activity activity = activities.get(activities.size()-1);
        if (activity==null){
            return;
        }
        AlertView mAlertView = new AlertView(title, message, "取消", new String[]{"确定"}, null, activity, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    ToastUtil.showShort(activity, "确定");
                }
            }
        }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {

            }
        });
        mAlertView.show();
    }

    /**
     * 新建了一个activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }


    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }


    /**
     * 结束所有的activity
     */
    @Override
    public void onTerminate() {
        super.onTerminate();

        for (Activity activity : activities) {
            activity.finish();
        }

        System.exit(0);
    }

    private void setPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static HelperApplication getInstance() {
        return instance;
    }

    public List<TaskType> getTaskTypes() {
        return taskTypes;
    }

    public void setTaskTypes(List<TaskType> taskTypes) {
        this.taskTypes = taskTypes;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void initChat() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;

    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            String s = ToolUtil.readFromAsset(mContext, "city.json");
            if (s != null) {
                mCityJson = new JSONObject(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initImageLoader(Context mContext) {
//        File cacheDir = StorageUtils.getOwnCacheDirectory(mContext,"caterin/Cache");
//        // LruCache通过构造函数传入缓存值，以KB为单位。
//        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        // 使用最大可用内存值的1/8作为缓存的大小。
//        int cacheSize = maxMemory / 8;
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
//                .memoryCacheExtraOptions(480, 800)
//                .threadPoolSize(3)// 线程池内加载的数量
//                .threadPriority(Thread.NORM_PRIORITY - 1).tasksProcessingOrder(QueueProcessingType.FIFO) // default
//                .denyCacheImageMultipleSizesInMemory().memoryCache(new UsingFreqLimitedMemoryCache(cacheSize))
//                // You can pass your own memory cache
//                .memoryCache(new LruMemoryCache(cacheSize)).memoryCache(new WeakMemoryCache())
//                // implementation你可以通过自己的内存缓存实现
//                .memoryCacheSize(cacheSize).memoryCacheSizePercentage(13) // default
//                .discCacheSize(cacheSize)
//                // .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                // 将保存的时候的URI名称用MD5加密
//                // .discCacheFileNameGenerator(new
//                // HashCodeFileNameGenerator())// 将保存的时候的URI名称用HASHCODE加密
//                .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(1000) // 缓存的File数量
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()).imageDownloader(new BaseImageDownloader(mContext, 5 * 1000, 30 * 1000))
//                // connectTimeout (5s), readTimeout(30s)超时时间
//                .writeDebugLogs() // Remove for release app
//                .build();
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(mContext);
        ImageLoader.getInstance().init(configuration);// 全局初始化此配置
    }

}
