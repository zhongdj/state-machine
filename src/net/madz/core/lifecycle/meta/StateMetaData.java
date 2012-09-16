package net.madz.core.lifecycle.meta;

import net.madz.core.common.Dumpable;
import net.madz.core.lifecycle.IReactiveObject;
import net.madz.core.lifecycle.IState;
import net.madz.core.lifecycle.ITransition;
import net.madz.core.meta.MetaData;

public interface StateMetaData<R extends IReactiveObject, S extends IState<R, S>>
	extends MetaData, Dumpable {

    public static enum StateTypeEnum {
	Initial, End, Running, Stopped, Corrupted, Waiting, Unknown
    }

    S getState();

    StateTypeEnum getType();

    boolean illegalTransition(ITransition transition);

    S nextState(ITransition transition);

    boolean containsCorruptTransition();

    TransitionMetaData getCorruptTransitionMetaData();
}