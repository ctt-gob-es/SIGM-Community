package es.dipucr.sigem.api.rule.procedures.licenciasObra;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import es.dipucr.sigem.api.rule.common.utils.RegistroPresencialUtil;


/**
 * [dipucr-Felipe #835]
 * Para las licencias de obra que se inicien por administración,
 * cambia el interesado del registro con el solicitante real
 * @author Felipe
 */
public class CambiarInteresadoRegistroLicenciaObraRule implements IRule 
{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validación y obtención de los datos de la licencia de obras
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		String numexp = null;
		IClientContext cct = rulectx.getClientContext();
		IEntitiesAPI entitiesAPI = null;
		
		try{
			entitiesAPI = cct.getAPI().getEntitiesAPI();
			numexp = rulectx.getNumExp();
						
			//Recuperamos los parámetros de filtrado
			IItemCollection colParametros = entitiesAPI.getEntities("LICENCIA_OBRAS", numexp);
			
			if (colParametros.toList().size() > 0){
				IItem itemDatosLicencia = (IItem)colParametros.iterator().next();
				
				String sAdmin = itemDatosLicencia.getString("ADMIN");
				boolean bAdmin = (!StringUtils.isEmpty(sAdmin) && "true".equals(sAdmin));
				
				if (bAdmin){
					
					IItemCollection colIntRep = entitiesAPI.getEntities("INTERESADO_REPRESENTANTE", numexp);
					IItem itemIntRep = (IItem)colIntRep.iterator().next();
					
					String nif = itemIntRep.getString("INT_NIFCIF");
					String nombre = itemIntRep.getString("INT_NOMBRE");
					
					IItem itemExpediente = entitiesAPI.getExpedient(numexp);
					String nreg = itemExpediente.getString("NREG");
					
					if (!StringUtils.isEmpty(nreg) && !StringUtils.isEmpty(nif) && !StringUtils.isEmpty(nombre)){
						RegistroPresencialUtil.modificaInteresadoRegistroEntrada(cct, nreg, nif, nombre);
					}
				}
			}
			
		}
		catch (Exception e) {
			throw new ISPACRuleException("Error al modificar el interesado del registro de la licencia", e);
		}
		return new Boolean(true);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
