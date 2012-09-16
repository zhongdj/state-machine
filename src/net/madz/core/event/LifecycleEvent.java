package net.madz.core.event;

public enum LifecycleEvent {

    INIT_EVENT,

    STARTUP_EVENT,

    @LifeCycleEventListeners()
    READY,

    SHUTDOWN_EVENT,

    TERMINATION_EVENT
}
