
package es.dipucr.contratacion.objeto.sw;

public class LicitadorBean{
    private Campo NUTS;

    private boolean autorizaEnvioComunicacionesElect;

    private java.lang.String calle;

    private java.lang.String cp;

    private java.lang.String email;

    private java.util.Calendar fechaAdjudicacion;

    private java.util.Calendar fechaFinFormalizacion;

    private java.lang.String identificador;

    private java.lang.String importeConImpuestos;

    private java.lang.String importeSinImpuestos;

    private java.lang.String justificacionDescripcion;

    private Campo justificacionProceso;

    private java.lang.String motivacion;

    private java.lang.String nombre;

    private java.lang.String numeroVia;

    private Campo pais;

    private java.lang.String poblacion;

    private java.lang.String tipoIdentificador;

    public LicitadorBean() {
    }

    public LicitadorBean(
           Campo NUTS,
           boolean autorizaEnvioComunicacionesElect,
           java.lang.String calle,
           java.lang.String cp,
           java.lang.String email,
           java.util.Calendar fechaAdjudicacion,
           java.util.Calendar fechaFinFormalizacion,
           java.lang.String identificador,
           java.lang.String importeConImpuestos,
           java.lang.String importeSinImpuestos,
           java.lang.String justificacionDescripcion,
           Campo justificacionProceso,
           java.lang.String motivacion,
           java.lang.String nombre,
           java.lang.String numeroVia,
           Campo pais,
           java.lang.String poblacion,
           java.lang.String tipoIdentificador) {
           this.NUTS = NUTS;
           this.autorizaEnvioComunicacionesElect = autorizaEnvioComunicacionesElect;
           this.calle = calle;
           this.cp = cp;
           this.email = email;
           this.fechaAdjudicacion = fechaAdjudicacion;
           this.fechaFinFormalizacion = fechaFinFormalizacion;
           this.identificador = identificador;
           this.importeConImpuestos = importeConImpuestos;
           this.importeSinImpuestos = importeSinImpuestos;
           this.justificacionDescripcion = justificacionDescripcion;
           this.justificacionProceso = justificacionProceso;
           this.motivacion = motivacion;
           this.nombre = nombre;
           this.numeroVia = numeroVia;
           this.pais = pais;
           this.poblacion = poblacion;
           this.tipoIdentificador = tipoIdentificador;
    }


    /**
     * Gets the NUTS value for this LicitadorBean.
     * 
     * @return NUTS
     */
    public Campo getNUTS() {
        return NUTS;
    }


    /**
     * Sets the NUTS value for this LicitadorBean.
     * 
     * @param NUTS
     */
    public void setNUTS(Campo NUTS) {
        this.NUTS = NUTS;
    }


    /**
     * Gets the autorizaEnvioComunicacionesElect value for this LicitadorBean.
     * 
     * @return autorizaEnvioComunicacionesElect
     */
    public boolean isAutorizaEnvioComunicacionesElect() {
        return autorizaEnvioComunicacionesElect;
    }


    /**
     * Sets the autorizaEnvioComunicacionesElect value for this LicitadorBean.
     * 
     * @param autorizaEnvioComunicacionesElect
     */
    public void setAutorizaEnvioComunicacionesElect(boolean autorizaEnvioComunicacionesElect) {
        this.autorizaEnvioComunicacionesElect = autorizaEnvioComunicacionesElect;
    }


    /**
     * Gets the calle value for this LicitadorBean.
     * 
     * @return calle
     */
    public java.lang.String getCalle() {
        return calle;
    }


    /**
     * Sets the calle value for this LicitadorBean.
     * 
     * @param calle
     */
    public void setCalle(java.lang.String calle) {
        this.calle = calle;
    }


    /**
     * Gets the cp value for this LicitadorBean.
     * 
     * @return cp
     */
    public java.lang.String getCp() {
        return cp;
    }


    /**
     * Sets the cp value for this LicitadorBean.
     * 
     * @param cp
     */
    public void setCp(java.lang.String cp) {
        this.cp = cp;
    }


    /**
     * Gets the email value for this LicitadorBean.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this LicitadorBean.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the fechaAdjudicacion value for this LicitadorBean.
     * 
     * @return fechaAdjudicacion
     */
    public java.util.Calendar getFechaAdjudicacion() {
        return fechaAdjudicacion;
    }


    /**
     * Sets the fechaAdjudicacion value for this LicitadorBean.
     * 
     * @param fechaAdjudicacion
     */
    public void setFechaAdjudicacion(java.util.Calendar fechaAdjudicacion) {
        this.fechaAdjudicacion = fechaAdjudicacion;
    }


    /**
     * Gets the fechaFinFormalizacion value for this LicitadorBean.
     * 
     * @return fechaFinFormalizacion
     */
    public java.util.Calendar getFechaFinFormalizacion() {
        return fechaFinFormalizacion;
    }


