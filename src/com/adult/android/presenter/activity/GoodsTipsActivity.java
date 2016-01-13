package com.adult.android.presenter.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.adult.android.R;
import com.adult.android.entity.TopicDTO.ImageUrl;
import com.adult.android.model.ImageInfoResponse;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnApplyExchangListener;
import com.adult.android.utils.ImageUtil;
import com.adult.android.utils.Misc;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;

public class GoodsTipsActivity extends BaseActivity implements OnClickListener {

	private EditText edTxtTipId;

	private List<File> fileList;

	private Bitmap bitmap;

	private ImageView img1, img2, img3;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intViews();
	}

	private void intViews() {
		setShowTitleBar(true);
		setActivityTitle("提交小票信息");
		setShowRightText("提交");
		loadingDialog = new LoadingDialog(this);
		setRightTextOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				loadingDialog.show();
				new GeocodeingTask(1).execute();
			}
		});
		fileList = new ArrayList<File>();
		edTxtTipId = (EditText) findViewById(R.id.good_tips_title);
		img1 = (ImageView) findViewById(R.id.good_tips_image_1);
		img2 = (ImageView) findViewById(R.id.good_tips_image_2);
		img3 = (ImageView) findViewById(R.id.good_tips_image_3);
		img1.setOnClickListener(new OnImageClickListener(1));
		img2.setOnClickListener(new OnImageClickListener(2));
		img3.setOnClickListener(new OnImageClickListener(3));
		findViewById(R.id.btn_controller).setOnClickListener(this);
		findViewById(R.id.btn_rop).setOnClickListener(this);

	}

	class OnImageClickListener implements OnClickListener {

		public OnImageClickListener(int i) {

		}

		@Override
		public void onClick(View view) {
			Intent intent = new Intent();
			/* 开启Pictures画面Type设定为image */
			intent.setType("image/*");
			/* 使用Intent.ACTION_GET_CONTENT这个Action */
			intent.setAction(Intent.ACTION_GET_CONTENT);
			/* 取得相片后返回本画面 */
			startActivityForResult(intent, 1);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			addImageView(uri);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String extend = "";

	private void addImageView(Uri uri) {
		ContentResolver cr = this.getContentResolver();
		try {
			bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
			String path = Misc.getImageAbsolutePath(this, uri);
			extend = path.substring(path.lastIndexOf(".") + 1, path.length());
			Log.e("文件地址", path + "," + extend);
			fileList.add(new File(Misc.getImageAbsolutePath(this, uri)));
			switch (fileList.size()) {
			case 1:
				img1.setImageBitmap(bitmap);
				break;
			case 2:
				img2.setImageBitmap(bitmap);
				break;
			case 3:
				img3.setImageBitmap(bitmap);
				break;
			default:
				break;
			}
		} catch (FileNotFoundException e) {
			Log.e("Exception", e.getMessage(), e);
		}
	}

	private class GeocodeingTask extends AsyncTask<Integer, Integer, Integer> {
		private int a = 1;

		public GeocodeingTask(int a) {
			this.a = a;
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (a) {
			case 1:
				postTipsController();
				break;
			case 2:
				postTipsRop();
				break;
			default:
				postTipsController();
				break;
			}

			return 0;
		}
	}

	public void postTipsController() {
		loadingDialog.show();
		UserModel.getInstance().postTipsController(extend, bitmap,
				new OnApplyExchangListener() {

					@Override
					public void onUploadSuccess(ImageInfoResponse info) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(GoodsTipsActivity.this,
								"Success");
					}

					@Override
					public void onUploadFail(String e) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(GoodsTipsActivity.this,
								"faild");
					}

				});
	}

	public void postTipsRop() {
		loadingDialog.show();
		UserModel.getInstance().postTipsRop(extend, bitmap,
				new OnApplyExchangListener() {

					@Override
					public void onUploadSuccess(ImageInfoResponse info) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(GoodsTipsActivity.this,
								"Success");
					}

					@Override
					public void onUploadFail(String e) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(GoodsTipsActivity.this,
								"faild");
					}

				});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_controller:
			loadingDialog.show();
			new GeocodeingTask(1).execute();
			break;
		case R.id.btn_rop:
			loadingDialog.show();
			new GeocodeingTask(2).execute();
			break;
		default:
			break;
		}

	}
}
