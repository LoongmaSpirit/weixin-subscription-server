package org.loongma.weixin.subserver.service;

/**
 * 微信签名验证接口，用于验证消息是否来自微信
 */
public interface WXSignatureService {

    boolean checkSign(String signature, String timestamp, String nonce);
}
