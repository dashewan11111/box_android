package com.adult.android.presenter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;

import com.adult.android.entity.UserDto;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.presenter.activity.BaseActivity;
import com.adult.android.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AgentApplication extends Application {

	private Typeface typeZhface;
	private Typeface typeXhface;

	/**
	 * TAG
	 */
	public static final String TAG = "AgentApplication";

	private Context context = null;

	private static AgentApplication application = null;

	private static List<Activity> activityList = new LinkedList<Activity>();

	/**
	 * 电信传过来的手机号
	 */
	private String productno;
	/**
	 * 电信传过来的地区码
	 */
	private String location;
	/**
	 * 电信传过来的签名
	 */
	private String sig;

	public static IWXAPI iwxapi;

	private String userId;

	private String sessionId, shopId, shopperId, customerId;

	private UserDto userInfo;

	private int cartCount;

	/**
	 * Constructor
	 */
	public AgentApplication() {
		super();
		application = this;
	}

	/**
	 * Get current application instance
	 */
	public static AgentApplication get() {
		return application;
	}

	@Override()
	public void onCreate() {
		super.onCreate();
		// TODO LiCheng 将下一行代码的注释打开，解决错误.
		// CatchHandler.getInstance().init();
		iwxapi = WXAPIFactory.createWXAPI(AgentApplication.get(),
				ServiceUrlConstants.WECHAT_APPID);
		iwxapi.registerApp(ServiceUrlConstants.WECHAT_APPID);

		// 设置开启日志,发布时请关闭日志
		// JPushInterface.setDebugMode(false);
		// 初始化 JPush
		// JPushInterface.init(this);
		typeZhface = Typeface.createFromAsset(getResources().getAssets(),
				"FZLTZHJW.TTF");
		typeXhface = Typeface.createFromAsset(getResources().getAssets(),
				"FZLTXHJW.TTF");
	}

	public Typeface getTypeZhface() {
		return typeZhface;
	}

	public void setTypeZhface(Typeface typeZhface) {
		this.typeZhface = typeZhface;
	}

	public Typeface getTypeXhface() {
		return typeXhface;
	}

	public void setTypeXhface(Typeface typeXhface) {
		this.typeXhface = typeXhface;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	/**
	 * 添加Activity 到容器中
	 *
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 遍历所有Activity 并finish
	 */
	public static void exit() {
		Iterator<Activity> it = activityList.iterator();
		while (it.hasNext()) {
			Activity activity = it.next();
			if (activity == null)
				continue;
			activity.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void exitWithoutActivity(Class<? extends BaseActivity> activityClass) {
		Iterator<Activity> it = activityList.iterator();
		while (it.hasNext()) {
			Activity activity = it.next();
			if (activity == null
					|| activity.getClass().getName()
							.equals(activityClass.getName()))
				continue;
			activity.finish();
		}
	}

	/**
	 * 移除指定activity
	 */
	public void removeActivity(Activity activity) {
		if (activityList.contains(activity)) {
			activityList.remove(activity);
		}
	}

	/**
	 * 关闭指定的activity
	 *
	 * @param activityClass
	 */
	public void exitThisActivity(Class<? extends BaseActivity> activityClass) {
		Iterator<Activity> it = activityList.iterator();
		while (it.hasNext()) {
			Activity activity = it.next();
			if (activity != null
					&& activity.getClass().getName()
							.equals(activityClass.getName())) {
				activity.finish();
			}
		}
	}

	public ApplicationInfo getApplicationInfo() {
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA);
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}
		return applicationInfo;
	}

	public String getProductno() {
		return productno;
	}

	public void setProductno(String productno) {
		this.productno = productno;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	private WXPayEntryActivity.WXPayCallback wxPayCallback;

	/**
	 * 设置微信支付回调
	 * 
	 * @param wxPayCallback
	 *            WXPayCallback 回调对象
	 */
	public void setWXPayCallBack(WXPayEntryActivity.WXPayCallback wxPayCallback) {
		this.wxPayCallback = wxPayCallback;
	}

	/**
	 * 获取微信支付回调
	 */
	public WXPayEntryActivity.WXPayCallback getWXPayCallback() {
		return this.wxPayCallback;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserDto getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserDto userInfo) {
		this.userInfo = userInfo;
	}

	public int getCartCount() {
		return cartCount;
	}

	public void setCartCount(int cartCount) {
		this.cartCount = cartCount;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopperId() {
		return shopperId;
	}

	public void setShopperId(String shopperId) {
		this.shopperId = shopperId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
