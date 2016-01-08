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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.adult.android.R;
import com.adult.android.entity.UserOrdersDTO;
import com.adult.android.model.OnUnPayOrderListResponse;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnUnPayOrderListCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.activity.OrderDetailsActivity2;
import com.adult.android.presenter.fragment.main.tab.adapter.OrderListAdapterForBox;
import com.adult.android.presenter.fragment.main.tab.adapter.OrderListAdapterForBox.OnPayOrderListener;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class OrderListFragmenForBox extends Fragment implements
		OnPayOrderListener {

	private View mainView;

	private PullToRefreshListView listView;

	private OrderListAdapterForBox adapter;

	private List<UserOrdersDTO> orderList = new ArrayList<UserOrdersDTO>();

	private LoadingDialog LoadingDialog;

	private LinearLayout llytNoOrder;

	private int currentPage = 1;

	private OrderChangedReceiver receiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.order_list_fragment_forbox, null);
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
		llytNoOrder = (LinearLayout) mainView.findViewById(R.id.order_null_box);
		mainView.findViewById(R.id.btn_refresh_order).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						getDateList(0);
					}
				});
		listView = (PullToRefreshListView) mainView
				.findViewById(R.id.order_listview_box);
		listView.setMode(Mode.PULL_DOWN_TO_REFRESH);

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
		LoadingDialog.show();
		UserModel.getInstance().getUnPayOrderList(
				new OnUnPayOrderListCompletedListener() {

					@Override
					public void onSuccess(OnUnPayOrderListResponse info) {
						LoadingDialog.dismiss();
						listView.onRefreshComplete();
						if ((null == info.getData().getUserOrders() || 0 == info
								.getData().getUserOrders().size())
								&& (null == orderList || 0 == orderList.size())) {
							llytNoOrder.setVisibility(View.VISIBLE);
						} else {
							llytNoOrder.setVisibility(View.GONE);
							currentPage++;
							if (0 == flag) {
								orderList = new ArrayList<UserOrdersDTO>();
							}
							orderList.addAll(info.getData().getUserOrders());
							adapter = new OrderListAdapterForBox(getActivity(),
									OrderListFragmenForBox.this, orderList);
							listView.setAdapter(adapter);
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
						// llytNoOrder.setVisibility(View.VISIBLE);
					}

					@Override
					public void onFailed(ResponseException e) {
						LoadingDialog.dismiss();
						listView.onRefreshComplete();
						llytNoOrder.setVisibility(View.VISIBLE);
						ToastUtil.showToastShort(getActivity(),
								e.getResultMsg());
					}
				});
	}

	class OrderChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			ToastUtil.showToastShort(getActivity(), "刷新");
			getDateList(0);
		}
	}

	@Override
	public void onPayOrder(final int position) {
		LoadingDialog.show();
		UserModel.getInstance().payOrder(orderList.get(position).getOrderId(),
				new OnUnPayOrderListCompletedListener() {

					@Override
					public void onSuccess(OnUnPayOrderListResponse info) {
						LoadingDialog.dismiss();
						UserOrdersDTO order = orderList.get(position);
						order.setOrderStatus(1);
						orderList.set(position, order);
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						LoadingDialog.dismiss();
						ToastUtil.showToastShort(getActivity(),
								e.getResultMsg());
					}

					@Override
					public void onFinish() {
						LoadingDialog.dismiss();
					}

					@Override
					public void onFailed(ResponseException e) {
						LoadingDialog.dismiss();
						ToastUtil.showToastShort(getActivity(),
								e.getResultMsg());
					}
				});
	}
}
