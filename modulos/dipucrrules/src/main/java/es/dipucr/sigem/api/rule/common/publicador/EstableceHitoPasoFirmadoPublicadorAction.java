package es.dipucr.sigem.api.rule.common.publicador;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tecdoc.sgm.base.guid.Guid;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.FicherosHito;
import ieci.tecdoc.sgm.core.services.consulta.HitoExpediente;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

/**
 * Acción para insertar un hito en el publicador tras cada firma de los circuitos
 * 
 */
public class EstableceHitoPasoFirmadoPublicadorAction implements IRule {
	
    private static final Logger logger = Logger.getLogger(EstableceHitoPasoFirmadoPublicadorAction.class);

    public static String _MENSAJE_FIRMADO = "Documento Firmado: ";
	public static String _MENSAJE_FIRMADO_REPARO = "Documento Firmado con Reparo: ";
	public static String _MENSAJE_RECHAZADO = "Documento Rechazado: ";

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
    	
        logger.info("INICIO - " + this.getClass().getName());

		int instancedStepId = 0;
		String usrFirmante = "";
		String nombreFirmante = "";
		String nombreDoc = "";
		Date fecha = new Date();
		StringBuffer textoHito = new StringBuffer();
		
        String numexp = "";
        try {	        
        
        	IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			ISignAPI signAPI = invesflowAPI.getSignAPI();

			instancedStepId = rulectx.getInt("ID_INSTANCIA_PASO");
			IItem instancedStep = signAPI.getStepInstancedCircuit(instancedStepId);
			
			int documentoId = instancedStep.getInt("ID_DOCUMENTO");
			IItem documento = DocumentosUtil.getDocumento(entitiesAPI, documentoId);
			nombreDoc = documento.getString("NOMBRE");
			numexp = documento.getString("NUMEXP");
			usrFirmante = instancedStep.getString("NOMBRE_FIRMANTE");
			
			nombreFirmante = UsuariosUtil.getNombreFirma((ClientContext) cct, usrFirmante);
			
			String strEstadoFirma = documento.getString("ESTADOFIRMA");

			textoHito.append("<labels><label locale=\"es\">");				
			
			if (strEstadoFirma.equals(SignStatesConstants.FIRMADO_CON_REPAROS)){
				textoHito.append(_MENSAJE_FIRMADO_REPARO);
			}
			else if (strEstadoFirma.equals(SignStatesConstants.RECHAZADO)){
				textoHito.append(_MENSAJE_RECHAZADO);
			}
			else{
				textoHito.append(_MENSAJE_FIRMADO);
			}
			
			textoHito.append( nombreDoc + " firmado por: " + nombreFirmante);
			textoHito.append("\n");
			textoHito.append("Fecha: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fecha));
			textoHito.append("</label></labels>");
			
			ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();
			
			boolean pasoAHistorico = true;
			
			HitoExpediente hito = new HitoExpediente();
			hito.setGuid(new Guid().toString());
			hito.setNumeroExpediente(numexp);
			hito.setCodigo(StringUtils.nullToEmpty(""));
			hito.setFecha(TypeConverter.toString(fecha, "yyyy-MM-dd"));
			hito.setDescripcion(StringUtils.nullToEmpty(textoHito.toString()));
			hito.setInformacionAuxiliar("");
			
			consulta.establecerHitoActual(hito, new FicherosHito(), pasoAHistorico, EntidadHelper.getEntidad());

        } catch (ISPACException e) {
        	logger.error("Error al establecer el hito de fin de paso en circuito de firma del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("Error al establecer el hito de fin de paso en circuito de firma del expediente: " + numexp + ". " + e.getMessage());
        } catch (Throwable e) {
        	logger.error("Error al establecer el hito de fin de paso en circuito de firma del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("Error al establecer el hito de fin de paso en circuito de firma del expediente: " + numexp + ". " + e.getMessage());
        }
        
        logger.info("FIN - " + this.getClass().getName());
        return true;
    }

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}