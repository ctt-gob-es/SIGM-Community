/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesdoc.Idocarchhdr;
import com.ieci.tecdoc.common.invesdoc.Iuserdepthdr;
import com.ieci.tecdoc.common.invesdoc.Iusergrouphdr;
import com.ieci.tecdoc.common.invesdoc.Iuseruserhdr;
import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxPK;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.DtrFdrResults;
import com.ieci.tecdoc.common.isicres.UpdHisDocFdrResults;
import com.ieci.tecdoc.common.isicres.UpdHisFdrResults;
import com.ieci.tecdoc.common.utils.ISicresQueries;
import com.ieci.tecdoc.isicres.usecase.book.xml.AsocRegsResults;
import com.ieci.tecdoc.utils.HibernateUtil;

import es.dipucr.sgm.registropresencial.actions.TercerosAction;
import es.dipucr.sgm.registropresencial.beans.CompulsaBean;
import es.dipucr.sgm.registropresencial.beans.DocumentoBean;
import es.dipucr.sgm.registropresencial.beans.EscanerBean;
import es.dipucr.sgm.registropresencial.bussinessobject.CompulsaBo;
import es.dipucr.sgm.registropresencial.bussinessobject.DocumentoBo;
import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.api.business.manager.impl.RegistroManagerImpl;
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
import es.msssi.sgm.registropresencial.arboldocumentos.Document;
import es.msssi.sgm.registropresencial.beans.AsocRegisterBean;
import es.msssi.sgm.registropresencial.beans.ContentTypeEnum;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.businessobject.DistributionBo;
import es.msssi.sgm.registropresencial.businessobject.InputRegisterBo;
import es.msssi.sgm.registropresencial.businessobject.InterestedBo;
import es.msssi.sgm.registropresencial.businessobject.RegInterchangeBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterDocumentsBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPDistributionException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPInputRegisterException;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.utils.UtilsHash;
import es.msssi.sgm.registropresencial.validations.ValidationBo;
import es.msssi.sgm.registropresencial.validations.ValidationListBo;


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
	private InputRegisterBo inputRegisterBo;
	private RegisterBo registerBo;
	private RegisterDocumentsBo registerDocumentsBo;
	private InputRegisterBean inputRegisterBean;
	private ScrRegstate book;
	private Integer registerCopy;
	private String registerNumCopy;
	private List<UpdHisFdrResults> listUpdates;
	private List<UpdHisDocFdrResults> listDocumentUpdates;
	private List<DtrFdrResults> listHistDistribution;
	private List<IntercambioRegistralEntradaVO> listHistInputInterchange;
	private List<IntercambioRegistralSalidaVO> listHistOutputInterchange;
	private List<Axdoch> listDocuments;
	private RegInterchangeBo regInterchangeBo = null;

	// DISTRIBUCION
	/** tipo de destino seleccionado en la distribución. */
	private int typeDestinoRedis = 2;
	/** Motivo de la distribución. */
	private String motivoDistribucion = null;
	
	/** departamento seleccionado en la distribución. */
	private Iuseruserhdr selectdestinoRedisUsuarios = null;
	/** departamento seleccionado en la distribución. */
	private Iuserdepthdr selectdestinoRedisDepartamentos = null;
	/** grupo seleccionado en la distribución. */
	private Iusergrouphdr selectdestinoRedisGrupos = null;

	/** Implementación de las funcionalidades de distribución. */
	private DistributionBo distributionBo;
	
	/** lista de departamentos para la distribución. */
	private List<Iuseruserhdr> listUsuarios = null;
	/** lista de departamentos para la distribución. */
	private List<Iuserdepthdr> listDepartament = null;
	/** lista de departamentos para la distribución. */
	private List<Iusergrouphdr> listGrupos = null;
	
	/**
	 * Origen del formulario. B:Búsqueda de registros D:Distribución
	 * IR:Intercambio Registral salida I: Intercambio Registral entrada
	 * */
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
	private Axpageh selectPage;	
	private String listNameDocument = null;	

	private TercerosAction tercerosAction;

	private String entidadId = useCaseConf.getEntidadId();
	
	private DocumentoBean documentoBean;
	private CompulsaBean compulsaBean;
	private EscanerBean escanerBean;
	
	/**
	 * Constructor. Valida el registro.
	 * 
	 * @throws SessionException
	 *             si ha habido un problema de sesión.
	 * @throws ValidationException
	 *             Si ha habido un problema de validación de parámetros.
	 */
	public InputRegisterAction() throws SessionException, ValidationException {
		
		init();
		
		deleteSession("reportsLabelAction");
		book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
		
		if (book == null) {
			throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
		}
		try {
			validateIdBook(book.getIdocarchhdr().getId(), useCaseConf);
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.GET_INFORMATION_BOOK_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
		}
		if (regInterchangeBo == null) {
			regInterchangeBo = new RegInterchangeBo();
		}
		if (facesContext.getExternalContext().getFlash().get("registerSelect") != null) {
			inputRegisterBean = new InputRegisterBean();
			inputRegisterBean.setFdrid((Integer) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("registerSelect"));
			
			if (facesContext.getExternalContext().getFlash().get("distribucion") != null) {
				origen = "D";
			} else if (facesContext.getExternalContext().getFlash().get("intercambio") != null) {
				origen = "IR";
			} else if (facesContext.getExternalContext().getFlash().get("intercambioR") != null) {
				origen = "I";
			}
		}
		
		/* EDICION O CONSULTA */
		if (inputRegisterBean != null && inputRegisterBean.getFdrid() != null) {
			
			validateRegister(useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid());
			loadInputRegister();
			
		} else {
			/* COPIAR */
			if (facesContext.getExternalContext().getFlash().get("registerCopy") != null) {

				registerCopy = ((Integer) facesContext.getExternalContext().getFlash().get("registerCopy"));
				registerNumCopy = ((String) facesContext.getExternalContext().getFlash().get("registerNumCopy"));

				try {
					copyInputRegister();
				} catch (SessionException sessionException) {
					LOG.error(ErrorConstants.COPY_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
					Utils.redirectToErrorPage(null, sessionException, null);
				}
			}
		}
		
		setTercerosAction(new TercerosAction());
		
		if(null == documentoBean){
			iniciaDocumentoBean();
		}
		if(null == compulsaBean){
			iniciaCompulsaBean();
		}
		if(null == escanerBean){
			iniciaEscanerBean();
		}
	}
	
	private void iniciaDocumentoBean(){
		
		documentoBean = new DocumentoBean();
		
		documentoBean.setUseCaseConf(useCaseConf);
		documentoBean.setRegisterBean(inputRegisterBean);
		documentoBean.setBookId(book.getIdocarchhdr().getId());
		documentoBean.setEntidadId(entidadId);
		
		actualizaArbol();
	}

	private void iniciaCompulsaBean(){
		
		compulsaBean = new CompulsaBean();
		
		compulsaBean.setRegisterBean(inputRegisterBean);
		compulsaBean.setUseCaseConf(useCaseConf);
		
		compulsaBean.setBookId(book.getIdocarchhdr().getId());
		compulsaBean.setSessionId(((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getId());
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		compulsaBean.setUrlServer(request.getScheme() + "://" + request.getServerName() +":" + request.getServerPort() + request.getContextPath());	
		
		UsuarioVO usuario = (UsuarioVO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("USERVO");
		compulsaBean.setNombreFirmante(usuario.getFullName());
		
		Transaction tran = null;
        try {
            Session session = HibernateUtil.currentSession(entidadId);
			tran = session.beginTransaction();
		
			List<?> list = ISicresQueries.getUserUserHdrByName(session, useCaseConf.getUserName());

            if (list.size() > 0) {
            	Iuseruserhdr user = (Iuseruserhdr) session.load(Iuseruserhdr.class, ((Iuseruserhdr) list.get(0)).getId());
            	if(null != user && null != user.getIuserdata()){
            		compulsaBean.setNumeroSerieCertificado(user.getIuserdata().getId_certificado());
            		compulsaBean.setDniFirmante(user.getIuserdata().getDni());            		
            	}
            }
			
			HibernateUtil.commitTransaction(tran);
        } catch (HibernateException e) {
			LOG.error("ERROR al recuperar el número de serie del usuario. " + e.getMessage(), e);
		}
	}
	
	private void iniciaEscanerBean() {
		escanerBean = new EscanerBean();
		escanerBean.setRegisterBean(inputRegisterBean);
		escanerBean.setUseCaseConf(useCaseConf);
		
		escanerBean.setBookId(book.getIdocarchhdr().getId());
		escanerBean.setSessionId(((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getId());
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		escanerBean.setUrlServer(request.getScheme() + "://" + request.getServerName() +":" + request.getServerPort() + request.getContextPath());	
	}

	/**
	 * Borra los datos del interesado y crea uno vacío.
	 * 
	 */
	public void reinit() {
		Interesado interesado = new Interesado();
		interesado.setTipo(useCaseConf.getInterestedType());
		inputRegisterBean.setInteresado(interesado);
	}

	/**
	 * Borra los datos de la distribución y crea una vacío.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void reinitDis() {
		init();
		try {
			deleteSession("reportsLabelAction");
			
			this.listUsuarios = null;
			this.setSelectdestinoRedisUsuarios(null);
			this.listDepartament = null;
			this.setSelectdestinoRedisDepartamentos(null);
			this.listGrupos = null;
			this.setSelectdestinoRedisGrupos(null);
			
			if (inputRegisterBean.getFld8() != null && inputRegisterBean.getFld8().getId() != null) {
				
				List<Integer> orgIds = new ArrayList<Integer>();
				orgIds.add(inputRegisterBean.getFld8().getId());
				
				if(1 == typeDestinoRedis){						
					this.listUsuarios = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
					this.setSelectdestinoRedisUsuarios(Utils.defaultUser(this.listUsuarios, inputRegisterBean.getFld8().getId()));						
				} else if( 2 == typeDestinoRedis){
					this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
					this.setSelectdestinoRedisDepartamentos(Utils.defaultDepart(this.listDepartament, inputRegisterBean.getFld8().getId()));
				} else {
					this.listGrupos = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
					this.setSelectdestinoRedisGrupos(Utils.defaultGroup(this.listGrupos, inputRegisterBean.getFld8().getId()));
				}
			} else {				
				if(1 == typeDestinoRedis){
					this.listUsuarios = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), null);
					this.setSelectdestinoRedisUsuarios(null);
				} else if( 2 == typeDestinoRedis){
					this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), null);
					this.setSelectdestinoRedisDepartamentos(null);
				} else {
					this.listGrupos = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), null);
					this.setSelectdestinoRedisGrupos(null);
				}
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
	
	@SuppressWarnings("unchecked")
	public void reLoadUserDeptsGroupDistribucion(ValueChangeEvent event) {
		init();
		
		int nuevoTypeDestinoRedis = ((Integer) event.getNewValue()).intValue();

		try {
			deleteSession("reportsLabelAction");
			
			this.listUsuarios = null;
			this.setSelectdestinoRedisUsuarios(null);
			this.listDepartament = null;
			this.setSelectdestinoRedisDepartamentos(null);
			this.listGrupos = null;
			this.setSelectdestinoRedisGrupos(null);
			
			if (inputRegisterBean.getFld8() != null && inputRegisterBean.getFld8().getId() != null) {
				
				List<Integer> orgIds = new ArrayList<Integer>();
				orgIds.add(inputRegisterBean.getFld8().getId());
				
				if(1 == nuevoTypeDestinoRedis){						
					this.listUsuarios = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
					this.setSelectdestinoRedisUsuarios(Utils.defaultUser(this.listUsuarios, inputRegisterBean.getFld8().getId()));						
				} else if( 2 == nuevoTypeDestinoRedis){
					this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
					this.setSelectdestinoRedisDepartamentos(Utils.defaultDepart(this.listDepartament, inputRegisterBean.getFld8().getId()));
				} else {
					this.listGrupos = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
					this.setSelectdestinoRedisGrupos(Utils.defaultGroup(this.listGrupos, inputRegisterBean.getFld8().getId()));
				}
			} else {				
				if(1 == nuevoTypeDestinoRedis){
					this.listUsuarios = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), null);
					this.setSelectdestinoRedisUsuarios(null);
				} else if( 2 == nuevoTypeDestinoRedis){
					this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), null);
					this.setSelectdestinoRedisDepartamentos(null);
				} else {
					this.listGrupos = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), null);
					this.setSelectdestinoRedisGrupos(null);
				}
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
		
		if(null == documentoBean){
			iniciaDocumentoBean();
		}
		
		if(null == compulsaBean){
			iniciaCompulsaBean();
		}
		
		if(null == escanerBean){
			iniciaEscanerBean();
		}
	}

	@PreDestroy
	public void destroy1() {
		deleteSession("reportsLabelAction");
		if (inputRegisterBean != null) {
			RegistroManagerImpl registroManagerImpl = new RegistroManagerImpl();
			UsuarioVO usuario = new UsuarioVO();
			if (inputRegisterBean.getFdrid() != null) {
				usuario = (UsuarioVO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("USERVO");
				IdentificadorRegistroVO identificadorRegistro = new IdentificadorRegistroVO( String.valueOf(inputRegisterBean.getFdrid()), String.valueOf(book.getIdocarchhdr().getId()));
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
		UIViewRoot viewRoot = viewHandler.createView(facesContext, facesContext.getViewRoot().getViewId());
		facesContext.setViewRoot(viewRoot);
		facesContext.renderResponse(); // Optional
	}

	/**
	 * Da de alta un nuevo registro.
	 * 
	 * @throws BookException
	 *             si ha habido algún problema con el libro de registro.
	 * @throws SessionException
	 *             si ha habido algún problema con la sesión.
	 * @throws ValidationException
	 *             si ha habido algún problema con en la validación.
	 */
	public void saveInputRegister() throws BookException, SessionException,
			ValidationException {

		if (book == null) {
			throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
		}
		// validaciones de seguridad
		try {
			ValidationBo.validationSecurityUser(book.getIdocarchhdr().getId(), null, useCaseConf);
		} catch (RPGenericException rpGenericException) {
			LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " + rpGenericException.getShortMessage());
			Utils.redirectToErrorPage(rpGenericException, null, null);
		}
		deleteSession("reportsLabelAction");
//		if (validateInterested()) {
			loadDocSirSelected();
			if (inputRegisterBean.getFdrid() != null) {
				try {
					InputRegisterBo.saveOrUpdateFolder(useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid(), null, inputRegisterBean, inputRegisterBean.getInteresados(), null);

				} catch (RPInputRegisterException rpInputRegisterException) {
					LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpInputRegisterException.getCode().getCode() + " . Mensaje: " + rpInputRegisterException.getShortMessage());
					Utils.redirectToErrorPage(rpInputRegisterException, null, null);
				} catch (RPGenericException rpGenericException) {
					LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " + rpGenericException.getShortMessage());
					Utils.redirectToErrorPage(rpGenericException, null, null);
				}
				FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha guardado el Registro Correctamente", "Se ha guardado el Registro Correctamente"));
			} else {
				try {
					inputRegisterBean.setFdrid(InputRegisterBo.saveOrUpdateFolder(useCaseConf, book .getIdocarchhdr().getId(), null, null, inputRegisterBean, inputRegisterBean .getInteresados(), null));
				} catch (RPInputRegisterException rpInputRegisterException) {
					LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpInputRegisterException.getCode().getCode() + " . Mensaje: " + rpInputRegisterException.getShortMessage());
					Utils.redirectToErrorPage(rpInputRegisterException, null, null);
				} catch (RPGenericException rpGenericException) {
					LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: "	+ rpGenericException.getShortMessage());
					Utils.redirectToErrorPage(rpGenericException, null, null);
				}
				FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha creado el Registro Correctamente.", "Se ha creado el Registro Correctamente."));
			}

			loadInputRegister();
//		} else {
//			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Es obligatorio incluir o mínimo un interesado o el origen.", "Es obligatorio incluir o mínimo un interesado o el origen."));
//		}
	}

	/**
	 * Carga un registro.
	 */
	public void loadInputRegister() {
		try {
			init();
			deleteSession("reportsLabelAction");
			book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
			if (book == null) {
				throw new SessionException( SessionException.ERROR_SESSION_EXPIRED);
			}

			/* MODIFICACION */
			if (inputRegisterBean.getFdrid() != null) {
				Boolean readOnlyFlash = false;
				if (facesContext.getExternalContext().getFlash().get("readOnly") != null) {
					readOnlyFlash = (Boolean) facesContext.getExternalContext().getFlash().get("readOnly");
				}
				if (registerBo == null) {
					registerBo = new RegisterBo();
				}
				if (inputRegisterBo == null) {
					inputRegisterBo = new InputRegisterBo();
				}
				try {
					readOnly = registerBo.readOnly(readOnlyFlash, inputRegisterBean.getFdrid(), useCaseConf, book .getIdocarchhdr().getId());

					distributionBo = new DistributionBo();
					try {
						canDistr = distributionBo.canDistributionRegister( useCaseConf, inputRegisterBean.getFdrid(), book .getIdocarchhdr().getId());
					} catch (RPDistributionException e) {
						LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
					}
					
					LOG.info("Cargando datos del registro " + inputRegisterBean.getFdrid());
					
					inputRegisterBean = inputRegisterBo.loadInputRegisterBean( useCaseConf, book, inputRegisterBean.getFdrid());
					loadSelectedDocSIR();
					
					try {
						readOnlyIR = regInterchangeBo.isIntercambioRegistralNotEdit( Integer.toString(book.getIdocarchhdr().getId()), Integer .toString(inputRegisterBean.getFdrid()), String.valueOf(inputRegisterBean.getFld5().getId()));
					} catch (RPRegistralExchangeException e1) {
						LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + e1.getCode().getCode() + " . Mensaje: " + e1.getShortMessage(), e1);
					}

				} catch (RPInputRegisterException rpInputRegisterException) {
					LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpInputRegisterException.getCode().getCode() + " . Mensaje: " + rpInputRegisterException.getShortMessage());
					Utils.redirectToErrorPage(rpInputRegisterException, null, null);
				} catch (RPRegisterException rpRegisterException) {
					LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpRegisterException.getCode().getCode() + " . Mensaje: " + rpRegisterException.getShortMessage());
					Utils.redirectToErrorPage(rpRegisterException, null, null);
				} catch (RPGenericException rpGenericException) {
					LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " + rpGenericException.getShortMessage());
					Utils.redirectToErrorPage(rpGenericException, null, null);
				}
			}
			
			iniciaDocumentoBean();
			iniciaCompulsaBean();
			iniciaEscanerBean();
			
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
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
		
		boolean openFolderDtr = ("D".equals(origen)) ? true : false;
		
		listDocuments = new ArrayList<Axdoch>();
		if (registerDocumentsBo == null) {
			registerDocumentsBo = new RegisterDocumentsBo();
		}
		try {
			LOG.info("Cargando documentos asociados al registro "
					+ inputRegisterBean.getFdrid());
			listDocuments = registerDocumentsBo.getDocumentsBasicInfo( useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid(), openFolderDtr);
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
		
		DocumentoBo.actualizaArbol(openFolderDtr, documentoBean);
		
	}
	
	public void actualizaArbol(){
		boolean openFolderDtr = ("D".equals(origen)) ? true : false;
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
		
		DocumentoBo.actualizaArbol(openFolderDtr, documentoBean);
		getRegisterHistDocumentsUpdates();
	}
	
	

	/**
	 * Obtiene el histórico de un registro.
	 */
	public void getRegisterHistoricalUpdates() {
		listUpdates = new ArrayList<UpdHisFdrResults>();
		try {
			LOG.info("Cargando histórico del registro " + inputRegisterBean.getFdrid());
			if (registerBo == null) {
				registerBo = new RegisterBo();
			}
			listUpdates = registerBo.getUpdHisFdrResults(useCaseConf, book, inputRegisterBean.getFdrid(), inputRegisterBean.getFld1());
		} catch (RPRegisterException rpRegisterException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpRegisterException.getCode().getCode() + " . Mensaje: " + rpRegisterException.getShortMessage());
			Utils.redirectToErrorPage(rpRegisterException, null, null);
		} catch (RPGenericException rpGenericException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " + rpGenericException.getShortMessage());
			Utils.redirectToErrorPage(rpGenericException, null, null);
		}
	}
	
	/**
	 * Obtiene el histórico de documentos de un registro.
	 */
	public void getRegisterHistDocumentsUpdates() {
		listDocumentUpdates = new ArrayList<UpdHisDocFdrResults>();
		
		try {
			if(null != inputRegisterBean && null != inputRegisterBean.getFdrid() && null != inputRegisterBean.getFld1()){
				LOG.info("Cargando histórico del registro " + inputRegisterBean.getFdrid());
				if (registerBo == null) {
					registerBo = new RegisterBo();
				}
				listDocumentUpdates = registerBo.getUpdHisDocResults(useCaseConf, book, inputRegisterBean.getFdrid(), inputRegisterBean.getFld1());
			}
		} catch (RPRegisterException rpRegisterException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpRegisterException.getCode().getCode() + " . Mensaje: " + rpRegisterException.getShortMessage());
			Utils.redirectToErrorPage(rpRegisterException, null, null);
			
		} catch (RPGenericException rpGenericException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " + rpGenericException.getShortMessage());
			Utils.redirectToErrorPage(rpGenericException, null, null);
		}
	}

	/**
	 * Obtiene el histórico de distribución de un registro.
	 */
	public void getRegisterHistDistribution() {
		listHistDistribution = new ArrayList<DtrFdrResults>();
		try {
			LOG.info("Cargando histórico de distribución del registro " + inputRegisterBean.getFdrid());
			distributionBo = new DistributionBo();
			listHistDistribution = distributionBo.getHistDistribution( useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid());
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
			book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
			if (book == null) {
				throw new SessionException( SessionException.ERROR_SESSION_EXPIRED);
			}

			if (regInterchangeBo == null) {
				regInterchangeBo = new RegInterchangeBo();
			}
			LOG.info("Cargando histórico de Intecambio Registral del registro " + inputRegisterBean.getFdrid());
			listHistInputInterchange = regInterchangeBo.getHistorialIntercambioRegistralEntrada( String.valueOf(book.getIdocarchhdr().getId()), String.valueOf(inputRegisterBean.getFdrid()), String.valueOf(inputRegisterBean.getFld5().getId()));
			listHistOutputInterchange = regInterchangeBo.getHistorialIntercambioRegistralSalida( String.valueOf(book.getIdocarchhdr().getId()), String.valueOf(inputRegisterBean.getFdrid()), String.valueOf(inputRegisterBean.getFld5().getId()));
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
			Object[] regAsoc = registerBo.getAsocReg(useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid());
			if (regAsoc != null && (!((Map<AxPK, AxSf>) regAsoc[1]).isEmpty() || !((Map<AxPK, AxSf>) regAsoc[2]).isEmpty())) {
				idocs = (Map<String, Idocarchhdr>) regAsoc[0];
				asocReg = registerBo.getListAsoReg((Map<AxPK, AxSf>) regAsoc[1], idocs);
				asocRegPrim = registerBo.getAsoReg((Map<AxPK, AxSf>) regAsoc[2], idocs);
			}
		} catch (RPRegisterException rpRegisterException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpRegisterException.getCode().getCode() + " . Mensaje: " + rpRegisterException.getShortMessage());
			Utils.redirectToErrorPage(rpRegisterException, null, null);
		} catch (RPGenericException rpGenericException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " + rpGenericException.getShortMessage());
			Utils.redirectToErrorPage(rpGenericException, null, null);
		}
	}

	/**
	 * Copia un registro.
	 * 
	 * @throws SessionException
	 *             si ha habido algún problema con la sesión.
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
				inputRegisterBean = inputRegisterBo.copyInputRegisterBean( useCaseConf, book, registerCopy);
				loadSelectedDocSIR();
				FacesContext.getCurrentInstance().addMessage( "Registro Copiado", new FacesMessage(FacesMessage.SEVERITY_INFO, "Se han copiado los datos del registro: " + registerNumCopy, "Se han copiado los datos del registro: " + registerNumCopy));
			} catch (RPInputRegisterException rpInputRegisterException) {
				LOG.error(ErrorConstants.COPY_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpInputRegisterException.getCode().getCode() + " . Mensaje: " + rpInputRegisterException.getShortMessage());
				Utils.redirectToErrorPage(rpInputRegisterException, null, null);
			} catch (RPGenericException rpGenericException) {
				LOG.error(ErrorConstants.COPY_INPUT_REGISTER_ERROR_MESSAGE + ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " + rpGenericException.getShortMessage());
				Utils.redirectToErrorPage(rpGenericException, null, null);
			}
		}
	}

	/**
	 * borra un registro asociado.
	 * 
	 * @throws SessionException
	 *             si ha habido algún problema con la sesión.
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
			Object[] regAsoc = registerBo.getAsocReg(useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid());
			if (regAsoc != null) {
				idocs = (Map<String, Idocarchhdr>) regAsoc[0];
				asocReg = registerBo.getListAsoReg((Map<AxPK, AxSf>) regAsoc[1], idocs);
				asocRegPrim = registerBo.getAsoReg((Map<AxPK, AxSf>) regAsoc[2], idocs);
			}
			FacesContext.getCurrentInstance().addMessage( "Asociar Registros", new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha desasociado el registro correctamente", "Se ha desasociado el registro correctamente"));
		} catch (RPRegisterException e) {
			LOG.error(ErrorConstants.DELETE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: " + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
			Utils.redirectToErrorPage(e, null, null);
		} catch (RPGenericException rpGenericException) {
			LOG.error(ErrorConstants.DELETE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " + rpGenericException.getShortMessage());
			Utils.redirectToErrorPage(rpGenericException, null, null);
		}
	}

	/**
	 * asocia un registro.
	 * 
	 * @throws SessionException
	 *             si ha habido algún problema con la sesión.
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
			List<AsocRegsResults> listAsoc = registerBo.saveAssociation(useCaseConf, asocRegisterBean, inputRegisterBean.getFdrid(), book.getIdocarchhdr() .getId());

			if (listAsoc == null || listAsoc.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage( "Asociar Registros", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha encontrado el registro a asociar", "No se ha encontrado el registro a asociar"));
			} else {

				Object[] regAsoc = registerBo.getAsocReg(useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid());
				if (regAsoc != null) {
					idocs = (Map<String, Idocarchhdr>) regAsoc[0];
					asocReg = registerBo.getListAsoReg((Map<AxPK, AxSf>) regAsoc[1], idocs);
					asocRegPrim = registerBo.getAsoReg((Map<AxPK, AxSf>) regAsoc[2], idocs);
					FacesContext.getCurrentInstance().addMessage( "Asociar Registros", new FacesMessage( FacesMessage.SEVERITY_INFO, "Se ha asociado el registro correctamente", "Se ha asociado el registro correctamente"));
				}
			}
		} catch (RPRegisterException e) {
			LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: " + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
			Utils.redirectToErrorPage(e, null, null);
		} catch (RPGenericException rpGenericException) {
			LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " + rpGenericException.getShortMessage());
			Utils.redirectToErrorPage(rpGenericException, null, null);
		}
	}

	/**
	 * Redirecciona a la página de intercambioRegistral.
	 */
	public void goRegInterchangeForm() {
		deleteSession("reportsLabelAction");
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
				Utils.navigate(parameter, false,
						"searchDestinationRegInterchange.xhtml");
			} else {
				FacesContext.getCurrentInstance().addMessage("Intercambio Registral", new FacesMessage( FacesMessage.SEVERITY_ERROR, "No se puede enviar el registro. El campo Documentación Soporte es obligatorio para los envíos por intercambio registral.", "No se puede enviar el registro. El campo Documentación Soporte es obligatorio para los envíos por intercambio registral."));
			}

		} else {
			FacesContext.getCurrentInstance().addMessage("Intercambio Registral", new FacesMessage( FacesMessage.SEVERITY_ERROR, "No se puede enviar el registro. No dispone de interesados válidos. Recuerde que debe guardar el registro si ha realizado cambios.","No se puede enviar el registro. No dispone de interesados válidos. Recuerde que debe guardar el registro si ha realizado cambios."));
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
		List<Interesado> interadosCon = interestedBo.getAllInterested(book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid(), useCaseConf);
		if (interadosCon == null || interadosCon.isEmpty()) {
			result = false;
		} else {
			for (Interesado inter : interadosCon) {
				if (StringUtils.isEmpty(inter.getDocIdentidad()) || StringUtils.isEmpty(inter.getTipodoc())) {
					result = false;
				} else {
					if (inter.getRepresentante() != null && !"N".equals(inter.getRepresentante().getTipo())) {
						if (StringUtils.isEmpty(inter.getRepresentante().getDocIdentidad()) || StringUtils.isEmpty(inter.getRepresentante().getTipodoc())) {
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
		if (inputRegisterBean.getFld504() == null && inputRegisterBean.getFld505() == null && inputRegisterBean.getFld506() == null) {
			result = false;
		}
		return result;
	}

	/**
	 * Método que distribuye de los elementos seleccionados.
	 * 
	 * @throws SessionException
	 *             errror de sessión.
	 */
	public void distribuir() throws SessionException {
		init();
		deleteSession("reportsLabelAction");
		ScrRegstate book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
		if (book == null) {
			throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
		}
		if (distributionBo == null) {
			distributionBo = new DistributionBo();
		}
		try {

			Integer userId = null;
			
			if(1 == typeDestinoRedis){
				userId = selectdestinoRedisUsuarios.getId();
			} else if(2 == typeDestinoRedis){
				userId = selectdestinoRedisDepartamentos.getId();
			} else {
				userId = selectdestinoRedisGrupos.getId();
			}

			List<Integer> ids = new ArrayList<Integer>();
			ids.add(inputRegisterBean.getFdrid());
			String result = distributionBo.createDistribution(useCaseConf, ids, motivoDistribucion, typeDestinoRedis, userId, book .getIdocarchhdr().getId());
			// distribucion
			distributionBo = new DistributionBo();
			listHistDistribution = distributionBo.getHistDistribution( useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid());
			if ("".equals(result.trim())) {
				result = "Se han distribuido los registros correctamente.";
				canDistr = false;
				RequestContext context = RequestContext.getCurrentInstance();
				context.update("formLink");
			}
			result = result.replaceAll(";", "<br/>");
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_INFO, result, result));
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
	 * 
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
					IdentificadorDocumentoElectronicoAnexoVO identificadorDocumentoElectronicoAnexoVO = new IdentificadorDocumentoElectronicoAnexoVO();
					identificadorDocumentoElectronicoAnexoVO.setIdLibro(Long.parseLong(String.valueOf(book.getIdocarchhdr().getId())));
					identificadorDocumentoElectronicoAnexoVO.setIdRegistro(Long.parseLong(String.valueOf(inputRegisterBean.getFdrid())));
					documentoElectronico.setId(identificadorDocumentoElectronicoAnexoVO);

					// CODE NAME
					String codeName = ("codeName" + documentoElectronico.getName());
					codeName = StringUtils.abbreviate(codeName, 21);
					documentoElectronico.setCodeName(codeName);

					// extension
					String extension = documentoElectronico.getName().substring( documentoElectronico.getName().lastIndexOf(".") + 1, documentoElectronico.getName().length());
					documentoElectronico.setExtension(extension);

					// contenido
					DocumentoElectronicoAnexoContenidoVO contenido = new DocumentoElectronicoAnexoContenidoVO();
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
					contenido.setContent(content);

					documentoElectronico.setContenido(contenido);

					// TIPO MIME
					// MimetypesFileTypeMap mimeTypesMap = new
					// MimetypesFileTypeMap();
					// documentoElectronico.setMimeType(mimeTypesMap.getContentType());
					try {
						if (ContentTypeEnum.valueOf(extension.toUpperCase()) != null && ContentTypeEnum.valueOf(extension.toUpperCase()).getContentType().length() <= 20) {
							documentoElectronico.setMimeType(ContentTypeEnum.valueOf(extension.toUpperCase()).getContentType());
						}
					} catch (Exception e) {
						LOG.error("ERROR: " + e.getMessage(), e);
					}
					// TIPO DOCUMENTO
					documentoElectronico.setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.DOCUMENTO_ADJUNTO_FORMULARIO);

					if (validateDocument != null) {
						documentoElectronico.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.getEnum(Integer.valueOf(validateDocument.substring(1, 2))));
					}

					// datos hash
					DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumento = new DocumentoElectronicoAnexoDatosFirmaVO();

					UtilsHash utilHash = new UtilsHash();
					String hashDocumento = UtilsHash.getBase64Sring(utilHash.generarHash(content));
					datosFirmaDocumento.setHash(hashDocumento);

					documentoElectronico.setDatosFirma(datosFirmaDocumento);
					// FIRMAS
					List<DocumentoElectronicoAnexoVO> firmas = new ArrayList<DocumentoElectronicoAnexoVO>();
					DocumentoElectronicoAnexoVO firmaDocumento = new DocumentoElectronicoAnexoVO();
					if (signDocument != null && !"".equals(signDocument)) {
						IdentificadorDocumentoElectronicoAnexoVO idFima = new IdentificadorDocumentoElectronicoAnexoVO();

						idFima.setIdLibro(identificadorDocumentoElectronicoAnexoVO.getIdLibro());
						idFima.setIdRegistro(identificadorDocumentoElectronicoAnexoVO.getIdRegistro());
						firmaDocumento.setId(idFima);
						String codeNameFirma = "codeNameSign" + documentoElectronico.getName();
						codeNameFirma = StringUtils.abbreviate(codeNameFirma, 21);
						firmaDocumento.setCodeName(codeNameFirma);

						// contenido
						DocumentoElectronicoAnexoContenidoVO contenidoFirma = new DocumentoElectronicoAnexoContenidoVO();

						JSONArray arraySignContent = (JSONArray) parser.parse(signDocument);

						LinkedList<byte[]> blocksSignContent = new LinkedList<byte[]>();
						JSONObject blockSignContent = null;
						for (int i = 0; i < arraySignContent.size(); i++) {
							blockSignContent = (JSONObject) arraySignContent.get(i);
							blocksSignContent.add(Base64.decodeBase64((String) blockSignContent.get("block")));
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
						DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumentoFirma = new DocumentoElectronicoAnexoDatosFirmaVO();
						String hashDocumentoFirma = UtilsHash.getBase64Sring(utilHash.generarHash(signcontent));
						datosFirmaDocumentoFirma.setHash(hashDocumentoFirma);
						firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
						// NOMBRE FICHERO FIRMA

						String nameFirma = "FIRMA.csig";
						firmaDocumento.setName(nameFirma);
						firmaDocumento.setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.FICHERO_TECNICO);
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
					UsuarioVO usuario = (UsuarioVO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("USERVO");
					DocumentoElectronicoAnexoVO documentSaved = registerDocumentsBo.saveDocuments(useCaseConf.getSessionID(), book.getIdocarchhdr().getId(),inputRegisterBean.getFdrid(), documentoElectronico, registerData, useCaseConf.getLocale(), useCaseConf.getEntidadId(), usuario);
					if (documentSaved != null) {
						getRegisterAttachedDocuments();
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha adjuntado el documento " + documentoElectronico.getName() + " correctamente", "Se ha adjuntado el documento " + documentoElectronico.getName() + " correctamente"));
					}
				} else {
					FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR, "No se ha podido adjuntar el documento. El nombre del fichero no puede tener mas de 80 caracteres: " + documentoElectronico.getName(), "No se ha podido adjuntar el documento. El nombre del fichero no puede tener mas de 80 caracteres: " + documentoElectronico.getName()));
				}
			} else {
				ValidationBo.showDialog( "Adjuntar", new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se ha podido adjuntar el documento. El nombre del fichero ya existe"));
			}
		} catch (RPRegisterException e) {
			LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE + ". Código: " + e.getCode().getCode() + " . Mensaje: " + e.getMessage());
			Utils.redirectToErrorPage(e, null, null);
		} catch (ParseException e) {
			LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Mensaje: " + e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Mensaje: " + e.getMessage(), e);
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
	 * @param tabChangeEvent
	 *            Evento de cambio de panel.
	 */
	public void changeListener(TabChangeEvent tabChangeEvent) {
		deleteSession("reportsLabelAction");
		String tabId = tabChangeEvent.getTab().getId();
		if ("inputRegisterData".equals(tabId)) {
			this.setMyCurrentTab(NUMBER0);
			if (inputRegisterBean == null) {
				loadInputRegister();
			}
		} else if ("inputRegisterAttachedDocuments".equals(tabId)) {
			this.setMyCurrentTab(NUMBER1);
			if (listDocuments == null) {
				getRegisterAttachedDocuments();
			}
		} else if ("inputRegisterHistorical".equals(tabId)) {
			this.setMyCurrentTab(NUMBER2);
			// if (listUpdates == null || listUpdates.size() == 0 ) {
			getRegisterHistoricalUpdates();
			// }
		} else if ("inputDocumentHistorical".equals(tabId)) {
			this.setMyCurrentTab(NUMBER3);
			getRegisterHistDocumentsUpdates();
		} else if ("inputRegisterDistributionHistorical".equals(tabId)) {
			this.setMyCurrentTab(NUMBER4);
			if (listHistDistribution == null) {
				getRegisterHistDistribution();
			}
		} else if ("inputRegisterInterchangeHistorical".equals(tabId)) {
			this.setMyCurrentTab(NUMBER5);
			if (listHistInputInterchange == null
					&& listHistOutputInterchange == null) {
				getRegisterHistInputAndOutputInterchange();
			}
		} else if ("inputRegisterAssociatedRegisters".equals(tabId)) {
			this.setMyCurrentTab(NUMBER6);
			if (asocReg == null) {
				getRegisterAssociatedRegisters();
			}
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
	 * @param readOnlyIR
	 *            valor del campo a guardar.
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
	 * @param readOnly
	 *            valor del campo a guardar.
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
	 * @param registerCopy
	 *            valor del campo a guardar.
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
	 * @param inputRegisterBo
	 *            valor del campo a guardar.
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
	 * @param inputRegisterBean
	 *            valor del campo a guardar.
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
	 * @param listUpdates
	 *            valor del campo a guardar.
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
	 * @param listHistDistribution
	 *            valor del campo a guardar.
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
	 * @param origen
	 *            valor del campo a guardar.
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
	 * @param asocReg
	 *            valor del campo a guardar.
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
	 * @param asocRegPrim
	 *            valor del campo a guardar.
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
	 * @param idocs
	 *            valor del campo a guardar.
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
	 * @param asocRegisterBean
	 *            valor del campo a guardar.
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
	 * @param listHistInputInterchange
	 *            valor del campo a guardar.
	 */
	public void setListHistInputInterchange( List<IntercambioRegistralEntradaVO> listHistInputInterchange) {
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
	 * @param listHistOutputInterchange
	 *            valor del campo a guardar.
	 */
	public void setListHistOutputInterchange( List<IntercambioRegistralSalidaVO> listHistOutputInterchange) {
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
	 * @param listDocuments
	 *            valor del campo a guardar.
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
	 * @param motivoDistribucion
	 *            valor del campo a guardar.
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
	 * @param selectdestinoRedisDepartamentos
	 *            valor del campo a guardar.
	 */
	public void setSelectdestinoRedisDepartamentos( Iuserdepthdr selectdestinoRedisDepartamentos) {
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
	 * @param myCurrentTab
	 *            valor del campo a guardar.
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
	 * @param registerNumCopy
	 *            valor del campo a guardar.
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
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG") != null) {
			unidad = (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG");
			resetValues("accordion:inputRegisterDataForm:fld7");
			getInputRegisterBean().setFld7(unidad);
		}
	}

	/**
	 * Método que actualiza el campo destino con el valor seleccionado en la
	 * búsqueda avanzada de organismos.
	 */
	public void updateDestination() {
		ScrOrg unidad = null;
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG") != null) {
			unidad = (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG");
			resetValues("accordion:inputRegisterDataForm:fld8");
			getInputRegisterBean().setFld8(unidad);
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
	 * @param canDistr
	 *            valor del campo a guardar.
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
	 * @param selectedDocSIR
	 *            valor del campo a guardar.
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
	 * @param contentDocument
	 *            valor del campo a guardar.
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
	 * @param documentoElectronico
	 *            valor del campo a guardar.
	 */
	public void setDocumentoElectronico( DocumentoElectronicoAnexoVO documentoElectronico) {
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
	 * @param validateDocument
	 *            valor del campo a guardar.
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
	 * @param signDocument
	 *            valor del campo a guardar.
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
	 * @param certificateDocument
	 *            valor del campo a guardar.
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
	 * @param selectDocument
	 *            valor del campo a guardar.
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
	 * @param listNameDocument
	 *            valor del campo a guardar.
	 */
	public void setListNameDocument(String listNameDocument) {
		this.listNameDocument = listNameDocument;
	}

	public void deleteLabel() {
		init();
		deleteSession("reportsLabelAction");
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
	 * @param listDepartament
	 *            valor del campo a guardar.
	 */
	public void setListDepartament(List<Iuserdepthdr> listDepartament) {
		this.listDepartament = listDepartament;
	}	

	public void crearCarpeta() {
		DocumentoBo.crearCarpeta(documentoBean);
		actualizaArbol();
		
	}	
	
	public void modificarNombreDocumento(){		
		DocumentoBo.modificarNombre(documentoBean);
		actualizaArbol();
	}
	
	public void borrarDocumento(){	
		DocumentoBo.borrar(documentoBean);
		actualizaArbol();
	}
		
	public void subirDocumento(FileUploadEvent event){
		
		DocumentoBean documentoBeanTemp = new DocumentoBean();
		
		documentoBeanTemp.setUseCaseConf(useCaseConf);
		documentoBeanTemp.setRegisterBean(inputRegisterBean);
		documentoBeanTemp.setBookId(book.getIdocarchhdr().getId());
		documentoBeanTemp.setEntidadId(entidadId);
		
		documentoBeanTemp.setFile(event.getFile());
		documentoBeanTemp.setCarpetaAnexarDoc(documentoBean.getCarpetaAnexarDoc());
		
		DocumentoBo.subirDocumento(documentoBeanTemp);		
		
		actualizaArbol();			
	}

	public Axpageh getSelectPage() {
		return selectPage;
	}

	public void setSelectPage(Axpageh selectPage) {
		this.selectPage = selectPage;
	}

	public void addInteresado() {
		if (inputRegisterBean.getInteresado() != null) {
			useCaseConf.setInterestedType(inputRegisterBean.getInteresado().getTipo());
			saveUseCaseConf(useCaseConf);
		}
		inputRegisterBean.getInteresados().add( inputRegisterBean.getInteresado());
		reinit();
	}

	public void actualizaDestino(ValueChangeEvent event) {
		if (null != event.getNewValue()	&& !event.getNewValue().equals(event.getOldValue())) {

			Transaction tran = null;
			ScrOrg scrOrg = null;
			try {
				Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
				tran = session.beginTransaction();
				scrOrg = (ScrOrg) session.load(ScrOrg.class, new Integer( ((ScrCa) event.getNewValue()).getIdOrg()));
			} catch (HibernateException e) {
				LOG.error("ERROR: " + e.getMessage(), e);
				HibernateUtil.rollbackTransaction(tran);
			} finally {
				try {
					HibernateUtil.commitTransaction(tran);
				} catch (HibernateException e) {
					LOG.error("ERROR: " + e.getMessage(), e);
				}
			}
			if (null != scrOrg) {
				inputRegisterBean.setFld8(scrOrg);
			}
		}
	}

	public TercerosAction getTercerosAction() {
		return tercerosAction;
	}

	public void setTercerosAction(TercerosAction tercerosAction) {
		this.tercerosAction = tercerosAction;
	}
	
	public int getTypeDestinoRedis() {
		return typeDestinoRedis;
	}

	public void setTypeDestinoRedis(int typeDestinoRedis) {
		this.typeDestinoRedis = typeDestinoRedis;
	}
	
	public Iuseruserhdr getSelectdestinoRedisUsuarios() {
		return selectdestinoRedisUsuarios;
	}

	public void setSelectdestinoRedisUsuarios(Iuseruserhdr selectdestinoRedisUsuarios) {
		this.selectdestinoRedisUsuarios = selectdestinoRedisUsuarios;
	}

	public Iusergrouphdr getSelectdestinoRedisGrupos() {
		return selectdestinoRedisGrupos;
	}

	public void setSelectdestinoRedisGrupos(Iusergrouphdr selectdestinoRedisGrupos) {
		this.selectdestinoRedisGrupos = selectdestinoRedisGrupos;
	}

	public List<Iuseruserhdr> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<Iuseruserhdr> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}

	public List<Iusergrouphdr> getListGrupos() {
		return listGrupos;
	}

	public void setListGrupos(List<Iusergrouphdr> listGrupos) {
		this.listGrupos = listGrupos; 
	}

	public void cargarListaDocsCompulsar(ActionEvent event) {
		if(null != documentoBean.getSelectAnexo()){
			CompulsaBo.prepararFirmaDoc(compulsaBean, documentoBean.getSelectAnexo());
		}
	}
	
	public void cargarListaDocsCompulsarTodos(ActionEvent event) {
		CompulsaBo.prepararFirmaDoc(compulsaBean, (Document) documentoBean.getArbolDocumentos().getData());
	}
	
	public boolean isHayDocumentosParaCompulsar(){
		return CompulsaBo.hayDocumentosParaCompulsar((Document) documentoBean.getArbolDocumentos().getData());
	}
	
	public void compulsarTodos(ActionEvent event){
		compulsaBean.setEsFirmaTodos(true);
	}
	
	public void compulsaDocumento (){	
		compulsaBean.setEsFirmaTodos(false);
	}

	public DocumentoBean getDocumentoBean() {
		return documentoBean;
	}

	public void setDocumentoBean(DocumentoBean documentoBean) {
		this.documentoBean = documentoBean;
	}

	public CompulsaBean getCompulsaBean() {
		return compulsaBean;
	}

	public void setCompulsaBean(CompulsaBean compulsaBean) {
		this.compulsaBean = compulsaBean;
	}

	public EscanerBean getEscanerBean() {
		return escanerBean;
	}

	public void setEscanerBean(EscanerBean escanerBean) {
		this.escanerBean = escanerBean;
	}

	public List<UpdHisDocFdrResults> getListDocumentUpdates() {
		return listDocumentUpdates;
	}

	public void setListDocumentUpdates(List<UpdHisDocFdrResults> listDocumentUpdates) {
		this.listDocumentUpdates = listDocumentUpdates;
	}
	
	public void consultaDocumento(){
		DocumentoBo.consultaDocumento(documentoBean);
        getRegisterHistDocumentsUpdates();
	}
}