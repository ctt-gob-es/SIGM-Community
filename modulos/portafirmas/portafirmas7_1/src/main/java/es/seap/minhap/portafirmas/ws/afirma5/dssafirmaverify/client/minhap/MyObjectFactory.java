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

package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.minhap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class MyObjectFactory {
	
    private final static QName _IgnoreGracePeriod_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "IgnoreGracePeriod");

    /**
     * Create a new MyObjectFactory that can be used to create new instances of schema derived classes for package: afirma.dss._1_0.profile.xss.schema
     * 
     */
    public MyObjectFactory() {

    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "IgnoreGracePeriod")
    public JAXBElement<String> createIgnoreGracePeriod(String value) {
        return new JAXBElement<String>(_IgnoreGracePeriod_QNAME, String.class, null, value);
    }


}

