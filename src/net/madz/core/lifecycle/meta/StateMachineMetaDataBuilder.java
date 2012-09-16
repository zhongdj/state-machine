package net.madz.core.lifecycle.meta;

import java.lang.reflect.AnnotatedElement;

import net.madz.core.meta.MetaData;
import net.madz.core.meta.MetaDataBuilder;

public interface StateMachineMetaDataBuilder extends
	MetaDataBuilder<StateMachineMetaData<?, ?, ?>, MetaData> {

    StateMachineMetaData<?, ?, ?> build(MetaData parent,
	    AnnotatedElement element);

}