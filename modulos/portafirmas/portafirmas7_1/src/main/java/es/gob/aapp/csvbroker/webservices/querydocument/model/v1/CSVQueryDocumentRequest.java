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


package es.gob.aapp.csvbroker.webservices.querydocument.model.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para CSVQueryDocumentRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="CSVQueryDocumentRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="csv" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir3" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CSVQueryDocumentRequest", propOrder = {
    "csv",
    "dir3"
})
public class CSVQueryDocumentRequest {

    @XmlElement(required = true)
    protected String csv;
    @XmlElement(required = true)
    protected String dir3;

    /**
     * Obtiene el valor de la propiedad csv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsv() {
        return csv;
    }

    /**
     * Define el valor de la propiedad csv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsv(String value) {
        this.csv = value;
    }

    /**
     * Obtiene el valor de la propiedad dir3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir3() {
        return dir3;
    }

    /**
     * Define el valor de la propiedad dir3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir3(String value) {
        this.dir3 = value;
    }

}
