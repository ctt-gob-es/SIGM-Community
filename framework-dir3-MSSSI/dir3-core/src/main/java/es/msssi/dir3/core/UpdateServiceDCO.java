/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core;

import es.msssi.dir3.core.errors.DIR3Exception;

/**
 * 
 * Servicio para actualizar el catalogo de datos del DCO.
 * 
 * @author cmorenog.
 * 
 */
public interface UpdateServiceDCO {
	
	/**
     * Realiza la actualización directamente en BBDD tomando como fecha de la útlima actualización el 01/01/2000.
     * 
     * @throws DIR3Exception .
     */
    public void reseteaDCO() throws DIR3Exception;
	
	/**
     * Realiza la actualización directamente en BBDD.
     * 
     * @throws DIR3Exception .
     */
    public void updateDCO() throws DIR3Exception;

}
