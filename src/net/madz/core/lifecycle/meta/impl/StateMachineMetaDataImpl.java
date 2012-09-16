package net.madz.core.lifecycle.meta.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.madz.core.common.DottedPath;
import net.madz.core.common.Dumper;
import net.madz.core.common.ParameterString;
import net.madz.core.lifecycle.IReactiveObject;
import net.madz.core.lifecycle.IState;
import net.madz.core.lifecycle.ITransition;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.StateMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData;
import net.madz.core.meta.MetaData;
import net.madz.core.verification.VerificationFailureSet;

public class StateMachineMetaDataImpl<R extends IReactiveObject, S extends IState<R, S>, T extends ITransition>
	implements MetaData, StateMachineMetaData<R, S, T> {

    protected Class<R> reactiveObjectClass;
    protected Class<S> stateEnumClass;
    protected Class<T> transitionEnumClass;

    protected StateMetaData<R, S> initialState;
    protected final List<StateMetaData<R, S>> allStates = new ArrayList<StateMetaData<R, S>>();
    protected final List<StateMetaData<R, S>> finalStates = new ArrayList<StateMetaData<R, S>>();;
    protected final List<StateMetaData<R, S>> transientStates = new ArrayList<StateMetaData<R, S>>();
    protected final HashMap<S, StateMetaData<R, S>> stateIndexMap = new HashMap<S, StateMetaData<R, S>>();

    protected final List<TransitionMetaData> allTransitions = new ArrayList<TransitionMetaData>();;
    protected TransitionMetaData corruptTransition;
    protected TransitionMetaData recoverTransition;
    protected TransitionMetaData redoTransition;
    protected final HashMap<ITransition, TransitionMetaData> transitionIndexMap = new HashMap<ITransition, TransitionMetaData>();
    protected final MetaData parent;
    protected final DottedPath dottedPath;

    public StateMachineMetaDataImpl(MetaData parent,
	    Class<R> reactiveObjectClass, Class<S> stateEnumClass) {

	this.parent = parent;

	if (null == parent) {
	    this.dottedPath = DottedPath.parse(reactiveObjectClass.getName()
		    + ".StateMachine");
	} else {
	    this.dottedPath = parent.getDottedPath().append(
		    reactiveObjectClass.getName() + ".StateMachine");
	}
	this.reactiveObjectClass = reactiveObjectClass;
	this.stateEnumClass = stateEnumClass;
    }

    public Class<T> getTransitionEnumClass() {
	return transitionEnumClass;
    }

    public void setTransitionEnumClass(Class<T> transitionEnumClass) {
	this.transitionEnumClass = transitionEnumClass;
    }

    @SuppressWarnings("unchecked")
    public T getTranstion(String name) throws Exception {
	Field enumField = transitionEnumClass.getField(name);
	return (T) enumField.get(transitionEnumClass);
    }

    @Override
    public StateMetaData<R, S> getInitialState() {
	return initialState;
    }

    public void setInitialState(StateMetaData<R, S> initialState) {
	this.initialState = initialState;
	addState(initialState);
    }

    @Override
    public void addFinalState(StateMetaData<R, S> finalState) {
	this.finalStates.add(finalState);
	addState(finalState);
    }

    private void addState(StateMetaData<R, S> state) {
	this.allStates.add(state);
	this.stateIndexMap.put(state.getState(), state);
    }

    @Override
    public void addTransientState(StateMetaData<R, S> state) {
	this.transientStates.add(state);
	addState(state);
    }

    @Override
    public void addTransition(TransitionMetaData transition) {
	this.allTransitions.add(transition);
	this.transitionIndexMap.put(transition.getTransition(), transition);
    }

    @Override
    public TransitionMetaData getCorruptTransition() {
	return corruptTransition;
    }

    public void setCorruptTransition(TransitionMetaData corruptTransition) {
	this.corruptTransition = corruptTransition;
	this.addTransition(corruptTransition);
    }

    @Override
    public TransitionMetaData getRecoverTransition() {
	return recoverTransition;
    }

    public void setRecoverTransition(TransitionMetaData recoverTransition) {
	this.recoverTransition = recoverTransition;
	this.addTransition(recoverTransition);
    }

    @Override
    public TransitionMetaData getRedoTransition() {
	return redoTransition;
    }

    public void setRedoTransition(TransitionMetaData redoTransition) {
	this.redoTransition = redoTransition;
	this.addTransition(redoTransition);
    }

    @Override
    public StateMetaData<R, S> getStateMetaData(S state) {
	return this.stateIndexMap.get(state);
    }

    @Override
    public TransitionMetaData getTransitionMetaData(ITransition transition) {
	return this.transitionIndexMap.get(transition);
    }

    @Override
    public MetaData getParent() {
	return parent;
    }

    @Override
    public DottedPath getDottedPath() {
	return dottedPath;
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
	for (StateMetaData<R, S> m : allStates) {
	    m.verifyMetaData(verificationSet);
	}

	for (TransitionMetaData m : allTransitions) {
	    m.verifyMetaData(verificationSet);
	}

    }

    @Override
    public void dump(Dumper dumper) {
	dumper.println(toString()).indent()
		.dump(Collections.unmodifiableList(allStates))
		.dump(Collections.unmodifiableList(allTransitions));
    }

    @Override
    public final String toString() {
	return toString(new ParameterString(getClass().getSimpleName()))
		.toString();
    }

    public ParameterString toString(ParameterString sb) {
	sb.append("name", this.dottedPath.getAbsoluteName());
	sb.append("states", this.stateIndexMap);
	sb.append("transition", this.transitionIndexMap);
	return sb;
    }

    @Override
    public T getTransition(String name) {
	for (TransitionMetaData t : allTransitions) {
	    if (t.getDottedPath().getName().equals(name)) {
		return t.getTransition();
	    }
	}
	throw new IllegalStateException("Cannot find transition: " + name
		+ " within stateMachine: " + getDottedPath().getAbsoluteName());
    }

}
