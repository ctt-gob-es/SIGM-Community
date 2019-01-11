/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.beans;

import java.io.Serializable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Bean que guarda la informacion basica de un organismo.
 * 
 * @author cmorenog
 */
public class OrgBasicBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idOrg = null;
    private String nameOrg = null;
    private Integer idFather = null;
    private Integer children = null;
    private String codeOrg = null;

    /**
     * Constructor.
     */
    public OrgBasicBean() {
    }


    /**
     * @return the idOrg
     */
    public Integer getIdOrg() {
        return idOrg;
    }


    /**
     * @param idOrg the idOrg to set
     */
    public void setIdOrg(Integer idOrg) {
        this.idOrg = idOrg;
    }


    /**
     * @return the nameOrg
     */
    public String getNameOrg() {
        return nameOrg;
    }


    /**
     * @param nameOrg the nameOrg to set
     */
    public void setNameOrg(String nameOrg) {
        this.nameOrg = nameOrg;
    }


    /**
     * @return the idFather
     */
    public Integer getIdFather() {
        return idFather;
    }


    /**
     * @param idFather the idFather to set
     */
    public void setIdFather(Integer idFather) {
        this.idFather = idFather;
    }


    /**
     * @return the children
     */
    public Integer getChildren() {
        return children;
    }


    /**
     * @param children the children to set
     */
    public void setChildren(Integer children) {
        this.children = children;
    }


    /**
     * @return the codeOrg
     */
    public String getCodeOrg() {
        return codeOrg;
    }


    /**
     * @param codeOrg the codeOrg to set
     */
    public void setCodeOrg(String codeOrg) {
        this.codeOrg = codeOrg;
    }

    
}