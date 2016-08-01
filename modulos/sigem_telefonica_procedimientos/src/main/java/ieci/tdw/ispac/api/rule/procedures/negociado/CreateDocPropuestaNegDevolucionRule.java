package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * @date 13/11/2009
 * @propósito Actualiza el campo estado de la entidad para mostrar los enlaces de Propuesta/Decreto.
 * No crea el documento zip de Contenido de la propuesta.
 */
public class CreateDocPropuestaNegDevolucionRule extends CreateDocPropuestaNegRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		STR_entidad = "SGN_NEGOCIADO";
		STR_queryDocumentos = 
			"COD_TPDOC = 'Inf servicio' OR " +
			"COD_TPDOC = 'Inf Intervencion'" ;	
		return true;
	}

}
