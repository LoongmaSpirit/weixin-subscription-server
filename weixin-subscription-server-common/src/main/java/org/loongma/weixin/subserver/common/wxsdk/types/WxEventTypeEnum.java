package org.loongma.weixin.subserver.common.wxsdk.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum WxEventTypeEnum {
    SUBSCRIBE("subscribe", "用户关注"),
    UNSUBSCRIBE("unsubscribe", "用户取关"),

    SCAN("SCAN", "已关注的用户扫码"),
    CLICK("CLICK", "用户点击自定义菜单"),
    LOCATION("LOCATION", "用户上报地理位置"),

    ;

    private String event;
    private String desc;
}
