package com.zuora.core.state.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.zuora.core.state.IReactiveObject;
import com.zuora.core.state.IState;

@Retention(RetentionPolicy.RUNTIME)
public @interface StateSet {

   Class<? extends IState<? extends IReactiveObject>> value();

}
