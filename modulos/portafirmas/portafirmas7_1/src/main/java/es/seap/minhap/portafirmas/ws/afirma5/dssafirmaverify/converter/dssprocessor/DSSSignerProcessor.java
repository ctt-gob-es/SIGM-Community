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

package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor;


import java.util.List;

import javax.xml.bind.JAXBElement;
/*import es.mpt.dsic.inside.ws.service.model.ConfiguracionAmpliarFirma;
import es.mpt.dsic.inside.ws.service.obtenerinformacionfirma.ContenidoFirmado;
import es.mpt.dsic.inside.ws.service.obtenerinformacionfirma.ContentNotExtractedException;*/import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema.AdditionalReportOption;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema.IncludeProperties;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema.IncludePropertyType;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.minhap.MyObjectFactory;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.AnyType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ClaimedIdentity;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ResponseBaseType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.VerifyRequest;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.profiles.verificationreport.schema.ReportOptionsType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.profiles.verificationreport.schema.ReturnVerificationReport;

public abstract class DSSSignerProcessor {

	protected static es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ObjectFactory of_oasis_core = new es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ObjectFactory ();
	protected static es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.saml._1_0.assertion.ObjectFactory of_oasis_assertion = new es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.saml._1_0.assertion.ObjectFactory ();
	protected static es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.profiles.verificationreport.schema.ObjectFactory of_oasis_profile_verificationreport = new es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.profiles.verificationreport.schema.ObjectFactory();
	protected static es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema.ObjectFactory of_afirma_profile = new es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema.ObjectFactory();
	protected static MyObjectFactory my_of_afirma_profile = new es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.minhap.MyObjectFactory();

	/**
	 * M�todo abstracto que rellena el nodo "SignatureObject" de la petici�n para realizar validaciones de firma
	 * @param vf Objeto VerifyRequest
	 * @param sign Firma
	 */
	public abstract void fillSignatureObjectAndInputDocuments (VerifyRequest vf, final byte sign[]);

	/**
	 * M�todo abstracto que crea el nodo "ReturnUpdatedSignature" para las peticiones de ampliaci�n de firma
	 * @param at Objeto AnyType
	 * @param sign Firma
	 * @param upgradeFormat Formato al que se ampl�a
	 */
	public abstract void fillReturnUpdatedSignature (AnyType at, final String upgradeFormat);

	/**
	 * M�todo abstracto que permite obtener el contenido firmado
	 * @param verifyResponse Objeto VerifyResponse de respuesta
	 * @param sign Firma
	 * @return El contenido firmado
	 * @throws ContentNotExtractedException
	 */
	//public abstract ContenidoFirmado getSignedData (ResponseBaseType verifyResponse, final byte sign[]) throws ContentNotExtractedException ;

	/**
	 * M�todo abstracto que permite obtener la firma ampliada de un petici�n de upgrade de firma
	 * @param verifyResponse Objeto VerifyResponse de respuesta del servicio
	 * @return Firma ampliada
	 */
	public abstract byte[] getUpgradedSignature (ResponseBaseType verifyResponse);

	/**
	 * M�todo que construye la petici�n para realizar una validaci�n de firma
	 * @param sign Firma que se va a validar
	 * @param idAplicacion Identificador de aplicaci�n
	 * @return Objeto que define la petici�n del servicio
	 */
	public VerifyRequest buildVerifyRequest (byte sign[], String idAplicacion) {
		VerifyRequest vf = of_oasis_core.createVerifyRequest();
		fillSignatureObjectAndInputDocuments (vf, sign);
		
		vf.setOptionalInputs(buildOptionalInputs (idAplicacion));	
		return vf;
	}

	/**
	 * M�todo que construye la petici�n para realizar una amplicaci�n de firma
	 * @param idAplicacion Identificador de aplicaci�n
	 * @param sign Firma que se va ampliar
	 * @param upgradeFormat Formato de firma al que se ampl�a
	 * @return Objeto que define la petici�n del servicio
	 */
	public VerifyRequest buildVerifyRequestToUpgrade (String idAplicacion, 
													  byte sign[], 
													  List<byte[]> certificateList, 
													  boolean ignoreGracePeriod,
													  String formatoAmpliacion) {
		VerifyRequest vf = of_oasis_core.createVerifyRequest();

		// Objeto SigntureObjet //
		fillSignatureObjectAndInputDocuments (vf, sign);

		// Objeto OptionalInputs //
		
		AnyType optionalInputs = buildOptionalInputsToUpgrade (idAplicacion, certificateList, ignoreGracePeriod);
															   
		fillReturnUpdatedSignature(optionalInputs, formatoAmpliacion);
		vf.setOptionalInputs(optionalInputs);

		return vf;
	}

