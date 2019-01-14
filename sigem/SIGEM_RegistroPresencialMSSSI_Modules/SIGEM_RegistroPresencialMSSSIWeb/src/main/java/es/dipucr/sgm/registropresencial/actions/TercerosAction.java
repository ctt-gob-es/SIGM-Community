package es.dipucr.sgm.registropresencial.actions;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIInput;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;
import com.ieci.tecdoc.isicres.context.ISicresBeansProvider;

import es.dipucr.sgm.registropresencial.beans.TercerosBean;
import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.terceros.business.manager.MasterValuesManager;
import es.ieci.tecdoc.isicres.terceros.business.manager.TerceroManager;
import es.ieci.tecdoc.isicres.terceros.business.manager.impl.MasterValuesManagerImpl;
import es.ieci.tecdoc.isicres.terceros.business.vo.BaseDireccionVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.CiudadVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.DireccionFisicaVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.DireccionTelematicaVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.PaisVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.ProvinciaVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoFisicoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoJuridicoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TipoDireccionTelematicaVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TipoDocumentoIdentificativoTerceroVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.search.SearchType;
import es.msssi.sgm.registropresencial.actions.GenericActions;
import es.msssi.sgm.registropresencial.actions.InputRegisterAction;
import es.msssi.sgm.registropresencial.actions.OutputRegisterAction;
import es.msssi.sgm.registropresencial.beans.BusquedaInteresado;
import es.msssi.sgm.registropresencial.beans.BusquedaRepresentante;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.Representante;
import es.msssi.sgm.registropresencial.provider.ISicresBeanTercerosProvider;

public class TercerosAction extends GenericActions{

	private static final long serialVersionUID = 1L;
	
	private TercerosBean tercerosBean = new TercerosBean();
	
	private GenericActions registroOriginal = null;

	private Representante representanteBusqueda = null;
	private List<Representante> representantesBusqueda = null;
	private BusquedaRepresentante busquedaRepresentante = null;
	
	private Interesado interesadoBusqueda = new Interesado();
    private List<Interesado> interesadosBusqueda = new ArrayList<Interesado>();
    private BusquedaInteresado busquedaInteresado = new BusquedaInteresado();
    
    Interesado interesadoOriginal = null;
    
	private List<TipoDocumentoIdentificativoTerceroVO> listaTiposDocumentosIdentificativos = new ArrayList<TipoDocumentoIdentificativoTerceroVO>();	
    
    
    public TercerosAction() throws SessionException, ValidationException {
		init();
	}


    /**
     * Obtiene el valor del parámetro interesado.
     * 
     * @return interesado valor del campo a obtener.
     */
    public Interesado getInteresadoBusqueda() {
    	return interesadoBusqueda;
    }

    /**
     * Guarda el valor del parámetro interesado.
     * 
     * @param interesado
     *            valor del campo a guardar.
     */
    public void setInteresadoBusqueda(Interesado interesadoBusqueda) {
    	this.interesadoBusqueda = interesadoBusqueda;
    }

    /**
     * Obtiene el valor del parámetro interesados.
     * 
     * @return interesados valor del campo a obtener.
     */
    public List<Interesado> getInteresadosBusqueda() {
    	return interesadosBusqueda;
    }

    /**
     * Guarda el valor del parámetro interesados.
     * 
     * @param interesados
     *            valor del campo a guardar.
     */
    public void setInteresadosBusqueda(List<Interesado> interesadosBusqueda) {
    	this.interesadosBusqueda = interesadosBusqueda;
    }
    
    /**
     * Obtiene el valor del parámetro BusquedaInteresado.
     * 
     * @return busquedaInteresado valor del campo a obtener.
     */
    public BusquedaInteresado getBusquedaInteresado() {
		return busquedaInteresado;
	}
    
    /**
     * Guarda el valor del parámetro busquedaInteresado.
     * 
     * @param busquedaInteresado
     *            valor del campo a guardar.
     */
	public void setBusquedaInteresado(BusquedaInteresado busquedaInteresado) {
		this.busquedaInteresado = busquedaInteresado;
	}
	
