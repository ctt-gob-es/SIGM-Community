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


package es.mpt.dsic.inside.ws.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para firmaFicheroWithPropertieResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="firmaFicheroWithPropertieResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="datosSalida" type="{http://service.ws.inside.dsic.mpt.es/}datosSalida" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "firmaFicheroWithPropertieResponse", propOrder = {
    "datosSalida"
})
public class FirmaFicheroWithPropertieResponse {

    protected DatosSalida datosSalida;

    /**
     * Obtiene el valor de la propiedad datosSalida.
     * 
     * @return
     *     possible object is
     *     {@link DatosSalida }
     *     
     */
    public DatosSalida getDatosSalida() {
        return datosSalida;
    }

    /**
     * Define el valor de la propiedad datosSalida.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosSalida }
     *     
     */
    public void setDatosSalida(DatosSalida value) {
        this.datosSalida = value;
    }

}
