package com.zuora.core.state.annotations.action;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Timeout {
    long value() default 300000L;
}
