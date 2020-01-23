
package es.dipucr.contratacion.objeto.sw;

import java.util.Calendar;

public class DuracionContratoBean{
    private String duracion;
    private Campo durationMeasure;
    private Calendar fechaFinal;
    private Calendar fechaInicio;

    public DuracionContratoBean() {
    }

    public DuracionContratoBean(
           String duracion,
           Campo durationMeasure,
           Calendar fechaFinal,
           Calendar fechaInicio) {
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
    public String getDuracion() {
        return duracion;
    }


    /**
     * Sets the duracion value for this DuracionContratoBean.
     * 
     * @param duracion
     */
    public void setDuracion(String duracion) {
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
    public Calendar getFechaFinal() {
        return fechaFinal;
    }


    /**
     * Sets the fechaFinal value for this DuracionContratoBean.
     * 
     * @param fechaFinal
     */
    public void setFechaFinal(Calendar fechaFinal) {
        this.fechaFinal = fechaFinal;
    }


    /**
     * Gets the fechaInicio value for this DuracionContratoBean.
     * 
     * @return fechaInicio
     */
    public Calendar getFechaInicio() {
        return fechaInicio;
    }


    /**
     * Sets the fechaInicio value for this DuracionContratoBean.
     * 
     * @param fechaInicio
     */
    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}
