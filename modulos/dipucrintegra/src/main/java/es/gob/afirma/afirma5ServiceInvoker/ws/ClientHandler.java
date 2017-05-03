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
 * <b>File:</b><p>es.gob.afirma.afirma5ServiceInvoker.ws.ClientHandler.java.</p>
 * <b>Description:</b><p>Class secures SOAP messages of @Firma requests.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>03/10/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 23/03/2011.
 */
package es.gob.afirma.afirma5ServiceInvoker.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.SOAPPart;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.CredentialException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecSignature;
import org.apache.ws.security.message.WSSecUsernameToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.gob.afirma.afirma5ServiceInvoker.Afirma5ServiceInvokerException;
import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;

/**
 * <p>Class secures SOAP messages of @Firma requests.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 28/09/2011.
 */
class ClientHandler extends AbstractCommonHandler {

    // Opciones de seguridad
    /** Opción de seguridad UserNameToken. */
    static final String USERNAMEOPTION = WSConstants.USERNAME_TOKEN_LN;
    /** Opción de seguridad BinarySecurityToken. */
    static final String CERTIFICATEOPTION = WSConstants.BINARY_TOKEN_LN;
    /** Sin seguridad. */
    static final String NONEOPTION = "none";

    /**
     * Attribute that represents serial version identificator.
     */
    private static final long serialVersionUID = 1L;

    /** Opción de seguridad del objeto actual.*/
    private String securityOption = "";

