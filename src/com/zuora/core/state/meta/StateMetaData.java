package com.zuora.core.state.meta;

import com.zuora.core.state.IState;
import com.zuora.core.state.ITransition;

public interface StateMetaData extends MetaData {

   public static enum StateTypeEnum {
      Initial, End, Running, Stopped, Corrupted, Waiting, Unknown
   }

   IState getState();

   StateTypeEnum getType();

   boolean illegalTransition(ITransition transition);

   IState nextState(ITransition transition);

}