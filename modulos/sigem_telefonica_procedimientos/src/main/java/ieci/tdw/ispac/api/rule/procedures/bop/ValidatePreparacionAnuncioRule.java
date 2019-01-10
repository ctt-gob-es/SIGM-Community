package ieci.tdw.ispac.api.rule.procedures.bop;

import java.util.Iterator;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

/**
 * 
 * @author teresa
 * @date 08/03/2010
 * @propósito Valida que en el trámite de Preparación del anuncio se haya asignado valor a los campos de Entidad y Clasificación
 * de la entidad Solicitud BOP.
 */
public class ValidatePreparacionAnuncioRule implements IRule{
    
    private static final Logger LOGGER = Logger.getLogger(ValidatePreparacionAnuncioRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {        
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        
        try{
            
            //Comprobar si los campos Entidad y Clasificacion tienen asignado valor
            
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
    
            IItem exp = null;
            String entidad = null;
            String clasificacion = null;
            String numExp = rulectx.getNumExp();
            IItemCollection collExps = entitiesAPI.getEntities("BOP_SOLICITUD", numExp);
            Iterator<?> itExps = collExps.iterator();
            
            if (itExps.hasNext()){
                exp = (IItem)itExps.next();
                entidad = exp.getString("ENTIDAD");
                clasificacion = exp.getString("CLASIFICACION");
                
                if (StringUtils.isEmpty(entidad) || StringUtils.isEmpty(clasificacion)){
                    rulectx.setInfoMessage("No se puede iniciar el trámite ya que no se han completado los campos Entidad y Clasificacion" + " de la entidad Solicitud BOP");
                    return false;
                    
                }else {
                    return true;
                }            
            } else {
                rulectx.setInfoMessage("No se puede iniciar el trámite ya que no se han completado los campos Entidad y Clasificacion" + " de la entidad Solicitud BOP");
                return false;
            }
        
        } catch (Exception e) {
            LOGGER.info("No hace nada, solo captura el posible error: " + e.getMessage(), e);

            try {
                throw new ISPACInfo("Se ha producido un error al comprobar los campos Entidad y Clasificacion" + " de la entidad Solicitud BOP");
                
            } catch (ISPACInfo e1) {
                LOGGER.error("ERROR. " + e1.getMessage(), e1);
            }
        }
        return true;

    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
