package com.github.rapid.common.util;
import java.util.logging.Logger;
import java.util.logging.Level;

public class JULDemo {
    // 获取Logger实例（通常以类名命名）
    private static final Logger logger = Logger.getLogger(JULDemo.class.getName());

    public static void main(String[] args) {
        // 记录不同级别的日志
        logger.severe("严重错误111");    // Level.SEVERE
        logger.warning("警告信息222");  // Level.WARNING
        logger.info("普通信息333");     // Level.INFO
        logger.config("配置信息444");   // Level.CONFIG (默认不输出)
        logger.fine("调试信息555");     // Level.FINE   (默认不输出)
    }
}