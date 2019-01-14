package es.dipucr.metadatos.bussinessobject;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.entidades.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.entity.dao.DBEntityDAOFactory;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.invesicres.ScrDocumentMetadatos;
import com.ieci.tecdoc.common.keys.HibernateKeys;
import com.ieci.tecdoc.common.utils.ISicresQueries;
import com.ieci.tecdoc.common.utils.ISicresSaveQueries;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.dipucr.metadatos.beans.MetadatosDocumentoBean;
import es.dipucr.metadatos.diccionarios.EstadosElaboracion;
import es.dipucr.metadatos.diccionarios.NombresMetadatos;
import es.dipucr.metadatos.diccionarios.OrigenCiudadanoAdministracion;
import es.dipucr.metadatos.diccionarios.TiposDocumentales;
import es.dipucr.metadatos.diccionarios.TiposFirmas;
import es.dipucr.metadatos.mensajes.MetadatosMensajes;
import es.dipucr.sigem.api.rule.common.utils.FileUtils;

public class MetadatosBo {

    private static final Logger LOGGER = Logger.getLogger(MetadatosBo.class);

    public static final String SALTO_LINEA = "\n";
    public static final String TABULADOR = "\t";    
    
    public String getMetadatosXML(MetadatosDocumentoBean metadatosBean) {
        
        StringBuilder metadatos;
        
        if(StringUtils.isNotEmpty(metadatosBean.getIdEspecificoDocumento())){
        
            metadatos = new StringBuilder("<metadatos>" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.VERSION_NTI + ">" + metadatosBean.getVersionNTI() + "</" + NombresMetadatos.VERSION_NTI + ">" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.IDENTIFICADOR + ">" + metadatosBean.getIdentificador() + "</" + NombresMetadatos.IDENTIFICADOR + ">" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.ORGANO + ">" + metadatosBean.getOrgano() + "</" + NombresMetadatos.ORGANO + ">" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.FECHA_CAPTURA + ">" + metadatosBean.getsFechaCaptura() + "</" + NombresMetadatos.FECHA_CAPTURA + ">" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.ORIGEN + ">" + metadatosBean.getOrigen() + "</" + NombresMetadatos.ORIGEN + ">" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.ESTADO_ELABORACION + ">" + metadatosBean.getEstadoElaboracion() + "</" + NombresMetadatos.ESTADO_ELABORACION + ">" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.NOMBRE_FORMATO + ">" + metadatosBean.getNombreFormato() + "</" + NombresMetadatos.NOMBRE_FORMATO + ">" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.TIPO_DOCUMENTAL + ">" + metadatosBean.getTipoDocumental() + "</" + NombresMetadatos.TIPO_DOCUMENTAL + ">" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.TIPO_FIRMA + ">" + metadatosBean.getTipoFirma() + "</" + NombresMetadatos.TIPO_FIRMA + ">" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.CSV + ">" + metadatosBean.getCsv() + "</" + NombresMetadatos.CSV + ">" + SALTO_LINEA);
            metadatos.append(TABULADOR + "<" + NombresMetadatos.IDENTIFICADOR_DOC_ORIGEN + ">" + metadatosBean.getIdentificadorDocOrigen() + "</" + NombresMetadatos.IDENTIFICADOR_DOC_ORIGEN + ">" + SALTO_LINEA);
            metadatos.append("</metados>");
        } else {
            metadatos = new StringBuilder("NO SE HA PODIDO RECUPERAR EL ID ESPECÍFICO DEL DOCUMENTO");
        }
        
        return metadatos.toString();
    }
    
