package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.dao.sign.SignCircuitDetailDAO;
import ieci.tdw.ispac.ispaclib.dao.sign.SignCircuitHeaderDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.sign.SignCircuitFilter;
import ieci.tdw.ispac.ispaclib.sign.SignDetailEntry;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnectorFactory;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.vo.Signer;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;

/**
 * [eCenpri-Felipe #601] 
 * @since 13.04.12
 * @author Felipe
 */
public class CircuitosUtil {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(CircuitosUtil.class);
	
	//[dipucr-Felipe/Tere #1318]
	public static final int CIRCUITO_VUELO = 0;
	public static final String CIRCUITO_VUELO_AVISO_FIRMADO = "Ya ha sido FIRMADO el documento en el Portafirmas Externo";
	public static final String CIRCUITO_VUELO_AVISO_RECHAZADO = "Ha sido RECHAZADO el documento por el firmante en el Portafirmas Externo.";
	
	/**
	 * [Ticket#1270#Teresa] Creación de un circuito de firma.
	 * @throws ISPACRuleException 
	 * **/
	@SuppressWarnings("unchecked")
	public static int crearCircuitoFirma(IClientContext ctx, String nombreCircuito, ArrayList<ItemBean> pasosCircuito) throws ISPACRuleException{
		
        int instancedCircuitIdCabecera = 0;
        DbCnt cnt = null;

		try {
			cnt = ctx.getConnection();
			
			CollectionDAO circuito = SignCircuitHeaderDAO.getCircuitByDescripcion(cnt, nombreCircuito);
			if(circuito.toList().size()==0){
				instancedCircuitIdCabecera = IdSequenceMgr.getIdSequence(cnt, "SPAC_SQ_ID_CTOS_FIRMA_CABECERA");
				SignCircuitHeaderDAO cabeceraCircuito = new SignCircuitHeaderDAO(cnt);
				cabeceraCircuito.createNew(cnt);
				cabeceraCircuito.set("ID_CIRCUITO", instancedCircuitIdCabecera);
				cabeceraCircuito.set("NUM_PASOS", pasosCircuito.size());
				cabeceraCircuito.set("DESCRIPCION", nombreCircuito);
				cabeceraCircuito.set("TIPO", 2);
				cabeceraCircuito.set("SISTEMA", 1);
				cabeceraCircuito.set("SECUENCIA", 1);
				cabeceraCircuito.set("APLICACION", FirmaConfiguration.getInstance(ctx).getProperty(FirmaConfiguration.PROCESS_SIGN_CONNECTOR_APPLICATION));
				cabeceraCircuito.store(cnt);

				//Instanciamos el circuito
				for (Iterator<ItemBean> iter = pasosCircuito.iterator(); iter.hasNext();) {
					ItemBean item = iter.next();
					SignCircuitDetailDAO detalleCircuito = new SignCircuitDetailDAO();
					int instancedCircuitId = IdSequenceMgr.getIdSequence(cnt, "SPAC_SQ_ID_CTOS_FIRMA_DETALLE");
					detalleCircuito.createNew(cnt);
					detalleCircuito.set("ID", instancedCircuitId);
					detalleCircuito.set("ID_CIRCUITO", instancedCircuitIdCabecera);
					detalleCircuito.set("ID_PASO", new Integer(item.getString("ORDEN")).intValue());
					detalleCircuito.set("ID_FIRMANTE", item.getString("DNI"));
					detalleCircuito.set("NOMBRE_FIRMANTE", item.getString("NOMBRE"));
					detalleCircuito.set("TIPO_NOTIF", MailUtil.NOTIF_TYPE_EMAIL);
					detalleCircuito.set("DIR_NOTIF", item.getString("EMAIL"));
					detalleCircuito.set("TIPO_FIRMANTE", Signer.TYPE_SIGNER_USER);
					detalleCircuito.store(cnt);
				}
			}else{		
				IItemCollection itcolCircuito = circuito.disconnect();
				for (Iterator<IItem> iterator = itcolCircuito.iterator(); iterator.hasNext();) {
					IItem itemBean = (IItem) iterator.next();
					if(itemBean.getInt("ID_CIRCUITO")>0)instancedCircuitIdCabecera = itemBean.getInt("ID_CIRCUITO");
					
				}
			}

		} catch (ISPACRuleException e) {
			throw new ISPACRuleException("Error al crear el circuito de firma" +e.getMessage(), e);
		} catch (ISPACException e) {
			throw new ISPACRuleException("Error al crear el circuito de firma" +e.getMessage(), e);
		} finally {
			ctx.releaseConnection(cnt);
		}

		return instancedCircuitIdCabecera;
	}
	
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
	@Deprecated //Portafirmas
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
	 * [dipucr-Felipe #1246]
	 * @param cct
	 * @return
	 * @throws ISPACRuleException
	 */
	public static String getFirmantesDocumentoPortafirmas(IClientContext cct, int documentId) throws ISPACRuleException{
		
		try{
			ProcessSignConnector processConnector = ProcessSignConnectorFactory.getInstance(cct).getProcessSignConnector();
			List<SignDetailEntry> listFirmas = processConnector.getSigns((ClientContext) cct, String.valueOf(documentId));
			
			//Recuperamos los nombres de los firmantes
			StringBuilder sbFirmantes = new StringBuilder();
			for (SignDetailEntry entry : listFirmas){
				sbFirmantes.append(entry.getAuthor());
				sbFirmantes.append("; ");
			}
			return sbFirmantes.toString();
		}
		catch(Exception ex){
			throw new ISPACRuleException("Error la obtener los firmantes del documento del portafirmas", ex);
		}
	}
	
	public static IItemCollection getSpacCtosFirmaByIdDocumento(IClientContext cct, int idDoc) 
			throws ISPACRuleException
	{
		try{
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
		return iniciarCircuitoTramite(rulectx.getClientContext(), idDocumento, rulectx.getTaskProcedureId());
	}		
		

	/**
	 * [eCenpri-Felipe #592#743]
	 * Inicia el circuito de firma configurado en el trámite indicado en el parámetro para el documento pasado como parámetro
	 * @param rulectx
	 * @param idDocumento
	 * @return
	 * @throws ISPACException 
	 */
	public static int iniciarCircuitoTramite(IClientContext cct, int idDocumento, int idTaskPcd) throws ISPACException{
		
		//*********************************************
  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
		ISignAPI signAPI = invesFlowAPI.getSignAPI();
		//*********************************************
		
		//Filtro obtener los circuitos de firma del trámite
		SignCircuitFilter filter = new SignCircuitFilter();
		filter.setTaskPcdId(idTaskPcd);
		
		//Recuperamos los circuitos del trámite
		IItemCollection colCircuitos = signAPI.getCircuitsTramite(filter);
		List<?> listCircuitos = colCircuitos.toList();
		
		//Si no tiene circuitos configurados en el trámite, lanzamos excepción
		if (listCircuitos.size() == 0){
			throw new ISPACException("No hay ningún circuito de firma definido para el trámite.");
		}

		//Si hay varios circuitos de trámite, cogemos el primero
		IItem itemCircuito = (IItem) listCircuitos.get(0);
		int idCircuito = itemCircuito.getInt("ID_CIRCUITO");
		
		return signAPI.initCircuitPortafirmas(idCircuito, idDocumento);
	}
	
	public static int iniciarCircuitoTramite(IRuleContext rulectx, int idDocumento, int idCircuito) throws ISPACException{
		
		//*********************************************
		IClientContext cct = rulectx.getClientContext();
  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
		ISignAPI signAPI = invesFlowAPI.getSignAPI();
		//*********************************************
			
		
//		return signAPI.initCircuit(idCircuito, idDocumento);//[dipucr-Felipe #1246]
		return signAPI.initCircuitPortafirmas(idCircuito, idDocumento);
	}
	
	/**
	 * [eCenpri-Teresa #1053]
	 * Inicia el circuito de firma configurado en el trámite para el
	 * documento pasado como parámetro
	 * @param cct [dipucr-Felipe #1246]
	 * @param idDocumento
	 * @return boolean especifíca si es id de documento ha sido firmado por el circuito de firma
	 * específico del trámite o por otro circuito de firma
	 * @throws ISPACException 
	 */

	//[dipucr-Felipe #1246] Modifico todo el código para adaptarlo al portafirmas
	public static boolean firmadoDocumentoConCircuitoFirmaEspecifico(IClientContext cct, int idDoc, int taskId) 
			throws ISPACRuleException {
		
		boolean firmadoCircuitoEspecifico = false;
		try{
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ISignAPI signAPI = invesFlowAPI.getSignAPI();
			
			//INICIO [dipucr-Felipe #1246]
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
			String sIdCircuitoDocumento = itemDocumento.getString("ID_CIRCUITO");//Portafirmas
			int idCircuitoDocumento = Integer.MIN_VALUE;
			
			if (StringUtils.isEmpty(sIdCircuitoDocumento)){
				IItemCollection colCircuitosDocumento = CircuitosUtil.getSpacCtosFirmaByIdDocumento(cct, idDoc);
				if (colCircuitosDocumento.next()){
					IItem itemCircuitoDocumento = colCircuitosDocumento.value();
					idCircuitoDocumento = itemCircuitoDocumento.getInt("ID_CIRCUITO");
				}
			}
			else{
				idCircuitoDocumento = Integer.valueOf(sIdCircuitoDocumento);
			}
			
			//Filtro obtener los circuitos de firma del trámite
			SignCircuitFilter filter = new SignCircuitFilter();
			filter.setTaskPcdId(taskId);
			IItemCollection colCircuitosTramite = signAPI.getCircuitsTramite(filter);

			//Si no tiene circuitos configurados en el trámite, lanzamos excepción
			while (colCircuitosTramite.next() && !firmadoCircuitoEspecifico){
				
				IItem itemCircuitoTramite = colCircuitosTramite.value();
				int idCircuitoTramite = itemCircuitoTramite.getInt("ID_CIRCUITO");

				if(idCircuitoTramite == idCircuitoDocumento){
					firmadoCircuitoEspecifico=true;
				}
			}
			
		}catch(ISPACException e){
			String error = "Error al comprobar si el doc " + idDoc + " fue firmado por un circuito específico. " + e.getMessage();
			logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return firmadoCircuitoEspecifico;
	}
	
}
