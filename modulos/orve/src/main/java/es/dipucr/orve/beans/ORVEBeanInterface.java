package es.dipucr.orve.beans;

import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.registro.UserInfo;

import java.rmi.RemoteException;
import java.util.Date;

import org.apache.log4j.Logger;

public interface ORVEBeanInterface {
    
    Logger LOGGER = Logger.getLogger(ORVEBeanInterface.class);

    Entidad getEntidad();

    void setEntidad(Entidad entidad);

    boolean isEsDemoOrve();

    void setEsDemoOrve(boolean esDemoOrve);
    
    boolean isTieneConfiguracion();
    
    boolean isDeshacerFechaUltimaActualizacion();

    void setDeshacerFechaUltimaActualizacion( boolean deshacerFechaUltimaActualizacion);

    Date getFechaActualizacionAnterior();

    void setFechaActualizacionAnterior(Date fechaActualizacionAnterior);

    boolean isEsCorreoEnviado();

    void setEsCorreoEnviado(boolean esCorreoEnviado);

    UserInfo getUserInfo();

    void setUserInfo(UserInfo userInfo);
    
    String getSessionID();

    void setSessionID(String sessionID);

    Integer getBookId();

    void setBookId(Integer bookId);
    
    String getTipoTransporte();

    void setTipoTransporte(String tipoTransporte);
    
    String getCodOficina();

    void setCodOficina(String codOficina);
    
    int getIdOficinaOrve();

    void setIdOficinaOrve(int idOficinaOrve);

    String getTipoAsunto();

    void setTipoAsunto(String tipoAsunto);
    
    int[] getIdentificadoresDeORVE();
    
    boolean esRegistroYaConsolidado(int identificadorORVE);

    void anotaRegistroEnHistoricoORVE(int identificador, String nregSigem);
    
    void enviaCorreoRollbackFechaUltimaActualizacion();
    
    void enviaCorreoRollbackFechaUltimaActualizacion(String mensajeError);
    
    void deshacerFechaUltimaActualizacion();

    String getRegistroORVEYConsolidaEnSigem(int identificadorORVE) throws RemoteException;

}
