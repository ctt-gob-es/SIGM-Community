package es.dipucr.sigem.api.rule.procedures.certificadosPadron;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [eCenpri-Felipe #743]
 * Envia un mail al usuario con el certificado/volante de empadronamiento generado
 * @author Felipe
 * @since 15.11.2012
 */
public class EnviarMailCertPadronRule  implements IRule {
	
	private static final String EMAIL_SUBJECT_VAR_NAME = "PADRON_EMAIL_SUBJECT";
	private static final String EMAIL_CONTENT_VAR_NAME = "PADRON_EMAIL_CONTENT";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		String nombreCert = null;
		String numDocumento = null;
		
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //---------------------------------------------------------------------------------------------- 

	        IItemCollection collection = null;
			String numexp = rulectx.getNumExp();
			StringBuffer sbQuery = new StringBuffer();
	        
			//Vemos si se ha generado el documento
			sbQuery.append("NOMBRE='");
			sbQuery.append(Constants.CERTPADRON._DOC_CERTIFICADO);
			sbQuery.append("'");
			collection = entitiesAPI.getDocuments(numexp, sbQuery.toString(), "");
			
			//Si no hay documentos, ha ocurrido algún error
			//Ya se ha enviado el mail de error y por tanto no hacemos nada
			if (collection.toList().size() == 0){
				return new Boolean(true);
			}
			
			IItem itemDocumento = (IItem) collection.iterator().next();
			
	        //Recuperamos los datos del solicitante
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			numDocumento = itemExpediente.getString("NIFCIFTITULAR");
			String nombre = itemExpediente.getString("IDENTIDADTITULAR");
			String mailInteresado = itemExpediente.getString("DIRECCIONTELEMATICA");
			
			//Recuperar los datos 
			collection = entitiesAPI.getEntities("CERT_PADRON", numexp);
			IItem itemSolicitudCertificado = (IItem)collection.iterator().next();
			int tipoCert = itemSolicitudCertificado.getInt("ID_CERTIFICADO");
			nombreCert = itemSolicitudCertificado.getString("DESC_CERTIFICADO");
			
			//Sólo los certificados irán firmados, los volantes no
			String strInfoPag = null;
			if (PadronUtils.esTipoCertificado(tipoCert)){//[dipucr-Felipe #515]
				strInfoPag = itemDocumento.getString("INFOPAG_RDE");
			}
			else{
				strInfoPag = itemDocumento.getString("INFOPAG");
			}
			
			//Recuperamos el fichero
			File fileCert = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			
			//Enviamos el mail
			Map<String, String> variables = PadronUtils.getVariablesPadron
					(itemSolicitudCertificado, itemExpediente, null);
			
			//INICIO [dipucr-Felipe 3#703]
			File fileCertNombre = File.createTempFile(nombreCert.replace(" ", "_"), "." + Constants._EXTENSION_PDF);
			
			FileOutputStream fos = new FileOutputStream(fileCertNombre);
			FileUtils.copy(fileCert, fos);
			
//			MailUtil.enviarCorreoConAcusesYVariables(rulectx, mailInteresado, EMAIL_SUBJECT_VAR_NAME,
//					EMAIL_CONTENT_VAR_NAME, variables, fileCert, nombreAdjunto, nombre, false);
			MailUtil.enviarCorreoConVariablesUsoExterno(rulectx, mailInteresado, EMAIL_SUBJECT_VAR_NAME,
					EMAIL_CONTENT_VAR_NAME, fileCertNombre, variables, false);

			fos.close();
			fileCertNombre.delete();
			//FIN [dipucr-Felipe 3#703]
			
			return new Boolean(true);
		}
		catch(Exception e){
			throw new ISPACRuleException("Error al enviar el " + nombreCert +
					" al solicitante con nº documento " + numDocumento, e);
	    }
	}

		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
