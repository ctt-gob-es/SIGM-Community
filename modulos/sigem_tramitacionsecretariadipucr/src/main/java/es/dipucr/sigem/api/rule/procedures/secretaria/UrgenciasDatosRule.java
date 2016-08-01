package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class UrgenciasDatosRule implements IRule {

	private static final Logger logger = Logger.getLogger(PropuestaDatosRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	
    	try{
	       	//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        //Obtener la sesion del expediente
			String organo = SecretariaUtil.getOrgano(rulectx);
			
			//Obtener el listado de propuestas
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() +"'";
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_URGENCIAS", strQuery);
	        
	        Iterator it = collection.iterator();
	
	        while (it.hasNext()) {
	        	IItem item = ((IItem)it.next());
				if(organo.equals("JGOB")){
	
		            StringBuffer result_vota = new StringBuffer("");
		            StringBuffer debate = new StringBuffer("");
		            StringBuffer informe = new StringBuffer("");
		            StringBuffer dictamen = new StringBuffer("");
	            	
	            	//Columna Informe
	            	informe.append("Visto el informe emitido el día .... 20 de abril del 2010 por la ....(Jefe de Servicio de ...., " +
	            			"la Intervención de Fondos...)(En su caso: debe tacharse el párrafo si no procede).");
	            	
	            	//columna dictamen
	            	dictamen.append("Visto el dictamen emitido el día veinte de abril de 2010 por la Comisión Informativa permanente " +
	    			"de Infraestructura. (En su caso: debe tacharse el párrafo si no procede).");
	            	
	            	//columan debate
	            	debate.append("Ningún integrante de la Junta de Gobierno solicita la apertura de debate. (En su caso: debe tacharse el párrafo si " +
	            			"no procede) o bien.\n\tAbierto el debate, ....(aquí iría el texto del debate si existiera).");
	            	
	            	//columna resultado de la votacion
	            	result_vota.append("Sometida a votacion la urgencia del asunto, Junta de Gobierno en votacion ordinaria y por ... unanimidad de los asistentes, " +
	            			"con ....nueve votos a favor, .... ningún voto en contra y ....ninguna abstención, adopta el siguiente acuerdo:\n" +
	            			"\tRatificar la urgencia de asunto.\n" +
	            			"\tTras ello, sometido a votación el fondo del asunto, Junta de Gobierno, por ... unanimidad, " +
	            			" con .... ocho votos a favor, " +
	            			".... ningún voto en contra y ninguna abstención, adopta el siguiente acuerdo:\n" +
	            			"\tAprobar la transcrita propuesta, sin enmienda alguna.");
	
	            	item.set("INFORMES", informe.toString());
	            	item.set("DICTAMEN", dictamen.toString());
	            	item.set("DEBATE", debate.toString());
	            	item.set("RESULTADO_VOTACION", result_vota);
	
		            item.store(cct);
				}
				if(organo.equals("PLEN")){
					
				}
				if(organo.equals("MESA")){
					
				}
				if(organo.equals("COMI")){
					
				}
	        }
		return new Boolean(true);
		
    	} catch(Exception e) {
    		logger.error(e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta",e);
        }
    }

	public void cancel(IRuleContext arg0) throws ISPACRuleException {	
	}

}
