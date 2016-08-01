package aww.sigem.expropiaciones.catastro.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import aww.sigem.expropiaciones.catastro.entidades.CargaInicial;
import aww.sigem.expropiaciones.catastro.entidades.ErrorGeneral;
import aww.sigem.expropiaciones.catastro.entidades.Expropiacion;
import aww.sigem.expropiaciones.catastro.entidades.Expropiado;
import aww.sigem.expropiaciones.catastro.entidades.Finca;
import aww.sigem.expropiaciones.catastro.entidades.Subparcela;


public class ExprParser extends DefaultHandler{
		Expropiacion expropiacion = new Expropiacion();
		List<Finca> listaFincas = new ArrayList<Finca>();
		boolean bexpropiado = false;
		boolean bfinca =false;
		boolean bsubparcela=false;
		public Properties props;
		public String tagActual;
		public String contenido;
	    int tagCount = 0;
	    Expropiado expropiado = null;
	    Finca finca = null;
	    Subparcela subparcela = null;
	    boolean errorFinca = false;
	    boolean datosErrorFinca = false;
	    ErrorGeneral errorGeneralFinca = null;
	    
	    List <CargaInicial> errores = new ArrayList<CargaInicial>();
	    CargaInicial datosInicial = null;
	    boolean error = false;
	    boolean datosError = false;
	    ErrorGeneral errorGeneral = null;
	    
	    boolean blista = false;
	    Subparcela subMayor;
	    boolean bpropietario = false;
	    
	    String valorSuperficie = "";
	    
        public void startElement(String uri, String localName, String rawName, Attributes attributes){
        	//System.out.println(rawName);
        	if(rawName.equals("TIT")){
    		  bexpropiado = true;
    		  expropiado = new Expropiado();
    	  }        	
    	  if(rawName.equals("BIE")){
    		  bfinca=true;  
    		  finca = new Finca();
    		  finca.setDatosInicial(datosInicial);
    	  }
    	  if(rawName.equals("SPA")){
    		  bsubparcela=true;
    		  subparcela = new Subparcela();
    	  }
    	  if(rawName.equals("ERR")){
    		  error = true;
    		  errorGeneral = new ErrorGeneral();
    	  }
    	  if(bfinca && rawName.equals("LER2")){
    		  errorFinca = true;
    		  errorGeneralFinca = new ErrorGeneral();
    	  }
    	  if(rawName.equals("DSA")){
    		  datosInicial = new CargaInicial(); 
    	  }
    	  tagActual = rawName;
        }
		
			public void endElement(String uri, String localName, String name)
					throws SAXException {
				//System.out.println("/"+name);				
				if(name.equals("TIT")){
					if(finca!=null&&expropiado!=null){					  
						finca.addExpropiado(expropiado);
					}
					expropiado = null;
					bexpropiado=false;
				}else
				if(name.equals("BIE")){					
					if(finca!=null&&finca.getNum_poligono()==null||(finca.getNum_poligono().length()==0)){
						if(finca.getAprovechamiento()!=null&&finca.getAprovechamiento().equals("UR")){
							finca.setNum_poligono("Urbana");
							finca.setNum_parcela(finca.getLocalizacion_finca());
						}
					}
					if(!comprobarBienesFincaRepetidos()){
						listaFincas.add(finca);
					}
					
					
					bfinca = false;
				}else
				if(name.equals("SPA")){
					if(finca!=null&&subparcela!=null){
						finca.addSubparcela(subparcela);
					}
					subparcela=null;
					bsubparcela=false;					
				}else
				if(name.equals("LSU")){
					if (blista){						
						subMayor = finca.getParcelaMayoritaria();	
						if (subMayor != null)
							finca.setAprovechamiento(finca.getAprovechamiento() +" - "+subMayor.getCultivo());
						else
							finca.setAprovechamiento("");
					}					
					blista = false;
				}

				
				if(name.equals("ERR")){
					error = false;
					
					if(errorGeneral!=null && datosError){	
						datosInicial.setError(errorGeneral);
						errorGeneral = null;
						errores.add(datosInicial);
						datosError = false;
					}
				}
				if(name.equals("LER2")){
					errorFinca = false;
					if(errorGeneralFinca!=null && datosErrorFinca){
						finca.setError(errorGeneralFinca);
						errorGeneralFinca = null;
						datosErrorFinca = false;
					}
				}

				
			}
			
