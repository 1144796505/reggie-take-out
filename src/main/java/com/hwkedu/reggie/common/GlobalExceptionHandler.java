package com.hwkedu.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author 侯文柯
 * @version 1.0
 * 全局异常处理器
 */
//@ControllerAdvice将当前类标识为异常处理的组件
    //annotations ={RestController.class, Controller.class}
    @ControllerAdvice()
    @ResponseBody //java对象转为json格式的数据
    @Slf4j
public class GlobalExceptionHandler {
    //@ExceptionHandler用于设置所标识方法处理的异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] str = ex.getMessage().split(" ");
            String msg = str[2] + "已存在";
            return R.error(msg);
        }
        return R.error("失败了");
    }
    //@ExceptionHandler用于设置所标识方法处理的异常
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }
}
