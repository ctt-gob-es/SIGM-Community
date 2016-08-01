package es.dipucr.sigem.api.rule.common;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

/**
 * [dipucr-Felipe #1136]
 * Regla que inserta el Interesado principal como participante (notificado)
 * y en la pestaña Interesado/Participante
 * @author Felipe
 *
 */
public class DipucrInsertaInteresadoIntRepYParticipante implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			String numexp = rulectx.getNumExp();
			
			IItem itemExpediente = rulectx.getItem();
			String nifcif = itemExpediente.getString("NIFCIFTITULAR");
			
			//Si está cargado el interesado en la pestaña de expediente
			if (!StringUtils.isEmpty(nifcif)){
				
				//Recuperamos sus datos
				String nombre = itemExpediente.getString("IDENTIDADTITULAR");
				if (null == nombre) nombre = "";

				String direccion = itemExpediente.getString("DOMICILIO");
				if (null == direccion) direccion = "";

				String cPostal = itemExpediente.getString("CPOSTAL");
				if (null == cPostal) cPostal = "";

				String localidad = itemExpediente.getString("CIUDAD");
				if (null == localidad) localidad = "";

				String region = itemExpediente.getString("REGIONPAIS");
				if (null == region)	region = "";

				String email = itemExpediente.getString("DIRECCIONTELEMATICA");
				if (null == email) email = "";
				
				//Inserción en la pestaña Interesado/Representante
				//------------------------------------------------
				String strQueryIntRep = " WHERE NUMEXP = '" + numexp + "'";
				IItemCollection collectionIntRep = entitiesAPI.queryEntities
						("INTERESADO_REPRESENTANTE", strQueryIntRep);
				
				//Sólo lo sobreescribimos si no hay nada escrito
				if (!collectionIntRep.next()){
				
					//Creación
					IItem nuevoIntRep = entitiesAPI.createEntity("INTERESADO_REPRESENTANTE", numexp);
					
					nuevoIntRep.set("INT_NIFCIF", nifcif);
					nuevoIntRep.set("INT_NOMBRE", nombre);
					nuevoIntRep.set("INT_DIRECCION", direccion);
					nuevoIntRep.set("INT_CPOSTAL", cPostal);
					nuevoIntRep.set("INT_LOCALIDAD", localidad);
					nuevoIntRep.set("INT_PAIS", region);
					nuevoIntRep.set("INT_EMAIL", email);
					
					nuevoIntRep.store(cct);
				}
				
				//Inserción en la pestaña de Participantes como Notificado
				//--------------------------------------------------------
				// Comprobamos si existe ya el participante, si existe no hacemos nada
				IItemCollection collectionParticipantes = ParticipantesUtil.getParticipantes
						(cct, numexp, " NDOC = '" + nifcif + "'", "");

				if (!collectionParticipantes.iterator().hasNext()) {
					
					IItem nuevoParticipante = null;
					nuevoParticipante = entitiesAPI.createEntity
							(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, numexp);

					nuevoParticipante.set("ROL", "INT");
					nuevoParticipante.set("TIPO_PERSONA", "F");
					nuevoParticipante.set("NDOC", nifcif);
					nuevoParticipante.set("NOMBRE", nombre);
					nuevoParticipante.set("TIPO_DIRECCION", "T");
					nuevoParticipante.set("EMAIL", email);
					nuevoParticipante.set("DIRECCIONTELEMATICA", email);
					nuevoParticipante.set("DIRNOT", direccion);
					nuevoParticipante.set("C_POSTAL", cPostal);
					nuevoParticipante.set("LOCALIDAD", localidad);
					nuevoParticipante.set("CAUT", region);
					
					nuevoParticipante.store(cct);
				}
				
			}
			
		}
		catch(Exception ex){
			throw new ISPACRuleException("Error al insertar el interesado principal "
					+ "en las pestañas IntRep y Participantes: " + ex.getMessage(), ex);
		}

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
