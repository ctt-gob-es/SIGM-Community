package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.services.PlataformaContratacionStub.Campo;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class ControlarDocObligatorioTramContratoDepRule implements IRule{
	
	protected static final Logger LOGGER = Logger.getLogger(ControlarDocObligatorioTramContratoDepRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean terminarTramite = true;
		StringBuffer mensajeError = new StringBuffer("");
		/**
		 * CONTRATOS MENORES
		 * Obras: Hasta 40.000 (mas IVA)
		 * Servicios y Sumninistros: hasta 15.000 (más IVA)
		 * **/
		try {	
			//Informe razonado de la necesidad de un contrato administrativo
			if(tieneFirmadoTipoDoc(rulectx, "inf-nec-cont-adm")){
				Iterator<IItem> itPeticion = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_PETICION", "NUMEXP='"+rulectx.getNumExp()+"'");
				if(itPeticion.hasNext()){
					IItem peticion = itPeticion.next();
					String tipoContrato = "";
					String presupuesto = "";
					if(StringUtils.isNotEmpty(peticion.getString("TIPO_CONTRATO")) && StringUtils.isNotEmpty(peticion.getString("PRESUPUESTO"))){
						tipoContrato = peticion.getString("TIPO_CONTRATO");
						presupuesto = peticion.getString("PRESUPUESTO");
						Float fPresupe = new Float(0);
						try{
							fPresupe = Float.parseFloat(presupuesto); 
						}catch (NumberFormatException e) {
							mensajeError.append("El presupuesto estimado introducido no es un número: "+presupuesto+". Pestaña Necesidad y Objeto del Contrato");
							terminarTramite = false;
						}
						String [] vtipoContrato = tipoContrato.split(" - ");
						Campo campo = new Campo();
						if(vtipoContrato.length >1){							
							campo.setId(vtipoContrato[0]);
							campo.setValor(vtipoContrato[1]);
							//3 - Obras
							if(campo.getId().equals("3")){
								if(fPresupe == 0){
									mensajeError.append("El presupuesto estimado introducido no es correcto, introduzca un valor. Pestaña Necesidad y Objeto del Contrato");
									terminarTramite = false;
								}
								else{
									if(tieneFirmadoTipoDoc(rulectx, "proy-obras")){
										if(fPresupe>40000){
											if(tieneFirmadoTipoDoc(rulectx, "acta-replan") && tieneFirmadoTipoDoc(rulectx, "PPT")){
//												if(!tieneDisponibilidadReplanteo(rulectx)){
//													mensajeError.append("En los tipos de contratos de obras con el presupuesto mayor de 40.000 es obligatorio tener la 'Disponibilidad de Terrenos' firmado electrónicamente. Generar el trámite 'Solicitud Informe Disponibilidad del Terreno'.");
//													terminarTramite = false;
//												}
												//Permitimos seguir ya que solo es obligatorio el informe de Disponibilidad de Terreno en algunos casos, deben saber ellos cuándo.
											}
											else{
												mensajeError.append("En los tipos de contratos de obras con el presupuesto mayor de 40.000 es obligatorio tener el 'Acta de Replanteo' y el 'Pliego de Prescripciones Técnicas' firmado electrónicamente. Añadir al trámite 'Informe razonado de la necesidad del contrato administrativo'.");
												terminarTramite = false;
											}
										}
									}
									else{
										mensajeError.append("En los Contratos de Obras se debe anexar al expediente el proyecto o memoria valorada de obras, según proceda, dentro del tipo de documento 'Proyecto de Obras.'");
										terminarTramite = false;
									}
								}						
							}
							else{
							//if(campo.getValor().equals("2 - Servicios") || campo.getValor().equals("1 - Suministros")){
								if(fPresupe>15000){
									if(!tieneFirmadoTipoDoc(rulectx, "PPT")){
										mensajeError.append("En los tipos de contratos de Suministros o Servicios con el presupuesto mayor de 15.000 es obligatorio tener el 'Pliego de Prescripciones Técnicas' firmado electrónicamente.");
										terminarTramite = false;
									}
									
								}
							}
						}
						else{
							mensajeError.append("El tipo de contrato esta mal seleccionado. Pestaña Necesidad y Objeto del Contrato. "+tipoContrato);
							terminarTramite = false;
						}
						
					}
					else{
						mensajeError.append("Es necesario añadir el tipo de contrato o el presupuesto estimado. Pestaña Necesidad y Objeto del Contrato");
						terminarTramite = false;
					}					
				}
				else{
					mensajeError.append("Es necesario añadir el tipo de contrato o el presupuesto estimado. Pestaña Necesidad y Objeto del Contrato");
					terminarTramite = false;
				}
			}
			else{
				mensajeError.append("Falta por firmar electrónicamente el documento 'Informe razonado de la necesidad de un contrato administrativo'.");
				terminarTramite = false;		
			
			}
		} catch (ISPACException e) { 
			LOGGER.error("Error en el expediente. "+rulectx.getNumExp()+", en la pestaña Necesidad y Objeto del Contrato "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el expediente. "+rulectx.getNumExp()+", en la pestaña Necesidad y Objeto del Contrato "+e.getMessage(), e);
		}
		if(StringUtils.isNotEmpty(mensajeError.toString())){
			rulectx.setInfoMessage(mensajeError.toString());
		}
		return terminarTramite;
	}

	@SuppressWarnings("unchecked")
	private boolean tieneDisponibilidadReplanteo(IRuleContext rulectx) throws ISPACRuleException {
		boolean tieneActaRep = false;
		try{
			IItem tramiteByCod = TramitesUtil.getTramiteByCode(rulectx, "sol-disp-ter");
			if(tramiteByCod!=null){
				String nombreTramiteByCode = tramiteByCod.getString("NOMBRE");
				IItemCollection itColTramite = TramitesUtil.getTramites(rulectx.getClientContext(), rulectx.getNumExp(), "NOMBRE='"+nombreTramiteByCode+"'", "FECHA_INICIO DESC");
				Iterator<IItem> iteratorTramite = itColTramite.iterator();
				if(iteratorTramite.hasNext()){
					IItem tramite = iteratorTramite.next();
					String observaciones = "";
					if(StringUtils.isNotEmpty(tramite.getString("OBSERVACIONES"))){
						observaciones = tramite.getString("OBSERVACIONES");
						String[] vObse = observaciones.split(" - Exp.Relacionado: ");
						if(vObse!=null && vObse.length>0){
							String numexp = vObse[1];
							IItemCollection itDoc = DocumentosUtil.getDocumentos(rulectx.getClientContext(), numexp, "NOMBRE='Documento Firmado'", "FAPROBACION DESC");
							Iterator<IItem> iteratorDoc = itDoc.iterator();
							if(iteratorDoc.hasNext()){
								tieneActaRep = true;
							}
						}
					}
				}
			}
		} catch (ISPACException e) { 
			LOGGER.error("Busqueda del tieneActaReplanteo "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Busqueda del tieneActaReplanteo "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		
		return tieneActaRep;
	}

	@SuppressWarnings("unchecked")
	private boolean tieneFirmadoTipoDoc(IRuleContext rulectx, String codigoTramite) throws ISPACRuleException {
		boolean iguales = false;
		try {
			
			String nombreTipoDoc = DocumentosUtil.getTipoDocNombreByCodigo(rulectx.getClientContext(), codigoTramite);
			IItemCollection collDoc = DocumentosUtil.getDocumentos(rulectx.getClientContext(), rulectx.getNumExp(), "NOMBRE='"+nombreTipoDoc+"' AND FAPROBACION IS NOT NULL", "FAPROBACION DESC");
			Iterator<IItem> itColDoc = collDoc.iterator();
			if (itColDoc.hasNext()) {
				iguales = true;						
			}
		} catch (ISPACException e) { 
			LOGGER.error("Busqueda del tipo de doc codigoTramite "+codigoTramite+" - "+rulectx.getNumExp()+" "+e.getMessage(), e);
			throw new ISPACRuleException("Busqueda del tipo de doc codigoTramite "+codigoTramite+" - "+rulectx.getNumExp()+" "+e.getMessage(), e);
		}
		return iguales;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
