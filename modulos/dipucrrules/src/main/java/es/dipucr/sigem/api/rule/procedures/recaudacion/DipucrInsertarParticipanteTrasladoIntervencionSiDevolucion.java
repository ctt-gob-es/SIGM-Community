package es.dipucr.sigem.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.participantes.DipucrInsertarParticipanteTrasladoIntervencionRule;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrInsertarParticipanteTrasladoIntervencionSiDevolucion extends DipucrInsertarParticipanteTrasladoIntervencionRule{
	
	private static final Logger logger = Logger.getLogger(DipucrInsertarParticipanteTrasladoIntervencionSiDevolucion.class);
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();			
	        //----------------------------------------------------------------------------------------------
	        		    
	        //Comprobamos si tiene devolución, si la tiene insertamos el participante, si no no hacemos nada, si sí, insertamos intervención.
	        String numexp = rulectx.getNumExp();
	        
	        IItemCollection devolucionesCollection = entitiesAPI.getEntities("REC_DAT_DEVOL", numexp);
	        Iterator<?> devolucionesIterator = devolucionesCollection.iterator();
	        boolean tieneDevolucion = false;
	        
	        while(devolucionesIterator.hasNext()){
	        	IItem devolucion = (IItem) devolucionesIterator.next();
	        	String importe = devolucion.getString("IMPORTE");
	        	
	        	//En el momento que uno de los registros tenga devolución ya insertamos el traslado
	        	if(importe != null && !importe.equals("") && Double.parseDouble(importe)>0.0){
	        		tieneDevolucion = true;
	        	}
	        }
	        
	        if(tieneDevolucion){
	        	if(StringUtils.isNotEmpty(idTraslado))
	        		ParticipantesUtil.insertarParticipanteById(rulectx, rulectx.getNumExp(), idTraslado, ParticipantesUtil._TIPO_TRASLADO, ParticipantesUtil._TIPO_PERSONA_FISICA, email);
	        }
		} catch (ISPACException e) {		
        	logger.error("Error al guardar el participante en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		logger.info("FIN - " + this.getClass().getName());
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {	
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}