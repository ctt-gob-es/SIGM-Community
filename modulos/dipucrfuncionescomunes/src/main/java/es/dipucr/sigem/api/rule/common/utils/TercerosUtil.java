package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IThirdPartyAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;

public class TercerosUtil {
	
	/**
	 * Búsqueda de terceros por NIF/CIF
	 * @param cct
	 * @param idTercero 
	 * @return 
	 * @throws ISPACException 
	 * **/
	public static IThirdPartyAdapter[] getDatosTerceroByNif (ClientContext cct, String dni) throws ISPACException{
		
		IThirdPartyAPI thirdPartyAPI = cct.getAPI().getThirdPartyAPI();
		IThirdPartyAdapter[] tercero = thirdPartyAPI.lookup(dni, true);
		return tercero;
	}

}
