package es.dipucr.sigem.api.rule.procedures.personal;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class NumConvocatoriaRule implements IRule{
	
	protected static final Logger LOGGER = Logger.getLogger(NumConvocatoriaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			Iterator<IItem> itdatosConcurs = ConsultasGenericasUtil.queryEntities(rulectx, "PERSONAL_DATOS_CONCURSO", "NUMEXP IS NOT NULL ORDER BY ID DESC");
			int numConv = 1;
			if(itdatosConcurs.hasNext()){
				IItem personalDatos = itdatosConcurs.next();
				String numConvocatoria = "";
				if(personalDatos.getString("NUMCONV")!=null){
					numConvocatoria = personalDatos.getString("NUMCONV");
					try{
						numConv = Integer.parseInt(numConvocatoria);
						numConv = numConv + 1;
					}
					catch (NumberFormatException e) {
						LOGGER.error("Expediente. "+rulectx.getNumExp()+" el formato de la última convocatoria es errónea "+numConvocatoria+" con el expediente. "+personalDatos.getString("NUMEXP"));
					}
					
				}
			}
			IItem personalDatosConcurso = entitiesAPI.createEntity("PERSONAL_DATOS_CONCURSO", rulectx.getNumExp());
			personalDatosConcurso.set("NUMCONV", numConv);
			personalDatosConcurso.store(cct);
		} catch (ISPACException e) {
			LOGGER.error("Error al almacenar en la tabla PERSONAL_DATOS_CONCURSO, en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al almacenar en la tabla PERSONAL_DATOS_CONCURSO, en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

}
