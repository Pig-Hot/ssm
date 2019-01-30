package annotation;

import java.lang.annotation.*;

/**
 * Created by zhuran on 2019/1/23 0023
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyBean {
}
