package com.lingsha.reggie.common;

/**
 * 自定义业务异常
 * @auther Ling.Sha
 * @date 2022/8/7 - 15:47
 */
public class CustomException extends RuntimeException{
    public CustomException(String message)
    {
        super(message);
    }
}
