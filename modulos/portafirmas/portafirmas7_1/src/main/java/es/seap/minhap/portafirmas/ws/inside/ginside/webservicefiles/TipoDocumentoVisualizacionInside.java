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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para TipoDocumentoVisualizacionInside complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoDocumentoVisualizacionInside">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documentoEni" type="{https://ssweb.seap.minhap.es/Inside/XSD/v1.0/visualizacion/documento-e}TipoDocumentoEniBinarioOTipo"/>
 *         &lt;element name="metadatosAdicionales" type="{https://ssweb.seap.minhap.es/Inside/XSD/v1.0/metadatosAdicionales}TipoMetadatosAdicionales" minOccurs="0"/>
 *         &lt;element name="opcionesVisualizacionDocumento" type="{https://ssweb.seap.minhap.es/Inside/XSD/v1.0/visualizacion/documento-e}TipoOpcionesVisualizacionDocumento"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoDocumentoVisualizacionInside", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/visualizacion/documento-e", propOrder = {
    "documentoEni",
    "metadatosAdicionales",
    "opcionesVisualizacionDocumento"
})
public class TipoDocumentoVisualizacionInside {

    @XmlElement(required = true)
    protected TipoDocumentoEniBinarioOTipo documentoEni;
    protected TipoMetadatosAdicionales metadatosAdicionales;
    @XmlElement(required = true)
    protected TipoOpcionesVisualizacionDocumento opcionesVisualizacionDocumento;

    /**
     * Obtiene el valor de la propiedad documentoEni.
     * 
     * @return
     *     possible object is
     *     {@link TipoDocumentoEniBinarioOTipo }
     *     
     */
    public TipoDocumentoEniBinarioOTipo getDocumentoEni() {
        return documentoEni;
    }

    /**
     * Define el valor de la propiedad documentoEni.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoDocumentoEniBinarioOTipo }
     *     
     */
    public void setDocumentoEni(TipoDocumentoEniBinarioOTipo value) {
        this.documentoEni = value;
    }

    /**
     * Obtiene el valor de la propiedad metadatosAdicionales.
     * 
     * @return
     *     possible object is
     *     {@link TipoMetadatosAdicionales }
     *     
     */
    public TipoMetadatosAdicionales getMetadatosAdicionales() {
        return metadatosAdicionales;
    }

    /**
     * Define el valor de la propiedad metadatosAdicionales.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoMetadatosAdicionales }
     *     
     */
    public void setMetadatosAdicionales(TipoMetadatosAdicionales value) {
        this.metadatosAdicionales = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesVisualizacionDocumento.
     * 
     * @return
     *     possible object is
     *     {@link TipoOpcionesVisualizacionDocumento }
     *     
     */
    public TipoOpcionesVisualizacionDocumento getOpcionesVisualizacionDocumento() {
        return opcionesVisualizacionDocumento;
    }

    /**
     * Define el valor de la propiedad opcionesVisualizacionDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoOpcionesVisualizacionDocumento }
     *     
     */
    public void setOpcionesVisualizacionDocumento(TipoOpcionesVisualizacionDocumento value) {
        this.opcionesVisualizacionDocumento = value;
    }

}
