package com.zuora.core.state.meta.impl;

import com.zuora.core.state.IState;
import com.zuora.core.state.ITransition;
import com.zuora.core.state.meta.MetaData;
import com.zuora.core.state.meta.StateMetaData;

public class StateMetaDataImpl implements MetaData, StateMetaData {

   private final IState state;

   private final StateTypeEnum type;

   public StateMetaDataImpl(IState state, StateTypeEnum type) {
      super();
      this.state = state;
      this.type = type;
   }

   @Override
   public IState getState() {
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
   public IState nextState(ITransition transition) {
      return state.getTransitionFunction().get(transition);
   }
}
