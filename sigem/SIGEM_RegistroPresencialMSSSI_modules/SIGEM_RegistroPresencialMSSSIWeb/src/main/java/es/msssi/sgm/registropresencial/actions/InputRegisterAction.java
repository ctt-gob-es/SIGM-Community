/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.actions;

import ieci.tecdoc.sgm.backoffice.utils.Utilidades;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.ApplicationContext;

import servcert.msssi.es.tipos.PeticionValidacionType;
import servcert.msssi.es.tipos.RespuestaValidacionType;
import beans.DocumentArchiveResponse;
import beans.DocumentDownloadResponse;
import beans.RemoteSignBeginResponse;
import beans.RemoteSignEndResponse;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesdoc.Idocarchhdr;
import com.ieci.tecdoc.common.invesdoc.Iuserdepthdr;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxPK;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.DtrFdrResults;
import com.ieci.tecdoc.common.isicres.UpdHisFdrResults;
import com.ieci.tecdoc.common.utils.ISDistribution;
import com.ieci.tecdoc.isicres.session.folder.FolderFileSession;
import com.ieci.tecdoc.isicres.usecase.book.xml.AsocRegsResults;
import com.ieci.tecdoc.isicres.web.util.ContextoAplicacionUtil;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import core.tools.Conversor;
import core.tools.FileNameUtils;
import core.tools.codec.Base64Codec;
import core.tools.hasher.HashType;
import core.tools.hasher.Hasher;
import core.web.server.bean.ResponseValue;
import core.web.server.bean.ServerResponse;
import core.web.server.bean.StatusInfo;
import es.ieci.tecdoc.isicres.api.business.manager.impl.RegistroManagerImpl;
import es.ieci.tecdoc.isicres.api.business.vo.BaseOficinaVO;
import es.ieci.tecdoc.isicres.api.business.vo.ContextoAplicacionVO;
import es.ieci.tecdoc.isicres.api.business.vo.IdentificadorRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoContenidoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoDatosFirmaVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.IdentificadorDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoValidezDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralEntradaVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralSalidaVO;
import es.mscbs.wsclient.servcert.facade.ServcertWSService;
import es.mscbs.wsclient.servcert.utils.ServcertClientUtils;
import es.msssi.sgm.registropresencial.beans.AsocRegisterBean;
import es.msssi.sgm.registropresencial.beans.Configuration;
import es.msssi.sgm.registropresencial.beans.ContentTypeEnum;
import es.msssi.sgm.registropresencial.beans.ContentTypeIRPermitEnum;
import es.msssi.sgm.registropresencial.beans.FieldsCopyInputRegEnum;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.businessobject.DistributionBo;
import es.msssi.sgm.registropresencial.businessobject.InputRegisterBo;
import es.msssi.sgm.registropresencial.businessobject.InterestedBo;
import es.msssi.sgm.registropresencial.businessobject.ProcessorBo;
import es.msssi.sgm.registropresencial.businessobject.RegInterchangeBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterDocumentsBo;
import es.msssi.sgm.registropresencial.businessobject.UserBo;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.connector.AntivirusConnector;
import es.msssi.sgm.registropresencial.connector.PFEConnector;
import es.msssi.sgm.registropresencial.connector.TramConnector;
import es.msssi.sgm.registropresencial.daos.UserDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPDistributionException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPInputRegisterException;
import es.msssi.sgm.registropresencial.errors.RPRegisterErrorCode;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.ResourceRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.utils.UtilsHash;
import es.msssi.sgm.registropresencial.validations.ValidationBo;
import es.msssi.sgm.registropresencial.validations.ValidationListBo;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdr;
import es.msssi.sigm.dao.dataaccess.service.SIGMServiceManager;
import es.sigm.tramitador.ws.RespuestaConsultaExpedientes;
import es.sigm.tramitador.ws.RespuestaIniciarExpediente;

/**
 * Action de registro de entrada.
 *
 * @author cmorenog
 */
public class InputRegisterAction extends GenericActions {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(InputRegisterAction.class.getName());
    private boolean readOnly = false;
    private boolean readOnlyIR = false;
    private boolean enableCompulsaValue = false;
    private InputRegisterBo inputRegisterBo;
    private RegisterBo registerBo;
    private RegisterDocumentsBo registerDocumentsBo;
    private InputRegisterBean inputRegisterBean;
    private ScrRegstate book;
    private Integer registerCopy;
    private String registerNumCopy;
    private List<UpdHisFdrResults> listUpdates;
    private List<DtrFdrResults> listHistDistribution;
    private List<IntercambioRegistralEntradaVO> listHistInputInterchange;
    private List<IntercambioRegistralSalidaVO> listHistOutputInterchange;
    private List<Axdoch> listDocuments;
    private RegInterchangeBo regInterchangeBo = null;
    private String procedureDestination;
    private static String SEPARATOR_PROCEDURES = "¬";

    // DISTRIBUCION
    /**
     * tipo de destino seleccionado en la distribución.
     */
    private int typeDestinoRedis = 2;
    /**
     * Motivo de la distribución.
     */
    private String motivoDistribucion = null;
    /**
     * departamento seleccionado en la distribución.
     */
    private Iuserdepthdr selectdestinoRedisDepartamentos = null;

    /**
     * Implementación de las funcionalidades de distribución.
     */
    private DistributionBo distributionBo;
    /**
     * lista de departamentos para la distribución.
     */
    private List<Iuserdepthdr> listDepartament = null;
    /**
     * Origen del formulario. B:Búsqueda de registros DI:Distribución entrada
     * DO: Distribucion salida IR:Intercambio Registral salida I: Intercambio
     * Registral entrada
     */
    private String origen = "B";
    private List<AxSf> asocReg;
    private AxSf asocRegPrim;
    private Map<String, Idocarchhdr> idocs;
    private AsocRegisterBean asocRegisterBean = new AsocRegisterBean();
    private int myCurrentTab = 0;
    private boolean canDistr = true;
    private String selectedDocSIR = null;
    private String validateDocument = null;
    private String contentDocument = null;
    private String signDocument = null;
    private String certificateDocument = null;
    DocumentoElectronicoAnexoVO documentoElectronico = new DocumentoElectronicoAnexoVO();
    private Axdoch selectDocument;
    private String listNameDocument = null;
    private UploadedFile fileUpload = null;
    private ScrOrg selectedDestOrg;
    private ScrOrg selectedOriginOrg;
    private boolean saveInterested = false;
    ContextoAplicacionVO contextoAplicacion = null;
    private static ApplicationContext appContext;
    private boolean showExpedient = true;
    private AntivirusConnector antivirusConnector;

    // Cliente Firma
    private UploadedFile file;
    private String fileBase64;
    private String fileName;
    private String jsonServerResponse;
    private String certChain;
    private String csv;
    private byte[] signedData = null;
    private String fileNameDig = null;

    /**
     * Indica si se ha activado el check de documentos leidos.
     */
    private boolean readCheck;
    private String taskId;
    
    static {
        appContext =
                RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
                        .getApplicationContext();
    }

    /**
     * Constructor. Valida el registro.
     *
     * @throws SessionException    si ha habido un problema de sesión.
     * @throws ValidationException Si ha habido un problema de validación de parámetros.
     */
    public InputRegisterAction() throws SessionException, ValidationException {
        init();
        deleteSession("reportsLabelAction");
        book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
        if (book == null) {
            throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
        }
        try {
            validateIdBook(book.getId(), useCaseConf);
        } catch (BookException bookException) {
            LOG.error(ErrorConstants.GET_INFORMATION_BOOK_ERROR_MESSAGE, bookException);
            Utils.redirectToErrorPage(null, bookException, null);
        }
        if (regInterchangeBo == null) {
            regInterchangeBo = new RegInterchangeBo();
        }
        if (facesContext.getExternalContext().getFlash().get("registerSelect") != null) {
            inputRegisterBean = new InputRegisterBean();
            inputRegisterBean.setFdrid((Integer) FacesContext.getCurrentInstance()
                    .getExternalContext().getFlash().get("registerSelect"));
            if (facesContext.getExternalContext().getFlash().get("distribucionIn") != null) {
                origen = "DI";
            } else {
                if (facesContext.getExternalContext().getFlash().get("distribucionOut") != null) {
                    origen = "DO";
                } else {
                    if (facesContext.getExternalContext().getFlash().get("intercambio") != null) {
                        origen = "IR";
                    } else {
                        if (facesContext.getExternalContext().getFlash().get("intercambioR") != null) {
                            origen = "I";
                        }
                    }
                }
            }
        }

	/* EDICION O CONSULTA */
        if (inputRegisterBean != null && inputRegisterBean.getFdrid() != null) {
            validateRegister(useCaseConf, book.getId(), inputRegisterBean.getFdrid());
            loadInputRegister();
            if(origen=="DI")
            {
                canDistr=true;
            }
        } else {
        /* COPIAR */
            if (facesContext.getExternalContext().getFlash().get("registerCopy") != null) {

                registerCopy =
                        ((Integer) facesContext.getExternalContext().getFlash().get("registerCopy"));
                registerNumCopy =
                        ((String) facesContext.getExternalContext().getFlash()
                                .get("registerNumCopy"));

                try {
                    copyInputRegister();
                } catch (SessionException sessionException) {
                    LOG.error(ErrorConstants.COPY_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
                    Utils.redirectToErrorPage(null, sessionException, null);
                }
            }
        }

        //miramos si es funcionario
        // Recuperamos la caché
        try {
            CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
                    useCaseConf.getSessionID());
            enableCompulsaValue = (Boolean)cacheBag.get(ConstantesGestionUsuariosBackOffice.PARAMETRO_FUNCIONARIO_HABILITADO);
        } catch (Exception e) {
            LOG.error("Error al recoger Parametro de funcionario habilitado", e);
        }
    }