    public static void setMetadatosDefecto(MetadatosDocumentoBean metadatosDocumentoBean, Date fechaCaptura, String nombreDoc ) {
        if(null != metadatosDocumentoBean){
            
            metadatosDocumentoBean.setVersionNTI(MetadatosDocumentoBean.VERSION_NTI_VALOR);
            
            if(StringUtils.isEmpty(metadatosDocumentoBean.getOrgano())){
                String organoDIR3 = getOrganoDir3(metadatosDocumentoBean.getEntidadId());                
                metadatosDocumentoBean.setOrgano(organoDIR3);
            }

            if(null != fechaCaptura){
                metadatosDocumentoBean.setFechaCaptura(fechaCaptura);
            }
            
            if(StringUtils.isEmpty(metadatosDocumentoBean.getOrigen())){
                metadatosDocumentoBean.setOrigen(OrigenCiudadanoAdministracion.CIUDADANO);
            }
            
            if(StringUtils.isEmpty(metadatosDocumentoBean.getEstadoElaboracion())){
                metadatosDocumentoBean.setEstadoElaboracion(EstadosElaboracion.OTROS);
            }
            
            if(StringUtils.isEmpty(metadatosDocumentoBean.getNombreFormato()) && StringUtils.isNotEmpty(nombreDoc)){
                metadatosDocumentoBean.setNombreFormato(FileUtils.getExtensionByNombreDoc(nombreDoc));
            }
            
            if(StringUtils.isEmpty(metadatosDocumentoBean.getTipoDocumental())){
                metadatosDocumentoBean.setTipoDocumental(TiposDocumentales.SOLICITUD);
            }
        }
    }    

