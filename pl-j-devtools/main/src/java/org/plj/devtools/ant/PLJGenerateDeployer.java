/*
 * Created on Jan 23, 2005
 */

package org.plj.devtools.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.plj.devtools.base.DbPlatform;
import org.plj.devtools.base.Parameter;
import org.plj.devtools.base.PostgreSQLPLJ;

import com.thoughtworks.qdox.ant.AbstractQdoxTask;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;


/**
 * 
 * 
 * @author Laszlo Hornyak
 */
public class PLJGenerateDeployer extends AbstractQdoxTask {

	DbPlatform dbPlatform = null;

	public void execute() throws BuildException {

		if (targetPath == null) {
			throw new BuildException("The attribute sourcePath is mandatory");
		}
		super.execute();

		log("Generating code for PostgreSQL");
		dbPlatform = new PostgreSQLPLJ();

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetPath + File.separator + platform
					+ ".sql");
		} catch (FileNotFoundException e) {
			throw new BuildException("File not found", e);
		}
		PrintWriter pw = new PrintWriter(fos);
		Calendar cal = new GregorianCalendar();

		pw.write(dbPlatform.comment("Generated by PL-J DevTools"));
		pw.write(dbPlatform.comment("see: http://plj.codehaus.org/"));
		pw.write(dbPlatform.comment(""));

		Iterator i = allClasses.iterator();
		while (i.hasNext()) {

			JavaClass jcl = (JavaClass) i.next();
			log(jcl.getName());
			JavaMethod[] methods = jcl.getMethods();
			for (int j = 0; j < methods.length; j++) {
				JavaMethod m = methods[j];
				DocletTag udfTag = m.getTagByName("jsproc.udf");

				String udfName = udfTag.getNamedParameter("name");
				if (udfName == null)
					throw new BuildException(
							"Name parameter is required for tag jsproc.udf in class "
									+ jcl.getName() + " method " + m.getName());
				
				DocletTag triggerTag = m.getTagByName("jsproc.trigger");
				if (udfTag != null) {
					DocletTag[] paramModifiers = m
							.getTagsByName("jsproc.param");
					DocletTag returnModifier = m.getTagByName("jsproc.returns");
					Type returns = m.getReturns();
					
					JavaParameter[] params = m.getParameters();
					
					List paramList = new ArrayList();
					for(int k=0; k<params.length;k++){
						DocletTag pMod = null;
						for(int l=0; l<paramModifiers.length; l++){
							String[] parameters = paramModifiers[l].getParameters();
							if(parameters.length >0 && params[k].getName().equals(parameters[0])){
								pMod = paramModifiers[l];
								break;
							}
						}
						Parameter p = makeParameter(params[k], pMod);
						paramList.add(p);
					}

					String cre = dbPlatform.createUdf(jcl.getName(), m
							.getName(), null, m.getComment(), udfName, returns
							.getValue(), paramList);
					pw.write(cre);
				}
			}
		}

	}
	private String targetPath = null;
	private String platform = "PostgreSQLPLJ";


	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	private Parameter makeParameter(JavaParameter paramDesc, DocletTag tag){
		return null;
	}
	
}