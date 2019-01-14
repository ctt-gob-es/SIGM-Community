package es.dipucr.sigemaytos.domain.beans;

import java.util.ArrayList;
import java.util.List;

import es.dipucr.sigemaytos.config.ConfigProperties;
import es.dipucr.sigemaytos.config.ConstantesConfiguracion;
import es.dipucr.sigemaytos.utils.StringUtils;

public class Entidad implements Comparable<Entidad> {

	public static final String HOST_LABEL = "{HOST}";
	public static final String ENTITY_LABEL = "{ENTIDAD}";
	
	public static final String CONFIG_SEPARATOR = ";";
	public static final String EMAIL_SEPARATOR = ",";
	
	protected String id;
	protected String nombre;
	protected String domain;
	protected String bbddHost;
	protected String bbddPort;
	protected List<String> listContactos;
	
	protected String rutaPortal;
	protected String rutaRegistro;
	protected String rutaRegistroMSSSI;
	protected String rutaTramitador;
	
	public Entidad(String lineaConfig){
		
		String[] arrConfig = lineaConfig.split(CONFIG_SEPARATOR);
		this.id = arrConfig[0];
		this.domain = arrConfig[1];
		this.bbddHost = arrConfig[2];
		this.bbddPort = arrConfig[3];
		this.nombre = arrConfig[4];
		
		listContactos = new ArrayList<String>();
		String contactos = arrConfig[5];
		if (!StringUtils.esNuloOVacio(contactos)){
			String[] arrContactos = contactos.split(EMAIL_SEPARATOR);
			for (String contacto : arrContactos){
				listContactos.add(contacto);
			}
		}
		
		getRutas();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getBbddHost() {
		return bbddHost;
	}

	public void setBbddHost(String bbddHost) {
		this.bbddHost = bbddHost;
	}

	public String getBbddPort() {
		return bbddPort;
	}

	public void setBbddPort(String bbddPort) {
		this.bbddPort = bbddPort;
	}

	public List<String> getListContactos() {
		return listContactos;
	}

	public void setListContactos(List<String> listContactos) {
		this.listContactos = listContactos;
	}

	public String getRutaPortal() {
		return rutaPortal;
	}

	public void setRutaPortal(String rutaPortal) {
		this.rutaPortal = rutaPortal;
	}

	public String getRutaRegistro() {
		return rutaRegistro;
	}

	public void setRutaRegistro(String rutaRegistro) {
		this.rutaRegistro = rutaRegistro;
	}
	
	public String getRutaRegistroMSSSI() {
		return rutaRegistroMSSSI;
	}

	public void setRutaRegistroMSSSI(String rutaRegistroMSSSI) {
		this.rutaRegistroMSSSI = rutaRegistroMSSSI;
	}

	public String getRutaTramitador() {
		return rutaTramitador;
	}

	public void setRutaTramitador(String rutaTramitador) {
		this.rutaTramitador = rutaTramitador;
	}

	public int compareTo(Entidad otra) {
		
		return this.nombre.compareTo(otra.getNombre());
	}

	public void getRutas() {
		ConfigProperties properties = ConfigProperties.getInstance();
		rutaPortal = getRuta(properties.getProperty(ConstantesConfiguracion.SIGEM.PORTAL));
		rutaRegistro = getRuta(properties.getProperty(ConstantesConfiguracion.SIGEM.REGISTRO));
		rutaRegistroMSSSI = getRuta(properties.getProperty(ConstantesConfiguracion.SIGEM.REGISTROMSSSI));
		rutaTramitador = getRuta(properties.getProperty(ConstantesConfiguracion.SIGEM.TRAMITADOR));
	}
	
	private String getRuta(String ruta){
		
		ruta = ruta.replace(HOST_LABEL, domain);
		ruta = ruta.replace(ENTITY_LABEL, id);
		return ruta;
	}
}

