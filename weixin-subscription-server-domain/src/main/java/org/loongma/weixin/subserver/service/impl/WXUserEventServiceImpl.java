package org.loongma.weixin.subserver.service.impl;

import com.google.common.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.loongma.weixin.subserver.common.constants.ErrorCode;
import org.loongma.weixin.subserver.common.exception.WXSubServerException;
import org.loongma.weixin.subserver.common.utils.XmlUtil;
import org.loongma.weixin.subserver.service.WXUserEventService;
import org.loongma.weixin.subserver.common.wxsdk.entity.WXMessageEntity;
import org.loongma.weixin.subserver.service.entity.UserEventMessageEntity;
import org.loongma.weixin.subserver.common.wxsdk.types.WXMsgTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WXUserEventServiceImpl implements WXUserEventService {
    @Value("${wx.config.originalid}")
    private String originalId;
    @Resource(name = "rateLimitCache")
    private Cache<String, String> rateLimitCache;

    @Override
    public String acceptUserEvent(UserEventMessageEntity request) {
        // 非 TEXT 文本类型，暂时不处理
        if (!WXMsgTypeEnum.TEXT.getCode().equals(request.getMsgType())) {
            throw new WXSubServerException(ErrorCode.RUNTIME_ERR);
        }

        String isExistCode = rateLimitCache.getIfPresent(request.getOpenId());
        if (StringUtils.isNotBlank(isExistCode)) {
            throw new WXSubServerException(ErrorCode.OK, "忽略频繁触发的请求");
        } else {
            rateLimitCache.put(request.getOpenId(), "mark");
        }
        // 返回普通问候信息
        WXMessageEntity echo = new WXMessageEntity();
        echo.setToUserName(request.getOpenId());
        echo.setFromUserName(originalId);
        echo.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
        echo.setMsgType("text");
        echo.setContent("我已经收到了您的留言，稍后回复 (此消息为自动发送)");
        return XmlUtil.beanToXml(echo);
    }
}
