package com.zuora.core.state;

public interface IReactiveObject {

   <S extends IState<? extends IReactiveObject>> S getState();

}
