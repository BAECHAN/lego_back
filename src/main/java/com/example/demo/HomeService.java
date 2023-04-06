package com.example.demo;

import com.example.demo.mapper.HomeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Service
public class HomeService {

    @Autowired
    HomeMapper homeMapper;

    @Value("${profile.image.file.dir}")
    private String fileDir;

    public List<ThemeVO> selectListTheme() throws Exception {
        return homeMapper.selectListTheme();
    }

    public List<ProductVO> selectListProduct(int theme_id, int offset, int take, String sort, HashMap<String, Object> filter) throws Exception {
        return homeMapper.selectListProduct(theme_id, offset, take, sort, filter);
    }

    public int selectListProductCount(int theme_id, HashMap<String, Object> filter) throws Exception {
        return homeMapper.selectListProductCount(theme_id, filter);
    }

    public List<HashMap> selectListProductFilter(int theme_id) throws Exception {
        return homeMapper.selectListProductFilter(theme_id);
    }

    public ProductVO selectProductInfo(int product_number) throws Exception {
        return homeMapper.selectProductInfo(product_number);
    }

    public int insertAccount(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.insertAccount(paramMap);
    }

    public UserVO selectLoginChk(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.selectLoginChk(paramMap);
    }


    public ThemeVO selectThemeByProduct(int product_number) throws Exception {
        return homeMapper.selectThemeByProduct(product_number);
    }

    public List<ProductVO> selectListViewedProduct(ArrayList<Integer> product_number_arr) throws Exception {
        return homeMapper.selectListViewedProduct(product_number_arr);
    }

    public List<HashMap> selectListWishedProduct(int page, String email) throws Exception {
        return homeMapper.selectListWishedProduct(page, email);
    }

    public int insertAddWish(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.insertAddWish(paramMap);
    }

    public int updateDelWish(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateDelWish(paramMap);
    }

    public int createToken(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.createToken(paramMap);
    }

    public TokenVO selectTokenChk(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.selectTokenChk(paramMap);
    }

    public int updatePassword(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updatePassword(paramMap);
    }

    public int selectProductEnable(ArrayList<HashMap<String, Object>> cart_info_list) throws Exception {
        return homeMapper.selectProductEnable(cart_info_list);
    }

    @Transactional
    public int addCart(HashMap<String, Object> paramMap) throws Exception {

        // 1. 상품을 카트에 추가
        int isAdd = insertAddCart(paramMap);

        if(isAdd > 0){
            System.err.println("상품을 카트에 담았습니다.");
            paramMap.put("state", "add");
            int isUpdateProduct = updateCommitProduct(paramMap);

            return isUpdateProduct;

        }else{
            return 900; // 상품을 카트에 추가하는 쿼리 실행 시 문제 발생
        }
    }

    @Transactional
    public int delCart(HashMap<String, Object> paramMap) throws Exception {

        // 1. 상품을 장바구니에서 제거
        int isDel = updateDelCart(paramMap);

        if(isDel > 0){
            System.err.println("상품을 카트에서 제거하였습니다.");
            paramMap.put("state", "del");
            int isUpdateProduct = updateCommitProduct(paramMap);

            return isUpdateProduct;
        }else{
            return 900;
        }
    }
    public int insertAddCart(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.insertAddCart(paramMap);
    }

    public List<HashMap> selectListCartProduct(int page, String email) throws Exception {
        return homeMapper.selectListCartProduct(page, email);
    }

    public int updateDelCart(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateDelCart(paramMap);
    }

    public int updateDelCarts(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateDelCarts(paramMap);
    }

    public int updateRollbackCarts(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateRollbackCarts(paramMap);
    }

    @Transactional
    public int updateQuantity(HashMap<String, Object> paramMap) throws Exception {

        // 1. cart 수량 변경

        try{
            int isUpdateCart = updateQuantityCart(paramMap);

            if(isUpdateCart > 0){
                System.err.println("cart 에 담긴 상품의 수량이 변경되었습니다.");
                int isCommitProduct = updateCommitProduct(paramMap);

                System.err.println("상품 정보가 변경되었습니다.");
                return isCommitProduct;
            }else{
                throw new Exception("장바구니 수량 변경 쿼리 실행 시 문제 발생");
            }
        } catch (Exception e){
            e.printStackTrace();
            return 900; // 장바구니 수량 변경 쿼리 실행 시 문제 발생
        }
    }

    public int updateQuantityCart(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateQuantityCart(paramMap);
    }

    public UserVO selectUserInfo(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.selectUserInfo(paramMap);
    }

    public int updateWakeupAccount(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateWakeupAccount(paramMap);
    }

    public int updateUserInfo(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateUserInfo(paramMap);
    }

