package com.adult.android.presenter.fragment.main.tab;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.adult.android.R;
import com.adult.android.entity.CommunityDTO;
import com.adult.android.entity.CommunityResponse;
import com.adult.android.model.CommunityModel;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.activity.MainActivity;
import com.adult.android.presenter.activity.TopicListActivity;
import com.adult.android.presenter.fragment.main.BaseTabFragment;
import com.adult.android.presenter.fragment.main.tab.adapter.CommunityListAdapter;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

@SuppressLint("NewApi")
public class CommunityFragment extends BaseTabFragment {
	private View mainView;

	private PullToRefreshListView listView;

	private CommunityListAdapter adapter;

	private List<CommunityDTO> communityList = new ArrayList<CommunityDTO>();

	private LoadingDialog LoadingDialog;

	private int currentPage = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.community_list_fragment, null);
		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}

	private void initViews() {
		initActivityTitle();
		LoadingDialog = new LoadingDialog(getActivity());
		listView = (PullToRefreshListView) mainView
				.findViewById(R.id.community_listview);
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
						intent.putExtra("community",
								communityList.get((int) id));
						intent.putExtra(TopicListActivity.EXTRA_COMMUNITY_ID,
								communityList.get((int) id).getCommunityId());
						intent.setClass(getActivity(), TopicListActivity.class);
						startActivity(intent);
					}
				});
		getDateList(0);
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		LoadingDialog.show();
		CommunityModel.getInstance().getCommunityList(currentPage + "",
				new CommunityModel.OnGetCommunityListCompletedListener() {

					@Override
					public void onSuccess(CommunityResponse info) {
						LoadingDialog.dismiss();
						listView.onRefreshComplete();
						if (0 == flag) {
							communityList = new ArrayList<CommunityDTO>();
						}
						if (null == info.getData().getCommunityList()) {
							return;
						}
						currentPage++;
						communityList.addAll(info.getData().getCommunityList());
						if (null == adapter) {
							adapter = new CommunityListAdapter(getActivity(),
									communityList);
							listView.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}

					}

					@Override
					public void onStart() {

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						LoadingDialog.dismiss();
						listView.onRefreshComplete();
					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailed(ResponseException e) {
						LoadingDialog.dismiss();
						listView.onRefreshComplete();
						ToastUtil.showToastShort(getActivity(),
								e.getResultMsg());
					}
				});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			initActivityTitle();
		}
	}

	private void initActivityTitle() {
		((MainActivity) getActivity()).setActivityTitle("社区");
		((MainActivity) getActivity()).setShowMiddleView();
		((MainActivity) getActivity()).setShowLeftButton(false);
		((MainActivity) getActivity()).setShowRightButton(false);
	}
}