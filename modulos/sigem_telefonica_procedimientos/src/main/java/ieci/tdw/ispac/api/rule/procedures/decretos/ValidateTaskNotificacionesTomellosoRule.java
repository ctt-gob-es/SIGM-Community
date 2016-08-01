package ieci.tdw.ispac.api.rule.procedures.decretos;

import java.util.Iterator;

import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * @date 17/03/2010
 * @propósito Valida que no se haya creado anteriormente un trámite de Notificaciones en la fase actual
 * y que se hayan cerrado el primer y segundo trámite
 */
public class ValidateTaskNotificacionesTomellosoRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItemCollection itemCollection = entitiesAPI.getStageTasks(rulectx.getNumExp(), rulectx.getStageProcedureId());
			
			// Se compara el tamaño > 3, ya que itemCollection contiene el elemento que estamos intentando crear 
			if (itemCollection!=null && itemCollection.toList().size()>4){
				rulectx.setInfoMessage("Existen más de dos trámites asociado a la fase actual");
				return false;
			}else{
				
				Iterator iteratorCollection = itemCollection.iterator();
				IItem task = null;
				boolean creacionCerrado = false;
				boolean preparacionCerrado = false;
				int numCreacion = 0;
				int numInforme = 0;
				int numPreparacion = 0;
				int numNotificaciones = 0;
				while (iteratorCollection.hasNext()){
					task = (IItem)iteratorCollection.next();
					
					if (task.getString("NOMBRE").equalsIgnoreCase(TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"))){
						numCreacion++;
						if (task.getInt("ESTADO") == 3){
							creacionCerrado = true;
						}else{
							creacionCerrado = false;
							rulectx.setInfoMessage("No se ha terminado el trámite de "+TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"));
						}
					}else if (task.getString("NOMBRE").equalsIgnoreCase("Informe Jefe del Departamentos/Responsable")){
						numInforme++;
						if (task.getInt("ESTADO") == 3){
							creacionCerrado = true;
						}else{
							creacionCerrado = false;
							rulectx.setInfoMessage("No se ha terminado el trámite de Informe Jefe del Departamentos/Responsable");
						}
					}else if (task.getString("NOMBRE").equalsIgnoreCase("Preparación de firmas y traslado del Decreto")){
						numPreparacion++;
						if (task.getInt("ESTADO") == 3){
							preparacionCerrado = true;
						}else{
							rulectx.setInfoMessage("No se ha terminado el trámite de Preparación de firmas y traslado del Decreto");
						}
					}
					else if (task.getString("NOMBRE").equalsIgnoreCase("Notificaciones")){
						numNotificaciones++;
					}
				}
				
				if (creacionCerrado && preparacionCerrado && numCreacion == 1 && numInforme == 1 && numPreparacion == 1 && numNotificaciones == 1 ){
					return true;	
				}else{
					if (!creacionCerrado && numCreacion == 1){
						rulectx.setInfoMessage("No se ha terminado el trámite de "+TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"));
					}else if (!preparacionCerrado && numPreparacion == 1){
						rulectx.setInfoMessage("No se ha terminado el trámite de Preparación de firmas y traslado del Decreto");
					}else if (numCreacion == 0){
						rulectx.setInfoMessage("No se ha creado el trámite de "+TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"));
					}else if (numPreparacion == 0){
						rulectx.setInfoMessage("No se ha creado el trámite de Preparación de firmas y traslado del Decreto");
					}else if (numCreacion > 1 || numPreparacion > 1 || numNotificaciones > 1){
						rulectx.setInfoMessage("Sólo se puede crear un trámite de cada tipo");
					}
					return false;
				}
				
				
			}
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al comprobar el número de trámites de la fase actual", e);
	    } 
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
