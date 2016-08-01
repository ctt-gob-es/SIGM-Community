package es.dipucr.sigem.api.rule.procedures.rrhh;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.CircuitosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.webempleado.services.licencias.LicenciasWSProxy;


/**
 * [eCenpri-Felipe ticket #206]
 * Regla para validar la respuesta de personal a la petición de Moscosos o Vacaciones
 * Si se rechaza, comunicamos con el Portal, si no, continuamos con la firma del Jefe
 * @author Felipe
 * @since 28.03.2011
 */
public class ValidacionLicenciasPersonalRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(ValidacionLicenciasPersonalRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase, el trámite y envío al Jefe de departamento para firma
	 */
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ISignAPI signAPI = invesFlowAPI.getSignAPI();
			//*********************************************
			
			//*********************************************
			//Respuesta al portal del empleado -> Se actualiza el estado
			//*********************************************
			//Obtenemos los datos de la solicitud de licencias
			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			String numexp = DocumentosUtil.getNumExp(entitiesAPI, idDoc);
			IItemCollection collection = entitiesAPI.getEntities("RRHH_LICENCIAS", numexp);
			IItem itemSolicitudLicencias = (IItem)collection.iterator().next();
			
			//Vemos si la solicitud ha sido firmada o rechazada
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
			String strEstado = itemDocumento.getString("ESTADOFIRMA");
			
			//Si está rechazado, se lo comunicamos al portal
			if (strEstado.equals(SignStatesConstants.RECHAZADO)){
				
				String strMotivo = itemDocumento.getString("MOTIVO_RECHAZO");
				//Desglosamos el id de solicitud en NIF, año y NºLicencia
				String strIdSolicitud = itemSolicitudLicencias.getString("ID_SOLICITUD");
				String [] arrIdSolicitud = strIdSolicitud.split("-");
				String strNif = arrIdSolicitud[0];
				int iAno = Integer.valueOf(arrIdSolicitud[1]);
				String strNlic = arrIdSolicitud[2];
				
				//Hacemos la petición al servicio web
				LicenciasWSProxy wsLicencias = new LicenciasWSProxy();
				//[eCenpri-Felipe #601] Datos del circuito de firma
				String firmantes = CircuitosUtil.getFirmantesCircuito(rulectx);
//				wsLicencias.ponerLicenciaValidada(strNif, iAno, strNlic, false, strMotivo);
				wsLicencias.ponerLicenciaValidada(strNif, iAno, strNlic, false, strMotivo, firmantes);
				
				//Cerramos el trámite y el expediente
				ExpedientesUtil.cerrarExpediente(cct, numexp);
			}
			else{ 
				//Si ha sido aceptada, continuamos con la firma de las personas pertinentes
	    		//Estos circuitos de firma están configurados en la tabla de validación RRHH_VLDTBL_CIRCUITOS_LICENCIAS
	    		int idCircuitoFirma = Integer.MIN_VALUE;
	    		String strCodDepartamento = itemSolicitudLicencias.getString("COD_DEPARTAMENTO");
	    		String strQuery = "WHERE VALOR = '" + strCodDepartamento + "'";
				collection = entitiesAPI.queryEntities("RRHH_VLDTBL_CTOS_LICENCIAS", strQuery);
				List listCircuitos = collection.toList();
				
				if (listCircuitos.size() == 0){
					throw new ISPACRuleException("No hay ningún circuito añadido en la tabla de validación " +
							"RRHH_VLDTBL_CIRCUITOS_LICENCIAS para el Departamento(VALOR) " + strCodDepartamento);
				}
				else if (listCircuitos.size() > 1){
					throw new ISPACRuleException("Sa ha definido más de un circuito de firma en la tabla de validación " +
							"RRHH_VLDTBL_CIRCUITOS_LICENCIAS para el Departamento(VALOR) " + strCodDepartamento);
				}
				else if (listCircuitos.size() == 1){
					IItem itemCircuito = (IItem) listCircuitos.get(0);
					idCircuitoFirma = Integer.valueOf(itemCircuito.getString("SUSTITUTO"));
				}
				
	    		//Esta instrucción coge el sustituto de firma, si está definido
	    		signAPI.initCircuit(idCircuitoFirma, idDoc);
			}
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
