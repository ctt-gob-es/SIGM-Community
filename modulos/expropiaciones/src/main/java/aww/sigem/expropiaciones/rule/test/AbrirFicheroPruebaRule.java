package aww.sigem.expropiaciones.rule.test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.Properties;
import ieci.tdw.ispac.api.item.Property;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

public class AbrirFicheroPruebaRule implements IRule {

		/** Logger de la clase. */
		private static final Logger logger = Logger
				.getLogger(AbrirFicheroPruebaRule.class);

		public boolean init(IRuleContext rulectx) throws ISPACRuleException {
			return true;
		}

		public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
			return true;
		}

		public Object execute(IRuleContext rulectx) throws ISPACRuleException {
			try {
				logger.warn("Ejecutando regla AbrirFicheroPruebaRule");

				IClientContext cct = rulectx.getClientContext();
				IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
				IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
				IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
		
				logger.warn("Inicializando");
				logger.warn("Obteniendo NumExp");
				String numExp = rulectx.getNumExp();
				logger.warn("NumEXP:" + numExp);
				logger.warn("Obteniendo TaskId");
				int taskId = rulectx.getTaskId();
				logger.warn("TaskId:" + taskId);
				
				
				logger.warn("Obteniendo información de los documentos del trámite");
				IItemCollection taskDocumentosColeccion = entitiesAPI.getTaskDocuments(numExp, taskId);
				
				Iterator itDocumentos = taskDocumentosColeccion.iterator();
				
				if(!itDocumentos.hasNext()){
					logger.warn("No hay Documentos");
				}
				
				String refDoc = null;
				java.sql.Timestamp fDoc = null;
				while(itDocumentos.hasNext()){
					logger.warn("Documento");
					IItem documento = (IItem) itDocumentos.next();
				
					java.sql.Timestamp fItem = (java.sql.Timestamp) documento.get("SPAC_DT_DOCUMENTOS:FDOC");
					logger.warn("FDoc: " + fItem);
					if(fDoc!=null) {
						if(fItem.after(fDoc)) {
							logger.warn("Este item es mas nuevo");
							fDoc = fItem;
							refDoc = (String) documento.get("SPAC_DT_DOCUMENTOS:INFOPAG");
							logger.warn("RefDoc: " + refDoc); 
						}
					} else {
						fDoc = fItem;
						refDoc = (String) documento.get("SPAC_DT_DOCUMENTOS:INFOPAG");
						logger.warn("RefDoc: " + refDoc);
					}
					
				} 
				
				logger.warn("Creo el outputstream");
				OutputStream osDocumento = new ByteArrayOutputStream();
		
				logger.warn("Obteniendo documento y guardándolo en el OutputStream");
				gendocAPI.getDocument(cct, refDoc, osDocumento); 
				logger.warn("Documento obtenido");
				
				logger.warn("Contenido: " +osDocumento.toString());
				
							
				logger.warn("Fin de ejecución regla AbrirFicheroPruebaRule");
			} catch (Exception e) {
				logger.error("Se produjo una excepción", e);
				throw new ISPACRuleException(e);
			}
			return null;
		}

		public void cancel(IRuleContext rulectx) throws ISPACRuleException {

		}


}
