package ieci.tdw.ispac.ispaclib.util;

import ieci.tdw.ispac.api.errors.ISPACException;

public class FileTemporaryManager extends FileTemporary {
  
  private static FileTemporaryManager mInstance = null;

  public static synchronized FileTemporaryManager getInstance()
  throws ISPACException {
	//[Ruben #625290] Para mantener el funcionamiento del resto de llamadas
	  return getInstance(true);
  }
  
  
  
  public static synchronized FileTemporaryManager getInstance(boolean clean)
  throws ISPACException {
  	
 	if ( mInstance == null)
  	{ 
 	    String sFileTemporaryPath;
 	    
 	    ISPACConfiguration parameters = ISPACConfiguration.getInstance();
 	    sFileTemporaryPath = parameters.get( ISPACConfiguration.TEMPORARY_PATH);                      
 	    if (sFileTemporaryPath.endsWith("/"))
 	    {
 	      	sFileTemporaryPath = sFileTemporaryPath.substring(0,sFileTemporaryPath.length() - 1);
 	    }
	//[Ruben #625290] Incluimos el booleano clean para decir si borramos o no los ficheros temporales y evitar así el fallo de la primera firma tras reinciar
 		mInstance = new FileTemporaryManager(sFileTemporaryPath, clean);
 	}

  	return mInstance;
  }

  protected FileTemporaryManager( String sFileTemporaryPath) 
  throws ISPACException {
    super( sFileTemporaryPath);
  }

	//[Ruben #625290] Incluimos el booleano clean para decir si borramos o no los ficheros temporales y evitar así el fallo de la primera firma tras reinciar
  protected FileTemporaryManager( String sFileTemporaryPath, boolean clean) 
  throws ISPACException {
    super( sFileTemporaryPath, clean);
  }
}
