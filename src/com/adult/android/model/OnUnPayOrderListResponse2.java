package com.adult.android.model;

import java.util.List;

import com.adult.android.entity.BaseEntity;
import com.adult.android.entity.UserOrdersDTO;

public class OnUnPayOrderListResponse2 extends BaseEntity {

	private List<UserOrdersDTO> UserOrders;

	public List<UserOrdersDTO> getUserOrders() {
		return UserOrders;
	}

	public void setUserOrders(List<UserOrdersDTO> userOrders) {
		UserOrders = userOrders;
	}

}
