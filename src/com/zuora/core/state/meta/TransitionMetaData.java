package com.zuora.core.state.meta;

import com.zuora.core.state.ITransition;

public interface TransitionMetaData extends MetaData {

   public static enum TransitionTypeEnum {
      Corrupt, Recover, Redo, Other
   }

   TransitionTypeEnum getType();

   ITransition getTransition();

   long getTimeout();

}