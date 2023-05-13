package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @GetMapping("/product-list")
    public ResponseEntity getProductList(@RequestParam HashMap<String,Object> paramMap)throws Exception {
        Map<String,Object> resultMap = new HashMap<String,Object>();

        int theme_id = Integer.parseInt(paramMap.get("theme_id").toString());
        int page = Integer.parseInt(paramMap.get("page").toString());
        int take = Integer.parseInt(paramMap.get("take").toString());
        String sort = paramMap.get("sort").toString();

        String jsonString = paramMap.get("filter").toString();
        ObjectMapper objectMapper = new ObjectMapper();

        HashMap<String,Object> filter = new HashMap<String,Object>();

        try{
            filter = objectMapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {});
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        int offset = (page-1) * take;

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
        List<HashMap> productFilter = service.selectListProductFilter(theme_id);
        return new ResponseEntity(productFilter, HttpStatus.OK);
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

    @PostMapping("/email-chk")
    public ResponseEntity getEmailChk(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        Optional<UserVO> userInfo = Optional.ofNullable(service.selectUserInfo(paramMap));

        if(userInfo.isPresent()){
            resultMap.put("isOverlap", true);
            // 이메일 중복
        }else{
            resultMap.put("isOverlap", false);
            // 이메일 사용 가능
        }
        return new ResponseEntity(resultMap, HttpStatus.OK);

    }

    @PostMapping("/nickname-chk")
    public ResponseEntity getNicknameChk(@RequestBody HashMap<String,Object> paramMap) throws Exception{

        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        Optional<UserVO> userInfo = Optional.ofNullable(service.selectNameChk(paramMap));

        if(userInfo.isPresent()){
            resultMap.put("isOverlap", true);
            // 닉네임 중복
        }else{
            resultMap.put("isOverlap", false);
            // 닉네임 사용 가능
        }
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/upd-nickname")
    public ResponseEntity updateNicknameAfterCheck(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        service.updateUserInfo(paramMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/create-account")
    public ResponseEntity createAccount(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        service.saveAccount(paramMap);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login-chk")
    public ResponseEntity getLoginChk(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        Optional<UserVO> userInfo = Optional.ofNullable(service.selectLoginChk(paramMap));

        if(userInfo.isPresent()){
            resultMap.put("result",userInfo);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/password-chk")
    public ResponseEntity getPasswordChkReturnToken(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        Optional<UserVO> userInfo = Optional.ofNullable(service.selectLoginChk(paramMap));

        if(userInfo.isPresent()){
            String randomStr = service.createToken(paramMap);

            paramMap.put("token",randomStr);

            int result = service.insertToken(paramMap);

            if(result > 0){
                resultMap.put("token", randomStr);
                return new ResponseEntity(resultMap, HttpStatus.OK);
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }else {
            return new ResponseEntity(resultMap, HttpStatus.OK);
        }

    }

    @PostMapping("/get-user-info")
    public ResponseEntity getUserInfo(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        Optional<UserVO> userInfo = Optional.ofNullable(service.selectUserInfo(paramMap));
        return new ResponseEntity(userInfo, HttpStatus.OK);
    }

    @PostMapping("/get-user-info-oauth")
    public ResponseEntity getUserInfoOAuth(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        return service.selectUserUpdateConnect(paramMap);
    }

    @PatchMapping("/upd-user-oauth")
    public ResponseEntity updateUserOauth(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        int result = service.updateUserOauth(paramMap);

        if(result > 0){
            return new ResponseEntity(resultMap, HttpStatus.OK);
        }else{
            return new ResponseEntity(resultMap, HttpStatus.NO_CONTENT);
        }

    }

    @GetMapping("/theme-by-product")
    public ResponseEntity getThemeByProduct(@RequestParam(value= "product_number", required=true) int product_number) throws Exception {
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        Optional<ThemeVO> themeInfo = Optional.ofNullable(service.selectThemeByProduct(product_number));

        if(themeInfo.isPresent()){
            resultMap.put("result",themeInfo);
        }

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @GetMapping("/product-viewed-list")
    public ResponseEntity getProductViewedList(@RequestParam(value = "product_number_arr") List<String> productNumbers)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        List<ProductVO> productList = service.selectListViewedProduct(productNumbers);

        resultMap.put("productList",productList);
        resultMap.put("productListCount",productList.size());

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/product-wish-list")
    public ResponseEntity getProductWishList(@RequestBody HashMap<String,Object> paramMap) throws Exception {

        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        List<HashMap> wishList = service.selectListWishedProduct(paramMap);

        resultMap.put("wishList",wishList);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PostMapping("/add-wish")
    public ResponseEntity addWish(@RequestBody HashMap<String,Object> paramMap)throws Exception {
        service.insertAddWish(paramMap);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/del-wish")
    public ResponseEntity delWish(@RequestBody HashMap<String,Object> paramMap)throws Exception {
        service.updateDelWish(paramMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PostMapping("/create-token")
    public ResponseEntity gettingToken(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        Map<String,Object> resultMap = new HashMap<String,Object>();

        String randomStr = service.createToken(paramMap);

        paramMap.put("token",randomStr);

        int result  = service.insertToken(paramMap);

        if(result > 0){
            resultMap.put("token", randomStr);
        }

        return new ResponseEntity(resultMap, HttpStatus.CREATED);
    }

    @PostMapping("/token-chk")
    public ResponseEntity passwordTokenCheck(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        TokenVO tokenResult = service.selectTokenChk(paramMap);

        resultMap.put("result",tokenResult);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PatchMapping("/update-password")
    public ResponseEntity updatePassword(@RequestBody HashMap<String,Object> paramMap) throws Exception{
        service.updatePassword(paramMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/add-cart")
    public ResponseEntity addCart(@RequestBody HashMap<String,Object> paramMap)throws Exception {
        int result = service.addCart(paramMap);

        if(result == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }else {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    @PostMapping("/product-cart-list")
    public ResponseEntity getProductCartList(@RequestBody HashMap<String,Object> paramMap) throws Exception {

        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        List<HashMap> cartList = service.selectListCartProduct(paramMap);

        resultMap.put("cartList",cartList);

        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    @PatchMapping("/del-cart")
    public ResponseEntity delCart(@RequestBody HashMap<String,Object> paramMap)throws Exception {
        int result = service.delCart(paramMap);

        if(result == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }

    @PatchMapping("/upd-cart")
    public ResponseEntity updateCart(@RequestBody HashMap<String,Object> paramMap)throws Exception {

        int result = service.updateQuantity(paramMap);

        if(result == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
    @PatchMapping("/wakeup-account")
    public ResponseEntity updateWakeupAccount(@RequestBody HashMap<String,Object> paramMap)throws Exception {
        service.updateWakeupAccount(paramMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/withdraw-account")
    public ResponseEntity updateWithdrawAccount(@RequestBody HashMap<String,Object> paramMap)throws Exception {
        service.updateWithdrawAccount(paramMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/manage-shipping")
    public ResponseEntity addShipping(@RequestBody HashMap<String,Object> paramMap)throws Exception {
        service.manageShipping(paramMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/del-shipping")
    public ResponseEntity delShipping(@RequestBody HashMap<String,Object> paramMap)throws Exception {
        service.updateDelShipping(paramMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/upd-shipping-priority")
    public ResponseEntity updateShippingPriority(@RequestBody HashMap<String,Object> paramMap)throws Exception {
        service.updateShippingPriority(paramMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
        service.saveOrder(paramMap);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/order-list")
    public ResponseEntity getOrderList(@RequestBody HashMap<String,Object> paramMap)throws Exception {

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
        service.orderRefund(paramMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
