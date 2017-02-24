package es.msssi.sgm.registropresencial.beans.ibatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Bean para la tabla de usuarios.
 * 
 * @author cmorenog
 */
public class User implements Serializable {

    private static final long serialVersionUID = -5286803184764261549L;

    /**
     * Valor de id usuario.
     */
    private int userId;

    /**
     * valor del usuario.
     */
    private String userUser;

    /**
     * valor del password.
     */
    private String userPassword;

    /**
     * valor del repassword.
     */
    private String userRePassword;

    /**
     * valor de la descripción.
     */
    private String userDescription;

    /**
     * valor del departamento.
     */
    private Departament userDepartament;
    /**
     * valor del nombre.
     */
    private String userName;

    /**
     * valor del apellido 1.
     */
    private String userSurname1;

    /**
     * valor del apellido 2.
     */
    private String userSurname2;

    /**
     * valor del apellidoS.
     */
    private String userSurname;

    /**
     * valor del cargo.
     */
    private String userAppointment;

    /**
     * valor del userEmail.
     */
    private String userEmail;

    /**
     * valor del userTelephone.
     */
    private String userTelephone;

    /**
     * valor de la fecha de creacion.
     */
    private Date userCreateData;

    /**
     * valor de la fecha de modificacion.
     */
    private Date userUpdateData;
    /**
     * valor de la fecha de modificacion de contrasena.
     */
    private long userPwdLastUpdts;
    /**
     * valor de la fecha de modificacion.
     */
    private long userUpdateId;
    /**
     * Valor de permisos.
     */
    private List<Permission> permissions;
    /**
     * Valor de perfiles.
     */
    private List<Profile> profiles;

    private String userAddress;
    private String userCity;
    private String userCountry;
    private String userZip;
    private String userFax;
    private byte[] userConfig;
    private int userProfileType;
    private Office userPreferentOffice;
    private List<String> userGenericPermissionsList;
    private int userGenericPermissions;
    private boolean changeDepartament;
    
    /**
     * usuarios registro electrónico.
     */
    private String gottaOffice;
    private String gottaUnit;
    private String gottaDepartament;
    private int gottaCapicom;
    private String gottaLanguage;
    private int gottaAttachedSign;
    private int gottaRequiredSign;

    
    /**
     * constructor.
     */
    public User() {
	this.userId = -1;
	this.userUser = null;
	this.userPassword = null;
	this.userRePassword = null;
	this.userDescription = null;
	this.userDepartament = null;
	this.userName = null;
	this.userSurname1 = null;
	this.userSurname2 = null;
	this.userAppointment = null;
	this.userEmail = null;
	this.userTelephone = null;
	this.permissions = null;
	this.userCreateData = null;
	this.userUpdateData = null;
	this.userPwdLastUpdts = -1;
	this.userUpdateId = -1;
	this.profiles = null;
	this.userAddress = null;
	this.userCity = null;
	this.userCountry = null;
	this.userZip = null;
	this.userFax = null;
	this.userConfig = "<Configuration></Configuration>".getBytes();
	this.userProfileType = 1;
	this.userPreferentOffice = null;
	initGenericPermissionList ();
	changeDepartament = false;
	gottaOffice = null;
	gottaUnit = null;
	gottaDepartament = null;
	gottaCapicom = 0;
	gottaLanguage = "J";
	gottaAttachedSign = 0;
	gottaRequiredSign = 1;
    }
    
    private void initGenericPermissionList (){
	userGenericPermissionsList = new ArrayList <String>();
    }
    
    /**
     * Retorna el valor de userGenericPermissionsList.
     * 
     * @return Valor de userGenericPermissionsList.
     */
    public List<String> getUserGenericPermissionsList() {
        return userGenericPermissionsList;
    }
    /**
     * Establece el valor de userGenericPermissionsList.
     * 
     * @param userGenericPermissionsList
     *            Valor de userGenericPermissionsList.
     */
    public void setUserGenericPermissionsList(
	List<String> userGenericPermissionsList) {
        this.userGenericPermissionsList = userGenericPermissionsList;
    }
    /**
     * Retorna el valor de userGenericPermissions.
     * 
     * @return Valor de userGenericPermissions.
     */
    public int getUserGenericPermissions() {
        return userGenericPermissions;
    }
    /**
     * Establece el valor de userGenericPermissions.
     * 
     * @param userGenericPermissions
     *            Valor de userGenericPermissions.
     */
    public void setUserGenericPermissions(
        int userGenericPermissions) {
        this.userGenericPermissions = userGenericPermissions;
    }

