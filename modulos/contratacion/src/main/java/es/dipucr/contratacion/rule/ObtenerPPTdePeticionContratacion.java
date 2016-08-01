package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class ObtenerPPTdePeticionContratacion implements IRule {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(ObtenerPPTdePeticionContratacion.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {	
	    	
	    	/*****************************************************************/
	    	ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesflowAPI.getGenDocAPI();
			Object connectorSession = genDocAPI.createConnectorSession();
			/*****************************************************************/
			
			logger.warn("Inicio obtener documento de la petición de contratación");
			
			//Obtengo el número de expediente de la petición de contrato
			String consulta = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION LIKE '%Petición Contrato%'";
			IItemCollection itCollExpRel = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consulta);
			
			Iterator<IItem> itExpRel = itCollExpRel.iterator();
	        if (itExpRel.hasNext())
	        {
	        	IItem iPetCont = itExpRel.next();
	        	String peticContratacion = iPetCont.getString("NUMEXP_PADRE");
	        	
	        	IItemCollection itCollDocPPT = entitiesAPI.getDocuments(peticContratacion, "NOMBRE = 'Pliego de Prescripciones Técnicas'", "FDOC DESC");
	        	
	        	if(itCollDocPPT.toList().size() > 0){
	        		Iterator<IItem> itDoc = itCollDocPPT.iterator();
	        		while(itDoc.hasNext()){
	        			IItem iPPT = itDoc.next();
	        			
	        			
	        			IItem documentPPT = (IItem)genDocAPI.createTaskDocument(rulectx.getTaskId(), iPPT.getInt("ID_TPDOC"));
	        			documentPPT.set("DESCRIPCION", iPPT.getString("DESCRIPCION"));
	        			
	        			String infopag = iPPT.getString("INFOPAG_RDE");
	        			String extension = iPPT.getString("EXTENSION_RDE");
	        			if(infopag==null){
	        				infopag = iPPT.getString("INFOPAG"); 
	        				documentPPT.set("EXTENSION", iPPT.getString("EXTENSION"));
	        				extension = iPPT.getString("EXTENSION");
	        			}
	        			else{
	        				documentPPT.set("EXTENSION", iPPT.getString("EXTENSION_RDE"));
	        				documentPPT.set("FAPROBACION", iPPT.getDate("FAPROBACION"));
	        				documentPPT.set("ESTADOFIRMA", iPPT.getString("ESTADOFIRMA"));
	        				documentPPT.set("BLOQUEO", iPPT.getString("BLOQUEO"));
	        			}
	        			        			
	        			
	        			File filePPT = DocumentosUtil.getFile(cct, infopag, null, null);
	        			
	        			FileInputStream inGD = new FileInputStream(filePPT);
	
	        			String mimeType = MimetypeMapping.getMimeType(extension);
	        			
						IItem docNuevo = genDocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), documentPPT.getInt("ID"), inGD, (int)filePPT.length(), mimeType, "Pliego de Prescripciones Técnicas");
						docNuevo.set("INFOPAG_RDE", docNuevo.getString("INFOPAG"));
						docNuevo.set("EXTENSION_RDE", mimeType);
						//Actualizar campos
						documentPPT.store(cct);
		
	        		}
	        	}
	        }
			
			
			
			logger.warn("Fin obtener documento de la petición de contratación");
		} catch (ISPACException e) { 
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

}
