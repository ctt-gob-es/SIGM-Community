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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import es.msssi.dir3.core.vo.CriterionUO;

/**
 * Datos entrada del servicio web de consulta de contador de UO.
 * 
 * @author cmorenog
 * 
 */
@XmlType(name = "countUORequest",
    namespace = "http://es.msssi.dir3.ws.ConsultService/countUORequest")
public class CountUORequest extends CommonRequest {

    private static final long serialVersionUID = 1L;
    /**
     * Lista de criterios.
     */

    private List<CriterionUO> criteria = null;

    /**
     * Obtiene el valor del parámetro criteria.
     * 
     * @return criteria valor del campo a obtener.
     * 
     */
    public List<CriterionUO> getCriteria() {
	return criteria;
    }

    /**
     * Guarda el valor del parámetro criteria.
     * 
     * @param criteria
     *            valor del campo a guardar.
     */
    public void setCriteria(
	List<CriterionUO> criteria) {
	this.criteria = new ArrayList<CriterionUO>();
	if (criteria != null) {
	    this.criteria.addAll(criteria);
	}
    }
}
