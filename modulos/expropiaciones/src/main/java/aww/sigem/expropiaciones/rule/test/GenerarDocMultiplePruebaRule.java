package aww.sigem.expropiaciones.rule.test;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.util.FileTemplateManager;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

/**
 * Generar múltiples documentos a partir de una plantilla de prueba.
 */
public class GenerarDocMultiplePruebaRule implements IRule {

	/** Logger de la clase. */
	private static final Logger logger = Logger
			.getLogger(GenerarDocMultiplePruebaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			logger.warn("Ejecutando regla GenerarDocMultiplePruebaRule");

			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
	
			logger.warn("Inicializando");
			IItem entityDocument = null;
			int documentTypeId = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();
			
			logger.warn("Stage id: " + rulectx.getStageId());

			IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			logger.warn("Obteniendo NumExp");
			String numExp = rulectx.getNumExp();
			logger.warn("NumEXP:" + numExp);
			int documentId = 0;
			Object connectorSession = null;
			
			/* */
			String sqlQueryPart = "WHERE NUMEXP = '" + numExp + "'";
			IItemCollection fincas = entitiesAPI.queryEntities("EXPR_FINCAS",
					sqlQueryPart);
			
			if ((fincas != null) && (fincas.toList().size() >= 1)) {
				logger.warn("Fincas no es null");
			}
			/**/
			
			IItemCollection taskTpDocCollection = procedureAPI
					.getTaskTpDoc(idTramCtl);
			
			logger.warn("idTramCtl: " + idTramCtl);
			
			if ((taskTpDocCollection == null)
					|| (taskTpDocCollection.toList().isEmpty())) {
				throw new ISPACInfo(// Messages.getString(
						"error.decretos.acuses.TaskTpDoc"
				// )
				);
			}

			logger.warn("Obteniendo Tipo de documento");
			Iterator it = taskTpDocCollection.iterator();
			
			logger.warn("Hay documentos: " + it.hasNext());
			
			while (it.hasNext()) {
				IItem taskTpDoc = (IItem) it.next();
				logger.warn("taskTpDoc: " + taskTpDoc.get("CT_TPDOC:NOMBRE"));
				if (taskTpDoc.get("CT_TPDOC:NOMBRE").equals(
				// "Notificación Decreto"
						"EXPR-xxx - Prueba plantilla")) {
					documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
					logger.warn("Obteniendo documentTypeId: " + documentTypeId);
				}

			}
			
			if (documentTypeId != 0) {
				logger.warn("Obteniendo plantilla ");
				IItemCollection tpDocsTemplatesCollection = procedureAPI
						.getTpDocsTemplates(documentTypeId);
				if ((tpDocsTemplatesCollection == null)
						|| (tpDocsTemplatesCollection.toList().isEmpty())) {
					throw new ISPACInfo(// Messages.getString(
							"error.decretos.acuses.tpDocsTemplates"
					// )
					);
				}
				IItem tpDocsTemplate = (IItem) tpDocsTemplatesCollection
						.iterator().next();
				templateId = tpDocsTemplate.getInt("ID");
				logger.warn("Obteniendo templateId:" + templateId);
			}
			
			
			
			logger.warn("iniciando creacion documento");
			String ids[] = null;

			ids = new String[] { String.valueOf(taskId),String.valueOf(taskId)  };

			try {
				logger.warn("creando ConnectorSession");
				connectorSession = gendocAPI.createConnectorSession();
				logger.warn("connectorSession creado");
				for (int i = 0; i < ids.length; i++) {
					logger.warn("for: vuelta " + i);
					int currentId = Integer.parseInt(ids[i]);
					logger.warn("currentId " + currentId);
					// Ejecución en un contexto transaccional
					boolean bCommit = false;

					try {
						logger.warn("abrir transaccion" + i);
						// Abrir transacción para que no se pueda generar un
						// documento sin fichero
						cct.beginTX();

						
						cct.setSsVariable("VUELTA", String.valueOf(i));
						
						
						// Crear el documento
						logger.warn("crear el documento (createTaskDocument) " + i);
						entityDocument = gendocAPI.createTaskDocument(
								currentId, documentTypeId);
						
						documentId = entityDocument.getKeyInt();
						logger.warn("documentId " + documentId);
						
						// Generar el documento a partir la plantilla
						// seleccionada
						logger.warn("generar documento a partir de plantilla " + i);
						IItem entityTemplate = null;
						entityTemplate = gendocAPI.attachTaskTemplate(
								connectorSession, currentId, documentId,
								templateId);
						
						// Referencia al fichero del documento en el gestor
						// documental
						String docref = entityTemplate.getString("INFOPAG");
						logger.warn("infotag: " + docref);
						String sMimetype = gendocAPI.getMimeType(
								connectorSession, docref);
						logger.warn("mimeType: " + sMimetype);
						entityTemplate.set("EXTENSION", MimetypeMapping
								.getExtension(sMimetype));
						entityTemplate.store(cct);
						
						logger.warn("Documento generado" );
						// Si todo ha sido correcto se hace commit de la
						// transacción
						bCommit = true;

						logger.warn("variable vuelta: " + cct.getSsVariable("VUELTA"));
						cct.deleteSsVariable("VUELTA");
						
						
					} catch (Exception e) {

						String message = "exception.documents.generate";
						String extraInfo = null;
						if (e instanceof ISPACException) {
							if (e.getCause() instanceof NoConnectException) {
								extraInfo = "exception.extrainfo.documents.openoffice.off";
							} else if (e.getCause() instanceof DisposedException) {
								extraInfo = "exception.extrainfo.documents.openoffice.stop";
							} else if (e.getCause() != null) {
								extraInfo = e.getCause().getMessage();
							} else {
								extraInfo = ((ISPACException) e).getMessage();
							}
						} else if (e instanceof DisposedException) {
							extraInfo = "exception.extrainfo.documents.openoffice.stop";
						} else {
							extraInfo = e.getMessage();
						}
						logger.error(message + " >>" + extraInfo);
						throw new ISPACInfo(message, extraInfo, false);

					} finally {
						cct.endTX(bCommit);
					}
				}

			} finally {
				if (connectorSession != null) {
					gendocAPI.closeConnectorSession(connectorSession);
				}
			}
			logger.warn("Fin de ejecución regla GenerarDocMultiplePruebaRule");
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}