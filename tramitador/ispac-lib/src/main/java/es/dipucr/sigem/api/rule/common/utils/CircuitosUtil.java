package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.sign.SignCircuitDetailDAO;
import ieci.tdw.ispac.ispaclib.dao.sign.SignCircuitHeaderDAO;
import ieci.tdw.ispac.ispaclib.sign.SignCircuitFilter;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * [eCenpri-Felipe #601] 
 * @since 13.04.12
 * @author Felipe
 */
public class CircuitosUtil {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(CircuitosUtil.class);
	
	/**
	 * Ticket #93 - [ecenpri-Felipe] Añadir trámite y circuito al aviso
	 * Recupera el nombre del circuito a partir de su id
	 * @param ctx: Contexto
	 * @param idCircuito: Identificador del circuito
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings("unchecked")
	public static String getNombreCircuito(IClientContext ctx, int idCircuito) throws ISPACException {

		String nombreCircuito = "";
		IItemCollection collection = (IItemCollection) SignCircuitHeaderDAO.getCircuit(ctx.getConnection(), idCircuito).disconnect();

		Iterator <IItem> it = collection.iterator();
		IItem item = null;
		if (it.hasNext()) {
			item = (IItem) it.next();
			nombreCircuito = item.getString("DESCRIPCION");
		}
		return nombreCircuito;
	}
	
	/**
	 * [dipucr-Felipe #791] Recupera el número de firmantes de un determinado circuito
	 * @param ctx: Contexto
	 * @param idCircuito: Identificador del circuito
	 * @return
	 * @throws ISPACException
	 */
	public static int getNumFirmantesCircuito(IClientContext cct, int idCircuito) throws ISPACException {

		IItemCollection collection = SignCircuitDetailDAO.getSteps
			(cct.getConnection(), idCircuito).disconnect();
		return collection.toList().size();
	}
	
	/**
	 * [eCenpri-Felipe #601]
	 * Devuelve los firmantes del circuito de firma
	 * @param rulectx
	 * @return
	 * @throws ISPACRuleException
	 */
	public static String getFirmantesCircuito(IRuleContext rulectx) throws ISPACRuleException{
		
		try{
			IClientContext cct = rulectx.getClientContext();
			int idCircuito = rulectx.getInt("ID_CIRCUITO");
			IItemCollection collection = SignCircuitDetailDAO.getSteps
				(cct.getConnection(), idCircuito).disconnect();
			
			//Recuperamos los nombres de los firmantes
			StringBuilder sbFirmantes = new StringBuilder();
			@SuppressWarnings("rawtypes")
			Iterator iter = collection.iterator();
			while (iter.hasNext()){
				IItem itemFirmante = (IItem) iter.next();
				sbFirmantes.append(itemFirmante.get("NOMBRE_FIRMANTE"));
				sbFirmantes.append("; ");
			}
			return sbFirmantes.toString();
		}
		catch(Exception ex){
			throw new ISPACRuleException("Error la obtener los firmantes del circuito", ex);
		}
	}
	
	/**
	 * [eCenpri-Felipe #601]
	 * Devuelve los firmantes reales del documento,
	 * teniendo en cuenta las posibles sustituciones.
	 * @param rulectx
	 * @return
	 * @throws ISPACRuleException
	 */
	public static String getFirmantesRealesDocumento(IRuleContext rulectx) 
			throws ISPACRuleException
	{
		try{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
//			int idPasoActual = rulectx.getInt("ID_PASO");
//			String nombreFirmanteActual = rulectx.get("NOMBRE_FIRMANTE");
			
			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			String strQuery = "WHERE ID_DOCUMENTO = " + idDoc + "ORDER BY ID_PASO";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_CTOS_FIRMA",strQuery);
			
			//Recuperamos los nombres de los firmantes
			StringBuilder sbFirmantes = new StringBuilder();
			@SuppressWarnings("rawtypes")
			Iterator iter = collection.iterator();
			while (iter.hasNext()){
				IItem itemFirmante = (IItem) iter.next();
				sbFirmantes.append(itemFirmante.get("NOMBRE_FIRMANTE"));
				sbFirmantes.append("; ");
			}
			return sbFirmantes.toString();
		}
		catch(Exception ex){
			throw new ISPACRuleException("Error la obtener los firmantes del documento", ex);
		}
	}
	
	public static IItemCollection getSpacCtosFirmaByIdDocumento(IRuleContext rulectx, int idDoc) 
			throws ISPACRuleException
	{
		try{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();


			String strQuery = "WHERE ID_DOCUMENTO = " + idDoc + "ORDER BY ID_PASO";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_CTOS_FIRMA",strQuery);
			
			//Recuperamos los nombres de los firmantes
			return collection;
		}
		catch(Exception ex){
			throw new ISPACRuleException("Error la obtener los firmantes del documento", ex);
		}
	}
	

	/**
	 * [eCenpri-Felipe #592#743]
	 * Inicia el circuito de firma configurado en el trámite para el
	 * documento pasado como parámetro
	 * @param rulectx
	 * @param idDocumento
	 * @return
	 * @throws ISPACException 
	 */
	public static int iniciarCircuitoTramite(IRuleContext rulectx, int idDocumento) throws ISPACException{
	
		//*********************************************
		IClientContext cct = rulectx.getClientContext();
  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
		ISignAPI signAPI = invesFlowAPI.getSignAPI();
		//*********************************************
			
		int idTaskPcd = rulectx.getTaskProcedureId();
		
		//Filtro obtener los circuitos de firma del trámite
		SignCircuitFilter filter = new SignCircuitFilter();
		filter.setTaskPcdId(idTaskPcd);
		
		//Recuperamos los circuitos del trámite
		IItemCollection colCircuitos = signAPI.getCircuitsTramite(filter);
		@SuppressWarnings("rawtypes")
		List listCircuitos = colCircuitos.toList();
		
		//Si no tiene circuitos configurados en el trámite, lanzamos excepción
		if (listCircuitos.size() == 0){
			throw new ISPACException("No hay ningún circuito de firma definido para el trámite.");
		}

		//Si hay circuitos de trámite, cogemos el primero
		IItem itemCircuito = (IItem) listCircuitos.get(0);
		int idCircuito = itemCircuito.getInt("ID_CIRCUITO");
		
		return signAPI.initCircuit(idCircuito, idDocumento);
	}
	
	/**
	 * [eCenpri-Teresa #1053]
	 * Inicia el circuito de firma configurado en el trámite para el
	 * documento pasado como parámetro
	 * @param rulectx
	 * @param idDocumento
	 * @return boolean especifíca si es id de documento ha sido firmado por el circuito de firma
	 * específico del trámite o por otro circuito de firma
	 * @throws ISPACException 
	 */

	@SuppressWarnings("unchecked")
	public static boolean firmadoDocumentoConCircuitoFirmaEspecifico(
			IRuleContext rulectx, int idDoc, int taskId) throws ISPACRuleException {
		boolean firmadoCircuitoEspecifico = false;
		try{
			/****************************************************************/
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			ISignAPI signAPI = invesFlowAPI.getSignAPI();
			/******************************************************************/
			
			//Filtro obtener los circuitos de firma del trámite
			SignCircuitFilter filter = new SignCircuitFilter();
			filter.setTaskPcdId(taskId);
			
			//Recuperamos los circuitos del trámite
			IItemCollection colCircuitos = signAPI.getCircuitsTramite(filter);
			List <IItem> listCircuitos = colCircuitos.toList();
			logger.warn("listCircuitos.size"+ listCircuitos.size());
			//Si no tiene circuitos configurados en el trámite, lanzamos excepción
			if (listCircuitos.size() != 0){
				logger.warn("No hay ningún circuito de firma definido para el trámite.");

				IItemCollection circuitosFirma = CircuitosUtil.getSpacCtosFirmaByIdDocumento(rulectx, idDoc);
				Iterator<IItem> itCirFirma = circuitosFirma.iterator();
				//Con seleccionar al primer firmante y coger el identificador del circuito de firma es suficiente.
				if(itCirFirma.hasNext()){
					IItem circuito = itCirFirma.next();
					int idCircuitoDocumento = circuito.getInt("ID_CIRCUITO");

					logger.warn("idCircuitoDocumento "+idCircuitoDocumento);
					for(int i=0; i<listCircuitos.size()&&!firmadoCircuitoEspecifico; i++){					
						//Si hay circuitos de trámite, cogemos el primero
						IItem itemCircuito = (IItem) listCircuitos.get(i);
						int idCircuitoEspecifico = itemCircuito.getInt("ID_CIRCUITO");
						logger.warn("idCircuitoEspecifico "+idCircuitoEspecifico);
						if(idCircuitoEspecifico==idCircuitoDocumento){
							logger.warn("IGUALES");
							firmadoCircuitoEspecifico=true;
						}
					}
				}
				
			}
		}catch(ISPACException e){
			logger.error("Error al comprobar el doc: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al comprobar el doc: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return firmadoCircuitoEspecifico;
	}
	
	
	/**
	 * [eCenpri-Felipe #601]
	 * Devuelve los firmantes reales del documento,
	 * teniendo en cuenta las posibles sustituciones.
	 * @param rulectx
	 * @return
	 * @throws ISPACRuleException
	 */
//	public static String getFirmantesRealesDocumento(IRuleContext rulectx) 
//			throws ISPACRuleException
//	{
//		try{
//			IClientContext cct = rulectx.getClientContext();
//			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
//			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
//			
//			int idDoc = rulectx.getInt("ID_DOCUMENTO");
//			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
//			
//			String strInfoPag = itemDocumento.getString("INFOPAG_RDE");
//			File fileDoc = DipucrCommonFunctions.getFile(genDocAPI, strInfoPag);
//			FileInputStream fis = new FileInputStream(fileDoc);
//			
//			PdfReader reader = new PdfReader((InputStream) fis);
//			AcroFields af = reader.getAcroFields(); 
//	        ArrayList names = af.getSignatureNames(); 
//	        for (Object o : names) {
//	        	String nombre = (String)o;
//	        }
//
//			return "";
//		}
//		catch(Exception ex){
//			throw new ISPACRuleException("Error la obtener los firmantes del documento", ex);
//		}
//	}
}
