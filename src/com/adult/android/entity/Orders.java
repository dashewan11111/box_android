package com.adult.android.entity;

import java.util.Date;

public class Orders {
    private String orderId;

    private String orderNumber;

    private String shopName;

    private String orderRemark;

    private Double orderPrice;

    private Double orderDiscountPrice;

    private Double orderFreightPrice;

    private Double orderFreightDiscountPrice;
    
    private Double orderDomisticDiscountPrice;

    private Integer orderStatus;

    private Date orderDateTime;

    private String orderBarCode;

    private String orderShippingFeeBarCode;

    private String shopId;

    private String userId;

    private String shopperId;
    
    private Integer totalQty;


	public Double getOrderDomisticDiscountPrice() {
		return orderDomisticDiscountPrice;
	}

	public void setOrderDomisticDiscountPrice(Double orderDomisticDiscountPrice) {
		this.orderDomisticDiscountPrice = orderDomisticDiscountPrice;
	}

	public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark == null ? null : orderRemark.trim();
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Double getOrderDiscountPrice() {
        return orderDiscountPrice;
    }

    public void setOrderDiscountPrice(Double orderDiscountPrice) {
        this.orderDiscountPrice = orderDiscountPrice;
    }

    public Double getOrderFreightPrice() {
        return orderFreightPrice;
    }

    public void setOrderFreightPrice(Double orderFreightPrice) {
        this.orderFreightPrice = orderFreightPrice;
    }

    public Double getOrderFreightDiscountPrice() {
        return orderFreightDiscountPrice;
    }

    public void setOrderFreightDiscountPrice(Double orderFreightDiscountPrice) {
        this.orderFreightDiscountPrice = orderFreightDiscountPrice;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(Date orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getOrderBarCode() {
        return orderBarCode;
    }

    public void setOrderBarCode(String orderBarCode) {
        this.orderBarCode = orderBarCode == null ? null : orderBarCode.trim();
    }

    public String getOrderShippingFeeBarCode() {
        return orderShippingFeeBarCode;
    }

    public void setOrderShippingFeeBarCode(String orderShippingFeeBarCode) {
        this.orderShippingFeeBarCode = orderShippingFeeBarCode == null ? null : orderShippingFeeBarCode.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getShopperId() {
        return shopperId;
    }

    public void setShopperId(String shopperId) {
        this.shopperId = shopperId == null ? null : shopperId.trim();
    }

	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}
}