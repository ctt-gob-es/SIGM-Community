package es.dipucr.tecdoc.sgm.ct.struts.form;

import org.apache.struts.action.ActionForm;

/**
 * Clase ActionForm representativa del formulario de BusquedaExpedientes.jsp 
 */
public class BusquedaExpedientesForm extends ActionForm {
	
	private String NIF;
	private String fechaDesde;
	private String fechaHasta;
	 
    /**
     * Devuelve NIF del interesado
     * 
     * @return NIF
     */
    public String getNIF() {
            return NIF;
    }
    
    /**
     * Establece NIF del interesado
     * 
     * @param NIF
     */
    
    public void setNIF (String NIF) {
        this.NIF = NIF;
    }
    
    /**
     * Devuelve fechaDesde para la busqueda
     * 
     * @return fechaDesde
     */
    
    public String getFechaDesde() {
        return fechaDesde;
    }
    
    /**
     * Establece fechaDesde del formulario
     * 
     * @param fechaDesde
     */
    public void setFechaDesde (String fechaDesde) {
            this.fechaDesde = fechaDesde;
    }
    
    /**
     * Devuelve fechaHasta para la busqueda
     * 
     * @return fechaHasta
     */
    public String getFechaHasta() {
        return fechaHasta;
    }
    
    /**
     * Establece fechaHasta del formulario
     * 
     * @param fechaHasta
     */
    public void setFechaHasta (String fechaHasta) {
            this.fechaHasta = fechaHasta;
    }
}
