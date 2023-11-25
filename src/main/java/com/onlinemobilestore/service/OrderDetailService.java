package com.onlinemobilestore.service;

import com.onlinemobilestore.entity.OrderDetail;
import com.onlinemobilestore.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderDetailService {
    List<OrderDetail> getDiscountByNameAndOrderId(String nameDiscount, int orderID);

}
