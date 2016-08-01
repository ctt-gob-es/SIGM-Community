package es.dipucr.sigem.api.rule.procedures.secretaria;

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

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class CargaPropuestaUrgencia implements IRule {

	private static final Logger logger = Logger.getLogger(CargaPropuestaUrgencia.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
    		//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	      //Antes de empezar se borra la lista de propuestas
	        //logger.warn("Elimino los datos de la tabla SECR_URGENCIAS");
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        IItemCollection collAllProps = entitiesAPI.queryEntities("SECR_URGENCIAS", strQuery);
	        Iterator itAllProps = collAllProps.iterator();
        	IItem iProp = null;
	        while(itAllProps.hasNext()) {
	        	iProp = ((IItem)itAllProps.next());
	        	iProp.delete(cct);
	        }
	        
	        String numExp = rulectx.getNumExp();
	        
	        //Obtención de la lista de expedientes relacionados
	        strQuery = "WHERE NUMEXP_HIJO='" + numExp + "'";
	        IItemCollection collExpRels = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
	        Iterator itExpRels = collExpRels.iterator();
	        if (! itExpRels.hasNext()) {
	        	return new Boolean(false);
	        }
	        while ( itExpRels.hasNext())
	        {
	        	IItem iExpRels = ((IItem)itExpRels.next());
		        String strExpPropuesta = iExpRels.getString("NUMEXP_PADRE");
		        logger.warn("strExpPropuesta "+strExpPropuesta);
		        
		        //Obtención de la propuesta
		        strQuery = "WHERE NUMEXP='" + strExpPropuesta + "'";
		        IItemCollection collProps = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
		        Iterator itProps = collProps.iterator();
		        //Comprueba porque como m
		        if (! itProps.hasNext()) {
		        	return new Boolean(false);
		        }
	        	IItem iPropuesta = ((IItem)itProps.next());
	        	
	        	//COmpruebo que esa propuesta no vaya en la convocatoria
	        	strQuery = "WHERE NUMEXP_ORIGEN='" + strExpPropuesta + "'";
		        IItemCollection collPropsConv = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
		        Iterator itPropsConv = collPropsConv.iterator();
		        int tamano = collPropsConv.toList().size();
		        logger.warn("tamano "+tamano);
		        
		        boolean propEncontr = false;
		        if(tamano != 0){
		        	 while ( itPropsConv.hasNext() && !propEncontr){
				        IItem iUrgencia = ((IItem)itPropsConv.next());
				        String expPadre = iUrgencia.getString("NUMEXP");
				        if(expPadre.equals(numExp)){
				        	propEncontr = true;
				        }
		        	 }
		        }
		        
		        if (tamano == 0 || !propEncontr) {
		        	//Obtención de los datos del expediente
			        strQuery = "WHERE NUMEXP='" + strExpPropuesta + "'";
			        IItem iExpediente = ExpedientesUtil.getExpediente(cct, strExpPropuesta);
			        if (iExpediente == null) {
			        	return new Boolean(false);
			        }
			        logger.warn("inicio");
			        //Creación de nueva propuesta en la sesión
			        IItem item = entitiesAPI.createEntity("SECR_URGENCIAS",rulectx.getNumExp());
			        if(iExpediente.getString("IDENTIDADTITULAR")!=null)	item.set("INTERESADO", iExpediente.getString("IDENTIDADTITULAR"));
		        	if(iPropuesta.getString("ORIGEN")!=null)	item.set("ORIGEN", iPropuesta.getString("ORIGEN"));
		        	if(iPropuesta.getString("DESTINO")!=null)	item.set("DESTINO", iPropuesta.getString("DESTINO"));
		        	if(iPropuesta.getString("PRIORIDAD")!=null)	item.set("PRIORIDAD", iPropuesta.getString("PRIORIDAD"));
		        	if(iPropuesta.getString("PRIORIDAD_MOTIVO")!=null)	item.set("PRIORIDAD_MOTIVO", iPropuesta.getString("PRIORIDAD_MOTIVO"));
		        	if(iPropuesta.getString("EXTRACTO")!=null)  item.set("EXTRACTO", iPropuesta.getString("EXTRACTO"));
		        	item.set("NUMEXP_ORIGEN", strExpPropuesta);
		        	
		        	logger.warn("Insertado");
		        	
		        	item.store(cct);
		         
		        	//Actualizo el estado administrativo
		        	String strEstado = SecretariaUtil.getEstadoAdmPropuesta(rulectx);
		        	iExpediente.set("ESTADOADM", strEstado);
		        	iExpediente.store(cct);
		        	logger.warn("cambniado estado");
		        }
	        }
	        
	      //Ordenación de las propuestas (de 10 en 10 para permitir insertar luego urgencias entre medias)
	        strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "' ORDER BY ORIGEN ASC";
	        collAllProps = entitiesAPI.queryEntities("SECR_URGENCIAS", strQuery);
	        itAllProps = collAllProps.iterator();
	        int nOrden = 10;
	        DecimalFormat df = new DecimalFormat("0000");
	        iProp = null;
	        while(itAllProps.hasNext()) {
	        	iProp = ((IItem)itAllProps.next());
	        	iProp.set("ORDEN", df.format(nOrden));
	        	iProp.store(cct);
	        	nOrden = nOrden + 10;
	        }
    		
    		return new Boolean(true);
    	} catch(Exception e) {
    		logger.error("No se ha podido inicializar la propuesta. " + e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta. " + e.getMessage(), e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}
