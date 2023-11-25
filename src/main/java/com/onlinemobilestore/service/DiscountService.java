package com.onlinemobilestore.service;

import com.onlinemobilestore.entity.Discount;
import com.onlinemobilestore.entity.OrderDetail;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiscountService {
    Discount findDiscountByName(String name);

}
