package com.enterprise.platform.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AES加密工具类，用于refresh_token加密存储
 */
@Component
public class AesEncryptUtil {

    @Value("${aes.secret}")
    private String secretKey;

    @Value("${aes.iv}")
    private String iv;

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * 加密方法
     * @param data 待加密数据
     * @return 加密后的Base64字符串
     */
    public String encrypt(String data) throws Exception {
        // 将十六进制密钥转换为字节数组
        byte[] keyBytes = hexStringToByteArray(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        
        // 将十六进制IV转换为字节数组
        byte[] ivBytes = hexStringToByteArray(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * 解密方法
     * @param encryptedData 加密后的Base64字符串
     * @return 解密后的原始数据
     */
    public String decrypt(String encryptedData) throws Exception {
        // 将十六进制密钥转换为字节数组
        byte[] keyBytes = hexStringToByteArray(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        
        // 将十六进制IV转换为字节数组
        byte[] ivBytes = hexStringToByteArray(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }
    
    /**
     * 将十六进制字符串转换为字节数组
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                                 + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }
}