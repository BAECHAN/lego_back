package com.example.demo.mapper;

import com.example.demo.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public interface HomeMapper {
    List<ThemeVO> selectListTheme() throws Exception;

    List<ProductVO> selectListProduct(int theme_id, int offset, int take, String sort, HashMap<String, Object> filter) throws Exception;

    int selectListProductCount(int theme_id, HashMap<String, Object> filter) throws Exception;

    List<HashMap> selectListProductFilter(int theme_id) throws Exception;

    ProductVO selectProductInfo(int product_number) throws Exception;

    void insertAccount(HashMap<String, Object> paramMap) throws Exception;

    UserVO selectLoginChk(HashMap<String, Object> paramMap) throws Exception;

    ThemeVO selectThemeByProduct(int product_number) throws Exception;

    List<ProductVO> selectListViewedProduct(List<String> product_number_arr) throws Exception;

    List<HashMap> selectListWishedProduct(HashMap<String, Object> paramMap) throws Exception;

    void insertAddWish(HashMap<String, Object> paramMap) throws Exception;

    void updateDelWish(HashMap<String, Object> paramMap) throws Exception;

    int insertToken(HashMap<String, Object> paramMap) throws Exception;

    TokenVO selectTokenChk(HashMap<String, Object> paramMap) throws Exception;

    int updatePassword(HashMap<String, Object> paramMap) throws Exception;

    int insertAddCart(HashMap<String, Object> paramMap) throws Exception;

    int selectProductEnable(ArrayList<HashMap<String, Object>> cart_info_list) throws Exception;

    List<HashMap> selectListCartProduct(HashMap<String, Object> paramMap) throws Exception;

    int updateDelCart(HashMap<String, Object> paramMap) throws Exception;
    int updateDelCarts(HashMap<String, Object> paramMap) throws Exception;
    int updateRollbackCarts(HashMap<String, Object> paramMap) throws Exception;

    int updateQuantityCart(HashMap<String, Object> paramMap) throws Exception;

    UserVO selectUserInfo(HashMap<String, Object> paramMap) throws Exception;

    void updateWakeupAccount(HashMap<String, Object> paramMap) throws Exception;

    void updateUserInfo(HashMap<String, Object> paramMap) throws Exception;

    UserVO selectNameChk(HashMap<String, Object> paramMap) throws Exception;

    void updateWithdrawAccount(HashMap<String, Object> paramMap) throws Exception;

    void insertShipping(HashMap<String, Object> paramMap) throws Exception;

    void updateShipping(HashMap<String, Object> paramMap) throws Exception;;

    List<ShippingVO> selectListShipping(HashMap<String, Object> paramMap) throws Exception;

    int resetShippingDefault(HashMap<String, Object> paramMap) throws Exception;

    int updateDelShipping(HashMap<String, Object> paramMap) throws Exception;

    int selectListShippingCount(HashMap<String, Object> paramMap) throws Exception;

    int updateUserImage(String email, String savedPath) throws Exception;

    int updateDefaultImage(String email) throws Exception;

    void updateShippingPriority(HashMap<String, Object> paramMap) throws Exception;

    int updateShippingDeliveryRequest(HashMap<String, Object> paramMap) throws Exception;

    int insertOrderGroup(HashMap<String, Object> paramMap) throws Exception;

    int insertOrderItem(HashMap<String, Object> paramMap) throws Exception;

    List<OrderVO> selectListOrderItems(List<Integer> orderGroupIds) throws Exception;

    List<OrderVO> selectListOrderGroups(String email, int offset, int take) throws Exception;

    int updateOrderRefund(HashMap<String, Object> paramMap) throws Exception;

    ArrayList<HashMap<String,Object>> selectListOrderQuantity(HashMap<String, Object> paramMap) throws Exception;

    int updateNoStockProduct(HashMap<String, Object> paramMap) throws Exception;

    int updateRollbackNoStockProduct(HashMap<String, Object> paramMap) throws Exception;

    int updateQuantityProduct(HashMap<String, Object> paramMap) throws Exception;

    int updateUserOauth(HashMap<String, Object> paramMap) throws Exception;

    int selectOrderGroupsTotalCount(String email) throws Exception;
}