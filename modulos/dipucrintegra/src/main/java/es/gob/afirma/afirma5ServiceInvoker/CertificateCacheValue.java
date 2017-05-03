// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/**
 * <b>File:</b><p>es.gob.afirma.afirma5ServiceInvoker.CertificateCacheValue.java.</p>
 * <b>Description:</b><p>Class that represents each entry of the certificates validation responses cache.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>04/02/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 04/02/2014.
 */
package es.gob.afirma.afirma5ServiceInvoker;

import java.util.Date;

/**
 * <p>Class that represents each entry of the certificates validation responses cache.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 04/02/2014.
 */
public class CertificateCacheValue {

    /**
     * Attribute that represents the date when the entry has added to the certificates validation responses cache.
     */
    private Date insertDate;

    /**
     * Attribute that represents the XML with the certificate validation response retrieved from @Firma.
     */
    private String xmlResponse;

    /**
     * Constructor method for the class CertificateCacheValue.java.
     * @param insertDateParam Parameter that represents the date when the entry has added to the certificates validation responses cache.
     * @param xmlResponseParam Parameter that represents the XML with the certificate validation response retrieved from @Firma.
     */
    public CertificateCacheValue(Date insertDateParam, String xmlResponseParam) {
	insertDate = insertDateParam;
	xmlResponse = xmlResponseParam;
    }

    /**
     * Gets the value of the attribute {@link #insertDate}.
     * @return the value of the attribute {@link #insertDate}.
     */
    public final Date getInsertDate() {
	return insertDate;
    }

    /**
     * Sets the value of the attribute {@link #insertDate}.
     * @param insertDateParam The value for the attribute {@link #insertDate}.
     */
    public final void setInsertDate(Date insertDateParam) {
	this.insertDate = insertDateParam;
    }

    /**
     * Gets the value of the attribute {@link #xmlResponse}.
     * @return the value of the attribute {@link #xmlResponse}.
     */
    public final String getXmlResponse() {
	return xmlResponse;
    }

    /**
     * Sets the value of the attribute {@link #xmlResponse}.
     * @param xmlResponseParam The value for the attribute {@link #xmlResponse}.
     */
    public final void setXmlResponse(String xmlResponseParam) {
	this.xmlResponse = xmlResponseParam;
    }

}
