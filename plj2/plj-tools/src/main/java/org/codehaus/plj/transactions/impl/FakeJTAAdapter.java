/*
 * Created on Aug 24, 2004
 */
package org.codehaus.plj.transactions.impl;

import javax.transaction.UserTransaction;

import org.codehaus.plj.transactions.JTAAdapter;

/**
 * Fake JTA adapter.
 * @author Laszlo Hornyak
 * @avalon.component name="jta-adapter" lifestyle="singleton" 
 * @avalon.service type="org.codehaus.plj.transactions.JTAAdapter"
 * 
 * @dna.component
 * @dna.service type="org.codehaus.plj.transactions.JTAAdapter"
 */
public class FakeJTAAdapter implements JTAAdapter {

	/**
	 * 
	 */
	public FakeJTAAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.codehaus.plj.transactions.JTAAdapter#getUserTransaction()
	 */
	public UserTransaction getUserTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

}
