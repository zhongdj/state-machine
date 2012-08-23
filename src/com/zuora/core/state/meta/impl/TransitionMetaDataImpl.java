package com.zuora.core.state.meta.impl;

import java.lang.reflect.Method;

import com.zuora.core.state.ITransition;
import com.zuora.core.state.meta.MetaData;
import com.zuora.core.state.meta.StateMachineMetaData;
import com.zuora.core.state.meta.TransitionMetaData;

public class TransitionMetaDataImpl implements MetaData, TransitionMetaData {

   protected final TransitionTypeEnum type;
   protected final ITransition transition;
   protected final long timeout;
   protected final StateMachineMetaData<?, ?, ?> parent;
   protected Method transitionMethod;

   public TransitionMetaDataImpl(StateMachineMetaData<?, ?, ?> parent, TransitionTypeEnum type, ITransition transition, long timeout) {
      super();
      this.parent = parent;
      this.type = type;
      this.transition = transition;
      this.timeout = timeout;
   }

   @Override
   public TransitionTypeEnum getType() {
      return type;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T extends ITransition> T  getTransition() {
      return (T) transition;
   }

   @Override
   public long getTimeout() {
      return timeout;
   }

   @Override
   public StateMachineMetaData<?, ?, ?> getParent() {
      return parent;
   }

   @Override
   public Method getTransitionMethod() {
      return this.transitionMethod;
   }
   
   public void setTransitionMethod(Method method) {
      this.transitionMethod = method;
   }

}
