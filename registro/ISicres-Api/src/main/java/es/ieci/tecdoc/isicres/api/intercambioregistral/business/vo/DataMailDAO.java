package es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo;

import java.util.List;

public class DataMailDAO {
	
	/**
	 * Texto del email
	 */
	private List<String> textSender;
	
	/**
	 * Código Unidad Tramitadora
	 */
	private String codeTramUnit;
	
	/**
	 * Id Unidad Tramitadora Destino
	 */
	private Integer idDest;
	
	/**
	 * Tipo de registro
	 */
	private String typeRegister;

	/**
	 * @return the textSender
	 */
	public List<String> getTextSender() {
		return textSender;
	}

	/**
	 * @param textSender the textSender to set
	 */
	public void setTextSender(List<String> textSender) {
		this.textSender = textSender;
	}

	
	/**
	 * @return the codeTramUnit
	 */
	public String getCodeTramUnit() {
		return codeTramUnit;
	}

	/**
	 * @param codeTramUnit the codeTramUnit to set
	 */
	public void setCodeTramUnit(String codeTramUnit) {
		this.codeTramUnit = codeTramUnit;
	}

	/**
	 * @return the typeRegister
	 */
	public String getTypeRegister() {
		return typeRegister;
	}

	/**
	 * @param typeRegister the typeRegister to set
	 */
	public void setTypeRegister(String typeRegister) {
		this.typeRegister = typeRegister;
	}

	/**
	 * @return the idDest
	 */
	public Integer getIdDest() {
		return idDest;
	}

	/**
	 * @param idDest the idDest to set
	 */
	public void setIdDest(Integer idDest) {
		this.idDest = idDest;
	}

}
