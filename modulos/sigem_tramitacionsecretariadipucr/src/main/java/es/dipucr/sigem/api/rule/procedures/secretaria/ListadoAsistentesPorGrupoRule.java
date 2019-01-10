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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ListadoAsistentesPorGrupoRule implements IRule{
	protected static final Logger LOGGER = Logger.getLogger(ListadoAsistentesPorGrupoRule.class);

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

	        Hashtable <String,String> htCargosGob = new Hashtable<String, String>();
	        
	        /**Carga de los tipos de cargos politicos existente**/
	        String [] htCargosOrden = cargaCargos(entitiesAPI, htCargosGob);
	        
	        /*Hash con los datos finales de los usuarios y los cargos que le corresponde*/
	        Hashtable <String,Vector> htResultAsistentes = new Hashtable<String, Vector>();
	        
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
                
                String strQueryExp = "WHERE NIFCIFTITULAR = '" + dniParti + "' AND FCIERRE IS null";
                IItemCollection collectionExp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, strQueryExp);
                
    	        Iterator itExp = collectionExp.iterator();
    	        IItem itemExp = null;
    	        while (itExp.hasNext()) {
    	        	itemExp = ((IItem)itExp.next());
    	        	String numexpParti = itemExp.getString("NUMEXP");
    	        	//COnsultar con ese numero de expediente de ese usuario sacar el cargo que tiene
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
        	        while (itCargo.hasNext()) {
        	        	itemCargo = ((IItem)itCargo.next());
        	        	String patriCargo = itemCargo.getString("CARGO");
        	        	if(patriCargo!=null){
	        	        	String particargoSusti = htCargosGob.get(patriCargo);
	        	        	if(particargoSusti!= null){
	        	        		LOGGER.warn("nombreParti "+nombreParti);
	        	        		String nomParticipante = itemExp.getString("OBSERVACIONES")+" "+ DipucrCommonFunctions.transformarMayusMinus(nombreParti);
	        	        		insertarCargoSustituto(htResultAsistentes,nomParticipante, particargoSusti);
	        	        	}
        	        	}
        	        }   	        	
    	        	
    	        }
	        }
	        listado = mostrarInformacion(htResultAsistentes, htCargosGob, htCargosOrden);
    		return listado;
    		
        } catch(Exception e) {
        	LOGGER.error("Error al obtener los participantes. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de asistentes "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
    }

	@SuppressWarnings("rawtypes")
	private String mostrarInformacion(
			Hashtable<String, Vector> htResultAsistentes, Hashtable<String, String> htCargosGob, String[] htCargosOrden) {
		
		String resultado = "";
	
		String obj;  
		for(int i=0;i<htCargosOrden.length;i++) {  
			obj = (String) htCargosOrden[i];
			String cargo = htCargosGob.get(obj);
			if(!obj.equals("")){
				Vector part = htResultAsistentes.get(cargo);
				if(part!=null){
					resultado += cargo +"\n";
					for(int j=0 ;j<part.size();j++){
						resultado+="\t"+part.get(j)+"\n";
					}
				}
			}
		} 

		return resultado;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void insertarCargoSustituto(Hashtable<String, Vector> htResultAsistentes, String nombreParti,
			String particargoSusti) {
		if(!htResultAsistentes.isEmpty()){
			Vector vRes = htResultAsistentes.get(particargoSusti);
			if(vRes!=null){
				vRes.add(nombreParti);
			}
			else{
				Vector v = new Vector();
				v.add(nombreParti);
				htResultAsistentes.put(particargoSusti, v);
			}
		}
		else{
			Vector v = new Vector();
			v.add(nombreParti);
			htResultAsistentes.put(particargoSusti, v);
		}
	}

	@SuppressWarnings("rawtypes")
	private String [] cargaCargos(IEntitiesAPI entitiesAPI, Hashtable<String, String> htCargos) throws ISPACRuleException{
		String [] htResult = null;
		
        try {
			IItemCollection collection = entitiesAPI.queryEntities("SECR_VLDTBL_CARGOSGOB","ORDER BY ORDEN");
			Iterator it = collection.iterator();
	        IItem item = null;
	        htResult = new String [collection.toList().size()];
	        int i = 0;
	        while (it.hasNext()) {
	        	item = ((IItem)it.next());
	        	String valor = item.getString("VALOR");
	        	String sustituto = item.getString("SUSTITUTO");
	        	htCargos.put(valor, sustituto);
	        	htResult[i] = valor;
	        	i++;
	        } 
		} catch (ISPACException e) {
			LOGGER.error("Error al obtener los participantes.  - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener los participantes.  - "+e.getMessage(), e);
		}
		
		return htResult;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}
