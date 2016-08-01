package aww.sigem.expropiaciones.catastro.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.rule.test.ExpropiacionesIniciarProcedimientoTestRule;


public class Finca {
	
	 
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(Finca.class);

	private Integer numFinca;
	
	private List listaExpropiados = new ArrayList();
	private List listaSubparcelas = new ArrayList();
	
	private ErrorGeneral error = null;
	
	public void addSubparcela(Subparcela subparcela){
		if(!listaSubparcelas.contains(subparcela)){
			listaSubparcelas.add(subparcela);
		}
	}
	public void addExpropiado (Expropiado expropiado){
		if(!listaExpropiados.contains(expropiado)){
			listaExpropiados.add(expropiado);
		}
	}
	/**
	 * Datos iniciales
	 * **/
	private CargaInicial datosInicial = null;
	/**
	 * Superficie a expropiar
	 * **/
	private String superficieExpr = ""; 
	
	/**
	 * CAMPOS XML DE CATASTRO
	 */
	
	private String numexp; 
	private String num_finca;
	private String aprovechamiento; 
	/**
	 * TIP
	 */
	public static final String APROVECHAMIENTO="TIP"; 
	private String sup_parcela; 
	/**
	 * SUPF
	 */
	public static final String SUP_PARCELA="SUPF";
	private String num_parcela;
	/**
	 * LFI
	 */
	public static final String NUM_PARCELA="LFI"; //Parcela catastral sacar con expresión regular
	private String num_poligono; 
	/**
	 * LFI
	 */
	public static final String NUM_POLIGONO="LFI"; //Sacar con expresión regular
	private String provincia; 
	/**
	 * LFI
	 */
	public static final String PROVINCIA="LFI"; //Entre paréntesis
	private String clase_finca; 
	/**
	 * TIF
	 */
	public static final String CLASE_FINCA = "TIF";
	private String delegacion_meh; 
	/**
	 * DEL
	 */
	public static final String DELEGACION_MEH ="DEL";
	private String cod_mun;
	/**
	 * MUN
	 */
	public static final String COD_MUN="MUN";
	private String parcela_catastral;
	/**
	 * PCA
	 */
	public static final String PARCELA_CATASTRAL="PCA";
	private String naturaleza_inmueble;
	/**
	 * TIO
	 */
	public static final String NATURALEZA_INMUEBLE="TIP";
	private String numero_cargo;
	/**
	 * CAR
	 */
	public static final String NUMERO_CARGO="CAR";
	private String caracter_control1; 
	/**
	 * CDC1
	 */
	public static final String CARACTER_CONTROL1="CDC1";
	private String caracter_control2;
	/**
	 * CDC2
	 */
	public static final String CARACTER_CONTROL2="CDC2";
	private String uso_inmueble;
	/**
	 * USO
	 */
	public static final String USO_INMUEBLE="USO";
	private String superficie_construida;
	/**
	 * SUP
	 */
	public static final String SUPERFICIE_CONSTRUIDA="SUP";
	private String domicilio_tributario; 
	/**
	 * DTR
	 */
	public static final String DOMICILIO_TRIBUTARIO="DTR";
	private String localizacion_finca;
	/**
	 * LFI
	 */
	public static final String LOCALIZACION_FINCA = "LFI"; //PARSEAR PARA RELLENAR LOS CAMPOS QUE COMPARTEN ESTE VALOR
	private String superficie_construida2; 
	/**
	 * SUCF
	 */
	public static final String SUPERFICIE_CONSTRUIDA2="SUCF";
	private String subparcelas; 
	/**
	 * LSU
	 */
	public static final String SUBPARCELAS = "LSU"; //Parsear las subparcelas y crear un campo de texto formateado.
	private String elementos_constructivos; 
	/**
	 * LEC
	 */
	public static final String ELEMENTOS_CONSTRUCTIVOS= "LEC";
	private String ano_catastral;
	/**
	 * AAC
	 */
	public static final String ANO_CATASTRAL="AAC";
	private String valor_catastral; 
	/**
	 * VCA
	 */
	public static final String VALOR_CATASTRAL="VCA";
	private String valor_catastral_suelo;
	/**
	 * VSU
	 */
	public static final String VALOR_CATASTRAL_SUELO="VSU";
	private String valor_catastral_construccion;
	/**
	 * VCO
	 */
	public static final String VALOR_CATASTRAL_CONSTRUCCION="VCO";
	private String coeficiente_inmueble; 
	/**
	 * CPA
	 */
	public static final String COEFICIENTE_INMUEBLE="CPA";
	
	
	
