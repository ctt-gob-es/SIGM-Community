package es.dipucr.contratacion.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;

public class AvisoNuevoInformeTecnicoValoresAnormalesContratacion implements IRule 
{
	private Logger logger = Logger.getLogger(AvisoNuevoInformeTecnicoValoresAnormalesContratacion.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		
		try{
			
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //---------------------------------------------------------------------------------------------- 
			
	        String numexpContratacion = calculaExpContratacion(rulectx);

	        //Obtengo el numexp del procedimiento de Petición de contratación
	        String sqlQueryPart = "WHERE NUMEXP_HIJO='"+numexpContratacion+"' AND RELACION='Petición Contrato'";
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        Iterator<IItem> itExpRel = exp_relacionados.iterator();
	        String numexpPetCont = "";
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpPetCont = itemExpRel.getString("NUMEXP_PADRE");
	        }
			
			//Generamos el aviso
			generarAvisoAnuncio(rulectx, ResponsablesUtil.getRespProcedimientoByNumexp(rulectx, numexpPetCont).getUID(), numexpPetCont);
		}
		catch (Exception e) {
			
			logger.error("Se produjo una excepciÃ³n "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return null;
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
	            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
	            @SuppressWarnings("unchecked")
				Iterator<IItem> itExpRelLicitador = itemCollection.iterator();
	            while(itExpRelLicitador.hasNext()){
	            	IItem padreLicitador = itExpRelLicitador.next();
	            	String numexpPlica = padreLicitador.getString("NUMEXP_PADRE");
	            	 IItemCollection itemCollContratacion = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_HIJO = '" + numexpPlica + "' AND RELACION = 'Plica'");
	            	 @SuppressWarnings("unchecked")
	 				Iterator<IItem> itExpRelContratacion = itemCollContratacion.iterator();
	            	 while(itExpRelContratacion.hasNext()){
	            		 IItem expContratacion = itExpRelContratacion.next();
	            		 numexpConta = expContratacion.getString("NUMEXP_PADRE");
	            	 }
	            }
	            
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
        		throw new ISPACRuleException("Error. "+e.getMessage(),e);
        	throw new ISPACRuleException("No se ha podido obtener la fecha actual "+e.getMessage(),e);
        }
		return numexpConta;
	}
	


	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	public static String _ADVERTISE_MESSAGE = "Solicitud de Informe sobre Valores Anormales o Desproporcionados";
	
	private void generarAvisoAnuncio(IRuleContext rulectx, String sResponsable, String numexp)
			throws ISPACException
	{
		
		IClientContext ctx = rulectx.getClientContext();
		int stageId = rulectx.getStageId();
		IInvesflowAPI invesflowAPI = ctx.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
		int processId = invesflowAPI.getProcess(numexp).getInt("ID");
//		ITask task = invesflowAPI.getTask(idTask);
//		String nombreTramite = task.getString("NOMBRE");
		IItem itemExpediente = entitiesAPI.getExpedient(numexp);
		String asunto = itemExpediente.getString("ASUNTO");
		//Enlace que dirige directamente al trámite
		String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?stageId=" + stageId +
			"\" class=\"displayLink\">" + _ADVERTISE_MESSAGE + "</a><br/>Asunto: " + asunto;
		AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, sResponsable, ctx);
	}
}