	/**
     * Obtiene el valor del parámetro busquedaRepresentante.
     * 
     * @return busquedaRepresentante valor del campo a obtener.
     */
	public BusquedaRepresentante getBusquedaRepresentante() {
		return busquedaRepresentante;
	}

	/**
     * Guarda el valor del parámetro busquedaRepresentante.
     * 
     * @param busquedaRepresentante
     *            valor del campo a guardar.
     */
	public void setBusquedaRepresentante( BusquedaRepresentante busquedaRepresentante) {
		this.busquedaRepresentante = busquedaRepresentante;
	}

	/**
     * Obtiene el valor del parámetro representanteBusqueda.
     * 
     * @return representanteBusqueda valor del campo a obtener.
     */
	public Representante getRepresentanteBusqueda() {
		return representanteBusqueda;
	}

	/**
     * Guarda el valor del parámetro representanteBusqueda.
     * 
     * @param representanteBusqueda
     *            valor del campo a guardar.
     */
	public void setRepresentanteBusqueda(Representante representanteBusqueda) {
		this.representanteBusqueda = representanteBusqueda;
	}

	/**
     * Obtiene el valor del parámetro representantesBusqueda.
     * 
     * @return representantesBusqueda valor del campo a obtener.
     */
	public List<Representante> getRepresentantesBusqueda() {
		return representantesBusqueda;
	}

	/**
     * Guarda el valor del parámetro representantesBusqueda.
     * 
     * @param representantesBusqueda
     *            valor del campo a guardar.
     */
	public void setRepresentantesBusqueda( List<Representante> representantesBusqueda) {
		this.representantesBusqueda = representantesBusqueda;
	}

