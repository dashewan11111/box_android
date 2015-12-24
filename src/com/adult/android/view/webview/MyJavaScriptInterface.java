package com.adult.android.view.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class MyJavaScriptInterface {

	private Context context;

	public MyJavaScriptInterface(Context context) {
		this.context = context;
	}

	@JavascriptInterface
	public void jumpToProductDetails(String json) {

	}

}
