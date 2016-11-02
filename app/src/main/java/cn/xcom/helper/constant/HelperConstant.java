package cn.xcom.helper.constant;

/**
 * Created by zhuchongkun on 16/5/27.
 */
public interface HelperConstant {
    /**
     *  是否打印log
     *  true 打印
     */
    public static final boolean isLog=true;
    /**
     *
     */
    public static final String DEVICE_STATE="2";
    /**
     * 请求地址
     */
    public static final String NET_HOST="http://bang.xiaocool.net";
    /**
     * 接口前缀
     */
    public static final String NET_HOST_PREFIX = "http://www.my51bang.com/index.php?g=apps&m=index&";

    /**
     * 是否实名认证（1是,2否）
     */
    public static final String IS_HAD_AUTHENTICATION = "IS_HAD_AUTHENTICATION";

    /**
     * 是否保险认证（1-已认证， －1认证中， 0 未认证或认证失败）
     */
    public static final String IS_INSURANCE = "IS_INSURANCE";

    /**
     * 邀请码
     */
    public static final String MY_REFERAL = "MY_REFERAL";

}
