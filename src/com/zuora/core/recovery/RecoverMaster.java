package com.zuora.core.recovery;

import com.zuora.core.event.ILifeCycleEventListenter;
import com.zuora.core.event.LifecycleEvent;

public class RecoverMaster implements ILifeCycleEventListenter {

   
   
   @Override
   public void onLifeCycleEvent(LifecycleEvent event) {
       if (LifecycleEvent.READY != event) {
          return;
       }
       
       
   }

}
