package com.zuora.core.state.meta.impl.builder;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import com.zuora.core.state.IState;
import com.zuora.core.state.annotations.action.End;
import com.zuora.core.state.annotations.state.Corrupted;
import com.zuora.core.state.annotations.state.Initial;
import com.zuora.core.state.annotations.state.Running;
import com.zuora.core.state.annotations.state.Stopped;
import com.zuora.core.state.annotations.state.Waiting;
import com.zuora.core.state.meta.MetaDataBuilder;
import com.zuora.core.state.meta.StateMetaData;
import com.zuora.core.state.meta.StateMetaData.StateTypeEnum;
import com.zuora.core.state.meta.impl.StateMetaDataImpl;

public class StateMetaDataBuilder implements MetaDataBuilder<StateMetaData> {

   @Override
   public StateMetaData build(AnnotatedElement element) {

      if (!(element instanceof Field)) {
         throw new IllegalArgumentException("ONLY accept Field type element.");
      }

      Field stateField = (Field) element;
      final StateTypeEnum typeEnum;
      if (null != stateField.getAnnotation(Initial.class)) {
         typeEnum = StateTypeEnum.Initial;
      }
      else if (null != stateField.getAnnotation(End.class)) {
         typeEnum = StateTypeEnum.End;
      }
      else if (null != stateField.getAnnotation(Running.class)) {
         typeEnum = StateTypeEnum.Running;
      }
      else if (null != stateField.getAnnotation(Stopped.class)) {
         typeEnum = StateTypeEnum.Stopped;
      }
      else if (null != stateField.getAnnotation(Corrupted.class)) {
         typeEnum = StateTypeEnum.Corrupted;
      }
      else if (null != stateField.getAnnotation(Waiting.class)) {
         typeEnum = StateTypeEnum.Waiting;
      }
      else {
         typeEnum = StateTypeEnum.Unknown;
      }

      final Class<?> stateEnumClass = stateField.getDeclaringClass();

      try {
         final IState state = (IState) stateField.get(stateEnumClass);
         return new StateMetaDataImpl(state, typeEnum) ;
      }
      catch (Exception ex) {
         throw new IllegalArgumentException("Cannot get value from Enum Class:" + stateEnumClass.getName() + " Field: " + stateField.getName(), ex);
      }
   }

}
