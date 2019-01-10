package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOPasaANotificado implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOPasaANotificado.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        String numexpSolicitud="";
        String descripcion = "";
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            String numexp = rulectx.getNumExp();
            int tramiteId = rulectx.getTaskId();
            
            //Recuperamos los expedientes relacionados
            IItemCollection docsCol = entitiesAPI.getDocuments(numexp, "ID_TRAMITE = '" + tramiteId + "' AND UPPER(NOMBRE) = 'NOTIFICACIÓN'", "");
            Iterator<?> docsIt = docsCol.iterator();
            
            while (docsIt.hasNext()){
                IItem doc = (IItem)docsIt.next();

                descripcion = SubvencionesUtils.getString(doc, DocumentosUtil.DESCRIPCION);
                numexpSolicitud = (descripcion.split("-"))[2].trim();
                                 
                //Cambiamos el estado
                String estadoAdmSolicitud = ExpedientesUtil.getEstadoAdm(cct, numexpSolicitud);
                
                if (ExpedientesUtil.EstadoADM.NE.equals(estadoAdmSolicitud)){
                    IItem expediente = ExpedientesUtil.getExpediente(cct, numexpSolicitud);
                    
                    expediente.set(ExpedientesUtil.ESTADOADM, ExpedientesUtil.EstadoADM.NT);
                    expediente.store(cct);
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass());
            return true;
            
        } catch(Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + ". Procesando el documento: " + descripcion + ". Error al cambiar el estado del expediente: " + numexpSolicitud + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + ". Procesando el documento: " + descripcion + ". Error al cambiar el estado del expediente: " + numexpSolicitud + ". " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

}