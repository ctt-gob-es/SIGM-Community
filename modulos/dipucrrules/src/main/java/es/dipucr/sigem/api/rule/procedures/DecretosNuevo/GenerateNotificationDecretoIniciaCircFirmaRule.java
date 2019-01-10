package es.dipucr.sigem.api.rule.procedures.DecretosNuevo;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
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

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class GenerateNotificationDecretoIniciaCircFirmaRule implements IRule {

	private static final Logger LOGGER = Logger.getLogger(GenerateNotificationDecretoIniciaCircFirmaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			LOGGER.info("GenerateNotificacionDecreto - Init");

			String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();

			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();

			// Variables
			IItem entityDocument = null;
			int documentTypeId = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();

			IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");

			String numExp = rulectx.getNumExp();
			String idExt = "";
			int documentId = 0;

			Object connectorSession = null;
			String sFileTemplate = null;

			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			String sqlQueryPart = "WHERE (ROL != 'TRAS' OR ROL IS NULL) AND NUMEXP = '" + numExp + "' ORDER BY ID";
			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);

			// 2. Comprobar que hay algún participante para el cual generar su notificación
			if (participantes != null && participantes.toList().size() >= 1) {

				// 3. Obtener plantilla "Notificación Decreto"
				// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
				IItemCollection taskTpDocCollection = (IItemCollection) procedureAPI.getTaskTpDoc(idTramCtl);
				if (taskTpDocCollection == null || taskTpDocCollection.toList().isEmpty()) {
					throw new ISPACInfo(Messages.getString("error.decretos.acuses.TaskTpDoc"));
				} else {

					// Hay dos tipos de documento asociados al trámite: Decreto y Notificación Decreto
					// Necesitamos el de Notificación del Decreto
					Iterator<IItem> it = taskTpDocCollection.iterator();
					while (it.hasNext()) {
						IItem taskTpDoc = (IItem) it.next();
						if ("Notificación Decreto".equals(taskTpDoc.get("CT_TPDOC:NOMBRE"))) {
							documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
						}
					}

					// Comprobamos que haya encontrado el Tipo de documento
					if (documentTypeId != 0) {

						// Comprobar que el tipo de documento tiene asociado una plantilla
						IItemCollection tpDocsTemplatesCollection = (IItemCollection) procedureAPI
								.getTpDocsTemplates(documentTypeId);
						if (tpDocsTemplatesCollection == null || tpDocsTemplatesCollection.toList().isEmpty()) {
							throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
						} else {
							IItem tpDocsTemplate = (IItem) tpDocsTemplatesCollection.iterator().next();
							templateId = tpDocsTemplate.getInt("ID");

							// 4. Para cada participante generar una notificación
							for (int i = 0; i < participantes.toList().size(); i++) {
								try {
									connectorSession = gendocAPI.createConnectorSession();
									IItem participante = (IItem) participantes.toList().get(i);
									// Abrir transacción para que no se pueda
									// generar un documento sin fichero

									if (participante != null) {
										DocumentosUtil.setParticipanteAsSsVariable(cct, participante);

										if ((String) participante.get(ParticipantesUtil.ID_EXT) != null) {
											idExt = (String) participante.get(ParticipantesUtil.ID_EXT);
										} else {
											idExt = "";
										}

										entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
										documentId = entityDocument.getKeyInt();

										IItemCollection documentsCollection = entitiesAPI.getDocuments(
												rulectx.getNumExp(), "NOMBRE='Plantilla de Notificaciones'",
												"FDOC DESC");
										String infoPag = "";
										int idPlantilla = -1;
										String autor = "";
										String autorInfo = "";
										IItem document = null;
										if (documentsCollection != null && documentsCollection.next()) {
											document = (IItem) documentsCollection.iterator().next();
											infoPag = document.getString("INFOPAG");
											idPlantilla = document.getInt("ID");
											autor = document.getString("AUTOR");
											autorInfo = document.getString("AUTOR_INFO");
										}

										sFileTemplate = getFile(gendocAPI, connectorSession, infoPag, templateId,
												idPlantilla);

										// Generar el documento a partir la
										// plantilla "Notificación Decreto"
										IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId,
												documentId, templateId, sFileTemplate);

										// Referencia al fichero del documento
										// en el gestor documental
										entityTemplate.set("EXTENSION", extensionEntidad);
										String templateDescripcion = entityTemplate.getString("DESCRIPCION");
										templateDescripcion = templateDescripcion + " - " + cct.getSsVariable("NOMBRE");
										entityTemplate.set("DESCRIPCION", templateDescripcion);
										entityTemplate.set("DESTINO", cct.getSsVariable("NOMBRE"));
										/**
										 * INICIO[Teresa] Ticket#106#: añadir el
										 * campo id_ext
										 **/
										entityTemplate.set("DESTINO_ID", idExt);
										/**
										 * FIN[Teresa] Ticket#106#: añadir el
										 * campo id_ext
										 **/

										entityTemplate.set("AUTOR", autor);
										entityTemplate.set("AUTOR_INFO", autorInfo);

										entityTemplate.store(cct);

										// 5. Actualizar el campo
										// 'DECRETO_NOTIFICADO' con valor 'Y'
										IItem participanteAActualizar = entitiesAPI
												.getParticipant(participante.getInt("ID"));
										participanteAActualizar.set("DECRETO_NOTIFICADO", "Y");
										participanteAActualizar.store(cct);

										// Si todo ha sido correcto borrar las
										// variables de la session
										DocumentosUtil.borraParticipanteSsVariable(cct);

										deleteFile(sFileTemplate);

									}
								} catch (Exception e) {

									// Si se produce algún error se hace
									// rollback de la transacción
									cct.endTX(false);

									String message = "exception.documents.generate";
									String extraInfo = null;
									Throwable eCause = e.getCause();

									if (eCause instanceof ISPACException) {

										if (eCause.getCause() instanceof NoConnectException) {
											extraInfo = "exception.extrainfo.documents.openoffice.off";
										} else {
											extraInfo = eCause.getCause().getMessage();
										}
									} else if (eCause instanceof DisposedException) {
										extraInfo = "exception.extrainfo.documents.openoffice.stop";
									} else {
										extraInfo = e.getMessage();
									}
									LOGGER.error("Error al recuperar los documentos.: " + rulectx.getNumExp() + ". "
											+ e.getMessage(), e);
									throw new ISPACInfo(message, extraInfo);

								} finally {

									if (connectorSession != null) {
										gendocAPI.closeConnectorSession(connectorSession);
									}
								}
							} // for
						}

					} else {
						throw new ISPACInfo("No existe el tipo de documento Notificación Decreto.");
					}
				}
			}
			// Si todo ha sido correcto se hace commit de la transacción
			DecretosUtil.mandarAfirmarCircuitoFirmaEspecificoTodosDocumentosTramite(rulectx);

		} catch (Exception e) {
			LOGGER.error("Error al recuperar los documentos.: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// Empty method
	}

	/**
	 * Obtiene el fichero correspondiente al infoPag indicado
	 *
	 * @param rulectx
	 * @param infoPag
	 * @param templateId
	 * @return
	 * @throws ISPACException
	 */
	private String getFile(IGenDocAPI gendocAPI, Object connectorSession, String infoPag, int templateId,
			int idPlantilla) throws ISPACException {

		try {
			connectorSession = gendocAPI.createConnectorSession();
			File file = null;
			try {
				String extension = MimetypeMapping.getExtension(gendocAPI.getMimeType(connectorSession, infoPag));

				FileTemplateManager templateManager = null;

				// Obtiene el manejador de plantillas
				templateManager = (FileTemplateManager) FileTemplateManager.getInstance();

				// Se almacena documento
				String fileName = FileTemporaryManager.getInstance().newFileName("." + extension);

				String fileNamePath = templateManager.getFileMgrPath() + "/" + fileName;

				// Nombre de la plantilla
				String sName = Integer.toString(templateId) + "." + extension;

				// Control de plantillas por multientidad
				OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
				if (info != null) {
					String organizationId = info.getOrganizationId();
					// Se añade el numExp al nombre de la plantilla para evitar
					// colisiones al generar notificaciones simultaneamente
					// desde
					// dos expedientes distintos de la misma entidad
					sName = organizationId + "_" + idPlantilla + "_" + sName;
				}

				OutputStream out = new FileOutputStream(fileNamePath);
				gendocAPI.getDocument(connectorSession, infoPag, out);
				file = new File(fileNamePath);
				File file2 = new File(templateManager.getFileMgrPath() + "/" + sName);
				file.renameTo(file2);
				file.delete();

				OutputStream out2 = new FileOutputStream(fileNamePath);
				gendocAPI.getDocument(connectorSession, infoPag, out2);
				File file3 = new File(fileNamePath);
				File file4 = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sName);
				file3.renameTo(file4);
				file3.delete();

				return sName;
			} catch (FileNotFoundException e) {
				throw new ISPACRuleException("Error al intentar obtener el documento, no existe.", e);
			}
		} finally {
			if (connectorSession != null) {
				gendocAPI.closeConnectorSession(connectorSession);
			}
		}
	}

	/**
	 * Elimina el fichero template y el temporary correspondientes
	 *
	 * @param rulectx
	 * @param infoPag
	 * @param templateId
	 * @return
	 * @throws ISPACException
	 */
	private boolean deleteFile(String sFileTemplate) throws ISPACException {

		FileTemplateManager templateManager = null;

		try {

			// Obtiene el manejador de plantillas
			templateManager = (FileTemplateManager) FileTemplateManager.getInstance();

			boolean resultado = true;

			File fTemplate = new File(templateManager.getFileMgrPath() + "/" + sFileTemplate);
			File fTemporary = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sFileTemplate);

			if (fTemplate != null && fTemplate.exists() && !fTemplate.delete()) {
				LOGGER.error(
						"No se pudo eliminar el documento: " + templateManager.getFileMgrPath() + "/" + sFileTemplate);
				resultado = false;
			}

			if (fTemporary != null && fTemporary.exists() && !fTemporary.delete()) {
				LOGGER.error("No se pudo eliminar el documento: "
						+ FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sFileTemplate);
				resultado = false;
			}

			return resultado;
		} catch (Exception e) {
			// throw new ISPACRuleException("Error al eliminar el documento
			// "+FileTemplateManager.getInstance().getFileTemplateMgrPath() +
			// "/" + sFileTemplate, e);
			throw new ISPACRuleException(
					"Error al eliminar el documento " + templateManager.getFileMgrPath() + "/" + sFileTemplate, e);
		}
	}
}
