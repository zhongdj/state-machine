package com.zuora.core.state.meta;

import com.zuora.core.state.IReactiveObject;
import com.zuora.core.state.IState;
import com.zuora.core.state.ITransition;

public interface StateMachineMetaData<R extends IReactiveObject, S extends IState<R>, T extends ITransition> extends MetaData {

   void addFinalState(StateMetaData<R> finalState);

   void addTransientState(StateMetaData<R> state);

   void addTransition(TransitionMetaData transition);

}