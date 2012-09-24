package net.madz.core.event;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class LifeCycleEventUtils {

    private static final ArrayList<ILifeCycleEventListener> LISTENERs = new ArrayList<ILifeCycleEventListener>();

    static {
        LifeCycleEventListeners annotation = LifeCycleEvent.class.getAnnotation(LifeCycleEventListeners.class);
        Class<? extends ILifeCycleEventListener>[] listenerClasses = annotation.value();
        for (Class<? extends ILifeCycleEventListener> listenerClass : listenerClasses) {
            try {
                LISTENERs.add(listenerClass.newInstance());
            } catch (Exception e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Failed to create instance of class: " + listenerClass.getName(), e);
            }
        }
    }

    public static void notify(LifeCycleEvent event) {
        for (ILifeCycleEventListener listener : LISTENERs) {
            listener.onLifeCycleEvent(event);
        }
    }
}