	public List<ProvinciaVO> getProvincias() {
		if (null == tercerosBean.getProvincias()) {
			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			MasterValuesManager masterValuesManager = (MasterValuesManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("masterValuesManager");
			tercerosBean.setProvincias(masterValuesManager.getProvincias());
		}
		return tercerosBean.getProvincias();
	}

	public void setProvincias(List<ProvinciaVO> provincias) {
		tercerosBean.setProvincias(provincias);
	}

	public ProvinciaVO getProvinciaDefecto() {
		if (null == tercerosBean.getProvinciaDefecto()) {
			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			MasterValuesManager masterValuesManager = (MasterValuesManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("masterValuesManager");
			tercerosBean.setProvinciaDefecto(masterValuesManager.getProvinciaByNombre(Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_PROVINCIA_POR_DEFECTO)));
		}		
		
		return tercerosBean.getProvinciaDefecto();
	}

	public void setProvinciaDefecto(ProvinciaVO provinciaDefecto) {
		tercerosBean.setProvinciaDefecto(provinciaDefecto);
	}

	public DireccionFisicaVO getNuevaDireccionFisica() {
		if (null == tercerosBean.getNuevaDireccionFisica()) {
			tercerosBean.setNuevaDireccionFisica(new DireccionFisicaVO());
			tercerosBean.getNuevaDireccionFisica().setPais(getPaisDefecto());
			tercerosBean.getNuevaDireccionFisica().setProvincia(getProvinciaDefecto());
			tercerosBean.getNuevaDireccionFisica().setPrincipal(false);
		}
		return tercerosBean.getNuevaDireccionFisica();
	}

	public void setNuevaDireccionFisica(DireccionFisicaVO nuevaDireccionFisica) {
		tercerosBean.setNuevaDireccionFisica(nuevaDireccionFisica);
	}

	public DireccionFisicaVO getDireccionFisicaBorrar() {
		return tercerosBean.getDireccionFisicaBorrar();
	}

	public void setDireccionFisicaBorrar(DireccionFisicaVO direccionFisicaBorrar) {
		tercerosBean.setDireccionFisicaBorrar(direccionFisicaBorrar);
	}

	public DireccionTelematicaVO getNuevaDireccionTelematica() {
		if (null == tercerosBean.getNuevaDireccionTelematica()) {
			tercerosBean.setNuevaDireccionTelematica(new DireccionTelematicaVO());
			tercerosBean.getNuevaDireccionTelematica().setTipoDireccionTelematica(tercerosBean.getTipoDireccionTelematicaDefecto());
			tercerosBean.getNuevaDireccionTelematica().setPrincipal(false);
		}
		return tercerosBean.getNuevaDireccionTelematica();
	}

	public void setNuevaDireccionTelematica( DireccionTelematicaVO nuevaDireccionTelematica) {
		tercerosBean.setNuevaDireccionTelematica(nuevaDireccionTelematica);
	}

	public void actualizaCiudades(ValueChangeEvent event) {
		if (null != event.getNewValue() && !event.getNewValue().equals(event.getOldValue())) {

			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			MasterValuesManager masterValuesManager = (MasterValuesManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("masterValuesManager");

			// Se instancia un nuevo objeto ya que si no pasa la provincia por
			// referencia y se va liando todo el rato.
			ProvinciaVO provinciaAux = masterValuesManager.getProvinciaByNombre((String) event.getNewValue());
			ProvinciaVO provincia = new ProvinciaVO();
			provincia.setCodigo(provinciaAux.getCodigo());
			provincia.setNombre(provinciaAux.getNombre());
			provincia.setCiudades(provinciaAux.getCiudades());

			CiudadVO ciudadBlanco = new CiudadVO();
			ciudadBlanco.setNombre("Seleccione un valor");

			DireccionFisicaVO direccion = (DireccionFisicaVO) ((UIInput) event.getSource()).getAttributes().get("direccion");
			direccion.setCiudad(ciudadBlanco);
			direccion.setProvincia(provincia);
		}
	}

	public void actualizaTipoDireccionTelematica(ValueChangeEvent event) {
		if (null != event.getNewValue() && !event.getNewValue().equals(event.getOldValue())) {
			String tipoDireccion = (String) event.getNewValue();
			for (TipoDireccionTelematicaVO tipoDireccionTelematica : getTiposDireccionTelematica()) {
				if (tipoDireccionTelematica.getDescripcion().equals( tipoDireccion)) {
					DireccionTelematicaVO direccion = (DireccionTelematicaVO) ((UIInput) event.getSource()).getAttributes().get("direccion");
					direccion.getTipoDireccionTelematica().setId( tipoDireccionTelematica.getId());
					direccion.getTipoDireccionTelematica().setCodigo( tipoDireccionTelematica.getCodigo());
				}
			}
		}
	}
	
	/**
	 * Crea una nueva dirección fisica
	 */
	public void nuevaDirFisica() {
		tercerosBean.setNuevaDireccionFisica(new DireccionFisicaVO());
		tercerosBean.getNuevaDireccionFisica().setPais(getPaisDefecto());
		tercerosBean.getNuevaDireccionFisica().setProvincia(getProvinciaDefecto());
		tercerosBean.getNuevaDireccionFisica().setPrincipal(false);
	}

	/**
	 * Crea una nueva dirección fisica
	 */
	public void nuevaDirTelematica() {
		tercerosBean.setNuevaDireccionTelematica(new DireccionTelematicaVO());
		tercerosBean.getNuevaDireccionTelematica().setTipoDireccionTelematica(getTipoDireccionTelematicaDefecto());
		tercerosBean.getNuevaDireccionTelematica().setPrincipal(false);
	}

	public void deleteDireccionFisica() {
		tercerosBean.getInteresadoDeTrabajo().getDireccionesFisicas().remove(getDireccionFisicaBorrar());
	}

	public void deleteDireccionTelematica() {
		tercerosBean.getInteresadoDeTrabajo().getDireccionesTelematicas().remove(getDireccionTelematicaBorrar());
	}

	public List<PaisVO> getPaises() {
		if (null == tercerosBean.getPaises()) {
			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			MasterValuesManager masterValuesManager = (MasterValuesManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("masterValuesManager");
			tercerosBean.setPaises(masterValuesManager.getPaises());
		}
		return tercerosBean.getPaises();
	}

	public void setPaises(List<PaisVO> paises) {
		tercerosBean.setPaises(paises);
	}

	public PaisVO getPaisDefecto() {
		if (null == tercerosBean.getPaisDefecto()) {
			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			MasterValuesManager masterValuesManager = (MasterValuesManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("masterValuesManager");
			tercerosBean.setPaisDefecto(masterValuesManager.getPais(Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_PAIS_POR_DEFECTO)));
		}

		return tercerosBean.getPaisDefecto();
	}

	public void setPaisDefecto(PaisVO paisDefecto) {
		tercerosBean.setPaisDefecto(paisDefecto);
	}

	public List<TipoDireccionTelematicaVO> getTiposDireccionTelematica() {
		if (null == tercerosBean.getTiposDireccionTelematica()) {
			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			MasterValuesManager masterValuesManager = (MasterValuesManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("masterValuesManager");
			tercerosBean.setTiposDireccionTelematica(masterValuesManager.getTiposDireccionesTelematicas());
		}
		return tercerosBean.getTiposDireccionTelematica();
	}

	public void setTiposDireccionTelematica( List<TipoDireccionTelematicaVO> tiposDireccionTelematica) {
		tercerosBean.setTiposDireccionTelematica(tiposDireccionTelematica);
	}

	public void setTipoDireccionTelematicaDefecto(TipoDireccionTelematicaVO tipoDireccionTelematicaDefecto) {
		tercerosBean.setTipoDireccionTelematicaDefecto(tipoDireccionTelematicaDefecto);
	}

	public DireccionTelematicaVO getDireccionTelematicaBorrar() {
		return tercerosBean.getDireccionTelematicaBorrar();
	}

	public void setDireccionTelematicaBorrar(DireccionTelematicaVO direccionTelematicaBorrar) {
		tercerosBean.setDireccionTelematicaBorrar(direccionTelematicaBorrar);
	}
	
	public TipoDireccionTelematicaVO getTipoDireccionTelematicaDefecto() {
		final String KEY_DESKTOP_PAIS_POR_DEFECTO = "CE";

		if (null == tercerosBean.getTipoDireccionTelematicaDefecto()) {
			for (TipoDireccionTelematicaVO tipoDireccionTelematica : getTiposDireccionTelematica()) {
				if (tipoDireccionTelematica.getCodigo().equals( KEY_DESKTOP_PAIS_POR_DEFECTO)) {
					tercerosBean.setTipoDireccionTelematicaDefecto(tipoDireccionTelematica);
				}
			}
		}

		return tercerosBean.getTipoDireccionTelematicaDefecto();
	}
	
	/**
	 * guarda en sesion el valor por defecto del tipo de interesado.
	 */
	public void buscarInteresados(ActionEvent event) {	
		registroOriginal = (GenericActions) event.getComponent().getAttributes().get("registroOriginal");
		
		if (null != getBusquedaInteresado()) {
			TerceroManager terceroManager = ISicresBeanTercerosProvider.getInstance().getTerceroManager();
			setInteresadosBusqueda(getBusquedaInteresado().buscarInteresado(terceroManager, useCaseConf.getEntidadId()));
		}
	}
	
	/**
	 * guarda en sesion el valor por defecto del tipo de interesado.
	 */
	public void buscarInteresados() {		
		if (null != getBusquedaInteresado()) {
			TerceroManager terceroManager = ISicresBeanTercerosProvider.getInstance().getTerceroManager();
			setInteresadosBusqueda(getBusquedaInteresado().buscarInteresado(terceroManager, useCaseConf.getEntidadId()));
		}
	}

	/**
	 * guarda en sesion el valor por defecto del tipo de interesado.
	 */
	public void buscarRepresentante() {
		if (null != getBusquedaRepresentante()) {
			TerceroManager terceroManager = ISicresBeanTercerosProvider.getInstance().getTerceroManager();
			setRepresentantesBusqueda(getBusquedaRepresentante().buscarRepresentante(terceroManager, useCaseConf.getEntidadId()));
		}
	}
	
	/**
	 * guarda en sesion el valor por defecto del tipo de interesado.
	 */
	public void saveInterestedTypeDefault() {
		
		if (tercerosBean.getInteresadoDeTrabajo() != null) {
			useCaseConf.setInterestedType(tercerosBean.getInteresadoDeTrabajo().getTipo());
			saveUseCaseConf(useCaseConf);
		}
		if(null != registroOriginal){
			List<Interesado> listaInteresados = null;
			if(registroOriginal instanceof InputRegisterAction){
				listaInteresados = ((InputRegisterAction) registroOriginal).getInputRegisterBean().getInteresados();
			} else if(registroOriginal instanceof OutputRegisterAction){
				listaInteresados = ((OutputRegisterAction) registroOriginal).getOutputRegisterBean().getInteresados();
			}
			if(!listaInteresados.contains(tercerosBean.getInteresadoDeTrabajo())){
				listaInteresados.add(tercerosBean.getInteresadoDeTrabajo());
			}
		}
	}

	public void guardaNuevaDireccionPostal() {
		tercerosBean.getInteresadoDeTrabajo().getDireccionesFisicas().add(getNuevaDireccionFisica());
		nuevaDirFisica();
	}

	public void guardaNuevaDireccionTelematica() {
		tercerosBean.getInteresadoDeTrabajo().getDireccionesTelematicas().add(getNuevaDireccionTelematica());
		nuevaDirTelematica();
	}
	
	public void saveTercero() {
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
		Interesado interesado = tercerosBean.getInteresadoDeTrabajo();
		TerceroManager terceroManager = ISicresBeanTercerosProvider.getInstance().getTerceroManager();
		setInteresadosBusqueda(getBusquedaInteresado().buscarInteresado(terceroManager, useCaseConf.getEntidadId()));

		TipoDocumentoIdentificativoTerceroVO tipoDocumento = new TipoDocumentoIdentificativoTerceroVO();
		tipoDocumento.setId(interesado.getTipodoc());

		List<DireccionFisicaVO> direccionesFisicas = interesado.getDireccionesFisicas();
		List<DireccionTelematicaVO> direccionesTelematicas = interesado.getDireccionesTelematicas();
		List<BaseDireccionVO> direcciones = new ArrayList<BaseDireccionVO>();
		
		for (DireccionFisicaVO direccionFisica : direccionesFisicas) {
			if (direccionFisica.isPrincipal()) {
				interesado.setDireccionFisicaPrincipal(direccionFisica);
				direcciones.add(direccionFisica);
				break;
			}
		}
		for (DireccionFisicaVO direccionFisica : direccionesFisicas) {
			if (!direccionFisica.isPrincipal()) {
				direcciones.add(direccionFisica);
			}
		}
		for (DireccionTelematicaVO direccionTelematica : direccionesTelematicas) {
			if (direccionTelematica.isPrincipal()) {
				interesado.setDireccionTelematicaPrincipal(direccionTelematica);
				direcciones.add(direccionTelematica);
				break;
			}
		}
		for (DireccionTelematicaVO direccionTelematica : direccionesTelematicas) {
			if (!direccionTelematica.isPrincipal()) {
				direcciones.add(direccionTelematica);
			}
		}

		TerceroValidadoVO anEntity = null;
		if (interesado.getTipo().equals("P")) {
			anEntity = new TerceroValidadoFisicoVO();
			anEntity.setNombre(interesado.getNombre());
			((TerceroValidadoFisicoVO) anEntity).setApellido1(interesado.getPapellido());
			((TerceroValidadoFisicoVO) anEntity).setApellido2(interesado.getSapellido());
		} else if (interesado.getTipo().equals("J")) {
			anEntity = new TerceroValidadoJuridicoVO();
			((TerceroValidadoJuridicoVO) anEntity).setRazonSocial(interesado.getRazonSocial());
		}
		
		if (null != anEntity) {
			anEntity.setId(interesado.getIdTercero() == null ? null : String.valueOf(interesado.getIdTercero()));
			
			String docIdentidad = interesado.getDocIdentidad();
			if(StringUtils.isNotEmpty(docIdentidad)){
				docIdentidad = docIdentidad.toUpperCase();				
			}			
			anEntity.setNumeroDocumento(docIdentidad);
			
			anEntity.setTipoDocumento(tipoDocumento);
			anEntity.setDirecciones(direcciones);

			if (anEntity.getId() != null) {
				terceroManager.updateAll(anEntity);
			} else {
				terceroManager.save(anEntity);
			}

			buscarInteresados();
			buscarRepresentante();
		}
	}

	public void saveTerceroRepresentante() {
		saveTercero();
		setInteresadoAsRepresentante();	
	}
	
	public void setInteresadoAsRepresentante() {
		
		Interesado interesado = tercerosBean.getInteresadoDeTrabajo();
		
		Representante representante = new Representante();

		representante.setTipodoc(interesado.getTipodoc());
		representante.setDocIdentidad(interesado.getDocIdentidad());
		representante.setRazonSocial(interesado.getRazonSocial());
		representante.setNombre(interesado.getNombre());
		representante.setPapellido(interesado.getPapellido());
		representante.setSapellido(interesado.getSapellido());
		representante.setTipo(interesado.getTipo());
		representante.setId(interesado.getId());
		representante.setIdTercero(interesado.getIdTercero());

		representante.setDireccionesFisicas(interesado.getDireccionesFisicas());
		for (DireccionFisicaVO direccionFisica : interesado.getDireccionesFisicas()) {
			if (direccionFisica.isPrincipal()) {
				representante.setDireccionFisicaPrincipal(direccionFisica);
				break;
			}
		}

		representante.setDireccionesTelematicas(interesado.getDireccionesTelematicas());
		for (DireccionTelematicaVO direccionTelematica : interesado.getDireccionesTelematicas()) {
			if (direccionTelematica.isPrincipal()) {
				representante.setDireccionTelematicaPrincipal(direccionTelematica);
				break;
			}
		}

		tercerosBean.setInteresadoDeTrabajo(interesadoOriginal);
		tercerosBean.getInteresadoDeTrabajo().setRepresentante(representante);		
	}
	
	public void setRepresentanteAsInteresado() {
		interesadoOriginal = tercerosBean.getInteresadoDeTrabajo();
		
		Representante representante = tercerosBean.getInteresadoDeTrabajo().getRepresentante();
		Interesado interesado = new Interesado();

		interesado.setTipodoc(representante.getTipodoc());
		interesado.setDocIdentidad(representante.getDocIdentidad());
		interesado.setRazonSocial(representante.getRazonSocial());
		interesado.setNombre(representante.getNombre());
		interesado.setPapellido(representante.getPapellido());
		interesado.setSapellido(representante.getSapellido());
		interesado.setTipo(representante.getTipo());
		interesado.setId(representante.getId());
		interesado.setIdTercero(representante.getIdTercero());
		
		interesado.setDireccionesFisicas(representante.getDireccionesFisicas());
		for (DireccionFisicaVO direccionFisica : representante.getDireccionesFisicas()) {
			if (direccionFisica.isPrincipal()) {
				interesado.setDireccionFisicaPrincipal(direccionFisica);
				break;
			}
		}
		
		interesado.setDireccionesTelematicas(representante.getDireccionesTelematicas());
		
		for (DireccionTelematicaVO direccionTelematica : representante.getDireccionesTelematicas()) {
			if (direccionTelematica.isPrincipal()) {
				interesado.setDireccionTelematicaPrincipal(direccionTelematica);
				break;
			}
		}

		tercerosBean.setInteresadoDeTrabajo(interesado);
	}	

	public void nuevoInteresado() {
		nuevoInteresado(getBusquedaInteresado().getTipo());
	}

	public void nuevoInteresadoRepresentante() {
		nuevoInteresado(getBusquedaRepresentante().getTipo());
	}

	public void nuevoInteresado(String tipo) {
		tercerosBean.setInteresadoDeTrabajo(new Interesado());
		tercerosBean.getInteresadoDeTrabajo().setTipo(tipo);
		tercerosBean.getInteresadoDeTrabajo().setDireccionesFisicas(new ArrayList<DireccionFisicaVO>());
		tercerosBean.getInteresadoDeTrabajo().setDireccionesTelematicas(new ArrayList<DireccionTelematicaVO>());
		
		if (tercerosBean.getInteresadoDeTrabajo() != null) {
			useCaseConf.setInterestedType(tercerosBean.getInteresadoDeTrabajo().getTipo());
			saveUseCaseConf(useCaseConf);
		}
		
	}
	
	public TercerosBean getTercerosBean() {
		return tercerosBean;
	}

	public void setTercerosBean(TercerosBean tercerosBean) {
		this.tercerosBean = tercerosBean;
	}

	public GenericActions getRegistroOriginal() {
		return registroOriginal;
	}

	public void setRegistroOriginal(GenericActions registroOriginal) {
		this.registroOriginal = registroOriginal;
	}
	
	/**
	 * Borra los datos de la búsqueda y crea uno vacío
	 */
	public void reinitBusqueda() {
		BusquedaInteresado interesadoBusqueda = new BusquedaInteresado();
		Interesado interesado = new Interesado();
		interesado.setTipo(useCaseConf.getInterestedType());

		setBusquedaInteresado(interesadoBusqueda);
		setInteresadoBusqueda(interesado);
		setInteresadosBusqueda(new ArrayList<Interesado>());

		BusquedaRepresentante busquedaRepresentante = new BusquedaRepresentante();
		Representante representante = new Representante();
		representante.setTipo(useCaseConf.getInterestedType());

		setBusquedaRepresentante(busquedaRepresentante);
		setRepresentanteBusqueda(representante);
		setRepresentantesBusqueda(new ArrayList<Representante>());
	}

	public void reinitRepresentante() {
		BusquedaRepresentante busquedaRepresentante = new BusquedaRepresentante();
		Representante representante = new Representante();
		representante.setTipo(useCaseConf.getInterestedType());

		setBusquedaRepresentante(busquedaRepresentante);
		setRepresentanteBusqueda(representante);
		setRepresentantesBusqueda(new ArrayList<Representante>());
	}
	
	public void eliminarRepresentante() {
		tercerosBean.getInteresadoDeTrabajo().setRepresentante(new Representante());
	}
	
	public List<TipoDocumentoIdentificativoTerceroVO> getListaTiposDocumentosIdentificativos() {
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
		
		if(null != tercerosBean.getInteresadoDeTrabajo() && tercerosBean.getInteresadoDeTrabajo().getTipo().equals(Interesado.TIPO_PERSONA_JURIDICA)){
			listaTiposDocumentosIdentificativos = getTiposDocumentosIdentificativos(SearchType.JURIDICO);
		}
		else{
			listaTiposDocumentosIdentificativos = getTiposDocumentosIdentificativos(SearchType.FISICO);
		}
		
		return listaTiposDocumentosIdentificativos;
	}
	
	public void setListaTiposDocumentosIdentificativos(List<TipoDocumentoIdentificativoTerceroVO> listaTiposDocumentosIdentificativos) {
		this.listaTiposDocumentosIdentificativos = listaTiposDocumentosIdentificativos;
	}
	
	private List<TipoDocumentoIdentificativoTerceroVO> getTiposDocumentosIdentificativos(SearchType tipo) {
		MasterValuesManager masterValuesManager = (MasterValuesManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("masterValuesManager");
		List<TipoDocumentoIdentificativoTerceroVO> tiposDocumentosIdentificativos = masterValuesManager.getTiposDocumentoIdentificativo(tipo);
		return tiposDocumentosIdentificativos;
	}
}
