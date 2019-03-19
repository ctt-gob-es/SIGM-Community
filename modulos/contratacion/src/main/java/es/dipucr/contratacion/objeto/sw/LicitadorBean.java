
package es.dipucr.contratacion.objeto.sw;

import java.util.Calendar;


public class LicitadorBean{
    private Campo NUTS;

    private boolean autorizaEnvioComunicacionesElect;
    private String calle;
    private String cp;
    private String email;
    private Calendar fechaAdjudicacion;
    private Calendar fechaFinFormalizacion;
    private String identificador;
    private String importeConImpuestos;
    private String importeSinImpuestos;
    private String justificacionDescripcion;
    private Campo justificacionProceso;
    private String motivacion;
    private String nombre;
    private String numeroVia;
    private Campo pais;
    private String poblacion;
    private String tipoIdentificador;

    public LicitadorBean() {
    }

    public LicitadorBean(
           Campo NUTS,
           boolean autorizaEnvioComunicacionesElect,
           String calle,
           String cp,
           String email,
           Calendar fechaAdjudicacion,
           Calendar fechaFinFormalizacion,
           String identificador,
           String importeConImpuestos,
           String importeSinImpuestos,
           String justificacionDescripcion,
           Campo justificacionProceso,
           String motivacion,
           String nombre,
           String numeroVia,
           Campo pais,
           String poblacion,
           String tipoIdentificador) {
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
    public String getCalle() {
        return calle;
    }


    /**
     * Sets the calle value for this LicitadorBean.
     * 
     * @param calle
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }


    /**
     * Gets the cp value for this LicitadorBean.
     * 
     * @return cp
     */
    public String getCp() {
        return cp;
    }


    /**
     * Sets the cp value for this LicitadorBean.
     * 
     * @param cp
     */
    public void setCp(String cp) {
        this.cp = cp;
    }


    /**
     * Gets the email value for this LicitadorBean.
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this LicitadorBean.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Gets the fechaAdjudicacion value for this LicitadorBean.
     * 
     * @return fechaAdjudicacion
     */
    public Calendar getFechaAdjudicacion() {
        return fechaAdjudicacion;
    }


    /**
     * Sets the fechaAdjudicacion value for this LicitadorBean.
     * 
     * @param fechaAdjudicacion
     */
    public void setFechaAdjudicacion(Calendar fechaAdjudicacion) {
        this.fechaAdjudicacion = fechaAdjudicacion;
    }


    /**
     * Gets the fechaFinFormalizacion value for this LicitadorBean.
     * 
     * @return fechaFinFormalizacion
     */
    public Calendar getFechaFinFormalizacion() {
        return fechaFinFormalizacion;
    }


    /**
     * Sets the fechaFinFormalizacion value for this LicitadorBean.
     * 
     * @param fechaFinFormalizacion
     */
    public void setFechaFinFormalizacion(Calendar fechaFinFormalizacion) {
        this.fechaFinFormalizacion = fechaFinFormalizacion;
    }


    /**
     * Gets the identificador value for this LicitadorBean.
     * 
     * @return identificador
     */
    public String getIdentificador() {
        return identificador;
    }


    /**
     * Sets the identificador value for this LicitadorBean.
     * 
     * @param identificador
     */
    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }


    /**
     * Gets the importeConImpuestos value for this LicitadorBean.
     * 
     * @return importeConImpuestos
     */
    public String getImporteConImpuestos() {
        return importeConImpuestos;
    }


    /**
     * Sets the importeConImpuestos value for this LicitadorBean.
     * 
     * @param importeConImpuestos
     */
    public void setImporteConImpuestos(String importeConImpuestos) {
        this.importeConImpuestos = importeConImpuestos;
    }


    /**
     * Gets the importeSinImpuestos value for this LicitadorBean.
     * 
     * @return importeSinImpuestos
     */
    public String getImporteSinImpuestos() {
        return importeSinImpuestos;
    }


    /**
     * Sets the importeSinImpuestos value for this LicitadorBean.
     * 
     * @param importeSinImpuestos
     */
    public void setImporteSinImpuestos(String importeSinImpuestos) {
        this.importeSinImpuestos = importeSinImpuestos;
    }


    /**
     * Gets the justificacionDescripcion value for this LicitadorBean.
     * 
     * @return justificacionDescripcion
     */
    public String getJustificacionDescripcion() {
        return justificacionDescripcion;
    }


    /**
     * Sets the justificacionDescripcion value for this LicitadorBean.
     * 
     * @param justificacionDescripcion
     */
    public void setJustificacionDescripcion(String justificacionDescripcion) {
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
    public String getMotivacion() {
        return motivacion;
    }


    /**
     * Sets the motivacion value for this LicitadorBean.
     * 
     * @param motivacion
     */
    public void setMotivacion(String motivacion) {
        this.motivacion = motivacion;
    }


    /**
     * Gets the nombre value for this LicitadorBean.
     * 
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this LicitadorBean.
     * 
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the numeroVia value for this LicitadorBean.
     * 
     * @return numeroVia
     */
    public String getNumeroVia() {
        return numeroVia;
    }


    /**
     * Sets the numeroVia value for this LicitadorBean.
     * 
     * @param numeroVia
     */
    public void setNumeroVia(String numeroVia) {
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
    public String getPoblacion() {
        return poblacion;
    }


    /**
     * Sets the poblacion value for this LicitadorBean.
     * 
     * @param poblacion
     */
    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }


    /**
     * Gets the tipoIdentificador value for this LicitadorBean.
     * 
     * @return tipoIdentificador
     */
    public String getTipoIdentificador() {
        return tipoIdentificador;
    }


    /**
     * Sets the tipoIdentificador value for this LicitadorBean.
     * 
     * @param tipoIdentificador
     */
    public void setTipoIdentificador(String tipoIdentificador) {
        this.tipoIdentificador = tipoIdentificador;
    }
}
