package net.madz.core.lifecycle.meta.impl;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import net.madz.core.lifecycle.IState;
import net.madz.core.lifecycle.annotations.StateMachine;
import net.madz.core.lifecycle.annotations.StateSet;
import net.madz.core.lifecycle.annotations.Transition;
import net.madz.core.lifecycle.annotations.TransitionSet;
import net.madz.core.lifecycle.meta.StateMachineMetaDataBuilder;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.StateMetaData;
import net.madz.core.lifecycle.meta.StateMetaDataBuilder;
import net.madz.core.lifecycle.meta.TransitionMetaData;
import net.madz.core.meta.MetaData;

public class StateMachineMetaDataBuilderImpl implements
	StateMachineMetaDataBuilder {

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public StateMachineMetaData<?, ?, ?> build(MetaData parent,
	    AnnotatedElement element) {
	if (!(element instanceof Class)) {
	    throw new IllegalArgumentException("Should be interface class.");
	}
	final StateMachine stateMachine = element
		.getAnnotation(StateMachine.class);
	if (null == stateMachine) {
	    throw new IllegalArgumentException(
		    "No StateMachine Annotation found.");
	}

	Class reactiveObjectClass = (Class) element;

	final StateSet states = stateMachine.states();
	Class<? extends IState> stateEnumClass = states.value();

	final StateMachineMetaDataImpl<?, ?, ?> m = new StateMachineMetaDataImpl(
		reactiveObjectClass, stateEnumClass);

	StateMetaData initialState = null;
	final Field[] stateFields = stateEnumClass.getFields();
	final ArrayList<StateMetaData<?, ?>> finalStateList = new ArrayList<StateMetaData<?, ?>>();
	final ArrayList<StateMetaData<?, ?>> transientStateList = new ArrayList<StateMetaData<?, ?>>();
	for (Field stateField : stateFields) {
	    if (!stateEnumClass.isAssignableFrom(stateField.getType())
		    || !Modifier.isStatic(stateField.getModifiers())) {
		continue;
	    }

	    final StateMetaDataBuilder stateMetaDataBuilder = new StateMetaDataBuilderImpl();
	    final StateMetaData stateMetaData = stateMetaDataBuilder.build(m,
		    stateField);
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

	m.setInitialState(initialState);

	TransitionMetaData corruptTransition = null;
	TransitionMetaData recoverTransition = null;
	TransitionMetaData redoTransition = null;

	final TransitionSet transitions = stateMachine.transitions();
	final Class transitionEnumClass = transitions.value();
	final Field[] transitionFields = transitionEnumClass.getFields();
	final ArrayList<TransitionMetaData> transitionList = new ArrayList<TransitionMetaData>();
	for (Field transitionField : transitionFields) {
	    if (!transitionEnumClass
		    .isAssignableFrom(transitionField.getType())
		    || !Modifier.isStatic(transitionField.getModifiers())) {
		continue;
	    }

	    final TransitionMetaDataBuilderImpl transitionMetaDataBuilder = new TransitionMetaDataBuilderImpl();
	    final TransitionMetaDataImpl transitionMetaData = (TransitionMetaDataImpl) transitionMetaDataBuilder
		    .build(m, transitionField);

	    final String transitionName = transitionField.getName();
	    Method[] methods = reactiveObjectClass.getMethods();
	    for (Method method : methods) {
		if (method.getName().equalsIgnoreCase(transitionName)) {
		    transitionMetaData.setTransitionMethod(method);
		    break;
		} else {
		    final Transition transitionAnnotation = method
			    .getAnnotation(Transition.class);
		    if (null != transitionAnnotation) {
			if (!Transition.NULL.equals(transitionAnnotation
				.value())) {
			    if (method.getName().equalsIgnoreCase(
				    transitionAnnotation.value())) {
				transitionMetaData.setTransitionMethod(method);
				break;
			    }
			}
		    }
		}
	    }

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

	for (TransitionMetaData transition : transitionList) {
	    m.addTransition(transition);
	}

	for (StateMetaData state : finalStateList) {
	    m.addFinalState(state);
	}

	for (StateMetaData state : transientStateList) {
	    m.addTransientState(state);
	}

	m.setCorruptTransition(corruptTransition);
	m.setRecoverTransition(recoverTransition);
	m.setRedoTransition(redoTransition);

	return m;
    }
}
