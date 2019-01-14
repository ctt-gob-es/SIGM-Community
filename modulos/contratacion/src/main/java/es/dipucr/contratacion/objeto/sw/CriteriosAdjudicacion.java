
package es.dipucr.contratacion.objeto.sw;

public class CriteriosAdjudicacion{
    private Campo codigoAlgoritmo;

    private CriterioAdjudicacionMultCrit[] critAdjudicacionMultCrit;

    private java.lang.String[] descripcion;

    private java.lang.String[] descripcionBajaTemeraria;

    private java.lang.String[] descripcionComiteTecnico;

    private PersonaComite[] persComite;

    private Campo tipoAdjudicacion;

    private Campo valoracionTipoOferta;

    public CriteriosAdjudicacion() {
    }

    public CriteriosAdjudicacion(
           Campo codigoAlgoritmo,
           CriterioAdjudicacionMultCrit[] critAdjudicacionMultCrit,
           java.lang.String[] descripcion,
           java.lang.String[] descripcionBajaTemeraria,
           java.lang.String[] descripcionComiteTecnico,
           PersonaComite[] persComite,
           Campo tipoAdjudicacion,
           Campo valoracionTipoOferta) {
           this.codigoAlgoritmo = codigoAlgoritmo;
           this.critAdjudicacionMultCrit = critAdjudicacionMultCrit;
           this.descripcion = descripcion;
           this.descripcionBajaTemeraria = descripcionBajaTemeraria;
           this.descripcionComiteTecnico = descripcionComiteTecnico;
           this.persComite = persComite;
           this.tipoAdjudicacion = tipoAdjudicacion;
           this.valoracionTipoOferta = valoracionTipoOferta;
    }


    /**
     * Gets the codigoAlgoritmo value for this CriteriosAdjudicacion.
     * 
     * @return codigoAlgoritmo
     */
    public Campo getCodigoAlgoritmo() {
        return codigoAlgoritmo;
    }


    /**
     * Sets the codigoAlgoritmo value for this CriteriosAdjudicacion.
     * 
     * @param codigoAlgoritmo
     */
    public void setCodigoAlgoritmo(Campo codigoAlgoritmo) {
        this.codigoAlgoritmo = codigoAlgoritmo;
    }


    /**
     * Gets the critAdjudicacionMultCrit value for this CriteriosAdjudicacion.
     * 
     * @return critAdjudicacionMultCrit
     */
    public CriterioAdjudicacionMultCrit[] getCritAdjudicacionMultCrit() {
        return critAdjudicacionMultCrit;
    }


    /**
     * Sets the critAdjudicacionMultCrit value for this CriteriosAdjudicacion.
     * 
     * @param critAdjudicacionMultCrit
     */
    public void setCritAdjudicacionMultCrit(CriterioAdjudicacionMultCrit[] critAdjudicacionMultCrit) {
        this.critAdjudicacionMultCrit = critAdjudicacionMultCrit;
    }


    /**
     * Gets the descripcion value for this CriteriosAdjudicacion.
     * 
     * @return descripcion
     */
    public java.lang.String[] getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this CriteriosAdjudicacion.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String[] descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the descripcionBajaTemeraria value for this CriteriosAdjudicacion.
     * 
     * @return descripcionBajaTemeraria
     */
    public java.lang.String[] getDescripcionBajaTemeraria() {
        return descripcionBajaTemeraria;
    }


    /**
     * Sets the descripcionBajaTemeraria value for this CriteriosAdjudicacion.
     * 
     * @param descripcionBajaTemeraria
     */
    public void setDescripcionBajaTemeraria(java.lang.String[] descripcionBajaTemeraria) {
        this.descripcionBajaTemeraria = descripcionBajaTemeraria;
    }


    /**
     * Gets the descripcionComiteTecnico value for this CriteriosAdjudicacion.
     * 
     * @return descripcionComiteTecnico
     */
    public java.lang.String[] getDescripcionComiteTecnico() {
        return descripcionComiteTecnico;
    }


    /**
     * Sets the descripcionComiteTecnico value for this CriteriosAdjudicacion.
     * 
     * @param descripcionComiteTecnico
     */
    public void setDescripcionComiteTecnico(java.lang.String[] descripcionComiteTecnico) {
        this.descripcionComiteTecnico = descripcionComiteTecnico;
    }


    /**
     * Gets the persComite value for this CriteriosAdjudicacion.
     * 
     * @return persComite
     */
    public PersonaComite[] getPersComite() {
        return persComite;
    }


    /**
     * Sets the persComite value for this CriteriosAdjudicacion.
     * 
     * @param persComite
     */
    public void setPersComite(PersonaComite[] persComite) {
        this.persComite = persComite;
    }


    /**
     * Gets the tipoAdjudicacion value for this CriteriosAdjudicacion.
     * 
     * @return tipoAdjudicacion
     */
    public Campo getTipoAdjudicacion() {
        return tipoAdjudicacion;
    }


    /**
     * Sets the tipoAdjudicacion value for this CriteriosAdjudicacion.
     * 
     * @param tipoAdjudicacion
     */
    public void setTipoAdjudicacion(Campo tipoAdjudicacion) {
        this.tipoAdjudicacion = tipoAdjudicacion;
    }


    /**
     * Gets the valoracionTipoOferta value for this CriteriosAdjudicacion.
     * 
     * @return valoracionTipoOferta
     */
    public Campo getValoracionTipoOferta() {
        return valoracionTipoOferta;
    }


    /**
     * Sets the valoracionTipoOferta value for this CriteriosAdjudicacion.
     * 
     * @param valoracionTipoOferta
     */
    public void setValoracionTipoOferta(Campo valoracionTipoOferta) {
        this.valoracionTipoOferta = valoracionTipoOferta;
    }
}
