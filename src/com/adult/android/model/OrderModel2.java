package com.adult.android.model;

import java.util.HashMap;
import java.util.Map;

import com.adult.android.entity.EvaluationResponse;
import com.adult.android.entity.NewOrderDetailResponse;
import com.adult.android.entity.NewOrderListResponse;
import com.adult.android.entity.OrderSkuListResponse;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.constants.ServiceUrlConstants.OrderParams2;
import com.adult.android.model.internet.InternetClient;
import com.adult.android.model.internet.bean.InputBean;
import com.adult.android.model.internet.bean.StatusInfo;
import com.adult.android.model.internet.exception.BusinessException;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.model.internet.listener.HttpResponseListener;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.utils.CopUtils;

/**
 * 分类业务模块
 * 
 * @author Administrator
 * 
 */
public class OrderModel2 {

	private static OrderModel2 cartModel = null;

	public static OrderModel2 getInstance() {
		if (null == cartModel) {
			cartModel = new OrderModel2();
		}
		return cartModel;
	}

	/** 获取购物车列表 */
	public void getOrderList(String userId, String pageCount, String status,
			final OnGetOrderListCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, OrderParams2.GET_ORDER_LIST);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		// 业务参数:
		maps.put(OrderParams2.USER_ID, userId);
		maps.put(OrderParams2.PAGE_COUNT, pageCount);
		maps.put(OrderParams2.STATUS, status);
		String url = CopUtils.buildGetUrl(maps, ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, NewOrderListResponse.class, new HttpResponseListener<NewOrderListResponse>() {

			@Override
			public void onStart() {
				listener.onStart();
			}

			@Override
			public void onSuccess(NewOrderListResponse response) {
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
				ResponseException exception = new ResponseException(throwable);
				exception.setResultMsg("请求失败");
				listener.onFailed(exception);
			}

			@Override
			public void onFinish() {

			}
		});
	}

	/** 获取订单详情 */
	public void getOrderDetail(String orderId, final OnGetOrderDetailCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, OrderParams2.GET_ORDER_DETAIL);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		// 业务参数:
		maps.put(OrderParams2.ORDER_ID, orderId);

		String url = CopUtils.buildGetUrl(maps, ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, NewOrderDetailResponse.class, new HttpResponseListener<NewOrderDetailResponse>() {

			@Override
			public void onStart() {
				listener.onStart();
			}

			@Override
			public void onSuccess(NewOrderDetailResponse response) {
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
				ResponseException exception = new ResponseException(throwable);
				exception.setResultMsg("请求失败");
				listener.onFailed(exception);
			}

			@Override
			public void onFinish() {

			}
		});
	}

	/** 删除订单 */
	public void deleteOrder(String orderId, final OnDeleteOrderCompletedListener listener) {
		// 共通参数
		InputBean input = new InputBean();
		input.putQueryParam(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		input.putQueryParam(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		input.putQueryParam(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		input.putQueryParam(ServiceUrlConstants.MOTHOD, OrderParams2.DELETE_ORDER);
		// 业务参数:
		input.putQueryParam(OrderParams2.ORDER_ID, orderId);

		InternetClient.post(ServiceUrlConstants.getApiHost(), input, StatusInfo.class,
				new HttpResponseListener<StatusInfo>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(StatusInfo response) {
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
						ResponseException exception = new ResponseException(throwable);
						exception.setResultMsg("请求失败");
						listener.onFailed(exception);
					}

					@Override
					public void onFinish() {

					}
				});
	}

	/** 获取运费 */
	public void getLogisticsPrice(String province, final OnDeleteOrderCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, OrderParams2.GET_LOGISTICS_PRICE);
		// 业务参数:
		maps.put(OrderParams2.PROVINCE, province);

		String url = CopUtils.buildGetUrl(maps, ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, StatusInfo.class, new HttpResponseListener<StatusInfo>() {

			@Override
			public void onStart() {
				listener.onStart();
			}

			@Override
			public void onSuccess(StatusInfo response) {
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
				ResponseException exception = new ResponseException(throwable);
				exception.setResultMsg("请求失败");
				listener.onFailed(exception);
			}

			@Override
			public void onFinish() {

			}
		});
	}

	/** 获取订单详情 */
	public void getOrderSkuList(String orderId, final OnGetOrderSkuListCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, OrderParams2.GET_EVALUATION_LIST);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		// 业务参数:
		maps.put(OrderParams2.ORDER_ID, orderId);

		String url = CopUtils.buildGetUrl(maps, ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, OrderSkuListResponse.class, new HttpResponseListener<OrderSkuListResponse>() {

			@Override
			public void onStart() {
				listener.onStart();
			}

			@Override
			public void onSuccess(OrderSkuListResponse response) {
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
				ResponseException exception = new ResponseException(throwable);
				exception.setResultMsg("请求失败");
				listener.onFailed(exception);
			}

			@Override
			public void onFinish() {

			}
		});
	}

	/** 列表回调 */
	public static interface OnGetOrderListCompletedListener {

		void onSuccess(NewOrderListResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 详情回调 */
	public static interface OnGetOrderDetailCompletedListener {

		void onSuccess(NewOrderDetailResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 删除回调 */
	public static interface OnDeleteOrderCompletedListener {

		void onSuccess(StatusInfo info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 订单shu列表回调 */
	public static interface OnGetOrderSkuListCompletedListener {

		void onSuccess(OrderSkuListResponse info);

		void onFailed(ResponseException exception);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	public void postEvaluation(String skuId, String orderId, String point, String content,
			final OnPostCommentCompletedListener listener) {
		// 共通参数
		InputBean inputBean = new InputBean();

		inputBean.putQueryParam(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.MOTHOD, OrderParams2.POST_EVALUATION);
		// 业务参数:
		inputBean.putQueryParam(OrderParams2.USER_ID, AgentApplication.get().getUserId());
		inputBean.putQueryParam(OrderParams2.ORDER_ID, orderId);
		inputBean.putQueryParam(OrderParams2.SKU_ID, skuId);
		inputBean.putQueryParam(OrderParams2.EVALUATION_PRICE, point);
		inputBean.putQueryParam(OrderParams2.EVALUATION_CONTENT, content);

		InternetClient.post(ServiceUrlConstants.getApiHost(), inputBean, EvaluationResponse.class,
				new HttpResponseListener<EvaluationResponse>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(EvaluationResponse response) {
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
						ResponseException exception = new ResponseException(throwable);
						exception.setResultMsg("请求失败");
						listener.onFailed(exception);
					}

					@Override
					public void onFinish() {

					}
				});
	}

	/** 评论 */
	public static interface OnPostCommentCompletedListener {

		void onSuccess(StatusInfo info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

}
