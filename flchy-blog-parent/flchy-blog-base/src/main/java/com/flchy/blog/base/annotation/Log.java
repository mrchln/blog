package com.flchy.blog.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.flchy.blog.base.enums.OperateCodeEnum;

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
	/**
	 * 接口名称
	 * @return
	 */
    String value() default "";
    OperateCodeEnum type() default OperateCodeEnum.SELECT;
    
}
