package com.zuora.core.state.meta.impl.builder;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import com.zuora.core.state.ITransition;
import com.zuora.core.state.annotations.action.Corrupt;
import com.zuora.core.state.annotations.action.Recover;
import com.zuora.core.state.annotations.action.Redo;
import com.zuora.core.state.annotations.action.Timeout;
import com.zuora.core.state.meta.MetaDataBuilder;
import com.zuora.core.state.meta.TransitionMetaData;
import com.zuora.core.state.meta.TransitionMetaData.TransitionTypeEnum;
import com.zuora.core.state.meta.impl.TransitionMetaDataImpl;

public class TransitionMetaDataBuilder implements MetaDataBuilder<TransitionMetaData> {

   @Override
   public TransitionMetaData build(AnnotatedElement element) {
      if (!(element instanceof Field)) {
         throw new IllegalArgumentException("ONLY accept Field type element.");
      }

      final Field transitionField = (Field) element;

      final TransitionTypeEnum type;
      if (null != transitionField.getAnnotation(Corrupt.class)) {
         type = TransitionTypeEnum.Corrupt;
      }
      else if (null != transitionField.getAnnotation(Recover.class)) {
         type = TransitionTypeEnum.Recover;
      }
      else if (null != transitionField.getAnnotation(Redo.class)) {
         type = TransitionTypeEnum.Redo;
      }
      else {
         type = TransitionTypeEnum.Other;
      }

      final Timeout timeoutAnnotation = transitionField.getAnnotation(Timeout.class);
      final long timeout;
      if (null != timeoutAnnotation) {
         timeout = timeoutAnnotation.value();
      }
      else {
         timeout = 30000L;
      }

      final Class<?> transitionEnumClass = transitionField.getDeclaringClass();

      try {
         final ITransition transition = (ITransition) transitionField.get(transitionEnumClass);
         return new TransitionMetaDataImpl(type, transition, timeout);
      }
      catch (Exception ex) {
         throw new IllegalArgumentException("Cannot get value from Enum Class:" + transitionEnumClass.getName() + " Field: " + transitionField.getName(), ex);
      }

   }

}
