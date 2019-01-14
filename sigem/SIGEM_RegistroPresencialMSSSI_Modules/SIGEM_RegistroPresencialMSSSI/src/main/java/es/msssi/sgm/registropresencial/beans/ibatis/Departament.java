package es.msssi.sgm.registropresencial.beans.ibatis;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Bean para la tabla de Departamentos.
 * 
 * @author cmorenog
 */
public class Departament implements Serializable {

    private static final long serialVersionUID = -5286803184764261549L;

    /**
     * Valor de departamentId.
     */
    private int departamentId;

    /**
     * Valor de departamentName.
     */
    private String departamentName;

    /**
     * Valor de departamentParentId.
     */
    private int departamentParentId;

    /**
     * Valor de departamentParentName.
     */
    private String departamentParentName;

    /**
     * Valor de departamentMgrid.
     */
    private int departamentMgrid;

    /**
     * Valor de departamentType.
     */
    private int departamentType;

    /**
     * Valor de departamentRemarks.
     */
    private String departamentRemarks;

    /**
     * Valor de departamentCrtrid.
     */
    private int departamentCrtrid;

    /**
     * Valor de departamentCrtndate.
     */
    private Date departamentCrtndate;

    /**
     * Valor de departamentUpdrid.
     */
    private int departamentUpdrid;

    /**
     * Valor de departamentUpddate.
     */
    private Date departamentUpddate;

    /**
     * Valor de departamentNivel.
     */
    private int departamentNivel;

    /**
     * Valor de isEqualText.
     */
    private int isEqualText;

    /**
     * Valor de permisos.
     */
    private List<Permission> permissions;

    /**
     * Crea un nuevo Departament.
     */
    public Departament() {
	this.departamentId = -1;
	this.departamentName = null;
	this.departamentParentId = -1;
	this.departamentParentName = null;
	this.departamentMgrid = 0;
	this.departamentType = 0;
	this.departamentRemarks = null;
	this.departamentCrtrid = 3;
	this.departamentCrtndate = null;
	this.departamentUpdrid = -1;
	this.departamentUpddate = null;
	this.isEqualText = 0;
	this.permissions = null;
    }

    /**
     * Crea un nuevo Departament.
     */
    public Departament(String departamentName) {
	this.departamentId = -1;
	this.departamentName = departamentName;
	this.departamentParentId = -1;
	this.departamentMgrid = 0;
	this.departamentParentName = null;
	this.departamentType = 0;
	this.departamentRemarks = null;
	this.departamentCrtrid = 3;
	this.departamentCrtndate = null;
	this.departamentUpdrid = -1;
	this.departamentUpddate = null;
	this.isEqualText = 0;
	this.permissions = null;
    }

    /**
     * Crea un nuevo Departament.
     */
    public Departament(int departamentId, String departamentName, int departamentParentId,String  departamentParentName) {
	this.departamentId = departamentId;
	this.departamentName = departamentName;
	this.departamentParentId = departamentParentId;
	this.departamentParentName = departamentParentName;
	this.departamentMgrid = 0;
	this.departamentType = 0;
	this.departamentRemarks = null;
	this.departamentCrtrid = 3;
	this.departamentCrtndate = null;
	this.departamentUpdrid = -1;
	this.departamentUpddate = null;
	this.isEqualText = 0;
	this.permissions = null;
    }
    
    /**
     * Retorna el valor de departamentId.
     * 
     * @return Valor de departamentId.
     */
    public int getDepartamentId() {
	return departamentId;
    }

    /**
     * Establece el valor de departamentId.
     * 
     * @param departamentId
     *            Valor de departamentId.
     */
    public void setDepartamentId(
	int departamentId) {
	this.departamentId = departamentId;
    }

    /**
     * Retorna el valor de departamentName.
     * 
     * @return Valor de departamentName.
     */
    public String getDepartamentName() {
	return departamentName;
    }

    /**
     * Establece el valor de departamentName.
     * 
     * @param departamentName
     *            Valor de departamentName.
     */
    public void setDepartamentName(
	String departamentName) {
	this.departamentName = departamentName;
    }

    /**
     * Retorna el valor de departamentParentId.
     * 
     * @return Valor de departamentParentId.
     */
    public int getDepartamentParentId() {
	return departamentParentId;
    }

    /**
     * Establece el valor de departamentParentId.
     * 
     * @param departamentParentId
     *            Valor de departamentParentId.
     */
    public void setDepartamentParentId(
	int departamentParentId) {
	this.departamentParentId = departamentParentId;
    }

    /**
     * Retorna el valor de departamentMgrid.
     * 
     * @return Valor de departamentMgrid.
     */
    public int getDepartamentMgrid() {
	return departamentMgrid;
    }

    /**
     * Establece el valor de departamentMgrid.
     * 
     * @param departamentMgrid
     *            Valor de departamentMgrid.
     */
    public void setDepartamentMgrid(
	int departamentMgrid) {
	this.departamentMgrid = departamentMgrid;
    }

