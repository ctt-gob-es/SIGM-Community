package aww.rule.docs.tags;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.resp.Responsible;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna una cadena que contiene el Servicio de la Estructura organizativa a la que pertence el usuario actual.
 *
 */
public class GetServicioUsuarioTagRule implements IRule {
	
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
	        String servicio;	        
	        
	        //Obtenemos el departamento (servicio) al que pertenece el usuario
	        IResponsible dep = resp.getRespOrgUnit();
	       
	       	servicio=dep.getName();   
	       	
	       	if(servicio == null||servicio.equals("")){
	       		throw new ISPACRuleException("Error obteniendo el Servicio del usuario.");
	       	}	        
	        
	       	//Basta con devolver un string y se sustituye
	        return servicio;
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error obteniendo el Servicio del usuario.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
