package es.dipucr.contratacion.common.participantes;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class InsertarDepartamentoTrasladoValoresAnormaRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(InsertarDepartamentoTrasladoValoresAnormaRule.class);

	public void cancel(IRuleContext arg0) throws ISPACRuleException {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
	 		//--------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //-----------------------------------------------------------------------------
	        
	        String numexpContratacion = calculaExpContratacion(rulectx);

	        //Obtengo el numexp del procedimiento de Petición de contratación
	        String sqlQueryPart = "WHERE NUMEXP_HIJO='"+numexpContratacion+"' AND RELACION='Petición Contrato'";
	        logger.warn("numexpContratacion "+numexpContratacion);
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        Iterator<IItem> itExpRel = exp_relacionados.iterator();
	        String numexpPetCont = "";
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpPetCont = itemExpRel.getString("NUMEXP_PADRE");
	        }
        	
			//Obtener Participantes de la propuesta actual, con relación "Trasladado"
 			sqlQueryPart = "WHERE ROL= 'TRAS' AND NUMEXP = '"+numexpPetCont+"' ORDER BY ID";
 			logger.warn(" sqlQueryPart "+sqlQueryPart);
			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
			
			
			// Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
			if(participantes != null && participantes.toList()!= null && participantes.toList().size() != 0){
				for (int i=0; i<participantes.toList().size(); i++)
				{
					IItem participante = (IItem) participantes.toList().get(i);
					
					IItem itempartDep = entitiesAPI.createEntity("SPAC_DT_INTERVINIENTES","");
					
					//Sobre A
					itempartDep.set("NUMEXP", rulectx.getNumExp());
					itempartDep.set("ID_EXT", participante.getString("ID_EXT"));
					itempartDep.set("ROL", participante.getString("ROL"));
					itempartDep.set("TIPO_PERSONA", participante.getString("TIPO_PERSONA"));
					itempartDep.set("NDOC", participante.getString("NDOC"));
					itempartDep.set("NOMBRE", participante.getString("NOMBRE"));
					itempartDep.set("EMAIL", participante.getString("EMAIL"));
					itempartDep.set("DIRECCIONTELEMATICA", participante.getString("DIRECCIONTELEMATICA"));
					itempartDep.store(cct);
					
				}
			}
			logger.warn("FIN TrasladarSubsanacionInforme. "); 
	            
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
        		throw new ISPACRuleException("Error. "+e.getMessage(),e);
        	throw new ISPACRuleException("No se ha podido obtener la fecha actual "+e.getMessage(),e);
        }
		return new Boolean(true);
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
	            logger.warn("InsertarDepartamentoTrasladoValoresAnormaRule  "+consultaSQL);
	            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
	            @SuppressWarnings("unchecked")
				Iterator<IItem> itExpRelLicitador = itemCollection.iterator();
	            while(itExpRelLicitador.hasNext()){
	            	IItem padreLicitador = itExpRelLicitador.next();
	            	String numexpPlica = padreLicitador.getString("NUMEXP_PADRE");
	            	logger.warn("WHERE NUMEXP_HIJO = '" + numexpPlica + "' AND RELACION = 'Plica'");
	            	 IItemCollection itemCollContratacion = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_HIJO = '" + numexpPlica + "' AND RELACION = 'Plica'");
	            	 @SuppressWarnings("unchecked")
	 				Iterator<IItem> itExpRelContratacion = itemCollContratacion.iterator();
	            	 while(itExpRelContratacion.hasNext()){
	            		 IItem expContratacion = itExpRelContratacion.next();
	            		 numexpConta = expContratacion.getString("NUMEXP_PADRE");
	            		 logger.warn("numexpConta "+numexpConta);
	            	 }
	            }
	            
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
        		throw new ISPACRuleException("Error. "+e.getMessage(),e);
        	throw new ISPACRuleException("No se ha podido obtener la fecha actual "+e.getMessage(),e);
        }
		return numexpConta;
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		// TODO Auto-generated method stub
		return true;
	}

}
