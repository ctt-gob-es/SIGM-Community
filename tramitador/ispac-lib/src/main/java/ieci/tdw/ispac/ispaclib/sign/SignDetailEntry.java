package ieci.tdw.ispac.ispaclib.sign;

import ieci.tdw.ispac.api.ISignAPI;

import java.util.Date;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

/**
 * Detalle de una firma de un documento firmado.
 * 
 * @author antoniomaria_sanchez at ieci.es
 * @since 28/01/2009
 */

public class SignDetailEntry implements Comparable<SignDetailEntry> {

	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = Logger.getLogger(SignDetailEntry.class);
	
	String identifier;//[dipucr-Felipe #1246]
	
	String author;

	boolean isFirmado=false;
	
	boolean isRechazado=false;//[dipucr-Felipe #1246]

	String signDate;
	
	String integrity=ISignAPI.INTEGRIDAD_STRANGER;
	
	String cargo;//[dipucr-Felipe #1246]
	

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}


	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public boolean isFirmado() {
		return isFirmado;
	}

	public void setFirmado(boolean isFirmado) {
		this.isFirmado = isFirmado;
	}
	
	public boolean isRechazado() {
		return isRechazado;
	}

	public void setRechazado(boolean isRechazado) {
		this.isRechazado = isRechazado;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public String getIntegrity() {
		return integrity;
	}

	public void setIntegrity(String integrity) {
		this.integrity = integrity;
	}
	
	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String toString() {
		return "author: " + author + ", state: " + isFirmado
				+ ", signDate: " + signDate;
	}

	/**
	 * [dipucr-Felipe #1377]
	 */
	public int compareTo(SignDetailEntry other) {
		
		try{
			Date dateThis = FechasUtil.convertToDate(this.signDate);
			Date dateOther = FechasUtil.convertToDate(other.getSignDate());
			return dateThis.compareTo(dateOther);
		}
		catch(Exception ex){
			LOGGER.error("Error al comparar las fechas " + this.signDate + " y " + other.getSignDate(), ex);
			return 0;
		}
	}

}
