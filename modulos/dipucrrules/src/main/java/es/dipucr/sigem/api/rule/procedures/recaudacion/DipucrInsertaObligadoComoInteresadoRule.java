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

public class DipucrInsertaObligadoComoInteresadoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrInsertaObligadoComoInteresadoRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	//comprueba si existe representante, si existe, elimina el obligado como interesado e inserta el representante en su lugar
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO DipucrInsertaObligadoComoInteresadoRule");
		  try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();		
	        //----------------------------------------------------------------------------------------------
	      
	        cct.beginTX();
	        //Vamos a ver si existe representante
			//Recuperamos el registro en rec_obligado
			String consultaObligado = " WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			
			IItemCollection iColObligado = entitiesAPI.queryEntities("REC_OBLIGADO", consultaObligado);
			
			if(iColObligado.next()){
				Iterator itObligado = iColObligado.iterator();
				IItem obligado = (IItem) itObligado.next();
				
				
				String nif = obligado.getString("NIF");
				if(nif == null) nif = "";
				//Si no tenemos DNI lo insertamos directamente
				
				//Comprobamos si existe ya el participante, si existe no hacemos nada				
				IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(), " NDOC = '" + nif +"'", "");

				if(!participantes.iterator().hasNext()){					
				
					String nombre = obligado.getString("NOMBRE");
					if(nombre == null) nombre = "";
					
					String calle = obligado.getString("CALLE");
					if(calle == null) calle = "";
					
					String numero = obligado.getString("NUMERO");
					if(numero == null) numero = "";
					
					String escalera = obligado.getString("ESCALERA");
					if(escalera == null) escalera = "";
					
					String planta_puerta = obligado.getString("PLANTA_PUERTA");
					if(planta_puerta == null) planta_puerta = "";
					
					String cPostal = obligado.getString("C_POSTAL");
					if(cPostal == null) cPostal = "";
					
					String ciudad = obligado.getString("CIUDAD");
					if(ciudad == null) ciudad = "";
					
					String region = obligado.getString("REGION");
					if(region == null) region = "";
					
					String email = obligado.getString("D_EMAIL");
					if(email == null) email = "";
								
					IItem nuevoParticipante = null;
					nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, rulectx.getNumExp());
					
					nuevoParticipante.set("ROL", "INT");
					nuevoParticipante.set("TIPO_PERSONA", "F");
					nuevoParticipante.set("NDOC", nif);
					nuevoParticipante.set("NOMBRE", nombre);				            
					nuevoParticipante.set("TIPO_DIRECCION", "T");
					nuevoParticipante.set("EMAIL", email);
					nuevoParticipante.set("DIRECCIONTELEMATICA", email);	 
					nuevoParticipante.set("DIRNOT", calle+" "+numero+" "+escalera+" "+planta_puerta);
					nuevoParticipante.set("C_POSTAL", cPostal);
					nuevoParticipante.set("LOCALIDAD", ciudad);
					nuevoParticipante.set("CAUT", region);
					
					try{
						nuevoParticipante.store(cct);				            	
					}
					catch(Exception e){
						logger.error(e.getMessage(), e);
						cct.endTX(false);
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