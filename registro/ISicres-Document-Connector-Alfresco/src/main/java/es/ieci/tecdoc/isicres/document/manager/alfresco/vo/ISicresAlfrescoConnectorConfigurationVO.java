package es.ieci.tecdoc.isicres.document.manager.alfresco.vo;

import com.thoughtworks.xstream.XStream;

import es.ieci.tecdoc.isicres.document.connector.vo.ISicresBasicDatosEspecificosValueVO;
import es.ieci.tecdoc.isicres.document.connector.vo.IsicresAbstractConnectorConfigurationVO;

/**
 * 
 * @author Iecisa 
 * @version $Revision$ 
 *
 */
public class ISicresAlfrescoConnectorConfigurationVO extends IsicresAbstractConnectorConfigurationVO{

	
	private String usuario;
	private String pass;
	private String ip;
	private String puerto;	
	
	// [Ruben CMIS] Parámetros para CMIS
	private String version;	
	private String protocolo;	
	
	
	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the puerto
	 */
	public String getPuerto() {
		return puerto;
	}

	/**
	 * @param puerto the puerto to set
	 */
	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	// [Ruben CMIS] Metodos integracion CMIS 
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
	// FIN Metodos integracion CMIS 
	
	public void fromXml(String xml) {
		XStream xstream = configureXstream();
		ISicresAlfrescoConnectorConfigurationVO objectFromXml = (ISicresAlfrescoConnectorConfigurationVO)xstream.fromXML(xml);
		this.ip 		= objectFromXml.getIp();
		this.pass 		= objectFromXml.getPass();
		this.puerto 	= objectFromXml.getPuerto();
		this.usuario 	= objectFromXml.getUsuario();
		
		// [Ruben CMIS] Adaptación a CMIS
		this.version	= objectFromXml.getVersion();
		this.protocolo	= objectFromXml.getProtocolo();
	}

	public String toXml() {
		String result="";
		XStream xstream = configureXstream();
		xstream.alias("Configuracion", this.getClass());
		xstream.alias("Connector", ISicresBasicDatosEspecificosValueVO.class);
		
		result=xstream.toXML(this);
		return result;
	}
	
	protected XStream configureXstream(){
		XStream result = new XStream();
		result.alias("Configuracion", ISicresAlfrescoConnectorConfigurationVO.class);
		result.alias("Connector", ISicresBasicDatosEspecificosValueVO.class);
		return result;	
	}

}
