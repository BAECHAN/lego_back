package com.example.demo;

import com.example.demo.mapper.HomeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HomeService {

    @Autowired
    HomeMapper homeMapper;

    public List<HomeVO> selectList() throws Exception {
        return homeMapper.selectList();

    }
}