    public static void getMetadatos(MetadatosDocumentoBean metadatosDocumentoBean){
        
        if(null != metadatosDocumentoBean){
        
            metadatosDocumentoBean.setVersionNTI(getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.VERSION_NTI));
            metadatosDocumentoBean.setIdEspecificoDocumento(String.valueOf(metadatosDocumentoBean.getFileId()));
            metadatosDocumentoBean.setIdentificador(getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.IDENTIFICADOR));
            metadatosDocumentoBean.setOrgano(getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.ORGANO));
            
            SimpleDateFormat sdf = new SimpleDateFormat(MetadatosDocumentoBean.FORMATO_FECHA);
            String sFechaCaptura = getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.FECHA_CAPTURA);
            
            try {
                if(StringUtils.isNotEmpty(sFechaCaptura)){
                    metadatosDocumentoBean.setFechaCaptura(sdf.parse(sFechaCaptura));
                }
            } catch (ParseException e) {
                LOGGER.error("ERROR al recuperar la fecha de captura de los metadatos. " + e.getMessage(), e);
            }
            
            metadatosDocumentoBean.setOrigen(getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.ORIGEN));
            metadatosDocumentoBean.setEstadoElaboracion(getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.ESTADO_ELABORACION));
            metadatosDocumentoBean.setNombreFormato(getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.NOMBRE_FORMATO));
            metadatosDocumentoBean.setIdentificadorDocOrigen(getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.IDENTIFICADOR_DOC_ORIGEN));
            metadatosDocumentoBean.setTipoDocumental(getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.TIPO_DOCUMENTAL));
            metadatosDocumentoBean.setTipoFirma(getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.TIPO_FIRMA));
            metadatosDocumentoBean.setCsv(getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.CSV));
        }
    }
    
    public static ScrDocumentMetadatos getMetadatoByNombre(MetadatosDocumentoBean metadatosDocumentoBean, String nombreMetadato){
        ScrDocumentMetadatos metadato = null;
        
        Transaction tran = null;
        try{
            Session session = HibernateUtil.currentSession(metadatosDocumentoBean.getEntidadId());            
            tran = session.beginTransaction();
            
            List<?> scrDocumentMetadatosList = ISicresQueries.getScrDocumentMetadatosByNombre(session, metadatosDocumentoBean.getBookId(), metadatosDocumentoBean.getFolderId(), metadatosDocumentoBean.getPageId(), metadatosDocumentoBean.getFileId(), nombreMetadato);
            
            if (scrDocumentMetadatosList != null && !scrDocumentMetadatosList.isEmpty()) {
                metadato = ((ScrDocumentMetadatos)scrDocumentMetadatosList.get(0));
            }
            
            if(session.isOpen()){
                HibernateUtil.commitTransaction(tran);
            }
            
        } catch (HibernateException e){
            LOGGER.error(MetadatosMensajes.ERROR_GUARDAR_METADATOS + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } catch (Exception e) {
            LOGGER.error(MetadatosMensajes.ERROR_GUARDAR_METADATOS + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } 
        
        return metadato;
    }
    
    public static String getValorMetadatoByNombre(MetadatosDocumentoBean metadatosDocumentoBean, String nombreMetadato){
        String valorMetadato = "";

        ScrDocumentMetadatos metadatos = getMetadatoByNombre(metadatosDocumentoBean, nombreMetadato);
        
        if(null != metadatos){
            valorMetadato = metadatos.getValorMetadato();
        }
        
        return valorMetadato;
    }

    public static void insertaMetadatos(String sessionId, MetadatosDocumentoBean metadatosDocumentoBean) {
        
        try{
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.VERSION_NTI, metadatosDocumentoBean.getVersionNTI());
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.IDENTIFICADOR, metadatosDocumentoBean.getIdentificador());
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.ORGANO, metadatosDocumentoBean.getOrgano());
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.FECHA_CAPTURA, metadatosDocumentoBean.getsFechaCaptura());
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.ORIGEN, metadatosDocumentoBean.getOrigen());
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.ESTADO_ELABORACION, metadatosDocumentoBean.getEstadoElaboracion());
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.NOMBRE_FORMATO, metadatosDocumentoBean.getNombreFormato());
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.IDENTIFICADOR_DOC_ORIGEN, metadatosDocumentoBean.getIdentificadorDocOrigen());
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.TIPO_DOCUMENTAL, metadatosDocumentoBean.getTipoDocumental());
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.TIPO_FIRMA, metadatosDocumentoBean.getTipoFirma());
            insertaMetadato(sessionId, metadatosDocumentoBean, NombresMetadatos.CSV, metadatosDocumentoBean.getCsv());
            
        } catch (Exception e) {
            LOGGER.error(MetadatosMensajes.ERROR_GUARDAR_METADATOS + e.getMessage(), e);
        } 
    }
    
    private static void insertaMetadato(String sessionId, MetadatosDocumentoBean metadatosDocumentoBean, String nombreMetadato, String valorMetadato) {
        
        Transaction tran = null;
        try{
            if(null != metadatosDocumentoBean && StringUtils.isNotEmpty(sessionId) && StringUtils.isNotEmpty(nombreMetadato)){
                
                Session session = HibernateUtil.currentSession(metadatosDocumentoBean.getEntidadId());            
                tran = session.beginTransaction();
                
                if(!tieneMetadato(metadatosDocumentoBean, nombreMetadato)){
                    CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(sessionId);
                    AuthenticationUser user = (AuthenticationUser) cacheBag.get(HibernateKeys.HIBERNATE_Iuseruserhdr);
                    
                    Integer updateAuditId = Integer.valueOf(DBEntityDAOFactory.getCurrentDBEntityDAO().getNextIdForScrDocumentMetadatos(user.getId(), metadatosDocumentoBean.getEntidadId()));                
                    ISicresSaveQueries.saveScrDocumentMetadato(session, updateAuditId, metadatosDocumentoBean.getBookId(), metadatosDocumentoBean.getFolderId(), metadatosDocumentoBean.getPageId(), metadatosDocumentoBean.getFileId(), nombreMetadato, valorMetadato);
                    
                } else {
                    updateMetadato(metadatosDocumentoBean, valorMetadato, nombreMetadato);
                }
                
                if(session.isOpen()){
                    HibernateUtil.commitTransaction(tran);
                }
            }
        } catch (HibernateException e){
            LOGGER.error(MetadatosMensajes.getMensajeErrorGuardarMetadato(nombreMetadato, valorMetadato) + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } catch (SessionException e) {
            LOGGER.error(MetadatosMensajes.getMensajeErrorGuardarMetadato(nombreMetadato, valorMetadato) + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } catch (TecDocException e) {
            LOGGER.error(MetadatosMensajes.getMensajeErrorGuardarMetadato(nombreMetadato, valorMetadato) + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } catch (SQLException e) {
            LOGGER.error(MetadatosMensajes.getMensajeErrorGuardarMetadato(nombreMetadato, valorMetadato) + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } catch (Exception e) {
            LOGGER.error(MetadatosMensajes.getMensajeErrorGuardarMetadato(nombreMetadato, valorMetadato) + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } 
    }
    
    public static void updateMetadatos(MetadatosDocumentoBean metadatosDocumentoViejos, MetadatosDocumentoBean metadatosDocumentoNuevos) {        
        try{
            if(null != metadatosDocumentoNuevos){
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getVersionNTI(), NombresMetadatos.VERSION_NTI);
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getIdentificador(), NombresMetadatos.IDENTIFICADOR);
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getOrgano(), NombresMetadatos.ORGANO);
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getsFechaCaptura(), NombresMetadatos.FECHA_CAPTURA);
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getOrigen(), NombresMetadatos.ORIGEN);
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getEstadoElaboracion(), NombresMetadatos.ESTADO_ELABORACION);
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getNombreFormato(), NombresMetadatos.NOMBRE_FORMATO);
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getIdentificadorDocOrigen(), NombresMetadatos.IDENTIFICADOR_DOC_ORIGEN);
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getTipoDocumental(), NombresMetadatos.TIPO_DOCUMENTAL);
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getTipoFirma(), NombresMetadatos.TIPO_FIRMA);
                updateMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos.getCsv(), NombresMetadatos.CSV);
            }
        } catch (Exception e) {
            LOGGER.error(MetadatosMensajes.ERROR_GUARDAR_METADATOS + e.getMessage(), e);
        } 
    }
    
    public static void updateMetadato(MetadatosDocumentoBean metadatosDocumento, String valorNuevo, String nombreMetadato) {
        
        Transaction tran = null;
        try{
            if(null != metadatosDocumento && StringUtils.isNotEmpty(nombreMetadato)){
                Session session = HibernateUtil.currentSession(metadatosDocumento.getEntidadId());            
                tran = session.beginTransaction();
                
                ScrDocumentMetadatos metadato = getMetadatoByNombre(metadatosDocumento, nombreMetadato);
            
                if (null != metadato){
                    metadato.setValorMetadato(valorNuevo);                
                    session.update(metadato);
                }
                    
                if(session.isOpen()){
                    HibernateUtil.commitTransaction(tran);
                }
            }
        } catch (HibernateException e){
            LOGGER.error(MetadatosMensajes.getMensajeErrorActualizarMetadato(nombreMetadato, valorNuevo) + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } catch (Exception e) {
            LOGGER.error(MetadatosMensajes.getMensajeErrorActualizarMetadato(nombreMetadato, valorNuevo) + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } 
    }
    
    public static void upateDocMetadatos(MetadatosDocumentoBean metadatosDocumentoViejos, MetadatosDocumentoBean metadatosDocumentoNuevos) {        
        try{
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.VERSION_NTI);
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.IDENTIFICADOR);
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.ORGANO);
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.FECHA_CAPTURA);
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.ORIGEN);
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.ESTADO_ELABORACION);
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.NOMBRE_FORMATO);
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.IDENTIFICADOR_DOC_ORIGEN);
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.TIPO_DOCUMENTAL);
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.TIPO_FIRMA);
            updateDocMetadato(metadatosDocumentoViejos, metadatosDocumentoNuevos, NombresMetadatos.CSV);
            
        } catch (Exception e) {
            LOGGER.error(MetadatosMensajes.ERROR_ACTUALIZAR_DOCUMENTO + e.getMessage(), e);
        } 
    }
    
    private static void updateDocMetadato(MetadatosDocumentoBean metadatosDocumentoViejos, MetadatosDocumentoBean metadatosDocumentoNuevos, String nombreMetadato) {
        
        Transaction tran = null;
        try{
            if(null != metadatosDocumentoViejos && null != metadatosDocumentoNuevos && StringUtils.isNotEmpty(nombreMetadato)){
                Session session = HibernateUtil.currentSession(metadatosDocumentoViejos.getEntidadId());            
                tran = session.beginTransaction();
                
                ScrDocumentMetadatos metadato = getMetadatoByNombre(metadatosDocumentoViejos, nombreMetadato);
            
                if (null != metadato){
                    metadato.setIdBook(metadatosDocumentoNuevos.getBookId());                
                    metadato.setIdFld(metadatosDocumentoNuevos.getFolderId());
                    metadato.setIdPage(metadatosDocumentoNuevos.getPageId());
                    metadato.setIdFile(metadatosDocumentoNuevos.getFileId());
                    
                    session.update(metadato);
                }
                    
                if(session.isOpen()){
                    HibernateUtil.commitTransaction(tran);
                }
            }
        } catch (HibernateException e){
            LOGGER.error(MetadatosMensajes.ERROR_ACTUALIZAR_DOCUMENTO + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } catch (Exception e) {
            LOGGER.error(MetadatosMensajes.ERROR_ACTUALIZAR_DOCUMENTO + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } 
    }

    public static void borrarMetadatos(MetadatosDocumentoBean metadatosDocumento) {
        if(null != metadatosDocumento){
            
            borrarMetadato(metadatosDocumento, NombresMetadatos.VERSION_NTI);
            borrarMetadato(metadatosDocumento, NombresMetadatos.IDENTIFICADOR);
            borrarMetadato(metadatosDocumento, NombresMetadatos.ORGANO);
            borrarMetadato(metadatosDocumento, NombresMetadatos.FECHA_CAPTURA);
            borrarMetadato(metadatosDocumento, NombresMetadatos.ORIGEN);
            borrarMetadato(metadatosDocumento, NombresMetadatos.ESTADO_ELABORACION);
            borrarMetadato(metadatosDocumento, NombresMetadatos.NOMBRE_FORMATO);
            borrarMetadato(metadatosDocumento, NombresMetadatos.IDENTIFICADOR_DOC_ORIGEN);
            borrarMetadato(metadatosDocumento, NombresMetadatos.TIPO_DOCUMENTAL);
            borrarMetadato(metadatosDocumento, NombresMetadatos.TIPO_FIRMA);
            borrarMetadato(metadatosDocumento, NombresMetadatos.CSV);
        }        
    }
    
    private static void borrarMetadato(MetadatosDocumentoBean metadatosDocumento, String nombreMetadato) {
        Transaction tran = null;
        try{
            if(null != metadatosDocumento){
                Session session = HibernateUtil.currentSession(metadatosDocumento.getEntidadId());            
                tran = session.beginTransaction();
                
                ScrDocumentMetadatos metadato = getMetadatoByNombre(metadatosDocumento, nombreMetadato);
            
                if (null != metadato){                
                    session.delete(metadato);
                }
                    
                if(session.isOpen()){
                    HibernateUtil.commitTransaction(tran);
                }
            }
        } catch (HibernateException e){
            LOGGER.error(MetadatosMensajes.getMensajeErrorBorrarMetadato(nombreMetadato, metadatosDocumento.getIdentificador()) + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        } catch (Exception e) {
            LOGGER.error(MetadatosMensajes.getMensajeErrorBorrarMetadato(nombreMetadato, metadatosDocumento.getIdentificador()) + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
        }         
    }
    
    public static void insertaMetadatosAnexar(String sessionID, int bookId, int folderId, int pageId, int fileId, String entidadId, Date fechaCaptura, String extension) {

        MetadatosDocumentoBean metadatosDocumentoBean = new MetadatosDocumentoBean(bookId, folderId, pageId, fileId, entidadId, "");
        
        metadatosDocumentoBean.setVersionNTI(MetadatosDocumentoBean.VERSION_NTI_VALOR);
        metadatosDocumentoBean.setIdEspecificoDocumento("" + fileId);
        
        if(null != fechaCaptura){
            metadatosDocumentoBean.setFechaCaptura(fechaCaptura);
        } else {
            metadatosDocumentoBean.setFechaCaptura(new Date());
        }
        
        metadatosDocumentoBean.setOrigen(OrigenCiudadanoAdministracion.CIUDADANO);
        metadatosDocumentoBean.setEstadoElaboracion(EstadosElaboracion.OTROS);
        metadatosDocumentoBean.setNombreFormato(extension);
        metadatosDocumentoBean.setTipoDocumental(TiposDocumentales.SOLICITUD);
    
        MetadatosBo.insertaMetadatos(sessionID, metadatosDocumentoBean);
    }

    public static void insertaMetadatosEscaneo(String sessionID, int bookId, int folderId, int pageId, int fileId, String entidadId, Date fechaCaptura, String extension) {
        MetadatosDocumentoBean metadatosDocumentoBean = new MetadatosDocumentoBean(bookId, folderId, pageId, fileId, entidadId, "");
        
        metadatosDocumentoBean.setVersionNTI(MetadatosDocumentoBean.VERSION_NTI_VALOR);
        metadatosDocumentoBean.setIdEspecificoDocumento("" + fileId);
        metadatosDocumentoBean.setFechaCaptura(fechaCaptura);
        metadatosDocumentoBean.setOrigen(OrigenCiudadanoAdministracion.CIUDADANO);
        metadatosDocumentoBean.setEstadoElaboracion(EstadosElaboracion.OTROS);
        metadatosDocumentoBean.setNombreFormato(extension);
        metadatosDocumentoBean.setTipoDocumental(TiposDocumentales.SOLICITUD);

        MetadatosBo.insertaMetadatos(sessionID, metadatosDocumentoBean);
        
    }

    public static void actualizaMetadatosCompulsa( MetadatosDocumentoBean metadatosDocumentoOriginal, MetadatosDocumentoBean metadatosDocumentoNuevo, String fileIdDocOrigen, String extension, String csv) {
        
        metadatosDocumentoNuevo.setVersionNTI(metadatosDocumentoOriginal.getVersionNTI());
        metadatosDocumentoNuevo.setIdEspecificoDocumento("" + metadatosDocumentoNuevo.getFileId());
        metadatosDocumentoNuevo.setOrgano(metadatosDocumentoOriginal.getOrgano());
        metadatosDocumentoNuevo.setFechaCaptura(metadatosDocumentoOriginal.getFechaCaptura());
        metadatosDocumentoNuevo.setOrigen(metadatosDocumentoOriginal.getOrigen());
        metadatosDocumentoNuevo.setEstadoElaboracion(EstadosElaboracion.COPIA_AUTENTICA_DOC_PAPEL);
        metadatosDocumentoNuevo.setIdentificadorDocOrigen(fileIdDocOrigen);
        metadatosDocumentoNuevo.setNombreFormato(extension);
        metadatosDocumentoNuevo.setTipoDocumental(metadatosDocumentoOriginal.getTipoDocumental());
        metadatosDocumentoNuevo.setTipoFirma(TiposFirmas.TIPO_FIRMA_PADES);
        metadatosDocumentoNuevo.setCsv(csv);
        
        MetadatosBo.updateMetadatos(metadatosDocumentoOriginal, metadatosDocumentoNuevo);
        MetadatosBo.upateDocMetadatos(metadatosDocumentoOriginal, metadatosDocumentoNuevo);
    }

    public static void insertaMetadatosJustificante(String sessionID, int bookId, int folderId, int pageId, int fileId, String entidadId, Date fechaCaptura, String extension, String csv) {
        MetadatosDocumentoBean metadatosJustificante = new MetadatosDocumentoBean(bookId, folderId, pageId, fileId, entidadId, "");
        
        metadatosJustificante.setVersionNTI(MetadatosDocumentoBean.VERSION_NTI_VALOR);
        metadatosJustificante.setIdEspecificoDocumento("" + fileId);
        metadatosJustificante.setFechaCaptura(fechaCaptura);
        metadatosJustificante.setOrigen(OrigenCiudadanoAdministracion.ADMINISTRACION);
        metadatosJustificante.setEstadoElaboracion(EstadosElaboracion.ORIGINAL);
        metadatosJustificante.setNombreFormato(extension);
        metadatosJustificante.setTipoDocumental(TiposDocumentales.COMUNICACION);
        metadatosJustificante.setTipoFirma(TiposFirmas.TIPO_FIRMA_PADES);
        metadatosJustificante.setCsv(csv);
        
        MetadatosBo.insertaMetadatos(sessionID, metadatosJustificante);
    }
    
    public static void insertaMetadatosConsolidacion(String sessionID, int bookId, int folderId, int pageId, int fileId, String entidadId, Date fechaCaptura, String extension) {
        MetadatosDocumentoBean metadatosDocumentoBean = new MetadatosDocumentoBean(bookId, folderId, pageId, fileId, entidadId, "");
        
        metadatosDocumentoBean.setVersionNTI(MetadatosDocumentoBean.VERSION_NTI_VALOR);
        metadatosDocumentoBean.setIdEspecificoDocumento("" + fileId);
        metadatosDocumentoBean.setFechaCaptura(fechaCaptura);
        metadatosDocumentoBean.setOrigen(OrigenCiudadanoAdministracion.CIUDADANO);
        metadatosDocumentoBean.setEstadoElaboracion(EstadosElaboracion.OTROS);
        metadatosDocumentoBean.setNombreFormato(extension);
        metadatosDocumentoBean.setTipoDocumental(TiposDocumentales.SOLICITUD);

        MetadatosBo.insertaMetadatos(sessionID, metadatosDocumentoBean);
    }

    public static void insertaMetadatosCrearRegistroSW(String sessionID, int bookId, int folderId, int pageId, int fileId, String entidadId, Date fechaCaptura, String extension, String tipoDocumental, String tipoFirma, String csv) {
        MetadatosDocumentoBean metadatosDocumentoBean = new MetadatosDocumentoBean(bookId, folderId, pageId, fileId, entidadId, "");
        
        metadatosDocumentoBean.setVersionNTI(MetadatosDocumentoBean.VERSION_NTI_VALOR);
        
        metadatosDocumentoBean.setIdEspecificoDocumento("" + fileId);
        metadatosDocumentoBean.setFechaCaptura(fechaCaptura);
        
        metadatosDocumentoBean.setOrigen(OrigenCiudadanoAdministracion.ADMINISTRACION);
        metadatosDocumentoBean.setEstadoElaboracion(EstadosElaboracion.ORIGINAL);
        
        metadatosDocumentoBean.setNombreFormato(extension);        
        metadatosDocumentoBean.setTipoDocumental(tipoDocumental);
        metadatosDocumentoBean.setTipoFirma(tipoFirma);
        metadatosDocumentoBean.setCsv(csv);

        MetadatosBo.insertaMetadatos(sessionID, metadatosDocumentoBean);
    }
    
    public static boolean tieneMetadatos(MetadatosDocumentoBean metadatosDocumentoBean){
        boolean tieneMetadatos = false;
        
        boolean tieneVersionNTI = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.VERSION_NTI);
        boolean tieneIdentificador = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.IDENTIFICADOR);
        boolean tieneOrgano = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.ORGANO);
        boolean tieneFechaCaptura = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.FECHA_CAPTURA);
        
        tieneMetadatos = tieneVersionNTI && tieneIdentificador && tieneOrgano && tieneFechaCaptura;
        
        if(tieneMetadatos){
            boolean tieneOrigen = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.ORIGEN);
            boolean tieneEstadoElaboracion = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.ESTADO_ELABORACION);
            boolean tieneNombreFormato = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.NOMBRE_FORMATO);
            boolean tieneIdentificadorDocOrigen = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.IDENTIFICADOR_DOC_ORIGEN);
            
            tieneMetadatos = tieneOrigen && tieneEstadoElaboracion && tieneNombreFormato && tieneIdentificadorDocOrigen;
        }
        
        if(tieneMetadatos){
            boolean tieneTipoDocumental = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.TIPO_DOCUMENTAL);
            boolean tieneTipoFirma = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.TIPO_FIRMA);
            boolean tieneCSV = tieneMetadato(metadatosDocumentoBean, NombresMetadatos.CSV);
            
            tieneMetadatos = tieneTipoDocumental && tieneTipoFirma && tieneCSV;
        }
        
        return tieneMetadatos;
    }
    
    
    public static boolean tieneMetadato(MetadatosDocumentoBean metadatosDocumentoBean, String nombreMetadato){
        boolean tieneMetadato = false;
        try{
            if(null != metadatosDocumentoBean && StringUtils.isNotEmpty(nombreMetadato)){
                
                ScrDocumentMetadatos metadato = getMetadatoByNombre(metadatosDocumentoBean, nombreMetadato);
            
                if (null != metadato){
                    tieneMetadato = true;
                }
            }
        } catch (Exception e) {
            LOGGER.error(MetadatosMensajes.ERROR_RECUPERAR_METADATOS + e.getMessage(), e);
        }
        return tieneMetadato;
    }
    
    public static String getOrganoDir3(String entidadId) {
        String organoDIR3 = "";
        try {
            ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
            Entidad entidad = servicioEntidades.obtenerEntidad(entidadId);
            if(null != entidad){
                organoDIR3 = entidad.getDir3();
            }
            
        } catch (SigemException e) {
            LOGGER.error(MetadatosMensajes.getMensajeErrorRecuperarDIR3(entidadId) + e.getMessage(), e);
        }
        
        return organoDIR3;
    }
}
