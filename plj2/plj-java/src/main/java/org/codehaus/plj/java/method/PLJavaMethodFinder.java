/*
 * Created on Oct 2, 2004
 */

package org.codehaus.plj.java.method;

import java.lang.reflect.Method;

import org.codehaus.plj.messages.TriggerCallRequest;
import org.codehaus.plj.typemapping.MappingException;
import org.postgresql.pljava.TriggerData;


/**
 * PL/Java method finder, it has it's own special trigger api.
 * 
 * @avalon.component name="method-finder"
 * @avalon.service type="org.codehaus.plj.tools.methodfinder.MethodFinder"
 * 
 * @dna.component
 * @dna.service type="org.codehaus.plj.tools.methodfinder.MethodFinder"
 * 
 * @author Laszlo Hornyak
 */
public class PLJavaMethodFinder extends DefaultMethodFinder {


	private final static Class[] pljava_args = new Class[]{TriggerData.class};

	protected Method findTriggerMethod(TriggerCallRequest call, Class clazz)
			throws MappingException, NoSuchMethodException {
		return clazz.getMethod(call.getMethodname(), pljava_args);
	}

	protected Object[] createParametersforTrigger(TriggerCallRequest call,
			Method method) throws MappingException {
		PLJavaTriggerData trigdata = new PLJavaTriggerData(call);
		return new Object[]{trigdata};
	}
}