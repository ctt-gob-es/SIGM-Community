package es.ieci.tecdoc.isicres.terceros.business.vo;

import es.ieci.tecdoc.fwktd.core.model.Entity;

/**
 * @author Iecisa
 * @version $Revision$
 *
 */
public class TipoDocumentoIdentificativoTerceroVO extends Entity {

	private static final long serialVersionUID = 1875988551007999102L;

	/**
	 * Id asociado al tipo de documento identificativo.
	 */
	protected String id;
	
	/**
	 * Código asociado al tipo de documento identificativo.
	 */
	protected String codigo;

	/**
	 * Descripción textual larga asociada al tipo de documento identificativo.
	 */
	protected String descripcion;

	/**
	 * Constructor por defecto
	 */
	public TipoDocumentoIdentificativoTerceroVO() {
		// nothing to do
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String toString(){
		return descripcion;
	}

	public boolean equals(Object o){
		if (null == o || !(o instanceof TipoDocumentoIdentificativoTerceroVO)){
			return false;
		}
		return ((TipoDocumentoIdentificativoTerceroVO) o).getId().equals(this.id);			
	}
}
