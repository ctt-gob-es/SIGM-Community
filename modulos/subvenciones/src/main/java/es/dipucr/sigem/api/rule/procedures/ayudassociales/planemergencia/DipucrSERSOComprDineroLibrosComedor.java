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

import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOComprDineroLibrosComedor implements IRule {
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOComprDineroLibrosComedor.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        String numexp = "";
        try {
            // ----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            // ----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            numexp = rulectx.getNumExp();

            double importeSol = 0;
            double maximoMunicipio = 0;

            String tipoAyuda = "";
            String columna = "";
            String convocatoria = "";
            String ciudad = "";

            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();
            if (solicitudIterator.hasNext()) {
                IItem solicitud = (IItem) solicitudIterator.next();
                tipoAyuda = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);

                if (ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda) || ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda)) {
                    if (ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda)) {
                        columna = ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS;
                    } else if (ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda)) {
                        columna = ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.IMPORTETOTALCOMEDOR;
                    }

                    convocatoria = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                    ciudad = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);

                    // Recuperamos las cantidades del ayuntamiento en cuestión
                    IItemCollection cantidadesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, ConstantesString.WHERE + " LOCALIDAD = '" + ciudad
                            + "' AND NUMEXPCONVOCATORIA = '" + convocatoria + "'ORDER BY ID LIMIT 1");
                    Iterator<?> cantidadesIt = cantidadesCol.iterator();
                    while (cantidadesIt.hasNext()) {
                        IItem cantidades = (IItem) cantidadesIt.next();
                        maximoMunicipio = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TRIMESTRAL);
                    }

                    StringBuilder consulta = new StringBuilder(ConstantesString.WHERE);
                    consulta.append(" CONVOCATORIA = '" + convocatoria + "'");
                    consulta.append(" AND  CIUDAD = '" + ciudad + "'");
                    consulta.append(" AND (NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE ESTADOADM IN ('RS','AP', 'AP25','NE', 'NE25','NA','NT', 'NT25'))");
                    consulta.append(" OR NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES_H WHERE ESTADOADM IN ('RS','AP', 'AP25','NE', 'NE25','NA','NT', 'NT25')))");

                    IItemCollection solicitudesCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, consulta.toString());
                    Iterator<?> solicitudesIterator = solicitudesCollection.iterator();

                    while (solicitudesIterator.hasNext()) {

                        IItem solicitudes = (IItem) solicitudesIterator.next();

                        IItemCollection concesionLibrosComedorCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, solicitudes.getString("NUMEXP"));
                        Iterator<?> concesionLibrosComedorIterator = concesionLibrosComedorCollection.iterator();
                        if (concesionLibrosComedorIterator.hasNext()) {
                            IItem concesionLibrosComedor = (IItem) concesionLibrosComedorIterator.next();
                            importeSol += SubvencionesUtils.getDouble(concesionLibrosComedor, columna);
                        }
                    }

                    IItemCollection concesionLibrosComedorSolCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, numexp);
                    Iterator<?> concesionLibrosComedorSolIterator = concesionLibrosComedorSolCollection.iterator();
                    if (concesionLibrosComedorSolIterator.hasNext()) {
                        IItem concesionLibrosSolComedor = (IItem) concesionLibrosComedorSolIterator.next();
                        importeSol += SubvencionesUtils.getDouble(concesionLibrosSolComedor, columna);
                    }

                    if (importeSol > maximoMunicipio) {                        
                        SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, ConstantesPlanEmergencia.DpcrSERSOAvisos.TEXTOASUNTO_LIMITE_MUNICIPIO);                        
                    }
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return true;

        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al comprobar el dinero concedido al iniciar el expediente " + numexp + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al comprobar el dinero concedido al iniciar el expediente " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

}