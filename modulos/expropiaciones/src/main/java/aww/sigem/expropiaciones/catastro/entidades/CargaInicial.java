package aww.sigem.expropiaciones.catastro.entidades;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CargaInicial {

	/**
	 * RC
	 */
	private static final String RC ="RC";
	private String rc; 
	/**
	 * PROV
	 */
	private static final String PROV="PROV";
	private String provincia;
	/**
	 * MUN
	 */
	private static final String MUN = "MUN";
	private String  municipio;
	/**
	 * POL
	 */
	private static final String POL="POL";
	private String poligono; 
	/**
	 * PAR
	 */
	private static final String PAR = "PAR";
	private String parcela;	
	/**
	 * ERR
	 */
	private ErrorGeneral error = null;
	

	
	public void setValue(String tagName, String value){
		if(tagName!=null&&tagName.equals(RC)){
			this.rc=value;
		}else if(tagName!=null&&tagName.equals(PROV)){
			this.provincia=value;
		}else if(tagName!=null&&tagName.equals(MUN)){
			this.municipio=value;
		}else if(tagName!=null&&tagName.equals(POL)){
			this.poligono=value;
		}else if(tagName!=null&&tagName.equals(PAR)){
			this.parcela=value; //Expresión regular
		}
		
	}



	public String getRc() {
		return rc;
	}



	public void setRc(String rc) {
		this.rc = rc;
	}



	public String getProvincia() {
		return provincia;
	}



	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}



	public String getMunicipio() {
		return municipio;
	}



	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}



	public String getPoligono() {
		return poligono;
	}



	public void setPoligono(String poligono) {
		this.poligono = poligono;
	}



	public String getParcela() {
		return parcela;
	}



	public void setParcela(String parcela) {
		this.parcela = parcela;
	}



	public ErrorGeneral getError() {
		return error;
	}



	public void setError(ErrorGeneral error) {
		this.error = error;
	}

	
}
