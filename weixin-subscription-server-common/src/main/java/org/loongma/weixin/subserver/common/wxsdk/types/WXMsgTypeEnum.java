package org.loongma.weixin.subserver.common.wxsdk.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum WXMsgTypeEnum {
    EVENT("event", "事件消息"),
    TEXT("text", "文本消息");

    private String code;
    private String desc;
}