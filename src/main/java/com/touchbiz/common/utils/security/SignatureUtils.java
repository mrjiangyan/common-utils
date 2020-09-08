package com.touchbiz.common.utils.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

/**
 * 签名工具
 *
 * @author wuyuzhen
 * @version 1.0
 * @created 15/12/8 21:36
 */
public class SignatureUtils {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * 签名计算
     *
     * @param key   要签名的应用秘钥
     * @param param 按照自然顺序的参数map
     * @return
     */
    public static String hmacSHA1(String key, TreeMap<String, Object> param) throws InvalidKeyException, NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        for (String name : param.keySet()) {
            sb.append('&').append(name).append('=').append(param.get(name));
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return hmacSHA1(key, sb.toString());
    }

    /**
     * 签名计算
     *
     * @param key  要签名的应用秘钥
     * @param data 按照自然顺序的参数，url参数格式
     * @return
     */
    public static String hmacSHA1(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
        return hmacSHA1(key.getBytes(StandardCharsets.UTF_8), data.getBytes(StandardCharsets.UTF_8));
    }

    // 生产签名
    public static String hmacSHA1(byte[] key, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);
        return new String(Base64.encodeBase64(rawHmac));
    }
}
