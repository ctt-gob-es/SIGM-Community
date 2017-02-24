/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core.vo;

import java.util.Date;


public class UnitRegistro extends Entity {

    /** identifier field */
    private Integer idOrg;

    /** persistent field */
    private String code;

    /** nullable persistent field */
    private Integer idFather;
    
    /** nullable persistent field */
    private String codeFather;
    
    /** persistent field */
    private String acron;

    /** persistent field */
    private String name;

    /** persistent field */
    private Date creationDate;

    /** nullable persistent field */
    private Date disableDate;

    /** persistent field */
    private int enabled;

    /** nullable persistent field */
    private String cif;

    /** persistent field */
    private int type;

    /** nullable persistent field */
    private Integer hierarchicalLevel;
    /** nullable persistent field */
    private String adminLevel;
    /** nullable persistent field */
    private String entityType;
    /** nullable persistent field */
    private String uoType;
    /** nullable persistent field */
    private Integer idRoot;
    /** nullable persistent field */
    private String codeRoot;
    /** nullable persistent field */
    private String idCCAA;
    /** nullable persistent field */
    private Integer idProv;
    
    /** default constructor */
    public UnitRegistro() {
    }


    public Integer getIdOrg() {
        return idOrg;
    }

    public void setIdOrg(Integer idOrg) {
        this.idOrg = idOrg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIdFather() {
        return idFather;
    }

    public void setIdFather(Integer idFather) {
        this.idFather = idFather;
    }

    public String getAcron() {
        return acron;
    }

    public void setAcron(String acron) {
        this.acron = acron;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDisableDate() {
        return disableDate;
    }

    public void setDisableDate(Date disableDate) {
        this.disableDate = disableDate;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getHierarchicalLevel() {
        return hierarchicalLevel;
    }

    public void setHierarchicalLevel(Integer hierarchicalLevel) {
        this.hierarchicalLevel = hierarchicalLevel;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getUoType() {
        return uoType;
    }

    public void setUoType(String uoType) {
        this.uoType = uoType;
    }

    public Integer getIdRoot() {
        return idRoot;
    }

    public void setIdRoot(Integer idRoot) {
        this.idRoot = idRoot;
    }

    public String getIdCCAA() {
        return idCCAA;
    }

    public void setIdCCAA(String idCCAA) {
        this.idCCAA = idCCAA;
    }

    public Integer getIdProv() {
        return idProv;
    }

    public void setIdProv(Integer idProv) {
        this.idProv = idProv;
    }


    public String getCodeFather() {
        return codeFather;
    }


    public void setCodeFather(String codeFather) {
        this.codeFather = codeFather;
    }


    public String getCodeRoot() {
        return codeRoot;
    }


    public void setCodeRoot(String codeRoot) {
        this.codeRoot = codeRoot;
    }


}
