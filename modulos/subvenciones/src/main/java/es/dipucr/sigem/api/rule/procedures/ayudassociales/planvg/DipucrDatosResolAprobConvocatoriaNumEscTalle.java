package es.dipucr.sigem.api.rule.procedures.ayudassociales.planvg;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrDatosResolAprobConvocatoriaNumEscTalle extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriaNumEscTalle.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try{
            IClientContext cct = rulectx.getClientContext();
            
            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
        } catch(ISPACException e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
        }
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }
    
    public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
        try {
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
                
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            
            int totalTalleres = 0;
            int totalActividades = 0;
            int totalBullying = 0;
            
            List<String> expedientesRelacionadosList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS);
            
            for(String numexpHijo : expedientesRelacionadosList){
                IItemCollection resolucionCollection = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo);
                Iterator<?> resolucionIterator = resolucionCollection.iterator();
                
                if(resolucionIterator.hasNext()){
                    IItem resolucion = (IItem)resolucionIterator.next();

                    totalTalleres += SubvencionesUtils.getInt(resolucion, ConstantesSubvenciones.DatosResolucion.TALLERES_EDU);
                    totalActividades += SubvencionesUtils.getInt(resolucion, ConstantesSubvenciones.DatosResolucion.ACTIVIDADES_INTERCULT);
                    totalBullying += SubvencionesUtils.getInt(resolucion, ConstantesSubvenciones.DatosResolucion.TALLERES_BULLYING);
                }
            }  
            
            cct.setSsVariable("TOTALTALLERES", totalTalleres > 0 ? "Número total de talleres de educación en igualdad: " + totalTalleres : "");
            cct.setSsVariable("TOTALACTIVIDADES", totalActividades > 0 ? "Número total de talleres de educación en interculturalidad: " + totalActividades : "");
            cct.setSsVariable("TOTALBULLYING", totalBullying > 0 ? "Número total de talleres contra el bullying y el ciberbullying: " + totalBullying : "");

            
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}