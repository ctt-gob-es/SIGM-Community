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
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrDatosResolAprobConvocatoriasNumEscuelasEELL extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriasNumEscuelasEELL.class);
    
    public static final double [] DISTRIBUCION_4_COLUMNAS_RS = {50, 20, 15, 15};
    public static final double [] DISTRIBUCION_4_COLUMNAS_RC = {35, 15, 15, 35};
    public static final double [] DISTRIBUCION_2_COLUMNAS = {80, 20};
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try{
            IClientContext cct = rulectx.getClientContext();
            
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
            numexp = rulectx.getNumExp();
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
            
            double importeTotal = 0;
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.IDENTIDADTITULAR);
            for (String numexpHijo : expedientesList){
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
        int numeroColumnas = 4;
        int numFilas = 0;
        double [] distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RS;
                
        List<String> expedientesList;
        
        String ayuntamiento = "";
        String cif = "";        
        double importe = 0;        
        int nescuelas = 0;
        String motivoDenegacion = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();
            
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RS;                
                numeroColumnas = 4;
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RS;
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RC;
                numeroColumnas = 4;
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS_RC;
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RN;
                numeroColumnas = 2;
                distribucionColumnas = DISTRIBUCION_2_COLUMNAS;
            }
            
            expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, ExpedientesUtil.IDENTIDADTITULAR);
            
            numFilas = expedientesList.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
            
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);

                if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.N_ESCUELAS);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.IMPORTE);
                    
                } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.N_ESCUELAS);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.MOTIVO_DENEGACION);
                }
                
                int i = 1;
                for (String numexpHijo : expedientesList){
                    i++;
                    
                    ayuntamiento = "";
                    cif = "";        
                    importe = 0;        
                    nescuelas = 0;
                    motivoDenegacion = "";
                    
                    IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);
                    
                    ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
                    cif = SubvencionesUtils.getString(expediente, ExpedientesUtil.NIFCIFTITULAR);
                    
                    Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                    if(expResolucion.hasNext()){
                        IItem resolucion = (IItem) expResolucion.next();
                        
                        importe = SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                        nescuelas = SubvencionesUtils.getInt(resolucion, ConstantesSubvenciones.DatosResolucion.NUMESCUELAS);
                        motivoDenegacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    }
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                    
                    if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, "" + nescuelas);
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importe));

                    } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, "" + nescuelas);
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, motivoDenegacion);
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
