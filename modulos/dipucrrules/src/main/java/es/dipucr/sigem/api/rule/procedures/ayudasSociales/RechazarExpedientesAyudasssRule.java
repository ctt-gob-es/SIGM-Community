package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.TablaValidacionUtil;


/**
 * [dipucr-Felipe #593]
 * Regla que borra los expedientes introducidos en la pestaña "Ayudas sociales - Rechazo"
 * @since 25.08.17
 */
public class RechazarExpedientesAyudasssRule implements IRule 
{
	
	public static final String NUMEXP_SEPARATOR = ";";
	public static final String COD_PCD_AYUDASSS = "PCD-AYUDASSS";
	
	
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(RechazarExpedientesAyudasssRule.class);
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			IItem itemAyudasssRechazo = rulectx.getItem();
			String expedientesRechazar = itemAyudasssRechazo.getString("NUMEXP_RECHAZAR");
			
			if (StringUtils.isEmpty(expedientesRechazar)){
				rulectx.setInfoMessage("Debe rellenar el campo 'Estado', en que quiere que se marquen los expedientes");
				return false;
			}
			
			if (StringUtils.isEmpty(expedientesRechazar)){
				rulectx.setInfoMessage("Debe rellenar el campo 'Expedientes a rechazar'");
				return false;
			}
			
			String codEstado = itemAyudasssRechazo.getString("ESTADO");
			String descEstado = TablaValidacionUtil.getSustituto(entitiesAPI, "VLDTBL_AYUDASSS_ESTADOS", codEstado);
			String textoRechazo = "[" + descEstado + "]";
			
			ArrayList<String> listErrores = new ArrayList<String>();
			String [] arrNumexps = expedientesRechazar.split(NUMEXP_SEPARATOR);
			for (String numexpRechazar : arrNumexps){
				numexpRechazar = numexpRechazar.trim();
				IItem itemExpedientRechazar = entitiesAPI.getExpedient(numexpRechazar);
				if (null != itemExpedientRechazar){
					String asunto = itemExpedientRechazar.getString("ASUNTO");
					//Comprobamos que el expediente es de ayudas sociales
					if (COD_PCD_AYUDASSS.equals(itemExpedientRechazar.getString("CODPROCEDIMIENTO"))){
						
						//Actualizamos el estado en la pestaña ayudas
						IItemCollection colAyudaRechazar = entitiesAPI.getEntities("AYUDASSS", numexpRechazar);
						IItem itemAyudaRechazar = colAyudaRechazar.value();
						itemAyudaRechazar.set("ESTADO", codEstado);
						itemAyudaRechazar.store(cct);
						
						//Actualizamos el asunto del expediente siempre que no est
						if (!asunto.startsWith(textoRechazo)){
							asunto = textoRechazo + " " + asunto;
							itemExpedientRechazar.set("ASUNTO", asunto);
							itemExpedientRechazar.store(cct);
							listErrores.add(" - El expediente '" + numexpRechazar + "' se ha marcado como " + textoRechazo + ".");
						}
						else{
							listErrores.add(" - El expediente '" + numexpRechazar + "' ya estaba marcado como " + textoRechazo + ".");
						}
					}
					else{
						listErrores.add(" - El expediente '" + numexpRechazar + "' no es un expediente de Ayudas Sociales.");
					}
				}
				else{
					listErrores.add(" - No se ha encontrado el expediente con número '" + numexpRechazar + "'.");
				}
			}
			
			if (listErrores.size() > 0){
				StringBuffer sbLogErrores = new StringBuffer();
				for (String error : listErrores){
					sbLogErrores.append(error);
					sbLogErrores.append("\n");
				}
				itemAyudasssRechazo.set("LOG_ERRORES", sbLogErrores.toString());
			}
			
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la anulación de la ayuda " + e.getMessage(), e);
		}
		return true;
	}
	
	/**
	 * Generación de la fase y el trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
