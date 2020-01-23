package es.dipucr.sigem.api.rule.procedures.rrhh;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FileBean;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.common.utils.ZipUtils;
import es.dipucr.sigem.api.rule.procedures.Constants;
//import com.lowagie.text.pdf.AcroFields;


/**
 * [eCenpri-Felipe ticket #206]
 * Regla para enviar a la firma del jefe de departamento la Solicitud de Licencias de un empleado
 * @author Felipe
 * @since 26.11.2010
 */
public class EnviarSolicitudLicenciasAFirmaRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger
			.getLogger(EnviarSolicitudLicenciasAFirmaRule.class);
	
	protected static final String _TRAMITE_FIRMAS = "Firmar licencias"; //TODO Constants
	protected static final String _DOC_JUSTIFICANTE = "Justificante";
	protected static final String _DOC_SOLICITUD = "RRHH - Solicitud de Licencias";
	
	protected static final String _DEP_OK_PERSONAL = "OK_PERSO";
	protected static final String _FIRMA_PERSONAL = "RH";
	//[dipucr-Felipe 3#224] Firma personal previa a la firma del jefe para ciertos tipos de licencias
	protected static final String _FIRMA_PERSONAL_JEFE = "RJ";
	protected static final String _BOOLEANO_SI = "SI";
	
	
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
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
			ISignAPI signAPI = invesFlowAPI.getSignAPI();
			//*********************************************
			
			String strQuery = null;
			IItemCollection collection = null;	
			String numexp = rulectx.getNumExp();
			int pid = rulectx.getProcessId();
			
			//Obtenemos el objeto con los datos de la solicitud
			collection = entitiesAPI.getEntities("RRHH_LICENCIAS", numexp);
			IItem itemSolicitudLicencias = (IItem)collection.iterator().next();
			
			//INICIO [eCenpri-Felipe #51WE]
			//Obtenemos el objeto con los datos del expediente
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			
			//Obtenemos el NIF y los datos de la solicitud
			String strNif = itemExpediente.getString("NIFCIFTITULAR");
			int anio = itemSolicitudLicencias.getInt("ANIO");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String strFechaInicio = itemSolicitudLicencias.getString("FECHA_INICIO");
			String strFechaFin = itemSolicitudLicencias.getString("FECHA_FIN");
			Date dFechaInicio = sdf.parse(strFechaInicio);
			Date dFechaFin = sdf.parse(strFechaFin);
			Calendar calendarInicio = Calendar.getInstance();
			calendarInicio.setTime(dFechaInicio);
			Calendar calendarFin = Calendar.getInstance();
			calendarFin.setTime(dFechaFin);
			
			//Comprobamos si hay licencias coincidentes para el empleado
			//Puede haberse producido un error al firmar y que se solicite dos veces el mismo periodo
			boolean bHayLicenciasCoincidentes = LicenciasWSDispatcher.
				existenLicenciasCoincidentes(cct, strNif, anio, calendarInicio, calendarFin);
			
			//Si hay licencias coincidentes, lo anotamos en el log y cerramos el expediente
			//sin hacer nada más
			if (bHayLicenciasCoincidentes){
				logger.error("RRHH-Licencias: Ya existen licencias para el usuario " + strNif
						+ " en el periodo comprendido entre " + strFechaInicio + " y " + strFechaFin);
				logger.error("Se procede a cerrar el expediente de licencias " + numexp);
				ExpedientesUtil.cerrarExpediente(cct, numexp);
				return null;
			}
			//FIN [eCenpri-Felipe #51WE]
						
			//Creamos el trámite
			//******************
			//Necesitamos sacar la fase y la fasePcd, que no vienen en el contexto
			IItemCollection stages = invesFlowAPI.getStagesProcess(pid);
			IItem itemFase = (IItem) stages.toList().get(0);
			int idFase = Integer.valueOf(itemFase.getString("ID_FASE_BPM"));
			int idFasePcd = itemFase.getInt("ID_FASE");
			
			//Obtenemos primero el id del trámite en el diseño, tabla spac_p_tramites
			strQuery = "WHERE ID_FASE = " + idFasePcd + " AND NOMBRE = '" + _TRAMITE_FIRMAS + "'";
			collection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQuery);
			IItem itemTramite = (IItem)collection.iterator().next();
			int idTramitePcd = itemTramite.getInt("ID"); 
			
			//Creamos el trámite
			int idTramite = tx.createTask(idFase, idTramitePcd);
			
			//Copiamos el documento de Justificante en el trámite
			//***************************************************
			//Obtenemos el documento de justificante
			collection = DocumentosUtil.getDocumentsByNombre(cct, numexp, _DOC_JUSTIFICANTE);
			IItem itemDocJustificante = (IItem)collection.iterator().next();
			String strInfoPag = itemDocJustificante.getString("INFOPAG");
			File fileACopiar = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			
			//Pasamos los bytes a un fichero
			File fileJustificante = FileTemporaryManager.getInstance().newFile("." + Constants._EXTENSION_PDF);
			FileOutputStream fos = new FileOutputStream(fileJustificante);
			FileUtils.copy(fileACopiar, fos);
			fos.close();
			
			//Adjuntamos el documento de Justificante
			//Obtenemos primero el tipo de documento
			int idTipoDocumento = DocumentosUtil.getTipoDoc(cct, _DOC_SOLICITUD, DocumentosUtil.BUSQUEDA_EXACTA, false);

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
			
			//Adjuntamos el fichero de justificante como si fuese un documento de solicitud de licencias
			FileInputStream fis = new FileInputStream(fileJustificante);
			
    		//Quitamos firmas y ponemos la visualización por defecto
			PdfReader reader = new PdfReader((InputStream) fis);
			reader.removeFields(); //Eliminamos las firmas
			reader.consolidateNamedDestinations();
			
			//[dipucr-Felipe #1365 3#91] Creamos un nuevo documento, para que no se crucen las streams
			File fileAux = FileTemporaryManager.getInstance().newFile();
			fos = new FileOutputStream(fileAux, true);
			
			PdfStamper stamper = new PdfStamper(reader, fos);
			PdfWriter writer = stamper.getWriter();
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			reader.close();
			stamper.close();
			fos.close();
			
			//[dipucr-Felipe #1365 3#91]
			org.apache.commons.io.FileUtils.copyFile(fileAux, fileJustificante);
			fileAux.delete();
			
			//INICIO [eCenpri-Felipe #549] Anexamos los documentos adjuntos al justificante
			//INICIO [dipucr-Felipe #1350]
			IItemCollection anexosCollection = entitiesAPI.getDocuments
					(numexp, " TP_REG = 'ENTRADA' AND UPPER(DESCRIPCION) LIKE '%ZIP'", ""); //[dipucr-Felipe #1350]
			
			if (anexosCollection.toList().size() > 0){ //[eCenpri-Felipe #925]
				
				IItem itemZipAnexo =  (IItem) anexosCollection.iterator().next();
				String sInfopagZipAnexo = itemZipAnexo.getString("INFOPAG");
				
				File fileZipAnexos = DocumentosUtil.getFile
						(cct, sInfopagZipAnexo, null, null);
				
				List<FileBean> listFicheros = ZipUtils.extraerTodosFicheros(fileZipAnexos);
				
				//Los metemos en el PDF como adjuntos
				for (FileBean fb : listFicheros){
				
                    PdfUtil.anexarDocumento
				    (
				    		fileJustificante,
				    		fb.getFile(),
				    		normalizar(fb.getName())
				    );
				}
			}
			//FIN [dipucr-Felipe #1350]
    		//FIN [eCenpri-Felipe #549]
			
			IItem itemDocSolicitud = DocumentosUtil.generaYAnexaDocumento(rulectx, idTramite, idTipoDocumento, 
					itemSolicitudLicencias.getString("LICENCIA"), fileJustificante, Constants._EXTENSION_PDF);
			int idDocumento = itemDocSolicitud.getKeyInt();

    		//Mandamos el justificante a la firma
    		//***********************************
    		//Obtenemos primero el código de departamento recibido
    		String strCodDepartamento = itemSolicitudLicencias.getString("COD_DEPARTAMENTO");
			String strTipoLicencia = itemSolicitudLicencias.getString("COD_LICENCIA");
    		String strContratado = itemSolicitudLicencias.getString("CONTRATADO");
    		boolean bContratado = strContratado.equals(_BOOLEANO_SI);
    		
    		//Iniciamos un circuito de firma en el que se envía al jefe de departamento
    		//Estos circuitos de firma están configurados en la tabla de validación RRHH_VLDTBL_CIRCUITOS_LICENCIAS
    		int idCircuitoFirma = Integer.MIN_VALUE;
    		
    		//Inicio [eCenpri-Felipe #549] Comprobamos si la licencia va sólo a personal
    		if (strCodDepartamento.startsWith(_FIRMA_PERSONAL)){
    			String strCodDepartamentoReal = strCodDepartamento.replaceFirst(_FIRMA_PERSONAL, "");
    			itemSolicitudLicencias.set("COD_DEPARTAMENTO", strCodDepartamentoReal);
    			strCodDepartamento = _FIRMA_PERSONAL;
    		}
    		//INICIO [dipucr-Felipe 3#224]
    		else if (strCodDepartamento.startsWith(_FIRMA_PERSONAL_JEFE)){
    			String strCodDepartamentoReal = strCodDepartamento.replaceFirst(_FIRMA_PERSONAL_JEFE, "");
    			itemSolicitudLicencias.set("COD_DEPARTAMENTO", strCodDepartamentoReal);
    			strCodDepartamento = _DEP_OK_PERSONAL;
    		}
    		//FIN [dipucr-Felipe 3#224]
    		else{
	    		//Si es contratado tendremos que enviar inicialmente al departamento de personal
	    		if (bContratado && LicenciasWSDispatcher.tieneMaximoDias(cct, itemSolicitudLicencias)){
	    			strCodDepartamento = _DEP_OK_PERSONAL;
	    		}
    		}
    		
    		//Recuperamos de la tabla de validación el circuito asignado
    		strQuery = "WHERE VALOR = '" + strCodDepartamento + "'";
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
			
			//INICIO [eCenpri-Felipe #514]
    		//Cambiamos el Asunto del expediente para que le aparezca al firman
			String strNombreTitular = itemExpediente.getString("IDENTIDADTITULAR");
			String strNombreLicencia = itemSolicitudLicencias.getString("LICENCIA");
    		StringBuffer sbAsunto = new StringBuffer()
    			.append("Solicitud de ")
    			.append(strNombreLicencia)
    			.append("</br><b>")
				.append(strNombreTitular)
    			.append("</b></br>")
    			.append("Detalle días: ");
    		
    		if (strFechaInicio.equals(strFechaFin)){
    			sbAsunto.append(strFechaInicio);
    		}
    		else{
    			sbAsunto.append(" Del ")
	    				.append(strFechaInicio)
	    				.append(" al ")
	    				.append(strFechaFin);
    		}

    		itemExpediente.set("ASUNTO", sbAsunto.toString());
    		itemExpediente.store(cct);
    		//FIN [eCenpri-Felipe #514]
			
    		//Esta instrucción coge el sustituto de firma, si está definido
    		signAPI.initCircuit(idCircuitoFirma, idDocumento);
    		
    		//Creamos la licencia pendiente en la BBDD de personal
			//Esto se hace llamando al servicio web LicenciasWS de la WebEmpleado
			//***********************************
			//Obtenemos los datos de la solicitud
    		String strNumDias = itemSolicitudLicencias.getString("NUM_DIAS");
    		String strDetalleDias = itemSolicitudLicencias.getString("FECHAS_SOLICITADAS");
			String strObservaciones = itemSolicitudLicencias.getString("OBSERVACIONES");
			if (null == strObservaciones) strObservaciones = "";
			
			//Petición al WS
			//INICIO [dipucr-Felipe PE#105] Enviamos nreg, freg y numexp al portal del empleado
			String nreg = itemExpediente.getString("NREG");
			Date dFreg = itemExpediente.getDate("FREG");
			Calendar calendarFreg = Calendar.getInstance();
			calendarFreg.setTime(dFreg);
			String strIdSolicitud = LicenciasWSDispatcher.crearLicenciaPendiente(cct, strNif, strTipoLicencia, anio, 
					calendarInicio, calendarFin, strNumDias, strDetalleDias, strObservaciones, numexp, nreg, calendarFreg);
			//FIN [dipucr-Felipe PE#105]
			itemSolicitudLicencias.set("ID_SOLICITUD", strIdSolicitud);
			itemSolicitudLicencias.store(cct);
			
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación de los trámites y envío a firma de las licencias", e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	/**
	 * Normalizar
	 * @param name
	 * @return
	 */
	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}
	
	
}
