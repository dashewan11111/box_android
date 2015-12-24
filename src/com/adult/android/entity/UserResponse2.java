package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class UserResponse2 extends StatusInfo {

	private UserResponse3 data;

	public UserResponse3 getData() {
		return data;
	}

	public void setData(UserResponse3 data) {
		this.data = data;
	}

}
