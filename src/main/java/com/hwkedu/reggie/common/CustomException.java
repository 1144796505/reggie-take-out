package com.hwkedu.reggie.common;

import org.apache.commons.lang.StringUtils;

/**
 * @author 侯文柯
 * @version 1.0
 * 自定义异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String ex){
        super(ex);
    }

}
