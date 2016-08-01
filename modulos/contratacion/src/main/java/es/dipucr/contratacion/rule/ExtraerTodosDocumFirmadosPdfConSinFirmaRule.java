package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrGenerarExpedienteFoliadoConIndiceRule;


public class ExtraerTodosDocumFirmadosPdfConSinFirmaRule  extends DipucrGenerarExpedienteFoliadoConIndiceRule{
	private static final Logger logger = Logger.getLogger(ExtraerTodosDocumFirmadosPdfConSinFirmaRule.class);	

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
	        añadePortada = true;
	        añadeContraportada = true;
	        añadeIndice = true;
	        return true;
	}
	
	public String getNumExpFoliar(IRuleContext rulectx, IEntitiesAPI entitiesAPI){
		String numexp = "";
		
		try {
			numexp = rulectx.getNumExp();
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar todos los documentos del expediente. " + e.getMessage(), e);
		}
		return numexp;
	} 
	
	public String getConsultaDocumentos(ArrayList<String> expedientes, IRuleContext rulectx, IItem expedienteOriginal)
			throws ISPACException {
		String consulta ="";
		for (int i = 0; i < expedientes.size(); i++) {
			consulta = "WHERE NUMEXP='"+rulectx.getNumExp()+"' ORDER BY CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION ASC";
		}

		return consulta;
	}
}