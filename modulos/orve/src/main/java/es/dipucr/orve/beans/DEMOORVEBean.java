package es.dipucr.orve.beans;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.services.dto.Entidad;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import es.dipucr.orve.sw.constantes.ConstantesWSExportacion;
import es.dipucr.orve.sw.constantes.ObtenerIdentificadoresMensajesSW;
import es.dipucr.orve.sw.constantes.ObtenerRegistroMensajesSW;
import es.dipucr.sicres30.FicheroIntercambioSICRES3;
import es.dipucr.sicres30.utils.SICRES30Utils;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;
import es.minhap.seap.ssweb.demoorve.FiltrosIdentificadores;
import es.minhap.seap.ssweb.demoorve.ObtenerIdentificaoresRespuestaWS;
import es.minhap.seap.ssweb.demoorve.ObtenerRegistroRespuestaWS;
import es.minhap.seap.ssweb.demoorve.Security;
import es.minhap.seap.ssweb.demoorve.WSExportacionPortType;
import es.minhap.seap.ssweb.demoorve.WSExportacionPortTypeProxy;
import es.minhap.seap.ssweb.demoorve.holders.SecurityHolder;

public class DEMOORVEBean extends ORVEBeanSuperClass implements ORVEBeanInterface{
    
    public static final Logger LOGGER = Logger.getLogger(DEMOORVEBean.class);
    
    private WSExportacionPortType wsExportacion = null;
    private SecurityHolder securityHolder = null;
    
    public DEMOORVEBean(Entidad entidad){
        super(entidad);
                
        if (null != propiedadesSW){
            getWSExportacionConfig();        
            getSecurityHolderConfig();

        } else {
            LOGGER.error(MENSAJE_ERROR_NO_EXISTE_PROPIEDADES + ServiciosWebConfiguration.DEFAULT_CONFIG_FILENAME + MENSAJE_PARA_LA_ENTIDAD + entidad.getIdentificador() + " - " + entidad.getNombreLargo());
        }
    }
    
    public boolean isTieneConfiguracion() {
        return null != wsExportacion && null != securityHolder;
    }

    public WSExportacionPortType getWsExportacion() {
        return wsExportacion;
    }

    public void setWsExportacion(WSExportacionPortType wsExportacion) {
        this.wsExportacion = wsExportacion;
    }

    public SecurityHolder getSecurityHolder() {
        return securityHolder;
    }

    public void setSecurityHolder(SecurityHolder securityHolder) {
        this.securityHolder = securityHolder;
    }
    
    private void getWSExportacionConfig() {
            
        if(null != propiedadesSW){
            String endPointORVE = propiedadesSW.get(ServiciosWebConfiguration.ORVE_URL);
            
            if(StringUtils.isNotEmpty(endPointORVE)){
                wsExportacion = new WSExportacionPortTypeProxy(endPointORVE);
                
            } else {
                LOGGER.warn(MENSAJE_ERROR_PROPIEDAD_NO_DEFINIDA + ServiciosWebConfiguration.ORVE_URL + MENSAJE_EN_EL_FICHERO_DE_CONFIGURACION);
            }
        }
    }    
    
    private void getSecurityHolderConfig() {
        if(null != propiedadesSW){
        
            String usuarioORVE = propiedadesSW.get(ServiciosWebConfiguration.ORVE_USUARIO);
            String passwordORVE = propiedadesSW.get(ServiciosWebConfiguration.ORVE_PASSWORD);
            
            if(StringUtils.isNotEmpty(usuarioORVE) && StringUtils.isNotEmpty(passwordORVE)){
                Security valorSecurity = new Security(usuarioORVE, passwordORVE);            
                securityHolder = new SecurityHolder(valorSecurity);
                
            } else {
                LOGGER.warn(MENSAJE_ERROR_PROPIEDAD_NO_DEFINIDA + ServiciosWebConfiguration.ORVE_USUARIO + " y/o " + ServiciosWebConfiguration.ORVE_PASSWORD + MENSAJE_EN_EL_FICHERO_DE_CONFIGURACION);
            }
        }
    }
    
