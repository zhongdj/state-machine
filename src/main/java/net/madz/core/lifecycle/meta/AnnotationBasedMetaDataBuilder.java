package net.madz.core.lifecycle.meta;

import java.lang.reflect.AnnotatedElement;

import net.madz.core.meta.MetaData;
import net.madz.core.meta.MetaDataBuilder;

public interface AnnotationBasedMetaDataBuilder<SELF extends MetaData, PARENT extends MetaData>
	extends MetaDataBuilder<SELF, PARENT> {

    SELF build(PARENT parent, AnnotatedElement element);

}
