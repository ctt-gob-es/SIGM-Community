package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
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

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrDatosResolAprobConvPPPlanExtraordinario extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvPPPlanExtraordinario.class);
    
    public static final double[] DISTRIBUCION_COLUMNAS_RS = {35, 15, 35, 15};
    public static final double[] DISTRIBUCION_COLUMNAS_RC = {35, 15, 25, 25};

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }

            refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA3 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA4;
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
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            numexp = rulectx.getNumExp();

            double importeTotalObras = 0;
            double importeTotalAccesibilidad = 0;

            List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.IDENTIDADTITULAR);

            for (String numexpHijo : expedientesRelacionados) {
                Iterator<?> resolucionIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                
                if (resolucionIterator.hasNext()) {
                    IItem resolucion = (IItem) resolucionIterator.next();
                    Iterator<?> tipoExpIterator = cct.getAPI().getEntitiesAPI().getEntities("DPCR_PP_TIPO_SOLICITUD", numexpHijo).iterator();

                    if (tipoExpIterator.hasNext()) {
                        IItem tipoExp = (IItem) tipoExpIterator.next();
                        String tipo = SubvencionesUtils.getString(tipoExp, "TIPO");
                        if ("1".equals(tipo)) {
                            importeTotalObras += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                        } else {
                            importeTotalAccesibilidad += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                        }
                    }
                } else {
                    cct.getAPI().getEntitiesAPI().createEntity(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo);
                }
            }

            cct.setSsVariable("IMPORTE_OBRAS", "" + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotalObras));
            cct.setSsVariable("IMPORTE_ACCESIBILIDAD", "" + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotalAccesibilidad));
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
            cct.deleteSsVariable("IMPORTE_OBRAS");
            cct.deleteSsVariable("IMPORTE_ACCESIBILIDAD");
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        
        double [] distribucionColumnas = DISTRIBUCION_COLUMNAS_RS;
        
        String estadoADM = ExpedientesUtil.EstadoADM.RS;
        String tipo = "1";

        String consulta = "";
        
        String numexpHijo = "";
        String beneficiario = "";
        String nifCifTitular = "";
        String proyecto = "";
        String importe = "";
        String motivoRechazo = "";
        
        
        try {
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)) {
                estadoADM = ExpedientesUtil.EstadoADM.RS;
                distribucionColumnas = DISTRIBUCION_COLUMNAS_RS;
                tipo = "1";

            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)) {
                estadoADM = ExpedientesUtil.EstadoADM.RS;
                distribucionColumnas = DISTRIBUCION_COLUMNAS_RS;
                tipo = "2";
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)) {
                estadoADM = ExpedientesUtil.EstadoADM.RC;
                distribucionColumnas = DISTRIBUCION_COLUMNAS_RC;
                tipo = "1";
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA4.equals(refTabla)) { 
                estadoADM = ExpedientesUtil.EstadoADM.RC;
                distribucionColumnas = DISTRIBUCION_COLUMNAS_RC;
                tipo = "2";
            }
            
            consulta = ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " IN (SELECT " + ExpedientesRelacionadosUtil.NUMEXP_HIJO + ConstantesString.FROM + Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS
                    + ConstantesString.WHERE + ExpedientesRelacionadosUtil.NUMEXP_PADRE + " = '" + rulectx.getNumExp() + "')"
                        + ConstantesString.AND + ExpedientesUtil.NUMEXP + " IN (SELECT NUMEXP FROM DPCR_PP_TIPO_SOLICITUD WHERE TIPO = '" + tipo + "') "
                    + ConstantesString.AND + ExpedientesUtil.ESTADOADM + " = '" + estadoADM + "'"
                    + " ORDER BY " + ExpedientesUtil.IDENTIDADTITULAR;
            

            IItemCollection expedientesCollection = entitiesAPI.queryEntities( Constants.TABLASBBDD.SPAC_EXPEDIENTES, consulta);
            Iterator<?> expedientesIterator = expedientesCollection.iterator();
            
            int numFilas = expedientesCollection.toList().size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, 4);
            
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                
                if(LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla) || LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.IMPORTE);
                } else {
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.MOTIVO_DENEGACION);
                }
                
                int i = 1;
                while (expedientesIterator.hasNext()) {
                    i++;
                    
                    numexpHijo = "";
                    beneficiario = "";
                    nifCifTitular = "";
                    proyecto = "";
                    importe = "";
                    motivoRechazo = "";
                    
                    IItem expediente = (IItem) expedientesIterator.next();
                    numexpHijo = SubvencionesUtils.getString(expediente, ExpedientesUtil.NUMEXP);
                    
                    ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(rulectx.getClientContext(), numexpHijo);
                    
                    beneficiario = solicitudConvocatoria.getBeneficiario();
                    nifCifTitular = solicitudConvocatoria.getNifCifTitular();

                    proyecto = solicitudConvocatoria.getCampoSolicitud(ConstantesSubvenciones.DatosSolicitud.FINALIDAD);

                    importe = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTE, ConstantesString.FORMATO_IMPORTE);
                    motivoRechazo = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    
                    solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
                    solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
                    solicitudConvocatoria.insertaParticipante(rulectx.getClientContext(), numexp);
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, beneficiario);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, nifCifTitular);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto);
                    
                    if(LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla) || LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, importe);
                    } else {
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, motivoRechazo);
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }
}
