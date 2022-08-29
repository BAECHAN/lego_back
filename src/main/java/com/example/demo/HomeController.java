package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    HomeService service;

    @GetMapping("/index")
    public String home(ModelMap model) throws Exception {

        List<HomeVO> resultList = service.selectList();
        List<HomeVO> listA1 = new ArrayList<>();
        List<HomeVO> listA2 = new ArrayList<>();
        List<HomeVO> listB1 = new ArrayList<>();
        List<HomeVO> listB2 = new ArrayList<>();
        List<HomeVO> listC1 = new ArrayList<>();
        List<HomeVO> listC2 = new ArrayList<>();
        List<HomeVO> listFlat = new ArrayList<>();

        for(int i=0;i<resultList.size();i++){
            if("A-1".equals(resultList.get(i).getFinal_order())){
                listA1.add(resultList.get(i));
            }else if("A-2".equals(resultList.get(i).getFinal_order())){
                listA2.add(resultList.get(i));
            }else if("B-1".equals(resultList.get(i).getFinal_order())){
                listB1.add(resultList.get(i));
            }else if("B-2".equals(resultList.get(i).getFinal_order())){
                listB2.add(resultList.get(i));
            }else if("C-1".equals(resultList.get(i).getFinal_order())){
                listC1.add(resultList.get(i));
            }else if("C-2".equals(resultList.get(i).getFinal_order())){
                listC2.add(resultList.get(i));
            }else if("Flat".equals(resultList.get(i).getFinal_order())){
                listFlat.add(resultList.get(i));
            }
        }

        HashMap<String, List> resultMap = new HashMap<String, List>();

        resultMap.put("listA1",listA1);
        resultMap.put("listA2",listA2);
        resultMap.put("listB1",listB1);
        resultMap.put("listB2",listB2);
        resultMap.put("listC1",listC1);
        resultMap.put("listC2",listC2);
        resultMap.put("listFlat",listFlat);
        resultMap.put("resultList",resultList);

        model.addAllAttributes(resultMap);
        return "index.html";

    }
}
