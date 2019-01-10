package ieci.tdw.ispac.api.rule.procedures.sancionador;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
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
 * @date 24/03/2010
 * @propósito Validar el campo Transición de un expediente al terminar una fase
 */
public class ValidateStatusStageSancionadorRule implements IRule {
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            IItem exp = null;
            String numExp = rulectx.getNumExp();
            
            IItemCollection collExps = entitiesAPI.getEntities("URB_SANCIONADOR", numExp);
            Iterator<?> itExps = collExps.iterator();
            String transicion = null;
            
            if (itExps.hasNext()) {
                exp = (IItem)itExps.next();
                transicion = exp.getString("TRANSICION");
                
                if (ValidateStatusTaskSancionadorRule.isTramiteIniciado(transicion)){
                    rulectx.setInfoMessage("No se puede avanzar de fase ya que existe un trámite en proceso");
                    return false;
                    
                } else {
                    String inicio = ValidateStatusTaskSancionadorRule.getSegundoTramite(transicion);
                    
                    int idCtStage = rulectx.getStageProcedureId();
                    
                    IItem itStage = null;
                    String nameStage = null;
                    String strQuery = "WHERE ID = '" + idCtStage + "'";
                    IItemCollection collStages = entitiesAPI.queryEntities("SPAC_P_FASES", strQuery);
                    Iterator<?> itStages = collStages.iterator();
                    
                    if (itStages.hasNext()){
                        itStage = (IItem)itStages.next();
                        nameStage = itStage.getString("NOMBRE");
                        
                        if (StringUtils.isNotEmpty(nameStage)){
                            strQuery = "WHERE NUMEXP='" + numExp + "' AND INICIO='" + inicio + "' AND FIN='" + nameStage + "'";
                            
                            IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
                            Iterator<?> itTransiciones = collTransiciones.iterator();
                            
                            if (!itTransiciones.hasNext()){
                                rulectx.setInfoMessage("No se puede avanzar de fase desde el estado actual");
                                return false;
                            }
                            
                        } else {
                            rulectx.setInfoMessage("Error al obtener el nombre de la fase en el catálogo");
                            return false;
                        }
                    } else {
                        rulectx.setInfoMessage("No se ha encontrado la fase actual en el catálogo de procedimientos");
                        return false;
                    }
                }
            }
        } catch(ISPACException e){
            throw new ISPACRuleException(e);
        }
        
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso.
    }
}