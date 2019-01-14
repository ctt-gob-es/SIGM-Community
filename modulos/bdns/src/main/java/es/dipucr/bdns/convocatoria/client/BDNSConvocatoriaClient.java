package es.dipucr.bdns.convocatoria.client;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;

import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticion;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoria;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosGenerales;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosRespuesta;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Atributos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Emisor;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitante;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmisionDatosGenericos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitudes;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Transmision;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.DatosGenericos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Estado;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.TransmisionDatos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Transmisiones;
import es.dipucr.bdns.api.impl.BDNSAPI;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.common.DatosEspecificos;
import es.dipucr.bdns.common.DatosEspecificosDatosEspecificosRespuestaAbstracta;
import es.dipucr.bdns.common.WSBDNSProperties;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.bdns.objetos.Convocatoria;
import es.dipucr.bdns.objetos.EntidadConvocatoria;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.minhap.igae.ws.convocatoria.ConvocatoriaProxy;

public class BDNSConvocatoriaClient {
	
	private static final Logger logger = Logger.getLogger(BDNSConvocatoriaClient.class);
	
	public static Atributos obtenerAtributos(IRuleContext rulectx) throws ISPACRuleException {
		/**
		 * El identificador de petición se formará concatenando el código del
		 * organismo asignado por DIR3 y un número secuencial de petición. El
		 * sistema no validará si el número de peticiones recibidas es
		 * correlativo. Longitud 26
		 * **/
		String idPeticion = "";
		try {
			idPeticion = BDNSAPI.getIdPeticion((ClientContext) rulectx.getClientContext(), rulectx.getClientContext().getAPI().getEntitiesAPI());
			//idPeticion = Constantes.DIR3Dipu+"-"+rulectx.getTaskId();
		} catch (ISPACException e) {
			logger.error("Obtener el id de la petición. "+rulectx.getNumExp()+ " en el método BDNSAPI.getIdPeticion - "+e.getMessage(), e);
			throw new ISPACRuleException("Obtener el id de la petición. "+rulectx.getNumExp()+ " en el método BDNSAPI.getIdPeticion - "+e.getMessage(), e);
		}
		/**
		 * Funcionamiento síncrono: valor 1
		 * **/
		int numElementos = 1;
		/**
		 * Marca de tiempo en la que se ha realizado la petición.
		 * **/
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
		String timeStamp = sdf.format(cal.getTime());

		/**
		 * ESTADO. Bloque único. Contiene información sobre el estado de la
		 * petición. Información sobre la situación de la petición. Tiene
		 * sentido en el mensaje de respuesta.
		 * **/
		//Estado estado = null;

		String codigoCertificado = Constantes.COD_APP_CONVOCATORIAS;

		Atributos atributos = new Atributos();
		if(codigoCertificado!=null){
			atributos.setCodigoCertificado(codigoCertificado);
		}		
		if(idPeticion!=null){
			atributos.setIdPeticion(idPeticion);
		}		
		atributos.setNumElementos(numElementos);
		if(timeStamp!=null){
			atributos.setTimeStamp(timeStamp);
		}		
		return atributos;
	}
	
	public static SolicitudTransmision[] obtenerSolicitudes(IRuleContext rulectx, String idPeticion, TipoMovimiento tipoMovimiento, Convocatoria convocatoria) {

		SolicitudTransmisionDatosGenericos datosGenericos = null;
		if(obtenerDatosGenericosTramision(idPeticion)!=null){
			datosGenericos = obtenerDatosGenericosTramision(idPeticion);
		}
		
		DatosEspecificos datosEspecificos = null;
		if(obtenerDatosEspecificos(rulectx, tipoMovimiento, convocatoria)!=null){
			datosEspecificos = obtenerDatosEspecificos(rulectx, tipoMovimiento, convocatoria);
		}
		
		SolicitudTransmision[] solicitudes = new SolicitudTransmision[1];
		/**
		 * Se repite de 1 a n veces por esquema. Contiene los datos relativos a
		 * una solicitud de transmisión. Para el modo síncrono del servicio sólo
		 * se admite que aparezca una única vez (sólo se acepta una
		 * transmisión/solicitud por petición).
		 * **/
		SolicitudTransmision solTram = new SolicitudTransmision();
		if(datosGenericos!=null){
			solTram.setDatosGenericos(datosGenericos);
		}
		if(datosEspecificos!=null){
			solTram.setDatosEspecificos(datosEspecificos);
		}
		
		solicitudes[0] = solTram;
		return solicitudes;
	}
	
	
	private static SolicitudTransmisionDatosGenericos obtenerDatosGenericosTramision(String idPeticion) {
		// Contiene datos del emisor de la solicitud
		String nifEmisor = Constantes.NIF_IGAE;
		String nombreEmisor = Constantes.NOMBRE_IGAE;
		Emisor emisor = new Emisor(nifEmisor, nombreEmisor);
		// Contiene datos del solicitante de la transacción.
		String identificadorSolicitante = Constantes.DIR3_DIPUCR;
		String nombreSolicitante = Constantes.NOMBRE_DIPUCR;
		//String finalidad = null;
		//String consentimiento = null;
		Solicitante solicitante = new Solicitante();
		solicitante.setIdentificadorSolicitante(identificadorSolicitante);
		solicitante.setNombreSolicitante(nombreSolicitante);
		
		// El contenido de este bloque se ignora.
		//Titular titular = null;
		String codigoCertificado = Constantes.COD_APP_CONVOCATORIAS;
		// En caso de ser intercambio síncrono el identificador de solicitud
		// debe coincidir con el identificador de petición.
		String idSolicitud = idPeticion;
		//String idTransmision = null;
		//String fechaGeneracion = null;
		// Contiene los datos del certificado que se va a solicitar.
		Transmision transmision = new Transmision();
		transmision.setCodigoCertificado(codigoCertificado);
		transmision.setIdSolicitud(idSolicitud);
		
		SolicitudTransmisionDatosGenericos datosGenericos = new SolicitudTransmisionDatosGenericos();
		datosGenericos.setEmisor(emisor);
		datosGenericos.setSolicitante(solicitante);
		datosGenericos.setTransmision(transmision);
		
		return datosGenericos;
	}	
	
