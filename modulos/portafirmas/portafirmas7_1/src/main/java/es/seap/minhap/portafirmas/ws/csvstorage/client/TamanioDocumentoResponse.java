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


package es.seap.minhap.portafirmas.ws.csvstorage.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tamanioDocumentoResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="tamanioDocumentoResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0}response">
 *       &lt;sequence>
 *         &lt;element name="tamanioDocumento" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tamanioDocumentoResponse", propOrder = {
    "tamanioDocumento"
})
public class TamanioDocumentoResponse
    extends Response
{

    protected Long tamanioDocumento;

    /**
     * Obtiene el valor de la propiedad tamanioDocumento.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTamanioDocumento() {
        return tamanioDocumento;
    }

    /**
     * Define el valor de la propiedad tamanioDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTamanioDocumento(Long value) {
        this.tamanioDocumento = value;
    }

}
