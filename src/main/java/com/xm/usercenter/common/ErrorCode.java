package com.xm.usercenter.common;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor

public enum ErrorCode {

    SUCCESS(0,"SUCCESS",""),
    PARAMS_ERROR(40000,"PARAMETERS ERROR",""),
    PARAMS_NULL(40001,"PARAMETERS NULL",""),
    NO_AUTH_ERROR(40100,"NO PERMISSION",""),
    NO_LOGIN(40101,"NO LOGIN",""),
    SYSTEM_ERROR(50000,"SYSTEM ERROR","")
    ;

    private final int code;

    private final String msg;

    private final String description;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDescription() {
        return description;
    }
}
