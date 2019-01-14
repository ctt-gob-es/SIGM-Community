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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.ibm.icu.text.SimpleDateFormat;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;

import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.businessobject.ReportsLabelBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase que genera las etiquetas.
 * 
 * @author cmorenog
 */
public class ReportsLabelAction extends GenericActions implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ReportsLabelAction.class.getName());


    /** Bean que contiene información acerca del registro entrada. */
    private InputRegisterBean inputRegisterBean;
    /** Bean que contiene información acerca del registro salida. */
    private OutputRegisterBean outputRegisterBean;
    private ScrRegstate book;
    /** Clase con la lógica de negocio. */
    private ReportsLabelBo reportsBo;
    private static SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * objeto que contiene el fichero.
     */
    private StreamedContent file;
    private byte[] fileByte = null;

    /**
     * Constructor.
     */
    public ReportsLabelAction() {
    	reportsBo = new ReportsLabelBo();
    	}

    /**
     * Construye los informes de la aplicación correspondientes a certificados
     * de registros.
     */
    public void buildLabelReport() {
		LOG.trace("Entrando en ReportsLabelAction.buildLabelReport()");
		try {
		    init();
		    book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
		   
		    // Se ecogen el Id. o Ids. de registro que llegan
		    if (book.getIdocarchhdr().getType() == 1){
		    	if (facesContext.getViewRoot().getViewMap().get("inputRegisterAction") != null) {
	        		inputRegisterBean = ((InputRegisterAction) facesContext.getViewRoot().getViewMap().get("inputRegisterAction")).getInputRegisterBean();
	        		LOG.info("Llega un Id.: " + inputRegisterBean.getFdrid());
	        
	        		// Se lanza la consulta para obtener los datos
	        		LinkedHashMap<String, Object> reportResults = null;
	        		reportResults = buildLabel(inputRegisterBean);
	
	        		fileByte = reportsBo.buildLabelJasperReport(reportResults, book, String.valueOf(inputRegisterBean.getFdrid()), useCaseConf, inputRegisterBean.getFld1004());
	        		inputRegisterBean.setFld1004(reportsBo.getAcuseJson());
	        		((InputRegisterAction) facesContext.getViewRoot().getViewMap().get("inputRegisterAction")).setInputRegisterBean(inputRegisterBean);
	        	}
		    } else {
		    	if (facesContext.getViewRoot().getViewMap().get("outputRegisterAction") != null) {
	    		    outputRegisterBean = ((OutputRegisterAction) facesContext.getViewRoot().getViewMap().get("outputRegisterAction")).getOutputRegisterBean();
	    		    LOG.info("Llega un Id.: " + outputRegisterBean.getFdrid());
	            
	            	// Se lanza la consulta para obtener los datos
	            	LinkedHashMap<String, Object> reportResults = null;
	            	reportResults = buildLabel(outputRegisterBean);
	            	
	            	fileByte = reportsBo.buildLabelJasperReport(reportResults, book, String.valueOf(outputRegisterBean.getFdrid()), useCaseConf, outputRegisterBean.getFld1004());
	            	outputRegisterBean.setFld1004(reportsBo.getAcuseJson());
	            	((OutputRegisterAction) facesContext.getViewRoot().getViewMap().get("outputRegisterAction")).setOutputRegisterBean(outputRegisterBean);
	            }
		    }
		} catch (Exception exception) {
		    LOG.error("Error al generar las etiquetas: ", exception);
		    Utils.redirectToErrorPage(null, null, exception);
		}
    }

    private LinkedHashMap<String, Object> buildLabel(InputRegisterBean inputRegisterBean) {
    	LinkedHashMap<String, Object> reportResults = new LinkedHashMap<String, Object>();
    	// Se obtiene el Id del libro y se valida
    	Integer bookId;
    	try {
    		bookId = getAndValidateBook();
    		
    		reportResults.put("NUMERO_REGISTRO", inputRegisterBean.getFld1());
    		reportResults.put("FECHA_REGISTRO", formateador.format(inputRegisterBean.getFld2()));
    		ScrOfic ofic = inputRegisterBean.getFld5();
    		reportResults.put("CODIGOOFICINA", ofic.getCode());
    		reportResults.put("IDOFICINA", ofic.getId());
    		reportResults.put("OFICINA", ofic.getName());
    		reportResults.put("LIBRO", bookId);
    		reportResults.put("TIPO_REGISTRO", "E");
    		
    	} catch (SessionException bookException) {
    		LOG.error(ErrorConstants.GET_INFORMATION_BOOK_ERROR_MESSAGE, bookException);
    		Utils.redirectToErrorPage(null, bookException, null);
    	}
    	return reportResults;
    }

    private LinkedHashMap<String, Object> buildLabel(OutputRegisterBean outputRegisterBean) {
    	LinkedHashMap<String, Object> reportResults = new LinkedHashMap<String, Object>();
    	// Se obtiene el Id del libro y se valida
    	Integer bookId;
    	try {
    		bookId = getAndValidateBook();
    		
    		reportResults.put("NUMERO_REGISTRO", outputRegisterBean.getFld1());
    		reportResults.put("FECHA_REGISTRO", formateador.format(outputRegisterBean.getFld2()));
    		ScrOfic ofic = outputRegisterBean.getFld5();
    		reportResults.put("CODIGOOFICINA", ofic.getCode());
    		reportResults.put("IDOFICINA", ofic.getId());
    		reportResults.put("OFICINA", ofic.getName());
    		reportResults.put("LIBRO", bookId);
    		reportResults.put("TIPO_REGISTRO", "S");
    		
    	} catch (SessionException bookException) {
    		LOG.error(ErrorConstants.GET_INFORMATION_BOOK_ERROR_MESSAGE, bookException);
    		Utils.redirectToErrorPage(null, bookException, null);
    	}
    	return reportResults;
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
     * Devuelve el file.
     * 
     * @return file el objeto file.
     */
    public StreamedContent getFile() {
    	if (fileByte != null){
    		InputStream stream = new ByteArrayInputStream( fileByte );
    		//stream.mark(0); //remember to this position!
    		file = new DefaultStreamedContent(stream, KeysRP.MIME_TYPE_PDF, "etiqueta.pdf");
    	}
        return file;
    }

    /**
     * Método resetea los mensajes.
     */
    public void reset() {
    	file = null;
    }
}