    /**
     * Retorna el valor de userId.
     * 
     * @return Valor de userId.
     */
    public int getUserId() {
	return userId;
    }

    /**
     * Establece el valor de userId.
     * 
     * @param userId
     *            Valor de userId.
     */
    public void setUserId(
	int userId) {
	this.userId = userId;
    }

    /**
     * Retorna el valor de userUser.
     * 
     * @return Valor de userUser.
     */
    public String getUserUser() {
	return userUser;
    }

    /**
     * Establece el valor de userUser.
     * 
     * @param userUser
     *            Valor de userUser.
     */
    public void setUserUser(
	String userUser) {
	this.userUser = userUser;
    }

    /**
     * Retorna el valor de userPassword.
     * 
     * @return Valor de userPassword.
     */
    public String getUserPassword() {
	return userPassword;
    }

    /**
     * Establece el valor de userPassword.
     * 
     * @param userPassword
     *            Valor de userPassword.
     */
    public void setUserPassword(
	String userPassword) {
	this.userPassword = userPassword;
    }

    /**
     * Retorna el valor de userRePassword.
     * 
     * @return Valor de userRePassword.
     */
    public String getUserRePassword() {
	return userRePassword;
    }

    /**
     * Establece el valor de userRePassword.
     * 
     * @param userRePassword
     *            Valor de userRePassword.
     */
    public void setUserRePassword(
	String userRePassword) {
	this.userRePassword = userRePassword;
    }

    /**
     * Retorna el valor de userDescription.
     * 
     * @return Valor de userDescription.
     */
    public String getUserDescription() {
	return userDescription;
    }

    /**
     * Establece el valor de userDescription.
     * 
     * @param userDescription
     *            Valor de userDescription.
     */
    public void setUserDescription(
	String userDescription) {
	this.userDescription = userDescription;
    }

    /**
     * Retorna el valor de userDepartament.
     * 
     * @return Valor de userDepartament.
     */
    public Departament getUserDepartament() {
	return userDepartament;
    }

    /**
     * Establece el valor de userDepartament.
     * 
     * @param userDepartament
     *            Valor de userDepartament.
     */
    public void setUserDepartament(
	Departament userDepartament) {
	this.userDepartament = userDepartament;
    }

    /**
     * Retorna el valor de userName.
     * 
     * @return Valor de userName.
     */
    public String getUserName() {
	return userName;
    }

    /**
     * Establece el valor de userName.
     * 
     * @param userName
     *            Valor de userName.
     */
    public void setUserName(
	String userName) {
	this.userName = userName;
    }

    /**
     * Retorna el valor de userSurname2.
     * 
     * @return Valor de userSurname2.
     */
    public String getUserSurname2() {
	return userSurname2;
    }

    /**
     * Establece el valor de userSurname2.
     * 
     * @param userSurname2
     *            Valor de userSurname2.
     */
    public void setUserSurname2(
	String userSurname2) {
	this.userSurname2 = userSurname2;
    }

    /**
     * Retorna el valor de userSurname1.
     * 
     * @return Valor de userSurname1.
     */
    public String getUserSurname1() {
	return userSurname1;
    }

    /**
     * Establece el valor de userSurname1.
     * 
     * @param userSurname1
     *            Valor de userSurname1.
     */
    public void setUserSurname1(
	String userSurname1) {
	this.userSurname1 = userSurname1;
    }

    /**
     * Retorna el valor de userAppointment.
     * 
     * @return Valor de userAppointment.
     */
    public String getUserAppointment() {
	return userAppointment;
    }

    /**
     * Establece el valor de userAppointment.
     * 
     * @param userAppointment
     *            Valor de userAppointment.
     */
    public void setUserAppointment(
	String userAppointment) {
	this.userAppointment = userAppointment;
    }

