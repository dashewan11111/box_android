package com.adult.android.entity;

import java.io.Serializable;

/**
 * @author zhengqiang.shi 2015年12月23日 下午5:40:38
 */
public class OrderDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -357941727950700282L;

	private Orders orders;

	private String msg;

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
