package net.madz.core.lifecycle;

public interface IStateChangeInterceptor {

    void interceptStateChange(StateContext<?, ?> context);
}
