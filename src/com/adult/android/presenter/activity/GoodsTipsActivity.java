package com.adult.android.presenter.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.adult.android.R;
import com.adult.android.entity.UserOrdersDTO;
import com.adult.android.model.ImageInfoResponse;
import com.adult.android.model.OnUnPayOrderListResponse;
import com.adult.android.model.UserModel;
import com.adult.android.model.UserModel.OnApplyExchangListener;
import com.adult.android.model.UserModel.OnUnPayOrderListCompletedListener;
import com.adult.android.model.internet.exception.HttpResponseException;
import com.adult.android.model.internet.exception.ResponseException;
import com.adult.android.utils.GeneralTool;
import com.adult.android.utils.ImageUtil;
import com.adult.android.utils.ToastUtil;
import com.adult.android.view.LoadingDialog;

public class GoodsTipsActivity extends BaseActivity implements OnClickListener {

	private static final String IMAGE_FILE_LOCATION_PIC = "file:///sdcard/temp_pic.jpg";

	// The Uri to store the big bitmap
	Uri imageUriPhoto = Uri.parse(IMAGE_FILE_LOCATION_PIC);

	private EditText edTxtTipId;

	private List<File> fileList;

	private Bitmap bitmap;

	private UserOrdersDTO orderInfo;

	private ImageView img2, img3, image_show;

	private LoadingDialog loadingDialog;

