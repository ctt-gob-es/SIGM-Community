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

public class DipucrGeneraRenunciaConvocatorias2AsociGrupoEELL extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrGeneraRenunciaConvocatorias2AsociGrupoEELL.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }

            refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2;

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

        int numFilas = 0;
        
        String identidadTitular = "";
        
        List<String[]> datosRenuncia = new ArrayList<String[]>();
        List<String[]> datosRenuncia1 = new ArrayList<String[]>();
        List<String[]> datosRenuncia2 = new ArrayList<String[]>();

        List<String> expedientesList;
        try {
            IClientContext cct = rulectx.getClientContext();
            
            expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.JF, ExpedientesUtil.IDENTIDADTITULAR);

            for (String numexpHijo : expedientesList) {
                
                IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);                
                identidadTitular = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);

                Iterator<?> expResolucion = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                
                if (expResolucion.hasNext()) {
                    IItem resolucion = (IItem) expResolucion.next();
                    
                    String renuncia1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.RENUNCIA1);
                    String nombreGrupoAsociacion1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);
                    String cifGrupoAsociacion1 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                    String importe1 = SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE));
                    
                    String renuncia2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.RENUNCIA2);
                    String nombreGrupoAsociacion2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION2);
                    String cifGrupoAsociacion2 = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2);
                    String importe2 = SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE2));

                    
                    if ("SI".equals(renuncia1)) {
                        String[] datoRenuncia = { identidadTitular, nombreGrupoAsociacion1, cifGrupoAsociacion1, importe1};
                        datosRenuncia1.add(datoRenuncia);
                    }
                    
                    if ("SI".equals(renuncia2)) {
                        String[] datoRenuncia = { identidadTitular, nombreGrupoAsociacion2, cifGrupoAsociacion2, importe2};
                        datosRenuncia2.add(datoRenuncia);
                    }
                }
            }
            
            

            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)) {                
                datosRenuncia = datosRenuncia1;
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)) {                
                datosRenuncia = datosRenuncia2;
            }
            
            numFilas = datosRenuncia.size();
                
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, 4);
            if(null != tabla){
                double[] distribucionColumnas = {35, 35, 15, 15};
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
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }
}
