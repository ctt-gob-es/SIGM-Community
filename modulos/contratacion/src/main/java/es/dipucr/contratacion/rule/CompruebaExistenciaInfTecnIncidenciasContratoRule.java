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

import java.util.Iterator;

import org.apache.log4j.Logger;

public class CompruebaExistenciaInfTecnIncidenciasContratoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(CompruebaExistenciaInfTecnIncidenciasContratoRule.class);

	public void cancel(IRuleContext arg0) throws ISPACRuleException {	
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean docInformeTecnico = false;
		try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			
	        
	        String nombreDescripcionDoc = "Informe Técnico";
	        IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE="+rulectx.getTaskId()+" AND NOMBRE='"+nombreDescripcionDoc+"'", "FDOC DESC");
	        Iterator<IItem> itDoc = documentsCollection.iterator();
	        if(itDoc.hasNext()){
	        	docInformeTecnico = true;
	        }
	        else{
	        	rulectx.setInfoMessage("Falta por introducir el Informe Técnico en el trámite de Solicitud de Informe sobre Incidencias de Contrato");
	        }
	        
    	 }
    	catch(ISPACException e) 
        {
    		logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al comprobar si existe el informe técnico. Numexp. "+rulectx.getNumExp()+" Error. "+e.getMessage(),e);
        }
		return docInformeTecnico;
	}

}
