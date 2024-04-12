package org.loongma.weixin.subserver.service;

import org.loongma.weixin.subserver.service.entity.UserEventMessageEntity;

/**
 * 用户事件处理服务
 */
public interface WXUserEventService {

    String acceptUserEvent(UserEventMessageEntity entity);
}
