package es.dipucr.sigem.api.rule.procedures.comunadminelec;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class InsertaInteresadoComoParticipanteRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(InsertaInteresadoComoParticipanteRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	/**
	 * [eCenpri-Felipe #947]
	 * Inserta el Interesado de la entidad INTERESADO_REPRESENTANTE en la pestaña Participantes
	 */
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		logger.info("INICIO - " + this.getClass().getName());
		
		try {
			
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------

			cct.beginTX();
			
			// Vamos a ver si existe representante
			// Recuperamos el registro en interesado_representante
			String strQueryRepresentante = " WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
			IItemCollection collectionIntRep = entitiesAPI.queryEntities
					("INTERESADO_REPRESENTANTE", strQueryRepresentante);

			if (collectionIntRep.next()) {
				
				Iterator itIntRep = collectionIntRep.iterator();
				IItem itemIntRep = (IItem) itIntRep.next();

				String nif = itemIntRep.getString("INT_NIFCIF");
				
				//Si hay interesado (tiene que haberlo) seguimos ejecutanto
				if (StringUtils.isNotEmpty(nif)){

					// Comprobamos si existe ya el participante, si existe no hacemos nada
					IItemCollection collectionParticipantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(), " NDOC = '" + nif + "'", "");
	
					if (!collectionParticipantes.iterator().hasNext()) {
	
						String nombre = itemIntRep.getString("INT_NOMBRE");
						if (nombre == null) nombre = "";
	
						String direccion = itemIntRep.getString("INT_DIRECCION");
						if (direccion == null) direccion = "";
	
						String cPostal = itemIntRep.getString("INT_CPOSTAL");
						if (cPostal == null) cPostal = "";
	
						String localidad = itemIntRep.getString("INT_LOCALIDAD");
						if (localidad == null) localidad = "";
	
						String region = itemIntRep.getString("INT_PAIS");
						if (region == null)	region = "";
	
						String email = itemIntRep.getString("INT_EMAIL");
						if (email == null) email = "";
	
						IItem nuevoParticipante = null;
						nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, rulectx.getNumExp());
	
						nuevoParticipante.set("ROL", "INT");
						nuevoParticipante.set("TIPO_PERSONA", "F");
						nuevoParticipante.set("NDOC", nif);
						nuevoParticipante.set("NOMBRE", nombre);
						nuevoParticipante.set("TIPO_DIRECCION", "T");
						nuevoParticipante.set("EMAIL", email);
						nuevoParticipante.set("DIRECCIONTELEMATICA", email);
						nuevoParticipante.set("DIRNOT", direccion);
						nuevoParticipante.set("C_POSTAL", cPostal);
						nuevoParticipante.set("LOCALIDAD", localidad);
						nuevoParticipante.set("CAUT", region);
	
						try {
							nuevoParticipante.store(cct);
						} catch (Exception e) {
							logger.error("Error al insertar el participante con NIF " + nif + " en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
							cct.endTX(false);
							throw new ISPACException("Error al insertar el participante con NIF " + nif + " en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
						}
					}
				}
			}
			cct.endTX(true);
		} catch (ISPACException e) {
			logger.error("Error al insertar el interesado principal" +
					" de la entidad INTERESADO_REPRESENTANTE en la tabla de participantes en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al insertar el interesado principal" +
					" de la entidad INTERESADO_REPRESENTANTE en la tabla de participantes en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		logger.info("FIN - " + this.getClass().getName());
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return false;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}