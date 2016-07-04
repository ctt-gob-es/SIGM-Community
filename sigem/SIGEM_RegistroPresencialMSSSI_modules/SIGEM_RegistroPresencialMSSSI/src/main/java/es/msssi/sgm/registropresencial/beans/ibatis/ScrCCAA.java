/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.beans.ibatis;

/**
 * Clase para manejar los datos de la tabla SCR_CCAA desde Ibatis, referentes
 * a la obtención de comunidades autónomas.
 * 
 * @author cmorenog
 */
public class ScrCCAA {
    // Id de la CCAA
    private String id;
    // Nombre de la CCAA
    private String name;

    /**
     * Obtiene el valor del parámetro id.
     * 
     * @return id valor del campo a obtener.
     */
    public String getId() {
        return id;
    }
    /**
     * Guarda el valor del parámetro id.
     * 
     * @param id
     *            valor del campo a guardar.
     */
    public void setId(
        String id) {
        this.id = id;
    }
    /**
     * Obtiene el valor del parámetro name.
     * 
     * @return name valor del campo a obtener.
     */
    public String getName() {
        return name;
    }
    /**
     * Guarda el valor del parámetro name.
     * 
     * @param name
     *            valor del campo a guardar.
     */
    public void setName(
        String name) {
        this.name = name;
    }

}