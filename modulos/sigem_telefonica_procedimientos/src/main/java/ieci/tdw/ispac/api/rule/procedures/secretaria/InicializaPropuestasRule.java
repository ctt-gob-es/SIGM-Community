package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.text.DecimalFormat;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class InicializaPropuestasRule implements IRule {

	private static final Logger logger = Logger.getLogger(InicializaPropuestasRule.class);
	
	@SuppressWarnings("unchecked")
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
		return true;
    }

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean cierraTramite = true;
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        	        
	        StringBuffer mensajePropuestasErroneas = new StringBuffer("Las propuestas que faltan por introducir el extracto son: ");
	        boolean mensajeErrorPropuesta = false;
	
	        //Antes de empezar se borra la lista de propuestas
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        IItemCollection collAllProps = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        Iterator<IItem> itAllProps = collAllProps.iterator();
	        logger.warn("collAllProps "+collAllProps);
	        logger.warn("itAllProps "+itAllProps);
        	IItem iProp = null;
	        while(itAllProps.hasNext()) {
	        	iProp = ((IItem)itAllProps.next());
	        	iProp.delete(cct);
	        }
	        
	        //Obtención de la lista de expedientes relacionados
	        strQuery = "WHERE NUMEXP_HIJO='" + rulectx.getNumExp() + "'";
	        IItemCollection collExpRels = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator<IItem> itExpRels = collExpRels.iterator();
	        if (! itExpRels.hasNext()) {
	        	logger.warn("No se encuentran expedientes relacionados");
	        	return false;
	        }
	        logger.warn(" strQuery "+strQuery);
	        while ( itExpRels.hasNext())
	        {
	        	IItem iExpRels = ((IItem)itExpRels.next());
		        String strExpPropuesta = iExpRels.getString("NUMEXP_PADRE");
		        logger.warn("strExpPropuesta "+strExpPropuesta);
		        if(strExpPropuesta != null){
		        
			        //Obtención de la propuesta
			        strQuery = "WHERE NUMEXP='" + strExpPropuesta + "'";
			        IItemCollection collProps = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
			        Iterator<IItem> itProps = collProps.iterator();
			        if (itProps.hasNext()) {			        	
			        	IItem iPropuesta = ((IItem)itProps.next());
			        	if(iPropuesta.getString("EXTRACTO")==null){
			        		logger.warn("No se encuentra el extracto de la propuesta: "+strExpPropuesta);
				        	mensajePropuestasErroneas.append(" ' "+strExpPropuesta+" ' ");
				        	mensajeErrorPropuesta = true;
				        	cierraTramite = false;
			        	}  	
			        }
			        else{
			        	logger.warn("No se encuentra el extracto de la propuesta: "+strExpPropuesta);
			        	mensajePropuestasErroneas.append(" ' "+strExpPropuesta+" ' ");
			        	mensajeErrorPropuesta = true;
			        	cierraTramite = false;
			        }
	        	}
	        }
	        
	        if(mensajeErrorPropuesta){
	        	rulectx.setInfoMessage(mensajePropuestasErroneas.toString());
	        	
	        }
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException){
        		logger.error("Error al cargar las propuestas."+e.getMessage(), e);
			    throw new ISPACRuleException(e);
        	}
			logger.error("Error al cargar las propuestas."+e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta",e);
        }
		logger.warn(cierraTramite);
		return cierraTramite;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        

	        //Comprobamos si la sesión no es constitutiva
	        String organo = SecretariaUtil.getOrgano(rulectx);
			String sesion = SecretariaUtil.getSesion(rulectx);
			if(organo.equals("PLEN") && sesion.equals("CONS")){
				//Se debería eliminar el expediente relacionado y cerrar el expediente.
				Iterator<IItem> itColllection = ConsultasGenericasUtil.queryEntities(rulectx, "SPAC_EXP_RELACIONADOS", "NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Sesión/Propuesta'");
				while (itColllection.hasNext()) {
					IItem exp_relacionados = (IItem) itColllection.next();
					String numexpPropuesta = exp_relacionados.getString("NUMEXP_PADRE");
					exp_relacionados.delete(cct);
					ExpedientesUtil.cerrarExpediente(cct, numexpPropuesta);
				}
			}
			else{
				StringBuffer mensajePropuestasErroneas = new StringBuffer("Las propuestas que faltan por introducir el extracto son: ");
		        boolean mensajeErrorPropuesta = false;
		
		        //Antes de empezar se borra la lista de propuestas
		        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
		        IItemCollection collAllProps = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
		        Iterator<IItem> itAllProps = collAllProps.iterator();
		        logger.warn("collAllProps "+collAllProps);
		        logger.warn("itAllProps "+itAllProps);
	        	IItem iProp = null;
		        while(itAllProps.hasNext()) {
		        	iProp = ((IItem)itAllProps.next());
		        	iProp.delete(cct);
		        }
		        
		        //Obtención de la lista de expedientes relacionados
		        strQuery = "WHERE NUMEXP_HIJO='" + rulectx.getNumExp() + "'";
		        IItemCollection collExpRels = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
				Iterator<IItem> itExpRels = collExpRels.iterator();
		        if (! itExpRels.hasNext()) {
		        	logger.warn("No se encuentran expedientes relacionados");
		        	return false;
		        }
		        logger.warn(" strQuery "+strQuery);
		        while ( itExpRels.hasNext())
		        {
		        	IItem iExpRels = ((IItem)itExpRels.next());
			        String strExpPropuesta = iExpRels.getString("NUMEXP_PADRE");
			        logger.warn("strExpPropuesta "+strExpPropuesta);
			        if(strExpPropuesta != null){
			        
				        //Obtención de la propuesta
				        strQuery = "WHERE NUMEXP='" + strExpPropuesta + "'";
				        IItemCollection collProps = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
				        Iterator<IItem> itProps = collProps.iterator();
				        if (itProps.hasNext()) {			        	
				        	IItem iPropuesta = ((IItem)itProps.next());
				            
				        	//Obtención de los datos del expediente
					        strQuery = "WHERE NUMEXP='" + strExpPropuesta + "'";
					        IItemCollection collExps = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", strQuery);
					        Iterator<IItem> itExps = collExps.iterator();
					        if (! itExps.hasNext()) {
					        	logger.warn("No se encuentra el expediente asociado a la propuesta");
					        	return false;
					        }
					        IItem iExpediente = ((IItem)itExps.next());
			
					        //Creación de nueva propuesta en la sesión
					        IItem item = entitiesAPI.createEntity("SECR_PROPUESTA",rulectx.getNumExp());
					        item.set("NUMEXP_ORIGEN", strExpPropuesta);
					        
					        String interesado = "";
					        if(iExpediente.getString("IDENTIDADTITULAR")!=null)interesado = iExpediente.getString("IDENTIDADTITULAR");
				        	item.set("INTERESADO", interesado);
				        	
				        	String origen = "";
				        	if(iPropuesta.getString("ORIGEN")!=null)origen = iPropuesta.getString("ORIGEN");
				        	item.set("ORIGEN", origen);
				        	
				        	String destino = "";
				        	if(iPropuesta.getString("DESTINO")!=null)destino = iPropuesta.getString("DESTINO");
				        	item.set("DESTINO", destino);
				        	
				        	String prioridad = "";
				        	if(iPropuesta.getString("PRIORIDAD")!=null)prioridad = iPropuesta.getString("PRIORIDAD");
				        	item.set("PRIORIDAD", prioridad);
				        	
				        	String prioridadMotivo = "";
				        	if(iPropuesta.getString("PRIORIDAD_MOTIVO")!=null)prioridadMotivo = iPropuesta.getString("PRIORIDAD_MOTIVO");
				        	item.set("PRIORIDAD_MOTIVO", prioridadMotivo);
				        	
				        	String extracto = "";
				        	if(iPropuesta.getString("EXTRACTO")!=null)extracto = iPropuesta.getString("EXTRACTO");
				        	item.set("EXTRACTO", extracto);
				        	logger.warn("extracto "+extracto);
				        	
				        	String dictamen = "";
				        	if(iPropuesta.getString("DICTAMEN")!=null)dictamen = iPropuesta.getString("DICTAMEN");
				        	item.set("DICTAMEN", dictamen);
				        	
				        	item.store(cct);
				        	
				        	logger.warn("inserccion hecha");
				        	
				        	//Actualizo el estado administrativo
				        	String strEstado = CommonFunctions.getEstadoAdmPropuesta(rulectx);
				        	iExpediente.set("ESTADOADM", strEstado);
				        	iExpediente.store(cct);
				        	
				        	
				        }
				        else{
				        	logger.warn("No se encuentra el extracto de la propuesta: "+strExpPropuesta);
				        	mensajePropuestasErroneas.append(" ' "+strExpPropuesta+" ' ");
				        	mensajeErrorPropuesta = true;
				        }
		        	}
		        }
		        
		        //Ordenación de las propuestas (de 10 en 10 para permitir insertar luego
		        //urgencias entre medias)
		        strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "' ORDER BY ORIGEN DESC";
		        collAllProps = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
		        itAllProps = collAllProps.iterator();
		        int nOrden = 10;
		        DecimalFormat df = new DecimalFormat("0000");
		        while(itAllProps.hasNext()) {
		        	iProp = ((IItem)itAllProps.next());
		        	iProp.set("ORDEN", df.format(nOrden));
		        	iProp.store(cct);
		        	nOrden = nOrden + 10;
		        }	        
	        	
			}
	        
			return true;
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException){
        		logger.error("Error al cargar las propuestas."+e.getMessage(), e);
			    throw new ISPACRuleException(e);
        	}
			logger.error("Error al cargar las propuestas."+e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta",e);
        }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}
	
}