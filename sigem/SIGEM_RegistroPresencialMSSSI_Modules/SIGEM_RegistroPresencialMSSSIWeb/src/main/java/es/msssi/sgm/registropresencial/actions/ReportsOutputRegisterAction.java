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

import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.RowSearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.businessobject.ReportsOutputRegisterBo;
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
public class ReportsOutputRegisterAction extends GenericActions {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(ReportsOutputRegisterAction.class.getName());

	private static ApplicationContext appContext;
	/** Bean que contiene información acerca del registro. */
	private OutputRegisterBean outputRegisterBean;
	/** Filas del datatable que se han seleccionado. */
	private RowSearchOutputRegisterBean[] selectedResults;
	/** Bean con los criterios del buscador. */
	private SearchOutputRegisterBean searchOutputRegister = new SearchOutputRegisterBean();
	/** Clase con la lógica de negocio. */
	private ReportsOutputRegisterBo reportsBo;
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
	public ReportsOutputRegisterAction() {
		reportsBo = new ReportsOutputRegisterBo();
		
		if (reportDAO == null) {
			reportDAO = (ReportDAO) appContext.getBean("reportDAO");
		}
		
		searchOutputRegister = new SearchOutputRegisterBean();
	}

	/**
	 * Método que comprueba si hay seleccionado algún elemento. Si no hay
	 * elementos seleccionados muestra un mensaje de error.
	 */
	public void onRowSelectNavigateBottomBuildReport() {
		
		if (facesContext.getExternalContext().getSessionMap().get("searchOutputRegisterAction") != null) {
			selectedResults = ((SearchOutputRegisterFormAction) facesContext.getExternalContext().getSessionMap().get("searchOutputRegisterAction")).getSelectedResults();
			
			if (selectedResults != null && selectedResults.length > 0) {
				buildCertificateReport();
				
			} else {
				ValidationBo.showDialog("Informe", new FacesMessage( FacesMessage.SEVERITY_WARN, "Informe", "Debe seleccionar, como mínimo, un registro"));
			}
			
		} else {
			ValidationBo.showDialog("Informe", new FacesMessage( FacesMessage.SEVERITY_WARN, "Informe", "Debe seleccionar, como mínimo, un registro"));
		}
	}

