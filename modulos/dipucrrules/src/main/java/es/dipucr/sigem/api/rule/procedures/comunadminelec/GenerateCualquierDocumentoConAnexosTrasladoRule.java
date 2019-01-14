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
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;

public class GenerateCualquierDocumentoConAnexosTrasladoRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(GenerateCualquierDocumentoConAnexosTrasladoRule.class);

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
			
			
        	documentTypeId = DocumentosUtil.getTipoDoc(cct, "Carta digital", DocumentosUtil.BUSQUEDA_EXACTA, false);
			
			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL = 'TRAS'", "ID");
			
			// 2. Comprobar que hay algún participante para el cual generar su notificación
			if (participantes!=null && participantes.toList().size()>=1) {
				
				File resultado1 = null;
				
				IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), "(EXTENSION='odt' or EXTENSION='doc') AND NOMBRE!='Anexo'", "");
				Iterator iDoc = documentos.iterator();
				String docFilePath= "";
				
				if(iDoc.hasNext()){					
		        	
		        	int templateId = 0;
					IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
		        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
		        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
		        	}else{
		        		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
			        	templateId = tpDocsTemplate.getInt("ID");
		        	}			        	
					
					IItem doc = (IItem)iDoc.next();
					String infoPag = doc.getString("INFOPAG");
					String extension = doc.getString("EXTENSION");
					
		        	//Plantilla de Notificaciones
		        	resultado1 = DocumentosUtil.getFile(cct, infoPag, null, null);			        	
		    		IItem entityDoc1 = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), documentTypeId, templateId, "Prueba Carta Digital Plantilla", resultado1, "pdf");
		    		entityDoc1.store(cct);
		        	
		        	
		        	String docInfoPag = entityDoc1.getString("INFOPAG");
					// Convertir el documento original a PDF
		    		docFilePath= DocumentConverter.convert2PDF(cct.getAPI(), docInfoPag,extension);
		    		// Obtener la información del fichero convertido
		    		file = new File(docFilePath);	
				} else {
					documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), "EXTENSION='pdf' AND NOMBRE!='Anexo'", "FDOC DESC");
					iDoc = documentos.iterator();
					
					if(iDoc.hasNext()){
						String infopag = "";
						IItem doc = (IItem)iDoc.next();
						if(doc.getString("INFOPAG")!=null) {
							infopag = doc.getString("INFOPAG");
						}
						file = DocumentosUtil.getFile(cct, infopag, "", doc.getString("EXTENSION"));
					}
				}
				File resultado = null;
				String rutaFileName = null;
				FileInputStream fisFileAnexo = null;
				PdfWriter writer = null;
	    		if(iDocAnexar.hasNext()){
	    			rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+FileTemporaryManager.getInstance().newFileName()+".pdf";
					resultado = new File(rutaFileName);
					FileOutputStream resultadoFO = new FileOutputStream(resultado.getPath());
					
					fisFileAnexo = new FileInputStream(file);
					reader = new PdfReader((InputStream) fisFileAnexo);
					reader.consolidateNamedDestinations();
					int n = reader.getNumberOfPages();
					
					document = new Document(reader.getPageSizeWithRotation(1));
			
					document.setMargins(0, 0, 0, 0);
					
					writer = PdfWriter.getInstance(document, resultadoFO);				
					writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);

					document.open();
					for(int i = 1; i <= n; i++){
						document.newPage();
						Image imagen = Image.getInstance(writer.getImportedPage(reader, i));
						imagen.scalePercent(100);
						document.add(imagen);
						
					}

					while(iDocAnexar.hasNext()) {
						IItem docAnexar = (IItem) iDocAnexar.next();
						
						String  infoPagAnexar = "";
						String ext = "";
						if(docAnexar.getString("INFOPAG_RDE")!=null){
							infoPagAnexar = docAnexar.getString("INFOPAG_RDE");
							 ext = docAnexar.getString("EXTENSION_RDE");
						} else {
							infoPagAnexar = docAnexar.getString("INFOPAG");
							 ext = docAnexar.getString("EXTENSION");
						}

						String descripcion = docAnexar.getString("DESCRIPCION");
						
						File fileAnexo = DocumentosUtil.getFile(cct, infoPagAnexar, null, null);
										
						nombreDocumento = descripcion + "." + ext;
						
						//[dipucr-Felipe 3#91]
						PdfUtil.anexarDocumento(writer, fileAnexo.getAbsolutePath(), nombreDocumento, normalizar(descripcion));
		
						fileAnexo.delete();
						fileAnexo = null;																	
					}
					
					document.close();								
					resultadoFO.close();								
					reader.close();
	    		}
	    		if(resultado == null){
	    			resultado = file;
	    		}
				
				
				//Guarda el resultado en gestor documental
				int tpdoc = DocumentosUtil.getTipoDoc(cct, "Carta digital", DocumentosUtil.BUSQUEDA_EXACTA, false);
	    		
	    		IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, "Carta digital", resultado, "pdf"); 

	    		String templateDescripcion = entityDoc.getString("DESCRIPCION");
				entityDoc.set("DESCRIPCION", templateDescripcion);		    		
	    		entityDoc.store(cct);
	    		file.delete();
	    		file = null;
	    		resultado.delete();
	    		resultado = null;
	    		if(rutaFileName!=null){
	    			DocumentosUtil.deleteFile(rutaFileName);
	    		}
	    		
	    		
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
		        if(writer != null) {
		        	writer.close();
		        }
		        writer = null;
		        DocumentosUtil.deleteFile(docFilePath);
			}//fin if			
			
		} catch (ISPACException e) {
			LOGGER.error("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			LOGGER.error("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		} catch (DocumentException e) {
			LOGGER.error("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		}
		return true;
	}
	
	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean crearTramite = true;
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			/*********************************************************/
			IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE!='Anexo'", "");
			Iterator<IItem> iDoc = documentos.iterator();
			if(!iDoc.hasNext()){
				crearTramite = false;
				rulectx.setInfoMessage("Tiene que existe un documento que sea distinto al tipo de documento Anexo");
			}
		} catch (ISPACException e) {
			LOGGER.error("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos numexp. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
		}
		return crearTramite;
	}
}
