package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity getProductList(
            @RequestParam(value= "theme_id", required=true) int theme_id,
            @RequestParam(value= "page", required=true) int page,
            @RequestParam(value= "take", required=true) int take

    ) throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();
        int offset = page * take;

        List<ProductVO> productList = service.selectListProduct(theme_id,offset,take);
        if(productList.size() < take){
           resultMap.put("isLast",true);
        }

        resultMap.put("productList",productList);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/getProductInfo")
    public ResponseEntity getProductInfo(@RequestParam(value= "product_number", required=true) int product_number) throws Exception {

        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        ProductVO productInfo = service.selectProductInfo(product_number);

        String[] dtl_img_list;
        if(productInfo.getDtl_img_list() != null){
            dtl_img_list = productInfo.getDtl_img_list().split(",");
            resultMap.put("product_img_list",dtl_img_list);
        }

        resultMap.put("product_info",productInfo);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }
}