    /**
     * Retorna el valor de departamentType.
     * 
     * @return Valor de departamentType.
     */
    public int getDepartamentType() {
	return departamentType;
    }

    /**
     * Establece el valor de departamentType.
     * 
     * @param departamentType
     *            Valor de departamentType.
     */
    public void setDepartamentType(
	int departamentType) {
	this.departamentType = departamentType;
    }

    /**
     * Retorna el valor de departamentRemarks.
     * 
     * @return Valor de departamentRemarks.
     */
    public String getDepartamentRemarks() {
	return departamentRemarks;
    }

    /**
     * Establece el valor de departamentRemarks.
     * 
     * @param departamentRemarks
     *            Valor de departamentRemarks.
     */
    public void setDepartamentRemarks(
	String departamentRemarks) {
	this.departamentRemarks = departamentRemarks;
    }

    /**
     * Retorna el valor de departamentCrtrid.
     * 
     * @return Valor de departamentCrtrid.
     */
    public int getDepartamentCrtrid() {
	return departamentCrtrid;
    }

    /**
     * Establece el valor de departamentCrtrid.
     * 
     * @param departamentCrtrid
     *            Valor de departamentCrtrid.
     */
    public void setDepartamentCrtrid(
	int departamentCrtrid) {
	this.departamentCrtrid = departamentCrtrid;
    }

    /**
     * Retorna el valor de departamentCrtndate.
     * 
     * @return Valor de departamentCrtndate.
     */
    public Date getDepartamentCrtndate() {
	return departamentCrtndate;
    }

    /**
     * Establece el valor de departamentCrtndate.
     * 
     * @param departamentCrtndate
     *            Valor de departamentCrtndate.
     */
    public void setDepartamentCrtndate(
	Date departamentCrtndate) {
	this.departamentCrtndate = departamentCrtndate;
    }

    /**
     * Retorna el valor de departamentUpdrid.
     * 
     * @return Valor de departamentUpdrid.
     */
    public int getDepartamentUpdrid() {
	return departamentUpdrid;
    }

    /**
     * Establece el valor de departamentUpdrid.
     * 
     * @param departamentUpdrid
     *            Valor de departamentUpdrid.
     */
    public void setDepartamentUpdrid(
	int departamentUpdrid) {
	this.departamentUpdrid = departamentUpdrid;
    }

    /**
     * Retorna el valor de departamentUpddate.
     * 
     * @return Valor de departamentUpddate.
     */
    public Date getDepartamentUpddate() {
	return departamentUpddate;
    }

    /**
     * Establece el valor de departamentUpddate.
     * 
     * @param departamentUpddate
     *            Valor de departamentUpddate.
     */
    public void setDepartamentUpddate(
	Date departamentUpddate) {
	this.departamentUpddate = departamentUpddate;
    }

    /**
     * Retorna el valor de departamentParentName.
     * 
     * @return Valor de departamentParentName.
     */
    public String getDepartamentParentName() {
	return departamentParentName;
    }

    /**
     * Establece el valor de departamentParentName.
     * 
     * @param departamentParentName
     *            Valor de departamentParentName.
     */
    public void setDepartamentParentName(
	String departamentParentName) {
	this.departamentParentName = departamentParentName;
    }

    /**
     * Retorna el valor de departamentNivel.
     * 
     * @return Valor de departamentNivel.
     */
    public int getDepartamentNivel() {
	return departamentNivel;
    }

    /**
     * Establece el valor de departamentNivel.
     * 
     * @param departamentNivel
     *            Valor de departamentNivel.
     */
    public void setDepartamentNivel(
	int departamentNivel) {
	this.departamentNivel = departamentNivel;
    }

    /**
     * Retorna el valor de isEqualText.
     * 
     * @return Valor de isEqualText.
     */
    public int getIsEqualText() {
	return isEqualText;
    }

    /**
     * Establece el valor de isEqualText.
     * 
     * @param isEqualText
     *            Valor de isEqualText.
     */
    public void setIsEqualText(
	int isEqualText) {
	this.isEqualText = isEqualText;
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
	Departament departament) {
	int result = 0;
	if (departament == null){
	    result = -1;
	}
	else{
	    if (this.getDepartamentName().compareTo(departament.getDepartamentName())  < 0){
		result = -1;
	    }
	    else {
		if (this.getDepartamentId() > departament.getDepartamentId()){
		    result = -1;
		}
		else {
		    if (this.getDepartamentId() < departament.getDepartamentId()){
			    result = 1;
			}
		}
	    }
	}
	return result;
    }
    
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Departament other = (Departament) obj;
        if (this.getDepartamentId() == -1) {
            if (other.getDepartamentId() != -1)
                return false;
        } else if (this.getDepartamentId() != (other.getDepartamentId()))
            return false;
        return true;
    }
}