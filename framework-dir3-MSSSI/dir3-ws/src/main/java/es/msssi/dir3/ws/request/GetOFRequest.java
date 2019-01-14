/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.ws.request;

import javax.xml.bind.annotation.XmlType;

/**
 * Datos entrada del servicio web de recuperar de OF.
 * 
 * @author cmorenog
 * 
 */
@XmlType(name = "getOFRequest", namespace = "http://es.msssi.dir3.ws.ConsultService/getOFRequest")
public class GetOFRequest extends CommonRequest {

    private static final long serialVersionUID = 1L;
    private String officeId = null;

    /**
     * Obtiene el valor del parámetro officeId.
     * 
     * @return officeId valor del campo a obtener.
     * 
     */
    public String getOfficeId() {
	return officeId;
    }

    /**
     * Guarda el valor del parámetro officeId.
     * 
     * @param officeId
     *            valor del campo a guardar.
     */
    public void setOfficeId(
	String officeId) {
	this.officeId = officeId;
    }

}
