package com.onlinemobilestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInOrderDTO {
    private int id;
    private int productId;
    private Date createDate;
    private String nameProduct;
    private double price;
    private List<String> images;

}