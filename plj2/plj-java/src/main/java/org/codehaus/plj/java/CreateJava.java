/*
 * Created on Oct 3, 2004
 */
package org.codehaus.plj.java;

import org.codehaus.plj.messages.CallRequest;


/**
 * @author Laszlo Hornyak
 */
public class CreateJava extends BasicPrivilegedJSProc {

	protected CreateJava(JavaExecutor je) {
		super(je);
	}

	/* (non-Javadoc)
	 * @see org.codehaus.plj.jexec.PrivilegedJSProc#getName()
	 */
	public String getName() {
		return "create_java";
	}

	/* (non-Javadoc)
	 * @see org.codehaus.plj.jexec.PrivilegedJSProc#perform(org.codehaus.plj.messages.CallRequest)
	 */
	public Object perform(CallRequest call) throws Exception {
		return null;
	}

}
