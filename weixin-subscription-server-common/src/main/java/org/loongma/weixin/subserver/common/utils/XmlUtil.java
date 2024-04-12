package org.loongma.weixin.subserver.common.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.commons.lang3.StringUtils;

import java.io.Writer;

/**
 * 微信 XML 消息转换工具类
 */
public class XmlUtil {

    /**
     * XStream 扩展，bean 转 xml 自动加上 ![CDATA[]]
     *
     * @return XStream
     */
    public static XStream createCustomXStream() {
        return new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {

                    @Override
                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                    }

                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        //对所有非数字 xml 节点，加上 cdata 标记
                        if (!StringUtils.isNumeric(text)) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
    }

    /**
     * bean 转成微信的 xml 消息格式
     */
    public static String beanToXml(Object object) {
        XStream customXStream = createCustomXStream();
        customXStream.alias("xml", object.getClass());
        customXStream.processAnnotations(object.getClass());
        String xml = customXStream.toXML(object);
        if (!StringUtils.isEmpty(xml)) {
            return xml;
        } else {
            return null;
        }
    }

    /**
     * 微信的 xml 消息格式转成 bean
     */
    public static <T> T xmlToBean(String xml, Class<T> clazz) {
        XStream xStream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{clazz});
        xStream.processAnnotations(new Class[]{clazz});
        xStream.setMode(XStream.NO_REFERENCES);
        xStream.alias("xml", clazz);
        return clazz.cast(xStream.fromXML(xml));
    }
}
