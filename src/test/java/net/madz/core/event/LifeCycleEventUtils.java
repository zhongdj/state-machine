package net.madz.core.event;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class LifeCycleEventUtils {

    private static final ArrayList<ILifeCycleEventListenter> listeners = new ArrayList<ILifeCycleEventListenter>();

    static {
	LifeCycleEventListeners annotation = LifeCycleEvent.class
		.getAnnotation(LifeCycleEventListeners.class);
	Class<? extends ILifeCycleEventListenter>[] listenerClasses = annotation
		.value();
	for (Class<? extends ILifeCycleEventListenter> listenerClass : listenerClasses) {
	    try {
		listeners.add(listenerClass.newInstance());
	    } catch (Exception e) {
		Logger.getAnonymousLogger().log(
			Level.SEVERE,
			"Failed to create instance of class: "
				+ listenerClass.getName(), e);
	    }
	}
    }

    public static void notify(LifeCycleEvent event) {
	for (ILifeCycleEventListenter listener : listeners) {
	    listener.onLifeCycleEvent(event);
	}
    }
}
