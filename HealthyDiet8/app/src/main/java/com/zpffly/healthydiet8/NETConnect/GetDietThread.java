package com.zpffly.healthydiet8.NETConnect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zpffly.healthydiet8.api.GetDiet;

import org.json.JSONObject;


public class GetDietThread extends Thread {


    private Handler handler;
    private String name;

    public GetDietThread(String name, Handler handler) {
        this.name = name;
        this.handler = handler;
    }

    @Override
    public void run() {
        String diet = "";
        try{
            if ("非果蔬食材".equals(name))
                throw new IllegalArgumentException("ss");
            diet = GetDiet.request(name);
//            if ("{\"code\":250,\"msg\":\"数据查询失败\"}".equals(diet))
//                throw new IllegalArgumentException("xixi");
            JSONObject json = new JSONObject(diet);
            if (json.optString("code").equals("250"))
                throw new IllegalArgumentException("xixi");
        }catch (Exception e){
            diet = "{\n" +
                    "\"code\":0,\n" +
                    "\"msg\":\"failed\",\n" +
                    "\"newslist\":[\n" +
                    "{\n" +
                    "\"id\":0000,\n" +
                    "\"type_id\":000,\n" +
                    "\"type_name\":\"非果蔬类\",\n" +
                    "\"cp_name\":\"非果蔬类\",\n" +
                    "\"zuofa\":\"\",\n" +
                    "\"tishi\":\"\",\n" +
                    "\"tiaoliao\":\"\",\n" +
                    "\"yuanliao\":\"\"\n" +
                    "}\n" +
                    "]\n" +
                    "}";
        }finally {
            Message message = new Message();
            Bundle bundle = new Bundle();
//            System.out.println(diet);
            bundle.putString("diet", diet);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
