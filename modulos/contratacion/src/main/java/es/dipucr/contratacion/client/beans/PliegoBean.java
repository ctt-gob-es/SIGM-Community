/**
 * PliegoBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class PliegoBean  extends es.dipucr.contratacion.client.beans.AnuncioBean  implements java.io.Serializable {
    private es.dipucr.contratacion.client.beans.AplicacionPresupuestaria[] apliPesu;

    private es.dipucr.contratacion.client.beans.Campo[] clasificacion;

    private es.dipucr.contratacion.client.beans.CondicionesLicitadores condLicit;

    private es.dipucr.contratacion.client.beans.Campo[] cpv;

    private es.dipucr.contratacion.client.beans.CriteriosAdjudicacion criterios;

    private es.dipucr.contratacion.client.beans.Documento documentoPCAP;

    private es.dipucr.contratacion.client.beans.Documento documentoPPT;

    private es.dipucr.contratacion.client.beans.DuracionContratoBean duracionContrato;

    private java.util.Calendar fechaPresentacionSolcitudesParticipacion;

    private java.lang.String formulaRevisionPrecios;

    private es.dipucr.contratacion.client.beans.FundacionPrograma fundacionPrograma;

    private es.dipucr.contratacion.client.beans.Garantia[] garantia;

    private java.lang.String numExpediente;

    private java.lang.String objetoContrato;

    private es.dipucr.contratacion.client.beans.PersonalContacto personalContantoContratacion;

    private es.dipucr.contratacion.client.beans.PersonalContacto personalContantoSecretaria;

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

    public PliegoBean() {
    }

    public PliegoBean(
           es.dipucr.contratacion.client.beans.AplicacionPresupuestaria[] apliPesu,
           es.dipucr.contratacion.client.beans.Campo[] clasificacion,
           es.dipucr.contratacion.client.beans.CondicionesLicitadores condLicit,
           es.dipucr.contratacion.client.beans.Campo[] cpv,
           es.dipucr.contratacion.client.beans.CriteriosAdjudicacion criterios,
           es.dipucr.contratacion.client.beans.Documento documentoPCAP,
           es.dipucr.contratacion.client.beans.Documento documentoPPT,
           es.dipucr.contratacion.client.beans.DuracionContratoBean duracionContrato,
           java.util.Calendar fechaPresentacionSolcitudesParticipacion,
           java.lang.String formulaRevisionPrecios,
           es.dipucr.contratacion.client.beans.FundacionPrograma fundacionPrograma,
           es.dipucr.contratacion.client.beans.Garantia[] garantia,
           java.lang.String numExpediente,
           java.lang.String objetoContrato,
           es.dipucr.contratacion.client.beans.PersonalContacto personalContantoContratacion,
           es.dipucr.contratacion.client.beans.PersonalContacto personalContantoSecretaria,
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
        this.documentoPCAP = documentoPCAP;
        this.documentoPPT = documentoPPT;
        this.duracionContrato = duracionContrato;
        this.fechaPresentacionSolcitudesParticipacion = fechaPresentacionSolcitudesParticipacion;
        this.formulaRevisionPrecios = formulaRevisionPrecios;
        this.fundacionPrograma = fundacionPrograma;
        this.garantia = garantia;
        this.numExpediente = numExpediente;
        this.objetoContrato = objetoContrato;
        this.personalContantoContratacion = personalContantoContratacion;
        this.personalContantoSecretaria = personalContantoSecretaria;
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
     * Gets the apliPesu value for this PliegoBean.
     * 
     * @return apliPesu
     */
    public es.dipucr.contratacion.client.beans.AplicacionPresupuestaria[] getApliPesu() {
        return apliPesu;
    }


    /**
     * Sets the apliPesu value for this PliegoBean.
     * 
     * @param apliPesu
     */
    public void setApliPesu(es.dipucr.contratacion.client.beans.AplicacionPresupuestaria[] apliPesu) {
        this.apliPesu = apliPesu;
    }


    /**
     * Gets the clasificacion value for this PliegoBean.
     * 
     * @return clasificacion
     */
    public es.dipucr.contratacion.client.beans.Campo[] getClasificacion() {
        return clasificacion;
    }


    /**
     * Sets the clasificacion value for this PliegoBean.
     * 
     * @param clasificacion
     */
    public void setClasificacion(es.dipucr.contratacion.client.beans.Campo[] clasificacion) {
        this.clasificacion = clasificacion;
    }


    /**
     * Gets the condLicit value for this PliegoBean.
     * 
     * @return condLicit
     */
    public es.dipucr.contratacion.client.beans.CondicionesLicitadores getCondLicit() {
        return condLicit;
    }


    /**
     * Sets the condLicit value for this PliegoBean.
     * 
     * @param condLicit
     */
    public void setCondLicit(es.dipucr.contratacion.client.beans.CondicionesLicitadores condLicit) {
        this.condLicit = condLicit;
    }


    /**
     * Gets the cpv value for this PliegoBean.
     * 
     * @return cpv
     */
    public es.dipucr.contratacion.client.beans.Campo[] getCpv() {
        return cpv;
    }


    /**
     * Sets the cpv value for this PliegoBean.
     * 
     * @param cpv
     */
    public void setCpv(es.dipucr.contratacion.client.beans.Campo[] cpv) {
        this.cpv = cpv;
    }


    /**
     * Gets the criterios value for this PliegoBean.
     * 
     * @return criterios
     */
    public es.dipucr.contratacion.client.beans.CriteriosAdjudicacion getCriterios() {
        return criterios;
    }


    /**
     * Sets the criterios value for this PliegoBean.
     * 
     * @param criterios
     */
    public void setCriterios(es.dipucr.contratacion.client.beans.CriteriosAdjudicacion criterios) {
        this.criterios = criterios;
    }


    /**
     * Gets the documentoPCAP value for this PliegoBean.
     * 
     * @return documentoPCAP
     */
    public es.dipucr.contratacion.client.beans.Documento getDocumentoPCAP() {
        return documentoPCAP;
    }


    /**
     * Sets the documentoPCAP value for this PliegoBean.
     * 
     * @param documentoPCAP
     */
    public void setDocumentoPCAP(es.dipucr.contratacion.client.beans.Documento documentoPCAP) {
        this.documentoPCAP = documentoPCAP;
    }


    /**
     * Gets the documentoPPT value for this PliegoBean.
     * 
     * @return documentoPPT
     */
    public es.dipucr.contratacion.client.beans.Documento getDocumentoPPT() {
        return documentoPPT;
    }


    /**
     * Sets the documentoPPT value for this PliegoBean.
     * 
     * @param documentoPPT
     */
    public void setDocumentoPPT(es.dipucr.contratacion.client.beans.Documento documentoPPT) {
        this.documentoPPT = documentoPPT;
    }


    /**
     * Gets the duracionContrato value for this PliegoBean.
     * 
     * @return duracionContrato
     */
    public es.dipucr.contratacion.client.beans.DuracionContratoBean getDuracionContrato() {
        return duracionContrato;
    }


    /**
     * Sets the duracionContrato value for this PliegoBean.
     * 
     * @param duracionContrato
     */
    public void setDuracionContrato(es.dipucr.contratacion.client.beans.DuracionContratoBean duracionContrato) {
        this.duracionContrato = duracionContrato;
    }


    /**
     * Gets the fechaPresentacionSolcitudesParticipacion value for this PliegoBean.
     * 
     * @return fechaPresentacionSolcitudesParticipacion
     */
    public java.util.Calendar getFechaPresentacionSolcitudesParticipacion() {
        return fechaPresentacionSolcitudesParticipacion;
    }


    /**
     * Sets the fechaPresentacionSolcitudesParticipacion value for this PliegoBean.
     * 
     * @param fechaPresentacionSolcitudesParticipacion
     */
    public void setFechaPresentacionSolcitudesParticipacion(java.util.Calendar fechaPresentacionSolcitudesParticipacion) {
        this.fechaPresentacionSolcitudesParticipacion = fechaPresentacionSolcitudesParticipacion;
    }


    /**
     * Gets the formulaRevisionPrecios value for this PliegoBean.
     * 
     * @return formulaRevisionPrecios
     */
    public java.lang.String getFormulaRevisionPrecios() {
        return formulaRevisionPrecios;
    }


    /**
     * Sets the formulaRevisionPrecios value for this PliegoBean.
     * 
     * @param formulaRevisionPrecios
     */
    public void setFormulaRevisionPrecios(java.lang.String formulaRevisionPrecios) {
        this.formulaRevisionPrecios = formulaRevisionPrecios;
    }


    /**
     * Gets the fundacionPrograma value for this PliegoBean.
     * 
     * @return fundacionPrograma
     */
    public es.dipucr.contratacion.client.beans.FundacionPrograma getFundacionPrograma() {
        return fundacionPrograma;
    }


    /**
     * Sets the fundacionPrograma value for this PliegoBean.
     * 
     * @param fundacionPrograma
     */
    public void setFundacionPrograma(es.dipucr.contratacion.client.beans.FundacionPrograma fundacionPrograma) {
        this.fundacionPrograma = fundacionPrograma;
    }


    /**
     * Gets the garantia value for this PliegoBean.
     * 
     * @return garantia
     */
    public es.dipucr.contratacion.client.beans.Garantia[] getGarantia() {
        return garantia;
    }


    /**
     * Sets the garantia value for this PliegoBean.
     * 
     * @param garantia
     */
    public void setGarantia(es.dipucr.contratacion.client.beans.Garantia[] garantia) {
        this.garantia = garantia;
    }


    /**
     * Gets the numExpediente value for this PliegoBean.
     * 
     * @return numExpediente
     */
    public java.lang.String getNumExpediente() {
        return numExpediente;
    }


    /**
     * Sets the numExpediente value for this PliegoBean.
     * 
     * @param numExpediente
     */
    public void setNumExpediente(java.lang.String numExpediente) {
        this.numExpediente = numExpediente;
    }


    /**
     * Gets the objetoContrato value for this PliegoBean.
     * 
     * @return objetoContrato
     */
    public java.lang.String getObjetoContrato() {
        return objetoContrato;
    }


    /**
     * Sets the objetoContrato value for this PliegoBean.
     * 
     * @param objetoContrato
     */
    public void setObjetoContrato(java.lang.String objetoContrato) {
        this.objetoContrato = objetoContrato;
    }


    /**
     * Gets the personalContantoContratacion value for this PliegoBean.
     * 
     * @return personalContantoContratacion
     */
    public es.dipucr.contratacion.client.beans.PersonalContacto getPersonalContantoContratacion() {
        return personalContantoContratacion;
    }


    /**
     * Sets the personalContantoContratacion value for this PliegoBean.
     * 
     * @param personalContantoContratacion
     */
    public void setPersonalContantoContratacion(es.dipucr.contratacion.client.beans.PersonalContacto personalContantoContratacion) {
        this.personalContantoContratacion = personalContantoContratacion;
    }


    /**
     * Gets the personalContantoSecretaria value for this PliegoBean.
     * 
     * @return personalContantoSecretaria
     */
    public es.dipucr.contratacion.client.beans.PersonalContacto getPersonalContantoSecretaria() {
        return personalContantoSecretaria;
    }


    /**
     * Sets the personalContantoSecretaria value for this PliegoBean.
     * 
     * @param personalContantoSecretaria
     */
    public void setPersonalContantoSecretaria(es.dipucr.contratacion.client.beans.PersonalContacto personalContantoSecretaria) {
        this.personalContantoSecretaria = personalContantoSecretaria;
    }


    /**
     * Gets the presentacionOfertas value for this PliegoBean.
     * 
     * @return presentacionOfertas
     */
    public es.dipucr.contratacion.client.beans.Periodo getPresentacionOfertas() {
        return presentacionOfertas;
    }


    /**
     * Sets the presentacionOfertas value for this PliegoBean.
     * 
     * @param presentacionOfertas
     */
    public void setPresentacionOfertas(es.dipucr.contratacion.client.beans.Periodo presentacionOfertas) {
        this.presentacionOfertas = presentacionOfertas;
    }


    /**
     * Gets the presupuestoConIva value for this PliegoBean.
     * 
     * @return presupuestoConIva
     */
    public java.lang.String getPresupuestoConIva() {
        return presupuestoConIva;
    }


    /**
     * Sets the presupuestoConIva value for this PliegoBean.
     * 
     * @param presupuestoConIva
     */
    public void setPresupuestoConIva(java.lang.String presupuestoConIva) {
        this.presupuestoConIva = presupuestoConIva;
    }


    /**
     * Gets the presupuestoSinIva value for this PliegoBean.
     * 
     * @return presupuestoSinIva
     */
    public java.lang.String getPresupuestoSinIva() {
        return presupuestoSinIva;
    }


    /**
     * Sets the presupuestoSinIva value for this PliegoBean.
     * 
     * @param presupuestoSinIva
     */
    public void setPresupuestoSinIva(java.lang.String presupuestoSinIva) {
        this.presupuestoSinIva = presupuestoSinIva;
    }


    /**
     * Gets the procContratacion value for this PliegoBean.
     * 
     * @return procContratacion
     */
    public es.dipucr.contratacion.client.beans.Campo getProcContratacion() {
        return procContratacion;
    }


    /**
     * Sets the procContratacion value for this PliegoBean.
     * 
     * @param procContratacion
     */
    public void setProcContratacion(es.dipucr.contratacion.client.beans.Campo procContratacion) {
        this.procContratacion = procContratacion;
    }


    /**
     * Gets the reqDecl value for this PliegoBean.
     * 
     * @return reqDecl
     */
    public es.dipucr.contratacion.client.beans.RequisitfiDeclaraciones[] getReqDecl() {
        return reqDecl;
    }


    /**
     * Sets the reqDecl value for this PliegoBean.
     * 
     * @param reqDecl
     */
    public void setReqDecl(es.dipucr.contratacion.client.beans.RequisitfiDeclaraciones[] reqDecl) {
        this.reqDecl = reqDecl;
    }


    /**
     * Gets the sobreElect value for this PliegoBean.
     * 
     * @return sobreElect
     */
    public es.dipucr.contratacion.client.beans.SobreElectronico[] getSobreElect() {
        return sobreElect;
    }


    /**
     * Sets the sobreElect value for this PliegoBean.
     * 
     * @param sobreElect
     */
    public void setSobreElect(es.dipucr.contratacion.client.beans.SobreElectronico[] sobreElect) {
        this.sobreElect = sobreElect;
    }


    /**
     * Gets the solvenciaEconomica value for this PliegoBean.
     * 
     * @return solvenciaEconomica
     */
    public es.dipucr.contratacion.client.beans.SolvenciaEconomica[] getSolvenciaEconomica() {
        return solvenciaEconomica;
    }


    /**
     * Sets the solvenciaEconomica value for this PliegoBean.
     * 
     * @param solvenciaEconomica
     */
    public void setSolvenciaEconomica(es.dipucr.contratacion.client.beans.SolvenciaEconomica[] solvenciaEconomica) {
        this.solvenciaEconomica = solvenciaEconomica;
    }


    /**
     * Gets the solvenciaTecn value for this PliegoBean.
     * 
     * @return solvenciaTecn
     */
    public es.dipucr.contratacion.client.beans.SolvenciaTecnica[] getSolvenciaTecn() {
        return solvenciaTecn;
    }


    /**
     * Sets the solvenciaTecn value for this PliegoBean.
     * 
     * @param solvenciaTecn
     */
    public void setSolvenciaTecn(es.dipucr.contratacion.client.beans.SolvenciaTecnica[] solvenciaTecn) {
        this.solvenciaTecn = solvenciaTecn;
    }


    /**
     * Gets the subTipoContrato value for this PliegoBean.
     * 
     * @return subTipoContrato
     */
    public es.dipucr.contratacion.client.beans.Campo getSubTipoContrato() {
        return subTipoContrato;
    }


    /**
     * Sets the subTipoContrato value for this PliegoBean.
     * 
     * @param subTipoContrato
     */
    public void setSubTipoContrato(es.dipucr.contratacion.client.beans.Campo subTipoContrato) {
        this.subTipoContrato = subTipoContrato;
    }


    /**
     * Gets the tipoContrato value for this PliegoBean.
     * 
     * @return tipoContrato
     */
    public es.dipucr.contratacion.client.beans.Campo getTipoContrato() {
        return tipoContrato;
    }


    /**
     * Sets the tipoContrato value for this PliegoBean.
     * 
     * @param tipoContrato
     */
    public void setTipoContrato(es.dipucr.contratacion.client.beans.Campo tipoContrato) {
        this.tipoContrato = tipoContrato;
    }


    /**
     * Gets the tipoPresentacionOferta value for this PliegoBean.
     * 
     * @return tipoPresentacionOferta
     */
    public es.dipucr.contratacion.client.beans.Campo getTipoPresentacionOferta() {
        return tipoPresentacionOferta;
    }


    /**
     * Sets the tipoPresentacionOferta value for this PliegoBean.
     * 
     * @param tipoPresentacionOferta
     */
    public void setTipoPresentacionOferta(es.dipucr.contratacion.client.beans.Campo tipoPresentacionOferta) {
        this.tipoPresentacionOferta = tipoPresentacionOferta;
    }


    /**
     * Gets the tipoTramitacion value for this PliegoBean.
     * 
     * @return tipoTramitacion
     */
    public es.dipucr.contratacion.client.beans.Campo getTipoTramitacion() {
        return tipoTramitacion;
    }


    /**
     * Sets the tipoTramitacion value for this PliegoBean.
     * 
     * @param tipoTramitacion
     */
    public void setTipoTramitacion(es.dipucr.contratacion.client.beans.Campo tipoTramitacion) {
        this.tipoTramitacion = tipoTramitacion;
    }


    /**
     * Gets the tramitacionGasto value for this PliegoBean.
     * 
     * @return tramitacionGasto
     */
    public es.dipucr.contratacion.client.beans.Campo getTramitacionGasto() {
        return tramitacionGasto;
    }


    /**
     * Sets the tramitacionGasto value for this PliegoBean.
     * 
     * @param tramitacionGasto
     */
    public void setTramitacionGasto(es.dipucr.contratacion.client.beans.Campo tramitacionGasto) {
        this.tramitacionGasto = tramitacionGasto;
    }


    /**
     * Gets the valorEstimadoContrato value for this PliegoBean.
     * 
     * @return valorEstimadoContrato
     */
    public java.lang.String getValorEstimadoContrato() {
        return valorEstimadoContrato;
    }


    /**
     * Sets the valorEstimadoContrato value for this PliegoBean.
     * 
     * @param valorEstimadoContrato
     */
    public void setValorEstimadoContrato(java.lang.String valorEstimadoContrato) {
        this.valorEstimadoContrato = valorEstimadoContrato;
    }


    /**
     * Gets the varOfert value for this PliegoBean.
     * 
     * @return varOfert
     */
    public es.dipucr.contratacion.client.beans.VariantesOfertas getVarOfert() {
        return varOfert;
    }


    /**
     * Sets the varOfert value for this PliegoBean.
     * 
     * @param varOfert
     */
    public void setVarOfert(es.dipucr.contratacion.client.beans.VariantesOfertas varOfert) {
        this.varOfert = varOfert;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PliegoBean)) return false;
        PliegoBean other = (PliegoBean) obj;
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
            ((this.documentoPCAP==null && other.getDocumentoPCAP()==null) || 
             (this.documentoPCAP!=null &&
              this.documentoPCAP.equals(other.getDocumentoPCAP()))) &&
            ((this.documentoPPT==null && other.getDocumentoPPT()==null) || 
             (this.documentoPPT!=null &&
              this.documentoPPT.equals(other.getDocumentoPPT()))) &&
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
            ((this.numExpediente==null && other.getNumExpediente()==null) || 
             (this.numExpediente!=null &&
              this.numExpediente.equals(other.getNumExpediente()))) &&
            ((this.objetoContrato==null && other.getObjetoContrato()==null) || 
             (this.objetoContrato!=null &&
              this.objetoContrato.equals(other.getObjetoContrato()))) &&
            ((this.personalContantoContratacion==null && other.getPersonalContantoContratacion()==null) || 
             (this.personalContantoContratacion!=null &&
              this.personalContantoContratacion.equals(other.getPersonalContantoContratacion()))) &&
            ((this.personalContantoSecretaria==null && other.getPersonalContantoSecretaria()==null) || 
             (this.personalContantoSecretaria!=null &&
              this.personalContantoSecretaria.equals(other.getPersonalContantoSecretaria()))) &&
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
        if (getDocumentoPCAP() != null) {
            _hashCode += getDocumentoPCAP().hashCode();
        }
        if (getDocumentoPPT() != null) {
            _hashCode += getDocumentoPPT().hashCode();
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
        if (getNumExpediente() != null) {
            _hashCode += getNumExpediente().hashCode();
        }
        if (getObjetoContrato() != null) {
            _hashCode += getObjetoContrato().hashCode();
        }
        if (getPersonalContantoContratacion() != null) {
            _hashCode += getPersonalContantoContratacion().hashCode();
        }
        if (getPersonalContantoSecretaria() != null) {
            _hashCode += getPersonalContantoSecretaria().hashCode();
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
        new org.apache.axis.description.TypeDesc(PliegoBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PliegoBean"));
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
        elemField.setFieldName("documentoPCAP");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "documentoPCAP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Documento"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentoPPT");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "documentoPPT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Documento"));
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
        elemField.setFieldName("numExpediente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "numExpediente"));
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
        elemField.setFieldName("personalContantoContratacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "personalContantoContratacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PersonalContacto"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalContantoSecretaria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "personalContantoSecretaria"));
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
