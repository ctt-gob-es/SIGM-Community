package es.dipucr.sigem.api.rule.procedures.rrhh;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.CircuitosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.webempleado.services.avisos.InsertarAvisoProxy;
import es.dipucr.webempleado.services.comisionServicio.ComisionServicioWSProxy;


/**
 * [eCenpri-Felipe ticket #206]
 * Regla para enviar la respuesta de la firma al portal del empleado, notificar por comparece
 * y cerrar tanto el trámite como el expediente
 * @author Felipe
 * @since 28.02.2011
 */
public class RespuestaSolicitudLicenciasRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(RespuestaSolicitudLicenciasRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase, el trámite y envío al Jefe de departamento para firma
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			//*********************************************
			//Respuesta al portal del empleado -> Se actualiza el estado
			//*********************************************
			//Vemos si la solicitud ha sido firmada o rechazada
			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
			String estado = itemDocumento.getString("ESTADOFIRMA");
			String strMotivo = null;
			boolean bFirmado = false;
			
			if (estado.equals(SignStatesConstants.FIRMADO)){
				bFirmado = true;
				strMotivo = itemDocumento.getString("MOTIVO_REPARO");
				//por si hay un reparo anterior [eCenpri-Felipe #601]
			}
			else if (estado.equals(SignStatesConstants.FIRMADO_CON_REPAROS)){
				bFirmado = true;
				strMotivo = itemDocumento.getString("MOTIVO_REPARO");
			}
			else if (estado.equals(SignStatesConstants.RECHAZADO)){
				bFirmado = false;
				strMotivo = itemDocumento.getString("MOTIVO_RECHAZO");
			}
			//[eCenpri-Felipe #601] Datos del circuito de firma
			String firmantes = CircuitosUtil.getFirmantesCircuito(rulectx);
			
			//Desglosamos el id de solicitud en NIF, año y NºLicencia
			String numexp = DocumentosUtil.getNumExp(entitiesAPI, idDoc);
			IItemCollection collection = entitiesAPI.getEntities("RRHH_LICENCIAS", numexp);
			
			if (collection.toList().size() > 0){
				IItem itemSolicitudLicencias = (IItem)collection.iterator().next();
				String strIdSolicitud = itemSolicitudLicencias.getString("ID_SOLICITUD");
				
				//Hacemos la petición al servicio web
				LicenciasWSDispatcher.ponerLicenciaValidada
					(cct, strIdSolicitud, bFirmado,	strMotivo, firmantes);
			}
			else{//INICIO [dipucr-Felipe #693]
				collection = entitiesAPI.getEntities("COMISION_SERVICIO", numexp);
				
				StringBuffer sbCuerpo = new StringBuffer();
				String asunto = null;
				
				if (collection.toList().size() > 0){
					
					IItem itemComision = collection.value();
					Integer idComision = Integer.valueOf(itemComision.getString("ID_COMISION"));
					
					ComisionServicioWSProxy wsComisiones = new ComisionServicioWSProxy();
					wsComisiones.cambiarEstadoComision(idComision, bFirmado, strMotivo, firmantes);
				}
				else{
					String nombreDoc = itemDocumento.getString("NOMBRE");
					if (bFirmado){
						asunto = "Autorizado: " + nombreDoc;
					}
					else{
						asunto = "Rechazado: " + nombreDoc;
					}
					sbCuerpo.append(asunto);
					sbCuerpo.append("\nFirmante(s): " + firmantes);
					
					IItem itemExpediente = entitiesAPI.getExpedient(numexp);
					String nif = itemExpediente.getString("NIFCIFTITULAR");
					
					InsertarAvisoProxy wsAvisos = new InsertarAvisoProxy();
					wsAvisos.nuevoAvisoUsuario(asunto, sbCuerpo.toString(), nif);
				}
				
			}//FIN [dipucr-Felipe #693]
			
			//Cerramos el trámite y el expediente
			ExpedientesUtil.cerrarExpediente(cct, numexp);
		}
		catch (Exception e) {
			logger.error("Error en la generación de los trámites y envío a firma de las licencias. " + e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación de los trámites y envío a firma de las licencias. " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
