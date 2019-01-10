package es.dipucr.sigem.api.rule.common.documento;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.TemplateDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.uno.Exception;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrAutoGeneraDocIniTramiteRule implements IRule {

	private static final Logger LOGGER = Logger.getLogger(DipucrAutoGeneraDocIniTramiteRule.class);

	protected String tipoDocumento;
	protected String plantilla;
	protected int templateId = 0;
    protected int documentTypeId = 0;
    
	protected String refTablas;
	protected boolean bEditarTextos;

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	/**
	 * Método execute
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		IClientContext cct = null;
		String numexp = "";
		
		try {
			LOGGER.info("INICIO - " + this.getClass().getName());

			numexp = rulectx.getNumExp();
			
			if(StringUtils.isNotEmpty(tipoDocumento) && StringUtils.isNotEmpty(plantilla)){
				cct = rulectx.getClientContext();
				IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
				IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();

				cct.endTX(true);
	
				int taskId = rulectx.getTaskId();
	
				IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
	
				String numExp = rulectx.getNumExp();
				
				documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);

				if (documentTypeId != 0) {

					cct.beginTX();
					setSsVariables(cct, rulectx);
					cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString(TramitesUtil.NOMBRE));
		
					IItem template = TemplateDAO.getTemplate(cct, plantilla, documentTypeId);
											
					if(template != null){
						templateId = template.getInt("ID");
						
						IItem documento = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, "", "");
						/**
				         * [Teresa# Ticket194] INICIO ALSIGM3 Cambio en la regla DipucrAutoGeneraDocIniTramiteRule para que añada el destino en el documento
				         * **/
						if(documento != null){
							String tipoRegistro = "";
							if((String)documento.getString(DocumentosUtil.TP_REG) != null) {
								tipoRegistro = documento.getString(DocumentosUtil.TP_REG);
							}
							
							if(tipoRegistro.equals(DocumentosUtil.TiposRegistros.SALIDA)){
								IItemCollection partCol = ParticipantesUtil.getParticipantesByRol(cct, numexp, ParticipantesUtil._TIPO_INTERESADO);
								Iterator<?> partIt = partCol.iterator();
								
								if(partIt.hasNext()){
									IItem part = (IItem)partIt.next();
									
									String idExt = "";
									if (StringUtils.isNotEmpty(part.getString(ParticipantesUtil.ID_EXT))) {
										idExt = (String)part.getString(ParticipantesUtil.ID_EXT); 
									}
									documento.set(DocumentosUtil.DESTINO_ID, idExt);
									
									String nombreDoc = "";
									if(StringUtils.isNotEmpty(documento.getString(DocumentosUtil.NOMBRE))) {
										nombreDoc = documento.getString(DocumentosUtil.NOMBRE);
									}
									
									String nombre = "";
									if (StringUtils.isNotEmpty(part.getString(ParticipantesUtil.NOMBRE))) {
										nombre = (String)part.getString(ParticipantesUtil.NOMBRE);
									}
									
									documento.set(DocumentosUtil.DESCRIPCION, nombreDoc + " - " + nombre);
									documento.store(cct);
								}
							}							
						}
						/**
				         * [Teresa# Ticket194] FIN ALSIGM3 Cambio en la regla DipucrAutoGeneraDocIniTramiteRule para que añada el destino en el documento
				         * **/
						
						deleteSsVariables(cct);
						cct.deleteSsVariable("NOMBRE_TRAMITE");

						String docRef = DocumentosUtil.getInfoPag(rulectx, documento.getInt("ID"));
						
						if (StringUtils.isNotEmpty(refTablas) || bEditarTextos){
							editarContenidoDocumento(gendocAPI, docRef, rulectx, documento.getKeyInt(), refTablas, entitiesAPI, numExp);
						}
					}				
				}
				cct.endTX(true);
			}
			LOGGER.info("FIN - " + this.getClass().getName());
		} catch (ISPACException e) {
			if(cct != null)
				try {
					cct.endTX(false);
				} catch (ISPACException e1) {
					LOGGER.error("Error al generar el documento en el expediente: " + numexp + ". " + e1.getMessage(), e1);
				}
			
			LOGGER.error("Error al generar el documento en el expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el documento en el expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return true;
	}

	/**
	 * Edita el documento con las librerías de OO, realizando dos procesos:
	 * - Insertando tablas
	 * - Editando los textos del documento
	 * @param gendocAPI
	 * @param docref
	 * @param rulectx
	 * @param documentId
	 * @param string
	 * @param entitiesAPI
	 * @param numexp
	 */
	public void editarContenidoDocumento(IGenDocAPI gendocAPI, String docref, IRuleContext rulectx, int documentId, String string, IEntitiesAPI entitiesAPI, String numexp) {
		
		Object connectorSession = null;
    	OpenOfficeHelper ooHelper = null;
    	try {
			//Abre el documento
    		//[dipucr-Felipe 3#249] Cambio la forma en la que se obtiene el documento
    		IClientContext cct = rulectx.getClientContext();
			File file = DocumentosUtil.getFile(cct, docref, null, null);
			ooHelper = OpenOfficeHelper.getInstance();
			XComponent xComponent = ooHelper.loadDocument("file://" + file.getPath());
			
			//Inserción de la tabla
			if (StringUtils.isNotEmpty(refTablas)){
				String[] refTabla = refTablas.split(",");			
				for(int i = 0; i<refTabla.length; i++){						
					insertaTabla(rulectx, xComponent, refTabla[i], entitiesAPI, numexp);
				}
			}
			
			//Inclusión de textos en el documento
			if (bEditarTextos){
				editarTextosDocumento(rulectx, xComponent);
			}
		    
		    //Guarda el documento
			//TODO: Llamar al DocumentosUtil.anexaDocumento
			String fileNameOut = FileTemporaryManager.getInstance().newFileName("." + Constants._EXTENSION_ODT);
			fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
			String mime = Constants._MIMETYPE_ODT;
			OpenOfficeHelper.saveDocument(xComponent,"file://" + fileNameOut,"");
			File fileOut = new File(fileNameOut);
			InputStream in = new FileInputStream(fileOut);
			gendocAPI.setDocument(connectorSession, documentId, docref, in, (int)(fileOut.length()), mime);
			
			//Borra archivos temporales
			if (null != file) {
				file.delete();
			}
			if(null != fileOut){
				fileOut.delete();
			}
			if(null != in){
				in.close();
			}
			
    	} catch (ISPACException e) {
			LOGGER.error("Error al generar el documento. " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			LOGGER.error("Error al generar el documento. " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error al generar el documento. " + e.getMessage(), e);
		} catch (java.lang.Exception e) {
			LOGGER.error("Error al generar el documento. " + e.getMessage(), e);
		} finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
	}	

	/**
	 * Método de inserción de tablas a sobreescribir en los hijos
	 * @param rulectx
	 * @param component
	 * @param refTabla
	 * @param entitiesAPI
	 * @param numexp
	 */
	public void insertaTabla(IRuleContext rulectx, XComponent component,
			String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
		LOGGER.debug("Método insertaTabla de la clase: "+this.getClass().getName());
	}
	
	/**
	 * Método de edición del documento a sobreescribir en los hijos
	 * @param rulectx
	 * @param component
	 * @param refTabla
	 * @param entitiesAPI
	 * @param numexp
	 */
	public void editarTextosDocumento(IRuleContext rulectx, XComponent component) {
		LOGGER.debug("Método insertaTabla de la clase: "+this.getClass().getName());
	}
	
	
	/**
	 * Métodod para setear variables del sistema si deseamos incluir alguna en
	 * los documentos. Hay que sobreescribirlos y añadir las variables que
	 * deseemos, y no olvidar eliminarlas
	 * 
	 * @param cct
	 * @param rulectx
	 */
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
	}

	public void deleteSsVariables(IClientContext cct) {
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
