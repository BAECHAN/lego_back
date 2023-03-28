package com.example.demo;

import com.example.demo.mapper.HomeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

        String email = paramMap.get("email").toString();
        int shippingId = Integer.parseInt(paramMap.get("shipping_id").toString());
        String deliveryRequest = paramMap.get("delivery_request").toString();
        String deliveryRequestDirect = paramMap.get("delivery_request_direct").toString();
        ArrayList<HashMap<String, Object>> cartInfo = (ArrayList<HashMap<String, Object>>) paramMap.get("cart_info");

        System.err.println(email);
        System.err.println(shippingId);
        System.err.println(deliveryRequest);
        System.err.println(deliveryRequestDirect);
        System.err.println(cartInfo);

        System.err.println(cartInfo);

        int countAbleProduct = selectProductEnable(cartInfo);

        System.err.println(cartInfo.size());
        System.err.println(countAbleProduct);
        System.err.println(cartInfo.size() == countAbleProduct);


        // 1. 판매가 불가능한 상품이 있는지 확인합니다.
        if (cartInfo.size() == countAbleProduct) {
            System.err.println("판매가능 수가 일치합니다.");

            // 2. 배송요청사항이 변경 시 배송지정보를 update 처리 합니다.
            int isUpdateShippingDeliveryRequest = updateShippingDeliveryRequest(paramMap);

            if (isUpdateShippingDeliveryRequest > 0) {
                System.err.println("배송 요청 사항이 변경되었습니다.");

                // 3. 전체 주문 정보( user_order_group )를 insert 합니다.
                int isInsertOrderGroup = insertOrderGroup(paramMap);

                System.err.println(isInsertOrderGroup);

                if(isInsertOrderGroup > 0){
                    System.err.println("전체 주문 정보가 등록되었습니다.");

                    // 4. 먼저 상품별 주문 정보( user_order_item )를 insert 합니다.
                    int isInsertOrderItem = insertOrderItem(paramMap);

                    if(isInsertOrderItem > 0){
                        System.err.println("상품별 주문 정보가 등록되었습니다.");

                        // 5. 장바구니에 있던 주문된 상품들은 장바구니에서 제외합니다.
                        int isUpdateDelCarts = updateDelCarts(paramMap);

                        if(isUpdateDelCarts > 0){
                            System.err.println("주문완료 된 상품들은 장바구니에서 제외되었습니다.");

                            // 6. 상품 재고 ea를 수정합니다.
                            int isProductCommit = updateSubQuantityProduct(cartInfo);

                            if(isProductCommit > 0){
                                System.err.println("주문완료 된 상품들의 상품 재고를 수정하였습니다.");

                                updateNoStockProduct(cartInfo);

                                System.out.println("완료됨");
                                return 1;
                            }else{
                                return 905;
                            }
                        }else{
                            return 904;
                        }
                    }else{
                        return 903;
                    }
                }else{
                    return 902;
                }
            } else {
                return 901;
            }
        } else {
            return 900;
        }
    }

    private int updateNoStockProduct(ArrayList<HashMap<String, Object>> product_quantity_arr) throws Exception{
        return homeMapper.updateNoStockProduct(product_quantity_arr);
    }

    private int updateRollbackNoStockProduct(ArrayList<HashMap<String, Object>> product_quantity_arr) throws Exception{
        return homeMapper.updateRollbackNoStockProduct(product_quantity_arr);
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
        int isRefund = updateOrderRefund(paramMap);

        // 1. 환불요청
        if(isRefund > 0){
            // 2. 주문에 대한 상품 정보 가져오기

            ArrayList<HashMap<String,Object>> orderQuantityByProduct = new ArrayList<>();

            orderQuantityByProduct = selectListOrderQuantity(paramMap);

            if(orderQuantityByProduct.size() > 0){
                System.err.println(orderQuantityByProduct);

                // 3. 주문 상품에 대한 rollback
                int isProductRollback = updateAddQuantityProduct(orderQuantityByProduct);

                if(isProductRollback > 0){
                    System.err.println("주문 상품에 대한 rollback 완료");
                    // 4. 장바구니에 대한 rollback
                    paramMap.put("cart_info",orderQuantityByProduct);

                    int isCartRollback = updateRollbackCarts(paramMap);

                    if(isCartRollback > 0){
                        System.err.println("장바구니 rollback 완료");
                        updateRollbackNoStockProduct(orderQuantityByProduct);
                        System.err.println("성공적");
                        return 1;
                    }else{
                        return 909;
                    }
                }else{
                    return 908;
                }
            }else{
                return 907;
            }
        }else{
            return 906;
        }
    }

    public int updateAddQuantityProduct(ArrayList<HashMap<String, Object>> product_quantity_arr) throws Exception{
        return homeMapper.updateAddQuantityProduct(product_quantity_arr);
    }

    public int updateSubQuantityProduct(ArrayList<HashMap<String, Object>> product_quantity_arr) throws Exception{
        return homeMapper.updateSubQuantityProduct(product_quantity_arr);
    }

    public ArrayList<HashMap<String,Object>> selectListOrderQuantity(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.selectListOrderQuantity(paramMap);
    }


}
