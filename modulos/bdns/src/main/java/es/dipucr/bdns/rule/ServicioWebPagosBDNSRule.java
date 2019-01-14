package es.dipucr.bdns.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes.TipoInforme;
import es.dipucr.bdns.concesiones.client.BDNSConcesionesPagosClient;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.bdns.objetos.EntidadConvocatoria;
import es.dipucr.bdns.objetos.Pago;
import es.dipucr.factura.domain.bean.OperacionGastosBean;
import es.dipucr.factura.services.factura.FacturaWSProxy;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.TercerosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class ServicioWebPagosBDNSRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(ServicioWebPagosBDNSRule.class);
	
	public static final String ESTADOADM_APROBADO = "AP";
	public static final String SICALWIN_ESTADO_PAGO_REAL = "R";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@Deprecated //TODO: [dipucr-Felipe] Esta regla actualmente no se usará, pues se hará desde scheduler
	public boolean validateNotUsed(IRuleContext rulectx) throws ISPACRuleException {

		try {
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			String idEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			
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
			if (StringUtils.isEmpty(convocatoria.getRefContable())){
				rulectx.setInfoMessage("Debe rellenar el campo 'Referencia Contable' en la pestaña 'Base de Datos Nacional de Subvenciones'");
				return false;
			}
			
			//Obtenemos las solicitudes relacionadas en estado aprobado
			List<String> listNumexpRel = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ESTADOADM_APROBADO);
			ArrayList<Pago> listPagos = new ArrayList<Pago>();
			
			FacturaWSProxy wsFactura = new FacturaWSProxy();
			
			for (String numexpSolicitud : listNumexpRel){

				Pago pago = new Pago();
				pago.setIdConvocatoria(idConvocatoria);
				pago.setReferenciaConcesion(numexpSolicitud);
				
				IItem itemExpediente = entitiesAPI.getExpedient(numexpSolicitud);
				String cifBeneficiario = itemExpediente.getString("NIFCIFTITULAR");
				if (StringUtils.isEmpty(cifBeneficiario)){
					rulectx.setInfoMessage("Expediente " + numexpSolicitud + ". No está rellenado el "
							+ "interesado principal del expediente o este no tiene CIF");
					return false;
				}
				IThirdPartyAdapter[] arrTerceros = TercerosUtil.getDatosTerceroByNif((ClientContext)cct, cifBeneficiario);
				if (null == arrTerceros || arrTerceros.length == 0){
					rulectx.setInfoMessage("Expediente " + numexpSolicitud + ". El interesado con CIF/NIF " + cifBeneficiario
							+ " no existe en la base de datos de registro de la aplicación. Introduzca un tercero validado");
					return false;
				}
				pago.setCifBeneficiario(cifBeneficiario);
				
				//Recuperamos los datos del pago de Sicalwin
				OperacionGastosBean[] arrPagos = wsFactura.recuperarOperacionesGastoDefinitivas(idEntidad, convocatoria.getEjercicio(), 
						cifBeneficiario, convocatoria.getRefContable(), SICALWIN_ESTADO_PAGO_REAL);
				if (null == arrPagos || arrPagos.length == 0){
					rulectx.setInfoMessage("No se ha encontrado ningún pago físico en la contabilidad (fase 'R') para el tercero '" + cifBeneficiario 
							+ " - " + arrTerceros[0].getNombreCompleto() + "' y la referencia contable '" + convocatoria.getRefContable() + "'");
					return false;
				}
				OperacionGastosBean datosPagoSical = arrPagos[0]; 
				pago.setReferenciaPago(datosPagoSical.getNumOperacion());
				pago.setFechaPago(FechasUtil.convertToDate(datosPagoSical.getFechaOperacion()));
				pago.setImportePagado(Double.valueOf(datosPagoSical.getImporte().replace(",", ".")));
				pago.setbRetencion(false);
				
				LOGGER.warn(pago.toStringLine());
				listPagos.add(pago);
				
			}
			
			//Llamada al servicio web
			List<Respuesta> listRespuestas = null;
			if (bAlta){
				listRespuestas = BDNSConcesionesPagosClient.altaModifPagos(cct, listPagos);
			}
			else{
				listRespuestas = BDNSConcesionesPagosClient.bajaPagos(cct, listPagos);
			}
			BDNSDipucrFuncionesComunes.generarInformeConvocatoria(rulectx, listRespuestas, TipoInforme.CONCPAG_ADM);
			BDNSDipucrFuncionesComunes.generarInformeConvocatoria(rulectx, listPagos, TipoInforme.PAGO);
		
		} catch (Exception e) {
			String error = "Error al enviar la concesion. " + rulectx.getNumExp() + " en los servicios web - " + e.getMessage();
			LOGGER.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		
		return true;
	}

}
