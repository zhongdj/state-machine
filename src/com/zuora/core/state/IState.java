package com.zuora.core.state;

import java.util.Map;

public interface IState<R extends IReactiveObject> {

   int seq();

   Map<? extends ITransition, ? extends IState<R>> getTransitionFunction();

    IState<R> doStateChange(StateContext<R, IState<R>> context);
//   <S extends IState<R>> S doStateChange(StateContext<R, S> context);

}
