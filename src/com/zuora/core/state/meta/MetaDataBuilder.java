package com.zuora.core.state.meta;

import java.lang.reflect.AnnotatedElement;


public interface MetaDataBuilder<SELF extends MetaData, PARENT extends MetaData> {

   SELF build(PARENT parent, AnnotatedElement element);
}
