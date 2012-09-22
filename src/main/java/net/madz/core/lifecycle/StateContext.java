package net.madz.core.lifecycle;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public final class StateContext<R extends IReactiveObject, S extends IState> implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private final R reactiveObject;
    private final S currentState;
    private final S nextState;
    private final ITransition transition;

    public StateContext(R reactiveObject, S nextState, ITransition ongoingTransition) {
        this.reactiveObject = reactiveObject;
        this.currentState = this.reactiveObject.getState();
        this.nextState = nextState;
        this.transition = ongoingTransition;
    }

    public S getCurrentState() {
        return this.currentState;
    }

    public R getReactiveObject() {
        return reactiveObject;
    }

    public S getNextState() {
        return nextState;
    }

    public ITransition getTransition() {
        return transition;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
