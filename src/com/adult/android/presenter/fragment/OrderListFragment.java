package com.adult.android.presenter.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.adult.android.R;
import com.adult.android.entity.NewOrderListResponse;
import com.adult.android.entity.OrderDtoForList;
import com.adult.android.model.OrderModel2;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.presenter.activity.OrderDetailsActivity2;
import com.adult.android.presenter.fragment.main.tab.adapter.OrderListAdapter;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class OrderListFragment extends Fragment {

	private View mainView;

	private PullToRefreshListView listView;

	private OrderListAdapter adapter;

	private List<OrderDtoForList> orderList = new ArrayList<OrderDtoForList>();

	private LoadingDialog LoadingDialog;

	private LinearLayout llytNoOrder;

	private int currentPage = 1;

	private OrderChangedReceiver receiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.order_list_fragment, null);
		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		receiver = new OrderChangedReceiver();
	}

	@Override
	public void onStart() {
		super.onStart();
		getActivity().registerReceiver(receiver,
				new IntentFilter(OrderDetailsActivity2.ACTION_ORDER_CHANGED));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}

	private void initViews() {
		LoadingDialog = new LoadingDialog(getActivity());
		LoadingDialog.show();
		llytNoOrder = (LinearLayout) mainView.findViewById(R.id.order_null);
		listView = (PullToRefreshListView) mainView
				.findViewById(R.id.order_listview);
		listView.setMode(Mode.PULL_UP_TO_REFRESH);

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
		getDateList(0);
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		OrderModel2.getInstance().getOrderList(
				AgentApplication.get().getUserId(), "" + currentPage,
				getArguments().getString("type"),
				new OrderModel2.OnGetOrderListCompletedListener() {

					@Override
					public void onSuccess(NewOrderListResponse info) {
						LoadingDialog.dismiss();
						listView.onRefreshComplete();
						if ((null == info.getData().getOrderList() || 0 == info
								.getData().getOrderList().size())
								&& (null == orderList || 0 == orderList.size())) {
							llytNoOrder.setVisibility(View.VISIBLE);
						} else {
							currentPage++;
							if (0 == flag) {
								orderList = new ArrayList<OrderDtoForList>();
							}
							orderList.addAll(info.getData().getOrderList());
							if (null == adapter) {
								adapter = new OrderListAdapter(getActivity(),
										orderList);
								listView.setAdapter(adapter);
							} else {
								adapter.notifyDataSetChanged();
							}
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						LoadingDialog.dismiss();
						listView.onRefreshComplete();
						ToastUtil.showToastShort(getActivity(),
								e.getResultMsg());
					}

					@Override
					public void onFinish() {
						LoadingDialog.dismiss();
						listView.onRefreshComplete();
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

	class OrderChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			//ToastUtil.showToastShort(getActivity(), "刷新");
			//getDateList(0);
		}
	}
}
