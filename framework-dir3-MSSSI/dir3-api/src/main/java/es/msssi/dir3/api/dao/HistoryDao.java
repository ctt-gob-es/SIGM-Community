/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.dao;

import java.sql.SQLException;
import java.util.Map;

import es.msssi.dir3.api.type.CriterionEnum;
import es.msssi.dir3.api.vo.HistoryVO;

/**
 * Interfaz de los DAOs de datos básicos del histórico de oficinas y unidades.
 * 
 * @author cmorenog
 * 
 */
public interface HistoryDao extends BaseDao<HistoryVO, Map<String,String>, CriterionEnum> {

    /**
     * Devuelve el código de la causa de baja que corresponde a la descripción
     * que entra como parámetro.
     * 
     * @param cause
     *            descripción de la causa.
     * @return Código de la causa de baja.
     * @throws SQLException
     */
    public String getCauseId(
	String cause)
	throws SQLException;


}
