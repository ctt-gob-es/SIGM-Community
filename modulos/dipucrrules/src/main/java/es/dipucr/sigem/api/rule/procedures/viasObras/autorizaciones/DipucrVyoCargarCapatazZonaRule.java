package es.dipucr.sigem.api.rule.procedures.viasObras.autorizaciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IElectronicAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IPostalAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.TercerosUtil;

public class DipucrVyoCargarCapatazZonaRule implements IRule {

	private static final Logger LOGGER = Logger.getLogger(DipucrVyoCargarCapatazZonaRule.class);
	
	public void cancel(IRuleContext paramIRuleContext)
			throws ISPACRuleException { 
	}

	public Object execute(IRuleContext rulectx)	throws ISPACRuleException {
		try {
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			String numexp = rulectx.getNumExp();
			// ----------------------------------------------------------------
			LOGGER.info("INICIO - " + this.getClass().getName());
			
			IItem itemDatosAutorizacion = rulectx.getItem();
			String zona = itemDatosAutorizacion.getString("ZONA");
			
			if (!StringUtils.isEmpty(zona)){
				
				String idZona = zona.split(" - ")[0];
				String query = "WHERE VALOR='" + idZona + "'";
				IItemCollection colZonaCapataz = entitiesAPI.queryEntities("VYO_ZONA_CAPATAZ", query);
				
				if (colZonaCapataz.toList().size() > 0){
					IItem itemZonaCapataz = colZonaCapataz.value();
					String nifCapataz = itemZonaCapataz.getString("SUSTITUTO");
					
					IThirdPartyAdapter[] arrTercero = TercerosUtil.getDatosTerceroByNif(cct, nifCapataz);
					if (arrTercero.length > 0){
						IThirdPartyAdapter tercero = arrTercero[0];
						
						IItemCollection colCapataz = entitiesAPI.getEntities("VYO_AUTO_CARR_CAPATAZ", numexp);
						IItem itemCapataz = null;
						if (colCapataz.toList().size() > 0){
							itemCapataz = colCapataz.value();
							itemCapataz.delete(cct);
						}
						
						itemCapataz = entitiesAPI.createEntity("VYO_AUTO_CARR_CAPATAZ", numexp);
						
						itemCapataz.set("NIF", tercero.getIdentificacion());
						itemCapataz.set("NOMBRE", tercero.getNombreCompleto());
						
						IPostalAddressAdapter dirPostal = tercero.getDefaultDireccionPostal();
						if (dirPostal != null) {
							itemCapataz.set("DIRNOT", dirPostal.getDireccionPostal());
							itemCapataz.set("LOCALIDAD", dirPostal.getMunicipio());
							itemCapataz.set("REGION", dirPostal.getProvincia());
							itemCapataz.set("C_POSTAL", dirPostal.getCodigoPostal());
							itemCapataz.set("TFNO_FIJO", dirPostal.getTelefono());
						}
						
						IElectronicAddressAdapter dirElectronica = tercero.getDefaultDireccionElectronica();
			        	if (dirElectronica != null) {
			        		if (dirElectronica.getTipo() == IElectronicAddressAdapter.MOBILE_PHONE_TYPE) {
			        			itemCapataz.set("TFNO_MOVIL", dirElectronica.getDireccion());
			        		} else {
			        			itemCapataz.set("EMAIL", dirElectronica.getDireccion());
			        		}
			        	}
						itemCapataz.store(cct);
					}
					else{
						LOGGER.error("No sé ha encontrado en la BBDD de terceros ningún capataz "
								+ "con NIF '" + nifCapataz + "'");
					}
				}
				else{
					LOGGER.error("No sé ha configurado ningún capataz para la zona "
							+ idZona + " en la tabla VYO_ZONA_CAPATAZ");
				}
			}
			
			LOGGER.info("FIN - " + this.getClass().getName());
		} catch (ISPACException e) {
			LOGGER.error("ERROR al copiar los datos del Interesado Principal: " + e.getMessage());
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
