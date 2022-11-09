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


package es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para convertirExpedienteAEniConMAdicionalesAutocontenidoResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="convertirExpedienteAEniConMAdicionalesAutocontenidoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="expediente" type="{https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/file}ExpedienteEniFileInsideConMAdicionales" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "convertirExpedienteAEniConMAdicionalesAutocontenidoResponse", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", propOrder = {
    "expediente"
})
public class ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse {

    protected ExpedienteEniFileInsideConMAdicionales expediente;

    /**
     * Obtiene el valor de la propiedad expediente.
     * 
     * @return
     *     possible object is
     *     {@link ExpedienteEniFileInsideConMAdicionales }
     *     
     */
    public ExpedienteEniFileInsideConMAdicionales getExpediente() {
        return expediente;
    }

    /**
     * Define el valor de la propiedad expediente.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpedienteEniFileInsideConMAdicionales }
     *     
     */
    public void setExpediente(ExpedienteEniFileInsideConMAdicionales value) {
        this.expediente = value;
    }

}
