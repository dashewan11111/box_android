package com.adult.android.model;

import com.adult.android.entity.BaseEntity;

public class CommitOrderResponse2 extends BaseEntity {

	private String orderId;

	private String cartCount;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCartCount() {
		return cartCount;
	}

	public void setCartCount(String cartCount) {
		this.cartCount = cartCount;
	}

}
