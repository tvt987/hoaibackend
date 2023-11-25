package com.onlinemobilestore.service.serviceImpl;

import com.onlinemobilestore.entity.Discount;
import com.onlinemobilestore.repository.DiscountRepository;
import com.onlinemobilestore.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {
    private DiscountRepository discountRepository;


    @Override
    public Discount findDiscountByName(String name) {
        Discount discount =  discountRepository.findDiscountByName(name);
        if(discount.isActive() == true && discount !=null){
            return discount;
        }
        return null;
    }
    public DiscountServiceImpl(DiscountRepository discountRepository){
        this.discountRepository = discountRepository;
    }
}
