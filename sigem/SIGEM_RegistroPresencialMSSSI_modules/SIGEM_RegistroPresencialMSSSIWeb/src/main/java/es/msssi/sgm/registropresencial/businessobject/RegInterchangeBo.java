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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.ApplicationContext;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.ieci.tecdoc.fwktd.sir.core.types.CriterioEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.EstadoAsientoRegistralEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.OperadorCriterioEnum;
import es.ieci.tecdoc.fwktd.sir.core.vo.AsientoRegistralVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.CriterioVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.CriteriosVO;
import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.business.vo.IdentificadorRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.exception.IntercambioRegistralException;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaEntradaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaSalidaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralEntradaVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralSalidaVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralVO;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchDestinationRegInterchangeBean;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.ReportDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeErrorCode;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase q implementa IGenericBo que contiene los métodos relacionados con el
 * intercambio registral.
 * 
 * @author cmorenog
 * 
 */
public class RegInterchangeBo implements IGenericBo {
    private static final Logger LOG = Logger.getLogger(RegInterchangeBo.class.getName());
    private static IntercambioRegistralManager intercambioManager;
    private static ApplicationContext appContext;
    /** Clase con la lógica de negocio. */
    private ReportsInputRegisterBo reportsInputBo;
    private ReportsOutputRegisterBo reportsOuputBo;
    private String acuseJson = "";
    
    /**
     * Clase para obtener los datos de informes de certificados mediante acceso
     * a base de datos.
     */
    private static ReportDAO reportDAO;

    static {
	appContext =
		RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
			.getApplicationContext();
	if (intercambioManager == null) {
	    intercambioManager =
		    IsicresManagerProvider.getInstance().getIntercambioRegistralManager();
	}
    }

    public RegInterchangeBo() {
	if (reportDAO == null) {
	    reportDAO = (ReportDAO) appContext.getBean("reportDAO");
	}

    }

