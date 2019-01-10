package es.dipucr.sigem.api.rule.procedures.comunadminelec;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.tools.ant.types.FlexInteger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FileUtils;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;

public class GenerateDocUnicoConAnexosRule implements IRule{
	
	private static final String ERROR_GENERAR_DOCUMENTOS = "Error al generar los documentos. ";
	private static final String CARTA_DIGITAL = "Carta digital";
	
	private static final Logger LOGGER = Logger.getLogger(GenerateDocUnicoConAnexosRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// Empty method
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			/*********************************************************/
			//File con el contenido en pdf
			File file = null;
			
			String nombreDocumento = "";
	    	int documentTypeId = 0;
			
			Document document = null;
			// Creamos un reader para el documento
			PdfReader reader = null;
			
			//Compruebo que tenga anexos este expediente y si tiene que se ejecute la regla y si no tiene que
			//no haga nada
			String sqlQuery1 = "NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='Anexo'";
			IItemCollection documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery1, "");
			Iterator<IItem> iDocAnexar = documentosAnexar.iterator();
			
			String numExp = rulectx.getNumExp();
			
        	documentTypeId = DocumentosUtil.getTipoDoc(cct, CARTA_DIGITAL, DocumentosUtil.BUSQUEDA_EXACTA, false);
			
			if(iDocAnexar.hasNext()){
				// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
				IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL != 'TRAS'", "ID");
				
				// 2. Comprobar que hay algún participante para el cual generar su notificación
				if (participantes!=null && participantes.toList().size()>=1) {
					
					for (int j=0;j<participantes.toList().size();j++){
						
						//Compruebo que tenga anexos este expediente y si tiene que se ejecite la regla y si no tiene que
						//no haga nada
						documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Anexo'", "");
						iDocAnexar = documentosAnexar.iterator();
						
						IItem participante = (IItem) participantes.toList().get(j);
						
						if (participante!=null){
							IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), "(UPPER(EXTENSION)='ODT' or UPPER(EXTENSION)='DOC')  AND ESTADOFIRMA = '"+SignStatesConstants.SIN_FIRMA+"' AND NOMBRE!='Anexo'", "");
							Iterator iDoc = documentos.iterator();
							
							while(iDoc.hasNext()){
								
								// Añadir a la session los datos para poder utilizar <ispactag sessionvar='var'> en la plantilla
								DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
								
					        	int templateId = 0;
								IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
					        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
					        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
					        	}else{
					        		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
						        	templateId = tpDocsTemplate.getInt("ID");
					        	}
					        	
					        	IItem doc = (IItem)iDoc.next();
								String infoPag = doc.getString(DocumentosUtil.INFOPAG);
								String extension = doc.getString(DocumentosUtil.EXTENSION);
								
					        	//Plantilla de Notificaciones
					        	File resultado1 = DocumentosUtil.getFile(cct, infoPag, null, null);
					        	
					    		IItem entityDoc1 = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), documentTypeId, templateId, "Prueba Carta Digital Plantilla", resultado1, "pdf");
					    		entityDoc1.set(DocumentosUtil.DESTINO, participante.getString(ParticipantesUtil.NOMBRE));
					    		entityDoc1.store(cct);
					        	
					        	
					        	String docInfoPag = entityDoc1.getString(DocumentosUtil.INFOPAG);
								// Convertir el documento original a PDF
					    		String docFilePath= DocumentConverter.convert2PDF(cct.getAPI(), docInfoPag, extension);
					    		
					    		// Obtener la información del fichero convertido
					    		file = new File(docFilePath);
					    		if (!file.exists()){
					    			throw new ISPACException("No se ha podido convertir el documento a PDF");
					    		}
				
								String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+FileTemporaryManager.getInstance().newFileName()+".pdf";
								File resultado = new File(rutaFileName);
								FileOutputStream resultadoFO = new FileOutputStream(resultado.getPath());
								
								FileInputStream fisFileAnexo = new FileInputStream(file);
								reader = new PdfReader((InputStream) fisFileAnexo);
								reader.consolidateNamedDestinations();
								int n = reader.getNumberOfPages();
								
								document = new Document(reader.getPageSizeWithRotation(1));
						
								document.setMargins(0, 0, 0, 0);
								
								PdfWriter writer = PdfWriter.getInstance(document, resultadoFO);				
								writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
				
