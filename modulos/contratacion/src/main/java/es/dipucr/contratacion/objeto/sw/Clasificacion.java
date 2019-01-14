/**
 * Clasificacion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.objeto.sw;

public class Clasificacion{
    private Campo[] clasificacion;

    private Campo clasificacionAcreditarRequisito;

    public Clasificacion() {
    }

    public Clasificacion(
           Campo[] clasificacion,
           Campo clasificacionAcreditarRequisito) {
           this.clasificacion = clasificacion;
           this.clasificacionAcreditarRequisito = clasificacionAcreditarRequisito;
    }


    /**
     * Gets the clasificacion value for this Clasificacion.
     * 
     * @return clasificacion
     */
    public Campo[] getClasificacion() {
        return clasificacion;
    }


    /**
     * Sets the clasificacion value for this Clasificacion.
     * 
     * @param clasificacion
     */
    public void setClasificacion(Campo[] clasificacion) {
        this.clasificacion = clasificacion;
    }


    /**
     * Gets the clasificacionAcreditarRequisito value for this Clasificacion.
     * 
     * @return clasificacionAcreditarRequisito
     */
    public Campo getClasificacionAcreditarRequisito() {
        return clasificacionAcreditarRequisito;
    }


    /**
     * Sets the clasificacionAcreditarRequisito value for this Clasificacion.
     * 
     * @param clasificacionAcreditarRequisito
     */
    public void setClasificacionAcreditarRequisito(Campo clasificacionAcreditarRequisito) {
        this.clasificacionAcreditarRequisito = clasificacionAcreditarRequisito;
    }
}
