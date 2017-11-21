package com.flchy.blog.inlets.config;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
@Component("sample")
public class Sample {
	//设置APPID/AK/SK
    public static final String APP_ID = "6421153";
    public static final String API_KEY = "LkheGWjIT6PelB0gGKv1UGB5";
    public static final String SECRET_KEY = "UiMrMQAHV6dtILQ7GYr1GKjCO5Drn3or";

    public static void main(String[] args) {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
        // 设置可选参数
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", "5");
        options.put("pit", "5");
        options.put("per", "4");
        options.put("ctp", "1");
        // 调用接口
        TtsResponse res = client.synthesis("你好百度", "zh", 1, null);
        System.out.println(res.getErrorCode());
        System.out.println(res);
        byte[] data = res.getData();
        System.out.println(data);

    }
    
    public TtsResponse getVerific(){
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
        // 设置可选参数
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", "5");
        options.put("pit", "5");
        options.put("per", "4");
        options.put("ctp", "1");
        // 调用接口
        TtsResponse res = client.synthesis("你好百度", "zh", 1, null);
        return res;
    }
    
}
