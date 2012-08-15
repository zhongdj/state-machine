package com.zuora.core.state.meta.impl;

import java.util.ArrayList;
import java.util.List;

import com.zuora.core.state.IReactiveObject;
import com.zuora.core.state.IState;
import com.zuora.core.state.ITransition;
import com.zuora.core.state.meta.MetaData;
import com.zuora.core.state.meta.StateMachineMetaData;
import com.zuora.core.state.meta.StateMetaData;
import com.zuora.core.state.meta.TransitionMetaData;

public class StateMachineMetaDataImpl<R extends IReactiveObject<S>, S extends IState, T extends ITransition> implements MetaData, StateMachineMetaData<R, S, T> {

   protected Class<R> reactiveObjectClass;
   protected Class<S> stateEnumClass;
   protected Class<T> transitionEnumClass;

   protected StateMetaData initialState;
   protected final List<StateMetaData> allStates = new ArrayList<StateMetaData>();
   protected final List<StateMetaData> finalStates = new ArrayList<StateMetaData>();;
   protected final List<StateMetaData> transientStates = new ArrayList<StateMetaData>();;

   protected final List<TransitionMetaData> allTransitions = new ArrayList<TransitionMetaData>();;
   protected TransitionMetaData corruptTransition;
   protected TransitionMetaData recoverTransition;
   protected TransitionMetaData redoTransition;

   public StateMachineMetaDataImpl(Class<R> reactiveObjectClass, Class<S> stateEnumClass, Class<T> transitionEnumClass, StateMetaData initialState, TransitionMetaData corruptTransition,
      TransitionMetaData recoverTransition, TransitionMetaData redoTransition) {
      super();
      this.reactiveObjectClass = reactiveObjectClass;
      this.stateEnumClass = stateEnumClass;
      this.transitionEnumClass = transitionEnumClass;
      this.initialState = initialState;
      this.corruptTransition = corruptTransition;
      this.recoverTransition = recoverTransition;
      this.redoTransition = redoTransition;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.zuora.core.state.meta.IStateMachineMetaData#addFinalState(com.zuora.core.state.meta.impl.StateMetaDataImpl)
    */
   @Override
   public void addFinalState(StateMetaData finalState) {
      this.finalStates.add(finalState);
      addState(finalState);
   }

   private void addState(StateMetaData state) {
      this.allStates.add(state);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.zuora.core.state.meta.IStateMachineMetaData#addTransientState(com.zuora.core.state.meta.impl.StateMetaDataImpl)
    */
   @Override
   public void addTransientState(StateMetaData state) {
      this.transientStates.add(state);
      addState(state);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.zuora.core.state.meta.IStateMachineMetaData#addTransition(com.zuora.core.state.meta.impl.TransitionMetaDataImpl)
    */
   @Override
   public void addTransition(TransitionMetaData transition) {
      this.allTransitions.add(transition);
   }
}
