package com.adult.android.model.constants;

public class ServiceUrlConstants {
	public static final String PLATFORM_ID = "PLATFORM_ID";
	public static final String TEST_PHONE_NO = "13366287438";// 测试手机号
	public static final String TEST_LOCATION = "01";// 北京
	public static final String TEST_SIG = "";

	// 二维码前缀
	// public static final String CODE_HOST =
	// "http://yundongma.cn/yunyike-wap-shopping/shopper/connectShopper/";

	// public static final String UPLOAD_PATH =
	// "http://192.168.10.93:8081/yunyike-app-web/upload/img";
	public static enum CODE_HOST {
		TEST(1,
				"http://yundongma.cn/yunyike-wap-shopping/shopper/connectShopper/"), RELEASE(
				2,
				"http://shop.yunyike.com/yunyike-wap-shopping/shopper/connectShopper/");
		private int id;
		private String codeHost;

		private CODE_HOST(int id, String codeHost) {
			this.id = id;
			this.setCodeHost(codeHost);

		}

		public int getId() {
			return id;
		}

		public String getCodeHost() {
			return codeHost;
		}

		public void setCodeHost(String codeHost) {
			this.codeHost = codeHost;
		}

	}

	public static enum HOST {
		DEVELOP(0, "http://192.168.10.93:8081/yunyike-app-web/router",
				"http://yundongma.cn/yunyike-wap-shopping"), TEST(1,
				"http://yundongma.cn/yunyike-app-web/router",
				"http://static.yunyike.com"), RELEASE(2,
				"http://shop.yunyike.com/yunyike-app-web/router",
				"http://static.yunyike.com");
		private int id;
		private String apiHost;
		private String imageHost;

		private HOST(int id, String apiHost, String imageHost) {
			this.id = id;
			this.apiHost = apiHost;
			this.imageHost = imageHost;
		}

		public int getId() {
			return id;
		}

		public String getApiHost() {
			return apiHost;
		}

		public String getImageHost() {
			return imageHost;
		}
	}

	public static enum MAIN_INDEX_URL {
		DEVELOP("http://192.168.0.197:8080/app/index.html",
				"http://192.168.0.197:8080/app/act/xsj_9.html",
				"http://192.168.1.202:8080/view/productMix/toShowList"), TEST(
				"http://192.168.0.197:8080/app/index.html",
				"http://192.168.0.197:8080/app/act/xsj_9.html",
				"http://192.168.1.202:8080/view/productMix/toShowList"), REGRESS(
				"http://192.168.0.197:8080/app/index.html",
				"http://192.168.0.197:8080/app/act/xsj_9.html",
				"http://192.168.1.202:8080/view/productMix/toShowList"), RELEASE(
				"http://api.ccigmall.com",
				"http://api.ccigmall.com/act/fenlei.html",
				"http://api.ccigmall.com/act/guojiaguan.html");
		private String homePageUrl;
		private String categoryUrl;
		private String nationalMuseumUrl;

		MAIN_INDEX_URL(String homePageUrl, String categoryUrl,
				String nationalMuseumUrl) {
			this.homePageUrl = homePageUrl;
			this.categoryUrl = categoryUrl;
			this.nationalMuseumUrl = nationalMuseumUrl;
		}

		public String getHomePageUrl() {
			return homePageUrl;
		}

		public String getCategoryUrl() {
			return categoryUrl;
		}

		public String getNationalMuseumUrl() {
			return nationalMuseumUrl;
		}
	}

	public static enum WAP_URL {
		TEST(1, "http://192.168.1.208:8080/ccigmall-wap-customer"), RELEASE(2,
				"http://kj.ccigmall.com/");

		private int id;
		private String wapUrl;

		private WAP_URL(int id, String wapUrl) {
			this.id = id;
			this.wapUrl = wapUrl;
		}

		public int getId() {
			return id;
		}

		public String getWapUrl() {
			return wapUrl;
		}
	}

	public static enum UPDATE {
		TEST(1, "http://192.168.1.215:8089/b2client/findNewVersion"), REGRESS(
				2, "http://192.168.1.215:8089/b2client/findNewVersion"), RELEASE(
				3, "http://www.91xsj.com/app/download/b2client/findNewVersion");

		private int id;
		private String host;

		private UPDATE(int id, String host) {
			this.id = id;
			this.host = host;
		}

		public int getId() {
			return id;
		}

		public String getHost() {
			return host;
		}
	}

