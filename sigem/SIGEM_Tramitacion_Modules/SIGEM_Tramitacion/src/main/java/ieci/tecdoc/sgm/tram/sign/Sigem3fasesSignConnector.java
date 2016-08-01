package ieci.tecdoc.sgm.tram.sign;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.common.constants.EntityLockStates;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.messages.MessagesFormatter;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.sign.InfoFirmante;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.sign.afirma.AFirmaSign3FasesVerifyConnector;
import ieci.tdw.ispac.ispaclib.sign.afirma.TelefonicaSignConnector;
import ieci.tdw.ispac.ispaclib.sign.exception.InvalidSignatureValidationException;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.cripto.firma.Firmante;
import ieci.tecdoc.sgm.core.services.cripto.firma.ResultadoValidacionFirma;
import ieci.tecdoc.sgm.core.services.cripto.firma.ServicioFirmaDigital;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.ServicioEstructuraOrganizativa;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuario;
import ieci.tecdoc.sgm.core.services.gestioncsv.CodigosAplicacionesConstants;
import ieci.tecdoc.sgm.core.services.gestioncsv.InfoDocumentoCSV;
import ieci.tecdoc.sgm.core.services.gestioncsv.InfoDocumentoCSVForm;
import ieci.tecdoc.sgm.core.services.gestioncsv.ServicioGestionCSV;
import ieci.tecdoc.sgm.core.services.tiempos.ServicioTiempos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import es.dipucr.sigem.api.rule.common.utils.GestorDecretos;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;


public class Sigem3fasesSignConnector extends AFirmaSign3FasesVerifyConnector {
	
	protected static final String INFODOCUMENTO_CSV_TIPO_MIME = "application/pdf";

	
	/**
	 * Constructor. 
	 * 
	 */
	public Sigem3fasesSignConnector() {
		super();
	}

	/**
	 * Realiza una validación de una firma o un hash.
	 * 
	 * @param signatureValue Valor de la firma
	 * @param signedContentB64 Contenido de la firma en base 64
	 * @throws InvalidSignatureValidationException
	 *             Si la firma no es válida.
	 * @return Detalles de la validación.
	 * 
	 */
	public  Map verify(String signatureValue, String signedContentB64) {
		
		boolean firmaVerificada=false;
		Map result = new HashMap();
		
		try {

			ServicioFirmaDigital firmaDigital = LocalizadorServicios.getServicioFirmaDigital();
			//Llamamos al servicio de firma digital que invocará al servicio de @firma para validar la firma.

			ResultadoValidacionFirma validacionFirma= firmaDigital.validarFirma(signatureValue.getBytes(),signedContentB64.getBytes());
			firmaVerificada=validacionFirma.isValidationResultOK();
			List signers= validacionFirma.getSigners();
			result.put(ISignAPI.DN, ((Firmante)signers.get(0)).getName());
			

		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			result.put(ISignAPI.INTEGRIDAD, ISignAPI.INTEGRIDAD_ERROR);
		}
		if(firmaVerificada){
		result.put(ISignAPI.INTEGRIDAD, ISignAPI.INTEGRIDAD_OK);
		
		}
		else{
			result.put(ISignAPI.INTEGRIDAD, ISignAPI.INTEGRIDAD_STRANGER);
		}
		return result;
	}
	
	
	/**
	 * Obtiene el documento correspondiente del repositorio convertido en pdf
	 * 
	 * @param docRef referencia del documento
	 * @throws InvalidSignatureValidationException si no se puede recuperar
	 * @return retorna el documento pdf
	 * 
	 */
	protected File getFile(String docRef) throws ISPACException{

		IGenDocAPI gendocAPI = clientContext.getAPI().getGenDocAPI();
		Object connectorSession = null;
		File file = null; 
		
		try {
			connectorSession = gendocAPI.createConnectorSession();

			String extension = MimetypeMapping.getExtension(gendocAPI.getMimeType(connectorSession, docRef));
			if(extension != "pdf") {
				
				// Convertir el documento original a PDF
				file = convert2PDF(docRef, extension);
				
			} else {
				
				// Se obtiene el documento del repositorio documental
				String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
				fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;

				
				OutputStream out = new FileOutputStream(fileName);
				gendocAPI.getDocument(connectorSession, docRef, out);

				file = new File(fileName);
			}
			
			signDocument.setDocument(new FileInputStream(file));
				
		} catch (FileNotFoundException e) {
			logger.error("Error al obtener el fichero: " + docRef, e);
			throw new ISPACException(e);
		} finally {
			if (connectorSession != null) {
				gendocAPI.closeConnectorSession(connectorSession);
			}
		}
		
		return file;
	}
	
