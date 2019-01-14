package es.dipucr.contratacion.objeto.sw;

public class Lotes{
    private java.lang.String detallePosibilidadAdjudicacion;

    private Lote[] lotes;

    private java.lang.Integer numeroMaximoAdjudicacion;

    private java.lang.Integer numeroMaximoPresentacion;

    private Campo seDebeOfertar;

    private boolean tieneLotes;

    public Lotes() {
    }

    public Lotes(
           java.lang.String detallePosibilidadAdjudicacion,
           Lote[] lotes,
           java.lang.Integer numeroMaximoAdjudicacion,
           java.lang.Integer numeroMaximoPresentacion,
           Campo seDebeOfertar,
           boolean tieneLotes) {
           this.detallePosibilidadAdjudicacion = detallePosibilidadAdjudicacion;
           this.lotes = lotes;
           this.numeroMaximoAdjudicacion = numeroMaximoAdjudicacion;
           this.numeroMaximoPresentacion = numeroMaximoPresentacion;
           this.seDebeOfertar = seDebeOfertar;
           this.tieneLotes = tieneLotes;
    }


    /**
     * Gets the detallePosibilidadAdjudicacion value for this Lotes.
     * 
     * @return detallePosibilidadAdjudicacion
     */
    public java.lang.String getDetallePosibilidadAdjudicacion() {
        return detallePosibilidadAdjudicacion;
    }


    /**
     * Sets the detallePosibilidadAdjudicacion value for this Lotes.
     * 
     * @param detallePosibilidadAdjudicacion
     */
    public void setDetallePosibilidadAdjudicacion(java.lang.String detallePosibilidadAdjudicacion) {
        this.detallePosibilidadAdjudicacion = detallePosibilidadAdjudicacion;
    }


    /**
     * Gets the lotes value for this Lotes.
     * 
     * @return lotes
     */
    public Lote[] getLotes() {
        return lotes;
    }


    /**
     * Sets the lotes value for this Lotes.
     * 
     * @param lotes
     */
    public void setLotes(Lote[] lotes) {
        this.lotes = lotes;
    }


    /**
     * Gets the numeroMaximoAdjudicacion value for this Lotes.
     * 
     * @return numeroMaximoAdjudicacion
     */
    public java.lang.Integer getNumeroMaximoAdjudicacion() {
        return numeroMaximoAdjudicacion;
    }


    /**
     * Sets the numeroMaximoAdjudicacion value for this Lotes.
     * 
     * @param numeroMaximoAdjudicacion
     */
    public void setNumeroMaximoAdjudicacion(java.lang.Integer numeroMaximoAdjudicacion) {
        this.numeroMaximoAdjudicacion = numeroMaximoAdjudicacion;
    }


    /**
     * Gets the numeroMaximoPresentacion value for this Lotes.
     * 
     * @return numeroMaximoPresentacion
     */
    public java.lang.Integer getNumeroMaximoPresentacion() {
        return numeroMaximoPresentacion;
    }


    /**
     * Sets the numeroMaximoPresentacion value for this Lotes.
     * 
     * @param numeroMaximoPresentacion
     */
    public void setNumeroMaximoPresentacion(java.lang.Integer numeroMaximoPresentacion) {
        this.numeroMaximoPresentacion = numeroMaximoPresentacion;
    }


    /**
     * Gets the seDebeOfertar value for this Lotes.
     * 
     * @return seDebeOfertar
     */
    public Campo getSeDebeOfertar() {
        return seDebeOfertar;
    }


    /**
     * Sets the seDebeOfertar value for this Lotes.
     * 
     * @param seDebeOfertar
     */
    public void setSeDebeOfertar(Campo seDebeOfertar) {
        this.seDebeOfertar = seDebeOfertar;
    }


    /**
     * Gets the tieneLotes value for this Lotes.
     * 
     * @return tieneLotes
     */
    public boolean isTieneLotes() {
        return tieneLotes;
    }


    /**
     * Sets the tieneLotes value for this Lotes.
     * 
     * @param tieneLotes
     */
    public void setTieneLotes(boolean tieneLotes) {
        this.tieneLotes = tieneLotes;
    }
}
