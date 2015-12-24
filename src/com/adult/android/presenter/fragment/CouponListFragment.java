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
import android.widget.ListView;

import com.adult.android.R;
import com.adult.android.entity.CouponDto;
import com.adult.android.entity.CouponResponse;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnGetMyCouponListCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.presenter.activity.TopicDetailsActivity;
import com.adult.android.presenter.fragment.main.tab.adapter.CouponListAdapter;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CouponListFragment extends Fragment {

	private View mainView;

	private PullToRefreshListView listView;

	private CouponListAdapter adapter;

	private List<CouponDto> couponList = new ArrayList<CouponDto>();

	private LoadingDialog LoadingDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.coupon_list_fragment, null);
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
		listView = (PullToRefreshListView) mainView
				.findViewById(R.id.coupon_listview);
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
							int position, long arg3) {
						Intent intent = new Intent();
						intent.setClass(getActivity(),
								TopicDetailsActivity.class);
						startActivity(intent);
					}
				});
		getDateList(0);
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		UserModel.getInstance().getMyCouponList(
				AgentApplication.get().getUserId(), "1",
				getArguments().getString("status"),
				new OnGetMyCouponListCompletedListener() {

					@Override
					public void onSuccess(CouponResponse info) {
						LoadingDialog.dismiss();
						if (0 == flag) {
							couponList = new ArrayList<CouponDto>();
						}
						couponList.addAll(info.getData());
						if (null == adapter) {
							adapter = new CouponListAdapter(getActivity(),
									couponList);
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
						LoadingDialog.dismiss();

					}

					@Override
					public void onFinish() {
						LoadingDialog.dismiss();

					}

					@Override
					public void onFailed(ResponseException e) {
						LoadingDialog.dismiss();

					}
				});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}
}
