package com.example.demo.mapper;

import com.example.demo.HomeVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeMapper {
    public List<HomeVO> selectList() throws Exception;
}
