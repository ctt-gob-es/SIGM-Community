
package es.dipucr.contratacion.objeto.sw;

public class PublicacionesOficialesBean{
    private boolean enviarBOE;

    private boolean enviarDOUE;

    private java.util.Calendar fechaEnvioBOE;

    private java.util.Calendar fechaEnvioDOUE;

    private java.util.Calendar fechaPubDOUE;

    private java.util.Calendar fechaPubOtrosDiarios;

    private java.lang.String nombreOtrosDiarios;

    private java.lang.String publishURLOtrosDiarios;

    private boolean yaEnviadoBOE;

    private boolean yaEnviadoDOUE;

    public PublicacionesOficialesBean() {
    }

    public PublicacionesOficialesBean(
           boolean enviarBOE,
           boolean enviarDOUE,
           java.util.Calendar fechaEnvioBOE,
           java.util.Calendar fechaEnvioDOUE,
           java.util.Calendar fechaPubDOUE,
           java.util.Calendar fechaPubOtrosDiarios,
           java.lang.String nombreOtrosDiarios,
           java.lang.String publishURLOtrosDiarios,
           boolean yaEnviadoBOE,
           boolean yaEnviadoDOUE) {
           this.enviarBOE = enviarBOE;
           this.enviarDOUE = enviarDOUE;
           this.fechaEnvioBOE = fechaEnvioBOE;
           this.fechaEnvioDOUE = fechaEnvioDOUE;
           this.fechaPubDOUE = fechaPubDOUE;
           this.fechaPubOtrosDiarios = fechaPubOtrosDiarios;
           this.nombreOtrosDiarios = nombreOtrosDiarios;
           this.publishURLOtrosDiarios = publishURLOtrosDiarios;
           this.yaEnviadoBOE = yaEnviadoBOE;
           this.yaEnviadoDOUE = yaEnviadoDOUE;
    }


    /**
     * Gets the enviarBOE value for this PublicacionesOficialesBean.
     * 
     * @return enviarBOE
     */
    public boolean isEnviarBOE() {
        return enviarBOE;
    }


    /**
     * Sets the enviarBOE value for this PublicacionesOficialesBean.
     * 
     * @param enviarBOE
     */
    public void setEnviarBOE(boolean enviarBOE) {
        this.enviarBOE = enviarBOE;
    }


    /**
     * Gets the enviarDOUE value for this PublicacionesOficialesBean.
     * 
     * @return enviarDOUE
     */
    public boolean isEnviarDOUE() {
        return enviarDOUE;
    }


    /**
     * Sets the enviarDOUE value for this PublicacionesOficialesBean.
     * 
     * @param enviarDOUE
     */
    public void setEnviarDOUE(boolean enviarDOUE) {
        this.enviarDOUE = enviarDOUE;
    }


    /**
     * Gets the fechaEnvioBOE value for this PublicacionesOficialesBean.
     * 
     * @return fechaEnvioBOE
     */
    public java.util.Calendar getFechaEnvioBOE() {
        return fechaEnvioBOE;
    }


    /**
     * Sets the fechaEnvioBOE value for this PublicacionesOficialesBean.
     * 
     * @param fechaEnvioBOE
     */
    public void setFechaEnvioBOE(java.util.Calendar fechaEnvioBOE) {
        this.fechaEnvioBOE = fechaEnvioBOE;
    }


    /**
     * Gets the fechaEnvioDOUE value for this PublicacionesOficialesBean.
     * 
     * @return fechaEnvioDOUE
     */
    public java.util.Calendar getFechaEnvioDOUE() {
        return fechaEnvioDOUE;
    }


    /**
     * Sets the fechaEnvioDOUE value for this PublicacionesOficialesBean.
     * 
     * @param fechaEnvioDOUE
     */
    public void setFechaEnvioDOUE(java.util.Calendar fechaEnvioDOUE) {
        this.fechaEnvioDOUE = fechaEnvioDOUE;
    }


    /**
     * Gets the fechaPubDOUE value for this PublicacionesOficialesBean.
     * 
     * @return fechaPubDOUE
     */
    public java.util.Calendar getFechaPubDOUE() {
        return fechaPubDOUE;
    }


    /**
     * Sets the fechaPubDOUE value for this PublicacionesOficialesBean.
     * 
     * @param fechaPubDOUE
     */
    public void setFechaPubDOUE(java.util.Calendar fechaPubDOUE) {
        this.fechaPubDOUE = fechaPubDOUE;
    }


    /**
     * Gets the fechaPubOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @return fechaPubOtrosDiarios
     */
    public java.util.Calendar getFechaPubOtrosDiarios() {
        return fechaPubOtrosDiarios;
    }


    /**
     * Sets the fechaPubOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @param fechaPubOtrosDiarios
     */
    public void setFechaPubOtrosDiarios(java.util.Calendar fechaPubOtrosDiarios) {
        this.fechaPubOtrosDiarios = fechaPubOtrosDiarios;
    }


    /**
     * Gets the nombreOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @return nombreOtrosDiarios
     */
    public java.lang.String getNombreOtrosDiarios() {
        return nombreOtrosDiarios;
    }


    /**
     * Sets the nombreOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @param nombreOtrosDiarios
     */
    public void setNombreOtrosDiarios(java.lang.String nombreOtrosDiarios) {
        this.nombreOtrosDiarios = nombreOtrosDiarios;
    }


    /**
     * Gets the publishURLOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @return publishURLOtrosDiarios
     */
    public java.lang.String getPublishURLOtrosDiarios() {
        return publishURLOtrosDiarios;
    }


    /**
     * Sets the publishURLOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @param publishURLOtrosDiarios
     */
    public void setPublishURLOtrosDiarios(java.lang.String publishURLOtrosDiarios) {
        this.publishURLOtrosDiarios = publishURLOtrosDiarios;
    }


    /**
     * Gets the yaEnviadoBOE value for this PublicacionesOficialesBean.
     * 
     * @return yaEnviadoBOE
     */
    public boolean isYaEnviadoBOE() {
        return yaEnviadoBOE;
    }


    /**
     * Sets the yaEnviadoBOE value for this PublicacionesOficialesBean.
     * 
     * @param yaEnviadoBOE
     */
    public void setYaEnviadoBOE(boolean yaEnviadoBOE) {
        this.yaEnviadoBOE = yaEnviadoBOE;
    }


    /**
     * Gets the yaEnviadoDOUE value for this PublicacionesOficialesBean.
     * 
     * @return yaEnviadoDOUE
     */
    public boolean isYaEnviadoDOUE() {
        return yaEnviadoDOUE;
    }


    /**
     * Sets the yaEnviadoDOUE value for this PublicacionesOficialesBean.
     * 
     * @param yaEnviadoDOUE
     */
    public void setYaEnviadoDOUE(boolean yaEnviadoDOUE) {
        this.yaEnviadoDOUE = yaEnviadoDOUE;
    }
}
