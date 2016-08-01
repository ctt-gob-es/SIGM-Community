package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class CargaLicitadoresRule  implements IRule{
	private static final Logger logger = Logger.getLogger(CargaLicitadoresRule.class);

	public void cancel(IRuleContext arg0) throws ISPACRuleException {
	
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try {
			/*************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			 /***********************************************************/
			
			logger.warn("INICIO CargaLicitadoresRule");
			String consulta = "WHERE NUMEXP_HIJO = '"+rulectx.getNumExp()+"' AND RELACION='Contratación/Licitador'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", consulta);
	        Iterator<IItem> it = collection.iterator();
	        String numexp_licitador = "";
	        while (it.hasNext()){
	        	IItem contrato = (IItem)it.next();
	        	numexp_licitador = contrato.getString("NUMEXP_PADRE");
	        	logger.warn("numexp_licitador "+numexp_licitador);
	        	//Saco el participante de ese expediente
				IItem collectionExp= entitiesAPI.getExpedient(numexp_licitador);
				String nombreParticipante = collectionExp.getString("IDENTIDADTITULAR");
				
				consulta = "WHERE NOMBRE = '"+nombreParticipante+"'";
				IItemCollection collectionLicitador = entitiesAPI.queryEntities("CONTRATACION_LICITADORES", consulta);
		        Iterator<IItem> itLicitador = collectionLicitador.iterator();
		        if (!itLicitador.hasNext()){
					IItem registro = entitiesAPI.createEntity("CONTRATACION_LICITADORES","");
		     		registro.set("NUMEXP", rulectx.getNumExp());
		     		registro.set("NOMBRE", nombreParticipante);
		     		registro.set("SUBS_DOCUMENTOS", "No");
		     		registro.set("VAL_ANOR_DESPROPOR", "No");
		     		registro.store(cct);
		     		
		     		ParticipantesUtil.importarParticipantes(cct, entitiesAPI, numexp_licitador, rulectx.getNumExp());
		        }

	        }
			logger.warn("FIN CargaLicitadoresRule");
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return null;
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

}
