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

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class ExisteDocumInforSupervisionPresupMayorCantidadRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(ExisteDocumInforSupervisionPresupMayorCantidadRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		return new Boolean (true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean terminarTramite = true;
		try{
			//------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //------------------------------------------------------------------
			IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Petición Contrato' ORDER BY ID ASC");
	        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
	        while (exp_relacionadosIterator.hasNext()){
	        	String numexpPeticion = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_PADRE");	 
	        	if(numexpPeticion!=null){
	        		
	        		IItemCollection collectionPeticion = entitiesAPI.getEntities("CONTRATACION_PETICION", numexpPeticion);
					Iterator<IItem> itPeticion = collectionPeticion.iterator();					
					if (itPeticion.hasNext()) {
						IItem peticion = itPeticion.next();
						//Obtengo el presupuesto
						String presupuesto = peticion.getString("PRESUPUESTO");
						//Compruebo si el presupuesto es mayor de 350000, si esto es así, compruebo que exista el documento Informe de supervisión.
						double presupuestoD = new Double(presupuesto);
						double cantidadMAX = 350000.00;
						if(presupuestoD >= cantidadMAX){
							//Obtengo el id del tipo de documento a partir del código para luego hacer la búsqueda en spac_dt_documentos 'ID_TPDOC'
							//Informe de supervisión
							String strQuery = "WHERE COD_TPDOC = 'INFSUP'";
					        IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
					        Iterator<IItem> it = collection.iterator();
					        int tpdoc = 0;
					        if (it.hasNext())
					        {
					        	IItem tpd = (IItem)it.next();
					        	tpdoc = tpd.getInt("ID");
					        	if(tpdoc>0){
					        		IItemCollection itCollDocPPT = entitiesAPI.getDocuments(numexpPeticion, "ID_TPDOC = "+tpdoc+"", "FDOC DESC");
						        	
						        	if(itCollDocPPT.toList().size() == 0){
						        		terminarTramite = false;
						        		rulectx.setInfoMessage("Falta por introducir el documento 'Informe de Supervisión, ya que el presupuesto es mayor de 350000.'");
						        	}
					        	}//Fin if>0
					        }//fin bucle
						}//Fin comprobacion cantidad
					}
	        	}
	        	
	        }
		}catch(Exception e) 
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la comprobación del documento Informe de Supervisión. ",e);
        }
		return terminarTramite;
	}

}
