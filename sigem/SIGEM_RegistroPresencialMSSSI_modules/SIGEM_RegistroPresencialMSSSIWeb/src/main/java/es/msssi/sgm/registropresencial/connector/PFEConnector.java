package es.msssi.sgm.registropresencial.connector;

import java.util.ArrayList;
import java.util.List;

import beans.DocumentArchiveResponse;
import beans.DocumentDownloadResponse;
import beans.RemoteSignBeginResponse;
import beans.RemoteSignEndResponse;
import core.jsign.services.DocumentArchiveServiceImpl;
import core.jsign.services.RemoteSignServiceImpl;
import core.log.ApplicationLogger;
import core.ws.jsign.DocumentArchiveRequest;
import core.ws.jsign.DocumentDownloadRequest;
import core.ws.jsign.RemoteSignBeginRequest;
import core.ws.jsign.RemoteSignEndRequest;
import core.ws.jsign.SignatureAppearancePDF;
import core.ws.jsign.enums.DocumentState;
import core.ws.jsign.enums.MultisignatureType;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.msssi.sgm.registropresencial.utils.KeysRP;

/**
 * Conector con PFE.
 * 
 */
public class PFEConnector {


    /**
     * Solicitud de archivado de documento en PFE.
     * 
     * @param rq
     *            DocumentArchiveRequest.
     * 
     * @return Resultado de operacion de archivado de documento.
     * @throws RPRegisterException 
     */
    public static DocumentArchiveResponse documentArchive(DocumentArchiveRequest rq) throws RPRegisterException {
	DocumentArchiveResponse response = null;
	try {
	    response = DocumentArchiveServiceImpl.documentArchive(rq);
	}
	catch (Throwable t) {
	    ApplicationLogger.error(t.getMessage(), t);
	    throw new RPRegisterException(null, t);
	}
	return response;
    }

    /**
     * Solicitud de archivado de documento en PFE.
     * 
     * @param fileName
     *            Nombre de documento.
     * @param fileData
     *            Contenido del documento.
     * 
     * @return Resultado de operacion de archivado de documento.
     * @throws RPRegisterException 
     */
    public static DocumentArchiveResponse documentArchive(String fileName, byte[] fileData) throws RPRegisterException {
	DocumentArchiveRequest rq = new DocumentArchiveRequest();
	rq.setApplicationAlias(KeysRP.PFE_APP_ALIAS);
	rq.setFileName(fileName);
	rq.setFileData(fileData);
	return documentArchive(rq);
    }

    /**
     * Solicitud de descarga de documento de PFE.
     * 
     * @param rq
     *            DocumentDownloadRequest.
     * 
     * @return Resultado de operacion de descarga de documento.
     * @throws RPRegisterException 
     */
    public static DocumentDownloadResponse documentDownload(DocumentDownloadRequest rq) throws RPRegisterException {
	DocumentDownloadResponse response = null;
	try {
	    response = DocumentArchiveServiceImpl.documentDownload(rq);
	}
	catch (Throwable t) {
	    ApplicationLogger.error(t.getMessage(), t);
	    throw new RPRegisterException(null, t);
	}
	return response;
    }

    /**
     * Solicitud de descarga de documento de PFE.
     * 
     * @param documentId
     *            Id de documento.
     * @param original
     *            Retornar original.
     * 
     * @return Resultado de operacion de descarga de documento.
     * @throws RPRegisterException 
     */
	public static DocumentDownloadResponse documentDownload(String documentId, boolean original) throws RPRegisterException {
		List<String> listDocumentId = new ArrayList<String>();
		listDocumentId.add(documentId);
		return masiveDocumentDownload(listDocumentId, original);		
	}
    
