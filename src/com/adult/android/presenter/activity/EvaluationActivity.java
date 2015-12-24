package com.adult.android.presenter.activity;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.Sku2;
import com.adult.android.model.OrderModel2;
import com.adult.android.model.OrderModel2.OnPostCommentCompletedListener;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.model.internet.bean.StatusInfo;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.lidroid.xutils.BitmapUtils;

public class EvaluationActivity extends BaseActivity implements OnClickListener {

	protected static final String SKU = "sku";

	protected static final String ORDER_ID = "orderId";

	protected static final String ACTION_REFRESH = "action_refresh";

	private Sku2 sku;

	private TextView txtProductName, txtProductPrice, txtPoint;

	private ImageView imgProduct;

	private CheckBox imgHideName;

	private RatingBar evaluationPoint;

	private EditText edTxtContent;

	private Button btnCommit;

	private String orderId;

	private BitmapUtils bitmapUtils;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("评价");
		bitmapUtils = new BitmapUtils(this);
		loadingDialog = new LoadingDialog(this);
		sku = (Sku2) getIntent().getSerializableExtra(SKU);
		orderId = getIntent().getStringExtra(ORDER_ID);
		txtProductName = (TextView) findViewById(R.id.evaluation_detail_product_name);
		imgProduct = (ImageView) findViewById(R.id.evaluation_detail_product_image);
		txtProductPrice = (TextView) findViewById(R.id.evaluation_detail_product_price);
		evaluationPoint = (RatingBar) findViewById(R.id.evaluation_detail_point);
		txtPoint = (TextView) findViewById(R.id.evaluation_detail_point_txt);
		edTxtContent = (EditText) findViewById(R.id.evaluation_detail_edit_content);
		imgHideName = (CheckBox) findViewById(R.id.evaluation_detail_hide_name);
		btnCommit = (Button) findViewById(R.id.evaluation_detail_btn_commit);

		imgHideName.setOnClickListener(this);
		btnCommit.setOnClickListener(this);

		getSkuList();

	}

	/**
	 * 订单Sku列表
	 */
	private void getSkuList() {
		txtProductName.setText(sku.getName());
		txtProductPrice.setText(sku.getCurPrice());

		bitmapUtils.display(imgProduct, ServiceUrlConstants.getImageHost() + sku.getImgUrl());
		evaluationPoint.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float value, boolean arg2) {
				evaluationPoint.setRating(value);
				txtPoint.setText(value + "分");
			}
		});
	}

	private boolean isHide;

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.evaluation_detail_product_image:
			if (isHide) {

			}
			isHide = !isHide;
			break;
		case R.id.evaluation_detail_btn_commit:
			commitEvaluation();
			break;
		default:
			break;
		}
	}

	/** 提交评价 */
	private void commitEvaluation() {
		loadingDialog.show();
		String point = (int) evaluationPoint.getRating() + "";
		String content = edTxtContent.getText().toString().trim();
		OrderModel2.getInstance().postEvaluation(sku.getSkuId(), orderId, point, content,
				new OnPostCommentCompletedListener() {

					@Override
					public void onSuccess(StatusInfo info) {
						loadingDialog.dismiss();
						Intent intent = new Intent(ACTION_REFRESH);
						intent.putExtra(SKU, sku.getSkuId());
						sendBroadcast(intent);
						finish();
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						loadingDialog.dismiss();
					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailed(ResponseException e) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(EvaluationActivity.this, e.getResultMsg());
					}
				});
	}
}