    /**
     * Retorna el valor de userEmail.
     * 
     * @return Valor de userEmail.
     */
    public String getUserEmail() {
	return userEmail;
    }

    /**
     * Establece el valor de userEmail.
     * 
     * @param userEmail
     *            Valor de userEmail.
     */
    public void setUserEmail(
	String userEmail) {
	this.userEmail = userEmail;
    }

    /**
     * Retorna el valor de userTelephone.
     * 
     * @return Valor de userTelephone.
     */
    public String getUserTelephone() {
	return userTelephone;
    }

    /**
     * Establece el valor de userTelephone.
     * 
     * @param userTelephone
     *            Valor de userTelephone.
     */
    public void setUserTelephone(
	String userTelephone) {
	this.userTelephone = userTelephone;
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

    /**
     * Retorna el valor de userCreateData.
     * 
     * @return Valor de userCreateData.
     */
    public Date getUserCreateData() {
	return userCreateData;
    }

    /**
     * Establece el valor de userCreateData.
     * 
     * @param userCreateData
     *            Valor de userCreateData.
     */
    public void setUserCreateData(
	Date userCreateData) {
	this.userCreateData = userCreateData;
    }

    /**
     * Retorna el valor de userUpdateData.
     * 
     * @return Valor de userUpdateData.
     */
    public Date getUserUpdateData() {
	return userUpdateData;
    }

    /**
     * Establece el valor de userUpdateData.
     * 
     * @param userUpdateData
     *            Valor de userUpdateData.
     */
    public void setUserUpdateData(
	Date userUpdateData) {
	this.userUpdateData = userUpdateData;
    }

    /**
     * Retorna el valor de userPwdLastUpdts.
     * 
     * @return Valor de userPwdLastUpdts.
     */
    public long getUserPwdLastUpdts() {
	return userPwdLastUpdts;
    }

    /**
     * Establece el valor de userPwdLastUpdts.
     * 
     * @param userPwdLastUpdts
     *            Valor de userPwdLastUpdts.
     */
    public void setUserPwdLastUpdts(
	long userPwdLastUpdts) {
	this.userPwdLastUpdts = userPwdLastUpdts;
    }

    /**
     * Retorna el valor de userUpdateId.
     * 
     * @return Valor de userUpdateId.
     */
    public long getUserUpdateId() {
	return userUpdateId;
    }

    /**
     * Establece el valor de userUpdateId.
     * 
     * @param userUpdateId
     *            Valor de userUpdateId.
     */
    public void setUserUpdateId(
	long userUpdateId) {
	this.userUpdateId = userUpdateId;
    }

    /**
     * Retorna el valor de profiles.
     * 
     * @return Valor de profiles.
     */
    public List<Profile> getProfiles() {
	return profiles;
    }

    /**
     * Establece el valor de profiles.
     * 
     * @param profiles
     *            Valor de profiles.
     */
    public void setProfiles(
	List<Profile> profiles) {
	this.profiles = profiles;
    }

    /**
     * Retorna el valor de userAddress.
     * 
     * @return Valor de userAddress.
     */
    public String getUserAddress() {
	return userAddress;
    }

    /**
     * Establece el valor de userAddress.
     * 
     * @param userAddress
     *            Valor de userAddress.
     */
    public void setUserAddress(
	String userAddress) {
	this.userAddress = userAddress;
    }

    /**
     * Retorna el valor de userCity.
     * 
     * @return Valor de userCity.
     */
    public String getUserCity() {
	return userCity;
    }

    /**
     * Establece el valor de userCity.
     * 
     * @param userCity
     *            Valor de userCity.
     */
    public void setUserCity(
	String userCity) {
	this.userCity = userCity;
    }

    /**
     * Retorna el valor de userCountry.
     * 
     * @return Valor de userCountry.
     */
    public String getUserCountry() {
	return userCountry;
    }

    /**
     * Establece el valor de userCountry.
     * 
     * @param userCountry
     *            Valor de userCountry.
     */
    public void setUserCountry(
	String userCountry) {
	this.userCountry = userCountry;
    }

    /**
     * Retorna el valor de userZip.
     * 
     * @return Valor de userZip.
     */
    public String getUserZip() {
	return userZip;
    }

    /**
     * Establece el valor de userZip.
     * 
     * @param userZip
     *            Valor de userZip.
     */
    public void setUserZip(
	String userZip) {
	this.userZip = userZip;
    }

    /**
     * Retorna el valor de userFax.
     * 
     * @return Valor de userFax.
     */
    public String getUserFax() {
	return userFax;
    }

    /**
     * Establece el valor de userFax.
     * 
     * @param userFax
     *            Valor de userFax.
     */
    public void setUserFax(
	String userFax) {
	this.userFax = userFax;
    }

    /**
     * Retorna el valor de userConfig.
     * 
     * @return Valor de userConfig.
     */
    public byte[] getUserConfig() {
	return userConfig;
    }

    /**
     * Establece el valor de userConfig.
     * 
     * @param userConfig
     *            Valor de userConfig.
     */
    public void setUserConfig(
	byte[] userConfig) {
	this.userConfig = userConfig;
    }

    /**
     * Retorna el valor de userProfileType.
     * 
     * @return Valor de userProfileType.
     */
    public int getUserProfileType() {
	return userProfileType;
    }

    /**
     * Establece el valor de userProfileType.
     * 
     * @param userProfileType
     *            Valor de userProfileType.
     */
    public void setUserProfileType(
	int userProfileType) {
	this.userProfileType = userProfileType;
    }

    /**
     * Retorna el valor de userSurname.
     * 
     * @return Valor de userSurname.
     */
    public String getUserSurname() {
	String userSurname = "";
	if (this.userSurname1 != null) {
	    userSurname += this.userSurname1;
	    if (this.userSurname2 != null) {
		userSurname += " " +
		    userSurname2;
	    }
	    userSurname = userSurname.trim();
	}
	return userSurname;
    }

    /**
     * Retorna el valor de userPreferentOffice.
     * 
     * @return Valor de userPreferentOffice.
     */
    public Office getUserPreferentOffice() {
	return userPreferentOffice;
    }

    /**
     * Establece el valor de userPreferentOffice.
     * 
     * @param userPreferentOffice
     *            Valor de userPreferentOffice.
     */
    public void setUserPreferentOffice(
	Office userPreferentOffice) {
	this.userPreferentOffice = userPreferentOffice;
    }
    /**
     * Retorna el valor de changeDepartament.
     * 
     * @return Valor de changeDepartament.
     */
    public boolean getChangeDepartament() {
        return changeDepartament;
    }
    /**
     * Establece el valor de changeDepartament.
     * 
     * @param changeDepartament
     *            Valor de changeDepartament.
     */
    public void setChangeDepartament(
        boolean changeDepartament) {
        this.changeDepartament = changeDepartament;
    }

    /**
     * Retorna el valor de gottaOffice.
     * 
     * @return Valor de gottaOffice.
     */
    public String getGottaOffice() {
        return gottaOffice;
    }
    /**
     * Establece el valor de gottaOffice.
     * 
     * @param gottaOffice
     *            Valor de gottaOffice.
     */
    public void setGottaOffice(
        String gottaOffice) {
        this.gottaOffice = gottaOffice;
    }
    /**
     * Retorna el valor de gottaUnit.
     * 
     * @return Valor de gottaUnit.
     */
    public String getGottaUnit() {
        return gottaUnit;
    }
    /**
     * Establece el valor de gottaUnit.
     * 
     * @param gottaUnit
     *            Valor de gottaUnit.
     */
    public void setGottaUnit(
        String gottaUnit) {
        this.gottaUnit = gottaUnit;
    }
    /**
     * Retorna el valor de gottaDepartament.
     * 
     * @return Valor de gottaDepartament.
     */
    public String getGottaDepartament() {
        return gottaDepartament;
    }
    /**
     * Establece el valor de gottaDepartament.
     * 
     * @param gottaDepartament
     *            Valor de gottaDepartament.
     */
    public void setGottaDepartament(
        String gottaDepartament) {
        this.gottaDepartament = gottaDepartament;
    }
    /**
     * Retorna el valor de gottaCapicom.
     * 
     * @return Valor de gottaCapicom.
     */
    public int getGottaCapicom() {
        return gottaCapicom;
    }
    /**
     * Establece el valor de gottaCapicom.
     * 
     * @param gottaCapicom
     *            Valor de gottaCapicom.
     */
    public void setGottaCapicom(
        int gottaCapicom) {
        this.gottaCapicom = gottaCapicom;
    }
    /**
     * Retorna el valor de gottaLanguage.
     * 
     * @return Valor de gottaLanguage.
     */
    public String getGottaLanguage() {
        return gottaLanguage;
    }
    /**
     * Establece el valor de gottaLanguage.
     * 
     * @param gottaLanguage
     *            Valor de gottaLanguage.
     */
    public void setGottaLanguage(
        String gottaLanguage) {
        this.gottaLanguage = gottaLanguage;
    }
    /**
     * Retorna el valor de gottaAttachedSign.
     * 
     * @return Valor de gottaAttachedSign.
     */
    public int getGottaAttachedSign() {
        return gottaAttachedSign;
    }
    /**
     * Establece el valor de gottaAttachedSign.
     * 
     * @param gottaAttachedSign
     *            Valor de gottaAttachedSign.
     */
    public void setGottaAttachedSign(
        int gottaAttachedSign) {
        this.gottaAttachedSign = gottaAttachedSign;
    }
    /**
     * Retorna el valor de gottaRequiredSign.
     * 
     * @return Valor de gottaRequiredSign.
     */
    public int getGottaRequiredSign() {
        return gottaRequiredSign;
    }
    /**
     * Establece el valor de gottaRequiredSign.
     * 
     * @param gottaRequiredSign
     *            Valor de gottaRequiredSign.
     */
    public void setGottaRequiredSign(
        int gottaRequiredSign) {
        this.gottaRequiredSign = gottaRequiredSign;
    }
    
    /**
     * Establece el valor de userSurname.
     * 
     * @param userSurname
     *            Valor de userSurname.
     */
    public void setUserSurname(
        String userSurname) {
        this.userSurname = userSurname;
    }

    /**
     * Retorna el valor de completeusername.
     * 
     * @return Valor de completeusername.
     */
    public String getCompleteUserName() {
	String name = "";
	if (this.userSurname1 != null) {
	    name = this.userName +
		" " + this.userSurname1;
	    if (this.userSurname2 != null) {
		name += " " +
		    userSurname2;
	    }
	    name = name.trim();
	}
	return name;
    }

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userUser=" + userUser + ", userPassword=" + userPassword
				+ ", userRePassword=" + userRePassword + ", userDescription=" + userDescription + ", userDepartament="
				+ userDepartament + ", userName=" + userName + ", userSurname1=" + userSurname1 + ", userSurname2="
				+ userSurname2 + ", userSurname=" + userSurname + ", userAppointment=" + userAppointment
				+ ", userEmail=" + userEmail + ", userTelephone=" + userTelephone + ", userCreateData=" + userCreateData
				+ ", userUpdateData=" + userUpdateData + ", userPwdLastUpdts=" + userPwdLastUpdts + ", userUpdateId="
				+ userUpdateId + ", permissions=" + permissions + ", profiles=" + profiles + ", userAddress="
				+ userAddress + ", userCity=" + userCity + ", userCountry=" + userCountry + ", userZip=" + userZip
				+ ", userFax=" + userFax + ", userConfig=" + Arrays.toString(userConfig) + ", userProfileType="
				+ userProfileType + ", userPreferentOffice=" + userPreferentOffice + ", userGenericPermissionsList="
				+ userGenericPermissionsList + ", userGenericPermissions=" + userGenericPermissions
				+ ", changeDepartament=" + changeDepartament + ", gottaOffice=" + gottaOffice + ", gottaUnit="
				+ gottaUnit + ", gottaDepartament=" + gottaDepartament + ", gottaCapicom=" + gottaCapicom
				+ ", gottaLanguage=" + gottaLanguage + ", gottaAttachedSign=" + gottaAttachedSign
				+ ", gottaRequiredSign=" + gottaRequiredSign + "]";
	}
    
}
