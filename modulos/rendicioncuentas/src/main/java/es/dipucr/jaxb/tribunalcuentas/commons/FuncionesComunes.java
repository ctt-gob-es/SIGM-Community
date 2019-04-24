package es.dipucr.jaxb.tribunalcuentas.commons;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.objeto.sw.AplicacionPresupuestaria;
import es.dipucr.contratacion.objeto.sw.Campo;
import es.dipucr.contratacion.objeto.sw.DatosContrato;
import es.dipucr.contratacion.objeto.sw.DatosEmpresa;
import es.dipucr.contratacion.objeto.sw.DatosLicitacion;
import es.dipucr.contratacion.objeto.sw.DatosTramitacion;
import es.dipucr.contratacion.objeto.sw.DiariosOficiales;
import es.dipucr.contratacion.objeto.sw.LicitadorBean;
import es.dipucr.contratacion.objeto.sw.Lotes;
import es.dipucr.contratacion.objeto.sw.Peticion;
import es.dipucr.jaxb.juntaconsultiva.commons.Constantes.PROC_ADJUDICACION;
import es.dipucr.jaxb.juntaconsultiva.commons.Constantes.TRAMITE;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.Cabecera.Usuario;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento.OrganoContratante.Contrato.Anualidad;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento.OrganoContratante.Contrato.CPV;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento.OrganoContratante.Contrato.ClasificacionExigida;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Contratista;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Modificacion;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Publicidad;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Publicidad.PublicidadAdjudicacion;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Publicidad.PublicidadFormalizacion;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Publicidad.PublicidadLicitacion;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.ContratoMenor;
import es.dipucr.jaxb.tribunalcuentas.beans.SistemaAdjudicacion;
import es.dipucr.jaxb.tribunalcuentas.beans.TipoContrato;
import es.dipucr.jaxb.tribunalcuentas.beans.TipoContratoMenor;
import es.dipucr.jaxb.tribunalcuentas.beans.Tramitacion;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

public class FuncionesComunes {
	
	public static final Logger LOGGER = Logger.getLogger(FuncionesComunes.class);
	
	private FuncionesComunes(){
	}

