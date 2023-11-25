package com.onlinemobilestore.service.serviceImpl;

import com.onlinemobilestore.entity.*;
import com.onlinemobilestore.repository.*;
import com.onlinemobilestore.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    DiscountServiceImpl discountService;
    private OrderDetailRepository orderDetailRepository;
    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }


    @Override
    public List<OrderDetail> getDiscountByNameAndOrderId(String name,int id) {
        Discount discount = discountService.findDiscountByName(name);
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(id);
        orderDetails.stream().forEach(orderDetail -> {
            if(orderDetail.getProduct().getId() == discount.getProduct().getId()){
                double discountedPrice = orderDetail.getPrice() * (1 - (discount.getPercent() / 100.0));
                orderDetail.setPrice(discountedPrice);
            }
        });

        return orderDetails;
    }
}
