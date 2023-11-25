package com.onlinemobilestore.API;

import com.onlinemobilestore.entity.Discount;
import com.onlinemobilestore.entity.OrderDetail;
import com.onlinemobilestore.repository.DiscountRepository;
import com.onlinemobilestore.repository.OrderDetailRepository;
import com.onlinemobilestore.service.serviceImpl.DiscountServiceImpl;
import com.onlinemobilestore.service.serviceImpl.OrderDetailServiceImpl;
import com.onlinemobilestore.service.serviceImpl.OrderServiceImpl;
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
    OrderDetailServiceImpl orderDetailService;
    @Autowired
    OrderServiceImpl orderService;
    @GetMapping("/discount/{name}/{orderId}")
    public ResponseEntity<List<OrderDetail>> getDiscount(@PathVariable("name") String name, @PathVariable("orderId") int orderId) {
        return ResponseEntity.ok(orderDetailService.getDiscountByNameAndOrderId(name,orderId));
    }

    @PostMapping("/order/{userId}/{productId}")
    public ResponseEntity<?> createOrder(@PathVariable("userId") int userId,
                                         @PathVariable("productId") int productId){
        return ResponseEntity.ok(orderService.createOrderAndOrderDetailByUserIdAndByProductId(userId,productId));

    }



}
