package org.loongma.weixin.subserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.loongma.weixin.subserver.common.constants.ErrorCode;
import org.loongma.weixin.subserver.common.exception.WXSubServerException;
import org.loongma.weixin.subserver.common.utils.XmlUtil;
import org.loongma.weixin.subserver.common.wxsdk.entity.WXMessageEntity;
import org.loongma.weixin.subserver.service.WXUserEventService;
import org.loongma.weixin.subserver.service.WXSignatureService;
import org.loongma.weixin.subserver.service.entity.UserEventMessageEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/${app.config.api-version}/wx/portal/")
public class WXPortalRestController {

    @Value("${wx.config.appid}")
    private String appid;

    @Resource
    private WXSignatureService signatureService;
    @Resource
    private WXUserEventService eventService;

    @GetMapping(produces = "text/plain;charset=utf-8", path = "/{appid}")
    public String signature(@PathVariable("appid") String appid,
                            @RequestParam(value = "signature", required = false) String signature,
                            @RequestParam(value = "timestamp", required = false) String timestamp,
                            @RequestParam(value = "nonce", required = false) String nonce,
                            @RequestParam(value = "echostr", required = false) String echostr) {
        try {
            log.info("开始验证微信公众号签名：{} - [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
            if (!this.appid.equals(appid)) {
                throw new WXSubServerException(ErrorCode.UNKNOWN_APPLICATION);
            }
            if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new WXSubServerException(ErrorCode.PARAM_NOT_BLANK, "存在为空的参数");
            }
            boolean status = signatureService.checkSign(signature, timestamp, nonce);
            log.info("微信公众号签名验证完毕：{} - {}", appid, status);
            if (!status) {
                return null;
            }
            return echostr;
        } catch (Exception e) {
            log.error("微信公众号签名验证异常：{} - [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr, e);
            return null;
        }
    }

    @PostMapping(produces = "application/xml; charset=UTF-8", path = "/{appid}")
    public String accept(@PathVariable(value = "appid") String appid,
                         @RequestBody String requestBody,
                         @RequestParam("signature") String signature,
                         @RequestParam("timestamp") String timestamp,
                         @RequestParam("nonce") String nonce,
                         @RequestParam("openid") String openid,
                         @RequestParam(name = "encrypt_type", required = false) String encType,
                         @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        try {
            log.info("开始处理微信公众号消息: {} - {}", openid, requestBody);
            if (!this.appid.equals(appid)) {
                throw new WXSubServerException(ErrorCode.UNKNOWN_APPLICATION);
            }
            // 消息转换
            WXMessageEntity message = XmlUtil.xmlToBean(requestBody, WXMessageEntity.class);

            // 构建实体
            UserEventMessageEntity entity = UserEventMessageEntity.builder()
                    .openId(openid)
                    .event(message.getEvent())
                    .fromUserName(message.getFromUserName())
                    .msgType(message.getMsgType())
                    .content(StringUtils.isBlank(message.getContent()) ? null : message.getContent().trim())
                    .createTime(new Date(Long.parseLong(message.getCreateTime()) * 1000L))
                    .build();

            // 受理事件
            String result = eventService.acceptUserEvent(entity);
            log.info("微信公众号消息处理完成：{} - {}", openid, result);
            return result;
        } catch (Exception e) {
            log.error("微信公众号消息处理失败：{} - {}", openid, requestBody, e);
            return "";
        }
    }
}
