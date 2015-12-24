package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class OnConnectUserResponse extends StatusInfo {

	private OnConnectUserResponse2 data;

	public OnConnectUserResponse2 getData() {
		return data;
	}

	public void setData(OnConnectUserResponse2 data) {
		this.data = data;
	}
}
