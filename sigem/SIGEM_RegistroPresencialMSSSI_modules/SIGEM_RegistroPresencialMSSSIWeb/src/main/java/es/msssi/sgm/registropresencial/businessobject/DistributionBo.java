/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.businessobject;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.expression.Expression;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.conf.BookConf;
import com.ieci.tecdoc.common.conf.BookTypeConf;
import com.ieci.tecdoc.common.conf.InvesicresConf;
import com.ieci.tecdoc.common.entity.dao.DBEntityDAOFactory;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.DistributionException;
import com.ieci.tecdoc.common.exception.SecurityException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrDistreg;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.DistributionResults;
import com.ieci.tecdoc.common.isicres.DtrFdrResults;
import com.ieci.tecdoc.common.keys.HibernateKeys;
import com.ieci.tecdoc.common.utils.BBDDUtils;
import com.ieci.tecdoc.common.utils.ISDistribution;
import com.ieci.tecdoc.common.utils.ScrRegStateByLanguage;
import com.ieci.tecdoc.idoc.utils.ConfiguratorInvesicres;
import com.ieci.tecdoc.isicres.events.exception.EventException;
import com.ieci.tecdoc.isicres.session.book.BookSession;
import com.ieci.tecdoc.isicres.session.distribution.DistributionSession;
import com.ieci.tecdoc.isicres.session.distribution.DistributionSessionUtil;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.session.security.SecuritySession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.Validator;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.manager.DocumentoElectronicoAnexoManager;
import es.msssi.sgm.registropresencial.beans.DistributionResultsBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.RowSearchDistributionBean;
import es.msssi.sgm.registropresencial.beans.SearchDistributionBean;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPDistributionErrorCode;
import es.msssi.sgm.registropresencial.errors.RPDistributionException;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.ResourceRP;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase q implementa IGenericBo que contiene los métodos relacionados con la
 * distribución.
 * 
 * @author cmorenog
 * 
 */
public class DistributionBo extends DistributionSessionUtil implements IGenericBo {

    private static final Logger LOG = Logger.getLogger(DistributionBo.class.getName());
private static DocumentoElectronicoAnexoManager documentoElectronicoAnexoManager;
    
    static {
	if (documentoElectronicoAnexoManager == null) {
	    documentoElectronicoAnexoManager =
		    IsicresManagerProvider.getInstance().getDocumentoElectronicoAnexoManager();
	}
    }

    /**
     * Devuelve la lista de alarmas de la bandeja de entrada de distribución.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @return lista de alarmas de distribución
     * @throws DistributionException
     *             error la distribución
     * @throws SessionException
     *             sesión nula
     * @throws ValidationException
     *             error validación
     * */
    public List<String> getListMessageInit(
	UseCaseConf useCaseConf)
	throws DistributionException, SessionException, ValidationException {
	ArrayList<String> result = new ArrayList<String>();
	int sizeNewDist = 0;
	int sizeRejectedDist = 0;

	// nuevos
	sizeNewDist = DistributionSession.getNewFolderDistByDeptId(
	    useCaseConf.getSessionID(), useCaseConf.getEntidadId());

	// rechazados
	sizeRejectedDist = DistributionSession.getRejectedFolderDistByDeptId(
	    useCaseConf.getSessionID(), useCaseConf.getEntidadId());

	if (sizeNewDist == 1) {
	    result.add(ResourceRP.getInstance(
		useCaseConf.getLocale()).getProperty(
		KeysRP.I18N_DISTRIBUTION_NEW_FOLDER_FOR_USER));
	}
	else if (sizeNewDist > 1) {
	    result.add(MessageFormat.format(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_DISTRIBUTION_NEW_FOLDERS_FOR_USER),
		new Object[] { Integer.toString(sizeNewDist) }));
	}

