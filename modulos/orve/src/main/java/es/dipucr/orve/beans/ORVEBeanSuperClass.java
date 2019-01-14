package es.dipucr.orve.beans;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;

import com.ieci.tecdoc.common.exception.SecurityException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrTt;
import com.ieci.tecdoc.common.utils.EntityByLanguage;
import com.ieci.tecdoc.utils.HibernateUtil;

import es.dipucr.orve.dao.OrveFechaUltimaActualizacionDAO;
import es.dipucr.orve.dao.OrveHistoRegistroDAO;
import es.dipucr.orve.sw.constantes.ConstantesWSExportacion;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.messages.MessagesFormatter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.registro.UserInfo;
import ieci.tecdoc.sgm.registropresencial.autenticacion.Login;
import ieci.tecdoc.sgm.registropresencial.autenticacion.User;
import ieci.tecdoc.sgm.registropresencial.utils.SigemRegistroServiceAdapterUtil;

public abstract class ORVEBeanSuperClass implements ORVEBeanInterface{

	public static final String MENSAJE_ERROR_NO_EXISTE_PROPIEDADES = "No existe el fichero de configuración ";
    public static final String MENSAJE_ERROR_SESION_ID = "ERROR al crear el sessionID. ";
    public static final String MENSAJE_ERROR_PROPIEDAD_NO_DEFINIDA = "No se ha definido la/s propiedad/es ";
    public static final String MENSAJE_EN_EL_FICHERO_DE_CONFIGURACION = " en el fichero de configuración. ";
    
    public static final String MENSAJE_ERROR_RECUPERAR_IDENTIFICADORES = "ERROR al recuperar los identificadores del SW de ORVE para la entidad ";
    public static final String MENSAJE_ERROR_RECUPERAR_REGISTRO = "ERROR al recuperar el registro del SW de ORVE con identificador: ";
    public static final String MENSAJE_PARA_LA_ENTIDAD = " para la entidad: ";
   
    protected static final String COLUMNA_TRANSPORT = "transport";

    protected static final String SALTO_LINEA_HTML = "<br/>";
    
    protected Entidad entidad = null;
    protected ServiciosWebConfiguration propiedadesSW = null;
    
    protected boolean esDemoOrve = false;
    
    protected boolean deshacerFechaUltimaActualizacion = false;
    protected Date fechaActualizacionAnterior = null;
    protected boolean esCorreoEnviado = false;
    
    protected UserInfo userInfo = null;
    protected String sessionID = null;
    protected Integer bookId = null;
    
    protected String tipoTransporte;
    protected String codOficina;
    protected int idOficinaOrve;
    protected String tipoAsunto;
    
    
    public ORVEBeanSuperClass(Entidad entidad){
        this.entidad = entidad;
        
        this.propiedadesSW = getPropiedadesWS(entidad);
        
        if (null != propiedadesSW){
            getEsDemoOrveConfig();
            
            getUserInfoConfig();            
            getBookIdConfig();
            
            this.deshacerFechaUltimaActualizacion = false;
            
            getTipoTransporteConfig();
            getOficinaConfig();
            getTipoAsuntoConfig();
        
        } else {
            LOGGER.error(MENSAJE_ERROR_NO_EXISTE_PROPIEDADES + ServiciosWebConfiguration.DEFAULT_CONFIG_FILENAME + MENSAJE_PARA_LA_ENTIDAD + entidad.getIdentificador() + " - " + entidad.getNombreLargo());
        }
    }
    
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public ServiciosWebConfiguration getPropiedadesSW() {
        return propiedadesSW;
    }

    public void setPropiedadesSW(ServiciosWebConfiguration propiedadesSW) {
        this.propiedadesSW = propiedadesSW;
    }
    
    public boolean isEsDemoOrve() {
        return esDemoOrve;
    }

    public void setEsDemoOrve(boolean esDemoOrve) {
        this.esDemoOrve = esDemoOrve;
    }
    
    public boolean isDeshacerFechaUltimaActualizacion() {
        return deshacerFechaUltimaActualizacion;
    }

    public void setDeshacerFechaUltimaActualizacion( boolean deshacerFechaUltimaActualizacion) {
        this.deshacerFechaUltimaActualizacion = deshacerFechaUltimaActualizacion;
    }

