package com.zuora.core.state;

public interface IReactiveObject<S extends IState> {

   S getState();
   
}
