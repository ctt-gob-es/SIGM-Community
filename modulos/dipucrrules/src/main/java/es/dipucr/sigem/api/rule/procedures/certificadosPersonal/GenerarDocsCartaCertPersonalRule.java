package es.dipucr.sigem.api.rule.procedures.certificadosPersonal;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [eCenpri-Felipe #632]
 * Regla que se ejecuta en el procedimiento de carta digital
 * Recupera el documento de certificado y genera la plantilla
 * @author Felipe
 * @since 20.09.2012
 */
public class GenerarDocsCartaCertPersonalRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(GenerarDocsCartaCertPersonalRule.class);

	
	protected static final String _COD_PCD_CARTA = Constants.CERTPERSONAL._COD_PCD_CARTA;
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
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        String strQuery = null;
	        IItemCollection col = null;
	        String numexpActual = rulectx.getNumExp();
	        
	        //Generamos la plantilla de carta digital
	        DocumentosUtil.generarDocumento(rulectx, Constants.CERTPERSONAL._DOC_CARTADIGITAL, null);
	        	        
	        //Obtiene el expediente padre. El del certificado
	        strQuery = "WHERE NUMEXP_HIJO='" + numexpActual + "'";
	        col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
        	IItem itemExpRelacionados = (IItem)col.iterator().next();
        	String numexpPadre = itemExpRelacionados.getString("NUMEXP_PADRE");
        	
        	//Obtenemos el documento de certificado del expediente de Cert.Personal
        	col = entitiesAPI.getDocuments(numexpPadre, "NOMBRE='" + _DOC_CERTIFICADO +"'", "FDOC DESC");
        	IItem itemDocCertificado = (IItem)col.iterator().next();
			String strInfoPag = itemDocCertificado.getString("INFOPAG_RDE");
			File fileCertificado = DocumentosUtil.getFile(cct, strInfoPag, null, null);

    		String tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, "Anexo");
			int documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
    		DocumentosUtil.generaYAnexaDocumento(rulectx, documentTypeId, "Anexo", fileCertificado, itemDocCertificado.getString("EXTENSION_RDE"));
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