    /**
     * Inicializa el atributo securityOption.
     * @param securityOpt opción de seguridad.
     * @throws Afirma5ServiceInvokerException Exception
     */
    public ClientHandler(String securityOpt) throws Afirma5ServiceInvokerException {
	if (securityOpt == null) {
	    throw new Afirma5ServiceInvokerException(Language.getResIntegra(ILogConstantKeys.CH_LOG001));
	}

	if (securityOpt.equals(USERNAMEOPTION)) {
	    this.securityOption = USERNAMEOPTION;
	} else if (securityOpt.equals(CERTIFICATEOPTION)) {
	    this.securityOption = CERTIFICATEOPTION;
	} else if (securityOpt.equals(NONEOPTION)) {
	    this.securityOption = NONEOPTION;
	} else {
	    throw new Afirma5ServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.CH_LOG002, new Object[ ] { securityOpt }));
	}

    }

    /**
     * {@inheritDoc}
     * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
     */
    public void invoke(MessageContext msgContext) throws AxisFault {
	SOAPMessage msg, secMsg;
	Document doc = null;

	secMsg = null;

	try {
	    // Obtención del documento XML que representa la petición SOAP
	    msg = msgContext.getCurrentMessage();
	    doc = ((org.apache.axis.message.SOAPEnvelope) msg.getSOAPPart().getEnvelope()).getAsDocument();
	    // Securización de la petición SOAP según la opcion de seguridad
	    // configurada
	    if (this.securityOption.equals(USERNAMEOPTION)) {
		secMsg = this.createUserNameToken(doc);
	    } else if (this.securityOption.equals(CERTIFICATEOPTION)) {
		secMsg = this.createBinarySecurityToken(doc);
	    }

	    if (!this.securityOption.equals(NONEOPTION)) {
		// Modificación de la petición SOAP
		((SOAPPart) msgContext.getRequestMessage().getSOAPPart()).setCurrentMessage(secMsg.getSOAPPart().getEnvelope(), SOAPPart.FORM_SOAPENVELOPE);
	    }
	} catch (Exception e) {
	    throw AxisFault.makeFault(e);
	}
    }

    /**
     * Securiza, mediante el tag userNameToken, una petición SOAP no securizada.
     * @param soapEnvelopeRequest Documento xml que representa la petición SOAP sin securizar.
     * @return Un mensaje SOAP que contiene la petición SOAP de entrada securizada
     * mediante el tag userNameToken.
     * @throws TransformerException TransformerException
     * @throws IOException IOException
     * @throws SOAPException SOAPException
     * @throws WSSecurityException WSSecurityException
     */
    private SOAPMessage createUserNameToken(Document soapEnvelopeRequest) throws TransformerException, IOException, SOAPException, WSSecurityException {
	ByteArrayOutputStream baos;
	Document secSOAPReqDoc;
	DOMSource source;
	Element element;
	SOAPMessage res;
	StreamResult streamResult;
	String secSOAPReq;
	WSSecUsernameToken wsSecUsernameToken;
	WSSecHeader wsSecHeader;

	// Inserción del tag wsse:Security y userNameToken
	wsSecHeader = new WSSecHeader(null, false);
	wsSecUsernameToken = new WSSecUsernameToken();
	wsSecUsernameToken.setPasswordType(getPasswordType());
	wsSecUsernameToken.setUserInfo(getUserAlias(), getPassword());
	wsSecHeader.insertSecurityHeader(soapEnvelopeRequest);
	wsSecUsernameToken.prepare(soapEnvelopeRequest);
	// Añadimos una marca de tiempo inidicando la fecha de creación del tag
	wsSecUsernameToken.addCreated();
	wsSecUsernameToken.addNonce();
	// Modificación de la petición
	secSOAPReqDoc = wsSecUsernameToken.build(soapEnvelopeRequest, wsSecHeader);
	element = secSOAPReqDoc.getDocumentElement();

	// Transformación del elemento DOM a String
	source = new DOMSource(element);
	baos = new ByteArrayOutputStream();
	streamResult = new StreamResult(baos);
	TransformerFactory.newInstance().newTransformer().transform(source, streamResult);
	secSOAPReq = new String(baos.toByteArray());

	// Creación de un nuevo mensaje SOAP a partir del mensaje SOAP
	// securizado formado
	MessageFactory mf = new org.apache.axis.soap.MessageFactoryImpl();
	res = mf.createMessage(null, new ByteArrayInputStream(secSOAPReq.getBytes()));

	return res;
    }

    /**
     * Securiza, mediante el tag BinarySecurityToken y firma, una petición SOAP no securizada.
     * @param soapEnvelopeRequest Documento xml que representa la petición SOAP sin securizar.
     * @return Un mensaje SOAP que contiene la petición SOAP de entrada securizada
     * mediante el tag BinarySecurityToken.
     * @throws TransformerException TransformerException
     * @throws SOAPException SOAPException
     * @throws IOException IOException
     * @throws KeyStoreException  KeyStoreException
     * @throws CredentialException CredentialException
     * @throws CertificateException CertificateException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws WSSecurityException WSSecurityException
     */
    private SOAPMessage createBinarySecurityToken(Document soapEnvelopeRequest) throws TransformerException, IOException, SOAPException, KeyStoreException, CredentialException, NoSuchAlgorithmException, CertificateException, WSSecurityException {
	ByteArrayOutputStream baos;
	Crypto crypto;
	Document secSOAPReqDoc;
	DOMSource source;
	Element element;
	StreamResult streamResult;
	String secSOAPReq;
	SOAPMessage res;
	WSSecSignature wsSecSignature;
	WSSecHeader wsSecHeader;

	crypto = null;
	wsSecHeader = null;
	wsSecSignature = null;
	// Inserción del tag wsse:Security y BinarySecurityToken
	wsSecHeader = new WSSecHeader(null, false);
	wsSecSignature = new WSSecSignature();
	crypto = getCryptoInstance();
	// Indicación para que inserte el tag BinarySecurityToken
	wsSecSignature.setKeyIdentifierType(WSConstants.BST_DIRECT_REFERENCE);
	wsSecSignature.setUserInfo(getUserAlias(), getPassword());
	wsSecHeader.insertSecurityHeader(soapEnvelopeRequest);
	wsSecSignature.prepare(soapEnvelopeRequest, crypto, wsSecHeader);

	// Modificación y firma de la petición
	secSOAPReqDoc = wsSecSignature.build(soapEnvelopeRequest, crypto, wsSecHeader);
	element = secSOAPReqDoc.getDocumentElement();
	// Transformación del elemento DOM a String
	source = new DOMSource(element);
	baos = new ByteArrayOutputStream();
	streamResult = new StreamResult(baos);
	TransformerFactory.newInstance().newTransformer().transform(source, streamResult);
	secSOAPReq = new String(baos.toByteArray());

	// Creación de un nuevo mensaje SOAP a partir del mensaje SOAP
	// securizado formado
	MessageFactory mf = new org.apache.axis.soap.MessageFactoryImpl();
	res = mf.createMessage(null, new ByteArrayInputStream(secSOAPReq.getBytes()));

	return res;
    }

}