	private static DatosEspecificos obtenerDatosEspecificos(IRuleContext rulectx, TipoMovimiento tipoMovimiento, Convocatoria convocatoria) {
		DatosEspecificosDatosEspecificosPeticion datosEspecificosPeticion = obtenerDatosEspecificosPeticion(rulectx, tipoMovimiento, convocatoria);
		//DatosEspecificosDatosEspecificosRespuesta datosEspecificosRespuesta = null;
		DatosEspecificos datosEspecificos = new DatosEspecificos();
		if(datosEspecificosPeticion!=null){
			datosEspecificos.setDatosEspecificosPeticion(datosEspecificosPeticion);
		}
		
		return datosEspecificos;
	}
	
	private static DatosEspecificosDatosEspecificosPeticion obtenerDatosEspecificosPeticion(IRuleContext rulectx, TipoMovimiento tipoMovimiento, Convocatoria convocatoria) {
		// Órgano gestor (codificación DIR3).
		String organoGestor = Constantes.DIR3_DIPUCR;
		DatosEspecificosDatosEspecificosPeticionDatosGenerales datosGenerales = new DatosEspecificosDatosEspecificosPeticionDatosGenerales();
		if(organoGestor!=null){
			datosGenerales.setOrganoGestor(organoGestor);
		}
		if(tipoMovimiento!=null){
			datosGenerales.setTipoMovimiento(tipoMovimiento);
		}

		String idConvocatoria = convocatoria.getIdConvocatoria();
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov datosGeneralesCov = convocatoria.getDatosGeneralesCov();
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora datosBaseReguladora = convocatoria.getDatosBaseReguladora();
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion datosSolicitudJustificacionFinanciacion = convocatoria.getDatosSolicitudJustificacionFinanciacion();
		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos otrosDatos = convocatoria.getOtrosDatos();
		DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto extracto = convocatoria.getExtracto();
		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos otrosDocumentos = convocatoria.getOtrosDocumentos();
		
		DatosEspecificosDatosEspecificosPeticionConvocatoria convocatoriaIGAE = null;
		if(idConvocatoria!=null || datosGeneralesCov!=null || datosBaseReguladora!=null || datosSolicitudJustificacionFinanciacion!=null || otrosDatos!=null || extracto!=null || otrosDocumentos!=null){
			convocatoriaIGAE = new DatosEspecificosDatosEspecificosPeticionConvocatoria();
		}
		if(idConvocatoria!=null){
			convocatoriaIGAE.setIdConvocatoria(idConvocatoria);
		}
		if(datosGeneralesCov!=null){
			convocatoriaIGAE.setDatosGeneralesCov(datosGeneralesCov);
		}
		if(datosBaseReguladora!=null){
			convocatoriaIGAE.setDatosBaseReguladora(datosBaseReguladora);
		}
		if(datosSolicitudJustificacionFinanciacion!=null){
			convocatoriaIGAE.setDatosSolicitudJustificacionFinanciacion(datosSolicitudJustificacionFinanciacion);
		}
		if(otrosDatos!=null){
			convocatoriaIGAE.setOtrosDatos(otrosDatos);
		}
		if(extracto!=null){
			convocatoriaIGAE.setExtracto(extracto);
		}
		if(otrosDocumentos!=null){
			convocatoriaIGAE.setOtrosDocumentos(otrosDocumentos);
		}
		
		
		DatosEspecificosDatosEspecificosPeticion datosEspecificosPeticion = null;
		if(datosGenerales!=null){
			datosEspecificosPeticion = new DatosEspecificosDatosEspecificosPeticion();
			datosEspecificosPeticion.setDatosGenerales(datosGenerales);
		}
		if(convocatoriaIGAE!=null){
			datosEspecificosPeticion.setConvocatoria(convocatoriaIGAE);
		}
		return datosEspecificosPeticion;
	}


