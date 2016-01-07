package com.adult.android.presenter.activity;

import java.io.IOException;
import java.util.Hashtable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.OnConnectUserResponse;
import com.adult.android.entity.OnGetCartListResponse;
import com.adult.android.entity.OnGetMyResponse;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnAddToCartCompletedListener;
import com.adult.android.model.UserModel.OnConnectUserCompletedListener;
import com.adult.android.model.UserModel.OnGetCartListCompletedListener;
import com.adult.android.model.UserModel.OnGetMyCompletedListener;
import com.adult.android.model.UserModel.OnUpdateCartCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.presenter.AgentApplication;
import com.adult.android.utils.JsonUtils;
import com.adult.android.utils.Misc;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class AAAAAAActivity extends BaseActivity implements OnClickListener {

	private LoadingDialog loadingDialog;

	private TextView txtresult;

	private final static int LOADING_FOR_PRODUCT = 29;

	public static final int SCAN_USER = 1;

	public static final int SCAN_PRODUCT = 2;

	private static int QR_WIDTH = 200;

	private static int QR_HEIGHT = 200;

	private ImageView imageCode;

	private MyBroadCastReceive broadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		broadcastReceiver = new MyBroadCastReceive();
		IntentFilter filter = new IntentFilter("com.box.android");
		// 注册广播接收器
		registerReceiver(broadcastReceiver, filter);
	}

	private void initViews() {
		loadingDialog = new LoadingDialog(this);
		QR_WIDTH = QR_HEIGHT = Misc.dip2px(this, 260f);
		AgentApplication.get().setCustomerId(
				"db143c39-83f4-4bf1-8993-a84decc42f7");
		txtresult = (TextView) findViewById(R.id.txt_result);
		imageCode = (ImageView) findViewById(R.id.asistant_asistant_code);
		findViewById(R.id.btn_get_my).setOnClickListener(this);
		findViewById(R.id.btn_connectuser).setOnClickListener(this);
		findViewById(R.id.btn_get_cart_list).setOnClickListener(this);
		findViewById(R.id.btn_add_to_cart).setOnClickListener(this);
		findViewById(R.id.btn_update_cart).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		loadingDialog.show();
		switch (view.getId()) {
		case R.id.btn_get_my:
			// my();
			// 扫描商品
			// Intent intent2 = new Intent(this, CaptureActivity.class);
			// intent2.putExtra("type", SCAN_PRODUCT);
			// startActivityForResult(intent2, SCAN_PRODUCT);
			break;
		case R.id.btn_connectuser:
			// connectUser(AgentApplication.get().getCustomerId());
			createQRImage(AgentApplication.get().getShopperId());
			break;

		case R.id.btn_get_cart_list:
			getcartList();
			break;

		case R.id.btn_add_to_cart:
			addToCart();
			break;
		case R.id.btn_update_cart:
			updateCart();
			break;

		default:
			break;
		}

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

	private void my() {
		UserModel.getInstance().my(new OnGetMyCompletedListener() {

			@Override
			public void onSuccess(OnGetMyResponse info) {
				loadingDialog.dismiss();
				try {
					txtresult.setText(JsonUtils.generate(info));
				} catch (IOException e) {

					e.printStackTrace();
				}
				AgentApplication.get().setShopId(
						info.getData().getShop().getShopId());
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

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

			}
		});
	}

	private void connectUser(String userId) {
		UserModel.getInstance().connectUser(userId,
				new OnConnectUserCompletedListener() {

					@Override
					public void onSuccess(OnConnectUserResponse info) {
						loadingDialog.dismiss();
						try {
							txtresult.setText(JsonUtils.generate(info));
						} catch (IOException e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						loadingDialog.dismiss();
						txtresult.setText(e.getResultMsg());
					}

					@Override
					public void onFinish() {
						loadingDialog.dismiss();

					}

					@Override
					public void onFailed(ResponseException e) {
						loadingDialog.dismiss();
						txtresult.setText(e.getResultMsg());
					}
				});
	}

	private void getcartList() {
		UserModel.getInstance().getcartList(
				new OnGetCartListCompletedListener() {

					@Override
					public void onSuccess(OnGetCartListResponse info) {
						loadingDialog.dismiss();
						try {
							txtresult.setText(JsonUtils.generate(info));
						} catch (IOException e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						loadingDialog.dismiss();
						txtresult.setText(e.getResultMsg());
					}

					@Override
					public void onFinish() {
						loadingDialog.dismiss();

					}

					@Override
					public void onFailed(ResponseException e) {
						loadingDialog.dismiss();
						txtresult.setText(e.getResultMsg());
					}
				});
	}

	private void addToCart() {
		UserModel.getInstance().addToCart(new OnAddToCartCompletedListener() {

			@Override
			public void onSuccess(OnGetCartListResponse info) {
				loadingDialog.dismiss();
				try {
					txtresult.setText(JsonUtils.generate(info));
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onHttpException(HttpResponseException e) {
				loadingDialog.dismiss();
				txtresult.setText(e.getResultMsg());
			}

			@Override
			public void onFinish() {
				loadingDialog.dismiss();

			}

			@Override
			public void onFailed(ResponseException e) {
				loadingDialog.dismiss();
				txtresult.setText(e.getResultMsg());
			}
		});
	}

	private void updateCart() {
		UserModel.getInstance().updateCart("sss", "0",
				new OnUpdateCartCompletedListener() {

					@Override
					public void onSuccess(OnGetCartListResponse info) {
						loadingDialog.dismiss();
						try {
							txtresult.setText(JsonUtils.generate(info));
						} catch (IOException e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						loadingDialog.dismiss();
						txtresult.setText(e.getResultMsg());
					}

					@Override
					public void onFinish() {
						loadingDialog.dismiss();

					}

					@Override
					public void onFailed(ResponseException e) {
						loadingDialog.dismiss();
						txtresult.setText(e.getResultMsg());
					}
				});
	}

	private MyHandler mHandler = new MyHandler();

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			loadingDialog.dismiss();
			ToastUtil.showToastShort(AAAAAAActivity.this, (String) msg.obj);
		}
	}

	class MyBroadCastReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (null == intent) {
				return;
			}
			loadingDialog.show();
			Message msg2 = new Message();
			msg2.obj = intent.getStringExtra("scan_result");
			msg2.what = LOADING_FOR_PRODUCT;
			mHandler.sendMessageDelayed(msg2, 500);
			return;
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}
}
