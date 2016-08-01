package es.dipucr.bdns.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticion;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoria;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoCastellano;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitudAbierto;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacion;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoCastellano;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosConcesionPublicable;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosInstrumentos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosRegiones;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSubvencionNominativa;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosTiposBeneficiario;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosGenerales;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PieFirmaExtracto;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TextoExtracto;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Atributos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Emisor;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitante;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmisionDatosGenericos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Transmision;
import es.dipucr.bdns.api.impl.BDNSAPI;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.bdns.objetos.Convocatoria;
import es.dipucr.bdns.objetos.EntidadConvocatoria;
import es.dipucr.bdns.objetos.Tipo;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class BDNSDipucrFuncionesComunes {

	private static final Logger logger = Logger.getLogger(BDNSDipucrFuncionesComunes.class);

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

		String codigoCertificado = Constantes.codConvocatoria;

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
		String organoGestor = Constantes.DIR3Dipu;
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

	private static SolicitudTransmisionDatosGenericos obtenerDatosGenericosTramision(String idPeticion) {
		// Contiene datos del emisor de la solicitud
		String nifEmisor = Constantes.NIFIGAE;
		String nombreEmisor = Constantes.nombreIGAE;
		Emisor emisor = new Emisor(nifEmisor, nombreEmisor);
		// Contiene datos del solicitante de la transacción.
		String identificadorSolicitante = Constantes.DIR3Dipu;
		String nombreSolicitante = Constantes.nombreDipu;
		//String finalidad = null;
		//String consentimiento = null;
		Solicitante solicitante = new Solicitante();
		solicitante.setIdentificadorSolicitante(identificadorSolicitante);
		solicitante.setNombreSolicitante(nombreSolicitante);
		
		// El contenido de este bloque se ignora.
		//Titular titular = null;
		String codigoCertificado = Constantes.codConvocatoria;
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
	

	public static Convocatoria obtenerConvocatoria(IRuleContext rulectx, TipoMovimiento tipoMov) throws ISPACRuleException {
		Convocatoria convocatoria = new Convocatoria();
		try {
			
			EntidadConvocatoria entidadConv = cargaEntidadConvocatoria(rulectx);
			//Obligatorio en movimientos de Modificación y Baja
			if (!tipoMov.equals(TipoMovimiento.A)) {
				convocatoria.setIdConvocatoria(entidadConv.getIdConvocatoria());
				
			}
			//Requerido en el método de alta y modificación. Sin contenido en método de baja.
			if(!tipoMov.equals(TipoMovimiento.B)){
				DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov datosConv = obtenerDatosGeneralesConv(rulectx);
				if(datosConv!=null){
					convocatoria.setDatosGeneralesCov(datosConv);
				}
				
			}
			//Requerido en el método de alta y modificación. Sin contenido en método de baja.
			if(!tipoMov.equals(TipoMovimiento.B)){
				DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora baseReg = obteneDatosBaseReguladora(entidadConv);
				if(baseReg!=null){
					convocatoria.setDatosBaseReguladora(baseReg);
				}				
			}
			
			//DatosSolicitudJustificacionFinanciacion. INFORMACION DE LAS SOLICITUDES, JUSTIFICACION Y TIPOS DE FINANCIACION
			//Requerido en el método de alta y modificación. Sin contenido en método de baja.
			if(!tipoMov.equals(TipoMovimiento.B)){
				/**Condición de período de admisión de solicitudes permanentemente abierto. Indica si la convocatoria mantiene permanente abierto el período de admisión de solicitudes.
				Valores posibles:
				- '0': cerrado
				- '1': abierto**/
				DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion solicitudJustif = obtenerDatosSolicitudJustificacionFinanciacion(entidadConv);
				if(solicitudJustif!=null){
					convocatoria.setDatosSolicitudJustificacionFinanciacion(solicitudJustif);
				}				
			}
			//OTROS DATOS DE LA CONVOCATORIA. Requerido en el método de alta y modificación. Sin contenido en método de baja.
			if(!tipoMov.equals(TipoMovimiento.B)){
				DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos convocOtrosDatos = obtenerOtrosDatos(entidadConv);
				if(convocOtrosDatos!=null){
					convocatoria.setOtrosDatos(convocOtrosDatos);
				}				
			}
			//INFORMACION DEL EXTRACTO DE LA CONVOCATORIA. Opcional en el método de alta y modificación. Sin contenido en método de baja.
			if(!tipoMov.equals(TipoMovimiento.B)){
				DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto convocExtra = obtenerExtracto(entidadConv);
				if(convocExtra!=null){
					convocatoria.setExtracto(convocExtra);
				}				
			}
			//convocatoria.setOtrosDocumentos(otrosDocumentos);			

			// Referencia interna del gestor para la convocatoria
		} catch (ISPACRuleException e) {
			logger.error("Error al acceder a la tabla BDNS_IGAE_CONVOCATORIA. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al acceder a la tabla BDNS_IGAE_CONVOCATORIA. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
		}
		return convocatoria;
	}

	private static DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto obtenerExtracto(EntidadConvocatoria entidadConv) throws ISPACRuleException {
		
		String diarioOficial = null;
		if(entidadConv.getDiarioOficial()!=null){
			diarioOficial = entidadConv.getDiarioOficial();
		}
		String tituloExtracto = null;
		if(entidadConv.getTituloExtracto()!=null){
			tituloExtracto = entidadConv.getTituloExtracto();
		}
		TextoExtracto textoExtracto = null;
		if(entidadConv.getTextoExtracto()!=null){
			textoExtracto = new TextoExtracto(entidadConv.getTextoExtracto());
		}
		
		Date fechaFirma=null;
		if(entidadConv.getFechaFirma()!=null){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				fechaFirma = formatter.parse(entidadConv.getFechaFirma().toString());
			} catch (ParseException e) {
				logger.error("Error al obtener la fecha firma. "+entidadConv.getFechaFirma()+ " - "+e.getMessage(), e);
				throw new ISPACRuleException("Error al obtener la fecha firma. "+entidadConv.getFechaFirma()+ " - "+e.getMessage(), e);
			}
		}
		String lugarFirma = null;
		if(entidadConv.getLugarFirma()!=null){
			lugarFirma = entidadConv.getLugarFirma();
		}
		String firmante = null;
		if(entidadConv.getFirmante()!=null){
			firmante = entidadConv.getFirmante();
		}
		PieFirmaExtracto pieFirmaExtracto = null;
		if(fechaFirma!=null || lugarFirma!=null || firmante!=null){
			pieFirmaExtracto = new PieFirmaExtracto();
		}
		if(fechaFirma!=null){
			pieFirmaExtracto.setFechaFirma(fechaFirma);
		}
		if(lugarFirma!=null){
			pieFirmaExtracto.setLugarFirma(lugarFirma);
		}
		if(firmante!=null){
			pieFirmaExtracto.setFirmante(firmante);
		}
		
		DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoCastellano extractoCastellano = null;
		if(tituloExtracto!=null || textoExtracto!=null || pieFirmaExtracto!=null){
			extractoCastellano = new DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoCastellano();
		}
		if(tituloExtracto!=null){
			extractoCastellano.setTituloExtracto(tituloExtracto);
		}
		if(textoExtracto!=null){
			extractoCastellano.setTextoExtracto(textoExtracto);
		}
		if(pieFirmaExtracto!=null){
			extractoCastellano.setPieFirmaExtracto(pieFirmaExtracto);
		}
		
		DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto extracto = null;
		if(diarioOficial!=null || extractoCastellano!=null){
			extracto = new DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto();
		}
		if(diarioOficial!=null){
			extracto.setDiarioOficial(diarioOficial);
		}
		if(extractoCastellano!=null){
			extracto.setExtractoCastellano(extractoCastellano);
		}
		return extracto;
	}

	private static DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos obtenerOtrosDatos(EntidadConvocatoria entidadConv) {

		String autorizacionADE = null;
		if(entidadConv.getAutorizacionADE()!=null){
			autorizacionADE = entidadConv.getAutorizacionADE();
		}
		String referenciaUE = null;
		if(entidadConv.getReferenciaUE()!=null){
			referenciaUE = entidadConv.getReferenciaUE();
		}
		String reglamento = null;
		if(entidadConv.getReglamento()!=null){
			reglamento = entidadConv.getReglamento();
		}
		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos objetivos = null;
		if(entidadConv.getObjetivo()!=null){
			String[] vobjetivos = entidadConv.getObjetivo();			
			if(vobjetivos!=null){
				objetivos = new DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos();
				objetivos.setObjetivo(vobjetivos);
			}
			
		}

		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado ayudaEstado = null;
		
		if(autorizacionADE!=null || referenciaUE!=null || reglamento!=null || objetivos!=null){
			ayudaEstado = new DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado();
		}
		if(autorizacionADE!=null){
			ayudaEstado.setAutorizacionADE(autorizacionADE);
		}
		if(referenciaUE!=null){
			ayudaEstado.setReferenciaUE(referenciaUE);
		}
		if(reglamento!=null){
			ayudaEstado.setReglamento(reglamento);
		}
		if(objetivos!=null){
			ayudaEstado.setObjetivos(objetivos);
		}
		
		

		String finalidad = null;
		if(entidadConv.getFinalidad()!=null){
			finalidad = entidadConv.getFinalidad();
		}
		String impactoGenero = null;
		if(entidadConv.getImpactoGenero()!=null){
			impactoGenero = entidadConv.getImpactoGenero();
		}
		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosConcesionPublicable concesionPublicable = null;
		if(entidadConv.getConcesionPublicable()!=null){
			if(entidadConv.getConcesionPublicable().equals("SI")){
				concesionPublicable = DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosConcesionPublicable.fromValue("1");
			}
			else{
				concesionPublicable = DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosConcesionPublicable.fromValue("0");
			}
			
		}
				
		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSubvencionNominativa subvencionNominativa= null;
		if(entidadConv.getSubvencionNominativa()!=null){
			if(entidadConv.getSubvencionNominativa().equals("SI")){
				subvencionNominativa = DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSubvencionNominativa.fromValue("1");
			}
			else{
				subvencionNominativa = DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSubvencionNominativa.fromValue("0");
			}
			
		}
		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores sectores = null;
		if(entidadConv.getSector()!=null){
			String[] vsectores = entidadConv.getSector();
			if(vsectores!=null){
				sectores = new DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores();
				sectores.setSector(vsectores);
			}
			
		}
		
		
		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosRegiones regiones = null;
		if(entidadConv.getRegion()!=null){
			String[] vregiones = entidadConv.getRegion();
			if(vregiones!=null){
				regiones = new DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosRegiones();
				regiones.setRegion(vregiones);
			}			
		}
		
		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosInstrumentos instrumentos = null;
		if(entidadConv.getInstrumento()!=null){
			String[] vinstrumentos = entidadConv.getInstrumento();
			if(vinstrumentos!=null){
				instrumentos = new DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosInstrumentos();
				instrumentos.setInstrumento(vinstrumentos);
			}
		}
		
		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosTiposBeneficiario tiposBeneficiario = null;
		if(entidadConv.getTipoBeneficiario()!=null){
			String[] vtiposBeneficiario = entidadConv.getTipoBeneficiario();
			if(vtiposBeneficiario!=null){
				tiposBeneficiario = new DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosTiposBeneficiario();
				tiposBeneficiario.setTipoBeneficiario(vtiposBeneficiario);
			}
		}
		DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos otrosDatos = null;
		if(sectores!=null || regiones!=null || ayudaEstado!=null || instrumentos!=null || tiposBeneficiario!=null || finalidad!=null || impactoGenero!=null || concesionPublicable!=null || subvencionNominativa!=null){
			otrosDatos = new DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos();
		}
		if(sectores!=null){
			otrosDatos.setSectores(sectores);
		}
		if(regiones!=null){
			otrosDatos.setRegiones(regiones);
		}
		if(ayudaEstado!=null){
			otrosDatos.setAyudaEstado(ayudaEstado);
		}
		if(instrumentos!=null){
			otrosDatos.setInstrumentos(instrumentos);
		}
		if(tiposBeneficiario!=null){
			otrosDatos.setTiposBeneficiario(tiposBeneficiario);
		}
		if(finalidad!=null){
			otrosDatos.setFinalidad(finalidad);
		}
		if(impactoGenero!=null){
			otrosDatos.setImpactoGenero(impactoGenero);
		}
		if(concesionPublicable!=null){
			otrosDatos.setConcesionPublicable(concesionPublicable);
		}
		if(subvencionNominativa!=null){
			otrosDatos.setSubvencionNominativa(subvencionNominativa);
		}
		return otrosDatos;
	}

	private static DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion obtenerDatosSolicitudJustificacionFinanciacion(EntidadConvocatoria entidadConv) throws ISPACRuleException {
		
		String inicioSolicitud=null;
		if(entidadConv.getInicioSolicitud()!=null){
			inicioSolicitud = entidadConv.getInicioSolicitud();
		}
		Date fechaInicioSolicitud=null;
		if(entidadConv.getFechaInicioSolicitud()!=null){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				//String dateInicio = entidadConv.getFechaFinSolicitud().toString();
				String strDate = formatter.format(entidadConv.getFechaInicioSolicitud());
				fechaInicioSolicitud = formatter.parse(strDate);
				//logger.warn("dateInicio "+fechaInicioSolicitud.toString());
			} catch (ParseException e) {
				logger.error("Error al obtener la fechaInicioSolicitud. "+entidadConv.getFechaInicioSolicitud()+ " - "+e.getMessage(), e);
				throw new ISPACRuleException("Error al obtener la fechaInicioSolicitud. "+entidadConv.getFechaInicioSolicitud()+ " - "+e.getMessage(), e);
			}
		}
		String finSolicitud = null;
		if(entidadConv.getFinSolicitud()!=null){
			finSolicitud = entidadConv.getFinSolicitud();
		}
		Date fechaFinSolicitud = null;
		if(entidadConv.getFechaFinSolicitud()!=null){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				String dateFin = formatter.format(entidadConv.getFechaFinSolicitud());
				fechaFinSolicitud = formatter.parse(dateFin);
				logger.warn("fechaFinSolicitud "+fechaFinSolicitud.toString());
			} catch (ParseException e) {
				logger.error("Error al obtener la getFechaFinSolicitud. "+entidadConv.getFechaFinSolicitud()+ " - "+e.getMessage(), e);
				throw new ISPACRuleException("Error al obtener la getFechaFinSolicitud. "+entidadConv.getFechaFinSolicitud()+ " - "+e.getMessage(), e);
			}
		}
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitudAbierto peridoAdmision = null;
		if(entidadConv.getAbierto()!=null){
			if(entidadConv.getAbierto().equals("SI")){
				peridoAdmision = DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitudAbierto.fromValue("1");
			}
			else{
				peridoAdmision = DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitudAbierto.fromValue("0");
			}
			
		}
		
		//DENTIFICACIÓN ASOCIADA A LAS SOLICITUDES
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud solicitud = null;
		if(peridoAdmision!=null || inicioSolicitud!=null || fechaInicioSolicitud!=null || finSolicitud!=null || fechaFinSolicitud!=null){
			solicitud = new DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud();
		}
		if(peridoAdmision!=null){
			solicitud.setAbierto(peridoAdmision);
		}
		if(inicioSolicitud!=null){
			solicitud.setInicioSolicitud(inicioSolicitud);
		}
		if(fechaInicioSolicitud!=null){
			solicitud.setFechaInicioSolicitud(fechaInicioSolicitud);
		}
		if(finSolicitud!=null){
			solicitud.setFinSolicitud(finSolicitud);
		}
		if(fechaFinSolicitud!=null){
			solicitud.setFechaFinSolicitud(fechaFinSolicitud);
		}
		
		String sede=null;
		if(entidadConv.getSede()!=null){
			sede = entidadConv.getSede();
		}
		String justificacion=null;
		if(entidadConv.getJustificacion()!=null){
			justificacion = entidadConv.getJustificacion();
		}
		Date fechaJustificacion=null;
		if(entidadConv.getFechaJustificacion()!=null){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				fechaJustificacion = formatter.parse(entidadConv.getFechaJustificacion().toString());
			} catch (ParseException e) {
				logger.error("Error al obtener la getFechaJustificacion. "+entidadConv.getFechaJustificacion()+ " - "+e.getMessage(), e);
				throw new ISPACRuleException("Error al obtener la getFechaJustificacion. "+entidadConv.getFechaJustificacion()+ " - "+e.getMessage(), e);
			}
		}
		
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacion tiposFinanciacion = null;
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion[] financiacion = obtenerTipoFinanciacion(entidadConv);
		if(financiacion!=null && financiacion.length>0){
			tiposFinanciacion = new DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacion();
			tiposFinanciacion.setFinanciacion(financiacion);
		}
		
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE fondosUE = null;
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE[] vfondoUR = obtenerFondosUE(entidadConv);
		if(vfondoUR!=null && vfondoUR.length>0){
			fondosUE = new DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE();
			fondosUE.setFondoUE(vfondoUR);
		}
		
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion justificacionFinanciacion = null;
		if(solicitud!=null || sede!=null || justificacion!=null ||fechaJustificacion!=null || tiposFinanciacion!=null ||fondosUE!=null){
			justificacionFinanciacion = new DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion();
		}
		if(solicitud!=null){
			justificacionFinanciacion.setSolicitud(solicitud);
		}
		if(sede!=null){
			justificacionFinanciacion.setSede(sede);
		}
		if(justificacion!=null){
			justificacionFinanciacion.setJustificacion(justificacion);
		}
		if(fechaJustificacion!=null){
			justificacionFinanciacion.setFechaJustificacion(fechaJustificacion);
		}
		if(tiposFinanciacion!=null){
			justificacionFinanciacion.setTiposFinanciacion(tiposFinanciacion);
		}
		if(fondosUE!=null){
			justificacionFinanciacion.setFondosUE(fondosUE);
		}
		return justificacionFinanciacion;
	}

	private static DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE[] obtenerFondosUE(EntidadConvocatoria entidadConv) {
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE[] fondos = null;
		if(entidadConv.getFondo()!=null){
			fondos = new DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE[entidadConv.getFondo().length];
			for(int i=0; i<entidadConv.getFondo().length; i++){
				DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE fondo = new 
						DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE();
				if(entidadConv.getFondo()[i]!=null && entidadConv.getFondo()[i].getTipoFinanciacion()!=null){
					fondo.setTipoFondo(entidadConv.getFondo()[i].getTipoFinanciacion());
				}
				if(entidadConv.getFondo()[i]!=null && entidadConv.getFondo()[i].getImporteFinanciacion()!=null){
					fondo.setImporteFondo(entidadConv.getFondo()[i].getImporteFinanciacion());
				}
				fondos[i]=fondo;
			}
		}
				
		return fondos;
	}

	private static DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion[] obtenerTipoFinanciacion(EntidadConvocatoria entidadConv) {
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion [] vTipoFinanciacion = null;
		if(entidadConv.getFinanciacion()!=null){
			vTipoFinanciacion = new DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion[entidadConv.getFinanciacion().length];
			for(int i=0; i<entidadConv.getFinanciacion().length; i++){
				DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion financiacion = new 
						DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion();
				if(entidadConv.getFinanciacion()[i]!=null && entidadConv.getFinanciacion()[i].getTipoFinanciacion()!=null){
					financiacion.setTipoFinanciacion(entidadConv.getFinanciacion()[i].getTipoFinanciacion());
				}
				if(entidadConv.getFinanciacion()[i]!=null && entidadConv.getFinanciacion()[i].getImporteFinanciacion()!=null){
					financiacion.setImporteFinanciacion(entidadConv.getFinanciacion()[i].getImporteFinanciacion());
				}
				vTipoFinanciacion[i]= financiacion;
			}
		}		
		return vTipoFinanciacion;
	}

	private static DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora obteneDatosBaseReguladora(EntidadConvocatoria entidadConv) {
		//Nomenclatura de identificación de las bases reguladoras. Texto libre de acuerdo con la nomenclatura utilizada por la Administración correspondiente para la identificación de su normativa.
		String nomenclatura = null;
		if(entidadConv.getNomenclatura()!=null){
			nomenclatura = entidadConv.getNomenclatura();
		}
		//Diario Oficial de las bases reguladoras. Referencia al Diario Oficial de publicación de las bases reguladoras. Valor existente en tabla DIARIOS OFICIALES.
		String diarioOficialBR = null;
		if(entidadConv.getDiarioOficialBR()!=null){
			diarioOficialBR = entidadConv.getDiarioOficialBR();
		}
		//Descripción de las bases reguladoras. Texto del título de la norma que contiene las bases reguladoras que rigen la Convocatoria.
		String descripcionBR = null;
		if(entidadConv.getDescripcionBR()!=null){
			descripcionBR = entidadConv.getDescripcionBR();
		}
		//URL de las BBRR en castellano. Enlace al sitio web que contiene el texto completo en castellano de las bases reguladoras.
		String URLEspBR = null;
		if(entidadConv.getuRLEspBR()!=null){
			URLEspBR = entidadConv.getuRLEspBR();
		}
		//URL de las BBRR en segunda lengua oficial. Enlace al sitio web que contiene el texto completo en segunda lengua oficial de las bases reguladoras.
		String URLengBR = null;
		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora baseReguladora = null;
		if(nomenclatura!=null || diarioOficialBR!=null || descripcionBR!=null || URLEspBR!=null){
			baseReguladora = new DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora();
		}
		if(nomenclatura!=null){
			baseReguladora.setNomenclatura(nomenclatura);
		}
		if(diarioOficialBR!=null){
			baseReguladora.setDiarioOficialBR(diarioOficialBR);
		}
		if(descripcionBR!=null){
			baseReguladora.setDescripcionBR(descripcionBR);
		}
		if(URLEspBR!=null){
			baseReguladora.setURLEspBR(URLEspBR);
		}
		return baseReguladora;
	}

	@SuppressWarnings("resource")
	private static DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov obtenerDatosGeneralesConv(IRuleContext rulectx) throws ISPACRuleException {
		// INFORMACION GENERAL DE LA CONVOCATORIA
		String referenciaExterna = rulectx.getNumExp();
		
		IItem expedienteConv;
		try {
			expedienteConv = ExpedientesUtil.getExpediente(rulectx.getClientContext(), rulectx.getNumExp());
		} catch (ISPACException e) {
			logger.error("Error al obtener el expediente . " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el expediente . " + rulectx.getNumExp() + " - " + e.getMessage(), e);
		}
		String descripcionCov;
		try {
			descripcionCov = expedienteConv.getString("ASUNTO");
		} catch (ISPACException e) {
			logger.error("Error al obtener el asunto del expediente. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el asunto del expediente. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
		}

		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoCastellano documentoCastellano = null;
		IItem docResolucion = obtenerDocResolucionaConvocatoria(rulectx, "Trámite Resolución");
		/********************************************************************************************************************************************************************************/
		/**TODO**/
		/*if(docResolucion==null){
			try {
				docResolucion = DocumentosUtil.getDocumento(rulectx.getClientContext().getAPI().getEntitiesAPI(), 1902608);
			} catch (ISPACException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		/**********************************************************************************************************************************************************************************/

		if (docResolucion != null) {
			File fichero;
			try {
				fichero = DocumentosUtil.getFile(rulectx.getClientContext(), docResolucion.getString("INFOPAG_RDE"), docResolucion.getString("NOMBRE"),
						docResolucion.getString("EXTENSION_RDE"));
			} catch (ISPACException e) {
				logger.error("Error al obtener el fichero a partir del infopag. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
				throw new ISPACRuleException("Error al obtener el fichero a partir del infopag. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			}
			byte[] bFile = new byte[(int) fichero.length()];
			FileInputStream fileInputStream;
			try {
				fileInputStream = new FileInputStream(fichero);
			} catch (FileNotFoundException e) {
				logger.error("Error en la busqueda del documento. " + rulectx.getNumExp() + " - Fichero. " + fichero + " - " + e.getMessage(), e);
				throw new ISPACRuleException("Error en la busqueda del documento. " + rulectx.getNumExp() + " - Fichero. " + fichero + " - " + e.getMessage(), e);
			}
			try {
				fileInputStream.read(bFile);
			} catch (IOException e) {
				logger.error("Error al leer el documento . " + rulectx.getNumExp() + " - " + bFile + " - " + e.getMessage(), e);
				throw new ISPACRuleException("Error al leer el documento . " + rulectx.getNumExp() + " - " + bFile + " - " + e.getMessage(), e);
			}
			try {
				fileInputStream.close();
			} catch (IOException e) {
				logger.error("Error al cerrar el documento . " + rulectx.getNumExp() + " - " + fileInputStream + " - " + e.getMessage(), e);
				throw new ISPACRuleException("Error al cerrar el documento . " + rulectx.getNumExp() + " - " + fileInputStream + " - " + e.getMessage(), e);
			}

			try {
				documentoCastellano = new DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoCastellano(docResolucion.getString("NOMBRE")+"."+docResolucion.getString("EXTENSION_RDE"), bFile);
			} catch (ISPACException e) {
				logger.error("Error al tarer el nombre del documento. " + rulectx.getNumExp() + " - " + docResolucion + " - " + e.getMessage(), e);
				throw new ISPACRuleException("Error al tarer el nombre del documento. " + rulectx.getNumExp() + " - " + docResolucion + " - " + e.getMessage(), e);
			}
		}

		DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov datosGeneralesCov = null;
		if(referenciaExterna!=null || descripcionCov!=null || documentoCastellano!=null){
			datosGeneralesCov = new DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov();
		}
		if(referenciaExterna!=null){
			datosGeneralesCov.setReferenciaExterna(referenciaExterna);
		}
		if(descripcionCov!=null){
			datosGeneralesCov.setDescripcionCov(descripcionCov);
		}
		if(documentoCastellano!=null){
			datosGeneralesCov.setDocumentoCastellano(documentoCastellano);
		}
		
		return datosGeneralesCov;
	}

	private static EntidadConvocatoria cargaEntidadConvocatoria(IRuleContext rulectx) throws ISPACRuleException {
		EntidadConvocatoria convocatoria = new EntidadConvocatoria();
		// Número para la identificación de la convocatoria anual
		// asignado por la IGAE.
		Iterator<IItem> itConsulta = ConsultasGenericasUtil.queryEntities(rulectx, "BDNS_IGAE_CONVOCATORIA", "NUMEXP='" + rulectx.getNumExp() + "'");
		while (itConsulta.hasNext()) {
			IItem consu = itConsulta.next();
			if (consu != null) {
				try {
					if(consu.getString("IDCONVOCATORIA")!=null){
						convocatoria.setIdConvocatoria(consu.getString("IDCONVOCATORIA"));
					}
					if(consu.getString("DESCRIPCIONCOV")!=null){
						convocatoria.setDescripcionCov(consu.getString("DESCRIPCIONCOV"));
					}
					if(consu.getString("NOMENCLATURA")!=null){
						convocatoria.setNomenclatura(consu.getString("NOMENCLATURA"));
					}
					if(consu.getString("DIARIOOFICIALBR")!=null){
						String diario = consu.getString("DIARIOOFICIALBR");
						String [] vDiario = diario.split(" - ");
						if(vDiario.length >1){
							convocatoria.setDiarioOficialBR(vDiario[0]);
						}
						
					}
					if(consu.getString("DESCRIPCIONBR")!=null){
						convocatoria.setDescripcionBR(consu.getString("DESCRIPCIONBR"));
					}
					if(consu.getString("URLESPBR")!=null){
						convocatoria.setuRLEspBR(consu.getString("URLESPBR"));
					}
					if(consu.getString("ABIERTO")!=null){
						convocatoria.setAbierto(consu.getString("ABIERTO"));
					}
					if(consu.getString("INICIOSOLICITUD")!=null){
						convocatoria.setInicioSolicitud(consu.getString("INICIOSOLICITUD"));
					}
					if(consu.getString("FECHAINICIOSOLICITUD")!=null){
						convocatoria.setFechaInicioSolicitud(consu.getDate("FECHAINICIOSOLICITUD"));
					}
					if(consu.getString("FINSOLICITUD")!=null){
						convocatoria.setFinSolicitud(consu.getString("FINSOLICITUD"));
					}
					if(consu.getString("FECHAFINSOLICITUD")!=null){
						convocatoria.setFechaFinSolicitud(consu.getDate("FECHAFINSOLICITUD"));
					}
					if(consu.getString("SEDE")!=null){
						convocatoria.setSede(consu.getString("SEDE"));
					}
					if(consu.getString("JUSTIFICACION")!=null){
						convocatoria.setJustificacion(consu.getString("JUSTIFICACION"));
					}
					if(consu.getString("FECHAJUSTIFICACION")!=null){
						convocatoria.setFechaJustificacion(consu.getDate("FECHAJUSTIFICACION"));
					}
					if(consu.getInt("ID")>0){
						Iterator<ItemBean> itConsultaTF = queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='TIPOFINANCIACION' ORDER BY ID ASC");
						Iterator<ItemBean> itConsultaIF = queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='IMPORTEFINANCIACION' ORDER BY ID ASC");
						Vector<Tipo> financiacion = new Vector<Tipo>();
						while (itConsultaTF.hasNext()) {
							ItemBean consuTF = itConsultaTF.next();
							Tipo tipo = new Tipo();
							if (consuTF != null && consuTF.getString("VALUE")!=null) {
								tipo.setTipoFinanciacion(consuTF.getString("VALUE"));
								if(itConsultaIF.hasNext()){
									ItemBean consuIF = itConsultaIF.next();
									if (consuIF != null && consuIF.getString("VALUE")!=null) {
										tipo.setImporteFinanciacion(new BigDecimal(consuIF.getString("VALUE")));
										financiacion.add(tipo);
									}
								}
							}
						}
						if(financiacion.size()>0){
							Tipo [] tipo = new Tipo[financiacion.size()];
							convocatoria.setFinanciacion(financiacion.toArray(tipo));
						}
					}
					
					if(consu.getInt("ID")>0){
						Iterator<ItemBean> itConsultaTF = queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='TIPOFONDO' ORDER BY ID ASC");
						Iterator<ItemBean> itConsultaIF = queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='IMPORTEFONDO' ORDER BY ID ASC");
						Vector<Tipo> fondo = new Vector<Tipo>();
						while (itConsultaTF.hasNext()) {
							ItemBean consuTF = itConsultaTF.next();
							Tipo tipo = new Tipo();
							if (consuTF != null && consuTF.getString("VALUE")!=null) {
								tipo.setTipoFinanciacion(consuTF.getString("VALUE"));
								if(itConsultaIF.hasNext()){
									ItemBean consuIF = itConsultaIF.next();
									if (consuIF != null && consuIF.getString("VALUE")!=null) {
										tipo.setImporteFinanciacion(new BigDecimal(consuIF.getString("VALUE")));
										fondo.add(tipo);
									}
								}
							}
						}
						if(fondo.size()>0){
							Tipo [] tipo = new Tipo[fondo.size()];
							convocatoria.setFondo(fondo.toArray(tipo));
						}
					}
					if(consu.getInt("ID")>0){
						Iterator<ItemBean> itConsultaTF = queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='SECTOR' ORDER BY ID ASC");
						Vector<String> sector = new Vector<String>();
						while (itConsultaTF.hasNext()) {
							ItemBean consuTF = itConsultaTF.next();
							if (consuTF != null && consuTF.getString("VALUE")!=null) {
								sector.add(consuTF.getString("VALUE"));
							}
						}
						if(sector.size()>0){
							String [] vSector = new String[sector.size()];
							convocatoria.setSector(sector.toArray(vSector));
						}
					}
						
					if(consu.getString("REGION")!=null){
						Vector<String> sector = new Vector<String>();
						String diario = consu.getString("REGION");
						String [] vDiario = diario.split(" - ");
						if(vDiario.length >1){
							String [] vSector = new String[1];
							vSector[0] = diario;
							convocatoria.setRegion(sector.toArray(vSector));
						}
					}

					if(consu.getString("AUTORIZACIONADE")!=null){
						convocatoria.setAutorizacionADE(consu.getString("AUTORIZACIONADE"));
					}
					if(consu.getString("REFERENCIAUE")!=null){
						//me confundo al poner el campo por lo tanto 
						convocatoria.setReglamento(consu.getString("REFERENCIAUE"));
					}
					if(consu.getString("REGLAMENTOUE")!=null){
						convocatoria.setReferenciaUE(consu.getString("REGLAMENTOUE"));
					}
					
					if(consu.getInt("ID")>0){
						Iterator<ItemBean> itConsultaTF = queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='OBJETIVO' ORDER BY ID ASC");
						Vector<String> sector = new Vector<String>();
						while (itConsultaTF.hasNext()) {
							ItemBean consuTF = itConsultaTF.next();
							if (consuTF != null && consuTF.getString("VALUE")!=null) {
								sector.add(consuTF.getString("VALUE"));
							}
						}
						if(sector.size()>0){
							String [] vSector = new String[sector.size()];
							convocatoria.setObjetivo(sector.toArray(vSector));
						}
					}
					
					if(consu.getInt("ID")>0){
						Iterator<ItemBean> itConsultaTF = queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='INSTRUMENTO' ORDER BY ID ASC");
						Vector<String> sector = new Vector<String>();
						while (itConsultaTF.hasNext()) {
							ItemBean consuTF = itConsultaTF.next();
							if (consuTF != null && consuTF.getString("VALUE")!=null) {
								sector.add(consuTF.getString("VALUE"));
							}
						}
						if(sector.size()>0){
							String [] vSector = new String[sector.size()];
							convocatoria.setInstrumento(sector.toArray(vSector));
						}
					}
					
					if(consu.getInt("ID")>0){
						Iterator<ItemBean> itConsultaTF = queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='TIPOBENEFICIARIO' ORDER BY ID ASC");
						Vector<String> sector = new Vector<String>();
						while (itConsultaTF.hasNext()) {
							ItemBean consuTF = itConsultaTF.next();
							if (consuTF != null && consuTF.getString("VALUE")!=null) {
								sector.add(consuTF.getString("VALUE"));
							}
						}
						if(sector.size()>0){
							String [] vSector = new String[sector.size()];
							convocatoria.setTipoBeneficiario(sector.toArray(vSector));
						}
					}

					if(consu.getString("FINALIDAD")!=null){
						convocatoria.setFinalidad(consu.getString("FINALIDAD"));
					}
					if(consu.getString("IMPACTOGENERO")!=null){
						convocatoria.setImpactoGenero(consu.getString("IMPACTOGENERO"));
					}
					if(consu.getString("CONCESIONPUBLICABLE")!=null){
						convocatoria.setConcesionPublicable(consu.getString("CONCESIONPUBLICABLE"));
					}
					if(consu.getString("SUBVENCIONNOMINATIVA")!=null){
						convocatoria.setSubvencionNominativa(consu.getString("SUBVENCIONNOMINATIVA"));
					}
					if(consu.getString("DIARIOOFICIAL")!=null){
						String diario = consu.getString("DIARIOOFICIAL");
						String [] vDiario = diario.split(" - ");
						if(vDiario.length >1){
							convocatoria.setDiarioOficial(vDiario[0]);
						}
						
						
					}
					if(consu.getString("TITULOEXTRACTO")!=null){
						convocatoria.setTituloExtracto(consu.getString("TITULOEXTRACTO"));
					}
					//Fecha de aprobación
					if(consu.getDate("FECHAFIRMA")!=null){
						convocatoria.setFechaFirma(consu.getDate("FECHAFIRMA"));
					}					
					if(consu.getString("TEXTOEXTRACTO")!=null){
						convocatoria.setTextoExtracto(BDNSDipucrFuncionesComunes.obtenerTextoExtracto(consu.getString("TEXTOEXTRACTO")));
					}
					if(consu.getString("LUGARFIRMA")!=null){
						convocatoria.setLugarFirma(consu.getString("LUGARFIRMA"));
					}
					if(consu.getString("NOMBREFIRMANTE")!=null){
						convocatoria.setFirmante(consu.getString("NOMBREFIRMANTE"));
					}
					
				} catch (ISPACException e) {
					logger.error("Error al obtener la entidad BDNS_IGAE_CONVOCATORIA. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
					throw new ISPACRuleException("Error al obtener la entidad BDNS_IGAE_CONVOCATORIA. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
				}
			}

		}
		return convocatoria;
	}
	
	
	private static String[] obtenerTextoExtracto(String textoExtracto) {
		
		String [] vtextoExt = textoExtracto.split("\n");
		return vtextoExt;
	}

	public static Iterator<ItemBean>  queryEntitiesMultivalor(IRuleContext rulectx, String nombreTabla, String strQuery) throws ISPACRuleException{
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			 /***********************************************************************/
			Vector<ItemBean> valores = new Vector<ItemBean>();
			String query="SELECT FIELD,REG_ID,VALUE FROM "+nombreTabla+" WHERE "+strQuery;
	        ResultSet datos = cct.getConnection().executeQuery(query).getResultSet();
	        if(datos!=null)
	      	{
	        	while(datos.next()){
	        		
	        		ItemBean  itemB = new ItemBean();
	          		if (datos.getString("FIELD")!=null) itemB.setProperty("FIELD", datos.getString("FIELD"));
	          		if (datos.getString("VALUE")!=null) itemB.setProperty("VALUE", datos.getString("VALUE"));
	          		valores.add(itemB);
	          	}
	      	}
			
			return valores.iterator();			
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Numexp: "+rulectx.getNumExp()+" Error en el nombre de la tabla. "+nombreTabla+" en la consulta "+strQuery+": "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Numexp: "+rulectx.getNumExp()+" Error en el nombre de la tabla. "+nombreTabla+" en la consulta "+strQuery+": "+e.getMessage(),e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Numexp: "+rulectx.getNumExp()+" Error en el nombre de la tabla. "+nombreTabla+" en la consulta "+strQuery+": "+e.getMessage(),e);
		}
	}

	@SuppressWarnings("unchecked")
	public static IItem obtenerDocResolucionaConvocatoria(IRuleContext rulectx, String nombreTramite) throws ISPACRuleException {
		IItem docConv = null;
		try {
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			// Primero cogemos el último trámite de resolución de este
			// expediente y con el id del trámite miro en la tabla
			// 'SPAC_EXP_RELACIONADOS'
			IItemCollection colTramites = TramitesUtil.getTramites(cct, rulectx.getNumExp(), "NOMBRE='" + nombreTramite + "'", "FECHA_INICIO DESC");
			Iterator<IItem> itColTram = colTramites.iterator();

			if (itColTram.hasNext()) {
				IItem tram = itColTram.next();
				int idTramExp = tram.getInt("ID_TRAM_EXP");

				String consultaSQL = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION LIKE '%" + idTramExp + "%' ORDER BY ID DESC";
				IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
				Iterator<IItem> itExpRela = itemCollection.iterator();
				if (itExpRela.hasNext()) {
					IItem rela = itExpRela.next();
					String numexp_hijo = rela.getString("NUMEXP_HIJO");
					IItem expediente = ExpedientesUtil.getExpediente(cct, numexp_hijo);
					String nombreProced = expediente.getString("NOMBREPROCEDIMIENTO");
					if (nombreProced.contains("Decreto")) {
						IItemCollection collDoc = DocumentosUtil.getDocumentos(cct, numexp_hijo, "NOMBRE = 'Decreto'", "FDOC DESC");
						Iterator<IItem> itColDocDec = collDoc.iterator();
						if (itColDocDec.hasNext()) {
							docConv = itColDocDec.next();

						}
					}
					if (nombreProced.contains("Propuesta")) {
						consultaSQL = "WHERE NUMEXP_PADRE = '" + numexp_hijo + "' ORDER BY ID DESC";
						itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
						itExpRela = itemCollection.iterator();
						if (itExpRela.hasNext()) {
							rela = itExpRela.next();
							String numexp_hijo_Junta = rela.getString("NUMEXP_HIJO");
							IItemCollection collDocJunta = DocumentosUtil.getDocumentos(cct, numexp_hijo_Junta, "(NOMBRE = 'Propuesta' OR NOMBRE='Propuesta Urgencia') and descripcion like '%"
									+ numexp_hijo + "%'", "FDOC DESC");
							Iterator<IItem> itColDocJunta = collDocJunta.iterator();
							if (itColDocJunta.hasNext()) {
								IItem iDocProp = itColDocJunta.next();
								String descripcionProp = iDocProp.getString("DESCRIPCION");
								String[] vPropuesta = descripcionProp.split(" - ");
								if (vPropuesta.length > 0) {
									// Compruebo si es Propuesta o Propuesta
									// Urgencia
									String descripcion = "";
									String numPropuesta = vPropuesta[1].substring(0, 0);
									if (vPropuesta[0].equals("Propuesta Urgencia")) {
										descripcion = numPropuesta + ".- Urgencia";
									} else {
										descripcion = numPropuesta + ".-";
									}

									IItemCollection collCertAcuer = DocumentosUtil.getDocumentos(cct, numexp_hijo_Junta, "NOMBRE = 'Certificado de acuerdos' and descripcion like '%" + descripcion
											+ "%'", "FDOC DESC");
									Iterator<IItem> itCertiAcuerd = collCertAcuer.iterator();
									if (itCertiAcuerd.hasNext()) {
										docConv = itCertiAcuerd.next();
									}
								}
							}

						}
					}

				}
			}

		} catch (ISPACException e) {
			logger.error("Error al obtener el documento de aprobación de la convocatoria. NUMEXP. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el documento de aprobación de la convocatoria. NUMEXP. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
		}
		return docConv;
	}


	public static IItem obtenerItemBDNS(IRuleContext rulectx) throws ISPACRuleException {
		IItem consu = null;
		Iterator<IItem> itConsulta = null;
		try {
			itConsulta = ConsultasGenericasUtil.queryEntities(rulectx, "BDNS_IGAE_CONVOCATORIA", "NUMEXP='" + rulectx.getNumExp() + "'");
		} catch (ISPACRuleException e) {
			logger.error("Error al obtener el item de la entidad BDNS_IGAE_CONVOCATORIA. "+rulectx.getNumExp()+ " - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el item de la entidad BDNS_IGAE_CONVOCATORIA. "+rulectx.getNumExp()+ " - "+e.getMessage(), e);
		}
		if (itConsulta.hasNext()) {
			consu = itConsulta.next();
		}
		return consu;
	}

}
