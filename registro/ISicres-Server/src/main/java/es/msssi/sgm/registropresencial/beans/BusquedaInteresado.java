package es.msssi.sgm.registropresencial.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;

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

public class BusquedaInteresado implements Serializable {
	private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BusquedaInteresado.class.getName());
		
	private String tipo = "P";
	
	private String tipoBusqueda1;
	private String tipoBusqueda2;
	private String tipoBusqueda3;
	private String tipoBusqueda4;
	private String tipoBusqueda5;
	
	private String tipodoc;
	private String docIdentidad;
	private String razonSocial;  
	private String nombre;
	private String papellido;
	private String sapellido;
	
	public BusquedaInteresado(){
		super();
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public String getTipodoc() {
		return tipodoc;
	}
	
	public void setTipodoc(String tipodoc) {
		this.tipodoc = tipodoc;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipoBusqueda1() {
		return tipoBusqueda1;
	}

	public void setTipoBusqueda1(String tipoBusqueda1) {
		this.tipoBusqueda1 = tipoBusqueda1;
	}

	public String getTipoBusqueda2() {
		return tipoBusqueda2;
	}

	public void setTipoBusqueda2(String tipoBusqueda2) {
		this.tipoBusqueda2 = tipoBusqueda2;
	}

	public String getTipoBusqueda3() {
		return tipoBusqueda3;
	}

	public void setTipoBusqueda3(String tipoBusqueda3) {
		this.tipoBusqueda3 = tipoBusqueda3;
	}

	public String getTipoBusqueda4() {
		return tipoBusqueda4;
	}

	public void setTipoBusqueda4(String tipoBusqueda4) {
		this.tipoBusqueda4 = tipoBusqueda4;
	}
	
	public String getTipoBusqueda5() {
		return tipoBusqueda5;
	}

	public void setTipoBusqueda5(String tipoBusqueda5) {
		this.tipoBusqueda5 = tipoBusqueda5;
	}
	
	public String getDocIdentidad() {
		return docIdentidad;
	}
	
	public void setDocIdentidad(String docIdentidad) {
		this.docIdentidad = docIdentidad;
	}
	
	public String getRazonSocial() {
		return razonSocial;
	}
	
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getPapellido() {
		return papellido;
	}
	
	public void setPapellido(String papellido) {
		this.papellido = papellido;
	}
	
	public String getSapellido() {
		return sapellido;
	}
	
	public void setSapellido(String sapellido) {
		this.sapellido = sapellido;
	}
	
	/**
	 * 		<f:selectItem itemLabel="Empieza por" itemValue="1" />
	 * 		<f:selectItem itemLabel="Es Igual a" itemValue="2" />
	 * 		<f:selectItem itemLabel="Contiene" itemValue="3" />
	 * @param tipoBusqueda
	 * @return
	 */
	public OperatorEnum getOperador(String tipoBusqueda){
		
		if(tipoBusqueda.equalsIgnoreCase("1")){
			return OperatorEnum.COMIENZA_POR;
		}
		else if(tipoBusqueda.equalsIgnoreCase("2")){
			return OperatorEnum.ES_IGUAL;
		}
		else if(tipoBusqueda.equalsIgnoreCase("3")){
			return OperatorEnum.CONTIENE;
		}
			
		return null;
	}
	
	public CriteriaVO criteriaFisico(){
		CriteriaVO criterioInterFisico = new CriteriaVO();
		criterioInterFisico.setType(SearchType.FISICO);

		List<FilterVO> filtersFisico = new ArrayList<FilterVO>();
		
		if(null != docIdentidad && !docIdentidad.isEmpty()){
			FilterVO terceroFisicoNIF = new FilterVO();
			terceroFisicoNIF.setField("UPPER(NIF)");
			terceroFisicoNIF.setOperator(getOperador(tipoBusqueda1));
			terceroFisicoNIF.setValue(docIdentidad.toUpperCase());
			filtersFisico.add(terceroFisicoNIF);
		}
		
		if(null != nombre && !nombre.isEmpty()){
			FilterVO terceroFisicoNIF = new FilterVO();
			terceroFisicoNIF.setField("UPPER(SURNAME)");
			terceroFisicoNIF.setOperator(getOperador(tipoBusqueda2));
			terceroFisicoNIF.setValue(nombre.toUpperCase());
			filtersFisico.add(terceroFisicoNIF);
		}
		
		if(null != papellido && !papellido.isEmpty()){
			FilterVO terceroFisicoNIF = new FilterVO();
			terceroFisicoNIF.setField("UPPER(FIRST_NAME)");
			terceroFisicoNIF.setOperator(getOperador(tipoBusqueda3));
			terceroFisicoNIF.setValue(papellido.toUpperCase());
			filtersFisico.add(terceroFisicoNIF);
		}
		
		if(null != sapellido && !sapellido.isEmpty()){
			FilterVO terceroFisicoNIF = new FilterVO();
			terceroFisicoNIF.setField("UPPER(SECOND_NAME)");
			terceroFisicoNIF.setOperator(getOperador(tipoBusqueda4));
			terceroFisicoNIF.setValue(sapellido.toUpperCase());
			filtersFisico.add(terceroFisicoNIF);
		}
		
		if(filtersFisico.size() > 0){
			criterioInterFisico.setFilters(filtersFisico);		
		}
		else{
			criterioInterFisico = null;
		}
		return criterioInterFisico;
	}
	
	public CriteriaVO criteriaJuridico(){
		CriteriaVO criterioInterJuridico = new CriteriaVO();
		criterioInterJuridico.setType(SearchType.JURIDICO);

		List<FilterVO> filtersJuridico = new ArrayList<FilterVO>();
		
		if(null != docIdentidad && !docIdentidad.isEmpty()){
			FilterVO terceroJuridicoNIF = new FilterVO();
			terceroJuridicoNIF.setField("UPPER(CIF)");
			terceroJuridicoNIF.setOperator(getOperador(tipoBusqueda1));
			terceroJuridicoNIF.setValue(docIdentidad.toUpperCase());
			filtersJuridico.add(terceroJuridicoNIF);
		}
		
		if(null != razonSocial && !razonSocial.isEmpty()){
			FilterVO terceroJuridicoNIF = new FilterVO();
			terceroJuridicoNIF.setField("UPPER(NAME)");
			terceroJuridicoNIF.setOperator(getOperador(tipoBusqueda2));
			terceroJuridicoNIF.setValue(razonSocial.toUpperCase());
			filtersJuridico.add(terceroJuridicoNIF);
		}
				
		if(filtersJuridico.size() > 0){
			criterioInterJuridico.setFilters(filtersJuridico);
		}
		else{
			criterioInterJuridico = null;
		}
		return criterioInterJuridico;
	}
	
	public List<Interesado> buscarInteresado(TerceroManager terceroManager, String entidadId) {
		MultiEntityContextHolder.setEntity(entidadId);
		List<Interesado> resultInteresados = new ArrayList<Interesado>();
		
    	Transaction tran = null;
    	try {
	    	Session session = HibernateUtil.currentSession(entidadId);
		    tran = session.beginTransaction();

		    if(null != tipo && tipo.equalsIgnoreCase("P")){
		    	CriteriaVO criterioInterFisico = criteriaFisico();
		    	if(null != criterioInterFisico){
			    	List<TerceroValidadoVO> tercerosFisicos = terceroManager.findByCriteria(criterioInterFisico);
			    	
			    	if((tercerosFisicos.size()>0) && !(tercerosFisicos.isEmpty())){
						for(TerceroValidadoVO terceroVO : tercerosFisicos){
							Interesado interesado = new Interesado();
						    TerceroValidadoFisicoVO tercero = (TerceroValidadoFisicoVO) terceroVO;
							interesado.setId(tercero.getIdAsInteger());
							interesado.setIdTercero(tercero.getIdAsInteger());
						    interesado.setTipo("P");
						    if (tercero.getTipoDocumento() != null) {
						    	interesado.setTipodoc(tercero.getTipoDocumento().getId());
						    }
						    interesado.setDocIdentidad(tercero.getNumeroDocumento());
						    interesado.setNombre(tercero.getNombre());
						    interesado.setPapellido(tercero.getApellido1());
						    interesado.setSapellido(tercero.getApellido2());
						    
						    interesado.setDireccionesFisicas(tercero.getDireccionesFisicas());
						    interesado.setDireccionFisicaPrincipal(tercero.getDireccionFisicaPrincipal());
						    interesado.setDireccionesTelematicas(tercero.getDireccionesTelematicas());
						    interesado.setDireccionTelematicaPrincipal(tercero.getDireccionTelematicaPrincipal());
						    
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
						    	
								interesado.setDireccionSeleccionada(direccionSeleccionadaAux );
						    }
						    
						    if(resultInteresados.size()<100){
						    	resultInteresados.add(interesado);
						    }					    
						}
					}
			    }
		    }
		    else if(null != tipo && tipo.equalsIgnoreCase("J")){
		    	CriteriaVO criterioInterJuridico = criteriaJuridico();
		    	if(null != criterioInterJuridico){
			    	List<TerceroValidadoVO> tercerosJuridicos = terceroManager.findByCriteria(criterioInterJuridico);
			    	if((tercerosJuridicos.size()>0) && !(tercerosJuridicos.isEmpty())){
						for(TerceroValidadoVO terceroVO : tercerosJuridicos){
							Interesado interesado = new Interesado();
							TerceroValidadoJuridicoVO tercero = (TerceroValidadoJuridicoVO) terceroVO;
							interesado.setId(tercero.getIdAsInteger());
							interesado.setIdTercero(tercero.getIdAsInteger());
							interesado.setTipo("J");
							if (tercero.getTipoDocumento() != null) {
								interesado.setTipodoc(tercero.getTipoDocumento().getId());
							}
							interesado.setDocIdentidad(tercero.getNumeroDocumento());
							interesado.setRazonSocial(tercero.getRazonSocial());

						    interesado.setDireccionesFisicas(tercero.getDireccionesFisicas());
						    interesado.setDireccionFisicaPrincipal(tercero.getDireccionFisicaPrincipal());
						    interesado.setDireccionesTelematicas(tercero.getDireccionesTelematicas());
						    interesado.setDireccionTelematicaPrincipal(tercero.getDireccionTelematicaPrincipal());
						    
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
						    	
								interesado.setDireccionSeleccionada(direccionSeleccionadaAux );
						    }
			
							if(resultInteresados.size()<100){
						    	resultInteresados.add(interesado);
						    }
						}
					}
		    	}
		    }
		
			HibernateUtil.commitTransaction(tran);
    	}
    	catch (Exception exception) {
    	    LOGGER.error("ERROR al buscar los terceros: " + exception.getMessage(), exception);
    	    HibernateUtil.rollbackTransaction(tran);    	    
    	}
    	finally {
    	    HibernateUtil.closeSession(entidadId);
    	}
    	return resultInteresados;
	}
}