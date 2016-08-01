package es.dipucr.sigem.api.rule.common.participantes;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tecdoc.sgm.tram.thirdparty.SigemThirdPartyAPI;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrInsertarAyuntamientoAsesoramiento implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrInsertarAyuntamientoAsesoramiento.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings( "deprecation" )
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();			
	        
	    	
	    	//Buscar los idTraslado de los dos participantes Ayuntamiento y Juzgado.
	    	// Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			AccesoBBDDRegistro accsRegistro = new AccesoBBDDRegistro(entidad);
			
			String sQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
			IItemCollection represen = entitiesAPI.queryEntities(Constants.TABLASBBDD.ASES_EMISION, sQuery);
			Iterator<?> iReresent = represen.iterator();
			
			String ayuntamiento = "";
			
			if(iReresent.hasNext()){
				IItem itRepre = (IItem) iReresent.next();
				if(itRepre.getString("AYUNTAMIENTO") != null) ayuntamiento = itRepre.getString("AYUNTAMIENTO");
			}
			logger.debug("Ayuntamiento = "+ayuntamiento);
			
			String idInteresadoAyuntamiento = accsRegistro.getIdParticipanteJuridico("AYUNTAMIENTO DE "+ayuntamiento.toUpperCase());
	    	
	        		    
	        IItem nuevoParticipante = null;
			SigemThirdPartyAPI servicioTerceros = new SigemThirdPartyAPI();
			
			IThirdPartyAdapter terceroAyuntamiento = servicioTerceros.lookupById(idInteresadoAyuntamiento);
	
					    			            	   		       
            try{
            	cct.beginTX();
            	if(terceroAyuntamiento != null && terceroAyuntamiento.getIdExt()!= null){
            		logger.debug("Ayuntamiento");
    				ParticipantesUtil.insertarParticipanteById(rulectx, rulectx.getNumExp(), idInteresadoAyuntamiento, ParticipantesUtil._TIPO_INTERESADO, ParticipantesUtil._TIPO_PERSONA_JURIDICA, "");	            
            	}
            	else{
            		nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, rulectx.getNumExp());	        
 			        nuevoParticipante.set("ROL", "INT");
 			        nuevoParticipante.set("NOMBRE", "AYUNTAMIENTO DE "+ayuntamiento.toUpperCase());	
 			       nuevoParticipante.set("DIRNOT", "Toledo, 17");	
 	            	nuevoParticipante.store(cct);
            	}
            	cct.endTX(true);
            }
            catch(Exception e){
            	logger.error("Error al guardar el participante con id : " + terceroAyuntamiento.getIdExt() + ", en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
            	cct.endTX(false);
            }	        
		} catch (ISPACException e) {		
			logger.error("Error al guardar el participante en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
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