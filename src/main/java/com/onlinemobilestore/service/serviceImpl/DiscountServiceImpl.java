package com.onlinemobilestore.service.serviceImpl;

import com.onlinemobilestore.entity.Discount;
import com.onlinemobilestore.repository.DiscountRepository;
import com.onlinemobilestore.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {
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

    @Override
    public List<Discount> findDiscountIdByProductId(int id) {
        List<Discount> discounts = discountRepository.findDiscountIdByProductId(id);
        if(!discounts.isEmpty()){
            return discounts;
        }
        return null;
    }
}
