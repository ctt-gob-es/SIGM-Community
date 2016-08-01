package es.dipucr.contratacion.rule.comunicplace;

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

import es.dipucr.contratacion.common.DipucrFuncionesComunes;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class EnviarDocumentoAdicionalPLACERule implements IRule{
	
	public static final Logger logger = Logger.getLogger(EnviarDocumentoAdicionalPLACERule.class);
	public static String tipoDoc = "Doc-Aprob";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Boolean execute(IRuleContext rulectx) throws ISPACRuleException {
		/*try{
			
			/************************************************************************/
			//ClientContext cct = (ClientContext) rulectx.getClientContext();
			//IInvesflowAPI invesFlowAPI = cct.getAPI();
			//IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			//Compruebo que no se haya mandado antes el anuncio.
			/*IItemCollection colAdjudicacion = entitiesAPI.getEntities("CONTRATACION_ADJUDICACION", rulectx.getNumExp());
			Iterator <IItem> iterAdjudicacion = colAdjudicacion.iterator();
			if(iterAdjudicacion.hasNext()){
				IItem itemAdjudicacion = iterAdjudicacion.next();				
				if(itemAdjudicacion.getString("CONTRATACION_ADJUDICACION")!=null && itemAdjudicacion.getString("CONTRATACION_ADJUDICACION").equals("NO")){		*/
		
					DipucrFuncionesComunes.acuerdoDictamen(rulectx, tipoDoc);
				/*}
			}
		}catch(ISPACRuleException e){
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		} catch (ISPACException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		}*/
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean existedoc = false;
		try{
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------	
			//Compruebo que no se haya mandado antes el anuncio.
			/*IItemCollection colAdjudicacion = entitiesAPI.getEntities("CONTRATACION_ADJUDICACION", rulectx.getNumExp());
			Iterator <IItem> iterAdjudicacion = colAdjudicacion.iterator();
			if(iterAdjudicacion.hasNext()){
				IItem itemAdjudicacion = iterAdjudicacion.next();				
				if(itemAdjudicacion.getString("CONTRATACION_ADJUDICACION")!=null && itemAdjudicacion.getString("CONTRATACION_ADJUDICACION").equals("NO")){*/
			
					//1º compruebo que haya insertado el tipo de documento que es
					String query = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND TRAMITE="+rulectx.getTaskId()+"";
					IItemCollection itemC = entitiesAPI.queryEntities("CONTRATACION_DOC_ADICIONALES", query);
					if(itemC.toList().size() > 0){
						//2º que el documento exista
						String nombreTipoDoc = DocumentosUtil.getTipoDocNombreByCodigo(cct, tipoDoc);
				    	query = "ID_TRAMITE="+rulectx.getTaskId()+" AND NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='"+nombreTipoDoc+"'";
						IItemCollection docsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), query, "FAPROBACION DESC");
						Iterator <IItem> docIterator = docsCollection.iterator();
						if(docIterator.hasNext()){
							IItem docPres = docIterator.next();
		
							String descripcion = "";
							if(docPres.getString("DESCRIPCION")!=null) descripcion= docPres.getString("DESCRIPCION");
							if(descripcion.length()<=50){
								existedoc = true;
							}
							else{
								rulectx.setInfoMessage("La descripción del documento es mayor de 50 carácteres");
							}
		
							
						}
						else{
							rulectx.setInfoMessage("No hay ningún documento a mandar.");
						}
					}
					else{
						rulectx.setInfoMessage("No se ha insertado el tipo de documento.");
					}
				/*}
				else{
		        	rulectx.setInfoMessage("No se ha podido mandar el anuncio a la Plataforma de Contratación porque en la entidad 'Resultado de la Licitación'"
		        			+ " en el campo Envío anuncio es igual a SI");
				}
			}*/
			
	    
			return existedoc;
		}
		catch(ISPACRuleException e){
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		} catch (ISPACException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		}
		
	}

}
