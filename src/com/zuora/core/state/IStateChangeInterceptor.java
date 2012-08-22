package com.zuora.core.state;

public interface IStateChangeInterceptor {

   void interceptStateChange(StateContext<?,?> context);
}
