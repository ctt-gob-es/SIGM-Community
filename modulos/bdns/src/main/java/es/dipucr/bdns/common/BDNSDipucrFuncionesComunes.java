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
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCopy;

import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidades;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesionIdBeneficiario;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdPago;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdProyecto;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Estado;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta;
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
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PieFirmaExtracto;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TextoExtracto;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.bdns.convocatoria.client.BDNSConvocatoriaClient;
import es.dipucr.bdns.objetos.Concesion;
import es.dipucr.bdns.objetos.Convocatoria;
import es.dipucr.bdns.objetos.EntidadConvocatoria;
import es.dipucr.bdns.objetos.Pago;
import es.dipucr.bdns.objetos.Tipo;
import es.dipucr.sigem.api.rule.common.comparece.CompareceConfiguration;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.GestorDecretos;
import es.dipucr.sigem.api.rule.common.utils.TercerosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class BDNSDipucrFuncionesComunes {

	private static final Logger logger = Logger.getLogger(BDNSDipucrFuncionesComunes.class);
	
	//Enumeración de los tipos de informe
	public enum TipoInforme { CONV_ADM, CONCPAG_ADM, CONV, CONCESION, PAGO }
	
	
	@SuppressWarnings("unchecked")
	public static void generarInformeConvocatoria(IRuleContext rulectx, Object respuesta, TipoInforme tipoInforme) throws ISPACRuleException {
		File fichero = null;
		try {
			
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
			
			fichero = new File (FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance().newFileName(".pdf"));
		
			if (fichero.createNewFile()) {
				logger.warn("El fichero se ha creado correctamente");
				Document documentInforme = new Document();
				PdfCopy.getInstance(documentInforme,new FileOutputStream(fichero));
				documentInforme.open();
				
				insertarImagenes(rulectx, documentInforme);
				
				String descripcionDoc = "";
				if (tipoInforme.equals(TipoInforme.CONV_ADM)){
					conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta respuestaConv = 
							(conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta) respuesta;
					BDNSConvocatoriaClient.imprimeInforme(respuestaConv, documentInforme, rulectx);
					descripcionDoc = "Informe respuesta WS";
				}
				else if (tipoInforme.equals(TipoInforme.CONCPAG_ADM)){
					List<concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta> listRespuestas = 
							(List<concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta>) respuesta;
					imprimeInformeConcesionesPagosProyectosWS(listRespuestas, documentInforme, rulectx);
					descripcionDoc = "Informe respuesta WS";
				}
				else if (tipoInforme.equals(TipoInforme.CONCESION)){
					List<Concesion> listConcesiones = (List<Concesion>) respuesta;
					imprimeInformeConcesiones(listConcesiones, documentInforme, rulectx);
					descripcionDoc = "Detalle concesiones";
				}
				else if (tipoInforme.equals(TipoInforme.PAGO)){
					List<Pago> listPagos = (List<Pago>) respuesta;
					imprimeInformePagos(listPagos, documentInforme, rulectx);
					descripcionDoc = "Detalle pagos";
				}
				
				documentInforme.close();
				
				if (fichero != null) {
					IItemCollection tipDoc = entitiesAPI.queryEntities("SPAC_CT_TPDOC","WHERE NOMBRE='Informe'");
					Iterator<IItem> tipDocIterator = tipDoc.iterator();
					int idTipDoc = 0;
					if (tipDocIterator.hasNext())
						idTipDoc  = ((IItem) tipDocIterator.next()).getInt("ID");
					
					DocumentosUtil.generaYAnexaDocumento(rulectx, idTipDoc, descripcionDoc, fichero, Constants._EXTENSION_PDF);
				}
			}
		} catch (ISPACException e) {
			logger.error("Error al crear el nuevo fichero. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al crear el nuevo fichero. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Error al createNewFile. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al createNewFile. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
		} catch (DocumentException e) {
			logger.error("Error al PdfCopy.getInstance. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al PdfCopy.getInstance. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
		}
		finally{
			if(fichero!=null){
				fichero.delete();
			}
		}
	}
	
	
	private static void imprimeInformeConcesionesPagosProyectosWS(List<Respuesta> listRespuestas, Document documentInforme, IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			documentInforme.add(new Phrase("\n"));

			documentInforme.add(new Phrase("INFORME DE LA BASE DE DATOS NACIONAL DE SUBVENCIONES"));
			documentInforme.add(new Phrase("\n"));
			documentInforme.add(new Phrase("\n"));
			
			int contador = 0;
			for (Respuesta respuesta : listRespuestas){
			
				contador++;
				documentInforme.add(new Phrase("******************************************** (" + contador + ") ************************************************"));
				documentInforme.add(new Phrase("\n"));
				documentInforme.add(new Phrase("********* ATRIBUTOS ***********"));
				documentInforme.add(new Phrase("\n"));
				
				concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Atributos atributos = respuesta.getAtributos();
				
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
				
				concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Transmisiones transmision = respuesta.getTransmisiones();
				concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.TransmisionDatos[] vTransm = transmision.getTransmisionDatos();
				
				for(int i=0; i < vTransm.length; i++){
					concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.TransmisionDatos transmisionDatos = vTransm[i];
					concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.DatosGenericos datosGenericos = transmisionDatos.getDatosGenericos();
					concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Emisor emisor = datosGenericos.getEmisor();
					documentInforme.add(new Phrase(" - Emisor: "+emisor.getNifEmisor()+" - "+emisor.getNombreEmisor()));
					documentInforme.add(new Phrase("\n"));
					concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Solicitante solicitante = datosGenericos.getSolicitante();
					documentInforme.add(new Phrase(" - Solicitante: "+solicitante.getIdentificadorSolicitante()+" - "+solicitante.getNombreSolicitante()));
					documentInforme.add(new Phrase("\n"));
					documentInforme.add(new Phrase("\n"));
					
					DatosEspecificos datosEspecificos = transmisionDatos.getDatosEspecificos();
					DatosEspecificosDatosEspecificosRespuestaAbstracta datosResp = datosEspecificos.getDatosEspecificosRespuesta();
					
					concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosRespuesta datosRespuesta = 
							(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosRespuesta) datosResp;
					documentInforme.add(new Phrase("********* RESPUESTA ***********"));
					DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion datosIdentificador = datosRespuesta.getDatosIdentificacion();
					documentInforme.add(new Phrase("\n"));
					documentInforme.add(new Phrase("\n"));
					IdConcesion idConcesion = null;
					if(datosIdentificador.getIdConcesion()!=null){
						documentInforme.add(new Phrase(" - IDENTIFICACIÓN DE LA CONCESIÓN"));
						documentInforme.add(new Phrase("\n"));
						idConcesion = datosIdentificador.getIdConcesion();
					}
					
					if(datosIdentificador.getIdPago()!=null){
						documentInforme.add(new Phrase(" - IDENTIFICACIÓN DEL PAGO"));
						documentInforme.add(new Phrase("\n"));
						IdPago idPago = datosIdentificador.getIdPago();
						idConcesion = idPago.getIdConcesion();
						
					}
					
					if(datosIdentificador.getIdProyecto()!=null){
						documentInforme.add(new Phrase(" - IDENTIFICACIÓN DEL PROYECTO"));
						documentInforme.add(new Phrase("\n"));
						documentInforme.add(new Phrase("\n"));
						IdProyecto idProyecto = datosIdentificador.getIdProyecto();
						idConcesion = idProyecto.getIdConcesion();
					}
					
					documentInforme.add(new Phrase("     * Id. convocatoria: "+ idConcesion.getIdConvocatoria()));
					documentInforme.add(new Phrase("\n"));
					IdConcesionIdBeneficiario beneficiarioPago = idConcesion.getIdBeneficiario();
					documentInforme.add(new Phrase("     * Beneficiario: "+ beneficiarioPago.getIdPersonaBen() + " - "+ beneficiarioPago.getPaisBen()));
					documentInforme.add(new Phrase("\n"));
					documentInforme.add(new Phrase("     * Discriminador concesion: "+ idConcesion.getDiscriminadorConcesion()));
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

					documentInforme.add(new Phrase("\n"));
					documentInforme.add(new Phrase("*****************************************************************************************************"));
					documentInforme.newPage();
				}		
			}
				
		} catch (Exception e) {
			String error = "Error al generar el informe de respuesta al WS de Concesiones y Pagos: " + e.getMessage();
			logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
	}
	
	
	private static void imprimeInformeConcesiones(List<Concesion> listConcesiones, Document documentInforme, IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			DecimalFormat df = new DecimalFormat("0.00"); 
			
			documentInforme.add(new Phrase("\n"));
			documentInforme.add(new Phrase("INFORME DE LA BASE DE DATOS NACIONAL DE SUBVENCIONES - CONCESIONES"));

			for (Concesion concesion: listConcesiones){

				documentInforme.add(new Phrase("\n\n"));
				documentInforme.add(new Phrase("**************************************************************************************"));
				documentInforme.add(new Phrase("\n"));
				documentInforme.add(new Phrase("- Exp. Solicitud: " + concesion.getReferenciaConcesion() + "\n"));
				documentInforme.add(new Phrase("\n"));
				IThirdPartyAdapter tercero = concesion.getTercero();
				documentInforme.add(new Phrase("- Beneficiario: " + tercero.getIdentificacion() + " - " + tercero.getNombreCompleto() + "\n"));
				if (!StringUtils.isEmpty(concesion.getObjetivoConcesion())){
					documentInforme.add(new Phrase("- Objetivo/Finalidad: " + concesion.getObjetivoConcesion() + "\n"));
				}
				documentInforme.add(new Phrase("- Instrumento Ayuda: " + concesion.getInstrumentoAyuda() + "\n"));
				String sFechaConcesion = FechasUtil.getFormattedDate(concesion.getFechaConcesion());
				documentInforme.add(new Phrase("- Fecha de la concesión: " + sFechaConcesion + "\n"));
				documentInforme.add(new Phrase("\n"));
				if (null != concesion.getCosteConcesion()){
					documentInforme.add(new Phrase("- Importe solicitado: " + df.format(concesion.getCosteConcesion()) + "€\n"));
				}
				if (null != concesion.getSubvencionConcesion()){
					documentInforme.add(new Phrase("- Importe concedido: " + df.format(concesion.getSubvencionConcesion()) + "€\n"));
				}
				if (null != concesion.getAyudaConcesion()){
					documentInforme.add(new Phrase("- Ayuda Concesión: " + df.format(concesion.getAyudaConcesion()) + "€\n"));
				}
				if (null != concesion.getAyudaEquivalenteConcesion()){
					documentInforme.add(new Phrase("- Ayuda Equivalente Concesión: " + df.format(concesion.getAyudaEquivalenteConcesion()) + "€\n"));
				}
				documentInforme.add(new Phrase("\n"));
				
				documentInforme.add(new Phrase("- Anualidades:"));
				documentInforme.add(new Phrase("\n"));
				for (DatosAnualidadesAnualidades anualidad : concesion.getArrAnualidades()){
					documentInforme.add(new Phrase("   * " + anualidad.getAnualidad() + " - " + anualidad.getAplicacion() 
							+ " - " + df.format(anualidad.getImporteAnualporApli()) + "€\n"));
				}
			}
				
		} catch (Exception e) {
			String error = "Error al generar el informe de usuario de concesiones: " + e.getMessage();
			logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
	}
	
	
	private static void imprimeInformePagos(List<Pago> listPagos, Document documentInforme, IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			DecimalFormat df = new DecimalFormat("0.00"); 
			
			documentInforme.add(new Phrase("\n"));
			documentInforme.add(new Phrase("INFORME DE LA BASE DE DATOS NACIONAL DE SUBVENCIONES - PAGOS"));

			for (Pago pago: listPagos){

				documentInforme.add(new Phrase("\n\n"));
				documentInforme.add(new Phrase("**************************************************************************************"));
				documentInforme.add(new Phrase("\n"));
				documentInforme.add(new Phrase("- Exp. Solicitud: " + pago.getReferenciaConcesion() + "\n"));
				documentInforme.add(new Phrase("- Ref.Pago: " + pago.getReferenciaPago() + "\n"));
				documentInforme.add(new Phrase("\n"));
				IThirdPartyAdapter[] arrTerceros = TercerosUtil.getDatosTerceroByNif((ClientContext) rulectx.getClientContext(), pago.getCifBeneficiario());
				IThirdPartyAdapter tercero = arrTerceros[0];
				documentInforme.add(new Phrase("- Beneficiario: " + tercero.getIdentificacion() + " - " + tercero.getNombreCompleto() + "\n"));
				String sFechaPago = FechasUtil.getFormattedDate(pago.getFechaPago());
				documentInforme.add(new Phrase("- Fecha del pago: " + sFechaPago + "\n"));
				documentInforme.add(new Phrase("- Importe pagado: " + df.format(pago.getImportePagado()) + "€\n"));
			}
				
		} catch (Exception e) {
			String error = "Error al generar el informe de usuario de pagos: " + e.getMessage();
			logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
	}

	
	private static void insertarImagenes(IRuleContext rulectx, Document documentInforme) throws ISPACRuleException {
		String entidad = EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext());
		
		String imageLogoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_LOGO_PATH_DIPUCR);
		String imageFondoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_FONDO_PATH_DIPUCR);
//		String imagePiePath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_PIE_PATH_DIPUCR);

		try {
			File logoURL = new File(imageLogoPath);
			if (logoURL != null) {
				Image logo = Image.getInstance(imageLogoPath);
				logo.scalePercent(50);
				documentInforme.add(logo);
			}
			
			File fondoURL = new File(imageFondoPath);
			if(fondoURL != null){
				Image fondo = Image.getInstance(imageFondoPath);
				fondo.setAbsolutePosition(250, 50);
				fondo.scalePercent(70);
				documentInforme.add(fondo);
				
			}

			//TODO: Es imagen fija, no pie, y el texto se superpone
//			File pieURL = new File(imagePiePath);
//			if(pieURL != null){
//				Image pie = Image.getInstance(imagePiePath);
//				pie.setAbsolutePosition(documentInforme.getPageSize().getWidth() - 550, 15);
//				pie.scalePercent(80);
//				documentInforme.add(pie);
//			}
		} catch (DocumentException e) {
			logger.error("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
		} catch (MalformedURLException e) {
			logger.error("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
		}
	}


	public static Convocatoria obtenerConvocatoria(IRuleContext rulectx, TipoMovimiento tipoMov, EntidadConvocatoria entidadConv) throws ISPACException {
		Convocatoria convocatoria = new Convocatoria();
		try {
			
			//Obligatorio en movimientos de Modificación y Baja
			if (!tipoMov.equals(TipoMovimiento.A)) {
				convocatoria.setIdConvocatoria(entidadConv.getIdConvocatoria());
				
			}
			//Requerido en el método de alta y modificación. Sin contenido en método de baja.
			if(!tipoMov.equals(TipoMovimiento.B)){
				DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov datosConv = obtenerDatosGeneralesConv(rulectx, entidadConv);
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
			//[dipucr-Felipe #400]
			boolean bAyudaDirecta = Constants.VALIDACION.SI.equals(entidadConv.getAyudaDirecta());
			boolean bSubvNominativa = Constants.VALIDACION.SI.equals(entidadConv.getSubvencionNominativa());
			
			if(!tipoMov.equals(TipoMovimiento.B) && !(bAyudaDirecta || bSubvNominativa)){
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
	private static DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov obtenerDatosGeneralesConv(IRuleContext rulectx, EntidadConvocatoria entidadConv) throws ISPACException {
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
		IItem docResolucion = getDocumentoResolucionConvocatoria(rulectx.getClientContext(), entidadConv);
		
		if (docResolucion != null) {
			File fichero;
			try {
				logger.warn("Numexp. "+rulectx.getNumExp()+" - INFOPAG_RDE. "+docResolucion.getString("INFOPAG_RDE")+" - NOMBRE. "+docResolucion.getString("NOMBRE") + " - EXTENSION_RDE. "+docResolucion.getString("EXTENSION_RDE"));
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
				logger.warn("Numexp. "+rulectx.getNumExp()+ " longitud. "+bFile.length);
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
						StringBuffer sbQuery = new StringBuffer();
						sbQuery.append(" NOMBRE = 'Decreto'");
						sbQuery.append(" AND ID_TRAMITE_PCD IN (SELECT ID FROM SPAC_P_TRAMITES WHERE ID_CTTRAMITE IN "
								+ "(SELECT ID FROM SPAC_CT_TRAMITES WHERE COD_TRAM = '" + GestorDecretos.CATALOG_SPAC_TRAMITES_COD_TRAMITE_DECRETOS + "'))");
						sbQuery.append(" AND ESTADOFIRMA = '" + SignStatesConstants.FIRMADO + "'");
						IItemCollection collDoc = DocumentosUtil.getDocumentos(cct, numexp_hijo, sbQuery.toString(), "FDOC DESC");
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
	
	public static IItem obtenerItemConvocatoria(IRuleContext rulectx) throws ISPACRuleException {
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
	
	
private static String[] obtenerTextoExtracto(String textoExtracto) {
		
		String [] vtextoExt = textoExtracto.split("\n");
		ArrayList<String> listExtracto = new ArrayList<String>();
		for (String parrafo : vtextoExt){
			if (!StringUtils.isEmpty(parrafo) && !"\r".equals(parrafo)){
				listExtracto.add(parrafo);
			}
		}
		return listExtracto.toArray(new String[listExtracto.size()]);
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
	
	public static EntidadConvocatoria cargaEntidadConvocatoria(IRuleContext rulectx) throws ISPACRuleException {
		EntidadConvocatoria convocatoria = null;
		// Número para la identificación de la convocatoria anual
		// asignado por la IGAE.
		Iterator<IItem> itConsulta = ConsultasGenericasUtil.queryEntities(rulectx, "BDNS_IGAE_CONVOCATORIA", "NUMEXP='" + rulectx.getNumExp() + "'");
		while (itConsulta.hasNext()) {
			IItem consu = itConsulta.next();
			if (consu != null) {
				try {
					convocatoria = new EntidadConvocatoria();
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
					if(consu.getString("FECHA_CONCESION")!=null){
						convocatoria.setFechaConcesion(consu.getDate("FECHA_CONCESION"));
					}
					if(consu.getString("JUSTIFICACION")!=null){
						convocatoria.setJustificacion(consu.getString("JUSTIFICACION"));
					}
					if(consu.getString("FECHAJUSTIFICACION")!=null){
						convocatoria.setFechaJustificacion(consu.getDate("FECHAJUSTIFICACION"));
					}
					if(consu.getString("APLICACION")!=null){
						convocatoria.setAplicacion(consu.getString("APLICACION"));
					}
					if(consu.getString("EJERCICIO")!=null){
						convocatoria.setEjercicio(consu.getString("EJERCICIO"));
					}
					if(consu.getString("REF_CONTABLE")!=null){
						convocatoria.setRefContable(consu.getString("REF_CONTABLE"));
					}
					
					//INICIO [dipucr-Felipe #447]
					//Al final, los campos de financiación y fondos no se crean como multivalor,
					//pues son dependientes uno del otro, y sigem no nos permite esto con campos multivalor
					//TIPOFINANCIACION -> TIPO_FINANCIACION; IMPORTEFINANCIACION -> IMPORTE_FINANCIACION
					//TIPOFONDO -> TIPO_FONDO; IMPORTEFONDO -> IMPORTE_FONDO
					if(null != consu.getString("TIPO_FINANCIACION")){
						Tipo[] arrTipoFinanciacion = new Tipo[1];
						Tipo tipo = new Tipo();
						tipo.setTipoFinanciacion(consu.getString("TIPO_FINANCIACION"));
						if (null != consu.getString("IMPORTE_FINANCIACION")){
							tipo.setImporteFinanciacion(new BigDecimal(consu.getDouble
									("IMPORTE_FINANCIACION")).setScale(2, RoundingMode.HALF_DOWN));
						}
						arrTipoFinanciacion[0] = tipo;
						convocatoria.setFinanciacion(arrTipoFinanciacion);
					}
					
					if(null != consu.getString("TIPO_FONDO_UE")){
						Tipo[] arrTipoFondo = new Tipo[1];
						Tipo tipo = new Tipo();
						tipo.setTipoFinanciacion(consu.getString("TIPO_FONDO_UE"));
						if (null != consu.getString("IMPORTE_FONDO_UE")){
							tipo.setImporteFinanciacion(new BigDecimal(consu.getDouble
									("IMPORTE_FONDO_UE")).setScale(2, RoundingMode.HALF_DOWN));
						}
						arrTipoFondo[0] = tipo;
						convocatoria.setFondo(arrTipoFondo);
					}
//					if(consu.getInt("ID")>0){
//						Iterator<ItemBean> itConsultaTF = ConsultasGenericasUtil.queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='TIPOFINANCIACION' ORDER BY ID ASC");
//						Iterator<ItemBean> itConsultaIF = ConsultasGenericasUtil.queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='IMPORTEFINANCIACION' ORDER BY ID ASC");
//						Vector<Tipo> financiacion = new Vector<Tipo>();
//						while (itConsultaTF.hasNext()) {
//							ItemBean consuTF = itConsultaTF.next();
//							Tipo tipo = new Tipo();
//							if (consuTF != null && consuTF.getString("VALUE")!=null) {
//								tipo.setTipoFinanciacion(consuTF.getString("VALUE"));
//								if(itConsultaIF.hasNext()){
//									ItemBean consuIF = itConsultaIF.next();
//									if (consuIF != null && consuIF.getString("VALUE")!=null) {
//										tipo.setImporteFinanciacion(new BigDecimal(consuIF.getString("VALUE")));
//										financiacion.add(tipo);
//									}
//								}
//							}
//						}
//						if(financiacion.size()>0){
//							Tipo [] tipo = new Tipo[financiacion.size()];
//							convocatoria.setFinanciacion(financiacion.toArray(tipo));
//						}
//					}
//					
//					if(consu.getInt("ID")>0){
//						Iterator<ItemBean> itConsultaTF = ConsultasGenericasUtil.queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='TIPOFONDO' ORDER BY ID ASC");
//						Iterator<ItemBean> itConsultaIF = ConsultasGenericasUtil.queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='IMPORTEFONDO' ORDER BY ID ASC");
//						Vector<Tipo> fondo = new Vector<Tipo>();
//						while (itConsultaTF.hasNext()) {
//							ItemBean consuTF = itConsultaTF.next();
//							Tipo tipo = new Tipo();
//							if (consuTF != null && consuTF.getString("VALUE")!=null) {
//								tipo.setTipoFinanciacion(consuTF.getString("VALUE"));
//								if(itConsultaIF.hasNext()){
//									ItemBean consuIF = itConsultaIF.next();
//									if (consuIF != null && consuIF.getString("VALUE")!=null) {
//										tipo.setImporteFinanciacion(new BigDecimal(consuIF.getString("VALUE")));
//										fondo.add(tipo);
//									}
//								}
//							}
//						}
//						if(fondo.size()>0){
//							Tipo [] tipo = new Tipo[fondo.size()];
//							convocatoria.setFondo(fondo.toArray(tipo));
//						}
//					}

					//FIN [dipucr-Felipe #447]
					
					if(consu.getInt("ID")>0){
						Iterator<ItemBean> itConsultaTF = ConsultasGenericasUtil.queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='SECTOR' ORDER BY ID ASC");
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
						Iterator<ItemBean> itConsultaTF = ConsultasGenericasUtil.queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='OBJETIVO' ORDER BY ID ASC");
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
						Iterator<ItemBean> itConsultaTF = ConsultasGenericasUtil.queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='INSTRUMENTO' ORDER BY ID ASC");
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
						Iterator<ItemBean> itConsultaTF = ConsultasGenericasUtil.queryEntitiesMultivalor(rulectx, "BDNS_IGAE_CONVOCATORIA_S", "REG_ID=" + consu.getInt("ID") + " AND FIELD='TIPOBENEFICIARIO' ORDER BY ID ASC");
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
					if(consu.getString("AYUDA_DIRECTA")!=null){
						convocatoria.setAyudaDirecta(consu.getString("AYUDA_DIRECTA"));
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
						convocatoria.setTextoExtracto(obtenerTextoExtracto(consu.getString("TEXTOEXTRACTO")));
					}
					if(consu.getString("LUGARFIRMA")!=null){
						convocatoria.setLugarFirma(consu.getString("LUGARFIRMA"));
					}
					if(consu.getString("NOMBREFIRMANTE")!=null){
						convocatoria.setFirmante(consu.getString("NOMBREFIRMANTE"));
					}
					//[dipucr-Felipe #343] Marcamos el documento de resolución
					IItem docResolucion = obtenerDocResolucionaConvocatoria(rulectx, "Trámite Resolución");
					if (null != docResolucion){						
						convocatoria.setIdDocResolucion(String.valueOf(docResolucion.getKeyInt()));
						logger.warn("Numexp. "+rulectx.getNumExp()+" - Identificador del doc. decreto. "+convocatoria.getIdDocResolucion());
					}
					
				} catch (ISPACException e) {
					logger.error("Error al obtener la entidad BDNS_IGAE_CONVOCATORIA. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
					throw new ISPACRuleException("Error al obtener la entidad BDNS_IGAE_CONVOCATORIA. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
				}
			}

		}
		return convocatoria;
	}
	
	
	public static IItem getDocumentoResolucionConvocatoria(IClientContext cct, String idConvocatoria) throws ISPACException{
		
		IItem itemDoc = null;
		String codNumActoConv = Constantes.COD_DTDOC_NUMACTO_BDNS + idConvocatoria;
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
    	
		StringBuffer sbQuery = new StringBuffer();
    	sbQuery.append("WHERE NUM_ACTO='");
    	sbQuery.append(codNumActoConv);
    	sbQuery.append("'");
    	
    	//[dipucr-Felipe #799]
//    	IItemCollection colDocumento = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", sbQuery.toString());
    	IItemCollection colDocumento = DocumentosUtil.queryDocumentos(cct, sbQuery.toString());
    	
    	@SuppressWarnings("rawtypes")
		Iterator it = colDocumento.iterator();
    	if (it.hasNext()){
    		itemDoc = (IItem) it.next();
    	}
    	else{
    		logger.warn("No se ha encontrado ninguna resolución de convocatoria con NUM_ACTO = " + codNumActoConv);
    	}
    	
		return itemDoc;
	}
	
	
	public static IItem getDocumentoResolucionConvocatoria(IClientContext cct, EntidadConvocatoria convocatoria) throws ISPACException{
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
    	
    	return DocumentosUtil.getDocumento(entitiesAPI, Integer.parseInt(convocatoria.getIdDocResolucion()));
	}
	
	
	public static void marcarDocumentoResolucionConvocatoria(IClientContext cct, EntidadConvocatoria convocatoria) throws ISPACException{
		
		IItem itemDocResolucion = BDNSDipucrFuncionesComunes.getDocumentoResolucionConvocatoria(cct, convocatoria);
		String codNumActoConv = Constantes.COD_DTDOC_NUMACTO_BDNS + convocatoria.getIdConvocatoria();
		itemDocResolucion.set("NUM_ACTO", codNumActoConv);
		itemDocResolucion.set("ESTADO", "PÚBLICO");
		itemDocResolucion.store(cct);
	}

}

