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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.exception.AttributesException;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesdoc.Iuserdepthdr;
import com.ieci.tecdoc.common.invesdoc.Iuseruserhdr;
import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrProv;
import com.ieci.tecdoc.common.invesicres.ScrTt;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.desktopweb.utils.RBUtil;
import com.ieci.tecdoc.isicres.session.validation.ValidationSessionEx;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.Validator;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.msssi.sgm.registropresencial.beans.ItemBean;
import es.msssi.sgm.registropresencial.beans.QueryCompactSearchOrg;
import es.msssi.sgm.registropresencial.beans.ibatis.ScrCCAA;
import es.msssi.sgm.registropresencial.businessobject.AsuntConverter;
import es.msssi.sgm.registropresencial.businessobject.DepartConverter;
import es.msssi.sgm.registropresencial.businessobject.UnitsBo;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.validations.ValidationListBo;

/**
 * Action que muestra la lista de libros disponible.
 * 
 * @author cmorenog
 */
public class ValidationListAction extends GenericActions {
    public static final String HIBERNATEINVESDOCOBJPATH = "com.ieci.tecdoc.common.invesdoc.";
    public static final String HIBERNATEIUSERUSERHDR = HIBERNATEINVESDOCOBJPATH +
	"Iuseruserhdr";
    public static final String HIBERNATEIUSERDEPTHDR = HIBERNATEINVESDOCOBJPATH +
	"Iuserdepthdr";
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ValidationListAction.class.getName());
    private static final int FIELD2 = 2;
    private static final int FIELD4 = 4;
    private static final int FIELD12 = 12;
    private static final int FIELD5 = 5;
    private static final int FIELD6 = 6;
    private static final int FIELD7 = 7;
    private static final int FIELD8 = 8;
    private static final int FIELD11 = 11;
    private static final int FIELD14 = 14;
    private static final int NUMSTATESDIST = 7;
    // private static final int TYPEUSER = 1;
    private static final int TYPEDEP = 2;
    // private static final int TYPEGROUP = 3;
    private static final int STATECLOSE = 5;
    private static final int FIELD10 = 10;
    private static final int FIELD1002 = 1002;
    private ValidationListBo validationListBo;
    private List<ScrTt> listTransportes;
    private List<ScrOfic> listOficinas;
    private List<ScrCa> listAsuntos;

    private List<ItemBean> hashEstados = new ArrayList<ItemBean>();
    private List<ItemBean> hashTipoRegistro = new ArrayList<ItemBean>();
    private List<ItemBean> listStateDist = new ArrayList<ItemBean>();
    private List<Iuserdepthdr> listDepartamentos = null;
    private List<ScrCCAA> listCCAA;
    private List<ScrProv> listProv;
    private static ApplicationContext appContext;
    private static QueryCompactSearchOrg queryCompactSearchOrg;
    static {
	appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
       }
    /*
     * private List<Iusergrouphdr> listGrupos = null; private List<Iuseruserhdr>
     * listUsuarios = null;
     */

    /**
     * Constructor.
     */
    public ValidationListAction() {
	queryCompactSearchOrg = (QueryCompactSearchOrg)appContext.getBean("queryCompactSearchOrg");
    }

    /**
     * Rellena los listados al inicio.
     */
    @PostConstruct
    public void create() {
	if (validationListBo == null) {
	    validationListBo = new ValidationListBo();
	}
	try {
	    listTransportes = validationListBo.getScrtt(useCaseConf);
	    listOficinas = validationListBo.getScrOfic(useCaseConf);
	    listAsuntos = validationListBo.getScrCa(useCaseConf);

	    AsuntConverter asuntConvert = new AsuntConverter();
	    asuntConvert.setListAsuntos(listAsuntos);
	    listCCAA = validationListBo.getListCCAA();
	    listProv = validationListBo.getListProv(useCaseConf);
	    
	    hashTipoRegistro.add(new ItemBean(
		1, "Entrada"));
	    hashTipoRegistro.add(new ItemBean(
		2, "Salida"));

	    hashEstados.add(new ItemBean(
		0, "Completo"));
	    hashEstados.add(new ItemBean(
		1, "Incompleto"));
	    hashEstados.add(new ItemBean(
		STATECLOSE, "Cerrado"));
	    for (int i = 1; i < NUMSTATESDIST; i++) {
		if (i < NUMBER6) {
		    listStateDist.add(new ItemBean(
			i, RBUtil.getInstance(
			    useCaseConf.getLocale()).getProperty(
			    Keys.I18N_BOOKUSECASE_DISTRIBUTIONHISTORY_MINUTA_DIST_STATE +
				i)));
		}
	    }
	    //getDeptsGroupsUsers(useCaseConf);
	}
	// Si falla, redireccionamos a la página de error
	catch (BookException bookException) {
	    LOG.error(
		ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, bookException);
	    Utils.redirectToErrorPage(
		null, bookException, null);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(
		null, sessionException, null);
	}
	catch (ValidationException validationException) {
	    LOG.error(
		ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, validationException);
	    Utils.redirectToErrorPage(
		null, validationException, null);
	}
	catch (AttributesException attributesException) {
	    LOG.error(
		ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, attributesException);
	    Utils.redirectToErrorPage(
		null, attributesException, null);
	}
    }

    /**
     * Obtiene el valor del parámetro validationListBo.
     * 
     * @return validationListBo código de error a obtener.
     */
    public ValidationListBo getValidationListBo() {
	return validationListBo;
    }

    /**
     * Guarda el valor del parámetro validationListBo.
     * 
     * @param validationListBo
     *            Código de error a guardar.
     */
    public void setValidationListBo(
	ValidationListBo validationListBo) {
	this.validationListBo = validationListBo;
    }

    /**
     * Obtiene el valor del parámetro listTransportes.
     * 
     * @return listTransportes código de error a obtener.
     */
    public List<ScrTt> getListTransportes() {
	return listTransportes;
    }

    /**
     * Guarda el valor del parámetro listTransportes.
     * 
     * @param listTransportes
     *            Código de error a guardar.
     */
    public void setListTransportes(
	List<ScrTt> listTransportes) {
	this.listTransportes = listTransportes;
    }

    /**
     * Obtiene el valor del parámetro listOficinas.
     * 
     * @return listOficinas código de error a obtener.
     */
    public List<ScrOfic> getListOficinas() {
	return listOficinas;
    }

    /**
     * Guarda el valor del parámetro listOficinas.
     * 
     * @param listOficinas
     *            Código de error a guardar.
     */
    public void setListOficinas(
	List<ScrOfic> listOficinas) {
	this.listOficinas = listOficinas;
    }

    /**
     * Obtiene el valor del parámetro listAsuntos.
     * 
     * @return listAsuntos código de error a obtener.
     */
    public List<ScrCa> getListAsuntos() {
	return listAsuntos;
    }

    /**
     * Guarda el valor del parámetro listAsuntos.
     * 
     * @param listAsuntos
     *            Código de error a guardar.
     */
    public void setListAsuntos(
	List<ScrCa> listAsuntos) {
	this.listAsuntos = listAsuntos;
    }


    /**
     * Obtiene el valor del parámetro hashEstados.
     * 
     * @return hashEstados código de error a obtener.
     */
    public List<ItemBean> getHashEstados() {
	return hashEstados;
    }

    /**
     * Guarda el valor del parámetro hashEstados.
     * 
     * @param hashEstados
     *            Código de error a guardar.
     */
    public void setHashEstados(
	List<ItemBean> hashEstados) {
	this.hashEstados = hashEstados;
    }

    /**
     * Obtiene el valor del parámetro hashTipoRegistro.
     * 
     * @return hashTipoRegistro código de error a obtener.
     */
    public List<ItemBean> getHashTipoRegistro() {
	return hashTipoRegistro;
    }

    /**
     * Guarda el valor del parámetro hashTipoRegistro.
     * 
     * @param hashTipoRegistro
     *            Código de error a guardar.
     */
    public void setHashTipoRegistro(
	List<ItemBean> hashTipoRegistro) {
	this.hashTipoRegistro = hashTipoRegistro;
    }

    /**
     * Obtiene el valor del parámetro listStateDist.
     * 
     * @return listStateDist código de error a obtener.
     */
    public List<ItemBean> getListStateDist() {
	return listStateDist;
    }

    /**
     * Guarda el valor del parámetro listStateDist.
     * 
     * @param listStateDist
     *            lista a guardar.
     */
    public void setListStateDist(
	List<ItemBean> listStateDist) {
	this.listStateDist = listStateDist;
    }

    /**
     * Obtiene el valor del parámetro listDepartamentos.
     * 
     * @return listDepartamentos lista a obtener.
     */
    public List<Iuserdepthdr> getListDepartamentos() {
	return listDepartamentos;
    }

    /**
     * Guarda el valor del parámetro listDepartamentos.
     * 
     * @param listDepartamentos
     *            lista de departamentos a guardar.
     */
    public void setListDepartamentos(
	List<Iuserdepthdr> listDepartamentos) {
	this.listDepartamentos = listDepartamentos;
    }

    // /**
    // * Obtiene el valor del parámetro listGrupos.
    // *
    // * @return listGrupos lista a obtener.
    // */
    // public List<Iusergrouphdr> getListGrupos() {
    // return listGrupos;
    // }
    //
    // /**
    // * Guarda el valor del parámetro listGrupos.
    // *
    // * @param listGrupos
    // * lista de grupos a guardar.
    // */
    // public void setListGrupos(
    // List<Iusergrouphdr> listGrupos) {
    // this.listGrupos = listGrupos;
    // }

    // /**
    // * Obtiene el valor del parámetro listUsuarios.
    // *
    // * @return listUsuarios lista a obtener.
    // */
    // public List<Iuseruserhdr> getListUsuarios() {
    // return listUsuarios;
    // }
    //
    // /**
    // * Guarda el valor del parámetro listUsuarios.
    // *
    // * @param listUsuarios
    // * lista de usuarios a guardar.
    // */
    // public void setListUsuarios(
    // List<Iuseruserhdr> listUsuarios) {
    // this.listUsuarios = listUsuarios;
    // }

    /**
     * Implementa el autocompletado del combo de tipos de asuntos.
     * 
     * @param query
     *            Parte del nombre del tipo de asunto.
     * 
     * @return suggestions Lista de tipos de asunto según el texto insertado.
     */
    public List<ScrCa> completeAsunt(
	String query) {
	List<ScrCa> suggestions = new ArrayList<ScrCa>();
	String queryCS;
	queryCS = Utils.converterToCS(query);
	for (ScrCa p : listAsuntos) {
	    if (Utils.converterToCS(
		p.getCode()).indexOf(
		queryCS) != -1 ||
		Utils.converterToCS(
		    p.getMatter()).indexOf(
		    queryCS) != -1 || Utils.converterToCS(
		    p.getCode() +
			" " + p.getMatter()).indexOf(
		    queryCS) != -1) {
		suggestions.add(p);
	    }
	}
	return suggestions;
    }

    /**
     * Implementa el autocompletado del combo de organismos.
     * 
     * @param query
     *            Parte del nombre del organismo.
     * 
     * @return suggestions Lista de organismos según el texto insertado.
     */
    public List<ScrOrg> completeOrgDestino(
	String query) {
	List<ScrOrg> suggestions = new ArrayList<ScrOrg>();
	UnitsBo unitsBo = new UnitsBo();
	try {   
	    suggestions = unitsBo.getScrOrg(useCaseConf, query, queryCompactSearchOrg);
	}
	catch (ValidationException e) {
	    LOG.error(
		ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE, e);
	}
	catch (AttributesException e) {
	    LOG.error(
		ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE, e);
	}
	catch (SessionException e) {
	    LOG.error(
		ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE, e);
	}
	return suggestions;
    }

    /**
     * Implementa el autocompletado del combo de organismos.
     * 
     * @param query
     *            Parte del nombre del organismo propio.
     * 
     * @return suggestions Lista de organismos según el texto insertado.
     */
    public List<ScrOrg> completeOrgOrigen(
	String query) {
	List<ScrOrg> suggestions = new ArrayList<ScrOrg>();
	UnitsBo unitsBo = new UnitsBo();
	try {
	    suggestions = unitsBo.getScrOrg(useCaseConf, query, queryCompactSearchOrg);
	}
	catch (ValidationException e) {
	    LOG.error(
		ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE, e);
	}
	catch (AttributesException e) {
	    LOG.error(
		ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE, e);
	}
	catch (SessionException e) {
	    LOG.error(
		ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE, e);
	}
	return suggestions;
    }

    /**
     * Devuelve una oficina con un id.
     * 
     * @param id
     *            Identificador de la oficina.
     * @return p Oficina devuelta según el id.
     */
    private ScrOfic getOficina(
	Integer id) {
	ScrOfic office = null;
	boolean officeFound = false;
	for (ScrOfic p : listOficinas) {
	    if (p.getId().equals(
		id) &&
		!officeFound) {
		office = p;
		officeFound = true;
	    }
	}
	return office;
    }

    /**
     * Devuelve un organismo con un id.
     * 
     * @param code
     *            Identificador del organismo.
     * @return p Organismo devuelto según el Id.
     */
    private ScrOrg getOrganismo(
	String code) {
	ScrOrg organism = null;
	UnitsBo unitsBo = new UnitsBo();
	try {
	    organism = unitsBo.getOrg(
		useCaseConf, code);
	}
	catch (ValidationException e) {
	    LOG.error(
		ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE, e);
	}
	catch (AttributesException e) {
	    LOG.error(
		ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE, e);
	}
	catch (SessionException e) {
	    LOG.error(
		ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE, e);
	}
	return organism;
    }

    /**
     * Introduce el nombre del campo y un valor y convierte el código que viene
     * del valor al texto correspondiente.
     * 
     * @param idFld
     *            Id del campo.
     * @param value
     *            Valor del campo.
     * @param type
     *            tipo de libro.
     * @return result El valor del campo al que pertenece el Id en formato
     *         texto.
     */
    public String getTextOfValue(
	int idFld, String value, int type) {
	String result = null;

	SimpleDateFormat format = new SimpleDateFormat(
	    "dd/MM/yyyy HH:mm:ss");
	SimpleDateFormat formatShort = new SimpleDateFormat(
	    "dd/MM/yyyy");
	SimpleDateFormat formatOld = new SimpleDateFormat(
	    "yyyy-MM-dd HH:mm:ss.SS");

	if (value != null &&
	    !"".equals(value.trim())) {
	    if (type == 1) { // ENTRADA
		switch (idFld) {
		// fechas
		case FIELD2:
		case FIELD4:
		    try {
			result = format.format(formatOld.parse(value));
		    }
		    catch (ParseException e) {
			result = value;
		    }
		    break;
		case FIELD12:
		    try {
			result = formatShort.format(formatOld.parse(value));
		    }
		    catch (ParseException e) {
			result = value;
		    }
		    break;
		// oficina de registro
		case FIELD5:
		    ScrOfic ofic = getOficina(new Integer(
			value));
		    result = ofic.getCode() +
			" " + ofic.getName();
		    break;
		// estado
		case FIELD6:
		    result = Utils.getItemList(
			hashEstados, new Integer(
			    value));
		    break;
		// organismo origen
		case FIELD7:
		    ScrOrg org = getOrganismo(value);
		    result = org.getCode() +
			" " + org.getName();
		    break;
		// organismo destino
		case FIELD8:
		    ScrOrg org8 = getOrganismo(value);
		    result = org8.getCode() +
			" " + org8.getName();
		    break;
		// tipo registro
		case FIELD11:
		    if ("0".equals(value)) {
			result = "";
		    }
		    result = Utils.getItemList(
			hashTipoRegistro, new Integer(
			    value));
		    break;
		// transporte
		case FIELD14:
		    if ("0".equals(value) ||
			"".equals(value)) {
			result = "";
		    }
		    Iterator<ScrTt> iterScrTT = listTransportes.iterator();
		    ScrTt scrtt = null;
		    while (iterScrTT.hasNext() &&
			result == null) {
			scrtt = iterScrTT.next();
			if (("0" + Integer.toString(scrtt.getId())).equals(value)) {
			    result = scrtt.getTransport();
			}
		    }

		    break;
		 // tipo registro
		 case FIELD1002:
		     if ("0".equals(value) || "".equals(value)  ) {
			 result = "";
		     }
		     else {
			 if (("1".equals(value))){
			     result = "Repetido";
			 }else {
			     if (("2".equals(value))){
				 result = "Borrado";
			     }
			 }
		     }
		  break;
		 		
		 default:
		    result = value;
		    break;
		}
	    }
	    else { // SALIDA
		switch (idFld) {
		// fechas
		case FIELD2:
		case FIELD4:
		    try {
			result = format.format(formatOld.parse(value));
		    }
		    catch (ParseException e) {
			result = value;
		    }
		    break;
		// oficina de registro
		case FIELD5:
		    ScrOfic ofic = getOficina(new Integer(
			value));
		    result = ofic.getCode() +
			" " + ofic.getName();
		    break;
		// estado
		case FIELD6:
		    result = Utils.getItemList(
			hashEstados, new Integer(
			    value));
		    break;
		// organismo origen
		case FIELD7:
		    ScrOrg org = getOrganismo(value);
		    result = org.getCode() +
			" " + org.getName();
		    break;
		// organismo destino
		case FIELD8:
		    ScrOrg org8 = getOrganismo(value);
		    result = org8.getCode() +
			" " + org8.getName();
		    break;
		// transporte
		case FIELD10:
		    if ("0".equals(value) ||
			"".equals(value)) {
			result = "";
		    }
		    Iterator<ScrTt> iterScrTT = listTransportes.iterator();
		    ScrTt scrtt = null;
		    while (iterScrTT.hasNext() &&
			result == null) {
			scrtt = iterScrTT.next();
			if (("0" + Integer.toString(scrtt.getId())).equals(value)) {
			    result = scrtt.getTransport();
			}
		    }

		    break;
		default:
		    result = value;
		    break;
		}
	    }
	}
	return result;
    }

    /**
     * Devuelve una lista de departamentos, grupos o usuarios.
     * 
     * @param useCaseConf
     *            Configuración de la aplicación.
     * 
     */
    @SuppressWarnings("unchecked")
    private void getDeptsGroupsUsers(
	UseCaseConf useCaseConf) {

	// UserConverter userConvert = new UserConverter();
	DepartConverter deparConvert = new DepartConverter();
	// GroupConverter groupConvert = new GroupConverter();
	try {
	    // Tipo de listado (2-departamento, 3-Grupo, 1-Usuarios)
	    if (useCaseConf.getUseLdap().booleanValue()) {

		listDepartamentos = ValidationSessionEx.getGroupsUsersLDAP(
		    useCaseConf.getSessionID(), TYPEDEP, useCaseConf.getEntidadId());
		// listGrupos = ValidationSessionEx.getGroupsUsersLDAP(
		// useCaseConf.getSessionID(), TYPEGROUP,
		// useCaseConf.getEntidadId());
		// listUsuarios = ValidationSessionEx.getGroupsUsersLDAP(
		// useCaseConf.getSessionID(), TYPEUSER,
		// useCaseConf.getEntidadId());
	    }
	    else {
		listDepartamentos = ValidationListBo.getDeptsGroupsUsers(
		    useCaseConf.getSessionID(), TYPEDEP, useCaseConf.getEntidadId(), null);
		// listGrupos = ValidationSessionEx.getDeptsGroupsUsers(
		// useCaseConf.getSessionID(), TYPEGROUP,
		// useCaseConf.getEntidadId());
		// listUsuarios = getUsersNotFromElectronicRegister(
		// useCaseConf.getSessionID(), useCaseConf.getEntidadId());
	    }
	    // userConvert.setListUsuarios(listUsuarios);
	    deparConvert.setListDepart(listDepartamentos);
	    // groupConvert.setListGrupos(listGrupos);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(
		null, sessionException, null);
	}
	catch (ValidationException validationException) {
	    LOG.error(
		ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, validationException);
	    Utils.redirectToErrorPage(
		null, validationException, null);
	}
    }

    /**
     * 
     * Metodo que obtiene el listado de usuarios que no pertenezcan al organismo
     * de Registro Electrónico.
     * 
     * @param sessionID
     *            Id de sesión.
     * @param entidad
     *            Entidad.
     * @return Lista de usuarios.
     * @throws SessionException
     *             Si se ha producido una excepción en la sesión.
     * @throws ValidationException
     *             Si se ha producido un error en la validación.
     */
    public static List<Iuseruserhdr> getUsersNotFromElectronicRegister(
	String sessionID, String entidad)
	throws SessionException, ValidationException {
	Transaction tran = null;
	List<Iuseruserhdr> userList = null;
	HibernateUtil hibernateUtil = new HibernateUtil();
	try {
	    Validator.validate_String_NotNull_LengthMayorZero(
		sessionID, ValidationException.ATTRIBUTE_SESSION);
	    Session session = hibernateUtil.currentSession(entidad);
	    tran = session.beginTransaction();
	    // Recuperamos la sesion
	    CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
		sessionID);
	    AuthenticationUser user = (AuthenticationUser) cacheBag.get(HIBERNATEIUSERUSERHDR);
	    userList = getUsers(
		session, user.getId());
	    hibernateUtil.commitTransaction(tran);
	    return userList;
	}
	catch (SessionException sE) {
	    throw sE;
	}
	catch (Exception e) {
	    LOG.error(
		"Impossible to find deptsGroupsUserList", e);
	    throw new ValidationException(
		ValidationException.ERROR_USERS_LIST_NOT_FOUND);
	}
	finally {
	    hibernateUtil.closeSession(entidad);
	}
    }

    /**
     * Obtiene los usuarios de base de datos, que no pertenezcan al organismo de
     * Registro Electrónico.
     * 
     * @param session
     *            Sesión.
     * @param userId
     *            Id de usuario que realiza la petición.
     * @return Lista de usuarios.
     * @throws HibernateException
     *             Si se ha producido un error en la consulta con Hibernate.
     */
    @SuppressWarnings("unchecked")
    public static List<Iuseruserhdr> getUsers(
	Session session, Integer userId)
	throws HibernateException {
	StringBuffer query = new StringBuffer();
	query.append("FROM ");
	query.append(HIBERNATEIUSERUSERHDR);
	query.append(" scr WHERE scr.id != ? AND scr.deptid NOT IN");
	query.append(" (SELECT deptid.id FROM ");
	query.append(HIBERNATEIUSERDEPTHDR);
	query.append(" deptid WHERE deptid.type = 1)");
	query.append(" ORDER BY scr.name");
	return session.find(
	    query.toString(), new Object[] { userId }, new Type[] { Hibernate.INTEGER });
    }

    public List<ScrCCAA> getListCCAA() {
        return listCCAA;
    }

    public void setListCCAA(
        List<ScrCCAA> listCCAA) {
        this.listCCAA = listCCAA;
    }
    /**
     * Obtiene el valor del parámetro listProv.
     * 
     * @return listProv código de error a obtener.
     */
    public List<ScrProv> getListProv() {
        return listProv;
    }
    /**
     * Guarda el valor del parámetro listProv.
     * 
     * @param listProv
     *            Código de error a guardar.
     */
    public void setListProv(
        List<ScrProv> listProv) {
        this.listProv = listProv;
    }
    
}