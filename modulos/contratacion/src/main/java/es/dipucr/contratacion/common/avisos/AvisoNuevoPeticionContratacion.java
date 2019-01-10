package es.dipucr.contratacion.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;

public class AvisoNuevoPeticionContratacion implements IRule 
{
	private Logger logger = Logger.getLogger(AvisoNuevoPeticionContratacion.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {		
		try{
			
			//Comprobamos si el mismo funcionario hace contratos menores y el resto
			String unificado = ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), "TRAMITACION_CONTRATOS_UNIFICADO");
			if(StringUtils.isNotEmpty(unificado)){
				if(unificado.equals("SI")){
					generarAvisoAnuncio(rulectx, ResponsablesUtil.get_ID_RESP_Fase(rulectx));
				}
				if(unificado.equals("NO")){
					boolean mandarPersonalProcContra = false;
					//Mandar un aviso electrónico al grupo de PROC_CONTRATACION
					//Obras mayores de 40.000
					//Servicios y Suministros 15.000
					//EMAILS_NUEVOPROC_CONTR
					
					String query = "NUMEXP = '"+rulectx.getNumExp()+"'";
					Iterator<IItem> itPeticion = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_PETICION", query);
					while(itPeticion.hasNext()){
						IItem peticion = itPeticion.next();
						String presupuesto = "0.0";
						double valPresupuesto = 0.0;
						if(StringUtils.isNotEmpty(peticion.getString("PRESUPUESTO"))){
							presupuesto = peticion.getString("PRESUPUESTO");
							valPresupuesto = Double.parseDouble(presupuesto);
						}
						String tipoContrato = "";
						if(StringUtils.isNotEmpty(peticion.getString("TIPO_CONTRATO"))){
							tipoContrato = peticion.getString("TIPO_CONTRATO");
							if((tipoContrato.equals("1 - Suministros") || tipoContrato.equals("2 - Servicios")) && valPresupuesto>15000){						
								mandarPersonalProcContra = true;
							}
							else{
								if(tipoContrato.equals("3 - Obras") && valPresupuesto>40000){
									mandarPersonalProcContra = true;
								}
							}					
						}				
					}
					if(mandarPersonalProcContra){
						String restoContratoId = ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), "CONTRATO_RESTO_ID");
						generarAvisoAnuncio(rulectx, restoContratoId);
					}
					else{
						String contratoMenorId = ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), "CONTRATO_MENOR_ID");
						generarAvisoAnuncio(rulectx, contratoMenorId);
					}
					//Contratos Menores
					//EMAIL -> EMAILS_DEPCONTRA_PETSUM
					//Generamos el aviso
				}
			}			
		}
		catch (Exception e) {
			
			logger.error("Se produjo una excepciÃ³n "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return null;
	}
	


	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	public static String _ADVERTISE_MESSAGE = "Nueva petición del tramitación del contrato";
	
	private void generarAvisoAnuncio(IRuleContext rulectx, String sResponsable)
			throws ISPACException
	{
		
		IClientContext ctx = rulectx.getClientContext();
		int stageId = rulectx.getStageId();
		IInvesflowAPI invesflowAPI = ctx.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		
		String numexp = rulectx.getNumExp();		
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

