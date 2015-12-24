package com.adult.android.presenter.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.CartProductListResponse;
import com.adult.android.entity.CartProductListResponse2;
import com.adult.android.entity.ProductForCart;
import com.adult.android.model.CartModel;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.presenter.fragment.main.tab.adapter.CartListAdapter;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MyFavouriteActivity extends BaseActivity implements
		OnClickListener {

	private PullToRefreshListView listView;

	private CartListAdapter adapter;

	private CartProductListResponse2 cartDto;

	private List<ProductForCart> productList = new ArrayList<ProductForCart>();

	private LoadingDialog loadingDialog;

	private ImageView imgCheckAll;

	private TextView txtAllAmount;

	private Button btnSum;

	private int currentPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("我的收藏");
		loadingDialog = new LoadingDialog(this);
		imgCheckAll = (ImageView) findViewById(R.id.cart_fragment_all_check);
		txtAllAmount = (TextView) findViewById(R.id.cart_fragment_txt_all_amount);
		btnSum = (Button) findViewById(R.id.cart_fragment_btn_sum);
		imgCheckAll.setOnClickListener(this);
		btnSum.setOnClickListener(this);
		listView = (PullToRefreshListView) findViewById(R.id.cart_fragment_listView);
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
		listView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						Intent intent = new Intent();
						intent.setClass(MyFavouriteActivity.this,
								ProductDetailsActivity2.class);
						startActivity(intent);
					}
				});
		getDateList(0);
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		loadingDialog.show();
		CartModel.getInstance().getCartList(AgentApplication.get().getUserId(),
				currentPage + "",
				new CartModel.OnGetCartListCompletedListener() {

					@Override
					public void onSuccess(CartProductListResponse info) {
						loadingDialog.dismiss();
						cartDto = info.getData();
						if (null == cartDto || null == cartDto.getProductList()) {
							listView.onRefreshComplete();
							return;
						}
						if (0 == flag) {
							productList = new ArrayList<ProductForCart>();
							// adapter = new
							// CartListAdapter(MyFavouriteActivity.this,
							// productList, CarsFragment2.this);
							listView.setAdapter(adapter);
						}
						productList.addAll(cartDto.getProductList());
						if (null == adapter) {
							// adapter = new CartListAdapter(
							// MyFavouriteActivity.this, productList,
							// CarsFragment2.this);
							listView.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						listView.onRefreshComplete();
						txtAllAmount.setText(cartDto.getTotalAmount());
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

	/** 结算 */
	private void sumCars() {
		List<ProductForCart> selectedList = new ArrayList<ProductForCart>();
		for (ProductForCart product : productList) {
			if (product.isChecked()) {
				selectedList.add(product);
			}
		}
		Intent intent = new Intent();
		intent.setClass(this, OrderCommitActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("productList", (Serializable) selectedList);
		startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.cart_fragment_all_check:
			checkAll(!isAllCheced);
			break;
		case R.id.cart_fragment_btn_sum:
			sumCars();
			break;
		default:
			break;
		}
	}

	private boolean isAllCheced = true;

	/** 全选 */
	private void checkAll(boolean checked) {
		isAllCheced = checked;
		if (null == productList) {
			return;
		}
		for (int i = 0; i < productList.size(); i++) {
			productList.get(i).setChecked(isAllCheced);
		}
		if (isAllCheced) {
			imgCheckAll.setImageResource(R.drawable.cart_product_select_on);
		} else {
			imgCheckAll.setImageResource(R.drawable.cart_product_select_off);
		}
		adapter.notifyDataSetChanged();
	}

	/** 检测是否是全部选中。是：全选按钮置为true */
	private void checkIsAllChecked() {
		for (ProductForCart product : productList) {
			if (!product.isChecked()) {
				if (isAllCheced) {
					imgCheckAll
							.setImageResource(R.drawable.cart_product_select_off);
				}
				return;
			}
		}
		imgCheckAll.setImageResource(R.drawable.cart_product_select_on);
	}

}
