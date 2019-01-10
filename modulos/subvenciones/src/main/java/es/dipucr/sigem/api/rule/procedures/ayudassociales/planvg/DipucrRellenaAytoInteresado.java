package es.dipucr.sigem.api.rule.procedures.ayudassociales.planvg;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.tram.thirdparty.SigemThirdPartyAPI;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrRellenaAytoInteresado implements IRule{
    
    private static final Logger LOGGER = Logger.getLogger(DipucrRellenaAytoInteresado.class);
    
    protected String email = "";
    
    //Id de la tabla scr_pfis y scrPjur de las bbdd de registro
    protected String idTraslado = "";

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try {
            String numexp = rulectx.getNumExp();
            IClientContext cct = rulectx.getClientContext();
            IItemCollection participantes = ParticipantesUtil.getParticipantes(cct, numexp);
            if(participantes.toList().size() > 0){
                IItem participante = (IItem)(participantes.iterator()).next();
                idTraslado = participante.getString(ParticipantesUtil.ID_EXT);                
                
                participante.delete(cct);
            
                if(StringUtils.isNotEmpty(idTraslado)){
                    SigemThirdPartyAPI servicioTerceros = new SigemThirdPartyAPI();
                    IThirdPartyAdapter tercero = servicioTerceros.lookupById(idTraslado);
                                        
                    if(ParticipantesUtil.insertarParticipanteById(rulectx, numexp, idTraslado, ParticipantesUtil._TIPO_INTERESADO, ParticipantesUtil._TIPO_PERSONA_JURIDICA, email, "Aytos.Adm.Publ.")){
                        ExpedientesUtil.setTerceroAsInteresadoPrincipal(cct, numexp, tercero);   
                    }
                }
            }                
        } catch (ISPACException e) {        
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al guardar el participante en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        }
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return Boolean.TRUE;
    }

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return false;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
}