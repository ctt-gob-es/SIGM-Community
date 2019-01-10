package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FasesUtil;

/**
 * [Ticket #990 Teresa] CONTRATACION-SIGEM añadir trámite para almacenar documento de 
 * solicitud de informe técnico en el procedimiento de petición
 * **/
public class AnadirDocSolInfTecnIncidenciaContratPCPeticionContratacion extends AnadirDocExpedienteTramite{

	private static final Logger logger = Logger.getLogger(AnadirDocSolInfTecnIncidenciaContratPCPeticionContratacion.class);
	
	@SuppressWarnings("unchecked")
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
	 		//--------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //-----------------------------------------------------------------------------

	        //Obtengo el numexp del procedimiento de Petición de contratación
	        String sqlQueryPart = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"'";
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        String numexpProceContratacion = "";
	        Iterator<IItem> itExpRel = exp_relacionados.iterator();
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpProceContratacion = itemExpRel.getString("NUMEXP_PADRE");
	        	IItem faseExpContr = FasesUtil.getFase(cct, numexpProceContratacion);
	        	faseActual = faseExpContr.getInt("ID");;
	        }
	        
	        sqlQueryPart = "WHERE NUMEXP_HIJO='"+numexpProceContratacion+"' AND RELACION='Petición Contrato'";
	        exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        itExpRel = exp_relacionados.iterator();
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpPeticionContratacion = itemExpRel.getString("NUMEXP_PADRE");
	        }
	        
		}
		 catch(Exception e) {
			 logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
	     }

		codigoTramite = "Sol-Inf-Tecn-Inc";
		nombreDescripcionDoc = "Solicitud de Informe sobre Incidencias de Contrato - "+rulectx.getNumExp();
		return true;
	}

}
