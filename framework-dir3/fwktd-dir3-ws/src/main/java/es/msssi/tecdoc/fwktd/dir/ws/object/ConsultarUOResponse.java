package es.msssi.tecdoc.fwktd.dir.ws.object;

import java.util.List;
import javax.xml.bind.annotation.XmlType;
import es.msssi.tecdoc.fwktd.dir.ws.object.DatosUnidadOrganica;
/**
 * Datos salida del servicio web de consulta de UO.
 *
 * @author cmorenog
 *
 */
@XmlType(name = "consultarUOResponse",namespace="http://es.msssi.tecdoc.fwktd.dir.ws.ConsultService/consultarUOResponse")
public class ConsultarUOResponse extends CommonResponse{

	private static final long serialVersionUID = 1L;
	
	private List<DatosUnidadOrganica> unidades;

	/**
	 * Obtiene el valor del parámetro unidades.
	 * 
	 * @return unidades valor del campo a obtener.
	 *
	 */
	public List<DatosUnidadOrganica> getUnidades() {
		return unidades;
	}
	
	/**
	 * Guarda el valor del parámetro unidades.
	 * 
	 * @param unidades
	 *            valor del campo a guardar.
	 */
	public void setUnidades(List<DatosUnidadOrganica> unidades) {
		this.unidades = unidades;
	}
}
