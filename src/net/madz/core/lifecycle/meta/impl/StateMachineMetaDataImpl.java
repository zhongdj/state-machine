package net.madz.core.lifecycle.meta.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.madz.core.common.DottedPath;
import net.madz.core.lifecycle.IReactiveObject;
import net.madz.core.lifecycle.IState;
import net.madz.core.lifecycle.ITransition;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.StateMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData;
import net.madz.core.meta.MetaData;

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

    public StateMachineMetaDataImpl(Class<R> reactiveObjectClass,
	    Class<S> stateEnumClass) {
	this.reactiveObjectClass = reactiveObjectClass;
	this.stateEnumClass = stateEnumClass;
    }

    public Class<T> getTransitionEnumClass() {
	return transitionEnumClass;
    }

    public void setTransitionEnumClass(Class<T> transitionEnumClass) {
	this.transitionEnumClass = transitionEnumClass;
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
	return null;
    }

    @Override
    public DottedPath getDottedPath() {
	// TODO Auto-generated method stub
	return null;
    }
}