    public FiltrosIdentificadores getFiltros() {
        
        FiltrosIdentificadores filtros = new FiltrosIdentificadores();
        
        String fechaInicioFiltro = getFechaUltimaActualizacion();
        String fechaFinFiltro = setFechaActualizacion();
        
        filtros.setOficina("");
        filtros.setUnidad("");
        filtros.setNumeroRegistro("");
        
        filtros.setEstado(ConstantesWSExportacion.FiltroEstado.RECIBIDOS_CONFIRMADOS);
        
        filtros.setFechaInicio(fechaInicioFiltro);
        filtros.setFechaFin(fechaFinFiltro);
        
        filtros.setHistorico(ConstantesWSExportacion.FiltroHistorico.DESACTIVADO);
        
        return filtros;
    }
    
    public String getRegistroORVEYConsolidaEnSigem(int identificadorORVE) throws RemoteException{
        String nregSigem = "";
        
        ObtenerRegistroRespuestaWS resultadoSWRegistroORVE = this.getRegistroORVE(identificadorORVE);                
        
        if(null != resultadoSWRegistroORVE){
            FicheroIntercambioSICRES3 registroFormatoSICRES3 = SICRES30Utils.getRegistroFormatoSICRES3(resultadoSWRegistroORVE.getRegistro());
            
            nregSigem = SICRES30Utils.insertaRegistroEnSIGEM(this, registroFormatoSICRES3);
        }
        
        return nregSigem;
    }
    
    
    public int[] getIdentificadoresDeORVE() {
        int[] arrayIdentificadores = {};
        
        try{
            if(null != wsExportacion && null != securityHolder){
        
                FiltrosIdentificadores filtros = getFiltros();
                
                if(null != filtros){
                    ObtenerIdentificaoresRespuestaWS respuestaSWObtenerIdentificadores = wsExportacion.obtenerIdentificadores(securityHolder, filtros);
                
                    if(ObtenerIdentificadoresMensajesSW.CODIGO_EXITO.equals(respuestaSWObtenerIdentificadores.getCodigo())){
                        arrayIdentificadores = respuestaSWObtenerIdentificadores.getIdentificadores();
                        
                    } else {
                        LOGGER.warn("No hay registros en ORVE que consolidar.");
                    }
                }
            }
            
        } catch (RemoteException e) {            
            LOGGER.error(MENSAJE_ERROR_RECUPERAR_IDENTIFICADORES + entidad.getIdentificador() + " - " + entidad.getNombre() + ". " + e.getMessage(), e);
            this.deshacerFechaUltimaActualizacion = true;
            this.deshacerFechaUltimaActualizacion();
        }
        
        return arrayIdentificadores;
    }
    
    public ObtenerRegistroRespuestaWS getRegistroORVE(int identificador) throws RemoteException {
        ObtenerRegistroRespuestaWS resultadoSWObtenerREgistroORVE = null;
        
        try{
                    
            if(null != wsExportacion && null != securityHolder){
                ObtenerRegistroRespuestaWS respuestaSWObtenerRegistroORVE = wsExportacion.obtenerRegistro(securityHolder, identificador);
            
                if(ObtenerRegistroMensajesSW.CODIGO_EXITO.equals(respuestaSWObtenerRegistroORVE.getCodigo())){
                    resultadoSWObtenerREgistroORVE = respuestaSWObtenerRegistroORVE;
                    
                } else {
                    LOGGER.error(MENSAJE_ERROR_RECUPERAR_REGISTRO + identificador + ", el SW ha devuelto:");
                    LOGGER.error("Código de la respuesta: " + respuestaSWObtenerRegistroORVE.getCodigo());
                    LOGGER.error("Descripción de la respuesta: " + respuestaSWObtenerRegistroORVE.getDescripcion());
                    LOGGER.error("Significado de la respuesta según la documentación: " + ConstantesWSExportacion.RESULTADO_GET_REGISTRO_CODIGO_VALORES.get(respuestaSWObtenerRegistroORVE.getCodigo()));
                }
            } 
            
        } catch (RemoteException e) {
             LOGGER.error(MENSAJE_ERROR_RECUPERAR_REGISTRO + identificador + MENSAJE_PARA_LA_ENTIDAD + entidad.getIdentificador() + " - " + entidad.getNombreLargo() + ". " + e.getMessage(), e);
        }
        
        return resultadoSWObtenerREgistroORVE;
    }
}