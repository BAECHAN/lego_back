package com.example.demo;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/getProductList")
    public ResponseEntity getProductList(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int theme_id = Integer.parseInt(paramMap.get("theme_id").toString());
        int page = Integer.parseInt(paramMap.get("page").toString());
        int take = Integer.parseInt(paramMap.get("take").toString());
        String sort = paramMap.get("sort").toString();
        HashMap<String,Object> filter = (HashMap<String, Object>) paramMap.get("filter");

        int offset = page * take;

        System.err.println(theme_id);
        System.err.println(page);
        System.err.println(take);
        System.err.println(sort);
        System.err.println(filter);

        List<ProductVO> productList = service.selectListProduct(theme_id,offset,take,sort,filter);
        int productListCount = service.selectListProductCount(theme_id,filter);

        if(productList.size() < take){
           resultMap.put("isLast",true);
        }

        resultMap.put("productList",productList);
        resultMap.put("productListCount",productListCount);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/getProductFilter")
    public ResponseEntity getProductFilter(@RequestParam(value= "theme_id", required=true) int theme_id) throws Exception {

        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        List<HashMap> productFilter = service.selectListProductFilter(theme_id);

        resultMap.put("productFilter",productFilter);

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

    @GetMapping("/getEmailChk")
    public ResponseEntity getEmailChk(@RequestParam HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        int isResult = service.selectEmailChk(paramMap);
        resultMap.put("result",isResult);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/createAccount")
    public ResponseEntity createAccount(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        System.out.println("paramMap = " + paramMap);

        int isResult = service.createAccount(paramMap);

        System.out.println(isResult);
        if(isResult == 1){
            resultMap.put("result",isResult);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/getLoginChk")
    public ResponseEntity getLoginChk(@RequestParam HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        System.out.println("파람 : "+ paramMap);

        UserVO userInfo = service.selectUserInfo(paramMap);

        if(userInfo != null){
            resultMap.put("result",userInfo);
        }
        System.out.println(resultMap.get("result"));
        System.out.println(userInfo.getDate_created());

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/getThemeByProduct")
    public ResponseEntity getThemeByProduct(@RequestParam(value= "product_number", required=true) int product_number) throws Exception {
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        ThemeVO themeInfo = service.selectThemeByProduct(product_number);
        if(themeInfo != null){
            resultMap.put("result",themeInfo);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/getProductViewedList")
    public ResponseEntity getProductViewedList(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();


        ArrayList<Integer> product_number_arr = (ArrayList<Integer>) paramMap.get("product_number_arr");

        System.err.println(paramMap);
        System.err.println(product_number_arr);

        List<ProductVO> productList = service.selectListViewedProduct(product_number_arr);

        System.err.println(productList);
        System.err.println(productList.size());

        resultMap.put("productList",productList);
        resultMap.put("productListCount",productList.size());

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }
}