    /**
     * Obtiene la unidad de tramitacion mapeada en el modulo comun para el
     * organismo del registro.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param bookId
     *            Id del libro.
     * @param folderId
     *            Id de registro.
     * @return fldid Id de registro guardado o actualizado.
     * 
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     * @throws RPRegistralExchangeException
     *             si se ha producido algún error al recuperar el destinatario
     *             por defecto.
     */
    public SearchDestinationRegInterchangeBean getSearchDestinationRegInterchangeBean(
	    UseCaseConf useCaseConf, Integer bookId, int folderId) throws RPGenericException,
	    RPRegistralExchangeException {
	SearchDestinationRegInterchangeBean unidadPorDefecto =
		new SearchDestinationRegInterchangeBean();
	AxSf axsf;
	try {
	    axsf =
		    FolderSession.getBookFolder(useCaseConf.getSessionID(), bookId, folderId,
			    useCaseConf.getLocale(), useCaseConf.getEntidadId());

	    ScrOrg unidadAdministrativaDestino = axsf.getFld8();
	    unidadPorDefecto.setAxsf(axsf);
	    if (unidadAdministrativaDestino != null) {
		// Consultar mapeo por defecto.

		UnidadTramitacionIntercambioRegistralVO unidTramPorDefecto =
			intercambioManager
				.getUnidadTramitacionIntercambioRegistralVOByIdScrOrgs(unidadAdministrativaDestino
					.getId().toString());
		unidadPorDefecto.setUnidTramPorDefecto(unidTramPorDefecto);
	    }
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.CREATE_INPUT_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.SEARCH_DESTINATION_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.SEARCH_DESTINATION_REG_INTERCHANGE_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.SEARCH_DESTINATION_REG_INTERCHANGE_ERROR_MESSAGE,
		    sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.SEARCH_DESTINATION_REG_INTERCHANGE_ERROR_MESSAGE,
		    validationException);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	}
	return unidadPorDefecto;
    }

    /**
     * Envía al intercambio registral el registro que entra como parámetro.
     * 
     * @param destinationRegInterchange
     *            Datos sobre le destino y el registro a enviar.
     * @param usuario
     *            Nombre completo del usuario que realiza el envío.
      * @param usuarioContacto
     *            Nombre completo del usuario que realiza el envío.
     * @param idOficina
     *            Id de oficina.
     * @return idInterchange Id del intercambio registral.
     * 
     * @throws RPRegistralExchangeException
     *             si se ha producido algún error al realizar el envío.
     */
    public String sendInterchangeOutput(
	    SearchDestinationRegInterchangeBean destinationRegInterchange, String usuario, String usuarioContacto,
	    String idOficina, UseCaseConf useCaseConf) throws RPRegistralExchangeException {
	String id = null;
	String idLibro = null;
	String idRegistro = null;
	String fld1 = null;
	String tipoOrigen = null;
	UnidadTramitacionIntercambioRegistralVO unidadDestino;
	try {
	    idLibro = String.valueOf(destinationRegInterchange.getBook().getIdocarchhdr().getId());
	    tipoOrigen =
		    Integer.toString(destinationRegInterchange.getBook().getIdocarchhdr().getType());
	    idRegistro = destinationRegInterchange.getAxsf().getAttributeValueAsString("fdrid");
	    fld1 = destinationRegInterchange.getAxsf().getAttributeValueAsString("fld1");
	    unidadDestino = destinationRegInterchange.getUnidTramPorDefecto();

	    addReportRegister(idRegistro, destinationRegInterchange.getBook(), idOficina, usuario,
		    useCaseConf, fld1);

	    // realizamos el intercambiVo registral
	    id =
		    intercambioManager.enviarIntercambioRegistralSalida(idLibro, idRegistro,
			    idOficina, usuario,usuarioContacto, tipoOrigen, unidadDestino);
	}
	catch (IntercambioRegistralException irEx) {
	    LOG.error(ErrorConstants.SEND_REG_INTERCHANGE_ERROR_MESSAGE, irEx);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.SEND_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.SEND_REG_INTERCHANGE_ERROR_MESSAGE, irEx);
	}
	catch (Exception irEx) {
	    LOG.error(ErrorConstants.SEND_REG_INTERCHANGE_ERROR_MESSAGE, irEx);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.SEND_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.SEND_REG_INTERCHANGE_ERROR_MESSAGE, irEx);
	}

	return id;

    }

    private void addReportRegister(String idRegistro, ScrRegstate libro, String idOficina,
	    String usuario, UseCaseConf useCaseConf, String fld1) {
	// GENERAR ACUSE DE RECIBO

	List<Integer> fdridList = new ArrayList<Integer>();
	StreamedContent file;
	List<StreamedContent> filesToUpload = new ArrayList<StreamedContent>();
	InputRegisterBo inputRegisterBo = null;
	InputRegisterBean inputRegisterBean = null;
	OutputRegisterBo outputRegisterBo = null;
	OutputRegisterBean outputRegisterBean = null;
	List<LinkedHashMap<String, Object>> reportResults = null;
	RegisterDocumentsBo registerDocumentsBo = null;
	AxSf registerData = null;
	LinkedHashMap<String, Object> acuse = null;
	String nameCert = null;
	Integer numAcuses = 0;
	try {
	    registerDocumentsBo = new RegisterDocumentsBo();
	    if (libro.getIdocarchhdr().getType() == 1) {
		nameCert = KeysRP.IR_REPORT_CERTIFICATE_NAME+fld1+".pdf";
	    }else {
		nameCert = KeysRP.OR_REPORT_CERTIFICATE_NAME+fld1+".pdf";
	    }
	    
	    numAcuses =
		    registerDocumentsBo.countPageReport(useCaseConf, libro.getIdocarchhdr().getId(),
			    Integer.parseInt(idRegistro));
	    if (numAcuses > 0){
		    numAcuses = numAcuses + 1; //es el siguiente  
		    nameCert = nameCert.substring(0, nameCert.indexOf("."))+"_"+numAcuses + ".pdf";
		   
		}else {
		    numAcuses = numAcuses + 1; //es el siguiente  
		}
	    
	    fdridList.add(Integer.parseInt(idRegistro));

	    LOG.info("Hay " + fdridList.size() + " Ids");
	    if (libro.getIdocarchhdr().getType() == 1) {
		reportResults =
			reportDAO.getRegisterCertReports(fdridList, libro.getIdocarchhdr().getId(),
				true);
		reportsInputBo = new ReportsInputRegisterBo();
		acuse =
			reportsInputBo.buildJasperReportReturnMap(libro.getIdocarchhdr().getId(),
				reportResults);
		file =
			    new DefaultStreamedContent(new ByteArrayInputStream( (byte[]) acuse.get("DATA")),
				    KeysRP.MIME_TYPE_PDF, nameCert);
	    }
	    else {
		reportResults =
			reportDAO.getRegisterCertReports(fdridList, libro.getIdocarchhdr().getId(),
				false);
		reportsOuputBo = new ReportsOutputRegisterBo();
		acuse =
			reportsOuputBo.buildJasperReportReturnMap(libro.getIdocarchhdr().getId(),
				reportResults);
		file = new DefaultStreamedContent(new ByteArrayInputStream( (byte[]) acuse.get("DATA")),
				    KeysRP.MIME_TYPE_PDF, nameCert);
	    }
	    filesToUpload.add(file);
		if (libro.getIdocarchhdr().getType() == 1) {
		    inputRegisterBo = new InputRegisterBo();
		    inputRegisterBean =
			    inputRegisterBo.loadInputRegisterBean(useCaseConf, libro,
				    Integer.parseInt(idRegistro));
		    registerData = Utils.mappingInputRegisterToAxSF(inputRegisterBean);
		}
		else {
		    outputRegisterBo = new OutputRegisterBo();
		    outputRegisterBean =
			    outputRegisterBo.loadOutputRegisterBean(useCaseConf, libro,
				    Integer.parseInt(idRegistro));
		    registerData = Utils.mappingOutputRegisterToAxSF(outputRegisterBean);
		}

		DocumentoElectronicoAnexoVO documentSaved =
			registerDocumentsBo.saveStreamedContentDocuments(
				useCaseConf.getSessionID(), libro.getIdocarchhdr().getId(),
				Integer.parseInt(idRegistro), filesToUpload, registerData,
				useCaseConf.getLocale(), useCaseConf.getEntidadId());
		if (documentSaved != null){
		    acuseJson =  "[{\"iddoc\":"+documentSaved.getId().getId()+",\"idpag\":"+documentSaved.getId().getIdPagina()+",\"csv\":\""+(String) acuse.get("CSV")+"\"}]";
			InputRegisterBean inputBeanUpdate = new InputRegisterBean();
			inputBeanUpdate.setFdrid(inputRegisterBean.getFdrid());
			inputBeanUpdate.setFld1(inputRegisterBean.getFld1());
			inputBeanUpdate.setFld1004(acuseJson);
			    if (inputRegisterBean.getInteresados() != null && inputRegisterBean.getInteresados().size()>0){
				List<Interesado> interesados = new ArrayList<Interesado>();
				Interesado interesado = new Interesado();
				interesado.setNombre(inputRegisterBean.getInteresados().get(0).getNombre()
					+" "+inputRegisterBean.getInteresados().get(0).getPapellido());
				interesados.add(interesado);
				inputBeanUpdate.setInteresados(interesados );
			    }
			    inputRegisterBo.updateOnlyFolder(useCaseConf, libro.getIdocarchhdr().getId(),
				    inputRegisterBean.getFdrid(),inputBeanUpdate);
			    registerDocumentsBo.updateFlag(useCaseConf, libro.getIdocarchhdr().getId(),
				    inputRegisterBean.getFdrid(),Long.valueOf(documentSaved.getId().getIdPagina()).intValue(), numAcuses);
		}
	}
	catch (Exception exception) {
	    LOG.error("Error al generar el informe: ", exception);
	    Utils.redirectToErrorPage(null, null, exception);
	}
    }

    /**
     * Metodo que obtiene el historial del intercambio registral de Entrada para
     * un registro.
     * 
     * @param idLibro
     *            id del libro.
     * @param idRegistro
     *            Id del registro.
     * @param idOficina
     *            id de oficina.
     * @return Listado de objetos.
     * @throws RPRegistralExchangeException .
     */
    public List<IntercambioRegistralEntradaVO> getHistorialIntercambioRegistralEntrada(
	    String idLibro, String idRegistro, String idOficina)
	    throws RPRegistralExchangeException {
	List<IntercambioRegistralEntradaVO> result = null;
	try {
	    result =
		    intercambioManager.getHistorialIntercambioRegistralEntrada(idLibro, idRegistro,
			    idOficina);
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.GET_HIST_INPUT_REG_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.GET_HIST_INPUT_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.GET_HIST_INPUT_REG_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return result;
    }

    /**
     * Metodo que obtiene el historial del intercambio registral de Salida para
     * un registro.
     * 
     * @param idLibro
     *            libro.
     * @param idRegistro
     *            registro.
     * @param idOficina
     *            idOficina.
     * @return Listado de objetos.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public List<IntercambioRegistralSalidaVO> getHistorialIntercambioRegistralSalida(
	    String idLibro, String idRegistro, String idOficina)
	    throws RPRegistralExchangeException {
	List<IntercambioRegistralSalidaVO> result = null;
	try {
	    result =
		    intercambioManager.getHistorialIntercambioRegistralSalida(idLibro, idRegistro,
			    idOficina);
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return result;
    }

    /**
     * Devuelve la lista de registros de la bandeja de salida.
     * 
     * @param estado
     *            id del estado.
     * @param idOficina
     *            id de la oficina.
     * @param idLibro
     *            id del libro.
     * @return lista lista de registros de la bandeja de salida.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public List<BandejaSalidaItemVO>
	    getOutboxIR(Integer estado, Integer idOficina, Integer idLibro)
		    throws RPRegistralExchangeException {
	/** lista de registros. */
	List<BandejaSalidaItemVO> outBoxList = null;
	try {
	    outBoxList =
		    intercambioManager.getBandejaSalidaIntercambioRegistral(estado, idOficina,
			    idLibro);
	    if (outBoxList != null && (new Integer(1)).equals(estado)) {
		for (BandejaSalidaItemVO bandejaSalidaItemVO : outBoxList) {
		    intercambioManager.completarBandejaSalidaItem(bandejaSalidaItemVO);
		}
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.GET_INBOX_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return outBoxList;
    }

    /**
     * Devuelve la lista de registros de la bandeja de entrada.
     * 
     * @param estado
     *            id del estado.
     * @param idOficina
     *            id de la oficina.
     * @return lista lista de registros de la bandeja de entrada.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public List<BandejaEntradaItemVO> getInboxIR(Integer estado, Integer idOficina)
	    throws RPRegistralExchangeException {
	/** lista de registros. */
	List<BandejaEntradaItemVO> inBoxList = null;
	try {
	    inBoxList = intercambioManager.getBandejaEntradaIntercambioRegistral(estado, idOficina);
	    if (inBoxList != null && (new Integer(1)).equals(estado)) {
		for (BandejaEntradaItemVO bandejaEntradaItemVO : inBoxList) {
		    intercambioManager.completarBandejaEntradaItem(bandejaEntradaItemVO);
		}
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.GET_INBOX_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return inBoxList;
    }

    /**
     * Devuelve la lista de registros de la bandeja de entrada.
     * 
     * @param estado
     *            id del estado.
     * @param idOficina
     *            id de la oficina.
     * @param criterios criterios de búsqueda.
     * @return lista lista de registros de la bandeja de entrada.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public List<BandejaEntradaItemVO> getInboxIR(Integer estado, Integer idOficina, CriteriosVO criterios)
	    throws RPRegistralExchangeException {
	/** lista de registros. */
	List<BandejaEntradaItemVO> inBoxList = null;
	try {
	    inBoxList = intercambioManager.getBandejaEntradaIntercambioRegistral(estado, idOficina,criterios);
	    if (inBoxList != null && (new Integer(1)).equals(estado)) {
		for (BandejaEntradaItemVO bandejaEntradaItemVO : inBoxList) {
		    intercambioManager.completarBandejaEntradaItem(bandejaEntradaItemVO);
		}
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.GET_INBOX_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return inBoxList;
    }
    
    /**
     * Devuelve el número de registros la lista de registros de la bandeja de entrada.
     * 
     * @param estado
     *            id del estado.
     * @param idOficina
     *            id de la oficina.
     * @param criterios criterios de búsqueda.   
     * @return lista lista de registros de la bandeja de entrada.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public int getCountInboxIR(Integer estado, Integer idOficina, CriteriosVO criterios)
	    throws RPRegistralExchangeException {
	/** lista de registros. */
	int countInBoxList = 0;
	try {
	    countInBoxList = intercambioManager.getCountBandejaEntradaIntercambioRegistral(estado, idOficina,criterios);
	    
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.GET_INBOX_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return countInBoxList;
    }
    
    /**
     * Acepta una lista de intercambios registrales y los almacena en un libro.
     * 
     * @param selectedRegisters
     *            lista de registros.
     * @param book
     *            libro.
     * @param user
     *            usuario.
     * @param idOficina
     *            id de la oficina.
     * @param codOficina
     *            codigo oficina.
     * @return lista lista de registros que han fallado.
     * @throws RPRegistralExchangeException . Error
     *             en la operación.
     */
    public List<BandejaEntradaItemVO> accept(BandejaEntradaItemVO[] selectedRegisters, String book,
	    UsuarioVO user, Integer idOficina, String codOficina) throws RPRegistralExchangeException {
	/** lista de registros. */
	List<BandejaEntradaItemVO> errorList = null;
	String idIR = "";
	try {
	    for (BandejaEntradaItemVO bandejaEntradaItemVO : selectedRegisters) {
		idIR = bandejaEntradaItemVO.getIdIntercambioRegistral();
		intercambioManager.aceptarIntercambioRegistralEntradaById(
			String.valueOf(bandejaEntradaItemVO.getIdIntercambioInterno()), book, user,
			idOficina, codOficina, false);
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.ACCEPT_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.ACCEPT_INBOX_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.ACCEPT_INBOX_INTERCHANGE_ERROR_MESSAGE + "</br>"+((!"".equals(idIR))?"Intercambio Registral "+idIR+": ":"" ) + e.getMessage(),
		    e);
	}
	return errorList;
    }

    /**
     * rechazar una lista de intercambios registrales.
     * 
     * @param selectedRegisters
     *            lista de registros.
     * @param motivo
     *            motivo del rechazo.
     * @param tipoRechazo
     *            tipo del rechazo.
    * @param user
     *            usuario.
     * @param idOficina
     *            id de la oficina.
     * @param codOficina
     *            codigo oficina.
     * @return lista lista de registros que han fallado.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public List<BandejaEntradaItemVO> reject(BandejaEntradaItemVO[] selectedRegisters,
	    String motivo, String tipoRechazo,
	    UsuarioVO user, Integer idOficina, String codOficina) throws RPRegistralExchangeException {
	/** lista de registros. */
	List<BandejaEntradaItemVO> errorList = null;
	try {
	    for (BandejaEntradaItemVO bandejaEntradaItemVO : selectedRegisters) {
		intercambioManager.rechazarIntercambioRegistralEntradaById(
			String.valueOf(bandejaEntradaItemVO.getIdIntercambioInterno()),
			tipoRechazo, motivo, user,idOficina, codOficina);
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.REJECT_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.REJECT_INBOX_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.REJECT_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return errorList;
    }

    /**
     * reenviar una lista de intercambios registrales.
     * 
     * @param selectedRegisters
     *            lista de registros.
     * @param observaciones
     *            observaciones.
     * @param unidadTramitacionDestino
     *            unidad a la que va dirigido.
     * @param user
     *            usuario.
     * @param idOficina
     *            id de la oficina.
     * @param codOficina
     *            codigo oficina.
     * @return lista lista de registros que han fallado.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public List<BandejaEntradaItemVO> forward(BandejaEntradaItemVO[] selectedRegisters,
	    String observaciones, UnidadTramitacionIntercambioRegistralVO unidadTramitacionDestino,
	    UsuarioVO user, Integer idOficina, String codOficina)
	    throws RPRegistralExchangeException {
	/** lista de registros. */
	List<BandejaEntradaItemVO> errorList = null;
	try {
	    for (BandejaEntradaItemVO bandejaEntradaItemVO : selectedRegisters) {
		intercambioManager.reenviarIntercambioRegistralEntradaById(
			String.valueOf(bandejaEntradaItemVO.getIdIntercambioInterno()),
			unidadTramitacionDestino, observaciones, user,idOficina, codOficina);
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.FORWARD_INBOX_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return errorList;
    }

    /**
     * reenviar una lista de intercambios registrales.
     * 
     * @param selectedRegisters
     *            lista de registros.
     * @param observaciones
     *            observaciones.
     * @param unidadTramitacionDestino
     *            unidad a la que va dirigido.
     * @param user
     *            usuario.
     * @param idOficina
     *            id de la oficina.
     * @param codOficina
     *            codigo oficina.
     * @return lista lista de registros que han fallado.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public List<BandejaSalidaItemVO> forward(BandejaSalidaItemVO[] selectedRegisters,
	    String observaciones, UnidadTramitacionIntercambioRegistralVO unidadTramitacionDestino,
	    UsuarioVO user, Integer idOficina, String codOficina)
	    throws RPRegistralExchangeException {
	/** lista de registros. */
	List<BandejaSalidaItemVO> errorList = null;
	try {
	    for (BandejaSalidaItemVO bandejaSalidaItemVO : selectedRegisters) {
		intercambioManager.reenviarIntercambioRegistralSalidaById(
			String.valueOf(bandejaSalidaItemVO.getIdIntercambioInterno()),
			unidadTramitacionDestino, observaciones, user,idOficina, codOficina);
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.FORWARD_INBOX_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return errorList;
    }
    
    /**
     * Devuelve los datos de un asiento registral.
     * 
     * @param idIntercambio
     *            id del asiento.
     * @return AsientoRegistralVO el asiento registral.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public AsientoRegistralVO getInterchange(String idIntercambio)
	    throws RPRegistralExchangeException {

	AsientoRegistralVO asientoRegistralVO = null;
	try {
	    try {
		asientoRegistralVO =
			intercambioManager.getIntercambioRegistralByIdIntercambio(idIntercambio);
	    }
	    catch (Exception e) {
		LOG.error(ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
		throw new RPRegistralExchangeException(
			RPRegistralExchangeErrorCode.GET_INPUT_REG_INTERCHANGE_ERROR_MESSAGE,
			ErrorConstants.GET_INPUT_INTERCHANGE_ERROR_MESSAGE, e);
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.GET_OUTBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.GET_INPUT_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.GET_INPUT_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return asientoRegistralVO;
    }

    /**
     * Devuelve el número de registros pendientes.
     * 
     * @param idOficina
     *            id de la oficina.
     * @return int número de registros pendientes de la bandeja de entrada.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public int getCountPendingIR(Integer idOficina) throws RPRegistralExchangeException {
	/** lista de registros. */
	List<BandejaEntradaItemVO> inBoxList = null;
	int result = 0;
	try {
	    inBoxList = intercambioManager.getBandejaEntradaIntercambioRegistral(0, idOficina);
	    if (inBoxList != null) {
		result = inBoxList.size();
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.GET_PENDING_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.GET_PENDING_INBOX_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.GET_PENDING_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return result;
    }

    /**
     * Metodo que comprueba si un registro se puede modificar pq se haya enviado
     * o recibido de un intercambio.
     * 
     * @param idLibro
     *            libro.
     * @param idRegistro
     *            registro.
     * @param idOficina
     *            idOficina.
     * @return Listado de objetos.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public boolean
	    isIntercambioRegistralNotEdit(String idLibro, String idRegistro, String idOficina)
		    throws RPRegistralExchangeException {
	boolean result = false;
	List<IntercambioRegistralSalidaVO> lista = null;
	try {
	    lista =
		    intercambioManager.getHistorialIntercambioRegistralSalida(idLibro, idRegistro,
			    idOficina);
	    for (IntercambioRegistralSalidaVO ir : lista) {
		if (ir.getIdEstado() == 1 || ir.getIdEstado() == 2 || ir.getIdEstado() == 5
			|| ir.getIdEstado() == 6 || ir.getIdEstado() == 7 || ir.getIdEstado() == 14 || ir.getIdEstado() == 999) {
		    result = true;
		}
	    }
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return result;
    }

    public void isTramUnitByCode (String codeScrOrgs){
	UnidadTramitacionIntercambioRegistralVO unit = intercambioManager.getUnidadTramitacionIntercambioRegistralVOByCodeScrOrgs(codeScrOrgs);
	if (unit == null){
	    throw new ValidatorException(new FacesMessage(
			"El número de identificación del organismo introducido no es correcto o no está mapeado en DCO."));
	}
    }
    
    /**
     * comprueba que el numero de registro original no existe en la bandeja de rojos recibidos.
     * @param idOficina 
     * 		id de la oficina
     * @param numeroRegistroOriginal
     * 		numero de registro original
     * @return true si no existe el numero; false si si existe.
     */
    public boolean validateOriginalNumber ( String idOficina, String numeroRegistroOriginal){
	boolean result = true;
	CriteriosVO criteriosVO = null;
	criteriosVO = new CriteriosVO();
	
	try {

	    CriterioVO criterioEstado;
	    
	    criterioEstado =
		    new CriterioVO(CriterioEnum.ASIENTO_ESTADO, OperadorCriterioEnum.IN,
			    new Integer[] { EstadoAsientoRegistralEnum.RECIBIDO.getValue() });
	    criteriosVO.addCriterioVO(criterioEstado);

        	criterioEstado =
        	    new CriterioVO(CriterioEnum.ASIENTO_NUMERO_REGISTRO_INICIAL, OperadorCriterioEnum.EQUAL,
        		    numeroRegistroOriginal );
        	criteriosVO.addCriterioVO(criterioEstado);
        	int rowcount = 0;
	    	rowcount =
		    getCountInboxIR(0, Integer.parseInt(idOficina), criteriosVO);
	    	if (rowcount > 0){
	    	    result = false;
	    	}
	}
	catch (NumberFormatException numberFormatException) {
	    LOG.error(ErrorConstants.GET_INTERCHANGE_ERROR_MESSAGE, numberFormatException);
	}
	catch (RPRegistralExchangeException e) {
	    LOG.error(ErrorConstants.GET_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return result;
    }

    public void update(BandejaSalidaItemVO itemVO, String idRegistro ) throws RPRegistralExchangeException {

	try {
	    IdentificadorRegistroVO  identificadorRegistroVO = new IdentificadorRegistroVO();
	    identificadorRegistroVO.setIdLibro(String.valueOf(itemVO.getIdLibro()));
	    identificadorRegistroVO.setIdRegistro(idRegistro);
		intercambioManager.rectificarIntercambioRegistralSalidaById(
			String.valueOf(itemVO.getIdIntercambioInterno()),identificadorRegistroVO);
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.FORWARD_INBOX_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	}
    }
    
    /**
     * Metodo que devuelve el estado de IR de salida de un registro.
     * 
     * @param idLibro
     *            libro.
     * @param idRegistro
     *            registro.
     * @param idOficina
     *            idOficina.
     * @return estado.
     * @throws RPRegistralExchangeException
     *             Error en la operación.
     */
    public Integer
	    getStateIROutput(String idLibro, String idRegistro, String idOficina)
		    throws RPRegistralExchangeException {
	Integer result = null;
	List<IntercambioRegistralSalidaVO> lista = null;
	try {
	    lista =
		    intercambioManager.getHistorialIntercambioRegistralSalidaResumen(idLibro, idRegistro,
			    idOficina);
	    if (lista != null && lista.size() > 0){
		result = lista.get(0).getIdEstado();
	    }
	  
	}
	catch (Exception e) {
	    LOG.error(ErrorConstants.GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE, e);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE, e);
	}
	return result;
    }

}
