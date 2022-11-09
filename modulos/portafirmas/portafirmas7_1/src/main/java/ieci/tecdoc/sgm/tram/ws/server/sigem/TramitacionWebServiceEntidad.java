package ieci.tecdoc.sgm.tram.ws.server.sigem;

//id;dominio;ip;puerto;nombre
public class TramitacionWebServiceEntidad implements Comparable<TramitacionWebServiceEntidad>{
	
	private String id="";
	private String wsdl="";
	private String ip="";
	private String puerto="";
	private String nombre="";
	
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
	public String getWsdl() {
		return wsdl;
	}
	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPuerto() {
		return puerto;
	}
	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}
	@Override
	public int compareTo(TramitacionWebServiceEntidad otra) {
		return Integer.valueOf(this.getId()).compareTo(Integer.valueOf(otra.getId()));
	}
	
	
}
