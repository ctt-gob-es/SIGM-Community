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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Clase Java para TipoIndice complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoIndice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IndiceContenido" type="{http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/indice-e/contenido}TipoIndiceContenido"/>
 *         &lt;element ref="{http://administracionelectronica.gob.es/ENI/XSD/v1.0/firma}firmas"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoIndice", namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/indice-e", propOrder = {
    "indiceContenido",
    "firmas"
})
public class TipoIndice {

    @XmlElement(name = "IndiceContenido", required = true)
    protected TipoIndiceContenido indiceContenido;
    @XmlElement(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/firma", required = true)
    protected Firmas firmas;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Obtiene el valor de la propiedad indiceContenido.
     * 
     * @return
     *     possible object is
     *     {@link TipoIndiceContenido }
     *     
     */
    public TipoIndiceContenido getIndiceContenido() {
        return indiceContenido;
    }

    /**
     * Define el valor de la propiedad indiceContenido.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoIndiceContenido }
     *     
     */
    public void setIndiceContenido(TipoIndiceContenido value) {
        this.indiceContenido = value;
    }

    /**
     * Existir, al menos, una firma del contenido del indice del expediente electronico.
     * 
     * @return
     *     possible object is
     *     {@link Firmas }
     *     
     */
    public Firmas getFirmas() {
        return firmas;
    }

    /**
     * Define el valor de la propiedad firmas.
     * 
     * @param value
     *     allowed object is
     *     {@link Firmas }
     *     
     */
    public void setFirmas(Firmas value) {
        this.firmas = value;
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
