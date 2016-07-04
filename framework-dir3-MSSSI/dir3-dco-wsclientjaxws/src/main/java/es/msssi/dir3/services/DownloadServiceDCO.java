/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.services;

import java.util.Date;

import es.msssi.dir3.exception.GetFileDownloadDCOException;

/**
 * Servicio para obtener los ficheros de descarga del DCO.
 * 
 * @author cmorenog
 */
public interface DownloadServiceDCO {

    /**
     * Obtiene del DCO el fichero XML para actualizar las oficinas.
     * 
     * @param dateLastUpdate
     *            Fecha de la última actualización.
     * @return nombre del fichero de actualización de Oficinas.
     * @throws GetFileDownloadDCOException.
     */
    public String getFileUpdateOfficesDCO(
	Date dateLastUpdate)
	throws GetFileDownloadDCOException;

    /**
     * Obtiene del DCO el fichero XML para actualizar las unidades
     * 
     * @param dateLastUpdate
     *            Fecha de la última actualización.
     * @return nombre del fichero de actualización de organismos.
     * @throws GetFileDownloadDCOException.
     */
    public String getFileUpdateUnitsDCO(
	Date dateLastUpdate)
	throws GetFileDownloadDCOException;

    /**
     * Obtiene del DCO el fichero XML para actualizar las unidades no organicas.
     * 
     * @param dateLastUpdate
     *            Fecha de la última actualización.
     * @return nombre del fichero de actualización de organismos.
     * @throws GetFileDownloadDCOException.
     */
    public String getFileUpdateNotOrgUnitsDCO(
    	Date dateLastUpdate)
    	throws GetFileDownloadDCOException;
}
