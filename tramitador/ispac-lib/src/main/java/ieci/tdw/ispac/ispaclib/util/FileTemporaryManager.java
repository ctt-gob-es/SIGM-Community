package ieci.tdw.ispac.ispaclib.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.errors.ISPACException;

public class FileTemporaryManager extends FileTemporary {
	
	private static final Logger LOGGER = Logger.getLogger(FileTemporaryManager.class);
	
	private static FileTemporaryManager mInstance = null;
	
	public static synchronized FileTemporaryManager getInstance() throws ISPACException {
		
		if ( mInstance == null){
			
			String sFileTemporaryPath;
			
			ISPACConfiguration parameters = ISPACConfiguration.getInstance();
			sFileTemporaryPath = parameters.get( ISPACConfiguration.TEMPORARY_PATH);
 	    
			if (sFileTemporaryPath.endsWith("/")){
				sFileTemporaryPath = sFileTemporaryPath.substring(0,sFileTemporaryPath.length() - 1);
			}
			
			try {
				sFileTemporaryPath += "_" + InetAddress.getLocalHost().getHostAddress().split("\\.")[3];
			} catch (UnknownHostException e) {
				LOGGER.error ("ERROR al recuperar el nombre de la carpeta temporal. Se deja la carpeta por defecto." + e.getMessage(), e);
			} catch (Exception e) {
				LOGGER.error ("ERROR al recuperar el nombre de la carpeta temporal. Se deja la carpeta por defecto." + e.getMessage(), e);
			}
			
			mInstance = new FileTemporaryManager(sFileTemporaryPath);
		}
		
		return mInstance;
	}
	
	protected FileTemporaryManager( String sFileTemporaryPath) throws ISPACException {
		super( sFileTemporaryPath);
	}
}
