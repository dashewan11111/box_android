package com.adult.android.presenter.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.adult.android.R;
import com.adult.android.entity.Comment2;
import com.adult.android.entity.EvaluationResponse;
import com.adult.android.model.UserModel;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.fragment.main.tab.adapter.CommentListAdapter;
import com.adult.android.utils.GeneralTool;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CommentListActivity extends BaseActivity {

	private List<Comment2> commentList;

	private PullToRefreshListView listView;

	private CommentListAdapter adapter;

	private LoadingDialog loadingDialog;

	private String userId, productId;

	private int pageCount = 1;

	private LinearLayout llytNoData;

	private Button btnRetry;

	private String point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setShowTitleBar(true);
		setActivityTitle("评论");
		setShowRightButton(false);
		userId = getIntent().getStringExtra("userId");
		if (null == userId || GeneralTool.isEmpty(userId)) {
			setActivityTitle("评论");
		} else {
			setActivityTitle("我的评论");
		}
		productId = getIntent().getStringExtra("productId");
		if (null == productId || GeneralTool.isEmpty(productId)) {
			productId = "";
		}
		point = getIntent().getStringExtra("point");
		commentList = (List<Comment2>) getIntent().getSerializableExtra("commentList");
		initView();
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		loadingDialog.show();
		listView.onRefreshComplete();
		UserModel.getInstance().getCommentListByUserId(userId, productId, "" + pageCount,
				new UserModel.OnGetCommentListCompletedListener() {

					@Override
					public void onSuccess(EvaluationResponse info) {
						loadingDialog.dismiss();
						if (0 == flag) {
							commentList = new ArrayList<Comment2>();
						}
						if (null == info.getData().getCommentList() || 0 == info.getData().getCommentList().size()) {
							llytNoData.setVisibility(View.VISIBLE);
							return;
						}
						llytNoData.setVisibility(View.GONE);
						commentList.addAll(info.getData().getCommentList());
						if (null == adapter) {
							adapter = new CommentListAdapter(CommentListActivity.this, commentList);
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
						loadingDialog.dismiss();
						llytNoData.setVisibility(View.VISIBLE);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onFailed(ResponseException e) {
						loadingDialog.dismiss();
						// Misc.finishDelay(CommentListActivity.this);
						llytNoData.setVisibility(View.VISIBLE);
					}
				});
	}

	/** 初始化 */
	private void initView() {
		loadingDialog = new LoadingDialog(this);
		llytNoData = (LinearLayout) findViewById(R.id.comment_list_no_data);
		btnRetry = (Button) findViewById(R.id.comment_list_btn_retry);
		listView = (PullToRefreshListView) findViewById(R.id.product_comment_list_content);
		listView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				getDateList(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				getDateList(1);
			}
		});
		listView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

			}
		});
		btnRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getDateList(0);
			}
		});
		if (null != commentList && !GeneralTool.isEmpty(point)) {
			getDateList(0);
		} else {
			listView.setMode(Mode.DISABLED);
			if (null == adapter) {
				adapter = new CommentListAdapter(CommentListActivity.this, commentList);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		}

	}
}
