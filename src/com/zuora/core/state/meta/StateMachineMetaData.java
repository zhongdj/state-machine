package com.zuora.core.state.meta;

import com.zuora.core.state.IReactiveObject;
import com.zuora.core.state.IState;
import com.zuora.core.state.ITransition;

public interface StateMachineMetaData<R extends IReactiveObject, S extends IState<R, S>, T extends ITransition> extends MetaData {

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