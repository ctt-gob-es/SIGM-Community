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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimeSignatureInstructionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TimeSignatureInstructionType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:dss:1.0:core:schema}UpdateSignatureInstructionType">
 *       &lt;attribute name="TimeStampTheGivenSignature" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeSignatureInstructionType")
public class TimeSignatureInstructionType
    extends UpdateSignatureInstructionType
{

    @XmlAttribute(name = "TimeStampTheGivenSignature")
    protected Boolean timeStampTheGivenSignature;

    /**
     * Gets the value of the timeStampTheGivenSignature property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isTimeStampTheGivenSignature() {
        if (timeStampTheGivenSignature == null) {
            return false;
        } else {
            return timeStampTheGivenSignature;
        }
    }

    /**
     * Sets the value of the timeStampTheGivenSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTimeStampTheGivenSignature(Boolean value) {
        this.timeStampTheGivenSignature = value;
    }

}
