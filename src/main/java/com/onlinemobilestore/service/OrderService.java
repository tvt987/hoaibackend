package com.onlinemobilestore.service;

import com.onlinemobilestore.dto.OrderForUserDTO;
import com.onlinemobilestore.dto.ProductInOrderDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderService {
    ProductInOrderDTO createOrderAndOrderDetailByUserIdAndByProductId(int userId, int productId);
     List<OrderForUserDTO> getOrdersForUser(int userId);
     List<OrderForUserDTO> deleteOrderStateIs(int orderId, int userId);
}
