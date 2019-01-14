
package es.dipucr.contratacion.objeto.sw;

public class EventoAperturaBean{
    private java.lang.String calle;

    private java.lang.String cp;

    private java.lang.String descripcion;

    private java.util.Calendar fechaApertura;

    private java.lang.String idPais;

    private java.lang.String localidad;

    private java.lang.String lugar;

    private java.lang.String pais;

    private java.lang.String poblacion;

    private Campo tipoEvento;

    public EventoAperturaBean() {
    }

    public EventoAperturaBean(
           java.lang.String calle,
           java.lang.String cp,
           java.lang.String descripcion,
           java.util.Calendar fechaApertura,
           java.lang.String idPais,
           java.lang.String localidad,
           java.lang.String lugar,
           java.lang.String pais,
           java.lang.String poblacion,
           Campo tipoEvento) {
           this.calle = calle;
           this.cp = cp;
           this.descripcion = descripcion;
           this.fechaApertura = fechaApertura;
           this.idPais = idPais;
           this.localidad = localidad;
           this.lugar = lugar;
           this.pais = pais;
           this.poblacion = poblacion;
           this.tipoEvento = tipoEvento;
    }


    /**
     * Gets the calle value for this EventoAperturaBean.
     * 
     * @return calle
     */
    public java.lang.String getCalle() {
        return calle;
    }


    /**
     * Sets the calle value for this EventoAperturaBean.
     * 
     * @param calle
     */
    public void setCalle(java.lang.String calle) {
        this.calle = calle;
    }


    /**
     * Gets the cp value for this EventoAperturaBean.
     * 
     * @return cp
     */
    public java.lang.String getCp() {
        return cp;
    }


    /**
     * Sets the cp value for this EventoAperturaBean.
     * 
     * @param cp
     */
    public void setCp(java.lang.String cp) {
        this.cp = cp;
    }


    /**
     * Gets the descripcion value for this EventoAperturaBean.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this EventoAperturaBean.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the fechaApertura value for this EventoAperturaBean.
     * 
     * @return fechaApertura
     */
    public java.util.Calendar getFechaApertura() {
        return fechaApertura;
    }


    /**
     * Sets the fechaApertura value for this EventoAperturaBean.
     * 
     * @param fechaApertura
     */
    public void setFechaApertura(java.util.Calendar fechaApertura) {
        this.fechaApertura = fechaApertura;
    }


    /**
     * Gets the idPais value for this EventoAperturaBean.
     * 
     * @return idPais
     */
    public java.lang.String getIdPais() {
        return idPais;
    }


    /**
     * Sets the idPais value for this EventoAperturaBean.
     * 
     * @param idPais
     */
    public void setIdPais(java.lang.String idPais) {
        this.idPais = idPais;
    }


    /**
     * Gets the localidad value for this EventoAperturaBean.
     * 
     * @return localidad
     */
    public java.lang.String getLocalidad() {
        return localidad;
    }


    /**
     * Sets the localidad value for this EventoAperturaBean.
     * 
     * @param localidad
     */
    public void setLocalidad(java.lang.String localidad) {
        this.localidad = localidad;
    }


    /**
     * Gets the lugar value for this EventoAperturaBean.
     * 
     * @return lugar
     */
    public java.lang.String getLugar() {
        return lugar;
    }


    /**
     * Sets the lugar value for this EventoAperturaBean.
     * 
     * @param lugar
     */
    public void setLugar(java.lang.String lugar) {
        this.lugar = lugar;
    }


    /**
     * Gets the pais value for this EventoAperturaBean.
     * 
     * @return pais
     */
    public java.lang.String getPais() {
        return pais;
    }


    /**
     * Sets the pais value for this EventoAperturaBean.
     * 
     * @param pais
     */
    public void setPais(java.lang.String pais) {
        this.pais = pais;
    }


    /**
     * Gets the poblacion value for this EventoAperturaBean.
     * 
     * @return poblacion
     */
    public java.lang.String getPoblacion() {
        return poblacion;
    }


    /**
     * Sets the poblacion value for this EventoAperturaBean.
     * 
     * @param poblacion
     */
    public void setPoblacion(java.lang.String poblacion) {
        this.poblacion = poblacion;
    }


    /**
     * Gets the tipoEvento value for this EventoAperturaBean.
     * 
     * @return tipoEvento
     */
    public Campo getTipoEvento() {
        return tipoEvento;
    }


    /**
     * Sets the tipoEvento value for this EventoAperturaBean.
     * 
     * @param tipoEvento
     */
    public void setTipoEvento(Campo tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
}
