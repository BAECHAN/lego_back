package com.example.demo;

import com.example.demo.mapper.HomeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class HomeService {

    @Autowired
    HomeMapper homeMapper;

    public List<ThemeVO> selectListTheme() throws Exception {
        return homeMapper.selectListTheme();
    }

    public List<ProductVO> selectListProduct(int theme_id, int offset, int take) throws Exception {
        return homeMapper.selectListProduct(theme_id,offset,take);
    }
    public int selectListProductCount(int theme_id) throws Exception {
        return homeMapper.selectListProductCount(theme_id);
    }
    public ProductVO selectProductInfo(int product_number) throws Exception {
        return homeMapper.selectProductInfo(product_number);
    }

    public int selectEmailChk(HashMap<String,Object> paramMap) throws Exception {
        return homeMapper.selectEmailChk(paramMap);
    }

    public int createAccount(HashMap<String, Object> paramMap) throws Exception {
        return homeMapper.createAccount(paramMap);
    }
}
