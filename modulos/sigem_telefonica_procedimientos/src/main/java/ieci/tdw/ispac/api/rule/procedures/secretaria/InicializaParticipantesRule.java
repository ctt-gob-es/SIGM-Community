package ieci.tdw.ispac.api.rule.procedures.secretaria;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class InicializaParticipantesRule implements IRule {

	private static final Logger logger = Logger.getLogger(InicializaParticipantesRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        //Sólo actualizo la lista de participantes si está vacía
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        IItemCollection collAllParts = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
	        Iterator itAllParts = collAllParts.iterator();
	        if (!itAllParts.hasNext()) 
	        {
		        //Obtenemos los miembros del órgano correspondiente
		        String strOrgano = CommonFunctions.getOrganoSesion(rulectx, null);
		        strQuery = "WHERE NOMBRE='"+strOrgano+"'";		       
		        IItemCollection collMiembros = entitiesAPI.queryEntities("SECR_ORGANO", strQuery);
		        Iterator itMiembros = collMiembros.iterator();
	        	IItem iMiembro = null;
		        while(itMiembros.hasNext())
		        {
		        	iMiembro = ((IItem)itMiembros.next());
		        	String strNumExp = iMiembro.getString("NUMEXP");
		        	
		        	//Obtención de los datos del expediente del miembro
		        	 /**
			         * Ticket #396 INICIO SIGEM secretaria Gestion de integrantes Anular y dar de alta participantes
			         * **/
			        strQuery = "WHERE NUMEXP='" + strNumExp + "'";
			        /**
			         * Ticket #396 FIN SIGEM secretaria Gestion de integrantes Anular y dar de alta participantes
			         * **/
			        IItemCollection collExps = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", strQuery);
			        Iterator itExps = collExps.iterator();
			        if (! itExps.hasNext()) {
			        	logger.warn("No se encuentra el expediente asociado al miembro. Numexp=" + strNumExp);
			        }
			        else
			        {
				        IItem iExpediente = ((IItem)itExps.next());
				        
				        //Sólo se admiten integrantes en activo
				        //Comprobamos estado del expediente y fecha de cese
				        boolean enActivo = CheckFechas(rulectx, iExpediente);
				        
				        //En comisiones informativas comprobamos también el área
				        boolean mismaArea = CheckArea(rulectx, iExpediente);
				         
				        
				        //logger.warn("numexp "+strNumExp+" enActivo "+enActivo+" mismaArea "+mismaArea);
				        
			        	if (enActivo && mismaArea)
			        	{
				        	//La entidad de participantes siempre será la número 3
					        IItem item = entitiesAPI.createEntity(3);
					        item.set("NUMEXP", rulectx.getNumExp());
				        	item.set("NDOC", iExpediente.getString("NIFCIFTITULAR"));
				        	item.set("NOMBRE", iExpediente.getString("IDENTIDADTITULAR"));
				        	item.set("IDDIRECCIONPOSTAL", iExpediente.getString("IDDIRECCIONPOSTAL"));
				        	item.set("DIRNOT", iExpediente.getString("DOMICILIO"));
				        	item.set("C_POSTAL", iExpediente.getString("CPOSTAL"));
				        	item.set("LOCALIDAD", iExpediente.getString("CIUDAD"));
				        	item.set("CAUT", iExpediente.getString("REGIONPAIS"));
				        	item.set("DIRECCIONTELEMATICA", iExpediente.getString("DIRECCIONTELEMATICA"));
				        	item.set("TFNO_FIJO", iExpediente.getString("TFNOFIJO"));
				        	item.set("TFNO_MOVIL", iExpediente.getString("TFNOMOVIL"));
				        	item.set("TIPO_PERSONA", iExpediente.getString("TIPOPERSONA"));
				        	item.set("TIPO_DIRECCION", iExpediente.getString("TIPODIRECCIONINTERESADO"));
				        	item.set("ASISTE", "SI");
				        	item.store(cct);
			        	}
			        }
		        }
	        }
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido inicializar la lista de miembros",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	private boolean CheckFechas(IRuleContext rulectx, IItem iExpediente) throws ISPACException
	{
		boolean enActivo = true;
		try
		{
	        String fcierre = iExpediente.getString("FCIERRE"); 
	        if (fcierre!=null && fcierre.length()>0)
	        {
	        	enActivo = false;
	        }
	        String strNumExp = iExpediente.getString("NUMEXP"); 
	        String strQuery = "WHERE NUMEXP='" + strNumExp + "'";
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        IItemCollection collInfo = entitiesAPI.queryEntities("SECR_MIEMBRO", strQuery);
	        Iterator itInfo = collInfo.iterator();
	        if (itInfo.hasNext()) 
	        {
		        IItem iInfo = ((IItem)itInfo.next());
		        Date fnombr = iInfo.getDate("FECHA_NOMBR");
		        Date fcese = iInfo.getDate("FECHA_CESE");
		        Date fnow = new Date();
		        if ( (fnombr!=null && fnombr.after(fnow)) || 
		        	 (fcese!=null  && fcese.before(fnow))  )
		        {
		        	enActivo = false;
		        }
	        }
		}
		catch(ISPACException e)
		{
        	throw new ISPACException(e);
		}
		return enActivo;
	}
	
	private boolean CheckArea(IRuleContext rulectx, IItem iExpediente) throws ISPACException
	{
		boolean mismaArea = true;
		try
		{
			//En Comisión Informativa tenemos que comprobar que el Integrante
			//pertenezca al área de la Comisión
			String strOrgano = CommonFunctions.getOrganoSesion(rulectx, null);
			if (strOrgano.compareTo("COMI")==0)
			{
		        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		        String strNumExp = iExpediente.getString("NUMEXP");
		        //Esto no funciona porque aun no están consolidados los datos
		        //String strArea = CommonFunctions.getAreaSesion(rulectx, null);
		        IItem sesion = rulectx.getItem();
		        if (sesion != null)
		        {
			        String strArea = sesion.getString("AREA");
			        String strQuery = "WHERE NUMEXP='" + strNumExp + "' AND NOMBRE='" + strArea + "'";
			        IItemCollection coll = entitiesAPI.queryEntities("SECR_AREAS", strQuery);
			        Iterator it = coll.iterator();
			        if (!it.hasNext()) 
			        {
			        	mismaArea = false;
			        }
		        }
		        else
		        {
		        	//Parece que aun no se ha rellenado el campo area en la 
		        	//entidad sesion. Por tanto no metemos ningún participante.
		        	mismaArea = false;
		        }
			}
		}
		catch(ISPACException e)
		{
        	throw new ISPACException(e);
		}
		return mismaArea;
	}
	
}