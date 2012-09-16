package net.madz.core.lifecycle.meta;

import net.madz.core.lifecycle.IReactiveObject;
import net.madz.core.lifecycle.IState;
import net.madz.core.lifecycle.ITransition;
import net.madz.core.meta.MetaData;

public interface StateMachineMetaData<R extends IReactiveObject, S extends IState<R, S>, T extends ITransition>
	extends MetaData {

    void addFinalState(StateMetaData<R, S> finalState);

    void addTransientState(StateMetaData<R, S> state);

    void addTransition(TransitionMetaData transition);

    StateMetaData<R, S> getStateMetaData(S state);

    TransitionMetaData getTransitionMetaData(ITransition transition);

    TransitionMetaData getRedoTransition();

    TransitionMetaData getRecoverTransition();

    TransitionMetaData getCorruptTransition();

    StateMetaData<R, S> getInitialState();
}