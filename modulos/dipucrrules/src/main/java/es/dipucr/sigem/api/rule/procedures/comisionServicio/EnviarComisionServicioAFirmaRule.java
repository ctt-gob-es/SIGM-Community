package es.dipucr.sigem.api.rule.procedures.comisionServicio;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.FileBean;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.common.utils.ZipUtils;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.webempleado.services.comisionServicio.ComisionServicioWSProxy;


/**
 * [eCenpri-Felipe #693]
 * Recupera el documento de comisión de servicio y lo manda a la firma
 * @author Felipe
 * @since 27.03.2018
 */
public class EnviarComisionServicioAFirmaRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(EnviarComisionServicioAFirmaRule.class);


	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}
	
	/**
	 * Generación de la fase y el trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ISignAPI signAPI = invesFlowAPI.getSignAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			
			IItemCollection collection = entitiesAPI.getEntities("COMISION_SERVICIO", numexp);
			IItem itemComision = (IItem)collection.iterator().next();
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			
			ComisionServicioWSProxy wsComisiones = new ComisionServicioWSProxy();
			String nif = itemExpediente.getString("NIFCIFTITULAR");
			
			String strFechaInicio = itemComision.getString("FECHA_INICIO");
			String strFechaFin = itemComision.getString("FECHA_FIN");
			Calendar calFechaInicio = FechasUtil.convertToCalendar(FechasUtil.convertToDate(strFechaInicio));
			Calendar calFechaFin = FechasUtil.convertToCalendar(FechasUtil.convertToDate(strFechaFin));
			boolean bComisionesCoincidentes = wsComisiones.existenComisionesCoincidentes(nif, calFechaInicio, calFechaFin);
					
			if (bComisionesCoincidentes){
				logger.error("RRHH-Comisiones: Ya existen comisiones para el usuario " + nif
						+ " en el periodo comprendido entre " + strFechaInicio + " y " + strFechaFin);
				logger.error("Se procede a cerrar el expediente de licencias " + numexp);
				ExpedientesUtil.cerrarExpediente(cct, numexp);
				return null;
			}
			
			//El contexto viene vacío, tenemos que rellenarlo
			IProcess exp = invesFlowAPI.getProcess(numexp);
			// Inicia el contexto de ejecución para que se ejecuten
            // las reglas asociadas a la entidad
            StateContext stateContext = cct.getStateContext();
            if (stateContext == null) {
                stateContext = new StateContext();
                ((ClientContext)cct).setStateContext(stateContext);
            }
            stateContext.setPcdId(exp.getInt("ID_PCD"));
            stateContext.setProcessId(exp.getKeyInt());
            stateContext.setNumexp(numexp);
			
			IItemCollection anexosCollection = entitiesAPI.getDocuments
					(numexp, "UPPER(NOMBRE) = 'ANEXO A SOLICITUD'"
						  + " AND UPPER(DESCRIPCION) LIKE '%ZIP'", "");
			
			FileBean fb = null;
			if (anexosCollection.toList().size() > 0){
				
				IItem itemZipAnexo =  (IItem) anexosCollection.iterator().next();
				String sInfopagZipAnexo = itemZipAnexo.getString("INFOPAG");
				
				File fileZipAnexos = DocumentosUtil.getFile
						(cct, sInfopagZipAnexo, null, null);
				
				List<FileBean> listFicheros = ZipUtils.extraerTodosFicheros(fileZipAnexos);
				File filePrincipal = null;
				
				for (int i = 0; i<listFicheros.size(); i++){

					if (i == 0){
						fb = listFicheros.get(i);
						filePrincipal = fb.getFile();
					}
					else{
						FileBean fb2 = listFicheros.get(i); 
						PdfUtil.anexarDocumento(filePrincipal, fb2.getFile(), fb2.getName());
					}
				}
				fb.setFile(filePrincipal);
			}

			int idTipoDocumento = DocumentosUtil.getIdTipoDocByCodigo(cct, Constants.COMISIONSERV.COD_TPDOC_COMISION);
			IItem itemDocSolicitud = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), idTipoDocumento, 
					fb.getName(), fb.getFile(), Constants._EXTENSION_PDF);
			int idDocumento = itemDocSolicitud.getKeyInt();

    		//Mandamos el documento a la firma
    		//***********************************
    		//Iniciamos un circuito de firma en el que se envía al jefe de departamento
    		//Estos circuitos de firma están configurados en la tabla de validación RRHH_VLDTBL_CIRCUITOS_LICENCIAS
    		int idCircuitoFirma = Integer.MIN_VALUE;

    		String strCodDepartamento = itemComision.getString("COD_DEPARTAMENTO");
    		
    		//Recuperamos de la tabla de validación el circuito asignado
    		String strQuery = "WHERE VALOR = '" + strCodDepartamento + "'";
			collection = entitiesAPI.queryEntities("RRHH_VLDTBL_CTOS_LICENCIAS", strQuery);
			List<?> listCircuitos = collection.toList();
			
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
			
    		//Cambiamos el Asunto del expediente para que le aparezca al firman			
			String strNombreTitular = itemExpediente.getString("IDENTIDADTITULAR");
			
    		StringBuffer sbAsunto = new StringBuffer()
    			.append("Solicitud de Comisión de Servicio</br><b>")
				.append(strNombreTitular)
    			.append("</b></br>")
    			.append("Detalle días: ");
    		
    		if (strFechaInicio.equals(strFechaFin)){
    			sbAsunto.append(strFechaInicio);
    		}
    		else{
    			sbAsunto.append(" Del ").append(strFechaInicio)
	    				.append(" al ").append(strFechaFin);
    		}

    		itemExpediente.set("ASUNTO", sbAsunto.toString());
    		itemExpediente.store(cct);
			
    		//Esta instrucción coge el sustituto de firma, si está definido
    		signAPI.initCircuit(idCircuitoFirma, idDocumento);
    		
    		//Petición al WS
			String nreg = itemExpediente.getString("NREG");
			String motivo = itemComision.getString("MOTIVO");
			String horaInicio = itemComision.getString("HORA_INICIO");
			String horaFin = itemComision.getString("HORA_FIN");
			String observaciones = itemComision.getString("OBSERVACIONES");
			
			Integer idSolicitud = wsComisiones.crearComisionPendiente
					(nif, motivo, calFechaInicio, horaInicio, calFechaFin, horaFin, observaciones, nreg, numexp);
			itemComision.set("ID_COMISION", idSolicitud);
			itemComision.store(cct);
    		
		}
		catch (Exception e) {
			String error = "Error en la envío a la firma del documento de 'Comisión': " + rulectx.getNumExp() + ". " + e.getMessage();
			logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
