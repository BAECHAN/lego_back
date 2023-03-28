package com.example.demo;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class UserVO {
    private int id;
    private String email;
    private String name;
    private String image;
    private Date date_created;

    private int account_state;

    private Date date_withdraw;
    private String grade;
}
