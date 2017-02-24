/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.vo;

import java.util.List;

import es.msssi.dir3.core.vo.Entity;

/**
 * Datos básicos de las relaciones.
 * 
 * @author cmorenog
 * 
 */

public class RelationshipsOFIUOVO extends Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 3022391899522441647L;
    /**
     * Descripción del servicio.
     */
    private List<RelationshipOFIUOVO> relationships;

    /**
     * Obtiene el valor del parámetro relationships.
     * 
     * @return relationships valor del campo a obtener.
     */
    public List<RelationshipOFIUOVO> getRelationships() {
	return relationships;
    }

    /**
     * Guarda el valor del parámetro relationships.
     * 
     * @param relationships
     *            valor del campo a guardar.
     */
    public void setRelationships(
	List<RelationshipOFIUOVO> relationships) {
	this.relationships = relationships;
    }

}
