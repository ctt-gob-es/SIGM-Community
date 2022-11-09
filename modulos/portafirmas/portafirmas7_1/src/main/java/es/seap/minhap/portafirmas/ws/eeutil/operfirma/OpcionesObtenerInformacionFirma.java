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


package es.seap.minhap.portafirmas.ws.eeutil.operfirma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for opcionesObtenerInformacionFirma complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="opcionesObtenerInformacionFirma">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="obtenerFirmantes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="obtenerDatosFirmados" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="obtenerTipoFirma" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "opcionesObtenerInformacionFirma", propOrder = {
    "obtenerFirmantes",
    "obtenerDatosFirmados",
    "obtenerTipoFirma"
})
public class OpcionesObtenerInformacionFirma {

    protected boolean obtenerFirmantes;
    protected boolean obtenerDatosFirmados;
    protected boolean obtenerTipoFirma;

    /**
     * Gets the value of the obtenerFirmantes property.
     * 
     */
    public boolean isObtenerFirmantes() {
        return obtenerFirmantes;
    }

    /**
     * Sets the value of the obtenerFirmantes property.
     * 
     */
    public void setObtenerFirmantes(boolean value) {
        this.obtenerFirmantes = value;
    }

    /**
     * Gets the value of the obtenerDatosFirmados property.
     * 
     */
    public boolean isObtenerDatosFirmados() {
        return obtenerDatosFirmados;
    }

    /**
     * Sets the value of the obtenerDatosFirmados property.
     * 
     */
    public void setObtenerDatosFirmados(boolean value) {
        this.obtenerDatosFirmados = value;
    }

    /**
     * Gets the value of the obtenerTipoFirma property.
     * 
     */
    public boolean isObtenerTipoFirma() {
        return obtenerTipoFirma;
    }

    /**
     * Sets the value of the obtenerTipoFirma property.
     * 
     */
    public void setObtenerTipoFirma(boolean value) {
        this.obtenerTipoFirma = value;
    }

}
