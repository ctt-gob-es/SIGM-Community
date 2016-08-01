package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * [Ticket #990 Teresa] CONTRATACION-SIGEM añadir trámite para almacenar documento de 
 * solicitud de informe técnico en el procedimiento de petición
 * **/
public class AnadirDocSolInfTecnPeticionContratacion extends AnadirDocExpedienteTramite{

	private static final Logger logger = Logger.getLogger(AnadirDocSolInfTecnPeticionContratacion.class);
	
	@SuppressWarnings("unchecked")
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
	 		//--------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //-----------------------------------------------------------------------------
	        
	        String numexpContratacion = calculaExpContratacion(rulectx);

	        //Obtengo el numexp del procedimiento de Petición de contratación
	        String sqlQueryPart = "WHERE NUMEXP_HIJO='"+numexpContratacion+"' AND RELACION='Petición Contrato'";
	        logger.warn("numexpContratacion "+numexpContratacion);
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        Iterator<IItem> itExpRel = exp_relacionados.iterator();
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpPeticionContratacion = itemExpRel.getString("NUMEXP_PADRE");
	        }
	        
		}
		 catch(Exception e) {
			 logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
	     }

		codigoTramite = "SOLINFTECN";
		nombreDescripcionDoc = "Solicitud Informe Técnico sobre Valores Anormales o Desproporcionados";
		return true;
	}
	
	private String calculaExpContratacion(IRuleContext rulectx) throws ISPACRuleException {
		String numexpConta = "";
		try{
	 		//--------------------------------------------------------------------------------
		        ClientContext cct = (ClientContext) rulectx.getClientContext();
		        IInvesflowAPI invesFlowAPI = cct.getAPI();
		        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		        //-----------------------------------------------------------------------------
		        
		      //Obtengo los expedientes relacionados
	            String consultaSQL = "WHERE NUMEXP_HIJO = '" + rulectx.getNumExp() + "' AND RELACION='Val. anormales o despropor'";
	            logger.warn("InsertarDepartamentoTrasladoValoresAnormaRule  "+consultaSQL);
	            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
	            @SuppressWarnings("unchecked")
				Iterator<IItem> itExpRelLicitador = itemCollection.iterator();
	            while(itExpRelLicitador.hasNext()){
	            	IItem padreLicitador = itExpRelLicitador.next();
	            	String numexpPlica = padreLicitador.getString("NUMEXP_PADRE");
	            	logger.warn("WHERE NUMEXP_HIJO = '" + numexpPlica + "' AND RELACION = 'Plica'");
	            	 IItemCollection itemCollContratacion = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_HIJO = '" + numexpPlica + "' AND RELACION = 'Plica'");
	            	 @SuppressWarnings("unchecked")
	 				Iterator<IItem> itExpRelContratacion = itemCollContratacion.iterator();
	            	 while(itExpRelContratacion.hasNext()){
	            		 IItem expContratacion = itExpRelContratacion.next();
	            		 numexpConta = expContratacion.getString("NUMEXP_PADRE");
	            		 logger.warn("numexpConta "+numexpConta);
	            	 }
	            }
	            
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la fecha actual",e);
        }
		return numexpConta;
	}

}
