package com.adult.android.presenter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.Product2ForList;
import com.adult.android.entity.ProductFilter;
import com.adult.android.entity.ProductResponse;
import com.adult.android.model.CategoryModel;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.fragment.main.tab.adapter.FilterLisAdapter;
import com.adult.android.presenter.fragment.main.tab.adapter.FilterLisValueAdapter;
import com.adult.android.presenter.fragment.main.tab.adapter.ProductListAdapter;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.Misc;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ProductListActivity extends BaseActivity implements
		OnClickListener {

	public static boolean isGrid = false;

	public static String CATEGORY_ID = "categoryId";

	public static String KEWORD = "keword";

	private PullToRefreshListView listView;

	private PullToRefreshGridView gridView;

	private ListView filterNameListView, filterValueListView;

	private LinearLayout llytFilter, llytSortByPrice, llytSortBy,
			llytNoProduct;

	private TextView txtSortByAmount, txtSortByRenqi, txtFilter;

	private ProductListAdapter adapter;

	private List<Product2ForList> productList = new ArrayList<Product2ForList>();

	private List<ProductFilter> filterList = new ArrayList<ProductFilter>();

	private LoadingDialog LoadingDialog;

	private int currentPage = 1;

	boolean isPriceDown = false;

	private String categoryId = "", keyword = "";

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
			categoryId = getIntent().getStringExtra(CATEGORY_ID);
			keyword = getIntent().getStringExtra(KEWORD);
		}
		LoadingDialog = new LoadingDialog(this);
		LoadingDialog.show();
		listView = (PullToRefreshListView) findViewById(R.id.product_listview);
		listView.setMode(Mode.PULL_UP_TO_REFRESH);
		gridView = (PullToRefreshGridView) findViewById(R.id.product_gridview);
		gridView.setMode(Mode.PULL_UP_TO_REFRESH);
		listView.setVisibility(View.GONE);
		gridView.setVisibility(View.VISIBLE);
		llytFilter = (LinearLayout) findViewById(R.id.llyt_product_filter);
		txtSortByAmount = (TextView) findViewById(R.id.product_sort_by_salesamount);
		txtSortByRenqi = (TextView) findViewById(R.id.product_sort_by_renqi);
		llytSortBy = (LinearLayout) findViewById(R.id.llyt_producyListSort_filter);
		llytSortByPrice = (LinearLayout) findViewById(R.id.product_sort_by_price);
		llytNoProduct = (LinearLayout) findViewById(R.id.no_product);
		txtFilter = (TextView) findViewById(R.id.product_txt_filter);
		txtSortByAmount.setOnClickListener(this);
		txtSortByRenqi.setOnClickListener(this);
		llytSortByPrice.setOnClickListener(this);
		findViewById(R.id.btn_get_product_list).setOnClickListener(this);
		txtFilter.setOnClickListener(this);
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
						showFilter(false);
						Intent intent = new Intent();
						intent.putExtra(
								ProductDetailsActivity2.EXTRA_PRODUCT_ID,
								productList.get((int) id).getPid());
						intent.setClass(ProductListActivity.this,
								ProductDetailsActivity2.class);
						startActivity(intent);
					}
				});
		gridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				getDateList(0);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				getDateList(1);
			}
		});
		gridView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long id) {
						Intent intent = new Intent();
						intent.putExtra(
								ProductDetailsActivity2.EXTRA_PRODUCT_ID,
								productList.get((int) id).getPid());
						intent.setClass(ProductListActivity.this,
								ProductDetailsActivity2.class);
						startActivity(intent);
					}
				});
		// 获取数据
		getDateList(0);
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
			gridView.getRefreshableView().setSelection(0);
		}
	}

	/** 获取数据 */
	protected void getDateList(final int flag) {
		isPriceDown = false;
		if (GeneralTool.isEmpty(categoryId)) {
			getProductByKeyword(flag, keyword);
		} else {
			llytSortBy.setVisibility(View.VISIBLE);// 显示排序和筛选
			getProductByCategoryId(flag, categoryId, "3", "DESC");
		}
	}

	private int currentFilterPosition = 0, currentFilterValuePosition = 0;

	private HashMap<String, String> filterIds = new HashMap<String, String>();

	/** 初始化筛选 */
	protected void initFilter(List<ProductFilter> list) {
		filterList = list;
		filterNameListView = (ListView) llytFilter
				.findViewById(R.id.filter_name_listview);
		filterValueListView = (ListView) llytFilter
				.findViewById(R.id.filter_value_listview);
		filterNameListView.setAdapter(new FilterLisAdapter(this, filterList));
		filterValueListView.setAdapter(new FilterLisValueAdapter(this,
				filterList.get(0).getFilterValueList()));
		filterNameListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1,
					int position, long id) {
				currentFilterPosition = (int) id;
				if (null == filterList.get((int) id).getFilterValueList()) {
					ToastUtil.showToastShort(ProductListActivity.this, "没有数据");
					return;
				}
				filterValueListView.setAdapter(new FilterLisValueAdapter(
						ProductListActivity.this, filterList.get((int) id)
								.getFilterValueList()));
			}
		});
		filterValueListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1,
					int position, long id) {
				currentFilterValuePosition = (int) id;
				// 保存这两个变量值
				String filterName = filterList.get(currentFilterPosition)
						.getFilterId();
				String filerValue = filterList.get(currentFilterPosition)
						.getFilterValueList().get(currentFilterValuePosition)
						.getFilterValueId();
				setFilterIds(filterName, filerValue);
				// 获取数据
				getDateList(0);
				// 筛选框消失
				showFilter(false);
			}
		});
	}

	/** 保存筛选id */
	protected void setFilterIds(String filterId, String filterValueId) {
		if (filterIds.containsKey(filterId)) {
			filterIds.remove(filterId);
		}
		filterIds.put(filterId, filterValueId);
	}

	/** 筛选id */
	private String getFilterIds() {
		if (null == filterIds || 0 >= filterIds.size()) {
			return "";
		}
		// ToastUtil.showToastShort(this, Misc.transMapToString(filterIds));
		return Misc.transMapToString(filterIds);
	}

	private int salseAmount = 0, popular = 0, price = 0;

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.product_sort_by_salesamount:
			salseAmount++;
			if (0 == salseAmount % 2) {
				getProductByCategoryId(0, categoryId, "3", "ASC");
			} else {
				getProductByCategoryId(0, categoryId, "3", "DESC");
			}

			break;
		case R.id.product_sort_by_renqi:
			if (0 == salseAmount % 2) {
				getProductByCategoryId(0, categoryId, "1", "ASC");
			} else {
				getProductByCategoryId(0, categoryId, "1", "DESC");
			}

			break;
		case R.id.product_sort_by_price:
			if (0 == salseAmount % 2) {
				getProductByCategoryId(0, categoryId, "2", "ASC");
			} else {
				getProductByCategoryId(0, categoryId, "2", "DESC");
			}
			break;
		case R.id.product_txt_filter:
			if (View.VISIBLE == llytFilter.getVisibility()) {
				showFilter(false);
			} else {
				showFilter(true);
			}
			break;
		case R.id.btn_get_product_list:
			getDateList(0);
			break;
		default:
			break;
		}
	}

	private void showFilter(boolean isShow) {
		if (isShow) {
			TranslateAnimation mShowAction = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			mShowAction.setDuration(300);
			llytFilter.startAnimation(mShowAction);
			llytFilter.setVisibility(View.VISIBLE);
		} else {
			TranslateAnimation mHiddenAction = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f);
			mHiddenAction.setDuration(300);
			llytFilter.startAnimation(mHiddenAction);
			llytFilter.setVisibility(View.GONE);
		}
	}

	private void getProductByCategoryId(final int flag, String categoryId,
			String sortBy, String sortByValue) {
		CategoryModel.getInstance().getProductListByCategoryId(categoryId, "",
				currentPage + "", getFilterIds(), sortBy, sortByValue,
				new CategoryModel.OnGetProductListCompletedListener() {

					@Override
					public void onSuccess(ProductResponse info) {
						LoadingDialog.dismiss();
						if ((null == info.getData()
								|| null == info.getData().getProductList() || 0 == info
								.getData().getProductList().size())
								&& (null == productList || 0 == productList
										.size())) {
							llytNoProduct.setVisibility(View.VISIBLE);
							return;
						}
						llytNoProduct.setVisibility(View.GONE);
						currentPage++;
						// 排序和刷选条
						initFilter(info.getData().getFilterList());
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
									ProductListActivity.this, productList);
							gridView.setAdapter(adapter);
							listView.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						gridView.onRefreshComplete();
						listView.onRefreshComplete();
					}

					@Override
					public void onStart() {
						LoadingDialog.show();
						// llytNoProduct.setVisibility(View.GONE);
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
		gridView.onRefreshComplete();
		listView.onRefreshComplete();
		llytNoProduct.setVisibility(View.VISIBLE);
	}

	private void getProductByKeyword(final int flag, String keyword) {
		CategoryModel.getInstance().getProductListByKeyword(keyword,
				currentPage + "",
				new CategoryModel.OnGetProductListCompletedListener() {

					@Override
					public void onSuccess(ProductResponse info) {
						LoadingDialog.dismiss();
						llytNoProduct.setVisibility(View.GONE);
						// 排序和刷选条
						if (null == info.getData()
								|| null == info.getData().getProductList()
								|| 0 == info.getData().getProductList().size()) {
							llytNoProduct.setVisibility(View.VISIBLE);
							return;
						}
						// initFilter(info.getData().getFilterList());
						// TODO currentPage是否++？
						if (0 == flag) {
							productList = new ArrayList<Product2ForList>();
							adapter = new ProductListAdapter(
									ProductListActivity.this, productList);
							gridView.setAdapter(adapter);
							listView.setAdapter(adapter);
						}
						if (null == adapter) {
							adapter = new ProductListAdapter(
									ProductListActivity.this, productList);
							gridView.setAdapter(adapter);
							listView.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						gridView.onRefreshComplete();
						listView.onRefreshComplete();
					}

					@Override
					public void onStart() {
						LoadingDialog.show();
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

	@Override
	public void onBackPressed() {
		if (View.VISIBLE == llytFilter.getVisibility()) {
			showFilter(false);
			return;
		}
		finish();
	}
}
