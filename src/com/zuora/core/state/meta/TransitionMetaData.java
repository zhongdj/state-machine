package com.zuora.core.state.meta;

import java.lang.reflect.Method;

import com.zuora.core.state.ITransition;

public interface TransitionMetaData extends MetaData {

   public static enum TransitionTypeEnum {
      Corrupt, Recover, Redo, Other
   }

   TransitionTypeEnum getType();

   <T extends ITransition> T getTransition();

   long getTimeout();

   Method getTransitionMethod();
}