    public Date getFechaActualizacionAnterior() {
        return fechaActualizacionAnterior;
    }

    public void setFechaActualizacionAnterior(Date fechaActualizacionAnterior) {
        this.fechaActualizacionAnterior = fechaActualizacionAnterior;
    }

    public boolean isEsCorreoEnviado() {
        return esCorreoEnviado;
    }

    public void setEsCorreoEnviado(boolean esCorreoEnviado) {
        this.esCorreoEnviado = esCorreoEnviado;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    
    public String getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }
    
    public String getCodOficina() {
        return codOficina;
    }

    public void setCodOficina(String codOficina) {
        this.codOficina = codOficina;
    }
    
    public int getIdOficinaOrve() {
        return idOficinaOrve;
    }

    public void setIdOficinaOrve(int idOficinaOrve) {
        this.idOficinaOrve = idOficinaOrve;
    }

    public String getTipoAsunto() {
        return tipoAsunto;
    }

    public void setTipoAsunto(String tipoAsunto) {
        this.tipoAsunto = tipoAsunto;
    }

    private ServiciosWebConfiguration getPropiedadesWS(Entidad entidad) {
        try{
            if(null != entidad && StringUtils.isNotEmpty(entidad.getIdentificador())){
                propiedadesSW = ServiciosWebConfiguration.getInstance(entidad.getIdentificador());
            }
            
        } catch (ISPACRuleException e) {
            LOGGER.error("ERROR al cargar el fichero de configuración " + ServiciosWebConfiguration.DEFAULT_CONFIG_FILENAME + MENSAJE_PARA_LA_ENTIDAD + entidad.getIdentificador() + " - " + entidad.getNombreCorto() + ". " + e.getMessage(), e);
        }
            
        return propiedadesSW;
    }
    
    private void getEsDemoOrveConfig() {
        if(null != propiedadesSW){
            String endPointORVE = propiedadesSW.get(ServiciosWebConfiguration.ORVE_URL);
            
            if(StringUtils.isNotEmpty(endPointORVE)){
                esDemoOrve = endPointORVE.toUpperCase().contains("DEMOORVE");
                
            } else {
                LOGGER.warn(MENSAJE_ERROR_PROPIEDAD_NO_DEFINIDA + ServiciosWebConfiguration.ORVE_URL + MENSAJE_EN_EL_FICHERO_DE_CONFIGURACION);
            }
        }
    }
    
    private void getUserInfoConfig() {
        this.userInfo = new UserInfo();
        this.userInfo.setUserName(getUserNameRegistro());
        this.userInfo.setPassword(getPasswordRegistro());
        
        try {                
            User user = SigemRegistroServiceAdapterUtil.getWSUser(userInfo);
            
            if(null != user && StringUtils.isNotEmpty(user.getUserName()) && StringUtils.isNotEmpty(user.getPassword())){
                this.userInfo.setLocale(user.getLocale());
                this.sessionID = Login.login(user, entidad.getIdentificador());
            }
            
        } catch (SecurityException e) {
            LOGGER.error(MENSAJE_ERROR_SESION_ID + e.getMessage(), e);
            
        } catch (ValidationException e) {
            LOGGER.error(MENSAJE_ERROR_SESION_ID + e.getMessage(), e);
        }
        
    }
    
    private String getUserNameRegistro() {
        String userNameRegistro = null;
        
        if(null != propiedadesSW){
            userNameRegistro = propiedadesSW.get(ServiciosWebConfiguration.ORVE_USUARIO_REGISTRO);            
        }
            
        return userNameRegistro;
    }
    
    private String getPasswordRegistro() {
        String passwordRegistro = null;
        
        if(null != propiedadesSW){
            passwordRegistro = propiedadesSW.get(ServiciosWebConfiguration.ORVE_PASSWORD_REGISTRO);            
        }
            
        return passwordRegistro;
    }
    
