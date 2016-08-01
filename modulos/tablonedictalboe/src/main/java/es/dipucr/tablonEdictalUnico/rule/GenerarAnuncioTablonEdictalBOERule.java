package es.dipucr.tablonEdictalUnico.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.io.File;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.JasperReportUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.tablonEdictalUnico.commons.FuncionesComunes;
import es.dipucr.tablonEdictalUnico.xml.Envio;

public class GenerarAnuncioTablonEdictalBOERule implements IRule{
	
	public static final Logger logger = Logger.getLogger(GenerarAnuncioTablonEdictalBOERule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			Envio envio = FuncionesComunes.construccionEnvio(rulectx, null);
			File fileXML = FuncionesComunes.obtenerXMLFile(envio);
			if(fileXML!=null){
				//Guarda el resultado en gestor documental Notificaciones
				int tpdocXML = DocumentosUtil.getIdTipoDocByCodigo(rulectx.getClientContext(), "AnunTablonEdiBOE");
				String nombreTipoDocXML = DocumentosUtil.getNombreTipoDocByCod(rulectx, "AnunTablonEdiBOE");
				IItem docXML = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), tpdocXML, nombreTipoDocXML, fileXML, "xml");
				if(fileXML.exists()){
					fileXML.delete();
				}				
				
				File ffilePathJunta = JasperReportUtil.obtenerPdftoXml(rulectx, docXML,  "/dgp_sitt_schema/enteContratante/departamento/organoContratante/contrato", "Estadísticas de Tablón Edictal BOE", "TablonEdictalBOE.jasper");
				
				IItem entityDocumentParcela = DocumentosUtil.generaYAnexaDocumento(rulectx.getClientContext(), rulectx.getTaskId(), tpdocXML, nombreTipoDocXML,ffilePathJunta, Constants._EXTENSION_PDF);
				entityDocumentParcela.store(rulectx.getClientContext());
				if(ffilePathJunta.exists()){
					ffilePathJunta.delete();
				}
				
			}
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

}
