package com.adult.android.entity;

import java.util.List;


public class CouponResponse2 extends BaseEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4944871851994296483L;
	private List<CouponDto> couponList;

	public List<CouponDto> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<CouponDto> couponList) {
		this.couponList = couponList;
	}
}
