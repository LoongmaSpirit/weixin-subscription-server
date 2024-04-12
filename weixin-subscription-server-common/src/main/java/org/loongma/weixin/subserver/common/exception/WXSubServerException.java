package org.loongma.weixin.subserver.common.exception;

import org.loongma.weixin.subserver.common.constants.ErrorCode;

public class WXSubServerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected ErrorCode errCode;

    public WXSubServerException() {}

    public WXSubServerException(ErrorCode errCode) {
        this.errCode = errCode;
    }

    public WXSubServerException(ErrorCode errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public WXSubServerException(ErrorCode errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
    }

    public WXSubServerException(ErrorCode errCode, Throwable cause) {
        super(cause);
        this.errCode = errCode;
    }
}
