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

package es.seap.minhap.portafirmas.business.login.impl;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.login.LoginBusinessService;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;
import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.PersonalAttributeList;
import eu.stork.peps.auth.commons.STORKAuthnRequest;
import eu.stork.peps.auth.engine.STORKSAMLEngine;

@Service("loginBusinessService")
public class LoginBusinessServiceImpl implements LoginBusinessService {

    @Override
    public byte[] generaTokenClave(Properties claveProperties) throws PfirmaException {
    	String url = claveProperties.getProperty(Constants.PROPERTY_URL_CLAVE);
        IPersonalAttributeList pAttList = new PersonalAttributeList();

        /* eIdentifier */
        PersonalAttribute att = new PersonalAttribute();
        att.setName(claveProperties.getProperty("attribute1.name"));
        att.setIsRequired(true);
        pAttList.add(att);

        /* givenName */
        att = new PersonalAttribute();
        att.setName(claveProperties.getProperty("attribute2.name"));
        att.setIsRequired(true);
        pAttList.add(att);

        /* surname */
        att = new PersonalAttribute();
        att.setName(claveProperties.getProperty("attribute3.name"));
        att.setIsRequired(true);
        pAttList.add(att);

        STORKAuthnRequest authnRequest = new STORKAuthnRequest();

        authnRequest.setDestination(url);
        authnRequest.setProviderName(claveProperties.getProperty("provider.name"));
        authnRequest.setQaa(Integer.parseInt(claveProperties.getProperty("sp.qaalevel")));
        authnRequest.setPersonalAttributeList(pAttList);
        authnRequest.setAssertionConsumerServiceURL(claveProperties.getProperty("sp.return"));

        // new parameters
        authnRequest.setSpSector(claveProperties.getProperty("sp.sector"));
        authnRequest.setSpApplication(claveProperties.getProperty("sp.aplication"));

        // V-IDP parameters
        authnRequest.setSPID(claveProperties.getProperty("provider.name"));

        try {
            STORKSAMLEngine engine = STORKSAMLEngine.getInstance("SP");
            authnRequest = engine.generateSTORKAuthnRequest(authnRequest);
        } catch (Exception e) {
            throw new PfirmaException("no se puede generar el token para una petición SAML: " + e.getMessage(), e);
        }

        return authnRequest.getTokenSaml();
    }

}
