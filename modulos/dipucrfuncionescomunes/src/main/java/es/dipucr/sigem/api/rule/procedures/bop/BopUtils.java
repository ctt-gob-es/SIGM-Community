package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class BopUtils {

	public static final Logger logger = Logger.getLogger(BopUtils.class);
	
	public static String GRUPO_ADMIN_DEFECTO = "otras administraciones";
	
	//INICIO [eCenpri-Felipe #593]
	public static final String _MARCA_FIN_INDICE = "[FIN_INDICE]";
	public static final String _TEXTO_NUM_ANUNCIO = "Anuncio número ";
	//FIN [eCenpri-Felipe #593]
	
	/**
	 * [eCenpri-Felipe #451]
	 * Obtiene el grupo que corresponde a la clasificación de la BBDD
	 * @param rulectx
	 * @param clasificacion
	 * @return
	 * @throws Exception
	 */
	public static String getGrupoAdministracion
		(IRuleContext rulectx, String clasificacion) throws Exception
	{
		String administracion = null;
		//----------------------------------------------------------------------------------------------
        ClientContext cct = (ClientContext) rulectx.getClientContext();
        IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        
        String strQuery = "WHERE VALOR = '" + clasificacion + "'";
        IItemCollection itemCollection = entitiesAPI.queryEntities("BOP_VLDTBL_GRUPOS_CLASIF", strQuery);
        if (itemCollection.next()){
        	IItem item = (IItem) itemCollection.iterator().next();
        	administracion = item.getString("SUSTITUTO");
        }
        else{
        	administracion = GRUPO_ADMIN_DEFECTO;
        }
        
        return administracion;
	}
	
	/**
	 * Método que accede a la tabla CONT_VLDTBL_PORCENTAJESIVA para obtener el IVA actual,
	 * concretamente al registro con id=3 y columna valor
	 * @param rulectx
	 * @return
	 * @throws ISPACException
	 * @author Felipe - ecenpri
	 * @throws ISPACRuleException 
	 */
	public static float getIVAFacturacion(IRuleContext rulectx) throws ISPACRuleException{
		float iva = 0;
		try {
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			IItem item = entitiesAPI.getEntity("CONT_VLDTBL_PORCENTAJESIVA", 3);
			iva = Float.valueOf(item.getString("VALOR")) / 100;
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el IVA. " + e.getMessage(), e);
		}
		return iva;
	}
	
	
	/**
	 * [eCenpri-Felipe #593 #828]
	 * Devuelve los documentos BOP firmados en el procedimiento de publicación del BOP
	 * @param rulectx
	 * @param entitiesAPI
	 * @return
	 * @throws ISPACRuleException
	 * @throws ISPACException
	 */
	public static IItemCollection getBopsFirmados(IRuleContext rulectx) 
			throws ISPACRuleException, ISPACException {
		
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		StringBuffer sbQuery;
		IItemCollection collection;
		//Obtenemos el documento boletín
		//= Documento firmado del expediente con nombre "BOP - General"
		sbQuery = new StringBuffer();
		sbQuery.append("ID_FASE = ");
		sbQuery.append(rulectx.getStageId());
		sbQuery.append(" AND ESTADOFIRMA = '");
		sbQuery.append(SignStatesConstants.FIRMADO);
		sbQuery.append("' AND NOMBRE = '");
		sbQuery.append(Constants.BOP._DOC_BOP);
		sbQuery.append("'");
		
		collection = entitiesAPI.getDocuments(rulectx.getNumExp(),
				sbQuery.toString(), "FDOC DESC");
		return collection;
	}
}
