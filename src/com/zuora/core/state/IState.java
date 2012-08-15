package com.zuora.core.state;

import java.util.Map;

public interface IState {

   int seq();

   Map<ITransition, IState> getTransitionFunction();

   @SuppressWarnings("rawtypes")
   IState doStateChange(StateContext context);

}
