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

public class DipucrGeneraRenunciaConvocatorias3AsociGrupoEELL extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrGeneraRenunciaConvocatorias3AsociGrupoEELL.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }

            refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA3;

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
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {

        ArrayList<String[]> datosRenuncia = new ArrayList<String[]>();

        try {
            IClientContext cct = rulectx.getClientContext();
            
            List<String> estadosAdmList = new ArrayList<String>();
            estadosAdmList.add(ExpedientesUtil.EstadoADM.JF);
            estadosAdmList.add(ExpedientesUtil.EstadoADM.RN);
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosAdmList, ExpedientesUtil.IDENTIDADTITULAR);
            
            for (String numexpHijo : expedientesList) {
                IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);

                Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                
                if (expResolucion.hasNext()) {
                    IItem resolucion = (IItem) expResolucion.next();
                    
                    String identidadTitular = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
                    
                    String renuncia1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.RENUNCIA1);
                    String nombreGrupoAsociacion1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);
                    String cifGrupoAsociacion1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                    String importe1 = SubvencionesUtils.getFormattedDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                    
                    String renuncia2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.RENUNCIA2);
                    String nombreGrupoAsociacion2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION2);
                    String cifGrupoAsociacion2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2);
                    String importe2 = SubvencionesUtils.getFormattedDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE2);
                    
                    String renuncia3 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.RENUNCIA3);
                    String nombreGrupoAsociacion3 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION3);
                    String cifGrupoAsociacion3 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION3);
                    String importe3 = SubvencionesUtils.getFormattedDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE3);
                    
                    if (esRenuncia(renuncia1, refTabla)) { 
                        String[] datoRenuncia = { identidadTitular, nombreGrupoAsociacion1, cifGrupoAsociacion1, importe1 };
                        datosRenuncia.add(datoRenuncia);
                    }
                    
                    if (esRenuncia(renuncia2, refTabla)) {
                        String[] datoRenuncia = { identidadTitular, nombreGrupoAsociacion2, cifGrupoAsociacion2, importe2}; 
                        datosRenuncia.add(datoRenuncia);
                    }

                    if (esRenuncia(renuncia3, refTabla)) {
                        String[] datoRenuncia = { identidadTitular, nombreGrupoAsociacion3, cifGrupoAsociacion3, importe3};
                        datosRenuncia.add(datoRenuncia);
                    }
                }
            }           

            int numFilas = datosRenuncia.size();
            if(numFilas == 0){
                LibreOfficeUtil.buscaPosicion(component, refTabla);
            } else {
                XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, 4);
                if(null != tabla){
                    double [] distribucionColumnas = {25, 25, 15, 20};
                    LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
    
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.CIF);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.SUBVENCION);
    
                    int i = 1;
                    for (String[] datoRenuncia : datosRenuncia) {
                        i++;
                        LibreOfficeUtil.setTextoCelda(tabla, 1, i, datoRenuncia[0]);
                        LibreOfficeUtil.setTextoCelda(tabla, 2, i, datoRenuncia[1]);
                        LibreOfficeUtil.setTextoCelda(tabla, 3, i, datoRenuncia[2]);
                        LibreOfficeUtil.setTextoCelda(tabla, 4, i, datoRenuncia[3]);
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    private boolean esRenuncia(String renuncia, String refTabla) {
        boolean esRenuncia = false;
        
        if("SI".equals(renuncia)){
            if(LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                esRenuncia = true;
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                esRenuncia = true;
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)) {
                esRenuncia = true;
            }
        }

        return esRenuncia;
    }
}
