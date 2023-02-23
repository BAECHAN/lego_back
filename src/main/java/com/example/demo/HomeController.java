package com.example.demo;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;

@CrossOrigin(origins="*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    HomeService service;

    @GetMapping("/theme-list")
    public ResponseEntity getThemeList() throws Exception {

        List<ThemeVO> themeList = service.selectListTheme();
        for(int i=0;i<themeList.size();i++) {
            System.err.println(themeList.get(i).getTheme_id());
            System.err.println(themeList.get(i).getTheme_title());
        }
        return new ResponseEntity(themeList, HttpStatus.OK);
    }

    @PostMapping("/product-list")
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

    @GetMapping("/product-filter")
    public ResponseEntity getProductFilter(@RequestParam(value= "theme_id", required=true) int theme_id) throws Exception {

        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        List<HashMap> productFilter = service.selectListProductFilter(theme_id);

        resultMap.put("productFilter",productFilter);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }


    @GetMapping("/product-info")
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

    @GetMapping("/email-chk")
    public ResponseEntity getEmailChk(@RequestParam HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        int isResult = service.selectEmailChk(paramMap);
        resultMap.put("result",isResult);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/create-account")
    public ResponseEntity createAccount(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        System.out.println("paramMap = " + paramMap);

        int isResult = service.insertAccount(paramMap);

        System.out.println(isResult);
        if(isResult == 1){
            resultMap.put("result",isResult);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/login-chk")
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

    @GetMapping("/theme-by-product")
    public ResponseEntity getThemeByProduct(@RequestParam(value= "product_number", required=true) int product_number) throws Exception {
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        ThemeVO themeInfo = service.selectThemeByProduct(product_number);
        if(themeInfo != null){
            resultMap.put("result",themeInfo);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/product-viewed-list")
    public ResponseEntity getProductViewedList(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        System.err.println(paramMap);
        ArrayList<Integer> product_number_arr = (ArrayList<Integer>) paramMap.get("product_number_arr");

        System.err.println(product_number_arr);

        List<ProductVO> productList = service.selectListViewedProduct(product_number_arr);

        System.err.println(productList);
        System.err.println(productList.size());

        resultMap.put("productList",productList);
        resultMap.put("productListCount",productList.size());

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/product-wish-list")
    public ResponseEntity getProductWishList(@RequestParam(value= "page", required=true) int page,
                                             @RequestParam(value= "email", required = true) String email) throws Exception {

        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        List<HashMap> wishList = service.selectListWishedProduct(page, email);

        resultMap.put("wishList",wishList);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/add-wish")
    public ResponseEntity addWish(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.insertAddWish(paramMap);

        if(result == 1){
            resultMap.put("product_id",paramMap.get("product_id").toString());
            resultMap.put("wish",true);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PatchMapping("/del-wish")
    public ResponseEntity delWish(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.updateDelWish(paramMap);

        if(result == 1){
            resultMap.put("product_id",paramMap.get("product_id"));
            resultMap.put("wish",false);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/get-token")
    public ResponseEntity gettingToken(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int randomStrLen = 20;
        Random random = new Random();
        StringBuffer randomBuf = new StringBuffer();
        for (int i = 0; i < randomStrLen; i++) {
            // Random.nextBoolean() : 랜덤으로 true, false 리턴 (true : 랜덤 소문자 영어, false : 랜덤 숫자)
            if (random.nextBoolean()) {
                // 26 : a-z 알파벳 개수
                // 97 : letter 'a' 아스키코드
                // (int)(random.nextInt(26)) + 97 : 랜덤 소문자 아스키코드
                randomBuf.append((char)((int)(random.nextInt(26)) + 97));
            } else {
                randomBuf.append(random.nextInt(10));
            }
        }
        String randomStr = randomBuf.toString();
        System.out.println("[createRandomStrUsingRandomBoolean] randomStr : " + randomStr);
        // [createRandomStrUsingRandomBoolean] randomStr : iok887yt6sa31m99e4d6

        paramMap.put("token",randomStr);
        int result  = service.createToken(paramMap);

        if(result > 0){
            resultMap.put("token", randomStr);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/token-chk")
    public ResponseEntity passwordTokenCheck(@RequestParam HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        TokenVO tokenResult = service.selectTokenChk(paramMap);

        resultMap.put("result",tokenResult);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/update-password")
    public ResponseEntity updatePassword(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        System.out.println("paramMap = " + paramMap);

        int isResult = service.updatePassword(paramMap);

        System.out.println(isResult);
        if(isResult > 0){
            resultMap.put("result",isResult);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }
}
