/*
 * Created on Jul 20, 2003
 */

package org.codehaus.plj.postgresql;

import java.math.BigInteger;

import org.apache.log4j.Category;
import org.codehaus.plj.typemapping.MappingException;

/**
 * Smallint datatype adapter for PostgreSQL.
 * 
 * @author Laszlo Hornyak
 */
public class PGSmallInt extends AbstractPGField {

	private static Category cat = Category.getInstance(PGSmallInt.class);

	private static Class[] classes = {Integer.class, String.class};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.plj.typemapping.postgres.AbstractPGField#backMap(java.lang.Object)
	 */
	protected void backMap(Object obj) throws MappingException {

		if (raw == null)
			raw = new byte[4];

		if (obj instanceof Integer) {
			if (obj == null) {
				setNull(true);
				return;
			}

			int value = ((Integer) obj).intValue();
			raw[3] = (byte) (value & 0x000000ff);
			raw[2] = (byte) ((value & 0x0000ff00) >> 8);
			raw[1] = (byte) ((value & 0x00ff0000) >> 16);
			raw[0] = (byte) ((value & 0xff000000) >> 24);
		} else
		if(obj instanceof String){
			backMap(Integer.getInteger((String)obj));
		}
			throw new MappingException("I can map only Integers, sorry");
		

	}

	/**
	 * @see org.codehaus.plj.typemapping.Field#getJavaClasses()
	 * 
	 * @todo return classes or classes.clone()? (speed or security?)
	 */
	public Class[] getJavaClasses() {
		cat.debug("getJavaClasses()");
		return (Class[]) classes.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.plj.typemapping.Field#getPreferredClass()
	 */
	public Class getPreferredClass() {
		return Integer.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.plj.typemapping.Field#get(java.lang.Class)
	 */
	public Object get(Class clazz) throws MappingException {
		// TODO Auto-generated method stub
		cat.debug("mapping to " + clazz.getName());
		if (Integer.class.getName().equals(clazz.getName()))
			return defaultGet();
		else if (String.class.getName().equals(clazz.getName()))
			return defaultGet().toString();

		throw new MappingException("sorry this is on my todo list");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.plj.typemapping.Field#defaultGet()
	 */
	public Object defaultGet() throws MappingException {
		byte[] b = new byte[4];
		System.arraycopy(raw, 4, b, 0, 4);
		BigInteger in = new BigInteger(b);
		Integer x = new Integer(in.intValue());
		return x;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.plj.typemapping.Field#rdbmsType()
	 */
	public String rdbmsType() {
		return "int4";
	}

}