package net.madz.core.lifecycle.impl;

import net.madz.core.event.ILifeCycleEventListenter;
import net.madz.core.event.LifecycleEvent;

public class RecoverMaster implements ILifeCycleEventListenter {

    @Override
    public void onLifeCycleEvent(LifecycleEvent event) {
	if (LifecycleEvent.READY != event) {
	    return;
	}

    }

}
