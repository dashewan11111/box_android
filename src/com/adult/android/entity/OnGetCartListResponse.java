package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class OnGetCartListResponse extends StatusInfo {

	private OnGetCartListResponse2 data;

	public OnGetCartListResponse2 getData() {
		return data;
	}

	public void setData(OnGetCartListResponse2 data) {
		this.data = data;
	}

}
