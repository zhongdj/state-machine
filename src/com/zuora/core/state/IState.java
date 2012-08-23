package com.zuora.core.state;

import java.util.Map;
import java.util.Set;

public interface IState<R extends IReactiveObject, SELF> {

   int seq();

   Map<? extends ITransition, SELF> getTransitionFunction();

   SELF doStateChange(StateContext<R, SELF> context);

   Set<? extends ITransition> getOutboundTransitions();
}