	/**
	 * Convierte le fichero a pdf
	 * @param infoPag 
	 * @param extension
	 * @return
	 * @throws ISPACException
	 */	
	private File convert2PDF(String infoPag, String extension) throws ISPACException {
		
		// Convertir el documento a pdf
		String docFilePath= DocumentConverter.convert2PDF(clientContext.getAPI(), infoPag, extension);
		
		// Obtener la información del fichero convertido
		File file = new File( docFilePath);
		if (!file.exists())
			throw new ISPACException("No se ha podido convertir el documento a PDF");
		InputStream in;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new ISPACException("Fichero no encontrado: '" + file.getName() + "'");
		}
		signDocument.setDocument(in);
		return file;
	}
	
	/**
	 * Realiza la prefirma del documento
	 * 
	 * @return id del fichero firmado
	 * @throws ISPACException
	 */	
	public String presign(boolean changeState) throws ISPACException{
		
		String pathTemporal = generatePdfToSign(changeState);				
		return pathTemporal;

	}
	
	/**
	 * Realiza la postfirma del documento
	 * Una vez que el servidor de firma devuelve el documento firmado como temporal lo guardamos y actualizamos la base de datos
	 * Actualizacion de circuitos de firma, documentos del tramitador y repositorio
	 * @return id del fichero firmado
	 * @throws ISPACException
	 */	
	public String postsign(String pathFicheroTemporalFirmado, boolean changeState) throws ISPACException{
		
		String path = FileTemporaryManager.getInstance().getFileTemporaryPath();		
		path = path.concat(pathFicheroTemporalFirmado);		
		
		File file = new File(path);
		String infoPagRDE = signDocument.getItemDoc().getString("INFOPAG_RDE");
		
		if (StringUtils.isEmpty(infoPagRDE) ){
			addNewSign(file, changeState);
		}	
		else{
			addSign(file, changeState);
		}
		
		// Transacción de firma auxiliar para Diputación
		// TODO: Esto es un id interno de Diputación, no de entidad certificadora
		String idTransaction = GestorDecretos.generarIdTransaccionFirma(signDocument);
		
		return "OK";
		
	}
	
	/**
	 * Al firmar un documento por primera vez hay que actualizar no solo la firma sino también el propio documento
	 * @param file fichero temporal donde firmando
	 * @param changeState
	 * @throws ISPACException
	 */
	protected void addNewSign(File file, boolean changeState) throws ISPACException{
		
		InputStream in;
		if (file == null)
			throw new ISPACException("No se ha podido convertir el documento a PDF");
		
		else
		{
		
		String infoPagRDE = signDocument.getItemDoc().getString("INFOPAG_RDE");
		
		IInvesflowAPI invesflowAPI = clientContext.getAPI();
		//Obtenemos el tipo mime
		int index1 = file.getName().lastIndexOf(".");
		String sExtension = file.getName().substring(index1 + 1, file.getName().length());
		String sMimeType = MimetypeMapping.getMimeType(sExtension);

		IItem itemDoc = signDocument.getItemDoc();

		//Obtenemos el nombre del tipo de documento
		String doctype = itemDoc.getString("NOMBRE");

		//Obtenemos la fase
		String stageName = invesflowAPI.getProcedureStage(itemDoc.getInt("ID_FASE_PCD")).getString("NOMBRE");
			
		//Obtenemos el tramite si el documento esta asociado a un tramite
		String taskName = null;
		if(itemDoc.getInt("ID_TRAMITE_PCD") != 0)
			taskName = invesflowAPI.getProcedureTaskPCD(itemDoc.getInt("ID_TRAMITE_PCD")).getString("NOMBRE");
		
		try {			
			in = new FileInputStream(file);
		} catch (Exception e) {
			throw new ISPACException("Fichero no encontrado: '" + file.getName() + "'");
		}
		
		signDocument.setDocument(in);
			
		//Componemos los metadatos del documento firmado a guardar
		signDocument.setDocumentType(doctype);
		signDocument.setLength((int)file.length());
		signDocument.setMimetype(sMimeType);
		signDocument.setFechaCreacion(new Date());
		if (stageName != null) {
				signDocument.setStage(stageName);
		}
		if (taskName != null) {
				signDocument.setTaskName(taskName);
		}
			
		String documentName = itemDoc.getString("DESCRIPCION");
		if (StringUtils.isBlank(documentName)) {
			documentName = itemDoc.getString("NOMBRE");
		}
		documentName += "." + FileUtils.getFileExtension(file);
		signDocument.setDocumentName(documentName);

		//Se almacena el documento firmado
		infoPagRDE = store();
			
		// Eliminar el fichero PDF una vez subido al gestor documental
		file.delete();
	
		//Actualizamos el documento
		updateDataDoc(infoPagRDE, sExtension,changeState);
		
		}
			
	}
	/**
	 * Añadimos la nueva firma a la firma del documento.
	 * Cuando se está en un circuito de deberán almacenar varias firmas, en el momento que se alamacene la primera simplemente hay que
	 * actualizar la firma y los metadatos.
	 * 
	 * @param file fichero temporal donde firmando
	 * @param changeState
	 * @throws ISPACException
	 */
	protected void addSign(File file, boolean changeState) throws ISPACException{
		
		InputStream in;
		if (file == null)
			throw new ISPACException("No se ha podido convertir el documento a PDF");	
		
		else
		{				
						
				IGenDocAPI genDocAPI = clientContext.getAPI().getGenDocAPI();
				Object connectorSession = null;
				try {
					connectorSession = genDocAPI.createConnectorSession();
					
					in = new FileInputStream(file);
					signDocument.setDocument(in);
					
					//[dipucr #186]
					addNewSign(file, changeState);
					
				} catch (FileNotFoundException e) {
					throw new ISPACException("Fichero no encontrado: '" + file.getName() + "'");
				} finally {
					if (connectorSession != null) {
						genDocAPI.closeConnectorSession(connectorSession);
					}
				}	
		
		}
	}
	
	
	/**
	 * Realiza la firma del documento
	 * 
	 * @return id del fichero firmado
	 * @throws ISPACException
	 */	
	public void sign(boolean changeState) throws ISPACException{
		
		int signerNumber = signDocument.getSign().size();		
		if (signerNumber == 0) {
			throw new ISPACException("El documento " + signDocument.getIdPcd()
					+ "tiene que estar firmado.");
		}
		
		ServicioFirmaDigital firmaDigital;
		try {
			firmaDigital = LocalizadorServicios.getServicioFirmaDigital();
			String signatureValue = signDocument.getSign().get(signerNumber - 1).toString();
			//[eCenpri-Felipe Ticket#224]
			//String signCertificateValue = signDocument.getSignCertificate().get(signerNumber - 1).toString();
			//String signCertificateValue = signDocument.getSignCertificate().get(0).toString();
			String signCertificateValue = null; //TODO - de momento no puedo recuperar el certificado firmante
			String hash = signDocument.getHash();
			
			//Llamamos al servicio de firma digital
			//String idTransaction=firmaDigital.registrarFirma(Base64Util.decode(signatureValue), Base64Util.decode(signCertificateValue), Base64Util.decode(hash));
			String idTransaction="SIN_ID_TRANSACCION";//TODO como no tengo certificado para que registrar
			
			if (logger.isInfoEnabled()) {
				logger.info("Documento registrado en la plataforma afirma OK: "	+ idTransaction);
			}
			storeIdTransaccion(idTransaction);	
		} catch (Exception e) {
			logger.error("No se ha podido registrar el documento en la plataforma firma5.",e);
			throw new ISPACException(e);
		}

	}
	
	/**
	 * Obtiene el documento , pdf si ya se hubiese firmado alguna vez o el original en otro caso.
	 * Añade los datos del firmante y genera o modifica  el pdf.
	 * @param signDocument 
	 * @param changeState
	 * @throws ISPACException
	 */
	private String generatePdfToSign(boolean changeState) throws ISPACException{
		
		// AJM ruta del fichero temporal resultado
		String result= "/";
		
		//AJM rutas del repositorio
		String infoPag = signDocument.getItemDoc().getString("INFOPAG");
		String infoPagRDE = signDocument.getItemDoc().getString("INFOPAG_RDE");
		
		//AJM atributos iText y demas rutas para manejar el fichero
		PdfStamper stp = null;
		PdfReader reader = null;
		FileOutputStream fout = null;
		String pathFicheroFirmaOrigen = null;		
		String pathFicheroFirmaDestino = null;
		File file= null;
		PdfContentByte over = null;
		
		//AJM atributos banda gris
		String sNumDecreto = null;
		boolean bloquearDocumento = false;
		String codCotejo = "";
		float margen = 0;
		int num_paginas = 0;
		Image imagen_gris = null;
		
		if(StringUtils.isNotBlank(infoPagRDE)){
			 file = getFile(infoPagRDE);
		} else {
			 file = getFile(infoPag);
		}	
		
		try {			
			
			//AJM IDENTIFICAR EL FIRMANTE
			logger.warn("AJM IDENTIFICAR EL FIRMANTE");	
			
			logger.warn("NUMEXP. "+signDocument.getNumExp());
			//TODO: Realmente está cogiendo el que cargamos del UsuariosUtil en el action
			String firmante = ((InfoFirmante) signDocument.getSignCertificate().
					get(signDocument.getSignCertificate().size()-1)).getNombreFirmante();
			logger.warn("firmante "+firmante);
			String nombreFirmante = firmante;
			
			//**********************************************************
			
			//AJM FECHA DE LA FIRMA
			logger.warn("AJM FECHA DE LA FIRMA");
			Calendar signCalendar = Calendar.getInstance();
			SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
			String dateFirma = FORMATTER.format(signCalendar.getTime());
			logger.warn(dateFirma);
			
			//**********************************************************
			
			//AJM CONTROL DE DECRETO
			logger.warn("AJM CONTROL DE DECRETO");
			boolean isDocDecreto = TelefonicaSignConnector.isDocDecreto(signDocument);
//			int idDoc = signDocument.getItemDoc().getKeyInt();
//			boolean isFirmaPresidente = GestorDecretos.esFirmaPresidente(this.clientContext, idDoc);

			if(StringUtils.isBlank(infoPagRDE)){
				// No es la firma del presidente, se calcula el codCotejo y se añade la banda gris
				codCotejo = addCodVerificacion(signDocument, clientContext);
			}
			logger.warn("¿Es un decreto?"+isDocDecreto);
			
			logger.warn("AJM CONTROL FIRMA PRESIDENTE");
			
			//Obtenemos el número de decreto del signDocument
			sNumDecreto = signDocument.getNumDecreto();
			boolean isFirmaPresidente = (StringUtils.isNotEmpty(sNumDecreto) && !sNumDecreto.equals(GestorDecretos.CLAVE_FIRMA_FEDATARIO));
			logger.warn("¿Numero de decreto?"+sNumDecreto);
			
			// Es la firma del presidente, NO se calcula de nuevo el codCotejo, se añade el número de decreto en la zona superior derecha del documento		
			logger.warn("AJM CALCULO DEL CÓDIGO DE VERIFICACIÓN");
			if (StringUtils.isNotEmpty(sNumDecreto)){
				
				codCotejo = signDocument.getItemDoc().getString("COD_COTEJO");				
				
				//INICIO [eCenpri-Felipe #804]
				//Si le hemos dado número de decreto, lo bloqueamos para que no se pueda borrar 
				if (isDocDecreto){ //redundante, si tiene numDecreto es que es un decreto
					bloquearDocumento = true;
				}
				//FIN [eCenpri-Felipe #804]
			}
			logger.warn("Codigo de Verifiacion: "+codCotejo);
			
			//**********************************************************
			
			//AJM BANDA GRIS			
			logger.warn("AJM BANDA GRIS");
			// Pondremos la banda gris en los siguientes casos:
			// - Cualquier documento no firmado ya
			// - Documentos de decreto firmados por el presidente
			if((isDocDecreto && isFirmaPresidente) || StringUtils.isBlank(infoPagRDE)){
			
				//El fichero destino estara en la carpeta temporal mas el nombre del fichero origen del repositorio
				pathFicheroFirmaOrigen = FileTemporaryManager.getInstance().put(file.getAbsolutePath(), ".pdf");
				pathFicheroFirmaDestino = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFicheroFirmaOrigen;
				reader = new PdfReader(pathFicheroFirmaDestino);
				fout = new FileOutputStream(file.getAbsolutePath());
				
				stp = new PdfStamper(reader, fout);				
				
				//Eliminamos la firma del jefe de servicio en el caso de que sea decreto y la firma del presidente.
				//Si no es decreto en cualquier otro caso no borramos el resto de firmas.
				if(isDocDecreto && isFirmaPresidente){
					reader.removeFields();
				}			
				
				dateFirma = StringUtils.replace(dateFirma, "-", "/");
			
				
				//MQE ponemos la imagen antes de incrustar la firma
				margen = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(MARGIN_BAND));

				//PdfReader readerInicial = new PdfReader(file.getAbsolutePath());
				num_paginas = reader.getNumberOfPages();	
				
				//Obtiene la imagen gris .gif que se utiliza en la banda
				imagen_gris = createBgImage();
				
				for(int i=1; i<=num_paginas; i++){	
				
					int largo = (int) reader.getPageSize(i).getHeight();
					int ancho = (int) reader.getPageSize(i).getWidth();
					
					//Añadimos el rectángulo gris del fondo de la imagen
					imagen_gris.scaleAbsoluteWidth(largo);
					imagen_gris.scaleAbsoluteHeight(30);
					imagen_gris.setAbsolutePosition(0, 0);
					imagen_gris.setRotationDegrees(90F);
	
					over = stp.getOverContent(i);
					over.addImage(imagen_gris);
					//Fin de añadir el rectángulo gris del fondo de la imagen
				
					//Añadimos el textito con el número de decreto de la cabecera
					if(isDocDecreto && isFirmaPresidente){
						addNumDecreto(over, sNumDecreto, ancho, largo);
					}
					//Fin de añadir el textito con el número de decreto
					
					//Añadimos el texto de la izquierda con la información de la firma 
					//sólo pone el nombre del firmante si no es decreto por eso si es la firma del presidente le pasamos un false
					if (isDocDecreto && isFirmaPresidente)
						getImagen(dateFirma, over, margen, true, margen, num_paginas, i, nombreFirmante, codCotejo, sNumDecreto, false);					
					else
						getImagen(dateFirma, over, margen, true, margen, num_paginas, i, nombreFirmante, codCotejo, sNumDecreto, isDocDecreto);					
				}					
				stp.close();
			}			
									
			logger.warn("AJM FIN BANDA GRIS");			
			
		} catch(Exception exc) {
			logger.error("ERROR FIRMA al generar la banda gris [sign][Exception]", exc);
			throw new ISPACException(exc);
		}
		
		//**********************************************************
		
		try{
			
			//TODO BLOQUEAR DOCUMENTO (en caso de ser el primer firmante del circuito del Decreto)
			if (bloquearDocumento){ 
				IItem itemDoc = signDocument.getItemDoc();
				itemDoc.set("BLOQUEO", EntityLockStates.TOTAL_LOCK);
				itemDoc.store(clientContext);
			}
			
			
		} catch(Exception exc) {
			logger.error("ERROR FIRMA al bloquear documento [sign][Exception]", exc);
			throw new ISPACException(exc);
		}	
		
		result = result.concat(file.getName());
		
		if(result.equals("/"))
		{			
			logger.error("ERROR FIRMA, EL FICHERO RESULTADO DE LA PREFIRMA NO EXISTE");
			throw new ISPACException();
		}				
		
		try
		{			
			FileTemporaryManager.getInstance().delete(pathFicheroFirmaOrigen);
			
		} catch(Exception exc) {
			
			logger.error("NO EXISTE DOCUMENTO TEMPORAL PARA BORRAR, ES POSIBLE QUE SEA SEGUNDA FIRMA");
			
		}
		
		return result;
	}
	
	
	
	
	
	private void addNumDecreto(PdfContentByte over, String sNumDecreto, int x, int y) {
		try {
			String font = ISPACConfiguration.getInstance().getProperty(FONT_BAND);
			String encoding = ISPACConfiguration.getInstance().getProperty(ENCODING_BAND);
		
			BaseFont fuente;
			fuente = BaseFont.createFont(font, encoding, false);
			String texto = "Decreto número: "+sNumDecreto; 
    	
	    	over.beginText();	    	
	    	over.setFontAndSize(fuente, 12);
	    	over.moveText(x-275, y-75);
	    	over.showText(texto);
	    	over.endText();
	    	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ISPACException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	/**
	 * Almacena el id de transaccion generado por la plataforma afirma5 en el
	 * repositorio digital de documentos
	 * 
	 * @param signDocument
	 * @param idTransaccion
	 * @throws ISPACException
	 */
	private void storeIdTransaccion(String idTransaccion) throws ISPACException{
		IGenDocAPI genDocAPI = clientContext.getAPI().getGenDocAPI();
		Object connectorSession = null;
//		String signProperty = null;
		try {
			String infoPagRDE = signDocument.getItemDoc().getString("INFOPAG_RDE");
			
			if (logger.isInfoEnabled()){
				logger.info("Actualizando idTransaccion sobre el documento RDE: " + infoPagRDE);
			}
			
			//INICIO [eCenpri-Felipe #436]
		    //Insertamos también el idTrransaccion en la tabla de BBDD
		    IEntitiesAPI entitiesAPI = clientContext.getAPI().getEntitiesAPI();
		    IItem item = entitiesAPI.createEntity(SpacEntities.SPAC_ID_TRANSACCION_FIRMA);
			item.set("ID_DOCUMENTO", signDocument.getItemDoc().getKeyInt());
			item.set("ID_TRANSACCION", idTransaccion);
			item.set("HASH", signDocument.getHash());
//			item.set("DATOS_FIRMANTE", signDocument.getDatosFirmante());
			item.set("DATOS_FIRMANTE", getInfoFirmante());
			item.set("FECHA", new Date());
			item.store(clientContext);
		    //FIN [eCenpri-Felipe #436]
		    
		}
	    finally {
	    	if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}	
	}
	
	/**
	 * Devuelve la información del firmante para la transacción de firma
	 * @return
	 * @throws ISPACException 
	 */
	private String getInfoFirmante() throws ISPACException {
		
//		//TODO:EFICIENCIA InfoFirmante infoFirmante = (InfoFirmante) signDocument.getSignCertificate().
//				get(signDocument.getSignCertificate().size()-1);
//		StringBuffer sbInfoFirmante = new StringBuffer();
//		sbInfoFirmante.append(infoFirmante.getNombreFirmante());
//		sbInfoFirmante.append(" - NIF ");
//		sbInfoFirmante.append(infoFirmante.getIdDocumentoDeIdentidadEnCertificado());
		ClientContext cct = (ClientContext) this.clientContext;
		StringBuffer sbInfoFirmante = new StringBuffer();
		sbInfoFirmante.append(UsuariosUtil.getNombreFirma(cct));
		sbInfoFirmante.append(" - NIF ");
		sbInfoFirmante.append(UsuariosUtil.getDni(cct));
		return sbInfoFirmante.toString();
	}

	protected void getImagen(String dateFirma, PdfContentByte pdfContentByte, float margen, boolean vh, float x, int numberOfPages,
			int pageActual, String nombreFirmante, String codCotejo, String sNumDecreto, boolean isDocDecreto) throws ISPACException {
		
		try {
		
			String font = ISPACConfiguration.getInstance().getProperty(FONT_BAND);
			String encoding = ISPACConfiguration.getInstance().getProperty(ENCODING_BAND);
			float fontSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(FONTSIZE_BAND));
			//float positionY = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(MARGIN_BAND));
			
			BaseFont bf = BaseFont.createFont(font, encoding, false);
			pdfContentByte.beginText();
			pdfContentByte.setFontAndSize(bf, fontSize);

			String texto = "";
			String name = clientContext.getResponsible().getName();
			String entityId = this.signDocument.getEntityId();
	        
			ServicioEstructuraOrganizativa servEstOrg = LocalizadorServicios.getServicioEstructuraOrganizativa();
			Usuario user = servEstOrg.getUsuario(name, entityId);
	       
	        String descripcion = user.get_description();
	        if (descripcion != null && !descripcion.equals("")){
	        	texto = "\"" + descripcion + "\" " + nombreFirmante + " el día " + dateFirma.replace("/", "-");	
	        }else{
	        	texto = nombreFirmante + " el día " + dateFirma.replace("/", "-");
	        }
			
			BufferedReader br = new BufferedReader(new FileReader(getDataFile()));
			String sCadena = null;
			String sCadenaAux = null;
			
			//Se añade la "descripción" y nombre del firmante y la fecha en una línea. (Si !isDocDecreto)
			//El num de páginas y CVE en otra
			
			//[DipuCR-Agustin #122] ALSIGM3 evitar que el cve tenga espacios en blanco en la banda gris ya que en la consulta de Documentos por cve no coge los espacios en blanco al copiarlos de la banda gris.
			//String codCotejoBlancos = TelefonicaSignConnector.getCodCotejoBlancos(codCotejo);
			fontSize += 3F;
			int i = 0;
			while ((sCadena = br.readLine()) != null) {
				if (vh) {
					pdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, x, margen);
					if(i == 0){
						if(!isDocDecreto){
							pdfContentByte.showText(sCadena + texto);
						}else{
							pdfContentByte.showText("");
						}
					}else if (i == 1) {
						sCadenaAux = sCadena 
						+ MessagesFormatter.format(getString(clientContext.getLocale(), "pdf.band.pageInfo"), new String[] {
							String.valueOf(numberOfPages), 
							String.valueOf(pageActual), 
							String.valueOf(numberOfPages) });
						x -= fontSize;
					} else if (i == 2) {
						pdfContentByte.showText(sCadenaAux + " " + sCadena + codCotejo);
					} else {
						pdfContentByte.showText(sCadena);
					}
					i++;
					x += fontSize;
				} else {
	                pdfContentByte.setTextMatrix(margen, x);
	                if(i == 0){
						if(!isDocDecreto){
							pdfContentByte.showText(sCadena + texto);
						}else{
							pdfContentByte.showText("");
						}
					}else if(i == 1) {
						sCadenaAux = sCadena 
						+ MessagesFormatter.format(getString(clientContext.getLocale(), "pdf.band.pageInfo"), new String[] {
							String.valueOf(numberOfPages), 
							String.valueOf(pageActual), 
							String.valueOf(numberOfPages) });
						x += fontSize;
	                } else if (i == 2) {
	                	pdfContentByte.showText(sCadenaAux + " " + sCadena + codCotejo);
	                } else {
	                    pdfContentByte.showText(sCadena);
	                }
	                i++;
	                x -= fontSize;
	            }
			}

			pdfContentByte.endText();
			
			//INICIO [eCenpri-Felipe Ticket#195]
			br.close();
			
		} catch (Exception e) {
			logger.error("Error al componer la imagen de la banda lateral", e);
			throw new ISPACException(e);
		}
	}	
	
	
	/**
	 * Actualizar la referencia en el campo COD_COTEJO en la tabla de documentos
	 * @throws ISPACException
	 * [DipuCR-Agustin #121] Utilizar el servicio de CSV integrado en Sigem 3 para la firma 3 fases
	 */
	protected synchronized String addCodVerificacion(SignDocument signDocument, IClientContext clientContext) throws ISPACException {
		
		InfoDocumentoCSV infoDocumento = null;

		try {
			ServicioGestionCSV servicioGestionCSV = LocalizadorServicios.getServicioGestionCSV();

			// Información del documento
			IItem itemDoc = signDocument.getItemDoc();

			// Identificador de la entidad
			String entityId = getEntityId();

			// Generación del CSV para el documento
			// haciendo uso del servicio de gestión de CSV de generación y consulta
			Entidad entidad = new Entidad();
			entidad.setIdentificador(entityId);
			InfoDocumentoCSVForm infoDocumentoForm = new InfoDocumentoCSVForm();
			infoDocumentoForm
					.setCodigoAplicacion(CodigosAplicacionesConstants.TRAMITACION_EXPEDIENTES_CODE);
			infoDocumentoForm.setDisponible(true);
			// La fecha de caducidad es null porque nunca caduca
			infoDocumentoForm.setFechaCaducidad(null);
			// Fecha del documento (la de la firma)
			// Fecha de la firma del PDF
			ServicioTiempos servicioTiempos = LocalizadorServicios.getServicioTiempos();
			infoDocumentoForm.setFechaCreacion(servicioTiempos.getCurrentDate());
			// Nombre del documento más extensión
			// y tipo Mime asociado
			infoDocumentoForm.setNombre(itemDoc.getString("NOMBRE") + ".pdf");
			infoDocumentoForm.setTipoMime(INFODOCUMENTO_CSV_TIPO_MIME);

			// Generar el CSV invocando al Servicio de Gestión de CSV
			infoDocumento = servicioGestionCSV.generarCSV(entidad,
					infoDocumentoForm);

			if (logger.isDebugEnabled()) {
				logger.debug("SigemSignConnector.addCodVerificacion: Generado el CSV: [" + infoDocumento.getCsv()
						+ "] para el documento: [" + infoDocumento.getId() + "].");
			}

			// Establecer el código de cotejo electrónico a partir del CSV
			itemDoc.set("COD_COTEJO", infoDocumento.getCsv());

			//Bloqueamos el documento para la edicion
			// itemDoc.set("BLOQUEO", DocumentLockStates.EDIT_LOCK);
			itemDoc.store(clientContext);

			return infoDocumento.getCsv();

		} catch (Exception e) {

			// Si se produce algún error
			// anular el CSV si ya fue generado
			if (infoDocumento != null) {
				anularCodVerificacion(infoDocumento.getCsv());
			}

			logger.error("Error en la firma del documento al generar el CSV", e);
			throw new ISPACException(e);
		}
	}
	
	
	/**
	 * Anula el código de verificación generado para el documento.
	 *
	 * @param codCotejo
	 */
	protected void anularCodVerificacion(String codCotejo) {

		if (codCotejo != null) {

			// Eliminar el CSV
			try {
				Entidad entidad = new Entidad();
				entidad.setIdentificador(getEntityId());

				ServicioGestionCSV servicioGestionCSV = LocalizadorServicios.getServicioGestionCSV();
				InfoDocumentoCSV infoDocumentoCSV = servicioGestionCSV.getInfoDocumentoByCSV(entidad, codCotejo);
				if (infoDocumentoCSV != null) {

					// Eliminar la información de CSV del documento
					servicioGestionCSV.deleteInfoDocumento(entidad, infoDocumentoCSV.getId());
				}
			} catch (Exception e) {
				logger.error("Error en la firma al eliminar el CSV", e);
			}
		}
	}

	
	
    private static String getEntityId() {
    	
    	String entityId = null;

		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		if (info != null) {
			entityId = info.getOrganizationId();
		}
    	
		return entityId;
    }
	
}