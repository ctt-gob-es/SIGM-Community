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

import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumentoConversionInside;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumentoConversionInside.Csv;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumentoConversionInside.MetadatosEni;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.MetadatoAdicional;

public class DocumentEni {

    protected byte[] contenido;
    protected String contenidoId;
    protected String idDocument;
    protected boolean firmadoConCertificado;
    protected String fcaptura;
    protected TipoDocumentoConversionInside.MetadatosEni metadatosEni;
    protected TipoDocumentoConversionInside.Csv csv;
    protected List<String> organoList; 
    
    protected List<MetadatoAdicional> metadatosAdicionales;
    
    protected List<String> metadatosObligatorios; 
    
	/**
	 * @return the contenido
	 */
	public byte[] getContenido() {
		return contenido;
	}
	/**
	 * @param contenido the contenido to set
	 */
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
	/**
	 * @return the contenidoId
	 */
	public String getContenidoId() {
		return contenidoId;
	}
	/**
	 * @param contenidoId the contenidoId to set
	 */
	public void setContenidoId(String contenidoId) {
		this.contenidoId = contenidoId;
	}
	/**
	 * @return the firmadoConCertificado
	 */
	public boolean isFirmadoConCertificado() {
		return firmadoConCertificado;
	}
	/**
	 * @param firmadoConCertificado the firmadoConCertificado to set
	 */
	public void setFirmadoConCertificado(boolean firmadoConCertificado) {
		this.firmadoConCertificado = firmadoConCertificado;
	}
	/**
	 * @return the metadatosEni
	 */
	public TipoDocumentoConversionInside.MetadatosEni getMetadatosEni() {
		return metadatosEni;
	}
	/**
	 * @param metadatosEni the metadatosEni to set
	 */
	public void setMetadatosEni(
			TipoDocumentoConversionInside.MetadatosEni metadatosEni) {
		this.metadatosEni = metadatosEni;
	}
	/**
	 * @return the csv
	 */
	public TipoDocumentoConversionInside.Csv getCsv() {
		return csv;
	}
	/**
	 * @param csv the csv to set
	 */
	public void setCsv(TipoDocumentoConversionInside.Csv csv) {
		this.csv = csv;
	}
	public DocumentEni(byte[] contenido, String contenidoId,
			boolean firmadoConCertificado, MetadatosEni metadatosEni, Csv csv) {
		super();
		this.contenido = contenido;
		this.contenidoId = contenidoId;
		this.firmadoConCertificado = firmadoConCertificado;
		this.metadatosEni = metadatosEni;
		this.csv = csv;
	}
	public DocumentEni() {
		super();
		metadatosEni = new MetadatosEni();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the idDocument
	 */
	public String getIdDocument() {
		return idDocument;
	}
	/**
	 * @param idDocument the idDocument to set
	 */
	public void setIdDocument(String idDocument) {
		this.idDocument = idDocument;
	}
	/**
	 * @return the fechaCaptura
	 */
	public String getFcaptura() {
		return fcaptura;
	}
	/**
	 * @param fechaCaptura the fechaCaptura to set
	 */
	public void setFcaptura(String fcaptura) {
		this.fcaptura = fcaptura;
	}
	/**
	 * @return the organoList
	 */
	public List<String> getOrganoList() {
		return organoList;
	}
	/**
	 * @param organoList the organoList to set
	 */
	public void setOrganoList(List<String> organoList) {
		this.organoList = organoList;
	}
	
	public List<MetadatoAdicional> getMetadatosAdicionales() {
		return metadatosAdicionales;
	}
	public void setMetadatosAdicionales(
			List<MetadatoAdicional> metadatosAdicionales) {
		this.metadatosAdicionales = metadatosAdicionales;
	}
	/**
	 * @return the metadatosObligatorios
	 */
	public List<String> getMetadatosObligatorios() {
		return metadatosObligatorios;
	}
	/**
	 * @param metadatosObligatorios the metadatosObligatorios to set
	 */
	public void setMetadatosObligatorios(List<String> metadatosObligatorios) {
		this.metadatosObligatorios = metadatosObligatorios;
	}
	

	
}
