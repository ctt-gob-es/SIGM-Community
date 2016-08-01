/**
 * AnuncioLicitacionBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class AnuncioLicitacionBean  extends es.dipucr.contratacion.client.beans.AnuncioBean  implements java.io.Serializable {
    private es.dipucr.contratacion.client.beans.AplicacionPresupuestaria[] apliPesu;

    private es.dipucr.contratacion.client.beans.Campo[] clasificacion;

    private es.dipucr.contratacion.client.beans.CondicionesLicitadores condLicit;

    private es.dipucr.contratacion.client.beans.Campo[] cpv;

    private es.dipucr.contratacion.client.beans.CriteriosAdjudicacion criterios;

    private es.dipucr.contratacion.client.beans.PublicacionesOficialesBean diarios;

    private es.dipucr.contratacion.client.beans.DuracionContratoBean duracionContrato;

    private java.util.Calendar fechaPresentacionSolcitudesParticipacion;

    private java.lang.String formulaRevisionPrecios;

    private es.dipucr.contratacion.client.beans.FundacionPrograma fundacionPrograma;

    private es.dipucr.contratacion.client.beans.Garantia[] garantia;

    private java.lang.String numexp;

    private java.lang.String objetoContrato;

    private es.dipucr.contratacion.client.beans.PersonalContacto personalContactoContratacion;

    private es.dipucr.contratacion.client.beans.PersonalContacto personalContactoSecretaria;

    private es.dipucr.contratacion.client.beans.Periodo presentacionOfertas;

    private java.lang.String presupuestoConIva;

    private java.lang.String presupuestoSinIva;

    private es.dipucr.contratacion.client.beans.Campo procContratacion;

    private es.dipucr.contratacion.client.beans.RequisitfiDeclaraciones[] reqDecl;

    private es.dipucr.contratacion.client.beans.SobreElectronico[] sobreElect;

    private es.dipucr.contratacion.client.beans.SolvenciaEconomica[] solvenciaEconomica;

    private es.dipucr.contratacion.client.beans.SolvenciaTecnica[] solvenciaTecn;

    private es.dipucr.contratacion.client.beans.Campo subTipoContrato;

    private es.dipucr.contratacion.client.beans.Campo tipoContrato;

    private es.dipucr.contratacion.client.beans.Campo tipoPresentacionOferta;

    private es.dipucr.contratacion.client.beans.Campo tipoTramitacion;

    private es.dipucr.contratacion.client.beans.Campo tramitacionGasto;

    private java.lang.String valorEstimadoContrato;

    private es.dipucr.contratacion.client.beans.VariantesOfertas varOfert;

    public AnuncioLicitacionBean() {
    }

    public AnuncioLicitacionBean(
           es.dipucr.contratacion.client.beans.AplicacionPresupuestaria[] apliPesu,
           es.dipucr.contratacion.client.beans.Campo[] clasificacion,
           es.dipucr.contratacion.client.beans.CondicionesLicitadores condLicit,
           es.dipucr.contratacion.client.beans.Campo[] cpv,
           es.dipucr.contratacion.client.beans.CriteriosAdjudicacion criterios,
           es.dipucr.contratacion.client.beans.PublicacionesOficialesBean diarios,
           es.dipucr.contratacion.client.beans.DuracionContratoBean duracionContrato,
           java.util.Calendar fechaPresentacionSolcitudesParticipacion,
           java.lang.String formulaRevisionPrecios,
           es.dipucr.contratacion.client.beans.FundacionPrograma fundacionPrograma,
           es.dipucr.contratacion.client.beans.Garantia[] garantia,
           java.lang.String numexp,
           java.lang.String objetoContrato,
           es.dipucr.contratacion.client.beans.PersonalContacto personalContactoContratacion,
           es.dipucr.contratacion.client.beans.PersonalContacto personalContactoSecretaria,
           es.dipucr.contratacion.client.beans.Periodo presentacionOfertas,
           java.lang.String presupuestoConIva,
           java.lang.String presupuestoSinIva,
           es.dipucr.contratacion.client.beans.Campo procContratacion,
           es.dipucr.contratacion.client.beans.RequisitfiDeclaraciones[] reqDecl,
           es.dipucr.contratacion.client.beans.SobreElectronico[] sobreElect,
           es.dipucr.contratacion.client.beans.SolvenciaEconomica[] solvenciaEconomica,
           es.dipucr.contratacion.client.beans.SolvenciaTecnica[] solvenciaTecn,
           es.dipucr.contratacion.client.beans.Campo subTipoContrato,
           es.dipucr.contratacion.client.beans.Campo tipoContrato,
           es.dipucr.contratacion.client.beans.Campo tipoPresentacionOferta,
           es.dipucr.contratacion.client.beans.Campo tipoTramitacion,
           es.dipucr.contratacion.client.beans.Campo tramitacionGasto,
           java.lang.String valorEstimadoContrato,
           es.dipucr.contratacion.client.beans.VariantesOfertas varOfert) {
        this.apliPesu = apliPesu;
        this.clasificacion = clasificacion;
        this.condLicit = condLicit;
        this.cpv = cpv;
        this.criterios = criterios;
        this.diarios = diarios;
        this.duracionContrato = duracionContrato;
        this.fechaPresentacionSolcitudesParticipacion = fechaPresentacionSolcitudesParticipacion;
        this.formulaRevisionPrecios = formulaRevisionPrecios;
        this.fundacionPrograma = fundacionPrograma;
        this.garantia = garantia;
        this.numexp = numexp;
        this.objetoContrato = objetoContrato;
        this.personalContactoContratacion = personalContactoContratacion;
        this.personalContactoSecretaria = personalContactoSecretaria;
        this.presentacionOfertas = presentacionOfertas;
        this.presupuestoConIva = presupuestoConIva;
        this.presupuestoSinIva = presupuestoSinIva;
        this.procContratacion = procContratacion;
        this.reqDecl = reqDecl;
        this.sobreElect = sobreElect;
        this.solvenciaEconomica = solvenciaEconomica;
        this.solvenciaTecn = solvenciaTecn;
        this.subTipoContrato = subTipoContrato;
        this.tipoContrato = tipoContrato;
        this.tipoPresentacionOferta = tipoPresentacionOferta;
        this.tipoTramitacion = tipoTramitacion;
        this.tramitacionGasto = tramitacionGasto;
        this.valorEstimadoContrato = valorEstimadoContrato;
        this.varOfert = varOfert;
    }


    /**
     * Gets the apliPesu value for this AnuncioLicitacionBean.
     * 
     * @return apliPesu
     */
    public es.dipucr.contratacion.client.beans.AplicacionPresupuestaria[] getApliPesu() {
        return apliPesu;
    }


    /**
     * Sets the apliPesu value for this AnuncioLicitacionBean.
     * 
     * @param apliPesu
     */
    public void setApliPesu(es.dipucr.contratacion.client.beans.AplicacionPresupuestaria[] apliPesu) {
        this.apliPesu = apliPesu;
    }


    /**
     * Gets the clasificacion value for this AnuncioLicitacionBean.
     * 
     * @return clasificacion
     */
    public es.dipucr.contratacion.client.beans.Campo[] getClasificacion() {
        return clasificacion;
    }


    /**
     * Sets the clasificacion value for this AnuncioLicitacionBean.
     * 
     * @param clasificacion
     */
    public void setClasificacion(es.dipucr.contratacion.client.beans.Campo[] clasificacion) {
        this.clasificacion = clasificacion;
    }


    /**
     * Gets the condLicit value for this AnuncioLicitacionBean.
     * 
     * @return condLicit
     */
    public es.dipucr.contratacion.client.beans.CondicionesLicitadores getCondLicit() {
        return condLicit;
    }


    /**
     * Sets the condLicit value for this AnuncioLicitacionBean.
     * 
     * @param condLicit
     */
    public void setCondLicit(es.dipucr.contratacion.client.beans.CondicionesLicitadores condLicit) {
        this.condLicit = condLicit;
    }


    /**
     * Gets the cpv value for this AnuncioLicitacionBean.
     * 
     * @return cpv
     */
    public es.dipucr.contratacion.client.beans.Campo[] getCpv() {
        return cpv;
    }


    /**
     * Sets the cpv value for this AnuncioLicitacionBean.
     * 
     * @param cpv
     */
    public void setCpv(es.dipucr.contratacion.client.beans.Campo[] cpv) {
        this.cpv = cpv;
    }


    /**
     * Gets the criterios value for this AnuncioLicitacionBean.
     * 
     * @return criterios
     */
    public es.dipucr.contratacion.client.beans.CriteriosAdjudicacion getCriterios() {
        return criterios;
    }


    /**
     * Sets the criterios value for this AnuncioLicitacionBean.
     * 
     * @param criterios
     */
    public void setCriterios(es.dipucr.contratacion.client.beans.CriteriosAdjudicacion criterios) {
        this.criterios = criterios;
    }


    /**
     * Gets the diarios value for this AnuncioLicitacionBean.
     * 
     * @return diarios
     */
    public es.dipucr.contratacion.client.beans.PublicacionesOficialesBean getDiarios() {
        return diarios;
    }


    /**
     * Sets the diarios value for this AnuncioLicitacionBean.
     * 
     * @param diarios
     */
    public void setDiarios(es.dipucr.contratacion.client.beans.PublicacionesOficialesBean diarios) {
        this.diarios = diarios;
    }


    /**
     * Gets the duracionContrato value for this AnuncioLicitacionBean.
     * 
     * @return duracionContrato
     */
    public es.dipucr.contratacion.client.beans.DuracionContratoBean getDuracionContrato() {
        return duracionContrato;
    }


    /**
     * Sets the duracionContrato value for this AnuncioLicitacionBean.
     * 
     * @param duracionContrato
     */
    public void setDuracionContrato(es.dipucr.contratacion.client.beans.DuracionContratoBean duracionContrato) {
        this.duracionContrato = duracionContrato;
    }


    /**
     * Gets the fechaPresentacionSolcitudesParticipacion value for this AnuncioLicitacionBean.
     * 
     * @return fechaPresentacionSolcitudesParticipacion
     */
    public java.util.Calendar getFechaPresentacionSolcitudesParticipacion() {
        return fechaPresentacionSolcitudesParticipacion;
    }


    /**
     * Sets the fechaPresentacionSolcitudesParticipacion value for this AnuncioLicitacionBean.
     * 
     * @param fechaPresentacionSolcitudesParticipacion
     */
    public void setFechaPresentacionSolcitudesParticipacion(java.util.Calendar fechaPresentacionSolcitudesParticipacion) {
        this.fechaPresentacionSolcitudesParticipacion = fechaPresentacionSolcitudesParticipacion;
    }


    /**
     * Gets the formulaRevisionPrecios value for this AnuncioLicitacionBean.
     * 
     * @return formulaRevisionPrecios
     */
    public java.lang.String getFormulaRevisionPrecios() {
        return formulaRevisionPrecios;
    }


    /**
     * Sets the formulaRevisionPrecios value for this AnuncioLicitacionBean.
     * 
     * @param formulaRevisionPrecios
     */
    public void setFormulaRevisionPrecios(java.lang.String formulaRevisionPrecios) {
        this.formulaRevisionPrecios = formulaRevisionPrecios;
    }


    /**
     * Gets the fundacionPrograma value for this AnuncioLicitacionBean.
     * 
     * @return fundacionPrograma
     */
    public es.dipucr.contratacion.client.beans.FundacionPrograma getFundacionPrograma() {
        return fundacionPrograma;
    }


    /**
     * Sets the fundacionPrograma value for this AnuncioLicitacionBean.
     * 
     * @param fundacionPrograma
     */
    public void setFundacionPrograma(es.dipucr.contratacion.client.beans.FundacionPrograma fundacionPrograma) {
        this.fundacionPrograma = fundacionPrograma;
    }


    /**
     * Gets the garantia value for this AnuncioLicitacionBean.
     * 
     * @return garantia
     */
    public es.dipucr.contratacion.client.beans.Garantia[] getGarantia() {
        return garantia;
    }


    /**
     * Sets the garantia value for this AnuncioLicitacionBean.
     * 
     * @param garantia
     */
    public void setGarantia(es.dipucr.contratacion.client.beans.Garantia[] garantia) {
        this.garantia = garantia;
    }


    /**
     * Gets the numexp value for this AnuncioLicitacionBean.
     * 
     * @return numexp
     */
    public java.lang.String getNumexp() {
        return numexp;
    }


    /**
     * Sets the numexp value for this AnuncioLicitacionBean.
     * 
     * @param numexp
     */
    public void setNumexp(java.lang.String numexp) {
        this.numexp = numexp;
    }


    /**
     * Gets the objetoContrato value for this AnuncioLicitacionBean.
     * 
     * @return objetoContrato
     */
    public java.lang.String getObjetoContrato() {
        return objetoContrato;
    }


    /**
     * Sets the objetoContrato value for this AnuncioLicitacionBean.
     * 
     * @param objetoContrato
     */
    public void setObjetoContrato(java.lang.String objetoContrato) {
        this.objetoContrato = objetoContrato;
    }


    /**
     * Gets the personalContactoContratacion value for this AnuncioLicitacionBean.
     * 
     * @return personalContactoContratacion
     */
    public es.dipucr.contratacion.client.beans.PersonalContacto getPersonalContactoContratacion() {
        return personalContactoContratacion;
    }


    /**
     * Sets the personalContactoContratacion value for this AnuncioLicitacionBean.
     * 
     * @param personalContactoContratacion
     */
    public void setPersonalContactoContratacion(es.dipucr.contratacion.client.beans.PersonalContacto personalContactoContratacion) {
        this.personalContactoContratacion = personalContactoContratacion;
    }


    /**
     * Gets the personalContactoSecretaria value for this AnuncioLicitacionBean.
     * 
     * @return personalContactoSecretaria
     */
    public es.dipucr.contratacion.client.beans.PersonalContacto getPersonalContactoSecretaria() {
        return personalContactoSecretaria;
    }


    /**
     * Sets the personalContactoSecretaria value for this AnuncioLicitacionBean.
     * 
     * @param personalContactoSecretaria
     */
    public void setPersonalContactoSecretaria(es.dipucr.contratacion.client.beans.PersonalContacto personalContactoSecretaria) {
        this.personalContactoSecretaria = personalContactoSecretaria;
    }


    /**
     * Gets the presentacionOfertas value for this AnuncioLicitacionBean.
     * 
     * @return presentacionOfertas
     */
    public es.dipucr.contratacion.client.beans.Periodo getPresentacionOfertas() {
        return presentacionOfertas;
    }


    /**
     * Sets the presentacionOfertas value for this AnuncioLicitacionBean.
     * 
     * @param presentacionOfertas
     */
    public void setPresentacionOfertas(es.dipucr.contratacion.client.beans.Periodo presentacionOfertas) {
        this.presentacionOfertas = presentacionOfertas;
    }


    /**
     * Gets the presupuestoConIva value for this AnuncioLicitacionBean.
     * 
     * @return presupuestoConIva
     */
    public java.lang.String getPresupuestoConIva() {
        return presupuestoConIva;
    }


    /**
     * Sets the presupuestoConIva value for this AnuncioLicitacionBean.
     * 
     * @param presupuestoConIva
     */
    public void setPresupuestoConIva(java.lang.String presupuestoConIva) {
        this.presupuestoConIva = presupuestoConIva;
    }


    /**
     * Gets the presupuestoSinIva value for this AnuncioLicitacionBean.
     * 
     * @return presupuestoSinIva
     */
    public java.lang.String getPresupuestoSinIva() {
        return presupuestoSinIva;
    }


    /**
     * Sets the presupuestoSinIva value for this AnuncioLicitacionBean.
     * 
     * @param presupuestoSinIva
     */
    public void setPresupuestoSinIva(java.lang.String presupuestoSinIva) {
        this.presupuestoSinIva = presupuestoSinIva;
    }


    /**
     * Gets the procContratacion value for this AnuncioLicitacionBean.
     * 
     * @return procContratacion
     */
    public es.dipucr.contratacion.client.beans.Campo getProcContratacion() {
        return procContratacion;
    }


    /**
     * Sets the procContratacion value for this AnuncioLicitacionBean.
     * 
     * @param procContratacion
     */
    public void setProcContratacion(es.dipucr.contratacion.client.beans.Campo procContratacion) {
        this.procContratacion = procContratacion;
    }


    /**
     * Gets the reqDecl value for this AnuncioLicitacionBean.
     * 
     * @return reqDecl
     */
    public es.dipucr.contratacion.client.beans.RequisitfiDeclaraciones[] getReqDecl() {
        return reqDecl;
    }


    /**
     * Sets the reqDecl value for this AnuncioLicitacionBean.
     * 
     * @param reqDecl
     */
    public void setReqDecl(es.dipucr.contratacion.client.beans.RequisitfiDeclaraciones[] reqDecl) {
        this.reqDecl = reqDecl;
    }


    /**
     * Gets the sobreElect value for this AnuncioLicitacionBean.
     * 
     * @return sobreElect
     */
    public es.dipucr.contratacion.client.beans.SobreElectronico[] getSobreElect() {
        return sobreElect;
    }


    /**
     * Sets the sobreElect value for this AnuncioLicitacionBean.
     * 
     * @param sobreElect
     */
    public void setSobreElect(es.dipucr.contratacion.client.beans.SobreElectronico[] sobreElect) {
        this.sobreElect = sobreElect;
    }


    /**
     * Gets the solvenciaEconomica value for this AnuncioLicitacionBean.
     * 
     * @return solvenciaEconomica
     */
    public es.dipucr.contratacion.client.beans.SolvenciaEconomica[] getSolvenciaEconomica() {
        return solvenciaEconomica;
    }


    /**
     * Sets the solvenciaEconomica value for this AnuncioLicitacionBean.
     * 
     * @param solvenciaEconomica
     */
    public void setSolvenciaEconomica(es.dipucr.contratacion.client.beans.SolvenciaEconomica[] solvenciaEconomica) {
        this.solvenciaEconomica = solvenciaEconomica;
    }


    /**
     * Gets the solvenciaTecn value for this AnuncioLicitacionBean.
     * 
     * @return solvenciaTecn
     */
    public es.dipucr.contratacion.client.beans.SolvenciaTecnica[] getSolvenciaTecn() {
        return solvenciaTecn;
    }


    /**
     * Sets the solvenciaTecn value for this AnuncioLicitacionBean.
     * 
     * @param solvenciaTecn
     */
    public void setSolvenciaTecn(es.dipucr.contratacion.client.beans.SolvenciaTecnica[] solvenciaTecn) {
        this.solvenciaTecn = solvenciaTecn;
    }


    /**
     * Gets the subTipoContrato value for this AnuncioLicitacionBean.
     * 
     * @return subTipoContrato
     */
    public es.dipucr.contratacion.client.beans.Campo getSubTipoContrato() {
        return subTipoContrato;
    }


    /**
     * Sets the subTipoContrato value for this AnuncioLicitacionBean.
     * 
     * @param subTipoContrato
     */
    public void setSubTipoContrato(es.dipucr.contratacion.client.beans.Campo subTipoContrato) {
        this.subTipoContrato = subTipoContrato;
    }


    /**
     * Gets the tipoContrato value for this AnuncioLicitacionBean.
     * 
     * @return tipoContrato
     */
    public es.dipucr.contratacion.client.beans.Campo getTipoContrato() {
        return tipoContrato;
    }


    /**
     * Sets the tipoContrato value for this AnuncioLicitacionBean.
     * 
     * @param tipoContrato
     */
    public void setTipoContrato(es.dipucr.contratacion.client.beans.Campo tipoContrato) {
        this.tipoContrato = tipoContrato;
    }


    /**
     * Gets the tipoPresentacionOferta value for this AnuncioLicitacionBean.
     * 
     * @return tipoPresentacionOferta
     */
    public es.dipucr.contratacion.client.beans.Campo getTipoPresentacionOferta() {
        return tipoPresentacionOferta;
    }


    /**
     * Sets the tipoPresentacionOferta value for this AnuncioLicitacionBean.
     * 
     * @param tipoPresentacionOferta
     */
    public void setTipoPresentacionOferta(es.dipucr.contratacion.client.beans.Campo tipoPresentacionOferta) {
        this.tipoPresentacionOferta = tipoPresentacionOferta;
    }


    /**
     * Gets the tipoTramitacion value for this AnuncioLicitacionBean.
     * 
     * @return tipoTramitacion
     */
    public es.dipucr.contratacion.client.beans.Campo getTipoTramitacion() {
        return tipoTramitacion;
    }


    /**
     * Sets the tipoTramitacion value for this AnuncioLicitacionBean.
     * 
     * @param tipoTramitacion
     */
    public void setTipoTramitacion(es.dipucr.contratacion.client.beans.Campo tipoTramitacion) {
        this.tipoTramitacion = tipoTramitacion;
    }


    /**
     * Gets the tramitacionGasto value for this AnuncioLicitacionBean.
     * 
     * @return tramitacionGasto
     */
    public es.dipucr.contratacion.client.beans.Campo getTramitacionGasto() {
        return tramitacionGasto;
    }


    /**
     * Sets the tramitacionGasto value for this AnuncioLicitacionBean.
     * 
     * @param tramitacionGasto
     */
    public void setTramitacionGasto(es.dipucr.contratacion.client.beans.Campo tramitacionGasto) {
        this.tramitacionGasto = tramitacionGasto;
    }


    /**
     * Gets the valorEstimadoContrato value for this AnuncioLicitacionBean.
     * 
     * @return valorEstimadoContrato
     */
    public java.lang.String getValorEstimadoContrato() {
        return valorEstimadoContrato;
    }


    /**
     * Sets the valorEstimadoContrato value for this AnuncioLicitacionBean.
     * 
     * @param valorEstimadoContrato
     */
    public void setValorEstimadoContrato(java.lang.String valorEstimadoContrato) {
        this.valorEstimadoContrato = valorEstimadoContrato;
    }


    /**
     * Gets the varOfert value for this AnuncioLicitacionBean.
     * 
     * @return varOfert
     */
    public es.dipucr.contratacion.client.beans.VariantesOfertas getVarOfert() {
        return varOfert;
    }


    /**
     * Sets the varOfert value for this AnuncioLicitacionBean.
     * 
     * @param varOfert
     */
    public void setVarOfert(es.dipucr.contratacion.client.beans.VariantesOfertas varOfert) {
        this.varOfert = varOfert;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AnuncioLicitacionBean)) return false;
        AnuncioLicitacionBean other = (AnuncioLicitacionBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.apliPesu==null && other.getApliPesu()==null) || 
             (this.apliPesu!=null &&
              java.util.Arrays.equals(this.apliPesu, other.getApliPesu()))) &&
            ((this.clasificacion==null && other.getClasificacion()==null) || 
             (this.clasificacion!=null &&
              java.util.Arrays.equals(this.clasificacion, other.getClasificacion()))) &&
            ((this.condLicit==null && other.getCondLicit()==null) || 
             (this.condLicit!=null &&
              this.condLicit.equals(other.getCondLicit()))) &&
            ((this.cpv==null && other.getCpv()==null) || 
             (this.cpv!=null &&
              java.util.Arrays.equals(this.cpv, other.getCpv()))) &&
            ((this.criterios==null && other.getCriterios()==null) || 
             (this.criterios!=null &&
              this.criterios.equals(other.getCriterios()))) &&
            ((this.diarios==null && other.getDiarios()==null) || 
             (this.diarios!=null &&
              this.diarios.equals(other.getDiarios()))) &&
            ((this.duracionContrato==null && other.getDuracionContrato()==null) || 
             (this.duracionContrato!=null &&
              this.duracionContrato.equals(other.getDuracionContrato()))) &&
            ((this.fechaPresentacionSolcitudesParticipacion==null && other.getFechaPresentacionSolcitudesParticipacion()==null) || 
             (this.fechaPresentacionSolcitudesParticipacion!=null &&
              this.fechaPresentacionSolcitudesParticipacion.equals(other.getFechaPresentacionSolcitudesParticipacion()))) &&
            ((this.formulaRevisionPrecios==null && other.getFormulaRevisionPrecios()==null) || 
             (this.formulaRevisionPrecios!=null &&
              this.formulaRevisionPrecios.equals(other.getFormulaRevisionPrecios()))) &&
            ((this.fundacionPrograma==null && other.getFundacionPrograma()==null) || 
             (this.fundacionPrograma!=null &&
              this.fundacionPrograma.equals(other.getFundacionPrograma()))) &&
            ((this.garantia==null && other.getGarantia()==null) || 
             (this.garantia!=null &&
              java.util.Arrays.equals(this.garantia, other.getGarantia()))) &&
            ((this.numexp==null && other.getNumexp()==null) || 
             (this.numexp!=null &&
              this.numexp.equals(other.getNumexp()))) &&
            ((this.objetoContrato==null && other.getObjetoContrato()==null) || 
             (this.objetoContrato!=null &&
              this.objetoContrato.equals(other.getObjetoContrato()))) &&
            ((this.personalContactoContratacion==null && other.getPersonalContactoContratacion()==null) || 
             (this.personalContactoContratacion!=null &&
              this.personalContactoContratacion.equals(other.getPersonalContactoContratacion()))) &&
            ((this.personalContactoSecretaria==null && other.getPersonalContactoSecretaria()==null) || 
             (this.personalContactoSecretaria!=null &&
              this.personalContactoSecretaria.equals(other.getPersonalContactoSecretaria()))) &&
            ((this.presentacionOfertas==null && other.getPresentacionOfertas()==null) || 
             (this.presentacionOfertas!=null &&
              this.presentacionOfertas.equals(other.getPresentacionOfertas()))) &&
            ((this.presupuestoConIva==null && other.getPresupuestoConIva()==null) || 
             (this.presupuestoConIva!=null &&
              this.presupuestoConIva.equals(other.getPresupuestoConIva()))) &&
            ((this.presupuestoSinIva==null && other.getPresupuestoSinIva()==null) || 
             (this.presupuestoSinIva!=null &&
              this.presupuestoSinIva.equals(other.getPresupuestoSinIva()))) &&
            ((this.procContratacion==null && other.getProcContratacion()==null) || 
             (this.procContratacion!=null &&
              this.procContratacion.equals(other.getProcContratacion()))) &&
            ((this.reqDecl==null && other.getReqDecl()==null) || 
             (this.reqDecl!=null &&
              java.util.Arrays.equals(this.reqDecl, other.getReqDecl()))) &&
            ((this.sobreElect==null && other.getSobreElect()==null) || 
             (this.sobreElect!=null &&
              java.util.Arrays.equals(this.sobreElect, other.getSobreElect()))) &&
            ((this.solvenciaEconomica==null && other.getSolvenciaEconomica()==null) || 
             (this.solvenciaEconomica!=null &&
              java.util.Arrays.equals(this.solvenciaEconomica, other.getSolvenciaEconomica()))) &&
            ((this.solvenciaTecn==null && other.getSolvenciaTecn()==null) || 
             (this.solvenciaTecn!=null &&
              java.util.Arrays.equals(this.solvenciaTecn, other.getSolvenciaTecn()))) &&
            ((this.subTipoContrato==null && other.getSubTipoContrato()==null) || 
             (this.subTipoContrato!=null &&
              this.subTipoContrato.equals(other.getSubTipoContrato()))) &&
            ((this.tipoContrato==null && other.getTipoContrato()==null) || 
             (this.tipoContrato!=null &&
              this.tipoContrato.equals(other.getTipoContrato()))) &&
            ((this.tipoPresentacionOferta==null && other.getTipoPresentacionOferta()==null) || 
             (this.tipoPresentacionOferta!=null &&
              this.tipoPresentacionOferta.equals(other.getTipoPresentacionOferta()))) &&
            ((this.tipoTramitacion==null && other.getTipoTramitacion()==null) || 
             (this.tipoTramitacion!=null &&
              this.tipoTramitacion.equals(other.getTipoTramitacion()))) &&
            ((this.tramitacionGasto==null && other.getTramitacionGasto()==null) || 
             (this.tramitacionGasto!=null &&
              this.tramitacionGasto.equals(other.getTramitacionGasto()))) &&
            ((this.valorEstimadoContrato==null && other.getValorEstimadoContrato()==null) || 
             (this.valorEstimadoContrato!=null &&
              this.valorEstimadoContrato.equals(other.getValorEstimadoContrato()))) &&
            ((this.varOfert==null && other.getVarOfert()==null) || 
             (this.varOfert!=null &&
              this.varOfert.equals(other.getVarOfert())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getApliPesu() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getApliPesu());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getApliPesu(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getClasificacion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getClasificacion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getClasificacion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCondLicit() != null) {
            _hashCode += getCondLicit().hashCode();
        }
        if (getCpv() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCpv());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCpv(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCriterios() != null) {
            _hashCode += getCriterios().hashCode();
        }
        if (getDiarios() != null) {
            _hashCode += getDiarios().hashCode();
        }
        if (getDuracionContrato() != null) {
            _hashCode += getDuracionContrato().hashCode();
        }
        if (getFechaPresentacionSolcitudesParticipacion() != null) {
            _hashCode += getFechaPresentacionSolcitudesParticipacion().hashCode();
        }
        if (getFormulaRevisionPrecios() != null) {
            _hashCode += getFormulaRevisionPrecios().hashCode();
        }
        if (getFundacionPrograma() != null) {
            _hashCode += getFundacionPrograma().hashCode();
        }
        if (getGarantia() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGarantia());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGarantia(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNumexp() != null) {
            _hashCode += getNumexp().hashCode();
        }
        if (getObjetoContrato() != null) {
            _hashCode += getObjetoContrato().hashCode();
        }
        if (getPersonalContactoContratacion() != null) {
            _hashCode += getPersonalContactoContratacion().hashCode();
        }
        if (getPersonalContactoSecretaria() != null) {
            _hashCode += getPersonalContactoSecretaria().hashCode();
        }
        if (getPresentacionOfertas() != null) {
            _hashCode += getPresentacionOfertas().hashCode();
        }
        if (getPresupuestoConIva() != null) {
            _hashCode += getPresupuestoConIva().hashCode();
        }
        if (getPresupuestoSinIva() != null) {
            _hashCode += getPresupuestoSinIva().hashCode();
        }
        if (getProcContratacion() != null) {
            _hashCode += getProcContratacion().hashCode();
        }
        if (getReqDecl() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReqDecl());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReqDecl(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSobreElect() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSobreElect());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSobreElect(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSolvenciaEconomica() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSolvenciaEconomica());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSolvenciaEconomica(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSolvenciaTecn() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSolvenciaTecn());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSolvenciaTecn(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSubTipoContrato() != null) {
            _hashCode += getSubTipoContrato().hashCode();
        }
        if (getTipoContrato() != null) {
            _hashCode += getTipoContrato().hashCode();
        }
        if (getTipoPresentacionOferta() != null) {
            _hashCode += getTipoPresentacionOferta().hashCode();
        }
        if (getTipoTramitacion() != null) {
            _hashCode += getTipoTramitacion().hashCode();
        }
        if (getTramitacionGasto() != null) {
            _hashCode += getTramitacionGasto().hashCode();
        }
        if (getValorEstimadoContrato() != null) {
            _hashCode += getValorEstimadoContrato().hashCode();
        }
        if (getVarOfert() != null) {
            _hashCode += getVarOfert().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AnuncioLicitacionBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AnuncioLicitacionBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apliPesu");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "apliPesu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AplicacionPresupuestaria"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clasificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "clasificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("condLicit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "condLicit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "CondicionesLicitadores"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cpv");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "cpv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("criterios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "criterios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "CriteriosAdjudicacion"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("diarios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "diarios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PublicacionesOficialesBean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("duracionContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "duracionContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "DuracionContratoBean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaPresentacionSolcitudesParticipacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaPresentacionSolcitudesParticipacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formulaRevisionPrecios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "formulaRevisionPrecios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundacionPrograma");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fundacionPrograma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "FundacionPrograma"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("garantia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "garantia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Garantia"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numexp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "numexp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objetoContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "objetoContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalContactoContratacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "personalContactoContratacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PersonalContacto"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalContactoSecretaria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "personalContactoSecretaria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PersonalContacto"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presentacionOfertas");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "presentacionOfertas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Periodo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presupuestoConIva");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "presupuestoConIva"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presupuestoSinIva");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "presupuestoSinIva"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procContratacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "procContratacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reqDecl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "reqDecl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "RequisitfiDeclaraciones"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sobreElect");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "sobreElect"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SobreElectronico"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("solvenciaEconomica");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "solvenciaEconomica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SolvenciaEconomica"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("solvenciaTecn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "solvenciaTecn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SolvenciaTecnica"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subTipoContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "subTipoContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "tipoContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoPresentacionOferta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "tipoPresentacionOferta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoTramitacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "tipoTramitacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tramitacionGasto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "tramitacionGasto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valorEstimadoContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "valorEstimadoContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("varOfert");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "varOfert"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "VariantesOfertas"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
