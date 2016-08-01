package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;


/**
 * 
 * @author teresa
 * @date 17/11/2009
 * @propósito Inicializa el expediente de propuesta asociado al expediente de Convocatoria Negociado de contratación actual.
 * Asocia el documento zip de Contenido de la propuesta al expediente de Propuesta relacionado.
 * Es el mismo fichero físico zip en el repositorio de documentos
 */
public class InitPropuestaNegAprobacionRule extends InitPropuestaNegRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		STR_entidad = "SGN_NEGOCIADO";
		STR_extracto = "Aprobación de convocatoria de contratación";
		return true;
	}

}
