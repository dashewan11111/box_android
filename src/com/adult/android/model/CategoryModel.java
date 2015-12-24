package com.adult.android.model;

import java.util.HashMap;
import java.util.Map;

import com.adult.android.entity.CategoryResponse;
import com.adult.android.entity.ProductDetailResponse;
import com.adult.android.entity.ProductResponse;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.constants.ServiceUrlConstants.CategoryParams;
import com.adult.android.model.internet.InternetClient;
import com.adult.android.model.internet.exception.BusinessException;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.model.internet.listener.HttpResponseListener;
import com.adult.android.utils.CopUtils;

/**
 * 分类业务模块
 * 
 * @author Administrator
 * 
 */
public class CategoryModel {

	private static CategoryModel categoryModel = null;

	public static CategoryModel getInstance() {
		if (null == categoryModel) {
			categoryModel = new CategoryModel();
		}
		return categoryModel;
	}

	/** 获取类目列表 */
	public void getCategoryList(String categoryId, String pageCount,
			final OnGetCategoryListCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, CategoryParams.GET_CATEGORY_LIST);
		// 业务参数:
		maps.put(CategoryParams.CATEGORY_ID, categoryId);
		maps.put(CategoryParams.PAGE_COUNT, pageCount);
		String url = CopUtils.buildGetUrl(maps,
				ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, CategoryResponse.class,
				new HttpResponseListener<CategoryResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(CategoryResponse response) {
						listener.onSuccess(response);
					}

					@Override
					public void onHttpException(HttpResponseException e) {
						listener.onHttpException(e);
					}

					@Override
					public void onBusinessException(BusinessException e) {
						listener.onFailed(e);
					}

					@Override
					public void onOtherException(Throwable throwable) {
						ResponseException exception = new ResponseException(
								throwable);
						exception.setResultMsg("请求失败");
						listener.onFailed(exception);
					}

					@Override
					public void onFinish() {

					}
				});
	}

	/**
	 * 获取商品列表(通过类目Id)
	 * 
	 * @param sortByValue
	 */
	public void getProductListByCategoryId(String categoryId, String keyword,
			String pageCount, String filterValueIdList, String sortBy,
			String sortByValue, final OnGetProductListCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, CategoryParams.GET_PRODUCT_LIST);
		// 业务参数:
		maps.put(CategoryParams.CATEGORY_ID, categoryId);
		maps.put(CategoryParams.PAGE_COUNT, pageCount);
		maps.put(CategoryParams.FILTER_VALUE_ID_LIST, filterValueIdList);
		maps.put(CategoryParams.SORT_BY, sortBy);
		maps.put(CategoryParams.SORT_BY_VALUE, sortByValue);
		maps.put(CategoryParams.KEY_WORD, keyword);

		String url = CopUtils.buildGetUrl(maps,
				ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, ProductResponse.class,
				new HttpResponseListener<ProductResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(ProductResponse response) {
						listener.onSuccess(response);
					}

					@Override
					public void onHttpException(HttpResponseException e) {
						listener.onHttpException(e);
					}

					@Override
					public void onBusinessException(BusinessException e) {
						listener.onFailed(e);
					}

					@Override
					public void onOtherException(Throwable throwable) {
						ResponseException exception = new ResponseException(
								throwable);
						exception.setResultMsg("请求失败");
						listener.onFailed(exception);
					}

					@Override
					public void onFinish() {

					}
				});
	}

	/** 获取专题列表(通过专题Id) */
	public void getProductListByPromotionId(String promotionId,
			final OnGetProductListCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, CategoryParams.GET_PRODUCT_LIST);
		// 业务参数:
		maps.put(CategoryParams.PROMOTION_ID, promotionId);

		String url = CopUtils.buildGetUrl(maps,
				ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, ProductResponse.class,
				new HttpResponseListener<ProductResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(ProductResponse response) {
						listener.onSuccess(response);
					}

					@Override
					public void onHttpException(HttpResponseException e) {
						listener.onHttpException(e);
					}

					@Override
					public void onBusinessException(BusinessException e) {
						listener.onFailed(e);
					}

					@Override
					public void onOtherException(Throwable throwable) {
						ResponseException exception = new ResponseException(
								throwable);
						exception.setResultMsg("请求失败");
						listener.onFailed(exception);
					}

					@Override
					public void onFinish() {

					}
				});
	}

	/** 获取商品列表(通过关键字) */
	public void getProductListByKeyword(String keyword, String pageCount,
			final OnGetProductListCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD,
				CategoryParams.GET_PRODUCT_LIST_BY_KEYWORD);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		// 业务参数:
		maps.put(CategoryParams.PAGE_COUNT, pageCount);
		maps.put(CategoryParams.KEY_WORD, keyword);

		String url = CopUtils.buildGetUrl(maps,
				ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, ProductResponse.class,
				new HttpResponseListener<ProductResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(ProductResponse response) {
						listener.onSuccess(response);
					}

					@Override
					public void onHttpException(HttpResponseException e) {
						listener.onHttpException(e);
					}

					@Override
					public void onBusinessException(BusinessException e) {
						listener.onFailed(e);
					}

					@Override
					public void onOtherException(Throwable throwable) {
						ResponseException exception = new ResponseException(
								throwable);
						exception.setResultMsg("请求失败");
						listener.onFailed(exception);
					}

					@Override
					public void onFinish() {

					}
				});
	}

	/** 获取类目列表 */
	public void getProductDetail(String pid,
			final OnGetProducDetailCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, CategoryParams.GET_PRODUCT_DETAIL);
		// 业务参数:
		maps.put(CategoryParams.ID, pid);

		String url = CopUtils.buildGetUrl(maps,
				ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, ProductDetailResponse.class,
				new HttpResponseListener<ProductDetailResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(ProductDetailResponse response) {
						listener.onSuccess(response);
					}

					@Override
					public void onHttpException(HttpResponseException e) {
						listener.onHttpException(e);
					}

					@Override
					public void onBusinessException(BusinessException e) {
						listener.onFailed(e);
					}

					@Override
					public void onOtherException(Throwable throwable) {
						ResponseException exception = new ResponseException(
								throwable);
						exception.setResultMsg("请求失败");
						listener.onFailed(exception);
					}

					@Override
					public void onFinish() {

					}
				});
	}

	/** 获取购物车数据回调 */
	public static interface OnGetCategoryListCompletedListener {

		void onSuccess(CategoryResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 获取商品列表数据回调 */
	public static interface OnGetProductListCompletedListener {

		void onSuccess(ProductResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 获取商品详情数据回调 */
	public static interface OnGetProducDetailCompletedListener {

		void onSuccess(ProductDetailResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

}
