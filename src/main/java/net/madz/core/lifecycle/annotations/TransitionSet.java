package net.madz.core.lifecycle.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.madz.core.lifecycle.ITransition;

@Retention(RetentionPolicy.RUNTIME)
public @interface TransitionSet {

    Class<? extends ITransition> value();

}
