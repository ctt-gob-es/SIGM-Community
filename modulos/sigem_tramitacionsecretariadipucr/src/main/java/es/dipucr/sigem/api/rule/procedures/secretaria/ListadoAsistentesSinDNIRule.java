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

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class ListadoAsistentesSinDNIRule implements IRule{
	
	protected static final Logger logger = Logger.getLogger(ListadoAsistentesPorGrupoRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }
    /*Muestra los asistentes a comision, mesa y junta pero que politicos*/
    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
	        String listado = "";  //Listado de asistentes 

	        IItemCollection collection = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(), "ASISTE='SI'", "NOMBRE ASC");

	        Iterator it = collection.iterator();
	        IItem item = null;
	        while (it.hasNext()) {
	        	item = ((IItem)it.next());
                String nombreParti = item.getString("NOMBRE");
                
                String strQueryExp = "WHERE IDENTIDADTITULAR = '" + nombreParti + "'";
                IItemCollection collectionExp = ExpedientesUtil.queryExpedientes(cct, strQueryExp);
                
    	        Iterator itExp = collectionExp.iterator();
    	        IItem itemExp = null;
    	        while (itExp.hasNext()) {
    	        	itemExp = ((IItem)itExp.next());
    	        	String numexpParti = itemExp.getString("NUMEXP");
    	        	
    	        	//Compruebo que sea politico para añadirlo al certificado de asistencia
    	        	//COnsultar con ese numero de expediente de ese usuario sacar el cargo que tiene
    	        	String strQueryPolitico = "WHERE NUMEXP = '" + numexpParti+ "'";
                    IItemCollection collectionPolitico = entitiesAPI.queryEntities("SECR_MIEMBRO", strQueryPolitico);
                    Iterator itPolitico = collectionPolitico.iterator();
                    IItem itemPolitico = null;
                    
                    while(itPolitico.hasNext()){
                    	itemPolitico = ((IItem)itPolitico.next());
                    	String patriPolitico = itemPolitico.getString("POLITICO");
                    	
                    	if(patriPolitico.equals("SI")){
                    		String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
                    		IItemCollection collectionCargo;
                    		if(strOrgano.equals("COMI")){
                    			String area = SecretariaUtil.getAreaSesion(rulectx, null);
                    			String strQueryCargo = "WHERE NUMEXP = '" + numexpParti + "' AND NOMBRE ='" +area+ "'";
                                collectionCargo = entitiesAPI.queryEntities("SECR_AREAS", strQueryCargo);
                    		}
                    		else{
                    			String strQueryCargo = "WHERE NUMEXP = '" + numexpParti + "' AND NOMBRE ='" +strOrgano+ "'";
                                collectionCargo = entitiesAPI.queryEntities("SECR_ORGANO", strQueryCargo);
                    		}
                    		if(collectionCargo != null){
                    			Iterator itCargo = collectionCargo.iterator();
                    	        IItem itemCargo = null;
                    	        while (itCargo.hasNext()) {
    	                    		itemCargo = ((IItem)itCargo.next());
    	            	        	String patriCargo = itemCargo.getString("CARGO");
    	            	        	if(patriCargo!= null){
    	            	        		if(!patriCargo.equals("PRES")){
    	            	        			listado += itemExp.getString("OBSERVACIONES")+" "+ DipucrCommonFunctions.transformarMayusMinus(nombreParti)+"\n";
    		            	        	}
    	            	        	}
                    	        }
                    		}
                    	}
                    }
    	        }
	        }
    		return listado;
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de asistentes",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}
