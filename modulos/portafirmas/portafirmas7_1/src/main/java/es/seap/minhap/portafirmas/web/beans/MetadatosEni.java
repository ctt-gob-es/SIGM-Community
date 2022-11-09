/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.web.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;

//import es.seap.minhap.portafirmas.ws.inside.eni.documentoe.metadatos.TipoDocumental;
//import es.seap.minhap.portafirmas.ws.inside.eni.documentoe.metadatos.TipoEstadoElaboracion;

import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumental;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoEstadoElaboracion;



public class MetadatosEni {

	@XmlElement(name = "VersionNTI", required = true)
    protected String versionNTI;
    @XmlElement(name = "Identificador", required = true)
    protected String identificador;
    @XmlElement(name = "Organo", required = true)
    protected List<String> organo;
    @XmlElement(name = "FechaCaptura", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCaptura;
    @XmlElement(name = "OrigenCiudadanoAdministracion")
    protected boolean origenCiudadanoAdministracion;
    @XmlElement(name = "EstadoElaboracion", required = true)
    protected TipoEstadoElaboracion estadoElaboracion;
    @XmlElement(name = "TipoDocumental", required = true)
    @XmlSchemaType(name = "string")
    protected TipoDocumental tipoDocumental;
	/**
	 * @return the versionNTI
	 */
	public String getVersionNTI() {
		return versionNTI;
	}
	/**
	 * @param versionNTI the versionNTI to set
	 */
	public void setVersionNTI(String versionNTI) {
		this.versionNTI = versionNTI;
	}
	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}
	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	/**
	 * @return the organo
	 */
	public List<String> getOrgano() {
		return organo;
	}
	/**
	 * @param organo the organo to set
	 */
	public void setOrgano(List<String> organo) {
		this.organo = organo;
	}
	/**
	 * @return the fechaCaptura
	 */
	public XMLGregorianCalendar getFechaCaptura() {
		return fechaCaptura;
	}
	/**
	 * @param fechaCaptura the fechaCaptura to set
	 */
	public void setFechaCaptura(XMLGregorianCalendar fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}
	/**
	 * @return the origenCiudadanoAdministracion
	 */
	public boolean isOrigenCiudadanoAdministracion() {
		return origenCiudadanoAdministracion;
	}
	/**
	 * @param origenCiudadanoAdministracion the origenCiudadanoAdministracion to set
	 */
	public void setOrigenCiudadanoAdministracion(
			boolean origenCiudadanoAdministracion) {
		this.origenCiudadanoAdministracion = origenCiudadanoAdministracion;
	}
	/**
	 * @return the estadoElaboracion
	 */
	public TipoEstadoElaboracion getEstadoElaboracion() {
		return estadoElaboracion;
	}
	/**
	 * @param estadoElaboracion the estadoElaboracion to set
	 */
	public void setEstadoElaboracion(TipoEstadoElaboracion estadoElaboracion) {
		this.estadoElaboracion = estadoElaboracion;
	}
	/**
	 * @return the tipoDocumental
	 */
	public TipoDocumental getTipoDocumental() {
		return tipoDocumental;
	}
	/**
	 * @param tipoDocumental the tipoDocumental to set
	 */
	public void setTipoDocumental(TipoDocumental tipoDocumental) {
		this.tipoDocumental = tipoDocumental;
	}
	public MetadatosEni(String versionNTI, String identificador,
			List<String> organo, XMLGregorianCalendar fechaCaptura,
			boolean origenCiudadanoAdministracion,
			TipoEstadoElaboracion estadoElaboracion,
			TipoDocumental tipoDocumental) {
		super();
		this.versionNTI = versionNTI;
		this.identificador = identificador;
		this.organo = organo;
		this.fechaCaptura = fechaCaptura;
		this.origenCiudadanoAdministracion = origenCiudadanoAdministracion;
		this.estadoElaboracion = estadoElaboracion;
		this.tipoDocumental = tipoDocumental;
	}
	
	
	
	
}
