package net.madz.core.lifecycle;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public final class StateContext<R extends IReactiveObject, S extends IState> implements
	Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private final R reactiveObject;

    public StateContext(R reactiveObject) {
	this.reactiveObject = reactiveObject;
    }

    public S getCurrentState() {
	return this.reactiveObject.getState();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
	return super.clone();
    }

}
