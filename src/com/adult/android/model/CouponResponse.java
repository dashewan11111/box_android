package com.adult.android.model;

import java.util.List;

import com.adult.android.entity.CouponDto;
import com.adult.android.model.internet.bean.StatusInfo;

public class CouponResponse extends StatusInfo {

	private List<CouponDto> data;

	public List<CouponDto> getData() {
		return data;
	}

	public void setData(List<CouponDto> data) {
		this.data = data;
	}

}
