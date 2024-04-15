package org.loongma.weixin.subserver.service.impl;

import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.loongma.weixin.subserver.common.constants.ErrorCode;
import org.loongma.weixin.subserver.common.exception.WXSubServerException;
import org.loongma.weixin.subserver.common.utils.XmlUtil;
import org.loongma.weixin.subserver.common.wxsdk.types.WxEventTypeEnum;
import org.loongma.weixin.subserver.service.WXUserEventService;
import org.loongma.weixin.subserver.common.wxsdk.entity.WXMessageEntity;
import org.loongma.weixin.subserver.service.entity.UserEventMessageEntity;
import org.loongma.weixin.subserver.common.wxsdk.types.WXMsgTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class WXUserEventServiceImpl implements WXUserEventService {

    @Value("${wx.config.originalid}")
    private String originalId;
    @Resource(name = "rateLimitCache")
    private Cache<String, String> rateLimitCache;

    @Override
    public String acceptUserEvent(UserEventMessageEntity request) {
        String msgType = request.getMsgType();
        // 非 TEXT 文本类型，暂时不处理
        if (WXMsgTypeEnum.TEXT.getCode().equals(msgType)) {
            return textHandle(request);
        } else if (WXMsgTypeEnum.EVENT.getCode().equals(msgType)) {
            return eventsHandle(request);
        } else {
            throw new WXSubServerException(ErrorCode.RUNTIME_ERR);
        }
    }

    /**
     * 消息类型请求处理
     *
     * @param request 触发的用户事件
     * @return 处理结果
     */
    private String textHandle(UserEventMessageEntity request) {
        String isExistCode = rateLimitCache.getIfPresent(request.getOpenId());
        if (StringUtils.isNotBlank(isExistCode)) {
            log.info("");
            return "";
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

    /**
     * 事件类型处理
     *
     * @param request 触发的用户事件
     * @return 处理结果
     */
    private String eventsHandle(UserEventMessageEntity request) {
        String event = request.getEvent();
        if (WxEventTypeEnum.SUBSCRIBE.getEvent().equals(event)) {
            // 返回欢迎语
            WXMessageEntity echo = new WXMessageEntity();
            echo.setToUserName(request.getFromUserName());
            echo.setFromUserName(originalId);
            echo.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
            echo.setMsgType("text");
            echo.setContent("感谢您的关注！愿我们共同进步！");
            return XmlUtil.beanToXml(echo);
        } else {
            return "";
        }
    }
}
