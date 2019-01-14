package es.dipucr.sigem.api.rule.common.utils;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.EntitiesAPI;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tecdoc.sgm.core.base64.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dipucr.notifica.commons.ServiciosWebNotificaFunciones;
import es.dipucr.notifica.main.Base64Utils;
import es.dipucr.notifica.ws.notifica._1_0.ArrayOfIdentificador_envio;
import es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipoOrganismoEmisor;
import es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipo_destinatario;
import es.dipucr.notifica.ws.notifica._1_0.Consulta_cies;
import es.dipucr.notifica.ws.notifica._1_0.Direccion_electronica_habilitada;
import es.dipucr.notifica.ws.notifica._1_0.Documento;
import es.dipucr.notifica.ws.notifica._1_0.Identificador_envio;
import es.dipucr.notifica.ws.notifica._1_0.Info_envio;
import es.dipucr.notifica.ws.notifica._1_0.NotificaWsPortTypeProxy;
import es.dipucr.notifica.ws.notifica._1_0.Opciones_emision;
import es.dipucr.notifica.ws.notifica._1_0.ResultadoGetCies;
import es.dipucr.notifica.ws.notifica._1_0.ResultadoInfoEnvio;
import es.dipucr.notifica.ws.notifica._1_0.Resultado_alta;
import es.dipucr.notifica.ws.notifica._1_0.Resultado_certificacion;
import es.dipucr.notifica.ws.notifica._1_0.Resultado_datado;
import es.dipucr.notifica.ws.notifica._1_0.Resultado_estado;
import es.dipucr.notifica.ws.notifica._1_0.Resultado_organismos_activos;
import es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoEmisor;
import es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCIE;
import es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCorreos;
import es.dipucr.notifica.ws.notifica._1_0.Tipo_destinatario;
import es.dipucr.notifica.ws.notifica._1_0.Tipo_domicilio;
import es.dipucr.notifica.ws.notifica._1_0.Tipo_envio;
import es.dipucr.notifica.ws.notifica._1_0.Tipo_municipio;
import es.dipucr.notifica.ws.notifica._1_0.Tipo_pais;
import es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario;
import es.dipucr.notifica.ws.notifica._1_0.Tipo_procedimiento;
import es.dipucr.notifica.ws.notifica._1_0.Tipo_provincia;
import es.dipucr.sigem.api.rule.procedures.Constants;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author agustin
 * 
 * Clase que contiene los metodos que llaman a los servicos web de Notific@, esta clase contiene dos tipos de metodos los que llaman a los servicios o los que construyen los objetos
 * que se pasan por parametros a estos servicios, para facilitar su uso se ha seguido un patron en los nombres
 * 
 * metodo "notifica_nombredelservicio"
 * Estos metodos son los que hacen las llamadas a los servicios de notifica, hay uno principal que es el de envio y otros que generalmente sirven para consultar información del envio
 * 
 * metodo para_nombremetododelserviciodenotifica_crear_parametroquedevuelve o para_nombremetododelserviciodenotifica_preparar_parametroquedevuelve
 * Estos metodos van construyendo todos los objetos requeridos que al final forman por ejemplo el envio de la notificacion
 *
 */
public class NotificaUtil {
	
	private static Logger logger = Logger.getLogger("loggerNotifica");
	public String apiKey="";
	
		
	public NotificaUtil(String _apiKey){
		setApiKey(_apiKey);		
	}
	
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * LLAMADA AL SERVICIO WEB NOTIFIC@
	 * altaEnvio
	 * @param tipoEnvio Contiene toda la información del envío, en esta clase hay varios métodos para asignar valores a todos los parametros del envio, ver método main apartado ENVIO DE NOTIFICACION	
	 * @return
	 * @throws ISPACException 
	 */
	public Resultado_alta notifica_altaEnvio(IClientContext ctx, String numexp, String idResponsable, int idDoc, IItem document, Tipo_envio tipoEnvio) 
	{	
		logger.info("ENTRA EN NotificaUtil-->notifica_altaEnvio");
		
		try {					
				
				String notifica_address = ServiciosWebNotificaFunciones.getDireccionSW();
				
			    NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy(notifica_address);
			    
				Resultado_alta ra = new Resultado_alta();
				try {
					
					ra = swptp.altaEnvio(tipoEnvio, getApiKey());
					if(ra.getCodigo_respuesta().equals(Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_000)){
						notifica_altaEnvio_insertarAcuseNotifica(ctx, numexp, idDoc, idResponsable, document, tipoEnvio,ra);
						document.set("ESTADONOTIFICACION", Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA);
						document.set("ID_NOTIFICACION", ra.getIdentificadores().getItem(0).getIdentificador());
						document.store(ctx);
					}
					
					logger.info("RESPUESTA DEL ENVÍO: " + ra.toString());
					logger.info("SALE DE NotificaUtil-->notifica_altaEnvio");
					
				} catch (Exception e) {
					
					logger.error("ERROR AL REALIZAR EL ALTANVIO: " + e.getMessage());
					if(!e.getMessage().equals(Constants.NOTIFICACIONES_ERROR.NOTIFICA_DUPLICADA)){
						document.set("ESTADONOTIFICACION", Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_ERROR);
						document.store(ctx);
					}
					//throw new ISPACException("Error al enviar a Notifica " + e.getMessage());
					
				}				
				
				return ra;	
			
			//}
			
		} catch (ISPACException e) {
			logger.error("ERROR AL REALIZAR LA VALIDACION DEL EMAIL: " + e.getMessage());	
		}
		
		return null;
		
	        
	}
	
	/**
	 * Inserta registro en tabla DPCR_ACUSES_NOTIFICA que lleva control de envios a notifica
	 * 
	 * @param ctx
	 * @param numexp
	 * @param idDoc
	 * @param document
	 * @param envio
	 * @param respuesta
	 * @throws ISPACRuleException
	 */
	public void notifica_altaEnvio_insertarAcuseNotifica(IClientContext ctx, String numexp, int idDoc,  String idResponsable, IItem document, Tipo_envio envio, Resultado_alta respuesta) throws ISPACRuleException {

		IItem notice;
		int taskId = 0;
		
		try {
			
			if (document.getString("ID_TRAMITE") != null)
				taskId = document.getInt("ID_TRAMITE");	
			
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			notice = entitiesAPI.createEntity(Constants.TABLASBBDD.DPCR_ACUSES_NOTIFICA, "");
			notice.set("NUMEXP", numexp);
			notice.set("ID_PROC", invesflowAPI.getProcess(numexp).getInt("ID"));
			notice.set("ID_NOTIFICACION", respuesta.getIdentificadores().getItem(0).getIdentificador());
			notice.set("ID_RESPONSABLE",  idResponsable);
			notice.set("IDENT_DOC", idDoc);
			notice.set("ESTADO", respuesta.getCodigo_respuesta());
			notice.set("DNI_NOTIFICADO",   respuesta.getIdentificadores().getItem(0).getNif_titular());
			notice.set("TRAMITE", taskId);

			notice.store(ctx);

		} catch (ISPACException e) {
			logger.error("Error al insertarAcuseNotifica " + numexp + " al dniNotificado " + envio.getDestinatarios().getItem(0).getDestinatario().getNif() + " idDoc "+idDoc+". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al insertar en la tabla DPCR_ACUSES_NOTIFICA"+ e.getMessage(), e);			
		}
	}

	
	  
