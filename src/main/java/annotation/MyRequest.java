package annotation;

import java.lang.annotation.*;

/**
 * Created by zhuran on 2019/1/25 0025
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequest {
    String path() default "";

    String type() default "";
}
