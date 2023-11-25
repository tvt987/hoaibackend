package com.onlinemobilestore.service.serviceImpl;

import com.onlinemobilestore.dto.OrderForUserDTO;
import com.onlinemobilestore.entity.Order;
import com.onlinemobilestore.entity.OrderDetail;
import com.onlinemobilestore.repository.OrderRepository;
import com.onlinemobilestore.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public List<OrderForUserDTO> getOrdersForUser(int userId) {
        try {
            List<Order> orders = orderRepository.findByUserId(userId);
            int totalProducts = orders.stream()
                    .flatMap(order -> order.getOrderDetails().stream())
                    .mapToInt(OrderDetail::getQuantity)
                    .sum();
            List<OrderForUserDTO> orderForUser = orders.stream()
                    .map(order -> new OrderForUserDTO(order.getId(), order.getTotal(), totalProducts, order.isState(), order.getCreateDate()))
                    .collect(Collectors.toList());
            return orderForUser;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    private OrderRepository orderRepository;
    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
}
