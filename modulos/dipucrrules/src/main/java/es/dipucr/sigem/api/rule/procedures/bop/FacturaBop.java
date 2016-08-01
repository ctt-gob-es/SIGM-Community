package es.dipucr.sigem.api.rule.procedures.bop;

/**
 * [eCenpri-Felipe #153]
 * @author Felipe
 * @since 26.02.13
 */
public class FacturaBop {

	private boolean bTipoAlbaran;
	private String anio;
	private String numero;
	
	/**
	 * Constructor vacío	
	 */
	public FacturaBop() {
		super();
	}

	/**
	 * Constructor con los campos
	 * @param bTipo
	 * @param anio
	 * @param numero
	 */
	public FacturaBop(boolean bTipo, String anio, String numero) {
		super();
		this.bTipoAlbaran = bTipo;
		this.anio = anio;
		this.numero = numero;
	}

	/**
	 * Getters y setters
	 */
	public boolean isbTipoAlbaran() {
		return bTipoAlbaran;
	}

	public void setbTipoAlbaran(boolean bTipoAlbaran) {
		this.bTipoAlbaran = bTipoAlbaran;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
}
