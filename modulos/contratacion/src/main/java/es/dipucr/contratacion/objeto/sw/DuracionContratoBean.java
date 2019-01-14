
package es.dipucr.contratacion.objeto.sw;

public class DuracionContratoBean{
    private java.lang.String duracion;

    private Campo durationMeasure;

    private java.util.Calendar fechaFinal;

    private java.util.Calendar fechaInicio;

    public DuracionContratoBean() {
    }

    public DuracionContratoBean(
           java.lang.String duracion,
           Campo durationMeasure,
           java.util.Calendar fechaFinal,
           java.util.Calendar fechaInicio) {
           this.duracion = duracion;
           this.durationMeasure = durationMeasure;
           this.fechaFinal = fechaFinal;
           this.fechaInicio = fechaInicio;
    }


    /**
     * Gets the duracion value for this DuracionContratoBean.
     * 
     * @return duracion
     */
    public java.lang.String getDuracion() {
        return duracion;
    }


    /**
     * Sets the duracion value for this DuracionContratoBean.
     * 
     * @param duracion
     */
    public void setDuracion(java.lang.String duracion) {
        this.duracion = duracion;
    }


    /**
     * Gets the durationMeasure value for this DuracionContratoBean.
     * 
     * @return durationMeasure
     */
    public Campo getDurationMeasure() {
        return durationMeasure;
    }


    /**
     * Sets the durationMeasure value for this DuracionContratoBean.
     * 
     * @param durationMeasure
     */
    public void setDurationMeasure(Campo durationMeasure) {
        this.durationMeasure = durationMeasure;
    }


    /**
     * Gets the fechaFinal value for this DuracionContratoBean.
     * 
     * @return fechaFinal
     */
    public java.util.Calendar getFechaFinal() {
        return fechaFinal;
    }


    /**
     * Sets the fechaFinal value for this DuracionContratoBean.
     * 
     * @param fechaFinal
     */
    public void setFechaFinal(java.util.Calendar fechaFinal) {
        this.fechaFinal = fechaFinal;
    }


    /**
     * Gets the fechaInicio value for this DuracionContratoBean.
     * 
     * @return fechaInicio
     */
    public java.util.Calendar getFechaInicio() {
        return fechaInicio;
    }


    /**
     * Sets the fechaInicio value for this DuracionContratoBean.
     * 
     * @param fechaInicio
     */
    public void setFechaInicio(java.util.Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}
