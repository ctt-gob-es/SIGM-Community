/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.signature;

import java.io.Serializable;

import org.apache.log4j.Logger;

import beans.SignResponse;
import core.jsign.services.SignServiceImpl;
import core.ws.jsign.SignRequest;
import es.msssi.sgm.registropresencial.beans.WebParameter;
import es.msssi.sgm.registropresencial.businessobject.IGenericBo;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;


/**
 * Clase encargada de realizar las firmas de los informes. La firma será en
 * formato PDF.
 * 
 * @author jortizs
 */
public class ReportsSignature implements IGenericBo, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ReportsSignature.class.getName());
    private static String SIGNCERTALIAS = (String) WebParameter.getEntryParameter("cert_alias");

    /**
     * Realiza la firma de un informe en formato PDF utilizando la plataforma de
     * firma del MSSSI.
     * 
     * @param pdfReportByteArray
     *            Datos a firmar.
     * @return pdfSignedReportByteArray Datos firmados.
     */
    public static SignResponse signReport(
	byte[] pdfReportByteArray) {
	LOG.trace("Entrando en ReportsSignature.signReport()");
	SignResponse sign = null;
	try {
	    // jSign.init(Configuration.JNDICONTEXT);
	    SignRequest rq = new SignRequest();
	    rq.setApplicationAlias(KeysRP.PFE_APP_ALIAS);
	    rq.setAttachmentType(KeysRP.PFE_ATTACHMENT_TYPE);
	    rq.setCertificateAlias(SIGNCERTALIAS);
	    rq.setFileData(pdfReportByteArray);
	    rq.setFileName(KeysRP.PFE_SIGNED_DOC_NAME);
	    rq.setSignatureType(KeysRP.PFE_SIGN_TYPE);
	    rq.setMultisignatureType(KeysRP.PFE_MULTI_SIGN_TYPE);
	    rq.setPolicyId("PAdES_AGE_CSV");
	    sign = SignServiceImpl.sign(rq);
	    // CODIGO CSV IDENTIFICADOR DE LA FIRMA
	    LOG.info("CSV del fichero firmado: " +
		sign.getDocumentId());
	    // BYTES CON EL FICHERO FIRMADO
	   // pdfSignedReportByteArray = sign.getSignData();
	}
	catch (Exception exception) {
	    LOG.error(
		"Error al realizar la firma del informe", exception);
	    Utils.redirectToErrorPage(
		null, null, exception);
	}
	return sign;
    }
    
    /**
     * Realiza la firma de un informe en formato PDF utilizando la plataforma de
     * firma del MSSSI.
     * 
     * @param pdfReportByteArray
     *            Datos a firmar.
     * @param nameFile 
     * 		nombre del fichero.
     * @return pdfSignedReportByteArray Datos firmados.
     */
    public static SignResponse signReport(
	byte[] pdfReportByteArray, String nameFile) {
	LOG.trace("Entrando en ReportsSignature.signReport()");
	SignResponse sign = null;
	try {
	    // jSign.init(Configuration.JNDICONTEXT);
	    SignRequest rq = new SignRequest();
	    rq.setApplicationAlias(KeysRP.PFE_APP_ALIAS);
	    rq.setAttachmentType(KeysRP.PFE_ATTACHMENT_TYPE);
	    rq.setCertificateAlias(SIGNCERTALIAS);
	    rq.setFileData(pdfReportByteArray);
	    rq.setFileName(nameFile);
	    rq.setSignatureType(KeysRP.PFE_SIGN_TYPE);
	    rq.setMultisignatureType(KeysRP.PFE_MULTI_SIGN_TYPE);
	    rq.setPolicyId("PAdES_AGE_CSV");
	    sign = SignServiceImpl.sign(rq);
	    // CODIGO CSV IDENTIFICADOR DE LA FIRMA
	    LOG.info("CSV del fichero firmado: " +
		sign.getDocumentId());
	    // BYTES CON EL FICHERO FIRMADO
	   // pdfSignedReportByteArray = sign.getSignData();
	}
	catch (Exception exception) {
	    LOG.error(
		"Error al realizar la firma del informe", exception);
	    Utils.redirectToErrorPage(
		null, null, exception);
	}
	return sign;
    }
}