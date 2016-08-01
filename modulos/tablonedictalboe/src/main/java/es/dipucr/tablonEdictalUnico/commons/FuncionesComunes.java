package es.dipucr.tablonEdictalUnico.commons;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.encoding.Base64;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XText;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;

import es.boe.www.ServicioNotificaciones.Aviso;
import es.boe.www.ServicioNotificaciones.ListaAnuncios;
import es.boe.www.ServicioNotificaciones.ListaAvisos;
import es.boe.www.ServicioNotificaciones.ListaErrores;
import es.boe.www.ServicioNotificaciones.Respuesta;
import es.boe.www.ServicioNotificaciones.ServicioNotificacionesProxy;
import es.boe.www.ServicioNotificaciones.ServicioNotificacionesSOAPStub;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.tablonEdictalUnico.firma.ClientHandler;
import es.dipucr.tablonEdictalUnico.objetos.InformacionTablonEdictal;
import es.dipucr.tablonEdictalUnico.objetos.RecaudacionFichero;
import es.dipucr.tablonEdictalUnico.quartz.bean.Entidad;
import es.dipucr.tablonEdictalUnico.quartz.bean.TablonEdictalBoeDatos;
import es.dipucr.tablonEdictalUnico.quartz.dao.AvisoDAO;
import es.dipucr.tablonEdictalUnico.quartz.dao.GenericoDAO;
import es.dipucr.tablonEdictalUnico.quartz.dao.TablonEdictalBoeDatosDAO;
import es.dipucr.tablonEdictalUnico.xml.Anuncio;
import es.dipucr.tablonEdictalUnico.xml.Anuncios;
import es.dipucr.tablonEdictalUnico.xml.Emisor;
import es.dipucr.tablonEdictalUnico.xml.Envio;
import es.dipucr.tablonEdictalUnico.xml.InfPub;
import es.dipucr.tablonEdictalUnico.xml.Materia;
import es.dipucr.tablonEdictalUnico.xml.Materias;
import es.dipucr.tablonEdictalUnico.xml.Metadatos;
import es.dipucr.tablonEdictalUnico.xml.NodoEmisor;
import es.dipucr.tablonEdictalUnico.xml.NodoRemitente;
import es.dipucr.tablonEdictalUnico.xml.Notificado;
import es.dipucr.tablonEdictalUnico.xml.Notificados;
import es.dipucr.tablonEdictalUnico.xml.P;
import es.dipucr.tablonEdictalUnico.xml.PieFirma;
import es.dipucr.tablonEdictalUnico.xml.Procedimiento;
import es.dipucr.tablonEdictalUnico.xml.Remitente;
import es.dipucr.tablonEdictalUnico.xml.Table;
import es.dipucr.tablonEdictalUnico.xml.Tbody;
import es.dipucr.tablonEdictalUnico.xml.Td;
import es.dipucr.tablonEdictalUnico.xml.Texto;
import es.dipucr.tablonEdictalUnico.xml.Th;
import es.dipucr.tablonEdictalUnico.xml.Thead;
import es.dipucr.tablonEdictalUnico.xml.Thead.Tr;
import es.dipucr.tablonEdictalUnico.xml.TipoContenido;

public class FuncionesComunes {
	
	public static final Logger logger = Logger.getLogger(ServicioNotificacionesSOAPStub.class);
	

