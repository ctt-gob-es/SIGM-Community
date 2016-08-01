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

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class NoTerminarTramitePropuestaRule implements IRule {

	private static final Logger logger = Logger.getLogger(NoTerminarTramitePropuestaRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
				
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean cierra = true;
		Vector<Boolean> vCierra = new Vector<Boolean>();
		try {
			/**
			 * 1º que este firmado el documento 'Documentación de Propuesta'
			 * 2º Que tenga expedientes relacionados
			 * 3º que los expedientes relacionado tengan cerrados el certificado de acuerdo y el dictamen de acuerdo
			 * **/
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        IItemCollection itemCollDoc = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TPDOC=207", "");
	        Iterator<?> itDoc = itemCollDoc.iterator();
	        
	        if(itDoc.hasNext()){
	        	while(itDoc.hasNext()){
		        	IItem itemDoc = (IItem)itDoc.next();
		        	String infopag_rde = "";
		        	//1º prueba
		        	if(itemDoc.getString("INFOPAG_RDE")!=null) infopag_rde = itemDoc.getString("INFOPAG_RDE"); else infopag_rde = "";
		        	if(!infopag_rde.equals("")){
		        		//2º prueba
			        	String sQuery = "WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"'";
			        	IItemCollection iitemCoExpRel = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, sQuery);
			        	Iterator<?> iExpRel = iitemCoExpRel.iterator();
			        	if(iExpRel.hasNext()){
			        		while(iExpRel.hasNext()){
				        		IItem itExpRel = (IItem)iExpRel.next();
				        		String numexp_hijo = "";
				        		if(itExpRel.getString("NUMEXP_HIJO")!=null) numexp_hijo = itExpRel.getString("NUMEXP_HIJO"); else numexp_hijo = "";
				        		sQuery = "WHERE NUMEXP='"+numexp_hijo+"' AND (NOMBRE='Certificado de acuerdos' OR NOMBRE='Notificaciones y traslado de dictamenes')";
				        		IItemCollection iitemCoTram = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, sQuery);
					        	Iterator<?> iExpCoTram = iitemCoTram.iterator();
					        	//Punto 1
					        	if(iExpCoTram.hasNext()){
					        		IItem itExpConTram = (IItem)iExpCoTram.next();
					        		int estado = 0;
					        		 estado = itExpConTram.getInt("ESTADO");
					        		 if(estado != 3){
					        			 vCierra.add(false);
					        		 }
					        	}
					        	else{
					        		cierra = false;
					        		rulectx.setInfoMessage("No se puede cerrar el trámite, porque no se ha dictaminado o certificado la Propuesta");
					        	}
				        	}
			        	}
			        	else{
			        		cierra = false;
			        		rulectx.setInfoMessage("No se puede cerrar el trámite, porque no ha relacionado el expediente");
			        	}
		        	}
		        	else{
		        		cierra = false;
		        		rulectx.setInfoMessage("No se puede cerrar el trámite, porque el documento 'Documentación de Propuesta' esta sin firmar");
		        	}	        	
		        }
	        }
	        else{
	        	cierra = false;
        		rulectx.setInfoMessage("No se puede cerrar el trámite, porque no existe el documento 'Documentación de Propuesta'");
	        }
	        //Compruebo si cierra=true es porque ha ido todo bien hasta el Punto 1 y me queda comprobar el vector
	        //ya que si es false quiere decir que ha habido un error antes y se a puesto esa variable antes a false.
	        if(cierra){
		        for(int i=0; i< vCierra.size() && cierra; i++){
		        	cierra = (Boolean) vCierra.get(i);
		        }
		        if(!cierra){
		        	rulectx.setInfoMessage("No se puede cerrar el trámite, porque no se ha certificado o dictaminado la propuesta");
		        }
	        }
	        
		}
		catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	
		return cierra;
	}
}
