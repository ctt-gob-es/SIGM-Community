/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.manager;

import es.msssi.dir3.api.type.CriterionEnum;
import es.msssi.dir3.api.vo.UpdateStatusVO;
import es.msssi.dir3.core.errors.DIR3Exception;

/**
 * Interfaz para los managers de datos básicos de las actualizaciones.
 * 
 * @author cmorenog
 * 
 */
public interface UpdateStatusManager extends BaseManager<UpdateStatusVO, String, CriterionEnum> {

    /**
     * 
     * Obtiene la información de la última actualización correcta.
     * 
     * @return EstadoActualizacionDcoVO
     * 		La última actualización del sistema.
     * @throws DIR3Exception .
     */
    public UpdateStatusVO getLastSuccessUpdate()
	throws DIR3Exception;

}
