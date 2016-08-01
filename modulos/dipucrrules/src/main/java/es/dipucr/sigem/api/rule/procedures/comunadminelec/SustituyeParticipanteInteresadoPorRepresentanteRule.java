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

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class SustituyeParticipanteInteresadoPorRepresentanteRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(SustituyeParticipanteInteresadoPorRepresentanteRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	/**
	 * [eCenpri-Felipe #947]
	 * Comprueba si existe representante en la entidad INTERESADO_REPRESENTANTE.
	 * Si existe, elimina el interesado como participante e inserta el representante en su lugar
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		logger.info("INICIO SustituyeParticipanteInteresadoPorRepresentanteRule");
		
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();		
	        //----------------------------------------------------------------------------------------------
	      
	        cct.beginTX();
	        
	        //Vamos a ver si existe representante
			//Recuperamos el registro en interesado_representante
			String strQueryRepresentante = " WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
			IItemCollection collectionIntRep = entitiesAPI.queryEntities
					("INTERESADO_REPRESENTANTE", strQueryRepresentante);
			
			if(collectionIntRep.next()){
				
				IItem representante = (IItem) collectionIntRep.iterator().next();
				
				String nombreRepre = representante.getString("REP_NOMBRE");
				
				//Si tenemos representante borramos el interesado e insertamos el representante
				if(StringUtils.isNotEmpty(nombreRepre)){
					
					String nifRepre = representante.getString("REP_NIFCIF");
					if(nifRepre == null) nifRepre = "";
					
					String direccionRepre = representante.getString("REP_DIRECCION");
					if(direccionRepre == null) direccionRepre = "";
					
					String cpostalRepre = representante.getString("REP_CPOSTAL");
					if(cpostalRepre == null) cpostalRepre = "";
					
					String localidadRepre = representante.getString("REP_LOCALIDAD");
					if(localidadRepre == null) localidadRepre = "";
					
					String regionRepre = representante.getString("REP_PAIS");
					if(regionRepre == null) regionRepre = "";
					
					String emailRepre = representante.getString("REP_EMAIL");
					if(emailRepre == null) emailRepre = "";

					//Si no tenemos DNI no borramos el interesado ni comprobamos si existe el representante, lo insertamos directamente
					String nifInteresado = representante.getString("INT_NIFCIF");
					//Comprobamos si existe ya el participante, si existe no hacemos nada
					IItemCollection collectionParticipantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(), " NDOC = '" + nifInteresado + "'", "");

					if(collectionParticipantes.iterator().hasNext()){
						//Si existe el interesado lo borramos
						IItem itemInteresado = (IItem) collectionParticipantes.iterator().next();
						itemInteresado.delete(cct);
					}
								        	
					//Comprobamos si existe ya el participante, si existe no hacemos nada					
					collectionParticipantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(), " NDOC = '" + nifRepre + "'", "");
					
					if(!collectionParticipantes.iterator().hasNext() || nifRepre.equals("")){ //Esto lo tenía así Manu, no lo acabo de entender
					
						IItem nuevoParticipante = null;
						nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, rulectx.getNumExp());
						
						nuevoParticipante.set("ROL", "INT");
						nuevoParticipante.set("TIPO_PERSONA", "F");
						nuevoParticipante.set("NDOC", nifRepre);
						nuevoParticipante.set("NOMBRE", nombreRepre);				            
						nuevoParticipante.set("TIPO_DIRECCION", "T");
						nuevoParticipante.set("EMAIL", emailRepre);
						nuevoParticipante.set("DIRECCIONTELEMATICA", emailRepre);	 
						nuevoParticipante.set("DIRNOT", direccionRepre);
						nuevoParticipante.set("C_POSTAL", cpostalRepre);
						nuevoParticipante.set("LOCALIDAD", localidadRepre);
						nuevoParticipante.set("CAUT", regionRepre);
						
						try{
							nuevoParticipante.store(cct);				            	
						}
						catch(Exception e){
							logger.error("Error al insertar el participante con NIF " + nifRepre + " en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
							cct.endTX(false);
							throw new ISPACException("Error al insertar el participante con NIF " + nifRepre + " en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
						}				
					}
				}
			}
			cct.endTX(true);
		} catch (ISPACException e) {		
			logger.error("Error al sustituir el participante INTERESADO  de la entidad INTERESADO_REPRESENTANTE por el participante REPRESENTANTE en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);

		}
		logger.info("FIN SustituyeParticipanteInteresadoPorRepresentanteRule");
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return false;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}