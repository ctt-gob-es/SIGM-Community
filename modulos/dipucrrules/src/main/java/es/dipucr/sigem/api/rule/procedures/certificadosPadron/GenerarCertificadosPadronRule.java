package es.dipucr.sigem.api.rule.procedures.certificadosPadron;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.lock.LockManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ClsDocumentoResponse;
import org.tempuri.IPadronProxy;

import es.dipucr.sigem.api.rule.common.AccesoBBDDTramitador;
import es.dipucr.sigem.api.rule.common.utils.CircuitosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [eCenpri-Felipe #743]
 * Genera el documento de certificado conectándose con el WS de ATM
 * Adjunto el documento al trámite
 * @author Felipe
 * @since 09.11.2012
 */
public class GenerarCertificadosPadronRule implements IRule 
{
	private static final Logger logger = Logger.getLogger(GenerarCertificadosPadronRule.class);

	protected static final String _DOC_CERTIFICADO = Constants.CERTPADRON._DOC_CERTIFICADO;
	private static final String EMAIL_SUBJECT_VAR_NAME = "PADRON_EMAIL_SUBJECT";
	private static final String EMAIL_CONTENT_VAR_NAME_ERROR = "PADRON_EMAIL_CONTENT_ERROR";
	
	
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
			//*********************************************
			
			IItemCollection collection = null;
	        String entidad = EntidadesAdmUtil.obtenerEntidad((ClientContext)cct);
			String numexp = rulectx.getNumExp();
			
			//Desbloqueamos el trámite para más tarde cerrarlo
			int idTask = rulectx.getTaskId();

//			[Dipucr-Felipe-Manu Ticket #596] * ALSIGM3 Error al registrar WS y Web con creación de documentos - Hitos de creación y borrado de documentos - LockManager.lock
//			cct.setTicket(""); //para evitar nullpointer
			
			//Recuperamos los datos del solicitante
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			String numDocumento = itemExpediente.getString("NIFCIFTITULAR");
			String nombre = itemExpediente.getString("IDENTIDADTITULAR");
			String mailInteresado = itemExpediente.getString("DIRECCIONTELEMATICA");
			
			//Recuperar los datos 
			collection = entitiesAPI.getEntities("CERT_PADRON", numexp);
			IItem itemSolicitudCertificado = (IItem)collection.iterator().next();
			int tipoCert = itemSolicitudCertificado.getInt("ID_CERTIFICADO");
			String nombreCert = itemSolicitudCertificado.getString("DESC_CERTIFICADO");
			String codInstitucion = itemSolicitudCertificado.getString("CODIGO_INE"); //Código INE
			String observacionesDoc = itemSolicitudCertificado.getString("OBSERV_DOCUMENTO");
			String efectoDoc = itemSolicitudCertificado.getString("EFECTO_DOCUMENTO");
			String individual = itemSolicitudCertificado.getString("INDIVIDUAL");
			boolean bFamiliar = (null != individual && individual.equals("No"));
			boolean bTipoCertificado = PadronUtils.esTipoCertificado(tipoCert);
			
			//Para el caso en el que solicite Personal en nombre de un usuario
			String numDocInteresado = itemSolicitudCertificado.getString("NIF_INTERESADO");
			String nombreInteresado = itemSolicitudCertificado.getString("NOMBRE_INTERESADO");
			if (StringUtils.isNotEmpty(numDocInteresado)){
				numDocumento = numDocInteresado;
				itemExpediente.set("NIFCIFTITULAR", numDocInteresado);
				nombre = nombreInteresado;
				itemExpediente.set("IDENTIDADTITULAR", nombreInteresado);
			}
						
			//Ponemos nombre al asunto del expediente
			StringBuffer sbAsunto = new StringBuffer();
			sbAsunto.append("Solicitud por parte de ");
			sbAsunto.append(nombre);
			sbAsunto.append(" del '");
			sbAsunto.append(nombreCert);
			sbAsunto.append("'");
			itemExpediente.set("ASUNTO", sbAsunto.toString());
			itemExpediente.store(cct);
			
			//Recuperamos el certificado del servicio web de ATM
			//INICIO [dipucr-Felipe #515]
			IPadronProxy wsPadron = new IPadronProxy();
			
//			int tipoDocumento = PadronUtils.getTipoDocumento(numDocumento);
//			if (tipoDocumento == Constants.CERTPADRON._TIPO_DOC_NIE){
//				numDocumento = PadronUtils.retocarNIE(numDocumento);
//			}
			
			// [dipucr-Alberto #798]
			// Recuperamos la persona
			PersonaPadron personaPadron = PadronUtils.getPersona(codInstitucion, numDocumento);
			numDocumento = personaPadron.getDocumento();
						
			codInstitucion = PadronUtils.retocarCodInstitucion(codInstitucion);
			ClsDocumentoResponse response = wsPadron.getDocumento(codInstitucion, numDocumento, 
					PadronUtils.getEnumTipoCert(tipoCert), observacionesDoc, efectoDoc, bFamiliar);
			
			//Controlamos el posible error
			if (!response.getCORRECTO()){
				
				//Se ha producido un error
				String errorCompleto = PadronExceptions.getDescripcion(response.getCODIGO_RESULTADO());
				
				Map<String, String> variables = PadronUtils.getVariablesPadron
						(itemSolicitudCertificado, itemExpediente, errorCompleto);
				//INICIO [dipucr-Felipe 3#703]
//				MailUtil.enviarCorreoConAcusesYVariables(rulectx, mailInteresado, EMAIL_SUBJECT_VAR_NAME,
//						EMAIL_CONTENT_VAR_NAME_ERROR, variables, null, null, nombre, false);
				MailUtil.enviarCorreoConVariablesUsoExterno(rulectx, mailInteresado, EMAIL_SUBJECT_VAR_NAME,
						EMAIL_CONTENT_VAR_NAME_ERROR, null, variables, false);
				//FIN [dipucr-Felipe 3#703]
				
				AccesoBBDDTramitador accesoTram = new AccesoBBDDTramitador(entidad);
				accesoTram.borrarBloqueo(LockManager.LOCKTYPE_PROCESS, rulectx.getProcessId());
				TramitesUtil.cerrarTramite(idTask, rulectx);
				return new Boolean(true);
			}
			
			//Obtenemos un archivo temporal y escribimos los bytes
			File fileCert = FileTemporaryManager.getInstance().newFile();
			FileOutputStream fos = new FileOutputStream(fileCert.getAbsolutePath()); 
		    fos.write(response.getDOCUMENTO());
		    fos.close();
			//FIN [dipucr-Felipe #515]
        	
    		String tpDoc = DocumentosUtil.getTipoDocumentoByPlantilla(cct, _DOC_CERTIFICADO);
			int documentTypeId = DocumentosUtil.getTipoDoc(cct, tpDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);
    		IItem itemDocumento = DocumentosUtil.generaYAnexaDocumento(rulectx, documentTypeId, _DOC_CERTIFICADO, fileCert, "pdf");
    		
    		int idDocumento = itemDocumento.getKeyInt();
    		
			fileCert.delete();
			
			//Si es de tipo certificado, lo debemos mandar a firma
			if (bTipoCertificado){
				CircuitosUtil.iniciarCircuitoTramite(rulectx, idDocumento);
			}
			else{ //Si es volante, no hace falta firma
				//Quitamos el bloqueo porque el closeTask vuelve a bloquear
				AccesoBBDDTramitador accesoTram = new AccesoBBDDTramitador(entidad);
				accesoTram.borrarBloqueo(LockManager.LOCKTYPE_PROCESS, rulectx.getProcessId());
				TramitesUtil.cerrarTramite(idTask, rulectx);
			}
			
		}
		catch (Exception e) {
			logger.error("Error en la generación del certificado del padrón. Expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación del certificado del padrón. Expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return null;
	}
	
	

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