	/**
	 * Construye los informes de la aplicación correspondientes a certificados
	 * de registros.
	 */
	public void buildCertificateReport() {
	
		LOG.trace("Entrando en ReportsOutputRegisterAction.buildCertificateReport()");
		
		try {
			init();
			// Se obtiene el Id del libro y se valida
			Integer bookId = getAndValidateBook();
			String ultimoAcuse = null;
			String fld1 = null;

			// Se ecogen el Id. o Ids. de registro que llegan
			List<Integer> fdridList = new ArrayList<Integer>();
			if (facesContext.getViewRoot().getViewMap().get("outputRegisterAction") != null) {
				outputRegisterBean = ((OutputRegisterAction) facesContext.getViewRoot().getViewMap().get("outputRegisterAction")).getOutputRegisterBean();
				
				LOG.info("Llega solo un Id.: " + outputRegisterBean.getFdrid());
				
				fdridList.add(outputRegisterBean.getFdrid());
				ultimoAcuse = outputRegisterBean.getFld1004();
				fld1 = outputRegisterBean.getFld1();
				
			} else {
				if (facesContext.getExternalContext().getSessionMap().get("searchOutputRegisterAction") != null) {
					selectedResults = ((SearchOutputRegisterFormAction) facesContext.getExternalContext().getSessionMap().get("searchOutputRegisterAction")).getSelectedResults();
					
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
					RequestContext.getCurrentInstance().showMessageInDialog( message);
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
			if (fdridList == null || fdridList.size() < 1) {
				ValidationBo.showDialog("", new FacesMessage( FacesMessage.SEVERITY_WARN, "", "Debe seleccionar, como mínimo, un registro"));
				
			} else {
				LOG.info("Hay " + fdridList.size() + " Ids");
				// Se lanza la consulta para obtener los datos
				List<LinkedHashMap<String, Object>> reportResults = null;
				
				if (ultimoAcuse == null) {
					reportResults = reportDAO.getRegisterCertReports(fdridList, bookId, false);
					
					// Se construye y devuelve el informe
					file = reportsBo.buildJasperReport( (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK), reportResults, null);
					
					if (facesContext.getViewRoot().getViewMap().get("outputRegisterAction") != null) {
						
						outputRegisterBean.setFld1004(reportsBo.getAcuseJson());
						((OutputRegisterAction) facesContext.getViewRoot().getViewMap().get("outputRegisterAction")).setOutputRegisterBean(outputRegisterBean);
						((OutputRegisterAction) facesContext.getViewRoot().getViewMap().get("outputRegisterAction")).getRegisterAttachedDocuments();
						((OutputRegisterAction) facesContext.getViewRoot().getViewMap().get("outputRegisterAction")).getListUpdates();
						
						if (facesContext.getExternalContext().getSessionMap().get("reportsLabelAction") != null) {
							facesContext.getExternalContext().getSessionMap().remove("reportsLabelAction");
						}
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
					page.setName(KeysRP.OR_REPORT_CERTIFICATE_NAME + fld1 + ".pdf");
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
		
		LOG.trace("Entrando en ReportsOutputRegisterAction.buildRelationsReport()");
		
		try {
			init();
			// Se obtiene el Id del libro y se valida
			Integer bookId = getAndValidateBook();

			// Se obtiene la lista de registros
			List<RowSearchOutputRegisterBean> registersList = null;
			registersList = reportsBo.getRegistersForRelationReports(bookId,searchOutputRegister);
			/*
			 * registersList = (List<RowSearchOutputRegisterBean>) reportsBo
			 * .removeRegistersWithoutOrganisms(registersList);
			 */
			// if (registersList == null ||
			// registersList.size() <= 0) {
			int sizeRegister = reportsBo.getRowCount();
			
			if (sizeRegister <= 0) {
				FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Generación de informe ", "No hay registros que cumplan con los requisitos de búsqueda"));
				
			} else {// MQE
				if (sizeRegister >= KeysRP.NUM_REGISTERINFORM) {
					FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( FacesMessage.SEVERITY_WARN, "Generación de informe ", "El resultado del informe son " + KeysRP.NUM_REGISTERINFORM + " o más registros," + " debe reducir la búsqueda seleccionando más criterios de búsqueda"));
					
				} else {
					// Se construye y devuelve el informe
					file = reportsBo.buildJasperReport((ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK), null, registersList);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_WARN, "Generación de informe ", "Se ha generado el informe correctamente."));
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
	 * Devuelve el searchOutputRegister.
	 * 
	 * @return searchOutputRegister el objeto searchOutputRegister.
	 */
	public SearchOutputRegisterBean getSearchOutputRegister() {
		return searchOutputRegister;
	}

	/**
	 * Recibe el objeto searchOutputRegister.
	 * 
	 * @param searchOutputRegister
	 *            el objeto searchOutputRegister.
	 */
	public void setSearchOutputRegister( SearchOutputRegisterBean searchOutputRegister) {
		this.searchOutputRegister = searchOutputRegister;
	}

	/**
	 * Devuelve los objetos seleccionados del datatable.
	 * 
	 * @return selectedResults El array con los objetos seleccionados.
	 */
	public RowSearchOutputRegisterBean[] getSelectedResults() {
		return selectedResults;
	}

	/**
	 * Recibe los objectos seleccionados del datatable.
	 * 
	 * @param selectedResults
	 *            El array de objetos seleccionados.
	 */
	public void setSelectedResults(RowSearchOutputRegisterBean[] selectedResults) {
		this.selectedResults = selectedResults;
	}

	/**
	 * Método que actualiza el campo origen con el valor seleccionado en la
	 * búsqueda avanzada de organismos.
	 */
	public void updateOrigin() {
		ScrOrg unidad = null;
		
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG") != null) {
			unidad = (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG");
			getSearchOutputRegister().setFld7Value(unidad);
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
			getSearchOutputRegister().setFld8Value(unidad);
		}
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
	 * Método resetea los mensajes.
	 */
	public void reset() {

	}

	/**
	 * Construye los informes de la aplicación correspondientes a certificados
	 * de registros.
	 */
	public void reBuildCertificateReport() {
		
		LOG.trace("Entrando en ReportsOutputRegisterAction.reBuildCertificateReport()");
		
		try {
			init();
			// Se obtiene el Id del libro y se valida
			Integer bookId = getAndValidateBook();

			// Se ecogen el Id. o Ids. de registro que llegan
			List<Integer> fdridList = new ArrayList<Integer>();
			if (facesContext.getViewRoot().getViewMap().get("outputRegisterAction") != null) {
				outputRegisterBean = ((OutputRegisterAction) facesContext.getViewRoot().getViewMap().get("outputRegisterAction")).getOutputRegisterBean();
				
				LOG.info("Llega solo un Id.: " + outputRegisterBean.getFdrid());
				
				fdridList.add(outputRegisterBean.getFdrid());
			}

			// Se lanza la consulta para obtener los datos
			List<LinkedHashMap<String, Object>> reportResults = null;
			
			LOG.info("Hay " + fdridList.size() + " Ids");

			// si no existe el acuse se crea
			reportResults = reportDAO.getRegisterCertReports(fdridList, bookId, false);
			file = reportsBo.buildJasperReport((ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK), reportResults, null);
			
			if (facesContext.getViewRoot().getViewMap().get("outputRegisterAction") != null) {
				outputRegisterBean.setFld1004(reportsBo.getAcuseJson());
				((OutputRegisterAction) facesContext.getViewRoot().getViewMap().get("outputRegisterAction")).setOutputRegisterBean(outputRegisterBean);
				((OutputRegisterAction) facesContext.getViewRoot().getViewMap().get("outputRegisterAction")).getRegisterAttachedDocuments();
				((OutputRegisterAction) facesContext.getViewRoot().getViewMap().get("outputRegisterAction")).getListUpdates();

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