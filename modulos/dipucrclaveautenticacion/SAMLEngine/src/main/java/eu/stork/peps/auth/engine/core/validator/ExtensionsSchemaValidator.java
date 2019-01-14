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

package eu.stork.peps.auth.engine.core.validator;

import java.util.List;

import org.opensaml.saml2.common.Extensions;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

import eu.stork.peps.auth.engine.core.QAAAttribute;

/**
 * The Class ExtensionsSchemaValidator.
 * 
 * @author fjquevedo
 */
public class ExtensionsSchemaValidator implements Validator<Extensions> {


    /**
     * validate the extensions.
     * 
     * @param extensions the extensions
     * 
     * @throws ValidationException the validation exception
     */
    public final void validate(final Extensions extensions)
    throws ValidationException {
	if (extensions.getUnknownXMLObjects() == null
		|| extensions.getUnknownXMLObjects().size() <= 0) {
	    throw new ValidationException("Extension element is empty or not exist.");
	}
	
	List<XMLObject> qaa = extensions.getUnknownXMLObjects(QAAAttribute.DEF_ELEMENT_NAME);
	
	if (qaa.size() == 1) {
		final Validator<QAAAttribute> validatorQaa = new QAAAttributeSchemaValidator();
		validatorQaa.validate((QAAAttribute) qaa.get(0));
	} else {
		throw new ValidationException(
	    "Extensions must contain only one element QAALevel.");
	}

    }

}
