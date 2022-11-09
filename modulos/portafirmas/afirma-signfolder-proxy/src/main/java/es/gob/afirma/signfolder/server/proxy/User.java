package es.gob.afirma.signfolder.server.proxy;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase que representa el usuario del portafirmas.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = { "name",
		"surname", "secondSurname", "LDAPUser", "ID", "position", "headquarter", "profiles", "dataContact",
		"attachSignature", "attachReport", "pageSize", "applyAppFilter", "showPreviousSigner" })
public class User {

	/**
	 * Atributo que representa el nombre de usuario.
	 */
	private String name;

	/**
	 * Atributo que representa el primer apellido.
	 */
	private String surname;

	/**
	 * Atributo que representa el segundo apellido.
	 */
	private String secondSurname;

	/**
	 * Atributo que representa el LDAP del usuario.
	 */
	private String LDAPUser;

	/**
	 * Atributo que representa el identificador de usuario.
	 */
	private String ID;

	/**
	 * Atributo que representa la posición actual dentro de la empresa.
	 */
	private String position;

	/**
	 * Atributo que representa el departamento.
	 */
	private String headquarter;

	/**
	 * Atributo que representa la lista de perfiles activos.
	 */
	private List<UserProfile> profiles;

	/**
	 * Atributo que representa la lista de contactos de usuarios.
	 */
	private List<ContactData> dataContact;

	/**
	 * Bandera que indica si la firma debe ser adjunta.
	 */
	private boolean attachSignature;

	/**
	 * Bandera que indica si el reporte debe ser adjunto.
	 */
	private boolean attachReport;

	/**
	 * Atributo que representa el tamaño de página.
	 */
	private int pageSize;

	/**
	 * Bandera que indica si los filtros deben ser aplicados en la aplicación.
	 */
	private boolean applyAppFilter;

	/**
	 * Bandera que indica si es necesario mostrar los firmantes anteriores.
	 */
	private boolean showPreviousSigner;

	/**
	 * Get method for the <i>name</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set method for the <i>name</i> attribute.
	 * 
	 * @param name
	 *            new value of the attribute.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get method for the <i>surname</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Set method for the <i>surname</i> attribute.
	 * 
	 * @param surname
	 *            new value of the attribute.
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * Get method for the <i>secondSurname</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getSecondSurname() {
		return secondSurname;
	}

	/**
	 * Set method for the <i>secondSurname</i> attribute.
	 * 
	 * @param secondSurname
	 *            new value of the attribute.
	 */
	public void setSecondSurname(String secondSurname) {
		this.secondSurname = secondSurname;
	}

	/**
	 * Get method for the <i>LDAPUser</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getLDAPUser() {
		return LDAPUser;
	}

	/**
	 * Set method for the <i>LDAPUser</i> attribute.
	 * 
	 * @param LDAPUser
	 *            new value of the attribute.
	 */
	public void setLDAPUser(String LDAPUser) {
		this.LDAPUser = LDAPUser;
	}

	/**
	 * Get method for the <i>ID</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Set method for the <i>ID</i> attribute.
	 * 
	 * @param ID
	 *            new value of the attribute.
	 */
	public void setID(String ID) {
		this.ID = ID;
	}

	/**
	 * Get method for the <i>position</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * Set method for the <i>position</i> attribute.
	 * 
	 * @param position
	 *            new value of the attribute.
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * Get method for the <i>headquarter</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getHeadquarter() {
		return headquarter;
	}

	/**
	 * Set method for the <i>headquarter</i> attribute.
	 * 
	 * @param headquarter
	 *            new value of the attribute.
	 */
	public void setHeadquarter(String headquarter) {
		this.headquarter = headquarter;
	}

	/**
	 * Get method for the <i>profiles</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public List<UserProfile> getProfiles() {
		return profiles;
	}

	/**
	 * Set method for the <i>profiles</i> attribute.
	 * 
	 * @param profiles
	 *            new value of the attribute.
	 */
	public void setProfiles(List<UserProfile> profiles) {
		this.profiles = profiles;
	}

	/**
	 * Get method for the <i>dataContact</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public List<ContactData> getDataContact() {
		return dataContact;
	}

	/**
	 * Set method for the <i>dataContact</i> attribute.
	 * 
	 * @param dataContact
	 *            new value of the attribute.
	 */
	public void setDataContact(List<ContactData> dataContact) {
		this.dataContact = dataContact;
	}

	/**
	 * Get method for the <i>attachSignature</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public boolean isAttachSignature() {
		return attachSignature;
	}

	/**
	 * Set method for the <i>attachSignature</i> attribute.
	 * 
	 * @param attachSignature
	 *            new value of the attribute.
	 */
	public void setAttachSignature(boolean attachSignature) {
		this.attachSignature = attachSignature;
	}

	/**
	 * Get method for the <i>attachReport</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public boolean isAttachReport() {
		return attachReport;
	}

	/**
	 * Set method for the <i>attachReport</i> attribute.
	 * 
	 * @param attachReport
	 *            new value of the attribute.
	 */
	public void setAttachReport(boolean attachReport) {
		this.attachReport = attachReport;
	}

	/**
	 * Get method for the <i>pageSize</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Set method for the <i>pageSize</i> attribute.
	 * 
	 * @param pageSize
	 *            new value of the attribute.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Get method for the <i>applyAppFilter</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public boolean isApplyAppFilter() {
		return applyAppFilter;
	}

	/**
	 * Set method for the <i>applyAppFilter</i> attribute.
	 * 
	 * @param applyAppFilter
	 *            new value of the attribute.
	 */
	public void setApplyAppFilter(boolean applyAppFilter) {
		this.applyAppFilter = applyAppFilter;
	}

	/**
	 * Get method for the <i>showPreviousSigner</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public boolean isShowPreviousSigner() {
		return showPreviousSigner;
	}

	/**
	 * Set method for the <i>showPreviousSigner</i> attribute.
	 * 
	 * @param showPreviousSigner
	 *            new value of the attribute.
	 */
	public void setShowPreviousSigner(boolean showPreviousSigner) {
		this.showPreviousSigner = showPreviousSigner;
	}

}
