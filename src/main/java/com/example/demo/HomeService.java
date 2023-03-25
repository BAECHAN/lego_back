package com.example.demo;

import com.example.demo.mapper.HomeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class HomeService {

    @Autowired
    HomeMapper homeMapper;

    @Value("${profile.image.file.dir}")
    private String fileDir;

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

    public int updateWakeupAccount(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateWakeupAccount(paramMap);
    }

    public int updateUserInfo(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateUserInfo(paramMap);
    }

    public UserVO selectNameChk(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.selectNameChk(paramMap);
    }


    public String getToken(HashMap<String, Object> paramMap) throws Exception{
        int randomStrLen = 20;
        Random random = new Random();
        StringBuffer randomBuf = new StringBuffer();
        for (int i = 0; i < randomStrLen; i++) {
            // Random.nextBoolean() : 랜덤으로 true, false 리턴 (true : 랜덤 소문자 영어, false : 랜덤 숫자)
            if (random.nextBoolean()) {
                // 26 : a-z 알파벳 개수
                // 97 : letter 'a' 아스키코드
                // (int)(random.nextInt(26)) + 97 : 랜덤 소문자 아스키코드
                randomBuf.append((char)((int)(random.nextInt(26)) + 97));
            } else {
                randomBuf.append(random.nextInt(10));
            }
        }
        String randomStr = randomBuf.toString();
        System.out.println("[createRandomStrUsingRandomBoolean] randomStr : " + randomStr);
        // [createRandomStrUsingRandomBoolean] randomStr : iok887yt6sa31m99e4d6

        return randomStr;
    }

    public int updateWithdrawAccount(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateWithdrawAccount(paramMap);
    }

    @Transactional
    public int manageShipping(HashMap<String, Object> paramMap) throws Exception{


        int isResetShippingDefault = 0;
        int changeShipping = 0;

        if(paramMap.get("shippingDefault").toString() == "true"){
            homeMapper.resetShippingDefault(paramMap);
        }

        if(Integer.parseInt(paramMap.get("shippingId").toString()) == 0){
            changeShipping = homeMapper.insertShipping(paramMap);
        }else{
            changeShipping = homeMapper.updateShipping(paramMap);
        }

        return changeShipping;
    }

    public List<ShippingVO> selectListShipping(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.selectListShipping(paramMap);
    }

    public int updateDelShipping(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateDelShipping(paramMap);
    }

    public int selectListShippingCount(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.selectListShippingCount(paramMap);
    }

    @Transactional
    public int saveFile(MultipartFile uploadFile, String email) throws Exception{

        int result = 0;

        String orgFileName = uploadFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        String ext = orgFileName.substring(orgFileName.lastIndexOf("."));

        String savedFileName = uuid + ext;

        String savedPath = fileDir + savedFileName;

        try{
            uploadFile.transferTo(new File(savedPath));
            result = updateUserImage(email, savedPath);
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public int updateDefaultImage(String email) throws Exception{
        return homeMapper.updateDefaultImage(email);
    }
    private int updateUserImage(String email, String savedPath) throws Exception{
        return homeMapper.updateUserImage(email, savedPath);
    }

    public int updateShippingPriority(HashMap<String, Object> paramMap) throws Exception{
        return homeMapper.updateShippingPriority(paramMap);
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
