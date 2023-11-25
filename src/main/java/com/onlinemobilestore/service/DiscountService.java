package com.onlinemobilestore.service;

import com.onlinemobilestore.entity.Discount;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiscountService {
    Discount findDiscountByName(String name);

    List<Discount> findAll();

    Discount createDiscount(Discount discount);

    Discount updateDiscount(int discountId, Discount discount);

    String deleteDiscount(int discountId);
}
