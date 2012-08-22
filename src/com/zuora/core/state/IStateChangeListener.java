package com.zuora.core.state;

public interface IStateChangeListener {

   void onStateChanged(StateContext<?,?> context);
}
