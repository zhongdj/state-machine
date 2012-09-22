package net.madz.core.lifecycle.meta;

import java.lang.reflect.Method;

import net.madz.core.common.Dumpable;
import net.madz.core.lifecycle.ITransition;
import net.madz.core.meta.MetaData;

public interface TransitionMetaData extends MetaData, Dumpable {

    public static enum TransitionTypeEnum {
	Corrupt, Recover, Redo, Other
    }

    TransitionTypeEnum getType();

    <T extends ITransition> T getTransition();

    long getTimeout();

    Method getTransitionMethod();
}