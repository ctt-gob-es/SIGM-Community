package es.dipucr.sigem.api.rule.procedures.comunadminelec;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

/**
 * [eCenpri-Felipe #1202]
 * Regla que carga en las variables de sesión los datos de representante
 * y, si este no existe, los datos de interesado principal
 * Consulta la tabla INTERESADO_REPRESENTANTE
 */
public class CargarSessionVarsInteresadoRepresentanteRule implements IRule 
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.
			getLogger(CargarSessionVarsInteresadoRepresentanteRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException 
	{
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException 
	{
		return true;
	}
	
	/**
	 * Ejecución de la regla
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		
        try
        {
        	//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
        	//Recuperamos el registro en interesado_representante
        	String numexp = rulectx.getNumExp();
			String strQueryRepresentante = " WHERE NUMEXP = '" + numexp + "'";
			IItemCollection collectionIntRep = entitiesAPI.queryEntities
					("INTERESADO_REPRESENTANTE", strQueryRepresentante);
			
			if (collectionIntRep.next()){

				IItem itemIntRep = (IItem) collectionIntRep.iterator().next();
				String nombreRepre = itemIntRep.getString("REP_NOMBRE");
				
				boolean tieneRepresentante = false;
				if (StringUtils.isNotEmpty(nombreRepre)){
					tieneRepresentante = true;
				}
				
				String nifcif = "";
				String nombre = "";
				String direccion = "";
				String cPostal = "";
				String localidad = "";
				String region = "";
				String email = "";
				String movil = "";
				
				//Recuperamos los valores en función de si tiene o no representante
				if (!tieneRepresentante){ //(sólo interesado)
					nifcif = itemIntRep.getString("INT_NIFCIF");
					nombre = itemIntRep.getString("INT_NOMBRE");
					direccion = itemIntRep.getString("INT_DIRECCION");
					cPostal = itemIntRep.getString("INT_CPOSTAL");
					localidad = itemIntRep.getString("INT_LOCALIDAD");
					region = itemIntRep.getString("INT_PAIS");
					email = itemIntRep.getString("INT_EMAIL");
					movil = itemIntRep.getString("INT_MOVIL");
				}
				else{
					nifcif = itemIntRep.getString("REP_NIFCIF");
					nombre = itemIntRep.getString("REP_NOMBRE");
					direccion = itemIntRep.getString("REP_DIRECCION");
					cPostal = itemIntRep.getString("REP_CPOSTAL");
					localidad = itemIntRep.getString("REP_LOCALIDAD");
					region = itemIntRep.getString("REP_PAIS");
					email = itemIntRep.getString("REP_EMAIL");
					movil = itemIntRep.getString("REP_MOVIL");
				}
				
				cct.setSsVariable("NIFCIF", nifcif);
				cct.setSsVariable("NOMBRE", nombre);
				cct.setSsVariable("DIRNOT", direccion);
				cct.setSsVariable("C_POSTAL", cPostal);
				cct.setSsVariable("LOCALIDAD", localidad);
				cct.setSsVariable("CAUT", region);
				cct.setSsVariable("EMAIL", email);
				cct.setSsVariable("MOVIL", movil);
			}
			
			return new Boolean(true);
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error al cargar en sesión los datos de interesado/representante", e);
	    }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
