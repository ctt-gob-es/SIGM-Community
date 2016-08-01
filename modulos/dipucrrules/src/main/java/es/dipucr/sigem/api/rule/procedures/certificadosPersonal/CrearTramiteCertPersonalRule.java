package es.dipucr.sigem.api.rule.procedures.certificadosPersonal;

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


/**
 * [eCenpri-Felipe #632]
 * Regla que crea el trámite de "Firma del Certificado"
 * @author Felipe
 * @since 17.08.2012
 */
public class CrearTramiteCertPersonalRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CrearTramiteCertPersonalRule.class);
	
	
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
			
			String strQuery = null;
			IItemCollection collection = null;
			int pid = rulectx.getProcessId();
			
			//INICIO [eCenpri-Felipe #787]
			//Si el expediente no ha sido iniciado por un grupo, es que es manual
			//Cuando sea Manual no tenemos que hacer nada pues el "tras iniciar" da problemas
			if (!ExpedientesUtil.esIniciadoPorGrupo(rulectx)){
				ExpedientesUtil.updateEstadoExpediente(rulectx, ExpedientesUtil._TIPO_MANUAL);
				return new Boolean(true);
			}
			else{
				ExpedientesUtil.updateEstadoExpediente(rulectx, ExpedientesUtil._TIPO_AUTO);
			}
			//FIN [eCenpri-Felipe #787]
			
			//Creamos el trámite
			//******************
			//Necesitamos sacar la fase y la fasePcd, que no vienen en el contexto
			IItemCollection stages = invesFlowAPI.getStagesProcess(pid);
			IItem itemFase = (IItem) stages.toList().get(0);
			int idFase = Integer.valueOf(itemFase.getString("ID_FASE_BPM"));
			int idFasePcd = itemFase.getInt("ID_FASE");
			
			//Obtenemos primero el id del trámite en el diseño, tabla spac_p_tramites
			strQuery = "WHERE ID_FASE = " + idFasePcd + " AND NOMBRE = '" +
				Constants.CERTPERSONAL._TRAMITE_SUPERVISION + "'";
			collection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQuery);
			IItem itemTramite = (IItem)collection.iterator().next();
			int idTramitePcd = itemTramite.getInt("ID"); 
			
			//Creamos el trámite
			tx.createTask(idFase, idTramitePcd);
			
		}
		catch (Exception e) {
			logger.error("Error en la generación de los trámites y envío a firma de los anticipos del expediente: " + rulectx.getNumExp() + ". " +e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación de los trámites y envío a firma de los anticipos del expediente: " + rulectx.getNumExp() + ". " +e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
