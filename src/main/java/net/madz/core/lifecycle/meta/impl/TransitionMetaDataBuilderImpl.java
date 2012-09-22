package net.madz.core.lifecycle.meta.impl;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import net.madz.core.lifecycle.ITransition;
import net.madz.core.lifecycle.annotations.action.Corrupt;
import net.madz.core.lifecycle.annotations.action.Recover;
import net.madz.core.lifecycle.annotations.action.Redo;
import net.madz.core.lifecycle.annotations.action.Timeout;
import net.madz.core.lifecycle.meta.StateMachineMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData;
import net.madz.core.lifecycle.meta.TransitionMetaData.TransitionTypeEnum;
import net.madz.core.lifecycle.meta.TransitionMetaDataBuilder;
import net.madz.core.meta.impl.MetaDataBuilderBase;
import net.madz.core.verification.VerificationFailureSet;

public class TransitionMetaDataBuilderImpl extends
	MetaDataBuilderBase<TransitionMetaData, StateMachineMetaData<?, ?, ?>>
	implements TransitionMetaDataBuilder {

    protected TransitionMetaDataBuilderImpl(
	    StateMachineMetaData<?, ?, ?> parent, String name) {
	super(parent, name);
    }

    @Override
    public TransitionMetaData build(StateMachineMetaData<?, ?, ?> parent,
	    AnnotatedElement element) {
	if (!(element instanceof Field)) {
	    throw new IllegalArgumentException(
		    "ONLY accept Field type element.");
	}

	final Field transitionField = (Field) element;

	final TransitionTypeEnum type;
	if (null != transitionField.getAnnotation(Corrupt.class)) {
	    type = TransitionTypeEnum.Corrupt;
	} else if (null != transitionField.getAnnotation(Recover.class)) {
	    type = TransitionTypeEnum.Recover;
	} else if (null != transitionField.getAnnotation(Redo.class)) {
	    type = TransitionTypeEnum.Redo;
	} else {
	    type = TransitionTypeEnum.Other;
	}

	final Timeout timeoutAnnotation = transitionField
		.getAnnotation(Timeout.class);
	final long timeout;
	if (null != timeoutAnnotation) {
	    timeout = timeoutAnnotation.value();
	} else {
	    timeout = 30000L;
	}

	final Class<?> transitionEnumClass = transitionField
		.getDeclaringClass();

	try {
	    final ITransition transition = (ITransition) transitionField
		    .get(transitionEnumClass);
	    return new TransitionMetaDataImpl(parent, getName(), type,
		    transition, timeout);
	} catch (Exception ex) {
	    throw new IllegalArgumentException(
		    "Cannot get value from Enum Class:"
			    + transitionEnumClass.getName() + " Field: "
			    + transitionField.getName(), ex);
	}

    }

    @Override
    public void verifyMetaData(VerificationFailureSet verificationSet) {
	// TODO Auto-generated method stub

    }

}
