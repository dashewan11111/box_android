package com.adult.android.entity;

import java.util.List;


public class CartProductListResponse2 extends BaseEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6714863824489035240L;

	private List<ProductForCart> productList;

	private String cartCount;

	private String totalAmount;

	public List<ProductForCart> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductForCart> productList) {
		this.productList = productList;
	}

	public String getCartCount() {
		return cartCount;
	}

	public void setCartCount(String cartCount) {
		this.cartCount = cartCount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}
