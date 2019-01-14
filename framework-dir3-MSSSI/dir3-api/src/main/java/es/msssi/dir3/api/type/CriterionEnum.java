/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.type;

/**
 * Clase abstracta para los enumerados de las constantes de los nombres de los
 * criterios de las búsquedas.
 * 
 * @author cmorenog
 * 
 */
public class CriterionEnum extends StringValuedEnum {

    private static final long serialVersionUID = 3521411830835486969L;

    /**
     * Nombre de la tabla.
     */
    private String table = null;

    /**
     * Constructor.
     * 
     * @param table
     *            Nombre de la tabla.
     * @param name
     *            Nombre del enumerado.
     * @param value
     *            Valor del enumerado.
     */
    protected CriterionEnum(String table, String name, String value) {
	super(name, value);
	setTable(table);
    }

    /**
     * Obtiene el valor del parámetro table.
     * 
     * @return table valor del campo a obtener.
     */
    public String getTable() {
	return table;
    }

    /**
     * Guarda el valor del parámetro table.
     * 
     * @param table
     *            valor del campo a guardar.
     */
    public void setTable(
	String table) {
	this.table = table;
    }

}
