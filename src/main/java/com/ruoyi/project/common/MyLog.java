package com.ruoyi.project.common;
import java.lang.annotation.*;

/**
 * Created by Lovelyz
 * on 2019-07-31 16:39
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
//生成文档
public @interface MyLog {
    String value() default "";
}