	/**
	 * LLAMADA AL SERVICIO WEB NOTIFIC@
	 * consultaDatadoEnvio
	 * @param identificador_envio Obtener todos los datos del envío ya registrado en notifica
	 * @return
	 */
	public Resultado_datado notifica_consultaDatadoEnvio(String identificador_envio) 
	{
		String notifica_address = ServiciosWebNotificaFunciones.getDireccionSW();
	    NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy(notifica_address);		
	    Resultado_datado rd = new Resultado_datado();
		try {
			
			rd = swptp.consultaDatadoEnvio(identificador_envio, getApiKey());
			
		} catch (RemoteException e) {
			
			logger.error("ERROR. " + e.getMessage(), e);
			
		}
				
	    return rd;
	}
	  
	/**
	 * LLAMADA AL SERVICIO WEB NOTIFIC@
	 * consultaCertificacionEnvio
	 * @param identificador_envio
	 * @return
	 */
	public Resultado_certificacion notifica_consultaCertificacionEnvio(String identificador_envio) 
	{
		String notifica_address = ServiciosWebNotificaFunciones.getDireccionSW();
	    NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy(notifica_address);		
	    Resultado_certificacion rc = new Resultado_certificacion();
		try {
			
			rc = swptp.consultaCertificacionEnvio(identificador_envio, getApiKey());
			
		} catch (RemoteException e) {
			
			logger.error("ERROR. " + e.getMessage(), e);
			
		}
				
	    return rc;
	}
	
	public byte[] notifica_consultaCertificacionEnvio_contenido(String identificador_envio) 
	{
		String notifica_address = ServiciosWebNotificaFunciones.getDireccionSW();
	    NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy(notifica_address);		
	    Resultado_certificacion rc = new Resultado_certificacion();
		try {
			
			rc = swptp.consultaCertificacionEnvio(identificador_envio, getApiKey());
			
		} catch (RemoteException e) {
			
			logger.error("ERROR. " + e.getMessage(), e);
			
		}
				
	    return Base64.decode(rc.getCertificacion().getPdf_certificado());
	}
	  
	/**
	 * LLAMADA AL SERVICIO WEB NOTIFIC@
	 * consultaEstado
	 * @param idEnvio
	 * @return
	 */
	public Resultado_estado notifica_consultaEstado(String idEnvio)
	{		
		String notifica_address = ServiciosWebNotificaFunciones.getDireccionSW();
	    NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy(notifica_address);			
	    Resultado_estado re = new Resultado_estado();
		try {
			
			re = swptp.consultaEstado(idEnvio, getApiKey());
			
		} catch (RemoteException e) {
			
			logger.error("ERROR. " + e.getMessage(), e);
			
		}
				
	    return re;	    
	}
	
	
	
	/**
	 * Devuelve estado que retorna Notifica del id notificacion que se le pasa por parametro
	 * 
	 * @param identificador_envio
	 * @return
	 */
	public String notifica_consultaEstado_dameEstado(String identificador_envio){
		
		Resultado_estado re = notifica_consultaEstado(identificador_envio);
		if(null!=re)
			return re.getEstado().getEstado();
		else
			return null;
		
	}
	
	  
	/**
	 * LLAMADA AL SERVICIO WEB NOTIFIC@
	 * infoEnvio
	 * @param infoEnvio
	 * @return
	 */
	public ResultadoInfoEnvio notifica_infoEnvio(Info_envio infoEnvio)
	{		
		String notifica_address = ServiciosWebNotificaFunciones.getDireccionSW();
	    NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy(notifica_address);			
	    ResultadoInfoEnvio re = new ResultadoInfoEnvio();
		try {
			
			re = swptp.infoEnvio(infoEnvio, getApiKey());
			
		} catch (RemoteException e) {
			
			logger.error("ERROR. " + e.getMessage(), e);
			
		}
				
	    return re;		
	}
	
	/**
	 * LLAMADA AL SERVICIO WEB NOTIFIC@
	 * consultaCies
	 * @param consulta_cies
	 * @return
	 */
	public ResultadoGetCies notifica_consultaCies(Consulta_cies consulta_cies)
	{
		String notifica_address = ServiciosWebNotificaFunciones.getDireccionSW();
	    NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy(notifica_address);			
	    ResultadoGetCies rgc = new ResultadoGetCies();
		try {
			
			rgc = swptp.consultaCies(consulta_cies, getApiKey());
			
		} catch (RemoteException e) {
			
			logger.error("ERROR. " + e.getMessage(), e);
			
		}
				
	    return rgc;	
	}
	
	/**
	 * LLAMADA AL SERVICIO WEB NOTIFIC@
	 * consultaOrganismosActivos
	 * @return
	 */
	public Resultado_organismos_activos notifica_consultaOrganismosActivos()
	{
		String notifica_address = ServiciosWebNotificaFunciones.getDireccionSW();
	    NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy(notifica_address);			
	    Resultado_organismos_activos roa = new Resultado_organismos_activos();
		try {
			
			roa = swptp.consultaOrganismosActivos(getApiKey());
			
		} catch (RemoteException e) {
			
			logger.error("ERROR. " + e.getMessage(), e);
			
		}
				
	    return roa;	
	}
	
