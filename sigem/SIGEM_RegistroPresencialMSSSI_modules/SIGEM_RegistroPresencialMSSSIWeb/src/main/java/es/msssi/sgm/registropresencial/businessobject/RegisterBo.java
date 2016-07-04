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

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.ieci.tecdoc.common.exception.AttributesException;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SecurityException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesdoc.Idocarchhdr;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxPK;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.AxSfQuery;
import com.ieci.tecdoc.common.isicres.AxSfQueryResults;
import com.ieci.tecdoc.common.isicres.UpdHisFdrResults;
import com.ieci.tecdoc.common.keys.ISicresKeys;
import com.ieci.tecdoc.idoc.decoder.query.QueryFormat;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrField;
import com.ieci.tecdoc.isicres.session.book.BookSession;
import com.ieci.tecdoc.isicres.session.folder.FolderAsocSession;
import com.ieci.tecdoc.isicres.session.folder.FolderHistSession;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.isicres.usecase.book.BookUseCase;
import com.ieci.tecdoc.isicres.usecase.book.util.BookUseCaseAsocRegsUtil;
import com.ieci.tecdoc.isicres.usecase.book.xml.AsocRegsResults;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.msssi.sgm.registropresencial.actions.ValidationListAction;
import es.msssi.sgm.registropresencial.beans.AsocRegisterBean;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.MoveRegisterDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPRegisterErrorCode;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;

/**
 * Clase que implementa IGenericBo que contiene los métodos relacionados con los
 * registros.
 * 
 * @author cmorenog
 */
