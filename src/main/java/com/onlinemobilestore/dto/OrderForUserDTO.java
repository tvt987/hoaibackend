package com.onlinemobilestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class OrderForUserDTO {
    private int id;
    private Double total;
    private int quantity;
    private boolean state;
    private Date createDate;
}