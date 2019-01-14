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
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import com.ieci.tecdoc.common.invesdoc.Iuserdepthdr;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.IuserdepthdrDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;

/**
 * Clase que implementa el converter para el combo de departamentos. Permite que
 * los valores de un combo sean Beans.
 * 
 * @author cmorenog
 */
@FacesConverter(value = "departConverter")
public class DepartConverter implements Converter {
	
	private static final Logger LOG = Logger.getLogger(DepartConverter.class);
	
	private List<Iuserdepthdr> listDepart;
	private static ApplicationContext appContext;
	
	static {
		appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();

	}

	/**
	 * Convierte un Id de departamentos a Objeto.
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
		IuserdepthdrDAO iuserdepthdrDAO = null;
		
		if ("".equals(submittedValue.trim())) {
			LOG.info("El Id del departamento a convertir está vacío. El usuario queda nulo");
			result = null;
			
		} else {
			try {
				iuserdepthdrDAO = (IuserdepthdrDAO) appContext.getBean("iuserdepthdrDAO");
				int number = Integer.parseInt(submittedValue);
				Iuserdepthdr p = iuserdepthdrDAO.getDepart(number);
				
				LOG.info("Departamento convertido con id " + p.getId());
				
				result = p;
			} catch (NumberFormatException exception) {
				LOG.error("Error al parsear el Id a convertir", exception);
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE, ErrorConstants.USER_DOES_NOT_EXISTS_MESSAGE));
				
			}
			if (result == null) {
				LOG.error("Error al convertir el departamento");
				throw new ConverterException(new FacesMessage( FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE, ErrorConstants.USER_DOES_NOT_EXISTS_MESSAGE));
			}
		}
		return result;
	}

	/**
	 * Obtiene un Id de departamento a String.
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
			LOG.info("El Id del departamento a convertir es nulo o está vacío." + " El departamento queda nulo");
			idConverted = null;
			
		} else {
			idConverted = String.valueOf(((Iuserdepthdr) value).getId());
			LOG.info("Departamento convertido con Id " + idConverted);
		}
		
		return idConverted;
	}

	/**
	 * Obtiene la lista de departamentos.
	 * 
	 * @return listDepartamentos Lista de departamentos obtenida.
	 */
	public List<Iuserdepthdr> getListDepart() {
		return listDepart;
	}

	/**
	 * Guarda la lista de departamentos.
	 * 
	 * @param listDepart
	 *            Lista de departamentos a guardar.
	 */
	public void setListDepart(List<Iuserdepthdr> listDepart) {
		this.listDepart = listDepart;
	}
}