	public static void crearDocInformacionEnvioTEU(OpenOfficeHelper ooHelper, IRuleContext rulectx, Respuesta respuesta)
			throws ISPACRuleException {
		try {
			// ------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			// ------------------------------------------------------------------
			if (ooHelper != null)
				ooHelper.dispose();

			// Creación del documento 'Información de Notificación y Traslado'
			String filePathDoc = FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance().newFileName(".pdf");
			File ffilePathDoc = new File(filePathDoc);
			com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
			document.setMargins(document.leftMargin(), document.rightMargin(), document.topMargin(), 27);

			PdfWriter writer = PdfCopy.getInstance(document,
					new FileOutputStream(filePathDoc));

			writer.setPdfVersion(PdfCopy.VERSION_1_4);
			writer.setViewerPreferences(PdfCopy.PageModeUseOutlines);

			document.open();
			DocumentosUtil.nuevaPagina(document, true, true, true, PageSize.A4);

			Paragraph parrafo = new Paragraph();
			Font fuente = new Font(Font.TIMES_ROMAN);
			fuente.setStyle(Font.BOLD);
			fuente.setSize(15);
			StringBuffer texto = new StringBuffer("\n\n\n\n\n");
			texto.append("INFORMACIÓN SOBRE LA PUBLICACIÓN EN EL TABLÓN EDICTAL DEL BOE");
			texto.append("\n\n\n\n\n");

			Phrase frase = new Phrase(texto.toString(), fuente);
			parrafo.add(frase);

			PdfPCell cell = new PdfPCell();
			cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
			cell.setBackgroundColor(Color.GRAY);

			texto = new StringBuffer("");

			texto.append("RESPUESTA");
			texto.append("\n");
			texto.append(" - Identificador de envío: " + respuesta.getIdEnvio());
			texto.append("\n");
			texto.append(" - Fecha/Hora en la que se ha registrado el envío: " + respuesta.getFecha());
			texto.append("\n\n");
			if(respuesta.getResultado()!=null){
				texto.append("RESULTADO DEL ENVÍO:");
				texto.append("\n");
				texto.append("- Código del resultado. "+ respuesta.getResultado().getCodigo());
				texto.append("\n");
				texto.append("- Descripción del resultado. "+respuesta.getResultado().getDescripcion());
				texto.append("\n\n");
			}
			
			ListaAnuncios listaAnuncios = respuesta.getAnuncios();
			
			if(listaAnuncios!=null){
				es.boe.www.ServicioNotificaciones.Anuncio[] anuncios = listaAnuncios.getAnuncio();
				for (int i = 0; anuncios != null && i < anuncios.length; i++) {
					texto.append(i+". ANUNCIO");
					texto.append("\n");
					es.boe.www.ServicioNotificaciones.Anuncio anuncio = anuncios[i];
					if(anuncio.getIdBoe()!=null){
						texto.append(" - Identificador BOE. "+anuncio.getIdBoe());
						texto.append("\n");
					}
					if(anuncio.getEstadoBoe()!=null){
						texto.append(" - Estado del anuncio. "+anuncio.getEstadoBoe());
						texto.append("\n");
					}
					if(anuncio.getEstadoBoe()!=null){
						texto.append("Un anuncio puede estar en los siguientes estados:");
						texto.append("\n");
						texto.append(" * PENDIENTE. El anuncio ha sido recibido pero está pendiente de firma.");
						texto.append("\n");
						texto.append(" * ACEPTADO. El anuncio ha sido recibido y firmado. Está listo para su tramitación.");
						texto.append("\n");
						texto.append(" * RECIBIDO. El anuncio ha entrado en el sistema interno de publicación de la AEBOE.");
						texto.append("\n");
						texto.append(" * ANULADO. El anuncio ha sido anulado.");
						texto.append("\n");
						texto.append(" * PUBLICADO. El anuncio ha sido publicado en el BOE.");
						texto.append("\n\n\n\n");
					}
					if(anuncio.getEstadoBoe()!=null && !anuncio.getEstadoBoe().equals("PUBLICADO")){
						texto.append("Los campos Número de BOE, Código seguro de verificación electrónica, URL para localizar el anuncio en la sede electrónica de la AEBOE y "
								+ "Fecha de publicación del anuncio en el boletín no aparecerán hasta que el anuncio sea PUBLICADO");
						texto.append("\n");
					}
					if(anuncio.getNbo()!=null){
						texto.append(" - Número de BOE en el que se ha publicado. "+anuncio.getNbo());
						texto.append("\n");
					}
					if(anuncio.getCve()!=null){
						texto.append(" - Código seguro de verificación electrónica. \n"+anuncio.getCve());
						texto.append("\n");
					}
					if(anuncio.getFechaPub()!=null){
						texto.append(" - Fecha de publicación del anuncio en el boletín. "+anuncio.getFechaPub());
						texto.append("\n");
					}
					if(anuncio.getUrl()!=null){
						texto.append(" - URL para localizar el anuncio en la sede electrónica de la AEBOE. "+anuncio.getUrl());
						texto.append("\n");
						String url = anuncio.getUrl().toString();
						URL ficheroUrl = new URL(url);
						URLConnection urlCon = ficheroUrl.openConnection();
						InputStream is = urlCon.getInputStream();
						String fileAnuncio = FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance().newFileName(".pdf");
						FileOutputStream fos = new FileOutputStream(fileAnuncio);
						// buffer para ir leyendo.
						byte [] array = new byte[1000];
						 
						// Primera lectura y bucle hasta el final
						int leido = is.read(array);
						while (leido > 0) {
						   fos.write(array,0,leido);
						   leido=is.read(array);
						}
						 
						// Cierre de conexion y fichero.
						is.close();
						fos.close();
					}
					
					
					ListaAvisos[] vlistaAviso = anuncio.getAvisos();
					ListaErrores[] vlistaError = anuncio.getErrores();
					for (int j = 0; vlistaError!=null && j < vlistaError.length; j++) {
						ListaErrores listaError = vlistaError[i];
						es.boe.www.ServicioNotificaciones.Error[] verror = listaError.getError();
						for (int k = 0; k < verror.length; k++) {
							es.boe.www.ServicioNotificaciones.Error error = verror[k];
							texto.append("Error "+k+": ");
							texto.append("\n");
							texto.append(" - Código. "+error.getCodigo());
							texto.append("\n");
							texto.append(" - Descripción. "+error.getDescripcion());
							texto.append("\n");
						}
					}
					for (int j = 0; vlistaAviso!=null && j < vlistaAviso.length; j++) {
						ListaAvisos listaAviso = vlistaAviso[i];
						Aviso[] vaviso = listaAviso.getAviso();
						for (int k = 0; k < vaviso.length; k++) {
							Aviso aviso = vaviso[k];
							texto.append("Avisos "+k+": ");
							texto.append("\n");
							texto.append(" - Código. "+aviso.getCodigo());
							texto.append("\n");
							texto.append(" - Descripción. "+aviso.getDescripcion());
							texto.append("\n");
						}
					}
				}
			}
			
			
			
			Font font = new Font();
			font.setStyle(Font.NORMAL);
			font.setSize(12);
			font.setFamily("Arial");
			
			Phrase fraseParrafo = new Phrase(texto.toString(), font);
			parrafo.add(fraseParrafo);
			

			document.add(parrafo);



			document.close();

			int idTpdocNot = DocumentosUtil.getIdTipoDocByCodigo(cct, "InfAnuncTablEdic");
			String sTpdoc = DocumentosUtil.getTipoDocNombreByCodigo(cct, "InfAnuncTablEdic");
			IItem entityDocument = DocumentosUtil.generaYAnexaDocumento(cct, rulectx.getTaskId(), idTpdocNot, sTpdoc, ffilePathDoc,	Constants._EXTENSION_PDF);
			entityDocument.store(cct);
			
			if(ffilePathDoc!=null && ffilePathDoc.isFile()){
				ffilePathDoc.delete();
			}
			

		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ", e);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ", e);
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ", e);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ", e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ", e);
		}
	}
	
	
	/**
	 * @param _call
	 * @throws AxisFault
	 */
	public static void imprimirErrorEnvio(org.apache.axis.client.Call _call) throws AxisFault {
		String requestXML = _call.getMessageContext().getRequestMessage().getSOAPPartAsString();
		String responseXML = _call.getMessageContext().getResponseMessage().getSOAPPartAsString();
		logger.warn("REQUEST");
		logger.warn(requestXML);
		logger.warn("RESPONSE");
		logger.warn(responseXML);
	}
	
	
	/**
	 * @param _call
	 */
		
	public static void firmarPeticion(Call _call){
		try {
            Properties configuration = new Properties();
            configuration.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("securityConfiguration.properties"));
            // Creacion del manejador que securizara la peticion
            ClientHandler sender = new ClientHandler(configuration);
			
			SOAPService soapService=new SOAPService(sender, null, null);
			soapService.setUse(org.apache.axis.constants.Use.LITERAL);
			soapService.getServiceDescription().setTypeMapping(_call.getTypeMapping());
			soapService.getTypeMappingRegistry().register("", _call.getTypeMapping());
			_call.setSOAPService(soapService);

            
        } catch (Exception e) {
        	logger.error("Error al firmar la petición."+e.getMessage(), e);
        }
		
	}
	
	public static String creacionMensaje(Envio envio) throws ISPACRuleException {
		String sEnvio = "";

		try {
			sEnvio = obtenerXMLString(envio);
			logger.warn("XML: "+sEnvio);
			sEnvio = Base64.encode(sEnvio.getBytes());
		} catch (Exception e) {
			logger.error("Error al crear el mensaje xml ."+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}		
		return sEnvio;
	}

	
	private static String obtenerXMLString(es.dipucr.tablonEdictalUnico.xml.Envio envio) throws Exception {
		ByteArrayOutputStream osXml= new ByteArrayOutputStream();
		try{
			JAXBContext jaxbContext = JAXBContext.newInstance("es.dipucr.tablonEdictalUnico.xml");
			Marshaller marshaller = jaxbContext.createMarshaller();
			//indicamos que queremos formateada nuestra salida (con enters y tabs)
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			//indicamos el objeto que escribiremos a XML y la salida (puede ser un objeto tipo FILE)
			marshaller.marshal(envio, osXml);				
			
		} catch (JAXBException e) {
			logger.error("Error. "+e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return osXml.toString();
	}
	public static File obtenerXMLFile(es.dipucr.tablonEdictalUnico.xml.Envio envio) throws ISPACRuleException {
		File fileXML = null;
		try{
			String fileName = FileTemporaryManager.getInstance().newFileName(".xml");
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			
			fileXML = new File(fileName);
			//JAXBContext jaxbContext = JAXBContext.newInstance(Libro.class);
			JAXBContext jaxbContext = JAXBContext.newInstance("es.dipucr.tablonEdictalUnico.xml");
			Marshaller marshaller = jaxbContext.createMarshaller();
			//indicamos que queremos formateada nuestra salida (con enters y tabs)
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//indicamos el objeto que escribiremos a XML y la salida (puede ser un objeto tipo FILE)
			marshaller.marshal(envio, fileXML);
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		 } catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return fileXML;
	}
	
	public static es.dipucr.tablonEdictalUnico.xml.Envio construccionEnvio (IRuleContext rulectx, RecaudacionFichero recaudacion) throws ISPACRuleException{
		es.dipucr.tablonEdictalUnico.xml.Envio envio = new es.dipucr.tablonEdictalUnico.xml.Envio();
		
		
		try {
			envio.setVersion(Constantes.version);
			Anuncios anuncios = new Anuncios();

			String entidad = EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext());
			//---------2.1
			Remitente remitente = new Remitente();
			
			if(entidad.equals("005") || entidad.equals("001")){
				Iterator <IItem> itDir = ConsultasGenericasUtil.queryEntities(rulectx, "TABLON_EDICTAL_BOE_DIR3", "1=1 ORDER BY ID ASC");
				while(itDir.hasNext()){
					NodoRemitente nodoRemitentenivel = new NodoRemitente();
					IItem itemDir = itDir.next();
					if(itemDir.getString("VALOR")!=null) nodoRemitentenivel.setIdDir3(itemDir.getString("VALOR"));
					if(itemDir.getInt("ID")>0) nodoRemitentenivel.setNivel(itemDir.getInt("ID"));
					if(itemDir.getString("SUSTITUTO")!=null) nodoRemitentenivel.setValue(itemDir.getString("SUSTITUTO"));
					remitente.getNodoRemitente().add(nodoRemitentenivel);
				}
				//2.1.1
				/*NodoRemitente nodoRemitentenivel1 = new NodoRemitente();
				NodoRemitente nodoRemitentenivel2 = new NodoRemitente();
				//NodoRemitente nodoRemitentenivel3 = new NodoRemitente();
				
				nodoRemitentenivel1.setIdDir3(Constantes.DIR3_NIVEL1.ID);
				nodoRemitentenivel1.setNivel(Constantes.DIR3_NIVEL1.NIVEL);
				nodoRemitentenivel1.setValue(Constantes.DIR3_NIVEL1.NAME);
				
				nodoRemitentenivel2.setIdDir3(Constantes.DIR3_NIVEL2.ID);
				nodoRemitentenivel2.setNivel(Constantes.DIR3_NIVEL2.NIVEL);
				nodoRemitentenivel2.setValue(Constantes.DIR3_NIVEL2.NAME);
				//nodoRemitentenivel3.setIdDir3("LA0006584");
				//nodoRemitentenivel3.setNivel(3);
				//nodoRemitentenivel3.setValue("SECRETARIA");
				
				remitente.getNodoRemitente().add(nodoRemitentenivel1);			
				remitente.getNodoRemitente().add(nodoRemitentenivel2);*/
			}			
			
			//remitente.getNodoRemitente().add(nodoRemitentenivel3);
			anuncios.setRemitente(remitente);
			
			InformacionTablonEdictal informacionTE = obtenerDatosInformacionTablonEdictal(rulectx);
			
			//Sumo un día a la fecha actual

			//GregorianCalendar ahora = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			//ahora.set(GregorianCalendar.DATE,ahora.get(GregorianCalendar.DATE)+1); 
			if(informacionTE.getFechaPublicacion()!=null){
				XMLGregorianCalendar fechaPublicacion = DatatypeFactory.newInstance().newXMLGregorianCalendarDate
						(informacionTE.getFechaPublicacion().get(Calendar.YEAR), informacionTE.getFechaPublicacion().get(Calendar.MONTH)+1, 
								informacionTE.getFechaPublicacion().get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
				//---------2.2
				anuncios.setFechaPub(fechaPublicacion);
			}
			
			
			//---------2.3 [1..1]
			InfPub informacionPublicacion = new InfPub();
			//2.3.2 [0..1]
			informacionPublicacion.setEmail(informacionTE.getEmail());
			//2.3.1Dirección del servicio web a la que se informará de la fecha de publicación de los anuncios. [1..1]
			//informacionPublicacion.setUrlSW(value);
			anuncios.setInfPub(informacionPublicacion);
			
			
			//---------2.4 [1..*]
			Anuncio anuncio = new Anuncio();
			//**2.4.1 [1..1]
			Emisor emisor = new Emisor();
			
			if(entidad.equals("005") || entidad.equals("001")){
				Iterator <IItem> itDir = ConsultasGenericasUtil.queryEntities(rulectx, "TABLON_EDICTAL_BOE_DIR3", "1=1 ORDER BY ID ASC");
				int nivel = 1;
				while(itDir.hasNext()){
					NodoEmisor nodoEmisornivel = new NodoEmisor();
					IItem itemDir = itDir.next();
					if(itemDir.getString("VALOR")!=null) nodoEmisornivel.setIdDir3(itemDir.getString("VALOR"));
					if(itemDir.getInt("ID")>0) nodoEmisornivel.setNivel(itemDir.getInt("ID"));
					if(itemDir.getString("SUSTITUTO")!=null) nodoEmisornivel.setValue(itemDir.getString("SUSTITUTO"));
					emisor.getNodoEmisor().add(nodoEmisornivel);
					nivel ++;
				}
				//Añado el departamento
				itDir = ConsultasGenericasUtil.queryEntities(rulectx, "TABLON_EDICTAL_BOE_DATOS", "NUMEXP='"+rulectx.getNumExp()+"'");
				while(itDir.hasNext()){
					NodoEmisor nodoEmisornivel = new NodoEmisor();
					IItem itemDir = itDir.next();
					if(itemDir.getString("IDENT_DEPART_DIR3")!=null) nodoEmisornivel.setIdDir3(itemDir.getString("IDENT_DEPART_DIR3"));
					nodoEmisornivel.setNivel(nivel);
					if(itemDir.getString("NAME_DEPART_DIR3")!=null) nodoEmisornivel.setValue(itemDir.getString("NAME_DEPART_DIR3"));
					emisor.getNodoEmisor().add(nodoEmisornivel);
				}
				
				
				// 2.4.1.2 [1..*]
				/*NodoEmisor nodoEmisornivel1 = new NodoEmisor();
				NodoEmisor nodoEmisornivel2 = new NodoEmisor();
				NodoEmisor nodoEmisornivel3 = new NodoEmisor();
				nodoEmisornivel1.setIdDir3(Constantes.DIR3_NIVEL1.ID);
				nodoEmisornivel1.setNivel(Constantes.DIR3_NIVEL1.NIVEL);
				nodoEmisornivel1.setValue(Constantes.DIR3_NIVEL1.NAME);
				nodoEmisornivel2.setIdDir3(Constantes.DIR3_NIVEL2.ID);
				nodoEmisornivel2.setNivel(Constantes.DIR3_NIVEL2.NIVEL);
				nodoEmisornivel2.setValue(Constantes.DIR3_NIVEL2.NAME);
				nodoEmisornivel3.setIdDir3("LA0006584");
				nodoEmisornivel3.setNivel(3);
				nodoEmisornivel3.setValue("SECRETARIA GENERAL");
				emisor.getNodoEmisor().add(nodoEmisornivel1);			
				emisor.getNodoEmisor().add(nodoEmisornivel2);
				emisor.getNodoEmisor().add(nodoEmisornivel3);*/
			}
			
			
			anuncio.setEmisor(emisor);
			
			
			//**2.4.2 [1..1]
			Metadatos metadatos = new Metadatos();
			//2.4.2.1 [0..1]
			metadatos.setId(rulectx.getNumExp());
			//2.4.2.2 [1..1]
			//E: Publicación en extracto. Publicación en extracto (cuando el anuncio no contiene el contenido del acto administrativo
			//a notificar, sino únicamente la identificación del interesado y del procedimiento)
			//I: Publicación íntegra. Publicación íntegra (cuando en el texto del anuncio se recoge 
			//completo el contenido del acto administrativo objeto de notificación)
			metadatos.setFormPub(informacionTE.getFormaPublicacion());
			//2.4.2.3 [1..1]
			//N: No incluye ningún dato de carácter personal.
			//S: Incluye datos de carácter personal.
			if(informacionTE.getDatosPersonales().equals("Si") || informacionTE.getDatosPersonales().equals("SI")){
				metadatos.setDatosPersonales(Constantes.DATOS_PERSONALES.SI);
			}
			else{
				metadatos.setDatosPersonales(Constantes.DATOS_PERSONALES.NO);
			}
			
			//2.4.2.4 [0..1]
			if(informacionTE.getTipoAnuncio()!=null && informacionTE.getTipoAnuncio().length()>3){
				Materias materias = new Materias();
				//2.4.2.4.1 [1..*] 
				Materia materia = new Materia();
				materia.setIdMat(informacionTE.getTipoAnuncio().substring(0, 3));
				materia.setValue(informacionTE.getTipoAnuncio());
				materias.getMateria().add(materia);
				metadatos.setMaterias(materias);
			}
			
			// 2.4.2.5 [0..1]
			//El valor será “S” si el anuncio debe publicarse conforme a lo dispuesto en el artículo 112 de la Ley 58/2003 (Ley General Tributaria).
			if(informacionTE.getLgt().equals("Si") || informacionTE.getLgt().equals("SI")){
				metadatos.setLgt(Constantes.DATOS_PERSONALES.SI);
			}
			
			//2.4.2.6 Identificación del procedimiento. [0..1]
			Procedimiento procedimiento = new Procedimiento();
			procedimiento.setPlural(Constantes.PLURALIDAD.NO);
			procedimiento.setValue(informacionTE.getProcedimiento());
			metadatos.setProcedimiento(procedimiento);
			//2.4.2.7 [0..1]
			//Notificados notificados = new Notificados();
			//2.4.2.7.1 [1..*]
			/*Notificado notificado = new Notificado();
			notificado.setId("05695305E");
			notificado.setTipId("NIF");
			notificado.setValue("Teresa Carmona González");
			notificados.getNotificado().add(notificado);*/
			metadatos.setNotificados(informacionTE.getNotificados());
			
			
			//**2.4.2.8
			TipoContenido tipoContenido = new TipoContenido();
			//2.4.2.8.1 [1..1]
			Texto texto = new Texto();
			//2.4.2.8.1.1 P
			/**
			 * Texto del anuncio. Incluirá de forma obligatoria un atributo content-type (tipo de dato string) con el valor 'application/xml'
			 * El nodo texto estará formado por dos tipos de nodos que pueden repetirse tantas veces como sea necesario: párrafos (p) y 
			 * tablas (table). El anuncio debe contener al menos un elemento párrafo.
			 * **/
			texto.setContentType("application/xml");
			//2.4.2.8.1.2 table
			/*
			 * Este atributo puede tomar los siguientes valores:
			 * - parrafo: Párrafo por defecto.
			 * - titulo: Párrafo centrado con un tipo de letra mayor que el del párrafo por defecto.
			 * - pieFirma: El elemento no tendrá contenido alguno. Representa la posición donde se incorporá el texto del elemento pieFirma. 
			 *   De no incluirse, el pie de firma irá al final del texto.
			 * - page-break: El elemento no tendrá contenido alguno. Fuerza un salto de página a partir de este elemento.
			 * **/
			P titulo = new P();
			titulo.setClazz("titulo");
			titulo.getContent().add(informacionTE.getProcedimiento());
			texto.getPOrTable().add(titulo);
			
			Vector<String> parrafos = informacionTE.getParrafos();
			for (int i = 0; i < parrafos.size(); i++) {
				String sbParrafo = parrafos.get(i);
				if(!sbParrafo.equals("TABLA")){
					P parrafo = new P();
					parrafo.setClazz("parrafo");
					parrafo.getContent().add(sbParrafo);
					texto.getPOrTable().add(parrafo);	
				}
				else{
					if(!recaudacion.getRuta().equals("")){
						if(recaudacion.isRecaudacionNueva() || recaudacion.isRecaudacionFicheroZona() || recaudacion.isRecaudacionSWALExpedientes()){
							Notificados notificados = new Notificados();
							Table table = obtenerTablaRecaudacionNueva(recaudacion, notificados);
							metadatos.setNotificados(notificados);
							String [] nombreTabla = recaudacion.getNombreTabla().split(".xml");
							String nombre = "";
							if(nombreTabla.length>0){
								nombre = nombreTabla[0];
							}
							table.setCaption(nombre);
							texto.getPOrTable().add(table);
							
						}
						else{
							/*if(recaudacion.isRecaudacionFicheroZona()){
								obtenerTablaRecaudacionFicheroZona(texto, recaudacion.getRuta());
							}
							else{*/
								if(recaudacion.isFicheroTexto()){
									obtenerTextoDocumento(texto, recaudacion.getRuta());
								}
								else{
									obtenerTablaRecaudacionVieja(texto, recaudacion.getRuta());
								}
							//}
						}
					}			
					
				}
				
			}
			
			anuncio.setMetadatos(metadatos);
			
			tipoContenido.setTexto(texto);
			//2.4.2.8.2 [1..1]
			PieFirma pieFirma = new PieFirma();
			//2.4.2.8.2.1 [1..1]
			pieFirma.setLugar(informacionTE.getLugar());
			//2.4.2.8.2.2 [1..1]
			XMLGregorianCalendar fechaFirma = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(
					informacionTE.getFechaFirma().get(Calendar.YEAR), informacionTE.getFechaFirma().get(Calendar.MONTH)+1, 
					informacionTE.getFechaFirma().get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);	
			
			pieFirma.setFecha(fechaFirma);
			//2.4.2.8.2.3 [1..1] Cargo y nombre y dos apellidos del firmante
			pieFirma.setFirmante(informacionTE.getCargoNombre());
			tipoContenido.setPieFirma(pieFirma);
			anuncio.setContenido(tipoContenido);
			
			anuncios.getAnuncio().add(anuncio);
			
			
			envio.setAnuncios(anuncios);
		} catch (DatatypeConfigurationException e) {
			logger.error(e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return envio;
		
	}


	private static void obtenerTextoDocumento(Texto texto, String ruta) throws ISPACException {
		try{
			OpenOfficeHelper ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + ruta);
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
		    XTextRange inicio = xText.getStart();
		    StringBuffer textoSB = new StringBuffer(inicio.getText().getString());
		    
		    String[] vTexto = textoSB.toString().split("\\n");
		    logger.warn("INICIO");
		    for (int i = 1; i < vTexto.length; i++) {
		    	//logger.warn(vTexto[i]);
		    	P parrafo = new P();
				parrafo.setClazz("parrafo");
				parrafo.getContent().add(vTexto[i]);
				texto.getPOrTable().add(parrafo);
			}		    
		    
		}catch(Exception e){
			logger.warn("Error al leer el fichero. "+ruta+" ."+e.getMessage(), e);
			throw new ISPACException(e);
		}
	}


	private static void obtenerTablaRecaudacionFicheroZona(Texto texto,	String ruta) throws ISPACException {
		try{
			OpenOfficeHelper ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + ruta);
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
		    XTextRange inicio = xText.getStart();
		    StringBuffer textoSB = new StringBuffer(inicio.getText().getString());
		    
		    String[] vTexto = textoSB.toString().split("\\n");
		    logger.warn("INICIO");
		    if(vTexto!=null){
		    	Table table = new Table();
		    	Tbody cuerpoTabla = new Tbody();
			    Thead cabeceraTabla = new Thead();
				Tr filaCabecera = new Tr();
				Th celdaCabecera = new Th();
				celdaCabecera.setValue(vTexto[0]);
				filaCabecera.getTh().add(celdaCabecera);
				cabeceraTabla.getTr().add(filaCabecera);
				table.setThead(cabeceraTabla);
			    for (int i = 1; i < vTexto.length; i++) {
			    	logger.warn(vTexto[i]);
			    	es.dipucr.tablonEdictalUnico.xml.Tbody.Tr filaTabla = new es.dipucr.tablonEdictalUnico.xml.Tbody.Tr();
			    	Td celdaTabla = new Td();
				    celdaTabla.setValue(vTexto[i]+"\n");
					filaTabla.getTd().add(celdaTabla);
					cuerpoTabla.getTr().add(filaTabla);
					table.setTbody(cuerpoTabla);
				}
			    texto.getPOrTable().add(table);
		    }		    
		    
		}catch(Exception e){
			logger.warn("Error al leer el fichero. "+ruta+" ."+e.getMessage(), e);
			throw new ISPACException(e);
		}
		
	}


	private static void obtenerTablaRecaudacionVieja(Texto texto, String rutaFicheroTabla) throws ISPACRuleException {
		
		try {
		    
				FileReader fileRea = new FileReader(new File(rutaFicheroTabla));
				BufferedReader readbuffer = new BufferedReader(fileRea);			
				String strRead = "";

				
				boolean titulo = true;
				boolean bCabeceraTabla = false;
				Table table = new Table();
				Tbody cuerpoTabla = new Tbody();
				while (!((strRead = readbuffer.readLine()) == null)) {					
					if(titulo && !bCabeceraTabla){
						table = new Table();
						String tituloTabla = strRead;
						strRead = readbuffer.readLine();
						table.setCaption(tituloTabla+"\n"+strRead);
						titulo=false;
						bCabeceraTabla = true;
					}
					else{
						if(!titulo && bCabeceraTabla){
							bCabeceraTabla = false;
							Thead cabeceraTabla = new Thead();
							Tr filaCabecera = new Tr();
							Th celdaCabecera = new Th();
							celdaCabecera.setValue(strRead);
							filaCabecera.getTh().add(celdaCabecera);
							cabeceraTabla.getTr().add(filaCabecera);
							table.setThead(cabeceraTabla);
							
						}
						else{					
							if(strRead.equals(" ")){
								titulo = true;
								bCabeceraTabla = false;
								texto.getPOrTable().add(table);
								cuerpoTabla = new Tbody();
							}
							else{
									
								es.dipucr.tablonEdictalUnico.xml.Tbody.Tr filaTabla = new es.dipucr.tablonEdictalUnico.xml.Tbody.Tr();
							    Td celdaTabla = new Td();
							    celdaTabla.setValue(strRead+"\n");
								filaTabla.getTd().add(celdaTabla);
								cuerpoTabla.getTr().add(filaTabla);
								table.setTbody(cuerpoTabla);
							}							
						}						
					}
					
				}
				 
				readbuffer.close();
				fileRea.close();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
	}


	private static Table obtenerTablaRecaudacionNueva(RecaudacionFichero recaudacionFichero, Notificados notificados) throws ISPACRuleException {
		Table table = new Table();
		try {		
			 DocumentBuilderFactory fábricaCreadorDocumento = DocumentBuilderFactory.newInstance();
			    DocumentBuilder creadorDocumento = fábricaCreadorDocumento.newDocumentBuilder();
			    Document documento = creadorDocumento.parse(recaudacionFichero.getRuta());
			    //Obtener el elemento raíz del documento
			    Element raiz = documento.getDocumentElement();

			    //Obtener la lista de nodos que tienen etiqueta "EMPLEADO"
			    NodeList listaEmpleados = raiz.getElementsByTagName("BOP");
			    Thead cabeceraTabla = new Thead();
	        	Tr filaCabecera = new Tr();
			    Tbody cuerpoTabla = new Tbody();
			    boolean nombre = false;
			    boolean dni = false;
				
			    //Recorrer la lista de empleados
			    for(int i=0; i<listaEmpleados.getLength(); i++) {  
			    	
			    	es.dipucr.tablonEdictalUnico.xml.Tbody.Tr filaTabla = new es.dipucr.tablonEdictalUnico.xml.Tbody.Tr();
			        //Obtener de la lista un empleado tras otro
			        Node empleado = listaEmpleados.item(i);
			        //System.out.println("Empleado "+i);
			        //System.out.println("==========");    

			        //Obtener la lista de los datos que contiene ese empleado
			        NodeList datosEmpleado = empleado.getChildNodes();		
			        boolean insertadoCelda = false;
			        //Recorrer la lista de los datos que contiene el empleado
			        Notificado notificado = new Notificado();
			        for(int j=0; j<datosEmpleado.getLength(); j++) {
			        	Td celdaTabla = new Td();
			            //Obtener de la lista de datos un dato tras otro
			            Node dato = datosEmpleado.item(j);

			            //Comprobar que el dato se trata de un nodo de tipo Element
			            if(dato.getNodeType()==Node.ELEMENT_NODE) {
			            	if(dato.getNodeName().equals("BOP")){
			            		logger.error("Uno de los hijos del fichero tiene anidado una etiqueta BOP. Número de BOP: "+i);
			        			throw new ISPACRuleException("Uno de los hijos del fichero tiene anidado una etiqueta BOP. Número de BOP: "+i);
			            	}
			            	if(dato.getNodeName().equals("Nombre") || dato.getNodeName().equals("NOMBRE")){
		            			nombre = true;
		            		}
		            		if(dato.getNodeName().equals("DNI")){
		            			dni = true;
		            			notificado.setTipId("NIF");
		            		}
			                //Mostrar el nombre del tipo de dato
			            	if(i==0){			            		
			            		
			            		String inforMostrar = "";
			            		boolean insertar = false;
			            		if(dato.getNodeName().equals("Nombre")){
			            			inforMostrar = "Sujeto Pasivo";
			            			insertar = true;
			            		}
			            		if(dato.getNodeName().equals("Anio")){
			            			inforMostrar = "Ejercicio";
			            			insertar = true;
			            		}
			            		if(dato.getNodeName().equals("NumRecibo")){
			            			inforMostrar = "Número de Recibo";
			            			insertar = true;
			            		}
			            		if(dato.getNodeName().equals("ImpRecib")){
			            			inforMostrar = "Importe";
			            			insertar = true;
			            		}
			            		if(dato.getNodeName().equals("NomMunic")){
			            			inforMostrar = "Municipio";
			            			insertar = true;
			            		} 	
			            		if(dato.getNodeName().equals("NomPadro")){
			            			inforMostrar = "Concepto";
			            			insertar = true;
			            		} 
	
			            		//Campos de fichero de zona
			            		if(dato.getNodeName().equals("NOMBRE")){
			            			inforMostrar = "CONTRIBUYENTE";
			            			insertar = true;
			            		}
			            		if(recaudacionFichero.isRecaudacionFicheroZona() || recaudacionFichero.isRecaudacionSWALExpedientes()){
				            		if(dato.getNodeName().equals("DNI")){
				            			inforMostrar = "N.I.F.";
				            			insertar = true;
				            		}
			            		}
			            		if(dato.getNodeName().equals("N_EXPEDIENTE")){
			            			inforMostrar = "Nº Expediente";
			            			insertar = true;
			            		}
			            		if(dato.getNodeName().equals("ACTO_PENDIENTE_NOTIFICAR")){
			            			inforMostrar = "ACTO PENDIENTE DE NOTIFICAR";
			            			insertar = true;
			            		}
			            		if(dato.getNodeName().equals("LUGAR")){
			            			inforMostrar = "LUGAR";
			            			insertar = true;
			            		}
			            		if(dato.getNodeName().equals("TipActAdmin")){
			            			inforMostrar = "Tipo de actividad";
			            			insertar = true;
			            		}
			            		
			            		if(insertar){
			            			Th celdaCabecera = new Th();
						        	celdaCabecera.setValue(inforMostrar);
									filaCabecera.getTh().add(celdaCabecera);
			            		}
			            		
			            	}
			                //System.out.print(dato.getNodeName()+": ");
			                //El valor está contenido en un hijo del nodo Element
			                Node datoContenido = dato.getFirstChild();                        
			                //Mostrar el valor contenido en el nodo que debe ser de tipo Text
			                if(datoContenido!=null && datoContenido.getNodeType()==Node.TEXT_NODE){
			                	if(columnasMostrar(dato.getNodeName(), recaudacionFichero)){
			                		if(nombre){
				                		notificado.setValue(datoContenido.getNodeValue());
				                		nombre = false;
				                	}
				                	if(dni){
				                		notificado.setId(datoContenido.getNodeValue());
				                		dni = false;
				                	}
									celdaTabla.setValue(datoContenido.getNodeValue());
									filaTabla.getTd().add(celdaTabla);
									insertadoCelda = true;
			                	}			                	
			                }
			                else{
			                	if(columnasMostrar(dato.getNodeName(), recaudacionFichero)){
				                	celdaTabla.setValue("");
									filaTabla.getTd().add(celdaTabla);
									insertadoCelda = true;
			                	}
			                }
			            }		            
			        }
			        if(i==0){	
			        	//En la pasada 0 meto los campos de la cabecera y en la pasada 1 lo introduzco en la tabla
			        	cabeceraTabla.getTr().add(filaCabecera);
						table.setThead(cabeceraTabla);
			        }
			        //Se deja un salto de línea de separación entre cada empleado
			        if(insertadoCelda){
			        	cuerpoTabla.getTr().add(filaTabla);
			        	notificados.getNotificado().add(notificado);
			        }			        
			    }
			    table.setTbody(cuerpoTabla);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} 
		return table;
	}


	private static boolean columnasMostrar(String nodeName, RecaudacionFichero recaudacionFichero) {
		boolean mostrar = false;
		if(recaudacionFichero.isRecaudacionNueva()){
			if(nodeName.equals("Nombre") || nodeName.equals("NomPadro") || nodeName.equals("Anio") || 
					nodeName.equals("NomMunic") || nodeName.equals("NumRecibo") || nodeName.equals("ImpRecib")){
				mostrar= true;
			}
		}
		if(recaudacionFichero.isRecaudacionFicheroZona()){
			if(nodeName.equals("NOMBRE") || nodeName.equals("DNI") || nodeName.equals("N_EXPEDIENTE") || 
					nodeName.equals("ACTO_PENDIENTE_NOTIFICAR") || nodeName.equals("LUGAR")){
				mostrar= true;
			}
		}
		if(recaudacionFichero.isRecaudacionSWALExpedientes()){
			if(nodeName.equals("Nombre") || nodeName.equals("DNI") || nodeName.equals("TipActAdmin")){
				mostrar= true;
			}
		}
		return mostrar;
	}


	@SuppressWarnings("unchecked")
	private static InformacionTablonEdictal obtenerDatosInformacionTablonEdictal(IRuleContext rulectx) throws ISPACRuleException {
		InformacionTablonEdictal informacionTE = new InformacionTablonEdictal();
		//"TABLON_EDICTAL_BOE_DATOS", "TABLON_EDICTAL_BOE_PARRAFOS"
		try{
			
			/************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			 /***********************************************************************/
			Iterator <IItem> itTEUBOE = ConsultasGenericasUtil.queryEntities(rulectx, "TABLON_EDICTAL_BOE_DATOS", "NUMEXP='"+rulectx.getNumExp()+"'");
			while(itTEUBOE.hasNext()){
				IItem iTEUBOE = itTEUBOE.next();
				if(iTEUBOE.getString("CARGONOMBRE")!=null) informacionTE.setCargoNombre(iTEUBOE.getString("CARGONOMBRE"));
				if(iTEUBOE.getString("DATOSPERSONALES")!=null) informacionTE.setDatosPersonales(iTEUBOE.getString("DATOSPERSONALES"));
				if(iTEUBOE.getString("EMAIL_INCIDENCIAS")!=null) informacionTE.setEmail(iTEUBOE.getString("EMAIL_INCIDENCIAS"));
				if(iTEUBOE.getDate("FECHAFIRMA")!=null){
					Date date = iTEUBOE.getDate("FECHAFIRMA");
					GregorianCalendar fechaFirma= new GregorianCalendar();
					fechaFirma.setTime(date);
					informacionTE.setFechaFirma(fechaFirma);
				}
				if(iTEUBOE.getDate("FECHAPUBLICACION")!=null){
					Date date = iTEUBOE.getDate("FECHAPUBLICACION");
					GregorianCalendar fechaPublicacion= new GregorianCalendar();
					fechaPublicacion.setTime(date);
					informacionTE.setFechaPublicacion(fechaPublicacion);
				}
				if(iTEUBOE.getString("FORMA_PUBLICACION")!=null) informacionTE.setFormaPublicacion(iTEUBOE.getString("FORMA_PUBLICACION"));
				if(iTEUBOE.getString("LGT")!=null) informacionTE.setLgt(iTEUBOE.getString("LGT"));
				if(iTEUBOE.getString("LUGAR")!=null) informacionTE.setLugar(iTEUBOE.getString("LUGAR"));
				informacionTE.setNumexp(rulectx.getNumExp());
				if(iTEUBOE.getString("PROCEDIMIENTO")!=null) informacionTE.setProcedimiento(iTEUBOE.getString("PROCEDIMIENTO"));
				if(iTEUBOE.getString("TIPOANUNCIO")!=null) informacionTE.setTipoAnuncio(iTEUBOE.getString("TIPOANUNCIO"));				
			}
			
			//Carga los interesados participantes.
			
			IItemCollection itcoleparti = ParticipantesUtil.getParticipantes(cct, rulectx.getNumExp(), "(ROL = 'INT')", "NOMBRE");
			Iterator<IItem> iteParticipantes = itcoleparti.iterator();
			if(iteParticipantes.hasNext()){
				Notificados notificados = new Notificados();
				while(iteParticipantes.hasNext()){
					IItem participante = iteParticipantes.next();
					Notificado notificado = new Notificado();
					notificado.setId(participante.getString("NDOC"));
					notificado.setTipId("NIF");
					notificado.setValue(participante.getString("NOMBRE"));
					notificados.getNotificado().add(notificado);
				}
				informacionTE.setNotificados(notificados);
			}
			Iterator <IItem> itParrafos = ConsultasGenericasUtil.queryEntities(rulectx, "TABLON_EDICTAL_BOE_PARRAFOS", "NUMEXP='"+rulectx.getNumExp()+"' ORDER BY ID ASC");
			if(itParrafos.hasNext()){
				Vector<String> parrafos = new Vector<String>();
				while(itParrafos.hasNext()){
					IItem itParrafo = itParrafos.next();
					if(itParrafo.getString("PARRAFO")!=null)parrafos.add(itParrafo.getString("PARRAFO"));
				}
				informacionTE.setParrafos(parrafos);
			}
			
		
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en el número de expediente "+rulectx.getNumExp()+": "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return informacionTE;
	}


	@SuppressWarnings("unchecked")
	public static void obtenerRutaFicheroRecaudacion(IRuleContext rulectx, RecaudacionFichero recaudacion) throws ISPACRuleException {
		try {
			String refDoc = null;
			java.sql.Timestamp fDoc = null;
			File fichero = null;

			IItem tramRecauda = TramitesUtil.getTramiteByCode(rulectx,"fichNuevaRecauda");

			String consulta = "WHERE ID_TRAM_CTL = " + tramRecauda.getInt("ID")+ " AND NUMEXP='" + rulectx.getNumExp() + "'";
			IItemCollection tramspacDtTramite = TramitesUtil.queryTramites(rulectx.getClientContext(), consulta);
			Iterator<IItem> iterator = tramspacDtTramite.iterator();
			if (!iterator.hasNext()) {
				tramRecauda = TramitesUtil.getTramiteByCode(rulectx, "fichAntgRecauda");
				consulta = "WHERE ID_TRAM_CTL = " + tramRecauda.getInt("ID")+ " AND NUMEXP='" + rulectx.getNumExp() + "'";
				tramspacDtTramite = TramitesUtil.queryTramites(rulectx.getClientContext(), consulta);
				iterator = tramspacDtTramite.iterator();
				if(!iterator.hasNext()){
					tramRecauda = TramitesUtil.getTramiteByCode(rulectx, "fichZonaRecauda");
					consulta = "WHERE ID_TRAM_CTL = " + tramRecauda.getInt("ID")+ " AND NUMEXP='" + rulectx.getNumExp() + "'";
					tramspacDtTramite = TramitesUtil.queryTramites(rulectx.getClientContext(), consulta);
					iterator = tramspacDtTramite.iterator();		
					if(!iterator.hasNext()){
						tramRecauda = TramitesUtil.getTramiteByCode(rulectx, "fichero-texto");
						consulta = "WHERE ID_TRAM_CTL = " + tramRecauda.getInt("ID")+ " AND NUMEXP='" + rulectx.getNumExp() + "'";
						tramspacDtTramite = TramitesUtil.queryTramites(rulectx.getClientContext(), consulta);
						iterator = tramspacDtTramite.iterator();
						if(!iterator.hasNext()){
							tramRecauda = TramitesUtil.getTramiteByCode(rulectx, "fichNuevaRecExp");
							consulta = "WHERE ID_TRAM_CTL = " + tramRecauda.getInt("ID")+ " AND NUMEXP='" + rulectx.getNumExp() + "'";
							tramspacDtTramite = TramitesUtil.queryTramites(rulectx.getClientContext(), consulta);
							iterator = tramspacDtTramite.iterator();
							if(iterator.hasNext()){
								recaudacion.setRecaudacionSWALExpedientes(true);
							}
						}
						else{
							recaudacion.setFicheroTexto(true);
						}
					}
					else{
						recaudacion.setRecaudacionFicheroZona(true);
					}					
				}				
			}
			else {
					recaudacion.setRecaudacionNueva(true);
			}

			if (iterator.hasNext()) {
				IItem itTramitedt = iterator.next();

				IItemCollection taskDocumentosColeccion = DocumentosUtil.getDocumentosByTramites(rulectx, rulectx.getNumExp(),itTramitedt.getInt("ID_TRAM_EXP"));

				Iterator<IItem> itDocumentos = taskDocumentosColeccion
						.iterator();
				while (itDocumentos.hasNext()) {
					// logger.warn("Documento");
					IItem documento = (IItem) itDocumentos.next();
					java.sql.Timestamp fItem = (java.sql.Timestamp) documento.get("FDOC");
					// logger.warn("FDoc: " + fItem);
					if (fDoc != null) {
						if (fItem.after(fDoc)) {
							fDoc = fItem;
							refDoc = documento.getString("INFOPAG");
						}
					} else {
						fDoc = fItem;
						refDoc = (String) documento.getString("INFOPAG");
					}
					fichero = DocumentosUtil.getFile(rulectx.getClientContext(), refDoc, "Fichero",documento.getString("EXTENSION"));

				}
				if (fichero != null) {
					recaudacion.setRuta(fichero.getAbsolutePath());
					recaudacion.setNombreTabla(fichero.getName());
				}
			}
			 else {
					logger.error("No existe ningún documento cargado, por favor cargue el documento de recaudación");
					throw new ISPACRuleException(
							"No existe ningún documento cargado, por favor cargue el documento de recaudación");
				}
		} catch (ISPACRuleException e) {
			logger.error("Error. "+e.getMessage(), e);			
			throw new ISPACRuleException("Error al obtener la ruta del documento anexado, con numexp "+rulectx.getNumExp()+": "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error("Error. "+e.getMessage(), e);			
			throw new ISPACRuleException("Error al obtener la ruta del documento anexado, con numexp "+rulectx.getNumExp()+": "+e.getMessage(),e);
		}

	}


	public static void anadirInformacionRespuesta(IRuleContext rulectx, Respuesta respuesta) throws ISPACRuleException {
		try{
			
			/************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			 /***********************************************************************/
			Iterator <IItem> itTEUBOE = ConsultasGenericasUtil.queryEntities(rulectx, "TABLON_EDICTAL_BOE_DATOS", "NUMEXP='"+rulectx.getNumExp()+"'");
			while(itTEUBOE.hasNext()){
				IItem iTEUBOE = itTEUBOE.next();
				if(respuesta.getIdEnvio()!=null){
					iTEUBOE.set("IDENTIFICADORBOE", respuesta.getIdEnvio());
					iTEUBOE.set("TRAIDOANUNCIOTEU", "NO");
					iTEUBOE.set("ID_PROC", invesflowAPI.getProcess(rulectx.getNumExp()).getInt("ID"));
					ITask task = invesflowAPI.getTask(rulectx.getTaskId());
					iTEUBOE.set("UID_DESTINATARIO", task.getString("ID_RESP"));
					
					iTEUBOE.store(cct);
				}
				ListaAnuncios listaAnuncios = respuesta.getAnuncios();
				if(listaAnuncios!=null){
					es.boe.www.ServicioNotificaciones.Anuncio[] anuncios = listaAnuncios.getAnuncio();
					if(anuncios.length>=1){
						//Siempre vamos a mandar anuncio por expediente
						es.boe.www.ServicioNotificaciones.Anuncio anuncio = anuncios[0];
						iTEUBOE.set("IDENTIFICADORANUNCIOBOE", anuncio.getIdBoe());
						iTEUBOE.store(cct);
					}
					
				}
			}
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en el número de expediente "+rulectx.getNumExp()+": "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
	}


	public static void obtenerAnuncioTEUBOE(Entidad entidad) throws ISPACRuleException {
		try{
			
			TablonEdictalBoeDatosDAO tablonEdictal = new TablonEdictalBoeDatosDAO();
			Vector<TablonEdictalBoeDatos> anunciosNoTraidos = tablonEdictal.consultarAnunciosSinRecibir(entidad.getId());
			for (Iterator<TablonEdictalBoeDatos> iterator = anunciosNoTraidos.iterator(); iterator.hasNext();) {
				
				TablonEdictalBoeDatos tablonEdictalBoeDatos = (TablonEdictalBoeDatos) iterator.next();
				
				String tablonEdictal_address = ServiciosWebTEUFunciones.getDireccionSW();
				logger.warn("direccion. "+tablonEdictal_address);
				ServicioNotificacionesProxy notificacion = new ServicioNotificacionesProxy(tablonEdictal_address);
				if(tablonEdictalBoeDatos.getIdentificadoranuncioboe()!=null){
					Respuesta respuesta = notificacion.consultaAnuncio(tablonEdictalBoeDatos.getIdentificadoranuncioboe());
					ListaAnuncios listaAnuncios = respuesta.getAnuncios();					
					if(listaAnuncios!=null){
						es.boe.www.ServicioNotificaciones.Anuncio[] anuncios = listaAnuncios.getAnuncio();
						for (int i = 0; anuncios != null && i < anuncios.length; i++) {
							es.boe.www.ServicioNotificaciones.Anuncio anuncio = anuncios[i];
							if(anuncio.getEstadoBoe().equals("PUBLICADO")){
								insertarAviso(entidad, tablonEdictalBoeDatos);
							}
						}
					}
					
				}
				
			}

		} catch (RemoteException e) {
			logger.error("Error al enviar el anuncio de la entidad "+entidad+ "." + e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar el anuncio de la entidad "+entidad+ "." + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error al enviar el anuncio de la entidad "+entidad+ "." + e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar el anuncio de la entidad "+entidad+ "." + e.getMessage(), e);
		}
		
	}
	
	public static String getVarGlobal(Entidad entidad, String varName) {
		String valor = "";
		
		try {
			GenericoDAO generico = new GenericoDAO();
		    Connection con;
			con = generico.getConexion(entidad.getId(), Constantes.BBDD_TRAMITADOR);
		
		    String query = "SELECT VALOR FROM SPAC_VARS WHERE NOMBRE = '" + DBUtil.replaceQuotes(varName) + "'";
		    Statement sentencia = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);			
			
			ResultSet r = sentencia.executeQuery(query);			
			
			if(r.next()){
				valor = r.getString("VALOR");
				r.close();
				r=null;
				sentencia.close();
				sentencia=null;
				con.close();
				con=null;
				return valor;
			}
			
			else{
				r.close();
				r=null;
				sentencia.close();
				sentencia=null;
				con.close();
				con=null;
			}	
			
		} catch (Exception e) {
			logger.error("Error en la entidad "+entidad.getId()+" en la búsqueda de la variable "+varName+"."+e.getMessage(), e);
		}
		
		return valor;
	}


	private static void insertarAviso(Entidad entidad,
			TablonEdictalBoeDatos tablonEdictalBoeDatos) {
		AvisoDAO ac = new AvisoDAO();
    	
		es.dipucr.tablonEdictalUnico.quartz.bean.Aviso av = new es.dipucr.tablonEdictalUnico.quartz.bean.Aviso();
    	
    	av.setId_aviso(ac.nextIdAviso(entidad.getId()));
    	av.setId_proc(tablonEdictalBoeDatos.getId_proc());
    	av.setTipo_destinatario(1);
    	
    	String fecha="";
    	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    	fecha = formato.format(Calendar.getInstance().getTime());
    	av.setFecha(fecha);
    	
    	av.setId_expediente(tablonEdictalBoeDatos.getNumexp());
    	av.setEstado_aviso(0);
    	av.setMensaje("Ya ha sido publicado el Anuncio en el Tablón Edictal del BOE, puede generar el trámite de Recibir anuncio.");
    	av.setTipo_aviso("2");
    	av.setUid_destinatario(tablonEdictalBoeDatos.getUid_destinatario());
    	
    	ac.insertarAviso(entidad.getId(), av);  
	}

}
