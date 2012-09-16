package net.madz.core.lifecycle;

public interface IStateChangeListener {

    void onStateChanged(StateContext<?, ?> context);
}
