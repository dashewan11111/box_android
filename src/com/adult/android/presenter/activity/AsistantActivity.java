package com.adult.android.presenter.activity;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.OnGetMyResponse;
import com.adult.android.entity.Shop;
import com.adult.android.entity.Shopper;
import com.adult.android.logic.UserLogic;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnGetMyCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.utils.Misc;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lidroid.xutils.BitmapUtils;

public class AsistantActivity extends BaseActivity {

	private TextView txtShopName, txtShopperName;

	private ImageView imageShopper, imageCode;

	private LoadingDialog loadingDialog;

	private BitmapUtils bitmapUtils;

	private int QR_WIDTH = 200;

	private int QR_HEIGHT = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setShowTitleBar(true);
		setActivityTitle("Shop Asistant");
		QR_WIDTH = QR_HEIGHT = Misc.dip2px(this, 300f);
		loadingDialog = new LoadingDialog(this);
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils
				.configDefaultLoadFailedImage(R.drawable.default_user_avator_m);
		bitmapUtils.configDefaultLoadingImage(R.drawable.default_user_avator_m);
		txtShopName = (TextView) findViewById(R.id.asistant_shop_name);
		txtShopperName = (TextView) findViewById(R.id.asistant_shopper_name);
		imageShopper = (ImageView) findViewById(R.id.asistant_shopper_image);
		imageCode = (ImageView) findViewById(R.id.asistant_asistant_code);
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
				txtShopperName.setText(UserLogic.getInsatnace().getUserName());
				bitmapUtils.display(imageShopper, shopper.getShopperPhoto());
				AgentApplication.get().setShopId(
						info.getData().getShop().getShopId());
				createQRImage(AgentApplication.get().getShopperId());
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onHttpException(HttpResponseException e) {
				loadingDialog.dismiss();
				ToastUtil.showToastShort(AsistantActivity.this,
						e.getResultMsg());
			}

			@Override
			public void onFinish() {
				loadingDialog.dismiss();

			}

			@Override
			public void onFailed(ResponseException e) {
				loadingDialog.dismiss();
				ToastUtil.showToastShort(AsistantActivity.this,
						e.getResultMsg());
			}
		});
	}

	public void createQRImage(String url) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			// 显示到一个ImageView上面
			imageCode.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
}
