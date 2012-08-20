package com.zuora.core.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LifeCycleEventListeners {
   Class<? extends ILifeCycleEventListenter>[] value() default {};
}
