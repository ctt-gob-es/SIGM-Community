package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.api.rule.procedures.SuperClaseTresTablas;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrDatosResolAprobConvocatoriasDatosAsociGrupoEELL extends SuperClaseTresTablas{

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriasDatosAsociGrupoEELL.class);
    
    public static final double[] DISTRIBUCION_5_COLUMNAS_RS = {22,30,30,13,10};
    public static final double[] DISTRIBUCION_5_COLUMNAS_RC_RN = {22,25,25,13,20};                                                            
    
    public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
        String numexp = "";
        try {
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            numexp = rulectx.getNumExp();
            
            double importeTotal = 0;
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS);
            
            for(String numexpHijo : expedientesList){
                Iterator<?> resolucionIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                if( resolucionIterator.hasNext()){
                    IItem resolucion = (IItem) resolucionIterator.next();
                    importeTotal += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                } else{
                    importeTotal += 0;
                }
            }
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE, ""    + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotal));
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        String estadoAdm = ExpedientesUtil.EstadoADM.RS;
        double[] distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;
        String tituloColumna5 = ConstantesString.CabeceraTabla.IMPORTE;
        
        String entidad = "";
        String nreg = "";
        String cifAsociacion = "";
        String nombreAsociacion = "";
        String importe = "";
        String motivoDenegacion = "";
        String motivoRenuncia = "";
        
        List<String> expedientesResolucion = new ArrayList<String>();
        
        try{
            IClientContext cct = rulectx.getClientContext();
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RS;
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;
                tituloColumna5 = ConstantesString.CabeceraTabla.IMPORTE;
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RC;
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RC_RN;
                tituloColumna5 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
                 estadoAdm = ExpedientesUtil.EstadoADM.RN;
                 distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RC_RN;
                 tituloColumna5 = ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA;
             }
            
            expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm,  ExpedientesUtil.NREG);
                
            int numFilas = expedientesResolucion.size();

            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1 , 5);
            if(null != tabla){

                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);

                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.NUM_REGISTRO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, tituloColumna5);
    
                int i = 1;
                for (String numexpHijo : expedientesResolucion){
                    i++;
                    
                    entidad = "";
                    nreg = "";
                    cifAsociacion = "";
                    nombreAsociacion = "";
                    importe = "";
                    motivoDenegacion = "";
                    motivoRenuncia = "";
                    
                    ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);
                    
                    
                    entidad = solicitudConvocatoria.getBeneficiario();
                    nreg = solicitudConvocatoria.getNREG();

                    cifAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                    nombreAsociacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);
                    importe = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTE, ConstantesString.FORMATO_IMPORTE);
                    motivoDenegacion = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    motivoRenuncia = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA1);
                    
                    solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
                    solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
                    solicitudConvocatoria.insertaParticipante(cct, numexp);
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, nreg);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, entidad);
                    LibreOfficeUtil.setTextoCelda(tabla, 3, i, nombreAsociacion);
                    LibreOfficeUtil.setTextoCelda(tabla, 4, i, cifAsociacion);
                    
                    if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                        LibreOfficeUtil.setTextoCelda(tabla, 5, i, importe);
                        
                    } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                        LibreOfficeUtil.setTextoCelda(tabla, 5, i, motivoDenegacion);
                        
                    } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
                        LibreOfficeUtil.setTextoCelda(tabla, 5, i, motivoRenuncia);
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