public class RegisterBo implements IGenericBo, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RegisterBo.class.getName());
    private static final int NUMBER3 = 3;
    private static final int NUMBER4 = 4;
    private MoveRegisterDAO moveRegisterDao = new MoveRegisterDAO();
    private static ApplicationContext appContext;

    static {
	appContext =
		RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
			.getApplicationContext();
    }

    /**
     * Comprobamos si es formulario de solo lectura.
     * 
     * @param readOnlyFlash
     *            Indicativo de si el contexto Flash es solo de lectura.
     * @param idRegister
     *            id del registro.
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param bookId
     *            Id del libro.
     * @return readOnly Se devuelve <i>true</i> si es de sólo lectura y
     *         <i>false</i> si no lo es.
     * 
     * @throws RPRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro.
     * 
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    public boolean readOnly(Boolean readOnlyFlash, Integer idRegister, UseCaseConf useCaseConf,
	    Integer bookId) throws RPGenericException, RPRegisterException {
	LOG.trace("Entrando en registerBo.readOnly()");
	boolean readOnly = false;
	if (readOnlyFlash) {
	    readOnly = true;
	}
	if (!readOnly) {
	    // el metodo lockFolder devuelve:
	    // true ha sido bloqueada
	    // false ya estaba bloqueada
	    try {
		readOnly =
			!(FolderSession.lockFolder(useCaseConf.getSessionID(), bookId, idRegister,
				useCaseConf.getEntidadId()));
	    }
	    catch (ValidationException validationException) {
		LOG.error(ErrorConstants.LOCK_REGISTER_ERROR_MESSAGE, validationException);
		throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
			ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	    }
	    catch (BookException bookException) {
		LOG.error(ErrorConstants.LOCK_REGISTER_ERROR_MESSAGE, bookException);
		throw new RPRegisterException(RPRegisterErrorCode.LOCK_REGISTER_ERROR,
			ErrorConstants.BOOK_NOT_PREVIOUSLY_LOADED_ERROR_MESSAGE, bookException);
	    }
	    catch (SessionException sessionException) {
		LOG.error(ErrorConstants.LOCK_REGISTER_ERROR_MESSAGE, sessionException);
		throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
			ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	    }
	}
	return readOnly;
    }

    /**
     * Obtiene el histórico de registros.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param book
     *            libro.
     * @param idRegistro
     *            Id del registro.
     * @param numReg
     *            Número de registro.
     * @return result Listado de histórico de registros.
     * 
     * @throws RPRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro.
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    @SuppressWarnings("unchecked")
    public List<UpdHisFdrResults> getUpdHisFdrResults(UseCaseConf useCaseConf, ScrRegstate book,
	    int idRegistro, String numReg) throws RPGenericException, RPRegisterException {
	LOG.trace("Entrando en registerBo.getUpdHisFdrResults()");
	List<UpdHisFdrResults> result = null;
	UpdHisFdrResults hist;
	try {
	    AxSf axsf =
		    FolderSession.getBookFolder(useCaseConf.getSessionID(), book.getIdocarchhdr()
			    .getId(), idRegistro, useCaseConf.getLocale(), useCaseConf
			    .getEntidadId());
	    result =
		    FolderHistSession.getUpdHisFdrResults(useCaseConf.getSessionID(),
			    useCaseConf.getLocale(), book.getIdocarchhdr().getId(), idRegistro,
			    axsf, numReg, useCaseConf.getEntidadId());
	    // convertir los códigos en texto
	    ValidationListAction val = null;
	    if (FacesContext.getCurrentInstance().getExternalContext().getApplicationMap()
		    .get("validationListAction") == null) {
		val = new ValidationListAction();
		val.create();
	    }
	    else {
		val =
			(ValidationListAction) FacesContext.getCurrentInstance()
				.getExternalContext().getApplicationMap()
				.get("validationListAction");
	    }
	    for (int ind = 0; ind < result.size(); ind++) {
		hist = result.get(ind);

		hist.setValue(val.getTextOfValue(hist.getScrModifReg().getIdFld(), hist.getValue(),
			book.getIdocarchhdr().getType()));
		hist.setOldvalue(val.getTextOfValue(hist.getScrModifReg().getIdFld(),
			hist.getOldvalue(), book.getIdocarchhdr().getType()));
		result.set(ind, hist);
	    }
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.GET_HISTORICAL_REGISTER_ERROR_MESSAGE, validationException);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.GET_HISTORICAL_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_HISTORICAL_REGISTER_ERROR,
		    ErrorConstants.GET_HISTORICAL_OR_REGISTER_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.GET_HISTORICAL_REGISTER_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}
	return result;
    }

    /**
     * Abrir los registros que se hayan seleccionado.
     * 
     * @param useCaseConf
     *            configuración de la aplicación
     * @param bookID
     *            id libro
     * @param list
     *            lista de ids de registros
     * @throws RPRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro.
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     * **/
    public void openRegisters(UseCaseConf useCaseConf, Integer bookID, List<Integer> list)
	    throws RPRegisterException, RPGenericException {

	FlushFdrField field = new FlushFdrField();
	field.setFldid(AxSf.FLD6_FIELD_ID);
	field.setValue(String.valueOf(ISicresKeys.SCR_ESTADO_REGISTRO_COMPLETO));
	List<FlushFdrField> fields = new ArrayList<FlushFdrField>(1);
	fields.add(field);

	// actualizamos el estado a completo ya que pare cerrar un registro
	// deberia haber
	// estado completo
	BookUseCase bookUseCase = new BookUseCase();
	try {
	    bookUseCase.updateFields(useCaseConf, bookID, fields, list);
	}
	catch (ValidationException validationExc) {
	    LOG.error(ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, validationExc);
	    throw new RPRegisterException(RPRegisterErrorCode.OPEN_REGISTER_ERROR,
		    ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, validationExc);
	}
	catch (SecurityException securityExc) {
	    LOG.error(ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, securityExc);
	    throw new RPRegisterException(RPRegisterErrorCode.OPEN_REGISTER_ERROR,
		    ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, securityExc);
	}
	catch (AttributesException e) {
	    LOG.error(ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, e);
	    throw new RPRegisterException(RPRegisterErrorCode.OPEN_REGISTER_ERROR,
		    ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, e);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPRegisterException(RPRegisterErrorCode.OPEN_REGISTER_ERROR,
		    ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}
	catch (ParseException parseException) {
	    LOG.error(ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, parseException);
	    throw new RPRegisterException(RPRegisterErrorCode.OPEN_REGISTER_ERROR,
		    ErrorConstants.OPEN_REGISTER_ERROR_MESSAGE, parseException);
	}

	if (LOG.isDebugEnabled()) {
	    LOG.debug("openRegisters");
	}
    }

    /**
     * Cerrar los registros que se hayan seleccionado.
     * 
     * @param useCaseConf
     *            configuración de la aplicación
     * @param bookID
     *            id libro
     * @param list
     *            lista de registros
     * @throws RPRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro.
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     * **/
    public void closeRegisters(UseCaseConf useCaseConf, Integer bookID, List<Integer> list)
	    throws RPRegisterException, RPGenericException {

	FlushFdrField field = new FlushFdrField();
	field.setFldid(AxSf.FLD6_FIELD_ID); // TODO: Buscar en constantes: id
	// del campo estado
	field.setValue(String.valueOf(ISicresKeys.SCR_ESTADO_REGISTRO_CERRADO));
	List<FlushFdrField> fields = new ArrayList<FlushFdrField>(1);
	fields.add(field);

	// actualizamos el estado a completo ya que pare cerrar un registro
	// deberia haber
	// estado completo
	BookUseCase bookUseCase = new BookUseCase();
	try {
	    if (list != null && list.size() > 0) {
		bookUseCase.updateRegisterToClose(useCaseConf, bookID, fields, list);
	    }
	}
	catch (ValidationException validationExc) {
	    LOG.error(ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, validationExc);
	    throw new RPRegisterException(RPRegisterErrorCode.CLOSE_REGISTER_ERROR,
		    ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, validationExc);
	}
	catch (SecurityException securityExc) {
	    LOG.error(ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, securityExc);
	    throw new RPRegisterException(RPRegisterErrorCode.CLOSE_REGISTER_ERROR,
		    ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, securityExc);
	}
	catch (AttributesException e) {
	    LOG.error(ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, e);
	    throw new RPRegisterException(RPRegisterErrorCode.CLOSE_REGISTER_ERROR,
		    ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, e);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPRegisterException(RPRegisterErrorCode.CLOSE_REGISTER_ERROR,
		    ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}
	catch (ParseException parseException) {
	    LOG.error(ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, parseException);
	    throw new RPRegisterException(RPRegisterErrorCode.CLOSE_REGISTER_ERROR,
		    ErrorConstants.CLOSED_REGISTER_ERROR_MESSAGE, parseException);
	}

	if (LOG.isDebugEnabled()) {
	    LOG.debug("openRegisters");
	}
    }

    /**
     * Devuelve los registros asociados.
     * 
     * @param useCaseConf
     *            configuración
     * @param bookID
     *            id del libro
     * @param folderID
     *            id del registro
     * @return object[] idocs, axsfs,axsfPrim
     * @throws RPRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro.
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    public Object[] getAsocReg(UseCaseConf useCaseConf, Integer bookID, Integer folderID)
	    throws RPRegisterException, RPGenericException {
	Object[] result;
	try {
	    result =
		    FolderAsocSession.getAsocRegsFdr(useCaseConf.getSessionID(), bookID, folderID,
			    useCaseConf.getLocale(), useCaseConf.getEntidadId());
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.GET_ASOC_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPRegisterException(RPRegisterErrorCode.LOCK_REGISTER_ERROR,
		    ErrorConstants.BOOK_NOT_PREVIOUSLY_LOADED_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.GET_ASOC_REGISTER_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.GET_ASOC_REGISTER_ERROR_MESSAGE, validationException);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	}
	/*
	 * Map idocs = (Map) result[0]; Map axsfs = (Map) result[1]; Map
	 * axsfPrim = (Map) result[2];
	 */
	return result;
    }

    /**
     * Devuelve el registro principal asociado.
     * 
     * @param map
     *            map con el registro principal
     * @param idocs
     *            lista de libros asociados
     * @return AxSf registro principal
     */
    @SuppressWarnings("unchecked")
    public AxSf getAsoReg(Map<AxPK, AxSf> map, Map<String, Idocarchhdr> idocs) {
	AxSf result = null;
	if (map != null && !map.isEmpty()) {
	    for (AxPK key : map.keySet()) {
		result = (AxSf) map.get(key);
		result.getAttributesValues().put("libro",
			((Idocarchhdr) idocs.get(Integer.parseInt(key.getType()))).getName());
		result.getAttributesValues().put("idLibro", key.getType());
	    }
	}
	return result;
    }

    /**
     * Devuelve la lista de registros asociados.
     * 
     * @param map
     *            map con los registros
     * @param idocs
     *            lista de libros asociados
     * @return List<AxSf> lista de registros asociados
     */
    @SuppressWarnings("unchecked")
    public List<AxSf> getListAsoReg(Map<AxPK, AxSf> map, Map<String, Idocarchhdr> idocs) {
	List<AxSf> result = new ArrayList<AxSf>();
	AxSf axsf = null;
	if (map != null && !map.isEmpty()) {
	    for (AxPK key : map.keySet()) {
		axsf = (AxSf) map.get(key);
		axsf.getAttributesValues().put("libro",
			((Idocarchhdr) idocs.get(Integer.parseInt(key.getType()))).getName());
		axsf.getAttributesValues().put("idLibro", key.getType());
		result.add(axsf);
	    }
	}
	return result;
    }

    /**
     * Borra el registro asociado.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param idLibro
     *            Id del libro.
     * @param idReg
     *            Id del registro.
     * @throws RPRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro.
     */
    public void deleteAssociation(UseCaseConf useCaseConf, Integer idLibro, Integer idReg)
	    throws RPRegisterException {
	BooksBo booksBo = new BooksBo();
	try {

	    // Recuperamos la sesión
	    CacheBag cacheBag =
		    CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
	    if (!cacheBag.containsKey(idLibro)) {
		booksBo.openBook(useCaseConf, idLibro);
	    }

	    BookUseCaseAsocRegsUtil.deleteAsocRegsSec(useCaseConf, idLibro, idReg);
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.DELETE_ASOC_REGISTER_ERROR_MESSAGE, e);
	    throw new RPRegisterException(RPRegisterErrorCode.DELETE_ASSOC_REGISTER_ERROR,
		    ErrorConstants.DELETE_ASOC_REGISTER_ERROR_MESSAGE, e);
	}
    }

    /**
     * Asociar un registro.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param asocRegisterBean
     *            Registro a asociar.
     * @param folderId
     *            id registro.
     * @param archiveId
     *            id libro.
     * @return lista de registros asociados.
     * @throws RPRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro.
     */
    public List<AsocRegsResults> saveAssociation(UseCaseConf useCaseConf,
	    AsocRegisterBean asocRegisterBean, Integer folderId, Integer archiveId)
	    throws RPRegisterException {
	List<AsocRegsResults> listaRegs = null;
	try {
	    String regWhere = "@FLD1;Igual a;" + asocRegisterBean.getFld1() + ";#";
	    // Recuperamos la sesión
	    CacheBag cacheBag =
		    CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
	    if (!cacheBag.containsKey(asocRegisterBean.getIdLibro())) {
		BooksBo booksBo = new BooksBo();
		booksBo.openBook(useCaseConf, asocRegisterBean.getIdLibro());
	    }
	    listaRegs =
		    getValidateAsocRegsSearch(useCaseConf, regWhere, asocRegisterBean.getIdLibro(),
			    folderId, archiveId);
	    if (listaRegs != null && !listaRegs.isEmpty()) {
		String asocRegsSelected = mappingListtoString(listaRegs);
		String primaryReg = null;
		if (asocRegsSelected != null) {
		    primaryReg = asocRegsSelected.split("#")[0];
		}
		String code =
			getValidateAsocRegsSelected(useCaseConf, asocRegsSelected, folderId,
				archiveId);
		saveAsocRegs(useCaseConf, asocRegsSelected, primaryReg, Integer.parseInt(code),
			archiveId, folderId);
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
	    throw new RPRegisterException(RPRegisterErrorCode.SAVE_ASSOC_REGISTER_ERROR,
		    ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
	}
	return listaRegs;
    }

    /**
     * busca el registro seleccionado en la búsqueda de asociados.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param regWhere
     *            el where.
     * @param registerBook
     *            el libro que se quiere registrar.
     * @param folderId
     *            id registro.
     * @param archiveId
     *            id libro.
     * @return lista de registros asociados.
     * @throws RPRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro.
     */
    @SuppressWarnings("unchecked")
    private List<AsocRegsResults> getValidateAsocRegsSearch(UseCaseConf useCaseConf,
	    String regWhere, Integer registerBook, Integer folderId, Integer archiveId)
	    throws RPRegisterException {
	List<AsocRegsResults> listaRegs = null;
	try {
	    String dataBaseType = BookSession.getDataBaseType(useCaseConf.getSessionID());
	    AxSf axsfQ =
		    BookSession.getQueryFormat(useCaseConf.getSessionID(), registerBook,
			    useCaseConf.getEntidadId());

	    // aplicamos un orden para la consulta
	    String orderBy = "FLD1 ASC";
	    QueryFormat formatter = new QueryFormat(axsfQ.getFormat().getData());

	    AxSfQuery axSfQuery =
		    BookUseCaseAsocRegsUtil.getAsocRegsSearchCriteria(useCaseConf, regWhere,
			    useCaseConf.getLocale(), dataBaseType, registerBook, formatter);

	    if (axSfQuery.getPageSize() <= 0) {
		throw new BookException(BookException.ERROR_PAGE_SIZE);
	    }
	    int size =
		    FolderSession.openRegistersQuery(useCaseConf.getSessionID(), axSfQuery, null,
			    new Integer(0), useCaseConf.getEntidadId());

	    AxSfQueryResults queryResults = null;
	    boolean isRegCurrentAsoc = false;
	    if (size > 0) {
		queryResults =
			FolderSession.navigateRegistersQuery(useCaseConf.getSessionID(),
				registerBook, com.ieci.tecdoc.common.isicres.Keys.QUERY_ALL,
				useCaseConf.getLocale(), useCaseConf.getEntidadId(), orderBy);

		if (BookUseCaseAsocRegsUtil.isAsocReg(useCaseConf, folderId, archiveId)) {
		    isRegCurrentAsoc = true;
		    queryResults =
			    filterRegsResultsByAsocRegs(queryResults, useCaseConf, registerBook);
		}
	    }

	    if (queryResults != null) {
		listaRegs =
			BookUseCaseAsocRegsUtil.getAsocRegsResults(queryResults.getResults(),
				queryResults.getBookId(), useCaseConf.getLocale());

		if (!isRegCurrentAsoc) {
		    listaRegs =
			    BookUseCaseAsocRegsUtil.filterRegsResultByCurrent(listaRegs, archiveId,
				    folderId);
		}
	    }

	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
	    throw new RPRegisterException(RPRegisterErrorCode.SAVE_ASSOC_REGISTER_ERROR,
		    ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
	}
	return listaRegs;
    }

    /**
     * Mapea la lista de registros asociados a un string.
     * 
     * @param listaRegs
     *            lista de registros asociados.
     * @return String con los registros.
     */
    private String mappingListtoString(List<AsocRegsResults> listaRegs) {
	String result = "";
	String reg = "";
	for (AsocRegsResults asoc : listaRegs) {
	    reg =
		    asoc.getBookId() + ";" + asoc.getFolderId() + ";" + asoc.getFolderNumber()
			    + ";" + asoc.getFolderDate() + ";" + asoc.getSummary() + ";";
	    result = reg + "#";
	}
	return result;
    }

    /**
     * Validamos el registro actual respecto a los registros seleccionados para
     * comprobar si se puede realizar la asociacion.
     * 
     * @param useCaseConf
     *            configuración.
     * @param asocRegsSelected
     *            registros asociados.
     * @param folderId
     *            id registro.
     * @param archiveId
     *            id libro.
     * @return String.
     * @throws BookException
     *             Error en la operación.
     * @throws SessionException
     *             Error en la operación.
     * @throws ValidationException
     *             Error en la operación.
     */
    private String getValidateAsocRegsSelected(UseCaseConf useCaseConf, String asocRegsSelected,
	    Integer folderId, Integer archiveId) throws BookException, SessionException,
	    ValidationException {

	String code = "-1";

	if (BookUseCaseAsocRegsUtil.isAsocReg(useCaseConf, folderId, archiveId)) {
	    /*
	     * Si el registro ya esta asociado a otros registros, al realizar la
	     * busqueda solo se muestran los registros que no estan asociados a
	     * ningun otro registro, por lo que la asociacion es valida
	     */
	    code = "0";
	}
	else {
	    /*
	     * Si el registro no esta asociado a ningun registro se muestran
	     * todos los registros que coinciden con los criterios de busqueda.
	     */
	    String codeAux =
		    BookUseCaseAsocRegsUtil.getAsocRegsSelectedCode(asocRegsSelected, useCaseConf);

	    if (codeAux != null && codeAux.length() > 0) {
		code = codeAux;
	    }
	}
	return code;
    }

    /**
     * Asociamos los registros seleccionados.
     * 
     * @param useCaseConf
     *            configuración.
     * @param asocRegsSelected
     *            registros selecionados.
     * @param primaryReg
     *            registro principal.
     * @param code
     *            Código.
     * @param bookId
     *            Id del libro.
     * @param folderId
     *            Id del registro.
     * @throws RPRegisterException
     *             Error en la operación
     */
    @SuppressWarnings("unchecked")
    public void saveAsocRegs(UseCaseConf useCaseConf, String asocRegsSelected, String primaryReg,
	    int code, Integer bookId, Integer folderId) throws RPRegisterException {
	switch (code) {
	case 0:
	    try {
		Integer[] primario =
			BookUseCaseAsocRegsUtil.getAsocRegPrimario(useCaseConf, bookId, folderId);

		if (primario != null) {
		    List<AsocRegsResults> listaRegs =
			    BookUseCaseAsocRegsUtil.getAsocRegsResults(asocRegsSelected,
				    useCaseConf.getLocale());

		    if (listaRegs != null && !listaRegs.isEmpty()) {
			BookUseCaseAsocRegsUtil.saveAsocRegsSec(useCaseConf, listaRegs,
				primario[0], primario[1]);
		    }
		}
	    }
	    catch (Exception e) {
		LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
		throw new RPRegisterException(RPRegisterErrorCode.SAVE_ASSOC_REGISTER_ERROR,
			ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
	    }
	    break;
	case 1:
	    try {
		List<AsocRegsResults> listaRegs =
			BookUseCaseAsocRegsUtil.getAsocRegsResults(asocRegsSelected,
				useCaseConf.getLocale());

		if (listaRegs != null && !listaRegs.isEmpty()) {
		    Integer[] primario =
			    BookUseCaseAsocRegsUtil.getAsocRegPrimario(useCaseConf, listaRegs);

		    if (primario != null) {
			BookUseCaseAsocRegsUtil.saveAsocRegsSec(useCaseConf, primario[0],
				primario[1], bookId, folderId);
		    }
		}

	    }
	    catch (Exception e) {
		LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
		throw new RPRegisterException(RPRegisterErrorCode.SAVE_ASSOC_REGISTER_ERROR,
			ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
	    }
	    break;
	case 2:
	    try {
		AsocRegsResults regCurrent = new AsocRegsResults(bookId, folderId, "", "", "");

		List<AsocRegsResults> listaRegs =
			BookUseCaseAsocRegsUtil.getAsocRegsResults(asocRegsSelected,
				useCaseConf.getLocale());

		if (listaRegs == null) {
		    listaRegs = new ArrayList<AsocRegsResults>();
		}
		if (!listaRegs.isEmpty()) {
		    Integer[] primario =
			    BookUseCaseAsocRegsUtil.getAsocRegPrimario(useCaseConf, listaRegs);

		    listaRegs =
			    BookUseCaseAsocRegsUtil.getNoAsocRegsResults(useCaseConf, listaRegs);

		    listaRegs.add(regCurrent);

		    if (primario != null) {
			BookUseCaseAsocRegsUtil.saveAsocRegsSec(useCaseConf, listaRegs,
				primario[0], primario[1]);
		    }
		}

	    }
	    catch (Exception e) {
		LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
		throw new RPRegisterException(RPRegisterErrorCode.SAVE_ASSOC_REGISTER_ERROR,
			ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
	    }
	    break;
	case NUMBER3:
	    try {
		List<AsocRegsResults> listaRegs =
			BookUseCaseAsocRegsUtil.getAsocRegsResults(asocRegsSelected,
				useCaseConf.getLocale());

		if (listaRegs != null && !listaRegs.isEmpty()) {
		    AsocRegsResults asocRegsResults = (AsocRegsResults) listaRegs.get(0);

		    BookUseCaseAsocRegsUtil.saveAsocRegsSec(useCaseConf,
			    asocRegsResults.getBookId(), asocRegsResults.getFolderId(), bookId,
			    folderId);
		}

	    }
	    catch (Exception e) {
		LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
		throw new RPRegisterException(RPRegisterErrorCode.SAVE_ASSOC_REGISTER_ERROR,
			ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
	    }
	    break;
	case NUMBER4:
	    try {
		List<AsocRegsResults> listaPrimaryRegs =
			BookUseCaseAsocRegsUtil.getAsocRegsResults(primaryReg,
				useCaseConf.getLocale());

		if (listaPrimaryRegs != null && !listaPrimaryRegs.isEmpty()) {
		    AsocRegsResults asocRegsResultsPrimary =
			    (AsocRegsResults) listaPrimaryRegs.get(0);

		    List<AsocRegsResults> listaRegs =
			    BookUseCaseAsocRegsUtil.getAsocRegsResults(asocRegsSelected,
				    useCaseConf.getLocale());

		    listaRegs =
			    BookUseCaseAsocRegsUtil.filterRegsResultByCurrent(listaRegs,
				    asocRegsResultsPrimary.getBookId(),
				    asocRegsResultsPrimary.getFolderId());

		    if (listaRegs == null) {
			listaRegs = new ArrayList<AsocRegsResults>();
		    }

		    AsocRegsResults regCurrent = new AsocRegsResults(bookId, folderId, "", "", "");

		    listaRegs.add(regCurrent);

		    BookUseCaseAsocRegsUtil.saveAsocRegsSec(useCaseConf, listaRegs,
			    asocRegsResultsPrimary.getBookId(),
			    asocRegsResultsPrimary.getFolderId());

		}

	    }
	    catch (Exception e) {
		LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
		throw new RPRegisterException(RPRegisterErrorCode.SAVE_ASSOC_REGISTER_ERROR,
			ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, e);
	    }
	    break;
	default:
	    break;
	}
    }

    /**
     * Dada una lista de registros, eliminamos de ella los registros que tienen
     * registros asociados. Ya sean primarios o secuandarios.
     * 
     * @param queryResults
     *            resultados de la query.
     * @param useCaseConf
     *            configuración.
     * @param bookId
     *            Id del libro.
     * @return resultado de la query.
     * @throws BookException
     *             error en la operación.
     * @throws SessionException
     *             error en la operación.
     * @throws ValidationException
     *             error en la operación.
     */
    @SuppressWarnings("unchecked")
    private static AxSfQueryResults filterRegsResultsByAsocRegs(AxSfQueryResults queryResults,
	    UseCaseConf useCaseConf, Integer bookId) throws BookException, SessionException,
	    ValidationException {

	Collection resultsQueryResults = queryResults.getResults();
	// int size = queryResults.getTotalQuerySize();

	for (Iterator<AxSf> iterator = resultsQueryResults.iterator(); iterator.hasNext();) {
	    AxSf axSf = (AxSf) iterator.next();

	    boolean result =
		    FolderAsocSession.isAsocRegsFdr(useCaseConf.getSessionID(), bookId,
			    Integer.parseInt(String.valueOf(axSf.getAttributeValue("fdrid"))),
			    useCaseConf.getEntidadId());

	    if (result) {
		iterator.remove();
	    }
	}
	return queryResults;
    }

    /**
     * mueve un registro de un libro a otro.
     * 
     * @param fdridOriginRegister
     *            registro a mover.
     * @param bookIdOrigin
     *            libro origen.
     * @param bookIdDestination
     *            libro destino.
     */
    public void moveRegister(Integer fdridOriginRegister, Integer bookIdOrigin,
	    Integer bookIdDestination) throws RPRegisterException {
	try {

	    if (fdridOriginRegister != null && bookIdOrigin != null && bookIdDestination != null) {
		moveRegisterDao = (MoveRegisterDAO) appContext.getBean("moveRegisterDAO");
		moveRegisterDao.moveRegisterBook(fdridOriginRegister, bookIdOrigin,
			bookIdDestination);
	    }
	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.MOVE_REGISTER_ERROR, exception);
	    throw new RPRegisterException(RPRegisterErrorCode.MOVE_REGISTER_ERROR,
		    ErrorConstants.MOVE_REGISTER_ERROR, exception);
	}
    }
}