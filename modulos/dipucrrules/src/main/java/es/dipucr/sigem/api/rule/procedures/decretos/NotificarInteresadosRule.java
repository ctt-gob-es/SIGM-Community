package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Date;
import java.util.StringTokenizer;

import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.sgm.tram.sign.DipucrSignConnector;

public class NotificarInteresadosRule implements IRule {
	
	/**
	 * Ruta por defecto de la imagen de fondo del PDF.
	 */
	private static final String DEFAULT_PDF_BG_IMAGE_PATH = "firma/fondo.gif";
	
	/**
	 * Ruta por defecto del fichero con el texto de la banda lateral del PDF.
	 */
	private static final String DEFAULT_PDF_BG_DATA_PATH = "firma/datosFirmaRegistro.txt";
	
	public static final Logger logger = Logger.getLogger(NotificarInteresadosRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		ClientContext cct = (ClientContext) rulectx.getClientContext();
	    IInvesflowAPI invesFlowAPI = cct.getAPI();
	    IEntitiesAPI entitiesAPI = null;
		try {
			entitiesAPI = invesFlowAPI.getEntitiesAPI();
		} catch (ISPACException e2) {
        	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e2.getMessage(), e2);
		}
	    
	    String numExp = rulectx.getNumExp();
		int taskId = rulectx.getTaskId();
		
	    //Variables para el envío del correo
	    String nombreNotif = "";
	    String id_ext = "";
	    String nombreDoc ="Notificación";
	    String descripcionDoc = "Notificación";
	    Date fechaEnvio = new Date();
		boolean enviadoEmail = true;
		String emailNotif = "";
		String descripError = "";
		
		IItem participante = null;
		