								document.open();
								for(int i = 1; i <= n; i++){
									document.newPage();
									Image imagen = Image.getInstance(writer.getImportedPage(reader, i));
									imagen.scalePercent(100);
									document.add(imagen);
									
								}

								while(iDocAnexar.hasNext()){
									IItem docAnexar = (IItem) iDocAnexar.next();
									
									String  infoPagAnexar = "";
									String ext = "";
									if(docAnexar.getString(DocumentosUtil.INFOPAG_RDE)!=null){
										infoPagAnexar = docAnexar.getString(DocumentosUtil.INFOPAG_RDE);
										 ext = docAnexar.getString(DocumentosUtil.EXTENSION_RDE);
									} else {
										infoPagAnexar = docAnexar.getString(DocumentosUtil.INFOPAG);
										 ext = docAnexar.getString(DocumentosUtil.EXTENSION);
									}

									String descripcion = docAnexar.getString(DocumentosUtil.DESCRIPCION);
									
									File fileAnexo = DocumentosUtil.getFile(cct, infoPagAnexar, null, null);
									
									//INICIO [dipucr-Felipe #782]
									nombreDocumento = descripcion;
									if (!nombreDocumento.endsWith("." + ext)){
										nombreDocumento = descripcion + "." + ext;
									}
									//FIN [dipucr-Felipe #782]
									
									//[dipucr-Felipe 3#91]
									PdfUtil.anexarDocumento(writer, fileAnexo.getAbsolutePath(), nombreDocumento, FileUtils.normalizarNombre(descripcion));
					
									fileAnexo.delete();
									fileAnexo = null;																	
								}
								
								document.close();								
								resultadoFO.close();								
								reader.close();
								
								//Guarda el resultado en gestor documental
								int tpdoc = DocumentosUtil.getTipoDoc(cct, CARTA_DIGITAL, DocumentosUtil.BUSQUEDA_EXACTA, false);
					    		
					    		IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, CARTA_DIGITAL, resultado, "pdf"); 

					    		String templateDescripcion = entityDoc.getString(DocumentosUtil.DESCRIPCION);
								templateDescripcion = templateDescripcion + " - " + cct.getSsVariable(DocumentosUtil.NOMBRE);
								entityDoc.set(DocumentosUtil.DESCRIPCION, templateDescripcion);
								entityDoc.set(DocumentosUtil.DESTINO, participante.getString(ParticipantesUtil.NOMBRE));
								entityDoc.set("DESTINO_ID", participante.getString(ParticipantesUtil.ID_EXT));
					    		
								// Si todo ha sido correcto borrar las variables de la session
								DocumentosUtil.borraParticipanteSsVariable(cct);
					    		
					    		entityDoc.store(cct);
					    		file.delete();
					    		file = null;
					    		resultado.delete();
					    		resultado = null;
					    		DocumentosUtil.deleteFile(rutaFileName);
					    		
					    		//Borra los documentos intermedios del gestor documental
						        IItemCollection collectionBorrar = entitiesAPI.getDocuments(rulectx.getNumExp(), "DESCRIPCION = 'Prueba Carta Digital Plantilla'", "");
						        Iterator itBorrar = collectionBorrar.iterator();
						        while (itBorrar.hasNext()) {
						        	IItem docBorrar = (IItem)itBorrar.next();
						        	entitiesAPI.deleteDocument(docBorrar);
						        }
						        
						        if(resultado1 != null && resultado1.exists()) {
						        	resultado1.delete();
						        }
						        resultado1 = null;
						        
						        if(fisFileAnexo != null) {
						        	fisFileAnexo.close();
						        }
						        fisFileAnexo = null;
						        
						        writer.close();
						        writer = null;
						        DocumentosUtil.deleteFile(docFilePath);
							}
						}//fin if parti
					}//fin for
				}//fin if
			}
			
			
		} catch (ISPACException e) {
			LOGGER.error(ERROR_GENERAR_DOCUMENTOS+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException(ERROR_GENERAR_DOCUMENTOS+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			LOGGER.error(ERROR_GENERAR_DOCUMENTOS+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException(ERROR_GENERAR_DOCUMENTOS+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(ERROR_GENERAR_DOCUMENTOS+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException(ERROR_GENERAR_DOCUMENTOS+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		} catch (DocumentException e) {
			LOGGER.error(ERROR_GENERAR_DOCUMENTOS+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException(ERROR_GENERAR_DOCUMENTOS+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		}
		return true;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
