package com.boc.cloud.api.exception;

/**
 * Common client error exception (with i18n capability).
 */
public class Http403Exception extends RuntimeException {

    private String errorCode;
    private String msgKey;
    private Object[] args;

    public Http403Exception(String errorCode, String msgKey, Object... args) {
        super();
        this.errorCode = errorCode;
        this.msgKey = msgKey;
        this.args = args;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