    /**
     * Solicitud de descarga de documentos de PFE.
     * 
     * @param listDocumentId
     *            Ids de documento.
     * @param original
     *            Retornar original.
     * 
     * @return Resultado de operacion de descarga de documento.
     * @throws RPRegisterException 
     */
    public static DocumentDownloadResponse masiveDocumentDownload(List<String> listDocumentId, boolean original) throws RPRegisterException {
	DocumentDownloadRequest rq = new DocumentDownloadRequest();
	rq.setApplicationAlias(KeysRP.PFE_APP_ALIAS);
	rq.setDocumentId(listDocumentId);
	rq.setDocumentState(original ? DocumentState.ORIGINAL : DocumentState.SIGNED);
	return documentDownload(rq);
    }

    /**
     * Solicitud de primer paso de firma con PFE.
     * 
     * @param rq
     *            RemoteSignBeginRequest.
     * 
     * @return Resultado de operacion de primer paso de firma de documento.
     * @throws RPRegisterException 
     */
    public static RemoteSignBeginResponse remoteSignBegin(RemoteSignBeginRequest rq) throws RPRegisterException {
	RemoteSignBeginResponse response = null;
	try {
	    response = RemoteSignServiceImpl.remoteSignBegin(rq);
	}
	catch (Throwable t) {
	    ApplicationLogger.error(t.getMessage(), t);
	    throw new RPRegisterException(null, t);
	}
	return response;
    }

    /**
     * Solicitud de primer paso de firma con PFE.
     * 
     * @param docs
     *            Ids de documentos.
     * @param certs
     *            Certificados firmantes.
     * @param requestPolicy
     *            Politica de firma.
     * @param requestAttachmentType 
     * @param requestSignatureType 
     * @param requestPolicyId 
     * 
     * @return Resultado de operacion de primer paso de firma de documento.
     * @throws RPRegisterException 
     */
    public static RemoteSignBeginResponse remoteSignBegin(String[] docs, String[] certs,
	    String requestAttachmentType, String requestSignatureType, String requestPolicyId) throws RPRegisterException {
	RemoteSignBeginRequest rq = new RemoteSignBeginRequest();
	rq.setApplicationAlias(KeysRP.PFE_APP_ALIAS);
	if (certs != null && certs.length > 0) {
	    for (String cert : certs) {
		rq.getChain().add(cert);
	    }
	}
	if (docs != null && docs.length > 0) {
	    for (String doc : docs) {
		rq.getDocumentIdArray().add(doc);
	    }
	}
	rq.setMultisignatureType(MultisignatureType.SIMPLE.name());
	rq.setAttachmentType(requestAttachmentType);
	rq.setHashAlgorithm("SHA-1");
	rq.setSignatureType(requestSignatureType);
	rq.setPolicyId(requestPolicyId);
	rq.setClientSecurityEnabled(true);
	
	return remoteSignBegin(rq);
    }

    /**
     * Solicitud de segundo paso de firma con PFE.
     * 
     * @param rq
     *            RemoteSignEndRequest.
     * 
     * @return Resultado de operacion de segundo paso de firma de documento.
     * @throws RPRegisterException 
     */
    public static RemoteSignEndResponse remoteSignEnd(RemoteSignEndRequest rq) throws RPRegisterException {
	RemoteSignEndResponse response = null;
	try {
	    response = RemoteSignServiceImpl.remoteSignEnd(rq);
	}
	catch (Throwable t) {
	    ApplicationLogger.error(t.getMessage(), t);
	    throw new RPRegisterException(null, t);
	}
	return response;

    }

    /**
     * Solicitud de segundo paso de firma con PFE.
     * 
     * @param taskId
     *            Id de tarea de firma.
     * @param taskData
     *            Datos de la tarea de firma.
     * 
     * @return Resultado de operacion de segundo paso de firma de documento.
     * @throws RPRegisterException 
     */
    public static RemoteSignEndResponse remoteSignEnd(String taskId, byte[] taskData) throws RPRegisterException {
	RemoteSignEndRequest rq = new RemoteSignEndRequest();
	rq.setApplicationAlias(KeysRP.PFE_APP_ALIAS);
	rq.setTaskId(taskId);
	rq.setTaskData(taskData);
	rq.setValidateSignedData(true);
	return remoteSignEnd(rq);
    }

}