			private boolean comprobarBienesFincaRepetidos() {
				Iterator<Finca> itFinchas = listaFincas.iterator();
				boolean encontrado = false;
				if(itFinchas.hasNext()){
					while(itFinchas.hasNext() && !encontrado){
						Finca fincaList = itFinchas.next();
						String parcelaCatastral = fincaList.getParcela_catastral();
						String numero_cargo = fincaList.getNumero_cargo();
						String aprov = fincaList.getAprovechamiento();
						if(parcelaCatastral!=null){
							if(finca!=null){							
								if(finca.getParcela_catastral()!=null && parcelaCatastral.equals(finca.getParcela_catastral())){
									if(finca.getNumero_cargo()!=null && !numero_cargo.equals(finca.getNumero_cargo())){
										if(aprov.contains("RU - ")){
											encontrado = true;
										}
										else{
											if(finca.getAprovechamiento()!=null && finca.getAprovechamiento().contains("RU - ")){
												//Sustituyo datos
												copiaDatosFinca(fincaList);
											}
										}
									}
									
								}
							}
						}
					}
				}
				return encontrado;
			}
			private void copiaDatosFinca(Finca fincaList) {
				fincaList.setAno_catastral(finca.getAno_catastral());		
				fincaList.setAprovechamiento(finca.getAprovechamiento());
				fincaList.setCaracter_control1(finca.getCaracter_control1());
				fincaList.setCaracter_control2(finca.getCaracter_control2());
				fincaList.setClase_finca(finca.getClase_finca());
				fincaList.setCod_mun(finca.getCod_mun());
				fincaList.setCoeficiente_inmueble(finca.getCoeficiente_inmueble());
				fincaList.setDatosInicial(finca.getDatosInicial());
				fincaList.setDelegacion_meh(finca.getDelegacion_meh());
				fincaList.setDomicilio_tributario(finca.getDomicilio_tributario());
				fincaList.setElementos_constructivos(finca.getElementos_constructivos());
				fincaList.setError(finca.getError());
				fincaList.setListaExpropiados(finca.getListaExpropiados());
				fincaList.setListaSubparcelas(finca.getListaSubparcelas());
				fincaList.setLocalizacion_finca(finca.getLocalizacion_finca());
				fincaList.setMunicipio(finca.getMunicipio());
				fincaList.setNaturaleza_inmueble(finca.getNaturaleza_inmueble());
				fincaList.setNum_finca(finca.getNum_finca());
				fincaList.setNum_parcela(finca.getNum_parcela());
				fincaList.setNum_poligono(finca.getNum_poligono());
				fincaList.setNumero_cargo(finca.getNumero_cargo());
				fincaList.setNumexp(finca.getNumexp());
				fincaList.setNumFinca(finca.getNumFinca());
				fincaList.setParcela_catastral(finca.getParcela_catastral());
				fincaList.setProvincia(finca.getProvincia());
				fincaList.setSubparcelas(finca.getSubparcelas());
				fincaList.setSuperficie_construida(finca.getSuperficie_construida());
				fincaList.setSuperficie_construida2(finca.getSuperficie_construida2());
				fincaList.setSuperficieExpr(finca.getSuperficieExpr());
				fincaList.setUso_inmueble(finca.getUso_inmueble());
				fincaList.setValor_catastral(finca.getValor_catastral());
				fincaList.setValor_catastral_construccion(finca.getValor_catastral_construccion());
				fincaList.setValor_catastral_suelo(finca.getValor_catastral_suelo());
			}

			public void characters(char[] ch, int start, int length)
					throws SAXException {
				
		      	contenido = new String(ch, start, length).trim();
		      	//System.out.println(contenido);
		      	// Si la etiqueta es LSU y es Rural, hay al menos una subparcela.
		      	
		      	if(!bfinca){
		      		if(tagActual.equals("RC") || tagActual.equals("PROV") || tagActual.equals("MUN") || tagActual.equals("POL") || tagActual.equals("PAR")){
		      			if(!contenido.equals("")){
		      				this.datosInicial.setValue(tagActual, contenido);
		      			}
		      		}
		      		if(error){
		      			if(tagActual.equals("COD") || tagActual.equals("DES")){
		      				if(!contenido.equals("")){
			      				this.datosError = true;
			      				this.errorGeneral.setValue(tagActual, contenido);
		      				}
			      		}
		      		}
		      		
		      	}
		    	if (tagActual.equals("LSU")&&finca.getAprovechamiento().equals("RU")){
		      		blista = true;
		    	}
		      	
		    	if (tagActual.equals("DER")&&contenido.equals("PR")){
		    		bpropietario = true;
		    	}
		    	
		      	if(bexpropiado){
		      		if(!contenido.equals("")){
		      			expropiado.setValue(tagActual, contenido);
		      		}
		      	}
		      	if(bfinca){
		      		if(!contenido.equals("")){
			      		finca.setValue(tagActual, contenido);
			      		if(errorFinca){
			      			if(tagActual.equals("COD") || tagActual.equals("DES")){
			      				this.datosErrorFinca = true;
			      				this.errorGeneralFinca.setValue(tagActual, contenido);
				      		}
			      		}
		      		}
		      	}
		      	if(bsubparcela){
		      		if(!contenido.equals("")){
		      			subparcela.setValue(tagActual,contenido);
		      		}
		      	}
			}
		/**
		 * Almacenar todos los objetos en Sigem utilizando el api. 
		 */
	      public void endDocument()
	      {
	      }
		/**
		 * @return the listaFincas
		 */
		public List<Finca> getListaFincas() {
			return listaFincas;
		}
		
		public List<CargaInicial> getErrores(){
			return errores;
		}

		/**
		 * @param listaFincas the listaFincas to set
		 */
		public void setListaFincas(List<Finca> listaFincas) {
			this.listaFincas = listaFincas;
		}

}