	/**
	 * api环境配置
	 */
	public static final HOST mHost = HOST.RELEASE;
	/**
	 * 二维码环境配置
	 */
	public static final CODE_HOST mCodeHost = CODE_HOST.RELEASE;
	/**
	 * 更新配置
	 */
	public static final UPDATE mUpdate = UPDATE.TEST;
	/**
	 * 主页三个tab的URL地址。
	 */
	public static final MAIN_INDEX_URL mainIndexUrl = MAIN_INDEX_URL.TEST;

	/**
	 * wap环境配置
	 */
	public static final WAP_URL mWapUrl = WAP_URL.TEST;

	/**
	 * 推送配置
	 */
	public static final JPushUrl jPushUrl = JPushUrl.TEST;

	/**
	 * 签名密码
	 */
	public static final String APP_SECRET = "a4160d00-b083-40f9-a749-07aef8781d52";

	public static final String COUPON_JSP_URL = "http://api.ccigmall.com/promotion_notes.jsp";

	/**
	 * 获取API host
	 */
	public static final String getApiHost() {
		return mHost.getApiHost();
	}

	public static final String getCodeHost() {
		return mCodeHost.getCodeHost();
	}

	/**
	 * 获取WAP url
	 */
	public static final String getWapUrl() {
		return mWapUrl.getWapUrl();
	}

	/**
	 * 获取 更新应用的Host
	 */
	public static final String getUpdateHost() {
		return mUpdate.getHost();
	}

	/**
	 * 获取image host
	 */
	public static final String getImageHost() {
		return mHost.getImageHost();
	}

	public static final String CHECK_UPGRADE = getApiHost() + "checkupgrade";

	public static final String APP_KEY = "appKey";
	public static final String APP_KEY_VALUE = "100001";
	public static final String APP_SECRET_NAME = "appSecret";
	public static final String APP_SECRET_VALUE = "a4160d00-b083-40f9-a749-07aef8781d52";

	public static final String VERSION = "v";
	public static final String VERSION_VALUE = "1.0";
	public static final String MOTHOD = "method";

	public static final String SIGN = "sign";

	/**
	 * @author zhaoweiChuang
	 * @descripte 个人信息字段封装
	 */
	public class UserParams extends ServiceUrlConstants {
		// 获取个人信息
		public static final String userGet = "user.get";
		// 修改个人信息的 校验码
		public static final String userInfoCaptchaGet = "user.info.captcha.get";
		// 修改个人信息
		public static final String userGetEdit = "user.get.edit";

		public static final String userGetMobelEdit = "user.mobile.edit";
		// 实名认证
		public static final String userValidate = "user.validate.realName";
		// 普通方式注册的时候需要使用验证法，该方法是获取验证码，目前现在是默认验证码是5678
		public static final String captcha = "user.captcha.get";
		// 普通方式注册，用户名，密码，验证码为必须输入
		public static final String register = "user.register";
		// 登录（快速注册和普通注册都是使用该方式登录）
		public static final String login = "user.login";
		// 登录（快速注册和普通注册都是使用该方式登录）
		public static final String setReceiveArea = "receiveArea.set";
		public static final String getReceiveArea = "receiveArea.get";
		/**
		 * 登录之后才能够修改密码，该方法是修改密码 *
		 */
		public static final String pwdedit = "user.pwd.edit";
		/**
		 * 登录之后才能够修改密码，该方法是修改密码的验证码
		 */
		public static final String pwdeditCaptchaGet = "user.pwd.captcha.get";
		/**
		 * 登录之后才能够修改密码，该方法是验证验证码
		 */
		public static final String pwdeditCaptchaValidate = "user.captcha.validate";
		/**
		 * 登录之后才能够修改密码，该方法是修改密码的验证码
		 */
		public static final String loginOut = "user.logout";
		// 采用手机注册的时候，需要通过短信获取密码，默认密码是1234，手机号码必须
		public static final String defaultpwd = "user.defaultpwd.get";
		// 采用手机方式快速注册，手机号和密码必须传递
		public static final String quickRegister = "user.quick.register";

		/**
		 * 添加反馈 *
		 */
		public static final String feedbackAdd = "feedback.add";
		/**
		 * 找回密码的验证码 *
		 */
		public static final String FIND_PWD_VD = "user.find.pwd.captcha.get";
		/**
		 * 找回密码 *
		 */
		public static final String FIND_PWD = "user.find.pwd";
		// 用户名

