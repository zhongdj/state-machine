package com.zuora.core.state;

import com.zuora.core.state.annotations.StateMachine;

public class Utils {

   
	@SuppressWarnings({ "rawtypes", "unchecked" })
   public static Class reactiveInterface(Class reactiveClass) {
		if (reactiveClass.isInterface()) {
			return reactiveClass;
		}

		for (Class interfase : reactiveClass.getInterfaces()) {
			if (null != interfase.getAnnotation(StateMachine.class)) {
				return interfase;
			}
		}

		throw new IllegalStateException("Cannot find Reactive Interface from class: " + reactiveClass);
	}
}
