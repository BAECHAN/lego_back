package com.example.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderVO {
    private int order_group_id;             // 주문번호
    private int state;                      // 주문상태
    private Date date_registed;             // 주문일자
    private int pay_price;                  // 상품별 결제액
    private int order_quantity;             // 상품별 주문 개수
    private int product_id;                 // 상품 고유번호
    private int product_number;             // 상품 검색번호
    private String title;                   // 상품 이름
    private String image;                   // 상품 대표 이미지
    private String recipient;               // 수령인
    private String tel_number;              // 수령인 전화번호
    private String shipping_address1;       // 주소
    private String shipping_address2;       // 상세주소
    private String delivery_request;        // 배송요청사항
    private String delivery_request_direct; // 배송요청사항 - 직접입력
    private int total_price;                // 전체 결제액
    private int product_count;              // 주문한 상품 항목 수
}
