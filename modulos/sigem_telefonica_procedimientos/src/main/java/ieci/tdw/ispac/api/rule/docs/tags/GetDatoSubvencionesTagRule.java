package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

public class GetDatoSubvencionesTagRule implements IRule {
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        String strDato = "???";
        
        try {
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();

            String strEntity = rulectx.get("entity");
            String strProperty = rulectx.get("property");
            
            if ( ! StringUtils.isEmpty(strEntity) && !StringUtils.isEmpty(strProperty) ) {
                
                IItemCollection collection = entitiesAPI.getEntities("SUBV_REINTEGRO", rulectx.getNumExp());
                Iterator<?> it = collection.iterator();
                
                if (it.hasNext()) {
                    IItem reintegro = (IItem)it.next();

                    //Solicitud
                    String strNumexpSol = reintegro.getString("NUMEXP_SOL");
                    IItemCollection coll = entitiesAPI.getEntities("SUBV_SOLICITUD", strNumexpSol);
                    IItem solicitud = (IItem)coll.iterator().next();

                    //Convocatoria
                    String strNumexpConv = solicitud.getString("NUMEXP_CONV");
                    coll = entitiesAPI.getEntities("SUBV_CONVOCATORIA", strNumexpConv);
                    IItem convocatoria = (IItem)coll.iterator().next();

                    //Solicitante
                    coll = entitiesAPI.getEntities("SUBV_ENTIDAD", strNumexpSol);
                    IItem entidad = (IItem)coll.iterator().next();
                    
                    if (strEntity.compareTo("SUBV_CONVOCATORIA")==0) {
                        strDato = convocatoria.getString(strProperty);
                        
                    } else if (strEntity.compareTo("SUBV_SOLICITUD")==0) {
                        strDato = solicitud.getString(strProperty);
                        
                    } else if (strEntity.compareTo("SUBV_ENTIDAD")==0) {
                        strDato = entidad.getString(strProperty);
                        
                    } else if (strEntity.compareTo("SUBV_REINTEGRO")==0) {
                        strDato = reintegro.getString(strProperty);
                    }
                }
            }
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido obtener el dato",e);
        }
        return strDato;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}