	private Button btnUpload, btnPayOrder;
	// 保存图片本地路径
	public static final String ACCOUNT_DIR = Environment
			.getExternalStorageDirectory().getPath() + "/account/";
	public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "icon_cache/";
	private static final String IMGPATH = ACCOUNT_DIR
			+ ACCOUNT_MAINTRANCE_ICON_CACHE;
	private static final String IMAGE_FILE_NAME = "faceImage.jpeg";
	private static final String TMP_IMAGE_FILE_NAME = "tmp_faceImage.jpeg";
	File fileone = null;
	File filetwo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intViews();
	}

	private void intViews() {
		setShowTitleBar(true);
		setActivityTitle("提交小票信息");

		orderInfo = (UserOrdersDTO) getIntent().getSerializableExtra(
				"OrderInfo");
		loadingDialog = new LoadingDialog(this);

		fileList = new ArrayList<File>();
		edTxtTipId = (EditText) findViewById(R.id.good_tips_title);
		image_show = (ImageView) findViewById(R.id.good_tips_image_1);
		image_show.setVisibility(View.GONE);
		findViewById(R.id.add_pic_rl).setOnClickListener(this);
		btnUpload = (Button) findViewById(R.id.btn_upload);
		btnPayOrder = (Button) findViewById(R.id.btn_payorder);
		setButtonEnable(btnPayOrder, false);
		btnUpload.setOnClickListener(this);
		btnPayOrder.setOnClickListener(this);

		image_show.setOnClickListener(this);

		File directory = new File(ACCOUNT_DIR);
		File imagepath = new File(IMGPATH);
		if (!directory.exists()) {
			Log.i("zou", "directory.mkdir()");
			directory.mkdir();
		}
		if (!imagepath.exists()) {
			Log.i("zou", "imagepath.mkdir()");
			imagepath.mkdir();
		}

		fileone = new File(IMGPATH, IMAGE_FILE_NAME);
		filetwo = new File(IMGPATH, TMP_IMAGE_FILE_NAME);

		try {
			if (!fileone.exists() && !filetwo.exists()) {
				fileone.createNewFile();
				filetwo.createNewFile();
			}
		} catch (Exception e) {
		}
	}

	// 常量定义
	public static final int TAKE_A_PICTURE = 10;
	public static final int SELECT_A_PICTURE = 20;
	public static final int SET_PICTURE = 30;
	public static final int SET_ALBUM_PICTURE_KITKAT = 40;
	public static final int SELECET_A_PICTURE_AFTER_KIKAT = 50;

	/**
	 * <br>
	 * 功能简述:4.4以上裁剪图片方法实现---------------------- 相册 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void selectImageUriAfterKikat() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
	}

	/**
	 * <br>
	 * 功能简述:裁剪图片方法实现---------------------- 相册 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	private void cropImageUri() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", 640);
		// intent.putExtra("outputY", 640);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);// no face detection
		startActivityForResult(intent, SELECT_A_PICTURE);
	}

	private AlertDialog imageDialog;

	/**
	 * 单选dialog
	 */
	protected void showSelectDialog() {
		CharSequence[] items = { "相册", "拍照", "取消" };
		imageDialog = new AlertDialog.Builder(this).setTitle("上传图片")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 手机选图
						if (item == 0) {
							if (mIsKitKat) {
								selectImageUriAfterKikat();
							} else {
								cropImageUri();
							}
						} else if (item == 1) {// 拍照
							// Intent intent = new Intent(
							// MediaStore.ACTION_IMAGE_CAPTURE);
							// intent.putExtra(MediaStore.EXTRA_OUTPUT,
							// imageUriPhoto);
							// startActivityForResult(intent, 2);

							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(IMGPATH,
											IMAGE_FILE_NAME)));
							startActivityForResult(intent, 2);
						}
					}
				}).create();
		imageDialog.show();
	}

	private String mAlbumPicturePath = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 0)
			return;
		// 选图
		if (requestCode == SELECT_A_PICTURE) {
			if (resultCode == RESULT_OK && null != data) {
				// Log.i("zou", "4.4以下的");
				Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(new File(
						IMGPATH, TMP_IMAGE_FILE_NAME)));
				image_show.setVisibility(View.VISIBLE);
				image_show.setImageBitmap(bitmap);
				this.bitmap = bitmap;
			} else if (resultCode == RESULT_CANCELED) {
				ToastUtil.showToastShort(this, "取消");
			}
		} else if (requestCode == SELECET_A_PICTURE_AFTER_KIKAT) {
			if (resultCode == RESULT_OK && null != data) {
				// Log.i("zou", "4.4以上的");
				mAlbumPicturePath = getPath(getApplicationContext(),
						data.getData());
				extend = mAlbumPicturePath.substring(
						mAlbumPicturePath.lastIndexOf(".") + 1,
						mAlbumPicturePath.length());
				cropImageUri(Uri.fromFile(new File(mAlbumPicturePath)),
						SET_ALBUM_PICTURE_KITKAT);
			} else if (resultCode == RESULT_CANCELED) {
				ToastUtil.showToastShort(this, "取消");
			}
		} else if (requestCode == SET_ALBUM_PICTURE_KITKAT) {
			Log.i("zou", "4.4以上上的 RESULT_OK");
			Bitmap bitmap = data.getParcelableExtra("data");
			image_show.setVisibility(View.VISIBLE);
			image_show.setImageBitmap(bitmap);
			this.bitmap = bitmap;
		}

		// 拍照
		if (2 == requestCode) {
			cropImageUri(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)), 3);
		}

		if (3 == requestCode) {
			// bitmap = Misc.getSmallBitmap(imageUriPhoto.getPath());
			Bitmap bitmap = null;
			if (mIsKitKat) {
				if (null != data) {
					bitmap = data.getParcelableExtra("data");
				}
			} else {
				bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH,
						IMAGE_FILE_NAME)));
			}
			image_show.setVisibility(View.VISIBLE);
			image_show.setImageBitmap(bitmap);
			this.bitmap = bitmap;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	private String extend = "";

	final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	private void cropImageUri(Uri uri, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		if (mIsKitKat) {
			intent.putExtra("return-data", true);
		} else {
			intent.putExtra("return-data", false);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, requestCode);
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
				postTipsRop();
				break;
			case 2:
				postTipsRop();
				break;
			default:
				postTipsRop();
				break;
			}

			return 0;
		}
	}

	public void postTipsRop() {
		if (null == bitmap) {
			return;
		}
		Bitmap bim = compressImage(bitmap);
		byte[] im = ImageUtil.bitmap2Bytes(bim);
		String content = Base64.encodeToString(im, im.length);
		loadingDialog.show();
		UserModel.getInstance().postTipsRop(edTxtTipId.getText().toString(),
				orderInfo.getOrderId(), extend, content,
				new OnApplyExchangListener() {

					@Override
					public void onUploadSuccess(ImageInfoResponse info) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(GoodsTipsActivity.this,
								"图片上传成功");
						image_show.setClickable(false);
						findViewById(R.id.add_pic_rl).setClickable(false);
						setButtonEnable(btnUpload, false);
						setButtonEnable(btnPayOrder, true);
					}

					@Override
					public void onUploadFail(String e) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(GoodsTipsActivity.this,
								"faild");
					}

				});
	}

	protected void setButtonEnable(Button btn, boolean enable) {
		btn.setEnabled(enable);
		if (enable) {
			btn.setTextColor(R.color.white);
			btn.setBackgroundResource(R.drawable.common_round_conner_bg_main);
		} else {
			btn.setBackgroundResource(R.drawable.common_round_conner_bg_gray);
			btn.setTextColor(R.color.gray);
		}
	}

	protected void payOrder() {
		loadingDialog.show();
		UserModel.getInstance().payOrder(orderInfo.getOrderId(),
				new OnUnPayOrderListCompletedListener() {

					@Override
					public void onSuccess(OnUnPayOrderListResponse info) {
						loadingDialog.dismiss();
						sendBroadcast(new Intent(
								OrderDetailsActivity2.ACTION_ORDER_CHANGED));
						finish();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onHttpException(HttpResponseException e) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(GoodsTipsActivity.this,
								e.getResultMsg());
					}

					@Override
					public void onFinish() {
						loadingDialog.dismiss();

					}

					@Override
					public void onFailed(ResponseException e) {
						loadingDialog.dismiss();
						ToastUtil.showToastShort(GoodsTipsActivity.this,
								e.getResultMsg());

					}
				});

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// case R.id.btn_controller:
		// loadingDialog.show();
		// new GeocodeingTask(1).execute();
		// break;
		case R.id.good_tips_image_1:
			showSelectDialog();
			break;
		case R.id.add_pic_rl:
			showSelectDialog();
			break;
		case R.id.btn_payorder:
			payOrder();
			break;
		case R.id.btn_upload:
			if (GeneralTool.isEmpty(edTxtTipId.getText().toString())) {
				ToastUtil.showToastShort(GoodsTipsActivity.this, "请填写小票号");
				return;
			}

			if (null == orderInfo
					|| GeneralTool.isEmpty(orderInfo.getOrderId())) {
				ToastUtil.showToastShort(GoodsTipsActivity.this, "订单信息有误");
				return;
			}
			loadingDialog.show();
			new GeocodeingTask(2).execute();
			break;
		default:
			break;
		}

	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	private Bitmap compressImage(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap2 = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap2;
	}

	// 压缩图片尺寸
	public Bitmap compressBySize(String pathName, int targetWidth,
			int targetHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
		// 得到图片的宽度、高度；
		float imgWidth = opts.outWidth;
		float imgHeight = opts.outHeight;

		Log.e("width:", "" + opts.outWidth);
		Log.e("height:", "" + opts.outHeight);

		// 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
		if (targetWidth > 0 || targetHeight > 0) {
			int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
			int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
			opts.inSampleSize = 1;
			if (widthRatio > 1 || heightRatio > 1) {
				if (widthRatio > heightRatio) {
					opts.inSampleSize = widthRatio;
				} else {
					opts.inSampleSize = heightRatio;
				}
			}
		}
		// 设置好缩放比例后，加载图片进内容；
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pathName, opts);
		return bitmap;
	}

}