	/**
	 * 
	 * @param concepto, resumen de la notificación, máximo 50 caracteres
	 * @param referenciaInternaDeLaNotificacion id referencia de la notificación, debe ser único porque notifica no te permite enviar una notificación con id repetido, cuando se envía la notificación Notifi@ le asigna también un id alternativo de Notific@, este id es el que hay que utilizar para consultar el estado de la notificación
	 * @param destinatarios Array de destinatarios a los que se le envía la notificacion
	 * @param doc Documento que se adjunta a la notificación, ya debe estar preparado con el documento cargado y los parametros correspondientes de normalizacion, csv y hash, existe un metodo para ello
	 * @param fechaCaducidadCarpetaCiudadanaConObligadoFalse Cuando el destinatario no sea obligado en las opciones de emesión hay que marcar la fecha de caducidad de la notificación en la carpeta ciudadana
	 * @param diasRetardoEnCarpetaCiudadanaConObligadoTrue Numero de días en carpeta ciudadana antes de que pase a la deh en caso que el destinatario sea obligado
	 * @param organismoEmisor organismo emisor de la notificacion, contiene el dir3 y el nombre del organismo
	 * @param procedimiento procedimiento sia del que procede la notificacion, contiene el codigo sia y una descripcion
	 * @param tipoEnvio Obligatorio notificacion/comunicación.
	 * // POSIBLES PARAMETROS QUE QUITO PORQUE SERIAN SIEMPRE NULOS DIPUTACION NO TIENE CONVENIO CIE
	 * // organismoPagadorCIE Diputación no tiene un CIE externo de momento, este parametro hay que pasarlo a null
	 * // organismoPagadorCorreos Lo mismo que el anterior, este parametro hay que pasarlo a null
	 * @return Tipo_envio es el objeto que se le pasa al servicio de alta envío, este objeto contiene toda la información de la notificacion
	 */
	public Tipo_envio para_notifica_altaEnvio_crear_tipo_envio(
												   String concepto,
												   Tipo_destinatario[] destinatarios, 
												   Documento doc,												   
												   Date fechaCaducidadEnCarpetaCiudadanaConObligadoFalse,
												   int diasRetardoEnCarpetaCiudadanaConObligadoTrue,
												   TipoOrganismoEmisor organismoEmisor,
												   Tipo_procedimiento procedimiento,
												   String tipoEnvio											   
												   )
	{
		
		//Objeto que encapsula toda la información de la notificacion		
		Tipo_envio tipoEnvioReturn = new Tipo_envio();			
		
		//Documento adjunto a la notificación ya debe tener asignados los parametros obligatorios como el csv, hash y estado de normalizacion para cie
		Documento doc_= doc;
		
		//Concepto
		tipoEnvioReturn.setConcepto(concepto);				
			
		//Adjuto documento			 
		tipoEnvioReturn.setDocumento(doc_);		
		
		//Organismo emisor Obligatorio. Código correspondiente al DIR3.
		TipoOrganismoEmisor organismoEmisor_ = organismoEmisor;		
		tipoEnvioReturn.setOrganismo_emisor(organismoEmisor_);			
		
		//Procedimiento
		Tipo_procedimiento procedimiento_ = procedimiento;
		tipoEnvioReturn.setProcedimiento(procedimiento_);				
		
		//Tipo de envio notificación o comunicación.
		String tipoEnvio_ = tipoEnvio;
		tipoEnvioReturn.setTipo_envio(tipoEnvio_);	
		
		return tipoEnvioReturn;
	}
	
	/**
	 * 
	 * @param codigoDeProcedimientoDEHigualQueElSIA
	 * @param nif
	 * @param obligado
	 * @return
	 */
	public static Direccion_electronica_habilitada para_notifica_altaEnvio_preparar_direccion_electronica_habilitada
													(															
															String codigoDeProcedimientoDEHigualQueElSIA,
															String nif,
															boolean obligado
													)
	{		
		Direccion_electronica_habilitada deh= new Direccion_electronica_habilitada();		
		deh.setCodigo_procedimiento(codigoDeProcedimientoDEHigualQueElSIA);
		deh.setNif(nif);
		deh.setObligado(obligado);
		
		return deh;		
	}
		
	/**
	 * 
	 * @param obligado_true_fecha_de_envio_de_carpeta_ciudadana_a_deh
	 * @param obligado_false_dias_en_carpeta_ciudadana_no_pasa_a_deh
	 * @return
	 */
	public static Opciones_emision para_notifica_altaEnvio_preparar_opciones_de_emision
													(
															String obligado_false_fecha_de_caducidad_de_carpeta_ciudadana_no_pasa_a_deh,
															int obligado_true_dias_en_carpeta_ciudadana_antes_de_pasar_a_deh
													)
	{		
		Opciones_emision opciones_emision = new Opciones_emision();
		opciones_emision.setCaducidad(obligado_false_fecha_de_caducidad_de_carpeta_ciudadana_no_pasa_a_deh); //Por norma serían 10 días
		opciones_emision.setRetardo_postal_deh(obligado_true_dias_en_carpeta_ciudadana_antes_de_pasar_a_deh); //Pensamos en ponerla 2 días en carpeta ciudadana antes de pasar a deh
		
		return opciones_emision;		
	}
	
	/**
	 * 
	 * @param deh
	 * @param tipoDomicilio
	 * @param referencia_emisor
	 * @param servicio
	 * @param tipo_domicilio1
	 * @param documento_notifica
	 * @param fecha_envio_programado
	 * @param toe
	 * @param topcie
	 * @param topc
	 * @param tp
	 * @param tipo_envio
	 * @return
	 */
	public static Tipo_destinatario para_notifica_altaEnvio_tipo_envio_preparar_tipo_destinatario(
												   Tipo_persona_destinatario tipoPersonaDestinatario,
												   Direccion_electronica_habilitada deh, 
												   Opciones_emision opcionesEmision,
												   String referencia_emisor,
												   String servicio,
												   String tipo_domicilio
												   )
	{		
		Tipo_destinatario tipoDestinatario = new Tipo_destinatario();
		tipoDestinatario.setDestinatario(tipoPersonaDestinatario);	//PUEDEN SER DIFERENTES EL DESTINATARIO Y EL TITULAR
		tipoDestinatario.setTitular(tipoPersonaDestinatario);
		
		//Solo en el caso que venga true el obligado meto el nodo direccion electronica habilitada
		if(deh.isObligado())
			tipoDestinatario.setDireccion_electronica(deh);
		
		tipoDestinatario.setReferencia_emisor(referencia_emisor);
		tipoDestinatario.setServicio(servicio);
		tipoDestinatario.setTipo_domicilio(tipo_domicilio);
		tipoDestinatario.setOpciones_emision(opcionesEmision);	
		//tipoDestinatario.setDomicilio(domicilio); Usar en caso que se quiera enviar al cie
		
		return tipoDestinatario;		
	}
	
	/**
	 * 
	 * @param bytes contenido del fichero
	 * @param docGenerarCsv si/no Obligatorio. En caso de que desee que Notific@ genere un CSV y que el documento esté firmado digitalmente se debe introducir SI, caso contrario debe introducir NO. El CSV se introducirá en el pie del documento con la información de los firmantes para la consulta por parte del receptor
	 * @param docHash Obligatorio, hash del documento
	 * @param estaNormalizadoParaCie si/no Obligatorio. En caso de que el documento reserve en la primera página el espacio en blanco necesario para introducir la ventanilla del sobre (NCC, titular, destinatario, domicilio) se debe rellenar con SI. En caso contrario debe rellenarse con NO y el CIE adjuntará una página adicional con la ventana para el ensobrado, incrementando el coste del servicio por la página impresa y por el peso adicional del servicio postal.
	 * @return retorna el documento preparado para adjuntarlo a la notificacion, parametro obligatorio para el alta envio
	 */
	public Documento para_notifica_altaEnvio_preparar_documento_adjunto(
												   byte[] bytes,
												   String docGenerarCsv,
												   String docHash,
												   String estaNormalizadoParaCie
												   )
	{		
		//Documento
		Documento doc= new Documento();
		
		doc.setGenerar_csv(docGenerarCsv);
		doc.setHash_sha1(docHash);	
		doc.setNormalizado(estaNormalizadoParaCie); 
		
		return doc;		
	}
	
