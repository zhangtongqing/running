package com.peipao.framework.annotation;

import java.lang.annotation.*;

/**
 * 方法名称：SystemControllerLog
 * 功能描述：系统级别的controller层自定义注解
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/3 16:01
 * 修订记录：
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})//作用于参数或方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemControllerLog {
    String description() default "";
}
