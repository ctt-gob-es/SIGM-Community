package es.dipucr.sigem.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrCambiaParticipanteObligadoRepresentanteRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrCambiaParticipanteObligadoRepresentanteRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	//comprueba si existe representante, si existe, elimina el obligado como interesado e inserta el representante en su lugar
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		  try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();		
	        //----------------------------------------------------------------------------------------------
	      
	        cct.beginTX();
	        //Vamos a ver si existe representante
			//Recuperamos el registro en rec_obligado
			String consutlaRepresentante = " WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			
			IItemCollection iColRepresentante = entitiesAPI.queryEntities("REC_OBLIGADO", consutlaRepresentante);
			
			if(iColRepresentante.next()){
				Iterator itRepresentante = iColRepresentante.iterator();
				IItem representante = (IItem) itRepresentante.next();
				String nombreRepre = representante.getString("REPRES_NOMBRE");
				//Si tenemos representante borramos el obligado e insertamos el representante
				if( nombreRepre != null && !nombreRepre.equals("")){
					String nifRepre = representante.getString("REPRES_NIF");
					if(nifRepre == null) nifRepre = "";
					
					String rCalle = representante.getString("RCALLE");
					if(rCalle == null) rCalle = "";
					
					String rNumero = representante.getString("RNUMERO");
					if(rNumero == null) rNumero = "";
					
					String rEscalera = representante.getString("RESCALERA");
					if(rEscalera == null) rEscalera = "";
					
					String rPlanta_puerta = representante.getString("RPLANTA_PUERTA");
					if(rPlanta_puerta == null) rPlanta_puerta = "";
					
					String rCPostal = representante.getString("RC_POSTAL");
					if(rCPostal == null) rCPostal = "";
					
					String rciudad = representante.getString("RCIUDAD");
					if(rciudad == null) rciudad = "";
					
					String rregion = representante.getString("RREGION");
					if(rregion == null) rregion = "";
					
					String repre_email = representante.getString("REPRES_D_EMAIL");
					if(repre_email == null) repre_email = "";

					//Si no tenemos DNI no borramos el obligado ni comprobamos si existe el representante, lo insertamos directamente
					String nifObligado = representante.getString("NIF");
					
					//Comprobamos si existe ya el participante, si existe no hacemos nada
					IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(), " NDOC = '" +nifObligado+"'", "");

					if(participantes.iterator().hasNext()){
						//Si existe el obligado lo borramos
						IItem obligado = (IItem) participantes.iterator().next();
						obligado.delete(cct);
					}
								        	
					//Comprobamos si existe ya el participante, si existe no hacemos nada					
					participantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(), " NDOC = '" +representante.getString("REPRES_NIF")+"'", "");
					
					if(!participantes.iterator().hasNext() || nifRepre.equals("")){
					
						IItem nuevoParticipante = null;
						nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, rulectx.getNumExp());
						
						nuevoParticipante.set("ROL", "INT");
						nuevoParticipante.set("TIPO_PERSONA", "F");
						nuevoParticipante.set("NDOC", nifRepre);
						nuevoParticipante.set("NOMBRE", nombreRepre);				            
						nuevoParticipante.set("TIPO_DIRECCION", "T");
						nuevoParticipante.set("EMAIL", repre_email);
						nuevoParticipante.set("DIRECCIONTELEMATICA", repre_email);	 
						nuevoParticipante.set("DIRNOT", rCalle+" "+rNumero+" "+rEscalera+" "+rPlanta_puerta);
						nuevoParticipante.set("C_POSTAL", rCPostal);
						nuevoParticipante.set("LOCALIDAD", rciudad);
						nuevoParticipante.set("CAUT", rregion);
						
						try{
							nuevoParticipante.store(cct);				            	
						}
						catch(Exception e){
							logger.error(e.getMessage(), e);
							cct.endTX(false);
						}				
					}
				}
			}
			cct.endTX(true);
		} catch (ISPACException e) {		
			logger.error(e.getMessage(), e);
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