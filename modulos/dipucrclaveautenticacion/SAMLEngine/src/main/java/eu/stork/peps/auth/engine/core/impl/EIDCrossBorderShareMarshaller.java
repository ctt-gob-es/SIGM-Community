/* 
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 * 
 * http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations under
 * the Licence.
 */

package eu.stork.peps.auth.engine.core.impl;

import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

import eu.stork.peps.auth.engine.core.EIDCrossBorderShare;


/**
 * The Class EIDCrossBorderShareMarshaller.
 * 
 * @author fjquevedo
 */
public class EIDCrossBorderShareMarshaller extends AbstractSAMLObjectMarshaller {


	/**
	 * Marshall element content.
	 *
	 * @param samlObject the SAML object
	 * @param domElement the DOM element
	 * @throws MarshallingException the marshalling exception
	 */
	protected final void marshallElementContent(final XMLObject samlObject,
			final Element domElement) throws MarshallingException {
		final EIDCrossBorderShare crossBorderShare = (EIDCrossBorderShare) samlObject;
		XMLHelper.appendTextContent(domElement, crossBorderShare.getEIDCrossBorderShare());
	}
}
