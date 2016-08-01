package es.dipucr.sigem.api.rule.procedures.certificadosPersonal;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.io.File;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [eCenpri-Felipe #632]
 * Regla que se ejecuta al iniciar el trámite de "Notificación del Certificado"
 * @author Felipe
 * @since 05.09.2012
 */
public class CopiarDocCertPersonalRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CopiarDocCertPersonalRule.class);
	
	protected static final String _TRAMITE_FIRMA = Constants.CERTPERSONAL._TRAMITE_FIRMA;
	protected static final String _DOC_CERTIFICADO = Constants.CERTPERSONAL._DOC_CERTIFICADO;
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validamos si podemos insertar el tercero
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}
	
	/**
	 * Copiamos el documento del primer trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{

			//*********************************************
			IClientContext cct = rulectx.getClientContext();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			
			//Obtenemos el documento de certificado del primer trámite
			IItem itemDocPrimerTramite = DocumentosUtil.getPrimerDocumentByNombre(numexp, rulectx, _DOC_CERTIFICADO);
			String strInfoPag = itemDocPrimerTramite.getString("INFOPAG_RDE");
			String extension = itemDocPrimerTramite.getString("EXTENSION_RDE");
			File fileCertificado = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			
			String tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, _DOC_CERTIFICADO);
			int documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
			DocumentosUtil.generaYAnexaDocumento(rulectx, documentTypeId, _DOC_CERTIFICADO, fileCertificado, extension);
			
		}
		catch (Exception e) {
			logger.error("Error al crear el documento de notificación del certificado. " +e.getMessage(), e);
			throw new ISPACRuleException("Error al crear el documento de notificación del certificado", e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
