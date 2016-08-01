package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;


public class ExtraerTodosDocumRelacionadoRule  extends DipucrGenerarExpedienteFoliadoConIndiceRule{
	private static final Logger logger = Logger.getLogger(ExtraerTodosDocumRelacionadoRule.class);	

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
		StringBuffer consulta = new StringBuffer("");
		
		Vector<String> vNumexp = new Vector<String>();
		
		obtenerNumExpRelacionados(rulectx, rulectx.getNumExp(), vNumexp);
		consulta.append("WHERE NUMEXP IN ('"+vNumexp.elementAt(0)+"' ");
		for(int i = 1; i<vNumexp.size(); i++){
			consulta.append(", '"+vNumexp.elementAt(i)+"'");
		}
		consulta.append(") ORDER BY FFIRMA ASC;");

		logger.warn("*******************"+consulta.toString());
		return "WHERE 1=2";
	}

	@SuppressWarnings("unchecked")
	private void obtenerNumExpRelacionados(IRuleContext rulectx, String numexp, Vector<String> vNumexp) throws ISPACRuleException {
		try {
			
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			if (!numexp.equals("")){
				String sqlQueryPart = "WHERE NUMEXP_HIJO='"+numexp+"'";
		        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
		        Iterator<IItem> itExpRel = exp_relacionados.iterator();
		        String exp = "";
		        while(itExpRel.hasNext()){
		        	IItem itemExpRel = itExpRel.next();
		        	exp = itemExpRel.getString("NUMEXP_PADRE");
		        	if(!StringUtils.isEmpty(exp) && !vNumexp.contains(exp) && !rulectx.getNumExp().equals(exp)){
		        		vNumexp.add(exp);
		        		obtenerNumExpRelacionados(rulectx, exp, vNumexp);
		        	}
		        	
		        }	
		        sqlQueryPart = "WHERE NUMEXP_PADRE='"+numexp+"'";
		        exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
		        itExpRel = exp_relacionados.iterator();
		        while(itExpRel.hasNext()){
		        	IItem itemExpRel = itExpRel.next();
		        	exp = itemExpRel.getString("NUMEXP_HIJO");
		        	if(!StringUtils.isEmpty(exp) && !vNumexp.contains(exp) && !rulectx.getNumExp().equals(exp)){
		        		vNumexp.add(exp);
		        		obtenerNumExpRelacionados(rulectx, exp, vNumexp);
		        	}
		        }   
			}
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		}

	}
}