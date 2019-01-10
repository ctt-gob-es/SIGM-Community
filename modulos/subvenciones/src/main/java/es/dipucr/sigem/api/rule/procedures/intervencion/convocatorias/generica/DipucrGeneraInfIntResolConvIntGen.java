package es.dipucr.sigem.api.rule.procedures.intervencion.convocatorias.generica;

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

import bsh.ParseException;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrGeneraInfIntResolConvIntGen extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrGeneraInfIntResolConvIntGen.class);
    
    public static final double[] DISTRIBUCION_5_COLUMNAS = {25, 15, 10, 35, 15};
    public static final double[] DISTRIBUCION_4_COLUMNAS = {25, 15, 30, 30};

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + DipucrGeneraInfIntResolConvIntGen.class);

        tipoDocumento = "Informe del servicio";
        plantilla = "Informe de Servicio Resolución Convocatoria Intervención";
        refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2;

        LOGGER.info(ConstantesString.FIN + DipucrGeneraInfIntResolConvIntGen.class);
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
                
                Iterator<?> resolucionIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosSolicitud.NOMBRE_TABLA, numexpHijo).iterator();
                if( resolucionIterator.hasNext()){
                    IItem resolucion = (IItem) resolucionIterator.next();
                    
                    String importeSolString = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosSolicitud.SUBVENCION);
                    
                    if(StringUtils.isNotEmpty(importeSolString)){
                        importeSolString = importeSolString.replace(",",".");
                        
                        //Nos quedamos sólo con el último punto
                        //[dipucr-Felipe #803] Debemos escapar el punto para que no lo asuma como expresión regular 
                        String [] importeSolStringSplit = importeSolString.split("\\.");
                        
                        if(1 == importeSolStringSplit.length){
                            importeSolString = importeSolStringSplit[0];
                        }
                        if(2 == importeSolStringSplit.length){
                            importeSolString = importeSolStringSplit[0] + "." + importeSolStringSplit[1];
                        } else if(2 < importeSolStringSplit.length){
                            importeSolString = "";
                            for (int k = 0; k<importeSolStringSplit.length - 1; k++){                                
                                importeSolString += importeSolStringSplit[k];
                            }
                            importeSolString += "." + importeSolStringSplit[importeSolStringSplit.length - 1];
                        }                    
                        
                        try{
                            double importe = Double.parseDouble(importeSolString);//[dipucr-Felipe #803]
                        	importeTotal += importe;
                        }
                        catch(NumberFormatException ex){
                        	throw new ISPACRuleException("Error en el formato del importe de subvención del expediente "
                        			+ numexpHijo + ": " + importeSolString);
                        }
                        	
                    } else{                   
                           importeTotal += 0;
                       }
                   } else{                   
                       importeTotal += 0;
                   }
               }
            
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE, SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotal));
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al setear las variables de sesión del expediente en el expediente: " + numexp + ". " + e.getMessage(), e);
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
        int numeroColumnas = 5;
        double[] distribucionColumnas = DISTRIBUCION_5_COLUMNAS;
        
        String ayuntamiento = "";
        String cif = "";
        String proyecto = "";
        String importeSolString = "";
        String importeConcString = "";
        String motivoDenegacion = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();
            
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RS;
                numeroColumnas = 5;
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS;
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RC;
                numeroColumnas = 4;
                distribucionColumnas = DISTRIBUCION_4_COLUMNAS;
            }
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoAdm, ExpedientesUtil.IDENTIDADTITULAR);
            
            int numFilas = expedientesList.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
            
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.CIF);
                
                if(5 == numeroColumnas){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.SUBVENCION_SOLICITADA);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, ConstantesString.CabeceraTabla.SUBVENCION_CONCEDIDA);
                } else if (4 == numeroColumnas){
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.PROYECTO_ACTIVIDAD);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.MEMORIA_DENEGACION);
                }
    
                int i = 1;
                for( String numexpHijo : expedientesList){
                    i++;
                    
                    ayuntamiento = "";
                    cif = "";
                    proyecto = "";
                    importeSolString = "";
                    importeConcString = "";
                    motivoDenegacion = "";
                    
                    IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);
                    
                    ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
                    cif = SubvencionesUtils.getString(expediente, ExpedientesUtil.NIFCIFTITULAR);
                    
                    IItem solicitud = (IItem) entitiesAPI.getEntities(ConstantesSubvenciones.DatosSolicitud.NOMBRE_TABLA, numexpHijo).iterator().next();
                    
                    proyecto = SubvencionesUtils.getString(solicitud, ConstantesSubvenciones.DatosSolicitud.FINALIDAD);                
                    importeSolString = SubvencionesUtils.getString(solicitud, ConstantesSubvenciones.DatosSolicitud.PRESUPUESTO);
                    importeConcString = SubvencionesUtils.getString(solicitud, ConstantesSubvenciones.DatosSolicitud.SUBVENCION);
                    
                    Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                    if(expResolucion.hasNext()){
                        IItem resolucion = (IItem) expResolucion.next();
                        
                        motivoDenegacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    }
                    
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, ayuntamiento);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                    if(5 == numeroColumnas){
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, importeSolString);
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, proyecto);
                        LibreOfficeUtil.setTextoCelda(tabla, 5, i, importeConcString);
                    } else if (4 == numeroColumnas){
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, proyecto);
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
