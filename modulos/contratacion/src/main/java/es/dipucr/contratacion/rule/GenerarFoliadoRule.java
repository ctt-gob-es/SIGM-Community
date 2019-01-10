package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrGenerarExpedienteFoliadoConIndiceRule;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;


public class GenerarFoliadoRule extends DipucrGenerarExpedienteFoliadoConIndiceRule{
	
	private static final Logger logger = Logger.getLogger(GenerarFoliadoRule.class);

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
		StringBuffer numexps = new StringBuffer("");
		try{
			
			Vector<String> resultado = ExpedientesRelacionadosUtil.getTodosExpRelacionados(rulectx, rulectx.getNumExp());
			numexps.append("'"+resultado.get(0)+"'");
			for(int i=1; i<resultado.size(); i++){
				numexps.append(",'"+resultado.get(i)+"'");
			}
			
		}catch(ISPACException e){
        	logger.error("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
        	throw new ISPACRuleException("ERROR al recuperar los expedientes relacionados hijos: "+e.getMessage(), e);
		}
		
		String consulta = "WHERE NUMEXP IN ("+numexps.toString()+") ORDER BY CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION ASC";
		 //logger.warn("consulta "+consulta);

		return consulta;
	}

}
