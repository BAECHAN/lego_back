package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins="*", allowedHeaders = "*")
@RestController
public class HomeController {

    @Autowired
    HomeService service;

    @GetMapping("/index")
    public ResponseEntity home() throws Exception {

        List<HomeVO> resultList = service.selectList();
        for(int i=0;i<resultList.size();i++) {
            System.err.println(resultList.get(i).getTheme_id());
            System.err.println(resultList.get(i).getTheme_title());
        }
        return new ResponseEntity(resultList, HttpStatus.OK);

    }
}
