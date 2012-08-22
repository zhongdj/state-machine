package com.zuora.core.state.meta;

import com.zuora.core.state.IReactiveObject;
import com.zuora.core.state.IState;
import com.zuora.core.state.ITransition;

public interface StateMetaData<R extends IReactiveObject> extends MetaData {

   public static enum StateTypeEnum {
      Initial, End, Running, Stopped, Corrupted, Waiting, Unknown
   }

   IState<R> getState();

   StateTypeEnum getType();

   boolean illegalTransition(ITransition transition);

   IState<R> nextState(ITransition transition);

}