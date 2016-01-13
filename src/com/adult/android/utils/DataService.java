package com.adult.android.utils;

import java.io.File;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import android.util.Log;

import com.adult.android.model.CommunityModel.OnSuccessListner;

public class DataService {
	/**
	 * 批量上传图片
	 * 
	 * @param path
	 *            服务器地址
	 * @param name
	 *            用户名
	 * @param filePath
	 *            sd卡图片路径
	 * @param onSuccessListner
	 * @return
	 * @throws Exception
	 */
	public static String sendDataByHttpClientPost(String path,
			List<File> files, List<Part> mparameters, OnSuccessListner listner)
			throws Exception {

		for (int i = 0; i < files.size(); i++) {
			mparameters.add(new FilePart("file", files.get(i)));
		}
		Part[] parts = mparameters.toArray(new Part[0]);

		PostMethod filePost = new PostMethod(path);
		filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost
				.getParams()));
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		int status = client.executeMethod(filePost);
		if (status == 200) {
			Log.e("DataService", "" + filePost.getResponseCharSet());
			String result = new String(filePost.getResponseBodyAsString());
			Log.e("DataService", "--" + result);
			// JSONArray array = (JSONArray) JSON.parse(result);
			Log.e("JSONArray", "--" + result);
			listner.onSuccess(result);
			return result;
		} else {
			listner.onFailed();
			return null;
		}
	}

	/**
	 * 单次上传图片or文件
	 * 
	 * @param path
	 * @param name
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String sendDataByHttpClientPost(String path, String name,
			String filePath) throws Exception {

		/*
		 * List<Part> partList = new ArrayList<Part>(); partList.add(new
		 * StringPart("user", name));
		 * 
		 * for (int i = 0; i < 4; i++) { partList.add(new FilePart(name,
		 * FilePart())); }
		 */

		// 实例化上传数据的数组
		Part[] parts = { new StringPart("user", name),
				new FilePart("file", new File(filePath)) };

		PostMethod filePost = new PostMethod(path);

		filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost
				.getParams()));

		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		int status = client.executeMethod(filePost);
		Log.e("Other", "返回结果:" + status);
		if (status == 200) {

			System.out.println(filePost.getResponseCharSet());
			String result = new String(filePost.getResponseBodyAsString());

			System.out.println("--" + result);
			return result;
		} else {
			throw new IllegalStateException("服务器状态异常");
		}

	}
}
