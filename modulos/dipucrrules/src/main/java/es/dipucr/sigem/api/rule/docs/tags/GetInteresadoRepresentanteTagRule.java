package es.dipucr.sigem.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

/**
 * [eCenpri-Felipe #947]
 * Regla que recupera la variable del sistema pasada como parámetro
 * y sustituye los valores por los del interesado o representante
 * según corresponda, consultando la tabla INTERESADO_REPRESENTANTE
 */
public class GetInteresadoRepresentanteTagRule implements IRule 
{
	
	private final String DEFAULT_VAR = "DATOS_INTERESADO_REPRESENTANTE";
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(AdministracionTagRule.class);
	
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
	        
	        String nombreVar = rulectx.get("variable");
	        if (StringUtils.isEmpty(nombreVar)){
	        	nombreVar = DEFAULT_VAR;
	        }
	        
	        //Recuperamos el texto a sustituir de la variable
	        String texto = ConfigurationMgr.getVarGlobal(cct, nombreVar);

	        if (StringUtils.isEmpty(texto)){
	        	return "[ERROR: Variable del sistema " + nombreVar + " no encontrada para la entidad]";
	        }
	        else{
	        	//Recuperamos el registro en interesado_representante
	        	String numexp = rulectx.getNumExp();
				String strQueryRepresentante = " WHERE NUMEXP = '" + numexp + "'";
				IItemCollection collectionIntRep = entitiesAPI.queryEntities
						("INTERESADO_REPRESENTANTE", strQueryRepresentante);
				
				if (!collectionIntRep.next()){
					return "[ERROR: No existe la entidad INTERESADO_REPRESENTANTE " +
							"asociada al expediente en curso " + numexp + "]";
				}
				else{
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
					String textoAux = "";
					
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
						textoAux = "en su propio nombre";
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
						textoAux = "en representación de " + itemIntRep.getString("INT_NOMBRE");
						
						//Texto "como se acredita"
						String acreditado = rulectx.get("acreditado");
						boolean estaAcreditado = (StringUtils.isNotEmpty(acreditado) && acreditado.equals("true"));
						if (!estaAcreditado){
							textoAux += ", como se acredita mediante ............";
						}
					}
					
					//Sustituímos los valores en el texto
					texto = texto.replace("[NIF]", nifcif);
					texto = texto.replace("[NOMBRE]", nombre);
					texto = texto.replace("[DIRECCION]", (null != direccion ? direccion : ""));
					texto = texto.replace("[CIUDAD]", (null != localidad ? localidad : ""));
					texto = texto.replace("[REGION]", (null != region ? region : ""));
					texto = texto.replace("[CPOSTAL]", (null != cPostal ? cPostal : ""));
					texto = texto.replace("[EMAIL]", (null != email ? email : ""));
					texto = texto.replace("[MOVIL]", (null != movil ? movil : ""));
					texto = texto.replace("[TEXTO_AUX]", textoAux);
					
					return texto;
				}
	        }
	        
	    } catch (Exception e) {
	        return "[ERROR: Error al generar el texto de interesado / representante." + e + "]";
	    }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