	/**
	 * NM
	 */
	public static final String NOMBRE_MUNICIPIO="NM";
	
	/**
	 * NPA
	 */
	public static final String NOMBRE_PARAJE="NPA";
	
	private String municipio = "";
	
	
	
	public void setValue(String tagName,String value){
		if(tagName!=null&&tagName.equals(APROVECHAMIENTO)){
			this.aprovechamiento=value;
		}else if(tagName!=null&&tagName.equals(SUP_PARCELA)){
			
			// Notación española cantidad,decimales sin separador de miles. 
			// Eliminacion del separador de miles (notacion .)		
			this.sup_parcela = (value!=null&&value.indexOf(".")!=-1)?value.replaceAll("\\.", ""):value;
			
		}else if(tagName!=null&&(
				tagName.equals(NUM_PARCELA)||
				tagName.equals(NUM_POLIGONO)||
				tagName.equals(PROVINCIA)||
				tagName.equals(LOCALIZACION_FINCA))){
			
			//Obtener el nº de poligono
			if((num_poligono!=null&&!num_poligono.equals(""))||(num_parcela!=null&&!num_parcela.equals(""))||(provincia!=null&&!provincia.equals(""))
					|| (localizacion_finca!=null&&!localizacion_finca.equals(""))){
				this.localizacion_finca +=value;
				value=localizacion_finca;
			}
			
			try{
				if(value!=null&&value.indexOf("Polígono ")!=-1){					
					this.num_poligono="";
					boolean whitespace=false;
					for(int i=value.indexOf("Polígono ")+9;!whitespace;i++){
						if(((int)value.charAt(i))!=((int)' ')){
							this.num_poligono+=new Character(value.charAt(i)).toString();							
						}else{
							whitespace=true;
						}
					}
					
				}				
			//Obtener el numero de parcela
				if(value!=null&&value.indexOf("Parcela ")!=-1){
					this.num_parcela="";
					boolean whitespace=false;
					for(int i=value.indexOf("Parcela ")+8;!whitespace;i++){
						if(((int)value.charAt(i))!=((int)' ')){
							this.num_parcela+=new Character(value.charAt(i)).toString();
						}else{
							whitespace=true;
						}
					}
					
				}	
			//Obtener municipio 
				if(value!=null&&value.indexOf("(")!=-1){
					int start=0;
					boolean isNumber=false;
					for(int i=value.indexOf("(")-1;i>=0&&!isNumber;i--){
						char caracter = value.charAt(i);
						String string = Character.toString(caracter);			
						
		 				try{
							int number = Integer.parseInt(string);
							isNumber=true;
							start = i+1;
						}catch(NumberFormatException e){
							isNumber = false;							
						}
					}
					
					// La etiqueta LFI del XML aparece despues de NM y NPA, por lo que se rellenará después que el municipio.
					this.cod_mun = value.substring(start,value.indexOf("(")).trim();
					
					if (this.municipio.equals("") && this.cod_mun!=null){
						// Si existe el punto es porque el formato es PARAJE.MUNICIPIO
						if (this.cod_mun.indexOf(".") != -1){
							if (this.cod_mun.substring(0, this.cod_mun.indexOf(".")).length() == 0) {
								this.municipio = this.cod_mun.substring(this.cod_mun.indexOf(".")+1, this.cod_mun.length());
							}else{
								this.municipio = this.cod_mun.substring(this.cod_mun.indexOf(".")+1, this.cod_mun.length()) + " - " + this.cod_mun.substring(0, this.cod_mun.indexOf("."));
							}
						}else
							this.municipio = this.cod_mun;						
					}	
					
					// Se aplica al cod_num el mismo formato que tiene municipio. 
					this.cod_mun = this.municipio;
					
				}
			//Obtener la provincia
				if(value!=null&&value.indexOf("(")!=-1&&value.indexOf(")")!=-1){
					this.provincia=value.substring(value.indexOf("(")+1,value.indexOf(")"));
					
				}
			}catch(Exception exc){
				logger.error("[ERROR] Parseando la cadena \""+value+"\""+ exc.getMessage());
			}
			this.localizacion_finca=value;
			
				
		}else if(tagName!=null&&tagName.equals(CLASE_FINCA)){
			this.clase_finca=value;
		}else if(tagName!=null&&tagName.equals(DELEGACION_MEH)){
			this.delegacion_meh=value;
		}else if(tagName!=null&&tagName.equals(COD_MUN)){
			this.cod_mun = value;
		}else if(tagName!=null&&tagName.equals(PARCELA_CATASTRAL)){
			this.parcela_catastral=value;
		}else if(tagName!=null&&tagName.equals(NATURALEZA_INMUEBLE)){
			this.naturaleza_inmueble=value;
		}else if(tagName!=null&&tagName.equals(NUMERO_CARGO)){
			this.numero_cargo=(value!=null&&value.indexOf(".")!=-1)?value.replaceAll("\\.", ""):value;
			//this.numero_cargo=(value!=null&&value.indexOf(",")!=-1)?value.replaceAll(",", "."):value;
		}else if(tagName!=null&&tagName.equals(CARACTER_CONTROL1)){
			this.caracter_control1=value;
		}else if(tagName!=null&&tagName.equals(CARACTER_CONTROL2)){
			this.caracter_control2=value;
		}else if(tagName!=null&&tagName.equals(USO_INMUEBLE)){
			this.uso_inmueble=value;
		}else if(tagName!=null&&tagName.equals(SUPERFICIE_CONSTRUIDA)){
			this.superficie_construida=(value!=null&&value.indexOf(".")!=-1)?value.replaceAll("\\.", ""):value;
		}else if(tagName!=null&&tagName.equals(DOMICILIO_TRIBUTARIO)){
			this.domicilio_tributario=value;
		}else if(tagName!=null&&tagName.equals(LOCALIZACION_FINCA)){
			this.localizacion_finca=value;
		}else if(tagName!=null&&tagName.equals(SUPERFICIE_CONSTRUIDA2)){
			this.superficie_construida2 = (value!=null&&value.indexOf(".")!=-1)?value.replaceAll("\\.", ""):value;
		}else if(tagName!=null&&tagName.equals(SUBPARCELAS)){
			//FORMATEAR EL VALOR DE LAS SUBPARCELAS
			this.subparcelas=value;//formatear value;
		}else if(tagName!=null&&tagName.equals(ELEMENTOS_CONSTRUCTIVOS)){
			this.elementos_constructivos=value;
		}else if(tagName!=null&&tagName.equals(ANO_CATASTRAL)){
			this.ano_catastral=value;
		}else if(tagName!=null&&tagName.equals(VALOR_CATASTRAL)){
			this.valor_catastral = (value!=null&&value.indexOf(".")!=-1)?value.replaceAll("\\.", ""):value;
		}else if(tagName!=null&&tagName.equals(VALOR_CATASTRAL_SUELO)){
			this.valor_catastral_suelo=(value!=null&&value.indexOf(".")!=-1)?value.replaceAll("\\.", ""):value;
		}else if(tagName!=null&&tagName.equals(VALOR_CATASTRAL_CONSTRUCCION)){
			this.valor_catastral_construccion=value;
		}else if(tagName!=null&&tagName.equals(COEFICIENTE_INMUEBLE)){
			this.coeficiente_inmueble=value;
		} // El nombre del municipio se encuentra en el XML  antes que el del paraje.
		else if(tagName!=null&&tagName.equals(NOMBRE_MUNICIPIO)){
			this.municipio+=value + " ";
		}else if(tagName!=null&&tagName.equals(NOMBRE_PARAJE)){
			this.municipio+="- " +value;
		}
	}
	
