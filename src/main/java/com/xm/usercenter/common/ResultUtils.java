package com.xm.usercenter.common;

/**
 * result feedback
 */
public class ResultUtils {

    public static<T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"Ok");
    }

    public static  BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static  BaseResponse error(ErrorCode errorCode, String msg, String description) {
        return  new BaseResponse(errorCode.getCode(),null,msg,description);
    }

    public static  BaseResponse error(ErrorCode errorCode, String description) {
        return  new BaseResponse(errorCode.getCode(),null,errorCode.getMsg(),description);
    }
}
