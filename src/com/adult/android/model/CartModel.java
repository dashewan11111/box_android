package com.adult.android.model;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.adult.android.entity.AddToCartResponse;
import com.adult.android.entity.CalculateCartResponse;
import com.adult.android.entity.CartProductListResponse;
import com.adult.android.entity.CommitOrderResponse;
import com.adult.android.entity.GetCartAmountResponse;
import com.adult.android.entity.UpdateAddressResponse;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.constants.ServiceUrlConstants.CartParams2;
import com.adult.android.model.internet.InternetClient;
import com.adult.android.model.internet.bean.InputBean;
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
public class CartModel {

	private static CartModel cartModel = null;

	public static CartModel getInstance() {
		if (null == cartModel) {
			cartModel = new CartModel();
		}
		return cartModel;
	}

	/** 获取购物车列表 */
	public void getCartList(String userId, String pageCount,
			final OnGetCartListCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, CartParams2.GET_CART_LIST);
		// 业务参数:
		maps.put(CartParams2.USER_ID, userId);
		maps.put(CartParams2.PAGE_COUNT, pageCount);
		String url = CopUtils.buildGetUrl(maps,
				ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, CartProductListResponse.class,
				new HttpResponseListener<CartProductListResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(CartProductListResponse response) {
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

	/** 添加购物车 */
	public void addToCart(String userId, String skuId, String count,
			final OnAddToCartCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		InputBean inputBean = new InputBean();
		inputBean.putQueryParam(ServiceUrlConstants.APP_KEY,
				ServiceUrlConstants.APP_KEY_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.VERSION,
				ServiceUrlConstants.VERSION_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.MOTHOD,
				CartParams2.ADD_TO_CART);
		// 业务参数:
		inputBean.putQueryParam(CartParams2.USER_ID, userId);
		inputBean.putQueryParam(CartParams2.SKU_ID, skuId);
		inputBean.putQueryParam(CartParams2.COUNT, count);

		// String url = CopUtils.buildGetUrl(maps,
		// ServiceUrlConstants.getApiHost());
		InternetClient.post(ServiceUrlConstants.getApiHost(), inputBean,
				AddToCartResponse.class,
				new HttpResponseListener<AddToCartResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(AddToCartResponse response) {
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
	public void deleteCartProduct(String userId, String skuId, String count,
			final OnAddToCartCompletedListener listener) {
		// 共通参数
		InputBean input = new InputBean();
		input.putQueryParam(ServiceUrlConstants.APP_KEY,
				ServiceUrlConstants.APP_KEY_VALUE);
		input.putQueryParam(ServiceUrlConstants.VERSION,
				ServiceUrlConstants.VERSION_VALUE);
		input.putQueryParam(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		input.putQueryParam(ServiceUrlConstants.MOTHOD, CartParams2.DELETE_CART);
		// 业务参数:
		input.putQueryParam(CartParams2.USER_ID, userId);
		input.putQueryParam(CartParams2.SKU_ID, skuId);
		input.putQueryParam(CartParams2.COUNT, count);

		InternetClient.post(ServiceUrlConstants.getApiHost(), input,
				AddToCartResponse.class,
				new HttpResponseListener<AddToCartResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(AddToCartResponse response) {
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

	/** 结算 */
	public void calculateCart(String userId, String skuIds, String quickBuy,
			String skuIdQuickBuy, String qtyQuickBuy,
			final OnCalculateCartCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, CartParams2.CALCULATE_CART);
		// 业务参数:
		maps.put(CartParams2.USER_ID, userId);
		maps.put(CartParams2.SKU_ID_LIST, skuIds);
		maps.put(CartParams2.QUICK_BUY, quickBuy);
		maps.put(CartParams2.SKU_ID_QUICK_BUY, skuIdQuickBuy);
		maps.put(CartParams2.QTY_QUICK_BUY, qtyQuickBuy);

		String url = CopUtils.buildGetUrl(maps,
				ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, CalculateCartResponse.class,
				new HttpResponseListener<CalculateCartResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(CalculateCartResponse response) {
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

	/** 计算金额 */
	public void getCartAmount(String userId, String skuIds,
			final OnGetCartAmountCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, CartParams2.GET_CART_AMOUNT);
		// 业务参数:
		maps.put(CartParams2.USER_ID, userId);
		maps.put(CartParams2.SKU_ID_LIST, skuIds);

		String url = CopUtils.buildGetUrl(maps,
				ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, GetCartAmountResponse.class,
				new HttpResponseListener<GetCartAmountResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(GetCartAmountResponse response) {
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

	/** 提交订单 */
	public void commitOrder(String userId, String skuIds, String couponId,
			String message, String payway, String addressId,
			String useCounCodeId, String quickBuy, String skuIdQuickBuy,
			String qtyQuickyBuy, final OnOrderCommitCompletedListener listener) {
		// 共通参数
		InputBean input = new InputBean();
		input.putQueryParam(ServiceUrlConstants.APP_KEY,
				ServiceUrlConstants.APP_KEY_VALUE);
		input.putQueryParam(ServiceUrlConstants.VERSION,
				ServiceUrlConstants.VERSION_VALUE);
		input.putQueryParam(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		input.putQueryParam(ServiceUrlConstants.MOTHOD,
				CartParams2.COMMIT_ORDER);
		// 业务参数:
		input.putQueryParam(CartParams2.USER_ID, userId);
		input.putQueryParam(CartParams2.SKU_ID_LIST, skuIds);
		input.putQueryParam(CartParams2.COUPON_ID, couponId);
		input.putQueryParam(CartParams2.MESSAGE, message);
		input.putQueryParam(CartParams2.PAY_WAY, payway);
		input.putQueryParam(CartParams2.ADDRESS_ID, addressId);
		input.putQueryParam(CartParams2.USE_COUPON_CODE_ID, useCounCodeId);
		input.putQueryParam(CartParams2.QUICK_BUY, quickBuy);
		input.putQueryParam(CartParams2.SKU_ID_QUICK_BUY, skuIdQuickBuy);
		input.putQueryParam(CartParams2.QTY_QUICK_BUY, qtyQuickyBuy);

		InternetClient.post(ServiceUrlConstants.getApiHost(), input,
				CommitOrderResponse.class,
				new HttpResponseListener<CommitOrderResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(CommitOrderResponse response) {
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

	/** 更新收货地址 */
	public void updateAddress(String addressId, String userId,
			String provinceId, String cityId, String areaId,
			String detailAddress, String receiverName, String receiveMb,
			final OnUpdateAddressCompletedListener listener) {
		// 共通参数
		InputBean inputBean = new InputBean();
		inputBean.putQueryParam(ServiceUrlConstants.APP_KEY,
				ServiceUrlConstants.APP_KEY_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.VERSION,
				ServiceUrlConstants.VERSION_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.APP_SECRET_NAME,
				ServiceUrlConstants.APP_SECRET_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.MOTHOD,
				CartParams2.UPDATE_ADDRESS);
		// 业务参数:
		inputBean.putQueryParam(CartParams2.USER_ID, userId);
		inputBean.putQueryParam(CartParams2.ADDRESS_ID, addressId);
		inputBean.putQueryParam(CartParams2.PROVINCE_ID, provinceId);
		inputBean.putQueryParam(CartParams2.CITY_ID, cityId);
		inputBean.putQueryParam(CartParams2.AREA_ID, areaId);
		inputBean.putQueryParam(CartParams2.DETAIL_ADDRESS, detailAddress);
		inputBean.putQueryParam(CartParams2.RECEIVER_NAME, receiverName);
		inputBean.putQueryParam(CartParams2.RECEIVER_MB, receiveMb);

		InternetClient.post(ServiceUrlConstants.getApiHost(), inputBean,
				UpdateAddressResponse.class,
				new HttpResponseListener<UpdateAddressResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(UpdateAddressResponse response) {
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
	public static interface OnGetCartListCompletedListener {

		void onSuccess(CartProductListResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 获取商品列表数据回调 */
	public static interface OnAddToCartCompletedListener {

		void onSuccess(AddToCartResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 结算数据回调 */
	public static interface OnCalculateCartCompletedListener {

		void onSuccess(CalculateCartResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 获取购物车金额回调 */
	public static interface OnGetCartAmountCompletedListener {

		void onSuccess(GetCartAmountResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 获取购物车金额回调 */
	public static interface OnOrderCommitCompletedListener {

		void onSuccess(CommitOrderResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 获取购物车金额回调 */
	public static interface OnUpdateAddressCompletedListener {

		void onSuccess(UpdateAddressResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	public static String formatCartCount(String count) {
		if (!TextUtils.isEmpty(count) && !"0".equals(count)) {
			try {
				int number = Integer.parseInt(count);
				if (number > 99) {
					return "99+";
				} else if (number < 0) {
					return "";
				} else {
					return count;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return "";
			}
		} else {
			return "";
		}
	}

}
