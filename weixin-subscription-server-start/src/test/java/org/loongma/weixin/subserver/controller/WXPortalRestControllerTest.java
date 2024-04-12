package org.loongma.weixin.subserver.controller;

import org.junit.jupiter.api.Test;
import org.loongma.weixin.subserver.Application;
import org.loongma.weixin.subserver.common.wxsdk.types.WXMsgTypeEnum;
import org.loongma.weixin.subserver.service.WXUserEventService;
import org.loongma.weixin.subserver.service.entity.UserEventMessageEntity;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = {Application.class})
class WXPortalRestControllerTest {

    @Resource
    private WXUserEventService eventService;

    @Test
    void accept() {
        UserEventMessageEntity build = UserEventMessageEntity.builder()
                .msgType(WXMsgTypeEnum.TEXT.getCode())
                .build();
        System.out.println(eventService.acceptUserEvent(build));

        try {
            System.out.println(eventService.acceptUserEvent(build));
        } catch (Exception e) {
            // 正常应该抛异常
            e.printStackTrace();
        }
    }
}