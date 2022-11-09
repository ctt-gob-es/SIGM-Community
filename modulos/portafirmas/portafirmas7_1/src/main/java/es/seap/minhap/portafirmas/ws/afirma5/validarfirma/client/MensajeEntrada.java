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


package es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client;

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
 *         &lt;element name="peticion">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://afirmaws/ws/firma}CadenaSinEspacios">
 *               &lt;enumeration value="ValidarFirma"/>
 *               &lt;enumeration value="FirmaServidor"/>
 *               &lt;enumeration value="FirmaServidorCoSign"/>
 *               &lt;enumeration value="FirmaServidorCounterSign"/>
 *               &lt;enumeration value="FirmaUsuario3FasesF1"/>
 *               &lt;enumeration value="FirmaUsuario3FasesF1CoSign"/>
 *               &lt;enumeration value="FirmaUsuario3FasesF1CounterSign"/>
 *               &lt;enumeration value="FirmaUsuario3FasesF3"/>
 *               &lt;enumeration value="FirmaUsuario2FasesF2"/>
 *               &lt;enumeration value="ValidarFirmaBloquesCompleto"/>
 *               &lt;enumeration value="validarFirmaBloquesDocumento"/>
 *               &lt;enumeration value="ValidarFirmaBloquesDocumento"/>
 *               &lt;enumeration value="FirmaUsuarioBloquesF1"/>
 *               &lt;enumeration value="FirmaUsuarioBloquesF1CoSign"/>
 *               &lt;enumeration value="FirmaUsuarioBloquesF1CounterSign"/>
 *               &lt;enumeration value="FirmaUsuarioBloquesF3"/>
 *               &lt;enumeration value="ObtenerIdDocumentosBloqueFirmas"/>
 *               &lt;enumeration value="ObtenerIdDocumentosBloqueFirmasBackwards"/>
 *               &lt;enumeration value="ObtenerInformacionBloqueFirmas"/>
 *               &lt;enumeration value="ObtenerInformacionBloqueFirmasBackwards"/>
 *               &lt;enumeration value="ObtenerInfoCompletaBloqueFirmas"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="versionMsg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parametros" type="{http://afirmaws/ws/firma}parametros"/>
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
    "peticion",
    "versionMsg",
    "parametros"
})
@XmlRootElement(name = "mensajeEntrada")
public class MensajeEntrada {

    @XmlElement(required = true)
    protected String peticion;
    @XmlElement(required = true)
    protected String versionMsg;
    @XmlElement(required = true)
    protected Parametros parametros;

    /**
     * Gets the value of the peticion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeticion() {
        return peticion;
    }

    /**
     * Sets the value of the peticion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeticion(String value) {
        this.peticion = value;
    }

    /**
     * Gets the value of the versionMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionMsg() {
        return versionMsg;
    }

    /**
     * Sets the value of the versionMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionMsg(String value) {
        this.versionMsg = value;
    }

    /**
     * Gets the value of the parametros property.
     * 
     * @return
     *     possible object is
     *     {@link Parametros }
     *     
     */
    public Parametros getParametros() {
        return parametros;
    }

    /**
     * Sets the value of the parametros property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parametros }
     *     
     */
    public void setParametros(Parametros value) {
        this.parametros = value;
    }

}