    public UserVO selectNameChk(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.selectNameChk(paramMap);
    }
    @Transactional
    public int updateCommitProduct(HashMap<String, Object> paramMap) throws Exception {

        String state = paramMap.get("state").toString();

        // 1. 상품 재고 ea를 수정합니다.
        int isProductCommit = updateQuantityProduct(paramMap);

        if(isProductCommit > 0){
            System.err.println("주문완료 된 상품들의 상품 재고를 수정하였습니다.");
            // 2. 상품 재고가 0인 경우 일시품절 처리 합니다.

            if("add".equals(state) || "updateAdd".equals(state)){
                try{
                    updateNoStockProduct(paramMap);
                    System.err.println("상품 재고가 0인 경우 '일시품절' 처리하였습니다.");
                    System.out.println("완료됨");
                    return 1;

                }catch (SQLException se) {
                    se.printStackTrace();
                    return 993; // 상품 재고가 없을 경우 '일시 품절' 처리하는 쿼리 실행 시 문제 발생
                }
            }else if("del".equals(state) || "updateSub".equals(state)){
                try{
                    updateRollbackNoStockProduct(paramMap);
                    System.err.println("재고가 있는 상품의 판매 상태를 '판매'로 변경하였습니다.");
                    System.out.println("완료됨");
                    return 1;

                }catch (SQLException se) {
                    se.printStackTrace();
                    return 994; // 상품 재고가 있을 경우 '판매' 처리하는 쿼리 실행 시 문제 발생
                }
            }else{
                return 992; // 전달 받은 state에 해당하는 값이 없습니다.
            }
        }else{
            return 991; // 상품 재고 변경 쿼리 실행 시 문제 발생
        }
    }

    public String getToken(HashMap<String, Object> paramMap) throws Exception {
        int randomStrLen = 20;
        Random random = new Random();
        StringBuffer randomBuf = new StringBuffer();
        for (int i = 0; i < randomStrLen; i++) {
            // Random.nextBoolean() : 랜덤으로 true, false 리턴 (true : 랜덤 소문자 영어, false : 랜덤 숫자)
            if (random.nextBoolean()) {
                // 26 : a-z 알파벳 개수
                // 97 : letter 'a' 아스키코드
                // (int)(random.nextInt(26)) + 97 : 랜덤 소문자 아스키코드
                randomBuf.append((char) ((int) (random.nextInt(26)) + 97));
            } else {
                randomBuf.append(random.nextInt(10));
            }
        }
        String randomStr = randomBuf.toString();
        System.out.println("[createRandomStrUsingRandomBoolean] randomStr : " + randomStr);
        // [createRandomStrUsingRandomBoolean] randomStr : iok887yt6sa31m99e4d6

        return randomStr;
    }

    public int updateWithdrawAccount(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateWithdrawAccount(paramMap);
    }

    @Transactional
    public int manageShipping(HashMap<String, Object> paramMap) throws Exception {


        int isResetShippingDefault = 0;
        int changeShipping = 0;

        if (paramMap.get("shippingDefault").toString() == "true") {
            homeMapper.resetShippingDefault(paramMap);
        }

        if (Integer.parseInt(paramMap.get("shippingId").toString()) == 0) {
            changeShipping = homeMapper.insertShipping(paramMap);
        } else {
            changeShipping = homeMapper.updateShipping(paramMap);
        }

        return changeShipping;
    }

    public List<ShippingVO> selectListShipping(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.selectListShipping(paramMap);
    }

    public int updateDelShipping(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateDelShipping(paramMap);
    }

    public int selectListShippingCount(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.selectListShippingCount(paramMap);
    }

