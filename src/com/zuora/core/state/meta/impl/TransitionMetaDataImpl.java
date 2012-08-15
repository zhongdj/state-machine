package com.zuora.core.state.meta.impl;

import com.zuora.core.state.ITransition;
import com.zuora.core.state.meta.MetaData;
import com.zuora.core.state.meta.TransitionMetaData;

public class TransitionMetaDataImpl implements MetaData, TransitionMetaData {

   protected final TransitionTypeEnum type;
   protected final ITransition transition;
   protected final long timeout;

   public TransitionMetaDataImpl(TransitionTypeEnum type, ITransition transition, long timeout) {
      super();
      this.type = type;
      this.transition = transition;
      this.timeout = timeout;
   }

   @Override
   public TransitionTypeEnum getType() {
      return type;
   }

   @Override
   public ITransition getTransition() {
      return transition;
   }

   @Override
   public long getTimeout() {
      return timeout;
   }

}
