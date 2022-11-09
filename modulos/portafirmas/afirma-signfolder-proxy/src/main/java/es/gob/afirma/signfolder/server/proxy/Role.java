/**
 * 
 */
package es.gob.afirma.signfolder.server.proxy;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase que representa el rol de usuario del portafirmas.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = { "name",
		"surname", "secondSurname", "LDAPUser", "ID", "position", "headquarter", "profiles", "dataContact",
		"attachSignature", "attachReport", "pageSize", "applyAppFilter", "showPreviousSigner", "verifierIdentifier",
		"verifierName", "status", "sentReceived", "type", "senderReceiver", "initDate", "authorization", "endDate" })
public class Role {

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
	 * Atributo que representa el LDAP de usuario.
	 */
	private String LDAPUser;

	/**
	 * Atributo que representa el identificador (DNI) del usuario.
	 */
	private String ID;

	/**
	 * Atributo que representa la posición actual en la empresa.
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
	 * Atributo que representa la lista de datos de contacto.
	 */
	private List<ContactData> dataContact;

	/**
	 * bandera que indica si la firma debe ser adjunta.
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
	 * Atributo que representa el identificador del validador.
	 */
	private String verifierIdentifier;

	/**
	 * Atributo que representa el nombre del validador.
	 */
	private String verifierName;

	/**
	 * Atributo que representa el estado de la autorización.
	 */
	private String status;

	/**
	 * Atributo que representa si la autorización ha sido enviada o recibida.
	 */
	private String sentReceived;

	/**
	 * Atributo que representa el tipo de autorización.
	 */
	private AuthorizedType type;

	/**
	 * Atributo que representa el nombre del remitente o destinatario de la autorización.
	 */
	private String senderReceiver;

	/**
	 * Atributo que representa la fecha de inicio de la autorización.
	 */
	private Date initDate;

	/**
	 * Atributo que representa la fecha de creación de la autorización.
	 */
	private Date authorization;

	/**
	 * Atributo que representa la fecha de revocación de la autorización.
	 */
	private Date endDate;

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

	/**
	 * Get method for the <i>verifierIdentifier</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getVerifierIdentifier() {
		return verifierIdentifier;
	}

	/**
	 * Set method for the <i>verifierIdentifier</i> attribute.
	 * 
	 * @param verifierIdentifier
	 *            New value of the attribute.
	 */
	public void setVerifierIdentifier(String verifierIdentifier) {
		this.verifierIdentifier = verifierIdentifier;
	}

	/**
	 * Get method for the <i>verifierName</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getVerifierName() {
		return verifierName;
	}

	/**
	 * Set method for the <i>verifierName</i> attribute.
	 * 
	 * @param verifierName
	 *            New value of the attribute.
	 */
	public void setVerifierName(String verifierName) {
		this.verifierName = verifierName;
	}

	/**
	 * Get method for the <i>status</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Set method for the <i>status</i> attribute.
	 * 
	 * @param status
	 *            New value of the attribute.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Get method for the <i>sentReceived</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getSentReceived() {
		return sentReceived;
	}

	/**
	 * Set method for the <i>sentReceived</i> attribute.
	 * 
	 * @param sentReceived
	 *            New value of the attribute.
	 */
	public void setSentReceived(String sentReceived) {
		this.sentReceived = sentReceived;
	}

	/**
	 * Get method for the <i>type</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public AuthorizedType getType() {
		return type;
	}

	/**
	 * Set method for the <i>type</i> attribute.
	 * 
	 * @param type
	 *            New value of the attribute.
	 */
	public void setType(AuthorizedType type) {
		this.type = type;
	}

	/**
	 * Get method for the <i>senderReceiver</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getSenderReceiver() {
		return senderReceiver;
	}

	/**
	 * Set method for the <i>senderReceiver</i> attribute.
	 * 
	 * @param senderReceiver
	 *            New value of the attribute.
	 */
	public void setSenderReceiver(String senderReceiver) {
		this.senderReceiver = senderReceiver;
	}

	/**
	 * Get method for the <i>initDate</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public Date getInitDate() {
		return initDate;
	}

	/**
	 * Set method for the <i>initDate</i> attribute.
	 * 
	 * @param initDate
	 *            New value of the attribute.
	 */
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	/**
	 * Get method for the <i>authorization</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public Date getAuthorization() {
		return authorization;
	}

	/**
	 * Set method for the <i>authorization</i> attribute.
	 * 
	 * @param authorization
	 *            New value of the attribute.
	 */
	public void setAuthorization(Date authorization) {
		this.authorization = authorization;
	}

	/**
	 * Get method for the <i>endDate</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Set method for the <i>endDate</i> attribute.
	 * 
	 * @param endDate
	 *            New value of the attribute.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
