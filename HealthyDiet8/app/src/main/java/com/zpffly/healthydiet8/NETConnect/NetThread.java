package com.zpffly.healthydiet8.NETConnect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zpffly.healthydiet8.api.Ingredient;

import java.util.concurrent.Callable;

/**
 * 网络操作子线程类
 */
public class NetThread implements Runnable {

    private byte[] imgByte;
    private Handler handler;

    public NetThread(byte[] imgByte, Handler handler){
        this.imgByte = imgByte;
        this.handler = handler;
    }

    @Override
    public void run() {
        String res = null;
        try{
            res = Ingredient.ingredient(imgByte);
        }catch (Exception e){
            res = "error";
        }finally {
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("res", res);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
