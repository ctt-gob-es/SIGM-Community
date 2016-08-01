package aww.sigem.expropiaciones.catastro.entidades;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.rule.xmlcatastro.ComprobarXMLCatastroRule;

public class Expropiado {
	
	public static final Logger logger = Logger.getLogger(Expropiado.class);
	
	private static final String REGEX_C_POSTAL="([a-z\\s+A-Z]+)(\\d+)-([a-z\\s+A-Z]+)";
	private static final String REGEX_C_POSTAL_2="(\\d+)\\s+([a-z\\s+A-Z]+)-.([a-z\\s+A-Z]+).";
	private static final String REGEX_C_POSTAL_3="(\\d+)\\s+(.+)+(\\(.+\\)+)";
	private static final String REGEX_C_POSTAL_4="(\\d+).*?(\\(.*\\))";
	private static final String REGEX_CODIGO_POSTAL="(\\d+)";
	private String ndoc; 
	/**
	 * NIF
	 */
	private static final String NDOC ="NIF";
	private String nombre;
	/**
	 * APN
	 */
	private static final String NOMBRE="APN";
	private String  dirnot;
	/**
	 * DFT1
	 */
	private static final String DIRNOT = "DFT1";
	private String c_postal; 
	/**
	 * DFT2
	 */
	private static final String C_POSTAL="DFT2";
	private String localidad; 
	/**
	 * DFT2
	 */
	private static final String LOCALIDAD = "DFT2";
	private String caut; 
	/**
	 * DFT2
	 */
	private static final String CAUT = "DFT2";
	
	/**
	 * PDE
	 */
	private static final String PORCENTAJE = "PDE";
	private String pde;
	
	public void setValue(String tagName, String value){
		if(tagName!=null&&tagName.equals(NDOC)){
			this.ndoc=value;
		}else if(tagName!=null&&tagName.equals(NOMBRE)){
			this.nombre=value;
		}else if(tagName!=null&&tagName.equals(DIRNOT)){
			this.dirnot=value;
		}else if(tagName!=null&&tagName.equals(C_POSTAL)){
			setRegExpValues(value);
		}else if(tagName!=null&&tagName.equals(LOCALIDAD)){
			this.localidad=value; //Expresión regular
		}else if(tagName!=null&&tagName.equals(CAUT)){
			this.caut = value; //Expresión regular
		}else if (tagName!=null&&tagName.equals(PORCENTAJE)){
			this.pde = value; // Se añade el porcentaje de propiedad
		}
		
	}
	
	
	public String getPde() {
		return pde;
	}


	public void setPde(String pde) {
		this.pde = pde;
	}


	private void setRegExpValues(String values){
		values = values.replaceAll("  ", "-");
		Pattern pattern = Pattern.compile(REGEX_C_POSTAL);
		Matcher match = pattern.matcher(values);
		try{
		match.find();
			for(int i = 1;i<=match.groupCount();i++){
				if(i==1){
					this.localidad=match.group(i);
				}else
				if(i==2){
					this.c_postal=match.group(i);
				}else
				if(i==3){
					this.caut=match.group(i);
				}
				
			}
		
			if(c_postal!=null&&c_postal.length()>10){
				c_postal=c_postal.substring(0,9);
			}
		
		}catch(Exception exc){
				
			try{
			pattern = Pattern.compile(REGEX_C_POSTAL_2);
				match = pattern.matcher(values);
				match.find();
				for(int i = 1;i<=match.groupCount();i++){
					if(i==2){
						this.localidad=match.group(i);
					}else
					if(i==1){
						this.c_postal=match.group(i);
					}else
					if(i==3){
						this.caut=match.group(i);
					}
					
				}
				if(c_postal!=null&&c_postal.length()>10){
					c_postal=c_postal.substring(0,9);
				}
			}catch(Exception exc1){
				
				try{
				pattern = Pattern.compile(REGEX_C_POSTAL_3);
					match = pattern.matcher(values);
					match.find();				
					for(int i = 1;i<=match.groupCount();i++){
					
						if(i==2){
							this.localidad=match.group(i);
						}else
						if(i==1){
							this.c_postal=match.group(i);
						}else
						if(i==3){
							this.caut=match.group(i);
						}
						
					}
					if(c_postal!=null&&c_postal.length()>10){
						c_postal=c_postal.substring(0,9);
					}
					if(caut!=null&&caut.indexOf('(')!=-1){
						caut=caut.substring(caut.indexOf('(')+1,caut.indexOf(')'));
					}
					
				}catch(Exception exc2){
					
					try{
					pattern = Pattern.compile(REGEX_C_POSTAL_4);
						match = pattern.matcher(values);
						match.find();				
						for(int i = 1;i<=match.groupCount();i++){
						
							if(i==2){
								this.localidad=match.group(i);
							}else
							if(i==1){
								this.c_postal=match.group(i);
							}else
							if(i==3){
								this.caut=match.group(i);
							}
							
						}
						if(c_postal!=null&&c_postal.length()>10){
							c_postal=c_postal.substring(0,9);
						}
						if(caut!=null&&caut.indexOf('(')!=-1){
							caut=caut.substring(caut.indexOf('(')+1,caut.indexOf(')'));
						}
						
				}catch(Exception exc3){
					logger.error("Imposible parsear c_postal,localidad y provincia. -> " + values);
					this.localidad=values;
					try{
						pattern = Pattern.compile(REGEX_CODIGO_POSTAL);
						match = pattern.matcher(values);
						match.find();
						for(int i = 1;i<=match.groupCount();i++){
								this.c_postal=match.group(i);
						}
						if(c_postal!=null&&c_postal.length()>10){
							c_postal=c_postal.substring(0,9);
						}
						//System.out.println("c_postal " + c_postal);
						logger.error("localidad " + localidad);
						
				}catch(Exception exc4){
					pattern = Pattern.compile(REGEX_CODIGO_POSTAL);
					
				}
			
			}
		}
			
		}
		} 
	}
	public String getNdoc() {
		return ndoc;
	}
	public void setNdoc(String ndoc) {
		this.ndoc = ndoc;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDirnot() {
		return dirnot;
	}
	public void setDirnot(String dirnot) {
		this.dirnot = dirnot;
	}
	public String getC_postal() {
		return c_postal;
	}
	public void setC_postal(String c_postal) {
		this.c_postal = c_postal;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getCaut() {
		return caut;
	}
	public void setCaut(String caut) {
		this.caut = caut;
	}

	

}
