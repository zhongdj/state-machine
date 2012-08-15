package com.zuora.core.state.meta;

import java.lang.reflect.AnnotatedElement;


public interface MetaDataBuilder<MD extends MetaData> {

   MD build(AnnotatedElement element);
}
