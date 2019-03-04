package es.dipucr.jaxb.tribunalcuentas.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.objeto.sw.DatosContrato;
import es.dipucr.contratacion.objeto.sw.DatosEmpresa;
import es.dipucr.contratacion.objeto.sw.DatosLicitacion;
import es.dipucr.contratacion.objeto.sw.DatosTramitacion;
import es.dipucr.contratacion.objeto.sw.DiariosOficiales;
import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;
import es.dipucr.jaxb.juntaconsultiva.commons.Constantes;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.Cabecera;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.Cabecera.Usuario;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento.OrganoContratante;
import es.dipucr.jaxb.juntaconsultiva2.beans.DgpDeclaracion.EnteContratante.Departamento.OrganoContratante.Contrato;
import es.dipucr.jaxb.tribunalcuentas.commons.FuncionesComunes;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.JasperReportUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GenerarXMLJuntaConsultivaRule implements IRule {
	
	public static final Logger LOGGER = Logger.getLogger(GenerarXMLJuntaConsultivaRule.class);


	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			//Obtener el ejercicio de la entidad
			String ejercicio = FuncionesComunes.obtenerEjercicio(rulectx);
			Vector <String> codProcRendCuentas = FuncionesComunes.obtenerCodProcedRendicionCuentas(rulectx);
			StringBuffer sQuery = new StringBuffer("");
			sQuery.append("WHERE CODPROCEDIMIENTO IN ('"+codProcRendCuentas.get(0)+"'");
			for (int i = 1; i < codProcRendCuentas.size(); i++) {
				sQuery.append(", '"+codProcRendCuentas.get(i)+"'");
			}
			//sQuery.append(") and numexp like 'DPCR"+ejercicio+"/%' ORDER BY CODPROCEDIMIENTO, NUMEXP");
			sQuery.append(") ORDER BY CODPROCEDIMIENTO, NUMEXP");
			//Obtenemos los expedientes que cumplan los datos anteriores.
			IItemCollection itColExp = ExpedientesUtil.queryExpedientes(rulectx.getClientContext(), sQuery.toString());
			
			Vector <String> expedCont = FuncionesComunes.expedientesByCodProcemiento(itColExp);
			sQuery = new StringBuffer("");
			sQuery.append("NUMEXP IN ('"+expedCont.get(0)+"'");
			for (int i = 1; i < expedCont.size(); i++) {
				sQuery.append(", '"+expedCont.get(i)+"'");
			}
			
			//Consulta a la tabla CONTRATACION_DATOS_TRAMIT
			sQuery.append(") AND F_CONTRATO IS NOT NULL AND F_CONTRATO BETWEEN '"+ejercicio+"-01-01' and '"+ejercicio+"-12-31'");
			Iterator<IItem> itExp = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_DATOS_TRAMIT", sQuery.toString());
			
			DgpDeclaracion relacionContratos = new DgpDeclaracion();
			Calendar calendar = Calendar.getInstance(); 
			calendar.set(Calendar.YEAR, Integer.parseInt(ejercicio));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String date = sdf.format(calendar.getTime());
			XMLGregorianCalendar fec = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
			//Año de adjudicacion de los contratos
			relacionContratos.setAnio(fec);
			
			Cabecera cabecera = new Cabecera();
			//Remitente y señas de contacto
			Usuario usuario = FuncionesComunes.getUsuarioContacto(rulectx);
			cabecera.setUsuario(usuario);	
			cabecera.setTipoAdmin(Constantes.TIPO_ADMINISTRACION.ENTIDADLOCAL);
			cabecera.setTipoAdminLocal(Constantes.TIPO_ADMINISTRACION_LOCAL.DIPUTACIONPROVINCIAL_FORAL);
			//Número de contratos en la remesa
			cabecera.setRegistrosEnviados(new BigInteger(itColExp.toList().size()+""));			
			relacionContratos.setCabecera(cabecera);
			
			EnteContratante enteContratante = new EnteContratante();
			enteContratante.setCodEnteContratante(usuario.getProvincia());
			enteContratante.setNombreEnteContratante(DipucrCommonFunctions.getVarGlobal("NOMBREENTIDAD"));
			
			Departamento departamento = new Departamento();
			//Solo aplicable a la Adm. del Estado y a la CCAA
			//departamento.setCodigoDepartamento(value);
			//departamento.setNombreDepartamento(value);
			OrganoContratante organoContratanteJuntaGobierno = new OrganoContratante();
			//si la entidad que notifica los contratos sólo tiene un órgano contratante, se le asignará por defecto el valor '1'
			organoContratanteJuntaGobierno.setCodigoOrganoContratante(Constantes.ORGANO_CONTRATACION_JUNTACONSULTIVA.JUNTAGOBIERNO);
			organoContratanteJuntaGobierno.setNombreOrganoContratante("Junta de Gobierno");
			
			//Si la entidad es la 005 es diputacion por lo tanto los decretos son el presidente y es el resto es el alcalde
			OrganoContratante organoContratanteDecretos = new OrganoContratante();
			String codEntidad = EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext());
			if("005".equals(codEntidad)){
				organoContratanteDecretos.setCodigoOrganoContratante(Constantes.ORGANO_CONTRATACION_JUNTACONSULTIVA.PRESIDENTE);
				organoContratanteDecretos.setNombreOrganoContratante("Presidente");
			} else{
				organoContratanteDecretos.setCodigoOrganoContratante(Constantes.ORGANO_CONTRATACION_JUNTACONSULTIVA.ALCALDE);
				organoContratanteDecretos.setNombreOrganoContratante("Alcalde");
			}
			OrganoContratante organoContratanteJuntaPleno = new OrganoContratante();
			organoContratanteJuntaPleno.setCodigoOrganoContratante(Constantes.ORGANO_CONTRATACION_JUNTACONSULTIVA.PLENO);
			organoContratanteJuntaPleno.setNombreOrganoContratante("Pleno");
			
			OrganoContratante organoContratanteJuntaResto = new OrganoContratante();
			organoContratanteJuntaResto.setCodigoOrganoContratante(Constantes.ORGANO_CONTRATACION_JUNTACONSULTIVA.OTROS);
			organoContratanteJuntaResto.setNombreOrganoContratante("Otros");
			
			while(itExp.hasNext()){
				IItem expediente = itExp.next();
				String numexpExp = expediente.getString("NUMEXP");
				DatosContrato datosContrato = DipucrFuncionesComunesSW.getDatosContrato(rulectx.getClientContext(), numexpExp);
				DatosTramitacion datosTramitacion = DipucrFuncionesComunesSW.getDatosTramitacion(rulectx.getClientContext(), numexpExp, null);
				DatosLicitacion datosLicitacion = DipucrFuncionesComunesSW.getDatosLicitacion(rulectx.getClientContext(), numexpExp);
				DiariosOficiales diariosOficiales = DipucrFuncionesComunesSW.getDiariosOficiales(rulectx.getClientContext(), numexpExp);
				DatosEmpresa datosEmpresa = DipucrFuncionesComunesSW.getDatosEmpresa(rulectx.getClientContext(), numexpExp);
				if(null == datosContrato){
					datosContrato = new DatosContrato();
				}
				if(null == datosTramitacion){
					datosTramitacion = new DatosTramitacion();
				}
				if(null == diariosOficiales){
					diariosOficiales = new DiariosOficiales();
				}
				if(null == datosEmpresa){
					datosEmpresa = new DatosEmpresa();
				}
				Contrato contrato = FuncionesComunes.getContratoJuntaConsultiva(datosContrato, datosTramitacion, diariosOficiales, datosLicitacion, datosEmpresa);
				//compruebo que organo de contratación lo aprobo
				if(datosContrato.getOrganoContratacion().equals(Constantes.ORGANO_CONTRATACION.JUNTAGOBIERNO)){						
					if(null != contrato){
						organoContratanteJuntaGobierno.getContrato().add(contrato);
					}
				} else if(datosContrato.getOrganoContratacion().equals(Constantes.ORGANO_CONTRATACION.DECRETO)){
					if(null != contrato){
						organoContratanteDecretos.getContrato().add(contrato);
					}
				} else if(datosContrato.getOrganoContratacion().equals(Constantes.ORGANO_CONTRATACION.PLENO)){
					if(null != contrato){
						organoContratanteJuntaPleno.getContrato().add(contrato);
					}
				} else{
					if(null != contrato){
						organoContratanteJuntaResto.getContrato().add(contrato);
					}
				}
				
			}
			if(null != organoContratanteJuntaGobierno.getContrato() && organoContratanteJuntaGobierno.getContrato().size()>0){
				departamento.getOrganoContratante().add(organoContratanteJuntaGobierno);
			}
			if(null != organoContratanteDecretos.getContrato() && organoContratanteDecretos.getContrato().size()>0){
				departamento.getOrganoContratante().add(organoContratanteDecretos);
			}
			if(null != organoContratanteJuntaPleno.getContrato() && organoContratanteJuntaPleno.getContrato().size()>0){
				departamento.getOrganoContratante().add(organoContratanteJuntaPleno);
			}
			if(null != organoContratanteJuntaResto.getContrato() && organoContratanteJuntaResto.getContrato().size()>0){
				departamento.getOrganoContratante().add(organoContratanteJuntaResto);
			}

			//departamento.setCodigoDepartamento(value);
			//departamento.setNIF(value);
			//departamento.setNombreDepartamento(value);
			enteContratante.getDepartamento().add(departamento);
			
			relacionContratos.setEnteContratante(enteContratante);

			
			File fileXML = FuncionesComunes.obtenerXML(relacionContratos);
			
			if(null != fileXML){
				//Guarda el resultado en gestor documental Notificaciones
				int tpdocXML = DocumentosUtil.getIdTipoDocByCodigo(rulectx.getClientContext(), "xml-rendiCuen");
				String nombreTipoDocXML = DocumentosUtil.getNombreTipoDocByCod(rulectx.getClientContext(), "xml-rendiCuen");
				IItem docXML = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), tpdocXML, nombreTipoDocXML, fileXML, "xml");
				if(fileXML.exists()){
					fileXML.delete();
				}				
				
				File ffilePathJunta = JasperReportUtil.obtenerPdftoXml(rulectx, docXML,  "/dgp_sitt_schema/enteContratante/departamento/organoContratante/contrato", "Estadísticas Rendición Cuentas", "JuntaConsultiva.jasper");
				
				IItem entityDocumentParcela = DocumentosUtil.generaYAnexaDocumento(rulectx.getClientContext(), rulectx.getTaskId(), tpdocXML, nombreTipoDocXML,ffilePathJunta, Constants._EXTENSION_PDF);
				entityDocumentParcela.store(rulectx.getClientContext());
				if(ffilePathJunta.exists()){
					ffilePathJunta.delete();
				}
				
			}
			
			
			
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (DatatypeConfigurationException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return Boolean.TRUE;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

}
