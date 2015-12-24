package com.adult.android.model;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import com.adult.android.entity.CommunityResponse;
import com.adult.android.entity.TopicDetailResponse;
import com.adult.android.entity.TopicResponse;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.constants.ServiceUrlConstants.CommunityParams;
import com.adult.android.model.internet.InternetClient;
import com.adult.android.model.internet.bean.InputBean;
import com.adult.android.model.internet.bean.StatusInfo;
import com.adult.android.model.internet.exception.BusinessException;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.model.internet.listener.HttpResponseListener;
import com.adult.android.utils.CopUtils;
import com.adult.android.utils.DataService;

/**
 * 社区业务模块
 * 
 * @author Administrator
 * 
 */
public class CommunityModel {

	private static CommunityModel communityModel = null;

	public static CommunityModel getInstance() {
		if (null == communityModel) {
			communityModel = new CommunityModel();
		}
		return communityModel;
	}

	/** 获取社区模块列表 */
	public void getCommunityList(String pageCount, final OnGetCommunityListCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, CommunityParams.GET_COMMUNITY_LIST);
		// 业务参数:
		maps.put(CommunityParams.PAGE_COUNT, pageCount);
		String url = CopUtils.buildGetUrl(maps, ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, CommunityResponse.class, new HttpResponseListener<CommunityResponse>() {

			@Override
			public void onStart() {
				listener.onStart();
			}

			@Override
			public void onSuccess(CommunityResponse response) {
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

	/** 获取帖子列表(通过社区Id) */
	public void getTopicListByCommunityId(String communityId, String type, String pageCount,
			final OnGetTopicListCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, CommunityParams.GET_TOPIC_LIST_BY_COMMUNITY_ID);
		// 业务参数:
		maps.put(CommunityParams.COMMUNITY_ID, communityId);
		maps.put(CommunityParams.PAGE_COUNT, pageCount);
		maps.put(CommunityParams.TYPE, type);

		String url = CopUtils.buildGetUrl(maps, ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, TopicResponse.class, new HttpResponseListener<TopicResponse>() {

			@Override
			public void onStart() {
				listener.onStart();
			}

			@Override
			public void onSuccess(TopicResponse response) {
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

	/** 获取帖子详情 */
	public void getTopicDetail(String topicId, final OnGetTopicDetailCompletedListener listener) {
		// 共通参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		maps.put(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		maps.put(ServiceUrlConstants.MOTHOD, CommunityParams.GET_TOPIC_DETAIL);
		maps.put(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		// 业务参数:
		maps.put(CommunityParams.TOPIC_ID, topicId);

		String url = CopUtils.buildGetUrl(maps, ServiceUrlConstants.getApiHost());
		InternetClient.get(url, null, TopicDetailResponse.class, new HttpResponseListener<TopicDetailResponse>() {

			@Override
			public void onStart() {
				listener.onStart();
			}

			@Override
			public void onSuccess(TopicDetailResponse response) {
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

	/** 回复帖子 */
	public void replyTopic(String userId, String topicId, String content, final OnReplyTopicCompletedListener listener) {
		InputBean inputBean = new InputBean();
		// 共通参数
		inputBean.putQueryParam(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.MOTHOD, CommunityParams.POST_REPLY);
		// 业务参数
		inputBean.putQueryParam(CommunityParams.USER_ID, userId);
		inputBean.putQueryParam(CommunityParams.TOPIC_ID, topicId);
		inputBean.putQueryParam(CommunityParams.CONTENT, content);

		InternetClient.post(ServiceUrlConstants.getApiHost(), inputBean, StatusInfo.class,
				new HttpResponseListener<StatusInfo>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(StatusInfo response) {
						listener.onSuccess();
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

	/** 获取圈子列表数据回调 */
	public static interface OnGetCommunityListCompletedListener {

		void onSuccess(CommunityResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 获取帖子列表数据回调 */
	public static interface OnGetTopicListCompletedListener {

		void onSuccess(TopicResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 获取帖子详情数据回调 */
	public static interface OnGetTopicDetailCompletedListener {

		void onSuccess(TopicDetailResponse info);

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 获取帖子详情数据回调 */
	public static interface OnReplyTopicCompletedListener {

		void onSuccess();

		void onFailed(ResponseException e);

		void onHttpException(HttpResponseException e);

		void onStart();

		void onFinish();
	}

	/** 获取帖子详情数据回调 */
	public static interface OnSuccessListner {

		void onSuccess(String result);

		void onFailed();

	}

	public void postTopic(String userId, String communityId, String title, String content, List<File> fileList,
			final OnSuccessListner listener) {
		List<Part> params = new LinkedList<Part>();
		params.add(new StringPart(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE));
		params.add(new StringPart(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE));
		params.add(new StringPart(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE));

		params.add(new StringPart(CommunityParams.USER_ID, userId));
		params.add(new StringPart(CommunityParams.COMMUNITY_ID, communityId));
		params.add(new StringPart(CommunityParams.TITLE, title));
		params.add(new StringPart(CommunityParams.CONTENT, content));

		try {
			DataService.sendDataByHttpClientPost("http://123.56.229.178:8080/xscr-api-web/topic/postTopic", fileList,
					params, new OnSuccessListner() {

						@Override
						public void onSuccess(String result) {
							listener.onSuccess(result);
						}

						@Override
						public void onFailed() {

						}

					});
		} catch (Exception e) {
			e.printStackTrace();
		}

		// InputBean inputBean = new InputBean();
		// // 共通参数
		// inputBean.putQueryParam(ServiceUrlConstants.APP_KEY,
		// ServiceUrlConstants.APP_KEY_VALUE);
		// inputBean.putQueryParam(ServiceUrlConstants.VERSION,
		// ServiceUrlConstants.VERSION_VALUE);
		// inputBean.putQueryParam(ServiceUrlConstants.APP_SECRET_NAME,
		// ServiceUrlConstants.APP_SECRET_VALUE);
		// inputBean.putQueryParam(ServiceUrlConstants.MOTHOD,
		// CommunityParams.POST_REPLY);
		// // 业务参数
		// inputBean.putQueryParam(CommunityParams.USER_ID, userId);
		// inputBean.putQueryParam(CommunityParams.COMMUNITY_ID, communityId);
		// inputBean.putQueryParam(CommunityParams.TITLE, communityId);
		// inputBean.putQueryParam(CommunityParams.CONTENT, content);
		// InternetClient.post(ServiceUrlConstants.getApiHost(), inputBean,
		// StatusInfo.class, new HttpResponseListener<StatusInfo>() {
		//
		// @Override
		// public void onStart() {
		// listener.onStart();
		// }
		//
		// @Override
		// public void onSuccess(StatusInfo response) {
		// listener.onSuccess();
		// }
		//
		// @Override
		// public void onHttpException(HttpResponseException e) {
		// listener.onHttpException(e);
		// }
		//
		// @Override
		// public void onBusinessException(BusinessException e) {
		// listener.onFailed(e);
		// }
		//
		// @Override
		// public void onOtherException(Throwable throwable) {
		// ResponseException exception = new ResponseException(
		// throwable);
		// exception.setResultMsg("请求失败");
		// listener.onFailed(exception);
		// }
		//
		// @Override
		// public void onFinish() {
		//
		// }
		// });
	}

	public void rewordTopic(String userId, String topicId, String amount, final OnReplyTopicCompletedListener listener) {
		InputBean inputBean = new InputBean();
		// 共通参数
		inputBean.putQueryParam(ServiceUrlConstants.APP_KEY, ServiceUrlConstants.APP_KEY_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.VERSION, ServiceUrlConstants.VERSION_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.APP_SECRET_NAME, ServiceUrlConstants.APP_SECRET_VALUE);
		inputBean.putQueryParam(ServiceUrlConstants.MOTHOD, CommunityParams.REWORD_TOPIC);
		// 业务参数
		inputBean.putQueryParam(CommunityParams.USER_ID, userId);
		inputBean.putQueryParam(CommunityParams.TOPIC_ID, topicId);
		inputBean.putQueryParam(CommunityParams.AMOUNT, amount);

		InternetClient.post(ServiceUrlConstants.getApiHost(), inputBean, StatusInfo.class,
				new HttpResponseListener<StatusInfo>() {

					@Override
					public void onStart() {
						listener.onStart();
					}

					@Override
					public void onSuccess(StatusInfo response) {
						listener.onSuccess();
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

}
