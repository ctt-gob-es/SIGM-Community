package es.dipucr.sigem.api.rule.procedures.comunadminelec;

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
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GenerateDocUnicoConAnexosTrasladoRule  implements IRule{
	
	private static final Logger logger = Logger.getLogger(GenerateDocUnicoConAnexosRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			/*********************************************************/
			//File con el contenido en pdf
			File file = null;
			
			Object connectorSession = null;
			String nombreDocumento = "";
			
			String nombre = "";
	    	String dirnot = "";
	    	String c_postal = "";
	    	String localidad = "";
	    	String caut = "";
	    	int documentTypeId = 0;
			
			Document document = null;
			// Creamos un reader para el documento
			PdfReader reader = null;
			
			connectorSession = gendocAPI.createConnectorSession();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			
			int taskId = rulectx.getTaskId();
			
			//Compruebo que tenga anexos este expediente y si tiene que se ejecite la regla y si no tiene que
			//no haga nada
			String sqlQuery1 = "NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='Anexo'";
			IItemCollection documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery1, "");
			Iterator iDocAnexar = documentosAnexar.iterator();
			
			String numExp = rulectx.getNumExp();
			
			IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
        	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()){
        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.TaskTpDoc"));
        	}else {
        		//Hay dos tipos de documento asociados al trámite: Decreto y Notificación Decreto
        		//Necesitamos el de Notificación del Decreto
        		Iterator it = taskTpDocCollection.iterator();
        		while (it.hasNext()){
        			IItem taskTpDoc = (IItem)it.next();
        			if (taskTpDoc.get("CT_TPDOC:NOMBRE").equals("Carta digital")){
        				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
        			}
        		}
        	}
			
			if(iDocAnexar.hasNext()){
				// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
				IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL = 'TRAS'", "ID");
				
				// 2. Comprobar que hay algún participante para el cual generar su notificación
				if (participantes!=null && participantes.toList().size()>=1) {
					
					for (int j=0;j<participantes.toList().size();j++){
						
						//Compruebo que tenga anexos este expediente y si tiene que se ejecite la regla y si no tiene que
						//no haga nada
						documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Anexo'", "");
						iDocAnexar = documentosAnexar.iterator();
						
						IItem participante = (IItem) participantes.toList().get(j);
						
						if (participante!=null){
				
							IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), "(EXTENSION='odt' or EXTENSION='doc') AND NOMBRE!='Anexo'", "");
							Iterator iDoc = documentos.iterator();
							
							while(iDoc.hasNext()){
								
								// Añadir a la session los datos para poder utilizar <ispactag sessionvar='var'> en la plantilla
					        	if ((String)participante.get("NOMBRE")!=null){
					        		nombre = (String)participante.get("NOMBRE");
					        	}else{
					        		nombre = "";
					        	}
					        	if ((String)participante.get("DIRNOT")!=null){
					        		dirnot = (String)participante.get("DIRNOT");
					        	}else{
					        		dirnot = "";
					        	}
					        	if ((String)participante.get("C_POSTAL")!=null){
					        		c_postal = (String)participante.get("C_POSTAL");
					        	}else{
					        		c_postal = "";
					        	}
					        	if ((String)participante.get("LOCALIDAD")!=null){
					        		localidad = (String)participante.get("LOCALIDAD");
					        	}else{
					        		localidad = "";
					        	}
					        	if ((String)participante.get("CAUT")!=null){
					        		caut = (String)participante.get("CAUT");
					        	}else{
					        		caut = "";
					        	}
					        	
					        	cct.setSsVariable("NOMBRE", nombre);
					        	cct.setSsVariable("DIRNOT", dirnot);
					        	cct.setSsVariable("C_POSTAL", c_postal);
					        	cct.setSsVariable("LOCALIDAD", localidad);
					        	cct.setSsVariable("CAUT", caut);
								
								IItem doc = (IItem)iDoc.next();
								String infoPag = doc.getString("INFOPAG");
								String extension = doc.getString("EXTENSION");
								
								int templateId = 0;
								
								IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
					        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
					        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
					        	}else{
					        		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
						        	templateId = tpDocsTemplate.getInt("ID");
					        	}
					        	
					        	//Plantilla de Notificaciones
								int idPlantilla = DocumentosUtil.getIdDocumentByNombre(rulectx.getNumExp(), rulectx, "Plantilla Carta digital");
					        	File resultado1 = DocumentosUtil.getFile(cct, infoPag, null, null);
					        	
					    		IItem entityDoc1 = DocumentosUtil.generaYAnexaDocumento(rulectx, documentTypeId, "Carta digital", resultado1, Constants._EXTENSION_ODT);
					    		int documentId = entityDoc1.getKeyInt();
					    		IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, resultado1.getName());
					    		entityTemplate.set("EXTENSION", Constants._EXTENSION_ODT);
					    		entityTemplate.set("DESCRIPCION", "Prueba Carta Digital Plantilla");
					    		entityTemplate.set("DESTINO", nombre);
					    		entityTemplate.store(cct);
					        	
					        	
					        	String docInfoPag = entityTemplate.getString("INFOPAG");
								// Convertir el documento original a PDF
								file = convert2PDF(cct, docInfoPag, extension);
				
								String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+FileTemporaryManager.getInstance().newFileName()+".pdf";
								File resultado = new File(rutaFileName);
								FileOutputStream resultadoFO = new FileOutputStream(resultado.getPath());
								
								FileInputStream fisFileAnexo = new FileInputStream(file);
								reader = new PdfReader((InputStream) fisFileAnexo);
								reader.consolidateNamedDestinations();
								int n = reader.getNumberOfPages();
								
								document = new Document(reader.getPageSizeWithRotation(1));
								Rectangle rectangulo = document.getPageSize();
						
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
									
									String  infoPagAnexar = docAnexar.getString("INFOPAG");
									String ext = docAnexar.getString("EXTENSION");
									String descripcion = docAnexar.getString("DESCRIPCION");
									
									File fileAnexo = DocumentosUtil.getFile(cct, infoPagAnexar, null, null);

									nombreDocumento = descripcion + "." + ext;
									
									añadeDocumento(writer, fileAnexo.getAbsolutePath(), nombreDocumento, normalizar(descripcion));
					
									fileAnexo.delete();
								}
								
								document.close();
								
								resultadoFO.close();
								
								reader.close();
								
								//Guarda el resultado en gestor documental
						        int tpdoc = DocumentosUtil.getTipoDoc(cct, "Carta digital", DocumentosUtil.BUSQUEDA_EXACTA, false);

					    		IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, "Carta digital", resultado, Constants._EXTENSION_PDF);

					    		String templateDescripcion = entityDoc.getString("DESCRIPCION");
								templateDescripcion = templateDescripcion + " - " + cct.getSsVariable("NOMBRE");
								
								entityDoc.set("DESCRIPCION", templateDescripcion);
								entityDoc.set("DESTINO", nombre);					    		
					    		// Si todo ha sido correcto borrar las variables de la session
								cct.deleteSsVariable("NOMBRE");
								cct.deleteSsVariable("DIRNOT");
								cct.deleteSsVariable("C_POSTAL");
								cct.deleteSsVariable("LOCALIDAD");
								cct.deleteSsVariable("CAUT");
					    		
					    		entityDoc.store(cct);
					    		file.delete();	
					    		resultado.delete();
					    		
					    		//Borra los documentos intermedios del gestor documental
						        IItemCollection collectionBorrar = entitiesAPI.getDocuments(rulectx.getNumExp(), "AND DESCRIPCION = 'Prueba Carta Digital Plantilla'", "");
						        Iterator itBorrar = collectionBorrar.iterator();
						        while (itBorrar.hasNext())
						        {
						        	IItem docBorrar = (IItem)itBorrar.next();
						        	entitiesAPI.deleteDocument(docBorrar);
						        }
						        
						        if(resultado1 != null && resultado1.exists())resultado1.delete();
						        resultado1.delete();
							}
						}//fin if parti
					}//fin for
				}//fin if
			}			
			
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);	
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
		}
		return new Boolean (true);
	}

	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}
	
	private void añadeDocumento(PdfWriter writer, String rutaOriginal, String nombreDocumento, String descripcionAdjunto) {

		try{
			
			PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer, rutaOriginal, nombreDocumento, null);
			if (pfs != null)
				writer.addFileAttachment(descripcionAdjunto, pfs);
			
		}
		catch(IOException e){
			logger.error(e.getMessage(), e);
		}
		
	}

	/**
	 * Convierte le fichero a pdf
	 * @param cct 
	 * @param infoPag 
	 * @param extension
	 * @return
	 * @throws ISPACException
	 */
	
	private File convert2PDF(IClientContext cct, String infoPag, String extension) throws ISPACException {
		// Convertir el documento a pdf
		String docFilePath= DocumentConverter.convert2PDF(cct.getAPI(), infoPag,extension);
		
		// Obtener la información del fichero convertido
		File file = new File( docFilePath);
		if (!file.exists())
			throw new ISPACException("No se ha podido convertir el documento a PDF");
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new ISPACException("Fichero no encontrado: '" + file.getName() + "'");
		}
		finally{
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
		}
		return file;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
