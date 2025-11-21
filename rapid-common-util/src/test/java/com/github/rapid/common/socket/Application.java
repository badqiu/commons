package com.github.rapid.common.socket;

import org.apache.http.client.utils.HttpClientUtils;

import com.github.rapid.common.util.PingUtil;

public class Application {
    public static void main(String[] args) {
        // 配置全局网络超时
        NetworkTimeoutConfig.configureGlobalTimeouts();
        NetworkTimeoutConfig.configureProtocolSpecificTimeouts();
        
        PingUtil.socketPing("www.baidu.com:80");
        // 启动应用
        // ...
    }
}