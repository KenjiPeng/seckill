package io.kenji.seckill.common.shiro.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/27
 **/
public class CommonsUtils {

    public static String encryptPassword(String source, String salt) {
        return String.valueOf(new SimpleHash("MD5", source, salt, 1024));
    }
}
