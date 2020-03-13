package com.example.thweibo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpRequest {
    public MyHttpRequest(){}

    protected String sendHttpRequest(String urlStr, String params, String requestMethod) throws IOException {
        // 建立网络连接
        URL url = new URL(urlStr);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        // 向服务器写入数据
        http.setDoOutput(true);
        http.setRequestMethod(requestMethod);
        OutputStream out = http.getOutputStream();
        out.write(params.getBytes());
        out.flush();
        out.close();
        // 读取后台返回的数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String line = "";
        StringBuilder builder = new StringBuilder();    // 建立输入缓冲区
        while (null != (line = reader.readLine())) {    // 结束会读入null值
            builder.append(line);
        }
        return builder.toString();
    }
}
