package com.example.demo;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        UserVO userInfo = service.selectUserInfo(paramMap);

        if(userInfo != null){
            resultMap.put("result",userInfo.getAccount_state());
        }else{
            resultMap.put("result",0);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/nickname-chk")
    public ResponseEntity getNicknameChk(@RequestParam HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        UserVO userInfo = service.selectNameChk(paramMap);

        if(userInfo != null){
            resultMap.put("result",userInfo.getAccount_state());
        }else{
            resultMap.put("result",0);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/upd-nickname")
    public ResponseEntity updateNicknameAfterCheck(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        UserVO userInfo = service.selectNameChk(paramMap);

        if(userInfo != null){
            resultMap.put("result",userInfo.getAccount_state());
        }else{
            int isUpdate = service.updateUserInfo(paramMap);

            if(isUpdate > 0){
                resultMap.put("result",0);
            }else {
                resultMap.put("result", -1);
            }
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/create-account")
    public ResponseEntity createAccount(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        System.out.println("paramMap = " + paramMap);

        int result = service.insertAccount(paramMap);

        System.out.println(result);
        if(result == 1){
            resultMap.put("result",result);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/login-chk")
    public ResponseEntity getLoginChk(@RequestParam HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        System.out.println("파람 : "+ paramMap);

        UserVO userInfo = service.selectLoginChk(paramMap);

        if(userInfo != null){
            resultMap.put("result",userInfo);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/password-chk")
    public ResponseEntity getPasswordChkReturnToken(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        UserVO userInfo = service.selectLoginChk(paramMap);

        if(userInfo != null){
            String randomStr = "";
            randomStr = service.getToken(paramMap);

            paramMap.put("token",randomStr);
            int result  = service.createToken(paramMap);

            if(result > 0){
                resultMap.put("result",1);
                resultMap.put("token", randomStr);
            }else{
                resultMap.put("result",-1);
            }

        }else{
            resultMap.put("result",0);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/get-user-info")
    public ResponseEntity getUserInfo(@RequestParam HashMap<String,Object> paramMap) throws Exception{
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

        String randomStr = "";

        randomStr = service.getToken(paramMap);

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

    @PatchMapping("/update-password")
    public ResponseEntity updatePassword(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        System.out.println("paramMap = " + paramMap);

        int result = service.updatePassword(paramMap);

        System.out.println(result);
        if(result > 0){
            resultMap.put("result",result);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/add-cart")
    public ResponseEntity addCart(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.insertAddCart(paramMap);

        if(result > 0){
            resultMap.put("result",result);
        }
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/product-cart-list")
    public ResponseEntity getProductCartList(@RequestParam(value= "page", required=true) int page,
                                             @RequestParam(value= "email", required = true) String email) throws Exception {

        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        List<HashMap> cartList = service.selectListCartProduct(page, email);

        resultMap.put("cartList",cartList);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PatchMapping("/del-cart")
    public ResponseEntity delCart(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.updateDelCart(paramMap);

        if(result == 1){
            resultMap.put("result",result);
            resultMap.put("cartId",paramMap.get("cart_id"));
        }else{
            resultMap.put("result",0);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PatchMapping("/upd-cart")
    public ResponseEntity updateCart(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.updateQuantityCart(paramMap);

        if(result == 1){
            resultMap.put("result",result);
            resultMap.put("cartId",paramMap.get("cart_id"));
        }else{
            resultMap.put("result",0);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }
    @PatchMapping("/wakeup-account")
    public ResponseEntity updateWakeupAccount(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.updateWakeupAccount(paramMap);

        resultMap.put("result",result);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PatchMapping("/withdraw-account")
    public ResponseEntity updateWithdrawAccount(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.updateWithdrawAccount(paramMap);

        resultMap.put("result",result);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/manage-shipping")
    public ResponseEntity addShipping(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.manageShipping(paramMap);

        resultMap.put("result", result);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PatchMapping("/del-shipping")
    public ResponseEntity delShipping(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.updateDelShipping(paramMap);

        if(result == 1){
            resultMap.put("result",result);
        }else{
            resultMap.put("result",0);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PatchMapping("/upd-shipping-priority")
    public ResponseEntity updateShippingPriority(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.updateShippingPriority(paramMap);

        if(result == 1){
            resultMap.put("result",result);
        }else{
            resultMap.put("result",0);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/shipping-list")
    public ResponseEntity getShippingList(@RequestParam HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        int page = Integer.parseInt(paramMap.get("page").toString());
        int take = 5;
        int offset = (page-1) * take;

        paramMap.put("take",take);
        paramMap.put("offset",offset);

        List<ShippingVO> shippingList = service.selectListShipping(paramMap);
        int shippingListCount = service.selectListShippingCount(paramMap);

        if(shippingList.size() < take || shippingListCount == take * page){
            resultMap.put("isLastPage",true);
        }else{
            resultMap.put("isLastPage",false);
        }

        resultMap.put("shippingList",shippingList);
        resultMap.put("shippingListCount",shippingListCount);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/upd-user-image")
    public ResponseEntity updateUserImage(@RequestPart(value="image", required = false) MultipartFile uploadFile,
                                          @RequestPart(value="email", required = true) String email,
                                          @RequestPart(value="isDefault", required = true) String isDefault)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = 0;

        if(uploadFile != null && "0".equals(isDefault)){
            result = service.saveFile(uploadFile, email);
        }else{
            result = service.updateDefaultImage(email);
        }

        resultMap.put("result", result);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/add-order")
    public ResponseEntity addOrder(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.saveOrder(paramMap);

        resultMap.put("result",result);

        if(result != 1){
            System.err.println("결제 데이터 반입 에러 번호 : " + result);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/order-list")
    public ResponseEntity getOrderList(@RequestParam HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        String email = paramMap.get("email").toString();
        int page = Integer.parseInt(paramMap.get("page").toString());
        int take = Integer.parseInt(paramMap.get("take").toString());

        int offset = page * take;

        List<OrderVO> orderList = service.selectListOrderItems(email,offset,take);
        int orderListCount = service.selectCountOrderItems(email);

        if(orderList.size() < take){
            resultMap.put("isLast",true);
        }

        resultMap.put("orderList",orderList);
        resultMap.put("orderListCount",orderListCount);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PatchMapping("/upd-order-refund")
    public ResponseEntity updateOrderRefund(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();



        int result = service.orderRefund(paramMap);

        if(result > 0){
            resultMap.put("result",result);
        }else{
            resultMap.put("result",0);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

}
