package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class ListadoAsistentesPorGrupoDiputadosRule implements IRule{
	protected static final Logger logger = Logger.getLogger(ListadoAsistentesPorGrupoRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        /**Carga de los tipos de cargos politicos existente**/
	        Hashtable <String,String> htCargosGob = cargaCargos(entitiesAPI);
	        
	        /*Hash con los datos finales de los usuarios y los cargos que le corresponde*/
	        Hashtable <String,Vector> htResultAsistentes = new Hashtable();
	        
	        String listado = "";  //Listado de asistentes 

	        IItemCollection collection = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(), "ASISTE='SI'", "NOMBRE ASC");
	        Iterator it = collection.iterator();
	        IItem item = null;
	        
	      //Obtenemos los miembros del órgano correspondiente
	        String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
	        
	        while (it.hasNext()) {
	        	
                item = ((IItem)it.next());
                String nombreParti = item.getString("NOMBRE");
                String dniParti = item.getString("NDOC");
                logger.warn("nombreParti "+nombreParti+" dniParti "+dniParti);
                
                String strQueryExp = "WHERE NIFCIFTITULAR = '" + dniParti + "' AND FCIERRE IS null";
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
                    //logger.warn("[ListadoAsistentesPorGrupoRule:execute()]collectionPolitico "+collectionPolitico.toList().size());
                    Iterator itPolitico = collectionPolitico.iterator();
                    IItem itemPolitico = null;
                    
                    logger.warn("numexpParti "+numexpParti);
                    
                    while(itPolitico.hasNext()){
                    	itemPolitico = ((IItem)itPolitico.next());
                    	String patriPolitico = itemPolitico.getString("POLITICO");
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
                		Iterator itCargo = collectionCargo.iterator();
            	        IItem itemCargo = null;
                    	//Compruebo si es Politico
                    	if(patriPolitico!=null && patriPolitico.equals("SI")){
                    		
                	        while (itCargo.hasNext()) {
	                    		itemCargo = ((IItem)itCargo.next());
	            	        	String patriCargo = itemCargo.getString("CARGO");
	            	        	if(patriCargo!=null){
	                	        	String particargoSusti = htCargosGob.get(patriCargo);
	                	        	if(particargoSusti!=null){
	                	        		String nomParticipante = itemExp.getString("OBSERVACIONES")+" "+ DipucrCommonFunctions.transformarMayusMinus(nombreParti);
	                	        		insertarCargoSustituto(htResultAsistentes,nomParticipante, particargoSusti);
	                	        	}
	            	        	}
                	        }
                    	}
                    	//Si no es politico miro si esa persona es presedente
                    	else{
                    		while (itCargo.hasNext()) {
	                    		itemCargo = ((IItem)itCargo.next());
	            	        	String patriCargo = itemCargo.getString("CARGO");
	            	        	if(patriCargo!=null){
	            	        		if(patriCargo.equals("PRES")){
		                	        	String particargoSusti = htCargosGob.get(patriCargo);
		                	        	if(particargoSusti!=null){
		                	        		String nomParticipante = itemExp.getString("OBSERVACIONES")+" "+ DipucrCommonFunctions.transformarMayusMinus(nombreParti);
		                	        		insertarCargoSustituto(htResultAsistentes,nomParticipante, particargoSusti);
		                	        	}
	            	        		}
	            	        	}
                	        }
                    	}
                    }  	        	
    	        	
    	        }
	        }
	        listado = mostrarInformacion(htResultAsistentes);
    		return listado;
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de asistentes",e);
        }
    }

	@SuppressWarnings("rawtypes")
	private String mostrarInformacion(
			Hashtable<String, Vector> htResultAsistentes) {
		
		String resultado = "";
		
		Enumeration e = htResultAsistentes.keys();  

		String obj;  

		while (e.hasMoreElements()) {  
			obj = (String) e.nextElement();
			resultado += obj +"\n";
			Vector part = htResultAsistentes.get(obj);
			for(int i=0 ;i<part.size();i++){
				//String res = (String) part.get(i);

				resultado+="\t"+part.get(i)+"\n";
			}
			resultado+="\n";
		} 

		return resultado;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void insertarCargoSustituto(Hashtable<String, Vector> htResultAsistentes, String nombreParti,
			String particargoSusti) {
		//logger.warn("[ListadoAsistentesPorGrupoRule:insertarCargoSustituto()]");
		if(!htResultAsistentes.isEmpty()){
			//logger.warn("[ListadoAsistentesPorGrupoRule:insertarCargoSustituto()]no es nulo");
			Vector vRes = htResultAsistentes.get(particargoSusti);
			if(vRes!=null){
				vRes.add(nombreParti);
				//logger.warn("[ListadoAsistentesPorGrupoRule:insertarCargoSustituto()]nombreParti "+nombreParti);
			}
			else{
				//logger.warn("[ListadoAsistentesPorGrupoRule:insertarCargoSustituto()]Inserto los datos");
				Vector v = new Vector();
				v.add(nombreParti);
				htResultAsistentes.put(particargoSusti, v);
			}
			
		}
		else{
			//logger.warn("[ListadoAsistentesPorGrupoRule:insertarCargoSustituto()]insertando");
			Vector v = new Vector();
			v.add(nombreParti);
			htResultAsistentes.put(particargoSusti, v);
		}
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Hashtable <String,String> cargaCargos(IEntitiesAPI entitiesAPI) throws ISPACRuleException{
		Hashtable <String,String> htResult = new Hashtable ();
		
        try {
			IItemCollection collection = entitiesAPI.queryEntities("SECR_VLDTBL_CARGOSGOB","");
			Iterator it = collection.iterator();
	        IItem item = null;
	        while (it.hasNext()) {
	        	item = ((IItem)it.next());
	        	String valor = item.getString("VALOR");
	        	//logger.warn("[ListadoAsistentesPorGrupoRule:cargaCargos()]VALOR "+valor);
        		String sustituto = item.getString("SUSTITUTO");
	        	//logger.warn("[ListadoAsistentesPorGrupoRule:cargaCargos()]VALOR "+valor+" sustituto "+sustituto);
	        	htResult.put(valor, sustituto);
	        } 
		} catch (ISPACException e) {
			throw new ISPACRuleException("No se ha podido obtener la lista de los cargos de gobierno",e);
		}
       
		
		return htResult;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}

