package es.dipucr.sgm.registropresencial.beans;

import java.util.ArrayList;
import java.util.List;

import es.ieci.tecdoc.isicres.terceros.business.vo.DireccionFisicaVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.DireccionTelematicaVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.PaisVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.ProvinciaVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TipoDireccionTelematicaVO;
import es.msssi.sgm.registropresencial.beans.GenericBean;
import es.msssi.sgm.registropresencial.beans.Interesado;

public class TercerosBean extends GenericBean{

	private static final long serialVersionUID = 1L;
	
	private List<ProvinciaVO> provincias = null;
	private ProvinciaVO provinciaDefecto = null;
	
	private List<PaisVO> paises = null;
	private PaisVO paisDefecto = null;
	
	private DireccionFisicaVO nuevaDireccionFisica = null;
	private DireccionFisicaVO direccionFisicaBorrar = null;

	private DireccionTelematicaVO nuevaDireccionTelematica = null;
	private DireccionTelematicaVO direccionTelematicaBorrar = null;

	private List<TipoDireccionTelematicaVO> tiposDireccionTelematica = null;
	private TipoDireccionTelematicaVO tipoDireccionTelematicaDefecto = null;
	
	//Se crea el interesadoRepresentado para almacenar el interesado con el que
	// se está trabajando y poder recuperarlo luego sin tener que volver a
	// lanzar las búsquedas.
	private Interesado interesadoDeTrabajo = null;
	
	private Interesado nuevoInteresadoFisico = null;
	private Interesado nuevoInteresadoJuridico = null;
	
    public List<ProvinciaVO> getProvincias() {
		return provincias;
	}

	public void setProvincias(List<ProvinciaVO> provincias) {
		this.provincias = provincias;
	}

	public ProvinciaVO getProvinciaDefecto() {
		ProvinciaVO provincia = null;
		if(null != provinciaDefecto){
			// Se instancia un nuevo objeto ya que si no pasa la provincia por referencia y se va liando todo el rato.
			provincia = new ProvinciaVO();
			provincia.setCodigo(provinciaDefecto.getCodigo());
			provincia.setNombre(provinciaDefecto.getNombre());
			provincia.setCiudades(provinciaDefecto.getCiudades());
		}
				
		return provincia;
	}

	public void setProvinciaDefecto(ProvinciaVO provinciaDefecto) {
		this.provinciaDefecto = provinciaDefecto;
	}
    
    public List<PaisVO> getPaises() {
		return paises;
	}

	public void setPaises(List<PaisVO> paises) {
		this.paises = paises;
	}
	
	public PaisVO getPaisDefecto() {
		PaisVO pais = null;
		
		if (null != paisDefecto){
			// Se instancia un nuevo objeto ya que si no pasa la provincia por referencia y se va liando todo el rato.
			pais = new PaisVO();
			pais.setCodigo(paisDefecto.getCodigo());
			pais.setNombre(paisDefecto.getNombre());
		}
				
		return pais;
	}

	public void setPaisDefecto(PaisVO paisDefecto) {
		this.paisDefecto = paisDefecto;
	}
	
	public DireccionFisicaVO getNuevaDireccionFisica() {
		return nuevaDireccionFisica;
	}

	public void setNuevaDireccionFisica(DireccionFisicaVO nuevaDireccionFisica) {
		this.nuevaDireccionFisica = nuevaDireccionFisica;
	}

	public DireccionFisicaVO getDireccionFisicaBorrar() {
		return direccionFisicaBorrar;
	}

	public void setDireccionFisicaBorrar(DireccionFisicaVO direccionFisicaBorrar) {
		this.direccionFisicaBorrar = direccionFisicaBorrar;
	}

	public DireccionTelematicaVO getNuevaDireccionTelematica() {
		if (null == nuevaDireccionTelematica) {
			nuevaDireccionTelematica = new DireccionTelematicaVO();
			nuevaDireccionTelematica.setTipoDireccionTelematica(getTipoDireccionTelematicaDefecto());
			nuevaDireccionTelematica.setPrincipal(false);
		}
		return nuevaDireccionTelematica;
	}

	public void setNuevaDireccionTelematica( DireccionTelematicaVO nuevaDireccionTelematica) {
		this.nuevaDireccionTelematica = nuevaDireccionTelematica;
	}


	public List<TipoDireccionTelematicaVO> getTiposDireccionTelematica() {		
		return tiposDireccionTelematica;
	}

	public void setTiposDireccionTelematica( List<TipoDireccionTelematicaVO> tiposDireccionTelematica) {
		this.tiposDireccionTelematica = tiposDireccionTelematica;
	}

	public TipoDireccionTelematicaVO getTipoDireccionTelematicaDefecto() {

		TipoDireccionTelematicaVO aux = null;
		
		if(null != tipoDireccionTelematicaDefecto){
			// Se instancia un nuevo objeto ya que si no pasa la provincia por referencia y se va liando todo el rato.
			aux = new TipoDireccionTelematicaVO();		
			aux.setId(tipoDireccionTelematicaDefecto.getId());
			aux.setCodigo(tipoDireccionTelematicaDefecto.getCodigo());
			aux.setDescripcion(tipoDireccionTelematicaDefecto.getDescripcion());
		}

		return aux;
	}

	public void setTipoDireccionTelematicaDefecto(TipoDireccionTelematicaVO tipoDireccionTelematicaDefecto) {
		this.tipoDireccionTelematicaDefecto = tipoDireccionTelematicaDefecto;
	}
	
	public DireccionTelematicaVO getDireccionTelematicaBorrar() {
		return direccionTelematicaBorrar;
	}

	public void setDireccionTelematicaBorrar(DireccionTelematicaVO direccionTelematicaBorrar) {
		this.direccionTelematicaBorrar = direccionTelematicaBorrar;
	}
	
	public Interesado getInteresadoDeTrabajo() {
		return interesadoDeTrabajo;
	}

	public void setInteresadoDeTrabajo(Interesado interesadoDeTrabajo) {
		this.interesadoDeTrabajo = interesadoDeTrabajo;
	}

	public Interesado getNuevoInteresadoFisico() {
		nuevoInteresadoFisico = getNuevoInteresado("P");
		return nuevoInteresadoFisico;
	}

	public void setNuevoInteresadoFisico(Interesado nuevoInteresadoFisico) {
		this.nuevoInteresadoFisico = nuevoInteresadoFisico;
	}

	public Interesado getNuevoInteresadoJuridico() {
		nuevoInteresadoJuridico = getNuevoInteresado("J");
		return nuevoInteresadoJuridico;
	}

	public void setNuevoInteresadoJuridico(Interesado nuevoInteresadoJuridico) {
		this.nuevoInteresadoJuridico = nuevoInteresadoJuridico;
	}
	
	public Interesado getNuevoInteresado(String tipo){
		
		Interesado interesado = new Interesado();
		interesado.setTipo(tipo);
		interesado.setDireccionesFisicas(new ArrayList<DireccionFisicaVO>());
		interesado.setDireccionesTelematicas(new ArrayList<DireccionTelematicaVO>());
		
		return interesado;
	}
}
