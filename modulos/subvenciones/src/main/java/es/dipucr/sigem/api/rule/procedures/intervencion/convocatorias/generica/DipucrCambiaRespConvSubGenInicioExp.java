package es.dipucr.sigem.api.rule.procedures.intervencion.convocatorias.generica;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrCambiaRespConvSubGenInicioExp implements IRule {
    private static final Logger LOGGER = Logger.getLogger(DipucrCambiaRespConvSubGenInicioExp.class);

    private String numexpPadre;

    // Cristina
    private String idCristina = "1-269";
    // Rafa
    private String idRafa = "1-186";

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO +this.getClass().getName());
        try {
            // ----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            // ----------------------------------------------------------------------------------------------

            // Obtenemos la opción que se ha indicado en la solicitud haya
            // participado

            String strQuery = ConstantesString.WHERE + " NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";

            IItemCollection datos = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);

            if (datos.iterator().hasNext()) {
                numexpPadre = ((IItem) datos.iterator().next()).getString("NUMEXP_PADRE");
            } else{
                numexpPadre = "";
            }

            if (StringUtils.isNotEmpty(numexpPadre)) {
                
                strQuery = ConstantesString.WHERE + " NUMEXP = '" + rulectx.getNumExp() + "'";
                
                IItem fase = (IItem) entitiesAPI.queryEntities("SPAC_FASES", strQuery).iterator().next();

                if ("DPCR2012/18380".equals(numexpPadre)) {
                    fase.set("ID_RESP", idCristina);
                } else if ("DPCR2012/18366".equals(numexpPadre)) {
                    fase.set("ID_RESP", idRafa);
                } 
                
                fase.store(cct);
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        }
        LOGGER.info(ConstantesString.FIN +this.getClass().getName());
        return Boolean.TRUE;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
}