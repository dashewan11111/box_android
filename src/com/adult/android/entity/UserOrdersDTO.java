package com.adult.android.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sunqi
 */
public class UserOrdersDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -357941727950700282L;

	private Integer orderStatus;
	
	private String userId;
	
	private String orderId;
	
	private String orderNumber;
	
	private String shopName;
	
	private String shopId;
	
	private BigDecimal orderPrice;
	
	private Date orderDateTime;
	
	private String shopIcon;
	
	private String merchandiseCurrency;
	
	private BigDecimal orderDiscountPrice;

    private BigDecimal orderFreightPrice;

    private BigDecimal orderFreightDiscountPrice;
    
    private BigDecimal orderDomisticDiscountPrice;
    
    private Integer totalQty;
    
    private Integer boxNum =1;
    
    private BigDecimal oldOrderPrice;
    
    private BigDecimal oldFreightPrice;
    
    
	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}


	public BigDecimal getOrderDomisticDiscountPrice() {
		return orderDomisticDiscountPrice;
	}

	public void setOrderDomisticDiscountPrice(BigDecimal orderDomisticDiscountPrice) {
		this.orderDomisticDiscountPrice = orderDomisticDiscountPrice;
	}

	public BigDecimal getOldOrderPrice() {
		return oldOrderPrice;
	}

	public void setOldOrderPrice(BigDecimal oldOrderPrice) {
		this.oldOrderPrice = oldOrderPrice;
	}

	public BigDecimal getOldFreightPrice() {
		return oldFreightPrice;
	}

	public void setOldFreightPrice(BigDecimal oldFreightPrice) {
		this.oldFreightPrice = oldFreightPrice;
	}

	public BigDecimal getOrderDiscountPrice() {
		return orderDiscountPrice;
	}

	public void setOrderDiscountPrice(BigDecimal orderDiscountPrice) {
		this.orderDiscountPrice = orderDiscountPrice;
	}

	public BigDecimal getOrderFreightPrice() {
		return orderFreightPrice;
	}

	public void setOrderFreightPrice(BigDecimal orderFreightPrice) {
		this.orderFreightPrice = orderFreightPrice;
	}

	public BigDecimal getOrderFreightDiscountPrice() {
		return orderFreightDiscountPrice;
	}

	public void setOrderFreightDiscountPrice(BigDecimal orderFreightDiscountPrice) {
		this.orderFreightDiscountPrice = orderFreightDiscountPrice;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Date getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(Date orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getShopIcon() {
		return shopIcon;
	}

	public void setShopIcon(String shopIcon) {
		this.shopIcon = shopIcon;
	}

	public String getMerchandiseCurrency() {
		return merchandiseCurrency;
	}

	public void setMerchandiseCurrency(String merchandiseCurrency) {
		this.merchandiseCurrency = merchandiseCurrency;
	}

	public Integer getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}
	
	
	
	
	
}
