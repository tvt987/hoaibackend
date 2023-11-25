package com.onlinemobilestore.API;

import com.onlinemobilestore.entity.Discount;
import com.onlinemobilestore.entity.OrderDetail;
import com.onlinemobilestore.repository.DiscountRepository;
import com.onlinemobilestore.repository.OrderDetailRepository;
import com.onlinemobilestore.service.serviceImpl.DiscountServiceImpl;
import com.onlinemobilestore.service.serviceImpl.ProductServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(value = "*")
public class UserDiscountAPI {
    @Autowired
    DiscountServiceImpl discountService;
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @GetMapping("/discount/{name}/{orderId}")
    public ResponseEntity<List<OrderDetail>> getDiscount(@PathVariable("name") String name,
                                                         @PathVariable("orderId") int orderId) {
        Discount discount = discountService.findDiscountByName(name);
        if (discount == null) {
            return ResponseEntity.notFound().build();
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(orderId);
        orderDetails.forEach(orderDetail -> {
            if (orderDetail.getProduct().getId() == discount.getProduct().getId()) {
                double discountedPrice = orderDetail.getPrice() * (1 - (discount.getPercent() / 100.0));
                orderDetail.setPrice(discountedPrice);
                orderDetailRepository.save(orderDetail);
            }
        });
        return ResponseEntity.ok(orderDetails);
    }


}