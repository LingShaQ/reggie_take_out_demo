package com.lingsha.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})//拦截那些注解
@ResponseBody//返回json数据
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex)
    {
//        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry"))
        {
            //违反唯一约束,unique
            String[] split = ex.getMessage().split(" ");//通过空格分开找到第三个就是我们设置的重复名字
            String msg = split[2]; //取出来重复的名字ddddd
            return  R.error(msg+"名字重复");
        }
        return R.error("未知错误");
    }
    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> CustomException(CustomException ex)
    {
//        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
