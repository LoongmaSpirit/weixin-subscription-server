package org.loongma.weixin.subserver.common.utils;

public class BytesToHexStrUtil {
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String translate(byte[] bytes) {
        StringBuilder str = new StringBuilder();

        for (byte aByte : bytes) {
            char[] buf = new char[2];
            buf[0] = HEX_CHAR[(aByte >>> 4) & 0x0f];
            buf[1] = HEX_CHAR[aByte & 0x0f];
            str.append(new String(buf));
        }

        return str.toString();
    }
}
