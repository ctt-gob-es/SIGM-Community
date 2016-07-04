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

import java.util.Date;

import es.msssi.dir3.core.vo.Entity;

/**
 * Información básica de una actualización.
 * 
 * @author cmorenog
 * 
 */
public class UpdateStatusVO extends Entity {

    private static final long serialVersionUID = 1763557309106784979L;
    /**
     * Fecha de la actualización.
     */
    private Date updateDate;
    /**
     * Estado de la actualización.
     */
    private String status;

    /**
     * Obtiene el valor del parámetro updateDate.
     * 
     * @return updateDate valor del campo a obtener.
     */
    public Date getUpdateDate() {
	return updateDate;
    }

    /**
     * Guarda el valor del parámetro updateDate.
     * 
     * @param updateDate
     *            valor del campo a guardar.
     */
    public void setUpdateDate(
	Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     * Obtiene el valor del parámetro status.
     * 
     * @return status valor del campo a obtener.
     */
    public String getStatus() {
	return status;
    }

    /**
     * Guarda el valor del parámetro status.
     * 
     * @param status
     *            valor del campo a guardar.
     */
    public void setStatus(
	String status) {
	this.status = status;
    }

}