    /**
     * Sets the fechaFinFormalizacion value for this LicitadorBean.
     * 
     * @param fechaFinFormalizacion
     */
    public void setFechaFinFormalizacion(java.util.Calendar fechaFinFormalizacion) {
        this.fechaFinFormalizacion = fechaFinFormalizacion;
    }


    /**
     * Gets the identificador value for this LicitadorBean.
     * 
     * @return identificador
     */
    public java.lang.String getIdentificador() {
        return identificador;
    }


    /**
     * Sets the identificador value for this LicitadorBean.
     * 
     * @param identificador
     */
    public void setIdentificador(java.lang.String identificador) {
        this.identificador = identificador;
    }


    /**
     * Gets the importeConImpuestos value for this LicitadorBean.
     * 
     * @return importeConImpuestos
     */
    public java.lang.String getImporteConImpuestos() {
        return importeConImpuestos;
    }


    /**
     * Sets the importeConImpuestos value for this LicitadorBean.
     * 
     * @param importeConImpuestos
     */
    public void setImporteConImpuestos(java.lang.String importeConImpuestos) {
        this.importeConImpuestos = importeConImpuestos;
    }


    /**
     * Gets the importeSinImpuestos value for this LicitadorBean.
     * 
     * @return importeSinImpuestos
     */
    public java.lang.String getImporteSinImpuestos() {
        return importeSinImpuestos;
    }


    /**
     * Sets the importeSinImpuestos value for this LicitadorBean.
     * 
     * @param importeSinImpuestos
     */
    public void setImporteSinImpuestos(java.lang.String importeSinImpuestos) {
        this.importeSinImpuestos = importeSinImpuestos;
    }


    /**
     * Gets the justificacionDescripcion value for this LicitadorBean.
     * 
     * @return justificacionDescripcion
     */
    public java.lang.String getJustificacionDescripcion() {
        return justificacionDescripcion;
    }


    /**
     * Sets the justificacionDescripcion value for this LicitadorBean.
     * 
     * @param justificacionDescripcion
     */
    public void setJustificacionDescripcion(java.lang.String justificacionDescripcion) {
        this.justificacionDescripcion = justificacionDescripcion;
    }


    /**
     * Gets the justificacionProceso value for this LicitadorBean.
     * 
     * @return justificacionProceso
     */
    public Campo getJustificacionProceso() {
        return justificacionProceso;
    }


    /**
     * Sets the justificacionProceso value for this LicitadorBean.
     * 
     * @param justificacionProceso
     */
    public void setJustificacionProceso(Campo justificacionProceso) {
        this.justificacionProceso = justificacionProceso;
    }


    /**
     * Gets the motivacion value for this LicitadorBean.
     * 
     * @return motivacion
     */
    public java.lang.String getMotivacion() {
        return motivacion;
    }


    /**
     * Sets the motivacion value for this LicitadorBean.
     * 
     * @param motivacion
     */
    public void setMotivacion(java.lang.String motivacion) {
        this.motivacion = motivacion;
    }


    /**
     * Gets the nombre value for this LicitadorBean.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this LicitadorBean.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the numeroVia value for this LicitadorBean.
     * 
     * @return numeroVia
     */
    public java.lang.String getNumeroVia() {
        return numeroVia;
    }


    /**
     * Sets the numeroVia value for this LicitadorBean.
     * 
     * @param numeroVia
     */
    public void setNumeroVia(java.lang.String numeroVia) {
        this.numeroVia = numeroVia;
    }


    /**
     * Gets the pais value for this LicitadorBean.
     * 
     * @return pais
     */
    public Campo getPais() {
        return pais;
    }


    /**
     * Sets the pais value for this LicitadorBean.
     * 
     * @param pais
     */
    public void setPais(Campo pais) {
        this.pais = pais;
    }


    /**
     * Gets the poblacion value for this LicitadorBean.
     * 
     * @return poblacion
     */
    public java.lang.String getPoblacion() {
        return poblacion;
    }


    /**
     * Sets the poblacion value for this LicitadorBean.
     * 
     * @param poblacion
     */
    public void setPoblacion(java.lang.String poblacion) {
        this.poblacion = poblacion;
    }


    /**
     * Gets the tipoIdentificador value for this LicitadorBean.
     * 
     * @return tipoIdentificador
     */
    public java.lang.String getTipoIdentificador() {
        return tipoIdentificador;
    }


    /**
     * Sets the tipoIdentificador value for this LicitadorBean.
     * 
     * @param tipoIdentificador
     */
    public void setTipoIdentificador(java.lang.String tipoIdentificador) {
        this.tipoIdentificador = tipoIdentificador;
    }
}
