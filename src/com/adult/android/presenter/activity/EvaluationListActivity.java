package com.adult.android.presenter.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.OrderDtoForList;
import com.adult.android.entity.Sku2;
import com.adult.android.model.constants.ServiceUrlConstants;
import com.adult.android.utils.GeneralTool;
import com.lidroid.xutils.BitmapUtils;

public class EvaluationListActivity extends BaseActivity implements OnClickListener {

	public static String ORDER_DETAILS = "orderDetails";

	private OrderDtoForList orderDetailsDto;

	private BitmapUtils bitmapUtils;

	private LinearLayout llytProductContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		registerReceiver(broadcastReceiver, new IntentFilter(EvaluationActivity.ACTION_REFRESH));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("评价");
		orderDetailsDto = (OrderDtoForList) getIntent().getSerializableExtra(ORDER_DETAILS);
		bitmapUtils = new BitmapUtils(this);
		llytProductContainer = (LinearLayout) findViewById(R.id.evaluation_list_prodcut_container);
		addAllProduct(orderDetailsDto);
	}

	private void addAllProduct(final OrderDtoForList order) {
		if (null != llytProductContainer) {
			llytProductContainer.removeAllViewsInLayout();
		}
		if (null == order) {
			return;
		}

		for (final Sku2 sku : order.getSkuList()) {
			View view = getLayoutInflater().inflate(R.layout.item_evaluation_list, null);
			ImageView imageProduct = (ImageView) view.findViewById(R.id.item_evaluation_list_product_image);
			TextView txtProductName = (TextView) view.findViewById(R.id.item_evaluation_list_product_name);
			TextView txtProductPrice = (TextView) view.findViewById(R.id.item_evaluation_list_product_price);
			TextView txtProductFormat = (TextView) view.findViewById(R.id.item_evaluation_list_product_format);
			TextView txtProductCount = (TextView) view.findViewById(R.id.item_evaluation_list_product_count);
			final Button btnAction = (Button) view.findViewById(R.id.item_evaluation_list_product_action);
			String action = "";
			switch (order.getStatus()) {
			case 1:
				action = "去付款";
				break;
			case 2:
				// action = "待付款";
				break;
			case 3:
				// action = "待发货";
				break;
			case 4:
				action = "确认收货";
				break;
			case 5:
				action = "去评价";
				break;
			default:
				break;
			}
			btnAction.setText(action);
			btnAction.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					switch (order.getStatus()) {
					case 1:
						Intent intentEvaluation = new Intent(EvaluationListActivity.this, EvaluationActivity.class);
						intentEvaluation.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
						intentEvaluation.putExtra(EvaluationActivity.SKU, sku);
						intentEvaluation.putExtra(EvaluationActivity.ORDER_ID, order.getOrderId());
						startActivity(intentEvaluation);
						break;
					case 2:

						break;
					case 3:

						break;
					case 4:

						break;
					case 5:

						break;
					default:
						break;
					}
				}
			});
			bitmapUtils.display(imageProduct, ServiceUrlConstants.getImageHost() + sku.getImgUrl());
			txtProductName.setText(sku.getName());
			txtProductPrice.setText(getResources().getString(R.string.rmb) + sku.getCurPrice());
			txtProductFormat.setText(sku.getName());
			txtProductCount.setText("数量：" + order.getProductCount());

			llytProductContainer.addView(view);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		default:
			break;
		}
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		public void onReceive(android.content.Context arg0, Intent intent) {
			String skuId = intent.getStringExtra(EvaluationActivity.SKU);
			if (!GeneralTool.isEmpty(skuId)) {
				for (int i = 0; i < orderDetailsDto.getSkuList().size(); i++) {
					Sku2 item = orderDetailsDto.getSkuList().get(i);
					if (item.getSkuId().equals(skuId)) {
						List<Sku2> skuList = orderDetailsDto.getSkuList();
						skuList.remove(i);
						if (0 == skuList.size()) {
							finish();
							return;
						}
						orderDetailsDto.setSkuList(skuList);
						addAllProduct(orderDetailsDto);
						return;
					}
				}
			}
		};
	};

}
