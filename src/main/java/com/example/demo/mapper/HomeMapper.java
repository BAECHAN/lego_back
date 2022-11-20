package com.example.demo.mapper;

import com.example.demo.ProductVO;
import com.example.demo.ThemeVO;
import com.example.demo.UserVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface HomeMapper {
    public List<ThemeVO> selectListTheme() throws Exception;

    public List<ProductVO> selectListProduct(int theme_id, int offset, int take) throws Exception;

    public int selectListProductCount(int theme_id) throws Exception;

    public ProductVO selectProductInfo(int product_number) throws Exception;

    public int selectUserChk(HashMap<String, Object> paramMap) throws Exception;
}