package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [eCenpri-Felipe #828]
 * @author Felipe
 * @since 12.02.13
 * Nos solicitan desde el BOP que el estado del anuncio sea siempre por defecto "Aceptado"
 * Creamos un método genérico por si en futuro nos solicitan poner por defecto algún campo más 
 */
public class SetInteresadoFirmanteBopRule implements IRule {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(SetInteresadoFirmanteBopRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
				
        try{
			//----------------------------------------------------------------------------------------------
        	ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        String strQuery = null;
	        IItemCollection collection = null;
	        IItem itemExpediente = null;
	        IItem itemSolicitud = null;
	        IItem itemParticipante = null;
	        String cifEntidad = null;
	        String nombreEntidad = null;
	        String nifFirmante = null;
	        String nombreFirmante = null;
	        String cargoFirmante = null;
	        String numexp = null;
	        String nreg = null;
	        
	        //Obtenemos el número de expediente
	        numexp = rulectx.getNumExp();

	        //Comprobamos que viene del registro telemático
	        //Si no tiene número de registro, no hacemos nada
	        itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
	        nreg = itemExpediente.getString("NREG");
	        if (StringUtils.isEmpty(nreg)){
	        	return new Boolean(false);
	        }
			
	        //Recuperamos los valores de la solicitud
	        strQuery = "WHERE NUMEXP = '"+ numexp +"'";
	        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
	        itemSolicitud = (IItem) collection.iterator().next();
	        
	        cifEntidad = itemSolicitud.getString("CIF_ENTIDAD");
	        nombreEntidad = itemSolicitud.getString("ENTIDAD");
	        cargoFirmante = itemSolicitud.getString("CARGO_FIRMANTE");
	        
	        //Recuperamos los valores el expediente
	        nifFirmante = itemExpediente.getString("NIFCIFTITULAR");
	        nombreFirmante = itemExpediente.getString("IDENTIDADTITULAR");
	        
	        itemExpediente.set("NIFCIFTITULAR", cifEntidad);
	        itemExpediente.set("IDENTIDADTITULAR", nombreEntidad);
	        itemExpediente.store(cct);
	        
	        //Creamos el participante con los datos del firmante
	        itemParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, numexp);
	        itemParticipante.set("NDOC", nifFirmante);
	        itemParticipante.set("NOMBRE", nombreFirmante);
	        if (StringUtils.isNotEmpty(cargoFirmante)){
	        	itemParticipante.set("OBSERVACIONES", "CARGO: " + cargoFirmante);
	        }
	        itemParticipante.store(cct);
	        
	        return new Boolean(true);
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error al actualizar el interesado principal " +
	        		"y el participante/firmante del anuncio.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
