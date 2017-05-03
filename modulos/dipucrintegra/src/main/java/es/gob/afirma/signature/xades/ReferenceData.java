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

/*
 * Este fichero forma parte de la plataforma de @firma.
 * La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2009-,2011 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.signature.xades.ReferenceData.java.</p>
 * <b>Description:</b>Class contains necesary data of a <code>Reference</code> element (defined in the
 * <a href="http://www.w3.org/TR/xmldsig-core/">W3C Recommendation for XML-Signature Syntax and Processing</a>).</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>04/08/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 04/08/2011.
 */
package es.gob.afirma.signature.xades;

import java.util.List;

/**
 * <p>Class contains necesary data of a <code>Reference</code> element (defined in the
 * <a href="http://www.w3.org/TR/xmldsig-core/">W3C Recommendation for XML-Signature Syntax and Processing</a>).</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 04/08/2011.
 */
public class ReferenceData {

    /**
     * Attribute that represents id data.
     */
    private String id;

    /**
     * Attribute that represents uri data.
     */
    private String uri;

    /**
     * Attribute that represents type data.
     */
    private String type;

    /**
     * Attribute that represents transforms data.
     */
    private List<TransformData> transforms;

    /**
     * Attribute that represents digest method algorithm.
     */
    private String digestMethodAlg;

    /**
     * Attribute that represents digest value.
     */
    private String digestValue;

    /**
     * Constructor method for the class ReferenceData.java.
     * @param digestMethodAlgParam digest method algorithm
     * @param digestValueParam  digest value
     */
    public ReferenceData(String digestMethodAlgParam, String digestValueParam) {
	digestMethodAlg = digestMethodAlgParam;
	digestValue = digestValueParam;
    }

    /**
     * Gets the value of the attribute {@link #transforms}.
     * @return the value of the attribute {@link #transforms}.
     */
    public final List<TransformData> getTransforms() {
	return transforms;
    }

    /**
     * Gets the value of the attribute {@link #digestMethodAlg}.
     * @return the value of the attribute {@link #digestMethodAlg}.
     */
    public final String getDigestMethodAlg() {
	return digestMethodAlg;
    }

    /**
     * Gets the value of the attribute {@link #digestValue}.
     * @return the value of the attribute {@link #digestValue}.
     */
    public final String getDigestValue() {
	return digestValue;
    }

    /**
     * Gets the value of the attribute {@link #id}.
     * @return the value of the attribute {@link #id}.
     */
    public final String getId() {
	return id;
    }

    /**
     * Sets the value of the attribute {@link #id}.
     * @param idParam The value for the attribute {@link #id}.
     */
    public final void setId(String idParam) {
	this.id = idParam;
    }

    /**
     * Gets the value of the attribute {@link #uri}.
     * @return the value of the attribute {@link #uri}.
     */
    public final String getUri() {
	return uri;
    }

    /**
     * Sets the value of the attribute {@link #uri}.
     * @param uriParam The value for the attribute {@link #uri}.
     */
    public final void setUri(String uriParam) {
	this.uri = uriParam;
    }

    /**
     * Gets the value of the attribute {@link #type}.
     * @return the value of the attribute {@link #type}.
     */
    public final String getType() {
	return type;
    }

    /**
     * Sets the value of the attribute {@link #type}.
     * @param typeParam The value for the attribute {@link #type}.
     */
    public final void setType(String typeParam) {
	this.type = typeParam;
    }

    /**
     * Sets the value of the attribute {@link #transforms}.
     * @param transformsParams The value for the attribute {@link #transforms}.
     */
    public final void setTransforms(List<TransformData> transformsParams) {
	this.transforms = transformsParams;
    }

    /**
     * <p>Class contains necesary data of a <code>Transform</code> element (defined in the
     * <a href="http://www.w3.org/TR/xmldsig-core/">W3C Recommendation for XML-Signature Syntax and Processing</a>).</p>
     * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
     * certificates and electronic signature.</p>
     * @version 1.0, 04/08/2011.
     */
    public class TransformData {

	/**
	 * Attribute that represents algorithm.
	 */
	private String alg;

	/**
	 * Attribute that represents xPath reference list.
	 */
	private List<String> xPath;

	/**
	 * Constructor method for the class TransformData.
	 * @param algorithm
	 */
	public TransformData(String algorithm) {
	    this.alg = algorithm;
	}

	/**
	 * Constructor method for the class TransformData.
	 * @param algorithm digest algorithm
	 * @param xpathList list with xPath references
	 */
	public TransformData(String algorithm, List<String> xpathList) {
	    this.alg = algorithm;
	    this.xPath = xpathList;
	}

	/**
	 * Gets the value of the attribute {@link #alg}.
	 * @return the value of the attribute {@link #alg}.
	 */
	public final String getAlgorithm() {
	    return alg;
	}

	/**
	 * Gets the value of the attribute {@link #xPath}.
	 * @return the value of the attribute {@link #xPath}.
	 */
	public final List<String> getXPath() {
	    return xPath;
	}

    }

}
