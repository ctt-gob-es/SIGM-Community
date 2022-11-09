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


package es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigo_sistema_gestor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idSolicitud" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="documento" type="{http://ip2.docelweb.wsInterfazGenerica/types/}TODocumento"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "codigoSistemaGestor",
    "idSolicitud",
    "documento"
})
@XmlRootElement(name = "registrarDocumentoElement")
public class RegistrarDocumentoElement {

    @XmlElement(name = "codigo_sistema_gestor", required = true, nillable = true)
    protected String codigoSistemaGestor;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long idSolicitud;
    @XmlElement(required = true, nillable = true)
    protected TODocumento documento;

    /**
     * Gets the value of the codigoSistemaGestor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoSistemaGestor() {
        return codigoSistemaGestor;
    }

    /**
     * Sets the value of the codigoSistemaGestor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoSistemaGestor(String value) {
        this.codigoSistemaGestor = value;
    }

    /**
     * Gets the value of the idSolicitud property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdSolicitud() {
        return idSolicitud;
    }

    /**
     * Sets the value of the idSolicitud property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdSolicitud(Long value) {
        this.idSolicitud = value;
    }

    /**
     * Gets the value of the documento property.
     * 
     * @return
     *     possible object is
     *     {@link TODocumento }
     *     
     */
    public TODocumento getDocumento() {
        return documento;
    }

    /**
     * Sets the value of the documento property.
     * 
     * @param value
     *     allowed object is
     *     {@link TODocumento }
     *     
     */
    public void setDocumento(TODocumento value) {
        this.documento = value;
    }

}
