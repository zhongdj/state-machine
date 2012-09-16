package net.madz.core.lifecycle.meta.impl;

import net.madz.core.common.DottedPath;
import net.madz.core.common.Dumper;
import net.madz.core.lifecycle.IReactiveObject;
import net.madz.core.lifecycle.IState;
import net.madz.core.lifecycle.ITransition;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.StateMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData.TransitionTypeEnum;
import net.madz.core.meta.MetaData;
import net.madz.core.verification.VerificationException;
import net.madz.core.verification.VerificationFailureSet;

public class StateMetaDataImpl<R extends IReactiveObject, S extends IState<R, S>>
	implements MetaData, StateMetaData<R, S> {

    private final DottedPath path;

    private final S state;

    private final StateTypeEnum type;

    private final StateMachineMetaData<R, S, ?> parent;

    public StateMetaDataImpl(
	    StateMachineMetaData<R, S, ? extends ITransition> parent, S state,
	    StateTypeEnum type, String name) {
	super();
	this.parent = parent;
	this.state = state;
	this.type = type;
	this.path = parent.getDottedPath().append(name);
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
	ITransition transition = parent.getCorruptTransition().getTransition();
	return !illegalTransition(transition);
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
	return path;
    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {

	if (getType() == StateTypeEnum.Running && !containsCorruptTransition()) {
	    verificationSet.add(new VerificationException(this,
		    "RunningStateWithoutCorruptTransition",
		    "Each Running State Must Contains One Corrupt Transition: "
			    + getDottedPath().getAbsoluteName()));
	}

    }

    @Override
    public void dump(Dumper dumper) {
	dumper.dump(toString());
    }

    @Override
    public String toString() {
	return "StateMetaDataImpl [path=" + path + ", state=" + state
		+ ", type=" + type + "]";
    }

}
