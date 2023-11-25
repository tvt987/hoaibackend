package com.onlinemobilestore.API;

import com.onlinemobilestore.dto.OrderForUserDTO;
import com.onlinemobilestore.service.serviceImpl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/api")
public class UserOrderAPI {
    @Autowired
    OrderServiceImpl orderService;
    @DeleteMapping("/order/{orderId}/{userId}")
    public ResponseEntity<List<OrderForUserDTO>> deleteOrder(@PathVariable("userId") int userId,
                                                             @PathVariable("orderId") int orderId){
        return ResponseEntity.ok(orderService.deleteOrderStateIs(orderId,userId));

    }

}
