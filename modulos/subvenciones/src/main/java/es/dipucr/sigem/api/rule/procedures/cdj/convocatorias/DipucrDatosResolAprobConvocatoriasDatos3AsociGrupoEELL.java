package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
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

public class DipucrDatosResolAprobConvocatoriasDatos3AsociGrupoEELL extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriasDatos3AsociGrupoEELL.class);
    
    public static final double[] DISTRIBUCION_COLUMNAS_RESOLUCION = {22, 25, 30, 13, 10};
    public static final double[] DISTRIBUCION_COLUMNAS_RECHAZO = {22, 20, 25, 13, 20};
    public static final double[] DISTRIBUCION_COLUMNAS_RENUNCIA = {22, 20, 25, 13, 20};

    public static final String TEXTO1 = "Una vez comprobada y verificada la documentación remitida por los solicitantes, conforme a lo dispuesto en las bases de la convocatoria, procede el otorgamiento de las siguientes subvenciones:";
    public static final String TEXTO2 = "No procede el otorgamiento de la subvención a los solicitantes que seguidamente se indican, por los motivos que se señalan en cada caso:";
    public static final String TEXTO3 = "Ayuntamientos que renuncian:";
    public static final String TEXTO4 = "Una vez comprobada y verificada la documentación remitida por los solicitantes, conforme a lo dispuesto en las bases de la convocatoria, procede el otorgamiento de las siguientes subvenciones:";
    public static final String TEXTO5 = "No procede el otorgamiento de la subvención a los solicitantes que seguidamente se indican, por los motivos que se señalan en cada caso:";
    public static final String TEXTO6 = "Ayuntamientos que renuncian:";
    public static final String TEXTO7 = "Una vez comprobada y verificada la documentación remitida por los solicitantes, conforme a lo dispuesto en las bases de la convocatoria, procede el otorgamiento de las siguientes subvenciones:";
    public static final String TEXTO8 = "No procede el otorgamiento de la subvención a los solicitantes que seguidamente se indican, por los motivos que se señalan en cada caso:";
    public static final String TEXTO9 = "Ayuntamientos que renuncian:";

    protected List <String[]> datosResolucion1 = new ArrayList<String[]>();
    protected List <String[]> datosResolucion2 = new ArrayList<String[]>();
    protected List <String[]> datosResolucion3 = new ArrayList<String[]>();
    
    protected List <String[]> datosRechazados1 = new ArrayList<String[]>();
    protected List <String[]> datosRechazados3 = new ArrayList<String[]>();
    protected List <String[]> datosRechazados2 = new ArrayList<String[]>();
    
    protected List <String[]> datosRenuncia1 = new ArrayList<String[]>();
    protected List <String[]> datosRenuncia2 = new ArrayList<String[]>();
    protected List <String[]> datosRenuncia3 = new ArrayList<String[]>();

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }

            refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA3
                        + "," + LibreOfficeUtil.ReferenciasTablas.TABLA4 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA5 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA6
                        + "," + LibreOfficeUtil.ReferenciasTablas.TABLA7 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA8 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA9;           
        } catch (ISPACException e) {
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

            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.IDENTIDADTITULAR);

            for (String numexpHijo : expedientesList) {
                IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);
                
                Iterator<?> resolucionIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                
                String ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
                String nreg = SubvencionesUtils.getString(expediente, ExpedientesUtil.NREG);
                
                if (resolucionIterator.hasNext()) {
                    IItem resolucion = (IItem) resolucionIterator.next();
                    
                    String numexpResolucion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NUMEXP);
                    double importe1 = SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                    double importe2 = SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE2);
                    double importe3 = SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE3);
                    
                    if(importe1 > 0){
                        importeTotal += importe1;
                        String nombreAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);    
                        String cifAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                        
                        String[] dato = {numexpResolucion, nreg, ayuntamiento, nombreAsociacion, cifAsociacion, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importe1)}; 
                        datosResolucion1.add(dato);
                    }
                    if(importe2 > 0){
                        importeTotal += importe2;
                        String nombreAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION2);    
                        String cifAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2);
                        
                        String[] dato = {numexpResolucion, nreg, ayuntamiento, nombreAsociacion, cifAsociacion, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importe2)}; 
                        datosResolucion2.add(dato);
                    }
                    if(importe3 > 0){
                        importeTotal += importe3;
                        String nombreAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION3);    
                        String cifAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION3);
                        
                        String[] dato = {numexpResolucion, nreg, ayuntamiento, nombreAsociacion, cifAsociacion, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importe3)}; 
                        datosResolucion3.add(dato);
                    }
                } 
            }

            List<String> estadosADM = new ArrayList<String>();
            estadosADM.add(ExpedientesUtil.EstadoADM.RS);
            estadosADM.add(ExpedientesUtil.EstadoADM.RC);
            estadosADM.add(ExpedientesUtil.EstadoADM.RN);
            
            expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosADM);
            
            for (String numexpHijo : expedientesList){
                IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);
                
                String ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
                String nreg = SubvencionesUtils.getString(expediente, ExpedientesUtil.NREG);
                Iterator<?> rechazadoIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                
                if (rechazadoIterator.hasNext()) {
                    IItem rechazado = (IItem) rechazadoIterator.next();
                    
                    String motivoRechazo1 = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);    
                    if (StringUtils.isNotEmpty(motivoRechazo1)){
                        String numexpRechazo = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NUMEXP);
                        String nombreAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);    
                        String cifAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                        
                        String[] dato = {numexpRechazo, nreg, ayuntamiento, nombreAsociacion, cifAsociacion, motivoRechazo1}; 
                        datosRechazados1.add(dato);
                    }
                    
                    String motivoRechazo2 = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO2);                       
                    if (StringUtils.isNotEmpty(motivoRechazo2)){
                        String numexpRechazo = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NUMEXP);
                        String nombreAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION2);    
                        String cifAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2);
                        
                        String[] dato = {numexpRechazo, nreg, ayuntamiento, nombreAsociacion, cifAsociacion, motivoRechazo2}; 
                        datosRechazados2.add(dato);
                    }
                    
                    String motivoRechazo3 = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO3);                           
                    if (StringUtils.isNotEmpty(motivoRechazo3)){
                        String numexpRechazo = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NUMEXP);
                        String nombreAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION3);    
                        String cifAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION3);
                        
                        String[] dato = {numexpRechazo, nreg, ayuntamiento, nombreAsociacion, cifAsociacion, motivoRechazo3}; 
                        datosRechazados3.add(dato);
                    }     
                    
                    String motivoRenuncia1 = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA1);                                    
                    if (StringUtils.isNotEmpty(motivoRenuncia1)){
                        String numexpRenuncia = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NUMEXP);
                        String nombreAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);    
                        String cifAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                        
                        String[] dato = {numexpRenuncia, nreg, ayuntamiento, nombreAsociacion, cifAsociacion, motivoRenuncia1}; 
                        datosRenuncia1.add(dato);
                    }
                    
                    String motivoRenuncia2 = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA2);                                    
                    if (StringUtils.isNotEmpty(motivoRenuncia2)){
                        String numexpRenuncia = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NUMEXP);
                        String nombreAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION2);    
                        String cifAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2);
                        
                        String[] dato = {numexpRenuncia, nreg, ayuntamiento, nombreAsociacion, cifAsociacion, motivoRenuncia2}; 
                        datosRenuncia2.add(dato);
                    }
                    
                    String motivoRenuncia3 = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA3);                                    
                    if (StringUtils.isNotEmpty(motivoRenuncia3)){
                        String numexpRenuncia = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NUMEXP);
                        String nombreAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION3);    
                        String cifAsociacion = SubvencionesUtils.getString(rechazado, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION3);
                        
                        String[] dato = {numexpRenuncia, nreg, ayuntamiento, nombreAsociacion, cifAsociacion, motivoRenuncia3}; 
                        datosRenuncia3.add(dato);
                    }                    
                }
            }

            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotal));
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO1, TEXTO1);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO2, TEXTO2);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO3, TEXTO3);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO4, TEXTO4);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO5, TEXTO5);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO6, TEXTO6);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO7, TEXTO7);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO8, TEXTO8);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO9, TEXTO9);
            
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO1);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO2);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO3);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO4);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO5);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO6);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO7);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO8);
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO9);

        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        // Banda o Grupo 1
        if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)) {
            insertaTabla(component, refTabla, DISTRIBUCION_COLUMNAS_RESOLUCION, ConstantesString.CabeceraTabla.IMPORTE, datosResolucion1);
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)) {
            insertaTabla(component, refTabla, DISTRIBUCION_COLUMNAS_RECHAZO, ConstantesString.CabeceraTabla.MOTIVO_DENEGACION, datosRechazados1);
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)) {
            insertaTabla(component, refTabla, DISTRIBUCION_COLUMNAS_RENUNCIA, ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA, datosRenuncia1);
         // Banda o Grupo 2
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA4.equals(refTabla)) { 
            insertaTabla(component, refTabla, DISTRIBUCION_COLUMNAS_RESOLUCION, ConstantesString.CabeceraTabla.IMPORTE, datosResolucion2);
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA5.equals(refTabla)) {
            insertaTabla(component, refTabla, DISTRIBUCION_COLUMNAS_RECHAZO, ConstantesString.CabeceraTabla.MOTIVO_DENEGACION, datosRechazados2);
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA6.equals(refTabla)) {
            insertaTabla(component, refTabla, DISTRIBUCION_COLUMNAS_RENUNCIA, ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA, datosRenuncia2);
         // Banda o Grupo 3
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA7.equals(refTabla)) { 
            insertaTabla(component, refTabla, DISTRIBUCION_COLUMNAS_RESOLUCION, ConstantesString.CabeceraTabla.IMPORTE, datosResolucion3);
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA8.equals(refTabla)) {
            insertaTabla(component, refTabla, DISTRIBUCION_COLUMNAS_RECHAZO, ConstantesString.CabeceraTabla.MOTIVO_DENEGACION, datosRechazados3);
        } else if (LibreOfficeUtil.ReferenciasTablas.TABLA9.equals(refTabla)) {
            insertaTabla(component, refTabla, DISTRIBUCION_COLUMNAS_RENUNCIA, ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA, datosRenuncia3);
        }
    }
    
    public void insertaTabla(XComponent component, String refTabla, double[] distribucionColumnas, String tituloColumna5, List<String[]> datos){
        int numFilas = datos.size();
        
        if(numFilas <= 0){
            LibreOfficeUtil.buscaPosicion(component, refTabla);
        } else {        
            XTextTable tabla = null;
            try {
                tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1 , 5);
            } catch (Exception e) {
                LOGGER.error(ConstantesString.LOGGER_ERROR + " al iniciar la tabla: " + refTabla + "." + e.getMessage(), e);
            }
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                try {
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.NUM_REGISTRO);                
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.CIF);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, tituloColumna5);
    
                    int i = 1;
                    for (String[]dato : datos){
                        i++;                        
                        LibreOfficeUtil.setTextoCelda(tabla, 1, i, dato[1]);
                        LibreOfficeUtil.setTextoCelda(tabla, 2, i, dato[2]);
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, dato[3]);
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, dato[4]);
                        LibreOfficeUtil.setTextoCelda(tabla, 5, i, dato[5]);
                    }
                } catch (Exception e) {
                    LOGGER.error(ConstantesString.LOGGER_ERROR + " al insertar en la tabla: " + refTabla + e.getMessage(), e);
                }
            }
        }
    }
}
