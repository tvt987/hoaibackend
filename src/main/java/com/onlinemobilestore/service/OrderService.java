package com.onlinemobilestore.service;

import com.onlinemobilestore.dto.ProductInOrderDTO;
import org.springframework.stereotype.Component;

@Component
public interface OrderService {
    ProductInOrderDTO createOrderAndOrderDetailByUserIdAndByProductId(int userId, int productId);
}
