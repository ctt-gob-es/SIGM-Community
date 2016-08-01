package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class CargaDatosAdjudicadoRule implements IRule{

	public void cancel(IRuleContext arg0) throws ISPACRuleException {

		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			//------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //------------------------------------------------------------------
			IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"' AND RELACION='Plica' ORDER BY ID ASC");
	        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
	        String query = "";
	        while (exp_relacionadosIterator.hasNext()){
	        	String numexpHijo = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");
	        	query += "'"+numexpHijo+"',";	        	
	        }
	        		
			if(query.length()>0){
				query = query.substring(0,query.length()-1);
		    }

	    	
	    	String consulta= "WHERE NUMEXP IN ("+query+") AND APTO='SI' AND CLASIFICACION=1";
        	IItemCollection plicaCol = entitiesAPI.queryEntities("CONTRATACION_PLICA", consulta);
	    	Iterator<IItem> itPlica = plicaCol.iterator();

	    	if(itPlica.hasNext()){
	    		
	    		IItem plica = (IItem)itPlica.next();
	    		IItem resultadoAdj = entitiesAPI.createEntity("CONTRATACION_ADJUDICACION", rulectx.getNumExp());
	    		resultadoAdj.set("IDENTIFICADOR", plica.getString("DNI"));
	    		resultadoAdj.set("TIPOIDENTIFICADOR", "CIF - CIF");
	    		//Obtenemos el nombre del titular
	    		IItem exp = entitiesAPI.getExpedient(plica.getString("NUMEXP"));
	    		resultadoAdj.set("NOMBRE_COMPLETO", exp.getString("IDENTIDADTITULAR"));
	    		//Iva
	    		consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
	 			IItemCollection peticion = entitiesAPI.queryEntities("CONTRATACION_PETICION", consulta);
	 			Iterator<IItem> itPeticion = peticion.iterator();
	 			if(itPeticion.hasNext()){
	 				IItem itemPeticion = itPeticion.next();
	 				resultadoAdj.set("IMPORTE_CON_IVA", itemPeticion.getString("TOTAL"));
	 				resultadoAdj.set("IMPORTE_SIN_IVA", itemPeticion.getString("PRESUPUESTO"));
	 			}
	 			resultadoAdj.store(cct);
	    	}
	
		}catch(Exception e) 
		{
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException(e);
        }
		return new Boolean (true);
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {

		return true;
	}

}
