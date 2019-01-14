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

import com.ieci.tecdoc.common.invesdoc.Iusergrouphdr;
import com.ieci.tecdoc.common.utils.ISicresQueries;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;

import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;

/**
 * Clase que implementa el converter para el combo de grupos. Permite que
 * los valores de un combo sean Beans.
 * 
 * @author cmorenog
 */
@FacesConverter(value = "groupConverter")
public class GroupConverter implements Converter {
	
	private static final Logger LOG = Logger.getLogger(GroupConverter.class);
	
	private List<Iusergrouphdr> listGroup;
	protected UseCaseConf useCaseConf = null;

	@SuppressWarnings("unused")
	private static ApplicationContext appContext;
	
	static {
		appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
	}

	/**
	 * Convierte un Id de grupos a Objeto.
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
		
		LOG.trace("Entrando en DepartConverter.getAsObject()");
		
		Object result = null;
		
		if ("".equals(submittedValue.trim())) {
			LOG.info("El Id del grupo a convertir está vacío. El usuario queda nulo");
			result = null;
			
		} else {
			try {
				int number = Integer.parseInt(submittedValue);
				
				useCaseConf = (UseCaseConf) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Keys.J_USECASECONF);
				Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
				
				Iusergrouphdr grupo =  ISicresQueries.getUserGroupHdrByDeptId(session, number);
				
				LOG.info("Grupo convertido con id " + grupo.getId());
				
				result = grupo;
				
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
	 * Obtiene un Id de grupo a String.
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
			LOG.info("El Id del grupo a convertir es nulo o está vacío." + " El grupo queda nulo");
			idConverted = null;
			
		} else {
			idConverted = String.valueOf(((Iusergrouphdr) value).getId());
			LOG.info("Grupo convertido con Id " + idConverted);
		}
		
		return idConverted;
	}

	/**
	 * Obtiene la lista de grupos.
	 * 
	 * @return listGroup Lista de grupos obtenida.
	 */
	public List<Iusergrouphdr> getListGroup() {
		return listGroup;
	}
	/**
	 * Guarda la lista de grupos.
	 * 
	 * @param listGroup
	 *            Lista de grupos a guardar.
	 */
	public void setListGroup(List<Iusergrouphdr> listGroup) {
		this.listGroup = listGroup;
	}
}