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

import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;

public class CambioCaracteresRarosDescripcionNombreRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(CambioCaracteresRarosDescripcionNombreRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean respuesta = true;
		try{
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
		
			String query = "ID_TRAMITE='"+rulectx.getTaskId()+"' AND NOMBRE NOT LIKE '%Informe Necesidad Contrato%' AND NOMBRE!='Pliego de Prescripciones Técnicas'";
			IItemCollection docsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), query, "FDOC DESC");
			
			Iterator <IItem> docIterator = docsCollection.iterator();
			while(docIterator.hasNext()){
				
				IItem docPres = docIterator.next();
				String descripcion = docPres.getString("DESCRIPCION");
				rulectx.setInfoMessage("La descripción del documento ha cambiado "+descripcion);
				descripcion = DipucrFuncionesComunesSW.quitarExtensionNombre(descripcion);
				descripcion = DipucrFuncionesComunesSW.limpiarCaracteresEspeciales(descripcion);
				docPres.set("DESCRIPCION", descripcion);
				docPres.store(cct);			
			}
		}catch(ISPACRuleException e){
			logger.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		} 
		
		return respuesta;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
