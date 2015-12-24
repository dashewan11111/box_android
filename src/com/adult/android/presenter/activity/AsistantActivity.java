package com.adult.android.presenter.activity;

import java.io.IOException;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.OnGetMyResponse;
import com.adult.android.entity.Shop;
import com.adult.android.entity.Shopper;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnGetMyCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.utils.JsonUtils;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.lidroid.xutils.BitmapUtils;

public class AsistantActivity extends BaseActivity {

	private TextView txtShopName, txtShopperName;

	private ImageView imageShopper;

	private LoadingDialog loadingDialog;

	private BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("Shop Asistant");
		loadingDialog = new LoadingDialog(this);
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_user_avator_m);
		bitmapUtils.configDefaultLoadingImage(R.drawable.default_user_avator_m);
		txtShopName = (TextView) findViewById(R.id.asistant_shop_name);
		txtShopperName = (TextView) findViewById(R.id.asistant_shopper_name);
		imageShopper = (ImageView) findViewById(R.id.asistant_shopper_image);
		getShopperInfo();
	}

	private void getShopperInfo() {
		UserModel.getInstance().my(new OnGetMyCompletedListener() {

			@Override
			public void onSuccess(OnGetMyResponse info) {
				loadingDialog.dismiss();
				if (null == info.getData()) {
					finish();
					return;
				}
				Shop shop = info.getData().getShop();
				Shopper shopper = info.getData().getShopper();
				txtShopName.setText(shop.getShopNameCn());
				txtShopperName.setText(shopper.getShopperFirstName() + " " + shopper.getShopperLastName());
				bitmapUtils.display(imageShopper, shopper.getShopperPhoto());
				AgentApplication.get().setShopId(info.getData().getShop().getShopId());
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
				loadingDialog.dismiss();

			}

			@Override
			public void onFailed(ResponseException e) {
				loadingDialog.dismiss();
				ToastUtil.showToastShort(AsistantActivity.this, e.getResultMsg());
			}
		});
	}
}
