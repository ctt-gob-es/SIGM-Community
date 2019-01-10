package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * @date 13/11/2009
 * @propósito Actualiza el campo estado de la entidad para mostrar los enlaces de Propuesta/Decreto y crea el documento zip de
 * Contenido de la propuesta a partir de cinco documentos de trámites anteriores de las
 * dos primeras fases y lo asocia al trámite actual.
 */
public class CreateDocPropuestaNegAprobacionRule extends CreateDocPropuestaNegRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "SGN_NEGOCIADO";
        strQueryDocumentos = 
            "COD_TPDOC = 'Pet suministro' OR " +
            "COD_TPDOC = 'Elab pliego' OR " +
            "COD_TPDOC = 'Inf jur contrata' OR " +
            "COD_TPDOC = 'Inf jur compras' OR " +
            "COD_TPDOC = 'Inf Intervencion'" ;    
        return true;
    }

}
