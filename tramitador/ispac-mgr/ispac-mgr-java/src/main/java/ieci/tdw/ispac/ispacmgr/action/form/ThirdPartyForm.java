package ieci.tdw.ispac.ispacmgr.action.form;

public class ThirdPartyForm extends EntityForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String postalAddressId;
	private String electronicAddressId;
	private String nif;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String dir3;
	
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getElectronicAddressId() {
		return electronicAddressId;
	}
	public void setElectronicAddressId(String electronicAddressId) {
		this.electronicAddressId = electronicAddressId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPostalAddressId() {
		return postalAddressId;
	}
	public void setPostalAddressId(String postalAddressId) {
		this.postalAddressId = postalAddressId;
	}
	public String getDir3() {
		return dir3;
	}
	public void setDir3(String dir3) {
		this.dir3 = dir3;
	}
}
