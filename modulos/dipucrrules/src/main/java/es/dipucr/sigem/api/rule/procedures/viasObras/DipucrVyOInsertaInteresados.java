package es.dipucr.sigem.api.rule.procedures.viasObras;

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

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrVyOInsertaInteresados implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrVyOInsertaInteresados.class);
	
	public void cancel(IRuleContext paramIRuleContext)
			throws ISPACRuleException { 
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx)	throws ISPACRuleException {
		try {
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------
			logger.info("INICIO - " + this.getClass().getName());
			
			String numexp = rulectx.getNumExp();
			
			IItemCollection datosObligadoCollection = entitiesAPI.getEntities("REC_OBLIGADO", numexp);
			Iterator datosObligadoIterator = datosObligadoCollection.iterator();
			while(datosObligadoIterator.hasNext()){
				IItem datosObligado = (IItem)datosObligadoIterator.next();
				
				String nifPersJuridica = datosObligado.getString("NIF");
				String nifPersFisica = datosObligado.getString("REPRES_NIF");
				
				String nifInteresado = "";
				String nombreInteresado = "";
				String calle = "";					
				String numero = "";
				String escalera = "";
				String planta_puerta = "";					
				String cPostal = "";
				String ciudad = "";
				String region = "";
				String email = "";
				String movil = "";
				String tipoPersona = "";
				
				if(StringUtils.isNotBlank(nifPersJuridica)){
					nifInteresado = nifPersJuridica;
					nombreInteresado = datosObligado.getString("NOMBRE");
					calle = datosObligado.getString("CALLE");
					numero = datosObligado.getString("NUMERO");
					escalera = datosObligado.getString("ESCALERA");
					planta_puerta = datosObligado.getString("PLANTA_PUERTA");
					cPostal = datosObligado.getString("C_POSTAL");
					ciudad = datosObligado.getString("CIUDAD");
					region = datosObligado.getString("REGION");
					email = datosObligado.getString("D_EMAIL");
					movil = datosObligado.getString("MOVIL");
					tipoPersona = "J";
				}
				else if(StringUtils.isNotBlank(nifPersFisica)){
					nifInteresado = nifPersFisica;
					nombreInteresado = datosObligado.getString("REPRES_NOMBRE");
					calle = datosObligado.getString("RCALLE");
					numero = datosObligado.getString("RNUMERO");
					escalera = datosObligado.getString("RESCALERA");
					planta_puerta = datosObligado.getString("RPLANTA_PUERTA");				
					cPostal = datosObligado.getString("RC_POSTAL");
					ciudad = datosObligado.getString("RCIUDAD");
					region = datosObligado.getString("RREGION");
					email = datosObligado.getString("REPRES_D_EMAIL");
					movil = datosObligado.getString("REPRES_MOVIL");
					tipoPersona = "F";
				}
											
				IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(), " NDOC = '" + nifInteresado +"'", "");				
				if(!participantes.iterator().hasNext() && StringUtils.isNotEmpty(nifInteresado)){
				
					if(calle == null) calle = "";
					if(numero == null) numero = "";
					if(escalera == null) escalera = "";					
					if(planta_puerta == null) planta_puerta = "";
					if(cPostal == null) cPostal = "";
					if(ciudad == null) ciudad = "";
					if(region == null) region = "";					
					if(email == null) email = "";
					if(movil == null ) movil =  "";
					
					IItem nuevoParticipante = null;
					nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, rulectx.getNumExp());
					
					nuevoParticipante.set("ROL", "INT");
					nuevoParticipante.set("TIPO_PERSONA", tipoPersona);
					nuevoParticipante.set("NDOC", nifInteresado);
					nuevoParticipante.set("NOMBRE", nombreInteresado);				            
					nuevoParticipante.set("TIPO_DIRECCION", "T");
					nuevoParticipante.set("EMAIL", email);
					nuevoParticipante.set("DIRECCIONTELEMATICA", email);
					nuevoParticipante.set("DIRNOT", calle+" "+numero+" "+escalera+" "+planta_puerta);
					nuevoParticipante.set("C_POSTAL", cPostal);
					nuevoParticipante.set("LOCALIDAD", ciudad);
					nuevoParticipante.set("CAUT", region);
					nuevoParticipante.set("TFNO_MOVIL", movil);
					nuevoParticipante.set("RECURSO", "Pers.Fis.-Empr.");
					
					try{
						nuevoParticipante.store(cct);				            	
					}
					catch(Exception e){
						logger.error("ERROR al copiar los datos del Participante: " + e.getMessage());
						throw new ISPACRuleException("ERROR al copiar los datos del Participante: " + e.getMessage(), e);						
					}		
					
					IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
					if(expediente != null){
						expediente.set("NIFCIFTITULAR", nifInteresado);
						expediente.set("IDENTIDADTITULAR", nombreInteresado);
						expediente.set("DOMICILIO", calle+" "+numero+" "+escalera+" "+planta_puerta);
						expediente.set("CIUDAD", ciudad);
						expediente.set("REGIONPAIS",region);
						expediente.set("CPOSTAL", cPostal);
						expediente.set("TFNOMOVIL", movil);
						expediente.set("DIRECCIONTELEMATICA", email);
						expediente.set("TIPOPERSONA", tipoPersona);
						expediente.set("ROLTITULAR", "INT");
						expediente.set("TIPODIRECCIONINTERESADO", "P");
						
						try{
							expediente.store(cct);				            	
						}
						catch(Exception e){
							logger.error("ERROR al copiar los datos del Interesado Principal: " + e.getMessage());
							throw new ISPACRuleException("ERROR al copiar los datos del Interesado Principal: " + e.getMessage(), e);						
						}
					}
				}				
			}			
			
			logger.info("FIN - " + this.getClass().getName());
		} catch (ISPACException e) {
			logger.error("ERROR al copiar los datos del Interesado Principal: " + e.getMessage());
			throw new ISPACRuleException("ERROR al copiar los datos del Interesado Principal: " + e.getMessage(), e);
		}
		return null;
	}

	public boolean init(IRuleContext paramIRuleContext)
			throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext paramIRuleContext)
			throws ISPACRuleException {
		return true;
	}

}
