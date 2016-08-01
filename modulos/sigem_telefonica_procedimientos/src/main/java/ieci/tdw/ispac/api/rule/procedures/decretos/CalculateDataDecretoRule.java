package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Date;
import java.util.Iterator;

/**
 * 
 * @author diezp
 * @proposito Calcula y almacena la información del Decreto (año, fecha de creción, presidente que lo ha firmado), 
 * 			  al terminar el trámite Preparación de las firmas
 * 
 */
public class CalculateDataDecretoRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			try{
				
				//Comprobar si el campo motivo rechazo tiene asignado un valor
				
				//----------------------------------------------------------------------------------------------
		        ClientContext cct = (ClientContext) rulectx.getClientContext();
		        IInvesflowAPI invesFlowAPI = cct.getAPI();
		        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		        //----------------------------------------------------------------------------------------------
		
		        IItem exp = null;
		        String motivoRechazo = null;
		        String numExp = rulectx.getNumExp();
		        String strQuery = "WHERE NUMEXP='" + numExp + "'";
		        IItemCollection collExps = entitiesAPI.queryEntities("SGD_RECHAZO_DECRETO", strQuery);
		        Iterator itExps = collExps.iterator();
		        if (itExps.hasNext()) 
		        {
		        	exp = (IItem)itExps.next();
		        	motivoRechazo = exp.getString("RECHAZO_DECRETO");
		        	
		        	if (motivoRechazo!=null && !motivoRechazo.equals("")){
		        		return null;
		        	}
				
		        }
			
			}catch (Exception e){
				try {
					throw new ISPACInfo("Se ha producido un error al ejecutar la regla de obtener datos del decreto.");
				} catch (ISPACInfo e1) {
					e1.printStackTrace();
				}
			}
			
			
			//APIs
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			// Consulta para obtener los documentos del trámite actual
			String sqlDocQuery = "ID_TRAMITE = "+rulectx.getTaskId();
			IItemCollection tasksDocuments = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlDocQuery, "");
			
			// Sólo si hay un documento (decreto) adjunto
			if (tasksDocuments!=null && tasksDocuments.toList().size()==1){
		        IItem doc = (IItem)tasksDocuments.iterator().next();
				int docId = doc.getInt("ID");

				String sqlQuery = "WHERE ID_DOCUMENTO = "+docId+" ORDER BY ID_PASO ASC";
				
				IItemCollection ctosFirmaCollection = entitiesAPI.queryEntities("SPAC_CTOS_FIRMA", sqlQuery);
				
				// Comprobar que hay circuito de firmas para el documento, y que son dos pasos (presidente y fedatario)
				if(ctosFirmaCollection.next() && ctosFirmaCollection.toList().size()==2){
					
					// Datos del decreto
					String presidente = null;
					Date fechaFirmaPresidente = null;
					
					IItem cto_firma_presidente = (IItem)ctosFirmaCollection.toList().get(0);
					if(cto_firma_presidente!=null && cto_firma_presidente.getInt("ESTADO")==2){
						
						// Entidad Decreto
						IItemCollection decretos = entitiesAPI.getEntities("SGD_DECRETO", rulectx.getNumExp());
						IItem decreto = (IItem)decretos.iterator().next();

						String strNombrePresidente = decreto.getString("NOMBRE_PRESIDENTE");
						if (StringUtils.isBlank(strNombrePresidente)){
							presidente = cto_firma_presidente.getString("NOMBRE_FIRMANTE");
							if (presidente!=null) decreto.set("NOMBRE_PRESIDENTE", presidente);
							fechaFirmaPresidente = decreto.getDate("FECHA_DECRETO");
							if (fechaFirmaPresidente!=null) decreto.set("FECHA_PRESIDENTE", fechaFirmaPresidente);
							decreto.store(rulectx.getClientContext());
						}
					}
				}
			}
			
			return null;
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
