package es.dipucr.sgm.registropresencial.bussinessobject;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;

import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.terceros.business.manager.DireccionManager;
import es.ieci.tecdoc.isicres.terceros.business.manager.TerceroManager;
import es.ieci.tecdoc.isicres.terceros.business.manager.impl.TerceroManagerImpl;
import es.ieci.tecdoc.isicres.terceros.business.vo.BaseDireccionVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.DireccionFisicaVO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.provider.ISicresBeanTercerosProvider;

/**
 * Clase que implementa converter para el combo de Organismos de origen. Permite
 * que los valores de un combo sean Beans.
 * 
 * @author cmorenog
 */
@FacesConverter(value = "direccionFisicaConverter")
public class DireccionFisicaVOConverter implements Converter {
	private static final Logger LOG = Logger.getLogger(DireccionFisicaVOConverter.class);
	protected UseCaseConf useCaseConf = null;

	/**
	 * Convierte Un Id de interesado a Objeto.
	 * 
	 * @param facesContext
	 *            Contexto actual de Faces.
	 * @param component
	 *            Componente UI de Faces.
	 * @param submittedValue
	 *            Id a convertir.
	 * 
	 * @return El objeto convertido o error si no existe.
	 */
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		LOG.trace("Entrando en DireccionFisicaVOConverter.getAsObject()");
		DireccionFisicaVO result = null;
		if(null == submittedValue.trim() || "".equals(submittedValue.trim()) || "null".equals(submittedValue.trim())){
			result = null;
		} else {
			try {
				useCaseConf = (UseCaseConf) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Keys.J_USECASECONF);				
				String id = submittedValue.trim();
				result = getDireccionFisicaVO(id);
				if (null == result) {
					LOG.error("Error al convertir la dirección física");
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE, ErrorConstants.INTERESADO_DOES_NOT_EXISTS_MESSAGE));
				}
			} catch (NumberFormatException exception) {
				LOG.error("Error al parsear el Id a convertir", exception);
				throw new ConverterException(new FacesMessage( FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE, ErrorConstants.INTERESADO_DOES_NOT_EXISTS_MESSAGE));
			}
			LOG.info("Dirección física convertida con id " + result.getId());
		}
		return result;
	}

	/**
	 * Obtiene un Id de interesado a String.
	 * 
	 * @param facesContext 
	 *            Contexto actual de Faces.
	 * @param component
	 *            Componente UI de Faces.
	 * @param value
	 *            Id a convertir.
	 * 
	 * @return objectInString El valor convertido.
	 */
	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		String result = "";
		LOG.trace("Entrando en DireccionFisicaVOConverter.getAsString()");
		if (value == null || "".equals(value)) {
			result = "";
		} else {
			result = ((BaseDireccionVO) value).getId();
		}
		return result;
	}
	
	private DireccionFisicaVO getDireccionFisicaVO(String id){
		DireccionFisicaVO direccionFisicaVO = null;
		Transaction tran = null;
    	try {
    		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
	    	Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
	    	tran = session.beginTransaction();
		
			//Obtenemos los datos de la busqueda para persona fisica
			TerceroManager terceroManager = ISicresBeanTercerosProvider.getInstance().getTerceroManager();			
			DireccionManager direccionManager = ((TerceroManagerImpl)terceroManager).getDireccionManager();					
	
			direccionFisicaVO = (DireccionFisicaVO) direccionManager.get(id);
	
			HibernateUtil.commitTransaction(tran);
    	
    	} catch (Exception exception) {
    	    LOG.error(ErrorConstants.GET_OFFICES_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", exception);
    	    HibernateUtil.rollbackTransaction(tran);    	    
    	}
    	finally {
    	    HibernateUtil.closeSession(useCaseConf.getEntidadId());
    	}
    	return direccionFisicaVO;
    }
}