	/**
	 * 
	 * @param codigoDIR3
	 * @param nombreOrganismo
	 * @return TipoOrganismoEmisor parametro obligatorio para el alta envio
	 */	
	public static TipoOrganismoEmisor para_notifica_altaEnvio_preparar_tipo_organismo_emisor(
													String codigoDIR3,
													String nombreOrganismo
													)
	{		
		TipoOrganismoEmisor organismoEmisor = new TipoOrganismoEmisor();
		
		//L02000013 es el global de Diputación
		organismoEmisor.setCodigo_dir3(codigoDIR3);
		organismoEmisor.setNombre(nombreOrganismo);
		
		return organismoEmisor;
	}
	
	/**
	 * 
	 * @param codigoSIA
	 * @param descripcion
	 * @return Tipo_procedimiento  parametro obligatorio para el alta envio
	 */
	public static Tipo_procedimiento para_notifica_altaEnvio_preparar_tipo_procedimiento(
													String codigoSIA,
													String descripcion
													)
	{		
		Tipo_procedimiento procedimientoSIA = new Tipo_procedimiento();
		
		//L02000013 es el global de Diputación
		procedimientoSIA.setCodigo_sia(codigoSIA);
		procedimientoSIA.setDescripcion_sia(descripcion);
		
		return procedimientoSIA;
	}
	
	/**
	 * 
	 * @param contenidoBase64
	 * @param generaCSV
	 * @param hash_sha1
	 * @param normalizado
	 * @return
	 * @throws IOException 
	 */
	public static Documento para_notifica_altaEnvio_preparar_documento(
													byte[] contenidoBase64,
													String generaCSV, //si o no
													String hash_sha1,
													String normalizado //normalizado para ensobrado si o no, siempre no													
													) throws IOException
	{
		Documento doc = new Documento();
		doc.setContenido(Base64Utils.encodeBytesToBase64Binary(contenidoBase64));
		doc.setGenerar_csv(generaCSV);
		doc.setHash_sha1(hash_sha1);
		doc.setNormalizado(normalizado);
		
		return doc;		
	}
	
	/**
	 * 
	 * @param nif
	 * @param nombre
	 * @param apellidos
	 * @param email
	 * @param telefono
	 * @return
	 */
	public static Tipo_persona_destinatario para_notifica_altaEnvio_preparar_tipo_persona_destinatario(
													String nif,
													String nombre,
													String apellidos,
													String email,
													String telefono											
													)
	{
		
		Tipo_persona_destinatario tipoPersonaDestinatario = new Tipo_persona_destinatario();
		tipoPersonaDestinatario.setApellidos(apellidos);
		tipoPersonaDestinatario.setEmail(email);
		tipoPersonaDestinatario.setNif(nif);
		tipoPersonaDestinatario.setNombre(nombre);
		tipoPersonaDestinatario.setTelefono(telefono);	
		
		return tipoPersonaDestinatario;		
	}
	
	/**
	 * Genera un envío para Notific@, recoge todos los parámetros necesarios y opcionales, notar que este método sólo deja indicar un destinatario
	 * @param concepto
	 * @param codigoDIR3
	 * @param nombreEntidadEmisora
	 * @param pathDocumentoNotificacion
	 * @param generaCSV
	 * @param hash_sha1
	 * @param normalizadoParaCIE
	 * @param codigoDeProcedimientoDEHigualQueElSIA
	 * @param nifInteresado
	 * @param obligadoInteresado
	 * @param diasAntesDeCaducar
	 * @param diasEnCarpetaCiudadana
	 * @param nifDestinatario
	 * @param nombreDestinatario
	 * @param apellidosDestinatario
	 * @param emailDestinatario
	 * @param telefonoDestinatario
	 * @param idNotificacionReferenciaEmisor
	 * @param servicioNormaOurgente
	 * @param tipo_domicilio
	 * @param codigoSIA
	 * @param nombreDeProcedimientoSIA
	 * @param tipoEnvio
	 * @return
	 * @throws IOException 
	 */
	public static Tipo_envio para_notifica_altaEnvio_generaTipoEnvio(
													String concepto,
													String codigoDIR3,
													String nombreEntidadEmisora,
													byte[] contenidoBase64,
													String generaCSV, //si o no
													String hash_sha1,
													String normalizadoParaCIE, //si o no, que tenga el membrete de la direccion postal
													String codigoDeProcedimientoDEHigualQueElSIA,
													String nifInteresadoDEH,
													boolean obligadoInteresado, //true o false, si true va siempre a la DEH
													int diasAntesDeCaducar,
													int diasEnCarpetaCiudadana, //sólo le hace caso si el obligado es true
													int diasDeEsperaAntesDeEnviar, //si indicas 0 lo manda ahora mismo
													String nifDestinatario,
													String nombreDestinatario,
													String apellidosDestinatario,
													String emailDestinatario,
													String telefonoDestinatario,
													String idNotificacionReferenciaEmisor,
													String servicioNormaOurgente,
													String tipo_domicilio,
													String codigoSIA,
													String nombreDeProcedimientoSIA,
													String tipoEnvio //notificación o comunicación.			
													) throws IOException
	{
						
		Tipo_envio envio_type = new Tipo_envio();	
		
		//Concepto
		envio_type.setConcepto(concepto);	
		
		
		//Organismo emisor
		TipoOrganismoEmisor toe = NotificaUtil.para_notifica_altaEnvio_preparar_tipo_organismo_emisor(codigoDIR3, nombreEntidadEmisora);
		envio_type.setOrganismo_emisor(toe);	
		
		
		//Documento		
		Documento doc;
		doc = NotificaUtil.para_notifica_altaEnvio_preparar_documento(contenidoBase64, generaCSV, hash_sha1, normalizadoParaCIE);
		envio_type.setDocumento(doc);
					
		//Destinararios lo cargaremos del participante del expediente
		///
		Direccion_electronica_habilitada deh= NotificaUtil.para_notifica_altaEnvio_preparar_direccion_electronica_habilitada(codigoDeProcedimientoDEHigualQueElSIA, nifInteresadoDEH, obligadoInteresado);
		///
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DAY_OF_YEAR, diasAntesDeCaducar);
		Date date_caducidad = cal.getTime();
		String sdf = new SimpleDateFormat("yyyy-MM-dd").format(date_caducidad);	
		Opciones_emision opcionesEmision = NotificaUtil.para_notifica_altaEnvio_preparar_opciones_de_emision(sdf, diasEnCarpetaCiudadana);
		Tipo_persona_destinatario tipoPersonaDestinatario = NotificaUtil.para_notifica_altaEnvio_preparar_tipo_persona_destinatario(nifDestinatario,nombreDestinatario,apellidosDestinatario,emailDestinatario,telefonoDestinatario);
		Tipo_destinatario tipoDestinatario = NotificaUtil.para_notifica_altaEnvio_tipo_envio_preparar_tipo_destinatario(tipoPersonaDestinatario,deh,opcionesEmision,idNotificacionReferenciaEmisor,servicioNormaOurgente,tipo_domicilio);
		ArrayOfTipo_destinatario arrayDestinatarios = new ArrayOfTipo_destinatario();
		Tipo_destinatario [] arrayTipoDestinatario = new Tipo_destinatario[1];	
		arrayTipoDestinatario[0]= tipoDestinatario;
		arrayDestinatarios.setItem(arrayTipoDestinatario);
		envio_type.setDestinatarios(arrayDestinatarios);				
		
		
		//De momento es opcional y no lo utilizamos
		//Fecha envio programado, si le pones el mismo día aunque añadas horas da error ES OPCIONAL
		if(diasDeEsperaAntesDeEnviar!=0)
		{
			Calendar fechaEnvioProgramado = Calendar.getInstance();
			fechaEnvioProgramado.add(Calendar.DAY_OF_YEAR, diasDeEsperaAntesDeEnviar);
			envio_type.setFecha_envio_programado(fechaEnvioProgramado);	
		}
				
		
		//Procedimiento
		Tipo_procedimiento procedimiento = NotificaUtil.para_notifica_altaEnvio_preparar_tipo_procedimiento(codigoSIA,nombreDeProcedimientoSIA);
		envio_type.setProcedimiento(procedimiento);				
		
