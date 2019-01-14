/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.businessobject;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.ieci.tecdoc.common.invesdoc.Iuseruserhdr;
import com.ieci.tecdoc.common.utils.ISicresQueries;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;

import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;

/**
 * Clase que implementa el converter para el combo de usuarios. Permite que
 * los valores de un combo sean Beans.
 * 
 * @author cmorenog
 */
@FacesConverter(value = "userConverter")
public class UserConverter implements Converter {
	
	private static final Logger LOG = Logger.getLogger(UserConverter.class);
	
	private List<Iuseruserhdr> listUsuarios;
	protected UseCaseConf useCaseConf = null;
	@SuppressWarnings("unused")
	private static ApplicationContext appContext;
	
	static {
		appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();

	}

	/**
	 * Convierte un Id de Usuario a Objeto.
	 * 
	 * @param facesContext
	 *            Contexto actual de Faces.
	 * @param component
	 *            Componente UI de Faces.
	 * @param submittedValue
	 *            Id a convertir.
	 * 
	 * @return stringInObject El objeto convertido o error si no existe.
	 */
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		
		LOG.trace("Entrando en UserConverter.getAsObject()");
		
		Object result = null;
		
		if ("".equals(submittedValue.trim())) {
			LOG.info("El Id del usuario a convertir está vacío. El usuario queda nulo");
			result = null;
			
		} else {
			try {
				int number = Integer.parseInt(submittedValue);
				
				useCaseConf = (UseCaseConf) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Keys.J_USECASECONF);
				Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
				
				Iuseruserhdr usuario =  ISicresQueries.getUserUserHdrById(session, number);
				
				LOG.info("usuario convertido con id " + usuario.getId());
				
				result = usuario;
				
			} catch (NumberFormatException exception) {
				LOG.error("Error al parsear el Id a convertir", exception);
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE, ErrorConstants.USER_DOES_NOT_EXISTS_MESSAGE));
				
			} catch (HibernateException e) {
				LOG.error("Error al parsear el Id a convertir", e);
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE, ErrorConstants.USER_DOES_NOT_EXISTS_MESSAGE));
			}
		}
		
		return result;
	}

	/**
	 * Obtiene un Id de usuario a String.
	 * 
	 * @param facesContext
	 *            Contexto actual de Faces.
	 * @param component
	 *            Componente UI de Faces.
	 * @param value
	 *            Id a convertir.
	 * 
	 * @return El String convertido.
	 */
	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		
		LOG.trace("Entrando en UserConverter.getAsString()");
		
		String idConverted = null;
		
		if (value == null) {
			LOG.info("El Id del usuario a convertir es nulo o está vacío." + " El usuario queda nulo");
			idConverted = null;
			
		} else {
			idConverted = String.valueOf(((Iuseruserhdr) value).getId());
			LOG.info("Usuario convertido con Id " + idConverted);
		}
		
		return idConverted;
	}

	/**
	 * Obtiene la lista de usuarios.
	 * 
	 * @return listUsuarios Lista de usuarios obtenida.
	 */
	public List<Iuseruserhdr> getListUsuarios() {
		return listUsuarios;
	}

	/**
	 * Guarda la lista de usuarios.
	 * 
	 * @param listUsuarios
	 *            Lista de usuarios a guardar.
	 */

	public void setListUsuarios(List<Iuseruserhdr> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}
}