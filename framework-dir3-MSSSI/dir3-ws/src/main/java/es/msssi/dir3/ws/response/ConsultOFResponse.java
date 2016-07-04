/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.ws.response;

import java.util.List;

import javax.xml.bind.annotation.XmlType;

import es.msssi.dir3.core.vo.Office;

/**
 * Datos salida del servicio web de consulta de oficinas.
 * 
 * @author cmorenog
 * 
 */
@XmlType(name = "consultOFResponse",
    namespace = "http://es.msssi.dir3.ws.ConsultService/consultOFResponse")
public class ConsultOFResponse extends CommonResponse {

    private static final long serialVersionUID = 1L;

    private List<Office> offices;

    /**
     * Obtiene el valor del parámetro offices.
     * 
     * @return offices valor del campo a obtener.
     * 
     */
    public List<Office> getOffices() {
	return offices;
    }

    /**
     * Guarda el valor del parámetro offices.
     * 
     * @param offices
     *            valor del campo a guardar.
     */
    public void setOffices(
	List<Office> offices) {
	this.offices = offices;
    }
}
