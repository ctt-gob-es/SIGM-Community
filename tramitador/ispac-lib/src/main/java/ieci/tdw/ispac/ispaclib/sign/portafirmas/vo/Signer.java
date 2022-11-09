package ieci.tdw.ispac.ispaclib.sign.portafirmas.vo;

/**
 * ClasE que almacena la información de los firmantes
 * @author Iecisa
 * @version $Revision$
 *
 */
public class Signer {

	public static final String TYPE_SIGNER_USER		 		= "U";
	public static final String TYPE_SIGNER_CARGO			= "C";

	private String identifier;

	private String name;
	//[Ticket1273#Teresa]Información de ALSIGM
	private String nameALSIGM;
	private String idALSIGM;


	/*Indica el tipo de firmante asociado (usuario o cargo).
	 * Solo admite dos valores 'U' ó 'C'
	 */

	private String tipoFirmante;

	public Signer() {
		super();
		this.tipoFirmante=TYPE_SIGNER_USER;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTipoFirmante() {
		return tipoFirmante;
	}

	public void setTipoFirmante(String tipoFirmante) {
		this.tipoFirmante = tipoFirmante;
	}

	public String getNameALSIGM() {
		return nameALSIGM;
	}

	public void setNameALSIGM(String nameALSIGM) {
		this.nameALSIGM = nameALSIGM;
	}

	public String getIdALSIGM() {
		return idALSIGM;
	}

	public void setIdALSIGM(String idALSIGM) {
		this.idALSIGM = idALSIGM;
	}
}
