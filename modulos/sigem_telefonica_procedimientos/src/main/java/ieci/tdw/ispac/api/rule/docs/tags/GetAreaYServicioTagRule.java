package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.resp.Responsible;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna una cadena que contiene el Área y Servicio de la Estructura organizativa a la que pertence el usuario actual.
 *
 */
public class GetAreaYServicioTagRule implements IRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        Responsible resp = cct.getUser();
	        String areaYServicio = null, area = null, servicio = null;
	        
	        //Obtenemos el departamento (servicio) al que pertenece el usuario
	        IResponsible dep = null;
	        dep = resp.getRespOrgUnit();
	        if (dep != null){
	        	servicio=dep.getName();
	        	dep = dep.getRespOrgUnit();
	        }
	        //Obtenemos el departamento (area) al que pertenece el departamento
	        if (dep != null){
	        	area=dep.getName();
	        }
	        if (servicio != null){
	        	if (area != null){
	        		areaYServicio = area+"/"+servicio;
	        	}else{
		        	areaYServicio = servicio;
	        	}
	        }else{
	        	areaYServicio = "";
	        }
	        
	        //Obtenemos los grupos a los que pertenece el usuario
	        /*
	        Collection colGroups = resp.getUserGroups();
	        Iterator groupsIt = colGroups.iterator();
	        IItem item = null;
	        while (groupsIt.hasNext()) {
                item = ((IItem)groupsIt.next());
	        }
	        */
	        
	        return areaYServicio;
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error obteniendo el Área y Servicio del usuario.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
