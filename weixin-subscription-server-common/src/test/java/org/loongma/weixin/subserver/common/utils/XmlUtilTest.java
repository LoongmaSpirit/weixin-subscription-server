package org.loongma.weixin.subserver.common.utils;

import org.loongma.weixin.subserver.common.wxsdk.entity.WXMessageEntity;

class XmlUtilTest {

    public static String xml = "<xml>" +
            "<ToUserName><![CDATA[gh_21212121212]]></ToUserName>\n" +
            "<FromUserName><![CDATA[ogZAI6WEa0v2sfNvN0VZ79eAg]]></FromUserName>\n" +
            "<CreateTime>1712816905</CreateTime>\n" +
            "<MsgType><![CDATA[event]]></MsgType>\n" +
            "<Event><![CDATA[subscribe]]></Event>\n" +
            "<EventKey><![CDATA[]]></EventKey>\n" +
            "</xml>";

    static void beanToXml() {
        WXMessageEntity entity = new WXMessageEntity();
        entity.setContent("content");
        entity.setMsgId("01");
        String s = XmlUtil.beanToXml(entity);
        System.out.println(s);
    }

    static void xmlToBean() {
        WXMessageEntity messageEntity = XmlUtil.xmlToBean(xml, WXMessageEntity.class);
        System.out.println(messageEntity.getFromUserName());
    }

    public static void main(String[] args) {
        xmlToBean();
        beanToXml();
    }
}