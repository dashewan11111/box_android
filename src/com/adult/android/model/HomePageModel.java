package com.adult.android.model;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.adult.android.entity.HomePageResponse2;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.constants.ServiceUrlConstants.HomePageParams;
import com.adult.android.model.internet.InternetClient;
import com.adult.android.model.internet.exception.BusinessException;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.listener.HttpResponseListener;
import com.adult.android.utils.CopUtils;

/***
 * 购物车业务
 * 
 * @author LiCheng
 * 
 */
public class HomePageModel {

	private static HomePageModel homePageModel = null;

	public static HomePageModel getInsance() {
		if (null == homePageModel) {
			homePageModel = new HomePageModel();
		}
		return homePageModel;
	}

	/** 获取主页数据 */
	public void getHomePageData2(
			final OnGetHomePageDataCompletedListener2 listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, HomePageParams.CMS_INDEX_GET);

		String url = CopUtils.buildGetUrl(maps,
				ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, HomePageResponse2.class,
				new HttpResponseListener<HomePageResponse2>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(HomePageResponse2 result) {
						listener.onCompleted(result);
					}

					@Override
					public void onHttpException(HttpResponseException e) {
						Log.e("获取主页数据", "onHttpException");
						listener.onHttpException(e);
					}

					@Override
					public void onBusinessException(BusinessException e) {
						Log.e("获取主页数据", "onBusinessException", e);
						listener.onFailed(e);
					}

					@Override
					public void onOtherException(Throwable throwable) {
						Log.e("获取主页数据", "onOtherException", throwable);

					}

					@Override
					public void onFinish() {
						Log.e("获取主页数据", "onFinish");
						listener.onFinish();
					}
				});
	}

	public static interface OnGetHomePageDataCompletedListener2 {
		void onStart();

		void onFinish();

		void onCompleted(final HomePageResponse2 info);

		void onFailed(BusinessException e);

		void onHttpException(HttpResponseException e);

	}

}