		// 获取分类国家国旗
		public static final String getCountry = "country.get";
		// 搜索列表
		public static final String searchlist = "product.search";
		// 搜索热词
		public static final String hotSearch = "product.hot.search";
		// 分类-品类
		public static final String categorySort = "category.disp.get";

		public static final String UM = "um";
		// 昵称
		public static final String NM = "nm";
		// 生日
		public static final String BD = "bd";
		// 性别
		public static final String GD = "gd";
		// 用户id
		public static final String UID = "uid";
		public static final String MB = "mb";
		public static final String SESSIONID = "sessionId";
		public static final String NAME = "name";
		public static final String AC = "ac";
		public static final String PWD = "pwd";
		public static final String PASSWORD = "password";
		public static final String EL = "el";
		public static final String VD = "vd";
		public static final String OPWD = "oPwd";
		public static final String NPWD = "nPwd";
		public static final String CONTENT = "content";
		public static final String TYPE = "type";
		public static final String WAY = "way";
		public static final String RT = "rt";
		public static final String LEVEL = "level";

	}

	/**
	 * 购物车参数
	 */
	public class CartParams {
		/**
		 * 添加商品
		 */
		public static final String CART_ADD = "cart.add";
		/**
		 * 删除商品
		 */
		public static final String CART_DEL = "cart.del";
		/**
		 * 更新商品数量
		 */
		public static final String CART_QTY_UPDATE = "cart.qty.update";
		/**
		 * 获取购物车数据
		 */
		public static final String CART_GET = "cart.get";
		/**
		 * 获取选中商品信息
		 */
		public static final String CART_SELECT = "cart.select";
		/**
		 * 购物车提交订单
		 */
		public static final String ORDER_SUBMIT = "order.submit";
		/**
		 * 获取购物车中的商品数量
		 */
		public static final String CART_COUNT_GET = "cart.count.get";
		/**
		 * UserId
		 */
		public static final String USER_ID = "userId";
		/**
		 * KEY
		 */
		public static final String KEY = "key";
		/**
		 * 商品SKU_ID
		 */
		public static final String SKU_ID = "skuId";
		/**
		 * 商品数量
		 */
		public static final String QTY = "qty";
		/**
		 * SKU_LIST
		 */
		public static final String SKU_ID_LIST_STR = "skuIdListStr";
	}

	/**
	 * 主页数据
	 */
	public class HomePageParams {
		/**
		 * 获取主业数据
		 */
		public static final String CMS_INDEX_GET = "item.getHome";
	}

	/**
	 * 类目数据
	 */
	public class CategoryParams {

		public static final String GET_CATEGORY_LIST = "item.getCategoryList";

		public static final String CATEGORY_ID = "categoryId";

		public static final String PROMOTION_ID = "promotionId";

		public static final String PAGE_COUNT = "pageCount";

		public static final String GET_PRODUCT_LIST_BY_KEYWORD = "item.getProductListByKeyword";

		public static final String GET_PRODUCT_LIST = "item.getProductList";

		public static final String GET_PRODUCT_DETAIL = "item.getProductDetail";

		public static final String FILTER_VALUE_ID_LIST = "filterValueIds";

		public static final String SORT_BY = "sortBy";

		public static final String SORT_BY_VALUE = "sortVal";

		public static final String KEY_WORD = "keyword";

		public static final String ID = "id";

	}

	/**
	 * 购物车数据
	 */
	public class CartParams2 {

		public static final String GET_CART_LIST = "cart.getCartList";

		public static final String ADD_TO_CART = "cart.addToCart";

		public static final String UPDATE_CART = "cart.updateCart";

		public static final String DELETE_CART = "cart.deleteCartProduct";

		public static final String CALCULATE_CART = "cart.calculateCart";

		public static final String GET_CART_AMOUNT = "cart.getCartAmount";

		public static final String COMMIT_ORDER = "cart.commitOrder";

		public static final String UPDATE_ADDRESS = "cart.updateAddress";

		public static final String USER_ID = "userId";

		public static final String SKU_ID = "skuId";

		public static final String SKU_ID_LIST = "skuIds";

		public static final String COUNT = "count";

		public static final String PAGE_COUNT = "pageCount";

		public static final String SKU_ID_QUICK_BUY = "skuIdPromptlyBuy";

		public static final String QTY_QUICK_BUY = "qtyPromptlyBuy";

