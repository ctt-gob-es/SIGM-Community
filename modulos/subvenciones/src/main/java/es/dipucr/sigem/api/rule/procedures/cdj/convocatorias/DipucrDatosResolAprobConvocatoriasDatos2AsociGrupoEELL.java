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
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

/**
 * Ir cambiando a la regla que recupera las columnas de los datos específicos del trámite.
 *
 * @deprecated DipucrDatosResolAprobConvocatoriasDatos3AsociGrupoEELL en su lugar
 */
@Deprecated
public class DipucrDatosResolAprobConvocatoriasDatos2AsociGrupoEELL extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriasDatos2AsociGrupoEELL.class);
    
    public static final double[] DISTRIBUCION_5_COLUMNAS_RS = {15, 25, 35, 15, 10};
    public static final double[] DISTRIBUCION_5_COLUMNAS_RC_RS = {15, 25, 25, 15, 20};

    protected String texto1;
    protected String texto2;
    protected String texto3;
    protected String texto4;

    protected int hayRechazados1 = 0;
    protected int hayRechazados2 = 0;

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }

            refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA3 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA4;

            texto1 = "Una vez comprobada y verificada la documentación remitida por los solicitantes, conforme a lo dispuesto en las bases de la convocatoria, procede el otorgamiento de las siguientes subvenciones:";
            texto2 = "No procede el otorgamiento de la subvención a los solicitantes que seguidamente se indican, por los motivos que se señalan en cada caso:";
            texto3 = "Una vez comprobada y verificada la documentación remitida por los solicitantes, conforme a lo dispuesto en las bases de la convocatoria, procede el otorgamiento de las siguientes subvenciones:";
            texto4 = "No procede el otorgamiento de la subvención a los solicitantes que seguidamente se indican, por los motivos que se señalan en cada caso:";

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
            String textoRechazo = "";
            String textoRechazo2 = "";

            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.IDENTIDADTITULAR);
            
            for(String numexpHijo : expedientesList){
                Iterator<?> resolucionIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                if (resolucionIterator.hasNext()) {
                    IItem resolucion = (IItem) resolucionIterator.next();
                    importeTotal += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
                    importeTotal += SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE2);
                } else {
                    importeTotal += 0;
                }
            }

            List<String> estadosAdmList = new ArrayList<String>();
            estadosAdmList.add(ExpedientesUtil.EstadoADM.RC);
            estadosAdmList.add(ExpedientesUtil.EstadoADM.RS);
            expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosAdmList, ExpedientesUtil.IDENTIDADTITULAR);
            
            for(String numexpHijo : expedientesList){
                Iterator<?> rechazadoIterator = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpHijo).iterator();
                if (rechazadoIterator.hasNext()) {
                    IItem rechazado = (IItem) rechazadoIterator.next();
                    if (StringUtils.isNotEmpty(rechazado.getString(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO))) {
                        textoRechazo = texto2;
                        hayRechazados1++;
                    }
                    if (StringUtils.isNotEmpty(rechazado.getString(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO2))) {
                        textoRechazo2 = texto4;
                        hayRechazados2++;
                    }
                }
            }

            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.IMPORTE, "" + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeTotal));
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO1, texto1);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO2, textoRechazo);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO3, texto3);
            cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO4, textoRechazo2);
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

        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
   
    @SuppressWarnings("unchecked")
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        String textoCabeceraColumna5 = "";
        double [] distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;
        
        XTextTable tabla = null;
        
        List<String> expedientesList = new ArrayList<String>();

        try {
            Object [] resultado = creaTabla(rulectx, component, refTabla, numexp);
            
            tabla = (XTextTable)resultado[0];
            expedientesList = (List<String>)resultado[1];
            textoCabeceraColumna5 = (String)resultado[2];
            distribucionColumnas = (double[])resultado[3];

            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, ConstantesString.CabeceraTabla.NUM_REGISTRO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, ConstantesString.CabeceraTabla.AYUNTAMIENTO);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, ConstantesString.CabeceraTabla.GRUPO_ASOCIACION);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, ConstantesString.CabeceraTabla.CIF);
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, textoCabeceraColumna5);
                
                int i = 1;
                for(String numexpHijo : expedientesList){
                    i++;
                    insertaFila(rulectx.getClientContext(), tabla, refTabla, numexpHijo, i, numexp);
                }
            } 
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }
    
    private Object[] creaTabla(IRuleContext rulectx, XComponent component, String refTabla, String numexp) {
        Object[] resultado = new Object[4];
        XTextTable tabla = null;
        List<String> expedientesList = new ArrayList<String>();
        String textoCabeceraColumna5 = "";
    
        String consulta = "";
        String estadoAdm = ExpedientesUtil.EstadoADM.RS;
        List<String> estadosAdmList;
        int numeroColumnas = 5;
        double [] distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;
        int numFilas = 0;
        
        try{
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)) {
                estadoAdm = ExpedientesUtil.EstadoADM.RS;
                textoCabeceraColumna5 = ConstantesString.CabeceraTabla.IMPORTE;
                numeroColumnas = 5;
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;
                
                consulta = ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " = " + ExpedientesRelacionadosUtil.NUMEXP_HIJO + ConstantesString.AND + ExpedientesRelacionadosUtil.NUMEXP_PADRE + " = '" + numexp + "'"
                            + ConstantesString.AND + ExpedientesUtil.ESTADOADM + " = '" + estadoAdm + "'"
                            + ConstantesString.AND + ExpedientesUtil.NUMEXP + " IN (SELECT " + ConstantesSubvenciones.DatosResolucion.NUMEXP + ConstantesString.FROM + ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA
                                                    + ConstantesString.WHERE + ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION + " IS NOT NULL OR " + ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION + " != '')"
                        + "     ORDER BY " + ExpedientesUtil.NREG;
                
                expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByQuery(rulectx.getClientContext(), consulta);
                
                numFilas = expedientesList.size();
                tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RS;
                textoCabeceraColumna5 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
                numeroColumnas = 5;
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RC_RS;
                
                estadosAdmList = new ArrayList<String>();
                estadosAdmList.add(ExpedientesUtil.EstadoADM.RC);
                estadosAdmList.add(ExpedientesUtil.EstadoADM.RS);
                expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosAdmList, ExpedientesUtil.IDENTIDADTITULAR);
                
                if (!expedientesList.isEmpty() || 0 < hayRechazados1 ) {
                    numFilas = hayRechazados1;
                    tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
                }
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)) {
                estadoAdm = ExpedientesUtil.EstadoADM.RS;
                textoCabeceraColumna5 = ConstantesString.CabeceraTabla.IMPORTE;
                numeroColumnas = 5;
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RS;
                
                consulta = ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " = " + ExpedientesRelacionadosUtil.NUMEXP_HIJO + ConstantesString.AND + ExpedientesRelacionadosUtil.NUMEXP_PADRE + " = '" + numexp + "'"
                            + ConstantesString.AND + ExpedientesUtil.ESTADOADM + " = '" + estadoAdm + "'"
                            + ConstantesString.AND + ExpedientesUtil.NUMEXP + " IN (SELECT " + ConstantesSubvenciones.DatosResolucion.NUMEXP + ConstantesString.FROM + ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA
                                                    + ConstantesString.WHERE + ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2 + " IS NOT NULL OR " + ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2 + " != '')"
                        + "     ORDER BY " + ExpedientesUtil.NREG;
                
                expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByQuery(rulectx.getClientContext(), consulta);
                
                numFilas = expedientesList.size();
                tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);                
            
            }  else if (LibreOfficeUtil.ReferenciasTablas.TABLA4.equals(refTabla)){
                estadoAdm = ExpedientesUtil.EstadoADM.RS;
                textoCabeceraColumna5 = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
                numeroColumnas = 5;
                distribucionColumnas = DISTRIBUCION_5_COLUMNAS_RC_RS;
                
                estadosAdmList = new ArrayList<String>();
                estadosAdmList.add(ExpedientesUtil.EstadoADM.RC);
                estadosAdmList.add(ExpedientesUtil.EstadoADM.RS);
                expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, estadosAdmList, ExpedientesUtil.IDENTIDADTITULAR);
                
                if (!expedientesList.isEmpty() || 0 < hayRechazados2 ) {
                    numFilas = hayRechazados2;
                    tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numeroColumnas);
                }
            }
        } catch (Exception e){
            LOGGER.error("ERROR al generar la tabla. " + e.getMessage(), e);
        }
        
        resultado[0] = tabla;
        resultado[1] = expedientesList;
        resultado[2] = textoCabeceraColumna5;
        resultado[3] = distribucionColumnas;
        
        return resultado;
    }


    private void insertaFila(IClientContext cct, XTextTable tabla, String refTabla, String numexpHijo, int numFila, String numexpConvocatoria) {
        String beneficiario = "";
        String nreg = "";

        String cifAsociacion1 = "";
        String nombreAsociacion1 = "";
        String importe1 = "";
        String motivoDenegacion1 = "";
        
        String cifAsociacion2 = "";
        String nombreAsociacion2 = "";
        String importe2 = "";
        String motivoDenegacion2 = "";
        
        try{
            ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(cct, numexpHijo);
        
            beneficiario = solicitudConvocatoria.getBeneficiario();
            nreg = solicitudConvocatoria.getNREG();                    

            importe1 = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.IMPORTE, ConstantesString.FORMATO_IMPORTE);
            cifAsociacion1 = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
            nombreAsociacion1 = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);
            motivoDenegacion1 = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);

            importe2 = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.IMPORTE2, ConstantesString.FORMATO_IMPORTE);
            cifAsociacion2 = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2);
            nombreAsociacion2 = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION2);
            motivoDenegacion2 = solicitudConvocatoria.getCampoResolucion( ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO2);
            
            solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_JURIDICA);
            solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_PERSONAS_FISICAS_EMPR);
            solicitudConvocatoria.insertaParticipante(cct, numexpConvocatoria);
           
            LibreOfficeUtil.setTextoCelda(tabla, 1, numFila, nreg);
            LibreOfficeUtil.setTextoCelda(tabla, 2, numFila, beneficiario);
            
            if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)) {
                LibreOfficeUtil.setTextoCelda(tabla, 3, numFila, nombreAsociacion1);
                LibreOfficeUtil.setTextoCelda(tabla, 4, numFila, cifAsociacion1);
                LibreOfficeUtil.setTextoCelda(tabla, 5, numFila, importe1);
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)) {
                LibreOfficeUtil.setTextoCelda(tabla, 3, numFila, nombreAsociacion1);
                LibreOfficeUtil.setTextoCelda(tabla, 4, numFila, cifAsociacion1);
                LibreOfficeUtil.setTextoCelda(tabla, 5, numFila, motivoDenegacion1);
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)) {
                LibreOfficeUtil.setTextoCelda(tabla, 3, numFila, nombreAsociacion2);
                LibreOfficeUtil.setTextoCelda(tabla, 4, numFila, cifAsociacion2);
                LibreOfficeUtil.setTextoCelda(tabla, 5, numFila, importe2);
                
            } else if (LibreOfficeUtil.ReferenciasTablas.TABLA4.equals(refTabla)) {
                LibreOfficeUtil.setTextoCelda(tabla, 3, numFila, nombreAsociacion2);
                LibreOfficeUtil.setTextoCelda(tabla, 4, numFila, cifAsociacion2);
                LibreOfficeUtil.setTextoCelda(tabla, 5, numFila, motivoDenegacion2);
            }
        } catch (Exception e) {
            LOGGER.error("ERROR al inserta la fila: " + numFila + " que se corresponde con la solcitud: " + numexpHijo + ". " + e.getMessage(), e);
        }
    }
}
