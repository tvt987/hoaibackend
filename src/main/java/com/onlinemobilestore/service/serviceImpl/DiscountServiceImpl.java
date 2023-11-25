package com.onlinemobilestore.service.serviceImpl;

import com.onlinemobilestore.entity.Discount;
import com.onlinemobilestore.entity.OrderDetail;
import com.onlinemobilestore.repository.DiscountRepository;
import com.onlinemobilestore.repository.OrderDetailRepository;
import com.onlinemobilestore.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Discount> findAll() {
        try {
            return discountRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Discount createDiscount(Discount discount) {
        try {
            discount.setId(null);
            return  discountRepository.save(discount);
        } catch (DataIntegrityViolationException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Discount updateDiscount(int discountId, Discount discount) {
        Optional<Discount> optionalDiscount = discountRepository.findById(discountId);
        if(optionalDiscount.isPresent()){

            return  discountRepository.save(discount);
        }else {
            return null;
        }
    }

    @Override
    public String deleteDiscount(int discountId) {
        Optional<Discount> optionalDiscount = discountRepository.findById(discountId);
        if(optionalDiscount.isPresent()){
            discountRepository.deleteById(discountId);
            return "delete success";
        }else {
            return "delete fail";
        }

    }


}