    @Transactional
    public int saveFile(MultipartFile uploadFile, String email) throws Exception {

        int result = 0;

        String orgFileName = uploadFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        String ext = orgFileName.substring(orgFileName.lastIndexOf("."));

        String savedFileName = uuid + ext;

        String savedPath = fileDir + savedFileName;

        try {
            uploadFile.transferTo(new File(savedPath));
            result = updateUserImage(email, savedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int updateDefaultImage(String email) throws Exception {
        return homeMapper.updateDefaultImage(email);
    }

    private int updateUserImage(String email, String savedPath) throws Exception {
        return homeMapper.updateUserImage(email, savedPath);
    }

    public int updateShippingPriority(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateShippingPriority(paramMap);
    }

    @Transactional
    public int saveOrder(HashMap<String, Object> paramMap) throws Exception {
        System.err.println(paramMap);

        ArrayList<HashMap<String, Object>> cartInfo = (ArrayList<HashMap<String, Object>>) paramMap.get("cart_info");

        // 1. 배송요청사항이 변경 시 배송지정보를 update 처리 합니다.
        int isUpdateShippingDeliveryRequest = updateShippingDeliveryRequest(paramMap);

        if (isUpdateShippingDeliveryRequest > 0) {
            System.err.println("배송 요청 사항이 변경되었습니다.");

            // 2. 전체 주문 정보( user_order_group )를 insert 합니다.
            int isInsertOrderGroup = insertOrderGroup(paramMap);

            System.err.println(isInsertOrderGroup);

            if(isInsertOrderGroup > 0){
                System.err.println("전체 주문 정보가 등록되었습니다.");

                // 3. 먼저 상품별 주문 정보( user_order_item )를 insert 합니다.
                int isInsertOrderItem = insertOrderItem(paramMap);

                if(isInsertOrderItem > 0){
                    System.err.println("상품별 주문 정보가 등록되었습니다.");

                    // 4. 장바구니에 있던 주문된 상품들은 장바구니에서 제외합니다.
                    int isUpdateDelCarts = updateDelCarts(paramMap);

                    if(isUpdateDelCarts > 0){
                        System.err.println("주문완료 된 상품들은 장바구니에서 제외되었습니다.");
                        System.out.println("완료됨");
                        return 1;
                    }else{
                        return 904; // 장바구니에서 상품을 제외하는 쿼리 실행 시 문제 발생
                    }
                }else{
                    return 903; // 상품별 주문 정보를 등록하는 쿼리 실행 시 문제 발생
                }
            }else{
                return 902; // 주문 그룹을 등록하는 쿼리 실행 시 문제 발생
            }
        } else {
            return 901; // 배송 요청 사항 변경 쿼리 실행 시 문제 발생
        }
    }

    private int updateNoStockProduct(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateNoStockProduct(paramMap);
    }

    private int updateRollbackNoStockProduct(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateRollbackNoStockProduct(paramMap);
    }

    public int updateShippingDeliveryRequest(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updateShippingDeliveryRequest(paramMap);
    }

    public int insertOrderGroup(HashMap<String,Object> paramMap) throws Exception {
        return homeMapper.insertOrderGroup(paramMap);
    }

    public int insertOrderItem(HashMap<String,Object> paramMap) throws Exception {
        return homeMapper.insertOrderItem(paramMap);
    }

    public int selectCountOrderItems(String email) throws Exception {
        return homeMapper.selectCountOrderItems(email);
    }

    public List<OrderVO> selectListOrderItems(String email, int offset, int take) throws Exception{
        return homeMapper.selectListOrderItems(email,offset,take);
    }

    public int updateOrderRefund(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateOrderRefund(paramMap);
    }

    @Transactional
    public int orderRefund(HashMap<String, Object> paramMap) throws Exception {

        // 1. 환불요청
        int isRefund = updateOrderRefund(paramMap);

        if(isRefund > 0){
            // 2. 주문에 대한 상품 정보 가져오기
            ArrayList<HashMap<String,Object>> orderQuantityByProduct = new ArrayList<>();

            orderQuantityByProduct = selectListOrderQuantity(paramMap);

            if(orderQuantityByProduct.size() > 0) {
                System.err.println(orderQuantityByProduct);
                // 3. 장바구니에 다시 담기 ( rollback )

                paramMap.put("cart_info", orderQuantityByProduct);

                int isCartRollback = updateRollbackCarts(paramMap);

                if (isCartRollback > 0) {
                    System.err.println("장바구니 rollback 완료");
                    System.err.println("성공적");
                    return 1;
                } else {
                    return 908; // 장바구니에서 제외한 주문 상품들을 다시 장바구니에 담는 쿼리 실행 시 문제 발생
                }
            }else{
                return 907; // 주문했던 상품 정보를 가져오는 쿼리 실행 시 문제 발생
            }
        }else{
            return 906; // 환불 처리하는 쿼리 실행 시 문제 발생
        }
    }

    public int updateQuantityProduct(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateQuantityProduct(paramMap);
    }

    public ArrayList<HashMap<String,Object>> selectListOrderQuantity(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.selectListOrderQuantity(paramMap);
    }

    public int updateUserOauth(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateUserOauth(paramMap);
    }

    @Transactional
    public int selectUserUpdateConnect(HashMap<String, Object> paramMap) throws Exception{

        UserVO userInfo = selectUserInfo(paramMap);
        if (userInfo != null){
            if(userInfo.getOauth_connect() != null){
                return 200;
            }else{
                try {
                    updateUserOauth(paramMap);
                    return 201;

                } catch(SQLException sqle){
                    sqle.printStackTrace();
                    return 500;
                }
            }
        }else{
            return 204;
        }
    }
}
