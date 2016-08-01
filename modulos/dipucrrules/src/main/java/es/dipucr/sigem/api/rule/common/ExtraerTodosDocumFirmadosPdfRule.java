package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


public class ExtraerTodosDocumFirmadosPdfRule  extends DipucrGenerarExpedienteFoliadoConIndiceRule{
	private static final Logger logger = Logger.getLogger(ExtraerTodosDocumFirmadosPdfRule.class);	

	@SuppressWarnings("unchecked")
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try {
		
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			//Actualiza el campo estado de la entidad
	        //de modo que permita mostrar los enlaces para crear Propuesta/Decreto	        
	        IItemCollection col = entitiesAPI.getEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, rulectx.getNumExp());			
	        Iterator<IItem> itConv = col.iterator();
	        
	        IItem expediente = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());	        
	        String titulo = "";
	        if(expediente != null) {
	        	titulo = expediente.getString("ASUNTO");
	        }
	        
	        if (itConv.hasNext()){	        	
		        IItem entidad = (IItem)itConv.next();
		        entidad.set("ESTADO", "Inicio");		        
		        entidad.set("TITULO", titulo);
		        entidad.store(cct);
	        }
	        else{
	        	IItem item = entitiesAPI.createEntity(Constants.TABLASBBDD.SUBV_CONVOCATORIA,rulectx.getNumExp());
				item.set("NUMEXP", rulectx.getNumExp()); 
				item.set("ESTADO", "Inicio");
				item.set("TITULO", titulo);
		        item.store(rulectx.getClientContext());
	        }

	        añadePortada = false;
	        añadeContraportada = false;
	        añadeIndice = true;
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		}
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
			consulta = "WHERE ESTADOFIRMA = '02' AND NUMEXP='"+rulectx.getNumExp()+"' ORDER BY CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION ASC";
		}

		return consulta;
	}
}