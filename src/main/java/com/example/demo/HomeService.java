package com.example.demo;

import com.example.demo.mapper.HomeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class HomeService {

    @Autowired
    HomeMapper homeMapper;

    public List<ThemeVO> selectListTheme() throws Exception {
        return homeMapper.selectListTheme();
    }

    public List<ProductVO> selectListProduct(int theme_id, int offset, int take, String sort, HashMap<String, Object> filter) throws Exception {
        return homeMapper.selectListProduct(theme_id,offset,take,sort,filter);
    }
    public int selectListProductCount(int theme_id, HashMap<String, Object> filter) throws Exception {
        return homeMapper.selectListProductCount(theme_id, filter);
    }

    public List<HashMap> selectListProductFilter(int theme_id) throws Exception{
        return homeMapper.selectListProductFilter(theme_id);
    }
    public ProductVO selectProductInfo(int product_number) throws Exception {
        return homeMapper.selectProductInfo(product_number);
    }

    public int insertAccount(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.insertAccount(paramMap);
    }

    public UserVO selectLoginChk(HashMap<String,Object> paramMap) throws Exception {
        return homeMapper.selectLoginChk(paramMap);
    }


    public ThemeVO selectThemeByProduct(int product_number) throws Exception{
        return homeMapper.selectThemeByProduct(product_number);
    }

    public List<ProductVO> selectListViewedProduct(ArrayList<Integer> product_number_arr) throws Exception{
        return homeMapper.selectListViewedProduct(product_number_arr);
    }

    public List<HashMap> selectListWishedProduct(int page, String email) throws Exception{
        return homeMapper.selectListWishedProduct(page, email);
    }

    public int insertAddWish(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.insertAddWish(paramMap);
    }

    public int updateDelWish(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateDelWish(paramMap);
    }

    public int createToken(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.createToken(paramMap);
    }

    public TokenVO selectTokenChk(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.selectTokenChk(paramMap);
    }

    public int updatePassword(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.updatePassword(paramMap);
    }

    public ProductVO selectProductEnable(int product_id) throws Exception {
        return homeMapper.selectProductEnable(product_id);
    }

    @Transactional
    public int insertAddCart(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.insertAddCart(paramMap);
    }

    public List<HashMap> selectListCartProduct(int page, String email) throws Exception{
        return homeMapper.selectListCartProduct(page, email);
    }

    public int updateDelCart(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateDelCart(paramMap);
    }

    public int updateQuantityCart(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateQuantityCart(paramMap);
    }

    public UserVO selectUserInfo(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.selectUserInfo(paramMap);
    }

    public int updateAccountState(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateAccountState(paramMap);
    }

    public int updateUserInfo(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateUserInfo(paramMap);
    }

    public UserVO selectNameChk(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.selectNameChk(paramMap);
    }


    /** 나중에 주문할 때 상품 개수 체크해서 주문하는거로 */
//    @Transactional
//    public int insertAddCart(HashMap<String, Object> paramMap) throws Exception {
//
//        ProductVO productInfo = selectProductEnable(Integer.parseInt(paramMap.get("product_id").toString()));
//
//        if(productInfo != null){
//            return homeMapper.insertAddCart(paramMap);
//        }else{
//            return 0;
//        }
//    }
}
