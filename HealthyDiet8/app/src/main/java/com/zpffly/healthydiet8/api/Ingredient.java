package com.zpffly.healthydiet8.api;


import com.zpffly.healthydiet8.utils.AuthService;
import com.zpffly.healthydiet8.utils.Base64Util;
import com.zpffly.healthydiet8.utils.HttpUtil;

import java.net.URLEncoder;

public class Ingredient {

    public static String ingredient(byte[] imgData) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/classify/ingredient";
        try {
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        Ingredient.ingredient();
    }
}