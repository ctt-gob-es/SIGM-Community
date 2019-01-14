package es.dipucr.contratacion.objeto.sw;

import java.util.Calendar;

public class EspacioVirtualLicitacionBean{

	private String publishedByUser = null;
	private String numexp;
	private Campo tipoContrato = null;
	private Campo subTipoContrato = null;
	private String objetoContrato = "";
	private String organoContratacion = null;
	private Campo procContratacion = null;
	private Campo tipoTramitacion = null;
	private Campo tramitacionGasto = null;
	private Periodo presentacionOfertas = null;
	private Calendar fechaPresentacionSolcitudesParticipacion = null;
	private Campo[] cpv = null;
	private String presupuestoSinIva = "";
	private String presupuestoConIva = "";
	private String valorEstimadoContrato = "";
	private Clasificacion clasificacion = null;
	private Campo tipoPresentacionOferta = null;
	private PublicacionesOficialesBean diarios=null;
	private AplicacionPresupuestaria[] apliPesu = null;
	private CondicionesLicitadores condLicit = null;
	private RequisitfiDeclaraciones[] reqDecl = null;
	private SolvenciaTecnica[] solvenciaTecn = null;
	private SolvenciaEconomica[] solvenciaEconomica = null;
	private CriteriosAdjudicacion criterios = null;
	private SobreElectronico [] sobreElect = null;
	private VariantesOfertas varOfert = null;
	private Garantia [] garantia = null;
	private PersonalContacto personalContactoContratacion = null;
	private PersonalContacto personalContactoSecretaria = null;
	private FundacionPrograma fundacionPrograma = null;
	private String formulaRevisionPrecios = null;
	private DuracionContratoBean duracionContrato = null;
	private Campo lugarEjecucionContrato = null;
	private OrganoAsistencia organoAsistencia = null;
	private Documento documentoPPT = null;
	private Documento documentoPCAP = null;
	private Documento [] docAdicionales = null;
	//ANUNCIO DOUE
	private Campo contratoSujetoRegArmon;
	private Lotes lotes = null;
	private LicitadorBean[] licitadores = null;
	
