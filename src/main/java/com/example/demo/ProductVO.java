package com.example.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductVO {
    private int product_id;
    private String title;
    private String image;
    private int price;
    private int pieces;
    private int ages;
    private int product_number;
    private Date date_released;
    private int sale_enabled;
    private int discounting;
    private int rate_discount;
    private int ea;
    private String dtl_img_list;
    private int order_quantity;
}
