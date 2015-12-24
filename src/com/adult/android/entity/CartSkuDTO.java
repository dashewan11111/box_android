package com.adult.android.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhengqiang.shi 2015年12月19日 下午5:28:51
 */
public class CartSkuDTO implements Serializable {

	private static final long serialVersionUID = -1937916179370032983L;

	// 商品信息
	private Merchandise merchandise;

	// 数量
	private int qty;

	// 小计
	private BigDecimal subTotal;

	public Merchandise getMerchandise() {
		return merchandise;
	}

	public void setMerchandise(Merchandise merchandise) {
		this.merchandise = merchandise;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

}
