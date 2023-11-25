package com.onlinemobilestore.service;

import com.onlinemobilestore.entity.Discount;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@Component
@CrossOrigin
public interface DiscountService {
    Discount findDiscountByName(String name);
}
