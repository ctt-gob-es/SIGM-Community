package es.dipucr.sigem.api.rule.common.expFol;

import java.io.File;
import java.util.Date;

public class DocIncluir{

	public String infoPagRDE;
	public String numExp;
	public String tipoRegistro;
	public String nreg;
	public String freg;
	public File docPdf;
	public String nombreDoc;
	public String extension;
	public String descripcion;
	public Date fechaDoc;
	public Date fechaAprobacion;
	public File docOriginal;	
	public String idPlantilla;
	public int idTipDoc;
	
	public DocIncluir(){
		this.infoPagRDE = "";
		this.numExp = "";
		this.tipoRegistro = "";
		this.nreg = "";
		this.freg = "";		
		this.docPdf = null;
		this.nombreDoc = "";
		this.extension = "";
		this.descripcion = "";
		this.fechaDoc = new Date();
		this.fechaAprobacion = new Date();
		this.docOriginal = null;
		this.idPlantilla = "";
		this.idTipDoc = 0;
	}
	
	public DocIncluir(File docPdf, String nombreDoc, String extension, String descripcion, Date fechaDoc, Date fechaAprobacion, File docOriginal, String idPlantilla, int idTipoDoc){
		this.infoPagRDE = "";
		this.numExp = "";
		this.tipoRegistro = "";
		this.nreg = "";
		this.freg = "";
		this.docPdf = docPdf;
		this.nombreDoc = nombreDoc;
		this.extension = extension;
		this.descripcion = descripcion;
		this.fechaDoc = fechaDoc;
		this.fechaAprobacion = fechaAprobacion;
		this.docOriginal = docOriginal;
		this.idPlantilla = idPlantilla;
		this.idTipDoc = idTipoDoc;
	}
	
	public DocIncluir(String infoPagRDE, String numExp, String tipoRegistro, String nreg, String freg, String nombreDoc, String extension, String descripcion, Date fechaDoc, Date fechaAprobacion, String idPlantilla, int idTipoDoc){
		this.infoPagRDE = infoPagRDE;
		this.numExp = numExp;
		this.tipoRegistro = tipoRegistro;
		this.nreg = nreg;
		this.freg = freg;		
		this.docPdf = null;
		this.nombreDoc = nombreDoc;
		this.extension = extension;
		this.descripcion = descripcion;
		this.fechaDoc = fechaDoc;
		this.fechaAprobacion = fechaAprobacion;
		this.docOriginal = null;
		this.idPlantilla = idPlantilla;
		this.idTipDoc = idTipoDoc;
	}
	
	public DocIncluir(String infoPagRDE, String numExp, String tipoRegistro, String nreg, String freg, File docPdf, String nombreDoc, String extension, String descripcion, Date fechaDoc, Date fechaAprobacion, File docOriginal, String idPlantilla, int idTipoDoc){
		this.infoPagRDE = infoPagRDE;
		this.numExp = numExp;
		this.tipoRegistro = tipoRegistro;
		this.nreg = nreg;
		this.freg = freg;		
		this.docPdf = docPdf;
		this.nombreDoc = nombreDoc;
		this.extension = extension;
		this.descripcion = descripcion;
		this.fechaDoc = fechaDoc;
		this.fechaAprobacion = fechaAprobacion;
		this.docOriginal = docOriginal;
		this.idPlantilla = idPlantilla;
		this.idTipDoc = idTipoDoc;
	}
	
	public void borrar() {
		if(docPdf != null && docPdf.exists()) docPdf.delete();
		if(docOriginal != null && docOriginal.exists()) docOriginal.delete();
	}
}
