package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Calendar;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class SaveDatosContratoRule  implements IRule {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(SaveDatosContratoRule.class);
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
    	return true;
    }

    @SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try {	
    	
	    	/*****************************************************************/
	    	ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			/*****************************************************************/
			
			
			IItem datosFormulario = rulectx.getItem();
			String tipo_contrato = datosFormulario.getString("TIPO_CONTRATO");
			String numcontratoDatosContrato = datosFormulario.getString("NCONTRATO");
			String proc_adj = datosFormulario.getString("PROC_ADJ");
			
			if(proc_adj!=null && !proc_adj.equals("")){
				
				logger.warn("numcontrato. "+numcontratoDatosContrato);
				//Si es menor que cero quiere decir que no tiene valor el número del conrtato.
				//numcontrato. -2147483648 valor cuando el campo es vacío
				if(numcontratoDatosContrato==null){
					logger.warn("tipo_contrato. "+tipo_contrato);
			        String cod_tipo_contrato = "";

			        if(tipo_contrato!=""){
			        	String [] v_cod_tipo_contrato = tipo_contrato.split(" - ");
			        	if(v_cod_tipo_contrato.length > 0){
			        		cod_tipo_contrato = v_cod_tipo_contrato[0];
			        	}

			        }
			
			    	String consulta = "WHERE TIPO_CONTRATO = '"+cod_tipo_contrato+"'";
			        logger.warn(consulta);
			        IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_CONT_TIPCONTRAT", consulta);
			        Iterator<IItem> it = collection.iterator();
			        if(it.hasNext()){
			        	 while (it.hasNext()){
			             	IItem procExpediente = (IItem)it.next();
			             	//año
			             	Calendar calendar = Calendar.getInstance();
			             	int year =calendar.get(Calendar.YEAR);
			             	//tipo contrato
			             	String tipoContrato = "";
			             	int longitud = cod_tipo_contrato.length();
			             	if(longitud == 1){
			             		tipoContrato = "0"+cod_tipo_contrato;
			             	}
			             	else{
			             		tipoContrato = cod_tipo_contrato;
			             	}
			             	//Numero de contrato
			             	int iNumContrato = procExpediente.getInt("CONTADOR_NUM_CONTRATO");
			             	String snumcontrato = (iNumContrato+1)+"";
			             	if(snumcontrato.length()==1){
			             		snumcontrato = "00"+snumcontrato;
			             	}
			             	if(snumcontrato.length()==2){
			             		snumcontrato = "0"+snumcontrato;
			             	}
			             	procExpediente.set("CONTADOR_NUM_CONTRATO", iNumContrato+1);
			             	procExpediente.store(cct);
			             	datosFormulario.set("NCONTRATO", year +" - "+ tipoContrato+ " - " +snumcontrato);
			             }
			        }
				}
			}	
			
    	} catch (ISPACException e) { 
    		logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return new Boolean(true);
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}
