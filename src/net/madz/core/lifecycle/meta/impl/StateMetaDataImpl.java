package net.madz.core.lifecycle.meta.impl;

import net.madz.core.common.DottedPath;
import net.madz.core.lifecycle.IReactiveObject;
import net.madz.core.lifecycle.IState;
import net.madz.core.lifecycle.ITransition;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.StateMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData.TransitionTypeEnum;
import net.madz.core.meta.MetaData;

public class StateMetaDataImpl<R extends IReactiveObject, S extends IState<R, S>>
	implements MetaData, StateMetaData<R, S> {

    private final S state;

    private final StateTypeEnum type;

    private final StateMachineMetaData<R, S, ?> parent;

    public StateMetaDataImpl(StateMachineMetaData<R, S, ?> parent, S state,
	    StateTypeEnum type) {
	super();
	this.parent = parent;
	this.state = state;
	this.type = type;
    }

    @Override
    public S getState() {
	return state;
    }

    @Override
    public StateTypeEnum getType() {
	return type;
    }

    @Override
    public boolean illegalTransition(ITransition transition) {
	return !state.getTransitionFunction().containsKey(transition);
    }

    @Override
    public S nextState(ITransition transition) {
	return (S) state.getTransitionFunction().get(transition);
    }

    @Override
    public boolean containsCorruptTransition() {
	return false;
    }

    @Override
    public TransitionMetaData getCorruptTransitionMetaData() {
	for (ITransition transition : state.getOutboundTransitions()) {
	    TransitionMetaData transitionMetaData = parent
		    .getTransitionMetaData(transition);
	    if (transitionMetaData.getType() == TransitionTypeEnum.Corrupt) {
		return transitionMetaData;
	    }
	}
	return null;
    }

    @Override
    public MetaData getParent() {
	return parent;
    }

    @Override
    public DottedPath getDottedPath() {
	// TODO Auto-generated method stub
	return null;
    }
}
