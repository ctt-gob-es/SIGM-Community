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

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.isicres.context.ISicresBeansProvider;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.terceros.business.manager.DireccionManager;
import es.ieci.tecdoc.isicres.terceros.business.manager.InteresadoManager;
import es.ieci.tecdoc.isicres.terceros.business.manager.TerceroManager;
import es.ieci.tecdoc.isicres.terceros.business.manager.impl.TerceroManagerImpl;
import es.ieci.tecdoc.isicres.terceros.business.vo.BaseDireccionVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.BaseTerceroVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.DireccionFisicaVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.InteresadoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.RepresentanteInteresadoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoFisicoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoJuridicoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TipoDocumentoIdentificativoTerceroVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.enums.DireccionType;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.Representante;
import es.msssi.sgm.registropresencial.provider.ISicresBeanTercerosProvider;

/**
 * Clase que implementa IGenericBo que contiene los métodos relacionados con los
 * interesados.
 * 
 * @author cmorenog
 */
public class InterestedBo implements IGenericBo, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(InterestedBo.class.getName());
	private static TerceroManager terceroManager = null;
	private static InteresadoManager interesadoManager = null;

	public InterestedBo() {
		if (terceroManager == null) {
			interesadoManager = ISicresBeansProvider.getInstance().getInteresadoManager();
			terceroManager = ISicresBeanTercerosProvider.getInstance().getTerceroManager();
		}
	}

	/**
	 * Añade los interesados al registro.
	 * 
	 * @param inter
	 *            lista de interesados.
	 * @param bookID
	 *            libro.
	 * @param useCaseConf
	 *            configuración
	 * @param registerID
	 *            id del registro.
	 */
	public void addInterested(List<Interesado> inter, Integer bookID, Integer registerID, UseCaseConf useCaseConf) {
		
		LOG.trace("Entrando en Interested.addInterested()");
		// 1. borro los interesados que hay
		LOG.trace("Entrando en SearchInputRegisterBo.load()");
		LOG.trace("Borrando interesados");

		interesadoManager.deleteAll(bookID.toString(), String.valueOf(registerID));
		
		// 2. Inserto los terceros
		int order = 1;
		
		// seteamos al thread local		
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());

		TerceroValidadoVO tercero;
		LOG.trace("Insertando terceros");
		
		for (Interesado intere : inter) {
			
			if ((intere.getTipo().equals("P") && StringUtils.isNotEmpty(intere.getNombre())) || (intere.getTipo().equals("J") && StringUtils.isNotEmpty(intere.getRazonSocial()))) {
				
				tercero = addTerceros(intere, useCaseConf);
				intere.setIdTercero(tercero.getIdAsInteger());

				if (intere.getRepresentante() != null &&
					(
						(
							intere.getRepresentante().getTipo().equals("P") && StringUtils.isNotEmpty(intere.getRepresentante().getNombre())
						) || (
							intere.getRepresentante().getTipo().equals("J") && StringUtils.isNotEmpty(intere.getRepresentante().getRazonSocial())
						)
					)
				){
					
					tercero = addTerceros(intere.getRepresentante(), useCaseConf);
					intere.getRepresentante().setIdTercero( tercero.getIdAsInteger());
					
				} else {
					intere.setRepresentante(null);
				}

				InteresadoVO interesado = populateInteresado(null, bookID.toString(), String.valueOf(registerID), intere, order);
				order++;
				
				LOG.trace("Guardando interesados");
				
				if (interesado.getId() != null) {
					interesadoManager.update(interesado);
				} else {
					interesadoManager.save(interesado);
				}
			}
		}
	}

	/**
	 * Añade un tercero como interesado y devuelve el bean con su información.
	 * 
	 * @param inter
	 *            el interesado
	 * @param useCaseConf
	 *            configuración
	 * @return una instancia de <code>TerceroValidadoVO</code>
	 */
	private TerceroValidadoVO addTerceros(Interesado inter, UseCaseConf useCaseConf) {
		// seteamos al thread local
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());

		TipoDocumentoIdentificativoTerceroVO tipoDocumento = new TipoDocumentoIdentificativoTerceroVO();
		tipoDocumento.setId(inter.getTipodoc());
		TerceroValidadoVO tercero = null;
		
		
		String idTercero = "0";
		String nombre = null;
		String apellido1 = null;
		String apellido2 = null;
		String razonSocial = null;
		
		if(null == inter.getIdTercero() || "0".equals(inter.getIdTercero().toString())){
		
			if (inter.getTipo().equals("P")) {
				List<TerceroValidadoFisicoVO> listaTerceros = terceroManager.findTerceroFisicoByDocumentNumber(inter.getDocIdentidad());
				
				if(null != listaTerceros && !listaTerceros.isEmpty()){
					tercero = listaTerceros.get(0);
				} else {
					
					tercero = new TerceroValidadoFisicoVO();
					
					if(null != inter.getNombre()){
						nombre = inter.getNombre().length() > 30 ? inter.getNombre().substring(0, 29) : inter.getNombre();
					}
					
					if(null != inter.getPapellido()){
						apellido1 = inter.getPapellido().length() > 30 ? inter.getPapellido().substring(0, 29) : inter.getPapellido();
					}
					
					if(null != inter.getSapellido()){
						apellido2 = inter.getSapellido().length() > 30 ? inter.getSapellido().substring(0, 29) : inter.getSapellido();
					}
					
					((TerceroValidadoFisicoVO)tercero).setNombre(nombre);
					((TerceroValidadoFisicoVO)tercero).setApellido1(apellido1);
					((TerceroValidadoFisicoVO)tercero).setApellido2(apellido2);
				}

			} else {
				List<TerceroValidadoJuridicoVO> listaTerceros = terceroManager.findTerceroJuridicoByDocumentNumber(inter.getDocIdentidad());
					
				if(null != listaTerceros && !listaTerceros.isEmpty()){
					tercero = listaTerceros.get(0);
					
				} else {
					tercero = new TerceroValidadoJuridicoVO();

					if(null != inter.getSapellido()){
						razonSocial = inter.getRazonSocial().length() > 30 ? inter.getRazonSocial().substring(0, 29) : inter.getRazonSocial();
					}
				}
				((TerceroValidadoJuridicoVO)tercero).setRazonSocial(razonSocial);
			}
			
			tercero.setId(idTercero);
			tercero.setNumeroDocumento(inter.getDocIdentidad());
			tercero.setTipoDocumento(tipoDocumento);
			
		} else {
			tercero = terceroManager.get(inter.getIdTercero().toString());
		}

		return tercero;
	}

	/**
	 * Añade un tercero como representante y devuelve el bean con su
	 * información.
	 * 
	 * @param repre
	 *            el representante
	 * @param useCaseConf
	 *            configuración
	 * @return una instancia de <code>TerceroValidadoVO</code>
	 */
	private TerceroValidadoVO addTerceros(Representante repre, UseCaseConf useCaseConf) {
		
		// seteamos al thread local
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());

		TipoDocumentoIdentificativoTerceroVO tipoDocumento = new TipoDocumentoIdentificativoTerceroVO();
		tipoDocumento.setId(repre.getTipodoc());
		TerceroValidadoVO tercero = null;
		
		if (repre.getTipo().equals("P")) {

			TerceroValidadoFisicoVO anEntity = new TerceroValidadoFisicoVO();
			anEntity.setId(repre.getIdTercero() == null ? null : String.valueOf(repre.getIdTercero()));
			anEntity.setNombre(repre.getNombre());
			anEntity.setApellido1(repre.getPapellido());
			anEntity.setApellido2(repre.getSapellido());
			anEntity.setNumeroDocumento(repre.getDocIdentidad());
			anEntity.setTipoDocumento(tipoDocumento);
			
			if (anEntity.getId() != null) {
				tercero = terceroManager.update(anEntity);
			} else {
				tercero = terceroManager.save(anEntity);
			}
			
		} else {
			
			if (repre.getTipo().equals("J")) {
				
				TerceroValidadoJuridicoVO anEntity = new TerceroValidadoJuridicoVO();
				anEntity.setId(repre.getIdTercero() == null ? null : String.valueOf(repre.getIdTercero()));
				anEntity.setRazonSocial(repre.getRazonSocial());
				anEntity.setNumeroDocumento(repre.getDocIdentidad());
				anEntity.setTipoDocumento(tipoDocumento);
				
				if (anEntity.getId() != null) {
					tercero = terceroManager.update(anEntity);
				} else {
					tercero = terceroManager.save(anEntity);
				}
			}
		}
		return tercero;
	}

	/**
	 * Devuelve una instancia de <code>InteresadoVO</code> con la información
	 * que recibe por parámetro.
	 * 
	 * @param id
	 *            Id del interesado
	 * @param idLibro
	 *            Id del libro
	 * @param idRegistro
	 *            Id del registro
	 * @param inter
	 *            El interesado
	 * @param orden
	 *            Orden de interesados
	 * 
	 * @return una instancia de <code>InteresadoVO</code>
	 */
	private InteresadoVO populateInteresado(String id, String idLibro, String idRegistro, Interesado inter, int orden) {
		
		InteresadoVO interesado = new InteresadoVO();
		String nombre = "";
		interesado.setId(id);
		interesado.setIdLibro(idLibro);
		interesado.setIdRegistro(idRegistro);
		
		if (inter.getTipo().equals("P")) {
			if (StringUtils.isNotEmpty(inter.getDocIdentidad())) {
				nombre = inter.getDocIdentidad() + " ";
			}
			
			if(!inter.getNombre().startsWith(nombre)){
				nombre += inter.getNombre();
			} else {
				nombre = inter.getNombre();
			}
			
			if (StringUtils.isNotEmpty(inter.getPapellido())) {
				nombre += " " + inter.getPapellido();
				
				if (StringUtils.isNotEmpty(inter.getSapellido())) {
					nombre += " " + inter.getSapellido();
				}
			}
		} else {
			nombre = inter.getRazonSocial();
		}
		
		interesado.setNombre(nombre.trim());
		interesado.setOrden(orden);
		
		// Tercero que actua como interesado
		BaseTerceroVO tercero = new BaseTerceroVO();
		tercero.setId(String.valueOf(inter.getIdTercero()));
		
		if(null != inter.getDireccionSeleccionada()){
			BaseDireccionVO direccionNotificacion = new BaseDireccionVO();
			direccionNotificacion.setId(String.valueOf(inter.getDireccionSeleccionada().getIdAsInteger()));
			direccionNotificacion.setDireccion(inter.getDireccionSeleccionada().getDireccion());
			interesado.setDireccionNotificacion(direccionNotificacion);
		}

		tercero.setNombre(nombre);
		interesado.setTercero(tercero);
		// Representante del interesado
		if (null != inter.getRepresentante() 
				&& !"N".equals(inter.getRepresentante().getTipo())
				&& (
					(
						inter.getRepresentante().getTipo().equals("P") && StringUtils.isNotEmpty(inter.getRepresentante().getNombre())
					) || (
						inter.getRepresentante().getTipo().equals("J") && StringUtils.isNotEmpty(inter.getRepresentante().getRazonSocial())
					)
				)
			){
			
			RepresentanteInteresadoVO representanteInteresado = new RepresentanteInteresadoVO();
			representanteInteresado.setInteresado(interesado);
			representanteInteresado.setId(inter.getRepresentante().getId() != null ? String.valueOf(inter.getRepresentante().getId()) : null);
			
			// Tercero que actua como representante
			BaseTerceroVO representanteTercero = new BaseTerceroVO();
			representanteTercero.setId(String.valueOf(inter.getRepresentante().getIdTercero()));

			if (inter.getRepresentante().getTipo().equals("P")) {
				
				nombre = inter.getRepresentante().getNombre();
				
				if (StringUtils.isNotEmpty(inter.getRepresentante().getPapellido())) {
					
					nombre += " " + inter.getRepresentante().getPapellido();
					
					if (StringUtils.isNotEmpty(inter.getRepresentante().getSapellido())) {
						nombre += " " + inter.getRepresentante().getSapellido();
					}
				}
			} else {
				nombre = inter.getRepresentante().getRazonSocial();
			}

			representanteTercero.setNombre(nombre);
			representanteInteresado.setRepresentante(representanteTercero);
			representanteInteresado.setNombre(nombre);
			
			if(null != inter.getRepresentante().getDireccionSeleccionada()){
				BaseDireccionVO direccionNotificacionRepresentante = new BaseDireccionVO();
				direccionNotificacionRepresentante.setId(String.valueOf(inter.getRepresentante().getDireccionSeleccionada().getIdAsInteger()));
				direccionNotificacionRepresentante.setDireccion(inter.getRepresentante().getDireccionSeleccionada().getDireccion());
				representanteInteresado.setDireccionNotificacion(direccionNotificacionRepresentante);
			}
			
			interesado.setRepresentante(representanteInteresado);
		}
		return interesado;
	}

	/**
	 * Devuelve los interesados del registro.
	 * 
	 * @param bookID
	 *            libro.
	 * @param registerID
	 *            id del registro.
	 * @param useCaseConf
	 *            configuración
	 * @return una lista de interesados.
	 */
	public List<Interesado> getAllInterested(Integer bookID, Integer registerID, UseCaseConf useCaseConf) {
		
		List<Interesado> listInteresados = new ArrayList<Interesado>();
		Interesado interesado = null;
		
		// seteamos al thread local
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());

		// 1. borro los interesados que hay
		List<InteresadoVO> listIntVO = interesadoManager.getAll( String.valueOf(bookID), String.valueOf(registerID));
		
		if (listIntVO != null && listIntVO.size() > 0) {
		
			listInteresados = new ArrayList<Interesado>();

			for (InteresadoVO intVO : listIntVO) {
				interesado = mappingInteresadoVOToInteresado(intVO);
				listInteresados.add(interesado);
			}
		}
		return listInteresados;
	}

	/**
	 * Mapea el objeto interesadoVO a Interado.
	 * 
	 * @param interesadoVO
	 *            objeto de SIGEM interesadoVO.
	 * 
	 * @return interesado.
	 */
	private Interesado mappingInteresadoVOToInteresado(InteresadoVO interesadoVO) {
		
		Interesado interesado = new Interesado();
		TerceroValidadoVO terceroVO = null;
		String id = interesadoVO.getTercero().getId();
		
		LOG.debug("ID a buscar: " + id);

		// MQE el campo id_persona toma el valor 0 si no es ni física ni
		// jurídica ni representante, o cuando es un interesado no validado.
		
		if (!"0".equals(id)) {
			terceroVO = terceroManager.get(id);
			
			if (terceroVO instanceof TerceroValidadoFisicoVO) {
				TerceroValidadoFisicoVO tercero = (TerceroValidadoFisicoVO) terceroVO;
				interesado.setTipo("P");
				
				if (tercero.getTipoDocumento() != null) {
					interesado.setTipodoc(tercero.getTipoDocumento().getId());
				}
				
				interesado.setDocIdentidad(tercero.getNumeroDocumento());
				interesado.setNombre(tercero.getNombre());
				interesado.setPapellido(tercero.getApellido1());
				interesado.setSapellido(tercero.getApellido2());
				
			} else {
				TerceroValidadoJuridicoVO tercero = (TerceroValidadoJuridicoVO) terceroVO;
				interesado.setTipo("J");
				
				if (tercero.getTipoDocumento() != null) {
					interesado.setTipodoc(tercero.getTipoDocumento().getId());
				}
				
				interesado.setDocIdentidad(tercero.getNumeroDocumento());
				interesado.setRazonSocial(tercero.getRazonSocial());
			}

			interesado.setDireccionesFisicas(terceroVO.getDireccionesFisicas());
			interesado.setDireccionFisicaPrincipal(terceroVO.getDireccionFisicaPrincipal());
			interesado.setDireccionesTelematicas(terceroVO.getDireccionesTelematicas());
			interesado.setDireccionTelematicaPrincipal(terceroVO.getDireccionTelematicaPrincipal());
			
			if(null != interesadoVO.getDireccionNotificacion() && DireccionType.FISICA.equals(interesadoVO.getDireccionNotificacion().getTipo())){
				DireccionManager direccionManager = ((TerceroManagerImpl)terceroManager).getDireccionManager();				
				DireccionFisicaVO direccionSeleccionadaAux = (DireccionFisicaVO) direccionManager.get(interesadoVO.getDireccionNotificacion().getId());

				interesado.setDireccionSeleccionada(direccionSeleccionadaAux );
				
			} else {
				if( null != terceroVO.getDireccionFisicaPrincipal()){
			    	DireccionFisicaVO direccionSeleccionadaAux = new DireccionFisicaVO();
			    	
			    	direccionSeleccionadaAux.setId(terceroVO.getDireccionFisicaPrincipal().getId());
			    	direccionSeleccionadaAux.setCiudad(terceroVO.getDireccionFisicaPrincipal().getCiudad());
			    	direccionSeleccionadaAux.setCodigoPostal(terceroVO.getDireccionFisicaPrincipal().getCodigoPostal());
			    	direccionSeleccionadaAux.setDireccion(terceroVO.getDireccionFisicaPrincipal().getDireccion());
			    	direccionSeleccionadaAux.setPais(terceroVO.getDireccionFisicaPrincipal().getPais());
			    	direccionSeleccionadaAux.setPrincipal(terceroVO.getDireccionFisicaPrincipal().isPrincipal());
			    	direccionSeleccionadaAux.setProvincia(terceroVO.getDireccionFisicaPrincipal().getProvincia());
			    	direccionSeleccionadaAux.setTercero(terceroVO.getDireccionFisicaPrincipal().getTercero());
			    	direccionSeleccionadaAux.setTipo(terceroVO.getDireccionFisicaPrincipal().getTipo());
			    	
					interesado.setDireccionSeleccionada(direccionSeleccionadaAux );
			    }
			}
			
		} else {
			
			interesado.setNombre(interesadoVO.getNombre());
		}

		interesado.setId(interesadoVO.getIdAsInteger());
		interesado.setIdTercero(interesadoVO.getTercero().getIdAsInteger());

		if (interesadoVO.getRepresentante() != null) {
			if (interesadoVO.getRepresentante().getRepresentante() != null) {

				// MQE el campo id_persona toma el valor 0 si no es ni física ni
				// jurídica ni representante, o cuando es un interesado no validado.
				
				if (!"0".equals(interesadoVO.getRepresentante().getRepresentante().getId())) {
					terceroVO = terceroManager.get(interesadoVO.getRepresentante().getRepresentante().getId());

					Representante repre = new Representante();
					
					if (terceroVO instanceof TerceroValidadoFisicoVO) {
						TerceroValidadoFisicoVO tercero = (TerceroValidadoFisicoVO) terceroVO;
						repre.setTipo("P");
						
						if (tercero.getTipoDocumento() != null) {
							repre.setTipodoc(tercero.getTipoDocumento().getId());
						}
						
						repre.setDocIdentidad(tercero.getNumeroDocumento());
						repre.setNombre(tercero.getNombre());
						repre.setPapellido(tercero.getApellido1());
						repre.setSapellido(tercero.getApellido2());
						
					} else {
						
						TerceroValidadoJuridicoVO tercero = (TerceroValidadoJuridicoVO) terceroVO;
						repre.setTipo("J");
						
						if (tercero.getTipoDocumento() != null) {
							repre.setTipodoc(tercero.getTipoDocumento().getId());
						}
						
						repre.setDocIdentidad(tercero.getNumeroDocumento());
						repre.setRazonSocial(tercero.getRazonSocial());

						repre.setDireccionesFisicas(tercero.getDireccionesFisicas());
						repre.setDireccionFisicaPrincipal(tercero.getDireccionFisicaPrincipal());
						repre.setDireccionesTelematicas(tercero.getDireccionesTelematicas());
						repre.setDireccionTelematicaPrincipal(tercero.getDireccionTelematicaPrincipal());
						
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
					    	
							repre.setDireccionSeleccionada(direccionSeleccionadaAux );
					    }
					}
					
					repre.setId(interesadoVO.getRepresentante().getIdAsInteger());
					repre.setIdTercero(interesadoVO.getRepresentante().getRepresentante().getIdAsInteger());

					repre.setDireccionesFisicas(terceroVO.getDireccionesFisicas());
					repre.setDireccionFisicaPrincipal(terceroVO.getDireccionFisicaPrincipal());
					repre.setDireccionesTelematicas(terceroVO.getDireccionesTelematicas());
					repre.setDireccionTelematicaPrincipal(terceroVO.getDireccionTelematicaPrincipal());
					
					if ( null != interesadoVO.getRepresentante().getDireccionNotificacion()){
						DireccionManager direccionManager = ((TerceroManagerImpl)terceroManager).getDireccionManager();				
						DireccionFisicaVO direccionSeleccionadaAux = (DireccionFisicaVO) direccionManager.get(interesadoVO.getRepresentante().getDireccionNotificacion().getId());
						repre.setDireccionSeleccionada(direccionSeleccionadaAux );
						
					} else if( null != terceroVO.getDireccionFisicaPrincipal()){
				    	DireccionFisicaVO direccionSeleccionadaAux = new DireccionFisicaVO();
				    	
				    	direccionSeleccionadaAux.setId(terceroVO.getDireccionFisicaPrincipal().getId());
				    	direccionSeleccionadaAux.setCiudad(terceroVO.getDireccionFisicaPrincipal().getCiudad());
				    	direccionSeleccionadaAux.setCodigoPostal(terceroVO.getDireccionFisicaPrincipal().getCodigoPostal());
				    	direccionSeleccionadaAux.setDireccion(terceroVO.getDireccionFisicaPrincipal().getDireccion());
				    	direccionSeleccionadaAux.setPais(terceroVO.getDireccionFisicaPrincipal().getPais());
				    	direccionSeleccionadaAux.setPrincipal(terceroVO.getDireccionFisicaPrincipal().isPrincipal());
				    	direccionSeleccionadaAux.setProvincia(terceroVO.getDireccionFisicaPrincipal().getProvincia());
				    	direccionSeleccionadaAux.setTercero(terceroVO.getDireccionFisicaPrincipal().getTercero());
				    	direccionSeleccionadaAux.setTipo(terceroVO.getDireccionFisicaPrincipal().getTipo());
				    	
						repre.setDireccionSeleccionada(direccionSeleccionadaAux );
				    }

					interesado.setRepresentante(repre);
				}
			}
		}

		return interesado;
	}

	/**
	 * Clase que obtiene los interesados y sus representantes, de un registro y
	 * los mapea a la lista de resultados.
	 * 
	 * @param registerId
	 *            Id del registro del que se quieren obtener los datos.
	 * @param bookID
	 *            libro.
	 * @param useCaseConf
	 *            configuración.
	 * @return senderField Lista de interesados y representantes, si los
	 *         hubiera.
	 */
	public String fillSenderFieldFromSenderListToRelationsReport( Integer registerId, Integer bookID, UseCaseConf useCaseConf) {
		
		LOG.trace("Entrando en ReportsBo.fillSenderFieldFromSenderListToRelationsReport()");
		String senderField = "";
		
		List<Interesado> interesados = getAllInterested(bookID, registerId, useCaseConf);
		
		if (interesados != null && interesados.size() > 0) {
			Iterator<Interesado> interesadosIterator = interesados.iterator();
			
			while (interesadosIterator.hasNext()) {
				
				Interesado interesadoToString = interesadosIterator.next();
				
				if ("P".equals(interesadoToString.getTipo())) {
					if (StringUtils.isNotEmpty(interesadoToString.getDocIdentidad())) {
						senderField += interesadoToString.getDocIdentidad() + " ";
					}
					if (StringUtils.isNotEmpty(interesadoToString.getNombre())) {
						senderField += interesadoToString.getNombre(); 
					}
					if (StringUtils.isNotEmpty(interesadoToString.getPapellido())) {
						senderField += " " + interesadoToString.getPapellido();
					}
					if (StringUtils.isNotEmpty(interesadoToString.getSapellido())) {
						senderField += " " + interesadoToString.getSapellido();
					}					
				} else {
					if (StringUtils.isNotEmpty(interesadoToString.getDocIdentidad())) {
						senderField += interesadoToString.getDocIdentidad() + " ";
					}
					if (StringUtils.isNotEmpty(interesadoToString.getRazonSocial())) {
						senderField += interesadoToString.getRazonSocial();
					}
				}
				if(null != interesadoToString.getDireccionSeleccionada()){
					senderField += "\n(";
					senderField += interesadoToString.getDireccionSeleccionada().toString();
					senderField += ")";
					
				} else  if(null != interesadoToString.getDireccionFisicaPrincipal()){
					senderField += "\n(";
					senderField += interesadoToString.getDireccionFisicaPrincipal().toString();
					senderField += ")";
				}
				
				if (interesadoToString.getRepresentante() != null && !"N".equals(interesadoToString.getRepresentante().getTipo())) {
					Representante senderAgent = interesadoToString.getRepresentante();
					
					if ("P".equals(senderAgent.getTipo())) {
						if (StringUtils.isNotEmpty(senderAgent.getNombre())) {
							senderField += "\n representado o firmado por ";
							
							if (StringUtils.isNotEmpty(senderAgent.getDocIdentidad())) {
								senderField += senderAgent.getDocIdentidad() + " ";
							}
							
							senderField += senderAgent.getNombre();
						}
						
						if (StringUtils.isNotEmpty(senderAgent.getPapellido())) {						
							senderField += " " + senderAgent.getPapellido();
						}
						
						if (StringUtils.isNotEmpty(senderAgent.getSapellido())) {
							senderField += " " + senderAgent.getSapellido();
						}
						if(null != senderAgent.getDireccionSeleccionada()){
							senderField += "\n(";
							senderField += senderAgent.getDireccionSeleccionada().toString();
							senderField += ")";
							
						} else if(null != senderAgent.getDireccionFisicaPrincipal()){
							senderField += "\n(";
							senderField += senderAgent.getDireccionFisicaPrincipal().toString();
							senderField += ")";
						}
						
					} else {
						if ("J".equals(senderAgent.getTipo())) {
							if (StringUtils.isNotEmpty(senderAgent.getRazonSocial())) {
								
								senderField += "\n representado o firmado por ";
								
								if (StringUtils.isNotEmpty(senderAgent.getDocIdentidad())) {
									senderField += senderAgent.getDocIdentidad() + " ";
								}
								
								senderField += senderAgent.getRazonSocial();
							}
							if(null != senderAgent.getDireccionSeleccionada()){
								senderField += "\n(";
								senderField += senderAgent.getDireccionSeleccionada().toString();
								senderField += ")";
							} else if(null != senderAgent.getDireccionFisicaPrincipal()){
								senderField += "\n(";
								senderField += senderAgent.getDireccionFisicaPrincipal().toString();
								senderField += ")";
							}
						}
					}
				}
				senderField += "\n";

			}
		}
		
		LOG.info("Lista de interesados para el registro de id. " + registerId + ": " + senderField);
		
		return senderField;
	}

}