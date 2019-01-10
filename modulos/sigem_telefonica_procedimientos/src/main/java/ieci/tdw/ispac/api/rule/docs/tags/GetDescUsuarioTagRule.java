package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IThirdPartyAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.thirdparty.IPostalAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;


/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna una cadena que contiene la dirección del departamento (dado de alta como tercero) al que pertenece el usuario tramitador.
 *
 */
public class GetDescUsuarioTagRule implements IRule {
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            IClientContext cct =  rulectx.getClientContext();
            Responsible resp = cct.getUser();
            String descripcion = null;
            
            //Obtenemos el departamento (servicio) al que pertenece el usuario
            IResponsible dep = null;
            dep = resp.getRespOrgUnit();
            
            //IInvesflowAPI invesflowAPI = session.getAPI();
            IInvesflowAPI invesflowAPI = cct.getAPI();
            IThirdPartyAPI thirdPartyAPI = invesflowAPI.getThirdPartyAPI();
            // Buscar el tercero a partir del nombre
            IThirdPartyAdapter [] terceros = thirdPartyAPI.lookup(null, dep.getString("NAME"), null);
            
            if (terceros == null || terceros.length == 0) {
                throw new ISPACInfo("No se han encontrado datos para el Departamento '" + dep.getString("NAME") + "'");
                
            } else {
                int i=0;
                boolean encontrado=false;
                IThirdPartyAdapter thirdParty = null;
                
                while (i<terceros.length && !encontrado){
                    //request.setAttribute("ThirdParty", terceros[0]);
                    thirdParty = (IThirdPartyAdapter) terceros[i];

                    if (thirdParty != null) {
                        if (thirdParty.getPrimerApellido().equalsIgnoreCase(dep.getString("NAME"))){
                            
                            encontrado=true;

                            IPostalAddressAdapter[] postalAdapter = thirdParty.getDireccionesPostales();
                            String direccion = postalAdapter[0].getDireccionPostal();
                            String cp = postalAdapter[0].getCodigoPostal();
                            String ciudad = postalAdapter[0].getMunicipio();
                            String provincia = postalAdapter[0].getProvincia();
                            descripcion = direccion + "\n" + cp + " " + ciudad;
                            
                            if (StringUtils.isNotEmpty(provincia)){
                                descripcion += " ("+provincia+")";
                            }
                        }
                    } else {
                        throw new ISPACInfo("No se han encontrado datos para el Departamento '" + dep.getString("NAME") + "'");
                    }
                    
                    i++;
                }
            }
            
            if (descripcion == null){
                return "";
            }
            
            return descripcion;
            
        } catch (Exception e) {
            throw new ISPACRuleException("Error obteniendo el Área y Servicio del usuario.", e);
        }     
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