		//Tipo de envio		
		envio_type.setTipo_envio(tipoEnvio);
		
		logger.info("DETTALE DEL ENVÍO: " + envio_type.toString());				
		
		return envio_type;
				
		
	}
	
	/**
	 * 
	 * @param documentContent
	 * @return
	 * @throws ISPACException
	 */
	public static String generateHashCode(byte[] documentContent) throws ISPACException {

		String contentBase64 = new String(documentContent);

		// Crea un digest con el algoritmo SHA-1
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error al obtener la instancia de MessageDigest. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener la instancia de MessageDigest. " + e.getMessage(), e);
		}

		// Genera el código hash de la cadena
		byte[] hash = md.digest(contentBase64.getBytes());
		logger.debug("hexHash byte " + hash.toString());

		String hexHash = toHexadecimal(hash);
		logger.debug("hexHash " + hexHash);

		return hexHash;
	}
	
	/**
	 * 
	 * @param datos
	 * @return
	 */
	public static String toHexadecimal(byte[] datos) {
		String resultado = "";
		ByteArrayInputStream input = new ByteArrayInputStream(datos);
		String cadAux;
		int leido = input.read();
		while (leido != -1) {
			cadAux = Integer.toHexString(leido);
			if (cadAux.length() < 2) // Hay que añadir un 0
				resultado += "0";
			resultado += cadAux;
			leido = input.read();
		}
		return resultado;
	}
	
	public static String SHAsum(byte[] convertme) throws NoSuchAlgorithmException{
	    MessageDigest md = MessageDigest.getInstance("SHA-1"); 
	    return Base64.encodeBytes(md.digest(convertme));
	}

	private static String byteArray2Hex(final byte[] hash) {
	    Formatter formatter = new Formatter();
	    for (byte b : hash) {
	        formatter.format("%02x", b);
	    }
	    return formatter.toString();
	}
	
	
	public static void main(String args[]) throws ParseException{
		
		//ESTO NO HACE FALTA PARA ENVIO
		
		
//		ClientContext context = new ClientContext();
//    	IInvesflowAPI invesFlowAPI = new InvesflowAPI(context);    	
//    	IEntitiesAPI entitiesAPI;
//		try {
//			entitiesAPI = invesFlowAPI.getEntitiesAPI();			
//			IItem it = DocumentosUtil.getDocumento(entitiesAPI, 1729897);
//			it.getKeyInt();
//		} catch (ISPACException e) {
//			logger.error("ERROR. " + e.getMessage(), e);
//		}
		
		System.out.println(2|5);
		
		NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy("https://notificaws.redsara.es/ws/soap/NotificaWS");
	    //NotificaWsPortTypeProxy swptp = new NotificaWsPortTypeProxy("https://se-notificaws.redsara.es/ws/soap/NotificaWS");    
		
		
		// PARA ENVIO
			
		//NotificaUtil.para_notifica_altaEnvio_generaTipoEnvio(concepto, codigoDIR3, nombreEntidadEmisora, pathDocumentoNotificacion, generaCSV, hash_sha1, normalizadoParaCIE, codigoDeProcedimientoDEHigualQueElSIA, nifInteresado, obligadoInteresado, diasAntesDeCaducar, diasEnCarpetaCiudadana, diasDeEsperaAntesDeEnviar, nifDestinatario, nombreDestinatario, apellidosDestinatario, emailDestinatario, telefonoDestinatario, idNotificacionReferenciaEmisor, servicioNormaOurgente, tipo_domicilio, codigoSIA, nombreDeProcedimientoSIA, tipoEnvio);		
		//PRUEBA_MANZANARES_1_2017_08_sept_2017 59b2874a2ca3b
		
		try {
		
			Tipo_envio envio_type;
			byte[] contenido_documento = Base64Utils.encodeFileToBinaryBytes("G:\\doc\\pruebas_curso_sigem_autofirma.pdf");
			
		
			envio_type = NotificaUtil.para_notifica_altaEnvio_generaTipoEnvio(
					//"DPCR2017/99999", //concepto
					//"PRUEBA_DAIMIEL_6_05_sept_2017", //concepto
					//"PRUEBA_ALCOLEA_2_2017_22_sept_2017", //concepto
					//"PRUEBA_MIGUELTURRA_5_2017_19_sept_2017", //concepto
					//"NO HACER MAS PRUEBAS, FUNCIONA BIEN ESTAN PENDIENTES PRUEBA_LA_SOLANA_11_2017_21_sept_2017", //concepto
					//"PRUEBA_PARA_GENERAR_XML_DIPUTACION_30_05_OCT_2017", //concepto
					//"PRUEBA_VILLAMAYOR_DE_CVA_2_2017_22_sept_2017", //concepto
					//"PRUEBA_SOCUELLAMOS_2017_09_22_sept_2017", //concepto
					//"PRUEBA_PEDRO_MUÑOZ_2017_09_22_sept_2017", //concepto
					//"PRUEBA_CARRION_1_22_sept_2017", //concepto
					//"PRUEBA_INFANTES_1_22_sept_2017", //concepto
					//"PRUEBA ALMAGRO_01_03_SEPT_2017",
					//"PRUEBA MEMBRILLA_28_SEPT_2017",
					//"PRUEBA DAIMIEL_4_29_SEPT_2017",
					"TOMELLOSO_2_23_MAR_2018",
					//"PRUEBA ARROBA DE LOS MONTES 1_30_OCT_2017",
					//"PRUEBA ALAMILLO 1_6_NOV_2017",
					//"PRUEBA ALHAMBRA 1_6_NOV_2017",
					//"PUERTOLLANO2017/9999 22_11_NOV_2017",
					//"STACRUZMUDELA2017/9999 29_11_NOV_2017",
					//"ALMADEN2017/9999 29_11_NOV_2017",					
					//"NAVALPINO2017/9999 29_11_NOV_2017",
					//"LLANOSDELCAUDILLO2018/99999",
					//"L01130533", //codigoDIR3 Manzanares
					//"L01130795", //codigoDIR3 La Solana
					//"L01130071", //codigoDIR3 Alcolea de Calatrava
					//"L01130394", //codigoDIR3 Daimiel
					//"L01130564", //codigoDIR3 Miguelturra
					//"L02000013", //codigoDIR3 Diputacion de Ciudad Real
					//"L01130782", //codigoDIR3 Socuellamos
					//"L01130914",//codigoDIR3 VILLAMAYOR DE CVA
					//"L01130343",//codigoDIR3 Ciudad Real
					//"L01130317",//DIR3 Carrion					
					//"L01130610",//DIR3 Pedro Muñoz
					//"L01130935",//DIR3 Infantes
					//"L01130548", //DIR3 Membrilla					
					//"L01130132",//DIR3 Almagro
					"L01130821",//DIR3 Tomelloso
					//"L01130219",//DIR3 Arroba de los Montes
					//"L01130034", //DIR3 ALAMILLO
					//"L01130104", //DIR3 ALHAMBRA
					//"L01130718", //DIR3 PUERTOLLANO
					//"L01130776", //DIR3 STACRUZMUDELA "235666"
					//"L01130111", //DIR3 ALMADEN "283549"
					//"L01130599", //DIR3 NAVALPINO "287854"
					//"L01139040", //DIR3 LLANOS
					//"Diputacion Provincial de Ciudad Real", //nombre de la entidad		
					//"Ayuntamiento de Daimiel", //nombre de la entidad
					//"Ayuntamiento de Miguelturra", //nombre de la entidad
					//"Ayuntamiento de Alcolea de Calatrava", //nombre de la entidad
					//"Ayuntamiento de Socuellamos", //nombre de la entidad
				    //"Ayuntamiento de Manzanares", //nombre de la entidad
					//"Ayuntamiento de la Solana", //nombre de la entidad
					//"Ayuntamiento de Villamayor de Cva",
					//"Ayuntamiento de Ciudad Real",
					//"Ayuntamiento de Almagro",
					//"Ayuntamiento de Membrilla",
					//"Ayuntamiento de Carrion", //nombre de la entidad	
					//"Ayuntamiento de Pedro Muñoz", //nombre de la entidad
					//"Ayuntamiento de Infantes", //nombre de la entidad
					"Ayuntamiento de Tomelloso", //nombre de la entidad
					//"Ayuntamiento de Arroba de los Montes", //nombre de la entidad
					//"Ayuntamiento de Alamillo", //nombre de la entidad
					//"Ayuntamiento de Alhambra", //nombre de la entidad
					//"Ayuntamiento de Puertollano", //nombre de la entidad
					//"Ayuntamiento de Sta Cruz de Mudela", //nombre de la entidad
					//"Ayuntamiento de Almaden", //nombre de la entidad
					//"Ayuntamiento de Navalpino", //nombre de la entidad
					//"Ayuntamiento de Llanos del Caudillo", //nombre de la entidad
					contenido_documento, //pathDocumentoNotificacion 
					"no", //generaCSV 
					NotificaUtil.SHAsum(contenido_documento), //hash_sha1
					"no", //normalizadoParaCIE
					//"216152", //codigo procedimiento DEH
					"", //codigo procedimiento DEH
					"47062508T", //nif interesado DEH
					//"", //nif interesado DEH
					false, //obligadoInteresado,
					10, //diasAntesDeCaducar, creemos que es configurable en SIA puede haber determinados procedimientos que caduquen antes
					0, //diasEnCarpetaCiudadana,
					0, //diasDeEsperaAntesDeEnviar, puedo mandarla a notifica y que la fecha de puesta a disposición sea mañana u otro día
					//"", //nifDestinatario
					"47062508T",
					"Agustin", //nombreDestinatario, 
					"Jiménez Moreno", //apellidosDestinatario,
					//"Alberto",
					//"Hernández Mora",
					//"",//null//
					"agustin_jimenez@dipucr.es", //emailDestinatario, FUNCIONA CON EMAIL NULO
					"", //telefonoDestinatario,
					"2",//"31", //idNotificacionReferenciaEmisor,
					"normal", //servicioNormaOurgente,
					"", //tipo_domicilio,//"concreto"
					//"221413", //SIA Manzanares
					//"216152", //SIA La Solana 
					//"235148", //SIA Alcolea
					//"235435", //SIA Daimiel
					//"216073", //SIA Miguelturra 
					//"212201", //SIA Diputación de Ciudad Real
					//"234108", //SIA Diputación de Ciudad Real PRUEBAS
					//"235146", //SIA La Solana
					//"235146",//SIA Socuellamos
					//"220249",//SIA Ciudad Real
					//"235771",//SIA VILLAMAYOR DE CVA
					//"235414",//SIA CARRION
					//"235147",//SIA Infantes
					//"235540",//SIA Pedro Muñoz
					//"235144",//SIA Almagro
					//"235145",//SIA Membrilla
					"235687", //SIA Tomelloso
					//"283633",//SIA ARROBA
					//"283486",//SIA ALAMILLO
					//"235330",//SIA ALHAMBRA
					//"235603",//SIA PUERTOLLANO
					//"235666", //SIA STACRUZMUDELA "235666"
					//"283549", //SIA ALMADEN "283549"
					//"287854", //SIA NAVALPINO "287854"
					//"235498", //SIA LLANOS DEL CAUDILLO
					"Notificacion Electronica Genérica", //nombreDeProcedimientoSIA
					"notificacion"//notificación o comunicación tipoEnvio
			);
			
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////ENVIO DE NOTIFICACION
//				//Concepto
//				envio_type.setConcepto("PRUEBA2017_22_julio");	
//				
//				
//				//Organismo emisor
//				TipoOrganismoEmisor toe = NotificaUtil.para_notifica_altaEnvio_preparar_tipo_organismo_emisor("L02000013", "Diputacion Provincial de CiudadReal");
//				envio_type.setOrganismo_emisor(toe);	
				
				
				//Documento		
//				try {
//					Documento doc;
//					//El SHA1 del ejemplo era "EwdDiMdjw8ZcMj3E1fZetUaUTCI="
//					byte[] documentContent = IOUtils.toByteArray(new FileInputStream(new File("G:\\doc\\pruebas_curso_sigem_autofirma.pdf")));
//					doc = NotificaUtil.para_notifica_altaEnvio_preparar_documento(documentContent, "no",NotificaUtil.SHAsum(documentContent) , "no");
//					envio_type.setDocumento(doc);
//				} catch (IOException e) {
//					logger.error("ERROR. " + e.getMessage(), e);
//				}
						
							
//				//Destinararios lo cargaremos del participante del expediente
//				///
//				Direccion_electronica_habilitada deh= NotificaUtil.para_notifica_altaEnvio_preparar_direccion_electronica_habilitada("212201", "47062508T", true);
//				///
//				Calendar cal = Calendar.getInstance();		
//				cal.add(Calendar.DAY_OF_YEAR, 1);
//				Date date_caducidad = cal.getTime();
//				String sdf = new SimpleDateFormat("yyyy-MM-dd").format(date_caducidad);	
//				Opciones_emision opcionesEmision = NotificaUtil.para_notifica_altaEnvio_preparar_opciones_de_emision(sdf, 0);
//				///
//				//Tipo_persona_destinatario tipoPersonaDestinatario = NotificaUtil.para_notifica_altaEnvio_preparar_tipo_persona_destinatario("05644882S","Luis","De Juan Casero","luis_dejuan@dipucr.es","639401897");
//				Tipo_persona_destinatario tipoPersonaDestinatario = NotificaUtil.para_notifica_altaEnvio_preparar_tipo_persona_destinatario("47062508T","Agustín","Jiménez Moreno","agustin_jimenez@dipucr.es","679412760");
//				//Tipo_persona_destinatario tipoPersonaDestinatario = NotificaUtil.para_notifica_altaEnvio_preparar_tipo_persona_destinatario("05695305E","Teresa","Carmona Gonzalez","teresa_carmona@dipucr.es","685412901");
//				//Tipo_persona_destinatario tipoPersonaDestinatario = NotificaUtil.para_notifica_altaEnvio_preparar_tipo_persona_destinatario("05684076V","Manuel","Quesada Elvira","manuel_quesada@dipucr.es","619589317");
//				///
//				Tipo_destinatario tipoDestinatario = NotificaUtil.para_notifica_altaEnvio_tipo_envio_preparar_tipo_destinatario(tipoPersonaDestinatario,deh,opcionesEmision,"21","normal","concreto");
//				//7-->58b7f0c4eb35d (Carpeta Ciudadana Obligado False 10 dias Notificada)
//				//Estados: Pendiente de comparecencia --> Notificada --> FIN (Certificado)
//				//8-->58b7f215e91e2 (Carpeta Ciudadana Obligado False 10 dias Rehusada)
//				//Estados: Pendiente de comparecencia --> Rehusada --> FIN (Certificado)
//				//11-->58bec0fbab699 obligado true 10 dias en la carpeta ciudadana antes de pasar a la deh dejarla la mando el 7/03/2017 dejarla que pase a la deh y que caduque en la deh
//				//12-->58bfc39b3b762 obligado true 0 dias en la carpeta ciudadana
//				//13-->58bfc5817b9d9 obligado true 0 dias en la carpeta ciudadana Antonio
//				//14-->58bfd9ab08623 igual que 13 a manu, la ha abierto en la carpeta ciudadana
//				//15-->58bfda5b9a5d3 igual que 13 a tere, la ha abierto en la deh
//				//16-->58bfdd8d83116 igual que 13 a tere pero ha marcardo que no quiere notificaciones por la deh
//				//17-->58bfdeeb9bb55 obligado false 
//				//18-->58bfe0b0e015a obligado false a tere se da de alta en 212201 de la deh, le ha llegado!
//				//19-->58bfe83d52f5b obligado true a agustin le pongo fecha de caducidad de 10 dias haber que hace
//				//20-->58bfe931b8f82 idem 19 marcando 5 días de caducidad
//				//21-->58c1114acbdc0 idem 19 marcando 1 día de caducidad y con fecha de envío programada para que me llegue el 10 de marzo de 2017
//				//22-->58c94a968eca0 idem 19 la envío el 15/03/2017
//				//1729866-->58d3c828eb7e5 idem 19 la envío el 23/03/2017 desde el tramitador
//		        //25-->58d91bca79f2a a Cesar Herrero de Tavernes
//				///	
//				ArrayOfTipo_destinatario arrayDestinatarios = new ArrayOfTipo_destinatario();
//				Tipo_destinatario [] arrayTipoDestinatario = new Tipo_destinatario[1];	
//				arrayTipoDestinatario[0]= tipoDestinatario;
//				arrayDestinatarios.setItem(arrayTipoDestinatario);
//				envio_type.setDestinatarios(arrayDestinatarios);				
//				
//				
//				//Fecha envio programado, si le pones el mismo día aunque añadas horas da error ES OPCIONAL
//				Calendar fechaEnvioProgramado = Calendar.getInstance();
//				fechaEnvioProgramado.add(Calendar.DAY_OF_YEAR, 1);
//				//envio_type.setFecha_envio_programado(fechaEnvioProgramado);						
//						
//				
//				//Procedimiento
//				Tipo_procedimiento procedimiento = NotificaUtil.para_notifica_altaEnvio_preparar_tipo_procedimiento("212201","Notificacion Electronica Genérica");
//				envio_type.setProcedimiento(procedimiento);				
//				
//				//Tipo de envio
//				String tipo_envio = "notificacion";//notificación o comunicación.
//				envio_type.setTipo_envio(tipo_envio);	
		
		
		
		
		
		
		
		
		
		
		
		/////////////////////////////////////// METODO QUE ENVÍA /////////////////////////////////////////						
				
						
				Resultado_alta ra = new Resultado_alta();
				
					
					ra = swptp.altaEnvio(envio_type, "Mjk3MjE5MzgxNDc2Nzc3MTA0MA=="); //TOMELLOSO
					//ra = swptp.altaEnvio(envio_type, "MzE4OTkzMjc5NzA3OTk4Mjc2Mg=="); //DIPUTACION
				    //ra = swptp.altaEnvio(envio_type, "MTYzMDA2NjQwOTExNzI4NDAwMg=="); //DIPUTACION PRUEBAS
					//ra = swptp.altaEnvio(envio_type, "MjIxNTU3NDQxNTQxNTYzNDg5NQ=="); //MANZANARES
					//ra = swptp.altaEnvio(envio_type, "MTc3NzAwNjUzNDUwNDI3MjMxMw=="); //CIUDAD REAL

					
					logger.info("RESPUESTA DEL ENVÍO: " + ra.toString());	
					
				
		} catch (Exception e) {					
			logger.error("ERROR GENERAL: " + e.getMessage());
		}
			
		/////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		
		
		
		
		
		
				
		////////////////////////////////////////////////////////////////////////////////////////////////////////CONSULTAR ESTADO
//		System.out.println("consultarEstado");
//		//NotificaUtil nu = new NotificaUtil("MTYzMDA2NjQwOTExNzI4NDAwMg==") //PRUEBAS DIPUCR
//		NotificaUtil nu = new NotificaUtil("MzE4OTkzMjc5NzA3OTk4Mjc2Mg=="); //PRODUCCION DIPUCR
//		Resultado_estado re = nu.notifica_consultaEstado("59ccc9f1bd690"); //58c1114acbdc0 Rehusada, 58f5f42a03727 Notificada, 58ecbb565ba47 Pendiente de comparecencia
//		if(null!=re)
//		{
//			System.out.println("getDescripcion_respuesta: "+re.getDescripcion_respuesta());
//			System.out.println("getCodigo_respuesta: "+re.getCodigo_respuesta());	
//			System.out.println("getEstado: "+re.getEstado().getEstado());
//		}
//		System.out.println("FIN consultarEstado");
		
		/*		
		////////////////////////////////////////////////////////////////////////////////////////////////////////CONSULTAR CIES NO TENEMOS CONVENIO CON HACIENDA
		Consulta_cies cc = new Consulta_cies();			
		cc.setOrganismo_emisor("L02000013");
		NotificaUtil.notifica_consultaCies(cc);				 
		*/
		
		/*
		////////////////////////////////////////////////////////////////////////////////////////////////////////CONSULTAR ORGANISMOS DADOS DE ALTA EN NOTIFICA
		//swptp.consultaCies(cc);
		Resultado_organismos_activos roa = NotificaUtil.notifica_consultaOrganismosActivos();
		if(null!=roa)
		{
			System.out.println(roa.getDescripcion_respuesta());
			ArrayOfTipoOrganismoEmisor arrayOrganismos = roa.getOrganismos();
			for (int i = 0; i < arrayOrganismos.getItem().length; i++) {
				System.out.println(arrayOrganismos.getItem(i).getNombre());						
			}
		}
		*/
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////CONSULTAR ENVIO
		System.out.println("consultaDatadoEnvio");
		Resultado_datado rd;
		try {
			rd = swptp.consultaDatadoEnvio("5a0dede940fd9","MzE4OTkzMjc5NzA3OTk4Mjc2Mg==");		
			//Resultado_datado rd = NotificaUtil.notifica_consultaDatadoEnvio("59c0eb0cbf4ee");
			//Resultado_datado rd = swptp.consultaDatadoEnvio("58b68bbc7924d");
			System.out.println("Resultado_datado-->Descripcion_respuesta: "+rd.getDescripcion_respuesta());
			System.out.println("Resultado_datado-->Descripcion_estado_actual: "+rd.getDatado().getDescripcion_estado_actual());
			System.out.println("Resultado_datado-->Estado_actual: "+rd.getDatado().getEstado_actual());
			System.out.println("Resultado_datado-->Ncc_id_externo: "+rd.getDatado().getNcc_id_externo());
			System.out.println("Resultado_datado-->Fecha_actualizacion: "+rd.getDatado().getFecha_actualizacion().getTime().toString());
			System.out.println("##########################Identificador_envio##############################");	
			System.out.println("Resultado_datado-->Identificador_envio-->Identificador: "+rd.getDatado().getIdentificador_envio().getIdentificador());
			System.out.println("Resultado_datado-->Identificador_envio-->NifTitular: "+rd.getDatado().getIdentificador_envio().getNif_titular());
			System.out.println("Resultado_datado-->Identificador_envio-->ReferenciaEmisor: "+rd.getDatado().getIdentificador_envio().getReferencia_emisor());	
			System.out.println("##########################Datado##############################");	
			System.out.println("Resultado_datado-->Datado-->Item-->Descripcion: "+rd.getDatado().getDatado().getItem(0).getDescripcion());
			System.out.println("Resultado_datado-->Datado-->Item-->Estado: "+rd.getDatado().getDatado().getItem(0).getEstado());
			System.out.println("Resultado_datado-->Datado-->Item-->Fecha: "+rd.getDatado().getDatado().getItem(0).getFecha().getTime().toString());		
			System.out.println("FIN consultaDatadoEnvio");
		} catch (RemoteException e) {
			logger.error("ERROR. " + e.getMessage(), e);
		}
		
		
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////CONSULTAR ENVIO, puedo obtener toda información del envío.
		
//		System.out.println("infoEnvio");				
//		Info_envio ie = new Info_envio();
//		ie.setEnvio_destinatario("5a0dede940fd9");
//		ResultadoInfoEnvio rie;
//		try {
//			rie = swptp.infoEnvio(ie,"MzE4OTkzMjc5NzA3OTk4Mjc2Mg==");
//			System.out.println("Resultado de la petición: "+rie.getDescripcion_respuesta());				
//			System.out.println("FIN infoEnvio");
//		
//		} catch (RemoteException e) {
//			logger.error("ERROR. " + e.getMessage(), e);
//		}	
		
		
			
		//////////////////////////////////////////////////////////////////////////////////////////////////////// CONSULTAR CERTIFICADO ENVIO			
//		System.out.println("consultaCertificacionEnvio");				
//		Resultado_certificacion rc = nu.notifica_consultaCertificacionEnvio("59ccc9f1bd690");
//		System.out.println("getDescripcion_respuesta: "+rc.getDescripcion_respuesta());	
//		System.out.println("getCodigo_respuesta: "+rc.getCodigo_respuesta());
//		System.out.println("getFecha_actualizacion: "+rc.getCertificacion().getFecha_actualizacion().getTime().toString());
//		
//		//Add this to write a string to a file
//		//
//		try {
//		    
//			File file = new File("G:/doc/certificacion_membrilla.pdf");
//			FileOutputStream fos = new FileOutputStream(file);
//			fos.write(Base64.decode(rc.getCertificacion().getPdf_certificado()));
//			fos.close();
//			
//			
//			System.out.println("getCertificacion--->"+rc.getCertificacion().getCertificacion());
//			System.out.println("getXml_certificado--->"+rc.getCertificacion().getXml_certificado());
//			
//
//		}
//		catch (IOException e)
//		{
//		    System.out.println("Exception ");
//
//		}
//		System.out.println("FIN consultaCertificacionEnvio");
		
		
//		
//		//String string = "2017-09-22 09:10:42";
//		//DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		String string = "22/09/2017";
//		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//		
//		Date date_reg = format.parse(string);
//		
//		Calendar cal_actual = Calendar.getInstance();	 
//		  
//		Calendar cal_registro = Calendar.getInstance();	
//		cal_registro.setTime(date_reg);
//		cal_registro.add(Calendar.DAY_OF_YEAR, 10);
//			  
//		if(cal_actual.after(cal_registro)){
//			System.out.println("CADUCA");
//			System.out.println("reg "+cal_registro.getTime());
//			System.out.println("act "+cal_actual.getTime());
//		}
//		else{
//			System.out.println("NO CADUCA");
//			System.out.println("reg "+cal_registro.getTime());
//			System.out.println("act "+cal_actual.getTime());
//		}
		  
		
		
	}
	
	
}
