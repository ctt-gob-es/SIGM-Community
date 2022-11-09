/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.web.beans;

import java.util.List;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTemplatesDTO;
import es.seap.minhap.portafirmas.utils.Constants;

public class Inbox {

	// Acción que se lanza con el formulario
	private String action;

	// Listado de peticiones a mostrar
	private List<AbstractBaseDTO> requestList;

	// Número de peticiones pendientes
	private String numUnresolved;

	// Tipo de bandeja seleccionada
	private String requestBarSelected;
	
	// Tamaño personalizado de página
	private Integer customPageSize;

	// Petición de la bandeja seleccionada
	private Request selectedRequest;

	// Paginador de la tabla de peticiones
	private Paginator paginator;

	// Listado de aplicaciones del filtro
	private List<AbstractBaseDTO> labelList;

	// Listado de aplicaciones del filtro
	private List<AbstractBaseDTO> applicationList;

	// Listado de aplicaciones del filtro
	private List<PfRequestTemplatesDTO> templateList;
	
	// Listado de meses del filtro
	private List<Month> monthList;

	// Listado de años del filtro
	private List<String> yearList;
	
	// Filtro de búsqueda
	private String searchFilter;

	// Filtro de aplicaciones
	private String appFilter;
	
	// Filtro de etiquetas
	private String labelFilter;

	// Número de serie del certificado del usuario
	private String serialNumber;

	// Filtro de validadores
	private Boolean hasValidator;
		
	// Filtro para no validadas
	private Boolean viewNoValidate;

	// Filtro de tipos de petición
	private String requestTypeFilter;

	// Indica si se están paginando peticiones individuales
	private String requestPagination;
	
	// Fecha desde la que se recuperan peticiones
	private String sinceDate;
	
	// Fecha hasta la que se recuperan peticiones
	private String untilDate;
	
	// Mes de las peticiones
	private String monthFilter;
	
	// Año de las peticiones
	private String yearFilter;
	
	// Campo por el que se ordena
	private String orderField;
	
	// Sentido de la ordenación
	private String order;

	// Listado de mensajes a mostrar
    private List<AbstractBaseDTO> messageList;
    
    // Número de mensajes pendientes
 	private String numMessagesUnresolved;
 	
 	// Tiene mensajes pendientes
 	private Boolean hasMessages;
 	
 	private boolean fireActivo;
 	
 	private boolean administrator;
		
	public Inbox() {
		super();
		this.sinceDate = "";
		this.untilDate = "";
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<AbstractBaseDTO> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<AbstractBaseDTO> requestList) {
		this.requestList = requestList;
	}

	public String getNumUnresolved() {
		return numUnresolved;
	}

	public void setNumUnresolved(String numUnresolved) {
		this.numUnresolved = numUnresolved;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public String getRequestBarSelected() {
		return requestBarSelected;
	}

	public void setRequestBarSelected(String requestBarSelected) {
		this.requestBarSelected = requestBarSelected;
	}

	public Integer getCustomPageSize() {
		return customPageSize;
	}

	public void setCustomPageSize(Integer customPageSize) {
		this.customPageSize = customPageSize;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<AbstractBaseDTO> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(List<AbstractBaseDTO> applicationList) {
		this.applicationList = applicationList;
	}

	public Request getSelectedRequest() {
		return selectedRequest;
	}

	public void setSelectedRequest(Request selectedRequest) {
		this.selectedRequest = selectedRequest;
	}

	public String getSearchFilter() {
		return searchFilter;
	}

	public void setSearchFilter(String searchFilter) {
		this.searchFilter = searchFilter;
	}

	public String getAppFilter() {
		return appFilter;
	}

	public void setAppFilter(String appFilter) {
		this.appFilter = appFilter;
	}

	public String getLabelFilter() {
		return labelFilter;
	}

	public void setLabelFilter(String labelFilter) {
		this.labelFilter = labelFilter;
	}

	public List<AbstractBaseDTO> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<AbstractBaseDTO> labelList) {
		this.labelList = labelList;
	}

	public List<PfRequestTemplatesDTO> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<PfRequestTemplatesDTO> templateList) {
		this.templateList = templateList;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Boolean isHasValidator() {
		return hasValidator;
	}

	public void setHasValidator(Boolean hasValidator) {
		this.hasValidator = hasValidator;
	}

	public String getRequestTypeFilter() {
		return requestTypeFilter;
	}

	public void setRequestTypeFilter(String requestTypeFilter) {
		this.requestTypeFilter = requestTypeFilter;
	}

	public String getRequestPagination() {
		return requestPagination;
	}

	public void setRequestPagination(String requestPagination) {
		this.requestPagination = requestPagination;
	}
	
	public boolean isInRequest() {
		return Constants.MESSAGES_UNRESOLVED.equals(requestBarSelected) ||
				Constants.MESSAGES_AWAITING.equals(requestBarSelected) ||
				Constants.MESSAGES_FINISHED.equals(requestBarSelected) ||
				Constants.MESSAGES_EXPIRED.equals(requestBarSelected);
	}

	public boolean isOutRequest() {
		return Constants.MESSAGES_SENT.equals(requestBarSelected) ||
				Constants.MESSAGES_SENT_FINISHED.equals(requestBarSelected);
	}

	public String getSinceDate() {
		return sinceDate;
	}

	public void setSinceDate(String sinceDate) {
		this.sinceDate = sinceDate;
	}

	public String getUntilDate() {
		return untilDate;
	}

	public void setUntilDate(String untilDate) {
		this.untilDate = untilDate;
	}

	/**
	 * @return the messageList
	 */
	public List<AbstractBaseDTO> getMessageList() {
		return messageList;
	}

	/**
	 * @param messageList the messageList to set
	 */
	public void setMessageList(List<AbstractBaseDTO> messageList) {
		this.messageList = messageList;
	}

	/**
	 * @return the numMessagesUnresolved
	 */
	public String getNumMessagesUnresolved() {
		return numMessagesUnresolved;
	}

	/**
	 * @param numMessagesUnresolved the numMessagesUnresolved to set
	 */
	public void setNumMessagesUnresolved(String numMessagesUnresolved) {
		this.numMessagesUnresolved = numMessagesUnresolved;
	}

	/**
	 * @return the hasMessages
	 */
	public Boolean isHasMessages() {
		return hasMessages;
	}

	/**
	 * @param hasMessages the hasMessages to set
	 */
	public void setHasMessages(Boolean hasMessages) {
		this.hasMessages = hasMessages;
	}

	public List<Month> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<Month> monthList) {
		this.monthList = monthList;
	}

	public String getMonthFilter() {
		return monthFilter;
	}

	public void setMonthFilter(String monthFilter) {
		this.monthFilter = monthFilter;
	}

	public String getYearFilter() {
		return yearFilter;
	}

	public void setYearFilter(String yearFilter) {
		this.yearFilter = yearFilter;
	}

	public List<String> getYearList() {
		return yearList;
	}

	public void setYearList(List<String> yearList) {
		this.yearList = yearList;
	}

	public Boolean getViewNoValidate() {
		return viewNoValidate;
	}

	public void setViewNoValidate(Boolean viewNoValidate) {
		this.viewNoValidate = viewNoValidate;
	}

	public boolean isFireActivo() {
		return fireActivo;
	}

	public void setFireActivo(boolean fireActivo) {
		this.fireActivo = fireActivo;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

}
