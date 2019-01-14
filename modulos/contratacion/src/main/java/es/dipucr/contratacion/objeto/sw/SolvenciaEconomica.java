package es.dipucr.contratacion.objeto.sw;

public class SolvenciaEconomica{
    private Campo criterioSolvencia;

    private Campo criterioSolvenciaAcreditarRequisito;

    private java.lang.String[] descripcion;

    private Campo expresEvaluarCriterioEvalucion;

    private java.util.Calendar periodoDuracion;

    private java.lang.String valorUmbralImporte;

    private java.lang.String valorUmbralNoImporte;

    public SolvenciaEconomica() {
    }

    public SolvenciaEconomica(
           Campo criterioSolvencia,
           Campo criterioSolvenciaAcreditarRequisito,
           java.lang.String[] descripcion,
           Campo expresEvaluarCriterioEvalucion,
           java.util.Calendar periodoDuracion,
           java.lang.String valorUmbralImporte,
           java.lang.String valorUmbralNoImporte) {
           this.criterioSolvencia = criterioSolvencia;
           this.criterioSolvenciaAcreditarRequisito = criterioSolvenciaAcreditarRequisito;
           this.descripcion = descripcion;
           this.expresEvaluarCriterioEvalucion = expresEvaluarCriterioEvalucion;
           this.periodoDuracion = periodoDuracion;
           this.valorUmbralImporte = valorUmbralImporte;
           this.valorUmbralNoImporte = valorUmbralNoImporte;
    }


    /**
     * Gets the criterioSolvencia value for this SolvenciaEconomica.
     * 
     * @return criterioSolvencia
     */
    public Campo getCriterioSolvencia() {
        return criterioSolvencia;
    }


    /**
     * Sets the criterioSolvencia value for this SolvenciaEconomica.
     * 
     * @param criterioSolvencia
     */
    public void setCriterioSolvencia(Campo criterioSolvencia) {
        this.criterioSolvencia = criterioSolvencia;
    }


    /**
     * Gets the criterioSolvenciaAcreditarRequisito value for this SolvenciaEconomica.
     * 
     * @return criterioSolvenciaAcreditarRequisito
     */
    public Campo getCriterioSolvenciaAcreditarRequisito() {
        return criterioSolvenciaAcreditarRequisito;
    }


    /**
     * Sets the criterioSolvenciaAcreditarRequisito value for this SolvenciaEconomica.
     * 
     * @param criterioSolvenciaAcreditarRequisito
     */
    public void setCriterioSolvenciaAcreditarRequisito(Campo criterioSolvenciaAcreditarRequisito) {
        this.criterioSolvenciaAcreditarRequisito = criterioSolvenciaAcreditarRequisito;
    }


    /**
     * Gets the descripcion value for this SolvenciaEconomica.
     * 
     * @return descripcion
     */
    public java.lang.String[] getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this SolvenciaEconomica.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String[] descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the expresEvaluarCriterioEvalucion value for this SolvenciaEconomica.
     * 
     * @return expresEvaluarCriterioEvalucion
     */
    public Campo getExpresEvaluarCriterioEvalucion() {
        return expresEvaluarCriterioEvalucion;
    }


    /**
     * Sets the expresEvaluarCriterioEvalucion value for this SolvenciaEconomica.
     * 
     * @param expresEvaluarCriterioEvalucion
     */
    public void setExpresEvaluarCriterioEvalucion(Campo expresEvaluarCriterioEvalucion) {
        this.expresEvaluarCriterioEvalucion = expresEvaluarCriterioEvalucion;
    }


    /**
     * Gets the periodoDuracion value for this SolvenciaEconomica.
     * 
     * @return periodoDuracion
     */
    public java.util.Calendar getPeriodoDuracion() {
        return periodoDuracion;
    }


    /**
     * Sets the periodoDuracion value for this SolvenciaEconomica.
     * 
     * @param periodoDuracion
     */
    public void setPeriodoDuracion(java.util.Calendar periodoDuracion) {
        this.periodoDuracion = periodoDuracion;
    }


    /**
     * Gets the valorUmbralImporte value for this SolvenciaEconomica.
     * 
     * @return valorUmbralImporte
     */
    public java.lang.String getValorUmbralImporte() {
        return valorUmbralImporte;
    }


    /**
     * Sets the valorUmbralImporte value for this SolvenciaEconomica.
     * 
     * @param valorUmbralImporte
     */
    public void setValorUmbralImporte(java.lang.String valorUmbralImporte) {
        this.valorUmbralImporte = valorUmbralImporte;
    }


    /**
     * Gets the valorUmbralNoImporte value for this SolvenciaEconomica.
     * 
     * @return valorUmbralNoImporte
     */
    public java.lang.String getValorUmbralNoImporte() {
        return valorUmbralNoImporte;
    }


    /**
     * Sets the valorUmbralNoImporte value for this SolvenciaEconomica.
     * 
     * @param valorUmbralNoImporte
     */
    public void setValorUmbralNoImporte(java.lang.String valorUmbralNoImporte) {
        this.valorUmbralNoImporte = valorUmbralNoImporte;
    }
}
