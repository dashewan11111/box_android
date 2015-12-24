package com.adult.android.entity;

import com.adult.android.model.internet.bean.StatusInfo;

public class UserResponse extends StatusInfo {

	private UserDto data;

	public UserDto getData() {
		return data;
	}

	public void setData(UserDto data) {
		this.data = data;
	}
}
