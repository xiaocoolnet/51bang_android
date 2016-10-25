package cn.xcom.helper.constant;

/**
 * Created by zhuchongkun on 16/5/29.
 */
public interface NetConstant extends HelperConstant {
    /**
     * 注册
     */
    public final static String NET_REGISTER = NET_HOST_PREFIX + "a=AppRegister&";
    /**
     * 登录
     */
    public final static String NET_LOGIN = NET_HOST_PREFIX + "a=applogin&";
    /**
     * 验证用户是否注册
     */
    public final static String NET_CHECK_PHONE = NET_HOST_PREFIX + "a=checkphone&";
    /**
     * 发送验证码
     */
    public final static String NET_GET_CODE = NET_HOST_PREFIX + "a=SendMobileCode&";
    /**
     * 修改密码
     */
    public final static String NET_RESET_PASSWORD = NET_HOST_PREFIX + "a=forgetpwd&";
    /**
     * 获取个人资料
     */
    public final static String NET_GET_USER_INFO = NET_HOST_PREFIX + "a=getuserinfo&";
    /**
     * 上传图片
     */
    public final static String NET_UPLOAD_IMG = NET_HOST_PREFIX + "a=uploadimg";
    /**
     * 图片路径
     */
    public final static String NET_DISPLAY_IMG = "http://www.my51bang.com/uploads/images/";
    /**
     * 修改头像
     */
    public final static String NET_UPDATE_HEAD = NET_HOST_PREFIX + "a=UpdateUserAvatar&";
    /**
     * 修改性别
     */
    public final static String NET_UPDATE_GENDER = NET_HOST_PREFIX + "a=UpdateUserSex&";
    /**
     * 修改城市
     */
    public final static String NET_UPDATE_CITY = NET_HOST_PREFIX + "a=UpdateUserCity&";
    /**
     * 修改姓名
     */
    public final static String NET_UPDATE_NAME = NET_HOST_PREFIX + "a=UpdateUserName&";
    /**
     * 通过用户编号获取身份认证状态
     */
    public final static String NET_GET_IDENTITY = NET_HOST_PREFIX + "a=getAuthRecord&";
    /**
     * 身份认证
     */
    public final static String NET_IDENTITY_AUTHENTICATION = NET_HOST_PREFIX + "a=Authentication&";
    /**
     * 获取任务分类列表
     */
    public final static String NET_GET_TASKLIST = NET_HOST_PREFIX + "a=getTaskTypeList&";
    /**
     * 获取我的今天是否签到
     */
    public final static String NET_GET_SIGN_STATE = NET_HOST_PREFIX + "a=GetMySignLog";
    /**
     * 签到
     */
    public final static String NET_TO_SIGN = NET_HOST_PREFIX + "a=SignDay";
    /**
     * 获取我的钱包
     */
    public final static String NET_GET_WALLET = NET_HOST_PREFIX + "a=GetMyWallet";
    /**
     * 获取我的收支记录
     */
    public final static String NET_GET_WALLET_LOG = NET_HOST_PREFIX + "a=GetMyWalletLog";
    /**
     * 获取我的提现申请记录
     */
    public final static String NET_GET_WITHDRAW_LOG = NET_HOST_PREFIX + "a=GetMyWithdrawLog";

    /**
     * 获取认证帮列表
     */
    public final static String NET_GET_AUTHENTICATION_LIST = NET_HOST_PREFIX + "a=getAuthenticationUserList";
    /*
    获取商品信息
     */
    public final static String GOODSLIST = NET_HOST_PREFIX + "a=getshoppinglist";
    /*
     获取字典
     */
    public final static String DICTIONARYS_LIST = NET_HOST_PREFIX + "a=getDictionaryList&";
    /*
    *发布任务
     */
    public final static String RELEASE = NET_HOST_PREFIX + "a=PublishGoods&";

    /**
     * 发布任务
     */
    public final static String PUBLISHTASK = NET_HOST_PREFIX + "a=publishTask&";

