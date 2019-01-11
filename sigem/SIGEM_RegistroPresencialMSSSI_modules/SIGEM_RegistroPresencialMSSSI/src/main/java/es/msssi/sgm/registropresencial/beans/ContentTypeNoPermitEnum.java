/*
* Copyright 2017 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.beans;

/**
 * Enumeral de los content-type disponibles para la descarga/visualización de
 * documentos.
 * 
 * @author saludred
 * 
 */
public enum ContentTypeNoPermitEnum {
    RAR ("application/rar"),
    GZIP ("application/x-gzip"), 
    ZIP ("application/zip"), 
    ZIP1 ("application/x-compressed"), 
    ZIP2 ("application/x-zip-compressed"), 
    ZIP3 ("multipart/x-zip")    
    ;
    private String type;


    /**
     * Constructor
     * 
     * @param type
     *            tipo de contenido
     */
    private ContentTypeNoPermitEnum(String type) {
	this.type = type;
    }

    /**
     * Devuelve el contentType con el tipo
     * 
     * @return contentType
     */
    public String getContentType() {
	return type;
    }
    
    public static ContentTypeNoPermitEnum getIfPresentExt(String str) {
        for (ContentTypeNoPermitEnum ctnp : ContentTypeNoPermitEnum.values()) {
            if (ctnp.name().equalsIgnoreCase(str))
                return ctnp;
        }
        return null;
    }
    
    public static ContentTypeNoPermitEnum getIfPresentMime(String str) {
        for (ContentTypeNoPermitEnum ctnp : ContentTypeNoPermitEnum.values()) {
            if (ctnp.getContentType().equalsIgnoreCase(str))
                return ctnp;
        }
        return null;
    }

}
