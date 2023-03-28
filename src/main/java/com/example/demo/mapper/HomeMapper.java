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

    int insertAccount(HashMap<String, Object> paramMap) throws Exception;

    UserVO selectLoginChk(HashMap<String, Object> paramMap) throws Exception;

    ThemeVO selectThemeByProduct(int product_number) throws Exception;

    List<ProductVO> selectListViewedProduct(ArrayList<Integer> product_number_arr) throws Exception;

    List<HashMap> selectListWishedProduct(int page, String email) throws Exception;

    int insertAddWish(HashMap<String, Object> paramMap) throws Exception;

    int updateDelWish(HashMap<String, Object> paramMap) throws Exception;

    int createToken(HashMap<String, Object> paramMap) throws Exception;

    TokenVO selectTokenChk(HashMap<String, Object> paramMap) throws Exception;

    int updatePassword(HashMap<String, Object> paramMap) throws Exception;

    int insertAddCart(HashMap<String, Object> paramMap) throws Exception;

    int selectProductEnable(ArrayList<HashMap<String, Object>> cart_info_list) throws Exception;

    List<HashMap> selectListCartProduct(int page, String email) throws Exception;

    int updateDelCart(HashMap<String, Object> paramMap) throws Exception;
    int updateDelCarts(HashMap<String, Object> paramMap) throws Exception;
    int updateRollbackCarts(HashMap<String, Object> paramMap) throws Exception;

    int updateQuantityCart(HashMap<String, Object> paramMap) throws Exception;

    UserVO selectUserInfo(HashMap<String, Object> paramMap) throws Exception;

    int updateWakeupAccount(HashMap<String, Object> paramMap) throws Exception;

    int updateUserInfo(HashMap<String, Object> paramMap) throws Exception;

    UserVO selectNameChk(HashMap<String, Object> paramMap) throws Exception;

    int updateWithdrawAccount(HashMap<String, Object> paramMap) throws Exception;

    int insertShipping(HashMap<String, Object> paramMap) throws Exception;

    int updateShipping(HashMap<String, Object> paramMap) throws Exception;;

    List<ShippingVO> selectListShipping(HashMap<String, Object> paramMap) throws Exception;

    int resetShippingDefault(HashMap<String, Object> paramMap) throws Exception;

    int updateDelShipping(HashMap<String, Object> paramMap) throws Exception;

    int selectListShippingCount(HashMap<String, Object> paramMap) throws Exception;

    int updateUserImage(String email, String savedPath) throws Exception;

    int updateDefaultImage(String email) throws Exception;

    int updateShippingPriority(HashMap<String, Object> paramMap) throws Exception;

    int updateShippingDeliveryRequest(HashMap<String, Object> paramMap) throws Exception;

    int insertOrderGroup(HashMap<String, Object> paramMap) throws Exception;

    int insertOrderItem(HashMap<String, Object> paramMap) throws Exception;

    List<OrderVO> selectListOrderItems(String email, int offset, int take) throws Exception;

    int selectCountOrderItems(String email) throws Exception;

    int updateOrderRefund(HashMap<String, Object> paramMap) throws Exception;

    int updateAddQuantityProduct(ArrayList<HashMap<String, Object>> product_quantity_arr) throws Exception;
    int updateSubQuantityProduct(ArrayList<HashMap<String, Object>> product_quantity_arr) throws Exception;

    ArrayList<HashMap<String,Object>> selectListOrderQuantity(HashMap<String, Object> paramMap) throws Exception;

    int updateNoStockProduct(ArrayList<HashMap<String, Object>> product_quantity_arr) throws Exception;

    int updateRollbackNoStockProduct(ArrayList<HashMap<String, Object>> product_quantity_arr) throws Exception;
}