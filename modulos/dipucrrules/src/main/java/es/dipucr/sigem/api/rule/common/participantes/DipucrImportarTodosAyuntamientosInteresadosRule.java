package es.dipucr.sigem.api.rule.common.participantes;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrImportarTodosAyuntamientosInteresadosRule implements IRule {

	/******
	 * La consulta para generar el script de carga de todos los participantes de un expediente en otro es:
	 * select 'INSERT INTO spac_dt_intervinientes('||
            'id, id_ext, numexp, rol, tipo_persona, ndoc, nombre, dirnot, '||
            'c_postal, localidad, caut, tipo_direccion, '||
            'iddireccionpostal) VALUES ('||
            id||',\''|| 
            case when id_ext = null then '' else id_ext end||'\', \'[NUMEXP DESTINO]\', \''||
            case when rol = null then '' else rol end||'\', \''||
            case when tipo_persona = null then '' else tipo_persona end||'\', \''||
            case when ndoc = null then '' else ndoc end||'\', \''||
            case when nombre = null then '' else nombre end||'\', \''||
            case when dirnot = null then '' else dirnot end||'\', \''||
            case when c_postal = null then '' else c_postal end||'\', \''||
            case when localidad = null then '' else localidad end||'\', \''||
            case when caut = null then '' else caut end||'\', \''||
            case when tipo_direccion = null then '' else tipo_direccion end||'\', \''||
            case when iddireccionpostal = null then '' else iddireccionpostal end||'\');'
            from spac_dt_intervinientes where numexp='[NUMEXP ORIGEN]'
     *
     *[NUMEXP DESTINO] Número de expediente en el que deseamos importar los participantes
     *[NUMEXP ORIGEN] Número de expediente cuyos participantes deseamos importar
     *
	 */
	
	private static final Logger logger = Logger.getLogger(DipucrImportarTodosAyuntamientosInteresadosRule.class);
	
	//Expediente con todos los ayuntamientos en DESARROLLO
	private final String expedienteCuyosParticipantesImportamos = "DPCR2011/3793";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			logger.info("INICIO - " + this.getClass().getName());
			ClientContext cct = (ClientContext) rulectx.getClientContext();
		    IInvesflowAPI invesFlowAPI = cct.getAPI();
		    
		    ParticipantesUtil.importarParticipantes(cct, invesFlowAPI.getEntitiesAPI(),expedienteCuyosParticipantesImportamos,rulectx.getNumExp() );
			logger.info("FIN - " + this.getClass().getName());
			
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}	
		return true;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return false;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
