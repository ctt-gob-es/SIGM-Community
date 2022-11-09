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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para TipoOpcionValidacionDocumento.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="TipoOpcionValidacionDocumento">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TOVD01"/>
 *     &lt;enumeration value="TOVD02"/>
 *     &lt;enumeration value="TOVD03"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TipoOpcionValidacionDocumento", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/documento-e")
@XmlEnum
public enum TipoOpcionValidacionDocumento {

    @XmlEnumValue("TOVD01")
    TOVD_01("TOVD01"),
    @XmlEnumValue("TOVD02")
    TOVD_02("TOVD02"),
    @XmlEnumValue("TOVD03")
    TOVD_03("TOVD03");
    private final String value;

    TipoOpcionValidacionDocumento(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoOpcionValidacionDocumento fromValue(String v) {
        for (TipoOpcionValidacionDocumento c: TipoOpcionValidacionDocumento.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
