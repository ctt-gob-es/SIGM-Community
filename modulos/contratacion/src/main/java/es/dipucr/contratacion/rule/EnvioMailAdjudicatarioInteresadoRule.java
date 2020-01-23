package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.objeto.sw.Peticion;
import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;
import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class EnvioMailAdjudicatarioInteresadoRule extends DipucrEnviaDocEmailConAcuse {
	
	public static final Logger LOGGER = Logger.getLogger(EnvioMailAdjudicatarioInteresadoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
			String numexp = itemDocumento.getString("NUMEXP");

			LOGGER.warn("numexp. "+numexp);
			
			if(StringUtils.isNotEmpty(numexp)){
				Peticion peticion = DipucrFuncionesComunesSW.getPeticion(rulectx.getClientContext(), numexp);
				
				
				if(peticion!=null){
					IItemCollection itColl = ParticipantesUtil.getParticipantesByRol(rulectx.getClientContext(), numexp, ParticipantesUtil._TIPO_INTERESADO);
					Iterator<IItem> itColAdjudica = itColl.iterator();
					while(itColAdjudica.hasNext()){
						IItem particAdj = itColAdjudica.next();
						String email = "";
						if(StringUtils.isNotEmpty(particAdj.getString("DIRECCIONTELEMATICA")))email = particAdj.getString("DIRECCIONTELEMATICA");
						String nombre = "";
						if(StringUtils.isNotEmpty(particAdj.getString("NOMBRE")))nombre = particAdj.getString("NOMBRE");
						//Se adjunta la direccion de la persona de tesoreria a la que hay que mandarle el email
						LOGGER.warn("numexp. "+numexp+" nombre "+nombre);
						LOGGER.warn("numexp. "+numexp+" email "+email);
						if(StringUtils.isNotEmpty(email)){
							VAR_EMAILS = email;
							LOGGER.warn("numexp. "+numexp+" email "+VAR_EMAILS);						
							String motivoPeticion = "";
							if(StringUtils.isNotEmpty(peticion.getObjetoContrato()))motivoPeticion = peticion.getObjetoContrato();
							String total = "";
							if(StringUtils.isNotEmpty(peticion.getPresupuestoConIva()))total = peticion.getPresupuestoConIva();
							String servicioResponsable = "";
							if(StringUtils.isNotEmpty(peticion.getServicioResponsable()))servicioResponsable = peticion.getServicioResponsable();
							 
							contenido = nombre+ "\n"
									+ "Se le comunica que se le ha adjudicado el contrato de '"+motivoPeticion+"' por importe de "+total+" IVA incluído.\n"
									+ "Debe ponerse en contacto a la mayor brevedad posible con el "+servicioResponsable+" para su correcta ejecución.\n\n"
									+ "Un cordial saludo.\n\n"
									+ "Diputación Provincial Ciudad Real";

							asunto = "Aprobación del gasto en el contrato '"+motivoPeticion+"'";
							
							conDocumento = false;
						}
						
					}
				}
			}
				 
		 } catch (ISPACException e) {
			 LOGGER.error("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		return true;
	}
}
