package com.zuora.core.state.meta;

import com.zuora.core.state.IReactiveObject;
import com.zuora.core.state.IState;
import com.zuora.core.state.ITransition;

public interface StateMetaData<R extends IReactiveObject, S extends IState<R, S>> extends MetaData {

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