package com.oshiel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ログ出力クラス
 */
public class logUtil {

    /**
     * log4j インスタンス
     */
    private static Logger logger = LoggerFactory.getLogger(logUtil.class.getName());

    /**
     * infoログを出力
     * @param message
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * errorログを出力
     * @param message
     */
    public static void error(String message) {
        logger.error(message);
    }
}