	public Integer getNumFinca() {
		return numFinca;
	}
	public void setNumFinca(Integer numFinca) {
		this.numFinca = numFinca;
	}
	public List getListaExpropiados() {
		return listaExpropiados;
	}
	public void setListaExpropiados(List listaExpropiados) {
		this.listaExpropiados = listaExpropiados;
	}
	public List getListaSubparcelas() {
		return listaSubparcelas;
	}
	public void setListaSubparcelas(List listaSubparcelas) {
		this.listaSubparcelas = listaSubparcelas;
	}
	public String getNumexp() {
		return numexp;
	}
	public void setNumexp(String numexp) {
		this.numexp = numexp;
	}
	public String getNum_finca() {
		return num_finca;
	}
	public void setNum_finca(String num_finca) {
		this.num_finca = num_finca;
	}
	public String getAprovechamiento() {
		return aprovechamiento;
	}
	public void setAprovechamiento(String aprovechamiento) {
		this.aprovechamiento = aprovechamiento;
	}
	public String getSup_parcela() {
		return sup_parcela;
	}
	public void setSup_parcela(String sup_parcela) {
		this.sup_parcela = sup_parcela;
	}
	public String getNum_parcela() {
		return num_parcela;
	}
	public void setNum_parcela(String num_parcela) {
		this.num_parcela = num_parcela;
	}
	public String getNum_poligono() {
		return num_poligono;
	}
	public void setNum_poligono(String num_poligono) {
		this.num_poligono = num_poligono;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getClase_finca() {
		return clase_finca;
	}
	public void setClase_finca(String clase_finca) {
		this.clase_finca = clase_finca;
	}
	public String getDelegacion_meh() {
		return delegacion_meh;
	}
	public void setDelegacion_meh(String delegacion_meh) {
		this.delegacion_meh = delegacion_meh;
	}
	public String getCod_mun() {
		return cod_mun;
	}
	public void setCod_mun(String cod_mun) {
		this.cod_mun = cod_mun;
	}
	public String getParcela_catastral() {
		return parcela_catastral;
	}
	public void setParcela_catastral(String parcela_catastral) {
		this.parcela_catastral = parcela_catastral;
	}
	public String getNaturaleza_inmueble() {
		return naturaleza_inmueble;
	}
	public void setNaturaleza_inmueble(String naturaleza_inmueble) {
		this.naturaleza_inmueble = naturaleza_inmueble;
	}
	public String getNumero_cargo() {
		return numero_cargo;
	}
	public void setNumero_cargo(String numero_cargo) {
		this.numero_cargo = numero_cargo;
	}
	public String getCaracter_control1() {
		return caracter_control1;
	}
	public void setCaracter_control1(String caracter_control1) {
		this.caracter_control1 = caracter_control1;
	}
	public String getCaracter_control2() {
		return caracter_control2;
	}
	public void setCaracter_control2(String caracter_control2) {
		this.caracter_control2 = caracter_control2;
	}
	public String getUso_inmueble() {
		return uso_inmueble;
	}
	public void setUso_inmueble(String uso_inmueble) {
		this.uso_inmueble = uso_inmueble;
	}
	public String getSuperficie_construida() {
		return superficie_construida;
	}
	public void setSuperficie_construida(String superficie_construida) {
		this.superficie_construida = superficie_construida;
	}
	public String getDomicilio_tributario() {
		return domicilio_tributario;
	}
	public void setDomicilio_tributario(String domicilio_tributario) {
		this.domicilio_tributario = domicilio_tributario;
	}
	public String getLocalizacion_finca() {
		return localizacion_finca;
	}
	public void setLocalizacion_finca(String localizacion_finca) {
		this.localizacion_finca = localizacion_finca;
	}
	public String getSuperficie_construida2() {
		return superficie_construida2;
	}
	public void setSuperficie_construida2(String superficie_construida2) {
		this.superficie_construida2 = superficie_construida2;
	}
	public String getSubparcelas() {
 
		if(listaSubparcelas!=null&&listaSubparcelas.size()>0){
			Iterator it = listaSubparcelas.iterator();
			subparcelas = "";
			while(it.hasNext()){
				Subparcela subparcela = (Subparcela) it.next();
				subparcelas+=subparcela.toString();
			}
		}
		
		return subparcelas;
	}
	public void setSubparcelas(String subparcelas) {
		this.subparcelas = subparcelas;
	}
	public String getElementos_constructivos() {
		return elementos_constructivos;
	}
	public void setElementos_constructivos(String elementos_constructivos) {
		this.elementos_constructivos = elementos_constructivos;
	}
	public String getAno_catastral() {
		return ano_catastral;
	}
	public void setAno_catastral(String ano_catastral) {
		this.ano_catastral = ano_catastral;
	}
	public String getValor_catastral() {
		return valor_catastral;
	}
	public void setValor_catastral(String valor_catastral) {
		this.valor_catastral = valor_catastral;
	}
	public String getValor_catastral_suelo() {
		return valor_catastral_suelo;
	}
	public void setValor_catastral_suelo(String valor_catastral_suelo) {
		this.valor_catastral_suelo = valor_catastral_suelo;
	}
	public String getValor_catastral_construccion() {
		return valor_catastral_construccion;
	}
	public void setValor_catastral_construccion(String valor_catastral_construccion) {
		this.valor_catastral_construccion = valor_catastral_construccion;
	}
	public String getCoeficiente_inmueble() {
		return coeficiente_inmueble;
	}
	public void setCoeficiente_inmueble(String coeficiente_inmueble) {
		this.coeficiente_inmueble = coeficiente_inmueble;
	}

	
	// Formato del campo municipio Nombre_Municipio - Nombre_Paraje
	public String getMunicipio() {
		if (this.municipio.length() == 0)
			return "Desconocido";
		
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	
	// Método que extrae la subparcela de mayor superficie.
	public Subparcela getParcelaMayoritaria(){
		
		int indice=0;
		if ( this.getListaSubparcelas().size() > 0){
			Subparcela subparcela = (Subparcela) this.getListaSubparcelas().get(indice);
			Subparcela subparcelasig;		
			
			while (indice<this.getListaSubparcelas().size()-1){
				indice++;
				if (this.getListaSubparcelas().get(indice)!=null){
					subparcelasig = (Subparcela) this.getListaSubparcelas().get(indice);
					if (subparcela.getSuperficie_subparcela().compareTo(subparcelasig.getSuperficie_subparcela())<0){
						subparcela = subparcelasig;
					}
				}			
			}
			return subparcela;
		}
		return null;
	}
	public ErrorGeneral getError() {
		return error;
	}
	public void setError(ErrorGeneral error) {
		this.error = error;
	}
	public String getSuperficieExpr() {
		return superficieExpr;
	}
	public void setSuperficieExpr(String superficieExpr) {
		this.superficieExpr = superficieExpr;
	}
	public CargaInicial getDatosInicial() {
		return datosInicial;
	}
	public void setDatosInicial(CargaInicial datosInicial) {
		this.datosInicial = datosInicial;
	}
}
