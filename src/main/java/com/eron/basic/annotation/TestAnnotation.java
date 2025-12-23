

package com.eron.basic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.swing.DefaultBoundedRangeModel;

/**
 * 设计自定义注解
 *
 * @author ERON_AMD
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TestAnnotation {
    String name() default "test";

    String address() default "default";

    int age() default 0;

    SimpleTestColor color() default SimpleTestColor.GREEN;

    enum SimpleTestColor {
        GREEN, BLUE, RED, BLACK, WHITE
    }

}




