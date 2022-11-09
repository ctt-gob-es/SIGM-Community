package ieci.tdw.ispac.services.mgr;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.services.dto.InfoFichero;
import ieci.tdw.ispac.services.dto.InfoOcupacion;
import ieci.tdw.ispac.services.helpers.DocumentsHelper;
import ieci.tdw.ispac.services.vo.DocumentoVO;

import org.apache.log4j.Logger;

public class CustodiaManager extends ServiceManager {

	/** Logger de la clase. */
	protected static final Logger logger = 
		Logger.getLogger(CustodiaManager.class);

	/**
	 * Constructor.
	 */
	private CustodiaManager() {
		super();
	}
	
	/**
	 * Obtiene una instancia del manager.
	 * @return Instancia del manager.
	 */
	public static CustodiaManager getInstance() {
		return new CustodiaManager();
	}

    /**
     * Obtiene el contenido del documento.
     * @param guid GUID del documento
     * @return Contenido del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public byte [] getFichero(String guid) throws ISPACException {
	    return DocumentsHelper.getContenidoDocumento(getContext(), guid);
    }
    
    /**
     * [DipuCR-Agustin #1297]
     * Obtiene el contenido del documento.
     * @param guid GUID del documento
     * @return Contenido del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public byte [] getFicheroJustificanteFirma(String guid) throws ISPACException {
	    return DocumentsHelper.getContenidoDocumentoJustificanteFirma(getContext(), guid);
    }

	/**
	 * Obtiene la informaci�n de origen e integridad del documento.
	 * @param guid GUID del documento.
	 * @return Informaci�n del documento.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public InfoFichero getInfoFichero(String guid) 
			throws ISPACException {
    	InfoFichero info = null;
    	
        // Obtener la informaci�n del documento
		DocumentoVO doc = DocumentsHelper.getInfoDocumento(getContext(), guid);
	    if (doc != null) {
	        info = new InfoFichero();
	        info.setNombre(doc.getNombreCompleto());
			info.setFechaAlta(doc.getFechaAlta());
			info.setFirmas(DocumentsHelper.getFirmas(getContext(), doc));
	    }

		return info;
	}
	
    /**
     * [Dipucr-Agustin #781]
     * Obtiene el contenido del documento temporal.
     * @param guid GUID del documento
     * @return Contenido del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public byte [] getFicheroTemp(String guid) throws ISPACException {
	    return DocumentsHelper.getContenidoDocumentoTemp(getContext(), guid);
    }
    
    /**
     * [Dipucr-Agustin #781]
     * Obtiene el contenido del documento temporal.
     * @param guid GUID del documento
     * @param array de bytes a cargar
     * @return resultado de la operacion.
     * @throws ISPACException si ocurre alg�n error.
     */
    public boolean setFicheroTemp(String guid, byte[] data) throws ISPACException {
	    return DocumentsHelper.setContenidoDocumentoTemp(getContext(), guid, data);
    }

	/**
	 * Obtiene la informaci�n de ocupaci�n del repositorio.
	 * @return Informaci�n de ocupaci�n.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public InfoOcupacion getInfoOcupacion() throws ISPACException {
	    // Identificador del repositorio de custodia
        String repId = ISPACConfiguration.getInstance().get("RDE_ARCHIVE_ID");
        
        // Obtener la informaci�n de ocupaci�n
        return DocumentsHelper.getInfoOcupacion(getContext(), repId);
	}

	/**
	 * Elimina los documentos determinados por los GUIDs.
	 * @param guids Lista de GUIDs de los documentos.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void eliminaFicheros(String[] guids) 
			throws ISPACException {
        DocumentsHelper.removeFicheros(getContext(), guids);
	}

}
