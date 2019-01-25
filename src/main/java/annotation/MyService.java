package annotation;

import java.lang.annotation.*;

/**
 * Created by zhuran on 2019/1/17 0017
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyService {
    String value() default "";
}
