/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.connector;

import org.apache.log4j.Logger;

import beans.SignResponse;
import beans.ValidateSignResponse;
import core.jsign.services.SignServiceImpl;
import core.ws.jsign.SignRequest;
import core.ws.jsign.ValidateSignRequest;
import core.ws.jsign.ValidationResponse;
import core.ws.jsign.enums.SignatureRootType;
import es.msssi.sgm.registropresencial.beans.WebParameter;
import es.msssi.sigm.core.exception.SigmWSException;
import es.msssi.sigm.core.util.Constants;
import es.msssi.sigm.ws.beans.Acuse;
 
public class PfeConnector {
	private static Logger log = Logger.getLogger(PfeConnector.class.getName());
	
	
	public static boolean validate(byte[] contenido, byte[] firma) throws SigmWSException {
		
	    boolean validationOk = false;
	    	
		ValidateSignRequest request = new ValidateSignRequest();
		
		try{
			request.setApplicationAlias(Constants.PFE_APP_ALIAS);
			request.setSignatureType(SignatureRootType.XAdES);
			request.setSignData(firma);	
			request.setDetachedOriginalData(contenido);
			request.setReturnSignedContent(false);
			ValidateSignResponse signValidationResult = null;
			ValidationResponse validState;
			
//			log.debug("Peticion a PFE: \n"+XmlUtil.printJAXB(request));

			signValidationResult = SignServiceImpl.validateSign(request);
			validState = signValidationResult.getSigns().get(0).getValidState();
			
			if (ValidationResponse.VALID.equals(validState)) {
				log.debug("FIRMA VALIDA");
		    	validationOk = true;
		    } else {
		    	log.error("Firma no válida. Contenido documento: "+new String(contenido,  "UTF-8"));
		    	log.error("Firma no válida. Contenido firma: "+new String(firma,  "UTF-8"));
		    }

		} catch (Exception e) {
				throw new SigmWSException("err.pfe.general", e);
		}				
		return validationOk;
	}
	

	public static Acuse signReport(byte[] pdfReport, String reportName) throws SigmWSException {
		String cert_alias = (String) WebParameter.getEntryParameter("cert_alias");			
		
		Acuse result = null;
		
		try {
			
			SignRequest rq = new SignRequest();
			rq.setApplicationAlias(Constants.PFE_APP_ALIAS);
			rq.setAttachmentType(Constants.PFE_ATTACHMENT_TYPE_ATTACHED);  
			rq.setCertificateAlias(cert_alias);
			rq.setFileData(pdfReport);
			rq.setFileName(Constants.PFE_SIGNED_DOC_NAME);//EJ. "SignedReport.pdf"
			rq.setSignatureType(Constants.PFE_SIGN_TYPE_PADES);// "PAdES_BASIC"	       
			rq.setMultisignatureType(Constants.PFE_MULTI_SIGN_TYPE);// "SIMPLE"	 
			rq.setPolicyId("PAdES_AGE_CSV");
			
			SignResponse sign = SignServiceImpl.sign(rq);
			
		    // CODIGO CSV IDENTIFICADOR DE LA FIRMA
			log.debug("CSV del fichero firmado: " + sign.getDocumentId());
			
			result = new Acuse();
			result.setContenido(sign.getSignData());
			result.setNombre(reportName);
			result.setCsv(sign.getDocumentId());
			
			return result;
		}
		catch (Exception e) {
			throw new SigmWSException("sign.sign_pdf_fail", e);
		}
			
	}	
	
	
}
