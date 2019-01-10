package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import java.util.Iterator;
import java.util.List;

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

public class InitDecretoSubvRule implements IRule {

    protected String strEntidad = "";
    protected String strExtracto = "";

    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            //Obtiene el expediente de la entidad
            String numexpDecr = rulectx.getNumExp();    
            List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpRelacionadosPadres(entitiesAPI, numexpDecr);
            
            if (expedientesRelacionados.isEmpty()){
                return Boolean.FALSE;
            }        
            
            String numexpEnt = expedientesRelacionados.get(0);
            IItemCollection col = entitiesAPI.getEntities(strEntidad, numexpEnt);
            Iterator<?> it = col.iterator();
            
            if (!it.hasNext()) {
                return Boolean.FALSE;
            }
            
            IItem entidad = (IItem)it.next();
            
            //Inicializa los datos del Decreto
            IItem decreto = DecretosUtil.getDecreto(cct, numexpDecr);

            
            if (decreto != null) {
                decreto.set(DecretosUtil.DecretoTabla.EXTRACTO_DECRETO, strExtracto);
                decreto.store(cct);
                
            } else {
                return Boolean.FALSE;
            }
            
            //Actualiza el campo "estado" de la entidada para
            //que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
            entidad.set("ESTADO", "Decreto");
            entidad.store(cct);
            
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido inicializar el decreto.",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}
