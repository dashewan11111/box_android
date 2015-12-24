package com.adult.android.presenter.activity;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.Comment2;
import com.adult.android.entity.ProductDetailResponse;
import com.adult.android.entity.ProductDetailsDto;
import com.adult.android.entity.Promotion;
import com.adult.android.entity.ServiceDto;
import com.adult.android.entity.Sku2;
import com.adult.android.model.CategoryModel;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.fragment.main.tab.adapter.ProductDetailPicAdapter2;
import com.adult.android.utils.ActivityUtils;
import com.adult.android.utils.Misc;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.adult.android.view.MyScrollView;
import com.adult.android.view.ProductSkuListPopupWindow;
import com.adult.android.view.ProductSkuListPopupWindow.SkuListPopupWindowListener;
import com.lidroid.xutils.BitmapUtils;

public class ProductDetailsActivity2 extends WebViewActivity implements OnPageChangeListener, OnClickListener,
		SkuListPopupWindowListener {

	public static final String EXTRA_PRODUCT_ID = "extra_product_id";

	private ProductDetailsDto product;

	private WebView mWebView;

	private ViewPager mViewPager;

	private MyScrollView mScrollView;

	private TextView txtName, txtDesc, txtPrice, txtPriceMarket, txtSalesAmount, txtBuyNow, txtAddCart;

	private LinearLayout llytPagerPoiner, llytServiceList, llytPromotionList, llytCustomer, llytFavourite;

	private LoadingDialog LoadingDialog;

	private BitmapUtils bitmapUtils;

	private ProductSkuListPopupWindow skuPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		LoadingDialog = new LoadingDialog(this);
		bitmapUtils = new BitmapUtils(this);
		mViewPager = (ViewPager) findViewById(R.id.product_details_viewpager);
		txtName = (TextView) findViewById(R.id.product_details_name);
		txtDesc = (TextView) findViewById(R.id.product_details_desc);
		txtPrice = (TextView) findViewById(R.id.product_details_price);
		txtPriceMarket = (TextView) findViewById(R.id.product_details_price_market);
		txtSalesAmount = (TextView) findViewById(R.id.product_details_sales_amount);
		txtAddCart = (TextView) findViewById(R.id.product_details_addcart);
		txtBuyNow = (TextView) findViewById(R.id.product_details_buy_now);

		llytServiceList = (LinearLayout) findViewById(R.id.product_detail_service_container);
		llytPromotionList = (LinearLayout) findViewById(R.id.product_detail_promotion_container);
		llytPagerPoiner = (LinearLayout) findViewById(R.id.product_details_viewpager_poiner);
		llytCustomer = (LinearLayout) findViewById(R.id.product_details_customer_service);
		llytFavourite = (LinearLayout) findViewById(R.id.product_details_favourite);

		mWebView = (WebView) findViewById(R.id.product_detail_webview);
		txtAddCart.setOnClickListener(this);
		txtBuyNow.setOnClickListener(this);
		llytCustomer.setOnClickListener(this);
		llytFavourite.setOnClickListener(this);
		findViewById(R.id.layout_product_comment_more).setOnClickListener(this);
		initWebViewSetting(mWebView);
		initActivityTitle();
		// postMessage();
		getDateList(0);
	}

	private void initActivityTitle() {
		mScrollView = (MyScrollView) findViewById(R.id.product_details2_scrollview);
		final LinearLayout llytHeader = (LinearLayout) findViewById(R.id.layout_trans_header);
		llytHeader.findViewById(R.id.layout_trans_btn_back).setOnClickListener(this);
		llytHeader.findViewById(R.id.layout_trans_btn_cart).setOnClickListener(this);
		final TextView txtTitle = (TextView) llytHeader.findViewById(R.id.layout_trans_title);
		txtTitle.setText("商品详情");
		mScrollView.setOnScrollViewChangeListner(new MyScrollView.OnScrollViewChangeListner() {
			@Override
			public void onScrollChanged(int l, int t, int oldl, int oldt) {

				int alpha = t * 255 / (Misc.getScreenDisplay(ProductDetailsActivity2.this)[1] / 2);
				if (0 > alpha || 255 < alpha) {
					return;
				}
				txtTitle.setTextColor(Color.argb(alpha, 96, 96, 96));
				llytHeader.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
			}
		});
	}

	/** 填充数据 */
	private void initData() {
		// Sku2 defaultSku = product.getSkuList().get(0);
		txtName.setText(product.getName());
		txtDesc.setText(product.getDesc());
		txtPrice.setText(product.getPrice());
		txtPriceMarket.setText("市场价:" + product.getPrice());
		txtSalesAmount.setText(product.getBuyerCount() + "人购买");
		initViewPager();// 图片循环
		initServiceList();// 服务列表
		initPromotionList();// 促销列表
		initCommentList();// 评论列表
		mWebView.loadUrl("http://www.baidu.com");
		ViewTreeObserver vto2 = txtPriceMarket.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				txtPriceMarket.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				View view = findViewById(R.id.product_details_price_market_line);
				view.setVisibility(View.VISIBLE);
				ViewGroup.LayoutParams params = view.getLayoutParams();
				params.width = txtPriceMarket.getWidth();
				view.setLayoutParams(params);
			}
		});
	}

	/** 图片循环 */
	private void initViewPager() {
		ViewGroup.LayoutParams mViewPagerlayoutParams = mViewPager.getLayoutParams();
		if (mViewPagerlayoutParams != null) {
			mViewPagerlayoutParams.width = Misc.getScreenDisplay(this)[0];
			mViewPagerlayoutParams.height = (int) (mViewPagerlayoutParams.width);
			mViewPager.setLayoutParams(mViewPagerlayoutParams);
		}
		mViewPager.setAdapter(new ProductDetailPicAdapter2(this, product.getImgList()));
		mViewPager.setOnPageChangeListener(this);
		// 设置布局参数
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int marginParam = Misc.dip2px(this, 10) / 2;
		llytPagerPoiner.removeAllViewsInLayout();
		for (int i = 0; i < product.getImgList().size(); i++) {
			// 设置按钮属性
			View item = new ImageView(this);
			item.setBackgroundResource(R.drawable.recommend_gallery);
			item.setTag(String.valueOf(i));
			// item.setOnClickListener(parentActivity);
			params.setMargins(marginParam, 0, marginParam, 0);
			params.weight = 1;
			llytPagerPoiner.addView(item, params);
		}
		llytPagerPoiner.getChildAt(0).setBackgroundResource(R.drawable.recommend_gallery_select);
	}

	/** 服务列表 */
	private void initServiceList() {
		List<ServiceDto> serviceList = product.getServiceList();
		if (null == serviceList) {
			return;
		}
		for (ServiceDto service : serviceList) {
			View view = getLayoutInflater().inflate(R.layout.item_product_service_list, null);
			((TextView) view.findViewById(R.id.item_product_service_list_name)).setText(service.getContent());
			llytServiceList.addView(view);
		}
	}

	/** 促销列表 */
	private void initPromotionList() {
		List<Promotion> promotionList = product.getPromotionList();
		if (null == promotionList) {
			return;
		}
		for (Promotion promotion : promotionList) {
			if (null == promotion) {
				continue;
			}
			View view = getLayoutInflater().inflate(R.layout.item_product_promotion_list, null);
			TextView type = (TextView) view.findViewById(R.id.item_product_promotin_list_type);
			if ("0".equals(promotion.getType())) {
				type.setText("满减");
				type.setBackgroundResource(R.drawable.promotion_type_full_cut);
			}
			if ("1".equals(promotion.getType())) {
				type.setText("满包邮");
				type.setBackgroundResource(R.drawable.promotion_type_full_baoyou);
			}
			if ("2".equals(promotion.getType())) {
				type.setBackgroundResource(R.drawable.promotion_type_full_cut);
			}
			TextView desc = (TextView) view.findViewById(R.id.item_product_promotin_list_desc);
			desc.setText(promotion.getDesc());
			llytPromotionList.addView(view);
		}
	}

	/** 评论列表 */
	private void initCommentList() {
		TextView txtCommenterNum = (TextView) findViewById(R.id.layout_product_comment_commenter_num);
		TextView txtCommenterPoint = (TextView) findViewById(R.id.layout_product_comment_total_point);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.layout_product_comment_ratingbar);
		LinearLayout llytContainer = (LinearLayout) findViewById(R.id.layout_product_comment_container);
		txtCommenterNum.setText("用户评价(" + product.getCommentCount() + "人)");
		txtCommenterPoint.setText("综合评分:" + product.getCommentPoint());
		ratingBar.setRating(product.getCommentPoint());
		if (null == product.getCommentList() || 2 >= product.getCommentList().size()) {
			findViewById(R.id.layout_product_comment_more).setVisibility(View.GONE);
			return;
		}
		if (null != product.getCommentList()) {
			for (int i = 0; i < 2; i++) {
				Comment2 comment = product.getCommentList().get(i);
				if (null == comment) {
					return;
				}
				llytContainer.addView(creatCommentView(comment));
				llytContainer.addView(getLayoutInflater().inflate(R.layout.line_gray_full, null));
			}
		}
	}

	private View creatCommentView(Comment2 comment) {
		View item = getLayoutInflater().inflate(R.layout.item_comment_list, null);
		RatingBar itemRatingBar = (RatingBar) item.findViewById(R.id.item_comment_point);
		TextView txtCommenter = (TextView) item.findViewById(R.id.item_commenter);
		TextView txtCommentTime = (TextView) item.findViewById(R.id.item_comment_time);
		TextView txtCommentContent = (TextView) item.findViewById(R.id.item_comment_content);
		itemRatingBar.setRating(comment.getPoint());
		if (comment.isAnonymous() || null == comment.getCreatorName()) {
			txtCommenter.setText("匿名");
		} else {
			txtCommenter.setText(comment.getCreatorName());
		}
		txtCommentTime.setText(comment.getCreatTime());
		txtCommentContent.setText(comment.getContent());
		return item;
	}

	/** 获取数据 */
	protected void getDateList(int flag) {
		CategoryModel.getInstance().getProductDetail(getIntent().getStringExtra(EXTRA_PRODUCT_ID),
				new CategoryModel.OnGetProducDetailCompletedListener() {

					@Override
					public void onSuccess(ProductDetailResponse info) {
						LoadingDialog.dismiss();
						product = info.getData();
						initData();
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						Misc.finishDelay(ProductDetailsActivity2.this);
					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailed(ResponseException e) {
						ToastUtil.showToastShort(ProductDetailsActivity2.this, e.getResultMsg());
						Misc.finishDelay(ProductDetailsActivity2.this);
					}
				});
	}

	private void showSkuPopupWindow(int action) {
		skuPopupWindow = new ProductSkuListPopupWindow(this, action, pop_layout, product.getSkuList());
		skuPopupWindow.show(findViewById(R.id.pop_base_view));
	}

	@Override
	public void onPageSelected(int position) {
		final int childCount = llytPagerPoiner.getChildCount();
		if (childCount != 0 && position < childCount) {
			for (int j = 0; j < childCount; j++) {
				View view = llytPagerPoiner.getChildAt(j);
				if (view != null) {
					if (j == position) {
						view.setBackgroundResource(R.drawable.recommend_gallery_select);
					} else {
						view.setBackgroundResource(R.drawable.recommend_gallery);
					}
				}
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.layout_trans_btn_back:
			finish();
			break;
		case R.id.layout_trans_btn_cart:
			Intent intent2 = new Intent(this, CarsActivity.class);
			intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// intent2.putExtra(MainActivity.SWITCH_INDEX, 2);
			startActivity(intent2);
			break;
		case R.id.product_details_customer_service:
			break;
		case R.id.product_details_favourite:
			break;
		case R.id.product_details_addcart:
			showSkuPopupWindow(0);
			ActivityUtils.startScaleAnimotion(mScrollView, 0.85f, 0.85f);
			break;
		case R.id.product_details_buy_now:
			showSkuPopupWindow(1);
			ActivityUtils.startScaleAnimotion(mScrollView, 0.85f, 0.85f);
			break;
		case R.id.layout_product_comment_more:
			Intent intent4 = new Intent(this, CommentListActivity.class);
			intent4.putExtra("commentList", (Serializable) product.getCommentList());
			intent4.putExtra("point", product.getCommentPoint());
			intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent4);
			break;
		default:
			break;
		}
	}

	@Override
	public void onPopupWindowDismiss() {
		ActivityUtils.exitScaleAnimotion(mScrollView, 0.85f, 0.85f);
	}

	@Override
	public void onBtnOk(Sku2 sku, int count) {
		ToastUtil.showToastShort(this, sku.getName() + ",count：" + count);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

}
