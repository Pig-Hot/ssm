package annotation;

import java.lang.annotation.*;

/**
 * Created by zhuran on 2019/1/18 0018
 */
@Target({
        ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAutowired {
    boolean required() default true;
}
