package org.loongma.weixin.subserver.service.impl;

import org.loongma.weixin.subserver.service.WXSignatureService;
import org.loongma.weixin.subserver.common.wxsdk.utils.WXSignatureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WXSignatureServiceImpl implements WXSignatureService {

    // 微信公众号后台设置的 token
    @Value("${wx.config.token}")
    private String token;

    @Override
    public boolean checkSign(String signature, String timestamp, String nonce) {
        return WXSignatureUtil.check(signature, token, timestamp, nonce);
    }
}
