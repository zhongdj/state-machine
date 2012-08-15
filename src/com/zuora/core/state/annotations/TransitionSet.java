package com.zuora.core.state.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.zuora.core.state.ITransition;

@Retention(RetentionPolicy.RUNTIME)
public @interface TransitionSet {

   Class<? extends ITransition> value();

}
