package es.dipucr.contratacion.objeto.sw;


public class EspacioVirtualLicitacionBean{

	private String publishedByUser = null;	
	private String numexp = null;
	private DatosContrato datosContrato = null;
	private DatosTramitacion datosTramitacion = null;
	private DiariosOficiales diariosOficiales = null;
	private DatosEmpresa datosEmpresa = null;
	private DatosLicitacion datosLicitacion = null;
	private SobreElectronico [] sobreElectronico = null;
	private Solvencia solvencia = null;
	private Garantia[] garantia = null;
	private DepartamentosContacto departamentosContacto = null;
	private LicitadorBean[] licitadores = null;
	private Lotes lotes = null;
	private Documento documentoPPT = null;
	private Documento documentoPCAP = null;
	private Documento [] docAnexoPliegoAdicionales = null;
	private Documento documentoPublicacion = null;
	public String getPublishedByUser() {
		return publishedByUser;
	}
	public void setPublishedByUser(String publishedByUser) {
		this.publishedByUser = publishedByUser;
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
	public Documento[] getDocAnexoPliegoAdicionales() {
		return docAnexoPliegoAdicionales;
	}
	public void setDocAnexoPliegoAdicionales(Documento[] docAnexoPliegoAdicionales) {
		this.docAnexoPliegoAdicionales = docAnexoPliegoAdicionales;
	}
	public Documento getDocumentoPublicacion() {
		return documentoPublicacion;
	}
	public void setDocumentoPublicacion(Documento documentoPublicacion) {
		this.documentoPublicacion = documentoPublicacion;
	}
	public String getNumexp() {
		return numexp;
	}
	public void setNumexp(String numexp) {
		this.numexp = numexp;
	}
	public DatosContrato getDatosContrato() {
		return datosContrato;
	}
	public void setDatosContrato(DatosContrato datosContrato) {
		this.datosContrato = datosContrato;
	}
	public DatosTramitacion getDatosTramitacion() {
		return datosTramitacion;
	}
	public void setDatosTramitacion(DatosTramitacion datosTramitacion) {
		this.datosTramitacion = datosTramitacion;
	}
	public DiariosOficiales getDiariosOficiales() {
		return diariosOficiales;
	}
	public void setDiariosOficiales(DiariosOficiales diariosOficiales) {
		this.diariosOficiales = diariosOficiales;
	}
	public DatosEmpresa getDatosEmpresa() {
		return datosEmpresa;
	}
	public void setDatosEmpresa(DatosEmpresa datosEmpresa) {
		this.datosEmpresa = datosEmpresa;
	}
	public DatosLicitacion getDatosLicitacion() {
		return datosLicitacion;
	}
	public void setDatosLicitacion(DatosLicitacion datosLicitacion) {
		this.datosLicitacion = datosLicitacion;
	}
	public SobreElectronico [] getSobreElectronico() {
		return sobreElectronico;
	}
	public void setSobreElectronico(SobreElectronico [] sobreElectronico) {
		this.sobreElectronico = sobreElectronico;
	}
	public Solvencia getSolvencia() {
		return solvencia;
	}
	public void setSolvencia(Solvencia solvencia) {
		this.solvencia = solvencia;
	}
	public Garantia[] getGarantia() {
		return garantia;
	}
	public void setGarantia(Garantia[] garantia) {
		this.garantia = garantia;
	}
	public DepartamentosContacto getDepartamentosContacto() {
		return departamentosContacto;
	}
	public void setDepartamentosContacto(DepartamentosContacto departamentosContacto) {
		this.departamentosContacto = departamentosContacto;
	}
	public LicitadorBean[] getLicitadores() {
		return licitadores;
	}
	public void setLicitadores(LicitadorBean[] licitadores) {
		this.licitadores = licitadores;
	}
	public Lotes getLotes() {
		return lotes;
	}
	public void setLotes(Lotes lotes) {
		this.lotes = lotes;
	}
}