package es.dipucr.contratacion.rule;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.resp.Responsible;

public class CargaServicioResponsablePeticionContratoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(CargaServicioResponsablePeticionContratoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		 try{
			//--------------------------------------------------------------------------------
		        ClientContext cct = (ClientContext) rulectx.getClientContext();
		        IInvesflowAPI invesFlowAPI = cct.getAPI();
		        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		        //-----------------------------------------------------------------------------
		        
		        Iterator<IItem> itPeticion = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_PETICION", "NUMEXP='"+rulectx.getNumExp()+"'");
		        IItem peticion = null;
				if(itPeticion.hasNext()){
					peticion = itPeticion.next();
				}
				else{
					peticion = entitiesAPI.createEntity("CONTRATACION_PETICION","");
					peticion.set("NUMEXP", rulectx.getNumExp());
				}
		        
		        Responsible resp = cct.getUser();
		        //Obtenemos el departamento (servicio) al que pertenece el usuario
		        IResponsible dep = null;
		        String servicio = "";
		        dep = resp.getRespOrgUnit();
		        if (dep != null){
		        	servicio=dep.getName();		        	
		        	peticion.set("SERVICIO_RESPONSABLE", servicio);
		        	peticion.store(cct);
		        }
		 } catch (Exception e) {
			 logger.error("Error obteniendo el Servicio del usuario en el expediente ."+rulectx.getNumExp()+" - "+e.getMessage(), e);
		     throw new ISPACRuleException("Error obteniendo el Servicio del usuario en el expediente ."+rulectx.getNumExp()+" - "+e.getMessage(), e);
		 } 
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
