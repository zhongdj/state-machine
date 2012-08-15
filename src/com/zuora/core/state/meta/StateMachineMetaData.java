package com.zuora.core.state.meta;

import com.zuora.core.state.IReactiveObject;
import com.zuora.core.state.IState;
import com.zuora.core.state.ITransition;

public interface StateMachineMetaData<R extends IReactiveObject<S>, S extends IState, T extends ITransition> extends MetaData {

   void addFinalState(StateMetaData finalState);

   void addTransientState(StateMetaData state);

   void addTransition(TransitionMetaData transition);

}