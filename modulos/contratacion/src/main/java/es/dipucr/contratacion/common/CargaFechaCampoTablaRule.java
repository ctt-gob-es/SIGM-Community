package es.dipucr.contratacion.common;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.TransactionContainer;

public class CargaFechaCampoTablaRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(CargaFechaCampoTablaRule.class);
	
	public static String tablaInsertarDatos = "CONTRATACION_DATOS_TRAMIT";
	public static String campotablaInsertarDatos = "FECHA_ADJUDICACION"; 

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			TransactionContainer txcontainer=cct.getTXContainer();
			 /***********************************************************************/
			
			int taskId = rulectx.getTaskId();
			String sqlQueryDoc = "ID_TRAMITE = " + taskId + " AND ESTADOFIRMA IN ('02','03','04')";
			IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");
			
			Iterator<IItem> itDoc = documentos.iterator();
			if(itDoc.hasNext()){
				txcontainer.begin();
				IItem doc = itDoc.next();
				Date ffirma = doc.getDate("FAPROBACION");
				IItemCollection contratacion_datos_tramCollection = entitiesAPI.getEntities(tablaInsertarDatos, rulectx.getNumExp());

				Iterator <IItem> contratacion_datos_tramIterator = contratacion_datos_tramCollection.iterator();
				if(contratacion_datos_tramIterator.hasNext()){
					IItem contratacion_datos_lic = contratacion_datos_tramIterator.next();
					contratacion_datos_lic.set(campotablaInsertarDatos, ffirma);
					contratacion_datos_lic.store(cct);
				}
				else{
					IItem contratacion_datos_tram = entitiesAPI.createEntity(tablaInsertarDatos,"");
					contratacion_datos_tram.set("NUMEXP", rulectx.getNumExp());
					contratacion_datos_tram.set(campotablaInsertarDatos, ffirma);
					contratacion_datos_tram.store(cct);
				}
				 txcontainer.commit();
			}
			
			
			

		
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
	 	
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