		public static final String QUICK_BUY = "promptlyBuy";

		public static final String COUPON_ID = "couponId";

		public static final String MESSAGE = "message";

		public static final String PAY_WAY = "payWay";

		public static final String ADDRESS_ID = "addressId";

		public static final String PROVINCE_ID = "provinceId";

		public static final String CITY_ID = "cityId";

		public static final String AREA_ID = "areaId";

		public static final String DETAIL_ADDRESS = "detailAddress";

		public static final String RECEIVER_NAME = "receiverName";

		public static final String RECEIVER_MB = "receiveMb";

		public static final String USE_COUPON_CODE_ID = "useCouponCodeId";

	}

	public class UserParams2 {

		public static final String LOGIN = "user.login";

		public static final String REGIST = "user.register";

		public static final String FINDBACK_PASSWORD = "user.findBackPassword";

		public static final String GET_VERIFY_CODE = "user.getVerifyCode";

		public static final String GET_PAY_INFO = "user.getPayInfo";

		public static final String GET_MY_TOPIC_LIST = "user.myTopicList";

		public static final String GET_MY_COUPON_LIST = "user.getCouponList";

		public static final String GET_MY_EVALUATION_LIST = "user.myEvaluationList";

		public static final String GET_USER_INFO = "user.getUserInfo";

		public static final String SAVE_USER_INFO = "user.saveUserInfo";

		public static final String BIND_MOBILE = "user.bindMobile";

		public static final String UPDATE_PASSWORD = "user.updatePassword";

		public static final String USER_ID = "userId";

		public static final String SKU_ID = "skuId";

		public static final String PAGE_COUNT = "pageCount";

		public static final String PAY_TYPE = "payType";

		public static final String ORDER_ID = "orderId";

		public static final String ACCOUNT = "account";

		public static final String PASSWORD = "password";

		public static final String MOBILE = "mobile";

		public static final String VERIFY_CODE = "verifyCode";

		public static final String GEMDER = "gender";

		public static final String NICK_NAME = "nickname";

		public static final String STATUS = "status";

		public static final String MARRAGE_STATUS = "marriageStatus";

		public static final String SEX_ORIENTATION = "sexualOrientation";

	}

	/**
	 * 社区数据
	 */
	public class CommunityParams {

		public static final String GET_COMMUNITY_LIST = "forum.getCommunityList";

		public static final String GET_TOPIC_LIST_BY_COMMUNITY_ID = "forum.getTopicListByCommunityId";

		public static final String GET_TOPIC_DETAIL = "forum.getTopicDetail";

		public static final String POST_REPLY = "forum.postReply";

		public static final String REWORD_TOPIC = "forum.rewardTopic";

		public static final String COMMUNITY_ID = "communityId";

		public static final String TOPIC_ID = "topicId";

		public static final String USER_ID = "userId";

		public static final String TITLE = "title";

		public static final String CONTENT = "content";

		public static final String AMOUNT = "amount";

		public static final String PAGE_COUNT = "pageCount";

		public static final String TYPE = "type";
	}

	/**
	 * 订单数据
	 */
	public class OrderParams2 {

		public static final String GET_ORDER_LIST = "order.getOrderList";

		public static final String GET_ORDER_DETAIL = "order.getOrderDetail";

		public static final String DELETE_ORDER = "order.deleteOrder";

		public static final String GET_EVALUATION_LIST = "order.getEvaluationList";

		public static final String GET_MY_EVALUATION_LIST = "user.myEvaluationList";

		public static final String POST_EVALUATION = "order.evaluation";

		public static final String GET_LOGISTICS_PRICE = "cart.getLogisticsPrice";

		public static final String EVALUATION_PRICE = "evaluationPrice";

		public static final String EVALUATION_CONTENT = "evaluationContent";

		public static final String ORDER_ID = "orderId";

		public static final String USER_ID = "userId";

		public static final String PAGE_COUNT = "pageCount";

		public static final String STATUS = "status";

		public static final String SKU_ID = "skuId";

		public static final String PROVINCE = "province";

	}

	/**
	 * 订单数据
	 */
	public class OrderParams {
		/**
		 * 订单类型
		 */
		public static final String ORDER_TYPE = "orderType";
		/**
		 * 订单类型
		 */
		public static final String RECEIVE_NAME = "receiveName";
		/**
		 * 订单类型
		 */
		public static final String RECEIVE_MOBILE_PHONE = "receiveMobilePhone";
		/**
		 * 订单类型
		 */
		public static final String RECEIVE_ADDRESS = "receiveAddress";
		/**
		 * 订单类型
		 */
		public static final String MESSAGE = "message";
		/**
		 * 订单类型
		 */
		public static final String ORDER_PLATFORM = "orderPlatform";

