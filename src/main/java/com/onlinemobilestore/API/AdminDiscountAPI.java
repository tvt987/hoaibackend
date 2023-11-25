package com.onlinemobilestore.API;

import com.onlinemobilestore.entity.Discount;
import com.onlinemobilestore.service.serviceImpl.DiscountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/admin")
public class AdminDiscountAPI {
    @Autowired
    DiscountServiceImpl discountService;

    @GetMapping("/discounts")
    public ResponseEntity<List<Discount>> getDiscounts(){
        return ResponseEntity.ok(discountService.findAll());
    }

    @PostMapping("/create-discount")
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount) {
        try {
            return ResponseEntity.ok(discountService.createDiscount(discount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(discountService.createDiscount( discount));
        }
    }
    @PostMapping("/update-discount/{discountId}")
    public ResponseEntity<Discount> updateDiscount(@PathVariable("discountId") int discountId, @RequestBody Discount discount){
        try {
            return ResponseEntity.ok(discountService.updateDiscount(discountId, discount));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(discountService.updateDiscount(discountId, discount));
        }
    }
    @DeleteMapping("/delete-discount/{discountId}")
    public ResponseEntity<String> deleteDiscount(@PathVariable("discountId") int discountId){
        try {
            return ResponseEntity.ok(discountService.deleteDiscount(discountId));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }
    }


}
