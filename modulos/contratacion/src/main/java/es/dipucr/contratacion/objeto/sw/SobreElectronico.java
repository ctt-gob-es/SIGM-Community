package es.dipucr.contratacion.objeto.sw;

public class SobreElectronico{
    private Campo codOferta;

    private java.lang.String[] descripcion;

    private Documento[] doc;

    private boolean encriptadoSobre;

    private EventoAperturaBean eventoApertura;

    private boolean firmadoSobreRepresentado;

    private java.lang.String[] idLote;

    private java.lang.String idSobre;

    private boolean identificarLicitador;

    private java.lang.String presentacionSobre;

    public SobreElectronico() {
    }

    public SobreElectronico(
           Campo codOferta,
           java.lang.String[] descripcion,
           Documento[] doc,
           boolean encriptadoSobre,
           EventoAperturaBean eventoApertura,
           boolean firmadoSobreRepresentado,
           java.lang.String[] idLote,
           java.lang.String idSobre,
           boolean identificarLicitador,
           java.lang.String presentacionSobre) {
           this.codOferta = codOferta;
           this.descripcion = descripcion;
           this.doc = doc;
           this.encriptadoSobre = encriptadoSobre;
           this.eventoApertura = eventoApertura;
           this.firmadoSobreRepresentado = firmadoSobreRepresentado;
           this.idLote = idLote;
           this.idSobre = idSobre;
           this.identificarLicitador = identificarLicitador;
           this.presentacionSobre = presentacionSobre;
    }


    /**
     * Gets the codOferta value for this SobreElectronico.
     * 
     * @return codOferta
     */
    public Campo getCodOferta() {
        return codOferta;
    }


    /**
     * Sets the codOferta value for this SobreElectronico.
     * 
     * @param codOferta
     */
    public void setCodOferta(Campo codOferta) {
        this.codOferta = codOferta;
    }


    /**
     * Gets the descripcion value for this SobreElectronico.
     * 
     * @return descripcion
     */
    public java.lang.String[] getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this SobreElectronico.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String[] descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the doc value for this SobreElectronico.
     * 
     * @return doc
     */
    public Documento[] getDoc() {
        return doc;
    }


    /**
     * Sets the doc value for this SobreElectronico.
     * 
     * @param doc
     */
    public void setDoc(Documento[] doc) {
        this.doc = doc;
    }


    /**
     * Gets the encriptadoSobre value for this SobreElectronico.
     * 
     * @return encriptadoSobre
     */
    public boolean isEncriptadoSobre() {
        return encriptadoSobre;
    }


    /**
     * Sets the encriptadoSobre value for this SobreElectronico.
     * 
     * @param encriptadoSobre
     */
    public void setEncriptadoSobre(boolean encriptadoSobre) {
        this.encriptadoSobre = encriptadoSobre;
    }


    /**
     * Gets the eventoApertura value for this SobreElectronico.
     * 
     * @return eventoApertura
     */
    public EventoAperturaBean getEventoApertura() {
        return eventoApertura;
    }


    /**
     * Sets the eventoApertura value for this SobreElectronico.
     * 
     * @param eventoApertura
     */
    public void setEventoApertura(EventoAperturaBean eventoApertura) {
        this.eventoApertura = eventoApertura;
    }


    /**
     * Gets the firmadoSobreRepresentado value for this SobreElectronico.
     * 
     * @return firmadoSobreRepresentado
     */
    public boolean isFirmadoSobreRepresentado() {
        return firmadoSobreRepresentado;
    }


    /**
     * Sets the firmadoSobreRepresentado value for this SobreElectronico.
     * 
     * @param firmadoSobreRepresentado
     */
    public void setFirmadoSobreRepresentado(boolean firmadoSobreRepresentado) {
        this.firmadoSobreRepresentado = firmadoSobreRepresentado;
    }


    /**
     * Gets the idLote value for this SobreElectronico.
     * 
     * @return idLote
     */
    public java.lang.String[] getIdLote() {
        return idLote;
    }


    /**
     * Sets the idLote value for this SobreElectronico.
     * 
     * @param idLote
     */
    public void setIdLote(java.lang.String[] idLote) {
        this.idLote = idLote;
    }


    /**
     * Gets the idSobre value for this SobreElectronico.
     * 
     * @return idSobre
     */
    public java.lang.String getIdSobre() {
        return idSobre;
    }


    /**
     * Sets the idSobre value for this SobreElectronico.
     * 
     * @param idSobre
     */
    public void setIdSobre(java.lang.String idSobre) {
        this.idSobre = idSobre;
    }


    /**
     * Gets the identificarLicitador value for this SobreElectronico.
     * 
     * @return identificarLicitador
     */
    public boolean isIdentificarLicitador() {
        return identificarLicitador;
    }


    /**
     * Sets the identificarLicitador value for this SobreElectronico.
     * 
     * @param identificarLicitador
     */
    public void setIdentificarLicitador(boolean identificarLicitador) {
        this.identificarLicitador = identificarLicitador;
    }


    /**
     * Gets the presentacionSobre value for this SobreElectronico.
     * 
     * @return presentacionSobre
     */
    public java.lang.String getPresentacionSobre() {
        return presentacionSobre;
    }


    /**
     * Sets the presentacionSobre value for this SobreElectronico.
     * 
     * @param presentacionSobre
     */
    public void setPresentacionSobre(java.lang.String presentacionSobre) {
        this.presentacionSobre = presentacionSobre;
    }
}
