package com.zuora.core.state;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.zuora.core.state.annotations.StateMachine;
import com.zuora.core.state.annotations.StateSet;


public final class StateContext<S extends IState, T extends IReactiveObject<S>> implements Serializable, Cloneable {

   private static final long serialVersionUID = 1L;

   public StateContext(T reactiveObject) {
      if (null == reactiveObject.getState()) {
         StateMachine stateMachine = (StateMachine) Utils.reactiveInterface(reactiveObject.getClass()).getAnnotation(StateMachine.class);
         StateSet states = stateMachine.states();
         Class<? extends IState> stateEnumClass = states.value();
         
         
         Field[] fields = stateEnumClass.getFields();
         for (Field field : fields) {
            System.out.println(field.getName());
         }
      }
   }

   public IState getCurrentState() {
      return null;
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   
   
}
