package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.webempleado.services.ayudasSociales.AnticiposWSProxy;


/**
 * [eCenpri-Felipe #346]
 * Regla que crea el trámite de "Firmar anticipos"
 * @author Felipe
 * @since 28.06.2011
 */
public class CrearTramiteAnticiposRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CrearTramiteAnticiposRule.class);
	
	protected static final String _TRAMITE_FIRMAS = "Firmar anticipos"; //TODO Constants
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase y el trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			String strQuery = null;
			IItemCollection collection = null;
			int pid = rulectx.getProcessId();
			
			//Comprobar que no hay ya anticipos pendientes
			//Está comprobación ya se hace en el portal pero se vuelve a repetir
			//por si acaso alguién deja dos ventanas abiertas y pide seguido
			AnticiposWSProxy wsAnticipos = new AnticiposWSProxy();
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			String strNif = itemExpediente.getString("NIFCIFTITULAR");
			boolean bPuedePedirAnticipo = wsAnticipos.comprobarAnticipoPendiente(strNif);
			
			if (bPuedePedirAnticipo){
				//Creamos el trámite
				//******************
				//Necesitamos sacar la fase y la fasePcd, que no vienen en el contexto
				IItemCollection stages = invesFlowAPI.getStagesProcess(pid);
				IItem itemFase = (IItem) stages.toList().get(0);
				int idFase = Integer.valueOf(itemFase.getString("ID_FASE_BPM"));
				int idFasePcd = itemFase.getInt("ID_FASE");
				
				//Obtenemos primero el id del trámite en el diseño, tabla spac_p_tramites
				strQuery = "WHERE ID_FASE = " + idFasePcd + " AND NOMBRE = '" + _TRAMITE_FIRMAS + "'";
				collection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQuery);
				IItem itemTramite = (IItem)collection.iterator().next();
				int idTramitePcd = itemTramite.getInt("ID"); 
				
				//Creamos el trámite
				tx.createTask(idFase, idTramitePcd);
			}
			//Si no puede pedir anticipos cerramos directamente el expediente
			else{
				ExpedientesUtil.cerrarExpediente(cct, numexp);
			}
		
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación de los trámites y envío a firma de los anticipos. " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
