package com.adult.android.presenter.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.adult.android.R;
import com.adult.android.entity.MyTopicResponse;
import com.adult.android.entity.TopicDTO;
import com.adult.android.model.UserModel;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.presenter.fragment.main.tab.adapter.TopicListAdapter;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MyTopicListActivity extends BaseActivity {

	private PullToRefreshListView listView;

	private TopicListAdapter adapter;

	private List<TopicDTO> topicList = new ArrayList<TopicDTO>();

	private LoadingDialog LoadingDialog;

	private int pageCount = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("我的帖子");
		LoadingDialog = new LoadingDialog(this);
		LoadingDialog.show();
		listView = (PullToRefreshListView) findViewById(R.id.my_topic_listview);
		listView.setMode(Mode.BOTH);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getDateList(0);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getDateList(1);
			}
		});
		listView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long id) {
						Intent intent = new Intent();
						intent.putExtra("topicId", topicList.get((int) id)
								.getTopicId());
						intent.setClass(MyTopicListActivity.this,
								TopicDetailsActivity.class);
						startActivity(intent);
					}
				});
		getDateList(0);
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		UserModel.getInstance().getMyTopicList(
				AgentApplication.get().getUserId(), "" + pageCount,
				new UserModel.OnGetMyTopicListCompletedListener() {

					@Override
					public void onSuccess(MyTopicResponse info) {
						LoadingDialog.dismiss();
						if (0 == flag) {
							topicList = new ArrayList<TopicDTO>();
						}
						topicList.addAll(info.getData());
						if (null == adapter) {
							adapter = new TopicListAdapter(
									MyTopicListActivity.this, topicList);
							listView.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						listView.onRefreshComplete();
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
