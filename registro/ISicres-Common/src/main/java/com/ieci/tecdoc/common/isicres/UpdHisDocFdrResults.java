package com.ieci.tecdoc.common.isicres;

import java.io.Serializable;

import com.ieci.tecdoc.common.invesicres.ScrModifDoc;

/**
 * @author MABENITO
 */

public class UpdHisDocFdrResults implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*******************************************************************************************************************
     * Attributes
     ******************************************************************************************************************/	
	private ScrModifDoc scrModifDoc = null;
	private String tipoDoc = null;
	private String nombreDoc = null;
	private String nombreDocNuevo = null;
	private String accion = null;
	

    /*******************************************************************************************************************
     * Constructors
     ******************************************************************************************************************/

    /**
     *  
     */
	
	public UpdHisDocFdrResults() {
		super();
	}

	
    /*******************************************************************************************************************
     * Public methods
     ******************************************************************************************************************/
	/**
	 * @return the scrModifDoc
	 */
	public ScrModifDoc getScrModifDoc() {
		return scrModifDoc;
	}

	/**
	 * @param scrModifDoc the scrModifDoc to set
	 */
	public void setScrModifDoc(ScrModifDoc scrModifDoc) {
		this.scrModifDoc = scrModifDoc;
	}
    public String getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public String getNombreDoc() {
		return nombreDoc;
	}
	public void setNombreDoc(String nombreDoc) {
		this.nombreDoc = nombreDoc;
	}
	public String getNombreDocNuevo() {
		return nombreDocNuevo;
	}
	public void setNombreDocNuevo(String nombreDocNuevo) {
		this.nombreDocNuevo = nombreDocNuevo;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}

	/**
     * toString methode: creates a String representation of the object
     * @return the String representation

     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("UpdHisDocResults[");
        buffer.append("scrModifDoc = ").append(scrModifDoc);
        buffer.append(", tipoDoc = ").append(tipoDoc);
        buffer.append(", nombreDoc = ").append(nombreDoc);
        
        if(null == nombreDocNuevo){
        	buffer.append(", nombreDocNuevo = ").append("");
        } else {
        	buffer.append(", nombreDocNuevo = ").append(nombreDocNuevo);
        }
        
        buffer.append(", accion = ").append(accion);
        buffer.append("]");
        return buffer.toString();
    }


    
    /*******************************************************************************************************************
     * Protected methods
     ******************************************************************************************************************/

    /*******************************************************************************************************************
     * Private methods
     ******************************************************************************************************************/

    /*******************************************************************************************************************
     * Inner classes
     ******************************************************************************************************************/

    /*******************************************************************************************************************
     * Test brench
     ******************************************************************************************************************/

	
	
	
}
