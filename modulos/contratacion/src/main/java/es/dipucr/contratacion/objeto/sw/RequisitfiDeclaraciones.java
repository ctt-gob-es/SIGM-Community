package es.dipucr.contratacion.objeto.sw;

public class RequisitfiDeclaraciones{
    private java.lang.String descripcion;

    private java.lang.String nombre;

    private Campo requisitEspecifico;

    private Campo requisitEspecificoAcreditarRequisito;

    public RequisitfiDeclaraciones() {
    }

    public RequisitfiDeclaraciones(
           java.lang.String descripcion,
           java.lang.String nombre,
           Campo requisitEspecifico,
           Campo requisitEspecificoAcreditarRequisito) {
           this.descripcion = descripcion;
           this.nombre = nombre;
           this.requisitEspecifico = requisitEspecifico;
           this.requisitEspecificoAcreditarRequisito = requisitEspecificoAcreditarRequisito;
    }


    /**
     * Gets the descripcion value for this RequisitfiDeclaraciones.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this RequisitfiDeclaraciones.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the nombre value for this RequisitfiDeclaraciones.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this RequisitfiDeclaraciones.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the requisitEspecifico value for this RequisitfiDeclaraciones.
     * 
     * @return requisitEspecifico
     */
    public Campo getRequisitEspecifico() {
        return requisitEspecifico;
    }


    /**
     * Sets the requisitEspecifico value for this RequisitfiDeclaraciones.
     * 
     * @param requisitEspecifico
     */
    public void setRequisitEspecifico(Campo requisitEspecifico) {
        this.requisitEspecifico = requisitEspecifico;
    }


    /**
     * Gets the requisitEspecificoAcreditarRequisito value for this RequisitfiDeclaraciones.
     * 
     * @return requisitEspecificoAcreditarRequisito
     */
    public Campo getRequisitEspecificoAcreditarRequisito() {
        return requisitEspecificoAcreditarRequisito;
    }


    /**
     * Sets the requisitEspecificoAcreditarRequisito value for this RequisitfiDeclaraciones.
     * 
     * @param requisitEspecificoAcreditarRequisito
     */
    public void setRequisitEspecificoAcreditarRequisito(Campo requisitEspecificoAcreditarRequisito) {
        this.requisitEspecificoAcreditarRequisito = requisitEspecificoAcreditarRequisito;
    }
}
