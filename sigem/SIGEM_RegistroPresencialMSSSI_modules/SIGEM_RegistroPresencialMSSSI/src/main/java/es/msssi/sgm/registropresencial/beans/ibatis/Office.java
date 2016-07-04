package es.msssi.sgm.registropresencial.beans.ibatis;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Bean para la tabla de oficinas.
 * 
 * @author cmorenog
 */
public class Office implements Serializable {

    private static final long serialVersionUID = -5286803184764261549L;

    /**
     * Valor de officeId.
     */
    private int officeId;
    /**
     * Valor de officeCode.
     */
    private String officeCode;
    /**
     * Valor de officeAcron.
     */
    private String officeAcron;
    /**
     * Valor de officeName.
     */
    private String officeName;
    /**
     * Valor de officeCreationDate.
     */
    private Date officeCreationDate;
    /**
     * Valor de officeDisableDate.
     */
    private Date officeDisableDate;
    /**
     * Valor de officeIdOrgs.
     */
    private int officeIdOrgs;
    /**
     * Valor de officeDepartamentId.
     */
    private int officeDepartamentId;
    /**
     * Valor de officeType.
     */
    private int officeType;
    /**
     * Valor de officeStamp.
     */
    private String officeStamp;

    /**
     * Valor de permisos.
     */
    private List<Permission> permissions;

    /**
     * Crea una nueva oficina.
     */
    public Office() {
	this.officeId = -1;
	this.officeCode = null;
	this.officeAcron = null;
	this.officeName = null;
	this.officeCreationDate = null;
	this.officeDisableDate = null;
	this.officeIdOrgs = -1;
	this.officeDepartamentId = -1;
	this.officeType = -1;
	this.officeStamp = null;
	
    }

    /**
     * Retorna el valor de officeId.
     * 
     * @return Valor de officeId.
     */
    public int getOfficeId() {
	return officeId;
    }

    /**
     * Establece el valor de officeId.
     * 
     * @param officeId
     *            Valor de officeId.
     */
    public void setOfficeId(
	int officeId) {
	this.officeId = officeId;
    }

    /**
     * Retorna el valor de officeCode.
     * 
     * @return Valor de officeCode.
     */
    public String getOfficeCode() {
	return officeCode;
    }

    /**
     * Establece el valor de officeCode.
     * 
     * @param officeCode
     *            Valor de officeCode.
     */
    public void setOfficeCode(
	String officeCode) {
	this.officeCode = officeCode;
    }

    /**
     * Retorna el valor de officeAcron.
     * 
     * @return Valor de officeAcron.
     */
    public String getOfficeAcron() {
	return officeAcron;
    }

    /**
     * Establece el valor de officeAcron.
     * 
     * @param officeAcron
     *            Valor de officeAcron.
     */
    public void setOfficeAcron(
	String officeAcron) {
	this.officeAcron = officeAcron;
    }

    /**
     * Retorna el valor de officeName.
     * 
     * @return Valor de officeName.
     */
    public String getOfficeName() {
	return officeName;
    }

    /**
     * Establece el valor de officeName.
     * 
     * @param officeName
     *            Valor de officeName.
     */
    public void setOfficeName(
	String officeName) {
	this.officeName = officeName;
    }

    /**
     * Retorna el valor de officeCreationDate.
     * 
     * @return Valor de officeCreationDate.
     */
    public Date getOfficeCreationDate() {
	return officeCreationDate;
    }

    /**
     * Establece el valor de officeCreationDate.
     * 
     * @param officeCreationDate
     *            Valor de officeCreationDate.
     */
    public void setOfficeCreationDate(
	Date officeCreationDate) {
	this.officeCreationDate = officeCreationDate;
    }

    /**
     * Retorna el valor de officeDisableDate.
     * 
     * @return Valor de officeDisableDate.
     */
    public Date getOfficeDisableDate() {
	return officeDisableDate;
    }

    /**
     * Establece el valor de officeDisableDate.
     * 
     * @param officeDisableDate
     *            Valor de officeDisableDate.
     */
    public void setOfficeDisableDate(
	Date officeDisableDate) {
	this.officeDisableDate = officeDisableDate;
    }

    /**
     * Retorna el valor de officeIdOrgs.
     * 
     * @return Valor de officeIdOrgs.
     */
    public int getOfficeIdOrgs() {
	return officeIdOrgs;
    }

    /**
     * Establece el valor de officeIdOrgs.
     * 
     * @param officeIdOrgs
     *            Valor de officeIdOrgs.
     */
    public void setOfficeIdOrgs(
	int officeIdOrgs) {
	this.officeIdOrgs = officeIdOrgs;
    }

    /**
     * Retorna el valor de officeDepartamentId.
     * 
     * @return Valor de officeDepartamentId.
     */
    public int getOfficeDepartamentId() {
	return officeDepartamentId;
    }

    /**
     * Establece el valor de officeDepartamentId.
     * 
     * @param officeDepartamentId
     *            Valor de officeDepartamentId.
     */
    public void setOfficeDepartamentId(
	int officeDepartamentId) {
	this.officeDepartamentId = officeDepartamentId;
    }

    /**
     * Retorna el valor de officeType.
     * 
     * @return Valor de officeType.
     */
    public int getOfficeType() {
	return officeType;
    }

    /**
     * Establece el valor de officeType.
     * 
     * @param officeType
     *            Valor de officeType.
     */
    public void setOfficeType(
	int officeType) {
	this.officeType = officeType;
    }

    /**
     * Retorna el valor de permissions.
     * 
     * @return Valor de permissions.
     */
    public List<Permission> getPermissions() {
	return permissions;
    }

    /**
     * Establece el valor de permissions.
     * 
     * @param permissions
     *            Valor de permissions.
     */
    public void setPermissions(
	List<Permission> permissions) {
	this.permissions = permissions;
    }

    public int compareTo(
	Office office) {
	int result = 0;
	if (office == null) {
	    result = -1;
	}
	else {
	    if (this.getOfficeName().compareTo(
		office.getOfficeName()) < 0) {
		result = -1;
	    }
	    else {
		if (this.getOfficeCode().compareTo(
		    office.getOfficeCode()) < 0) {
		    result = -1;
		}
		else {
		    if (this.getOfficeId() > office.getOfficeId()) {
			result = -1;
		    }
		    else {
			if (this.getOfficeId() < office.getOfficeId()) {
			    result = 1;
			}
		    }
		}
	    }
	}
	return result;
    }

    /**
     * Retorna el valor de officeStamp.
     * 
     * @return Valor de officeStamp.
     */
    public String getOfficeStamp() {
        return officeStamp;
    }
    
    /**
     * Establece el valor de officeStamp.
     * 
     * @param officeStamp
     *            Valor de officeStamp.
     */
    public void setOfficeStamp(
        String officeStamp) {
        this.officeStamp = officeStamp;
    }

    public boolean equals(
	Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Office other = (Office) obj;
	if (this.getOfficeId() == -1) {
	    if (other.getOfficeId() != -1)
		return false;
	}
	else if (this.getOfficeId() != (other.getOfficeId()))
	    return false;
	return true;
    }
}