	public static Respuesta envioPeticionConvocatoria(IRuleContext rulectx, EntidadConvocatoria entidadConv) throws ISPACException {
	
		Respuesta respuesta = null;
		
		String url = WSBDNSProperties.getURL(ServiciosWebConfiguration.BDNS_APP_CONVOCATORIAS);
		ConvocatoriaProxy convocatoria = new ConvocatoriaProxy(url);
		
		/**
		 * TipoMovimiento
		 * Valores:
		 * A: para realizar alta de convocatorias
		 * B: para realizar la baja de convocatorias
		 * M: para realizar la modificación de convocatorias
		 * **/ 
		Atributos atributos = BDNSConvocatoriaClient.obtenerAtributos(rulectx);
		
		String tipoMovimiento = TramitesUtil.getDatosEspecificosOtrosDatos(rulectx.getClientContext(), rulectx.getTaskProcedureId());
		TipoMovimiento tipoMov = null;
		if(tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_ALTA)){
			tipoMov = TipoMovimiento.A;
		}
		if(tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_MODIFICACION)){
			tipoMov = TipoMovimiento.M;
		}
		if(tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_BAJA)){
			tipoMov = TipoMovimiento.B;
		}
		
		Convocatoria convocaObj = BDNSDipucrFuncionesComunes.obtenerConvocatoria(rulectx, tipoMov, entidadConv);
		SolicitudTransmision[] solicitudTransmision = BDNSConvocatoriaClient.obtenerSolicitudes(rulectx, atributos.getIdPeticion(), tipoMov, convocaObj);
		
		try {
			Peticion peticionSimple = new Peticion();
			Solicitudes solicitudes = new Solicitudes(solicitudTransmision, "identificador");
			peticionSimple.setSolicitudes(solicitudes);
			peticionSimple.setAtributos(atributos);
			respuesta = convocatoria.peticion(peticionSimple);
		} catch (RemoteException e) {
			logger.error("Error al enviar la convocatoria. "+rulectx.getNumExp()+ " en los servicios web - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar la convocatoria. "+rulectx.getNumExp()+ " en los servicios web - "+e.getMessage(), e);
		}
		
		return respuesta;
	}

	public static boolean cargaIdentificadorConvo(IRuleContext rulectx, Respuesta respuesta, EntidadConvocatoria convocatoria) throws ISPACRuleException {
		boolean correcto = true;
		Transmisiones trasmisiones = respuesta.getTransmisiones();
		TransmisionDatos[] vTrasmisionesDatos = trasmisiones.getTransmisionDatos();
		for (int i = 0; i < vTrasmisionesDatos.length; i++) {
			TransmisionDatos trasmisionesDatos = vTrasmisionesDatos[i];
			DatosEspecificos datosEspec = trasmisionesDatos.getDatosEspecificos();
			DatosEspecificosDatosEspecificosRespuestaAbstracta datosRespuestaAbstracta = datosEspec.getDatosEspecificosRespuesta();
			
			DatosEspecificosDatosEspecificosRespuesta datosRespuesta = (DatosEspecificosDatosEspecificosRespuesta)datosRespuestaAbstracta;
			
			if(datosRespuesta.getIdConvocatoria()!=null){
				IItem bdIGAEConv = BDNSDipucrFuncionesComunes.obtenerItemConvocatoria(rulectx);
				
				try {
					bdIGAEConv.set("IDCONVOCATORIA", datosRespuesta.getIdConvocatoria());
					bdIGAEConv.store(rulectx.getClientContext());
					convocatoria.setIdConvocatoria(datosRespuesta.getIdConvocatoria());
				} catch (ISPACException e) {
					logger.error("Error al almacenar el identificador de la convocatoria. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
					throw new ISPACRuleException("Error al almacenar el identificador de la convocatoria. "+rulectx.getNumExp()+ " - " + e.getMessage(), e);
				}
				
				
			}
			else{
				correcto = false;
				logger.error("Error al enviar la convocatoria. "+rulectx.getNumExp()+ ": Codigo error: "+datosRespuesta.getCodigoEstadoSo()+" - Descripcion: "+datosRespuesta.getLiteralErrorSo());
				throw new ISPACRuleException("Error al enviar la convocatoria. "+rulectx.getNumExp()+ ": Codigo error: "+datosRespuesta.getCodigoEstadoSo()+" - Descripcion: "+datosRespuesta.getLiteralErrorSo());
			}				
		}
		return correcto;
		
		
	}

	public static void imprimeInforme(Respuesta respuestaConv,Document documentInforme, IRuleContext rulectx) throws ISPACRuleException {
		try {			
			documentInforme.add(new Phrase("\n"));

			documentInforme.add(new Phrase("INFORME DE LA BASE DE DATOS NACIONAL DE SUBVENCIONES"));
			documentInforme.add(new Phrase("\n"));
			documentInforme.add(new Phrase("\n"));
			
			documentInforme.add(new Phrase("************************************************ATRIBUTOS************************************************"));
			documentInforme.add(new Phrase("\n"));
			
			conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Atributos atributos = respuestaConv.getAtributos();
			
			documentInforme.add(new Phrase(" - Id. petición: "+ atributos.getIdPeticion()));
			documentInforme.add(new Phrase("\n"));
			
			Estado estado = atributos.getEstado();
			documentInforme.add(new Phrase(" - Estado: "));
			documentInforme.add(new Phrase("\n"));
				documentInforme.add(new Phrase("    * Codigo estado: "+estado.getCodigoEstado()));
				documentInforme.add(new Phrase("\n"));
				documentInforme.add(new Phrase("    * Codigo estado secundario: "+estado.getCodigoEstadoSecundario()));
				documentInforme.add(new Phrase("\n"));
				if(estado.getCodigoEstadoSecundario().equals("1000")){
					documentInforme.add(new Phrase("    * Literal error: Solicitud correcta"));
				}
				else{
					documentInforme.add(new Phrase("    * Literal error: "+estado.getLiteralError()));
				}
				
				documentInforme.add(new Phrase("\n"));
				
			documentInforme.add(new Phrase(" - Código certificado: "+ atributos.getCodigoCertificado()));
			documentInforme.add(new Phrase("\n"));
			
			Transmisiones transmision = respuestaConv.getTransmisiones();
			TransmisionDatos[] vTransm = transmision.getTransmisionDatos();
			
			for(int i=0; i < vTransm.length; i++){
				TransmisionDatos transmisionDatos = vTransm[i];
				DatosGenericos datosGenericos = transmisionDatos.getDatosGenericos();
				conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Emisor emisor = datosGenericos.getEmisor();
				documentInforme.add(new Phrase(" - Emisor: "+emisor.getNifEmisor()+" - "+emisor.getNombreEmisor()));
				documentInforme.add(new Phrase("\n"));
				conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Solicitante solicitante = datosGenericos.getSolicitante();
				documentInforme.add(new Phrase(" - Solicitante: "+solicitante.getIdentificadorSolicitante()+" - "+solicitante.getNombreSolicitante()));
				documentInforme.add(new Phrase("\n"));
				documentInforme.add(new Phrase("\n"));
				
				DatosEspecificos datosEspecificos = transmisionDatos.getDatosEspecificos();
				DatosEspecificosDatosEspecificosRespuestaAbstracta datosResp = datosEspecificos.getDatosEspecificosRespuesta();
				
				DatosEspecificosDatosEspecificosRespuesta datosRespuesta = (DatosEspecificosDatosEspecificosRespuesta)datosResp;
				documentInforme.add(new Phrase("************************************************RESPUESTA************************************************"));
				documentInforme.add(new Phrase("\n"));
				documentInforme.add(new Phrase(" - Id. convocatoria: "+ datosRespuesta.getIdConvocatoria()));
				documentInforme.add(new Phrase("\n"));
				documentInforme.add(new Phrase(" - Código estado: "+ datosRespuesta.getCodigoEstadoSo()));
				documentInforme.add(new Phrase("\n"));
				if(datosRespuesta.getCodigoEstadoSo().equals("1000")){
					documentInforme.add(new Phrase(" - Error: Solicitud correcta"));
				}
				else{
					documentInforme.add(new Phrase(" - Error: "+ datosRespuesta.getLiteralErrorSo()));
				}
				if(datosRespuesta.getCodigoEstadoSecundarioSo()!=null){
					documentInforme.add(new Phrase("\n"));
					documentInforme.add(new Phrase(" - Código estado secundario: "+ datosRespuesta.getCodigoEstadoSecundarioSo()));
				}
			}
			
				
				
			} catch (BadElementException e) {
				logger.error("Error al generar el informe. "+rulectx.getNumExp()+ ": - : "+e.getMessage(), e);
				throw new ISPACRuleException("Error al generar el informe. "+rulectx.getNumExp()+ ": - : "+e.getMessage(), e);
			} catch (DocumentException e) {
				logger.error("Error al generar el informe. "+rulectx.getNumExp()+ ": - : "+e.getMessage(), e);
				throw new ISPACRuleException("Error al generar el informe. "+rulectx.getNumExp()+ ": - : "+e.getMessage(), e);
			}
		}

}
