package net.madz.core.lifecycle.meta.impl;

import java.lang.reflect.Method;

import net.madz.core.common.DottedPath;
import net.madz.core.lifecycle.ITransition;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData;
import net.madz.core.meta.MetaData;

public class TransitionMetaDataImpl implements MetaData, TransitionMetaData {

    protected final TransitionTypeEnum type;
    protected final ITransition transition;
    protected final long timeout;
    protected final StateMachineMetaData<?, ?, ?> parent;
    protected Method transitionMethod;

    public TransitionMetaDataImpl(StateMachineMetaData<?, ?, ?> parent,
	    TransitionTypeEnum type, ITransition transition, long timeout) {
	super();
	this.parent = parent;
	this.type = type;
	this.transition = transition;
	this.timeout = timeout;
    }

    @Override
    public TransitionTypeEnum getType() {
	return type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ITransition> T getTransition() {
	return (T) transition;
    }

    @Override
    public long getTimeout() {
	return timeout;
    }

    @Override
    public StateMachineMetaData<?, ?, ?> getParent() {
	return parent;
    }

    @Override
    public Method getTransitionMethod() {
	return this.transitionMethod;
    }

    public void setTransitionMethod(Method method) {
	this.transitionMethod = method;
    }

    @Override
    public DottedPath getDottedPath() {
	// TODO Auto-generated method stub
	return null;
    }

}
