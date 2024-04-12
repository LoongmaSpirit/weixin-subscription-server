package org.loongma.weixin.subserver.common.wxsdk.utils;

import org.loongma.weixin.subserver.common.utils.BytesToHexStrUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 微信验签工具类，用于第一次部署时，对接微信 API
 */
public class WXSignatureUtil {

    public static boolean check(String signature, String token, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        // 将 token、timestamp、nonce 三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (String s : arr) {
            content.append(s);
        }
        MessageDigest md;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行 sha1 混淆
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = BytesToHexStrUtil.translate(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将本地 sha1 混淆后的字符串可与微信生成的 signature 对比，标识该请求来源于微信
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }
}
