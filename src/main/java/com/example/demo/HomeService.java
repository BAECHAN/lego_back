package com.example.demo;

import com.example.demo.mapper.HomeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public int selectEmailChk(HashMap<String,Object> paramMap) throws Exception {
        return homeMapper.selectEmailChk(paramMap);
    }

    public int insertAccount(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.insertAccount(paramMap);
    }

    public UserVO selectUserInfo(HashMap<String,Object> paramMap) throws Exception {
        return homeMapper.selectUserInfo(paramMap);
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
}
