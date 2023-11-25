package com.onlinemobilestore.service;

import com.onlinemobilestore.dto.OrderForUserDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderService {
    List<OrderForUserDTO> getOrdersForUser(int userId);
}
