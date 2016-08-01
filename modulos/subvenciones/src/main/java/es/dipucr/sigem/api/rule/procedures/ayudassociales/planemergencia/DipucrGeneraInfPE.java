package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

    public class DipucrGeneraInfPE  extends
            DipucrAutoGeneraDocIniTramiteRule {

        private static final Logger LOGGER = Logger
                .getLogger(DipucrGeneraInfPE.class);

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
            } catch(Exception e){
                LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
                throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return true;
        }

        
        public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
            try {
                cct.setSsVariable("ANIO", "" +Calendar.getInstance().get(Calendar.YEAR));
                
                int tramite = rulectx.getTaskProcedureId();
                IItemCollection tramitesCollection = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, "WHERE NUMEXP='" +rulectx.getNumExp()+"' AND ID_TRAM_PCD='" +tramite+"'"); 
                List<?> tramitesList = tramitesCollection.toList();

                cct.setSsVariable("NRESOLUCIONPARCIAL", "" +(tramitesList.size()));
            } catch (ISPACException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        public void deleteSsVariables(IClientContext cct) {
            try {
                cct.deleteSsVariable("ANIO");
                cct.deleteSsVariable("NRESOLUCIONPARCIAL");
            } catch (ISPACException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
