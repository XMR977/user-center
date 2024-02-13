package com.xm.usercenter.exception;


import com.xm.usercenter.common.BaseResponse;
import com.xm.usercenter.common.ErrorCode;
import com.xm.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse busincessExceptionHandler(BusinessException e){
        log.error("Business Exception: ", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(),"");
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtime Exception: ", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"runtime error","");
    }
}
