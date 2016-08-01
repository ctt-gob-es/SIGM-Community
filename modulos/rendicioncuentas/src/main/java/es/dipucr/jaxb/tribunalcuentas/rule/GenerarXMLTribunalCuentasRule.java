package es.dipucr.jaxb.tribunalcuentas.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.common.DipucrFuncionesComunes;
import es.dipucr.contratacion.objeto.DatosContrato;
import es.dipucr.contratacion.objeto.DatosTramitacion;
import es.dipucr.contratacion.objeto.DiariosFechaOficiales;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato;
import es.dipucr.jaxb.tribunalcuentas.beans.TipoEntidad;
import es.dipucr.jaxb.tribunalcuentas.commons.FuncionesComunes;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.JasperReportUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GenerarXMLTribunalCuentasRule implements IRule {
	
	public static final Logger logger = Logger.getLogger(GenerarXMLTribunalCuentasRule.class);


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
			logger.warn("sQuery "+sQuery.toString());
			//Obtenemos los expedientes que cumplan los datos anteriores.
			IItemCollection itColExp = ExpedientesUtil.queryExpedientes(rulectx.getClientContext(), sQuery.toString());
			
			Vector <String> expedCont = FuncionesComunes.expedientesByCodProcemiento(itColExp);
			logger.warn("expedCont "+expedCont);
			sQuery = new StringBuffer("");
			sQuery.append("NUMEXP IN ('"+expedCont.get(0)+"'");
			for (int i = 1; i < expedCont.size(); i++) {
				sQuery.append(", '"+expedCont.get(i)+"'");
			}
			
			//Consulta a la tabla CONTRATACION_DATOS_TRAMIT
			sQuery.append(") AND F_CONTRATO IS NOT NULL AND F_CONTRATO BETWEEN '"+ejercicio+"-01-01' and '"+ejercicio+"-12-31'");
			logger.warn("sQuery "+sQuery);
			Iterator<IItem> itExp = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_DATOS_TRAMIT", sQuery.toString());
			
			
			RelacionContratos  relacionContratos = new RelacionContratos();
			relacionContratos.setEjercicio(ejercicio);
			relacionContratos.setNIF(DipucrCommonFunctions.getVarGlobal("NIFENTIDAD"));
			relacionContratos.setNomEntidad(DipucrCommonFunctions.getVarGlobal("NOMBREENTIDAD"));
			relacionContratos.setTipoEntidad(TipoEntidad.valueOf(DipucrCommonFunctions.getVarGlobal("TIPOENTIDAD")));
			
			
			while(itExp.hasNext()){
				IItem expediente = itExp.next();
				String numexpExp = expediente.getString("NUMEXP");
				DatosContrato datosContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, numexpExp);
				DatosTramitacion datosTramitacion = DipucrFuncionesComunes.getDatosTramitacion(rulectx, numexpExp);
				DiariosFechaOficiales diariosFechaOficiales = DipucrFuncionesComunes.getFechaDiariosOficiales(rulectx, numexpExp);
				Contrato contrato = FuncionesComunes.getContrato(datosContrato, datosTramitacion, diariosFechaOficiales);
				if(contrato!=null)	relacionContratos.getContrato().add(contrato);
			}
			
			File fileXML = FuncionesComunes.obtenerXML(relacionContratos);
			//Guarda el resultado en gestor documental Notificaciones
			int tpdocXML = DocumentosUtil.getIdTipoDocByCodigo(rulectx.getClientContext(), "xml-tribunalCuen");
			String nombreTipoDocXML = DocumentosUtil.getNombreTipoDocByCod(rulectx, "xml-tribunalCuen");
			IItem docXML = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), tpdocXML, nombreTipoDocXML, fileXML, "xml");
			
			if(fileXML.exists()){
				fileXML.delete();
			}
			
			File ffilePathJunta = JasperReportUtil.obtenerPdftoXml(rulectx, docXML,  "/RelacionContratos/Contrato","Estadísticas Rendición Cuentas", "TribunalCuentas.jasper");
			
			IItem entityDocumentParcela = DocumentosUtil.generaYAnexaDocumento(rulectx.getClientContext(), rulectx.getTaskId(), tpdocXML, nombreTipoDocXML,ffilePathJunta, Constants._EXTENSION_PDF);
			entityDocumentParcela.store(rulectx.getClientContext());
			if(ffilePathJunta.exists()){
				ffilePathJunta.delete();
			}
			
			fileXML.delete();
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

}
