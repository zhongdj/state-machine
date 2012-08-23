package com.zuora.core.state;

import java.io.Serializable;


public final class StateContext<R extends IReactiveObject, S> implements Serializable, Cloneable {

   private static final long serialVersionUID = 1L;

   private final R reactiveObject;
   
   public StateContext(R reactiveObject) {
      this.reactiveObject = reactiveObject;
   }

   public S getCurrentState() {
      return this.reactiveObject.getState();
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   
   
}
