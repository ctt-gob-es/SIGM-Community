package es.msssi.tecdoc.fwktd.dir.ws.object;

import javax.xml.bind.annotation.XmlType;
/**
 * Datos entrada del servicio web de consulta de UO.
 *
 * @author cmorenog
 *
 */
@XmlType(name = "consultarUORequest",namespace="http://es.msssi.tecdoc.fwktd.dir.ws.ConsultService/consultarUORequest")
public class ConsultarUORequest extends CommonRequest {

	private static final long serialVersionUID = 1L;
	private CriteriosUO criterios = null;
	
	/**
	 * Obtiene el valor del parámetro criterios.
	 * 
	 * @return criterios valor del campo a obtener.
	 *
	 */
	public CriteriosUO getCriterios() {
		return criterios;
	}
	
	/**
	 * Guarda el valor del parámetro criterios.
	 * 
	 * @param criterios
	 *            valor del campo a guardar.
	 */
	public void setCriterios(CriteriosUO criterios) {
		this.criterios = criterios;
	}
	
}
