package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrCompruebaExpRelacionadoCerradoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrCompruebaExpRelacionadoCerradoRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return false;
	}

	//Comprobamos si los expedientes relacionado hijos están cerrados
	//si no se han cerrado los hijos no sigue con otras reglas 
	@SuppressWarnings("rawtypes")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean cerrar = true;
		logger.info("INICIO DipucrCompruebaExpRelacionadoCerradoRule");
		try
		    {
		      ClientContext cct = (ClientContext)rulectx.getClientContext();
		      IInvesflowAPI invesFlowAPI = cct.getAPI();
		      IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		      
		      String consultaSQL = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "'";
		      IItemCollection itemCollection = entitiesAPI.queryEntities(41, consultaSQL);
		      
		      //Si no hay expedientes hijos cerramos el trámite
		      if ((itemCollection != null) && (itemCollection.next())) {
			        Iterator it = itemCollection.iterator();
			        IItem item = null;
			        String numexp_hijo = "";
			        
			        while (it.hasNext() && cerrar) {
			        	item = (IItem)it.next();
				        numexp_hijo = item.getString("NUMEXP_HIJO");
				        
				        IItem iExpedHijo = ExpedientesUtil.getExpediente(cct, numexp_hijo);
				        
				        Date fCierre= iExpedHijo.getDate("FCIERRE");
				        
				        if(fCierre == null){
				        	cerrar = false;
				        }
			        }
		      }
		}
		catch(Exception e){
			throw new ISPACRuleException(e);
		}
		logger.info("FIN DipucrCompruebaExpRelacionadoCerradoRule");
		if(cerrar){
			rulectx.setInfoMessage("");
		}
		else{
			rulectx.setInfoMessage("No se puede cerrar el trámite ya que no se ha terminado el expediente resolución (Decreto o Propuesta)");
		}
		return cerrar;
	}

}