		public static final String CARS_SKU_DTOS = "customerOrder";

	}

	/**
	 * 购物车参数
	 */
	public class OrderPayParams {
		/**
		 * 获取支付信息
		 */
		public static final String PAY_GET = "pay.get";
		/**
		 * 订单号
		 */
		public static final String OID = "oid";
		/**
		 * 支付金额
		 */
		public static final String PA = "pa";
		/**
		 * userId
		 */
		public static final String CID = "cid";
		/**
		 * 平台号
		 */
		public static final String CN = "cn";
		/**
		 * 支付渠道
		 */
		public static final String BPM = "bpm";

		public static final String SESSION_ID = "sessionId";
	}

	/**
	 * 优惠券
	 */
	public class CouponParams {
		/**
		 * 我的优惠券
		 */
		public static final String MY_COUPON_GET = "my.coupon.get";
		/**
		 * 订单优惠券
		 */
		public static final String MY_ORDER_COUPON_GET = "myorder.coupon.get";
		/**
		 * 订单优惠券数量
		 */
		public static final String MY_ORDER_COUPON_COUNT = "myorder.coupon.getCount";
	}

	/**
	 * WebView 相关
	 */
	public class WebView {
		/**
		 * JS 版本号
		 */
		public static final String JSAPI = "1";
		/**
		 * 接口名
		 */
		public static final String WEBVIEW_INTERFACE_NAME = "ccigmall_b2c";
		/**
		 * 跳转活动页的key
		 */
		public static final String PROMOTION_URL = "promotionUrl";

	}

	public class PayConstant {
		public static final String MERCHANTPWD = "767727";
	}

	/** 第三方分享相关 **/

	/**
	 * QQ分享，应用从腾讯QQ开发者平台网站申请的APP_ID*
	 */
	public static final String QQ_APP_ID = "1104728826";
	/**
	 * 应用从微信开发者平台网站申请到的合法APP_ID*
	 */
	public static final String WECHAT_APPID = "wxf80ee60293a88ede";
	/**
	 * APP SECRET
	 */
	public static final String WECHAT_APPSECRET = "ef7685be766fc7bcd77aa4429f673c8c";
	/**
	 * 新浪微博申请的APP_KEY*
	 */
	public static final String SINA_APP_KEY = "325287325";
	/**
	 * 新浪微博申请的REDIRECT_URL，暂时用的是demo*
	 */
	public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数，暂时用的是demo的*
	 */
	public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	/**
	 * 分享商品详情wap端方法*
	 */
	public static final String PRODUCT_DETAILS_WAP_SHARE = "item/get/";

	public static final String HelpUrl = "http://www.baidu.com";

	public static enum JPushUrl {
		TEST(1, "http://192.168.100.182:8089/web-pushsys/collect/"), RELEASE(2,
				"http://www.91xsj.com/app/pushsys/");

		private int jPushId = 0;
		private String jPush_url = "";

		private JPushUrl(int jPushId, String jPush_url) {
			this.jPushId = jPushId;
			this.jPush_url = jPush_url;
		}

		public int getjPushId() {
			return jPushId;
		}

		public String getjPush_url() {
			return jPush_url;
		}
	}

	/**
	 * 获取推送 Url
	 */
	public static final String getJPushUrl() {
		return jPushUrl.getjPush_url();
	}

	/** 推送 **/
	public class JpushMothod {
		// 注册设备
		public static final String REGISTER_DEVICE = "registDevice";
	}

	public class JpushParams {
		/** 注册Jpush时获取的唯一注册号 **/
		public static final String JPUSH_PARAMS_JPUSHID = "jpushId";
		/** 设备类型 **/
		public static final String JPUSH_PARAMS_DEVICETYPE = "deviceType";
		/** 机器设备号IMEI或UDID **/
		public static final String JPUSH_PARAMS_DEVICEID = "deviceId";
		/** 设备别名 **/
		public static final String JPUSH_PARAMS_DEVICEALIAS = "deviceAlias";
		/** 地理位置 **/
		public static final String JPUSH_PARAMS_LOCATION = "location";

	}
}
