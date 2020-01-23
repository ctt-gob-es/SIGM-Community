package es.dipucr.contratacion.objeto.sw;

import java.util.Calendar;


public class DiariosOficiales {
	
	private Calendar anunLicitacionPerfilContratante = null;
	private Calendar anunFormalizacionPerfilContratante = null;
	private Calendar anunAdjudicacionPerfilContratante = null;
	
	private Calendar anuncioLicitacionBOE = null;
	private Calendar anuncioFormalizacionBOE = null;
	private Calendar anuncioAdjudicacionBOE = null;
	
	private Calendar anuncioLicitacionDOUE = null;
	private Calendar anuncioFormalizacionDOUE = null;
	private Calendar anuncioAdjudicacionDOUE = null;
	
	private boolean boeAdjudicacion = false;
	private boolean doueAdjudicacion = false;
	
	private boolean boeFormalizacion = false;
	private boolean doueFormalizacion = false;
	
	public boolean isBoeAdjudicacion() {
		return boeAdjudicacion;
	}
	public void setBoeAdjudicacion(boolean boeAdjudicacion) {
		this.boeAdjudicacion = boeAdjudicacion;
	}
	public boolean isDoueAdjudicacion() {
		return doueAdjudicacion;
	}
	public void setDoueAdjudicacion(boolean doueAdjudicacion) {
		this.doueAdjudicacion = doueAdjudicacion;
	}
	public boolean isBoeFormalizacion() {
		return boeFormalizacion;
	}
	public void setBoeFormalizacion(boolean boeFormalizacion) {
		this.boeFormalizacion = boeFormalizacion;
	}
	public boolean isDoueFormalizacion() {
		return doueFormalizacion;
	}
	public void setDoueFormalizacion(boolean doueFormalizacion) {
		this.doueFormalizacion = doueFormalizacion;
	}
	private Campo contratoSujetoRegArmon = null;
	private Boolean adjudicatarioPYME = null;	
	
	
	public Calendar getAnuncioLicitacionBOE() {
		return anuncioLicitacionBOE;
	}
	public void setAnuncioLicitacionBOE(Calendar anuncioLicitacionBOE) {
		this.anuncioLicitacionBOE = anuncioLicitacionBOE;
	}
	public Calendar getAnuncioFormalizacionBOE() {
		return anuncioFormalizacionBOE;
	}
	public void setAnuncioFormalizacionBOE(Calendar anuncioFormalizacionBOE) {
		this.anuncioFormalizacionBOE = anuncioFormalizacionBOE;
	}
	public Calendar getAnuncioAdjudicacionBOE() {
		return anuncioAdjudicacionBOE;
	}
	public void setAnuncioAdjudicacionBOE(Calendar anuncioAdjudicacionBOE) {
		this.anuncioAdjudicacionBOE = anuncioAdjudicacionBOE;
	}
	public Calendar getAnuncioLicitacionDOUE() {
		return anuncioLicitacionDOUE;
	}
	public void setAnuncioLicitacionDOUE(Calendar anuncioLicitacionDOUE) {
		this.anuncioLicitacionDOUE = anuncioLicitacionDOUE;
	}
	public Calendar getAnuncioFormalizacionDOUE() {
		return anuncioFormalizacionDOUE;
	}
	public void setAnuncioFormalizacionDOUE(Calendar anuncioFormalizacionDOUE) {
		this.anuncioFormalizacionDOUE = anuncioFormalizacionDOUE;
	}
	public Calendar getAnuncioAdjudicacionDOUE() {
		return anuncioAdjudicacionDOUE;
	}
	public void setAnuncioAdjudicacionDOUE(Calendar anuncioAdjudicacionDOUE) {
		this.anuncioAdjudicacionDOUE = anuncioAdjudicacionDOUE;
	}
	public Calendar getAnunLicitacionPerfilContratante() {
		return anunLicitacionPerfilContratante;
	}
	public void setAnunLicitacionPerfilContratante(Calendar anunLicitacionPerfilContratante) {
		this.anunLicitacionPerfilContratante = anunLicitacionPerfilContratante;
	}
	public Calendar getAnunFormalizacionPerfilContratante() {
		return anunFormalizacionPerfilContratante;
	}
	public void setAnunFormalizacionPerfilContratante(Calendar anunFormalizacionPerfilContratante) {
		this.anunFormalizacionPerfilContratante = anunFormalizacionPerfilContratante;
	}
	public Calendar getAnunAdjudicacionPerfilContratante() {
		return anunAdjudicacionPerfilContratante;
	}
	public void setAnunAdjudicacionPerfilContratante(Calendar anunAdjudicacionPerfilContratante) {
		this.anunAdjudicacionPerfilContratante = anunAdjudicacionPerfilContratante;
	}
	public Campo getContratoSujetoRegArmon() {
		return contratoSujetoRegArmon;
	}
	public void setContratoSujetoRegArmon(Campo contratoSujetoRegArmon) {
		this.contratoSujetoRegArmon = contratoSujetoRegArmon;
	}
	public Boolean getAdjudicatarioPYME() {
		return adjudicatarioPYME;
	}
	public void setAdjudicatarioPYME(Boolean adjudicatarioPYME) {
		this.adjudicatarioPYME = adjudicatarioPYME;
	}	

}
