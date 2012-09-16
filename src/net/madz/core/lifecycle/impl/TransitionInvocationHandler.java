package net.madz.core.lifecycle.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.madz.core.lifecycle.IReactiveObject;
import net.madz.core.lifecycle.IState;
import net.madz.core.lifecycle.ITransition;
import net.madz.core.lifecycle.annotations.StateMachine;
import net.madz.core.lifecycle.annotations.Transition;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.StateMetaData;
import net.madz.core.lifecycle.meta.impl.StateMachineMetaDataBuilderImpl;
import net.madz.core.util.StringUtil;

public class TransitionInvocationHandler<R extends IReactiveObject, S extends IState<R, S>, T extends ITransition>
	implements InvocationHandler {

    private final IReactiveObject reactiveObject;

    public TransitionInvocationHandler(IReactiveObject reactiveObject) {
	super();
	this.reactiveObject = reactiveObject;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] args)
	    throws Throwable {
	StateMachineMetaData<R, S, T> stateMachineMetaData = findStateMachineMetaData();
	S state = reactiveObject.getState();
	StateMetaData<R, S> stateMetaData = stateMachineMetaData
		.getStateMetaData(state);

	final String transitionName;
	final Transition transition = method.getAnnotation(Transition.class);
	if (null == transition) {
	    return method.invoke(reactiveObject, args);
	}
	if (Transition.NULL.equals(transition.value())) {
	    transitionName = StringUtil.toUppercaseFirstCharacter(method
		    .getName());
	} else {
	    transitionName = transition.value();
	}
	T transitionEnum = stateMachineMetaData.getTransition(transitionName);

	if (stateMetaData.illegalTransition(transitionEnum)) {
	    throw new IllegalStateException("Cannot transit from State:"
		    + stateMetaData.getDottedPath().getName()
		    + " via Transition: " + transitionName);
	}

	try {
	    Object result = method.invoke(reactiveObject, args);
	    return result;
	} finally {
	    final S nextState = stateMetaData.nextState(transitionEnum);
            Method stateSetter = reactiveObject.getClass().getDeclaredMethod("setState", new Class[]{nextState.getClass()});
            stateSetter.setAccessible(true);
            stateSetter.invoke(reactiveObject, nextState);
            stateSetter.setAccessible(false);
	}
    }

    @SuppressWarnings("unchecked")
    private StateMachineMetaData<R, S, T> findStateMachineMetaData() {
	final Class<? extends IReactiveObject> reactiveClass = reactiveObject
		.getClass();
	Class<? extends IReactiveObject> stateMachineClass = null;
	for (Class<?> interfaze : reactiveClass.getInterfaces()) {
	    StateMachine annotation = (StateMachine) interfaze
		    .getAnnotation(StateMachine.class);
	    if (null == annotation) {
		continue;
	    } else {
		stateMachineClass = (Class<? extends IReactiveObject>) interfaze;
		break;
	    }
	}
	if (null == stateMachineClass) {
	    throw new IllegalStateException(
		    "Cannot find stateMachineClass through interfaces of Class: "
			    + reactiveClass.getName());
	}
	final StateMachineMetaDataBuilderImpl builder = new StateMachineMetaDataBuilderImpl(
		null, "StateMachine");
	final StateMachineMetaData<R, S, T> machineMetaData = (StateMachineMetaData<R, S, T>) builder
		.build(null, stateMachineClass);
	return machineMetaData;
    }
}
