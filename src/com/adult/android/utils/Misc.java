package com.adult.android.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.adult.android.R;
import com.adult.android.entity.DivisionCity;
import com.adult.android.entity.DivisionProvince;
import com.adult.android.entity.DivisionRegion;
import com.adult.android.presenter.AgentApplication;

public class Misc {
	public static String getHtmlOfUrl(String url) throws IOException {
		StringBuilder str = new StringBuilder("");
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		int status = response.getStatusLine().getStatusCode();
		if (status < 300 && status >= 200) {
			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				str.append(line + "\n");
			}
			in.close();
		}
		return str.toString();
	}

	/**
	 * 获取通知栏高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c;
		Object obj;
		Field field;
		int x, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int dip2px(Context context, Double dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static View inflate(Context context, int id) {
		return LayoutInflater.from(context).inflate(id, null);
	}

	/**
	 * 将长时间格式字符串转换为字符串,默认为yyyy-MM-dd HH:mm:ss
	 * 
	 * @param milliseconds
	 *            long型时间,单位是微秒
	 * @param dataFormat
	 *            需要返回的时间格式，例如： yyyy-MM-dd， yyyy-MM-dd HH:mm:ss，
	 *            yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 * @return dataFormat格式的时间结果字符串
	 * @see "http://download.oracle.com/technetwork/java/javase/6/docs/zh/api/java/text/SimpleDateFormat.html"
	 */
	public static String dateFormat(long milliseconds, String dataFormat) {
		if (TextUtils.isEmpty(dataFormat)) {
			dataFormat = "yyyy- MM- dd HH:mm:ss";
		}
		Date date = new Date(milliseconds * 1l);
		SimpleDateFormat formatter = new SimpleDateFormat(dataFormat,
				Locale.CHINA);
		return formatter.format(date);
	}

	/**
	 * 年月日格式
	 * 
	 * @param milliseconds
	 * @param dataFormat
	 * @return
	 */
	public static String dateFormatShort(long milliseconds, String dataFormat) {
		if (TextUtils.isEmpty(dataFormat)) {
			dataFormat = "yyyy- MM- dd";
		}
		Date date = new Date(milliseconds * 1l);
		SimpleDateFormat formatter = new SimpleDateFormat(dataFormat,
				Locale.CHINA);
		return formatter.format(date);
	}

	/**
	 * float类型，保留N个小数
	 */
	public static String scale(float scale, int count) {
		final String format = "%." + count + "f";
		String result;
		try {
			result = String.format(format, scale);
		} catch (Exception e) {
			e.printStackTrace();
			return scale + "";
		}
		return result;
	}

	/**
	 * double，保留N个小数
	 */
	public static String scale(double scale, int count) {
		final String format = "%." + count + "f";
		String result;
		try {
			result = String.format(format, scale);
		} catch (Exception e) {
			e.printStackTrace();
			return scale + "";
		}
		return result;
	}

	private static int[] screenDisplay;

	/**
	 * 获取屏幕分辨率
	 * 
	 * @return int[]数组，长度为2
	 */
	public static synchronized int[] getScreenDisplay(Activity activity) {
		if (screenDisplay == null || screenDisplay[0] == 0
				|| screenDisplay[1] == 0) {
			screenDisplay = new int[2];
			if (activity != null) {
				DisplayMetrics dm = new DisplayMetrics();
				activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
				screenDisplay[0] = dm.widthPixels;
				screenDisplay[1] = dm.heightPixels;
			}
		}
		return screenDisplay;
	}

	/**
	 * 利用反射获取资源id
	 * 
	 * @param c
	 *            class
	 * @param name
	 *            资源名称
	 */
	public static int getIdentifier(Class<?> c, String name) {
		// Resources res = context.getResources();
		// return res.getIdentifier(name, defType, context.getPackageName());
		Field field = null;
		try {
			field = c.getDeclaredField(name);
			field.setAccessible(true);
			return field.getInt(c);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return 0;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static void startSystemBrowser(Context context, String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		context.startActivity(i);
	}

	/**
	 * 将科学计数法变成普通计数法表示的数字
	 */
	public static String scientificNotationToNormal(double number) {
		BigDecimal db = new BigDecimal(number);
		return db.toPlainString();
	}

	/**
	 * 将科学计数法变成普通计数法表示的数字
	 */
	public static String scientificNotationToNormal(float number) {
		BigDecimal db = new BigDecimal(number);
		return db.toPlainString();
	}

	// 保留小数点后两位小数
	public static String Number2(double pDouble) {
		BigDecimal b = new BigDecimal(pDouble);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1 + "";
	}

	/**
	 * 获取设备号
	 */
	public static String getDeviceId(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		// Log.e("手机参数", "DeviceId:" + tmDevice + ",SimSerialNumber:" + tmSerial
		// + ",androidId:" + androidId);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		return deviceUuid.toString();
	}

	/**
	 * 获取应用版本名称
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return "";
		}
		return packageInfo.versionName;
	}

	/**
	 * 获取应用版本code
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static int getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return 0;
		}
		return packageInfo.versionCode;
	}

	public static String getChannelName(Context context) {
		String msg = "";
		ApplicationInfo appInfo;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			msg = appInfo.metaData.getString("BaiduMobAd_CHANNEL");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 检查网络是否可用
	 * 
	 * @param paramContext
	 * @return
	 */
	public static boolean netWorkEnable(Context paramContext) {
		boolean i = false;
		NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
				.getSystemService("connectivity")).getActiveNetworkInfo();
		if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable()))
			return true;
		return false;
	}

	/**
	 * 将ip的整数形式转换成ip形式
	 * 
	 * @param ipInt
	 * @return
	 */
	public static String int2ip(int ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}

	/**
	 * 获取当前ip地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalIpAddress(Context context) {
		try {
			if ("null".equals(getCurrentNetType(context))) {
				return null;
			}
			if ("wifi".equals(getCurrentNetType(context))) {
				WifiManager wifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				// 判断wifi是否开启
				if (!wifiManager.isWifiEnabled()) {
					getLocalIpAddressByGPRS();
				}
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				int i = wifiInfo.getIpAddress();
				return int2ip(i);
			} else {
				return getLocalIpAddressByGPRS();
			}
		} catch (Exception ex) {
			Log.e("WifiPreference IpAddress", "请保证是WIFI,或者请重新打开网络!");
		}
		return null;
	}

	public static String getLocalIpAddressByGPRS() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	/**
	 * 得到当前的手机网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentNetType(Context context) {
		String type = "";
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			type = "null";
		} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			type = "wifi";
		} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			int subType = info.getSubtype();
			if (subType == TelephonyManager.NETWORK_TYPE_CDMA
					|| subType == TelephonyManager.NETWORK_TYPE_GPRS
					|| subType == TelephonyManager.NETWORK_TYPE_EDGE) {
				type = "2g";
			} else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
					|| subType == TelephonyManager.NETWORK_TYPE_HSDPA
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_0
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
				type = "3g";
			} else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
				type = "4g";
			}
		}
		return type;
	}

	/**
	 * 判断某个界面是否在前台
	 * 
	 * @param context
	 * @param className
	 *            某个界面名称
	 */
	public static boolean isForeground(Class<? extends Activity> aClass) {
		if (TextUtils.isEmpty(aClass.getName())) {
			return false;
		}

		ActivityManager am = (ActivityManager) AgentApplication.get()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (aClass.getName().equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public static String getUTF8XMLString(String xml) {
		// A StringBuffer Object
		StringBuffer sb = new StringBuffer();
		sb.append(xml);
		String xmString = "";
		String xmlUTF8 = "";
		try {
			xmString = new String(sb.toString().getBytes("UTF-8"));
			xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
			Log.e("utf-8 编码：", xml + "-->" + xmlUTF8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return to String Formed
		return xmlUTF8;
	}

	public static void setPrice(Context context, TextView txtView, boolean big) {
		if (null == txtView) {
			return;
		}
		if (null == txtView.getText().toString()
				|| "".equals(txtView.getText().toString().trim())) {
			return;
		}
		txtView.setGravity(Gravity.CENTER);
		String price = txtView.getText().toString();
		SpannableString sp = new SpannableString(price);
		sp.setSpan(new AbsoluteSizeSpan(Misc.sp2px(context, 14)), 0, 1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (big) {
			sp.setSpan(new AbsoluteSizeSpan(Misc.sp2px(context, 21)), 1,
					price.indexOf("."), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			sp.setSpan(new AbsoluteSizeSpan(Misc.sp2px(context, 16)), 1,
					price.indexOf("."), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		sp.setSpan(new AbsoluteSizeSpan(Misc.sp2px(context, 14)),
				price.indexOf(".") + 1, price.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		txtView.setText(sp);
	}

	/**
	 * 判断活动对应的图标
	 * 
	 * @param promotionType
	 */
	public static int switchPromotionIcon(String promotionType) {
		if (promotionType.equals("fullReduction")) {
			// 满减
			return R.drawable.manjian;
		} else if (promotionType.equals("buy")) {
			// 买赠
			return R.drawable.maizeng;
		} else if (promotionType.equals("withIncreasing")) {
			// 满赠
			return R.drawable.manzeng;
		} else if (promotionType.equals("priceDown")) {
			// 直降
			return R.drawable.zhijiang;
		} else if (promotionType.equals("fullBack")) {
			// 满减
			return R.drawable.manfan;
		} else {
			return 0;
		}
	}

	/**
	 * 将单个的数字前面加"0"
	 * 
	 * @param num
	 * @return
	 */
	public static String formatInt(int num) {
		if (num > 9) {
			return String.valueOf(num);
		} else {
			return "0" + String.valueOf(num);
		}
	}

	/**
	 * Map转成带逗号的字符串
	 * 
	 * @param map
	 * @return
	 */
	public static String transMapToString(Map map) {
		Entry entry;
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			entry = (Entry) iterator.next();
			sb.append(entry.getValue().toString()).append("-");
		}
		return sb.substring(0, sb.length() - 1).toString();
	}

	public static DivisionProvince[] getPCAData(Context context) {
		DivisionProvince[] provinceList = null;
		AssetManager asset = context.getAssets();

		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserProvienceHandler handler = new XmlParserProvienceHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			List<DivisionProvince> tempList = handler.getDataList();
			final int size = tempList == null ? 0 : tempList.size();
			provinceList = new DivisionProvince[size];
			for (int i = 0; i < size; i++) {
				provinceList[i] = tempList.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return provinceList;
	}

	public String getAddressDescription(String provinceId, String cityId,
			String areaId, String addressDetails, Context context) {
		StringBuffer address = new StringBuffer("");
		DivisionProvince[] provinceList = getPCAData(context);
		for (int i = 0; i < provinceList.length; i++) {
			DivisionProvince province = provinceList[i];
			if (!TextUtils.isEmpty(provinceId)
					&& provinceId.equals(province.getId())) {
				address.append(province.getName());
				List<DivisionCity> cityList = province.getCity_list();
				A: for (DivisionCity city : cityList) {
					if (!TextUtils.isEmpty(cityId)
							&& cityId.equals(city.getId())) {
						address.append(city.getName());
						List<DivisionRegion> regionList = city.getArea_list();
						B: for (DivisionRegion region : regionList) {
							if (!TextUtils.isEmpty(areaId)
									&& areaId.equals(region.getId())) {
								address.append(region.getName());
								break B;
							}
						}
						break A;
					}
				}
				break;
			}
		}
		address.append(addressDetails);
		return address.toString();
	}

	public static void finishDelay(final Context context) {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				((Activity) context).finish();
				((Activity) context).overridePendingTransition(
						R.anim.activity_fade_in, R.anim.activity_fade_out);
			}
		}, 500);
	}

	/**
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
	 * 
	 * @param activity
	 * @param imageUri
	 * @author yaoxing
	 * @date 2014-10-12
	 */
	@TargetApi(19)
	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
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

	/**
	 * 图片转字节数组
	 * 
	 * @param bitmap
	 * @return
	 */
	public static byte[] getBytesFromBitmap(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	public static File getFileFromBytes(byte[] btyes, String outputFile) {
		File file = null;
		BufferedOutputStream stream = null;
		FileOutputStream fstream = null;
		try {
			file = new File(outputFile);
			fstream = new FileOutputStream(file);

			stream = new BufferedOutputStream(fstream);
			stream.write(btyes);
			stream.close();
			fstream.close();
		} catch (FileNotFoundException e) {
			Log.e("AgentUtils", "getFileFromBytes", e);
		} catch (IOException e) {
			Log.e("AgentUtils", "getFileFromBytes", e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					Log.e("AgentUtils", "getFileFromBytes", e);
				}
			}
			if (fstream != null) {
				try {
					fstream.close();
				} catch (IOException e) {
					Log.e("AgentUtils", "getFileFromBytes", e);
				}
			}
		}
		return file;
	}

	/** Get real path */
	public static String getRealImagePath(Activity activity, Uri uri) {
		if (uri.toString().startsWith("content")) {
			String[] proj = { MediaColumns.DATA };
			Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else if (uri.toString().startsWith("file")) {
			String tempUri = Uri.decode(uri.toString());
			return tempUri.substring(7, tempUri.length());
		} else {
			return "";
		}
	}

	public static Bitmap decodeBitmap(String bitmapPath) {
		return BitmapFactory.decodeFile(bitmapPath);
	}

	/** Get file's extension name */
	public static String getExtensionName(String fileName) {
		if (fileName != null) {
			int index = fileName.lastIndexOf(".");
			if (index >= 0) {
				return fileName.substring(index + 1);
			}
			return "";
		}
		return null;
	}

	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

}