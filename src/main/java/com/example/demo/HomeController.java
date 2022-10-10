package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins="*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    HomeService service;

    @GetMapping("/getThemeList")
    public ResponseEntity getThemeList() throws Exception {

        List<ThemeVO> themeList = service.selectListTheme();
        for(int i=0;i<themeList.size();i++) {
            System.err.println(themeList.get(i).getTheme_id());
            System.err.println(themeList.get(i).getTheme_title());
        }
        return new ResponseEntity(themeList, HttpStatus.OK);
    }

    @GetMapping("/getProductList")
    public ResponseEntity getProductList(@RequestParam(value= "theme_id", required=true) int theme_id) throws Exception {

        System.err.println(theme_id);
        List<ProductVO> productList = service.selectListProduct(theme_id);
        for(int i=0;i<productList.size();i++) {
            System.err.println(productList.get(i).getProduct_id());
            System.err.println(productList.get(i).getTitle());
        }
        return new ResponseEntity(productList, HttpStatus.OK);
    }

    @GetMapping("/getProductInfo")
    public ResponseEntity getProductInfo(@RequestParam(value= "product_number", required=true) int product_number) throws Exception {

        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        ProductVO productInfo = service.selectProductInfo(product_number);

        String[] dtl_img_list;
        dtl_img_list = productInfo.getDtl_img_list().split(",");

        resultMap.put("productInfo",productInfo);
        resultMap.put("dtl_img_list",dtl_img_list);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }
}