    private void getBookIdConfig() {
        if(null != propiedadesSW){
            String sBookId = propiedadesSW.get(ServiciosWebConfiguration.ORVE_BOOKID_REGISTRO);
            
            if(StringUtils.isNumeric(sBookId)){
                bookId = Integer.valueOf(sBookId);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void getTipoTransporteConfig() {
        tipoTransporte = ConstantesWSExportacion.ID_TIPO_TRANSPORTE_DEFECTO;
        String codTipoTransporte = ConstantesWSExportacion.COD_TIPO_TRANSPORTE_DEFECTO;
        
        Transaction tran = null;
        try {
            if(null != propiedadesSW){
                codTipoTransporte = propiedadesSW.get(ServiciosWebConfiguration.ORVE_COD_TIPO_TRANSPORTE);
            
                if(StringUtils.isEmpty(codTipoTransporte)){
                    codTipoTransporte = ConstantesWSExportacion.COD_TIPO_TRANSPORTE_DEFECTO;
                }

                Session session = HibernateUtil.currentSession(entidad.getIdentificador());
                tran = session.beginTransaction();
                    
                Criteria criteriaResults = session.createCriteria(EntityByLanguage.getScrTtLanguage((new Locale ("es","ES","")).getLanguage()));

                // Recuperamos los resultados                
                criteriaResults.add(Expression.eq(COLUMNA_TRANSPORT, codTipoTransporte));
                criteriaResults.addOrder(Order.asc(COLUMNA_TRANSPORT));

                List<ScrTt> listaTransportes = (List<ScrTt>)criteriaResults.list();
                HibernateUtil.commitTransaction(tran);
                
                if(null != listaTransportes && !listaTransportes.isEmpty()){
                    ScrTt scrTipoTransporte = listaTransportes.get(0);

                    int iIdTipoTransporte = scrTipoTransporte.getId();
                    
                    DecimalFormat df = new DecimalFormat("00");
                    tipoTransporte = df.format(iIdTipoTransporte);
                }
            }
        } catch (HibernateException e) {
            LOGGER.error("ERROR al recuperar el tipo de transporte con código : " + codTipoTransporte  + MENSAJE_PARA_LA_ENTIDAD + entidad.getIdentificador() + ". " + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
            
        } finally {
            HibernateUtil.closeSession(entidad.getIdentificador());
        }
        
        if(StringUtils.isEmpty(tipoTransporte)){
            tipoTransporte = ConstantesWSExportacion.ID_TIPO_TRANSPORTE_DEFECTO;
        }
    }
    
    @SuppressWarnings("unchecked")
    private void getOficinaConfig() {
        String sAcronOficina = ConstantesWSExportacion.ACRON_OFICINA_DEFECTO;
        
        Transaction tran = null;
        try {
            if(null != propiedadesSW){
                sAcronOficina = propiedadesSW.get(ServiciosWebConfiguration.ORVE_COD_OFICINA);
            
                if(StringUtils.isEmpty(sAcronOficina)){
                    sAcronOficina = ConstantesWSExportacion.ACRON_OFICINA_DEFECTO;
                }

                Session session = HibernateUtil.currentSession(entidad.getIdentificador());
                tran = session.beginTransaction();
                    
                Criteria criteriaResults = session.createCriteria(EntityByLanguage.getScrOficLanguage((new Locale ("es","ES","")).getLanguage()));

                // Recuperamos los resultados
                criteriaResults.add(Expression.eq("acron", sAcronOficina));

                List<ScrOfic> listaOficinas = (List<ScrOfic>)criteriaResults.list();
                HibernateUtil.commitTransaction(tran);
                
                if(null != listaOficinas && !listaOficinas.isEmpty()){
                    ScrOfic oficina = listaOficinas.get(0);

                    this.codOficina = oficina.getCode();
                    this.idOficinaOrve = oficina.getId();
                }
            }
        } catch (HibernateException e) {
            LOGGER.error("ERROR al recuperar el tipo de transporte con acron : " + sAcronOficina  + MENSAJE_PARA_LA_ENTIDAD + entidad.getIdentificador() + ". " + e.getMessage(), e);
            HibernateUtil.rollbackTransaction(tran);
            
        } finally {
            HibernateUtil.closeSession(entidad.getIdentificador());
        }
        
        if(StringUtils.isEmpty(this.codOficina)){
            this.codOficina = ConstantesWSExportacion.COD_OFICINA_DEFECTO;
        }
    }
    
    private void getTipoAsuntoConfig() {
        tipoAsunto = ConstantesWSExportacion.COD_TIPO_ASUNTO_DEFECTO;
        
        if(null != propiedadesSW){
            tipoAsunto = propiedadesSW.get(ServiciosWebConfiguration.ORVE_COD_TIPO_ASUNTO);
        }
        
        if(StringUtils.isEmpty(tipoAsunto)){
            tipoAsunto = ConstantesWSExportacion.COD_TIPO_ASUNTO_DEFECTO;
        }
    }
    
    public String getFechaUltimaActualizacion() {
        String fechaUltimaActualizacion = new SimpleDateFormat(ConstantesWSExportacion.FORMATO_FECHA_ORVE_ENVIO).format(new Date());

        DbCnt cnt = null;
        
        try {
            cnt = getDBCntRegistro(entidad);
            
            if(null != cnt){                
                cnt.getConnection();
            
                OrveFechaUltimaActualizacionDAO orveFechaUltimaActualizacionDAO = OrveFechaUltimaActualizacionDAO.getFechaUltimaActualizacion(cnt);
                
                if(null == orveFechaUltimaActualizacionDAO){
                    orveFechaUltimaActualizacionDAO = new OrveFechaUltimaActualizacionDAO(cnt);
                    orveFechaUltimaActualizacionDAO.createNew(cnt);
                    orveFechaUltimaActualizacionDAO.setFechaUltimaActualizacion(new Date());
                    orveFechaUltimaActualizacionDAO.setCorreoEnviado(false);
                    
                    orveFechaUltimaActualizacionDAO.store(cnt);
                    
                } else {
                    Date fechaUltimaActualizacionBD = orveFechaUltimaActualizacionDAO.getDate(OrveFechaUltimaActualizacionDAO.FECHA_ULTIMA_ACTUALIZACION);
                    
                    Calendar restarTiempoAFecha = Calendar.getInstance();
                    restarTiempoAFecha.setTime(fechaUltimaActualizacionBD);
                    restarTiempoAFecha.add(ConstantesWSExportacion.UNIDAD_TIEMPO_RESTAR, ConstantesWSExportacion.CANTIDAD_TIEMPO_RESTAR);
                    
                    Date fechaUltimaActualizacionRestado = restarTiempoAFecha.getTime();
                    fechaUltimaActualizacion = new SimpleDateFormat(ConstantesWSExportacion.FORMATO_FECHA_ORVE_ENVIO).format(fechaUltimaActualizacionRestado);
                    LOGGER.warn("Fecha de la última actualización: " + fechaUltimaActualizacion);
                }
            }
            
        } catch (ISPACException e){
            LOGGER.error("ERROR al recuperar la fecha de la ultima actualización. " + e.getMessage(), e);
            
        } finally {
            if(null != cnt) {
                cnt.closeConnection();
            }
        }
        
        return fechaUltimaActualizacion;
    }
    
    public String setFechaActualizacion() {
        String fechaUltimaActualizacion = new SimpleDateFormat(ConstantesWSExportacion.FORMATO_FECHA_ORVE_ENVIO).format(new Date());

        DbCnt cnt = null;
        
        try {
            cnt = getDBCntRegistro(entidad);
            
            if(null != cnt){                
                cnt.getConnection();
            
                OrveFechaUltimaActualizacionDAO orveFechaUltimaActualizacionDAO = OrveFechaUltimaActualizacionDAO.getFechaUltimaActualizacion(cnt);
                
                if(null == orveFechaUltimaActualizacionDAO){
                    orveFechaUltimaActualizacionDAO = new OrveFechaUltimaActualizacionDAO(cnt);
                    orveFechaUltimaActualizacionDAO.createNew(cnt);
                }
                
                fechaActualizacionAnterior = orveFechaUltimaActualizacionDAO.getDate(OrveFechaUltimaActualizacionDAO.FECHA_ULTIMA_ACTUALIZACION);
                esCorreoEnviado = orveFechaUltimaActualizacionDAO.isCorreoEnviado();
                
                Date fechaActual = new Date();
                
                orveFechaUltimaActualizacionDAO.setFechaUltimaActualizacion(fechaActual);
                orveFechaUltimaActualizacionDAO.setCorreoEnviado(false);
                orveFechaUltimaActualizacionDAO.store(cnt);
                
                fechaUltimaActualizacion = new SimpleDateFormat(ConstantesWSExportacion.FORMATO_FECHA_ORVE_ENVIO).format(fechaActual);
                LOGGER.warn("Fecha de la fin de la búsqueda: " + fechaUltimaActualizacion);
            }
            
        } catch (ISPACException e){
            LOGGER.error("ERROR al guardar la fecha de la ultima actualización. " + e.getMessage(), e);
            
        } finally {
            if(null != cnt) {
                cnt.closeConnection();
            }
        }
        
        return fechaUltimaActualizacion;
    }
    
    public void enviaCorreoRollbackFechaUltimaActualizacion(){
    	enviaCorreoRollbackFechaUltimaActualizacion("");
    }
    
    public void enviaCorreoRollbackFechaUltimaActualizacion(String mensaje){
        if(!esCorreoEnviado){
            enviaCorreosErrorORVE(mensaje);
        }
        
        deshacerFechaUltimaActualizacion();
    }
    
    public void deshacerFechaUltimaActualizacion() {

        DbCnt cnt = null;
        
        try {
            cnt = getDBCntRegistro(entidad);
            
            if(null != cnt){                
                cnt.getConnection();
            
                OrveFechaUltimaActualizacionDAO orveFechaUltimaActualizacionDAO = OrveFechaUltimaActualizacionDAO.getFechaUltimaActualizacion(cnt);
                
                if(null == orveFechaUltimaActualizacionDAO){
                    orveFechaUltimaActualizacionDAO = new OrveFechaUltimaActualizacionDAO(cnt);
                    orveFechaUltimaActualizacionDAO.createNew(cnt);
                }
                
                orveFechaUltimaActualizacionDAO.setFechaUltimaActualizacion(fechaActualizacionAnterior);
                orveFechaUltimaActualizacionDAO.setCorreoEnviado(esCorreoEnviado);
                orveFechaUltimaActualizacionDAO.store(cnt);
            }
            
        } catch (ISPACException e){
            LOGGER.error("ERROR al deshacer la fecha de la ultima actualización. " + e.getMessage(), e);
            
        } finally {
            if(null != cnt) {
                cnt.closeConnection(); 
            }
        }        
    }
    
    public void anotaRegistroEnHistoricoORVE(int identificador, String nregSigem) {
        
        DbCnt cnt = null;
        
        try {
            if(null != entidad && StringUtils.isNotEmpty(entidad.getIdentificador())){
                cnt = getDBCntRegistro(entidad);
                cnt.getConnection();
        
                if (null == OrveHistoRegistroDAO.getByIdentificadorOrve(cnt, identificador)){
                    
                    OrveHistoRegistroDAO orveHistoRegitro = new OrveHistoRegistroDAO(cnt);
                    orveHistoRegitro.createNew(cnt);
                    orveHistoRegitro.setIdentificadorOrve(identificador);
                    orveHistoRegitro.setNregSigem(nregSigem);
                    orveHistoRegitro.setFechaRegistro(new Date (System.currentTimeMillis()));
                    
                    orveHistoRegitro.store(cnt);
                }
            }
        
        } catch (ISPACException e) {
            LOGGER.error("ERROR al guardar en el histórico de ORVE el registro con identificador: " + identificador + ", número de registro en SIGEM: " + nregSigem + ". " + e.getMessage(), e);
            
        } finally {            
            if(null != cnt) {
                cnt.closeConnection();
            }
        }
    }
    
    public boolean esRegistroYaConsolidado(int identificadorORVE) {
        boolean resultado = false;
        
        DbCnt cnt = null;
        
        try {
            cnt = getDBCntRegistro(entidad);
            
            if(null != cnt){                
                cnt.getConnection();
        
                OrveHistoRegistroDAO orveHistoregistro = OrveHistoRegistroDAO.getByIdentificadorOrve(cnt, identificadorORVE);
                
                if(null != orveHistoregistro){
                    resultado = true;
                }
            }
        
        } catch (ISPACException e) {
            LOGGER.error("ERROR al consultar si el registro con identificadorORVE: " + identificadorORVE + " ya había sido consolidado. " + e.getMessage(), e);
            
        } finally {            
            if(null != cnt) {
                cnt.closeConnection();
            }
        }
        
        return resultado;
    }
            
    protected DbCnt getDBCntRegistro(Entidad entidad) throws ISPACException {
        return getDBCnt(entidad, ConstantesWSExportacion.POOL_NAME_REGISTRO);
    }
    
    protected DbCnt getDBCntTramitador(Entidad entidad) throws ISPACException {
        return getDBCnt(entidad, ConstantesWSExportacion.POOL_NAME_TRAMTIADOR);
    }
    
    protected DbCnt getDBCnt(Entidad entidad, String poolName) throws ISPACException {
        DbCnt conexion = null;
        
        if(null != entidad && StringUtils.isNotEmpty(entidad.getIdentificador())){
            String poolNameEntidad = MessagesFormatter.format(poolName, new Object[]{entidad.getIdentificador()});
            conexion = new DbCnt(poolNameEntidad);
        }

        return conexion;
    }

    public void enviaCorreosErrorORVE(String mensaje) {
        String correosError = "";
        
        if(null != propiedadesSW){
            correosError = propiedadesSW.get(ServiciosWebConfiguration.ORVE_MAIL_ERROR);
            
            if(StringUtils.isNotEmpty(correosError) && StringUtils.isNotEmpty(correosError.split(";"))){
                
                for(String correo : correosError.split(";")){
                    enviaCorreo(correo, mensaje);
                }
            }
        }
    }

    private void enviaCorreo(String correo, String mensaje) {
        if(StringUtils.isNotEmpty(correo)){
            
            String strAsunto = "[AL-SIGM] Error al sincronizar con ORVE la entidad " + entidad.getIdentificador() + " - " + entidad.getNombre();
            
            StringBuilder sbContenido = new StringBuilder();
            sbContenido.append("<img src='cid:escudo' width='200px'/>");
            sbContenido.append("<p align=justify>");
            sbContenido.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Estimado señor/a:");
            sbContenido.append(SALTO_LINEA_HTML);
            sbContenido.append(SALTO_LINEA_HTML);
            sbContenido.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Se ha producido algún error al recuperar o consolidar los registros de ORVE para la entidad: " + entidad.getIdentificador() + " - " + entidad.getNombre() + ".");
            sbContenido.append(SALTO_LINEA_HTML);
            sbContenido.append(SALTO_LINEA_HTML);
            
            if(StringUtils.isNotEmpty(mensaje)){
            	sbContenido.append(mensaje);
            	sbContenido.append(SALTO_LINEA_HTML);
            	sbContenido.append(SALTO_LINEA_HTML);
            }
            
            sbContenido.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Revise los logs y solucione el problema a la mayor brevedad posible.");

            sbContenido.append(SALTO_LINEA_HTML);
            sbContenido.append(SALTO_LINEA_HTML);
            
            DbCnt cnt = null;                    
            try {
                cnt = getDBCntTramitador(entidad);
                cnt.getConnection();
                
                IClientContext cct = new ClientContext(cnt);
                
                List<Object[]> imagenes = getImagenesCorreo();
                
                MailUtil.enviarCorreo(cct, correo, strAsunto, sbContenido.toString(), imagenes);
                
                esCorreoEnviado = true;
                
            } catch (ISPACException e) {
                LOGGER.error("ERROR al enviar el correo con el error al sincronizar el registro con ORVE para la dirección: " + correo + MENSAJE_PARA_LA_ENTIDAD + entidad.getIdentificador() + " - " + entidad.getNombre() + ". " + e.getMessage(), e);
            } finally {
                if (null != cnt){
                    cnt.closeConnection();
                }
            }
        }        
    }

    protected List<Object[]> getImagenesCorreo() {
        String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad.getIdentificador(), "/SIGEM_TramitacionWeb");

        Object[] imagen = {rutaImg, Boolean.TRUE, ConstantesWSExportacion.IMAGEN_CORREO_ERROR, "escudo"};
        List<Object[]> imagenes = new ArrayList<Object[]>();
        imagenes.add(imagen);
        
        return imagenes;
    }

    public abstract boolean isTieneConfiguracion();
    public abstract int[] getIdentificadoresDeORVE();
    public abstract String getRegistroORVEYConsolidaEnSigem(int identificadorORVE) throws RemoteException;
}
