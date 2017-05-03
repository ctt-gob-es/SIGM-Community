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
 * <b>File:</b><p>es.gob.afirma.afirma5ServiceInvoker.CertificateCacheKey.java.</p>
 * <b>Description:</b><p>Class that represents each key of the certificates validation responses cache.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>04/02/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 04/02/2014.
 */
package es.gob.afirma.afirma5ServiceInvoker;

import java.math.BigInteger;

import es.gob.afirma.utils.GeneralConstants;
import es.gob.afirma.utils.NumberConstants;

/**
 * <p>Class that represents each key of the certificates validation responses cache.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 04/02/2014.
 */
public class CertificateCacheKey {

    /**
     * Attribute that represents the issuer of the certificate.
     */
    private String issuer;

    /**
     * Attribute that represents the serial number of the certificate.
     */
    private BigInteger serialNumber;

    /**
     * Attribute that represents the type of the request to @Firma. The allowed values are:
     * <ul>
     * <li>{@link GeneralConstants#DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST}</li>
     * <li>{@link GeneralConstants#CERTIFICATE_VALIDATION_REQUEST}</li>
     * <li>{@link GeneralConstants#VALIDACION_CERTIFICADO_REQUEST}</li>
     * </ul>
     */
    private String requestType;

    /**
     * Constructor method for the class CertificateCacheKey.java.
     * @param issuerParam Parameter that represents the issuer of the certificate.
     * @param serialNumberParam Parameter that represents the serial number of the certificate.
     * @param requestTypeParam Parameter that represents the type of the request to @Firma.
     */
    public CertificateCacheKey(String issuerParam, BigInteger serialNumberParam, String requestTypeParam) {
	this.issuer = issuerParam;
	this.serialNumber = serialNumberParam;
	this.requestType = requestTypeParam;
    }

    /**
     * Gets the value of the attribute {@link #issuer}.
     * @return the value of the attribute {@link #issuer}.
     */
    public final String getIssuer() {
	return issuer;
    }

    /**
     * Sets the value of the attribute {@link #issuer}.
     * @param issuerParam The value for the attribute {@link #issuer}.
     */
    public final void setIssuer(String issuerParam) {
	this.issuer = issuerParam;
    }

    /**
     * Gets the value of the attribute {@link #serialNumber}.
     * @return the value of the attribute {@link #serialNumber}.
     */
    public final BigInteger getSerialNumber() {
	return serialNumber;
    }

    /**
     * Sets the value of the attribute {@link #serialNumber}.
     * @param serialNumberParam The value for the attribute {@link #serialNumber}.
     */
    public final void setSerialNumber(BigInteger serialNumberParam) {
	this.serialNumber = serialNumberParam;
    }

    /**
     * Gets the value of the attribute {@link #requestType}.
     * @return the value of the attribute {@link #requestType}.
     */
    public final String getRequestType() {
	return requestType;
    }

    /**
     * Sets the value of the attribute {@link #requestType}.
     * @param requestTypeParam The value for the attribute {@link #requestType}.
     */
    public final void setRequestType(String requestTypeParam) {
	this.requestType = requestTypeParam;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
	final int prime = NumberConstants.INT_31;
	int result = 1;
	result = prime * result + (issuer == null ? 0 : issuer.hashCode());
	result = prime * result + (requestType == null ? 0 : requestType.hashCode());
	result = prime * result + (serialNumber == null ? 0 : serialNumber.hashCode());
	return result;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	CertificateCacheKey other = (CertificateCacheKey) obj;
	if (issuer == null) {
	    if (other.issuer != null) {
		return false;
	    }
	} else if (!issuer.equals(other.issuer)) {
	    return false;
	}
	if (requestType == null) {
	    if (other.requestType != null) {
		return false;
	    }
	} else if (!requestType.equals(other.requestType)) {
	    return false;
	}
	if (serialNumber == null) {
	    if (other.serialNumber != null) {
		return false;
	    }
	} else if (!serialNumber.equals(other.serialNumber)) {
	    return false;
	}
	return true;
    }

}
