package org.seefly.code;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AESUtil {

    private static final String IV = "b52865243a3847dc";

    /**
     * 不允许实例化
     */
    private AESUtil() {
    }


    /**
     * 加密
     *
     * @param data 原数据
     * @param key  加密的key
     * @return 返回加密后的数据
     */
    public static String encrypt(String data, String key) {
        return encrypt(DigestUtils.sha1Hex(key).substring(0, 16), IV, data);
    }

    /**
     * 解密
     *
     * @param data 加密的数据
     * @param key  加密的key
     * @return 返回解密后的数据
     */
    public static String decrypt(String data, String key) {
        return decrypt(DigestUtils.sha1Hex(key).substring(0, 16), IV, Base64.decodeBase64(data));
    }

    /**
     * AES加密
     *
     * @param key  加密需要的KEY
     * @param iv   加密需要的向量
     * @param data 需要加密的数据
     * @return 返回加密后的结果
     */
    private static String encrypt(String key, String iv, String data) {
        byte[] encrypted = {};
        byte[] enCodeFormat = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv.getBytes()));
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            encrypted = cipher.doFinal(plaintext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(encrypted),StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param key  解密需要的KEY 同加密
     * @param iv   解密需要的向量 同加密
     * @param data 需要解密的数据
     * @return 返回解密后的结果
     */
    private static String decrypt(String key, String iv, byte[] data) {
        String content = "";
        byte[] enCodeFormat = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv.getBytes()));
            byte[] result = cipher.doFinal(data);
            content = new String(result,StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

}
