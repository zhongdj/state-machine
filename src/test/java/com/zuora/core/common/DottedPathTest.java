package com.zuora.core.common;

import junit.framework.TestCase;
import net.madz.core.common.DottedPath;

import org.junit.Test;

public class DottedPathTest {

    @Test
    public void testBaseFunctionality() {
	String str = null;
	DottedPath path = null;

	// Build up
	for (char ch = 'a'; ch <= 'l'; ch++) {
	    if (null == str)
		str = Character.toString(ch);
	    else
		str = str + "." + ch;

	    path = DottedPath.parse(str);
	    TestCase.assertEquals("Absolute path build up",
		    path.getAbsoluteName(), str);
	    TestCase.assertEquals("Local path", path.getName(),
		    Character.toString(ch));
	}

	// Tear down
	while (null != path) {
	    TestCase.assertEquals("Abosolute path tear down",
		    path.getAbsoluteName(), str);
	    path = path.getParent();
	    if (null != path) {
		str = str.substring(0, str.lastIndexOf('.'));
	    }
	}
    }
}