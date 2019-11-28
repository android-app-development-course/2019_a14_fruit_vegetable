package com.zpffly.healthydiet8.api;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDiet {

    private static final String httpUrl = "http://api.tianapi.com/txapi/caipu/index";
    private static final String apiKey = "336f364e7b087b466cf130e3defd47d4";
    // 请求的数据默认5条
    private static final int num = 5;


    public static String request(String arg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        PrintWriter out = null;
        String param = "key="+apiKey+"&num="+num+"&word="+arg;
        System.out.println(param);
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            // 输出流
            out = new PrintWriter(connection.getOutputStream());
            // 请求体
            out.print(param);
            out.flush();
            // 输入流
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
