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

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

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
                
            cct.setSsVariable("ANIO", "" + Calendar.getInstance().get(Calendar.YEAR));
            
            String numexp = rulectx.getNumExp();
            int totalTalleres = 0;
            int totalActividades = 0;            
            
             //Recuperamos los expedientes relacionados
            String strQuery = "WHERE NUMEXP_PADRE='" + numexp + "'";
            IItemCollection expRelCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
            Iterator<?> expRelIt = expRelCol.iterator();                  
            if(expRelIt.hasNext()){
                while (expRelIt.hasNext()){
                    IItem expRel = (IItem)expRelIt.next();
                    //Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
                    String numexpHijo = expRel.getString("NUMEXP_HIJO");
                    
                    IItem expHijo = ExpedientesUtil.getExpediente(cct, numexpHijo); 
                    if(expHijo != null && "RS".equals(expHijo.get("ESTADOADM"))){
                        IItemCollection resolucionCollection = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", numexpHijo);
                        Iterator<?> resolucionIterator = resolucionCollection.iterator();
                        if(resolucionIterator.hasNext()){
                            IItem resolucion = (IItem)resolucionIterator.next();
                            try{
                                totalTalleres += Integer.parseInt(resolucion.getString("TALLERES_EDU"));
                            } catch(Exception e){
                                LOGGER.debug("El campo TALLERES_EDU es nulo, vacío o no numérico. " + e.getMessage(), e);
                            }
                            try{
                                totalActividades += Integer.parseInt(resolucion.getString("ACTIVIDADES_INTERCULT"));
                            } catch(Exception e){
                                LOGGER.debug("El campo ACTIVIDADES_INNTERCULT es nulo, vacío o no numérico. " + e.getMessage(), e);
                            }
                        }                            
                    }                    
                }
            }  
            
            cct.setSsVariable("TOTALTALLERES", totalTalleres > 0 ? "Número total de talleres de educación en igualdad: " + totalTalleres : "");
            cct.setSsVariable("TOTALACTIVIDADES", totalActividades>0?"Número total de talleres de educación en interculturalidad: " + totalActividades : "");
            
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable("ANIO");
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}