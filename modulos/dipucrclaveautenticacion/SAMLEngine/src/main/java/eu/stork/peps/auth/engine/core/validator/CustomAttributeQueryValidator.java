package eu.stork.peps.auth.engine.core.validator;

import java.util.List;

import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.core.SubjectQuery;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

import eu.stork.peps.auth.engine.core.CustomAttributeQuery;
import eu.stork.peps.auth.engine.core.QAAAttribute;

public class CustomAttributeQueryValidator implements Validator<CustomAttributeQuery> {
	
	/*
	 * Validate action.
	 * 
	 * @param qaa the quality authentication assurance level attribute
	 * 
	 * @throws ValidationException the validation exception
	 */
	public final void validate(final CustomAttributeQuery attrQuery) throws ValidationException {
		validateAssertion(attrQuery);
		validateSubject(attrQuery);
		validateDestination(attrQuery);
	}


	/**
	 * Validate assertion.
	 * 
	 * @param attrQuery the attribute query
	 * 
	 * @throws ValidationException the validation exception
	 */
	protected final void validateAssertion(final CustomAttributeQuery attrQuery)
	throws ValidationException {
		if (DatatypeHelper.isEmpty(attrQuery.getAssertionConsumerServiceURL())) {
			throw new ValidationException("Consumer Service URL must be specified.");
		}
	}
	
	//Validate subject
	protected void validateSubject(CustomAttributeQuery query) throws ValidationException {
        if (query.getSubject() == null)
            throw new ValidationException("Subject is required");
    }
	
	//Validate destination
	protected void validateDestination(CustomAttributeQuery query) throws ValidationException {
        if (query.getDestination() == null)
            throw new ValidationException("Destination is required");
    }

}
