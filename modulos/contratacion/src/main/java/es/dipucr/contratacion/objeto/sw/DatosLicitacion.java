package es.dipucr.contratacion.objeto.sw;

public class DatosLicitacion {
	private Campo tipoPresentacionOferta = null;
	private AplicacionPresupuestaria[] aplicacionPres = null;
	private CriteriosAdjudicacion critAdj = null;
	private VariantesOfertas variantes = null;
	private FundacionPrograma fundacionPrograma = null;
	private String revisionPrecios = null;
	private Campo contratoSujetoRegArmon = null;
	private OrganoAsistencia organoAsistencia = null;
	
	public Campo getTipoPresentacionOferta() {
		return tipoPresentacionOferta;
	}

	public void setTipoPresentacionOferta(Campo tipoPresentacionOferta) {
		this.tipoPresentacionOferta = tipoPresentacionOferta;
	}

	public AplicacionPresupuestaria[] getAplicacionPres() {
		return aplicacionPres;
	}

	public void setAplicacionPres(AplicacionPresupuestaria[] aplicacionPres) {
		this.aplicacionPres = aplicacionPres;
	}

	public CriteriosAdjudicacion getCritAdj() {
		return critAdj;
	}

	public void setCritAdj(CriteriosAdjudicacion critAdj) {
		this.critAdj = critAdj;
	}

	public VariantesOfertas getVariantes() {
		return variantes;
	}

	public void setVariantes(VariantesOfertas variantes) {
		this.variantes = variantes;
	}

	public FundacionPrograma getFundacionPrograma() {
		return fundacionPrograma;
	}

	public void setFundacionPrograma(FundacionPrograma fundacionPrograma) {
		this.fundacionPrograma = fundacionPrograma;
	}

	public String getRevisionPrecios() {
		return revisionPrecios;
	}

	public void setRevisionPrecios(String revisionPrecios) {
		this.revisionPrecios = revisionPrecios;
	}

	public Campo getContratoSujetoRegArmon() {
		return contratoSujetoRegArmon;
	}

	public void setContratoSujetoRegArmon(Campo contratoSujetoRegArmon) {
		this.contratoSujetoRegArmon = contratoSujetoRegArmon;
	}

	public OrganoAsistencia getOrganoAsistencia() {
		return organoAsistencia;
	}

	public void setOrganoAsistencia(OrganoAsistencia organoAsistencia) {
		this.organoAsistencia = organoAsistencia;
	}

}
