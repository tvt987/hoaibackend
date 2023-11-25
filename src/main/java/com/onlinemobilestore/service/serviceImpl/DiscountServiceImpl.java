package com.onlinemobilestore.service.serviceImpl;

import com.onlinemobilestore.entity.Discount;
import com.onlinemobilestore.entity.OrderDetail;
import com.onlinemobilestore.repository.DiscountRepository;
import com.onlinemobilestore.repository.OrderDetailRepository;
import com.onlinemobilestore.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    private DiscountRepository discountRepository;
    public DiscountServiceImpl(DiscountRepository discountRepository){
        this.discountRepository = discountRepository;
    }

    @Override
    public Discount findDiscountByName(String name) {
        Discount discount =  discountRepository.findDiscountByName(name);
        if(discount.isActive() == true && discount !=null){
            return discount;
        }
        return null;
    }


}