    /**
     * 更新任务支付状态
     */
    public final static String UPDATETASKPAY = NET_HOST_PREFIX + "a=UpdataTaskPaySuccess&";


    /*
    *根据地点获取抢单列表
     */
    public final static String GETTASKLIST = NET_HOST_PREFIX + "a=getTaskListByCity&";

    /*
    *根据商品id查询商品详情
    */
    public final static String SHOP_INFO = NET_HOST_PREFIX + "a=showshoppinginfo&";


    /*
    *根据商家的ID找出该商家发布的全部商品
    */
    public final static String SHOP_GOOD = NET_HOST_PREFIX + "a=getMyshoppinglist&";

    /*
    *我的任务
    */
    public final static String GET_MEY_TASK = NET_HOST_PREFIX + "a=getMyApplyTaskList&";


    /*
    *便民圈接口
    */
    public final static String CONVENIENCE = NET_HOST_PREFIX + "a=getbbspostlist&";

    /*
        快递查询
    */
    public static final String DELIEVER = "http://m.kuaidi100.com/index_all.html";
    /*
        发布便民圈信息
     */
    public static final String CONVENIENCE_RELEASE = NET_HOST_PREFIX + "a=addbbsposts&";

    /*
        删除商品
     */
    public static final String DELETE_GOOD = NET_HOST_PREFIX + "a=DeleteGoods&";

    /*
   *更新任务状态
   */
    public static final String UPDATE_TASK_STATE = NET_HOST_PREFIX + "a=UpdataTaskState&";

    /*
    抢单
     */
    public static final String GRAB_TASK = NET_HOST_PREFIX + "a=ApplyTask&";
    /*
   *更新任务
   */
    public final static String UPDATA = NET_HOST_PREFIX + "a=UpdateGoodsInfo&";
    /*
  *更新图片
  */
    public final static String UPDATA_PICTURE = NET_HOST_PREFIX + "a=UpdateGoodsPicture&";
    /*
    产品收藏
     */
    public final static String GOOD_COLLECTION = NET_HOST_PREFIX + "a=addfavorite&";
    /*
   产品是否收藏
    */
    public final static String GOOD_IS_COLLECTION = NET_HOST_PREFIX + "a=CheckHadFavorite&";

    /*
   取消收藏
   */
    public final static String GOOD_CANCLE_COLLECTION = NET_HOST_PREFIX + "a=cancelfavorite&";
    /*
    收藏列表
     */
    public final static String HAS_COLLECTION = NET_HOST_PREFIX + "a=getfavoritelist&";
    /*
    添加用户地址
     */
    public final static String ADD_ADRESS = NET_HOST_PREFIX + "a=AddAddress&";
    /*
    获取用户地址列表
     */
    public final static String GET_ADRESS = NET_HOST_PREFIX + "a=GetMyAddressList&";
    /*
    修改用户地址
     */
    public final static String FIX_ADRESS = NET_HOST_PREFIX + "a=UpdateAddress&";
    /*
    删除用户地址
     */
    public final static String DELETE_ADRESS = NET_HOST_PREFIX + "a=DeleteAddress&";

    /*
    我的接单
     */
    public final static String GET_MY_TASK = NET_HOST_PREFIX + "a=getMyApplyTaskList&";

    /*
    检查用户是否实名认证
     */
    public final static String Check_Had_Authentication = NET_HOST_PREFIX + "a=CheckHadAuthentication&";

    /*
    检查用户是否保险认证
     */
    public final static String Check_Insurance = NET_HOST_PREFIX + "a=CheckInsurance&";

    /*
    获取银行信息
     */
    public final static String GET_USER_BANK_INFO = NET_HOST_PREFIX + "a=GetUserBankInfo&";

    /**
     * 绑定银行账号
     */
    public final static String BIND_BINK_ACCOUNT = NET_HOST_PREFIX + "a=UpdateUserBank&";

    /**
     * 绑定支付宝账号
     */
    public final static String BIND_ALIPAY_ACCOUNT = NET_HOST_PREFIX + "a=UpdateUserBank&";

