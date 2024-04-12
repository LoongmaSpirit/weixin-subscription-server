package org.loongma.weixin.subserver.common.constants;

/**
 * 错误信息定义
 */
public enum ErrorCode {
    // 操作成功
    OK(200, "操作成功"),

    // 未知错误
    RUNTIME_ERR(500, "未知错误"),

    // 参数错误
    PARAM_NOT_BLANK(10001, "参数不允许为空"),

    // 未知应用
    UNKNOWN_APPLICATION(20001, "未知应用")
    ;


    private final int code;
    private final String desc;

    ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
