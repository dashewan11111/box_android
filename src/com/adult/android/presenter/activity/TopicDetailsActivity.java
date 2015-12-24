package com.adult.android.presenter.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.ReplyDto;
import com.adult.android.entity.Rewarder;
import com.adult.android.entity.TopicDTO;
import com.adult.android.entity.TopicDTO.ImageUrl;
import com.adult.android.entity.TopicDetailResponse;
import com.adult.android.model.CommunityModel;
import com.adult.android.model.CommunityModel.OnReplyTopicCompletedListener;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.presenter.fragment.main.tab.adapter.ReplyAdapter;
import com.adult.android.utils.Misc;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.CircleImageView;
import com.adult.android.view.LoadingDialog;
import com.lidroid.xutils.BitmapUtils;

public class TopicDetailsActivity extends BaseActivity implements OnClickListener {

	private LinearLayout lltyContainer, llytRewarder;

	private ListView listView;

	private TopicDTO topicDto;// 帖子信息

	private List<ReplyDto> replyList;// 帖子回复

	private List<Rewarder> rewarderList;

	private ReplyAdapter adapter;

	private BitmapUtils bitmapUtils;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("帖子详情");
		setShowRightButton(false);
		setLeftButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		loadingDialog = new LoadingDialog(this);
		loadingDialog.show();

