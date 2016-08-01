package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrGenerarExpedienteFoliadoConIndiceRule;

public class ExtraerTodosValAnomDespInfContPdfRule extends DipucrGenerarExpedienteFoliadoConIndiceRule{
	
	private static final Logger logger = Logger.getLogger(ExtraerTodosValAnomDespInfContPdfRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
			
			añadePortada = false;
	        añadeContraportada = false;
	        añadeIndice = true;
	        tipoDoc = "%INFORME DE VALORES ANORMALES O DESPROPORCIONADOS%";
	        nombreDoc = "Informe de Valores Anormales o Desproporcionados";
	        
		return true;
	}
	
	public String getNumExpFoliar(IRuleContext rulectx, IEntitiesAPI entitiesAPI){
		String numexp = "";
		
		try {
			numexp = rulectx.getNumExp();
		} catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
		}
		return numexp;
	} 
	
	public String getConsultaDocumentos(ArrayList<String> expedientes,
			IEntitiesAPI entitiesAPI, IItem expedienteOriginal)
			throws ISPACException {
		String consulta = "WHERE NUMEXP IN " +
				"	(SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE IN " +
				"		(SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE='"+expedienteOriginal.getString("NUMEXP")+"' AND UPPER(RELACION) = 'PLICA' ORDER BY ID ASC) " +
				"	AND UPPER (RELACION) = 'VAL. ANORMALES O DESPROPOR') " +
				"AND UPPER (NOMBRE) LIKE '%INFORME DE CONTRATACIÓN%'";

		return consulta;
	}
	
}