	public static String obtenerEjercicio(IRuleContext rulectx) throws ISPACRuleException {
		String ejercicio = "";
		try{
			String strQuery = "NUMEXP='" + rulectx.getNumExp() + "'";
			Iterator <IItem> it = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_RELACION_CONTRA", strQuery);
			while(it.hasNext()){
				IItem datos = it.next();
				if(null != datos.getString("EJERCICIO")){
					ejercicio = datos.getString("EJERCICIO");
				}
			}
			
		}catch (ISPACException e){
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return ejercicio;
	}
	
	public static Usuario getUsuarioContacto(IRuleContext rulectx) throws ISPACRuleException {
		Usuario usuario = new Usuario();
		try{
			String strQuery = "NUMEXP='" + rulectx.getNumExp() + "'";
			Iterator <IItem> it = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_RENDCUENT_CONTA", strQuery);
			while(it.hasNext()){
				IItem datos = it.next();
				if(null != datos.getString("NOMBRE")){
					usuario.setNombre(datos.getString("NOMBRE"));
				}
				if(null != datos.getString("APELLIDO1")){
					usuario.setApellido1(datos.getString("APELLIDO1"));
				}
				if(null != datos.getString("APELLIDO2")){
					usuario.setApellido2(datos.getString("APELLIDO2"));
				}
				if(null != datos.getString("CARGO")){
					usuario.setCargo(datos.getString("CARGO"));
				}
				if(null != datos.getString("DIRECCION")){
					usuario.setDireccion(datos.getString("DIRECCION"));
				}
				if(null != datos.getString("PROVINCIA")){
					String [] vprovincia = datos.getString("PROVINCIA").split(" - ");
					if(vprovincia.length==2){
						usuario.setProvincia(vprovincia[0]);
					}					
				}
				if(null != datos.getString("MUNICIPIO")){
					String [] vmunicipio = datos.getString("MUNICIPIO").split(" - ");
					if(vmunicipio.length==2){
						usuario.setMunicipio(vmunicipio[0]);
					}
				}
				if(null != datos.getString("CODIGOPOSTAL")){
					usuario.setCodPostal(datos.getString("CODIGOPOSTAL"));
				}
				if(null != datos.getString("TELEFONO")){
					usuario.setTelefono(datos.getString("TELEFONO"));
				}
				if(null != datos.getString("EMAIL")){
					usuario.setMail(datos.getString("EMAIL"));
				}
			}
			
		}catch (ISPACException e){
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return usuario;
	}

	/*public static Vector<String> obtenerCodProcedRendicionCuentas(IRuleContext rulectx) throws ISPACRuleException {
		Vector <String> codProc = new Vector<String>();
		try{
			
			Iterator <IItem> it = ConsultasGenericasUtil.queryEntities(rulectx, "VLDTBL_CONTR_RENDIC_CUEN", "0=0");
			while(it.hasNext()){
				IItem datos = it.next();
				codProc.add(datos.getString("VALOR"));
			}
			
		}catch (ISPACException e){
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return codProc;
	}*/
	
	public static Vector<String> obtenerCodProcedRendicionCuentas(IRuleContext rulectx) throws ISPACRuleException {
		Vector <String> codProc = new Vector<String>();
		try{
			String query = " id_padre in (SELECT id FROM spac_ct_procedimientos WHERE nombre='Procedimiento Contratación' or nombre='Procedimiento de Contratación')";
			Iterator <IItem> it = ConsultasGenericasUtil.queryEntities(rulectx.getClientContext(), "SPAC_CT_PROCEDIMIENTOS", query);
			while(it.hasNext()){
				IItem datos = it.next();
				codProc.add(datos.getString("COD_PCD"));
			}
			
		}catch (ISPACException e){
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return codProc;
	}
	
	public static ContratoMenor getContratoMenor(DatosContrato datosContrato, DatosTramitacion datosTramitacion, Peticion peticion, DiariosOficiales diariosOficiales) {
		ContratoMenor contratoMenor = null;
		if(null != datosTramitacion && null != datosContrato){	
			contratoMenor = new ContratoMenor();
			
			//Contrato menor
			contratoMenor.setLey("2");		
			
			//Numero de referencia
			contratoMenor.setReferencia(datosContrato.getNumContrato());
			
			contratoMenor.setTipoContrato(obtenerTipoContratoMenor(datosContrato.getTipoContrato()));
			
			contratoMenor.setObjeto(remove(datosContrato.getObjetoContrato()));
			
			if(null != datosTramitacion.getDuracionContrato() && null != datosTramitacion.getDuracionContrato().getDuracion()){
				String duracion = datosTramitacion.getDuracionContrato().getDuracion();
				String[] vDuracion = duracion.split("\\.");
				if(vDuracion.length==1){
					contratoMenor.setDuracion(new BigDecimal(duracion+".0"));
				} else{
					contratoMenor.setDuracion(new BigDecimal(duracion));
				}
			}
			else{
				contratoMenor.setDuracion(new BigDecimal("1.0"));
			}
			
			
			//Compruebo que tenga los dos decimales
			if(null != peticion.getPresupuestoConIva()){
				String valor = peticion.getPresupuestoConIva();
				String [] vValor = valor.split("\\.");
				if(vValor.length==1){
					contratoMenor.setPrecLicitacion(new BigDecimal(valor+".00"));
				} else{
					if(vValor[1].length()==1){
						contratoMenor.setPrecLicitacion(new BigDecimal(valor+"0"));
					} else{
						if(vValor[1].length()==2){
							contratoMenor.setPrecLicitacion(new BigDecimal(valor));							
						}
						else{
							contratoMenor.setPrecLicitacion(new BigDecimal(vValor[0]+"."+vValor[1].substring(0, 1)));
						}					
					}
				}
			}
			else{
				if(null != datosTramitacion.getLicitador()[0].getImporteConImpuestos()){
					String valor = datosTramitacion.getLicitador()[0].getImporteConImpuestos();
					valor = valor.replace(",", ".");
					valor = valor.replace(" ", "");
					String [] vValor = valor.split("\\.");
					if(vValor.length==1){
						contratoMenor.setPrecLicitacion(new BigDecimal(valor+".00"));
					} else{
						if(vValor[1].length()==1){
							contratoMenor.setPrecLicitacion(new BigDecimal(valor+"0"));
						} else{
							if(vValor[1].length()==2){
								contratoMenor.setPrecLicitacion(new BigDecimal(valor));							
							}
							else{
								contratoMenor.setPrecLicitacion(new BigDecimal(vValor[0]+"."+vValor[1].substring(0, 1)));
							}
						}
					}
				}
			}
			
			if(null != datosTramitacion.getLicitador() && datosTramitacion.getLicitador().length>0){
				if(null != peticion && null!= peticion.getPresupuestoIva()){
					String valor = peticion.getPresupuestoIva();
					valor = valor.replace(",", ".");
					valor = valor.replace(" ", "");
					LOGGER.warn("IVA. "+valor);
					String [] vValor = valor.split("\\.");
					LOGGER.warn("IVA. "+vValor.length);
					if(vValor.length==1){
						contratoMenor.setIvaLicitacion(new BigDecimal(valor+".00"));
					} else{
						if(vValor[1].length()==1){
							contratoMenor.setIvaLicitacion(new BigDecimal(valor+"0"));
						} else{
							if(vValor[1].length()==2){
								contratoMenor.setIvaLicitacion(new BigDecimal(valor));							
							}
							else{
								contratoMenor.setIvaLicitacion(new BigDecimal(vValor[0]+"."+vValor[1].substring(0, 1)));
							}
						}
					}
				}
				else{
					if(null != datosTramitacion.getLicitador()[0].getImporteConImpuestos() && null != datosTramitacion.getLicitador()[0].getImporteSinImpuestos()){
						String valorConImpuestos = datosTramitacion.getLicitador()[0].getImporteConImpuestos();
						valorConImpuestos = valorConImpuestos.replace(",", ".");
						valorConImpuestos = valorConImpuestos.replace(" ", "");
						String valorSinImpuestos = datosTramitacion.getLicitador()[0].getImporteSinImpuestos();
						valorSinImpuestos = valorSinImpuestos.replace(",", ".");
						valorSinImpuestos = valorSinImpuestos.replace(" ", "");
						float decValorConImpuestos = Float.parseFloat(valorConImpuestos);
						float decValorSinImpuestos = Float.parseFloat(valorSinImpuestos);
						float total = decValorConImpuestos - decValorSinImpuestos;
						String sTotal = total+"";
						LOGGER.warn("IVA. "+sTotal);
						String [] vValor = sTotal.split("\\.");
						LOGGER.warn("IVA. "+vValor.length);
						if(vValor.length==1){
							contratoMenor.setIvaLicitacion(new BigDecimal(sTotal+".00"));
						} else{
							if(vValor[1].length()==1){
								contratoMenor.setIvaLicitacion(new BigDecimal(sTotal+"0"));
							} else{
								if(vValor[1].length()==2){
									contratoMenor.setIvaLicitacion(new BigDecimal(sTotal));							
								}
								else{
									contratoMenor.setIvaLicitacion(new BigDecimal(vValor[0]+"."+vValor[1].substring(0, 1)));
								}
							}
						}
					}
				}
				
				if(null != datosTramitacion.getLicitador()[0].getImporteConImpuestos()){
					String valor = datosTramitacion.getLicitador()[0].getImporteConImpuestos();
					valor = valor.replace(",", ".");
					valor = valor.replace(" ", "");
					String [] vValor = valor.split("\\.");
					if(vValor.length==1){
						contratoMenor.setPrecAdjudicacion(new BigDecimal(valor+".00"));
					} else{
						if(vValor[1].length()==1){
							contratoMenor.setPrecAdjudicacion(new BigDecimal(valor+"0"));
						} else{
							if(vValor[1].length()==2){
								contratoMenor.setPrecAdjudicacion(new BigDecimal(valor));							
							}
							else{
								contratoMenor.setPrecAdjudicacion(new BigDecimal(vValor[0]+"."+vValor[1].substring(0, 1)));
							}
						}
					}
				}
				if(null != datosTramitacion.getLicitador()[0].getImporteConImpuestos() && null != datosTramitacion.getLicitador()[0].getImporteSinImpuestos()){
					String valorConImpuestos = datosTramitacion.getLicitador()[0].getImporteConImpuestos();
					valorConImpuestos = valorConImpuestos.replace(",", ".");
					valorConImpuestos = valorConImpuestos.replace(" ", "");
					String valorSinImpuestos = datosTramitacion.getLicitador()[0].getImporteSinImpuestos();
					valorSinImpuestos = valorSinImpuestos.replace(",", ".");
					valorSinImpuestos = valorSinImpuestos.replace(" ", "");
					float decValorConImpuestos = Float.parseFloat(valorConImpuestos);
					float decValorSinImpuestos = Float.parseFloat(valorSinImpuestos);
					float total = decValorConImpuestos - decValorSinImpuestos;
					String sTotal = total+"";
					LOGGER.warn("sTotal "+sTotal);
					String [] vValor = sTotal.split("\\.");
					LOGGER.warn("vValor "+vValor.length);
					if(vValor.length==1){
						contratoMenor.setIvaAdjudicacion(new BigDecimal(sTotal+".00"));
					} else{
						if(vValor[1].length()==1){
							contratoMenor.setIvaAdjudicacion(new BigDecimal(sTotal+"0"));
						} else{
							if(vValor[1].length()==2){
								contratoMenor.setIvaAdjudicacion(new BigDecimal(sTotal));							
							}
							else{
								contratoMenor.setIvaAdjudicacion(new BigDecimal(vValor[0]+"."+vValor[1].substring(0, 1)));
							}
						}
					}
				}
				//Indicador de adjudicación definitiva del contrato
				if(null != datosTramitacion.getLicitador()[0].getFechaAdjudicacion()){
					contratoMenor.setFechaAdjDef(FechasUtil.getFormattedDate(datosTramitacion.getLicitador()[0].getFechaAdjudicacion().getTime()));
				}				
			}
			
			RelacionContratos.ContratoMenor.Publicidad publicidad = new RelacionContratos.ContratoMenor.Publicidad();
			
			boolean existePublicidad = false;
			boolean existePublicidadLicitacion = false;
			boolean existePublicidadFormalizacion = false;
			RelacionContratos.ContratoMenor.Publicidad.PublicidadLicitacion publicidadLictacion = new RelacionContratos.ContratoMenor.Publicidad.PublicidadLicitacion();
		
			if(null != diariosOficiales){
				//Licitacion
				if(null != diariosOficiales.getAnuncioLicitacionBOE()){
					existePublicidadLicitacion = true;
					existePublicidad = true;
					publicidadLictacion.setFechaOtros(FechasUtil.getFormattedDate(diariosOficiales.getAnuncioLicitacionBOE().getTime()));
				}
				if(null != datosTramitacion.getFechaBOPExpCont()){
					existePublicidad = true;
					existePublicidadLicitacion = true;
					publicidadLictacion.setFechaOtros(FechasUtil.getFormattedDate(datosTramitacion.getFechaBOPExpCont().getTime()));
				}
				if(null != diariosOficiales.getAnunLicitacionPerfilContratante()){
					existePublicidad = true;
					existePublicidadLicitacion = true;
					publicidadLictacion.setFechaContEstado(FechasUtil.getFormattedDate(diariosOficiales.getAnunLicitacionPerfilContratante().getTime()));
					publicidadLictacion.setFechaPerfil(FechasUtil.getFormattedDate(diariosOficiales.getAnunLicitacionPerfilContratante().getTime()));
				}
				publicidad.setPublicidadLicitacion(publicidadLictacion);
			}
			

			if(existePublicidad){
				publicidad.setExistePublicidad(Constantes.TIPO_INFORMATIVO.SI);
			} else{
				publicidad.setExistePublicidad(Constantes.TIPO_INFORMATIVO.NO);
			}
			contratoMenor.setPublicidad(publicidad);
			
			
			LicitadorBean[] vlicitador = datosTramitacion.getLicitador();
			if(vlicitador.length>0){
				RelacionContratos.ContratoMenor.Contratista contratista = null;
				for(int i=0; i<vlicitador.length; i++){
					LicitadorBean licitador = vlicitador[i];
					contratista = new RelacionContratos.ContratoMenor.Contratista();
					contratista.setAdjudicatario(licitador.getNombre());
					contratista.setNacionalidad("ES");
					contratista.setNIF(licitador.getIdentificador());
				}
				contratoMenor.getContratista().add(contratista);
			}
			
			if(datosTramitacion.getTextoAcuerdo()==null || StringUtils.isEmpty(datosTramitacion.getTextoAcuerdo())){
				contratoMenor.setObservaciones(remove(datosContrato.getObjetoContrato()));
			} else{
				contratoMenor.setObservaciones(remove(datosTramitacion.getTextoAcuerdo()));
			}
			
		}
		return contratoMenor;
	}

	public static Contrato getContrato(DatosContrato datosContrato, DatosTramitacion datosTramitacion, DiariosOficiales diariosFechaOficiales, Lotes lotes) {
		Contrato contrato = null;
		
		if(null != datosTramitacion && null != datosContrato){	
			contrato = new Contrato();
			
			if(datosContrato.isNuevaLey())contrato.setLey("4");
			else contrato.setLey("1");
		
			
			//Numero de referencia
			contrato.setReferencia(datosContrato.getNumContrato());
			
			//Indicador de si es un contarto por lotes.
			if(lotes!=null && lotes.isTieneLotes()){
				contrato.setLotes(Constantes.TIPO_INFORMATIVO.SI);
				contrato.setNumLotes(lotes.getNumLotes());
			}
			else{
				//No tenemos contratos por lotes.
				contrato.setLotes(Constantes.TIPO_INFORMATIVO.NO);
			}
			
			
			//Indicador de si es un contrato derivado de acuerdo marco
			contrato.setAcuerdoMarco(Constantes.TIPO_INFORMATIVO.NO);
			
			//Indicador de si es un contrato derivado de sistema dinámico
			contrato.setSistemaDinamico(Constantes.TIPO_INFORMATIVO.NO);
			
			//Indicador de si es un contrato con subasta electrónica.
			contrato.setSubastaElectronica(Constantes.TIPO_INFORMATIVO.NO);
			
			//Indicador de si es un contrato complementario.
			if(null != datosContrato.getProcNegCausa()){
				String causa = datosContrato.getProcNegCausa();
				String [] vCausa = causa.split(" - ");
				if(null != vCausa && vCausa.length>0){
					String num = vCausa[0];
					//22 - 09 Causas del Procedimiento de Negociado
					if("22".equals(num) || "09".equals(num)){
						contrato.setComplementario(Constantes.TIPO_INFORMATIVO.SI);
					} else{
						contrato.setComplementario(Constantes.TIPO_INFORMATIVO.NO);
					}
				}				
			} else{
				contrato.setComplementario(Constantes.TIPO_INFORMATIVO.NO);
			}
			
			
			//Indicador si el contrato esta sujeto a regulación armonizada.
			if(datosContrato.isRegulacionArmonizada()){
				contrato.setRegArmonizada(Constantes.TIPO_INFORMATIVO.SI);
			} else{
				contrato.setRegArmonizada(Constantes.TIPO_INFORMATIVO.NO);
			}
			
			contrato.setTipoContrato(obtenerTipoContrato(datosContrato.getTipoContrato()));
			
			contrato.setObjeto(remove(datosContrato.getObjetoContrato()));
			
			contrato.setSistAdjudicacion(obtenerSistemaAdjudicacion(datosContrato.getProcedimientoContratacion(), datosContrato.getCriteriosMultiples()));
			
			contrato.setTramitacion(obtenerTramitacion(datosContrato.getTipoTramitacion()));
			
			if(datosTramitacion.isProrroga()){
				contrato.setProrroga(Constantes.TIPO_INFORMATIVO.SI);
			} else{
				contrato.setProrroga(Constantes.TIPO_INFORMATIVO.NO);
			}
			
			if(null != contrato.getTmpProrroga()){
				contrato.setTmpProrroga(contrato.getTmpProrroga());
			}			
			
			//Compruebo que tenga los dos decimales
			if(null != datosContrato.getValorEstimadoContrato()){
				String valor = datosContrato.getValorEstimadoContrato();
				String [] vValor = valor.split("\\.");
				if(vValor.length==1){
					contrato.setPrecLicitacion(new BigDecimal(valor+".00"));
				} else{
					if(vValor[1].length()==1){
						contrato.setPrecLicitacion(new BigDecimal(valor+"0"));
					} else{
						if(vValor[1].length()==2){
							contrato.setPrecLicitacion(new BigDecimal(valor));
						}
						else{
							contrato.setPrecLicitacion(new BigDecimal(vValor[0]+"."+vValor[1].substring(0, 1)));
						}
					}
				}
			}
			
			if(null != datosTramitacion.getLicitador() && datosTramitacion.getLicitador().length>0){
				if(null != datosTramitacion.getLicitador()[0].getImporteSinImpuestos()){
					String valor = datosTramitacion.getLicitador()[0].getImporteSinImpuestos();
					String [] vValor = valor.split("\\.");
					if(vValor.length==1){
						contrato.setPrecAdjudicacion(new BigDecimal(valor+".00"));
					} else{
						if(vValor[1].length()==1){
							contrato.setPrecAdjudicacion(new BigDecimal(valor+"0"));
						} else{
							if(vValor[1].length()==2){
								contrato.setPrecAdjudicacion(new BigDecimal(valor));								
							}
							else{
								contrato.setPrecAdjudicacion(new BigDecimal(vValor[0]+"."+vValor[1].substring(0, 1)));
							}							
						}
					}
				}
				
				//Indicador de adjudicación definitiva del contrato
				//contrato.setAdjDef("ADJ DEF");
				if(null != datosTramitacion.getLicitador()[0].getFechaAdjudicacion()){
					contrato.setAdjDef(Constantes.TIPO_INFORMATIVO.SI);
					if(null != datosTramitacion.getLicitador()[0].getImporteConImpuestos()){
						String valor = datosTramitacion.getLicitador()[0].getImporteConImpuestos();
						String [] vValor = valor.split("\\.");
						if(vValor.length==1){
							contrato.setIvaAdjudicacion(new BigDecimal(valor+".00"));
						} else{
							if(vValor[1].length()==1){
								contrato.setIvaAdjudicacion(new BigDecimal(valor+"0"));
							} else{
								if(vValor[1].length()==2){
									contrato.setIvaAdjudicacion(new BigDecimal(valor));							
								}
								else{
									contrato.setIvaAdjudicacion(new BigDecimal(vValor[0]+"."+vValor[1].substring(0, 1)));
								}							
							}
						}
					}
					contrato.setIngreso(Constantes.TIPO_INFORMATIVO.NO);
					contrato.setFechaAdjDef(FechasUtil.getFormattedDate(datosTramitacion.getLicitador()[0].getFechaAdjudicacion().getTime()));
				} else{
					contrato.setAdjDef(Constantes.TIPO_INFORMATIVO.NO);
				}
			}
			
			//Indicador formalizacion
			//contrato.setFormalizacion("Formalizacion");
			if(null != datosTramitacion.getFormalizacion()){
				if(null != datosTramitacion.getFormalizacion().getFechaContrato()){
					contrato.setFormalizacion(Constantes.TIPO_INFORMATIVO.SI);
					contrato.setFechaForm(FechasUtil.getFormattedDate(datosTramitacion.getFormalizacion().getFechaContrato().getTime()));
				} else{
					contrato.setFormalizacion(Constantes.TIPO_INFORMATIVO.NO);
				}
			}
			if(null != datosTramitacion.getDuracionContrato() && null != datosTramitacion.getDuracionContrato().getDuracion()){
				String duracion = datosTramitacion.getDuracionContrato().getDuracion();
				String[] vDuracion = duracion.split("\\.");
				if(vDuracion.length==1){
					contrato.setPlazoEjecucion(new BigDecimal(duracion+".0"));
				} else{
					contrato.setPlazoEjecucion(new BigDecimal(duracion));
				}
			}
			
			Publicidad publicidad = new Publicidad();
			
			boolean existePublicidad = false;
			boolean existePublicidadLicitacion = false;
			boolean existePublicidadFormalizacion = false;
			PublicidadLicitacion publicidadLictacion = new PublicidadLicitacion();
			PublicidadFormalizacion publicidadFormalizacion = new PublicidadFormalizacion();
		
			if(null != diariosFechaOficiales){
				if(null != diariosFechaOficiales.getAnunAdjudicacionPerfilContratante()){
					existePublicidad = true;
					PublicidadAdjudicacion publicidadAdjudicacion = new PublicidadAdjudicacion();
					publicidadAdjudicacion.setFechaContEstado(FechasUtil.getFormattedDate(diariosFechaOficiales.getAnunAdjudicacionPerfilContratante().getTime()));
					publicidadAdjudicacion.setFechaPerfil(FechasUtil.getFormattedDate(diariosFechaOficiales.getAnunAdjudicacionPerfilContratante().getTime()));
					publicidad.setPublicidadAdjudicacion(publicidadAdjudicacion);
				}
				//Licitacion
				if(null != diariosFechaOficiales.getAnuncioLicitacionBOE()){
					existePublicidadLicitacion = true;
					existePublicidad = true;
					publicidadLictacion.setFechaBOE(FechasUtil.getFormattedDate(diariosFechaOficiales.getAnuncioLicitacionBOE().getTime()));
				}
				if(null != datosTramitacion.getFechaBOPExpCont()){
					existePublicidad = true;
					existePublicidadLicitacion = true;
					publicidadLictacion.setFechaBOP(FechasUtil.getFormattedDate(datosTramitacion.getFechaBOPExpCont().getTime()));
				}
				if(null != diariosFechaOficiales.getAnunLicitacionPerfilContratante()){
					existePublicidad = true;
					existePublicidadLicitacion = true;
					publicidadLictacion.setFechaContEstado(FechasUtil.getFormattedDate(diariosFechaOficiales.getAnunLicitacionPerfilContratante().getTime()));
					publicidadLictacion.setFechaPerfil(FechasUtil.getFormattedDate(diariosFechaOficiales.getAnunLicitacionPerfilContratante().getTime()));
				}
				if(null != diariosFechaOficiales.getAnuncioLicitacionDOUE()){
					existePublicidad = true;
					existePublicidadLicitacion = true;
					publicidadLictacion.setFechaDOUE(FechasUtil.getFormattedDate(diariosFechaOficiales.getAnuncioLicitacionDOUE().getTime()));
				}
				//Formalizacion
				if(null != diariosFechaOficiales.getAnuncioFormalizacionBOE()){
					existePublicidadFormalizacion = true;
					existePublicidad = true;
					publicidadFormalizacion.setFechaBOE(FechasUtil.getFormattedDate(diariosFechaOficiales.getAnuncioFormalizacionBOE().getTime()));
				}
				if(null != datosTramitacion.getFechaBOPFormalizacion()){
					existePublicidadFormalizacion = true;
					existePublicidad = true;
					publicidadFormalizacion.setFechaBOP(FechasUtil.getFormattedDate(datosTramitacion.getFechaBOPFormalizacion().getTime()));
				}
				if(null != diariosFechaOficiales.getAnunFormalizacionPerfilContratante()){
					existePublicidadFormalizacion = true;
					existePublicidad = true;
					publicidadFormalizacion.setFechaContEstado(FechasUtil.getFormattedDate(diariosFechaOficiales.getAnunFormalizacionPerfilContratante().getTime()));
					publicidadFormalizacion.setFechaPerfil(FechasUtil.getFormattedDate(diariosFechaOficiales.getAnunFormalizacionPerfilContratante().getTime()));	
				}
				if(null != diariosFechaOficiales.getAnuncioFormalizacionDOUE()){
					existePublicidadFormalizacion = true;
					existePublicidad = true;
					publicidadFormalizacion.setFechaDOUE(FechasUtil.getFormattedDate(diariosFechaOficiales.getAnuncioFormalizacionDOUE().getTime()));
				}
			}
			
			if(existePublicidadLicitacion){
				publicidad.setPublicidadLicitacion(publicidadLictacion);	
			}
			if(existePublicidadFormalizacion){
				publicidad.setPublicidadFormalizacion(publicidadFormalizacion);		
			}
			if(existePublicidad){
				publicidad.setExistePublicidad(Constantes.TIPO_INFORMATIVO.SI);
			} else{
				publicidad.setExistePublicidad(Constantes.TIPO_INFORMATIVO.NO);
			}
			contrato.setPublicidad(publicidad);

			
			LicitadorBean[] vlicitador = datosTramitacion.getLicitador();
			if(vlicitador.length>0){
				Contratista contratista = null;
				for(int i=0; i<vlicitador.length; i++){
					LicitadorBean licitador = vlicitador[i];
					contratista = new Contratista();
					contratista.setAdjudicatario(licitador.getNombre());
					contratista.setNacionalidad("ES");
					contratista.setNIF(licitador.getIdentificador());
				}
				contrato.getContratista().add(contratista);
			}
			if(datosTramitacion.getTextoAcuerdo()==null || StringUtils.isEmpty(datosTramitacion.getTextoAcuerdo())){
				contrato.setObservaciones(remove(datosContrato.getObjetoContrato()));
			} else{
				contrato.setObservaciones(remove(datosTramitacion.getTextoAcuerdo()));
			}
		}
		return contrato;
	}
	
	public static String remove(String input) {
		 // Cadena de caracteres original a sustituir.
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇª\".-";
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC    ";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    output = output.replaceAll("&#8220;", "'");
	    output = output.replaceAll("&#8221;", ",");
	    return output;
	}//remove

	private static Tramitacion obtenerTramitacion(Campo campo) {
		Tramitacion tramitacion = null;
		if("Ordinaria".equals(campo.getValor())){
			tramitacion = Tramitacion.O;
		}
		if("Urgente".equals(campo.getValor())){
			tramitacion = Tramitacion.U;
		}
		if("Emergencia".equals(campo.getValor())){
			tramitacion = Tramitacion.E;
		}
		return tramitacion;
	}
	/**
	 * Sistema de Adjudicación.
				Los valores posibles son:
				A - Abierto criterio precio (solo Contrato no menor, sujeto al Real Decreto Legislativo 3/2011 (TRLCSP))
				M - Abierto criterios múltiples
				R - Restringido criterio precio (solo Contrato no menor, sujeto al Real Decreto Legislativo 3/2011 (TRLCSP))
				C - Restringido criterios múltiples
				N - Negociado con publicidad (solo Contrato no menor, sujeto al Real Decreto Legislativo 3/2011 (TRLCSP))
				S - Negociado sin publicidad
				D - Diálogo competitivo
				O - Otros
				B - Abierto único criterio (solo Contrato no menor, sujeto a la Ley 9/2017 (LCSP))
				E - Abierto simplificado (solo Contrato no menor, sujeto a la Ley 9/2017 (LCSP))
				F - Abierto simplificado sumario (solo Contrato no menor, sujeto a la Ley 9/2017 (LCSP))
				G - Concurso de proyectos (solo Contrato no menor, sujeto a la Ley 9/2017 (LCSP))
				H - Restringido único criterio (solo Contrato no menor, sujeto a la Ley 9/2017 (LCSP))
				I - Licitación con negociación (solo Contrato no menor, sujeto a la Ley 9/2017 (LCSP))
				J - Asociación para la innovación (solo Contrato no menor, sujeto a la Ley 9/2017 (LCSP))
	 * **/
	private static SistemaAdjudicacion obtenerSistemaAdjudicacion(Campo campo, boolean criterioMultiples) {
		SistemaAdjudicacion sistemaAdj = null;
		if("Abierto".equals(campo.getValor())){
			if(criterioMultiples){
				sistemaAdj = SistemaAdjudicacion.M;
			} else{
				sistemaAdj = SistemaAdjudicacion.A;
			}
		}		
		if("Negociado sin publicidad".equals(campo.getValor())){
			sistemaAdj = SistemaAdjudicacion.S;
		}
		if("Negociado con publicidad".equals(campo.getValor())){
			sistemaAdj = SistemaAdjudicacion.N;
		}
		if("Abierto simplificado".equals(campo.getValor())){
			sistemaAdj = SistemaAdjudicacion.E;
		}
		if("Restringido".equals(campo.getValor())){
			sistemaAdj = SistemaAdjudicacion.H;
		}
		if("Licitación con negociación".equals(campo.getValor())){
			sistemaAdj = SistemaAdjudicacion.I;
		}
		if("Asociación para la innovación".equals(campo.getValor())){
			sistemaAdj = SistemaAdjudicacion.J;
		}
		return sistemaAdj;
	}
	/**
	 * Tipo de Contrato.
				Los valores posibles son:
				A - Obras
				E - Servicios
				C - Suministro
				F - Contratos Administrativos Especiales
				H - Contratos Concesión Obras Públicas
				B - Contratos Gestión Servicios Públicos
				I - Contratos Privados
				J - Contratos Colaboración Sector Público y Privado
				Z - Otros
				K - Concesión de Obras (solo Contrato no menor, sujeto a la Ley 9/2017 (LCSP))
				L - Concesión de Servicios (solo Contrato no menor, sujeto a la Ley 9/2017 (LCSP))
	 * **/
	private static TipoContrato obtenerTipoContrato(Campo campo) {
		TipoContrato tipoContratoTribunal = null;
		if("Obras".equals(campo.getValor())){
			tipoContratoTribunal = TipoContrato.A;
		} else{
			if("Servicios".equals(campo.getValor())){
				tipoContratoTribunal = TipoContrato.E;
			} else{
				if("Suministros".equals(campo.getValor())){
					tipoContratoTribunal = TipoContrato.C;
				} else{
					if("Privado".equals(campo.getValor())){
						tipoContratoTribunal = TipoContrato.I;
					}else{
						if("Administrativo especial".equals(campo.getValor())){
							tipoContratoTribunal = TipoContrato.F;
						}
						else{
							if("Concesión de Obras Públicas".equals(campo.getValor())){
								tipoContratoTribunal = TipoContrato.H;
							}
							else{
								if("Gestión de Servicios Públicos".equals(campo.getValor())){
									tipoContratoTribunal = TipoContrato.B;
								}
								else{
									if("Colaboración entre el sector público y sector privado".equals(campo.getValor())){
										tipoContratoTribunal = TipoContrato.J;
									}
									else{
										if("Concesión de Obras".equals(campo.getValor())){
											tipoContratoTribunal = TipoContrato.K;
										}
										else{
											if("Concesión de Servicios".equals(campo.getValor())){
												tipoContratoTribunal = TipoContrato.L;
											}
											else{
												tipoContratoTribunal = TipoContrato.Z;
											}											
										}										
									}
									
								}								
							}
						}						
					}					
				}
			}
		}
		return tipoContratoTribunal;
	}
	/**
	 * Tipo de Contrato.
				Los valores posibles son:
				Tipo de Contrato.
				Los valores posibles son:
				A - Obras
				E - Servicios
				C - Suministro
				Z - Otros
	 * **/
	private static TipoContratoMenor obtenerTipoContratoMenor(Campo campo) {
		TipoContratoMenor tipoContratoTribunal = null;
		if("Obras".equals(campo.getValor())){
			tipoContratoTribunal = TipoContratoMenor.A;
		} else{
			if("Servicios".equals(campo.getValor())){
				tipoContratoTribunal = TipoContratoMenor.E;
			} else{
				if("Suministros".equals(campo.getValor())){
					tipoContratoTribunal = TipoContratoMenor.C;
				} else{
						tipoContratoTribunal = TipoContratoMenor.Z;
				}
			}
		}
		return tipoContratoTribunal;
	}

	public static File obtenerXML(RelacionContratos relacionContratos) throws ISPACRuleException {
		File fileXML = null;
		try{
			String fileName = FileTemporaryManager.getInstance().newFileName(".xml");
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			
			fileXML = new File(fileName);
			//JAXBContext jaxbContext = JAXBContext.newInstance(Libro.class);
			JAXBContext jaxbContext = JAXBContext.newInstance("es.dipucr.jaxb.tribunalcuentas.beans");
			Marshaller marshaller = jaxbContext.createMarshaller();
			//indicamos que queremos formateada nuestra salida (con enters y tabs)
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//indicamos el objeto que escribiremos a XML y la salida (puede ser un objeto tipo FILE)
			marshaller.marshal(relacionContratos, fileXML);
		} catch (JAXBException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		 } catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return fileXML;
	}

	public static File obtenerXML(DgpDeclaracion relacionContratos) throws ISPACRuleException {
		File fileXML = null;
		try{
			String fileName = FileTemporaryManager.getInstance().newFileName(".xml");
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			
			fileXML = new File(fileName);
			//JAXBContext jaxbContext = JAXBContext.newInstance(Libro.class);
			JAXBContext jaxbContext = JAXBContext.newInstance("es.dipucr.jaxb.juntaconsultiva2.beans");
			Marshaller marshaller = jaxbContext.createMarshaller();
			//indicamos que queremos formateada nuestra salida (con enters y tabs)
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//indicamos el objeto que escribiremos a XML y la salida (puede ser un objeto tipo FILE)
			marshaller.marshal(relacionContratos, fileXML);
		} catch (JAXBException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		 } catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return fileXML;
	}

	public static es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento.OrganoContratante.Contrato getContratoJuntaConsultiva(DatosContrato datosContrato,
			DatosTramitacion datosTramitacion, DiariosOficiales diariosFechaOficiales, DatosLicitacion datosLicitacion, DatosEmpresa datosEmpresa) throws ISPACRuleException {
		es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento.OrganoContratante.Contrato contrato = new es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento.OrganoContratante.Contrato();
		
		try {
			if(null != datosContrato.getNumContrato()){
				//numero de contrato
				contrato.setNumero(datosContrato.getNumContrato());
			}
			if(null != datosContrato.getTipoContrato()){
				//Tipo de contrato (Tabla 9)
				contrato.setTipoContrato(obtenerTipoContrato(datosContrato.getTipoContrato()).value());
			}
			//Código correspondiente a la modalidad de la contratación (sólo aplicable a los contratos de gestión de servicios públicos)
			//contrato.setModalidad("");
			
			//Código provincia donde se realiza el contrato (Tabla 5)
			if(null != datosContrato.getProvinciaContrato()){
				contrato.setProvincia(datosContrato.getProvinciaContrato().getValor());					
			}
			if(null != datosContrato.getObjetoContrato()){
				//objeto del contrato
				contrato.setObjeto(datosContrato.getObjetoContrato());
			}
			
			//Características bienes solo en contratos de tipo C (Suministros) tabla 11
			if(contrato.getTipoContrato().equals(TipoContrato.C) && null != datosContrato.getCaracteristicasBienes()){
				String [] vcarac = datosContrato.getCaracteristicasBienes().split(" - ");
				if(vcarac.length==2){
					contrato.setCaracteristicaBienes(vcarac[0]);
				}
			}
			
			//Sólo permite meter un CPV
			if(null != datosContrato.getCpv() && datosContrato.getCpv().length>0){
				Campo[] sCPV = datosContrato.getCpv();
				for(int i=0; i<datosContrato.getCpv().length; i++){
					Campo cpv = sCPV[i];
					CPV jcCpv = new CPV();
					jcCpv.setCodigoCPV(cpv.getId());
					Calendar calendar = Calendar.getInstance(); 
					calendar.set(Calendar.YEAR, 2007);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
					String date = sdf.format(calendar.getTime());
					XMLGregorianCalendar fec = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
					
					//para comprobar este campo me voy a la tabla contratacion_servicios en el codice_pliegos="COD_CPV"
					//y la ruta que utilizamos es "https://contrataciondelestado.es/codice/cl/1.04/CPV2007-1.04.gc"
					jcCpv.setVersion(fec);
					contrato.setCPV(jcCpv);
				}
			}
			
			//Publicidad
			//Supongo que la publicidad que piden es del anuncio de licitación
			
			boolean publicidad = false;
			if(null != diariosFechaOficiales.getAnuncioLicitacionBOE()){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf.format(diariosFechaOficiales.getAnuncioLicitacionBOE().getTime());
				XMLGregorianCalendar fec = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
				contrato.setAnuncioBoe(fec);
				publicidad = true;
			}
			if(null != diariosFechaOficiales.getAnuncioLicitacionDOUE()){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf.format(diariosFechaOficiales.getAnuncioLicitacionDOUE().getTime());
				XMLGregorianCalendar fec = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
				contrato.setAnuncioDOUE(fec);
				publicidad = true;
			}
			if(null != datosTramitacion.getFechaBOPExpCont()){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf.format(datosTramitacion.getFechaBOPExpCont().getTime());
				XMLGregorianCalendar fec = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
				contrato.setAnuncioBop(fec);
				publicidad = true;
			}
			if(null != diariosFechaOficiales.getAnunLicitacionPerfilContratante()){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf.format(diariosFechaOficiales.getAnunLicitacionPerfilContratante().getTime());
				XMLGregorianCalendar fec = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
				contrato.setAnuncioPerfilContratante(fec);
				publicidad = true;
			}
			
			if(publicidad){
				contrato.setPublicidad("si");
			} else{
				contrato.setPublicidad("no");
			}
			
			if(null != datosContrato.getTipoTramitacion()){
				contrato.setTramite(getTramite(datosContrato.getTipoTramitacion()));
			}
			if(null != datosContrato.getProcedimientoContratacion()){
				//Procedimiento de adjudicación (Tabla 13)
				contrato.setProcedimientoAdjud(getProcedimientoAdjud(datosContrato.getProcedimientoContratacion()));
			}
			
			//Código del supuesto legal que amparó el uso de procedimiento negociado de adjudicación. Equivalente funcional 
			//(en formato normalizado) al número o código de artículo y apartado que lo regula)
			if(null != datosContrato.getProcNegCausa()){
				String [] vcarac = datosContrato.getProcNegCausa().split(" - ");
				if(vcarac.length==2){
					contrato.setProcNegCausa(vcarac[0]);
				}
				
			}
			if(null != datosTramitacion.getInvitacioneLicitar()){
				//Numero de invitaciones a licitar
				contrato.setInvitaciones(new BigInteger(datosTramitacion.getInvitacioneLicitar()+""));
			}
			if(null != datosContrato.getValorEstimadoContrato()){
				//Importe presupuesto licitación
				String valor = datosContrato.getValorEstimadoContrato();
				contrato.setImportePresupuesto(new BigInteger(valor.split("\\.")[0]));
			}
			//Código del criterio utilizado para la adjudicacion del contrato
			if(null != datosLicitacion.getCritAdj() && null != datosLicitacion.getCritAdj().getTipoAdjudicacion()){
				if(datosLicitacion.getCritAdj().getTipoAdjudicacion().getValor().contains("Oferta")){
					contrato.setFormaAdjud(es.dipucr.jaxb.juntaconsultiva.commons.Constantes.CRITERIOS_ADJUDICACION.OFERTA_MAS_VENTAJOSA);
				}
				if(datosLicitacion.getCritAdj().getTipoAdjudicacion().getValor().contains("Precio")){
					contrato.setFormaAdjud(es.dipucr.jaxb.juntaconsultiva.commons.Constantes.CRITERIOS_ADJUDICACION.PRECIO_MAS_VENTAJOSO);
				}
			} else{
				contrato.setFormaAdjud(es.dipucr.jaxb.juntaconsultiva.commons.Constantes.CRITERIOS_ADJUDICACION.OTRAS);
			}
			
			if(null != datosTramitacion.getDuracionContrato()){
				//De ejecución en meses
				contrato.setPlazo(new BigInteger(datosTramitacion.getDuracionContrato().getDuracion()+""));
				//plazoConcesionen meses, sólo en los contratos de tipos B o H
				if(("B".equals(contrato.getTipoContrato()) || "H".equals(contrato.getTipoContrato())) && null != datosTramitacion.getDuracionContrato().getDuracion()){
					String duracion = datosTramitacion.getDuracionContrato().getDuracion();
					String[] vDuracion = duracion.split("\\.");
					if(vDuracion.length==1){
						contrato.setPlazoConcesion(new BigInteger(duracion+".0"));
					} else{
						contrato.setPlazoConcesion(new BigInteger(duracion));
					}
				}
			}
			
			contrato.setPlurianual("No");
			contrato.setRevisionPrecios("No");
			if(null != datosLicitacion && null != datosLicitacion.getAplicacionPres()){
				//Aplicacion Presupuestaria
				AplicacionPresupuestaria[] vPresupuestaria = datosLicitacion.getAplicacionPres();
				if(null != vPresupuestaria && vPresupuestaria.length>0){
					contrato.setPlurianual("Si");
					for(int i=0; i<vPresupuestaria.length; i++){
						AplicacionPresupuestaria aplicacPre = vPresupuestaria[i];
						Anualidad anualidad = new Anualidad();
						Calendar calendar = Calendar.getInstance(); 
						calendar.set(Calendar.YEAR, Integer.parseInt(aplicacPre.getAnualidad()));
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
						String date = sdf.format(calendar.getTime());
						XMLGregorianCalendar fec = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
						anualidad.setAnio(fec);
						String valor = aplicacPre.getImporte();
						anualidad.setImporte(new BigInteger(valor.split("\\.")[0]));
						contrato.getAnualidad().add(anualidad);
					}
				}
				//Revision de precios
				if(null != datosLicitacion.getRevisionPrecios()){
					contrato.setRevisionPrecios("Si");
					//Numero que identifica la fórmula tipo de revisión de precios aplicada sólo para contratos de obras
					if(null != contrato.getTipoContrato() && "A".equals(contrato.getTipoContrato())){
						contrato.setFormulaTipo(datosLicitacion.getRevisionPrecios());
					}
				}
			}

			if(null != datosEmpresa && null != datosEmpresa.getClasificacion()){
				Campo[] vClasificacion = datosEmpresa.getClasificacion();
				//clasificacion exigida
				for(int i = 0; i < vClasificacion.length; i++){
					Campo clasi = vClasificacion[i];
					ClasificacionExigida clasificacion = new ClasificacionExigida();
					clasificacion.setCategoria(clasi.getId().substring(2,3));
					clasificacion.setGrupo(clasi.getId().substring(0,1));
					clasificacion.setSubgrupo(clasi.getId().substring(1,2));
					contrato.getClasificacionExigida().add(clasificacion);
				}
			}
	
			//Contratista
			if(null != datosTramitacion.getLicitador() && datosTramitacion.getLicitador().length>0){
				LicitadorBean licitador = datosTramitacion.getLicitador()[0];
				boolean bcontratista = false;
				es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento.OrganoContratante.Contrato.Contratista contratista = 
						new es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento.OrganoContratante.Contrato.Contratista();
				if(null != licitador.getIdentificador()){
					contratista.setNif(licitador.getIdentificador());
					bcontratista = true;
				}
				
				if(null != licitador.getNombre()){
					contratista.setDescripcion(licitador.getNombre());
					bcontratista = true;
				}
				
				if(bcontratista){
					contrato.getContratista().add(contratista);
				}			
				
				if(null != licitador.getFechaAdjudicacion()){
					//Fecha adjudicacion
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String date = sdf.format(licitador.getFechaAdjudicacion().getTime());
					XMLGregorianCalendar fec = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
					contrato.setFechaAdjudicacion(fec);
				}
				
				if(null != licitador.getImporteSinImpuestos()){
					//Importe adjudicacion
					String valor = licitador.getImporteSinImpuestos();
					contrato.setImporteAdjudicacion(new BigInteger(valor.split("\\.")[0]));
				}
			}
			
			if(null != datosTramitacion.getFormalizacion()){
				//Fecha formalizacion
				if(null != datosTramitacion.getFormalizacion().getFechaContrato()){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String date = sdf.format(datosTramitacion.getFormalizacion().getFechaContrato().getTime());
					XMLGregorianCalendar fec = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
					contrato.setFechaFormalizacion(fec);
				}
				
				if(null != datosTramitacion.getFormalizacion().getTextoAcuerdoFormalizacion()){
					contrato.setObservaciones(datosTramitacion.getFormalizacion().getTextoAcuerdoFormalizacion());
				}
				
			}
		} catch (DatatypeConfigurationException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return contrato;
	}


	private static String getProcedimientoAdjud(Campo campo) {
		String procedimiento = "";
		if(null != campo){
			if(campo.getValor().contains("Abierto")){
				procedimiento = PROC_ADJUDICACION.ABIERTO;
			}
			if(campo.getValor().contains("Negociado sin publicidad")){
				procedimiento = PROC_ADJUDICACION.RESTRINGIDO;
			}
			if(campo.getValor().contains("Negociado con publicidad")){
				procedimiento = PROC_ADJUDICACION.NEGOCIADO;
			}
		}
		return procedimiento;
	}

	private static String getTramite(Campo campo) {
		String tramite = "";
		if(null != campo){
			if("Ordinaria".equals(campo.getValor())){
				tramite = TRAMITE.ORDINARIO;
			}
			if("Urgente".equals(campo.getValor())){
				tramite = TRAMITE.URGENTE;
			}
			if("Emergencia".equals(campo.getValor())){
				tramite = TRAMITE.EMERGENCIA;
			}
		}
		return tramite;
	}

	@SuppressWarnings("unchecked")
	public static Vector<String> expedientesByCodProcemiento(IItemCollection itExpedCodProc) throws ISPACRuleException {
		Vector<String> exp = new Vector<String>();
		try{			
			Iterator<IItem> itExpedientes = itExpedCodProc.iterator();
			while(itExpedientes.hasNext()){
				IItem expediente = itExpedientes.next();
				exp.add(expediente.getString("NUMEXP"));
			}
		} catch(ISPACException e){
			LOGGER.error("Error para montar el vector de Números de expedientes. "+e.getMessage());
			throw new ISPACRuleException ("Error para montar el vector de Números de expedientes. "+e.getMessage(),e);
		}
		return exp;
	}
}
