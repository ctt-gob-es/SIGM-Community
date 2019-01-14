package ieci.tdw.ispac.ispaclib.util;

import ieci.tdw.ispac.api.errors.ISPACException;

public class FileRepositorioPlantillasComunesManager extends FileRepositorioPlantillasComunes {
  
  private static FileRepositorioPlantillasComunesManager mInstance = null;
  
  public static synchronized FileRepositorioPlantillasComunesManager getInstance()
  throws ISPACException {
  	
 	if ( mInstance == null)
  	{ 
 	    String sFileRepositorioPlantillasComunesPath;
 	    
 	    ISPACConfiguration parameters = ISPACConfiguration.getInstance();
 	    sFileRepositorioPlantillasComunesPath = parameters.get( ISPACConfiguration.REPOSITORIO_PLANTILLAS_COMUN_PATH);                      
 	    if (sFileRepositorioPlantillasComunesPath.endsWith("/"))
 	    {
 	      	sFileRepositorioPlantillasComunesPath = sFileRepositorioPlantillasComunesPath.substring(0,sFileRepositorioPlantillasComunesPath.length() - 1);
 	    }
 		mInstance = new FileRepositorioPlantillasComunesManager(sFileRepositorioPlantillasComunesPath);
 	}

  	return mInstance;
  }

  protected FileRepositorioPlantillasComunesManager( String sFileRepositorioPlantillasComunesPath) 
  throws ISPACException {
    super( sFileRepositorioPlantillasComunesPath);
  }
}
