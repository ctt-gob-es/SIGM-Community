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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.ieci.tecdoc.common.exception.SecurityException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.SessionInformation;
import com.ieci.tecdoc.isicres.usecase.book.xml.AsocRegsResults;
import com.ieci.tecdoc.isicres.web.util.ContextoAplicacionUtil;

import es.ieci.tecdoc.isicres.api.business.vo.BaseOficinaVO;
import es.ieci.tecdoc.isicres.api.business.vo.ContextoAplicacionVO;
import es.ieci.tecdoc.isicres.api.business.vo.OficinaVO;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaSalidaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralSIRVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralVO;
import es.msssi.sgm.registropresencial.beans.AsocRegisterBean;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ParamBookBean;
import es.msssi.sgm.registropresencial.beans.RowSearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchBoxRegInterchangeBean;
import es.msssi.sgm.registropresencial.businessobject.BooksBo;
import es.msssi.sgm.registropresencial.businessobject.InputRegisterBo;
import es.msssi.sgm.registropresencial.businessobject.InterestedBo;
import es.msssi.sgm.registropresencial.businessobject.OutputRegisterBo;
import es.msssi.sgm.registropresencial.businessobject.PermissionsBo;
import es.msssi.sgm.registropresencial.businessobject.RegInterchangeBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPBookException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPInputRegisterException;
import es.msssi.sgm.registropresencial.errors.RPOutputRegisterException;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.validations.ValidationBo;

/**
 * Action que gestiona la búsqueda de Intercambio Registral.
 * 
 * @author cmorenog
 * */
