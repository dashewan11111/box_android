package com.adult.android.entity;

public class ShopPromotion extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5511529201757262119L;

	private String shopPromotionMapping;

    private Integer promotionType;

    private Double orderPrice;

    private Double discount;

    private Integer couponType;

    private Double couponPrice;

    private Double usePrice;

    private String shopId;

    public String getShopPromotionMapping() {
        return shopPromotionMapping;
    }

    public void setShopPromotionMapping(String shopPromotionMapping) {
        this.shopPromotionMapping = shopPromotionMapping == null ? null : shopPromotionMapping.trim();
    }

    public Integer getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(Integer promotionType) {
        this.promotionType = promotionType;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(Double couponPrice) {
        this.couponPrice = couponPrice;
    }

    public Double getUsePrice() {
        return usePrice;
    }

    public void setUsePrice(Double usePrice) {
        this.usePrice = usePrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }
}