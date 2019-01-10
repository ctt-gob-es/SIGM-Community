package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
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
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrDatosResolAprobConvocatoriasEELL extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriasEELL.class);
    
    public static final double[] DISTRIBUCION_4_COLUMNAS_RS = {35, 15, 35, 15};
    public static final double[] DISTRIBUCION_4_COLUMNAS_RC = {30, 10, 30, 30};
    public static final double[] DISTRIBUCION_4_COLUMNAS_RN = {30, 10, 30, 30};
    
    public static final double[] DISTRIBUCION_5_COLUMNAS_RS = {30, 15, 25, 20, 10};
    public static final double[] DISTRIBUCION_5_COLUMNAS_RC = {30, 10, 20, 20, 20};
    public static final double[] DISTRIBUCION_5_COLUMNAS_RN = {30, 10, 20, 20, 20};
    
    private boolean conModalidadCategoria = false;

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try{
            IClientContext cct = rulectx.getClientContext();
            
            conModalidadCategoria = "TRUE".equalsIgnoreCase(TramitesUtil.getPropiedadDatosEspecificos(cct, rulectx.getTaskProcedureId(), ConstantesSubvenciones.DatosEspecificosOtrosDatos.PROPIEDAD_MODALIDAD));
            
            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
            
            refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA3;
        } catch(ISPACException e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        }
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }
    
    
    public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
        String numexp = "";
        try {
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            numexp = rulectx.getNumExp();
            
            double importeTotal = 0;

            List<String> expedientesRelacionadosList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.IDENTIDADTITULAR);
            
            for(String numexpHijo : expedientesRelacionadosList){    
                Iterator<?> resolucionIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                if( resolucionIterator.hasNext()){
                    IItem resolucion = (IItem) resolucionIterator.next();
                    importeTotal += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                } else{
                    cct.getAPI().getEntitiesAPI().createEntity(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo);
                    importeTotal += 0;
                }
            }
            
            cct.setSsVariable(ConstantesSubvenciones.DatosResolucion.IMPORTE, ""    + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotal));
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
            cct.deleteSsVariable(ConstantesSubvenciones.DatosResolucion.IMPORTE);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        
        String estadoAdm = ExpedientesUtil.EstadoADM.RS;
        double [] distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RS;
        String textoCol4 = ConstantesString.CabeceraTabla.IMPORTE;
        String textoCol5 = ConstantesString.CabeceraTabla.IMPORTE;
        int numColumnas = 4;
        
        String modalidad = "";
        String beneficiario = "";
        String nifCifTitular = "";
        String proyecto = "";
        double importe = 0;
        String motivoDenegacion = "";
        String motivoRenuncia = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();
            
            if(!conModalidadCategoria){
                Object[] configuracion = configuracion4Columnas(refTabla);
                
                estadoAdm = (String) configuracion[0];
                distribucionColumnas = (double[]) configuracion[1];
                textoCol4 = (String) configuracion[2];
                numColumnas = 4;
                
            } else {
                Object[] configuracion = configuracion5Columnas(refTabla);
                
                estadoAdm = (String) configuracion[0];
                 distribucionColumnas = (double[]) configuracion[1];
                 textoCol4 = (String) configuracion[2];
                 textoCol5 = (String) configuracion[3];
                numColumnas = 5;
            }
            
            List<String> expedientesRelacionadosList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, ExpedientesUtil.IDENTIDADTITULAR);

            int numFilas = expedientesRelacionadosList.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numColumnas);

            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, textoCol4);
                
                if(conModalidadCategoria){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, textoCol5);
                }

                int i = 1;
                
                for(String numexpHijo : expedientesRelacionadosList){
                    i++;
                    
                    modalidad = "";
                    beneficiario = "";
                    nifCifTitular = "";
                    proyecto = "";
                    importe = 0;
                    motivoDenegacion = "";
                    
                    ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);
                    
                    beneficiario = solicitudConvocatoria.getBeneficiario();
                    nifCifTitular = solicitudConvocatoria.getNifCifTitular();

                    proyecto = solicitudConvocatoria.getCampoSolicitud( ConstantesSubvenciones.DatosSolicitud.FINALIDAD);

                    modalidad = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.MODALIDAD1);
                    importe += solicitudConvocatoria.getImporte1();
                    motivoDenegacion = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    motivoRenuncia = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA1);
                    
                    solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
                    solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
                    solicitudConvocatoria.insertaParticipante(cct, numexp);
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, beneficiario);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, nifCifTitular);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto);
                    
                   if(!conModalidadCategoria){
                       String [] datos = {motivoDenegacion, motivoRenuncia};
                       insertaDatos4Columnas(tabla, refTabla, i, datos, importe);
                   } else {
                       String [] datos = {modalidad, motivoDenegacion, motivoRenuncia};
                       insertaDatos5Columnas(tabla, refTabla, i, datos, importe);
                   }
                }
            }            
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public Object[] configuracion4Columnas(String refTabla) {
        
        Object[] configuracion = new Object[3];
        
        String estadoAdm = ExpedientesUtil.EstadoADM.RS;
        double [] distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RS;
        String textoCol4 = ConstantesString.CabeceraTabla.IMPORTE;
        
        if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
            estadoAdm = ExpedientesUtil.EstadoADM.RS;
            distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RS;
            textoCol4 = ConstantesString.CabeceraTabla.IMPORTE;
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
            estadoAdm = ExpedientesUtil.EstadoADM.RC;
            distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RC;
            textoCol4 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
            estadoAdm = ExpedientesUtil.EstadoADM.RN;
            distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RN;
            textoCol4 = ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA;
        }
        
        configuracion[0] = estadoAdm;
        configuracion[1] = distribucionColumnas;
        configuracion[2] = textoCol4;
        
        return configuracion;
    }
    
    public Object[] configuracion5Columnas(String refTabla) {
        
        Object[] configuracion = new Object[4];
        
        String estadoAdm = ExpedientesUtil.EstadoADM.RS;
        double [] distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;
        String textoCol5 = ConstantesString.CabeceraTabla.IMPORTE;
        
        if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
            estadoAdm = ExpedientesUtil.EstadoADM.RS;
            distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;
            textoCol5 = ConstantesString.CabeceraTabla.IMPORTE;
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
            estadoAdm = ExpedientesUtil.EstadoADM.RC;
            distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RC;
            textoCol5 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
            
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
            estadoAdm = ExpedientesUtil.EstadoADM.RN;
            distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RN;
            textoCol5 = ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA;
        }
        
        configuracion[0] = estadoAdm;
        configuracion[1] = distribucionColumnas;
        configuracion[2] = ConstantesString.CabeceraTabla.MODALIDAD_CATEGORIA;
        configuracion[3] = textoCol5;
        
        return configuracion;
    }


    public void insertaDatos4Columnas(XTextTable tabla, String refTabla, int fila, String[] datos, double importe) throws ISPACException{
        try{
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                LibreOfficeUtil.setTextoCelda(tabla, 4, fila, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importe));
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                LibreOfficeUtil.setTextoCelda(tabla, 4, fila, datos[0]);
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
                LibreOfficeUtil.setTextoCelda(tabla, 4, fila, datos[1]);
            }
         } catch (Exception e) {
             LOGGER.error(ConstantesString.LOGGER_ERROR + " al insertar las columnas de la fila: " + fila + ". " + e.getMessage(), e);
             throw new ISPACException(ConstantesString.LOGGER_ERROR + " al insertar las columnas de la fila: " + fila + ". " + e.getMessage(), e);
         }
    }
    
    public void insertaDatos5Columnas(XTextTable tabla, String refTabla, int fila, String[] datos, double importe) throws ISPACException {
        try{
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                LibreOfficeUtil.setTextoCelda(tabla, 4, fila, datos[0]);
                LibreOfficeUtil.setTextoCelda(tabla, 5, fila, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importe));
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                LibreOfficeUtil.setTextoCelda(tabla, 4, fila, datos[0]);
                LibreOfficeUtil.setTextoCelda(tabla, 5, fila, datos[1]);
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
                LibreOfficeUtil.setTextoCelda(tabla, 4, fila, datos[0]);
                LibreOfficeUtil.setTextoCelda(tabla, 5, fila, datos[2]);
            }
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al insertar las columnas de la fila: " + fila + ". " + e.getMessage(), e);
            throw new ISPACException(ConstantesString.LOGGER_ERROR + " al insertar las columnas de la fila: " + fila + ". " + e.getMessage(), e);
        }
    }
}
