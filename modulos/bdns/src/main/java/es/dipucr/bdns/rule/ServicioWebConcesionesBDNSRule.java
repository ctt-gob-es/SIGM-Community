package es.dipucr.bdns.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.axis.types.PositiveInteger;
import org.apache.log4j.Logger;

import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidades;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidadesTipoAnualidad;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes.TipoInforme;
import es.dipucr.bdns.concesiones.client.BDNSConcesionesPagosClient;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.bdns.objetos.Concesion;
import es.dipucr.bdns.objetos.EntidadConvocatoria;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.TercerosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class ServicioWebConcesionesBDNSRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(ServicioWebConcesionesBDNSRule.class);
	
	public static final String ESTADOADM_APROBADO = "AP";
	public static final String ESTADOADM_NOTIFICADO_APROBADO = "NT";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		try {
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			/** Configuración del trámite para alta, baja o modificación 
			 *  Fusionamos el alta y la modificación para facilitar al usuario **/
			boolean bAlta = true;
			String tipoMovimiento = TramitesUtil.getDatosEspecificosOtrosDatos(rulectx.getClientContext(), rulectx.getTaskProcedureId());
			if(tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_ALTA)){
				bAlta = true;
			}
			else if(tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_BAJA)){
				bAlta = false;
			}
			else{
				rulectx.setInfoMessage("El trámite no tiene configurado el tipo de ALTA/BAJA en Datos Específicos/Otros Datos."
						+ " Consulte con el administrador.");
				return false;
			}
			
			//Obtenemos la convocatoria, su id y su instrumento (son los mismos para todas las concesiones)
			EntidadConvocatoria convocatoria = BDNSDipucrFuncionesComunes.cargaEntidadConvocatoria(rulectx);
			if (null == convocatoria){
				rulectx.setInfoMessage("Debe rellenar los datos de la convocatoria en la pestaña 'Base de Datos Nacional de Subvenciones'");
				return false;
			}
			String idConvocatoria = convocatoria.getIdConvocatoria();
			if (StringUtils.isEmpty(idConvocatoria)){
				rulectx.setInfoMessage("El id de la convocatoria en la pestaña 'Base de Datos Nacional de Subvenciones' es vacío "
						+ "pues la convocatoria no se ha enviado a la BDNS");
				return false;
			}
			Date dFechaConcesion = convocatoria.getFechaConcesion();
			if (null == dFechaConcesion){
				rulectx.setInfoMessage("Debe rellenar el campo 'Fecha Concesión' en la pestaña 'Base de Datos Nacional de Subvenciones'");
				return false;
			}
			String instrumento = null;
			String [] arrInstrumentos = convocatoria.getInstrumento();
			if(null != arrInstrumentos && arrInstrumentos.length > 0){
				instrumento = arrInstrumentos[0]; //Cojo siempre el primero
			}
			//Datos económicos
			if (StringUtils.isEmpty(convocatoria.getAplicacion())){
				rulectx.setInfoMessage("Debe rellenar el campo 'Aplicación presupuestaria' en la pestaña 'Base de Datos Nacional de Subvenciones'");
				return false;
			}
			if (StringUtils.isEmpty(convocatoria.getEjercicio())){
				rulectx.setInfoMessage("Debe rellenar el campo 'Ejercicio' en la pestaña 'Base de Datos Nacional de Subvenciones'");
				return false;
			}
			try{
				Integer.valueOf(convocatoria.getEjercicio());
			}
			catch(NumberFormatException ex){
				rulectx.setInfoMessage("El campo 'Ejercicio' en la pestaña 'Base de Datos Nacional de Subvenciones'"
						+ " no tiene un formato numérico correcto");
				return false;
			}
			if (StringUtils.isEmpty(convocatoria.getAyudaDirecta())){
				rulectx.setInfoMessage("Debe rellenar el campo 'Ayuda Directa' en la pestaña 'Base de Datos Nacional de Subvenciones'");
				return false;
			}
			if (StringUtils.isEmpty(convocatoria.getSubvencionNominativa())){
				rulectx.setInfoMessage("Debe rellenar el campo 'Subvención Nominativa' en la pestaña 'Base de Datos Nacional de Subvenciones'");
				return false;
			}
			
			boolean bAyudaDirecta = Constants.VALIDACION.SI.equals(convocatoria.getAyudaDirecta());
			boolean bSubvNominativa = Constants.VALIDACION.SI.equals(convocatoria.getSubvencionNominativa());
			
			//Listado de concesiones
			ArrayList<Concesion> listConcesiones = new ArrayList<Concesion>();
			
			//Controlamos si son ayuda directa o nominativa
			if (bAyudaDirecta || bSubvNominativa){
				
				String numexp = rulectx.getNumExp();
				Concesion concesion = new Concesion();
				
				StringBuffer sbError = new StringBuffer();
				IThirdPartyAdapter tercero = getDatosTercero(cct, numexp, sbError);
				if (!StringUtils.isEmpty(sbError.toString())){
					rulectx.setInfoMessage(sbError.toString());
					return false;
				}
				concesion.setTercero(tercero);
				
				IItemCollection colDirectasNominativas = entitiesAPI.getEntities("DPCR_SUBV_NOMINATIVA", numexp);
				if (colDirectasNominativas.toList().size() != 1){
					rulectx.setInfoMessage("Hay varias entradas, o ninguna, en la tabla DPCR_SUBV_NOMINATIVA referentes a este expediente");
					return false;
				}
				IItem itemDirectaNominativa = (IItem) colDirectasNominativas.iterator().next();
				
				concesion.setIdConvocatoria(idConvocatoria);
				concesion.setInstrumentoAyuda(instrumento);
				concesion.setReferenciaConcesion(numexp);
				concesion.setFechaConcesion(dFechaConcesion);
				
				//Importe solicitado
				String importeSolicitado = itemDirectaNominativa.getString("SUBVENCION");
				if (StringUtils.isEmpty(importeSolicitado)){
					rulectx.setInfoMessage("El campo 'Subvención que se solicita' en la pestaña 'Subvencion Nominativa' es nulo o vacío");
					return false;
				}
				double dImporteSolicitado = 0.0;
				try{
					dImporteSolicitado = parseImporte(importeSolicitado);
				}
				catch(ParseException ex){
					rulectx.setInfoMessage("El campo 'Subvención que se solicita' en la pestaña "
							+ "'Subvencion Nominativa' tiene un formato númerico incorrecto:" + importeSolicitado);
					return false;
				}
				concesion.setCosteConcesion(dImporteSolicitado);
				
				//Importe concedido en la subvención
				if (StringUtils.isEmpty(itemDirectaNominativa.getString("IMPORTE_CONCEDIDO"))){
					rulectx.setInfoMessage("El campo 'Importe concedido' en la pestaña 'Subvencion Nominativa' es nulo o vacío");
					return false;
				}
				//Controlar si es ayuda directa o subvención y rellenar uno de los dos campos
				//NO PERMITE ENVÍO DE CONCESIONES CON SUBVENCIÓN = 0.0 - LAS MANDAMOS TODAS COMO SUBVENCIÓN
				double dImporteConcedido = itemDirectaNominativa.getDouble("IMPORTE_CONCEDIDO");
				
				if (dImporteConcedido > dImporteSolicitado){
					rulectx.setInfoMessage("El importe concedido no puede ser superior a la subvención que se solicita");
					return false;
				}
				
				concesion.setSubvencionConcesion(dImporteConcedido);
				concesion.setAyudaEquivalenteConcesion(dImporteConcedido);
				concesion.setAyudaConcesion(null);
				
				//Anualidades
				DatosAnualidadesAnualidades[] arrAnualidades = getAnualidades(convocatoria, dImporteConcedido);
				concesion.setArrAnualidades(arrAnualidades);
				
				LOGGER.warn(concesion.toStringLine());
				listConcesiones.add(concesion);
			}
			else{
				//Obtenemos las solicitudes relacionadas en estado aprobado
				List<String> listNumexpRel = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ESTADOADM_APROBADO);
				//[dipucr-Felipe #509] Añadimos solicitudes en estado "NOTIFICADO - APROBADO"
				List<String> listNumexpRelNotificado = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ESTADOADM_NOTIFICADO_APROBADO);
				listNumexpRel.addAll(listNumexpRelNotificado);
				
				for (String numexpSolicitud : listNumexpRel){
	
					Concesion concesion = new Concesion();
					
					StringBuffer sbError = new StringBuffer();
					IThirdPartyAdapter tercero = getDatosTercero(cct, numexpSolicitud, sbError);
					if (!StringUtils.isEmpty(sbError.toString())){
						rulectx.setInfoMessage(sbError.toString());
						return false;
					}
					concesion.setTercero(tercero);
					
					ObjetoSolictudConvocatoriaSubvencion objConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpSolicitud);
					IItem itemSolicitud = objConvocatoria.getSolicitud();
					
					concesion.setIdConvocatoria(idConvocatoria);
					concesion.setInstrumentoAyuda(instrumento);
	//				concesion.setObjetivoConcesion("");//No es necesario
					concesion.setReferenciaConcesion(numexpSolicitud);
					concesion.setFechaConcesion(dFechaConcesion);
					
					//Importe solicitado
					String importeSolicitado = itemSolicitud.getString(ConstantesSubvenciones.DatosSolicitud.SUBVENCION);
					if (StringUtils.isEmpty(importeSolicitado)){
						rulectx.setInfoMessage("Expediente " + numexpSolicitud + ". El campo "
								+ "'Subvención' en la pestaña 'Datos de la Solicitud' es nulo o vacío");
						return false;
					}
					double dImporteSolicitado = 0.0;
					try{
						dImporteSolicitado = parseImporte(importeSolicitado);
					}
					catch(ParseException ex){
						rulectx.setInfoMessage("Expediente " + numexpSolicitud + ". El campo 'Subvención' en la pestaña "
								+ "'Datos de la Solicitud' tiene un formato incorrecto:" + importeSolicitado);
						return false;
					}
					concesion.setCosteConcesion(dImporteSolicitado);
					
					//Importe concedido en la subvención
					//Método que comprueba si existe el importe y no hay renuncia/rechazo para ese proyecto/grupo
					double importe1 = objConvocatoria.getImporteCorrecto(ConstantesSubvenciones.DatosResolucion.IMPORTE, 1);
					double importe2 = objConvocatoria.getImporteCorrecto(ConstantesSubvenciones.DatosResolucion.IMPORTE2, 2);
					double importe3 = objConvocatoria.getImporteCorrecto(ConstantesSubvenciones.DatosResolucion.IMPORTE3, 3);
					double importeProyecto1 = objConvocatoria.getImporteCorrecto(ConstantesSubvenciones.DatosResolucion.IMPORTEPROYECTO1, 1);
					double importeProyecto2 = objConvocatoria.getImporteCorrecto(ConstantesSubvenciones.DatosResolucion.IMPORTEPROYECTO2, 2);

					double dImporteConcedido = importe1 + importe2 + importe3 + importeProyecto1 + importeProyecto2;
					
					if (dImporteConcedido <= 0.0){
						
						rulectx.setInfoMessage("Expediente " + numexpSolicitud + ". El campo "
								+ "'Importe' en la pestaña 'Resolución de la Solicitud' es nulo o vacío");
						return false;
					}
					
					if (dImporteConcedido > dImporteSolicitado){
						rulectx.setInfoMessage("Expediente " + numexpSolicitud + ". El importe concedido "
								+ " en la resolución no puede ser superior al importe solicitado de la subvención");
						return false;
					}
					
					concesion.setSubvencionConcesion(dImporteConcedido);
					concesion.setAyudaEquivalenteConcesion(dImporteConcedido);
					concesion.setAyudaConcesion(null);
					
					//Posibles devoluciones / minorizaciones
					//Puede haber devoluciones para cualquiera de los proyectos
					double dImporteDevolucion = objConvocatoria.getDevolucion();
					dImporteDevolucion += objConvocatoria.getDevolucion2();
					dImporteDevolucion += objConvocatoria.getDevolucion3();
					if (dImporteDevolucion > 0){
						concesion.setDevolucionConcesion(dImporteDevolucion);
					}
					
					DatosAnualidadesAnualidades[] arrAnualidades = getAnualidades(convocatoria, dImporteConcedido);
					concesion.setArrAnualidades(arrAnualidades);
					
					LOGGER.warn(concesion.toStringLine());
					listConcesiones.add(concesion);
				}
			}
			
			//Llamada al servicio web
			List<Respuesta> listRespuestas = null;
			if (bAlta){
				listRespuestas = BDNSConcesionesPagosClient.altaModifConcesiones(rulectx, listConcesiones);
				altaBajaModifConcesionesBBDD(rulectx, listConcesiones, false);
			}
			else{
				listRespuestas = BDNSConcesionesPagosClient.bajaConcesiones(rulectx, listConcesiones);
				altaBajaModifConcesionesBBDD(rulectx, listConcesiones, true);
			}
			BDNSDipucrFuncionesComunes.generarInformeConvocatoria(rulectx, listRespuestas, TipoInforme.CONCPAG_ADM);
			BDNSDipucrFuncionesComunes.generarInformeConvocatoria(rulectx, listConcesiones, TipoInforme.CONCESION);
		
		} catch (Exception e) {
			String error = "Error al enviar la concesion. " + rulectx.getNumExp() + " en los servicios web - " + e.getMessage();
			LOGGER.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		
		return true;
	}

	/**
	 * Formatea importes de tipo cadena de las pestañas de solicitud y resolución
	 * @param importeParsear
	 * @return
	 * @throws ParseException
	 */
	private double parseImporte(String importeParsear) throws ParseException {

		String importe = importeParsear.replaceAll("[^.,0-9]+", "");

		if (importe.contains(".") && !importe.contains(",")){
			String [] arrImporte = importe.split("\\.");
			if (arrImporte[arrImporte.length - 1].length() == 2){//punto decimal
				StringBuilder b = new StringBuilder(importe);
				b.replace(importe.lastIndexOf("."), importe.lastIndexOf(".") + 1, ",");
				importe = b.toString();
			}
			else{
				importe = importe.replace(".", "");
			}
		}
		
		DecimalFormat df = new DecimalFormat();
		double dImporte = df.parse(importe).doubleValue();
		return dImporte;
	}

	/**
	 * Recupera el interesado principal del expediente
	 * @param cct
	 * @param numexp
	 * @param error
	 * @return
	 * @throws ISPACException
	 */
	private IThirdPartyAdapter getDatosTercero(IClientContext cct, String numexp, StringBuffer sbError) throws ISPACException {

		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		IItem itemExpediente = entitiesAPI.getExpedient(numexp);
		String cifBeneficiario = itemExpediente.getString("NIFCIFTITULAR");
		if (StringUtils.isEmpty(cifBeneficiario)){
			sbError.append("Expediente " + numexp + ". Debe rellenar el interesado principal del expediente o este no tiene CIF");
			return null;
		}
		IThirdPartyAdapter[] arrTerceros = TercerosUtil.getDatosTerceroByNif((ClientContext)cct, cifBeneficiario);
		if (null == arrTerceros || arrTerceros.length == 0){
			sbError.append("Expediente " + numexp + ". El interesado principal del expediente con CIF/NIF " + cifBeneficiario
					+ " no existe en la base de datos de registro de la aplicación. Introduzca un tercero validado");
			return null;
		}
		IThirdPartyAdapter tercero = arrTerceros[0];
		if (null == tercero.getDefaultDireccionPostal()){
			sbError.append("Expediente " + numexp + ". El interesado principal del expediente con CIF/NIF " + cifBeneficiario
					+ " debe contener una dirección postal validada por defecto. Consultar con el personal de Registro.");
			return null;
		}
		else{//[dipucr-Felipe #770]
			String cpostal = tercero.getDefaultDireccionPostal().getCodigoPostal();
			if (StringUtils.isEmpty(cpostal)){
				sbError.append("Expediente " + numexp + ". El interesado principal del expediente con CIF/NIF " + cifBeneficiario
						+ " tiene una dirección por defecto sin código postal. Consultar con el personal de Registro.");
				return null;
			}
		}
		return tercero;
	}

	/**
	 * Recupera una única anualidad para una concesión (convocatoria e importe concedido)
	 * @param convocatoria
	 * @param dImporteConcedido
	 * @return
	 */
	private DatosAnualidadesAnualidades[] getAnualidades(EntidadConvocatoria convocatoria, double dImporteConcedido) {
		
		DatosAnualidadesAnualidades anualidad = new DatosAnualidadesAnualidades();
		PositiveInteger piAnioAnualidad = new PositiveInteger(convocatoria.getEjercicio());
		anualidad.setAnualidad(piAnioAnualidad);
		anualidad.setAplicacion(convocatoria.getAplicacion());
		anualidad.setImporteAnualporApli(BigDecimal.valueOf(dImporteConcedido));
		anualidad.setTipoAnualidad(DatosAnualidadesAnualidadesTipoAnualidad.S);
		DatosAnualidadesAnualidades[] arrAnualidades = new DatosAnualidadesAnualidades[1];
		arrAnualidades[0] = anualidad;
		return arrAnualidades;
	}

	/**
	 * Alta y modificación de las concesiones en la BBDD de SIGEM
	 * @param cct
	 * @param listConcesiones
	 * @throws ISPACException
	 */
	private void altaBajaModifConcesionesBBDD(IRuleContext rulectx, ArrayList<Concesion> listConcesiones, boolean bBaja) throws ISPACException {
		
		IClientContext cct = rulectx.getClientContext();
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		String numexp = rulectx.getNumExp();
		
		for (Concesion concesion : listConcesiones){
			
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append(" WHERE NUMEXP='" + numexp + "'");
			sbQuery.append(" AND ID_CONVOCATORIA='" + concesion.getIdConvocatoria() + "'");//Innecesario
			sbQuery.append(" AND CIF_BENEFICIARIO='" + concesion.getTercero().getIdentificacion() + "'");
			
			IItemCollection colConcesiones = entitiesAPI.queryEntities("BDNS_IGAE_CONCESION", sbQuery.toString());
			IItem itemConcesion = null;
			boolean bPagada = false;
			
			//BAJA / MODIFICACIÓN
			if (colConcesiones.iterator().hasNext()){
				itemConcesion = (IItem) colConcesiones.iterator().next();
				if (bBaja){
					itemConcesion.delete(cct);
				}
				else{
					bPagada = (Constants.VALIDACION.SI.equals(itemConcesion.get("PAGADA")));
				}
			}
			else{//ALTA
				itemConcesion = entitiesAPI.createEntity("BDNS_IGAE_CONCESION", numexp);
				itemConcesion.set("ID_CONVOCATORIA", concesion.getIdConvocatoria());
				itemConcesion.set("CIF_BENEFICIARIO", concesion.getTercero().getIdentificacion());
			}
			
			if (!bPagada && !bBaja){
				itemConcesion.set("NUMEXP_SOLICITUD", concesion.getReferenciaConcesion());
				itemConcesion.set("NOMBRE_BENEFICIARIO", concesion.getTercero().getNombreCompleto());
				itemConcesion.set("INSTRUMENTO", concesion.getInstrumentoAyuda());
				itemConcesion.set("FECHA_CONCESION", concesion.getFechaConcesion());
				itemConcesion.set("COSTE", concesion.getCosteConcesion());
				itemConcesion.set("SUBVENCION", concesion.getSubvencionConcesion());
				itemConcesion.set("DEVOLUCION", concesion.getDevolucionConcesion());
				itemConcesion.set("AYUDA", concesion.getAyudaConcesion());
				itemConcesion.set("AYUDA_EQUIVALENTE", concesion.getAyudaEquivalenteConcesion());
				itemConcesion.set("PAGADA", Constants.VALIDACION.NO);
				itemConcesion.store(cct);
			}
		}
	}

}
