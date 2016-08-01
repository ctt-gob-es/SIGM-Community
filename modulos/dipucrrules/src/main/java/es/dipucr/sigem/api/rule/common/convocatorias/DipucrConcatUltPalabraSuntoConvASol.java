package es.dipucr.sigem.api.rule.common.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrConcatUltPalabraSuntoConvASol implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrConcatUltPalabraSuntoConvASol.class);
	
	private final String CODIGO_CONVOCATORIA = "SERSO-SOLASSAL"; 

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		IItem expedienteSolicitud = null;
		String numexpsolicitud = "";
		
		String ultimaPalabra = "";		
		String numexpGenerico = rulectx.getNumExp();
		
		try {
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			try{
				IItem expedienteGenerico = ExpedientesUtil.getExpediente(cct, numexpGenerico);
				
				IItemCollection expedientesCollection = ExpedientesUtil.queryExpedientes(cct, "WHERE NREG = '" + expedienteGenerico.getString("NREG") + "' ORDER BY ID DESC");					
				Iterator<?> expedientesIterator = expedientesCollection.iterator();
				
				if(expedientesIterator.hasNext()){
					expedienteSolicitud = (IItem) expedientesIterator.next();
					numexpsolicitud = expedienteSolicitud.getString("NUMEXP");					
									
					IItemCollection solicitudCollection = entitiesAPI.getEntities("DPCR_SOL_CONV_SUB", numexpsolicitud);
					Iterator<?> solicitudIterator = solicitudCollection.iterator();
					if(solicitudIterator.hasNext()){
						IItem solicitud = (IItem)solicitudIterator.next();
						String asuntoConvocatoria = solicitud.getString("CONVOCATORIA");
		
						if(StringUtils.isNotEmpty(asuntoConvocatoria)){
							String[] asuntoConvocatoriaSplit = asuntoConvocatoria.split(" ");
							ultimaPalabra = asuntoConvocatoriaSplit[asuntoConvocatoriaSplit.length -1];
						}
					}
				}
			}
			catch(Exception e){
				logger.error("Error al recuperar la última palabra de la convocatoria asociada al expediente: " + numexpsolicitud + ". " + e.getMessage(), e);
			}
			
			if(StringUtils.isNotEmpty(ultimaPalabra) && null != expedienteSolicitud && CODIGO_CONVOCATORIA.equals(expedienteSolicitud.getString("CODPROCEDIMIENTO"))){
				if(null != expedienteSolicitud){
					String asunto = expedienteSolicitud.getString("ASUNTO");
					expedienteSolicitud.set("ASUNTO" , ultimaPalabra + " - " + asunto);
					expedienteSolicitud.store(cct);
				}
			}

		} catch (ISPACException e) {
			logger.error("Error al recuperar o al asociar la última palabra de la convocatoria al expediente: " + numexpsolicitud + ". " + e.getMessage(), e);
		}
		
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
