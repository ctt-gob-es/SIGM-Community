package es.dipucr.sigem.api.rule.comparece;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

import es.dipucr.notificador.model.NotificacionWS;
import es.dipucr.sigem.api.rule.common.comparece.CompareceConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.sgm.tram.sign.DipucrSignConnector;
import es.jccm.notificador.ws.AplicacionesServiceProxy;

public class RecibirAcusesCompareceRule implements IRule {
	
	public static final Logger logger = Logger.getLogger(RecibirAcusesCompareceRule.class);
	
	/**
	 * Ruta por defecto de la imagen del logo dipu
	 * /home/sigem/SIGEM/conf/SIGEM_Tramitacion
	 */
	//private static final String IMAGE_PATH_DIPUCR = "firma/logoCabecera.png";
	
	/**
	 * Ruta por defecto de la imagen de fondo del PDF.
	 */
	//private static final String DEFAULT_PDF_BG_IMAGE_PATH = "firma/fondo.gif";
	
	/**
	 * Ruta por defecto del fichero con el texto de la banda lateral del PDF.
	 * /home/sigem/SIGEM/conf/SIGEM_Tramitacion
	 */
	private static final String DEFAULT_PDF_BG_DATA_PATH = "firma/datosFirmaRegistro.txt";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		boolean ejecutar = false;
		ClientContext cct = (ClientContext) rulectx.getClientContext();
		ArrayList<FileInputStream> inputStreamNotificaciones = new ArrayList<FileInputStream>();
		ArrayList<String> filePathNotificaciones = new ArrayList<String>();
		IEntitiesAPI entitiesAPI = null;
		Document documentComparece = new Document();
		File file;

		
		try {
			//----------------------------------------------------------------------------------------------
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
	        //----------------------------------------------------------------------------------------------
			
			File fileConcatenado;
			
			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			
			int idTipDoc = DocumentosUtil.getTipoDoc(cct, "Documentos Sellados", DocumentosUtil.BUSQUEDA_EXACTA, false);
	        
	        int idPlantilla = 0;
	        
	        String consulta="SELECT ID FROM SPAC_P_PLANTDOC WHERE NOMBRE='Documentos Sellados' AND ID_TPDOC="+idTipDoc+";";
	        ResultSet planDocIterator = cct.getConnection().executeQuery(consulta).getResultSet();	      
	        if(planDocIterator.next()) idPlantilla = planDocIterator.getInt("ID");
					
			
	        /*Documento para saber la gente que no ha podido leer la notificacion porque se le ha caducado*/
			
	        //File fileCompareceNombre = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/documentoComparece.pdf");
			File fileCompareceNombre = new File(FileTemporaryManager.getInstance().getFileTemporaryPath()  + File.separator + FileTemporaryManager.getInstance().newFileName(".pdf"));
          	
          	PdfCopy.getInstance(documentComparece, new FileOutputStream(fileCompareceNombre));
        	documentComparece.open();
        	
        	String imageLogoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath(new StringBuilder("skinEntidad_").append(entidad).append(File.separator).toString(), "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty("imagen_cabecera");
			
        	//String imagePath = ConfigurationHelper.getConfigFilePath("config_"+entidad+"/"+IMAGE_PATH_DIPUCR);
			
			File logoURL = new File(imageLogoPath);	
			if (logoURL != null){
				Image logo  = Image.getInstance(imageLogoPath);	
				documentComparece.add(logo);
			}
			
			String imagePiePath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_PIE_PATH_DIPUCR);
			File pieURL = new File(imagePiePath);
			if(pieURL != null){
				Image pie = Image.getInstance(imagePiePath);
				pie.setAbsolutePosition(documentComparece.getPageSize().getWidth() - 550, 15);
				pie.scalePercent(80);
				documentComparece.add(pie);
				
			}
			
			documentComparece.add(new Phrase("\n\n"));
			
        	documentComparece.add(new Phrase("PARTICIPANTES QUE NO HAN LEIDO LA NOTIFICACIÓN EN EL COMPARECE"));
        	documentComparece.add(new Phrase("\n\n"));
        	
        	boolean tieneParticipantesComparece = false;
        	
        	//Creacion del documento acuse
        	//Recuperamos el tipo de documento y el id de la plantilla
			//Metemos a capón el nombre del tipo de documento y la plantilla, habría que crear un parámetro o una entrada en la configuración para indicarlo.
			idTipDoc = DocumentosUtil.getTipoDoc(cct, Constants.COMPARECE.ACUSE_COMPARECE, DocumentosUtil.BUSQUEDA_EXACTA, false);

			//Recuperamos el id del tipo de documento y de la plantilla, lo buscamos por nombre, 
			//si encontramos varios nos quedamos con el primero, ya que nos da igual uno que otro

	        String strQueryNombre="SELECT ID FROM SPAC_P_PLANTDOC WHERE NOMBRE='"+Constants.COMPARECE.ACUSE_COMPARECE+"' AND ID_TPDOC="+idTipDoc+";";
	        ResultSet planDocIterator1 = cct.getConnection().executeQuery(strQueryNombre).getResultSet();
	        int idPlantillaCompar1 = 0;
	        if(planDocIterator1.next()) idPlantillaCompar1 = planDocIterator1.getInt("ID");
	      		     
	        String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND TRAMITE="+rulectx.getTaskId()+"";
	        IItemCollection collection = entitiesAPI.queryEntities(Constants.TABLASBBDD.DPCR_ACUSES_COMPARECE, strQuery);
	        Iterator<IItem> it = collection.iterator();
	        
	        String sIdent_doc = "";
	        Vector<String> vIdDoc = new Vector<String>();
	        while (it.hasNext()){
	        	
	        	IItem iAcuse = (IItem)it.next();
	        	
	        	if (iAcuse.getString("IDENT_DOC")!=null) 
	        		sIdent_doc = iAcuse.getString("IDENT_DOC");
	        	else 
	        		sIdent_doc="";
	        	
	        	if(!vIdDoc.contains(sIdent_doc)){
	        		vIdDoc.add(sIdent_doc);		        	
		        	String estado = "";
		        	if (iAcuse.getString("ESTADO")!=null) estado = iAcuse.getString("ESTADO"); else estado="";
		        	if(estado.equals("1")){
			        	AplicacionesServiceProxy asp = new AplicacionesServiceProxy();
			        	
			        	int valor = Integer.parseInt(iAcuse.getString("ID_NOTIFICACION"));
			        	NotificacionWS estadoNotif = asp.recuperarEstadosNotificaciones(entidad, valor);
			        	
			        	String desNoti = estadoNotif.getDescripcion();
			        	String [] vDescNotif = desNoti.split(" - ");
			        	String nombreDesc = "";
			        	if(vDescNotif != null){
			        		if(vDescNotif.length >= 2){
			        			nombreDesc = " - "+vDescNotif[1];
			        		}
			        	}

			        	//DataHandler acuse = asp.recuperarAcuseFirmado(Constants.COMPARECE.SIGEM, estadoNotif.getIdNotificacionRepresentante());
						DataHandler acuse = asp.recuperarAcuseFirmado(entidad, estadoNotif.getIdNotificacionRepresentante());
						if(acuse != null){
							String nombre = acuse.getName();
				        	file = new File(nombre);
				        	
				        	String descripcion_documento = "";
				        	
				        	IItem iDoc = DocumentosUtil.getDocumento(entitiesAPI, Integer.valueOf(sIdent_doc));
				        	
							if (iDoc.getString("DESTINO")!=null)
								descripcion_documento = iDoc.getString("DESCRIPCION").concat(" - certificado de Comparece");
							else throw new ISPACRuleException("El identificador de documento:"+sIdent_doc+" ya no existe en la base de datos, borrar de los envíos de Comparece");
							
				        	
				        	IItem newdoc = genDocAPI.createTaskDocument(rulectx.getTaskId(), idTipDoc);
				        	int docId = newdoc.getInt("ID");
				        	logger.warn("Hago transacción de documento auxiliar vacio en spac_dt_dcoumetos docId="+docId);
				        	newdoc.store(cct);				        	
				        	logger.warn("Antes de pasar el documento a SIGEM: docId="+docId);
				        	logger.warn("Antes de pasar el documento a SIGEM: rulectx.getTaskId()="+rulectx.getTaskId());
				        	IItem entityDocument1 = DocumentosUtil.anexaDocumento(rulectx, rulectx.getTaskId(), docId, file, "pdf", descripcion_documento);
				        	logger.warn("Después de pasar el documento a SIGEM: entityDocument1.getKeyInteger()="+entityDocument1.getKeyInteger());
				        	//entityDocument1.set("ID_PLANTILLA", idPlantillaCompar1);
				        	//logger.warn("Antes de realizarentityDocument1.store(cct);");
							//entityDocument1.store(cct);
							logger.warn("Después de realizarentityDocument1.store(cct);");
				        	file.delete();
							iAcuse.delete(cct);
						}
						
						//Actualizar el estado de la notificacion en spac_dt_documentos	
						logger.warn("Vamos a actualizar el estado de la notificación sIdent_doc="+sIdent_doc);
						IItem itemBean = entitiesAPI.getDocument(Integer.parseInt(sIdent_doc));
						logger.warn("Antes de actualizar el estado de la notificación itemBean.getKeyInt()="+itemBean.getKeyInt());
						itemBean.set("ESTADONOTIFICACION","OK");
						itemBean.store(cct);
						logger.warn("Después de actualizar el estado de la notificación");
		        	}
		        	else{
		        		
		        		//El estado es = 0 y ademas la fecha ha caducado
		        		String ident_doc = "";
			        	if (iAcuse.getString("IDENT_DOC")!=null) ident_doc = iAcuse.getString("IDENT_DOC"); else ident_doc="";
		    	        IItem itemDocument = entitiesAPI.getDocument(Integer.parseInt(ident_doc));
		    	        //logger.warn("ident_doc "+ident_doc);
		    	        String destino = "";
		    	        if (itemDocument.getString("DESTINO")!=null) destino = itemDocument.getString("DESTINO"); else destino="";
		    	        documentComparece.add(new Phrase("- "+destino));
		  				documentComparece.add(new Phrase("\n"));
		  				tieneParticipantesComparece = true;
		  				String infoPagRDE = "";
		  				String infoPag = "";
		  				if (itemDocument.getString("INFOPAG_RDE")!=null) infoPagRDE = itemDocument.getString("INFOPAG_RDE"); else infoPagRDE="";
		  				if (itemDocument.getString("INFOPAG")!=null) infoPag = itemDocument.getString("INFOPAG"); else infoPag="";
		  				String tipoRegistro = "";
		  				if (itemDocument.getString("TP_REG")!=null) tipoRegistro = itemDocument.getString("TP_REG"); else tipoRegistro="";
		  				String numRegistro = "";
		  				if (itemDocument.getString("NREG")!=null) numRegistro = itemDocument.getString("NREG"); else numRegistro="";
		  				String fechaRegistro = "";
		  				if (itemDocument.getString("FREG")!=null) fechaRegistro = itemDocument.getString("FREG"); else fechaRegistro="";
		  				String departamento = "";
		  				if (itemDocument.getString("ORIGEN")!=null) departamento = itemDocument.getString("ORIGEN"); else departamento="";
		  				
		  				file=null;
			  			if(StringUtils.isNotBlank(infoPagRDE)){
			  				 file = DocumentosUtil.getFile(cct, infoPagRDE, null, null);
			  			} else {
			  				 file = DocumentosUtil.getFile(cct, infoPag, null, null);
			  			}
			  			
		        		String pathFileTemp = FileTemporaryManager.getInstance().put(file.getAbsolutePath(), ".pdf");
		  				
		  				addGrayBand(file, pathFileTemp, infoPagRDE, tipoRegistro, numRegistro, fechaRegistro, departamento, entidad);
		  				
		  				String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
			  			
			  			File fileSello = new File(rutaFileName);
			  			FileInputStream fis = new FileInputStream(fileSello);
			  			
			  			inputStreamNotificaciones.add(fis);
			  			filePathNotificaciones.add(rutaFileName);
			  			
			  		    //Actualizar el estado de la notificacion en spac_dt_documentos				
						IItem itemBean = entitiesAPI.getDocument(Integer.parseInt(sIdent_doc));
						itemBean.set("ESTADONOTIFICACION","CA");
						itemBean.store(cct);
		        	}
	        	}
	        }
	        
	        documentComparece.close();
	                
	        fileConcatenado = concatenaPdf(inputStreamNotificaciones, filePathNotificaciones, genDocAPI);
	        
	        if(fileConcatenado != null){
				IItem entityDocument = DocumentosUtil.generaYAnexaDocumento(rulectx, idTipDoc, "Notificación Usuarios Caducados Comparece", fileConcatenado,  "pdf");
				entityDocument.set("ID_PLANTILLA", idPlantilla);
				entityDocument.store(cct);
	        }
	        
	        if(fileCompareceNombre != null && tieneParticipantesComparece){	        	       
				IItem entityDocumentListado = DocumentosUtil.generaYAnexaDocumento(rulectx, idTipDoc, "Listado Usuarios Caducados Comparece", fileCompareceNombre, "pdf");
		        entityDocumentListado.set("ID_PLANTILLA", idPlantillaCompar1);
		        entityDocumentListado.store(cct);
	        }
	        
	        ejecutar = true;	        
		   				
			//Elimino los registro de la tabla DPCR_ACUSES_COMPARECE
	        logger.warn("Antes de borrar registro de DPCR_ACUSES_COMPARECE rulectx.getNumExp()="+rulectx.getNumExp());
	        logger.warn("Antes de borrar registro de DPCR_ACUSES_COMPARECE +rulectx.getTaskId()="+rulectx.getTaskId());
			String strQuery1 = "WHERE NUMEXP = '"+rulectx.getNumExp()+"' AND TRAMITE="+rulectx.getTaskId()+"";
			entitiesAPI.deleteEntities(Constants.TABLASBBDD.DPCR_ACUSES_COMPARECE, strQuery1);
			logger.warn("Despues de borrar registro de DPCR_ACUSES_COMPARECE");
			
		} catch (ISPACException e) {
			logger.error("Error1. "+e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(), e);
		} catch (NumberFormatException e) {
			logger.error("Error2. "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(), e);
		} catch (RemoteException e) {
			logger.error("Error3. "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Error4. "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(), e);
		} catch (SQLException e) {
			logger.error("Error5. "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(), e);
		} catch (DocumentException e) {
			logger.error("Error6. "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error7. "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(), e);
		}finally{
			 try {
				 
				if(null!=documentComparece){
			    		
				    	if(documentComparece.isOpen())
				    		documentComparece.close();
				    	
			    }
				 
				
				 
				for (int i=0; i<inputStreamNotificaciones.size(); i++)					
						((FileInputStream)inputStreamNotificaciones.get(i)).close();		
						 
				
				System.gc();
				
				
			} catch (IOException e) {				
				logger.error("Error finally "+e.getMessage(), e);			
				throw new ISPACRuleException("Error. "+e.getMessage(), e);
			}
			
		}
			 
		return new Boolean(ejecutar);
	}
	
	void addGrayBand(File file, String pathFileTemp, String infoPagRDE, String tipoRegistro, String numRegistro, String fechaRegistro, String departamento, String entidad)
	throws Exception {

		float margen = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.MARGIN_BAND));
		float bandSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.SIZE_BAND));
		
		try {
		
			PdfReader readerInicial = new PdfReader(file.getAbsolutePath());
			int n = readerInicial.getNumberOfPages();
			int largo = (int) readerInicial.getPageSize(n).getHeight();			
			
			//tamaño de la banda
			//bandSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(SIZE_INICIAL_BAND));
			bandSize = 15;

			Image imagen = this.createBgImage(entidad);
			Document document = new Document();
			String ruta = FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/" + pathFileTemp;
			FileOutputStream fileOut = new FileOutputStream(ruta, true);
			
			PdfWriter writer = PdfWriter.getInstance(document, fileOut);
			document.open();
			Rectangle r = document.getPageSize();
		
			for (int i = 1; i <= n; i++) {
				PdfImportedPage page = writer.getImportedPage(readerInicial, i);
				Image image = Image.getInstance(page);

				image.setAbsolutePosition(bandSize, 0.0F);
				image.scaleAbsoluteWidth(r.getWidth() - bandSize);
				image.scaleAbsoluteHeight(r.getHeight());
				imagen.setRotationDegrees(90F);
				document.add(image);
				if (imagen != null) {
					for (int j = 0; j < largo; j = (int) ((float) j + imagen.getWidth())) {
						imagen.setAbsolutePosition(0.0F, j);
						imagen.scaleAbsoluteHeight(bandSize);
						document.add(imagen);
					}
				}
				PdfContentByte over = writer.getDirectContent();
				getImagen(over, margen, margen, n, i, tipoRegistro, numRegistro, fechaRegistro, departamento, entidad);				
					
				document.newPage();
			}
		
			document.close();			
			
			//INICIO [eCenpri-Felipe Ticket#195]
			//Error al firmar fichero grandes - Cerrar descriptores
			fileOut.close();
			writer.close();
			readerInicial.close();
			//FIN [eCenpri-Felipe Ticket#195]
		
		} catch (ISPACException e) {
			logger.error("Error al añadir la banda lateral al PDF", e);
			throw new ISPACRuleException("Error. ",e);
		} catch (Exception exc) {
			logger.error("Error al añadir la banda lateral al PDF", exc);
			throw new ISPACException(exc);
		}
	}
	
	protected Image createBgImage(String entidad) throws ISPACException {

		// Ruta relativa de la imagen de fondo
		//String imagePath = ISPACConfiguration.getInstance().getProperty("config_"+entidad+"/"+DEFAULT_PDF_BG_IMAGE_PATH);
		String imageFondoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_FONDO_PATH_DIPUCR);
		
		//if (StringUtils.isBlank(imagePath)) {
		//	imagePath = "config_"+entidad+"/"+DEFAULT_PDF_BG_IMAGE_PATH;
		//}

		try {
			// Ruta absoluta de la imagen de fondo
			File fondoURL = new File(imageFondoPath);
			Image fondo = null;
			if(fondoURL != null){
				fondo = Image.getInstance(imageFondoPath);
				fondo.setAbsolutePosition(250, 50);
				fondo.scalePercent(70);
				
			}
			return fondo;
			
		} catch (Exception e) {
			logger.error("Error al leer la imagen de fondo del PDF firmado: " + imageFondoPath, e);
			throw new ISPACException(e);
		}
	}
	
	protected void getImagen(PdfContentByte pdfContentByte,
			float margen, float x, int numberOfPages, int pageActual, String tipoReg, String numReg, String fecReg, String departamento, String entidad) throws ISPACException {
		
		try {
		
			String font = ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.FONT_BAND);
			String encoding = ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.ENCODING_BAND);
			float fontSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.FONTSIZE_BAND));
			
			BaseFont bf = BaseFont.createFont(font, encoding, false);
			pdfContentByte.beginText();
			pdfContentByte.setFontAndSize(bf, fontSize);

			BufferedReader br = new BufferedReader(new FileReader(this.getDataFile(entidad)));
			String sCadena = null;
			int i = 0;
			String scadenaReg = "";
			pdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, x, margen);		
			while ((sCadena = br.readLine()) != null) {
				if (i == 0) {
					scadenaReg += sCadena + " " + tipoReg +" ";
				} 
				if (i == 1) {
					scadenaReg += sCadena + " " + numReg + " ";			
				} 
				if (i == 2) {
					scadenaReg += sCadena + " " + fecReg + " ";						
				}
				if (i == 3) {
					scadenaReg += sCadena + " " + departamento;						
				}
				i++;
			}
			pdfContentByte.showText(scadenaReg);

			pdfContentByte.endText();
			
			//INICIO [eCenpri-Felipe Ticket#195]
			br.close();
			
		} catch (Exception e) {
			logger.error("Error al componer la imagen de la banda lateral", e);
			throw new ISPACException(e);
		}
	}
	
	protected File getDataFile(String entidad) throws ISPACException {
		
		// Ruta relativa del texto de la banda lateral
		String dataPath = ISPACConfiguration.getInstance().getProperty("config_"+entidad+"/"+DEFAULT_PDF_BG_DATA_PATH);
		if (StringUtils.isBlank(dataPath)) {
			dataPath = "config_"+entidad+"/"+DEFAULT_PDF_BG_DATA_PATH;
		}

		String basename = null;
		String ext = null;
		int dotIx = dataPath.lastIndexOf(".");
		if (dotIx > 0) {
			basename = dataPath.substring(0, dotIx);
			ext = dataPath.substring(dotIx);
		} else {
			basename = dataPath;
		}
		
		// Ruta absoluta del texto localizado de la banda lateral
		String dataFullPath = ConfigurationHelper.getConfigFilePath(basename + ext);
		if (StringUtils.isBlank(dataFullPath)) {
			
			// Ruta absoluta del texto de la banda lateral
			dataFullPath = ConfigurationHelper.getConfigFilePath(dataPath);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Texto de la banda lateraldel PDF: " + dataFullPath);
		}
		return new File(dataFullPath);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private File concatenaPdf(ArrayList<FileInputStream> inputStreamNotificaciones, ArrayList<String> filePathNotificaciones, IGenDocAPI genDocAPI) throws ISPACRuleException {
	    
		File resultado = null;
		File file = null;
		
		try {
			// Creamos un reader para el documento
	    	PdfReader reader = null;
	    	PdfReader readerBlanco = null;
	    	    	
			Document document = null;
	        PdfCopy  writer = null;
	        boolean primero = true;//Indica si es el primer fichero (sobre el que se concatenarán el resto de ficheros)
	        InputStream f = null;
	
	        int pageOffset = 0;
	        ArrayList master = new ArrayList();
	        
	        if(inputStreamNotificaciones.size() != 0){
		        Iterator inputStreamNotificacionesIterator = inputStreamNotificaciones.iterator();
		        while (inputStreamNotificacionesIterator.hasNext()){
				    reader = new PdfReader((InputStream)inputStreamNotificacionesIterator.next());
		        	
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
			        	file = PdfUtil.blancoPDF();
			        	f = new FileInputStream(file.getAbsolutePath());
			        	readerBlanco = new PdfReader(f);
			        	page = writer.getImportedPage(readerBlanco, 1);
			        	writer.addPage(page);
			        }
			        //MQE Fin modificaciones Ticket #180 para poder imprimir en dos caras
			        if(reader!=null){
			        	reader.close();
			        }
			        
			        if(readerBlanco!=null){
			        	readerBlanco.close();
			        }			        
			        if(f!=null){
			        	f.close();
			        	f = null;
			        }
			        if(file!=null){
			        	file.delete();
			        	file = null;
			        }
			        
			        //file.close();
		        }//while
	        }
	        if (!master.isEmpty())
	            writer.setOutlines(master);
	        if(inputStreamNotificaciones.size() != 0)	document.close();
	    }
	    catch(Exception e) {
	    	logger.error("Error. "+e.getMessage(), e);
	    	throw new ISPACRuleException("Error validate. "+e.getMessage(),e);
	    }
	    return resultado;
	}  

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean cierraTramiteComparece = false;
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			int tramite = rulectx.getTaskId();
	
			//si no existe ningun dato con estado 0 es que son todos 1 y por lo tanto han visto todos las notificaciones
			String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND ESTADO='0' AND TRAMITE="+tramite+"";

			IItemCollection collection = entitiesAPI.queryEntities("DPCR_ACUSES_COMPARECE", strQuery);
	        Iterator<IItem> it = collection.iterator();
	       
	        try{
				
				if (!it.hasNext()){
			        	cierraTramiteComparece = true;
			        	
			    }
			    if(!cierraTramiteComparece){
			        	rulectx.setInfoMessage("No se puede cerrar el trámite porque todavía quedan notificaciones sin recibir en COMPARECE.");
			    }
			     
			
			}
		    catch(Exception e){
		    	
		    	logger.error("La entidad no tiene configurado la variable de sistema API_KEY_NOTIFICA");
		    	
		    }
	        
	        
		} catch (ISPACException e) {
			throw new ISPACRuleException("Error validate. ",e);
		} 
		
		return cierraTramiteComparece;
	}

}
