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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.springframework.context.ApplicationContext;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;

import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.RowSearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.businessobject.ReportsInputRegisterBo;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.ReportDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.validations.ValidationBo;

/**
 * Clase que genera los informes relacionados con registros de entrada de la
 * aplicación.
 * 
 * @author jortizs
 */
public class ReportsInputRegisterAction extends GenericActions implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(ReportsInputRegisterAction.class.getName());

	private static ApplicationContext appContext;
	/** Filas del datatable que se han seleccionado. */
	private RowSearchInputRegisterBean[] selectedResults;
	/** Bean que contiene información acerca del registro. */
	private InputRegisterBean inputRegisterBean;
	/** Bean con los criterios del buscador. */
	private SearchInputRegisterBean searchInputRegister;
	/** Clase con la lógica de negocio. */
	private ReportsInputRegisterBo reportsBo;
	/**
	 * Clase para obtener los datos de informes de certificados mediante acceso
	 * a base de datos.
	 */
	private static ReportDAO reportDAO;
	/**
	 * objeto que contiene el fichero.
	 */
	private StreamedContent file;

	static {
		appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
	}

	/**
	 * Constructor.
	 */
	public ReportsInputRegisterAction() {
		reportsBo = new ReportsInputRegisterBo();
		
		if (reportDAO == null) {
			reportDAO = (ReportDAO) appContext.getBean("reportDAO");
		}
		
		searchInputRegister = new SearchInputRegisterBean();
	}

	/**
	 * Método que comprueba si hay seleccionado algún elemento. Si no hay
	 * elementos seleccionados muestra un mensaje de error.
	 */
	public void onRowSelectNavigateBottomBuildReport() {
		
		if (facesContext.getExternalContext().getSessionMap().get("searchInputRegisterAction") != null) {
			selectedResults = ((SearchInputRegisterFormAction) facesContext.getExternalContext().getSessionMap().get("searchInputRegisterAction")).getSelectedResults();
			
			if (selectedResults != null && selectedResults.length > 0) {
				buildCertificateReport();
				
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Informe", "Debe seleccionar, como mínimo, un registro");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				// ValidationBo.showDialog("", new
				// FacesMessage(FacesMessage.SEVERITY_WARN,
				// "Informe", "Debe seleccionar, como mínimo, un registro"));
			}
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Informe", "Debe seleccionar, como mínimo, un registro");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			// ValidationBo.showDialog("", new
			// FacesMessage(FacesMessage.SEVERITY_WARN,
			// "Informe", "Debe seleccionar, como mínimo, un registro"));
		}
	}

	/**
	 * Construye los informes de la aplicación correspondientes a certificados
	 * de registros.
	 */
	public void buildCertificateReport() {
		
		LOG.trace("Entrando en ReportsInputRegisterAction.buildCertificateReport()");
		
		try {
			init();
			// Se obtiene el Id del libro y se valida
			Integer bookId = getAndValidateBook();
			String ultimoAcuse = null;
			String fld1 = null;
			// Se ecogen el Id. o Ids. de registro que llegan
			List<Integer> fdridList = new ArrayList<Integer>();
		
			if (facesContext.getViewRoot().getViewMap().get("inputRegisterAction") != null) {
				inputRegisterBean = ((InputRegisterAction) facesContext.getViewRoot().getViewMap().get("inputRegisterAction")).getInputRegisterBean();
				
				LOG.info("Llega solo un Id.: " + inputRegisterBean.getFdrid());
				
				fdridList.add(inputRegisterBean.getFdrid());
				ultimoAcuse = inputRegisterBean.getFld1004();
				fld1 = inputRegisterBean.getFld1();

			} else {
				if (facesContext.getExternalContext().getSessionMap().get("searchInputRegisterAction") != null) {
					selectedResults = ((SearchInputRegisterFormAction) facesContext.getExternalContext().getSessionMap().get("searchInputRegisterAction")).getSelectedResults();
					
					if (selectedResults != null && selectedResults.length > 0) {
						String logMessage = "";
					
						if (selectedResults.length == 1) {
							logMessage = "Llega un Id.: ";
							fdridList.add(selectedResults[0].getFdrid());
							ultimoAcuse = selectedResults[0].getFld1004();
							fld1 = selectedResults[0].getFld1();
						
						} else {
							logMessage = "Llegan varios Ids.: ";
							
							for (int i = 0; i < selectedResults.length; i++) {
								fdridList.add(selectedResults[i].getFdrid());
								logMessage += selectedResults[i].getFdrid() + ",";
							}
						}
						LOG.info(logMessage);
					}
				} else {
					FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_WARN, "Informe", "Debe seleccionar, como mínimo, un registro");
					RequestContext.getCurrentInstance().showMessageInDialog(message);
					// ValidationBo.showDialog("", new
					// FacesMessage(FacesMessage.SEVERITY_WARN,
					// "Informe",
					// "Debe seleccionar, como mínimo, un registro"));
				}
				String logMessage = "";
				
				if (selectedResults.length == 1) {
					logMessage = "Llega un Id.: ";
					fdridList.add(selectedResults[0].getFdrid());
				
				} else {
					logMessage = "Llegan varios Ids.: ";
				
					for (int i = 0; i < selectedResults.length; i++) {
						fdridList.add(selectedResults[i].getFdrid());
						logMessage += selectedResults[i].getFdrid() + ",";
					}
				}
				
				LOG.info(logMessage);
			}
			
			if (fdridList == null || fdridList.size() <= 0) {
				ValidationBo.showDialog("", new FacesMessage( FacesMessage.SEVERITY_WARN, "", "Debe seleccionar, como mínimo, un registro"));
				
			} else {
				// Se lanza la consulta para obtener los datos
				List<LinkedHashMap<String, Object>> reportResults = null;
				
				LOG.info("Hay " + fdridList.size() + " Ids");

				// si no existe el acuse se crea
				if (ultimoAcuse == null) {
					reportResults = reportDAO.getRegisterCertReports(fdridList, bookId, true);
					
					file = reportsBo.buildJasperReport( (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK), reportResults, null);
					
					if (facesContext.getViewRoot().getViewMap().get("inputRegisterAction") != null) {
						inputRegisterBean.setFld1004(reportsBo.getAcuseJson());
						((InputRegisterAction) facesContext.getViewRoot().getViewMap().get("inputRegisterAction")).setInputRegisterBean(inputRegisterBean);
						((InputRegisterAction) facesContext.getViewRoot().getViewMap().get("inputRegisterAction")).getRegisterAttachedDocuments();
						((InputRegisterAction) facesContext.getViewRoot().getViewMap().get("inputRegisterAction")).getListUpdates();
					}
					
					if (facesContext.getExternalContext().getSessionMap().get("reportsLabelAction") != null) {
						facesContext.getExternalContext().getSessionMap().remove("reportsLabelAction");
					}
					
				} else {
					// si existe el acuse se recupera
					DocumentDownloadController download = new DocumentDownloadController();
					Axpageh page = new Axpageh();
					JSONParser parser = new JSONParser();
					JSONArray arrayContent = (JSONArray) parser.parse(ultimoAcuse);
					page.setDocId(Integer.valueOf(String.valueOf((Long) ((JSONObject) arrayContent.get(0)).get("iddoc"))));
					page.setId(Integer.valueOf(String.valueOf((Long) ((JSONObject) arrayContent.get(0)).get("idpag"))));
					page.setFdrid((Integer) fdridList.get(0));
					page.setLoc("pdf");
					page.setName(KeysRP.IR_REPORT_CERTIFICATE_NAME + fld1 + ".pdf");
					download.setPage(page);
					
					file = download.getFileDownload();
					
					if (facesContext.getExternalContext().getSessionMap().get("reportsLabelAction") != null) {
						facesContext.getExternalContext().getSessionMap().remove("reportsLabelAction");
					}
				}
			}
		} catch (Exception exception) {
			LOG.error("Error al generar el informe: ", exception);
			Utils.redirectToErrorPage(null, null, exception);
		}
	}

	/**
	 * Construye los informes de la aplicación correspondientes a relaciones
	 * diarias, por origen y destino, de registros diarios.
	 */
	public void buildRelationsReport() {
		
		LOG.trace("Entrando en ReportsInputRegisterAction.buildRelationsReport()");
		
		try {
			init();
			// Se obtiene el Id del libro y se valida
			Integer bookId = getAndValidateBook();

			// Se obtiene la lista de registros
			List<RowSearchInputRegisterBean> registersList = null;
			registersList = reportsBo.getRegistersForRelationReports(bookId, searchInputRegister);
			int sizeRegister = reportsBo.getRowCount();
			
			if (sizeRegister <= 0) {
				FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Generación de informe ", "No hay registros que cumplan con los requisitos de búsqueda"));
				
			} else {
				if (sizeRegister >= KeysRP.NUM_REGISTERINFORM) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_WARN, "Generación de informe ", "El resultado del informe son " + KeysRP.NUM_REGISTERINFORM + " o más registros," + " debe reducir la búsqueda seleccionando más criterios de búsqueda"));
					
				} else {

					file = reportsBo.buildJasperReport((ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK), null, registersList);
					FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( FacesMessage.SEVERITY_WARN, "Generación de informe ", "Se ha generado el informe correctamente."));
				}
			}
		} catch (Exception exception) {
			LOG.error("Error al generar el informe: ", exception);
			Utils.redirectToErrorPage(null, null, exception);
		}
	}

	/**
	 * Obtiene el libro de sesión y lo valida.
	 * 
	 * @return bookId Id. del libro.
	 * @throws SessionException
	 *             Si se ha producido un error en la sesión.
	 */
	private Integer getAndValidateBook() throws SessionException {
		
		Integer idBook = null;
		ScrRegstate book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
		
		if (book == null) {
			throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
		} else {
			idBook = book.getIdocarchhdr().getId();
			LOG.info("Id del libro: " + idBook);
		}

		try {
			validateIdBook(book.getIdocarchhdr().getId(), useCaseConf);
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.GET_INFORMATION_BOOK_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
		}
		return idBook;
	}

	/**
	 * Método que comprueba si hay seleccionado algún elemento. Si no hay
	 * elementos seleccionados muestra un mensaje de error.
	 */
	public void isSelect() {

		if (selectedResults == null || selectedResults.length == 0) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Informe", "Debe seleccionar, como mínimo, un registro");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			// ValidationBo.showDialog("", new FacesMessage(
			// FacesMessage.SEVERITY_WARN, "",
			// "Debe seleccionar mínimo un registro"));
		}
	}

	/**
	 * Devuelve el searchInputRegister.
	 * 
	 * @return searchInputRegister el objeto searchInputRegister.
	 */
	public SearchInputRegisterBean getSearchInputRegister() {
		return searchInputRegister;
	}

	/**
	 * Recibe el objeto searchInputRegister.
	 * 
	 * @param searchInputRegister
	 *            el objeto searchInputRegister.
	 */
	public void setSearchInputRegister( SearchInputRegisterBean searchInputRegister) {
		this.searchInputRegister = searchInputRegister;
	}

	/**
	 * Devuelve los objetos seleccionados del datatable.
	 * 
	 * @return selectedResults El array con los objetos seleccionados.
	 */
	public RowSearchInputRegisterBean[] getSelectedResults() {
		return selectedResults;
	}

	/**
	 * Recibe los objectos seleccionados del datatable.
	 * 
	 * @param selectedResults
	 *            El array de objetos seleccionados.
	 */
	public void setSelectedResults(RowSearchInputRegisterBean[] selectedResults) {
		this.selectedResults = selectedResults;
	}

	/**
	 * Devuelve el file.
	 * 
	 * @return file el objeto file.
	 */
	public StreamedContent getFile() {
		return file;
	}

	/**
	 * Método que actualiza el campo origen con el valor seleccionado en la
	 * búsqueda avanzada de organismos.
	 */
	public void updateOrigin() {
		
		ScrOrg unidad = null;
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG") != null) {
			unidad = (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG");
			getSearchInputRegister().setFld7Value(unidad);
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
			getSearchInputRegister().setFld8Value(unidad);
		}
	}

	/**
	 * Método resetea los mensajes.
	 */
	public void reset() {

	}

	/**
	 * Construye los informes de la aplicación correspondientes a certificados
	 * de registros.
	 */
	public void reBuildCertificateReport() {
		LOG.trace("Entrando en ReportsInputRegisterAction.reBuildCertificateReport()");
		try {
			init();
			// Se obtiene el Id del libro y se valida
			Integer bookId = getAndValidateBook();
			// Se ecogen el Id. o Ids. de registro que llegan
			List<Integer> fdridList = new ArrayList<Integer>();
			
			if (facesContext.getViewRoot().getViewMap().get("inputRegisterAction") != null) {
				inputRegisterBean = ((InputRegisterAction) facesContext.getViewRoot().getViewMap().get("inputRegisterAction")).getInputRegisterBean();
				
				LOG.info("Llega solo un Id.: " + inputRegisterBean.getFdrid());
				
				fdridList.add(inputRegisterBean.getFdrid());
			}

			// Se lanza la consulta para obtener los datos
			List<LinkedHashMap<String, Object>> reportResults = null;
			
			
			LOG.info("Hay " + fdridList.size() + " Ids");

			// si no existe el acuse se crea
			reportResults = reportDAO.getRegisterCertReports(fdridList, bookId, true);
			
			file = reportsBo.buildJasperReport((ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK), reportResults, null);
			
			if (facesContext.getViewRoot().getViewMap().get("inputRegisterAction") != null) {
				inputRegisterBean.setFld1004(reportsBo.getAcuseJson());
				((InputRegisterAction) facesContext.getViewRoot().getViewMap().get("inputRegisterAction")).setInputRegisterBean(inputRegisterBean);
				((InputRegisterAction) facesContext.getViewRoot().getViewMap().get("inputRegisterAction")).getRegisterAttachedDocuments();
				((InputRegisterAction) facesContext.getViewRoot().getViewMap().get("inputRegisterAction")).getListUpdates();
				
				if (facesContext.getExternalContext().getSessionMap().get("reportsLabelAction") != null) {
					facesContext.getExternalContext().getSessionMap().remove("reportsLabelAction");
				}
			}
		} catch (Exception exception) {
			LOG.error("Error al generar el informe: ", exception);
			Utils.redirectToErrorPage(null, null, exception);
		}
	}
}