	/**
	 * M�todo que rellena los capos opcionales de la petici�n de validaci�n de firma
	 * @param idAplicacion Identificador de aplicaci�n
	 * @return Par�metros opcionales
	 */
	public static AnyType buildOptionalInputs (String idAplicacion) {
		AnyType oi = of_oasis_core.createAnyType();

		// ----------------------- Claimed Identity ------------------------------ //
		ClaimedIdentity ci = of_oasis_core.createClaimedIdentity();
		ci.setName(of_oasis_assertion.createNameIdentifierType());
		ci.getName().setValue(idAplicacion);
		oi.getAny().add(ci);
		// ----------------------- End Claimed Identity ------------------------------ //
		
		//------------------- ReturnReadableCertificateInfo -----------------------		
		JAXBElement<Object> o = of_afirma_profile.createReturnReadableCertificateInfo("");
		oi.getAny().add(o);
		//------------------- End ReturnReadableCertificateInfo -----------------------
		
		// ----------------------- AdditionalReportOption----------------------
		AdditionalReportOption addro = of_afirma_profile.createAdditionalReportOption();
		IncludeProperties ip = of_afirma_profile.createIncludeProperties();
		
		IncludePropertyType pt = of_afirma_profile.createIncludePropertyType();
		pt.setType("urn:afirma:dss:1.0:profile:XSS:SignatureProperty:SignatureTimeStamp");		
		ip.getIncludeProperty().add(pt);
		pt.setDetailProperty("urn:afirma:dss:1.0:profile:XSS:SignatureProperty:SignatureTimeStamp:IncludeTST");
		
		addro.setIncludeProperties(ip);		
		oi.getAny().add(addro);
		//----------------------- AdditionalReportOption End----------------------
		
		
		// ---------------------- ReturnVerificationReport ----------------------
		ReturnVerificationReport rvr = of_oasis_profile_verificationreport.createReturnVerificationReport();
		ReportOptionsType ro = of_oasis_profile_verificationreport.createReportOptionsType();
		ro.setIncludeCertificateValues(true);
		ro.setReportDetailLevel("urn:oasis:names:tc:dss:1.0:reportdetail:allDetails");
		
		rvr.setReportOptions(ro);
		oi.getAny().add(rvr);
		// ---------------------- End ReturnVerificationReport -----------------------
		
		// ------------------------ ReturnSignedDataInfo ---------------------------
		JAXBElement<Object> rsdi = of_afirma_profile.createReturnSignedDataInfo("");
		oi.getAny().add(rsdi);
		// ----------------------End ReturnSignedDataInfo ---------------------------
		
		
		return oi;
		
	}

	/**
	 * M�todo que rellena los capos opcionales de la petici�n de ampliaci�n de firma
	 * @param idAplicacion Identificador de aplicaci�n
	 * @return Par�metros opcionales
	 */
	public static AnyType buildOptionalInputsToUpgrade (String idAplicacion, List<byte[]> targetSignersCerts, 
														boolean ignoreGracePeriod) {
		AnyType oi = of_oasis_core.createAnyType();

		// ----------------------- Claimed Identity ------------------------------ //
		ClaimedIdentity ci = of_oasis_core.createClaimedIdentity();
		ci.setName(of_oasis_assertion.createNameIdentifierType());
		ci.getName().setValue(idAplicacion);
		oi.getAny().add(ci);
		// ----------------------- End Claimed Identity ------------------------------ //

		// ------------------------------- Target Signers ------------------------------- //
		// Si no se indican certificados de firmantes se aplica la ampliaci�n para todos //
		if (targetSignersCerts != null) {
			for (byte[] cert : targetSignersCerts) {
				JAXBElement<byte[]> targetSigner = of_afirma_profile.createTargetSigner(cert);
				oi.getAny().add(targetSigner);
			}
		}
		// ------------------------------- End Target Signers --------------------------- //

		// ----------------------------- Ignore Grace Period ----------------------------- //
		if (ignoreGracePeriod) {
			JAXBElement<String> igp = my_of_afirma_profile.createIgnoreGracePeriod("");
			oi.getAny().add(igp);
		}
		// --------------------------- End Ignore Grace Period --------------------------- //
		
		return oi;
		
	}
}
