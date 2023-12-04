package com.onlinemobilestore.service;

import com.onlinemobilestore.dto.OrderForUserDTO;
import com.onlinemobilestore.dto.ProductInOrderDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderService {
    List<OrderForUserDTO> getOrdersForUser(int userId);
    ProductInOrderDTO createOrderAndOrderDetailByUserIdAndByProductId(int userId, int productId);
}
