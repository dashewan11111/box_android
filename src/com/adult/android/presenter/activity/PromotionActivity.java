package com.adult.android.presenter.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.adult.android.R;
import com.adult.android.entity.Product2ForList;
import com.adult.android.entity.ProductResponse;
import com.adult.android.model.CategoryModel;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.fragment.main.tab.adapter.ProductListAdapter;
import com.adult.android.view.HeaderGridView;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class PromotionActivity extends BaseActivity {

	public static boolean isGrid = false;

	public static String PROMOTION_ID = "promotionId";

	private PullToRefreshListView listView;

	private HeaderGridView gridView;;

	private LinearLayout llytNoProduct, llytHeader;

	private ProductListAdapter adapter;

	private List<Product2ForList> productList = new ArrayList<Product2ForList>();

	private LoadingDialog LoadingDialog;

	private int currentPage = 1;

	private String promotionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	@Override
	protected void onStart() {
		super.onStart();
		isGrid = true;
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle(getString(R.string.product_list_title));
		setRighButtonBackGround(R.drawable.btn_actionbar_grid);
		setRightButtonOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isGrid) {
					isGrid = false;
					setRighButtonBackGround(R.drawable.btn_actionbar_list);
				} else {
					isGrid = true;
					setRighButtonBackGround(R.drawable.btn_actionbar_grid);
				}
				updateUI();
			}
		});
		if (null != getIntent()) {
			promotionId = getIntent().getStringExtra(PROMOTION_ID);
		}
		LoadingDialog = new LoadingDialog(this);
		LoadingDialog.show();
		listView = (PullToRefreshListView) findViewById(R.id.promotion_listview);
		listView.setMode(Mode.PULL_UP_TO_REFRESH);
		gridView = (HeaderGridView) findViewById(R.id.promotion_gridview);

		listView.setVisibility(View.GONE);
		gridView.setVisibility(View.VISIBLE);

		llytNoProduct = (LinearLayout) findViewById(R.id.no_product);

		llytHeader = (LinearLayout) getLayoutInflater().inflate(
				R.layout.header_promotion_list, null);
		listView.getRefreshableView().addHeaderView(llytHeader);
		gridView.addHeaderView(llytHeader);
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
						toProductDetail((int) id);
					}
				});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				toProductDetail((int) id);
			}
		});
		// 获取数据
		getDateList(0);
	}

	/** 跳转商品详情 */
	protected void toProductDetail(int id) {
		Intent intent = new Intent();
		intent.putExtra(ProductDetailsActivity2.EXTRA_PRODUCT_ID, productList
				.get((int) id).getPid());
		intent.setClass(PromotionActivity.this, ProductDetailsActivity2.class);
		startActivity(intent);
	}

	/** 切换UI */
	protected void updateUI() {
		if (!isGrid) {
			listView.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
			listView.getRefreshableView().setSelection(0);
		} else {
			listView.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			// gridView.getRefreshableView().setSelection(0);
		}
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		getProductByPromotionId(flag, promotionId);
	}

	private void getProductByPromotionId(final int flag, String categoryId) {
		CategoryModel.getInstance().getProductListByPromotionId(categoryId,
				new CategoryModel.OnGetProductListCompletedListener() {

					@Override
					public void onSuccess(ProductResponse info) {
						LoadingDialog.dismiss();
						if (null == info.getData()
								|| null == info.getData().getProductList()
								|| 0 == info.getData().getProductList().size()) {
							llytNoProduct.setVisibility(View.VISIBLE);
							return;
						}
						// TODO currentPage是否++？
						if (0 == flag) {
							productList = new ArrayList<Product2ForList>();
							// adapter = new ProductListAdapter(
							// ProductListActivity.this, productList);
							// gridView.setAdapter(adapter);
							// listView.setAdapter(adapter);
						}
						productList.addAll(info.getData().getProductList());
						if (null == adapter) {
							adapter = new ProductListAdapter(
									PromotionActivity.this, productList);
							gridView.setAdapter(adapter);
							listView.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						// gridView.onRefreshComplete();
						listView.onRefreshComplete();
					}

					@Override
					public void onStart() {
						LoadingDialog.show();
						llytNoProduct.setVisibility(View.GONE);
					}

					@Override
					public void onHttpException(HttpResponseException e) {
						showNoProduct();
					}

					@Override
					public void onFinish() {
						showNoProduct();
					}

					@Override
					public void onFailed(ResponseException e) {
						showNoProduct();
					}
				});
	}

	protected void showNoProduct() {
		LoadingDialog.dismiss();
		// gridView.onRefreshComplete();
		listView.onRefreshComplete();
		llytNoProduct.setVisibility(View.VISIBLE);
	}
}
