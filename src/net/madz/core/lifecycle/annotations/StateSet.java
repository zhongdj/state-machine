package net.madz.core.lifecycle.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.madz.core.lifecycle.IReactiveObject;
import net.madz.core.lifecycle.IState;

@Retention(RetentionPolicy.RUNTIME)
public @interface StateSet {

    Class<? extends IState<? extends IReactiveObject, ?>> value();

}
