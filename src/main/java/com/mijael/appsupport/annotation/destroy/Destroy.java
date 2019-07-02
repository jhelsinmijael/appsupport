package com.mijael.appsupport.annotation.destroy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Destroy {

    int time() default 20000;
    Type type() default Type.ALERT_DIALOG;

}