	public String getNumexp() {
		return numexp;
	}
	public void setNumexp(String numexp) {
		this.numexp = numexp;
	}
	public Campo getTipoContrato() {
		return tipoContrato;
	}
	public void setTipoContrato(Campo tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
	public Campo getSubTipoContrato() {
		return subTipoContrato;
	}
	public void setSubTipoContrato(Campo subTipoContrato) {
		this.subTipoContrato = subTipoContrato;
	}
	public String getObjetoContrato() {
		return objetoContrato;
	}
	public void setObjetoContrato(String objetoContrato) {
		this.objetoContrato = objetoContrato;
	}
	public Campo getProcContratacion() {
		return procContratacion;
	}
	public void setProcContratacion(Campo procContratacion) {
		this.procContratacion = procContratacion;
	}
	public Campo getTipoTramitacion() {
		return tipoTramitacion;
	}
	public void setTipoTramitacion(Campo tipoTramitacion) {
		this.tipoTramitacion = tipoTramitacion;
	}
	public Campo getTramitacionGasto() {
		return tramitacionGasto;
	}
	public void setTramitacionGasto(Campo tramitacionGasto) {
		this.tramitacionGasto = tramitacionGasto;
	}
	public Campo[] getCpv() {
		return cpv;
	}
	public void setCpv(Campo[] cpv) {
		this.cpv = cpv;
	}
	public String getPresupuestoSinIva() {
		return presupuestoSinIva;
	}
	public void setPresupuestoSinIva(String presupuestoSinIva) {
		this.presupuestoSinIva = presupuestoSinIva;
	}
	public String getPresupuestoConIva() {
		return presupuestoConIva;
	}
	public void setPresupuestoConIva(String presupuestoConIva) {
		this.presupuestoConIva = presupuestoConIva;
	}
	public PublicacionesOficialesBean getDiarios() {
		return diarios;
	}
	public void setDiarios(PublicacionesOficialesBean diarios) {
		this.diarios = diarios;
	}
	public Campo getTipoPresentacionOferta() {
		return tipoPresentacionOferta;
	}
	public void setTipoPresentacionOferta(Campo tipoPresentacionOferta) {
		this.tipoPresentacionOferta = tipoPresentacionOferta;
	}
	public String getValorEstimadoContrato() {
		return valorEstimadoContrato;
	}
	public void setValorEstimadoContrato(String valorEstimadoContrato) {
		this.valorEstimadoContrato = valorEstimadoContrato;
	}
	public AplicacionPresupuestaria[] getApliPesu() {
		return apliPesu;
	}
	public void setApliPesu(AplicacionPresupuestaria[] apliPesu) {
		this.apliPesu = apliPesu;
	}
	public CondicionesLicitadores getCondLicit() {
		return condLicit;
	}
	public void setCondLicit(CondicionesLicitadores condLicit) {
		this.condLicit = condLicit;
	}
	public RequisitfiDeclaraciones[] getReqDecl() {
		return reqDecl;
	}
	public void setReqDecl(RequisitfiDeclaraciones[] reqDecl) {
		this.reqDecl = reqDecl;
	}
	public SolvenciaTecnica[] getSolvenciaTecn() {
		return solvenciaTecn;
	}
	public void setSolvenciaTecn(SolvenciaTecnica[] solvenciaTecn) {
		this.solvenciaTecn = solvenciaTecn;
	}
	public SolvenciaEconomica[] getSolvenciaEconomica() {
		return solvenciaEconomica;
	}
	public void setSolvenciaEconomica(SolvenciaEconomica[] solvenciaEconomica) {
		this.solvenciaEconomica = solvenciaEconomica;
	}
	public CriteriosAdjudicacion getCriterios() {
		return criterios;
	}
	public void setCriterios(CriteriosAdjudicacion criterios) {
		this.criterios = criterios;
	}
	public SobreElectronico[] getSobreElect() {
		return sobreElect;
	}
	public void setSobreElect(SobreElectronico[] sobreElect) {
		this.sobreElect = sobreElect;
	}
	public VariantesOfertas getVarOfert() {
		return varOfert;
	}
	public void setVarOfert(VariantesOfertas varOfert) {
		this.varOfert = varOfert;
	}
	public Garantia [] getGarantia() {
		return garantia;
	}
	public void setGarantia(Garantia [] garantia) {
		this.garantia = garantia;
	}
	public FundacionPrograma getFundacionPrograma() {
		return fundacionPrograma;
	}
	public void setFundacionPrograma(FundacionPrograma fundacionPrograma) {
		this.fundacionPrograma = fundacionPrograma;
	}
	public String getFormulaRevisionPrecios() {
		return formulaRevisionPrecios;
	}
	public void setFormulaRevisionPrecios(String formulaRevisionPrecios) {
		this.formulaRevisionPrecios = formulaRevisionPrecios;
	}
	public Calendar getFechaPresentacionSolcitudesParticipacion() {
		return fechaPresentacionSolcitudesParticipacion;
	}
	public void setFechaPresentacionSolcitudesParticipacion(
			Calendar fechaPresentacionSolcitudesParticipacion) {
		this.fechaPresentacionSolcitudesParticipacion = fechaPresentacionSolcitudesParticipacion;
	}
	public PersonalContacto getPersonalContactoContratacion() {
		return personalContactoContratacion;
	}
	public void setPersonalContactoContratacion(
			PersonalContacto personalContactoContratacion) {
		this.personalContactoContratacion = personalContactoContratacion;
	}
	public PersonalContacto getPersonalContactoSecretaria() {
		return personalContactoSecretaria;
	}
	public void setPersonalContactoSecretaria(PersonalContacto personalContactoSecretaria) {
		this.personalContactoSecretaria = personalContactoSecretaria;
	}
	public Periodo getPresentacionOfertas() {
		return presentacionOfertas;
	}
	public void setPresentacionOfertas(Periodo presentacionOfertas) {
		this.presentacionOfertas = presentacionOfertas;
	}
	public DuracionContratoBean getDuracionContrato() {
		return duracionContrato;
	}
	public void setDuracionContrato(DuracionContratoBean duracionContrato) {
		this.duracionContrato = duracionContrato;
	}
	public Campo getLugarEjecucionContrato() {
		return lugarEjecucionContrato;
	}
	public void setLugarEjecucionContrato(Campo lugarEjecucionContrato) {
		this.lugarEjecucionContrato = lugarEjecucionContrato;
	}
	public Campo getContratoSujetoRegArmon() {
		return contratoSujetoRegArmon;
	}
	public void setContratoSujetoRegArmon(Campo contratoSujetoRegArmon) {
		this.contratoSujetoRegArmon = contratoSujetoRegArmon;
	}
	public String getOrganoContratacion() {
		return organoContratacion;
	}
	public void setOrganoContratacion(String organoContratacion) {
		this.organoContratacion = organoContratacion;
	}
	public Lotes getLotes() {
		return lotes;
	}
	public void setLotes(Lotes lotes) {
		this.lotes = lotes;
	}
	public OrganoAsistencia getOrganoAsistencia() {
		return organoAsistencia;
	}
	public void setOrganoAsistencia(OrganoAsistencia organoAsistencia) {
		this.organoAsistencia = organoAsistencia;
	}
	public Documento getDocumentoPPT() {
		return documentoPPT;
	}
	public void setDocumentoPPT(Documento documentoPPT) {
		this.documentoPPT = documentoPPT;
	}
	public Documento getDocumentoPCAP() {
		return documentoPCAP;
	}
	public void setDocumentoPCAP(Documento documentoPCAP) {
		this.documentoPCAP = documentoPCAP;
	}
	public LicitadorBean[] getLicitadores() {
		return licitadores;
	}
	public void setLicitadores(LicitadorBean[] licitadores) {
		this.licitadores = licitadores;
	}
	public Clasificacion getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(Clasificacion clasificacion) {
		this.clasificacion = clasificacion;
	}
	public String getPublishedByUser() {
		return publishedByUser;
	}
	public void setPublishedByUser(String publishedByUser) {
		this.publishedByUser = publishedByUser;
	}
	public Documento [] getDocAdicionales() {
		return docAdicionales;
	}
	public void setDocAdicionales(Documento [] docAdicionales) {
		this.docAdicionales = docAdicionales;
	}
}