	if (sizeRejectedDist == 1) {
	    result.add(ResourceRP.getInstance(
		useCaseConf.getLocale()).getProperty(
		KeysRP.I18N_DISTRIBUTION_NEW_FOLDER_REJECTED_FOR_USER));
	}
	else if (sizeRejectedDist > 1) {
	    result.add(MessageFormat.format(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_DISTRIBUTION_NEW_FOLDERS_REJECTED_FOR_USER),
		new Object[] { Integer.toString(sizeRejectedDist) }));
	}
	return result;
    }

    /**
     * Histórico de distribución de un registro.
     * 
     * @param useCaseConf
     *            configuración de la aplicación
     * @param bookID
     *            identificador del libro
     * @param folderID
     *            identificador del registro
     * @return lista de modificaciones de estado de la distribución del registro
     * @throws BookException
     *             error en el libro
     * @throws SessionException
     *             sesión nula
     * @throws ValidationException
     *             error validación
     * */
    @SuppressWarnings("unchecked")
    public List<DtrFdrResults> getHistDistribution(
	UseCaseConf useCaseConf, Integer bookID, Integer folderID)
	throws BookException, SessionException, ValidationException {
	List<DtrFdrResults> result = null;
	result =
	    DistributionSession.getDtrFdrResults(
		useCaseConf.getSessionID(), bookID, folderID, useCaseConf.getEntidadId(),
		useCaseConf.getUseLdap().booleanValue());
	return result;
    }

    /**
     * Método que construye la query y controla los permisos para la búsqueda de
     * la distribución.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @param searchDistributionRegister
     *            parámetros de búsqueda.
     * @param firstRow
     *            primera fila.
     * @param pageSize
     *            tamaño búsqueda.
     * @param isOfficeAsoc
     *            si tiene oficina asociada.
     * @param sortField
     *            campo que ordena.
     * @return objeto con los datos necesarios para la búsqueda.
     * @throws ValidationException
     *             error en la validación
     * @throws DistributionException
     *             error en la distribución
     * @throws SessionException
     *             error de sesión
     * 
     * */
    @SuppressWarnings("unchecked")
    public DistributionResultsBean getDistribution(
	UseCaseConf useCaseConf, SearchDistributionBean searchDistributionRegister, int firstRow,
	int pageSize, boolean isOfficeAsoc, String sortField)
	throws ValidationException, DistributionException, SessionException {
	DistributionResultsBean result = null;
	HibernateUtil hibernateUtil = new HibernateUtil();
	BBDDUtils bbddUtils = new BBDDUtils();
	Transaction tran = null;
	try {
	    Session session = hibernateUtil.currentSession(useCaseConf.getEntidadId());
	    tran = session.beginTransaction();

	    // Recuperamos la sesión
	    CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
		useCaseConf.getSessionID());
	    AuthenticationUser user =
		(AuthenticationUser) cacheBag.get(HibernateKeys.HIBERNATE_Iuseruserhdr);
	    /* fecha del servidor */
	    Date currentDate =
		bbddUtils.getDateFromTimestamp(DBEntityDAOFactory.getCurrentDBEntityDAO()
		    .getDBServerDate(
			useCaseConf.getEntidadId()));

	    // lista de grupos del usuario
	    List<Integer> iUserGroupUser = DistributionSessionUtil.getUserGroups(
		session, user, useCaseConf.getUseLdap());

	    StringBuffer finalWhere =
		getWhereQuery(
		    session, user, iUserGroupUser, searchDistributionRegister, isOfficeAsoc,
		    useCaseConf.getEntidadId(), sortField);

	    // Número total de registros
	    int distributionCount = getDistributionCount(
		finalWhere.toString(), useCaseConf.getEntidadId());

	    Criteria criteriaResults = session.createCriteria(ScrDistreg.class);
	    criteriaResults.setFirstResult(firstRow);
	    criteriaResults.setMaxResults(pageSize);
	    criteriaResults.add(Expression.sql(finalWhere.toString()));
	    List<ScrDistreg> list = criteriaResults.list();

	    result =
		getDistResults(
		    list, distributionCount, currentDate, searchDistributionRegister.getType(),
		    useCaseConf);

	    if (session.isOpen()) {
		hibernateUtil.commitTransaction(tran);
	    }
	}
	catch (DistributionException distributionException) {
	    LOG.error(
		ErrorConstants.GET_DISTRIBUTION_ERROR_MESSAGE, distributionException);
	    Utils.redirectToErrorPage(
		null, distributionException, null);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_DISTRIBUTION_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(
		null, sessionException, null);
	}
	catch (Exception e) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(
		"No ha sidpo posible obtener la distribución de la sesión [" +
		    useCaseConf.getSessionID() + "]", e);
	    throw new DistributionException(
		DistributionException.ERROR_CANNOT_OBTAIN_DISTRIBUTION);
	}
	finally {
	    hibernateUtil.closeSession(useCaseConf.getEntidadId());
	}
	return result;
    }

    /**
     * Método que devuelve el where final de la query.
     * 
     * @param session
     *            sessión de bbdd.
     * @param user
     *            usuario logado.
     * @param iUserGroupUser
     *            id del grupo del usuario.
     * @param searchDistributionRegister
     *            parámetros de búsqueda.
     * @param oficAsoc
     *            si es oficina asociada.
     * @param entidad
     *            entidad.
     * @param sortField
     *            campo que ordena.
     * @return String el where.
     * @throws DistributionException
     *             error en la distribución
     * @throws HibernateException
     *             error de bbdd
     * 
     * */
    private StringBuffer getWhereQuery(
	Session session, AuthenticationUser user, List iUserGroupUser,
	SearchDistributionBean searchDistributionRegister, boolean oficAsoc, String entidad,
	String sortField)
	throws DistributionException, HibernateException {

	StringBuffer querySize = new StringBuffer();

	String processDistWhere = "";
	processDistWhere = getDistWhere(searchDistributionRegister);

	InvesicresConf invesicresConf = ConfiguratorInvesicres.getInstance(
	    entidad).getInvesicresConf();
	int autoArchiv = invesicresConf.getAutoArchiving();

	switch (searchDistributionRegister.getType()) {
	case DISTRIBUCION_IN_DIST:
	    querySize.append(getInDistQuery(
		user, iUserGroupUser, null, oficAsoc));
	    if (autoArchiv != 0) {
		try {
		    autoArchiving(
			session, querySize.toString(), autoArchiv, user, entidad);
		}
		catch (Exception e) {
		    LOG.error(DistributionException.ERROR_DISTRIBUTION_TYPE_NOT_SUPPORTED);
		    throw new DistributionException(
			DistributionException.ERROR_DISTRIBUTION_TYPE_NOT_SUPPORTED);
		}
	    }
	    break;
	case DISTRIBUCION_OUT_DIST:
	    querySize.append(getOutDistQuery(
		user, iUserGroupUser, null, oficAsoc));
	    break;
	default:
	    LOG.error(DistributionException.ERROR_DISTRIBUTION_TYPE_NOT_SUPPORTED);
	    throw new DistributionException(
		DistributionException.ERROR_DISTRIBUTION_TYPE_NOT_SUPPORTED);
	}

	if (!"".equals(processDistWhere)) {
	    querySize.append("and " +
		processDistWhere);
	}
	if (sortField != null){
        	querySize.append(" order by " +
        	    sortField);
	}
	return querySize;
    }

    /**
     * Método que devuelve el where de la query.
     * 
     * @param searchDistributionRegister
     *            parámetros de búsqueda.
     * @return String el where.
     * 
     * */
    private String getDistWhere(
	SearchDistributionBean searchDistributionRegister) {
	String where = "";
	String whereDis = searchDistributionRegister.fieldsDisttoQuery();
	String queryReg = searchDistributionRegister.fieldsRegtoQuery();

	if (whereDis != null) {
	    where += whereDis;
	}
	if (queryReg != null) {
	    if (whereDis != null) {
		where += " AND ";
	    }
	    where += "EXISTS (" +
		queryReg + " AND IDBOOK = id_arch AND FDRID = id_fdr)";
	}
	return where;
    }

    /**
     * Método que transforma los resultados de la búsqueda de SIGM en un bean de
     * la aplicación.
     * 
     * @param list
     *            lista de resultados.
     * @param distributionCount
     *            número de resultados.
     * @param currentDate
     *            fecha actual.
     * @param typeDist
     *            entrada o salida.
     * @param useCaseConf
     *            configuración de la aplicación.
     * @return bean con los datos necesarios para la búsqueda.
     * @throws HibernateException
     *             error de bbdd.
     * @throws ValidationException
     *             error en la validación.
     * @throws DistributionException
     *             error en la distribución.
     * @throws BookException
     *             error en el libro.
     * @throws SessionException
     *             error de sesión.
     */
    private DistributionResultsBean getDistResults(
	List<ScrDistreg> list, int distributionCount, Date currentDate, int typeDist,
	UseCaseConf useCaseConf)
	throws HibernateException, ValidationException, DistributionException, BookException,
	SessionException {
	DistributionResultsBean distributionResults = new DistributionResultsBean();

	distributionResults.setTotalSize(distributionCount);
	distributionResults.setActualDate(currentDate);

	List<RowSearchDistributionBean> result = new ArrayList<RowSearchDistributionBean>();
	HashMap<Integer, String> libros = new HashMap<Integer, String>();
	RowSearchDistributionBean row;
	String book;
	AxSf axsf = null;
	String sourceDesc;
	String targetDesc;
	for (ScrDistreg scrDis : list) {
	    row = new RowSearchDistributionBean(
		scrDis);

	    // Integer id1 = getDistributionResultDistType(session, row,
	    // useCaseConf.getEntidadId());
	    if (typeDist == DISTRIBUCION_IN_DIST) {
		row.setOrigenDist(getDistributionResultId(
		    row, typeDist, useCaseConf.getEntidadId(), useCaseConf.getUseLdap()));
	    }
	    else {
		row.setDestinoDist(getDistributionResultId(
		    row, typeDist, useCaseConf.getEntidadId(), useCaseConf.getUseLdap()));
	    }
	    if (!libros.containsKey(row.getIdArch())) {
		try {
		    BookSession.openBook(
			useCaseConf.getSessionID(), row.getIdArch(), useCaseConf.getEntidadId());

		    book =
			BookSession.getBookName(
			    useCaseConf.getSessionID(), row.getIdArch(), useCaseConf.getLocale(),
			    useCaseConf.getEntidadId());
		    libros.put(
			row.getIdArch(), book);
		}
		catch (BookException bookException) {
		    LOG.error(
			ErrorConstants.GET_DISTRIBUTION_RESULTS_ERROR_MESSAGE, bookException);
		    Utils.redirectToErrorPage(
			null, bookException, null);
		}
		catch (SessionException sessionException) {
		    LOG.error(
			ErrorConstants.GET_DISTRIBUTION_RESULTS_ERROR_MESSAGE, sessionException);
		    Utils.redirectToErrorPage(
			null, sessionException, null);
		}
	    }
	    axsf =
		FolderSession.getBookFolder(
		    useCaseConf.getSessionID(), row.getIdArch(), row.getIdFdr(),
		    useCaseConf.getLocale(), useCaseConf.getEntidadId());
	    row.setAxsf(axsf);
	    row.setNameBook(libros.get(row.getIdArch()));

	    sourceDesc =
		DistributionSession.getOrigDestDescription(
		    useCaseConf.getSessionID(), scrDis, true, useCaseConf.getEntidadId(),
		    useCaseConf.getUseLdap().booleanValue());
	    row.setOrigenDist(sourceDesc);
	    targetDesc =
		DistributionSession.getOrigDestDescription(
		    useCaseConf.getSessionID(), scrDis, false, useCaseConf.getEntidadId(),
		    useCaseConf.getUseLdap().booleanValue());
	    row.setDestinoDist(targetDesc);

	    row.setHistDistribution(getHistDistribution(
		useCaseConf, row.getIdArch(), row.getIdFdr()));
	    result.add(row);
	}

	distributionResults.setRows(result);
	distributionResults.setBooks(libros);

	LOG.debug("Recuperada una lista de distribucion [" +
	    result.size() + "/" + distributionResults.getTotalSize() + "].");

	return distributionResults;
    }

    /**
     * Método que archiva las distribuciones que pasan como parámetros y
     * devuelve una lista de distribuciones que si se han podido modificar
     * porque estaban aceptadas.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @param selectedResults
     *            lista de las distribuciones seleccionadas.
     * @return list lista con las distribuciones archivadas.
     * @throws RPGenericException
     *             error de sesión.
     * @throws RPDistributionException
     *             error en la distribución.
     * 
     */
    public List<ScrDistreg> archive(
	UseCaseConf useCaseConf, RowSearchDistributionBean[] selectedResults)
	throws RPGenericException, RPDistributionException {
	List<ScrDistreg> archidFdr = new ArrayList<ScrDistreg>();
	ScrDistreg scrDistReg;
	Transaction tran = null;
	ScrDistreg dis;
	HibernateUtil hibernateUtil = new HibernateUtil();
	try {
	    Validator.validate_String_NotNull_LengthMayorZero(
		useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);
	    LOG.info("Archivando distribuciones");
	    Session session = hibernateUtil.currentSession(useCaseConf.getEntidadId());
	    tran = session.beginTransaction();

	    // Recuperamos la sesión
	    CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
		useCaseConf.getSessionID());
	    AuthenticationUser user = (AuthenticationUser) cacheBag.get(HIBERNATE_Iuseruserhdr);

	    for (RowSearchDistributionBean rowDistReg : selectedResults) {
		if (rowDistReg.getState() == ISDistribution.STATE_ACEPTADO) {
		    dis = rowDistReg.toFather();
		    lockFolderDist(
			session, user, Integer.valueOf(rowDistReg.getIdArch()),
			rowDistReg.getIdFdr(), useCaseConf.getEntidadId());
		    scrDistReg =
			updateDistReg(
			    session, user, useCaseConf.getSessionID(), DISTRIBUTION_ARCHIVE_EVENT,
			    dis, ISDistribution.STATE_ARCHIVADO, rowDistReg.getId(), null,
			    useCaseConf.getEntidadId());
		    FolderSession.closeFolder(
			useCaseConf.getSessionID(), Integer.valueOf(scrDistReg.getIdArch()),
			scrDistReg.getIdFdr(), useCaseConf.getEntidadId());
		    archidFdr.add(scrDistReg);
		}
	    }
	    try {
		hibernateUtil.commitTransaction(tran);
	    }
	    catch (HibernateException e) {
		LOG.error(
		    ErrorConstants.CLOSE_SESSION_BD_ERROR_MESSAGE, e);
	    }
	}
	catch (DistributionException e) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(
		ErrorConstants.ARCHIVE_DISTRIBUTION_ERROR_MESSAGE, e);
	    throw new RPDistributionException(
		RPDistributionErrorCode.ARC_DISTRIBUTION_ERROR,
		ErrorConstants.ARCHIVE_DISTRIBUTION_ERROR_MESSAGE +
		    ". " + e.getMessage(), e);
	}
	catch (SessionException sessionException) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(
		ErrorConstants.GET_BOOKS_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(
		RPGenericErrorCode.SESSION_ERROR, ErrorConstants.SESSION_ERROR_MESSAGE,
		sessionException);
	}
	catch (Exception exception) {
	    LOG.error(
		ErrorConstants.ARCHIVE_DISTRIBUTION_ERROR_MESSAGE, exception);
	    throw new RPDistributionException(
		RPDistributionErrorCode.ARC_DISTRIBUTION_ERROR,
		ErrorConstants.ARCHIVE_DISTRIBUTION_ERROR_MESSAGE +
		    ". " + exception.getMessage(), exception);
	}
	finally {
	    hibernateUtil.closeSession(useCaseConf.getSessionID());
	}
	return archidFdr;
    }

    /**
     * Método que aceptar las distribuciones que pasan como parámetros y
     * devuelve una lista de distribuciones que si se han podido modificars.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @param selectedResults
     *            lista de las distribuciones seleccionadas.
     * @param bookId
     *            id del libro.
     * @param tipoRegistros
     * 		tipo de libros.
     * @return list lista con las distribuciones aceptadas.
     * @throws RPDistributionException
     *             error en la distribución.
     * 
     */
    @SuppressWarnings("unchecked")
    public List<ScrDistreg> accept(
	UseCaseConf useCaseConf, RowSearchDistributionBean[] selectedResults,
	Integer bookId, int tipoRegistros)
	throws RPDistributionException {

	List<ScrDistreg> archidFdr = new ArrayList<ScrDistreg>();

	try {
	    List<Integer> createPermBooks = null;
	    Map<Integer, String> createPermBooksInfo = null;
	    if (bookId.intValue() == 0) {
		List<ScrRegStateByLanguage> list = null;
		try {
		    if (tipoRegistros == 1){
			list =
        			BookSession.getInBooks(
        			    useCaseConf.getSessionID(), useCaseConf.getLocale(),
        			    useCaseConf.getEntidadId());
        		    boolean canCreate = false;
        		    createPermBooks = new ArrayList<Integer>();
        		    createPermBooksInfo = new HashMap<Integer, String>();
        		    for (Iterator<ScrRegStateByLanguage> it = list.iterator(); it.hasNext();) {
        			ScrRegStateByLanguage scrRegStateByLanguage =
        			    (ScrRegStateByLanguage) it.next();
        			Integer id = scrRegStateByLanguage.getIdocarchhdrId();
        			String name = scrRegStateByLanguage.getIdocarchhdrName();
        			BookSession.openBook(
        			    useCaseConf.getSessionID(), id, useCaseConf.getEntidadId());
        
        			canCreate = SecuritySession.canCreate(
        			    useCaseConf.getSessionID(), id);
        			if (canCreate) {
        			    createPermBooks.add(id);
        			    createPermBooksInfo.put(
        				id, name);
        			}
        		    }
		    }else {
			list =
	        		BookSession.getOutBooks(
	        			    useCaseConf.getSessionID(), useCaseConf.getLocale(),
	        			    useCaseConf.getEntidadId());
	        	boolean canCreate = false;
	        	createPermBooks = new ArrayList<Integer>();
	        	createPermBooksInfo = new HashMap<Integer, String>();
	        	for (Iterator<ScrRegStateByLanguage> it = list.iterator(); it.hasNext();) {
	        		ScrRegStateByLanguage scrRegStateByLanguage =
	        			    (ScrRegStateByLanguage) it.next();
	        		Integer id = scrRegStateByLanguage.getIdocarchhdrId();
	        		String name = scrRegStateByLanguage.getIdocarchhdrName();
	        		BookSession.openBook(
	        			    useCaseConf.getSessionID(), id, useCaseConf.getEntidadId());
	        
	        		canCreate = SecuritySession.canCreate(
	        		    useCaseConf.getSessionID(), id);
	        		if (canCreate) {
	        		    createPermBooks.add(id);
	        		    createPermBooksInfo.put(id, name);
	        		}
	        	}
		    }
		}
		catch (BookException bookException) {
		    LOG.error(
			ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
		    throw new RPDistributionException(
			RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
			ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
		}
		catch (SessionException sessionException) {
		    LOG.error(
			ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, sessionException);
		    throw new RPDistributionException(
			RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
			ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, sessionException);
		}
		catch (ValidationException e) {
		    LOG.error(
			ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, e);
		    throw new RPDistributionException(
			RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
			ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, e);
		}
		catch (SecurityException e) {
		    LOG.error(
			ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, e);
		    throw new RPDistributionException(
			RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
			ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, e);
		}
	    }
	    Object result = null;
	    Integer launchDistOutRegister = Integer.valueOf(BookSession.invesicresConf(
		useCaseConf.getEntidadId()).getDistSRegister());

	    Map<String, ScrDistreg> disId = new HashMap<String, ScrDistreg>();
	    Map<Integer, Map<String, ScrDistreg>> rowDis =
		new HashMap<Integer, Map<String, ScrDistreg>>();
	    List<Integer> ids = new ArrayList<Integer>();
	    for (RowSearchDistributionBean row : selectedResults) {
		disId.put(
		    String.valueOf(row.toFather().getIdFdr()), row.toFather());
		rowDis.put(
		    row.getId(), disId);
		ids.add(row.getId());
	    }

	    DistributionResults disResult = new DistributionResults();
	    disResult.setResults(rowDis);

	    try {
		result =
		    acceptDistribution(
			useCaseConf, ids, 0, 0, 0, 0, bookId, createPermBooks,
			disResult, null, null, launchDistOutRegister, useCaseConf.getLocale(),
			useCaseConf.getEntidadId());
	    }
	    catch (BookException bookException) {
		LOG.error(
		    ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
		throw new RPDistributionException(
		    RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
		    ErrorConstants.ACCEPT_DISTRIBUTION_ERROR_MESSAGE +
			". " + bookException.getMessage(), bookException);
	    }
	    catch (DistributionException e) {
		LOG.error(
		    ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, e);
		throw new RPDistributionException(
		    RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
		    ErrorConstants.ACCEPT_DISTRIBUTION_ERROR_MESSAGE +
			". " + e.getMessage(), e);
	    }
	    catch (SessionException sessionException) {
		LOG.error(
		    ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, sessionException);
		throw new RPDistributionException(
		    RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
		    ErrorConstants.ACCEPT_DISTRIBUTION_ERROR_MESSAGE +
			". " + sessionException.getMessage(), sessionException);
	    }
	    catch (ValidationException e) {
		LOG.error(
		    ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, e);
		throw new RPDistributionException(
		    RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
		    ErrorConstants.ACCEPT_DISTRIBUTION_ERROR_MESSAGE +
			". " + e.getMessage(), e);
	    }

	    if (result instanceof List) {
		archidFdr = (List<ScrDistreg>) result;
	    }
	}
	finally {
	    ScrDistreg distReg = null;
	    if (archidFdr != null &&
		!archidFdr.isEmpty()) {
		for (Iterator<ScrDistreg> it4 = archidFdr.iterator(); it4.hasNext();) {
		    distReg = (ScrDistreg) it4.next();
		    try {
			FolderSession.closeFolder(
			    useCaseConf.getSessionID(), Integer.valueOf(distReg.getIdArch()),
			    distReg.getIdFdr(), useCaseConf.getEntidadId());
		    }
		    catch (BookException bookException) {
			LOG.error(
			    ErrorConstants.CLOSE_BOOK_ERROR_MESSAGE, bookException);
			throw new RPDistributionException(
			    RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
			    ErrorConstants.CLOSE_BOOK_ERROR_MESSAGE, bookException);
		    }
		    catch (SessionException e) {
			LOG.error(
			    ErrorConstants.CLOSE_BOOK_ERROR_MESSAGE, e);
			throw new RPDistributionException(
			    RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
			    ErrorConstants.CLOSE_BOOK_ERROR_MESSAGE, e);
		    }
		    catch (ValidationException e) {
			LOG.error(
			    ErrorConstants.CLOSE_BOOK_ERROR_MESSAGE, e);
			throw new RPDistributionException(
			    RPDistributionErrorCode.ACEPTAR_DISTRIBUTION_ERROR,
			    ErrorConstants.CLOSE_BOOK_ERROR_MESSAGE, e);
		    }
		}
	    }
	}
	return archidFdr;
    }

    /**
     * Método que rechaza las distribuciones que pasan como parámetros y
     * devuelve una lista de distribuciones que si se han podido modificar.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @param selectedResults
     *            lista de las distribuciones seleccionadas.
     * @param motivo
     *            motivo del rechazo.
     * @return list lista con las distribuciones rechazadas.
     * @throws RPGenericException
     *             error de sesión.
     * @throws RPDistributionException
     *             error en la distribución.
     */
    public List<ScrDistreg> reject(
	UseCaseConf useCaseConf, RowSearchDistributionBean[] selectedResults, String motivo)
	throws RPDistributionException, RPGenericException {
	List<ScrDistreg> archidFdr = new ArrayList<ScrDistreg>();
	HibernateUtil hibernateUtil = new HibernateUtil();
	try {
	    Validator.validate_String_NotNull_LengthMayorZero(
		useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);
	}
	catch (ValidationException e2) {
	    LOG.error(
		ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, e2);
	    throw new RPGenericException(
		RPGenericErrorCode.ATTRIBUTES_ERROR, ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, e2);
	}
	Session session = null;
	Transaction tran = null;
	try {
	    try {
		session = hibernateUtil.currentSession(useCaseConf.getEntidadId());
		tran = session.beginTransaction();
	    }
	    catch (HibernateException e1) {
		LOG.error(
		    ErrorConstants.OPEN_SESSION_BD_ERROR_MESSAGE, e1);
		throw new RPGenericException(
		    RPGenericErrorCode.OPEN_SESSION_BD_ERROR_MESSAGE,
		    ErrorConstants.OPEN_SESSION_BD_ERROR_MESSAGE, e1);
	    }

	    // Recuperamos la sesión
	    CacheBag cacheBag = null;
	    AuthenticationUser user = null;
	    try {
		cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
		    useCaseConf.getSessionID());

		user = (AuthenticationUser) cacheBag.get(HIBERNATE_Iuseruserhdr);
	    }
	    catch (TecDocException e1) {
		LOG.error(
		    ErrorConstants.GET_USER_ERROR_MESSAGE, e1);
		throw new RPGenericException(
		    RPGenericErrorCode.GET_USER_ERROR_MESSAGE,
		    ErrorConstants.GET_USER_ERROR_MESSAGE, e1);
	    }
	    ScrDistreg scrDistReg;

	    for (RowSearchDistributionBean row : selectedResults) {
		if (row.getState() == ISDistribution.STATE_PENDIENTE ||
		    row.getState() == ISDistribution.STATE_ACEPTADO) {
		    try {
			scrDistReg = row.toFather();
			lockFolderDist(
			    session, user, row.getIdArch(), row.getIdFdr(),
			    useCaseConf.getEntidadId());
			scrDistReg =
			    updateDistReg(
				session, user, useCaseConf.getSessionID(),
				DISTRIBUTION_REJECT_EVENT, scrDistReg,
				ISDistribution.STATE_RECHAZADO, scrDistReg.getId(), motivo,
				useCaseConf.getEntidadId());

			DBEntityDAOFactory.getCurrentDBEntityDAO().deleteDistAccept(
			    Integer.valueOf(scrDistReg.getIdArch()), scrDistReg.getIdFdr(),
			    useCaseConf.getEntidadId());

			FolderSession.closeFolder(
			    useCaseConf.getSessionID(), Integer.valueOf(scrDistReg.getIdArch()),
			    scrDistReg.getIdFdr(), useCaseConf.getEntidadId());
			archidFdr.add(scrDistReg);
		    }
		    catch (DistributionException e) {
			hibernateUtil.rollbackTransaction(tran);
			LOG.error(
			    ErrorConstants.REJECT_DISTRIBUTION_ERROR_MESSAGE, e);
			throw new RPDistributionException(
			    RPDistributionErrorCode.RECHAZAR_DISTRIBUTION_ERROR,
			    ErrorConstants.REJECT_DISTRIBUTION_ERROR_MESSAGE +
				". " + e.getMessage(), e);
		    }
		    catch (SessionException sessionException) {
			hibernateUtil.rollbackTransaction(tran);
			LOG.error(
			    ErrorConstants.GET_BOOKS_ERROR_MESSAGE, sessionException);
			throw new RPGenericException(
			    RPGenericErrorCode.SESSION_ERROR, ErrorConstants.SESSION_ERROR_MESSAGE,
			    sessionException);
		    }
		    catch (EventException eE) {
			hibernateUtil.rollbackTransaction(tran);
			throw eE;
		    }
		    catch (Exception e) {
			hibernateUtil.rollbackTransaction(tran);
			LOG.error(
			    ErrorConstants.REJECT_DISTRIBUTION_ERROR_MESSAGE, e);
			throw new RPDistributionException(
			    RPDistributionErrorCode.RECHAZAR_DISTRIBUTION_ERROR,
			    ErrorConstants.REJECT_DISTRIBUTION_ERROR_MESSAGE, e);
		    }
		}
	    }
	    try {
		hibernateUtil.commitTransaction(tran);
	    }
	    catch (HibernateException e) {
		LOG.error(
		    ErrorConstants.CLOSE_SESSION_BD_ERROR_MESSAGE, e);
	    }

	    return archidFdr;
	}
	finally {
	    hibernateUtil.closeSession(useCaseConf.getEntidadId());
	}

    }

    /**
     * Método que cambia de destino las distribuciones que pasan como
     * parámetros.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @param selectedResults
     *            lista de las distribuciones seleccionadas.
     * @param destinoCambio
     *            organismo del nuevo destino.
     * @param typeDist
     *            tipo distribución (entrada o salida).
     * @throws RPGenericException
     *             error de sesión.
     * @throws RPDistributionException
     *             error en la distribución.
     */
    public void change(
	UseCaseConf useCaseConf, RowSearchDistributionBean[] selectedResults, ScrOrg destinoCambio,
	int typeDist)
	throws RPDistributionException, RPGenericException {
	Integer launchDistOutRegister = Integer.valueOf(BookSession.invesicresConf(
	    useCaseConf.getEntidadId()).getDistSRegister());
	Integer canDestWithoutList = Integer.valueOf(BookSession.invesicresConf(
	    useCaseConf.getEntidadId()).getCanChangeDestWithoutList());

	List<Integer> ids = new ArrayList<Integer>();
	for (RowSearchDistributionBean row : selectedResults) {
	    ids.add(row.getId());
	}

	try {
	    DistributionSession.changeDistribution(
		useCaseConf.getSessionID(), ids, destinoCambio.getCode(), typeDist,
		launchDistOutRegister, canDestWithoutList, useCaseConf.getLocale(),
		useCaseConf.getEntidadId());
	}
	catch (BookException bookException) {
	    LOG.error(
		ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
	    throw new RPDistributionException(
		RPDistributionErrorCode.CAMBIARDEST_DISTRIBUTION_ERROR,
		ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_BOOKS_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(
		RPGenericErrorCode.SESSION_ERROR, ErrorConstants.SESSION_ERROR_MESSAGE,
		sessionException);
	}
	catch (ValidationException e) {
	    LOG.error(
		ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, e);
	    throw new RPGenericException(
		RPGenericErrorCode.ATTRIBUTES_ERROR, ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, e);
	}
	catch (DistributionException e) {
	    LOG.error(
		ErrorConstants.CHANGE_DISTRIBUTION_ERROR_MESSAGE, e);
	    throw new RPDistributionException(
		RPDistributionErrorCode.CAMBIARDEST_DISTRIBUTION_ERROR,
		ErrorConstants.CHANGE_DISTRIBUTION_ERROR_MESSAGE +
		    ". " + e.getMessage(), e);
	}
    }

    /**
     * Metodo que redirecciona una distribucion.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @param selectedResults
     *            Ids de las distribuciones a modificar.
     * @param typeDist
     *            Tipo de distribucion (Entrada/Salida).
     * @param messageForUser
     *            Mensaje de la distribucion.
     * @param userType
     *            Tipo de Destino de la distribucion.
     * @param userId
     *            Id del destino de la distribucion.
     * @throws RPGenericException
     *             error de sesión.
     * @throws RPDistributionException
     *             error en la distribución.
     */
    public void redistribution(
	UseCaseConf useCaseConf, RowSearchDistributionBean[] selectedResults, int typeDist,
	String messageForUser, Integer userType, Integer userId)
	throws RPDistributionException, RPGenericException {
	// obtenemos los datos de configuracion
	Integer canDestWithoutList = Integer.valueOf(BookSession.invesicresConf(
	    useCaseConf.getEntidadId()).getCanChangeDestWithoutList());

	List<Integer> ids = new ArrayList<Integer>();
	for (RowSearchDistributionBean row : selectedResults) {
	    ids.add(row.getId());
	}

	try {
	    DistributionSession.redistributionDistribution(
		useCaseConf.getSessionID(), useCaseConf.getLocale(), useCaseConf.getEntidadId(),
		ids, userId, typeDist, canDestWithoutList, messageForUser, userType);
	}
	catch (BookException bookException) {
	    LOG.error(
		ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
	    throw new RPDistributionException(
		RPDistributionErrorCode.REDISTRIBUTION_DISTRIBUTION_ERROR,
		ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_BOOKS_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(
		RPGenericErrorCode.SESSION_ERROR, ErrorConstants.SESSION_ERROR_MESSAGE,
		sessionException);
	}
	catch (ValidationException e) {
	    LOG.error(
		ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, e);
	    throw new RPGenericException(
		RPGenericErrorCode.ATTRIBUTES_ERROR, ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, e);
	}
	catch (DistributionException e) {
	    LOG.error(
		ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
	    throw new RPDistributionException(
		RPDistributionErrorCode.REDISTRIBUTION_DISTRIBUTION_ERROR,
		ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE +
		    ". " + e.getMessage(), e);
	}
    }

    /**
     * Metodo que redirecciona una distribucion.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @param ids
     *            ids de los registros a distribuir.
     * @param messageForUser
     *            Mensaje de la distribucion.
     * @param userType
     *            Tipo de Destino de la distribucion.
     * @param userId
     *            Id del destino de la distribucion.
     * @param bookId
     *            Id del libro.
     * @return String mensaje de la distribución.
     * @throws RPGenericException
     *             error de sesión.
     * @throws RPDistributionException
     *             error en la distribución.
     */
    public String createDistribution(
	UseCaseConf useCaseConf, List<Integer> ids, String messageForUser, Integer userType,
	Integer userId, Integer bookId)
	throws RPDistributionException, RPGenericException {
	String result = null;
	Integer folderID = null;

	try {
	    try {
		result =
		    DistributionSession.createDistribution(
			useCaseConf.getSessionID(), bookId, ids, userType, userId, messageForUser,
			useCaseConf.getLocale(), useCaseConf.getEntidadId());
	    }
	    finally {
		for (Iterator<Integer> it = ids.iterator(); it.hasNext();) {
		    folderID = (Integer) it.next();
		    FolderSession.closeFolder(
			useCaseConf.getSessionID(), bookId, folderID.intValue(),
			useCaseConf.getEntidadId());
		}
	    }
	}
	catch (BookException bookException) {
	    LOG.error(
		ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
	    throw new RPDistributionException(
		RPDistributionErrorCode.REDISTRIBUTION_DISTRIBUTION_ERROR,
		ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_BOOKS_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(
		RPGenericErrorCode.SESSION_ERROR, ErrorConstants.SESSION_ERROR_MESSAGE,
		sessionException);
	}
	catch (ValidationException e) {
	    LOG.error(
		ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, e);
	    throw new RPGenericException(
		RPGenericErrorCode.ATTRIBUTES_ERROR, ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, e);
	}
	catch (DistributionException e) {
	    LOG.error(
		ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
	    throw new RPDistributionException(
		RPDistributionErrorCode.REDISTRIBUTION_DISTRIBUTION_ERROR,
		ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
	}
	return result;
    }

    /**
     * Metodo que obtiene el número de distribuciones que hay en la búsqueda.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @param searchDistributionRegister
     *            filtros de la búsqueda.
     * @param isOfficeAsoc
     *            si tiene una oficina asociada.
     * @return int Número de distribuciones que hay.
     * @throws RPDistributionException
     *             error en la distribución.
     * @throws RPGenericException
     *             error de sesión.
     */
    public int countSearchDistributionRegister(
	UseCaseConf useCaseConf, SearchDistributionBean searchDistributionRegister,
	boolean isOfficeAsoc)
	throws RPDistributionException, RPGenericException {
	HibernateUtil hibernateUtil = new HibernateUtil();
	Transaction tran = null;
	int distributionCount = 0;
	try {
	    Session session = hibernateUtil.currentSession(useCaseConf.getEntidadId());
	    tran = session.beginTransaction();

	    // Recuperamos la sesión
	    CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
		useCaseConf.getSessionID());
	    AuthenticationUser user =
		(AuthenticationUser) cacheBag.get(HibernateKeys.HIBERNATE_Iuseruserhdr);
	    /* fecha del servidor */

	    // lista de grupos del usuario
	    @SuppressWarnings("unchecked")
	    List<Integer> iUserGroupUser = DistributionSessionUtil.getUserGroups(
		session, user, useCaseConf.getUseLdap());
	    String sortField = " STATE_DATE DESC";
	    StringBuffer finalWhere =
		getWhereQuery(
		    session, user, iUserGroupUser, searchDistributionRegister, isOfficeAsoc,
		    useCaseConf.getEntidadId(), sortField);

	    // Número total de registros
	    distributionCount = getDistributionCount(
		finalWhere.toString(), useCaseConf.getEntidadId());

	    if (session.isOpen()) {
		hibernateUtil.commitTransaction(tran);
	    }
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_BOOKS_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(
		RPGenericErrorCode.SESSION_ERROR, ErrorConstants.SESSION_ERROR_MESSAGE,
		sessionException);
	}
	catch (ValidationException e) {
	    LOG.error(
		ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, e);
	    throw new RPGenericException(
		RPGenericErrorCode.ATTRIBUTES_ERROR, ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, e);
	}
	catch (DistributionException e) {
	    LOG.error(
		ErrorConstants.GET_COUNT_DISTRIBUTION_ERROR_MESSAGE, e);
	    throw new RPDistributionException(
		RPDistributionErrorCode.GET_COUNT_DISTRIBUTION_ERROR,
		ErrorConstants.GET_COUNT_DISTRIBUTION_ERROR_MESSAGE, e);
	}
	catch (Exception exception) {
	    LOG.error(
		ErrorConstants.GET_COUNT_DISTRIBUTION_ERROR_MESSAGE, exception);
	    throw new RPDistributionException(
		RPDistributionErrorCode.GET_COUNT_DISTRIBUTION_ERROR,
		ErrorConstants.GET_COUNT_DISTRIBUTION_ERROR_MESSAGE, exception);
	}
	finally {
	    hibernateUtil.closeSession(useCaseConf.getEntidadId());
	}
	return distributionCount;
    }

    /**
     * Verifica si el registro tiene ya una distribución pendiente para que solo
     * permita distribuir si no hay pendientes.
     * 
     * @param useCaseConf
     *            configuracion usuario.
     * @param idReg
     *            id del registro.
     * @param idBook
     * 		id del libro.
     * @return boolean si tiene distribuciones pendientes.
     * @throws RPDistributionException
     *             error en la distribución.
     * @throws RPGenericException
     *             error genérico.
     */
    public boolean canDistributionRegister(
	UseCaseConf useCaseConf, Integer idReg, Integer idBook)
	throws RPDistributionException, RPGenericException {
	boolean result = true;
	try {
	    Validator.validate_String_NotNull_LengthMayorZero(
		useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);
	}
	catch (ValidationException validationException) {
	    LOG.error(
		ErrorConstants.ATTRIBUTES_ERROR_MESSAGE, validationException);
	    throw new RPGenericException(
		RPGenericErrorCode.ATTRIBUTES_ERROR, ErrorConstants.ATTRIBUTES_ERROR_MESSAGE,
		validationException);
	}

	StringBuffer finalWhere = getWhereQueryCanDistributionRegister(idReg,idBook);

	// Número total de registros
	int totalDistr;
	try {
	    totalDistr = getDistributionCount(
		finalWhere.toString(), useCaseConf.getEntidadId());

	    if (totalDistr > 0) {
		result = false;
	    }
	}
	catch (SQLException exception) {
	    LOG.error(
		ErrorConstants.GET_COUNT_DISTRIBUTION_ERROR_MESSAGE, exception);
	    throw new RPDistributionException(
		RPDistributionErrorCode.GET_COUNT_DISTRIBUTION_ERROR,
		ErrorConstants.GET_COUNT_DISTRIBUTION_ERROR_MESSAGE, exception);
	}
	catch (Exception exception) {
	    LOG.error(
		ErrorConstants.GET_COUNT_DISTRIBUTION_ERROR_MESSAGE, exception);
	    throw new RPDistributionException(
		RPDistributionErrorCode.GET_COUNT_DISTRIBUTION_ERROR,
		ErrorConstants.GET_COUNT_DISTRIBUTION_ERROR_MESSAGE, exception);
	}
	LOG.debug("haveDistributionPenRegister():" +
	    totalDistr);
	return result;

    }
    
    /**
     * Forma el where de la consulta para saber si un registro se puede distribuir pq tiene una distribución pendiente ya.
     * @param idReg
     *            id del registro.
     * @param idBook
     * 		id del libro.
     * @return StringBuffer where.
     */
    private StringBuffer getWhereQueryCanDistributionRegister(
	Integer idReg, Integer idBook) {
	StringBuffer result = new StringBuffer();
	result.append(" STATE in (1,5) AND ID_FDR = "+idReg+" AND ID_ARCH = "+idBook+"  order by ID_FDR");
	return result;
    }

    private static Object acceptDistribution(UseCaseConf useCaseConf, List ids, int state, int firstRow,
	    int maxResults, int typeDist, Integer bookId, List createPermBooks,
	    DistributionResults distributionResults, String distWhere, String regWhere,
	    Integer launchDistOutRegister, Locale locale, String entidad) throws BookException,
	    DistributionException, SessionException, ValidationException {
	Validator.validate_String_NotNull_LengthMayorZero(useCaseConf.getSessionID(),
		ValidationException.ATTRIBUTE_SESSION);

	Transaction tran = null;
	Integer idBook = new Integer(-1);
	List archidFdr = new ArrayList();
	AuthenticationUser user = null;
	int error = 0;
	HibernateUtil hibernateUtil = new HibernateUtil();
	Map outputInputBookType = new HashMap();
	Map newRegisterIdOutInBook = new HashMap();
	Map distIds = distributionResults.getResults();
	Map scrDistRegIds = new HashMap();
	Map regStates = new HashMap();

	try {
	    Map result = new HashMap();

	    // Recuperamos la sesión
	    CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
	    user = (AuthenticationUser) cacheBag.get(HIBERNATE_Iuseruserhdr);

	    Session session = hibernateUtil.currentSession(entidad);
	    tran = session.beginTransaction();

	    archidFdr.addAll(lockFolderDistById(session, user, ids, distIds, archidFdr, false,
		    entidad));

	    outputInputBookType =
		    getOutputInputBookType(session, entidad, ids, distIds, outputInputBookType,
			    user);

	    // Comprobamos los permisos
	    if ((bookId.intValue() == 0)) {
		if (!outputInputBookType.isEmpty()) {
		    if (createPermBooks.isEmpty()) {
			/*
			 * throw new DistributionException(
			 * DistributionException
			 * .ERROR_NO_REGISTER_PERMISSION_ABOUT_ANY_BOOK);
			 */
		    }
		    else if (createPermBooks.size() == 1) {
			idBook = (Integer) createPermBooks.get(0);
		    }
		    else {
			result.put(idBook, archidFdr);
			return result;
		    }
		}
	    }
	    else {
		idBook = bookId;
	    }
	    if (!idBook.equals(0) && !idBook.equals(-1)) {
		// Generación ID de registro
		newRegisterIdOutInBook =
			generateNewRegsId(session, idBook, outputInputBookType, ids, user.getId(),
				entidad);
	    }
	    hibernateUtil.commitTransaction(tran);
	    tran = session.beginTransaction();

	    lockScrDistReg(ids, entidad);

	    scrDistRegIds = getScrDistRegIds(session, ids);
	    regStates.putAll(getRegStates(session, ids, regStates));

	    hibernateUtil.commitTransaction(tran);
	}
	catch (DistributionException e) {
	    hibernateUtil.rollbackTransaction(tran);
	    throw e;
	}
	catch (Exception e) {
	    error = 1;
	    hibernateUtil.rollbackTransaction(tran);
	    throw new DistributionException(DistributionException.ERROR_CANNOT_ACCEPT_DISTRIBUTION);
	}
	finally {
	    hibernateUtil.closeSession(entidad);
	    closeDistRegs(useCaseConf.getSessionID(), archidFdr, error, entidad);
	}
	if (!idBook.equals(0) && !idBook.equals(-1)) {
	    createNewRegister(useCaseConf, idBook, launchDistOutRegister, locale,
		    newRegisterIdOutInBook, outputInputBookType, scrDistRegIds, regStates,
		    archidFdr, entidad);

	    createRegsAsoc(useCaseConf.getSessionID(), newRegisterIdOutInBook, ids, archidFdr, idBook, entidad);
	}
	updateAcceptDistribution(useCaseConf.getSessionID(), ids, archidFdr, entidad);

	unlockDistRegsById(useCaseConf.getSessionID(), ids, entidad);

	return archidFdr;
    }
    
    public static void createNewRegister(
	    UseCaseConf useCaseConf, Integer idBook, Integer launchDistOutRegister, Locale locale,
	Map newRegisterIdOutInBook, Map outputInputBookType, Map scrDistRegIds, Map regStates,
	List archidFdr, String entidad)
	throws BookException, SessionException, ValidationException, DistributionException {
	HibernateUtil hibernateUtil = new HibernateUtil();
	Transaction tran = null;
	int error = 0;
	try {
	    Session session = hibernateUtil.currentSession(entidad);
	    tran = session.beginTransaction();

	    CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
		    useCaseConf.getSessionID());
	    AuthenticationUser user = (AuthenticationUser) cacheBag.get(HIBERNATE_Iuseruserhdr);
	    ScrOfic scrOfic = (ScrOfic) cacheBag.get(HIBERNATE_ScrOfic);

	    if (newRegisterIdOutInBook != null &&
		!newRegisterIdOutInBook.isEmpty()) {


		BookTypeConf bookTypeConf = bookTypeConf(
		    1, entidad);


		int idDestino = 0;
		for (Iterator it = scrDistRegIds.keySet().iterator(); it.hasNext();) {
		    Integer ids1 = (Integer) it.next();
		    if (newRegisterIdOutInBook.containsKey(ids1)) {
			ScrDistreg scrDistReg = (ScrDistreg) scrDistRegIds.get(ids1);

			AxSf axsf = FolderSession.getBookFolder(
				useCaseConf.getSessionID(), new Integer(
				scrDistReg.getIdArch()), scrDistReg.getIdFdr(), locale, entidad);

			AxSf newInputRegister = getNewInputRegister(
			    session, axsf, entidad);
			
			if (newInputRegister.getAttributeValueAsString("fld14") == null || 
				"".equals(newInputRegister.getAttributeValueAsString("fld14"))){
			    newInputRegister.setAttributeValue("fld14", "07");
			}
			
			int registerId = ((Integer) newRegisterIdOutInBook.get(ids1)).intValue();
			if (scrOfic != null) {

			    List interList = getNewRegisterInterList(
				new Integer(
				    scrDistReg.getIdArch()), scrDistReg.getIdFdr(), entidad);
			   
			    idDestino = FolderSession.createNewFolderFromDistribution(
				    useCaseConf.getSessionID(), idBook, newInputRegister, interList, registerId,
				launchDistOutRegister, bookTypeConf, locale, entidad);
			    
			    if (interList == null || interList.size()== 0 ){
				String orgInteresado = null;
				if (axsf.getFld8Name() != null && axsf.getFld8Name().length()>80){
				    orgInteresado =  axsf.getFld8Name().substring(0, 79);
				}else {
				    orgInteresado =  axsf.getFld8Name();
				}
				newRegisterInterListWithFld8(useCaseConf,
					idBook, idDestino,orgInteresado);
			    }
			}

			bookTypeConf = completeBookTypeConf(
			    bookTypeConf, user, axsf, scrOfic, locale);
			UsuarioVO usuario =
				(UsuarioVO) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("USERVO");
			documentoElectronicoAnexoManager.copyDocumentosElectronicoAnexoByRegistro
				(new Long(
					scrDistReg.getIdArch()),new Long( scrDistReg.getIdFdr()), 
					new Long(idBook), new Long(idDestino), usuario);
			
		    }
		}
	    }
	}
	catch (BookException bE) {
	    error = 1;
	    hibernateUtil.rollbackTransaction(tran);
	    throw bE;
	}
	catch (SessionException sE) {
	    error = 1;
	    hibernateUtil.rollbackTransaction(tran);
	    throw sE;
	}
	catch (EventException eE) {
	    error = 1;
	    hibernateUtil.rollbackTransaction(tran);
	    throw eE;
	}
	catch (Exception e) {
	    error = 1;
	    hibernateUtil.rollbackTransaction(tran);
	    throw new DistributionException(
		DistributionException.ERROR_CANNOT_ACCEPT_DISTRIBUTION);
	}
	finally {
	    hibernateUtil.closeSession(entidad);
	    closeDistRegs(
		    useCaseConf.getSessionID(), archidFdr, error, entidad);
	}
    }
    
    /**
     * Método que crea un interesado con el campo destino
     * 
     * @param bookId
     * @param fdrid
     * @param entidad
     * @return
     * @throws Exception
     */
    public static void newRegisterInterListWithFld8(
	    UseCaseConf  useCaseConf, Integer bookId, int fdrid, String fld8)
	throws Exception {

	InterestedBo interestedBo = new InterestedBo();
	List<Interesado> inters = new ArrayList<Interesado>();
	Interesado inter = new Interesado();
	inter.setRazonSocial(fld8);
	inter.setTipo("J");
	inters.add(inter);
	interestedBo.addInterested(inters, bookId, fdrid, useCaseConf);

    }
    
    /**
     * Método que devuelve el estado de la distribucion de un registro.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @param searchDistributionRegister
     *            parámetros de búsqueda.
     * @param isOfficeAsoc
     *            si tiene oficina asociada.
     * @return estado de la distribucion.
     * @throws ValidationException
     *             error en la validación
     * @throws DistributionException
     *             error en la distribución
     * @throws SessionException
     *             error de sesión
     * 
     * */
    @SuppressWarnings("unchecked")
    public Integer getStateDis(
	UseCaseConf useCaseConf, SearchDistributionBean searchDistributionRegister
	, boolean isOfficeAsoc)
	throws ValidationException, DistributionException, SessionException {
	HibernateUtil hibernateUtil = new HibernateUtil();
	Transaction tran = null;
	String sortField = " STATE_DATE DESC";
	Integer result = null;
	try {
	    Session session = hibernateUtil.currentSession(useCaseConf.getEntidadId());
	    tran = session.beginTransaction();

	    // Recuperamos la sesión
	    CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
		useCaseConf.getSessionID());
	    AuthenticationUser user =
		(AuthenticationUser) cacheBag.get(HibernateKeys.HIBERNATE_Iuseruserhdr);

	    // lista de grupos del usuario
	    List<Integer> iUserGroupUser = DistributionSessionUtil.getUserGroups(
		session, user, useCaseConf.getUseLdap());

	    StringBuffer finalWhere =
		getWhereQuery(
		    session, user, iUserGroupUser, searchDistributionRegister, isOfficeAsoc,
		    useCaseConf.getEntidadId(), sortField);


	    Criteria criteriaResults = session.createCriteria(ScrDistreg.class);
	    criteriaResults.setFirstResult(0);
	    criteriaResults.setMaxResults(1);
	    criteriaResults.add(Expression.sql(finalWhere.toString()));
	    List<ScrDistreg> list = criteriaResults.list();
	    
	    if (list != null && list.size() > 0){
		result = list.get(0).getState();
	    }

	    if (session.isOpen()) {
		hibernateUtil.commitTransaction(tran);
	    }
	}
	catch (DistributionException distributionException) {
	    LOG.error(
		ErrorConstants.GET_DISTRIBUTION_ERROR_MESSAGE, distributionException);
	    Utils.redirectToErrorPage(
		null, distributionException, null);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_DISTRIBUTION_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(
		null, sessionException, null);
	}
	catch (Exception e) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(
		"No ha sidpo posible obtener la distribución de la sesión [" +
		    useCaseConf.getSessionID() + "]", e);
	    throw new DistributionException(
		DistributionException.ERROR_CANNOT_OBTAIN_DISTRIBUTION);
	}
	finally {
	    hibernateUtil.closeSession(useCaseConf.getEntidadId());
	}
	return result;
    }
}
