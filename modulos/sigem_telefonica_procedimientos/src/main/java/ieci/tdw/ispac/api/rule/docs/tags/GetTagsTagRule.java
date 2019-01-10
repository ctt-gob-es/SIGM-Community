package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna una cadena que a su vez es un nuevo tag.
 *
 */
public class GetTagsTagRule implements IRule {
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
    
            String tag = "";  //Tag a devolver 

            //Obtenemos los parámetros para generar los tags
            String entity = rulectx.get("entity");
            String property = rulectx.get("property");
            String sessionvar = rulectx.get("sessionvar");
            
            if (StringUtils.isNotEmpty(sessionvar)){
                tag = "<ispactag sessionvar ='"+sessionvar+"'/>";
            }else{
                tag = "<ispactag entity='"+entity+"' property='"+property+"' />";
            }
            
            return tag;
            
        } catch (Exception e) {
            throw new ISPACRuleException("Error obteniendo el tag. ", e);
        }     
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