		try{			
			//Sacamos los intervinientes que tienen dirección de correo electrónico
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL= 'INT'", "ID");
			
			if (participantes != null && participantes.toList().size() > 0){
				for (int i=0; i<participantes.toList().size(); i++){
					descripError = "";
					enviadoEmail = true;
					nombreDoc = "Notificación";
					descripcionDoc = "Notificación";
					
					participante = (IItem) participantes.toList().get(i);
					emailNotif = participante.getString("DIRECCIONTELEMATICA");
					nombreNotif = participante.getString("NOMBRE");	
					
					id_ext = participante.getString("ID_EXT");
					
					if(emailNotif != null && !emailNotif.equals("")){
						StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
						
						String sqlQueryDoc = "";
						if (id_ext != null && !id_ext.equals("0")){
							sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND DESTINO_ID = '"+id_ext+"'";
						}
						else if(nombreNotif != null && !nombreNotif.equals("")){
							sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND DESTINO = '"+nombreNotif+"'";
						}					
						
						if(!sqlQueryDoc.equals("")){
							IItemCollection	documentos = entitiesAPI.getDocuments(numExp, sqlQueryDoc, "");
							
							if (documentos == null || documentos.toList().size()==0){
								descripError = "No existe el documento de la Notificación.";
								enviadoEmail = false;
								DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc,descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
							}else if (documentos.toList().size() > 1 ) {
								descripError = "Hay más de un documento asociado al interesado.";
								enviadoEmail = false;
								DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc,descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
							}else if (documentos.toList().size() == 1) {
									try{
										// Fichero a adjuntar
										IItem doc = (IItem)documentos.iterator().next();
										String estadoFirma = doc.getString("ESTADOFIRMA");
										String nRegistro = doc.getString("NREG");
										String infoPagRde = doc.getString("INFOPAG_RDE");
	
										nombreDoc = doc.getString("NOMBRE");
										descripcionDoc = doc.getString("DESCRIPCION");
										
										String cContenido = "<br/>Adjunto se envía la Notificación del documento '"+descripcionDoc+"' para D./Dª. "+nombreNotif;
										String cAsunto= "[SIGEM] "+descripcionDoc;
	
										if(!estadoFirma.equals("02")){
											descripError = "El documento '"+descripcionDoc+"' no ha sido firmado;";
											enviadoEmail = false;
											DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc,descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
										}
										else if(nRegistro == null || nRegistro.equals("")){
											descripError = "No se ha registrado la salida del documento '"+descripcionDoc+"'";
											enviadoEmail = false;
											DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc,descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx );
										}
										else{
											logger.error("taskid = "+taskId+" numexp = "+numExp+" infopatrde="+infoPagRde);
											File file = DocumentosUtil.getFile(cct, infoPagRde, null, null);
											File fileSello = null;
											
											while (tokens.hasMoreTokens()) {
												String cCorreoDestino = tokens.nextToken();	
									        	if (!StringUtils.isEmpty(cCorreoDestino)) {
						        	
													//Sellamos el documento										
													String pathFileTemp = FileTemporaryManager.getInstance().put(file.getAbsolutePath(), ".pdf");
													String tipoRegistro = doc.getString("TP_REG");
													String numRegistro = doc.getString("NREG");
										  			String fechaRegistro = doc.getString("FREG");
										  			String departamento = doc.getString("ORIGEN");
										  			
													addGrayBand(file, pathFileTemp, infoPagRde, tipoRegistro, numRegistro, fechaRegistro, departamento);
													
													String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
										  			fileSello = new File(rutaFileName);
										  			
										  			//[dipucr-Felipe 3#213]
										  			MailUtil.enviarCorreo(rulectx, cCorreoDestino, cAsunto, cContenido, fileSello);
										  			
										        }//if correoDestino
										    }//while
				
											IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
											participanteAActualizar.set("DECRETO_NOTIFICADO", "Y");
											participanteAActualizar.store(cct);
																					
											//Insertamos el registro en la tabla dpcr_acuse_email
											DipucrCommonFunctions.insertarAcuseEmail(nombreNotif, fechaEnvio, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);								
											file.delete();
											fileSello.delete();
										}//else sí ha sido firmado y registrado
									}//try
									catch(SendFailedException e)
									{
										logger.error("Error en el envío. "+rulectx.getNumExp()+" nombreNotif "+nombreNotif+ "descripcion error "+descripError+" - "+e.getMessage(), e);
										descripError = "Error en el envío a D./Dª. "+nombreNotif+". ";
										enviadoEmail = false;
										DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc,descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx );
										throw new ISPACRuleException("Error en el envío. "+rulectx.getNumExp()+" nombreNotif "+nombreNotif+ "descripcion error "+descripError+" - "+e.getMessage(), e);
									}
									catch(AddressException e)
									{	
										logger.error("Error en el envío. "+rulectx.getNumExp()+" nombreNotif "+nombreNotif+ "descripcion error "+descripError+" - "+e.getMessage(), e);
										descripError = "Error en la dirección de correo '" + emailNotif + "' de D./Dª. "+nombreNotif+". ";
										enviadoEmail = false;
										DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc,descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
										throw new ISPACRuleException("Error en el envío. "+rulectx.getNumExp()+" nombreNotif "+nombreNotif+ "descripcion error "+descripError+" - "+e.getMessage(), e);
									}
									catch(Exception e){	
										logger.error("Error en el envío. "+rulectx.getNumExp()+" nombreNotif "+nombreNotif+ "descripcion error "+descripError+" - "+e.getMessage(), e);
										descripError = "Error en el envío a D./Dª. "+nombreNotif+". ";
										enviadoEmail = false;	
										DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc,descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
										throw new ISPACRuleException("Error en el envío. "+rulectx.getNumExp()+" nombreNotif "+nombreNotif+ "descripcion error "+descripError+" - "+e.getMessage(), e);
									}
							}//else if	
						}//if sqlQueryDoc
						else{
							descripError = "El interesado "+nombreNotif+" no está validado.";
							enviadoEmail = false;
							DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc,descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
						}
					}
					else{
						descripError = "El email está vacío";
						enviadoEmail = false;
						DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc,descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
					}
				}//for
			}//if participantes
		}//Try		
		catch(Exception e) {
			logger.error("Error en el envío. "+rulectx.getNumExp()+" nombreNotif "+nombreNotif+ "descripcion error "+descripError+" - "+e.getMessage(), e);
        	throw new ISPACRuleException("Error en el envío. "+rulectx.getNumExp()+" nombreNotif "+nombreNotif+ "descripcion error "+descripError+" - "+e.getMessage(), e);
        }
		return new Boolean(true);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	void addGrayBand(File file, String pathFileTemp, String infoPagRDE, String tipoRegistro, String numRegistro, String fechaRegistro, String departamento)
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

			Image imagen = this.createBgImage();
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
				getImagen(over, margen, margen, n, i, tipoRegistro, numRegistro, fechaRegistro, departamento);				
					
				document.newPage();
			}
		
			document.close();
		
		} catch (ISPACException e) {
			logger.error("Error al añadir la banda lateral al PDF - "+e.getMessage(), e);
        	throw new ISPACRuleException("Error al añadir la banda lateral al PDF - "+e.getMessage(), e);
        
		} catch (Exception e) {
			logger.error("Error al añadir la banda lateral al PDF - "+e.getMessage(), e);
        	throw new ISPACRuleException("Error al añadir la banda lateral al PDF - "+e.getMessage(), e);
        
		}
	}
	
	protected Image createBgImage() throws ISPACException {

		// Ruta relativa de la imagen de fondo
		String imagePath = ISPACConfiguration.getInstance().getProperty(DEFAULT_PDF_BG_IMAGE_PATH);
		if (StringUtils.isBlank(imagePath)) {
			imagePath = DEFAULT_PDF_BG_IMAGE_PATH;
		}

		try {
			
			// Ruta absoluta de la imagen de fondo
			String imageFullPath = ConfigurationHelper.getConfigFilePath(imagePath);
			if (logger.isInfoEnabled()) {
				logger.info("Imagen de fondo del PDF: " + imageFullPath);
			}
			
			// Construir la imagen de fondo
			Image image = Image.getInstance(imageFullPath);
			image.setAbsolutePosition(200F, 400F);
			return image;
			
		} catch (Exception e) {
			logger.error("Error al leer la imagen de fondo del PDF firmado: " + imagePath+ " - "+e.getMessage(), e);
			throw new ISPACException("Error al leer la imagen de fondo del PDF firmado: " + imagePath+ " - "+e.getMessage(), e);
		}
	}
	
	protected void getImagen(PdfContentByte pdfContentByte,
			float margen, float x, int numberOfPages, int pageActual, String tipoReg, String numReg, String fecReg, String departamento) throws ISPACException {
		
		try {
		
			String font = ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.FONT_BAND);
			String encoding = ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.ENCODING_BAND);
			float fontSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.FONTSIZE_BAND));
			//float positionY = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(MARGIN_BAND));
			
			BaseFont bf = BaseFont.createFont(font, encoding, false);
			pdfContentByte.beginText();
			pdfContentByte.setFontAndSize(bf, fontSize);

			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(this.getDataFile()));
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
			
		} catch (Exception e) {
			logger.error("Error al componer la imagen de la banda lateral - "+e.getMessage(), e);
			throw new ISPACException("Error al componer la imagen de la banda lateral - "+e.getMessage(), e);
		}
	}
	
	protected File getDataFile() throws ISPACException {
		
		// Ruta relativa del texto de la banda lateral
		String dataPath = ISPACConfiguration.getInstance().getProperty(DEFAULT_PDF_BG_DATA_PATH);
		if (StringUtils.isBlank(dataPath)) {
			dataPath = DEFAULT_PDF_BG_DATA_PATH;
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
		logger.warn("dataFullPath: " + dataFullPath);
		return new File(dataFullPath);
	}
}