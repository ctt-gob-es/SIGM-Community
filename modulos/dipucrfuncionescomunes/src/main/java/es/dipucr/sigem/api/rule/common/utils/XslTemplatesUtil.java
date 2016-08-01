package es.dipucr.sigem.api.rule.common.utils;

import java.io.File;

/**
 * [dipucr-Felipe 3#82]
 */
public class XslTemplatesUtil {

	//Instancia singleton
	public static XslTemplatesUtil mInstance = null;
	
	//Variables
	public static String SEPARATOR = File.separator;
	protected static String module = "SIGEM_TramitacionWeb";
	protected static String folder = "xsl_templates";
	protected String basePath = null;
	
	/**
	 * Devuelve la instancia existente del singleton
	 * @return
	 * @throws Exception
	 */
	public static synchronized XslTemplatesUtil getInstance()
			throws Exception {

		if (mInstance == null) {
			mInstance = new XslTemplatesUtil();
		}
		return mInstance;
	}
	
	/**
	 * Constructor protegido
	 */
	protected XslTemplatesUtil(){
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); 
		String modulePath = classLoader.getResource(".." + SEPARATOR  + ".." + SEPARATOR).getPath();
		basePath = modulePath.substring(0, modulePath.length() - 1);
		basePath = basePath.substring(0, basePath.lastIndexOf("/"));
		basePath = basePath + SEPARATOR + module + SEPARATOR + folder;
	}
	
	public String getBasePath(){
		return basePath;
	}
	
	public String getSubfolderPath(String subFolder){
		return (basePath + SEPARATOR + subFolder);
	}
	
	public String getPath(String subFolder, String template){
		return (getSubfolderPath(subFolder) + SEPARATOR + template);	
	}
}
