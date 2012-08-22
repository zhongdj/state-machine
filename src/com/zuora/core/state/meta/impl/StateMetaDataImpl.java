package com.zuora.core.state.meta.impl;

import com.zuora.core.state.IReactiveObject;
import com.zuora.core.state.IState;
import com.zuora.core.state.ITransition;
import com.zuora.core.state.meta.MetaData;
import com.zuora.core.state.meta.StateMetaData;

public class StateMetaDataImpl<R extends IReactiveObject> implements MetaData, StateMetaData<R> {

   private final IState<R> state;

   private final StateTypeEnum type;

   public StateMetaDataImpl(IState<R> state, StateTypeEnum type) {
      super();
      this.state = state;
      this.type = type;
   }

   @Override
   public IState<R> getState() {
      return state;
   }

   @Override
   public StateTypeEnum getType() {
      return type;
   }

   @Override
   public boolean illegalTransition(ITransition transition) {
      return !state.getTransitionFunction().containsKey(transition);
   }

   @Override
   public IState<R> nextState(ITransition transition) {
      return state.getTransitionFunction().get(transition);
   }
}
