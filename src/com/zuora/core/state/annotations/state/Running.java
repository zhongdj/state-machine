package com.zuora.core.state.annotations.state;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Running {

   int priority() default 0;
}
