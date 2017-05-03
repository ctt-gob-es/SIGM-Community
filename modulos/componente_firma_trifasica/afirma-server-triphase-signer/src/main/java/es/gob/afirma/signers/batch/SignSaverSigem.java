/**
 * 
 */
package es.gob.afirma.signers.batch;

import ieci.tecdoc.sgm.tram.ws.server.TramitacionWebServiceProxy;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author agustin
 *
 */
public class SignSaverSigem implements SignSaver {

	private static final String PROP_WEBSERVICE = "SigemTramitacionWebService"; //$NON-NLS-1$

	private static final Logger LOGGER = Logger.getLogger("SignSaverSigem"); //$NON-NLS-1$

	private String webservice;

	/** Constyruye un objeto de guardado de firmas mediante un servicio HTTP POST.
	 * El servicio recibir&aacute; los datos en Base64 dentro del par&aacute;metro indicado.
	 * @param targetFileName Nombre del fichero, incluyendo ruta, donde guardar la firma. */
	public SignSaverSigem(final String targetSigemTramitacionWebService) {
		if (targetSigemTramitacionWebService == null) {
			throw new IllegalArgumentException(
				"La ruta del enpoint del servicio web documental de Sigem no puede ser nula" //$NON-NLS-1$
			);
		}
		this.webservice = targetSigemTramitacionWebService;
	}

	/** Constructor vac&iacute;o. */
	public SignSaverSigem() {
		// Vacio
	}

	@Override
	public Properties getConfig() {
		final Properties p = new Properties();
		p.put(PROP_WEBSERVICE, this.webservice);
		return p;
	}

	@Override
	public void saveSign(final SingleSign sign, final byte[] dataToSave) throws IOException {

		//Parametros de identificacion del documento
		String id_secuencia_lote,codent,guid="";
		id_secuencia_lote=sign.getId();
		codent = sign.getExtraParams().getProperty("codent");
		guid = sign.getExtraParams().getProperty("guid");
		
		
		sign.getProcessResult().toString();
		LOGGER.info("SignSaverSigem-->saveSign: VAMOS A GUARDAR EL DOCUMENTO");
		LOGGER.info("Id de la secuencia: " + id_secuencia_lote);
		LOGGER.info("Codigo de entidad: " + codent);
		LOGGER.info("Identificador guid path: " +guid);		
		
		//Asignar endpoint del web service de documentos
		TramitacionWebServiceProxy repositorio = new TramitacionWebServiceProxy();
		repositorio.setEndpoint(this.webservice);		
		
		if(repositorio.setFicheroTemp(codent, guid, dataToSave))
		{			
			LOGGER.info("SignSaverSigem-->saveSign: FIRMADO, GUARDADO EN SIGEM");			
		}
		else
		{
			LOGGER.info("SignSaverSigem-->saveSign: ERROR AL GUARDAR LA FIRMA EN SIGEM");			
		}		
	}

	@Override
	public void init(final Properties config) {
		if (config == null) {
			throw new IllegalArgumentException(
				"La configuracion no puede ser nula" //$NON-NLS-1$
			);
		}
		final String file = config.getProperty(PROP_WEBSERVICE);
		if (file == null) {
			throw new IllegalArgumentException(
				"Es obligarorio que la configuracion incluya un valor para la propiedad " + PROP_WEBSERVICE //$NON-NLS-1$
			);
		}
		this.webservice = file;
	}

	@Override
	public void rollback(final SingleSign sign) {		
	}

}
