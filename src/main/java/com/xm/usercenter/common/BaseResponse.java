package com.xm.usercenter.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {
    private int code;

    private T data;

    private String msg;

    private String description;

    public BaseResponse(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
        this.msg = "";
    }

    public BaseResponse(int code, String msg) {
        this.code = code;
        this.msg = "";
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null, errorCode.getMsg(),errorCode.getDescription());
    }
}

