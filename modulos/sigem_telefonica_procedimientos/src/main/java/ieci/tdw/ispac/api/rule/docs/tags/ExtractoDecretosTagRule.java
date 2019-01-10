package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna una cadena que contiene un listado con los expedientes de Decretos.
 * Recibe el parámetro tipoExtracto que indica si hay que marcar los expedientes de Decretos para que no vuelvan a ser
 * utilizados en futuros extractos (Extracto de creación "CRE") o no (Extracto de selección "SEL") 
 *
 */
public class ExtractoDecretosTagRule implements IRule {
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    
    /**
     * [dipucr-Felipe #1061]
     * Evitamos que se utilice esta regla, pues puede tags de tamaños muy grandes
     * pueden provocar caídas del tomcat
     */
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        return "Esta regla es antigua y ya no se utiliza, pues puede provocar problemas en la aplicación." +
                " Utilice el procedimiento Libro de Extractos. Si no lo localiza, consulte con el administrador.";
    }
    
    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
