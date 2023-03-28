package com.example.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TokenVO {
    private String email;
    private String token;
    private Boolean expired;
}