public class SearchOutboxRegInterchangeAction extends GenericActions implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SearchOutboxRegInterchangeAction.class
	.getName());
    /** bean de la búsqueda. */
    private SearchBoxRegInterchangeBean searchbean = null;
    /** lista de registros. */
    private List<BandejaSalidaItemVO> outBoxList = null;
    /** registro seleccionado . */
    private BandejaSalidaItemVO selectedRegister = null;
    private BandejaSalidaItemVO[] selectedRegisters = null;
    private RegInterchangeBo regInterchangeBo = null;
    private String observForward;
    private UnidadTramitacionIntercambioRegistralSIRVO unidadTramitadoraDestino;
    /**
     * Constructor.
     */
    public SearchOutboxRegInterchangeAction() {
	regInterchangeBo = new RegInterchangeBo();
	searchbean = new SearchBoxRegInterchangeBean();
	searchbean.setType("0");
	search();
    }

    /**
     * Muestra la bandeja de salida.
     */
    public void search() {
	init();
	ContextoAplicacionVO contextoAplicacion = null;
	BaseOficinaVO oficina = null;
	try {
	    contextoAplicacion =
		ContextoAplicacionUtil
		    .getContextoAplicacion((javax.servlet.http.HttpServletRequest) facesContext
			.getExternalContext().getRequest());
	    oficina = contextoAplicacion.getOficinaActual();
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.COPY_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(
		null, sessionException, null);
	}
	catch (TecDocException tecDocException) {
	    LOG.error(
		ErrorConstants.COPY_OUTPUT_REGISTER_ERROR_MESSAGE, tecDocException);
	    Utils.redirectToErrorPage(
		null, tecDocException, null);
	}
	// operativa de obtener la bandeja de salida
	try {
	    outBoxList = regInterchangeBo.getOutboxIR(
		searchbean.getOutState(), Integer.parseInt(oficina.getId()), searchbean.getBook());
	}
	catch (NumberFormatException numberFormatException) {
	    LOG.error(
		ErrorConstants.GET_OUTBOX_INTERCHANGE_ERROR_MESSAGE, numberFormatException);
	    Utils.redirectToErrorPage(
		null, null, numberFormatException);
	}
	catch (RPRegistralExchangeException e) {
	    LOG.error(
		ErrorConstants.GET_OUTBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    Utils.redirectToErrorPage(
		e, null, null);
	}

    }

    /**
     * Cambia de bandeja a la de entrada.
     */
    public void goInBoxRegInterchange() {
	if ("1".equals(searchbean.getType())) {
	    Utils.navigate("searchInboxRegInterchange.xhtml");
	}
    }

    /**
     * Obtiene el valor del parámetro searchbean.
     * 
     * @return searchbean valor del campo a obtener.
     */
    public SearchBoxRegInterchangeBean getSearchbean() {
	return searchbean;
    }

    /**
     * Guarda el valor del parámetro searchbean.
     * 
     * @param searchbean
     *            valor del campo a guardar.
     */
    public void setSearchbean(
	SearchBoxRegInterchangeBean searchbean) {
	this.searchbean = searchbean;
    }

    /**
     * Obtiene el valor del parámetro outBoxList.
     * 
     * @return outBoxList valor del campo a obtener.
     */
    public List<BandejaSalidaItemVO> getOutBoxList() {
	return outBoxList;
    }

    /**
     * Guarda el valor del parámetro outBoxList.
     * 
     * @param outBoxList
     *            valor del campo a guardar.
     */
    public void setOutBoxList(
	List<BandejaSalidaItemVO> outBoxList) {
	this.outBoxList = outBoxList;
    }

    /**
     * Obtiene el valor del parámetro selectedRegister.
     * 
     * @return selectedRegister valor del campo a obtener.
     */
    public BandejaSalidaItemVO getSelectedRegister() {
	return selectedRegister;
    }

    /**
     * Guarda el valor del parámetro selectedRegister.
     * 
     * @param selectedRegister
     *            valor del campo a guardar.
     */
    public void setSelectedRegister(
	BandejaSalidaItemVO selectedRegister) {
	this.selectedRegister = selectedRegister;
    }


    
    /**
     * Carga toda la bandeja de salida.
     * 
     * @param event
     *            evento lanzado de cambio de bandeja.
     */
    public void onInputRegister(
	SelectEvent event) {
	init();
	BandejaSalidaItemVO bandeja = ((BandejaSalidaItemVO) event.getObject());
	try {
	    clearView();
	    BooksBo booksBo = new BooksBo();

	    ScrRegstate bookID =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
		    KeysRP.J_BOOK);
	    if (bookID != null &&
		bookID.getIdocarchhdr().getId() != null) {
		booksBo.closeBook(
		    useCaseConf, bookID.getIdocarchhdr().getId());
	    }
	    booksBo.openBook(
		useCaseConf, Integer.valueOf(bandeja.getIdLibro().intValue()));
	    ScrRegstate book = booksBo.getBook(
		useCaseConf.getSessionID(), Integer.valueOf(bandeja.getIdLibro().intValue()));
	    facesContext.getExternalContext().getSessionMap().put(
		KeysRP.J_BOOK, book);

	    FacesContext.getCurrentInstance().getExternalContext().getFlash().put(
		"intercambio", true);
	    Map<String, Object> parameter = new HashMap<String, Object>();
	    parameter.put(
		"registerSelect", Integer.valueOf(bandeja.getIdRegistro().intValue()));
	    if (bandeja.getTipoLibro().equals(
		new Integer(
		    1))) {
		// si es de entrada (1)

		Utils.navigate(
		    parameter, false, "inputRegister.xhtml");
	    }
	    else {
		// si es de salida (2)
		Utils.navigate(
		    parameter, false, "outputRegister.xhtml");
	    }
	}
	catch (RPBookException rpBookException) {
	    LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE +
		". Código: " + rpBookException.getCode().getCode() + " . Mensaje: " +
		rpBookException.getShortMessage());
	    Utils.redirectToErrorPage(
		rpBookException, null, null);
	}
	catch (RPGenericException rpGenericException) {
	    LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE +
		". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
		rpGenericException.getShortMessage());
	    Utils.redirectToErrorPage(
		rpGenericException, null, null);
	}
    }
    
    /**
     * reenviar los registros seleccionados a otro organismo.
     */
    public void reenviar() {
	init();
	ContextoAplicacionVO contextoAplicacion = null;
	BaseOficinaVO oficina = null;
	UsuarioVO user = null;
	Integer idOficina = null;
	String codOficina = null;

	UnidadTramitacionIntercambioRegistralVO unidadTramitacionDestino =
	    new UnidadTramitacionIntercambioRegistralVO();

	if (unidadTramitadoraDestino == null ||
	    observForward == null || "".equals(observForward.trim())) {
	    FacesMessage message = new FacesMessage(
		FacesMessage.SEVERITY_INFO, "Validación", "Debe rellenar todos los campos.");
	    RequestContext.getCurrentInstance().showMessageInDialog(
		message);
	}
	else {
	    unidadTramitacionDestino.setCodeEntity(unidadTramitadoraDestino.getCodeEntity());
	    unidadTramitacionDestino.setNameEntity(unidadTramitadoraDestino.getNameEntity());
	    unidadTramitacionDestino.setCodeTramunit(unidadTramitadoraDestino.getCodeTramunit());
	    unidadTramitacionDestino.setNameTramunit(unidadTramitadoraDestino.getNameTramunit());
	    try {
		contextoAplicacion =
			    ContextoAplicacionUtil
				.getContextoAplicacion((javax.servlet.http.HttpServletRequest) facesContext
				    .getExternalContext().getRequest());
		SessionInformation sessionInformation =
			    (SessionInformation) facesContext.getExternalContext().getSessionMap().get(
				KeysRP.J_SESSIONINF);
		
		oficina = contextoAplicacion.getOficinaActual();
		user = contextoAplicacion.getUsuarioActual();
		user.setUserContact(sessionInformation.getUserContact());
		user.setFullName(sessionInformation.getUserName());

		idOficina = Integer.parseInt(oficina.getId());
		codOficina = ((OficinaVO) oficina).getCodigoOficina();
		}
		catch (SessionException sessionException) {
		    LOG.error(
			ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, sessionException);
		    Utils.redirectToErrorPage(
			null, sessionException, null);
		}
		catch (TecDocException tecDocException) {
		    LOG.error(
			ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, tecDocException);
		    Utils.redirectToErrorPage(
			null, tecDocException, tecDocException);
		}
	    try {
		regInterchangeBo.forward(
			selectedRegisters, observForward, unidadTramitacionDestino, user, idOficina,
			codOficina);
		clearView();
	    }
	    catch (RPRegistralExchangeException e) {
		LOG.error(
		    ErrorConstants.FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
		Utils.redirectToErrorPage(
		    e, null, null);
	    }
	}
    }
    /**
     * Método que actualiza el campo destino con el valor seleccionado en la
     * búsqueda avanzada de organismos.
     */
    public void updateDestinationDig() {
	UnidadTramitacionIntercambioRegistralSIRVO unidadTram = null;
	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
	    "UNITTRAMDIALOG") != null) {
	    unidadTram =
		(UnidadTramitacionIntercambioRegistralSIRVO) FacesContext.getCurrentInstance()
		    .getExternalContext().getSessionMap().get(
			"UNITTRAMDIALOG");
	    setUnidadTramitadoraDestino(unidadTram);
	}
    }

    /**
     * Obtiene el valor del parámetro observForward.
     * 
     * @return observForward valor del campo a obtener.
     */
    public String getObservForward() {
	return observForward;
    }

    /**
     * Guarda el valor del parámetro observForward.
     * 
     * @param observForward
     *            valor del campo a guardar.
     */
    public void setObservForward(
	String observForward) {
	this.observForward = observForward;
    }

    /**
     * Obtiene el valor del parámetro unidadTramitadoraDestino.
     * 
     * @return unidadTramitadoraDestino valor del campo a obtener.
     */
    public UnidadTramitacionIntercambioRegistralSIRVO getUnidadTramitadoraDestino() {
	return unidadTramitadoraDestino;
    }

    /**
     * Guarda el valor del parámetro unidadTramitadoraDestino.
     * 
     * @param unidadTramitadoraDestino
     *            valor del campo a guardar.
     */
    public void setUnidadTramitadoraDestino(
	UnidadTramitacionIntercambioRegistralSIRVO unidadTramitadoraDestino) {
	this.unidadTramitadoraDestino = unidadTramitadoraDestino;
    }


    /**
     * Guarda la entidad nueva a la que se reenvía el movimiento registral.
     */
    public void changeEntity() {

    }
    
    /**
     * Método que comprueba si hay seleccionado algún elemento. Si no hay
     * elementos seleccionados muestra un mensaje de error.
     */
    public void isSelect() {
	if (selectedRegisters == null ||
	    selectedRegisters.length == 0) {
	    ValidationBo.showDialog(
		"Validación", new FacesMessage(
		    FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un asiento."));
	}

    }

    public BandejaSalidaItemVO[] getSelectedRegisters() {
        return selectedRegisters;
    }

    public void setSelectedRegisters(BandejaSalidaItemVO[] selectedRegisters) {
        this.selectedRegisters = selectedRegisters;
    }

    public void rectificar() {
	       init();
	       // Usar selectedRegisters;
//	       List<BandejaSalidaItemVO> listBandejaSalida =  getOutBoxList();       
	       if(selectedRegisters == null || selectedRegisters.length == 0 || selectedRegisters.length > 1 )
	          return;
	    
	       try {
	           clearView();
	           
	           BooksBo booksBo = new BooksBo();

	           ScrRegstate bookID =
	             (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
	                 KeysRP.J_BOOK);
	           if (bookID != null &&
	             bookID.getIdocarchhdr().getId() != null) {
	             booksBo.closeBook(
	                 useCaseConf, bookID.getIdocarchhdr().getId());
	           }
	           
	           InputRegisterBean inputRegisterBean = null;
	           OutputRegisterBean outputRegisterBean = null;
	           BandejaSalidaItemVO itemVO = null;
	           for(int i=0;i<selectedRegisters.length;i++){
	                
	                  itemVO = selectedRegisters[i];
	                  
	                  booksBo.openBook(
	                    useCaseConf, Integer.valueOf(itemVO.getIdLibro().intValue()));
	                  ScrRegstate book = booksBo.getBook(
	                    useCaseConf.getSessionID(), Integer.valueOf(itemVO.getIdLibro().intValue()));
	                  facesContext.getExternalContext().getSessionMap().put(
	                    KeysRP.J_BOOK, book);
	                  try {
	          	    /* Configurar permisos del usuario sobre el libro */
	          	    ParamBookBean param;
        	          	  PermissionsBo permissionBo = new PermissionsBo();
        	          	    param =
        	          		    permissionBo.getPermission(useCaseConf, facesContext.getExternalContext()
        	          			    .getSessionMap().get(KeysRP.J_PARAMBOOK), Integer.valueOf(itemVO.getIdLibro().intValue()), false);
        
        	          	    facesContext.getExternalContext().getSessionMap().put(KeysRP.J_PARAMBOOK, param);
        	          	}
        	          	catch (SessionException sessionException) {
        	          	    LOG.error(ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, sessionException);
        	          	    Utils.redirectToErrorPage(null, sessionException, null);
        	          	}
        	          	catch (ValidationException validationException) {
        	          	    LOG.error(ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, validationException);
        	          	    Utils.redirectToErrorPage(null, validationException, null);
        	          	}
        	          	catch (SecurityException securityException) {
        	          	    LOG.error(ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, securityException);
        	          	    Utils.redirectToErrorPage(null, securityException, null);
        	          	}
	                  Integer idRegistro = itemVO.getIdRegistro().intValue();
	                  InputRegisterBo inputRegisterBo = new InputRegisterBo();         
	                  OutputRegisterBo outputRegisterBo = new OutputRegisterBo();    
	                  InterestedBo interestedBo = new InterestedBo();              
	                  // COPIAR REGISTRO E INTERESADOS
	                  if (itemVO.getTipoLibro().equals(
		                    new Integer( 1))) {
                         
        	                  inputRegisterBean = inputRegisterBo.copyInputRegisterBean(useCaseConf, book, idRegistro);
        	                  inputRegisterBean.setFld18("Rectificación del registro "+ itemVO.getNumeroRegistro());
      	                    
        	                  List<Interesado> interadosCon = 
        	                                  interestedBo.getAllInterested(book.getIdocarchhdr().getId(), idRegistro, useCaseConf);
        	                  inputRegisterBean.setInteresados(interadosCon);
        	                  
        	                  // INSERTAR REGISTRO                    
        	                  inputRegisterBean.setFdrid(InputRegisterBo.saveOrUpdateFolder(useCaseConf, book.getIdocarchhdr().getId(), null, null, inputRegisterBean,
        	                                        inputRegisterBean.getInteresados(), null));
        	                  

        	                  RegisterBo registerBo = new  RegisterBo();   
        	                  AsocRegisterBean asocRegisterBean= new AsocRegisterBean();
        	                  asocRegisterBean.setFld1(itemVO.getNumeroRegistro());
        	                  asocRegisterBean.setIdLibro(book.getIdocarchhdr().getId());
        	                  registerBo.saveAssociation(useCaseConf, asocRegisterBean,
        	               			    inputRegisterBean.getFdrid(), book.getIdocarchhdr().getId());
        	                  
        	                  try {
    	      				regInterchangeBo.update(itemVO, String.valueOf(inputRegisterBean.getFdrid()) );
                	      	    }
                	      	    catch (RPRegistralExchangeException e) {
                	      		LOG.error(
                	      		    ErrorConstants.FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
                	      		Utils.redirectToErrorPage(
                	      		    e, null, null);
                	      	    }
    	                   
        	                  
	                  } else {

	                      outputRegisterBean = outputRegisterBo.copyOutputRegisterBean(useCaseConf, book, idRegistro);
	                      outputRegisterBean.setFld14("Rectificación del registro "+ itemVO.getNumeroRegistro());
  	                    
    	                  List<Interesado> interadosCon = 
    	                                  interestedBo.getAllInterested(book.getIdocarchhdr().getId(), idRegistro, useCaseConf);
    	                  outputRegisterBean.setInteresados(interadosCon);
    	                  
    	                  // INSERTAR REGISTRO                    
    	                  outputRegisterBean.setFdrid(OutputRegisterBo.saveOrUpdateFolder(useCaseConf, book.getIdocarchhdr().getId(), null, null, outputRegisterBean,
    	                                        outputRegisterBean.getInteresados(), null));

        	                  RegisterBo registerBo = new  RegisterBo();   
        	                  AsocRegisterBean asocRegisterBean= new AsocRegisterBean();
        	                  asocRegisterBean.setFld1(itemVO.getNumeroRegistro());
        	                  asocRegisterBean.setIdLibro(book.getIdocarchhdr().getId());
        	                  registerBo.saveAssociation(useCaseConf, asocRegisterBean,
	               			    outputRegisterBean.getFdrid(), book.getIdocarchhdr().getId());
        	                  try {
      	      				regInterchangeBo.update(itemVO, String.valueOf(outputRegisterBean.getFdrid()) );
                  	      	    }
                  	      	    catch (RPRegistralExchangeException e) {
                  	      		LOG.error(
                  	      		    ErrorConstants.FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
                  	      		Utils.redirectToErrorPage(
                  	      		    e, null, null);
                  	      	    }
      	                  
	                  }

	                
	                  
	                 
	           }
	           
	           itemVO = selectedRegisters[selectedRegisters.length-1];//.get(listBandejaSalida.size()-1);
	           

	           // REDIRECCIÓN AL ÚLTIMO REGISTRO COPIADO
	                  FacesContext.getCurrentInstance().getExternalContext().getFlash().put(
	                    "intercambio", true);
	                  Map<String, Object> parameter = new HashMap<String, Object>();
	                 
	                  if (itemVO.getTipoLibro().equals(
	                    new Integer(
	                        1))) {
	                    // si es de entrada (1)
	                      parameter.put(
	  	                    "registerSelect", inputRegisterBean.getFdrid());
	                    Utils.navigate(
	                        parameter, false, "inputRegister.xhtml");
	                  }
	                  else {
	                    // si es de salida (2)
	                      parameter.put(
	  	                    "registerSelect", outputRegisterBean.getFdrid());
	                    Utils.navigate(
	                        parameter, false, "outputRegister.xhtml");
	                  }
	             }
	             catch (RPBookException rpBookException) {
	                  LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE +
	                    ". Código: " + rpBookException.getCode().getCode() + " . Mensaje: " +
	                    rpBookException.getShortMessage());
	                  Utils.redirectToErrorPage(
	                    rpBookException, null, null);
	             }
	             catch (RPGenericException rpGenericException) {
	                  LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE +
	                    ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
	                    rpGenericException.getShortMessage());
	                  Utils.redirectToErrorPage(
	                    rpGenericException, null, null);
	             }
	            catch (RPInputRegisterException rpInputRegisterException) {
	                    LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
	                           + rpInputRegisterException.getCode().getCode() + " . Mensaje: "
	                           + rpInputRegisterException.getShortMessage());
	                    Utils.redirectToErrorPage(rpInputRegisterException, null, null);
	            }
	       	catch (RPOutputRegisterException rpOutputRegisterException) {
	                    LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
		                           + rpOutputRegisterException.getCode().getCode() + " . Mensaje: "
		                           + rpOutputRegisterException.getShortMessage());
		                    Utils.redirectToErrorPage(rpOutputRegisterException, null, null);
		            }
	    catch (RPRegisterException e) {
		 LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
                         + e.getCode().getCode() + " . Mensaje: "
                         + e.getShortMessage());
                  Utils.redirectToErrorPage(e, null, null);
		    }
	       
	    }

    
}