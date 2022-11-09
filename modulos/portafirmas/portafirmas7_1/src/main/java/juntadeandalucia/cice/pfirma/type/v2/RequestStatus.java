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


package juntadeandalucia.cice.pfirma.type.v2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for requestStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="requestStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACEPTADO"/>
 *     &lt;enumeration value="RECHAZADO"/>
 *     &lt;enumeration value="CADUCADO"/>
 *     &lt;enumeration value="EN PROCESO"/>
 *     &lt;enumeration value="RETIRADO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "requestStatus")
@XmlEnum
public enum RequestStatus {

    ACEPTADO("ACEPTADO"),
    RECHAZADO("RECHAZADO"),
    CADUCADO("CADUCADO"),
    @XmlEnumValue("EN PROCESO")
    EN_PROCESO("EN PROCESO"),
    RETIRADO("RETIRADO");
    private final String value;

    RequestStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RequestStatus fromValue(String v) {
        for (RequestStatus c: RequestStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
