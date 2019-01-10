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
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.api.rule.procedures.SuperClaseUnaTabla;

public class DipucrGeneraRenunciaConvocatoriasAsociGrupoEELL extends SuperClaseUnaTabla {

    private static final Logger LOGGER = Logger.getLogger(DipucrGeneraRenunciaConvocatoriasAsociGrupoEELL.class);

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

        ArrayList<String[]> datosRenuncia1 = new ArrayList<String[]>();

        try {
            
            IClientContext cct = rulectx.getClientContext();

            List<String> estadosAdmList = new ArrayList<String>();
            estadosAdmList.add(ExpedientesUtil.EstadoADM.JF);
            estadosAdmList.add(ExpedientesUtil.EstadoADM.RN);
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosAdmList, ExpedientesUtil.IDENTIDADTITULAR);
                    
            for (String numexpHijo : expedientesList) {
                
                IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);
                String identidadTitular = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);

                Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                
                if (expResolucion.hasNext()) {
                    IItem resolucion = (IItem) expResolucion.next();
                    
                    String renuncia1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.RENUNCIA1);
                    String nombreGrupopAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);
                    String cifGrupopAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                    String importe = SubvencionesUtils.getFormattedDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);

                    if (ExpedientesUtil.EstadoADM.RN.equals(ExpedientesUtil.getEstadoAdm(cct, numexpHijo)) || "SI".equals(renuncia1)) {
                        String[] datoRenuncia = { identidadTitular, nombreGrupopAsociacion, cifGrupopAsociacion, importe};
                        datosRenuncia1.add(datoRenuncia);
                    }
                }
            }

            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)) {

                int numFilas = datosRenuncia1.size();
                XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, 4);
                if(null != tabla){
                    double [] distribucionColumnas = {25, 25, 15, 20};
                    LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                    
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.CIF);
                    LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.SUBVENCION);

                    int i = 1;
                    for (String[] datoRenuncia : datosRenuncia1) {
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
}