		bitmapUtils = new BitmapUtils(this);
		listView = (ListView) findViewById(R.id.community_detail_reply_list);
		lltyContainer = (LinearLayout) findViewById(R.id.community_detail_topic_container);
		llytRewarder = (LinearLayout) findViewById(R.id.community_detail_rewarder_container);
		findViewById(R.id.community_detail_topic_btn_tip).setOnClickListener(this);
		findViewById(R.id.community_detail_reply_btn_send).setOnClickListener(this);
		getDate();
	}

	/** 获取数据 */
	private void getDate() {
		CommunityModel.getInstance().getTopicDetail(getIntent().getStringExtra("topicId"),
				new CommunityModel.OnGetTopicDetailCompletedListener() {

					@Override
					public void onSuccess(TopicDetailResponse info) {
						loadingDialog.dismiss();
						topicDto = info.getData().getTopic();
						replyList = info.getData().getReplyList();
						rewarderList = info.getData().getRewarderList();
						addContent();// 填充帖子内容
						addReply();// 添加回复
						addRewarderList();// 添加打赏者
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onHttpException(HttpResponseException e) {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailed(ResponseException e) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(TopicDetailsActivity.this, e.getResultMsg());
						Misc.finishDelay(TopicDetailsActivity.this);
					}
				});
	}

	private TextView txtRewarderCount;

	/** 添加打赏者列表 */
	protected void addRewarderList() {
		txtRewarderCount = (TextView) findViewById(R.id.community_detail_topic_tip_count);
		if (null == rewarderList || 0 == rewarderList.size()) {
			txtRewarderCount.setText("无人打赏");
		} else {
			txtRewarderCount.setText(rewarderList.size() + "人打赏");
			for (Rewarder user : rewarderList) {
				addRewarder(user);
			}
		}
	}

	private void addRewarder(Rewarder user) {
		View view = getLayoutInflater().inflate(R.layout.coumunity_detail_image_circle, null);
		ImageView imageAvator = (ImageView) findViewById(R.id.community_reward_image);
		// bitmapUtils.display(imageAvator,
		// ServiceUrlConstants.getImageHost() + user.getUserImgUrl());
		llytRewarder.addView(view);
	}

	/** 添加图文混排的内容 */
	private void addContent() {
		CircleImageView imgUser = (CircleImageView) findViewById(R.id.community_detail_creator_avator);
		TextView txtUserName = (TextView) findViewById(R.id.community_detail_creator_name);
		TextView txtUserLevel = (TextView) findViewById(R.id.community_detail_creator_level);
		TextView txtRecommend = (TextView) findViewById(R.id.community_detail_recommend);
		TextView txtTopicTitle = (TextView) findViewById(R.id.community_detail_topic_title);
		TextView txtCreatTime = (TextView) findViewById(R.id.community_detail_creat_time);

		txtUserName.setText(topicDto.getCreaterName());
		txtUserLevel.setText(topicDto.getCareaterLevel());
		txtRecommend.setVisibility(topicDto.isRecommend() ? View.VISIBLE : View.GONE);
		txtTopicTitle.setText(topicDto.getTitle());
		txtCreatTime.setText("创建于" + new SimpleDateFormat("yyyy.MM.dd").format(new Date(topicDto.getCareaterTime())));
		// bitmapUtils.display(imgUser,
		// "http://pica.nipic.com/2008-03-19/2008319183523380_2.jpg");
		// 添加图文混排的内容
		for (ImageUrl imgUrl : topicDto.getImgList()) {
			// TextView txtContent = (TextView) getLayoutInflater().inflate(
			// R.layout.coumunity_detail_text, null);
			// txtContent.setText(topicDto.getContent());
			// lltyContainer.addView(txtContent);
			ImageView imageContent = (ImageView) getLayoutInflater().inflate(R.layout.coumunity_detail_image, null);
			bitmapUtils.display(imageContent, ServiceUrlConstants.getImageHost() + imgUrl.getImgUrl());
			lltyContainer.addView(imageContent);
		}
	}

	/** 添加回复 */
	private void addReply() {
		TextView txtReplyCount = (TextView) findViewById(R.id.community_detail_reply_count);
		txtReplyCount.setText("回帖(" + replyList.size() + ")");
		if (null == adapter) {
			adapter = new ReplyAdapter(this, replyList);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		findViewById(R.id.community_detail_mScrollview).scrollTo(0, 0);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.community_detail_topic_btn_tip:
			// 打赏
			rewordTppic();
			break;
		case R.id.community_detail_reply_btn_send:
			loadingDialog.show();
			final EditText editText = (EditText) findViewById(R.id.community_detail_reply_input);
			String content = editText.getText().toString();
			if (null == content || "".equals(content)) {
				ToastUtil.showToastShort(this, "内容不能为空");
				return;
			}
			CommunityModel.getInstance().replyTopic(AgentApplication.get().getUserId(),
					getIntent().getStringExtra("topicId"), content, new CommunityModel.OnReplyTopicCompletedListener() {

						@Override
						public void onSuccess() {
							loadingDialog.dismiss();
							ToastUtil.showToastLong(TopicDetailsActivity.this, "回帖成功");
							editText.setText("");
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); // 强制隐藏键盘
						}

						@Override
						public void onStart() {

						}

						@Override
						public void onHttpException(HttpResponseException e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub

						}

						@Override
						public void onFailed(ResponseException e) {
							// TODO Auto-generated method stub

						}
					});
			break;
		default:
			break;
		}
	}

	/**
	 * 打赏
	 */
	private void rewordTppic() {
		CommunityModel.getInstance().rewordTopic(AgentApplication.get().getUserId(),
				getIntent().getStringExtra("topicId"), "1", new OnReplyTopicCompletedListener() {

					@Override
					public void onSuccess() {
						ToastUtil.showToastShort(TopicDetailsActivity.this, "打赏成功");
						for (Rewarder reward : rewarderList) {
							if (null != reward && reward.getUserId().equals(AgentApplication.get().getUserId())) {
								// 自己打过赏了
								return;
							}
						}
						Rewarder rewarder = new Rewarder();
						rewarder.setUserImgUrl("");
						rewarder.setUserId(AgentApplication.get().getUserId());
						rewarderList.add(rewarder);
						txtRewarderCount.setText(rewarderList.size() + "人打赏");
						addRewarder(rewarder);
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailed(ResponseException e) {
						// TODO Auto-generated method stub

					}
				});
	}

}
