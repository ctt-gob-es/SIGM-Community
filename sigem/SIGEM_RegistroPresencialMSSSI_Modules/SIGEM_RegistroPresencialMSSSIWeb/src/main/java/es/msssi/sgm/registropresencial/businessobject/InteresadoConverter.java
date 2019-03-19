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

import java.util.ArrayList;
import java.util.List;

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
import es.ieci.tecdoc.isicres.terceros.business.manager.TerceroManager;
import es.ieci.tecdoc.isicres.terceros.business.vo.DireccionFisicaVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoFisicoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoJuridicoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.search.CriteriaVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.search.FilterVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.search.OperatorEnum;
import es.ieci.tecdoc.isicres.terceros.business.vo.search.SearchType;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.provider.ISicresBeanTercerosProvider;

/**
 * Clase que implementa converter para el combo de Organismos de origen. Permite
 * que los valores de un combo sean Beans.
 * 
 * @author cmorenog
 */
@FacesConverter(value = "interesadoConverter")
public class InteresadoConverter implements Converter {
	private static final Logger LOG = Logger.getLogger(InteresadoConverter.class);
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
		LOG.trace("Entrando en interesadoConverter.getAsObject()");
		Interesado result = null;
		if(null == submittedValue.trim() || "".equals(submittedValue.trim()) || "null".equals(submittedValue.trim())){
			result = null;
		} else {
			try {
				useCaseConf = (UseCaseConf) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Keys.J_USECASECONF);				
				String id = submittedValue.trim();
				result = getInteresado(id);
				if (null == result) {
					LOG.error("Error al convertir el interesado");
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE, ErrorConstants.INTERESADO_DOES_NOT_EXISTS_MESSAGE));
				}
			} catch (NumberFormatException exception) {
				LOG.error("Error al parsear el Id a convertir", exception);
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE,
						ErrorConstants.INTERESADO_DOES_NOT_EXISTS_MESSAGE));
			}
			LOG.info("Interesado convertido con id " + result.getId());
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
	public String getAsString(FacesContext facesContext, UIComponent component,
			Object value) {
		String result = "";
		LOG.trace("Entrando en interesadoConverter.getAsString()");
		if (value == null || "".equals(value)) {
			result = "";
		} else {
			result = String.valueOf(((Interesado) value).getId());
		}
		return result;
	}
	
	private Interesado getInteresado(String id){
		Interesado interesado = null;
		Transaction tran = null;
    	try {
    		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
	    	Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			    tran = session.beginTransaction();
	    	//Se compone la consulta para persona fisica
			CriteriaVO criterioInterFisico = new CriteriaVO();
			criterioInterFisico.setType(SearchType.FISICO);
		
			List<FilterVO> filtersFisico = new ArrayList<FilterVO>();
			//Filtro por nif
			FilterVO terceroFisicoNIF = new FilterVO();
			terceroFisicoNIF.setField("cast (id as varchar(100))");
			terceroFisicoNIF.setOperator(OperatorEnum.ES_IGUAL);
			terceroFisicoNIF.setValue(id);
			filtersFisico.add(terceroFisicoNIF);
		
			//Seteamos los filtros de persona fisica al criterio de busqueda
			criterioInterFisico.setFilters(filtersFisico);
		
			//Obtenemos los datos de la busqueda para persona fisica
			TerceroManager terceroManager = ISicresBeanTercerosProvider.getInstance().getTerceroManager();
			List<TerceroValidadoVO> tercerosFisicos = terceroManager.findByCriteria(criterioInterFisico);		
	
			List<Interesado> resultInteresados = new ArrayList<Interesado>();
	
			if((tercerosFisicos.size()>0) && !(tercerosFisicos.isEmpty())){
				// Si la busqueda de interesados fisicos tiene datos, se asigna al/ resultado de busqueda			
				for(TerceroValidadoVO terceroVO : tercerosFisicos){
					Interesado inter = new Interesado();
				    TerceroValidadoFisicoVO tercero = (TerceroValidadoFisicoVO) terceroVO;
				    inter.setId(tercero.getIdAsInteger());
				    inter.setIdTercero(tercero.getIdAsInteger());
				    inter.setTipo("P");
				    if (tercero.getTipoDocumento() != null) {
				    	inter.setTipodoc(tercero.getTipoDocumento().getId());
				    }
				    inter.setDocIdentidad(tercero.getNumeroDocumento());
				    inter.setNombre(tercero.getNombre());
				    inter.setPapellido(tercero.getApellido1());
				    inter.setSapellido(tercero.getApellido2());
				    

				    inter.setDireccionesFisicas(tercero.getDireccionesFisicas());
				    inter.setDireccionFisicaPrincipal(tercero.getDireccionFisicaPrincipal());
				    inter.setDireccionesTelematicas(tercero.getDireccionesTelematicas());
				    inter.setDireccionTelematicaPrincipal(tercero.getDireccionTelematicaPrincipal());
				    
				    if( null != tercero.getDireccionFisicaPrincipal()){
				    	DireccionFisicaVO direccionSeleccionadaAux = new DireccionFisicaVO();
				    	
				    	direccionSeleccionadaAux.setId(tercero.getDireccionFisicaPrincipal().getId());
				    	direccionSeleccionadaAux.setCiudad(tercero.getDireccionFisicaPrincipal().getCiudad());
				    	direccionSeleccionadaAux.setCodigoPostal(tercero.getDireccionFisicaPrincipal().getCodigoPostal());
				    	direccionSeleccionadaAux.setDireccion(tercero.getDireccionFisicaPrincipal().getDireccion());
				    	direccionSeleccionadaAux.setPais(tercero.getDireccionFisicaPrincipal().getPais());
				    	direccionSeleccionadaAux.setPrincipal(tercero.getDireccionFisicaPrincipal().isPrincipal());
				    	direccionSeleccionadaAux.setProvincia(tercero.getDireccionFisicaPrincipal().getProvincia());
				    	direccionSeleccionadaAux.setTercero(tercero.getDireccionFisicaPrincipal().getTercero());
				    	direccionSeleccionadaAux.setTipo(tercero.getDireccionFisicaPrincipal().getTipo());
				    	
						inter.setDireccionSeleccionada(direccionSeleccionadaAux );
				    }
				    
				    resultInteresados.add(inter);
				}
			} else{
				//Se compone la consulta para persona juridica
				CriteriaVO criteriaInterJuridico = new CriteriaVO();
				criteriaInterJuridico.setType(SearchType.JURIDICO);
			
				List<FilterVO> filtersJuridico = new ArrayList<FilterVO>();
				//Filtro por cif
				FilterVO terceroJuridicoCIF = new FilterVO();
				terceroJuridicoCIF.setField("cast (id as varchar(100))");
				terceroJuridicoCIF.setOperator(OperatorEnum.ES_IGUAL);
				terceroJuridicoCIF.setValue(id);
				filtersJuridico.add(terceroJuridicoCIF);
			
				//Seteamos los filtros de persona juridica al criterio de busqueda
				criteriaInterJuridico.setFilters(filtersJuridico);
			
				//Obtenemos los datos de la busqueda para persona juridica
				List<TerceroValidadoVO> tercerosJuridicos = terceroManager.findByCriteria(criteriaInterJuridico);
			
				// Generamos el objeto que contiene el resultado de busqueda
				if((tercerosJuridicos.size()>0) && !(tercerosJuridicos.isEmpty())){
					// Si la busqueda de interesados juridico tiene datos, se asigna al resultado de busqueda
					for(TerceroValidadoVO terceroVO : tercerosJuridicos){
						Interesado inter = new Interesado();
						TerceroValidadoJuridicoVO tercero = (TerceroValidadoJuridicoVO) terceroVO;
						inter.setId(tercero.getIdAsInteger());
						inter.setIdTercero(tercero.getIdAsInteger());
						inter.setTipo("J");
						if (tercero.getTipoDocumento() != null) {
							inter.setTipodoc(tercero.getTipoDocumento().getId());
						}
						inter.setDocIdentidad(tercero.getNumeroDocumento());
						inter.setRazonSocial(tercero.getRazonSocial());
						
					    inter.setDireccionesFisicas(tercero.getDireccionesFisicas());
					    inter.setDireccionFisicaPrincipal(tercero.getDireccionFisicaPrincipal());
					    inter.setDireccionesTelematicas(tercero.getDireccionesTelematicas());
					    inter.setDireccionTelematicaPrincipal(tercero.getDireccionTelematicaPrincipal());
					    
					    if( null != tercero.getDireccionFisicaPrincipal()){
					    	DireccionFisicaVO direccionSeleccionadaAux = new DireccionFisicaVO();
					    	
					    	direccionSeleccionadaAux.setId(tercero.getDireccionFisicaPrincipal().getId());
					    	direccionSeleccionadaAux.setCiudad(tercero.getDireccionFisicaPrincipal().getCiudad());
					    	direccionSeleccionadaAux.setCodigoPostal(tercero.getDireccionFisicaPrincipal().getCodigoPostal());
					    	direccionSeleccionadaAux.setDireccion(tercero.getDireccionFisicaPrincipal().getDireccion());
					    	direccionSeleccionadaAux.setPais(tercero.getDireccionFisicaPrincipal().getPais());
					    	direccionSeleccionadaAux.setPrincipal(tercero.getDireccionFisicaPrincipal().isPrincipal());
					    	direccionSeleccionadaAux.setProvincia(tercero.getDireccionFisicaPrincipal().getProvincia());
					    	direccionSeleccionadaAux.setTercero(tercero.getDireccionFisicaPrincipal().getTercero());
					    	direccionSeleccionadaAux.setTipo(tercero.getDireccionFisicaPrincipal().getTipo());
					    	
							inter.setDireccionSeleccionada(direccionSeleccionadaAux );
					    }
		
					    resultInteresados.add(inter);
					}
				}
			}			
			if (null != resultInteresados && resultInteresados.size() > 0) {
				interesado = resultInteresados.get(0);
			}
			HibernateUtil.commitTransaction(tran);
    	}
    	catch (Exception exception) {
    	    LOG.error(ErrorConstants.GET_OFFICES_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", exception);
    	    HibernateUtil.rollbackTransaction(tran);    	    
    	}
    	finally {
    	    HibernateUtil.closeSession(useCaseConf.getEntidadId());
    	}
    	return interesado;
    }
}