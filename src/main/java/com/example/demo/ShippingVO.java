package com.example.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ShippingVO {

    private int shipping_id;
    private String email;
    private String recipient;
    private String shipping_name;
    private String tel_number;
    private String shipping_zipcode;
    private String shipping_address1;
    private String shipping_address2;
    private int shipping_default;
    private String delivery_request;
    private String delivery_request_direct;
    private int state;
}
