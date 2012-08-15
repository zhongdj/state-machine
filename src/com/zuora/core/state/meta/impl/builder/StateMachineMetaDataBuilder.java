package com.zuora.core.state.meta.impl.builder;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.zuora.core.state.IState;
import com.zuora.core.state.annotations.StateMachine;
import com.zuora.core.state.annotations.StateSet;
import com.zuora.core.state.annotations.TransitionSet;
import com.zuora.core.state.meta.MetaDataBuilder;
import com.zuora.core.state.meta.StateMachineMetaData;
import com.zuora.core.state.meta.StateMetaData;
import com.zuora.core.state.meta.TransitionMetaData;
import com.zuora.core.state.meta.impl.StateMachineMetaDataImpl;

@SuppressWarnings("rawtypes")
public class StateMachineMetaDataBuilder implements MetaDataBuilder<StateMachineMetaData> {

   @SuppressWarnings("unchecked")
   public StateMachineMetaData build(AnnotatedElement element) {
      if (!(element instanceof Class)) {
         throw new IllegalArgumentException("Should be interface class.");
      }
      final StateMachine stateMachine = element.getAnnotation(StateMachine.class);
      if (null == stateMachine) {
         throw new IllegalArgumentException("No StateMachine Annotation found.");
      }

      Class reactiveObjectClass = (Class) element;

      final StateSet states = stateMachine.states();
      Class<? extends IState> stateEnumClass = states.value();

      StateMetaData initialState = null;
      final Field[] stateFields = stateEnumClass.getFields();
      final ArrayList<StateMetaData> finalStateList = new ArrayList<StateMetaData>();
      final ArrayList<StateMetaData> transientStateList = new ArrayList<StateMetaData>();
      for (Field stateField : stateFields) {
         if (!stateEnumClass.isAssignableFrom(stateField.getType())
            // || !stateField.isAccessible()
            || !Modifier.isStatic(stateField.getModifiers())) {
            continue;
         }

         final StateMetaDataBuilder stateMetaDataBuilder = new StateMetaDataBuilder();
         final StateMetaData stateMetaData = stateMetaDataBuilder.build(stateField);
         switch (stateMetaData.getType()) {
            case Initial:
               initialState = stateMetaData;
               break;
            case End:
               finalStateList.add(stateMetaData);
               break;
            default:
               transientStateList.add(stateMetaData);
               break;
         }

      }

      TransitionMetaData corruptTransition = null;
      TransitionMetaData recoverTransition = null;
      TransitionMetaData redoTransition = null;

      final TransitionSet transitions = stateMachine.transitions();
      final Class transitionEnumClass = transitions.value();
      final Field[] transitionFields = transitionEnumClass.getFields();
      final ArrayList<TransitionMetaData> transitionList = new ArrayList<TransitionMetaData>();
      for (Field transitionField : transitionFields) {
         if (!transitionEnumClass.isAssignableFrom(transitionField.getType())
             // || !transitionField.isAccessible()
            || Modifier.isStatic(transitionField.getModifiers())) {
            continue;
         }

         final TransitionMetaDataBuilder transitionMetaDataBuilder = new TransitionMetaDataBuilder();
         final TransitionMetaData transitionMetaData = transitionMetaDataBuilder.build(transitionField);

         switch (transitionMetaData.getType()) {
            case Corrupt:
               corruptTransition = transitionMetaData;

               break;
            case Recover:
               recoverTransition = transitionMetaData;
               break;
            case Redo:
               redoTransition = transitionMetaData;
               break;
            default:
               break;
         }
         transitionList.add(transitionMetaData);
      }

      final StateMachineMetaData m = new StateMachineMetaDataImpl(reactiveObjectClass, stateEnumClass, transitionEnumClass, initialState, corruptTransition,
         recoverTransition, redoTransition);

      for (TransitionMetaData transition : transitionList) {
         m.addTransition(transition);
      }

      for (StateMetaData state : finalStateList) {
         m.addFinalState(state);
      }

      for (StateMetaData state : transientStateList) {
         m.addTransientState(state);
      }

      return m;
   }
}
