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
import ieci.tdw.ispac.ispaclib.sign.SignConnectorFactory;

import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.CircuitosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.portafirmas.DipucrDocPortafirmas;


/**
 * [eCenpri-Felipe ticket #206]
 * Regla para validar la respuesta de personal a la petici�n de Moscosos o Vacaciones
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
	 * Generaci�n de la fase, el tr�mite y env�o al Jefe de departamento para firma
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
			
			//Si est� rechazado, se lo comunicamos al portal
			if (strEstado.equals(SignStatesConstants.RECHAZADO)){
				
				String strMotivo = itemDocumento.getString("MOTIVO_RECHAZO");
				//Desglosamos el id de solicitud en NIF, a�o y N�Licencia
				String strIdSolicitud = itemSolicitudLicencias.getString("ID_SOLICITUD");
				
				//Hacemos la petici�n al servicio web
				//[dipucr-Felipe #1246]
				boolean bPortafirmas = entitiesAPI.isDocumentPortafirmas(itemDocumento);
				String firmantes = null;
				if (bPortafirmas){
					firmantes = CircuitosUtil.getFirmantesDocumentoPortafirmas(cct, idDoc);
				}
				else{
					firmantes = CircuitosUtil.getFirmantesCircuito(rulectx);//[dipucr-Felipe #1246]
				}
				
				LicenciasWSDispatcher.ponerLicenciaValidada(cct, strIdSolicitud, false, strMotivo, firmantes);
				
				//Cerramos el tr�mite y el expediente
				ExpedientesUtil.cerrarExpediente(cct, numexp);
			}
			else{ 
				//Si ha sido aceptada, continuamos con la firma de las personas pertinentes
	    		//Estos circuitos de firma est�n configurados en la tabla de validaci�n RRHH_VLDTBL_CIRCUITOS_LICENCIAS
	    		int idCircuitoFirma = Integer.MIN_VALUE;
	    		String strCodDepartamento = itemSolicitudLicencias.getString("COD_DEPARTAMENTO");
	    		String strQuery = "WHERE VALOR = '" + strCodDepartamento + "'";
				collection = entitiesAPI.queryEntities("RRHH_VLDTBL_CTOS_LICENCIAS", strQuery);
				List listCircuitos = collection.toList();
				
				if (listCircuitos.size() == 0){
					throw new ISPACRuleException("No hay ning�n circuito a�adido en la tabla de validaci�n " +
							"RRHH_VLDTBL_CIRCUITOS_LICENCIAS para el Departamento(VALOR) " + strCodDepartamento);
				}
				else if (listCircuitos.size() > 1){
					throw new ISPACRuleException("Sa ha definido m�s de un circuito de firma en la tabla de validaci�n " +
							"RRHH_VLDTBL_CIRCUITOS_LICENCIAS para el Departamento(VALOR) " + strCodDepartamento);
				}
				else if (listCircuitos.size() == 1){
					IItem itemCircuito = (IItem) listCircuitos.get(0);
					idCircuitoFirma = Integer.valueOf(itemCircuito.getString("SUSTITUTO"));
				}
				
	    		//Esta instrucci�n coge el sustituto de firma, si est� definido
				//INICIO [dipucr-Felipe #1246] Portafirmas
				// Volvemos a mandar el mismo idDoc a la firma, por lo que borramos su registro en la tabla DPCR_DOC_PORTAFIRMAS
				if (entitiesAPI.isDocumentPortafirmas(itemDocumento)){ //Portafirmas
					DipucrDocPortafirmas docPortafirmas = new DipucrDocPortafirmas(cct, itemDocumento);
					docPortafirmas.delete();
					
					itemDocumento.set("INFOPAG_RDE", "");
					itemDocumento.store(cct);
				}
				//FIN [dipucr-Felipe #1246]
	    		signAPI.initCircuitPortafirmas(idCircuitoFirma, idDoc);//[dipucr-Felipe #1246]
			}
		}
		catch (Exception e) {
			logger.error("Error en la generaci�n de los tr�mites y env�o a firma de las licencias. " + e.getMessage(), e);
			throw new ISPACRuleException("Error en la generaci�n de los tr�mites y env�o a firma de las licencias. " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
