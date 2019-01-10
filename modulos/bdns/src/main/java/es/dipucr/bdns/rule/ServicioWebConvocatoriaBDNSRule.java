package es.dipucr.bdns.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes.TipoInforme;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.bdns.convocatoria.client.BDNSConvocatoriaClient;
import es.dipucr.bdns.objetos.EntidadConvocatoria;
import es.dipucr.bdns.objetos.Tipo;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ServicioWebConvocatoriaBDNSRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(ServicioWebConvocatoriaBDNSRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {		

		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		boolean respuestaConvocatoria = true;
		
		try{
			IClientContext cct = rulectx.getClientContext();

			String tipoMovimiento = TramitesUtil.getDatosEspecificosOtrosDatos(rulectx.getClientContext(), rulectx.getTaskProcedureId());
			if(tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_ALTA) || tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_BAJA) || tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_MODIFICACION)){
				respuestaConvocatoria = true;
			}
			else{
				rulectx.setInfoMessage("El trámite no tiene configurado el tipo de ALTA/BAJA en Datos Específicos/Otros Datos. Consulte con el administrador.");
				respuestaConvocatoria = false;
			}
			boolean bBaja = tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_BAJA);
					
			//Obtenemos la convocatoria, su id y su instrumento (son los mismos para todas las concesiones)
			EntidadConvocatoria convocatoria = BDNSDipucrFuncionesComunes.cargaEntidadConvocatoria(rulectx);
			String mensajeError = rellenadoConvocatoria(rulectx, convocatoria);
			if (!StringUtils.isEmpty(mensajeError)){
				rulectx.setInfoMessage(mensajeError);
				return false;
			}
			
			if(tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_ALTA) && !StringUtils.isEmpty(convocatoria.getIdConvocatoria())){
				rulectx.setInfoMessage("La convocatoria ya ha sido dada de alta, si quiere modificarla debe darla antes de baja");
				return false;	
			}
			if (bBaja && StringUtils.isEmpty(convocatoria.getIdConvocatoria())){
				rulectx.setInfoMessage("No es posible dar de baja la convocatoria pues no se dió de alta o ya está anulada");
				return false;
			}

			//Envío de la petición
			Respuesta respuesta = BDNSConvocatoriaClient.envioPeticionConvocatoria(rulectx, convocatoria);
			
			if(BDNSConvocatoriaClient.cargaIdentificadorConvo(rulectx, respuesta, convocatoria)){
				BDNSDipucrFuncionesComunes.marcarDocumentoResolucionConvocatoria(cct, convocatoria);//[dipucr-Felipe #343] Marcamos el doc de resolución
				BDNSDipucrFuncionesComunes.generarInformeConvocatoria(rulectx, respuesta, TipoInforme.CONV_ADM);		
			}
			
			//Para las bajas, eliminamos el id de la convocatoria
			if (bBaja){
				IItem itemConvocatoria = BDNSDipucrFuncionesComunes.obtenerItemConvocatoria(rulectx);
				itemConvocatoria.set("IDCONVOCATORIA", "");
				itemConvocatoria.store(cct);
			}
			
		} catch (ISPACException e) {
			logger.error("Error al enviar la convocatoria. "+rulectx.getNumExp()+ " en los servicios web - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar la convocatoria. "+rulectx.getNumExp()+ " en los servicios web - "+e.getMessage(), e);
		}
		return respuestaConvocatoria;

	}

	private String rellenadoConvocatoria(IRuleContext rulectx, EntidadConvocatoria convocatoria) {
		
		StringBuffer sbMensajeError = new StringBuffer();
		String error = null;
		
		if(null == convocatoria){
			error = "Debe rellenar los datos de la convocatoria en la pestaña Base de Datos Nacional de Subvenciones.";
		}
		else{
			if(StringUtils.isEmpty(convocatoria.getDescripcionCov())){
				sbMensajeError.append(getTextoFaltaCampo("Descripcion Conv"));
			}
			
			if(StringUtils.isEmpty(convocatoria.getIdDocResolucion())){
				sbMensajeError.append("- No se ha encontrado el documento de resolución, Decreto o Junta de Gobierno. Este no existe o todavía no se ha firmado.\\n");
			}
			
			//INFORMACIÓN DE LAS BASES REGULADORAS
	//		if(StringUtils.isEmpty(convocatoria.getNomenclatura())){
	//			sbMensajeError.append(getTextoFaltaCampo("INFORMACION DE LAS BASES REGULADORAS/Nomenclatura"));
	//		}
			if(StringUtils.isEmpty(convocatoria.getDiarioOficialBR())){
				sbMensajeError.append(getTextoFaltaCampo("Diario Oficial"));
			}
			if(StringUtils.isEmpty(convocatoria.getDescripcionBR())){
				sbMensajeError.append(getTextoFaltaCampo("DescripcionBR"));
			}
			if(StringUtils.isEmpty(convocatoria.getuRLEspBR())){
				sbMensajeError.append(getTextoFaltaCampo("URLEspBR"));
			}
			
			//IDENTIFICACIÓN ASOCIADA A LAS SOLICITUDES
			if(StringUtils.isEmpty(convocatoria.getAbierto())){
				sbMensajeError.append(getTextoFaltaCampo("Abierto"));
			}
			else if (convocatoria.getAbierto().equals(Constants.VALIDACION.NO)){ //Para periodos cerrados, controlamos las fechas
				if(null == convocatoria.getFechaInicioSolicitud() && StringUtils.isEmpty(convocatoria.getInicioSolicitud())){
					sbMensajeError.append("- Para periodos cerrados, debe rellenar el campo 'Fecha Inicio Solicitud' o,"
							+ " en su defecto, incluir un texto en el campo 'Inicio Solicitud'.\\n");
				}
				if(null == convocatoria.getFechaFinSolicitud() && StringUtils.isEmpty(convocatoria.getFinSolicitud())){
					sbMensajeError.append("- Para periodos cerrados, debe rellenar el campo 'Fecha Fin Solicitud' o, "
							+ "en su defecto, incluir un texto en el campo 'Fin Solicitud'.\\n");
				}
			}
			if(null != convocatoria.getFechaInicioSolicitud() && null != convocatoria.getFechaFinSolicitud()){
				if (!convocatoria.getFechaFinSolicitud().after(convocatoria.getFechaInicioSolicitud())){
					sbMensajeError.append("- La fecha 'Fecha Fin Solicitud' debe ser posterior a la fecha 'Fecha Inicio Solicitud'.\\n");
				}
			}
			if(StringUtils.isEmpty(convocatoria.getSede())){
				sbMensajeError.append(getTextoFaltaCampo("Sede"));
			}
			if(StringUtils.isEmpty(convocatoria.getJustificacion())){
				sbMensajeError.append(getTextoFaltaCampo("Justificación"));
			}
			else if(convocatoria.getJustificacion().equals("POS")){//posterior al pago
				if(null == convocatoria.getFechaJustificacion()){
					sbMensajeError.append("- Si la justificación es POSTERIOR, debe rellenar el campo 'Fecha Justificacion'.\\n");
				}
			}
			
			//INICIO [dipucr-Felipe #447]
			//TIPO DE FINANCIACIÓN
			if(null != convocatoria.getFinanciacion() && convocatoria.getFinanciacion().length > 0){
				Tipo tipoFinanciacion = convocatoria.getFinanciacion()[0];
				if (null == tipoFinanciacion.getImporteFinanciacion()){
					sbMensajeError.append("- Si rellena el campo 'Tipo de Financiación', debe rellenar también el campo 'Importe Financiación'.\\n");
				}
			}
			
			//TIPO DE FONDO UE
			//Importe fondo sólo es obligatorio si se rellena el tipo de fondo
			if(null != convocatoria.getFondo() && convocatoria.getFondo().length > 0){
				Tipo tipoFondo = convocatoria.getFondo()[0];
				if (null == tipoFondo.getImporteFinanciacion()){
					sbMensajeError.append("- Si rellena el campo 'Tipo de Fondo', debe rellenar también el campo 'Importe Fondo'.\\n");
				}
			}
			//FIN [dipucr-Felipe #447]
			
			
			//IDENTIFICACIÓN DE LAS REGIONES GEOGRAFICAS
			if(StringUtils.isEmpty(convocatoria.getRegion())){
				sbMensajeError.append(getTextoFaltaCampo("Region"));
			}
			
			//INFORMACION DE LAS ACTIVIDADES ECONÓMICAS
			if(StringUtils.isEmpty(convocatoria.getSector())){
				sbMensajeError.append(getTextoFaltaCampo("Sector"));
			}
			
			//INSTRUMENTOS DE AYUDA
			if(StringUtils.isEmpty(convocatoria.getInstrumento())){
				sbMensajeError.append(getTextoFaltaCampo("Instrumento"));
			}
			if(StringUtils.isEmpty(convocatoria.getTipoBeneficiario())){
				sbMensajeError.append(getTextoFaltaCampo("Tipo Beneficiario"));
			}
			if(StringUtils.isEmpty(convocatoria.getFinalidad())){
				sbMensajeError.append(getTextoFaltaCampo("Finalidad"));
			}
			if(StringUtils.isEmpty(convocatoria.getImpactoGenero())){
				sbMensajeError.append(getTextoFaltaCampo("Impacto Genero"));
			}
			if(StringUtils.isEmpty(convocatoria.getConcesionPublicable())){
				sbMensajeError.append(getTextoFaltaCampo("Concesión Publicable"));
			}
			//INICIO [dipucr-Felipe #400]
			boolean bAyudaDirecta = false;
			if(StringUtils.isEmpty(convocatoria.getAyudaDirecta())){
				sbMensajeError.append(getTextoFaltaCampo("Ayuda Directa"));
			}
			else{
				 bAyudaDirecta = Constants.VALIDACION.SI.equals(convocatoria.getAyudaDirecta());
			}
			
			boolean bSubvNominativa = false;
			if(StringUtils.isEmpty(convocatoria.getSubvencionNominativa())){
				sbMensajeError.append(getTextoFaltaCampo("Subvencion Nominativa"));
			}
			else{
				bSubvNominativa = Constants.VALIDACION.SI.equals(convocatoria.getSubvencionNominativa());
			}
			
			//INFORMACION DEL EXTRACTO DE LA CONVOCATORIA
			//No es necesario comprobar esta sección en el caso de ayudas directas o nominativas
			if (!bAyudaDirecta && !bSubvNominativa){ //FIN [dipucr-Felipe #400]
				if(StringUtils.isEmpty(convocatoria.getDiarioOficial())){
					sbMensajeError.append(getTextoFaltaCampo("Diario Oficial"));
				}
				if(StringUtils.isEmpty(convocatoria.getTituloExtracto())){
					sbMensajeError.append(getTextoFaltaCampo("Titulo Extracto"));
				}
				if(StringUtils.isEmpty(convocatoria.getTextoExtracto())){
					sbMensajeError.append(getTextoFaltaCampo("Texto Extracto"));
				}
				if(null == convocatoria.getFechaFirma()){
					sbMensajeError.append(getTextoFaltaCampo("Fecha Firma"));
				}
				if(StringUtils.isEmpty(convocatoria.getLugarFirma())){
					sbMensajeError.append(getTextoFaltaCampo("Lugar Firma"));
				}
				if(StringUtils.isEmpty(convocatoria.getFirmante())){
					sbMensajeError.append(getTextoFaltaCampo("Firmante"));
				}
			}
			
			error = sbMensajeError.toString();
			if (!StringUtils.isEmpty(error)){
				error = "En la pestaña BASE DE DATOS NACIONAL DE SUBVENCIONES:\\n\\n" + error;
			}
		}
		
		return error;
	}
	
	private String getTextoFaltaCampo(String nombreCampo){
		StringBuffer sbFaltaCampo = new StringBuffer();
		sbFaltaCampo.append("- Debe rellenar el campo '");
		sbFaltaCampo.append(nombreCampo);
		sbFaltaCampo.append("'.\\n");
		return sbFaltaCampo.toString();
	}

}
