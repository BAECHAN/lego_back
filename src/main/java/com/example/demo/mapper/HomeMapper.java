package com.example.demo.mapper;

import com.example.demo.ProductVO;
import com.example.demo.ThemeVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeMapper {
    public List<ThemeVO> selectListTheme() throws Exception;
    public List<ProductVO> selectListProduct(int theme_id) throws Exception;
}