    public void createExpedient() {
    	LOG.info("Entramos en el método createExpedient " + procedureDestination);
    	if(StringUtils.isNotBlank(procedureDestination)) 
    	{
    		init();
    		String procedureDescription = procedureDestination.substring(procedureDestination.lastIndexOf(SEPARATOR_PROCEDURES)+1);
    		procedureDestination = procedureDestination.substring(0, procedureDestination.lastIndexOf(SEPARATOR_PROCEDURES));
            TramConnector connector = (TramConnector) appContext.getBean("tramConnector");
            ProcessorBo tram = new ProcessorBo();
            FacesMessage message = null;
            StringBuffer error = new StringBuffer();
            String callbackParam = "ERROR";
            
            String codeErrorRespuestaIniciar, descErrorRespuestaIniciar, 
            	  codeErrorRespuestaConsulta, descErrorRespuestaConsulta,
            	  estadoRespuestaIniciar, estadoRespuestaConsulta = null;
            try {
                if (validateInterestedTram()) {
                    if (!tram.existExpedientProcedure(inputRegisterBean.getFld1(), procedureDestination)) 
                    {
                    	connector.setWsProcedure(procedureDestination);
                        RespuestaIniciarExpediente respuestaIniciar = connector.iniciarExpedientWithResponse(inputRegisterBean, useCaseConf, book);
                        
                        estadoRespuestaIniciar = StringUtils.isNotBlank(respuestaIniciar.getEstadoRespuesta()) ? respuestaIniciar.getEstadoRespuesta(): "";
                        
                        codeErrorRespuestaIniciar = respuestaIniciar.getErrorResponse() != null 
                        		&& StringUtils.isNotBlank(respuestaIniciar.getErrorResponse().getCodigo()) ? respuestaIniciar.getErrorResponse().getCodigo() : null;
                        		
                        descErrorRespuestaIniciar = respuestaIniciar.getErrorResponse() != null 
                           		&& StringUtils.isNotBlank(respuestaIniciar.getErrorResponse().getDescripcion()) ? respuestaIniciar.getErrorResponse().getDescripcion() : null;
                                		
                        // OK Iniciar
                        if(estadoRespuestaIniciar.equals("OK")) 
                        {
                        	LOG.info("Expediente creado con éxito. Procedemos a realiar su consulta");
                        	RespuestaConsultaExpedientes respuestaConsulta = connector.consultarExpediente(
                        			inputRegisterBean, procedureDestination, useCaseConf);
                        	
                        	estadoRespuestaConsulta = StringUtils.isNotBlank(respuestaConsulta.getEstadoRespuesta()) ? respuestaConsulta.getEstadoRespuesta(): "";
                        	
                        	codeErrorRespuestaConsulta = respuestaConsulta.getErrorResponse() != null 
                            		&& StringUtils.isNotBlank(respuestaConsulta.getErrorResponse().getCodigo()) ? respuestaConsulta.getErrorResponse().getCodigo() : "";
                        	
                            descErrorRespuestaConsulta = respuestaConsulta.getErrorResponse() != null 
                               		&& StringUtils.isNotBlank(respuestaConsulta.getErrorResponse().getDescripcion()) ? respuestaConsulta.getErrorResponse().getDescripcion() : null;		
                               		
                        	if(estadoRespuestaConsulta.equals("OK") && respuestaConsulta.getExpedientes() != null
                        			&& respuestaConsulta.getExpedientes().getExpediente() != null &&
                        			!respuestaConsulta.getExpedientes().getExpediente().isEmpty()) 
                        	{
                        		String numExp = Utils.getNumeroExpediente(respuestaConsulta.getExpedientes()).getNumExpediente();
                        		tram.insertExpedientProcedure(inputRegisterBean.getFld1(), procedureDestination);
                        		
                                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Crear expediente", 
                                		"Se ha generado el expediente <b>" + numExp + "</b> a partir de este registro");
                                callbackParam = "OK";
                        	}
                        	else
                        	{
                        		LOG.error("Se ha producido un error al consultar el expediente creado para el registro [" + inputRegisterBean.getFld1()+ "] "
                        				+ "y usuario: [" + useCaseConf.getUserName() + "]");
                            	LOG.error("ERROR CODE: " + codeErrorRespuestaConsulta + ", ERROR DESCRIPTION: " + 
                            			descErrorRespuestaConsulta);

                            	message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Crear expediente", 
                            			styleErrorIni + "Se ha producido un error al consultar el expediente de <b>"+ procedureDescription +"</b> "
                            					+ "a partir de este registro." + styleErrorEnd);
                        	}
                        }
                        
                        // ERROR al Iniciar
                        else 
                        {
                        	error.append("Se ha producido un error al iniciar expediemte para el registro [" + inputRegisterBean.getFld1()+ "] "
                        			+ "y usuario: [" + useCaseConf.getUserName() + "]");
                        	error.append("\n ERROR CODE: " + codeErrorRespuestaIniciar + ", ERROR DESCRIPTION: " + 
                        			descErrorRespuestaIniciar);
                        	LOG.error(error.toString());
                        	
                        	message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error inesperado", 
                        			styleErrorIni + "Se ha producido un error inesperado al crear un expediente de <b>"+ procedureDescription +"</b> a partir de este registro." + styleErrorEnd);
                        	
                        	if(StringUtils.isNotBlank(codeErrorRespuestaIniciar) && codeErrorRespuestaIniciar.equals("ERR05")) {
                        		message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de validación", 
                            			styleErrorIni + "No tiene permisos para poder crear un expediente de <b>"+ procedureDescription +"</b> a partir de este registro." + styleErrorEnd);
                        	}
                        }
                    } 
                    else 
                    {
                    	message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de validación", 
                    			styleErrorIni + "Ya existe un expediente generado con este registro para el procedimiento <b>" + procedureDescription + "</b>." + styleErrorEnd);
                    }
                } 
                else 
                {
                	message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de validación",
                			styleErrorIni + "Se debe informar del documento de identidad de los interesados." + styleErrorEnd);
                	
                }
            } 
            catch (RPGenericException e) 
            {
            	e.printStackTrace();
            	LOG.error("Se ha producido un error desconocido al iniciar el expediente: " + e.getMessage());
            	message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error desconocido",
            			styleErrorIni + "Se ha producido un error desconocido al iniciar el expediente. Consulte con su administrador." + styleErrorEnd);
            }
            RequestContext.getCurrentInstance().addCallbackParam(callbackParam, true);
            RequestContext.getCurrentInstance().showMessageInDialog(message);
    	}
    }
    
    /**
     * Borra los datos del interesado y crea uno vacío.
     */
    public void reinit() {
        Interesado interesado = new Interesado();
        interesado.setTipo(useCaseConf.getInterestedType());
        inputRegisterBean.setInteresado(interesado);
    }

    /**
     * Borra los datos de la distribución y crea una vacío.
     */
    @SuppressWarnings("unchecked")
    public void reinitDis() {
        init();
        try {
            deleteSession("reportsLabelAction");
            if (inputRegisterBean.getFld8() != null && inputRegisterBean.getFld8().getId() != null) {
                List<Integer> orgIds = new ArrayList<Integer>();
                orgIds.add(inputRegisterBean.getFld8().getId());
                this.listDepartament =
                        ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(),
                                typeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
                this.setSelectdestinoRedisDepartamentos(Utils.defaultDepart(this.listDepartament,
                        inputRegisterBean.getFld8().getId()));
            } else {
                this.listDepartament =
                        ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(),
                                typeDestinoRedis, useCaseConf.getEntidadId(), null);
                this.setSelectdestinoRedisDepartamentos(null);
            }
        } catch (SessionException sessionException) {
            LOG.error(ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, sessionException);
            Utils.redirectToErrorPage(null, sessionException, null);
        } catch (ValidationException validationException) {
            LOG.error(ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, validationException);
            Utils.redirectToErrorPage(null, validationException, null);
        }
        this.setMotivoDistribucion(null);
    }

    /**
     * Crea un nuevo Bean y un nuevo Business Object de registro de entrada.
     */
    @PostConstruct
    public void create() {
        if (inputRegisterBo == null) {
            inputRegisterBo = new InputRegisterBo();
        }
        if (inputRegisterBean == null) {
            inputRegisterBean = new InputRegisterBean();
        }
    }

    @PreDestroy
    public void destroy1() {
        deleteSession("reportsLabelAction");
        if (inputRegisterBean != null) {
            RegistroManagerImpl registroManagerImpl = new RegistroManagerImpl();
            UsuarioVO usuario = new UsuarioVO();
            if (inputRegisterBean.getFdrid() != null) {
                usuario =
                        (UsuarioVO) FacesContext.getCurrentInstance().getExternalContext()
                                .getSessionMap().get("USERVO");
                IdentificadorRegistroVO identificadorRegistro =
                        new IdentificadorRegistroVO(String.valueOf(inputRegisterBean.getFdrid()),
                                String.valueOf(book.getIdocarchhdr().getId()));
                registroManagerImpl.unlockRegistro(usuario, identificadorRegistro);
            }
        }
    }

    /**
     * Limpia el formulario y la tabla de resultados.
     */
    public void limpiar() {
        init();
        Application application = facesContext.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot =
                viewHandler.createView(facesContext, facesContext.getViewRoot().getViewId());
        facesContext.setViewRoot(viewRoot);
        facesContext.renderResponse(); // Optional
    }

    /**
     * Da de alta un nuevo registro.
     *
     * @throws BookException       si ha habido algún problema con el libro de registro.
     * @throws SessionException    si ha habido algún problema con la sesión.
     * @throws ValidationException si ha habido algún problema con en la validación.
     */
    public void saveInputRegister() throws BookException, SessionException, ValidationException {

        if (book == null) {
            throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
        }
        // validaciones de seguridad
        try {
            ValidationBo.validationSecurityUser(book.getId(), null, useCaseConf);
        } catch (RPGenericException rpGenericException) {
            LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                    + rpGenericException.getCode().getCode() + " . Mensaje: "
                    + rpGenericException.getShortMessage());
            Utils.redirectToErrorPage(rpGenericException, null, null);
        }
        deleteSession("reportsLabelAction");
        if (validateInterested()) {
            loadDocSirSelected();
            if (inputRegisterBean.getFdrid() != null) {
                try {
                    Integer idOffice = null;
                    if (saveInterested) {
                        try {
                            contextoAplicacion =
                                    ContextoAplicacionUtil
                                            .getContextoAplicacion((javax.servlet.http.HttpServletRequest) facesContext
                                                    .getExternalContext().getRequest());
                            BaseOficinaVO oficina = contextoAplicacion.getOficinaActual();
                            idOffice = Integer.parseInt(oficina.getId());
                        } catch (TecDocException rpGenericException) {
                            idOffice = null;
                            LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE
                                    + ". Código: " + rpGenericException.getCode() + " . Mensaje: "
                                    + rpGenericException.getMessage());
                        }
                    }
                    InputRegisterBo.saveOrUpdateFolder(useCaseConf, book.getId(),
                            inputRegisterBean.getFdrid(), null, inputRegisterBean,
                            inputRegisterBean.getInteresados(), null, saveInterested, idOffice);
                    saveInterested = false;
                } catch (RPInputRegisterException rpInputRegisterException) {
                    LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                            + rpInputRegisterException.getCode().getCode() + " . Mensaje: "
                            + rpInputRegisterException.getShortMessage());
                    Utils.redirectToErrorPage(rpInputRegisterException, null, null);
                } catch (RPGenericException rpGenericException) {
                    LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                            + rpGenericException.getCode().getCode() + " . Mensaje: "
                            + rpGenericException.getShortMessage());
                    Utils.redirectToErrorPage(rpGenericException, null, null);
                }
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Se ha guardado el Registro Correctamente",
                                "Se ha guardado el Registro Correctamente"));
            } else {
                try {
                    inputRegisterBean.setFdrid(InputRegisterBo.saveOrUpdateFolder(useCaseConf,
                            book.getId(), null, null, inputRegisterBean,
                            inputRegisterBean.getInteresados(), null));
                } catch (RPInputRegisterException rpInputRegisterException) {
                    LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                            + rpInputRegisterException.getCode().getCode() + " . Mensaje: "
                            + rpInputRegisterException.getShortMessage());
                    Utils.redirectToErrorPage(rpInputRegisterException, null, null);
                } catch (RPGenericException rpGenericException) {
                    LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                            + rpGenericException.getCode().getCode() + " . Mensaje: "
                            + rpGenericException.getShortMessage());
                    Utils.redirectToErrorPage(rpGenericException, null, null);
                }
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Se ha creado el Registro Correctamente.",
                                "Se ha creado el Registro Correctamente."));
            }

            loadInputRegister();
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Es obligatorio incluir mínimo un interesado.",
                            "Es obligatorio incluir mínimo un interesado."));
        }
    }

    private boolean validateInterested() {
        boolean result = true;
        if (inputRegisterBean.getInteresados() == null
                || inputRegisterBean.getInteresados().size() == 0) {
            result = false;
        }
        return result;
    }

    private boolean validateInterestedTram() {
        boolean result = true;
        if (inputRegisterBean.getInteresados() == null
                || inputRegisterBean.getInteresados().size() == 0) {
            result = false;
        } else {
            for (Interesado inter : inputRegisterBean.getInteresados()) {
                if (inter.getDocIndentidad() == null || inter.getDocIndentidad().isEmpty()) {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * guarda en sesion el valor por defecto del tipo de interesado.
     */
    public void saveInterestedTypeDefault() {
        if (inputRegisterBean.getInteresado() != null) {
            useCaseConf.setInterestedType(inputRegisterBean.getInteresado().getTipo());
            saveUseCaseConf(useCaseConf);
        }
        saveInterested = true;
    }

    /**
     * guarda en sesion el valor por defecto del tipo de interesado.
     */
    public void saveInterested() {
        saveInterested = true;
    }

    /**
     * Carga un registro.
     */
    public void loadInputRegister() {
        try {
            init();
            deleteSession("reportsLabelAction");
            book =
                    (ScrRegstate) facesContext.getExternalContext().getSessionMap()
                            .get(KeysRP.J_BOOK);
            if (book == null) {
                throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
            }

	    /* MODIFICACION */
            if (inputRegisterBean.getFdrid() != null) {
                Boolean readOnlyFlash = false;
                if (facesContext.getExternalContext().getFlash().get("readOnly") != null) {
                    readOnlyFlash =
                            (Boolean) facesContext.getExternalContext().getFlash().get("readOnly");
                }
                if (registerBo == null) {
                    registerBo = new RegisterBo();
                }
                if (inputRegisterBo == null) {
                    inputRegisterBo = new InputRegisterBo();
                }
                try {
                    readOnly =
                            registerBo.readOnly(readOnlyFlash, inputRegisterBean.getFdrid(),
                                    useCaseConf, book.getId());

                    distributionBo = new DistributionBo();
                    try {
                        canDistr =
                                distributionBo
                                        .canDistributionRegister(useCaseConf, inputRegisterBean
                                                .getFdrid(), book.getIdocarchhdr().getId());
                    } catch (RPDistributionException e) {
                        LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                                + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
                    }
                    LOG.info("Cargando datos del registro " + inputRegisterBean.getFdrid());
                    inputRegisterBean =
                            inputRegisterBo.loadInputRegisterBean(useCaseConf, book,
                                    inputRegisterBean.getFdrid());
                    loadSelectedDocSIR();
                    ProcessorBo tram = new ProcessorBo();
                    if (tram.existExpedient(inputRegisterBean.getFld1())) {
                        showExpedient = false;
                    }
                    try {
                        readOnlyIR =
                                regInterchangeBo.isIntercambioRegistralNotEdit(
                                        Integer.toString(book.getId()),
                                        Integer.toString(inputRegisterBean.getFdrid()),
                                        String.valueOf(inputRegisterBean.getFld5().getId()));
                    } catch (RPRegistralExchangeException e1) {
                        LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                                + e1.getCode().getCode() + " . Mensaje: " + e1.getShortMessage());
                    }
                    if (inputRegisterBean != null && inputRegisterBean.getFld7() != null) {
                        selectedOriginOrg = inputRegisterBean.getFld7();
                    }
                    if (inputRegisterBean != null && inputRegisterBean.getFld8() != null) {
                        selectedDestOrg = inputRegisterBean.getFld8();
                    }
                } catch (RPInputRegisterException rpInputRegisterException) {
                    LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                            + rpInputRegisterException.getCode().getCode() + " . Mensaje: "
                            + rpInputRegisterException.getShortMessage());
                    Utils.redirectToErrorPage(rpInputRegisterException, null, null);
                } catch (RPRegisterException rpRegisterException) {
                    LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                            + rpRegisterException.getCode().getCode() + " . Mensaje: "
                            + rpRegisterException.getShortMessage());
                    Utils.redirectToErrorPage(rpRegisterException, null, null);
                } catch (RPGenericException rpGenericException) {
                    LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                            + rpGenericException.getCode().getCode() + " . Mensaje: "
                            + rpGenericException.getShortMessage());
                    Utils.redirectToErrorPage(rpGenericException, null, null);
                }
            }
        } catch (SessionException sessionException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
            Utils.redirectToErrorPage(null, sessionException, null);
        }
    }

    private void loadDocSirSelected() {
        if (selectedDocSIR != null && !"".equals(selectedDocSIR)) {
            inputRegisterBean.setFld504(null);
            inputRegisterBean.setFld505(null);
            inputRegisterBean.setFld506(null);
            if (selectedDocSIR.equals("fld504")) {
                inputRegisterBean.setFld504(1);
            } else {
                if (selectedDocSIR.equals("fld505")) {
                    inputRegisterBean.setFld505(1);
                } else {
                    if (selectedDocSIR.equals("fld506")) {
                        inputRegisterBean.setFld506(1);
                    }
                }
            }
        }
    }

    private void loadSelectedDocSIR() {
        if (inputRegisterBean != null) {
            if (inputRegisterBean.getFld504() != null) {
                selectedDocSIR = "fld504";
            } else {
                if (inputRegisterBean.getFld505() != null) {
                    selectedDocSIR = "fld505";
                } else {
                    if (inputRegisterBean.getFld506() != null) {
                        selectedDocSIR = "fld506";
                    }
                }
            }
        }
    }

    /**
     * Obtiene los documentos asociados a un registro.
     */
    public void getRegisterAttachedDocuments() {
        boolean openFolderDtr = ("DI".equals(origen) || "DO".equals(origen)) ? true : false;
        listDocuments = new ArrayList<Axdoch>();
        if (registerDocumentsBo == null) {
            registerDocumentsBo = new RegisterDocumentsBo();
        }
        try {
            LOG.info("Cargando documentos asociados al registro " + inputRegisterBean.getFdrid());
            listDocuments =
                    registerDocumentsBo.getDocumentsBasicInfo(useCaseConf, book.getId(),
                            inputRegisterBean.getFdrid(), openFolderDtr);
            listNameDocument = "[";
            boolean primero = true;
            for (Axdoch doch : listDocuments) {
                if (primero) {
                    primero = false;
                } else {
                    listNameDocument += ",";
                }
                listNameDocument += "{\"NameDoc\":\"" + doch.getName() + "\"}";
            }
            listNameDocument += "]";
        } catch (RPRegisterException rPRegisterException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, rPRegisterException);
        }
    }

    /**
     * Obtiene el histórico de un registro.
     */
    public void getRegisterHistoricalUpdates() {
        listUpdates = new ArrayList<UpdHisFdrResults>();
        try {
            LOG.info("Cargando histórico del registro " + inputRegisterBean.getFdrid());
            listUpdates =
                    registerBo.getUpdHisFdrResults(useCaseConf, book, inputRegisterBean.getFdrid(),
                            inputRegisterBean.getFld1());
        } catch (RPRegisterException rpRegisterException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                    + rpRegisterException.getCode().getCode() + " . Mensaje: "
                    + rpRegisterException.getShortMessage());
            Utils.redirectToErrorPage(rpRegisterException, null, null);
        } catch (RPGenericException rpGenericException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                    + rpGenericException.getCode().getCode() + " . Mensaje: "
                    + rpGenericException.getShortMessage());
            Utils.redirectToErrorPage(rpGenericException, null, null);
        }
    }

    /**
     * Obtiene el histórico de distribución de un registro.
     */
    public void getRegisterHistDistribution() {
        listHistDistribution = new ArrayList<DtrFdrResults>();
        try {
            LOG.info("Cargando histórico de distribución del registro "
                    + inputRegisterBean.getFdrid());
            distributionBo = new DistributionBo();
            listHistDistribution =
                    distributionBo.getHistDistribution(useCaseConf, book.getId(),
                            inputRegisterBean.getFdrid());
        } catch (BookException bookException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, bookException);
            Utils.redirectToErrorPage(null, bookException, null);
        } catch (SessionException sessionException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
            Utils.redirectToErrorPage(null, sessionException, null);
        } catch (ValidationException validationException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, validationException);
            Utils.redirectToErrorPage(null, validationException, null);
        }
    }

    /**
     * Obtiene el histórico de entrada y salida del Intercambio Registral.
     */
    public void getRegisterHistInputAndOutputInterchange() {
        listHistInputInterchange = new ArrayList<IntercambioRegistralEntradaVO>();
        listHistOutputInterchange = new ArrayList<IntercambioRegistralSalidaVO>();
        try {
            init();
            book =
                    (ScrRegstate) facesContext.getExternalContext().getSessionMap()
                            .get(KeysRP.J_BOOK);
            if (book == null) {
                throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
            }

            if (regInterchangeBo == null) {
                regInterchangeBo = new RegInterchangeBo();
            }
            LOG.info("Cargando histórico de Intecambio Registral del registro "
                    + inputRegisterBean.getFdrid());
            listHistInputInterchange =
                    regInterchangeBo.getHistorialIntercambioRegistralEntrada(
                            String.valueOf(book.getId()),
                            String.valueOf(inputRegisterBean.getFdrid()),
                            String.valueOf(inputRegisterBean.getFld5().getId()));
            listHistOutputInterchange =
                    regInterchangeBo.getHistorialIntercambioRegistralSalida(
                            String.valueOf(book.getId()),
                            String.valueOf(inputRegisterBean.getFdrid()),
                            String.valueOf(inputRegisterBean.getFld5().getId()));
        } catch (RPRegistralExchangeException rPRegisterException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, rPRegisterException);
        } catch (SessionException sessionException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
            Utils.redirectToErrorPage(null, sessionException, null);
        }
    }

    /**
     * Obtiene los registros asociados a un registro.
     */
    @SuppressWarnings("unchecked")
    public void getRegisterAssociatedRegisters() {
        try {
            LOG.info("Cargando registros asociados al registro " + inputRegisterBean.getFdrid());
            Object[] regAsoc =
                    registerBo.getAsocReg(useCaseConf, book.getId(), inputRegisterBean.getFdrid());
            if (regAsoc != null
                    && (!((Map<AxPK, AxSf>) regAsoc[1]).isEmpty() || !((Map<AxPK, AxSf>) regAsoc[2])
                    .isEmpty())) {
                idocs = (Map<String, Idocarchhdr>) regAsoc[0];
                asocReg = registerBo.getListAsoReg((Map<AxPK, AxSf>) regAsoc[1], idocs);
                asocRegPrim = registerBo.getAsoReg((Map<AxPK, AxSf>) regAsoc[2], idocs);
            }
        } catch (RPRegisterException rpRegisterException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                    + rpRegisterException.getCode().getCode() + " . Mensaje: "
                    + rpRegisterException.getShortMessage());
            Utils.redirectToErrorPage(rpRegisterException, null, null);
        } catch (RPGenericException rpGenericException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                    + rpGenericException.getCode().getCode() + " . Mensaje: "
                    + rpGenericException.getShortMessage());
            Utils.redirectToErrorPage(rpGenericException, null, null);
        }
    }

    /**
     * Copia un registro.
     *
     * @throws SessionException si ha habido algún problema con la sesión.
     */
    public void copyInputRegister() throws SessionException {
        init();
        book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
        if (book == null) {
            throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
        }

        if (registerCopy != null) {
            if (inputRegisterBo == null) {
                inputRegisterBo = new InputRegisterBo();
            }
            try {
                Configuration configuration =
                        (Configuration) facesContext.getExternalContext().getSessionMap()
                                .get(KeysRP.CONFIGURATIONUSER);
                String[] fieldsCopy = null;
                if (configuration != null) {
                    fieldsCopy = configuration.getFieldInputSelected();
                }
                inputRegisterBean =
                        inputRegisterBo.copyInputRegisterBean(useCaseConf, book, registerCopy,
                                fieldsCopy, false);
                loadSelectedDocSIR();
                FacesContext.getCurrentInstance().addMessage(
                        "Registro Copiado",
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Se han copiado los siguientes datos del registro "
                                        + registerNumCopy + listField(fieldsCopy),
                                "Se han copiado los siguientes datos del registro "
                                        + registerNumCopy + listField(fieldsCopy)));

                if (inputRegisterBean != null && inputRegisterBean.getFld7() != null) {
                    selectedOriginOrg = inputRegisterBean.getFld7();
                }
                if (inputRegisterBean != null && inputRegisterBean.getFld8() != null) {
                    selectedDestOrg = inputRegisterBean.getFld8();
                }
            } catch (RPInputRegisterException rpInputRegisterException) {
                LOG.error(ErrorConstants.COPY_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                        + rpInputRegisterException.getCode().getCode() + " . Mensaje: "
                        + rpInputRegisterException.getShortMessage());
                Utils.redirectToErrorPage(rpInputRegisterException, null, null);
            } catch (RPGenericException rpGenericException) {
                LOG.error(ErrorConstants.COPY_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                        + rpGenericException.getCode().getCode() + " . Mensaje: "
                        + rpGenericException.getShortMessage());
                Utils.redirectToErrorPage(rpGenericException, null, null);
            }
        }
    }

    /**
     * lista la descripción de los campos copiados.
     *
     * @param fieldsCopy array con los campos
     * @return string con las descripciones concatenadas.
     */
    private String listField(String[] fieldsCopy) {
        ResourceRP resourceRP = ResourceRP.getInstance(useCaseConf.getLocale());
        String result = "";
        if (fieldsCopy != null && fieldsCopy.length > 0) {
            result = ":<br/>";
            boolean first = true;
            String text = null;
            for (String field : fieldsCopy) {
                text =
                        resourceRP.getProperty(FieldsCopyInputRegEnum.getFieldsCopy(field)
                                .getName());
                if (first) {
                    result += text;
                    first = false;
                } else {
                    result += ", " + text;
                }
            }
        }
        return result;
    }

    /**
     * borra un registro asociado.
     *
     * @throws SessionException si ha habido algún problema con la sesión.
     */
    @SuppressWarnings("unchecked")
    public void deleteAssociation() throws SessionException {
        init();
        deleteSession("reportsLabelAction");
        book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
        if (book == null) {
            throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
        }
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        if (registerBo == null) {
            registerBo = new RegisterBo();
        }
        Integer idReg = new Integer(params.get("idReg"));
        Integer idLibro = new Integer(params.get("idLibro"));
        try {
            registerBo.deleteAssociation(useCaseConf, idLibro, idReg);
            Object[] regAsoc =
                    registerBo.getAsocReg(useCaseConf, book.getId(), inputRegisterBean.getFdrid());
            if (regAsoc != null) {
                idocs = (Map<String, Idocarchhdr>) regAsoc[0];
                asocReg = registerBo.getListAsoReg((Map<AxPK, AxSf>) regAsoc[1], idocs);
                asocRegPrim = registerBo.getAsoReg((Map<AxPK, AxSf>) regAsoc[2], idocs);
            }
            FacesContext.getCurrentInstance().addMessage(
                    "Asociar Registros",
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Se ha desasociado el registro correctamente",
                            "Se ha desasociado el registro correctamente"));
        } catch (RPRegisterException e) {
            LOG.error(ErrorConstants.DELETE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: "
                    + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
            Utils.redirectToErrorPage(e, null, null);
        } catch (RPGenericException rpGenericException) {
            LOG.error(ErrorConstants.DELETE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: "
                    + rpGenericException.getCode().getCode() + " . Mensaje: "
                    + rpGenericException.getShortMessage());
            Utils.redirectToErrorPage(rpGenericException, null, null);
        }
    }

    /**
     * asocia un registro.
     *
     * @throws SessionException si ha habido algún problema con la sesión.
     */
    @SuppressWarnings("unchecked")
    public void saveAsocRegister() throws SessionException {
        init();
        deleteSession("reportsLabelAction");
        book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
        if (book == null) {
            throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
        }

        if (registerBo == null) {
            registerBo = new RegisterBo();
        }
        try {
            List<AsocRegsResults> listAsoc =
                    registerBo.saveAssociation(useCaseConf, asocRegisterBean,
                            inputRegisterBean.getFdrid(), book.getId());

            if (listAsoc == null || listAsoc.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(
                        "Asociar Registros",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "No se ha encontrado el registro a asociar",
                                "No se ha encontrado el registro a asociar"));
            } else {

                Object[] regAsoc =
                        registerBo.getAsocReg(useCaseConf, book.getId(),
                                inputRegisterBean.getFdrid());
                if (regAsoc != null) {
                    idocs = (Map<String, Idocarchhdr>) regAsoc[0];
                    asocReg = registerBo.getListAsoReg((Map<AxPK, AxSf>) regAsoc[1], idocs);
                    asocRegPrim = registerBo.getAsoReg((Map<AxPK, AxSf>) regAsoc[2], idocs);
                    FacesContext.getCurrentInstance().addMessage(
                            "Asociar Registros",
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Se ha asociado el registro correctamente",
                                    "Se ha asociado el registro correctamente"));
                }
            }
        } catch (RPRegisterException e) {
            LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: "
                    + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
            Utils.redirectToErrorPage(e, null, null);
        } catch (RPGenericException rpGenericException) {
            LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: "
                    + rpGenericException.getCode().getCode() + " . Mensaje: "
                    + rpGenericException.getShortMessage());
            Utils.redirectToErrorPage(rpGenericException, null, null);
        }
    }

    /**
     * Redirecciona a la página de intercambioRegistral.
     */
    public void goRegInterchangeForm() {
        deleteSession("reportsLabelAction");

        if (comprobarFormatoAnexos()) {
            if (comprobarInteresadosIR()) {
                if (comprobarSopFisicoIR()) {
                    Map<String, Object> parameter = new HashMap<String, Object>();
                    parameter.put("registerId", inputRegisterBean.getFdrid());
                    if (inputRegisterBean.getFld504() != null) {
                        parameter.put("docFisica", 1);
                    }
                    if (inputRegisterBean.getFld505() != null) {
                        parameter.put("docFisica", 2);
                    }
                    if (inputRegisterBean.getFld506() != null) {
                        parameter.put("docFisica", 3);
                    }
                    Utils.navigate(parameter, false, "searchDestinationRegInterchange.xhtml");
                } else {
                    FacesContext
                            .getCurrentInstance()
                            .addMessage(
                                    "Intercambio Registral",
                                    new FacesMessage(
                                            FacesMessage.SEVERITY_ERROR,
                                            "No se puede enviar el registro. El campo Documentación Soporte es obligatorio para los envíos por intercambio registral.",
                                            "No se puede enviar el registro. El campo Documentación Soporte es obligatorio para los envíos por intercambio registral."));
                }

            } else {
                FacesContext
                        .getCurrentInstance()
                        .addMessage(
                                "Intercambio Registral",
                                new FacesMessage(
                                        FacesMessage.SEVERITY_ERROR,
                                        "No se puede enviar el registro. No dispone de interesados válidos. Recuerde que debe guardar el registro si ha realizado cambios.",
                                        "No se puede enviar el registro. No dispone de interesados válidos. Recuerde que debe guardar el registro si ha realizado cambios."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    "Intercambio Registral",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                            "No se puede enviar el registro. Contiene documentos adjuntos en formatos .zip, .gzip o .rar.",
//                            "No se puede enviar el registro. Contiene documentos adjuntos en formatos .zip, .gzip o .rar."));
                    		"No se puede enviar el registro. Para los documentos adjuntos sólo se permiten los formatos .pptx, .jpg, .jpeg, .txt, .xml, .xsig, .xlsx, .odg, .odt, .ods, .pdf, .odp, .png, .svg, .tiff, .docx, .rtf.",
                            "No se puede enviar el registro. Para los documentos adjuntos sólo se permiten los formatos .pptx, .jpg, .jpeg, .txt, .xml, .xsig, .xlsx, .odg, .odt, .ods, .pdf, .odp, .png, .svg, .tiff, .docx, .rtf."));                    		
        }
    }

    /**
     * Comprobar si el registro tiene interesados y si son válidos.
     *
     * @return true si los interesados son válidos.
     */
    private boolean comprobarInteresadosIR() {
        init();
        boolean result = true;
        InterestedBo interestedBo = new InterestedBo();
        List<Interesado> interadosCon =
                interestedBo.getAllInterested(book.getId(), inputRegisterBean.getFdrid(),
                        useCaseConf);
        if (interadosCon == null || interadosCon.isEmpty()) {
            result = false;
        } else {
            for (Interesado inter : interadosCon) {
                if (inter.getDocIndentidad() == null || "".equals(inter.getDocIndentidad())
                        || inter.getTipodoc() == null || "".equals(inter.getTipodoc())) {
                    result = false;
                } else {
                    if (inter.getRepresentante() != null
                            && !"N".equals(inter.getRepresentante().getTipo())) {
                        if (inter.getRepresentante().getDocIndentidad() == null
                                || "".equals(inter.getRepresentante().getDocIndentidad())
                                || inter.getRepresentante().getTipodoc() == null
                                || "".equals(inter.getRepresentante().getTipodoc())) {
                            result = false;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Comprobar si el registro tiene interesados y si son válidos.
     *
     * @return true si los interesados son válidos.
     */
    private boolean comprobarSopFisicoIR() {
        init();
        boolean result = true;
        if (inputRegisterBean.getFld504() == null && inputRegisterBean.getFld505() == null
                && inputRegisterBean.getFld506() == null) {
            result = false;
        }
        return result;
    }

    /**
     * Comprobar si el registro tiene documentos con formato admitidos.
     *
     * @return true si los documentos cumple los requisitos.
     */
    private boolean comprobarFormatoAnexos() {
        init();


        if (listDocuments == null) {
            getRegisterAttachedDocuments();
        }
        return validateAttachedDocuments();
    }


    /**
     * Obtiene los documentos asociados a un registro.
     */
    public boolean validateAttachedDocuments() {
        boolean result = true;

        for (Axdoch doch : listDocuments) {

            List<Axpageh> pages = doch.getPages();
            for (Axpageh page : pages) {
                String extension = FolderFileSession.getExtensionFile(doch.getName(), book.getId().toString(), doch.getFdrid(), page.getPageId(), useCaseConf.getEntidadId());
                if (ContentTypeIRPermitEnum.getIfPresentExt(extension.toUpperCase()) == null) {
                    LOG.info("Content Type del documento no permitido para intercambio registral " + extension);
                    result = false;
                }

                String mimeType = page.getMimeType();

                if ((mimeType != null) && result) {
                    if (ContentTypeIRPermitEnum.getIfPresentMime(mimeType.toUpperCase()) == null) {
                        LOG.info("Content Type del documento no permitido para intercambio registral " + mimeType);
                        result = false;
                    }
                }
            }
            if (!result) {
                break;
            }
        }
        return result;
    }

    /**
     * Método que distribuye de los elementos seleccionados.
     *
     * @throws SessionException errror de sessión.
     */
    public void distribuir() throws SessionException {
        init();
        deleteSession("reportsLabelAction");
        ScrRegstate book =
                (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
        if (book == null) {
            throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
        }
        if (distributionBo == null) {
            distributionBo = new DistributionBo();
        }
        try {

            Integer userId = selectdestinoRedisDepartamentos.getId();

            List<Integer> ids = new ArrayList<Integer>();
            ids.add(inputRegisterBean.getFdrid());

            String result="";
            if(origen=="DI") {
                distributionBo.redistributionIds(useCaseConf, ids, ISDistribution.STATE_PENDIENTE, motivoDistribucion, typeDestinoRedis, userId);
            }
            else {
                result =
                        distributionBo.createDistribution(useCaseConf, ids, motivoDistribucion,
                                typeDestinoRedis, userId, book.getId());
            }
            // distribucion
            distributionBo = new DistributionBo();
            listHistDistribution =
                    distributionBo.getHistDistribution(useCaseConf, book.getId(),
                            inputRegisterBean.getFdrid());
            if ("".equals(result.trim())) {
                result = "Se han distribuido los registros correctamente.";
                canDistr = false;
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formLink");
            }
            result = result.replaceAll(";", "<br/>");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, result, result));
            RequestContext.getCurrentInstance().update("messagesform:messages");
        } catch (RPDistributionException e) {
            LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
            Utils.redirectToErrorPage(e, null, null);
        } catch (RPGenericException e) {
            LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
            Utils.redirectToErrorPage(e, null, null);
        } catch (BookException bookException) {
            LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, bookException);
            Utils.redirectToErrorPage(null, bookException, null);
        } catch (SessionException sessionException) {
            LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, sessionException);
            Utils.redirectToErrorPage(null, sessionException, null);
        } catch (ValidationException validationException) {
            LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, validationException);
            Utils.redirectToErrorPage(null, validationException, null);
        }
    }

    /**
     * Adjunta un documento al registro.
     */
    public void upload() {
        LOG.info("Adjuntando documento " + documentoElectronico.getName());
        JSONParser parser = new JSONParser();
        if (registerDocumentsBo == null) {
            registerDocumentsBo = new RegisterDocumentsBo();
        }
        deleteSession("reportsLabelAction");
        try {
            boolean exist = false;
            for (Axdoch doc : listDocuments) {
                if (doc.getName().equals(documentoElectronico.getName())) {
                    exist = true;
                }
            }
            if (!exist) {
                if (documentoElectronico.getName().length() <= 80) {
                    AxSf registerData = Utils.mappingInputRegisterToAxSF(inputRegisterBean);
                    // identificadorDocumentoElectronicoAnexoVO
                    IdentificadorDocumentoElectronicoAnexoVO identificadorDocumentoElectronicoAnexoVO =
                            new IdentificadorDocumentoElectronicoAnexoVO();
                    identificadorDocumentoElectronicoAnexoVO.setIdLibro(Long.parseLong(String
                            .valueOf(book.getIdocarchhdr().getId())));
                    identificadorDocumentoElectronicoAnexoVO.setIdRegistro(Long.parseLong(String
                            .valueOf(inputRegisterBean.getFdrid())));
                    documentoElectronico.setId(identificadorDocumentoElectronicoAnexoVO);

                    // CODE NAME
                    String codeName = ("codeName" + documentoElectronico.getName());
                    codeName = StringUtils.abbreviate(codeName, 21);
                    documentoElectronico.setCodeName(codeName);

                    // extension
                    String extension =
                            documentoElectronico.getName().substring(
                                    documentoElectronico.getName().lastIndexOf(".") + 1,
                                    documentoElectronico.getName().length());
                    documentoElectronico.setExtension(extension);

                    // contenido
                    DocumentoElectronicoAnexoContenidoVO contenido =
                            new DocumentoElectronicoAnexoContenidoVO();
                    // Contenido del fichero

                    JSONArray arrayContent = (JSONArray) parser.parse(contentDocument);

                    LinkedList<byte[]> blocksContent = new LinkedList<byte[]>();
                    JSONObject blockContent = null;
                    for (int i = 0; i < arrayContent.size(); i++) {
                        blockContent = (JSONObject) arrayContent.get(i);
                        blocksContent.add(Base64.decodeBase64((String) blockContent.get("block")));
                    }
                    ByteArrayOutputStream osContent = new ByteArrayOutputStream();
                    for (byte[] b : blocksContent) {
                        osContent.write(b, 0, b.length);
                    }
                    byte[] content = osContent.toByteArray();
                    osContent.close();

                    // ANTIVIRUS
          /*  try {
            antivirusConnector =
				(AntivirusConnector) RegistroPresencialMSSSIWebSpringApplicationContext
					.getInstance().getApplicationContext()
					.getBean("antivirusConnector");
			antivirusConnector.scan(documentoElectronico.getName(), content);
		    }
		    catch (RPGenericException e) {
			LOG.error(ErrorConstants.SCAN_FILE_ANTIVIRUS_CALL_ERROR_MESSAGE
				+ ". Código: " + e.getCode().getCode() + " . Mensaje: "
				+ e.getMessage());
			Utils.redirectToErrorPage(e, null, null);
		    }*/

                    contenido.setContent(content);

                    documentoElectronico.setContenido(contenido);

                    // TIPO MIME
                    // MimetypesFileTypeMap mimeTypesMap = new
                    // MimetypesFileTypeMap();
                    // documentoElectronico.setMimeType(mimeTypesMap.getContentType());
                    try {
                        if (ContentTypeEnum.valueOf(extension.toUpperCase()) != null
                                && ContentTypeEnum.valueOf(extension.toUpperCase())
                                .getContentType().length() <= 20) {
                            documentoElectronico.setMimeType(ContentTypeEnum.valueOf(
                                    extension.toUpperCase()).getContentType());
                        }
                    } catch (Exception e) {
                    }
                    // TIPO DOCUMENTO
                    documentoElectronico
                            .setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.DOCUMENTO_ADJUNTO_FORMULARIO);

                    if (validateDocument != null) {
                        documentoElectronico.setTipoValidez(TipoValidezDocumentoAnexoEnumVO
                                .getEnum(Integer.valueOf(validateDocument.substring(1, 2))));
                    }

                    // datos hash
                    DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumento =
                            new DocumentoElectronicoAnexoDatosFirmaVO();

                    String hashDocumento =
                            UtilsHash.getBase64Sring(Hasher.generateHash(content, HashType.SHA1));
                    datosFirmaDocumento.setHash(hashDocumento);
                    datosFirmaDocumento.setHashAlg(HashType.SHA1.getValue());
                    documentoElectronico.setDatosFirma(datosFirmaDocumento);
                    // FIRMAS
                    List<DocumentoElectronicoAnexoVO> firmas =
                            new ArrayList<DocumentoElectronicoAnexoVO>();
                    DocumentoElectronicoAnexoVO firmaDocumento = new DocumentoElectronicoAnexoVO();
                    if (signDocument != null && !"".equals(signDocument)) {
                        IdentificadorDocumentoElectronicoAnexoVO idFima =
                                new IdentificadorDocumentoElectronicoAnexoVO();

                        idFima.setIdLibro(identificadorDocumentoElectronicoAnexoVO.getIdLibro());
                        idFima.setIdRegistro(identificadorDocumentoElectronicoAnexoVO
                                .getIdRegistro());
                        firmaDocumento.setId(idFima);
                        String codeNameFirma = "codeNameSign" + documentoElectronico.getName();
                        codeNameFirma = StringUtils.abbreviate(codeNameFirma, 21);
                        firmaDocumento.setCodeName(codeNameFirma);

                        // contenido
                        DocumentoElectronicoAnexoContenidoVO contenidoFirma =
                                new DocumentoElectronicoAnexoContenidoVO();

                        JSONArray arraySignContent = (JSONArray) parser.parse(signDocument);

                        LinkedList<byte[]> blocksSignContent = new LinkedList<byte[]>();
                        JSONObject blockSignContent = null;
                        for (int i = 0; i < arraySignContent.size(); i++) {
                            blockSignContent = (JSONObject) arraySignContent.get(i);
                            blocksSignContent.add(Base64.decodeBase64((String) blockSignContent
                                    .get("block")));
                        }
                        ByteArrayOutputStream osSignContent = new ByteArrayOutputStream();
                        for (byte[] b : blocksSignContent) {
                            osSignContent.write(b, 0, b.length);
                        }
                        byte[] signcontent = osSignContent.toByteArray();
                        osSignContent.close();
                        contenidoFirma.setContent(signcontent);
                        firmaDocumento.setContenido(contenidoFirma);
                        // extension firma
                        String extensionFirma = "csig";
                        firmaDocumento.setExtension(extensionFirma);

                        // datos hash
                        DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumentoFirma =
                                new DocumentoElectronicoAnexoDatosFirmaVO();
                        String hashDocumentoFirma =
                                UtilsHash.getBase64Sring(Hasher.generateHash(signcontent,
                                        HashType.SHA1));
                        datosFirmaDocumentoFirma.setHash(hashDocumentoFirma);
                        datosFirmaDocumentoFirma.setHashAlg(HashType.SHA1.getValue());
                        firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
                        // NOMBRE FICHERO FIRMA

                        String nameFirma = "FIRMA.csig";
                        firmaDocumento.setName(nameFirma);
                        firmaDocumento
                                .setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.FICHERO_TECNICO);
                        firmaDocumento.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.ORIGINAL);

                        if (certificateDocument != null) {
                            datosFirmaDocumentoFirma.setCertificado(certificateDocument);
                        }
            /*
             * if (anexoVerificado.getFirma().getFirma() != null){
			 * String firma=new
			 * String(anexoVerificado.getFirma().getFirma());
			 * datosFirmaDocumentoFirma.setFirma(firma); }
			 */
                        firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
                        firmas.add(firmaDocumento);
                        documentoElectronico.setFirmas(firmas);

                    }
            /*
             * else { FacesContext.getCurrentInstance().addMessage(
		     * null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
		     * "No se ha podido adjuntar el documento. Extensión no válida: "
		     * + documentoElectronico.getName(),
		     * "No se ha podido adjuntar el documento. Extensión no válida: "
		     * + documentoElectronico.getName())); }
		     */
                    UsuarioVO usuario =
                            (UsuarioVO) FacesContext.getCurrentInstance().getExternalContext()
                                    .getSessionMap().get("USERVO");
                    DocumentoElectronicoAnexoVO documentSaved =
                            registerDocumentsBo.saveDocuments(useCaseConf.getSessionID(),
                                    book.getId(), inputRegisterBean.getFdrid(),
                                    documentoElectronico, registerData, useCaseConf.getLocale(),
                                    useCaseConf.getEntidadId(), usuario);
                    if (documentSaved != null) {
                        getRegisterAttachedDocuments();
                        checkSize();
                        FacesContext.getCurrentInstance()
                                .addMessage(
                                        null,
                                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                "Se ha adjuntado el documento "
                                                        + documentoElectronico.getName()
                                                        + " correctamente",
                                                "Se ha adjuntado el documento "
                                                        + documentoElectronico.getName()
                                                        + " correctamente"));
                    }
                } else {
                    FacesContext
                            .getCurrentInstance()
                            .addMessage(
                                    null,
                                    new FacesMessage(
                                            FacesMessage.SEVERITY_ERROR,
                                            "No se ha podido adjuntar el documento. El nombre del fichero no puede tener mas de 80 caracteres: "
                                                    + documentoElectronico.getName(),
                                            "No se ha podido adjuntar el documento. El nombre del fichero no puede tener mas de 80 caracteres: "
                                                    + documentoElectronico.getName()));
                }
            } else {
                ValidationBo.showDialog("Adjuntar", new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "",
                        "No se ha podido adjuntar el documento. El nombre del fichero ya existe"));
            }
        } catch (RPRegisterException e) {
            LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: "
                    + e.getCode().getCode() + " . Mensaje: " + e.getMessage());
            Utils.redirectToErrorPage(e, null, null);
        } catch (ParseException e) {
            LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Mensaje: "
                    + e.getMessage());
        } catch (IOException e) {
            LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Mensaje: "
                    + e.getMessage());
        } finally {
            documentoElectronico = new DocumentoElectronicoAnexoVO();
            validateDocument = null;
            contentDocument = null;
            signDocument = null;
            certificateDocument = null;

        }
    }

    /**
     * Alterna entre paneles del acordeón de la página mediante el evento
     * tabChangeEvent de Ajax.
     *
     * @param tabChangeEvent Evento de cambio de panel.
     */
    public void changeListener(TabChangeEvent tabChangeEvent) {
        deleteSession("reportsLabelAction");
        String tabId = tabChangeEvent.getTab().getId();
        if ("inputRegisterData".equals(tabId)) {
            this.setMyCurrentTab(0);
            if (inputRegisterBean == null) {
                loadInputRegister();
            }
        } else if ("inputRegisterAttachedDocuments".equals(tabId)) {
            this.setMyCurrentTab(1);
            if (listDocuments == null) {
                getRegisterAttachedDocuments();
            }
        } else if ("inputRegisterHistorical".equals(tabId)) {
            this.setMyCurrentTab(2);
            // if (listUpdates == null || listUpdates.size() == 0 ) {
            getRegisterHistoricalUpdates();
            // }
        } else if ("inputRegisterDistributionHistorical".equals(tabId)) {
            this.setMyCurrentTab(NUMBER3);
            if (listHistDistribution == null) {
                getRegisterHistDistribution();
            }
        } else if ("inputRegisterInterchangeHistorical".equals(tabId)) {
            this.setMyCurrentTab(NUMBER4);
            if (listHistInputInterchange == null && listHistOutputInterchange == null) {
                getRegisterHistInputAndOutputInterchange();
            }
        } else if ("inputRegisterAssociatedRegisters".equals(tabId)) {
            this.setMyCurrentTab(NUMBER5);
            if (asocReg == null) {
                getRegisterAssociatedRegisters();
            }
        }
    }

    /**
     * borra el documento del registro.
     */
    public void deleteDocument() {
        if (registerDocumentsBo == null) {
            registerDocumentsBo = new RegisterDocumentsBo();
        }
        try {
            deleteSession("reportsLabelAction");
            LOG.info("borramos el documento registro " + inputRegisterBean.getFdrid());
            if (selectDocument != null) {
                registerDocumentsBo.deleteDocument(useCaseConf, book.getId(),
                        inputRegisterBean.getFdrid(), selectDocument.getId().intValue());
                getRegisterAttachedDocuments();
            }
        } catch (RPRegisterException rPRegisterException) {
            LOG.error(ErrorConstants.DELETE_REGISTER_DOCUMENTS_ERROR_MESSAGE, rPRegisterException);
        }
    }

    /**
     * Obtiene el valor del parámetro readOnly.
     *
     * @return readOnly valor del campo a obtener.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Guarda el valor del parámetro readOnlyIR.
     *
     * @param readOnlyIR valor del campo a guardar.
     */
    public void setReadOnlyIR(boolean readOnlyIR) {
        this.readOnlyIR = readOnlyIR;
    }

    /**
     * Obtiene el valor del parámetro readOnlyIR.
     *
     * @return readOnlyIR valor del campo a obtener.
     */
    public boolean isReadOnlyIR() {
        return readOnlyIR;
    }

    /**
     * Guarda el valor del parámetro readOnly.
     *
     * @param readOnly valor del campo a guardar.
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Obtiene el valor del parámetro registerCopy.
     *
     * @return registerCopy valor del campo a obtener.
     */
    public Integer getRegisterCopy() {
        return registerCopy;
    }

    /**
     * Guarda el valor del parámetro registerCopy.
     *
     * @param registerCopy valor del campo a guardar.
     */
    public void setRegisterCopy(Integer registerCopy) {
        this.registerCopy = registerCopy;
    }

    /**
     * Obtiene el valor del parámetro inputRegisterBo.
     *
     * @return inputRegisterBo valor del campo a obtener.
     */
    public InputRegisterBo getInputRegisterBo() {
        return inputRegisterBo;
    }

    /**
     * Guarda el valor del parámetro inputRegisterBo.
     *
     * @param inputRegisterBo valor del campo a guardar.
     */
    public void setInputRegisterBo(InputRegisterBo inputRegisterBo) {
        this.inputRegisterBo = inputRegisterBo;
    }

    /**
     * Obtiene el valor del parámetro inputRegisterBean.
     *
     * @return inputRegisterBean valor del campo a obtener.
     */
    public InputRegisterBean getInputRegisterBean() {
        return inputRegisterBean;
    }

    /**
     * Guarda el valor del parámetro inputRegisterBean.
     *
     * @param inputRegisterBean valor del campo a guardar.
     */
    public void setInputRegisterBean(InputRegisterBean inputRegisterBean) {
        this.inputRegisterBean = inputRegisterBean;
    }

    /**
     * Obtiene el valor del parámetro listUpdates.
     *
     * @return listUpdates valor del campo a obtener.
     */
    public List<UpdHisFdrResults> getListUpdates() {
        return listUpdates;
    }

    /**
     * Guarda el valor del parámetro listUpdates.
     *
     * @param listUpdates valor del campo a guardar.
     */
    public void setListUpdates(List<UpdHisFdrResults> listUpdates) {
        this.listUpdates = listUpdates;
    }

    /**
     * Obtiene el valor del parámetro listHistDistribution.
     *
     * @return listHistDistribution valor del campo a obtener.
     */
    public List<DtrFdrResults> getListHistDistribution() {
        return listHistDistribution;
    }

    /**
     * Guarda el valor del parámetro listHistDistribution.
     *
     * @param listHistDistribution valor del campo a guardar.
     */
    public void setListHistDistribution(List<DtrFdrResults> listHistDistribution) {
        this.listHistDistribution = listHistDistribution;
    }

    /**
     * Obtiene el valor del parámetro origen.
     *
     * @return origen valor del campo a obtener.
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Guarda el valor del parámetro origen.
     *
     * @param origen valor del campo a guardar.
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
     * Obtiene el valor del parámetro asocReg.
     *
     * @return asocReg valor del campo a obtener.
     */
    public List<AxSf> getAsocReg() {
        return asocReg;
    }

    /**
     * Guarda el valor del parámetro asocReg.
     *
     * @param asocReg valor del campo a guardar.
     */
    public void setAsocReg(List<AxSf> asocReg) {
        this.asocReg = asocReg;
    }

    /**
     * Obtiene el valor del parámetro asocRegPrim.
     *
     * @return asocRegPrim valor del campo a obtener.
     */
    public AxSf getAsocRegPrim() {
        return asocRegPrim;
    }

    /**
     * Guarda el valor del parámetro asocRegPrim.
     *
     * @param asocRegPrim valor del campo a guardar.
     */
    public void setAsocRegPrim(AxSf asocRegPrim) {
        this.asocRegPrim = asocRegPrim;
    }

    /**
     * Obtiene el valor del parámetro idocs.
     *
     * @return idocs valor del campo a obtener.
     */
    public Map<String, Idocarchhdr> getIdocs() {
        return idocs;
    }

    /**
     * Guarda el valor del parámetro idocs.
     *
     * @param idocs valor del campo a guardar.
     */
    public void setIdocs(Map<String, Idocarchhdr> idocs) {
        this.idocs = idocs;
    }

    /**
     * Obtiene el valor del parámetro asocRegisterBean.
     *
     * @return asocRegisterBean valor del campo a obtener.
     */
    public AsocRegisterBean getAsocRegisterBean() {
        return asocRegisterBean;
    }

    /**
     * Guarda el valor del parámetro asocRegisterBean.
     *
     * @param asocRegisterBean valor del campo a guardar.
     */
    public void setAsocRegisterBean(AsocRegisterBean asocRegisterBean) {
        this.asocRegisterBean = asocRegisterBean;
    }

    /**
     * Obtiene el valor del parámetro listHistInputInterchange.
     *
     * @return listHistInputInterchange valor del campo a obtener.
     */
    public List<IntercambioRegistralEntradaVO> getListHistInputInterchange() {
        return listHistInputInterchange;
    }

    /**
     * Guarda el valor del parámetro listHistInputInterchange.
     *
     * @param listHistInputInterchange valor del campo a guardar.
     */
    public void setListHistInputInterchange(
            List<IntercambioRegistralEntradaVO> listHistInputInterchange) {
        this.listHistInputInterchange = listHistInputInterchange;
    }

    /**
     * Obtiene el valor del parámetro listHistOutputInterchange.
     *
     * @return listHistOutputInterchange valor del campo a obtener.
     */
    public List<IntercambioRegistralSalidaVO> getListHistOutputInterchange() {
        return listHistOutputInterchange;
    }

    /**
     * Guarda el valor del parámetro listHistOutputInterchange.
     *
     * @param listHistOutputInterchange valor del campo a guardar.
     */
    public void setListHistOutputInterchange(
            List<IntercambioRegistralSalidaVO> listHistOutputInterchange) {
        this.listHistOutputInterchange = listHistOutputInterchange;
    }

    /**
     * Obtiene el valor del parámetro listDocuments.
     *
     * @return listDocuments valor del campo a obtener.
     */
    public List<Axdoch> getListDocuments() {
        return listDocuments;
    }

    /**
     * Guarda el valor del parámetro listDocuments.
     *
     * @param listDocuments valor del campo a guardar.
     */
    public void setListDocuments(List<Axdoch> listDocuments) {
        this.listDocuments = listDocuments;
    }

    /**
     * Obtiene el valor del parámetro motivoDistribucion.
     *
     * @return motivoDistribucion valor del campo a obtener.
     */
    public String getMotivoDistribucion() {
        return motivoDistribucion;
    }

    /**
     * Guarda el valor del parámetro motivoDistribucion.
     *
     * @param motivoDistribucion valor del campo a guardar.
     */
    public void setMotivoDistribucion(String motivoDistribucion) {
        this.motivoDistribucion = motivoDistribucion;
    }

    /**
     * Obtiene el valor del parámetro selectdestinoRedisDepartamentos.
     *
     * @return selectdestinoRedisDepartamentos valor del campo a obtener.
     */
    public Iuserdepthdr getSelectdestinoRedisDepartamentos() {
        return selectdestinoRedisDepartamentos;
    }

    /**
     * Guarda el valor del parámetro selectdestinoRedisDepartamentos.
     *
     * @param selectdestinoRedisDepartamentos valor del campo a guardar.
     */
    public void setSelectdestinoRedisDepartamentos(Iuserdepthdr selectdestinoRedisDepartamentos) {
        this.selectdestinoRedisDepartamentos = selectdestinoRedisDepartamentos;
    }

    /**
     * Obtiene el valor del parámetro myCurrentTab.
     *
     * @return myCurrentTab valor del campo a obtener.
     */
    public int getMyCurrentTab() {
        return myCurrentTab;
    }

    /**
     * Guarda el valor del parámetro myCurrentTab.
     *
     * @param myCurrentTab valor del campo a guardar.
     */
    public void setMyCurrentTab(int myCurrentTab) {
        this.myCurrentTab = myCurrentTab;
    }

    /**
     * Obtiene el valor del parámetro registerNumCopy.
     *
     * @return registerNumCopy valor del campo a obtener.
     */
    public String getRegisterNumCopy() {
        return registerNumCopy;
    }

    /**
     * Guarda el valor del parámetro registerNumCopy.
     *
     * @param registerNumCopy valor del campo a guardar.
     */
    public void setRegisterNumCopy(String registerNumCopy) {
        this.registerNumCopy = registerNumCopy;
    }

    /**
     * Método que actualiza el campo origen con el valor seleccionado en la
     * búsqueda avanzada de organismos.
     */
    public void updateOrigin() {
        ScrOrg unidad = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("UNITSDIALOG") != null) {
            unidad =
                    (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                            .get("UNITSDIALOG");
            resetValues("accordion:inputRegisterDataForm:fld7");
            getInputRegisterBean().setFld7(unidad);
            selectedOriginOrg = unidad;
        }
    }

    /**
     * Método que actualiza el campo destino con el valor seleccionado en la
     * búsqueda avanzada de organismos.
     */
    public void updateDestination() {
        ScrOrg unidad = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("UNITSDIALOG") != null) {
            unidad =
                    (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                            .get("UNITSDIALOG");
            resetValues("accordion:inputRegisterDataForm:fld8");
            getInputRegisterBean().setFld8(unidad);
            selectedDestOrg = unidad;
        }
    }

    /**
     * Obtiene el valor del parámetro canDistr.
     *
     * @return canDistr valor del campo a obtener.
     */
    public boolean getCanDistr() {
        return canDistr;
    }

    /**
     * Guarda el valor del parámetro canDistr.
     *
     * @param canDistr valor del campo a guardar.
     */
    public void setCanDistr(boolean canDistr) {
        this.canDistr = canDistr;
    }

    /**
     * Redirecciona a la página de copiar desde el botón.
     */
    public void onRowSelectNavigateBottomCopy() {
        destroy1();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().clear();
        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("registerCopy", inputRegisterBean.getFdrid());
        parameter.put("registerNumCopy", inputRegisterBean.getFld1());
        Utils.navigate(parameter, false, "inputRegister.xhtml");
    }

    /**
     * Redirecciona a la página de nuevo desde el botón.
     */
    public void onRowSelectNavigateBottomNuevo() {
        clearView();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().remove("registerCopy");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().remove("registerSelect");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().remove("readOnly");
        Utils.navigate("inputRegister.xhtml");
    }

    /**
     * Comprueba que el usuario tenga permisos de tramitación de expedientes.
     */
    public void onUserPermissInitTram() {
        init();
        UserBo userBo = new UserBo();
        if(!userBo.isHasPermissInitTram(useCaseConf)) {
        	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Validación",
                    "No tiene permisos para poder crear expedientes");
        	RequestContext.getCurrentInstance().addCallbackParam("VALIDATION", true);
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    
    /**
     * initTramExpedient.
     */
    public void initTramExpedient() {
        init();
        TramConnector connector = (TramConnector) appContext.getBean("tramConnector");
        ProcessorBo tram = new ProcessorBo();
        try {
            if (validateInterestedTram()) {
                if (!tram.existExpedient(inputRegisterBean.getFld1())) {
                    connector.iniciarExpedient(inputRegisterBean, useCaseConf, book);
                    tram.insertExpedient(inputRegisterBean.getFld1());
                    showExpedient = false;
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Se ha generado un expediente relacionado con este registro",
                                    "Se ha generado un expediente relacionado con este registro"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Ya existe un expediente relacionado con este registro",
                                    "Ya existe un expediente relacionado con este registro"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Se debe informar del documento de identidad de los interesados",
                                "Se debe informar del documento de identidad de los interesados"));
            }
        } catch (RPGenericException e) {
        }
    }

    /**
     * Obtiene el valor del parámetro selectedDocSIR.
     *
     * @return selectedDocSIR valor del campo a obtener.
     */
    public String getSelectedDocSIR() {
        return selectedDocSIR;
    }

    /**
     * Guarda el valor del parámetro selectedDocSIR.
     *
     * @param selectedDocSIR valor del campo a guardar.
     */
    public void setSelectedDocSIR(String selectedDocSIR) {
        this.selectedDocSIR = selectedDocSIR;
    }

    public String getContentDocument() {
        return contentDocument;
    }

    /**
     * Guarda el valor del parámetro contentDocument.
     *
     * @param contentDocument valor del campo a guardar.
     */
    public void setContentDocument(String contentDocument) {
        this.contentDocument = contentDocument;
    }

    /**
     * Obtiene el valor del parámetro documentoElectronico.
     *
     * @return documentoElectronico valor del campo a obtener.
     */
    public DocumentoElectronicoAnexoVO getDocumentoElectronico() {
        return documentoElectronico;
    }

    /**
     * Guarda el valor del parámetro documentoElectronico.
     *
     * @param documentoElectronico valor del campo a guardar.
     */
    public void setDocumentoElectronico(DocumentoElectronicoAnexoVO documentoElectronico) {
        this.documentoElectronico = documentoElectronico;
    }

    /**
     * Obtiene el valor del parámetro validateDocument.
     *
     * @return validateDocument valor del campo a obtener.
     */
    public String getValidateDocument() {
        return validateDocument;
    }

    /**
     * Guarda el valor del parámetro validateDocument.
     *
     * @param validateDocument valor del campo a guardar.
     */
    public void setValidateDocument(String validateDocument) {
        this.validateDocument = validateDocument;
    }

    /**
     * Obtiene el valor del parámetro signDocument.
     *
     * @return signDocument valor del campo a obtener.
     */
    public String getSignDocument() {
        return signDocument;
    }

    /**
     * Guarda el valor del parámetro signDocument.
     *
     * @param signDocument valor del campo a guardar.
     */
    public void setSignDocument(String signDocument) {
        this.signDocument = signDocument;
    }

    /**
     * Obtiene el valor del parámetro certificateDocument.
     *
     * @return certificateDocument valor del campo a obtener.
     */
    public String getCertificateDocument() {
        return certificateDocument;
    }

    /**
     * Guarda el valor del parámetro certificateDocument.
     *
     * @param certificateDocument valor del campo a guardar.
     */
    public void setCertificateDocument(String certificateDocument) {
        this.certificateDocument = certificateDocument;
    }

    /**
     * Obtiene el valor del parámetro selectDocument.
     *
     * @return selectDocument valor del campo a obtener.
     */
    public Axdoch getSelectDocument() {
        return selectDocument;
    }

    /**
     * Guarda el valor del parámetro selectDocument.
     *
     * @param selectDocument valor del campo a guardar.
     */
    public void setSelectDocument(Axdoch selectDocument) {
        this.selectDocument = selectDocument;
    }

    /**
     * Obtiene el valor del parámetro listNameDocument.
     *
     * @return listNameDocument valor del campo a obtener.
     */
    public String getListNameDocument() {
        return listNameDocument;
    }

    /**
     * Guarda el valor del parámetro listNameDocument.
     *
     * @param listNameDocument valor del campo a guardar.
     */
    public void setListNameDocument(String listNameDocument) {
        this.listNameDocument = listNameDocument;
    }

    public void deleteLabel() {
        init();
        deleteSession("reportsLabelAction");
    }

    /**
     * Obtiene el valor del parámetro fileUpload.
     *
     * @return fileUpload valor del campo a obtener.
     */
    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    /**
     * Guarda el valor del parámetro fileUpload.
     *
     * @param fileUpload valor del campo a guardar.
     */
    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    /**
     * Obtiene el valor del parámetro listDepartament.
     *
     * @return listDepartament valor del campo a obtener.
     */
    public List<Iuserdepthdr> getListDepartament() {
        return listDepartament;
    }

    /**
     * Guarda el valor del parámetro listDepartament.
     *
     * @param listDepartament valor del campo a guardar.
     */
    public void setListDepartament(List<Iuserdepthdr> listDepartament) {
        this.listDepartament = listDepartament;
    }

    /**
     * Metodo que se lanza al seleccionar un organismo en el destino.
     *
     * @param event evento select.
     */
    public void onItemSelectDestinationOrg(SelectEvent event) {
        selectedDestOrg = (ScrOrg) event.getObject();
    }

    /**
     * Metodo que se lanza al cambiar un organismo en el destino.
     *
     * @param event evento change.
     */
    public void onChangeDestinationOrg(AjaxBehaviorEvent event) {
        if (((AutoComplete) event.getSource()).getItemValue() == null) {
            selectedDestOrg = null;
        } else {
            selectedDestOrg = (ScrOrg) ((AutoComplete) event.getSource()).getItemValue();
        }
    }

    /**
     * Metodo que se lanza al seleccionar un organismo en el origen.
     *
     * @param event evento select.
     */
    public void onItemSelectOriginOrg(SelectEvent event) {
        selectedOriginOrg = (ScrOrg) event.getObject();
    }

    /**
     * Metodo que se lanza al cambiar un organismo en el origen.
     *
     * @param event evento change.
     */
    public void onChangeOriginOrg(AjaxBehaviorEvent event) {
        if (((AutoComplete) event.getSource()).getItemValue() == null) {
            selectedOriginOrg = null;
        } else {
            selectedOriginOrg = (ScrOrg) ((AutoComplete) event.getSource()).getItemValue();
        }
    }

    /**
     * Gestiona el upload del fichero de la peticion.
     *
     * @param event FileUploadEvent.
     */
    public void handleFileUpload(FileUploadEvent event) {
        this.file = event.getFile();
        this.fileName = FileNameUtils.getName(event.getFile().getFileName());
        boolean exist = false;
        for (Axdoch doc : listDocuments) {
            if (doc.getName().equals(documentoElectronico.getName())) {
                exist = true;
            }
        }
        if (exist) {
            this.file = null;
            this.fileName = null;
        }
    }

    /**
     * Primer paso de firma de nueva peticion.
     */
    public void signRequestFirstStep() {
        byte[] byteArrayFile = null;

        if (file == null) {
            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se ha seleccionado ningún fichero",
                                    "No se ha seleccionado ningún fichero"));
        }
        // Comprobamos si el archivo cargado es un documento digitalizado
        if (fileBase64 != null && fileBase64.length() > 0) {
            byteArrayFile = Conversor.base64ToBytes(fileBase64);
        } else {
            byteArrayFile = this.file.getContents();
        }
        try {
        	
            MessageDigest gen = MessageDigest.getInstance("SHA-512");
            byte[] hash = gen.digest(byteArrayFile);

            DocumentArchiveResponse documentArchiveResponse =
                    PFEConnector.documentArchive(fileName + ".xsig", hash);
            if (documentArchiveResponse != null && documentArchiveResponse.getDocumentId() != null) {
                csv = documentArchiveResponse.getDocumentId();
            }
            LOG.info("CSV: " + csv);
            LOG.info("jsonServerResponse: " + this.jsonServerResponse);
            LOG.info("cerChain: " + certChain);
            ServerResponse serverResponse = new ServerResponse(this.jsonServerResponse);
            LOG.info("fin create serverResponse");
            RemoteSignBeginResponse remoteSignBeginResponse = null;
            ResponseValue responseValue = new ResponseValue();
            if (serverResponse.isError()) {
                LOG.info("ServerResponse error:" + ErrorConstants.INVALID_USER_ID_MSG);
                ServerResponse.addError(serverResponse, ErrorConstants.PF_SIGN_BEGIN_ERROR_CODE,
                        ErrorConstants.INVALID_USER_ID_MSG);
            } else {
            	
            	// Se valida el nombre del fichero para que no contenga caracteres especiales no válidos
            	if(!Utilidades.isValidNameFile(fileName)) 
            	{
            		LOG.error("El nombre asignado al fichero a anexar no es válido: " + fileName);
                    lanzarError(responseValue, serverResponse, 
               			 "El nombre del fichero no es válido.");
            	}
            	
                LOG.info("serverResponse no error");
                if (certChain != null && !"".equals(certChain)) {
                    String cerChainClean = this.certChain;
                    
                    
                    /*byte encodedCert[] = Base64.decodeBase64(cerChainClean);
                    java.io.ByteArrayInputStream inputStream  =  new java.io.ByteArrayInputStream(encodedCert);

                    java.security.cert.CertificateFactory certFactory = java.security.cert.CertificateFactory.getInstance("X.509");
                    X509Certificate cert = (X509Certificate)certFactory.generateCertificate(inputStream);
                    String cn = cert.getSubjectDN().getName();
                    String dni = Utilidades.getDni(cn);
                    
                    IUserUserHdr iuseruserhdr = SIGMServiceManager.getiUserUserHdrService().getByName(useCaseConf.getUserName());
                    UserDAO userDAO = (UserDAO) appContext.getBean("userDAO");
                    String userDni = userDAO.getUserDni(new Integer(String.valueOf(iuseruserhdr.getId())).intValue());
                    */
                    
                    // El certificado con el que se firma el documento debe ser el mismo que
                    // con el que se identificó en la aplicación.
                    PeticionValidacionType petCert = new PeticionValidacionType();
                    byte encodedCert[] = Base64.decodeBase64(cerChainClean);
                    petCert.setCertificadoBytes(encodedCert);
                    petCert.setInformacionCompleta(true);
                    petCert.setIdApp((String) appContext.getBean("IDAPP_SERVCERT"));
                    RespuestaValidacionType respCert = new ServcertWSService().verificaCertificado(petCert);
                    
                    String error = ServcertClientUtils.getErrorMsg(respCert);
                    if (StringUtils.isNotBlank(error)) 
                    {
                      LOG.error("Se ha producido un error en la validación del certificado: " + error);
                      lanzarError(responseValue, serverResponse, 
                 			 "El certificado con el que firma el documento no es válido: " + error);
                      
                    }
                    else
                    {
                    	String dni = ServcertClientUtils.getUpperPaddedNIF(respCert);
                    
                    	IUserUserHdr iuseruserhdr = SIGMServiceManager.getiUserUserHdrService().getByName(useCaseConf.getUserName());
                        UserDAO userDAO = (UserDAO) appContext.getBean("userDAO");
                        String userDni = userDAO.getUserDni(new Integer(String.valueOf(iuseruserhdr.getId())).intValue());
                    	
                        while (userDni.length() < 9) 
                        	userDni = '0' + userDni;
                        
	                    if(LOG.isInfoEnabled()) 
	                    {
	                    	LOG.info("Comprobamos el firmante ["+dni+"] con el que accede a la app [" + userDni + "]");
	                    }
	                    if(StringUtils.isBlank(dni) || StringUtils.isBlank(userDni) ||
	                    		!dni.toUpperCase().trim().equals(userDni.trim().toUpperCase()))
	                    {
	                    	 // ERROR DE VALIDACIÓN
	                    	 LOG.error("Error firma DNI usuario: " + dni.trim().toUpperCase() + " <=> DNI certificado: " + userDni.trim().toUpperCase());                 	 
	                    	 lanzarError(responseValue, serverResponse, 
	                    			 "El certificado con el que firma el documento debe ser el mismo que con el que accedió a la aplicación."); 
	                    }
                    }
                    
                    cerChainClean = this.certChain.substring(1, this.certChain.length() - 1);
                    cerChainClean = cerChainClean.replaceAll("\"", "");
                    LOG.info("cerChainClean: " + cerChainClean);
                    remoteSignBeginResponse =
                            PFEConnector.remoteSignBegin(new String[]{this.csv},
                                    cerChainClean.split(","), "INTERNALLYDETACHED",
                                    "XAdES_BES", "XAdES_AGE");
                    if (remoteSignBeginResponse != null && remoteSignBeginResponse.getErrorCode() == null) {
                        this.taskId = remoteSignBeginResponse.getTaskId();
                        LOG.info("taskId: " + this.taskId);
                        responseValue.setTaskData(new String(Base64Codec.encode(remoteSignBeginResponse.getTaskData())));
                        responseValue.setTaskId(this.taskId);
                        responseValue.setSecuredTaskData(new String(Base64Codec.encode(remoteSignBeginResponse.getSecuredTaskData())));
                    } else {
                        responseValue.setError(true);
                        responseValue.setErrorCode(remoteSignBeginResponse.getErrorCode());

                        throw new RPRegisterException(RPRegisterErrorCode.PF_SIGN_BEGIN_ERROR_CODE, ErrorConstants.PF_SIGN_BEGIN_ERROR_CODE);
                    }
                }
            }
            serverResponse.addResponseValue(responseValue);
            this.jsonServerResponse = serverResponse.toJSON().toString();
        } catch (RPRegisterException rpregisterException) {
            LOG.error(rpregisterException.getMessage(), rpregisterException);
        } catch (Exception e) {
            LOG.error("error: generico: " + e.getMessage(), e);
        }

    }

    /**
     * Segundo paso de firma de peticion.
     */
    public void signRequestSecondStep() {
        ServerResponse serverResponse = new ServerResponse(this.jsonServerResponse);

        byte[] taskData = null;

        try {

            if (!serverResponse.getStatusInfo().isError()) {
                taskData = Base64Codec.decode(serverResponse.getResponseValues().get(0).getTaskData().getBytes());
                RemoteSignEndResponse sr =
                        PFEConnector.remoteSignEnd(serverResponse.getResponseValues().get(0).getTaskId(), taskData);
                if (sr != null && sr.getErrorCode() == null) {
                    DocumentDownloadResponse documentDownloadResponse = PFEConnector.documentDownload(csv, false);
                    this.signedData = documentDownloadResponse.getFileData();
                    uploadClient();

                } else {
                    LOG.error(ErrorConstants.PF_SIGN_END_ERROR_CODE + ". " + sr.getErrorMessage());
                    throw new RPRegisterException(RPRegisterErrorCode.PF_SIGN_END_ERROR_CODE, ErrorConstants.PF_SIGN_END_ERROR_CODE);
                }

            } else {
                LOG.error(ErrorConstants.PF_SIGN_END_ERROR_CODE, serverResponse.getStatusInfo().getErrorCause());
                throw new RPRegisterException(RPRegisterErrorCode.PF_SIGN_END_ERROR_CODE, ErrorConstants.PF_SIGN_END_ERROR_CODE);
            }
            this.jsonServerResponse = serverResponse.toJSON().toString();
        } catch (RPRegisterException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    /**
     * Adjunta un documento al registro.
     */
    public void uploadClient() {
        LOG.info("Adjuntando documento ");

        if (registerDocumentsBo == null) {
            registerDocumentsBo = new RegisterDocumentsBo();
        }
        if (file != null) {
            documentoElectronico.setName(file.getFileName());
        } else {
            documentoElectronico.setName(fileName);
        }
        deleteSession("reportsLabelAction");
        try {
            boolean exist = false;
            for (Axdoch doc : listDocuments) {
                if (doc.getName().equals(documentoElectronico.getName().substring(0, documentoElectronico.getName().lastIndexOf(".")))) {
                    exist = true;
                }
            }
            if (!exist) {
                if (documentoElectronico.getName().length() <= 80) {
                    AxSf registerData = Utils.mappingInputRegisterToAxSF(inputRegisterBean);
                    // identificadorDocumentoElectronicoAnexoVO
                    IdentificadorDocumentoElectronicoAnexoVO identificadorDocumentoElectronicoAnexoVO =
                            new IdentificadorDocumentoElectronicoAnexoVO();
                    identificadorDocumentoElectronicoAnexoVO.setIdLibro(Long.parseLong(String
                            .valueOf(book.getIdocarchhdr().getId())));
                    identificadorDocumentoElectronicoAnexoVO.setIdRegistro(Long.parseLong(String
                            .valueOf(inputRegisterBean.getFdrid())));
                    documentoElectronico.setId(identificadorDocumentoElectronicoAnexoVO);

                    // CODE NAME
                    String codeName = ("codeName" + documentoElectronico.getName());
                    codeName = StringUtils.abbreviate(codeName, 21);
                    documentoElectronico.setCodeName(codeName);

                    // extension
                    String extension =
                            documentoElectronico.getName().substring(
                                    documentoElectronico.getName().lastIndexOf(".") + 1,
                                    documentoElectronico.getName().length());
                    documentoElectronico.setExtension(extension);

                    // contenido
                    DocumentoElectronicoAnexoContenidoVO contenido =
                            new DocumentoElectronicoAnexoContenidoVO();
                    // Contenido del fichero


                    // ANTIVIRUS
		    /*try {
			antivirusConnector =
				(AntivirusConnector) RegistroPresencialMSSSIWebSpringApplicationContext
					.getInstance().getApplicationContext()
					.getBean("antivirusConnector");
			if (file != null){
			    antivirusConnector.scan(documentoElectronico.getName(), file.getContents());
			}else {
			    antivirusConnector.scan(documentoElectronico.getName(), Conversor.base64ToBytes(fileBase64));
			}
		    }
		    catch (RPGenericException e) {
			LOG.error(ErrorConstants.SCAN_FILE_ANTIVIRUS_CALL_ERROR_MESSAGE
				+ ". Código: " + e.getCode().getCode() + " . Mensaje: "
				+ e.getMessage());
			Utils.redirectToErrorPage(e, null, null);
		    }*/
                    if (file != null) {
                        contenido.setContent(file.getContents());
                    } else {
                        contenido.setContent(Conversor.base64ToBytes(fileBase64));
                    }
                    documentoElectronico.setContenido(contenido);


                    try {
                        if (ContentTypeEnum.valueOf(extension.toUpperCase()) != null
                                && ContentTypeEnum.valueOf(extension.toUpperCase())
                                .getContentType().length() <= 20) {
                            documentoElectronico.setMimeType(ContentTypeEnum.valueOf(
                                    extension.toUpperCase()).getContentType());
                        }
                    } catch (Exception e) {
                    }
                    // TIPO DOCUMENTO
                    documentoElectronico
                            .setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.DOCUMENTO_ADJUNTO_FORMULARIO);

                    if (validateDocument != null) {
                        documentoElectronico.setTipoValidez(TipoValidezDocumentoAnexoEnumVO
                                .getEnum(Integer.valueOf(validateDocument.substring(1, 2))));
                    }

                    // datos hash
                    DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumento =
                            new DocumentoElectronicoAnexoDatosFirmaVO();

                    String hashDocumento = null;
                    if (file != null) {
                        hashDocumento = UtilsHash.getBase64Sring(Hasher.generateHash(file.getContents(), HashType.SHA1));
                    } else {
                        hashDocumento = UtilsHash.getBase64Sring(Hasher.generateHash(Conversor.base64ToBytes(fileBase64), HashType.SHA1));
                    }
                    datosFirmaDocumento.setHash(hashDocumento);
                    datosFirmaDocumento.setHashAlg(HashType.SHA1.getValue());
                    documentoElectronico.setDatosFirma(datosFirmaDocumento);
                    // FIRMAS
                    List<DocumentoElectronicoAnexoVO> firmas =
                            new ArrayList<DocumentoElectronicoAnexoVO>();
                    DocumentoElectronicoAnexoVO firmaDocumento = new DocumentoElectronicoAnexoVO();
                    if (this.signedData != null && !"".equals(this.signedData)) {
                        IdentificadorDocumentoElectronicoAnexoVO idFima =
                                new IdentificadorDocumentoElectronicoAnexoVO();

                        idFima.setIdLibro(identificadorDocumentoElectronicoAnexoVO.getIdLibro());
                        idFima.setIdRegistro(identificadorDocumentoElectronicoAnexoVO
                                .getIdRegistro());
                        firmaDocumento.setId(idFima);
                        String codeNameFirma = "codeNameSign" + documentoElectronico.getName();
                        codeNameFirma = StringUtils.abbreviate(codeNameFirma, 21);
                        firmaDocumento.setCodeName(codeNameFirma);

                        // contenido
                        DocumentoElectronicoAnexoContenidoVO contenidoFirma =
                                new DocumentoElectronicoAnexoContenidoVO();

                        contenidoFirma.setContent(this.signedData);
                        firmaDocumento.setContenido(contenidoFirma);
                        // extension firma
                        String extensionFirma = "xsig";
                        firmaDocumento.setExtension(extensionFirma);

                        // datos hash
                        DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumentoFirma =
                                new DocumentoElectronicoAnexoDatosFirmaVO();
                        String hashDocumentoFirma =
                                UtilsHash.getBase64Sring(Hasher.generateHash(this.signedData,
                                        HashType.SHA1));
                        datosFirmaDocumentoFirma.setHash(hashDocumentoFirma);
                        datosFirmaDocumentoFirma.setHashAlg(HashType.SHA1.getValue());
                        firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
                        // NOMBRE FICHERO FIRMA

                        String nameFirma = "FIRMA.xsig";
                        firmaDocumento.setName(nameFirma);
                        firmaDocumento
                                .setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.FICHERO_TECNICO);
                        firmaDocumento.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.ORIGINAL);

                        if (certificateDocument != null) {
                            datosFirmaDocumentoFirma.setCertificado(certificateDocument);
                        }

                        firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
                        firmas.add(firmaDocumento);
                        documentoElectronico.setFirmas(firmas);

                    }

                    UsuarioVO usuario =
                            (UsuarioVO) FacesContext.getCurrentInstance().getExternalContext()
                                    .getSessionMap().get("USERVO");
                    DocumentoElectronicoAnexoVO documentSaved =
                            registerDocumentsBo.saveDocuments(useCaseConf.getSessionID(),
                                    book.getId(), inputRegisterBean.getFdrid(),
                                    documentoElectronico, registerData, useCaseConf.getLocale(),
                                    useCaseConf.getEntidadId(), usuario);
                    if (documentSaved != null) {
                        getRegisterAttachedDocuments();
                        checkSize();
                        FacesContext.getCurrentInstance()
                                .addMessage(
                                        null,
                                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                "Se ha adjuntado el documento "
                                                        + documentoElectronico.getName()
                                                        + " correctamente",
                                                "Se ha adjuntado el documento "
                                                        + documentoElectronico.getName()
                                                        + " correctamente"));
                    }
                } else {
                    FacesContext
                            .getCurrentInstance()
                            .addMessage(
                                    null,
                                    new FacesMessage(
                                            FacesMessage.SEVERITY_ERROR,
                                            "No se ha podido adjuntar el documento. El nombre del fichero no puede tener mas de 80 caracteres: "
                                                    + documentoElectronico.getName(),
                                            "No se ha podido adjuntar el documento. El nombre del fichero no puede tener mas de 80 caracteres: "
                                                    + documentoElectronico.getName()));
                }
            } else {
                FacesContext
                        .getCurrentInstance()
                        .addMessage(
                                null,
                                new FacesMessage(
                                        FacesMessage.SEVERITY_ERROR,
                                        "No se ha podido adjuntar el documento. El nombre del fichero ya existe",
                                        "No se ha podido adjuntar el documento. El nombre del fichero ya existe"));
            }
        } catch (RPRegisterException e) {
            LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: "
                    + e.getCode().getCode() + " . Mensaje: " + e.getMessage());
            Utils.redirectToErrorPage(e, null, null);
        } finally {
            documentoElectronico = new DocumentoElectronicoAnexoVO();
            validateDocument = null;
            contentDocument = null;
            signDocument = null;
            certificateDocument = null;
            signedData = null;
            file = null;
            fileName = null;
            readCheck = false;
            fileBase64 = null;
            fileNameDig = null;
        }
    }

    private void checkSize()
    {
        Integer totalSize=0;
        for (Axdoch doch : listDocuments) {
            totalSize=totalSize+doch.getPages().get(0).getFileSize();
        }
        if(totalSize>=15100000)
        {
            FacesContext.getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "El tamaño de la suma de todos los documentos ha sobrepasado los 15MB","El tamaño de la suma de todos los documentos ha sobrepasado los 15MB"));
        }
    }

    public void enableSign() {
        if (file == null && fileBase64 == null) {
            this.readCheck = false;
            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se ha podido adjuntar el documento. No se ha seleccionado ningún fichero",
                                    "No se ha podido adjuntar el documento. No se ha seleccionado ningún fichero"));
        }
    }

   /**
    * Método que lanza un error en caso de no utilizar el mismo certificado con el que se accede a la aplicación para firmar un documento
    * o bien este certificado no es válido. . También se puede utilizar este método para cualquier tipo de error que se produzca al anexar un documento. 
    * @param responseValue - ResponseValue responseValue
    * @param serverResponse - ServerResponse serverResponse
    * @param msg - Mensaje del error
    * @throws RPRegisterException - Excepción lanzada
    */
   protected void lanzarError(ResponseValue responseValue, ServerResponse serverResponse, String msg) throws RPRegisterException {
    	
    	RequestContext.getCurrentInstance().addCallbackParam("ERROR", true);
   	 	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de Validación",
   			 	styleErrorIni + msg + styleErrorEnd);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
   	 
        responseValue.setError(true);
        responseValue.setErrorCode(RPRegisterErrorCode.PF_SIGN_BEGIN_ERROR_CODE.getCode());
        
        serverResponse.addResponseValue(responseValue);
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setError(true);
        serverResponse.setStatusInfo(statusInfo);
        this.jsonServerResponse = serverResponse.toJSON().toString();
        
   	 	throw new RPRegisterException(RPRegisterErrorCode.PF_SIGN_BEGIN_ERROR_CODE, ErrorConstants.INVALID_USER_SING_DOCUMENTO);
    } 
    
    public void uploadFileBase64() {
        if (fileNameDig != null) {
            fileName = fileNameDig;
        }
    }

    /**
     * Obtiene el valor del parámetro selectedDestOrg.
     *
     * @return selectedDestOrg valor del campo a obtener.
     */
    public ScrOrg getSelectedDestOrg() {
        return selectedDestOrg;
    }

    /**
     * Guarda el valor del parámetro selectedDestOrg.
     *
     * @param selectedDestOrg valor del campo a guardar.
     */
    public void setSelectedDestOrg(ScrOrg selectedDestOrg) {
        this.selectedDestOrg = selectedDestOrg;
    }

    /**
     * Obtiene el valor del parámetro selectedOriginOrg.
     *
     * @return selectedOriginOrg valor del campo a obtener.
     */
    public ScrOrg getSelectedOriginOrg() {
        return selectedOriginOrg;
    }

    /**
     * Guarda el valor del parámetro selectedOriginOrg.
     *
     * @param selectedOriginOrg valor del campo a guardar.
     */
    public void setSelectedOriginOrg(ScrOrg selectedOriginOrg) {
        this.selectedOriginOrg = selectedOriginOrg;
    }

    /**
     * @return the saveInterested
     */
    public boolean isSaveInterested() {
        return saveInterested;
    }

    /**
     * @param saveInterested the saveInterested to set
     */
    public void setSaveInterested(boolean saveInterested) {
        this.saveInterested = saveInterested;
    }

    /**
     * @return the showExpedient
     */
    public boolean getShowExpedient() {
        return showExpedient;
    }

    /**
     * @param showExpedient the showExpedient to set
     */
    public void setShowExpedient(boolean showExpedient) {
        this.showExpedient = showExpedient;
    }

    public String getCertChain() {
        return certChain;
    }

    /**
     * Establece el valor de certChain.
     *
     * @param certChain Valor de certChain.
     */
    public void setCertChain(String certChain) {
        this.certChain = certChain;
    }

    /**
     * Devuelve el valor de jsonServerResponse.
     *
     * @return valor de jsonServerResponse.
     */
    public String getJsonServerResponse() {
        return jsonServerResponse;
    }

    /**
     * Establece el valor de jsonServerResponse.
     *
     * @param jsonServerResponse valor de jsonServerResponse.
     */
    public void setJsonServerResponse(String jsonServerResponse) {
        this.jsonServerResponse = jsonServerResponse;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    /**
     * @return the fileBase64
     */
    public String getFileBase64() {
        return fileBase64;
    }

    /**
     * @param fileBase64 the fileBase64 to set
     */
    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

    /**
     * Resetea valores al cancelar.
     */
    public void onCancel() {
        this.readCheck = false;
    }

    /**
     * Retorna el valor del check de documentos leidos.
     *
     * @return Valor del check de documentos leidos.
     */
    public boolean isReadCheck() {
        return readCheck;
    }

    /**
     * Establece el valor del check de documentos leidos.
     *
     * @param readCheck Valor del check de documentos leidos.
     */
    public void setReadCheck(boolean readCheck) {
        this.readCheck = readCheck;
    }

    /**
     * @return the fileNameDig
     */
    public String getFileNameDig() {
        return fileNameDig;
    }

    /**
     * @param fileNameDig the fileNameDig to set
     */
    public void setFileNameDig(String fileNameDig) {
        this.fileNameDig = fileNameDig;
    }

    public boolean isEnableCompulsaValue() {
        return enableCompulsaValue;
    }

    public void setEnableCompulsaValue(boolean enableCompulsaValue) {
        this.enableCompulsaValue = enableCompulsaValue;
    }

	/**
	 * @return the procedureDestination
	 */
	public String getProcedureDestination() {
		return procedureDestination;
	}

	/**
	 * @param procedureDestination the procedureDestination to set
	 */
	public void setProcedureDestination(String procedureDestination) {
		this.procedureDestination = procedureDestination;
	}
}