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


public class ExtraerTodosDocumFirmadosPdf2Rule  extends DipuCrGenerarDocumentoUnicoExpediente{
	private static final Logger logger = Logger.getLogger(ExtraerTodosDocumFirmadosPdf2Rule.class);	

	@SuppressWarnings("rawtypes")
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try {
		
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			//Actualiza el campo estado de la entidad
	        //de modo que permita mostrar los enlaces para crear Propuesta/Decreto	        
	        IItemCollection col = entitiesAPI.getEntities("DPCR_RESOLUCION2", rulectx.getNumExp());			
	        Iterator itConv = col.iterator();
	        
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
	        	IItem item = entitiesAPI.createEntity("DPCR_RESOLUCION2",rulectx.getNumExp());
				item.set("NUMEXP", rulectx.getNumExp()); 
				item.set("ESTADO", "Inicio");
				item.set("TITULO", titulo);
		        item.store(rulectx.getClientContext());
	        }
			
			conExpRelacionadosHijos = false;
			conExpRelacionadosPadres = false;
			muestraPortada = false;
			muestracontraPortada = false;
			muestraIndice = true;
			expedienteOrigen = rulectx.getNumExp();
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(),e);
		}
		return true;
	}
	
	public String getConsultaSolicitudRegistro(ArrayList<String> expedientes,
			IEntitiesAPI entitiesAPI, IItem expedienteOriginal)
			throws ISPACException {
		
		return "WHERE 1=2";
	}
	
	public String getConsultaDocumentos(ArrayList<String> expedientes,
			IEntitiesAPI entitiesAPI, IItem expedienteOriginal, IRuleContext rulectx)
			throws ISPACException {
		String consulta ="";
		for (int i = 0; i < expedientes.size(); i++) {
			IItem itemProc = ExpedientesUtil.getExpediente(rulectx.getClientContext(), expedientes.get(i));
			consulta = "WHERE ESTADOFIRMA = '02' AND NUMEXP='"+itemProc.getString("NUMEXP")+"' ORDER BY CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION ASC";
		}

		return consulta;
	}
}