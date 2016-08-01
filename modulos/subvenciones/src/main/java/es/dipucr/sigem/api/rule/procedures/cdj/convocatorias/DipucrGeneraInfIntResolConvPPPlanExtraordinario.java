package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;


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
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrGeneraInfIntResolConvPPPlanExtraordinario extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrGeneraInfIntResolConvPPPlanExtraordinario.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }

            refTablas = "";
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(),
                    e);
        }
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
        String numexp = "";
        try {
            String anio = "" + Calendar.getInstance().get(Calendar.YEAR);
            cct.setSsVariable("ANIO", anio);
            numexp = rulectx.getNumExp();

            String textoPrimero = "";
            String textoSegundo = "";

            double importeTotalObras = 0;
            double importeTotalAccesibilidad = 0;

            // Obtenemos los expedientes relacionados y aprobados, ordenados por
            // ayuntamiento
            IItemCollection expRelacionadosCollection = cct.getAPI().getEntitiesAPI()
                    .queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='" + rulectx.getNumExp() + "'");
            Iterator<?> expRelacionadosIterator = expRelacionadosCollection.iterator();
            String query = "";
            while (expRelacionadosIterator.hasNext()) {
                String numexpHijo = ((IItem) expRelacionadosIterator.next()).getString("NUMEXP_HIJO");
                query += "'" + numexpHijo + "',";
            }

            if (query.length() > 0) {
                query = query.substring(0, query.length() - 1);
            }
            IItemCollection expedientesCollection = cct
                    .getAPI()
                    .getEntitiesAPI()
                    .queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES,
                            "WHERE NUMEXP IN (" + query + ") AND ESTADOADM='RS' ORDER BY IDENTIDADTITULAR");
            Iterator<?> expedientesIterator = expedientesCollection.iterator();

            while (expedientesIterator.hasNext()) {
                IItem expediente = (IItem) expedientesIterator.next();
                Iterator<?> resolucionIterator = cct.getAPI().getEntitiesAPI().getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP"))
                        .iterator();
                if (resolucionIterator.hasNext()) {
                    IItem resolucion = (IItem) resolucionIterator.next();
                    try {
                        Iterator<?> tipoExpIterator = cct.getAPI().getEntitiesAPI()
                                .getEntities("DPCR_PP_TIPO_CONV_PLAN_OBRAS", expediente.getString("NUMEXP")).iterator();

                        if (tipoExpIterator.hasNext()) {
                            IItem tipoExp = (IItem) tipoExpIterator.next();
                            String tipo = tipoExp.getString("TIPO");
                            if ("1".equals(tipo)) {
                                importeTotalObras += Double.parseDouble(resolucion.getString("IMPORTE") == null ? "0" : resolucion
                                        .getString("IMPORTE"));
                            } else {
                                importeTotalAccesibilidad += Double.parseDouble(resolucion.getString("IMPORTE") == null ? "0" : resolucion
                                        .getString("IMPORTE"));
                            }
                        }
                    } catch (ISPACException e) {
                        LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar el importe de la solicitud: " + expediente.getString("NUMEXP") + ", " + e.getMessage(), e);
                    }
                } else {
                    cct.getAPI().getEntitiesAPI().createEntity("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP"));
                }
            }

            if (importeTotalObras == 0 && importeTotalAccesibilidad == 0) {
                textoPrimero = "La propuesta de resolución parcial se ajusta al crédito autorizado por la convocatoria.";
                textoSegundo = "La documentación presentada hasta la fecha por los Ayuntamientos incluidos en dicha propuesta cumple con los requisitos establecidos en la Base Séptima de las reguladoras del Plan de Obras Municipales para "
                        + anio + ".";
            } else {
                textoPrimero = "La propuesta de resolución parcial, del Plan Extraordinario de Obras Municipales, por importe de "
                        + importeTotalObras + " euros y la del Plan Provincial de Accesibilidad y eliminación de barreras, por importe de "
                        + importeTotalAccesibilidad + " euros, se ajusta al crédito autorizado por la convocatoria.";
                textoSegundo = "La documentación presentada por los Ayuntamientos incluidos en dicha propuesta cumple con los requisitos establecidos en las Bases Sexta y Séptima de las reguladoras del Plan Extraordinario de Obras Municipales y Plan Provincial de Accesibilidad y Eliminación de Barreras para "
                        + anio + ".";
            }

            cct.setSsVariable("textoPrimero", textoPrimero);
            cct.setSsVariable("textoSegundo", textoSegundo);
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable("ANIO");
            cct.deleteSsVariable("textoPrimero");
            cct.deleteSsVariable("textoSegundo");
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
