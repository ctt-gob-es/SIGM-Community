package es.dipucr.sigem.imprimir.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.BaseDispatchAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;
import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EdicionDocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ImprimirAllDocumentAction extends BaseDispatchAction {
	
	private static final Logger logger = Logger.getLogger(ImprimirAllDocumentAction.class);
	
	File fileConcatenadoPdf = null;
	String pathFileTempConcatenadaPdf = "";
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward selectOption(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response, 
			SessionAPI session) throws Exception {
		
		/******************************************************************/
		   ClientContext cct = session.getClientContext();
			
	   	   IInvesflowAPI invesFlowAPI = session.getAPI();
	   	   IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	   	   IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	   	   
	   	   OpenOfficeHelper ooHelper = OpenOfficeHelper.getInstance();
		 /******************************************************************/
	   	   
	   	File file = null ;
	   	File fileConcatenado = null;
	   	  
	   	String numexp = "";
	   	int tramiteId = 0;
	    
	   	ArrayList inputStreamNotificaciones = new ArrayList();
		ArrayList filePathNotificaciones = new ArrayList();
		ArrayList arrayInfopags = new ArrayList();
		try{
			logger.info("Inicio ImprimirAllDocumentAction Ticket #226");
			String STR_plantillaBlanco = "blanco";
			
			String stateTicket = getStateticket(request);
			//logger.warn("stateTicket "+stateTicket);
			//El string que devuelve este metodo tiene esta forma
			//113|226|9499|230|921|15611|6393|DPCR2010%2F6309|6|7|15617|true|false|0|0|0|0|0|0
			//en la posicion 11 es donde esta el key ahí íria el id del documento asi que lo
			//voy a hacer es sacar esta posicion y meter a mano el id del documento
			String [] aStateTicket = stateTicket.split("\\|");
			String tramite = "";
			if(aStateTicket!=null && aStateTicket.length>11){
				tramite = aStateTicket[5];
			}

			tramiteId = Integer.parseInt(tramite); 
			
			String strQuery = "WHERE ID_TRAMITE="+tramiteId+" ORDER BY DESCRIPCION;";
//			String strQuery = "WHERE ID_TRAMITE="+tramiteId+" ORDER BY ID;";
			
			IItemCollection collAllDoc = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_DOCUMENTOS, strQuery);
	        Iterator itcollAllDoc = collAllDoc.iterator();
	        if(itcollAllDoc.hasNext()){
	        	
	        	//Compruebo un documento para ver cuales son las extensiones q tienen y asi crear un
	        	//documento pdf o creo un documento odt
	        	IItem document = (IItem)itcollAllDoc.next();
	        	
	        	numexp = document.getString("NUMEXP");
		  		
		  		String infoPag = document.getString("INFOPAG");
	  			String infoPagRDE = document.getString("INFOPAG_RDE");
	  			
	  			// Inicia el contexto de ejecución para que se ejecuten
	            // las reglas asociadas a la entidad
				IProcess exp = invesFlowAPI.getProcess(numexp);
	            StateContext stateContext = cct.getStateContext();
	            if (stateContext == null) {
	                stateContext = new StateContext();
	                ((ClientContext)cct).setStateContext(stateContext);
	            }
	            stateContext.setPcdId(exp.getInt("ID_PCD"));
	            stateContext.setProcessId(exp.getKeyInt());
	            stateContext.setNumexp(numexp);
	  			
	  			XComponent xComponent = null;
	  			
	  			String extension = "";
	  			if(StringUtils.isNotBlank(infoPagRDE)){
	  				extension = document.getString("EXTENSION_RDE");
	  				file = DocumentosUtil.getFile(cct,infoPagRDE, null,extension);	  				
	  			} else {
	  				 extension = document.getString("EXTENSION");
	  			}
	  			
	  			if(extension.equals(Constants._EXTENSION_ODT) || extension.equals(Constants._EXTENSION_DOC)){
	  				 //Documento en blanco creo una plantilla llamada blanco
	  				logger.warn("cct "+cct);
	  				logger.warn("genDocAPI "+genDocAPI);
	  				logger.warn("entitiesAPI "+entitiesAPI);
	  				logger.warn("STR_plantillaBlanco "+STR_plantillaBlanco);
	  				logger.warn("tramiteId "+tramiteId);
	  		    	String strInfoPag = generarDocumento(cct, genDocAPI, entitiesAPI, STR_plantillaBlanco, tramiteId);
	  				//String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
					//fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
					//file = new File(fileName);
	  		    	file = DocumentosUtil.getFile(cct, strInfoPag, null, extension);
					logger.warn("-- "+file.getPath());
	  				xComponent = ooHelper.loadDocument("file://" + file.getPath());
	  				file.delete();
	  				
	  				//Añado el primer documento
	  				infoPag = document.getString("INFOPAG");
	  				file = DocumentosUtil.getFile(cct,infoPag, null, extension);
	  				DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), Constants._EXTENSION_DOC);
		  			EdicionDocumentosUtil.insertaSaltoDePagina(xComponent);
		    		file.delete();
	  			}
	  			else{
	  				String pathFileTemp = FileTemporaryManager.getInstance().put(file.getAbsolutePath(), ".pdf");
	  				String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
//	  				fileImpresion = new File(rutaFileName);
//	  				fis = new FileInputStream(fileImpresion);
//	  				inputStreamNotificaciones.add(fis);
	  				if(StringUtils.isNotBlank(infoPagRDE))
	  					arrayInfopags.add(infoPagRDE);
	  				else
	  					arrayInfopags.add(infoPag);
		  			filePathNotificaciones.add(rutaFileName);
	  			}
		        
		        while(itcollAllDoc.hasNext()){
		        	
		        	document = (IItem)itcollAllDoc.next();
			  		
			  		infoPag = document.getString("INFOPAG");
		  			infoPagRDE = document.getString("INFOPAG_RDE");
		  			
		  			extension = "";
		  			if(StringUtils.isNotBlank(infoPagRDE)){
		  				extension = document.getString("EXTENSION_RDE");
		  				file = DocumentosUtil.getFile(cct,infoPagRDE, null, extension);		  				
		  			} else {
		  				extension = document.getString("EXTENSION");
		  				 file = DocumentosUtil.getFile(cct,infoPag, null, extension);		  				 
		  			}
		  			if(extension.equals(Constants._EXTENSION_ODT) || extension.equals(Constants._EXTENSION_DOC)){
		  				DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), Constants._EXTENSION_DOC);
			  			EdicionDocumentosUtil.insertaSaltoDePagina(xComponent);
			    		file.delete();
		  			}
		  			else{
		  				String pathFileTemp = FileTemporaryManager.getInstance().put(file.getAbsolutePath(), ".pdf");
		  				String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
//		  				fileImpresion = new File(rutaFileName);
//		  				fis = new FileInputStream(fileImpresion);
//		  				inputStreamNotificaciones.add(fis);		  				
		  				if(StringUtils.isNotBlank(infoPagRDE))
		  					arrayInfopags.add(infoPagRDE);
		  				else
		  					arrayInfopags.add(infoPag);
			  			filePathNotificaciones.add(rutaFileName);
		  			}
		  			
		  			
		        }
		        if(extension.equals(Constants._EXTENSION_ODT) || extension.equals(Constants._EXTENSION_DOC)){
		        	String fileName = FileTemporaryManager.getInstance().newFileName(".odt");
					fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
					fileConcatenadoPdf = new File(fileName);
					
					pathFileTempConcatenadaPdf = fileConcatenadoPdf.getPath();
					
					OpenOfficeHelper.saveDocument(xComponent,"file://" + fileConcatenadoPdf.getPath(),"");
					
					//Guarda el resultado en gestor documental
					String STR_nombreTramite = getNombreTramite(entitiesAPI, tramiteId);
					logger.warn("STR_nombreTramite "+STR_nombreTramite);
					int tpdoc = DocumentosUtil.getTipoDoc(cct, "blanco", DocumentosUtil.BUSQUEDA_EXACTA, false);

					IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(cct, tramiteId, tpdoc, Constants.PLANTILLADOC.IMPRIMIR, fileConcatenadoPdf, Constants._EXTENSION_ODT);

					entityDoc.set("NOMBRE", Constants.PLANTILLADOC.IMPRIMIR);
					entityDoc.store(cct);
					//file.delete();
					
					//Borra los documentos intermedios del gestor documental
					strQuery = "WHERE ID_TRAMITE="+tramiteId+" AND DESCRIPCION = 'Concatena'";
			        IItemCollection collection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_DOCUMENTOS, strQuery);
			        Iterator it = collection.iterator();
			        while (it.hasNext())
			        {
			        	IItem doc = (IItem)it.next();
			        	entitiesAPI.deleteDocument(doc);
			        }
					
					logger.warn("Fin ImprimirAllDocumentAction Ticket #226");
					return mapping.findForward("success");
		        }
		        //pdf
		        else{
		        	logger.warn("Creacion del pdf");
		        	int idTipDoc = 0;
		        	int idPlantilla = 0;
		        	//Recuperamos el tipo de documento y el id de la plantilla
					//Metemos a capón el nombre del tipo de documento y la plantilla, habría que crear un parámetro o una entrada en la configuración para indicarlo.
					idTipDoc = DocumentosUtil.getTipoDoc(cct, "Documentos Sellados", DocumentosUtil.BUSQUEDA_EXACTA, false);

			        String consulta="SELECT ID FROM SPAC_P_PLANTDOC WHERE NOMBRE='Documentos Sellados' AND ID_TPDOC="+idTipDoc+";";
			        ResultSet planDocIterator = cct.getConnection().executeQuery(consulta).getResultSet();	      
			        if(planDocIterator.next()) idPlantilla = planDocIterator.getInt("ID");
		        	
		        	fileConcatenado = concatenaPdf(arrayInfopags, filePathNotificaciones, cct);
		        	logger.warn("concatenado pdf");
		 			IItem entityDocument = DocumentosUtil.generaYAnexaDocumento(cct, Integer.parseInt(tramite), idTipDoc, "Impresión de Documentos", fileConcatenado, Constants._EXTENSION_PDF);
			        entityDocument.set("ID_PLANTILLA", idPlantilla);
			        entityDocument.set("NOMBRE", "Impresión de Documentos");
					entityDocument.store(cct);
			        cct.endTX(true);

					logger.warn("FIN Creacion del pdf");
		 			return mapping.findForward("success");
		        }
	        }
	        else{
				return mapping.findForward("failure");
	        }
			} catch (ISPACInfo e) {
				logger.error("Error al imprimir todos los documentos del expediente: " + numexp + " y trámite: " + tramiteId + ". " + e.getMessage(), e);
				e.setRefresh(false);
				return mapping.findForward("failure");
				//throw e;
			} catch (Exception e) {
				logger.error("Error al imprimir todos los documentos del expediente: " + numexp + " y trámite: " + tramiteId + ". " + e.getMessage(), e);
				//throw new ISPACInfo(e,false);
				return mapping.findForward("failure");
			} finally {				
				for (int i=0; i<inputStreamNotificaciones.size(); i++)
					((FileInputStream)inputStreamNotificaciones.get(i)).close();
				ooHelper.dispose();
				System.gc();			
			}
	}
	

	@SuppressWarnings("rawtypes")
	private String getNombreTramite(IEntitiesAPI entitiesAPI, int tramiteId) throws ISPACException {
		String nombreTramite = "";
		
		IItem iTask = entitiesAPI.getTask(tramiteId);
		int idTramite = iTask.getInt("ID_TRAM_PCD");
		//tabla SPAC_P_TRAMITES
		String strQuery = "WHERE ID_TRAMITE_BPM='"+idTramite+"'";
		logger.debug("strQuery: "+strQuery);
        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
        Iterator it = collection.iterator();
        if (it.hasNext()){
        	nombreTramite = ((IItem)it.next()).getString("NOMBRE");
        	
        }else{
        	throw new ISPACInfo("No se ha encontrado la fase actual del expediente.");
        }
		return nombreTramite;
	}

	public ActionForward muestraDoc(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response, 
			SessionAPI session) throws Exception {
		
		if(fileConcatenadoPdf != null && !pathFileTempConcatenadaPdf.equals("")){
			response.setContentType(MimetypeMapping.getMimeType("odt"));
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");
		    response.setHeader("Content-Transfer-Encoding", "binary");
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + pathFileTempConcatenadaPdf + "\"");
			response.setContentLength((int) fileConcatenadoPdf.length());
			
			ServletOutputStream ouputStream = response.getOutputStream();
			FileUtils.copy(fileConcatenadoPdf, ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}

		return null;
	}
	
	private String generarDocumento (ClientContext cct, IGenDocAPI genDocAPI, IEntitiesAPI entitiesAPI, String plantillaBlanco, int tramiteId) throws ISPACException, SQLException{
		
		String consulta="SELECT ID FROM "+Constants.TABLASBBDD.SPAC_P_PLANTDOC+" WHERE NOMBRE LIKE '%"+plantillaBlanco+"%'";
        ResultSet planDocIterator = cct.getConnection().executeQuery(consulta).getResultSet();
        String infopath = "";
        if(planDocIterator==null)
    	{
    		throw new ISPACInfo("No hay ninguna plantilla asociada al tipo de documento");
    	}
    	int templateId = 0;
    	if(planDocIterator.next()) templateId = planDocIterator.getInt("ID");
    	
    	logger.warn("templateId "+templateId);
    	
    	Object connectorSession = null;
		try
		{
			connectorSession = genDocAPI.createConnectorSession();
			// Abrir transacción para que no se pueda generar un documento sin fichero
	        cct.beginTX();
			
			int tpdoc = DocumentosUtil.getTipoDoc(cct, plantillaBlanco, DocumentosUtil.BUSQUEDA_EXACTA, false);
			
			logger.warn("tpdoc "+tpdoc);

        	IItem entityDocument = genDocAPI.createTaskDocument(tramiteId, tpdoc);
			int documentId = entityDocument.getKeyInt();
			
			logger.warn("documentId "+documentId);

			// Generar el documento a partir de la plantilla
			IItem entityTemplate = genDocAPI.attachTaskTemplate(connectorSession, tramiteId, documentId, templateId);
			entityTemplate.set("EXTENSION", Constants._EXTENSION_ODT);
			
			entityTemplate.set("DESCRIPCION", "Concatena");
			entityTemplate.set("NOMBRE", "Concatena");
			infopath = entityTemplate.getString("INFOPAG");
			entityTemplate.store(cct);
			entityTemplate.store(cct);
		}
		catch (Throwable e)
		{
			// Si se produce algún error se hace rollback de la transacción
			cct.endTX(false);
			
			String message = "exception.documents.generate";
			String extraInfo = null;
			Throwable eCause = e.getCause();
			
			if (eCause instanceof ISPACException)
			{
				if (eCause.getCause() instanceof NoConnectException) 
				{
					extraInfo = "exception.extrainfo.documents.openoffice.off"; 
				}
				else
				{
					extraInfo = eCause.getCause().getMessage();
				}
			}
			else if (eCause instanceof DisposedException)
			{
				extraInfo = "exception.extrainfo.documents.openoffice.stop";
			}
			else
			{
				extraInfo = e.getMessage();
			}
			logger.error("Error al imprimir documento" + e.getMessage(), e);
			throw new ISPACInfo(message, extraInfo);
			
			
		}
		finally
		{
			if (connectorSession != null)
			{
				genDocAPI.closeConnectorSession(connectorSession);
			}
		}
    	
    	// Si todo ha sido correcto se hace commit de la transacción
		cct.endTX(true);
		return infopath;

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private File concatenaPdf(ArrayList inputStreamNotificaciones, ArrayList filePathNotificaciones, IClientContext cct) {
	    
		File resultado = null;
		
		try {
			// Creamos un reader para el documento
	    	PdfReader reader = null;
	    	    	
			Document document = null;
	        PdfCopy  writer = null;
	        boolean primero = true;//Indica si es el primer fichero (sobre el que se concatenarán el resto de ficheros)
	        InputStream f = null;
	
	        int pageOffset = 0;
	        ArrayList master = new ArrayList();
	        
	        Iterator inputStreamNotificacionesIterator = inputStreamNotificaciones.iterator();
	        while (inputStreamNotificacionesIterator.hasNext()){
	        	FileInputStream file = new FileInputStream(DocumentosUtil.getFile(cct,(String)inputStreamNotificacionesIterator.next(), null,"pdf"));
			    reader = new PdfReader((InputStream)file);
	        	
		    	reader.consolidateNamedDestinations();
		        int n = reader.getNumberOfPages();
		        List bookmarks = SimpleBookmark.getBookmark(reader);
		        
		        if (bookmarks != null) {
		            if (pageOffset != 0)
		                SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
		            master.addAll(bookmarks);
		        }
		        
		        pageOffset += n;
		        
		        if (primero) {
		            // Creamos un objeto Document
		            document = new Document(reader.getPageSizeWithRotation(1));
		            // Creamos un writer que escuche al documento		            
		            resultado = new File((String)filePathNotificaciones.get(0));
		            
		            writer = new PdfCopy(document, new FileOutputStream(resultado));		            
		            
		            writer.setViewerPreferences(PdfCopy.PageModeUseOutlines);
		            // Abrimos el documento
		            document.open();
		            primero = false;
		        }
		        
		        // Añadimos el contenido
		        PdfImportedPage page;
		        
		        document.newPage();
		        
		        for (int i = 0; i < n;) {
		        	i++;
		            page = writer.getImportedPage(reader, i);
		            writer.addPage(page);		            
		        }
		        
		        //MQE Ticket #180 para poder imprimir en dos caras 
		        if(n%2==1){
		        	document.newPage();
		        	//[Ticket#153#Teresa] ALSIGM3 Quitar el fichero blanco.pdf del código para que no aparezca en '/tmpcenpri/SIGEM/temp/temporary'
		        	//String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/blanco.pdf";
		        	File filePdf = FileTemporaryManager.getInstance().newFile("." + "pdf");
		        	f = new FileInputStream(filePdf.getAbsolutePath());
		        	reader = new PdfReader(f);
		        	page = writer.getImportedPage(reader, 1);
		        	writer.addPage(page);
		        }
		        //MQE Fin modificaciones Ticket #180 para poder imprimir en dos caras
		        reader.close();
		        if(f!=null){
		        	f.close();
		        	f = null;
		        }
		        file.close();
	        }//while
	        if (!master.isEmpty())
	            writer.setOutlines(master);
	        document.close();
	        writer.close();
	    }
	    catch(Exception e) {
	    	logger.error("Error al concatenar los pdfs" + e.getMessage(), e);
	    } 
	    return resultado;
	}  

}
