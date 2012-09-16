package net.madz.core.meta;

import java.lang.reflect.AnnotatedElement;

public interface MetaDataBuilder<SELF extends MetaData, PARENT extends MetaData> {

    SELF build(PARENT parent, AnnotatedElement element);
}
