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

public class StateMachineMetaDataImpl<R extends IReactiveObject, S extends IState<R>, T extends ITransition> implements MetaData, StateMachineMetaData<R, S, T> {

   protected Class<R> reactiveObjectClass;
   protected Class<S> stateEnumClass;
   protected Class<T> transitionEnumClass;

   protected StateMetaData<R> initialState;
   protected final List<StateMetaData<R>> allStates = new ArrayList<StateMetaData<R>>();
   protected final List<StateMetaData<R>> finalStates = new ArrayList<StateMetaData<R>>();;
   protected final List<StateMetaData<R>> transientStates = new ArrayList<StateMetaData<R>>();;

   protected final List<TransitionMetaData> allTransitions = new ArrayList<TransitionMetaData>();;
   protected TransitionMetaData corruptTransition;
   protected TransitionMetaData recoverTransition;
   protected TransitionMetaData redoTransition;

   public StateMachineMetaDataImpl(Class<R> reactiveObjectClass, Class<S> stateEnumClass, Class<T> transitionEnumClass, StateMetaData<R> initialState, TransitionMetaData corruptTransition,
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

   @Override
   public void addFinalState(StateMetaData<R> finalState) {
      this.finalStates.add(finalState);
      addState(finalState);
   }

   private void addState(StateMetaData<R> state) {
      this.allStates.add(state);
   }

   @Override
   public void addTransientState(StateMetaData<R> state) {
      this.transientStates.add(state);
      addState(state);
   }

   @Override
   public void addTransition(TransitionMetaData transition) {
      this.allTransitions.add(transition);
   }
}
