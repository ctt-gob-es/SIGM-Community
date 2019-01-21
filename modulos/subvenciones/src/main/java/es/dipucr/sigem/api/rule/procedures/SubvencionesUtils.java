package es.dipucr.sigem.api.rule.procedures;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.QueryUtils;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class SubvencionesUtils {
    
    public static final Logger LOGGER = Logger.getLogger(SubvencionesUtils.class);
    
    public static final String DECRETO = "DECRETO";
    public static final String BOP = "BOP";
    
    public static final Map<String, String> MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA = new HashMap<String, String>();
    static{
        MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA.put(LibreOfficeUtil.ReferenciasTablas.TABLA1, TramitesUtil.DATOS_ESPECIFICOS_PROPIEDAD_TABLA1);
        MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA.put(LibreOfficeUtil.ReferenciasTablas.TABLA2, TramitesUtil.DATOS_ESPECIFICOS_PROPIEDAD_TABLA2);
        MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA.put(LibreOfficeUtil.ReferenciasTablas.TABLA3, TramitesUtil.DATOS_ESPECIFICOS_PROPIEDAD_TABLA3);
        MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA.put(LibreOfficeUtil.ReferenciasTablas.TABLA4, TramitesUtil.DATOS_ESPECIFICOS_PROPIEDAD_TABLA4);
        MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA.put(LibreOfficeUtil.ReferenciasTablas.TABLA5, TramitesUtil.DATOS_ESPECIFICOS_PROPIEDAD_TABLA5);
        MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA.put(LibreOfficeUtil.ReferenciasTablas.TABLA6, TramitesUtil.DATOS_ESPECIFICOS_PROPIEDAD_TABLA6);
        MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA.put(LibreOfficeUtil.ReferenciasTablas.TABLA7, TramitesUtil.DATOS_ESPECIFICOS_PROPIEDAD_TABLA7);
        MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA.put(LibreOfficeUtil.ReferenciasTablas.TABLA8, TramitesUtil.DATOS_ESPECIFICOS_PROPIEDAD_TABLA8);
        MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA.put(LibreOfficeUtil.ReferenciasTablas.TABLA9, TramitesUtil.DATOS_ESPECIFICOS_PROPIEDAD_TABLA9);
    }
    
    public static final String ORDEN_IGUAL = "ORDEN%";

	private static final String ORDEN_DEFECTO = ExpedientesUtil.NREG;

    public static int getInt(IItem entidad, String campo){
        int iValor = 0;
        String sValor = "";
        try {
            if(null != entidad){
                sValor = entidad.getString(campo);
                
                if(StringUtils.isInteger(sValor)){
                    iValor = Integer.parseInt(sValor.trim());
                }
            }
        } catch (ISPACException e) {
            LOGGER.error("ERROR al comprobar si la cadena: " + sValor + " es un entero. " + e.getMessage(), e);
        }
        return iValor;
    }
    
    public static int getShortAsInt(IItem entidad, String campo) {
        int iValor = 0;
        try {
            if(null != entidad){
                iValor = entidad.getShort(campo);
            }
        } catch (ISPACException e) {
            LOGGER.error("ERROR al recuperar el campo: " + campo + " del expediente: " + SubvencionesUtils.getString(entidad, "NUMEXP") + " como un short. " + e.getMessage(), e);
        }
        return iValor;
    }
    
    public static double getDouble(IItem entidad, String campo){
        double dValor = 0;
        String sValor = "";
        try {
            if(null != entidad){
                sValor = entidad.getString(campo);

                if(StringUtils.isDouble(sValor)){
                    dValor = Double.parseDouble(sValor.trim());
                }
            }
        } catch (ISPACException e) {
            LOGGER.error("ERROR al comprobar si la cadena: " + sValor + " es un double. " + e.getMessage(), e);
        }
        return dValor;
    }
    
    public static double isDouble(IItem entidad, String campo){
        double dValor = Double.MIN_VALUE;
        String sValor = "";
        try {
            if(null != entidad){
                sValor = entidad.getString(campo);
            
                if(StringUtils.isDouble(sValor)){
                    dValor = Double.parseDouble(sValor.trim());
                }
            }
        } catch (ISPACException e) {
            LOGGER.error("ERROR al comprobar si la cadena: " + sValor + " es un double. " + e.getMessage(), e);
        }
        return dValor;
    }
    
    public static double getFloat(IItem entidad, String campo) {
        float fValor = 0;
        String sValor = "";
        try {
            if(null != entidad){
                sValor = entidad.getString(campo);

                if(StringUtils.isDouble(sValor)){
                    fValor = Float.parseFloat(sValor.trim());
                }
            }
        } catch (ISPACException e) {
            LOGGER.error("ERROR al comprobar si la cadena: " + sValor + " es un float. " + e.getMessage(), e);
        }
        return fValor;
    }
    
    public static String getString(IItem entidad, String campo){
        String sValor = "";
        try{
            if(null != entidad){
                sValor = entidad.getString(campo);
            
                if(StringUtils.isEmpty(sValor)){
                    sValor= "";
                }
            }
        } catch (Exception e){
            LOGGER.error("ERROR al recuperar el campo " + campo + ". " + e.getMessage(), e);
        }
        return sValor;
    }
    
    public static Date getDate(IItem entidad, String campo) {
        Date dValor = new Date();
        try{
            if(null != entidad){
                dValor = entidad.getDate(campo);
            }
        } catch (Exception e){
            LOGGER.error("ERROR al recuperar el campo " + campo + ". " + e.getMessage(), e);
        }
        return dValor;
    }
    
    /**
     * Devuelve la lista como una cadena con el formato ('elem1',' 'elem2', ...) para la clausula WHERE de las consultas.
     * 
     * @param elementos
     * @return
     */
    public static String getWhereInFormat(List<String> elementos){
        String resultado = " ('";
        if(null != elementos && !elementos.isEmpty()){
            resultado += StringUtils.join(elementos, "','");
        }
        resultado += "') ";
        
        return resultado;
    }
    
    private SubvencionesUtils(){       
    }

    /**
     * @param elementos
     * @param campo
     * @return
     */
    public static String getWhereInFormat(IItemCollection elementos, String campo) {
        List<String> datos = new ArrayList<String>();
        String resultado = "";
        
        try{
            Iterator<?> elementosIt = elementos.iterator();
            
            if(elementosIt.hasNext()){
                while(elementosIt.hasNext()){
                    datos.add(((IItem) elementosIt.next()).getString(campo));
                }
            }
            
            resultado = getWhereInFormat(datos);
            
        } catch (ISPACException e){
            LOGGER.error("ERROR al montar la sentencia IN para el campo: " + campo + ". " + e.getMessage(), e);
            resultado = "('')";
        }
        
        return resultado;
    }

    public static String getPrimerNumexpBOP(IClientContext cct, String numexp) {
        return getNumexpBOP(cct, numexp, QueryUtils.EXPRELACIONADOS.ORDER_ASC);
    }
    
    public static String getNumexpBOP(IClientContext cct, String numexp, String orden) {
        String numexpBoletin = "";

        try{
            IItemCollection expRelacionadosPadreCollectionBop = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, ConstantesString.WHERE + ExpedientesRelacionadosUtil.NUMEXP_PADRE + " = '" + numexp + "' ORDER BY  " + ExpedientesRelacionadosUtil.NUMEXP_HIJO + " ASC ");
            Iterator<?> expRelacionadosPadreIteratorBop = expRelacionadosPadreCollectionBop.iterator();
            
            boolean encontrado = false;
            while (expRelacionadosPadreIteratorBop.hasNext() && !encontrado){
                IItem expRel = (IItem)expRelacionadosPadreIteratorBop.next();
                String numexpRel = expRel.getString(ExpedientesRelacionadosUtil.NUMEXP_HIJO);
                String nombreProc = "";
                IItem expPorc = ExpedientesUtil.getExpediente(cct, numexpRel);
                if(expPorc != null){
                    nombreProc = expPorc.getString(ExpedientesUtil.NOMBREPROCEDIMIENTO);                
                    if(nombreProc.trim().toUpperCase().contains(SubvencionesUtils.BOP)){
                        numexpBoletin = numexpRel;
                        encontrado = true;
                    }
                }
            } 
        } catch (ISPACException e ){
            LOGGER.error("ERROR al recuperar el expediente de boletín relacionado con el expediente: " + numexp + ". " + e.getMessage(), e);
        }
        
        return numexpBoletin;
    }
    
    /**
     * 
     * @param numero - double a formatear.
     * @return número formateado
     */
    public static String formateaDouble(double numero){
        return formateaDouble(ConstantesString.FORMATO_IMPORTE, numero);
    }
    
    /**
     * 
     * @param formato - String formato que hay que darle al double
     * @param numero - double a formatear.
     * @return número formateado
     */    
    public static String formateaDouble(String formato, double numero){
        return new DecimalFormat(formato).format(numero);
    }
    
    /**
     * 
     * @param numero - float a formatear.
     * @return número formateado
     */
    public static String formateaFloat(float numero){
        return formateaFloat(ConstantesString.FORMATO_IMPORTE, numero);
    }
    
    /**
     * 
     * @param formato - String formato que hay que darle al float
     * @param numero - float a formatear.
     * @return número formateado
     */    
    public static String formateaFloat(String formato, double numero){
        return new DecimalFormat(formato).format(numero);
    }
    
    /**
     * 
     * @param fecha - Date a formatear.
     * @return fecha formateada
     */
    public static String formateaFecha(Date fecha){
        return formateaFecha(ConstantesString.FORMATO_FECHA_DD_MM_YYYY, fecha);
    }
    
    
    /**
     * 
     * @param formato - String formato que hay que darle a la fecha
     * @param fecha - Date a formatear.
     * @return fecha formateada
     */
    public static String formateaFecha(String formato, Date fecha){

        String sFecha = "";
        
        if(StringUtils.isEmpty(formato)){
            formato = ConstantesString.FORMATO_FECHA_DD_MM_YYYY;
        }
        
        if (null != fecha){
            sFecha = new SimpleDateFormat(formato).format(fecha);
        }
        
        return sFecha;
    }

    public static void concatenaTextoAAsunto(IClientContext cct, String numexp, String texto) {
        concatenaTextoAAsunto(cct, numexp, texto, texto);
    }
    
    public static void concatenaTextoAAsunto(IClientContext cct, String numexp, String textoIndexOf, String textoAsunto) {
        try {
            IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
        
            if(null != expediente){                        
                String asunto = SubvencionesUtils.getString(expediente, ExpedientesUtil.ASUNTO);
                
                if(asunto.toUpperCase().indexOf(textoIndexOf) < 0){
                    asunto += textoAsunto;
                    expediente.set(ExpedientesUtil.ASUNTO, asunto);
                                                    
                    expediente.store(cct);
                }
            }
        } catch (ISPACException e) {
            LOGGER.error("ERROR al añadir al asunto del expediente: " + numexp + " la cadena: " + textoAsunto + ", buscando por: " + textoIndexOf + ". " + e.getMessage(), e);
        }
    }

    public static void insertaParticipante(IClientContext cct, String numexp, ParticipantesSubvencionesUtil participante) {
        
        try {
            if(StringUtils.isNotEmpty(participante.getNifParticipante())){
                //Comprobamos que no esté  
                
                IItemCollection nuevoParticipanteCol = ParticipantesUtil.getParticipantes( cct, numexp, ParticipantesUtil.NDOC + " = '" + participante.getNifParticipante() + "'", "");
            
                Iterator<?> nuevoParticipanteIt = nuevoParticipanteCol.iterator();
                if(!nuevoParticipanteIt.hasNext()){
                    
                    IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();    
                    IItem nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, numexp);
                    
                    if(StringUtils.isNotEmpty(participante.getRol())){
                        nuevoParticipante.set(ParticipantesUtil.ROL, participante.getRol());
                    }
                    
                    if(StringUtils.isNotEmpty(participante.getTipoPersona())){
                        nuevoParticipante.set(ParticipantesUtil.TIPO_PERSONA, participante.getTipoPersona());
                    }
                    
                    if(StringUtils.isNotEmpty(participante.getNifParticipante())){
                        nuevoParticipante.set(ParticipantesUtil.NDOC, participante.getNifParticipante());
                    }
                    
                    if(StringUtils.isNotEmpty(participante.getNombreParticipante())){
                        nuevoParticipante.set(ParticipantesUtil.NOMBRE, participante.getNombreParticipante());
                    }
                    
                    if(StringUtils.isNotEmpty(participante.getTipoDireccion())){
                        nuevoParticipante.set(ParticipantesUtil.TIPO_DIRECCION, participante.getTipoDireccion());
                    }
                    
                    if(StringUtils.isNotEmpty(participante.getRecurso())){
                        nuevoParticipante.set(ParticipantesUtil.RECURSO, participante.getRecurso());
                    }
        
                    nuevoParticipante.store(cct);
                }
            }
        } catch (ISPACException e) {
            LOGGER.error("Error al insertar el participante: " + participante.getNombreParticipante() + ", NIF/CIF: " + participante.getNifParticipante() + ", en el expediente: " + numexp + ", datos [ " + participante.getRol() + ", " + participante.getTipoPersona() + ", " + participante.getTipoDireccion() + ", " + participante.getRecurso() + "]. " + e.getMessage(), e);
        }
    }

    public static String getFormattedDouble(IItem entidad, String campo) {        
        return getFormattedDouble(entidad, campo, ConstantesString.FORMATO_IMPORTE);
    }
    
    public static String getFormattedDouble(IItem entidad, String campo, String formato) {
        if(StringUtils.isEmpty(formato)){
            formato = ConstantesString.FORMATO_IMPORTE;
        }
        
        return formateaDouble(formato, SubvencionesUtils.getDouble(entidad, campo));
    }
    
    public static String getFormattedFloat(IItem entidad, String campo) {        
        return getFormattedFloat(entidad, campo, ConstantesString.FORMATO_IMPORTE);
    }
    
    public static String getFormattedFloat(IItem entidad, String campo, String formato) {
        if(StringUtils.isEmpty(formato)){
            formato = ConstantesString.FORMATO_IMPORTE;
        }
        
        return formateaFloat(formato, SubvencionesUtils.getFloat(entidad, campo));
    }
    
    public static String getFormattedDoubleVacioSiMenorCero(IItem entidad, String campo){
        return getFormattedDoubleVacioSiMenorCero(entidad, campo, ConstantesString.FORMATO_IMPORTE);
    }
    
    public static String getFormattedDoubleVacioSiMenorCero(IItem entidad, String campo, String formato){
        String sValor = "";
        
        if(StringUtils.isEmpty(formato)){
            formato = ConstantesString.FORMATO_IMPORTE;
        }
        
        double dValor = SubvencionesUtils.getDouble(entidad, campo);
        
        if(0 < dValor){
            sValor = formateaDouble(formato, dValor);
        } else {
            sValor = "";
        }
        
        return sValor;
    }
    
    public static String getFormattedDoubleVacioSiMenorIgualCero(IItem entidad, String campo) {        
        return getFormattedDoubleVacioSiMenorIgualCero(entidad, campo, ConstantesString.FORMATO_IMPORTE);
    }
    
    public static String getFormattedDoubleVacioSiMenorIgualCero(IItem entidad, String campo, String formato){
        String sValor = "";
        
        if(StringUtils.isEmpty(formato)){
            formato = ConstantesString.FORMATO_IMPORTE;
        }
        
        double dValor = SubvencionesUtils.getDouble(entidad, campo);
        
        if(0 <= dValor){
            sValor = formateaDouble(formato, dValor);
        } else {
            sValor = "";
        }
        
        return sValor;
    }
    
    public static String getFormattedFecha(IItem entidad, String campo) {        
        return getFormattedFecha(entidad, campo, ConstantesString.FORMATO_FECHA_DD_MM_YYYY);
    }
    
    public static String getFormattedFecha(IItem entidad, String campo, String formato) {
        return formateaFecha(formato, SubvencionesUtils.getDate(entidad, campo));
    }
    
    public static String getMunicipioByValor(IClientContext cct, String valor){
        String municipio = "";
        try{
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            
            IItemCollection municipiosCollection = entitiesAPI.queryEntities(ConstantesSubvenciones.MunicipiosValidationTable.NOMBRE_TABLA, ConstantesString.WHERE + ConstantesSubvenciones.MunicipiosValidationTable.VALOR + " = '" + valor + "'");
            Iterator<?> municipiosIterator = municipiosCollection.iterator();
            
            if(municipiosIterator.hasNext()){
                municipio = ((IItem)municipiosIterator.next()).getString(ConstantesSubvenciones.MunicipiosValidationTable.SUSTITUTO);
            }
        } catch (ISPACException e){
            LOGGER.error("Error al recuperar los municipios con valor: " + valor + ". " + e.getMessage(), e);
        }
        return municipio;
    }

    public static IItemCollection getMunicipios(IClientContext cct, String consulta, String orden) {
        IItemCollection municipiosCollection = null;
        try{
            String sql = "";
            if(StringUtils.isNotEmpty(consulta)){
                if(!consulta.toUpperCase().trim().startsWith(ConstantesString.WHERE.trim())){
                    sql = ConstantesString.WHERE + consulta;
                } else {
                    sql = consulta;
                }
            
                if(StringUtils.isNotEmpty(orden) && !sql.contains(ConstantesString.ORDER_BY)){
                    if(orden.toUpperCase().indexOf(ConstantesString.ORDER_BY.trim()) > 0){
                        sql += orden;
                    } else{
                        sql += ConstantesString.ORDER_BY + orden;
                    }
                }
                
                IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
                
                municipiosCollection = entitiesAPI.queryEntities(ConstantesSubvenciones.MunicipiosValidationTable.NOMBRE_TABLA, sql);                
            }
        } catch (ISPACException e){
            LOGGER.error("Error al recuperar los municipios para la consulta: " + consulta + " y el orden: " + orden + ". " + e.getMessage(), e);
        }
        
        return municipiosCollection;
    }
    
    public static Object[] getFormatoTabla(IClientContext cct, String numexp, String refTabla, String renuncia1, String renuncia2, String renuncia3) {
        
        final double [] distribucion5Columnas = {15, 25, 30, 10, 20};
        final double [] distribucion4Columnas = {15, 35, 35, 15};
        
        Object [] resultado = new Object[8];
        
        String estadoAdm = "";
        
        String ayuntamiento = "";
        String nreg = "";
        String cifAsociacion = "";
        String nombreAsociacion = "";
        String motivoDenegacion = "";
        String motivoRenuncia = "";
        String renuncia = "";
            
        try{
            IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
            
            estadoAdm = SubvencionesUtils.getString(expediente, ExpedientesUtil.ESTADOADM);
            
            ayuntamiento = SubvencionesUtils.getString(expediente, ExpedientesUtil.IDENTIDADTITULAR);
            nreg = SubvencionesUtils.getString(expediente, ExpedientesUtil.NREG);
            
            Iterator<?> expResolucion = cct.getAPI().getEntitiesAPI().getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexp).iterator();
            if (expResolucion.hasNext()) {
                
                IItem resolucion = (IItem) expResolucion.next();
                
                if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
                    renuncia = renuncia1;
                    
                    motivoDenegacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
                    cifAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION);
                    nombreAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION);
                    motivoRenuncia = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA1);
                    
                } else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
                    renuncia = renuncia2;
                    
                    motivoDenegacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO2);
                    cifAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION2);
                    nombreAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION2);
                    motivoRenuncia = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA2);
                    
                } else if (LibreOfficeUtil.ReferenciasTablas.TABLA3.equals(refTabla)){
                    renuncia = renuncia3;
                    
                    motivoDenegacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO3);
                    cifAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.CIFGRUPOASOCIACION3);
                    nombreAsociacion = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.NOMBREGRUPOASOCIACION3);
                    motivoRenuncia = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA3);                    
                }
            }
            
            if(esRenuncia(renuncia)){
                resultado[0] = distribucion5Columnas;
                resultado[1] = 5;
                resultado[2] = ConstantesString.CabeceraTabla.MOTIVO_RENUNCIA;
                resultado[3] = motivoRenuncia;
                
            } else if (ExpedientesUtil.EstadoADM.RS.equals(estadoAdm)) {
                resultado[0] = distribucion4Columnas;
                resultado[1] = 4;
                resultado[2] = "";
                resultado[3] = "";
                
            } else {
                resultado[0] = distribucion5Columnas;
                resultado[1] = 5;
                resultado[2] = ConstantesString.CabeceraTabla.MOTIVO_DENEGACION;
                resultado[3] = motivoDenegacion;
            }
            
            resultado[4] = nreg;
            resultado[5] = ayuntamiento;
            resultado[6] = cifAsociacion;
            resultado[7] = nombreAsociacion;
            
        } catch(ISPACException e){
            LOGGER.error("ERROR al recuperar los datos para la tabla del expediente: " + numexp + ". " + e.getMessage(), e);
        }
        
        return resultado;
    }

    public static String getTextoResol(String cifAsociacion, String renuncia, String estadoAdm) {
        
        final String textoResolucion = "Esta Vicepresidencia propone la concesión de subvención, conforme al siguiente detalle: ";
        final String textoDenegacion = "Esta Vicepresidencia propone la denegación de la siguiente solicitud, por el motivo que se indica: ";
        final String textoRenuncia = "Ayuntamientos que han renunciado: ";
        
        String textoResol = "";
        
        if(StringUtils.isEmpty(cifAsociacion)){
            textoResol = "";
        } else if (esRenuncia(renuncia)){
            textoResol = textoRenuncia;
        } else if (ExpedientesUtil.EstadoADM.RS.equals(estadoAdm)) {
            textoResol = textoResolucion;
        } else {
            textoResol = textoDenegacion;
        }
        
        return textoResol;
    }
    
    public static boolean esRenuncia(String renuncia) {
        return "SI".equalsIgnoreCase(renuncia);
    }

    public static void insertaTabajadorSocialComoParticipante( IClientContext cct, String numexp, String nifTrabajadorSocial, String trabajadorSocial) {
        
        if(StringUtils.isNotEmpty(nifTrabajadorSocial)){
            ParticipantesSubvencionesUtil participante = new ParticipantesSubvencionesUtil(nifTrabajadorSocial, trabajadorSocial);
            
            participante.setRol(ParticipantesUtil._TIPO_INTERESADO);
            participante.setTipoDireccion(ParticipantesUtil.TIPO_DIRECCION_TELEMATICA);
            participante.setTipoPersona(ParticipantesUtil._TIPO_PERSONA_FISICA);
            
            SubvencionesUtils.insertaParticipante(cct, numexp, participante);
        }
    }

    public static List<String> getCabeceras(List<ConfiguracionColumna> atributosTabla) {
        List <String> cabeceras = new ArrayList<String>();
        
        for(ConfiguracionColumna columna : atributosTabla){
            cabeceras.add(columna.getCabeceraColumna());
        }
        
        return cabeceras;
    }
    
    public static double[] getDistribucionColumnas(List<ConfiguracionColumna> atributosTabla) {
        double[] distribucionColumnas = new double[atributosTabla.size()];
        
        for(int i = 0; i < atributosTabla.size() && null != distribucionColumnas; i++){
            ConfiguracionColumna columna = atributosTabla.get(i);
            if(columna.tieneDistribucion()){
                distribucionColumnas[i] = columna.getAncho();
            } else {
                distribucionColumnas = null;
            }
        }
        
        return distribucionColumnas;
    }
    
    public static void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, List<ConfiguracionColumna> configuracion, List<ObjetoSolictudConvocatoriaSubvencion> expedientesList){
        int numColumnas = configuracion.size();
        int numFilas = expedientesList.size();
        try {
            if(numFilas <= 0){
                LibreOfficeUtil.buscaPosicion(component, refTabla);
            } else {
                XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, numColumnas);
            
                if(null != tabla){
                    List<String> cabecerasTabla = SubvencionesUtils.getCabeceras(configuracion);
                    
                    double[] distribucionColumnas = SubvencionesUtils.getDistribucionColumnas(configuracion);
                    
                    if(null != distribucionColumnas){
                        LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
                    }
                    
                    for(int columna = 1; columna <= cabecerasTabla.size(); columna++){                
                        LibreOfficeUtil.setTextoCeldaCabecera(tabla, columna, cabecerasTabla.get(columna-1));
                    }

                    for (int fila = 1; fila <= expedientesList.size(); fila++){
                        ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = expedientesList.get(fila-1);
                        solicitudConvocatoria.insertaParticipante(rulectx.getClientContext(), rulectx.getNumExp());
                        
                        for (int columna = 1; columna <= numColumnas; columna++){
                            ConfiguracionColumna atributo = configuracion.get(columna-1);
                            LibreOfficeUtil.setTextoCelda(tabla, columna, fila+1, solicitudConvocatoria.getCampo(atributo));
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al iniciar la tabla: " + refTabla + "." + e.getMessage(), e);
        }
    }

    public static int getTipoDato(IItem entidad, String nombreColumna) {
        int tipoDato = Integer.MIN_VALUE;
        
        try{
            tipoDato = entidad.getProperty(nombreColumna).getType();
        } catch (Exception e){
            LOGGER.error("Error al recuperar el tipo de dato de la columna: " + nombreColumna + "." + e.getMessage(), e);
        }
        
        return tipoDato;
    }
    
    public static List<ConfiguracionColumna> getConfiguracionTabla(String configuracionTabla){
        List<ConfiguracionColumna> configuracion = new ArrayList<ConfiguracionColumna>();
        
        if(StringUtils.isNotEmpty(configuracionTabla)){
            String[] atributosTabla = configuracionTabla.split("\\|");

            for (int i = 0; i < atributosTabla.length; i++){            	
            	String atributos = atributosTabla[i];
            	            
            	if(!StringUtils.contains(atributos, ORDEN_IGUAL)){
		            String[] atributo = atributos.split("%");
		                
		            if(StringUtils.isNotEmpty(atributo)){
	                    ConfiguracionColumna confColumna = new ConfiguracionColumna();
	                    
	                    String[] infoColumna = atributo[0].split("\\.");
	                    
	                    confColumna.setTablaDato(infoColumna[0]);
	                    confColumna.setNombreColumna(infoColumna[1]);
	                    
	                    if(atributo.length >= 2){
	                        confColumna.setAncho(Double.parseDouble(atributo[1]));
	                    }
	                    
	                    if(atributo.length >= 3){
	                        confColumna.setFormatter(atributo[2]);
	                    }
	                    
	                    configuracion.add(confColumna);
	                }
            	}
             }        
        }
        return configuracion;
    }
    
    public static String getConfiguracionTabla(IRuleContext rulectx, String refTabla){
        String configuracionTabla = "";
        
        try{
            configuracionTabla = TramitesUtil.getPropiedadDatosEspecificos(rulectx.getClientContext(), rulectx.getTaskProcedureId(), MAPEO_REFERENCIA_DATOS_ESPECIFICOS_TABLA.get(refTabla));
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
                
        return configuracionTabla;
    }
    
    public static String getOrdenTabla(String configuracionTabla){
        String orden = "";
        
        if(StringUtils.isNotEmpty(configuracionTabla)){
            String[] atributosTabla = configuracionTabla.split("\\|");

            for (int i = 0; i < atributosTabla.length; i++){
            	String atributos = atributosTabla[i];
            	
            	if(StringUtils.contains(atributos, ORDEN_IGUAL)){
            		String[] atributo = atributos.split("%"); 
            		orden = atributo[1];
            	}
            }        
        }
        return orden;
    }
    
    public static List<ConfiguracionColumna> getConfiguracionColumnas(IRuleContext rulectx, String refTabla){                
        return SubvencionesUtils.getConfiguracionTabla(SubvencionesUtils.getConfiguracionTabla(rulectx, refTabla));
    }
    
    public static String getOrdenTabla(IRuleContext rulectx, String refTabla){
        String orden = SubvencionesUtils.getOrdenTabla(SubvencionesUtils.getConfiguracionTabla(rulectx, refTabla));
        
        if (StringUtils.isEmpty(orden)){
        	orden = ORDEN_DEFECTO;
        }
        return orden;
    }

    public static boolean esShort(int tipoDato) {
        return tipoDato == Types.BIT || tipoDato == Types.TINYINT || tipoDato == Types.SMALLINT;
    }

    public static boolean esInt(int tipoDato) {        
        return tipoDato == Types.INTEGER || tipoDato == Types.BIGINT;
    }

    public static boolean esDouble(int tipoDato) {
        return tipoDato == Types.REAL || tipoDato == Types.FLOAT || tipoDato == Types.DOUBLE;    
    }

    public static boolean esFloat(int tipoDato) {
        return tipoDato == Types.NUMERIC || tipoDato == Types.DECIMAL;
    }

    public static boolean esString(int tipoDato) {
        return tipoDato == Types.CHAR || tipoDato == Types.VARCHAR || tipoDato == Types.LONGVARCHAR;
    }

    public static boolean esFecha(int tipoDato) {
        return tipoDato == Types.DATE || tipoDato == Types.TIMESTAMP;
    }
}
