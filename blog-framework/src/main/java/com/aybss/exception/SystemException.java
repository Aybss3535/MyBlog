package com.aybss.exception;

import com.aybss.enums.AppHttpCodeEnum;

public class SystemException extends RuntimeException {

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum){
        super(httpCodeEnum.getMsg());
        code = httpCodeEnum.getCode();
        msg = httpCodeEnum.getMsg();
    }
}
