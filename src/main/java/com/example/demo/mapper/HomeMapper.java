package com.example.demo.mapper;

import com.example.demo.ProductVO;
import com.example.demo.ThemeVO;
import com.example.demo.UserVO;
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

    int selectEmailChk(HashMap<String, Object> paramMap) throws Exception;

    int createAccount(HashMap<String, Object> paramMap) throws Exception;

    UserVO selectUserInfo(HashMap<String, Object> paramMap) throws Exception;

    ThemeVO selectThemeByProduct(int product_number) throws Exception;

    List<ProductVO> selectListViewedProduct(ArrayList<Integer> product_number_arr) throws Exception;
}