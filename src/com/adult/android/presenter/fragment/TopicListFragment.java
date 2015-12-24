package com.adult.android.presenter.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.CommunityDTO;
import com.adult.android.entity.TopicDTO;
import com.adult.android.entity.TopicResponse;
import com.adult.android.model.CommunityModel;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.activity.TopicDetailsActivity;
import com.adult.android.presenter.activity.TopicListActivity;
import com.adult.android.presenter.fragment.main.tab.adapter.TopicListAdapter;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;

public class TopicListFragment extends Fragment {

	private View mainView;

	private PullToRefreshListView listView;

	private TopicListAdapter adapter;

	private List<TopicDTO> topicList = new ArrayList<TopicDTO>();

	private LoadingDialog LoadingDialog;

	private int pageCount = 1;

	private CommunityDTO dto;

	private String communityId, type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.topic_list_fragment, null);
		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}

	private void initViews() {
		LoadingDialog = new LoadingDialog(getActivity());
		LoadingDialog.show();
		listView = (PullToRefreshListView) mainView.findViewById(R.id.topic_listview);
		listView.setMode(Mode.BOTH);
		dto = (CommunityDTO) getArguments().getSerializable("community");
		if (null != dto) {
			View headeriew = LayoutInflater.from(getActivity()).inflate(R.layout.item_community_list, null);
			ImageView imageHeader = (ImageView) headeriew.findViewById(R.id.item_community_list_image);
			TextView txtTitle = (TextView) headeriew.findViewById(R.id.item_community_list_title);
			TextView txtContent = (TextView) headeriew.findViewById(R.id.item_community_list_content);
			new BitmapUtils(getActivity()).display(imageHeader, ServiceUrlConstants.getImageHost() + dto.getImgUrl());
			txtTitle.setText(dto.getName());
			txtContent.setText(dto.getDescription());
			listView.getRefreshableView().addHeaderView(headeriew);
		} else {

		}

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
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("topicId", topicList.get((int) id).getTopicId());
				intent.setClass(getActivity(), TopicDetailsActivity.class);
				startActivity(intent);
			}
		});
		getDateList(0);
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		communityId = getArguments().getString(TopicListActivity.EXTRA_COMMUNITY_ID);
		type = getArguments().getString("type");

		CommunityModel.getInstance().getTopicListByCommunityId(communityId, type, pageCount + "",
				new CommunityModel.OnGetTopicListCompletedListener() {

					@Override
					public void onSuccess(TopicResponse info) {
						LoadingDialog.dismiss();
						listView.onRefreshComplete();
						if (0 == flag) {
							topicList = new ArrayList<TopicDTO>();
						}
						if (null == info.getData().getTopicList()) {
							return;
						}
						pageCount++;
						topicList.addAll(info.getData().getTopicList());
						if (null == adapter) {
							adapter = new TopicListAdapter(getActivity(), topicList);
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
						ToastUtil.showToastShort(getActivity(), e.getResultMsg());
					}
				});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

}
