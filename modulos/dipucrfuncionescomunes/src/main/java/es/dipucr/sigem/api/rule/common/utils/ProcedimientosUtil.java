package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcedure;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [eCenpri-Felipe]
 * Clase con funciones comunes para procedimientos
 * @since 08.07.2011
 * @author Felipe
 */
public class ProcedimientosUtil {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(ProcedimientosUtil.class);
	
	protected static final String _REGLA_INIT_PRIMER_TRAMITE = "DipucrInitPrimerTramiteRule";
		
	/**
	 * Crea el procedimiento relacionado
	 * @param rulectx
	 * @param nombreProc
	 * @param nombreDocumento
	 * @param asunto
	 * @param extracto
	 * @param relacion
	 * @throws ISPACRuleException
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String crearPcdRelacionado(IRuleContext rulectx, String numexpActual, String codProc,
			Map<String, String> mapParamsSesion, String asunto, String relacion, boolean bInitTramite, String reglaAux) 
		throws ISPACRuleException
	{
		String numexpNuevo = null;
		
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
			//*********************************************
			
			IItemCollection iCollection = null;
			
			//Recuperamos el procedimiento
	        String strQuery = "WHERE UPPER(COD_PCD) LIKE UPPER('" + codProc + "') "
	        	+ "AND ID IN (SELECT ID FROM SPAC_P_PROCEDIMIENTOS WHERE ESTADO = " + IProcedure.PCD_STATE_CURRENT + ")";
	        iCollection = entitiesAPI.queryEntities("SPAC_CT_PROCEDIMIENTOS", strQuery);
	        IItem itemProc = (IItem)iCollection.iterator().next();
	        int idProc = itemProc.getInt("ID");
	        
	        //Creamos el expediente
	        Map<String, String> paramsPcd = new HashMap<String, String>();
			paramsPcd.put("COD_PCD", codProc);
	        int idExpediente = tx.createProcess(idProc, paramsPcd);
			IProcess process = invesFlowAPI.getProcess(idExpediente);
			numexpNuevo = process.getString("NUMEXP");
			
			//Ponemos el asunto al expediente nuevo
			IItem itemExpedienteActual = ExpedientesUtil.getExpediente(cct, numexpActual);
			IItem itemExpedienteNuevo = ExpedientesUtil.getExpediente(cct, numexpNuevo);
			itemExpedienteNuevo.set("ASUNTO", asunto);
			itemExpedienteNuevo.set("IDENTIDADTITULAR",
					itemExpedienteActual.getString("IDENTIDADTITULAR"));
			itemExpedienteNuevo.set("NIFCIFTITULAR",
					itemExpedienteActual.getString("NIFCIFTITULAR"));
			itemExpedienteNuevo.store(cct);
	        
	        //Ponemos en los expedientes relacionados
	        IItem itemRelacionados = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
			itemRelacionados.set("NUMEXP_PADRE", numexpActual);
			itemRelacionados.set("NUMEXP_HIJO", numexpNuevo);
			itemRelacionados.set("RELACION", relacion);
			itemRelacionados.store(cct);

			//Metemos los parámetros en la sesión para que se usen en próximas reglas
			Iterator<?> it = mapParamsSesion.entrySet().iterator();
			Map.Entry<String, String> entry = null;
			while (it.hasNext()) {
				entry = (Map.Entry<String, String>)it.next();
				rulectx.getClientContext().setSsVariable(entry.getKey(), entry.getValue());
			}
			
			//Ejecutar la regla de inicio de trámite si es necesario
			if (bInitTramite){
				ReglasUtil.ejecutarReglaPcd(cct, process, _REGLA_INIT_PRIMER_TRAMITE);
			}
			
			//Ejecuta la regla auxiliar si existe
			if (StringUtils.isNotEmpty(reglaAux)){
				ReglasUtil.ejecutarReglaPcd(cct, process, reglaAux);
			}
		}
		
		catch (Exception e) {
			throw new ISPACRuleException("Error al generar el nuevo expediente" +
					" relacionado con código " + codProc, e);
		}
		
		return numexpNuevo;
	}
	
	/**
	 * [eCenpri-Felipe]
	 * Recupera el id_pcd del procedimiento a partir de su cod_pcd
	 * @param cct
	 * @param codPcd
	 * @return
	 * @throws ISPACException
	 * @author Felipe
	 * @since 24.01.14
	 */
	public static int getIdPcdByCode(IClientContext cct, String codPcd)
			throws ISPACException {

		//recuperamos el procedimiento en estado vigente (current)
		IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
		IItem itemProc = procedureAPI.getProcedureByCode
				(codPcd, IProcedure.PCD_STATE_CURRENT);
		if (null == itemProc){
			throw new ISPACException("No existe ningún procedimiento vigente " +
					"con código procedimiento '" + codPcd + "'");
		}
		int idPcd = itemProc.getInt("CTPROCEDIMIENTOS:ID");
		return idPcd;
	}

	@SuppressWarnings("unchecked")
	public static IItem getProcedimientoByCodPcd(IRuleContext rulectx, String codpcd) throws ISPACRuleException{
		IItem procedimiento = null;
		try{
			/****************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlow = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlow.getEntitiesAPI();
			/****************************************************************************/
			
			String consulta = "WHERE COD_PCD='"+codpcd+"'";
			IItemCollection expPadreOrgCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, consulta);			
			Iterator<IItem> expPadreOrgColIterator =expPadreOrgCol.iterator();
			
			if(expPadreOrgColIterator.hasNext()){
				procedimiento = expPadreOrgColIterator.next();
			}

			
		}catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el código de procedimiento. "+codpcd+": "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el código de procedimiento. "+codpcd+": "+e.getMessage(),e);
		}
		
		return procedimiento;
	}
	@SuppressWarnings("unchecked")
	public static IItem getProcedimientoByCodNombre(IRuleContext rulectx, String nombrePcd) throws ISPACRuleException{
		IItem procedimiento = null;
		try{
			/****************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlow = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlow.getEntitiesAPI();
			/****************************************************************************/
			
			String consulta = "WHERE NOMBRE='"+nombrePcd+"'";
			IItemCollection expPadreOrgCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, consulta);			
			Iterator<IItem> expPadreOrgColIterator =expPadreOrgCol.iterator();
			
			if(expPadreOrgColIterator.hasNext()){
				procedimiento = expPadreOrgColIterator.next();
			}
			
		}catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el nombre de procedimiento. "+nombrePcd+": "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el nombre de procedimiento. "+nombrePcd+": "+e.getMessage(),e);
		}
		
		return procedimiento;
	}

	@SuppressWarnings("unchecked")
	public static IItem getProcedimientoByConsulta(IRuleContext rulectx, String consulta) throws ISPACRuleException {
		IItem procedimiento = null;
		try{
			/****************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlow = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlow.getEntitiesAPI();
			/****************************************************************************/

			IItemCollection expPadreOrgCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, consulta);			
			Iterator<IItem> expPadreOrgColIterator =expPadreOrgCol.iterator();
			
			if(expPadreOrgColIterator.hasNext()){
				procedimiento = expPadreOrgColIterator.next();
			}
			
		}catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el nombre de procedimiento. "+consulta+": "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el nombre de procedimiento. "+consulta+": "+e.getMessage(),e);
		}
		
		return procedimiento;
	}
}