    /**
     * 提现申请
     */
    public final static String APPLY_WITHDRAW = NET_HOST_PREFIX + "a=ApplyWithdraw&";

    /*
    获取城市列表（长字符串）
     */
    public final static String GET_CITY_LIST = NET_HOST_PREFIX + "a=GetCityListToStr";

    /*
    获取我的技能
     */
    public final static String GET_MY_SKILLS = NET_HOST_PREFIX + "a=getSkillListByUserId&";

    /*
    修改我的技能
     */
    public final static String CHANGE_MY_SKILLS = NET_HOST_PREFIX + "a=UpdataUserSkill&";

    /*
   删除自己的帖子
    */
    public final static String DELETE_OWN_POST = NET_HOST_PREFIX + "a=deletebbspost&";


    /**
     * 获取我发布的任务列表
     */
    public final static String GET_TASK_LIST_BY_USERID = NET_HOST_PREFIX + "a=getTaskListByUserid&";

    /**
     * 查看任务详情
     */
    public final static String GET_TASK_INFO_BY_TASK_ID = NET_HOST_PREFIX + "a=getTaskInfoByTaskid&";

    /**
     * 举报别人的帖子
     */
    public final static String REPORT_OHTHER_POST = NET_HOST_PREFIX + "a=Report&";

    /**
     * 获取认证帮列表
     */
    public final static String GET_AUTHENTICATION_LIST = NET_HOST_PREFIX + "a=getAuthenticationUserList&";


    /**
     * 发布任务者取消订单
     */
    public final static String OWNER_CANCEL_TASK = NET_HOST_PREFIX + "a=OwnerCancelTask&";


    /**
     * 买家获取我的商城商品列表
     */
    public final static String BUYER_GET_SHOP_ORDER_LIST = NET_HOST_PREFIX + "a=BuyerGetShoppingOrderList&";

    /**
     * 商户分享h5
     */
    public final static String SHARE_SHOP_H5 = "http://www.my51bang.com/index.php?g=portal&m=article&a=shop&id=";

    /**
     * 我的订单页面 取消订单
     */
    public final static String CANCEL_ORDER = NET_HOST_PREFIX + "a=cancelorder&";


    /**
     * 发表订单评价
     */
    public final static String POST_COMMENT = NET_HOST_PREFIX + "a=SetEvaluate&";

    /**
     * 用户二维码h5
     */
    public final static String SHARE_QRCODE_H5 = "http://www.my51bang.com/index.php?g=portal&m=article&a=server&userid=";


    /**
     * 卖家确认验证码
     */
    public final static String VERIFY_SHOPPING_CODE = NET_HOST_PREFIX + "a=VerifyShoppingCode&";


    /**
     * 卖家获取订单列表
     */
    public final static String SELLER_GET_SHOPPING_ORDER_LIST = NET_HOST_PREFIX + "a=SellerGetShoppingOrderList&";


    /**
     * 获取聊天列表
     */
    public final static String GET_CHAT_LIST = NET_HOST_PREFIX + "a=xcGetChatListData&";


    /**
     * 获取聊天信息（两个人之间的）
     * send_uid,receive_uid
     */
    public final static String GET_CHAT_DATA = NET_HOST_PREFIX + "a=xcGetChatData&";


    /**
     *    发送聊天信息
     */
    public final static String SEND_CHAT_DATA = NET_HOST_PREFIX + "a=SendChatData&";


    /**
     * 产品上架
     */
    public final static String GOODS_SHANG_JIA = NET_HOST_PREFIX + "a=Shangjia&";

    /**
     * 产品下架
     */
    public final static String GOODS_XIA_JIA = NET_HOST_PREFIX + "a=Xiajia&";


    /**
     * 提交保险认证照片
     */
    public final static String UPDATE_USER_INSURANCE = NET_HOST_PREFIX + "a=UpdateUserInsurance&";


    /**
     * 热卖］更新状态
     */
    public final static String UPDATE_SHOPPING_STATE = NET_HOST_PREFIX + "a=UpdataShoppingState&";


}
