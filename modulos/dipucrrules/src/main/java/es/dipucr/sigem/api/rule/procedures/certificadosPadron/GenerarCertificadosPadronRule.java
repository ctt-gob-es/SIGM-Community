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

import org.apache.axis.message.MessageElement;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.atm2.ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult;
import es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult;
import es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult;
import es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult;
import es.atm2.PadronSoapProxy;
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
			boolean bTipoCertificado = false;
			
			//Desbloqueamos el trámite para más tarde cerrarlo
			int idTask = rulectx.getTaskId();
			cct.setTicket(""); //para evitar nullpointer
			
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
			
			//Para el caso en el que solicite Personal en nombre de un usuario
			String numDocInteresado = itemSolicitudCertificado.getString("NIF_INTERESADO");
			String nombreInteresado = itemSolicitudCertificado.getString("NOMBRE_INTERESADO");
			if (StringUtils.isNotEmpty(numDocInteresado)){
				numDocumento = numDocInteresado;
				itemExpediente.set("NIFCIFTITULAR", numDocInteresado);
				nombre = nombreInteresado;
				itemExpediente.set("IDENTIDADTITULAR", nombreInteresado);
			}
			
			int tipoDocumento = PadronUtils.getTipoDocumento(numDocumento);
			if (tipoDocumento == Constants.CERTPADRON._TIPO_DOC_NIE){
				numDocumento = PadronUtils.retocarNIE(numDocumento);
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
			PadronSoapProxy wsPadron = new PadronSoapProxy();
			MessageElement[] arrResponse = null;
			MessageElement msgElement = null;
			
			if (tipoCert == Constants.CERTPADRON._TIPO_VOLANTE){
				ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult volante = 
						wsPadron.obtenerVolanteEmpadronamiento(codInstitucion, String.valueOf(tipoDocumento), numDocumento);
				arrResponse = volante.get_any();
			}
			else if (tipoCert == Constants.CERTPADRON._TIPO_CERTIFICADO){
				bTipoCertificado = true;
				ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult cert = 
						wsPadron.obtenerCertificadoEmpadronamiento(codInstitucion, String.valueOf(tipoDocumento), numDocumento);
				arrResponse = cert.get_any();
			}
			//INICIO [eCenpri-Felipe #1035] Volantes de convivencia
			else if (tipoCert == Constants.CERTPADRON._TIPO_VOL_FAMILIAR){
				ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult volanteFamiliar = 
						wsPadron.obtenerVolanteConvivencia(codInstitucion, String.valueOf(tipoDocumento), numDocumento);
				arrResponse = volanteFamiliar.get_any();
			}
			else if (tipoCert == Constants.CERTPADRON._TIPO_CERT_FAMILIAR){
				bTipoCertificado = true;
				ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult certFamiliar = 
						wsPadron.obtenerCertificadoConvivencia(codInstitucion, String.valueOf(tipoDocumento), numDocumento);
				arrResponse = certFamiliar.get_any();
			}//FIN [eCenpri-Felipe #1035]
			else{
				throw new ISPACRuleException("Tipo de certificado no soportado");
			}
			msgElement = arrResponse[0];
			
			//Obtenemos el nodo documento
			Node nodoDocumento = null;
			NodeList listNodosDoc = msgElement.getElementsByTagName("Documento");
			if (listNodosDoc.getLength() == 0){
				//Se ha producido un error
				Node nodoError = msgElement.getElementsByTagName("MensajeError").item(0);
				String textoError = nodoError.getFirstChild().getFirstChild().getNodeValue();
				String codigoError = nodoError.getLastChild().getFirstChild().getNodeValue();
				String errorCompleto = "[" + codigoError + "] " + textoError;
				
				Map<String, String> variables = PadronUtils.getVariablesPadron
						(itemSolicitudCertificado, itemExpediente, errorCompleto);
				MailUtil.enviarCorreoConAcusesYVariables(rulectx, mailInteresado, EMAIL_SUBJECT_VAR_NAME,
						EMAIL_CONTENT_VAR_NAME_ERROR, variables, null, null, nombre, false);
				
				//Quitamos el bloqueo porque el closeTask vuelve a bloquear
				//No usamos el siguiente código porque requiere de una sesión que no tenemos
//				LockManager lockMgr = new LockManager(cct);
//				lockMgr.unlockObj(LockManager.LOCKTYPE_PROCESS, rulectx.getProcessId());
				AccesoBBDDTramitador accesoTram = new AccesoBBDDTramitador(entidad);
				accesoTram.borrarBloqueo(LockManager.LOCKTYPE_PROCESS, rulectx.getProcessId());
				TramitesUtil.cerrarTramite(idTask, rulectx);
				return new Boolean(true);
			}
			
			nodoDocumento = listNodosDoc.item(0);
			String strB64File = nodoDocumento.getFirstChild().getNodeValue();
			logger.warn(strB64File);
			
//			BASE64Decoder b64Decoder = new BASE64Decoder();
//			byte[] arrBytes = b64Decoder.decodeBuffer(strB64File);
			byte[] arrBytes = Base64.decodeBase64(strB64File);
			
			//Obtenemos un archivo temporal y escribimos los bytes
			File fileCert = FileTemporaryManager.getInstance().newFile();
			FileOutputStream fos = new FileOutputStream(fileCert.getAbsolutePath()); 
		    fos.write(arrBytes);
		    fos.close();
        	
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
