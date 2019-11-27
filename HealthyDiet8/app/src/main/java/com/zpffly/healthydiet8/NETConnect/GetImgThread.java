package com.zpffly.healthydiet8.NETConnect;

import android.os.Bundle;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;

/**
 * 获取图片线程
 */
public class GetImgThread implements Runnable{

    private String urlStr;
    private Handler handler;

    public GetImgThread(String url, Handler handler){
        this.urlStr = url;
        this.handler = handler;
    }


    @Override
    public void run() {
        String msg = "success";
        byte[] byteArray = null;
        try{
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.connect();
            // 连接状态出错
            if (conn.getResponseCode() != 200){
                msg = "获取图片出错";
                return;
            }
            InputStream inputStream = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            int n = 0;
            while ((n = inputStream.read(temp, 0, 1024)) != -1){
                out.write(temp, 0, n);
            }
            byteArray = out.toByteArray();

        }catch (Exception e){
            msg = "获取图片失败";
        }finally {
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("msg", msg);
            bundle.putByteArray("arr", byteArray);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
