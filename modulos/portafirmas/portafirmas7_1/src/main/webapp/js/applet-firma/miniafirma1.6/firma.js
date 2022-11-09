	
var objetoFirmaPeticion = new Object();

var validarFirma;

/**
 * Método que realiza el proceso de firma de una petición
 * @param datosPeticion Configuración de firma de la petición y datos a firmar
 */
function firmaPeticion(request){
	
	var respuestaJSON = new Object();
	respuestaJSON.validarFirma = 1;
	respuestaJSON.state = 'success';
	respuestaJSON.requestTagHash = request.signatureConfig.requestTagHash;
	respuestaJSON.paralela = request.paralela;
	respuestaJSON.signatures = [];

	objetoFirmaPeticion.indice = 0;
	objetoFirmaPeticion.respuestaJSON = respuestaJSON;
	objetoFirmaPeticion.request = request;

	firmaDocumento(objetoFirmaPeticion.indice++);	
}

function firmaDocumento(indice){

	// Se recupera el documento a firmar
	var documento = objetoFirmaPeticion.request.signatureConfig.documentsConfig[indice];
	
	// Se agrega filtro del certificado. Como indicas un sólo certificado, no te lo vuelve a pedir.
	var paramsParam = documento.signParameter + "\nfilter=qualified:" + $('#serialNumber').val();
	// Datos a firmar
	var dataB64;

	// Si es un hash, lo obtenemos directamente. Si no, descargamos el b64 del fichero o de la firma
	if (documento.mode == 'HASH' && documento.operation == 'sign') {
		dataB64 = documento.data;
	} else {
		dataB64 = getRemoteDocumentJQuery(documento.data);
	}

	if(dataB64 == null) {
		 procesarErrorFirmaDocumento("Se agotó el tiempo para obtener el documento. Por favor, inténtelo de nuevo", objetoFirmaPeticion.respuestaJSON);
	} else {
		// Si se ha obtenido el documento, se realiza la operación de firma digital que corresponda
		if ((documento.operation == 'sign') || (documento.format.indexOf("PDF") != -1)) {
			
			if (objetoFirmaPeticion.request.lSignMarked){				
				paramsParam = paramsParam + "\nsignaturePositionOnPageLowerLeftX=5" + "\nsignaturePositionOnPageLowerLeftY=5" + 
				"\nsignaturePositionOnPageUpperRightX=550"+ "\nsignaturePositionOnPageUpperRightY=28" + 
				"\nsignaturePage=1" + "\nlayer2Text= Firmado por $$SUBJECTCN$$ el día $$SIGNDATE=dd/MM/yyyy$$" + 
				"\nlayer2FontSize=12" + "\nlayer2FontStyle=0";
			}

			MiniApplet.sign(dataB64, documento.hashAlgorithm, documento.signFormatParameter, paramsParam, signCorrect, signError);	

		} else if (documento.operation == 'countersign') {

			MiniApplet.counterSign(dataB64, documento.hashAlgorithm, 'AUTO', paramsParam, signCorrect, signError);

		} else if (documento.operation == 'cosign') {

			MiniApplet.coSign(dataB64, null, documento.hashAlgorithm, 'AUTO', paramsParam, signCorrect, signError);

		}
	}
}

function signCorrect(signatureB64, certificate){
	try {
		var arrayDocumentos = objetoFirmaPeticion.request.signatureConfig.documentsConfig;
		var documento = arrayDocumentos[objetoFirmaPeticion.indice-1];
		
		var firma = new Object();
		firma.docHash = documento.documentHash;
		firma.format = documento.format;
		firma.signature = signatureB64;
		
		var listaFirmas = objetoFirmaPeticion.respuestaJSON.signatures;
		listaFirmas[listaFirmas.length] = firma;
		
		// Se comprueba si hay que seguir firmando documentos de la petición
		if (objetoFirmaPeticion.indice == arrayDocumentos.length){
			saveSign(objetoFirmaPeticion.respuestaJSON);
		} else {
			firmaDocumento(objetoFirmaPeticion.indice++);	
		}
	} catch (e) {
		procesarErrorFirmaDocumento("Fallo inesperado al guardar la firma generada", objetoFirmaPeticion.respuestaJSON);
	}
}

function signError(errorType, errorMsg){
	var textoError = "";
	
	if (errorType == 'es.gob.afirma.core.AOCancelledOperationException') {
		textoError = "Operación cancelada.";			
	} else if (errorType == 'es.gob.afirma.keystores.common.AOCertificatesNotFoundException' || errorMsg.indexOf("SAF_19") != -1) {
		textoError = "No se ha encontrado certificado valido en su almacén.";
	} else if (errorType == 'es.gob.afirma.signers.xml.InvalidXMLException') {
		textoError = "Las firmas XAdES Enveloped solo pueden realizarse sobre datos XML.";
	} else if (errorType == 'es.gob.afirma.signers.pades.BadPdfPasswordException') {
		textoError = "El documento a firmar está protegido con contraseña.";
	} else if (errorType == 'es.gob.afirma.standalone.ApplicationNotFoundException') {
		textoError = "No se ha podido conectar con AutoFirma.";
	} else if (errorMsg.indexOf("SAF_09") != -1) {
		textoError = "Autofirma no consiguió realizar la firma eléctronica.";
	} else {
		textoError = "Fallo inesperado en el componente de firma: " + errorMsg + ".";
	}
	
	procesarErrorFirmaDocumento(textoError, objetoFirmaPeticion.respuestaJSON);
}

function getRemoteDocumentJQuery(remoteDocument) {
	var base64 = null;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "servlet/DescargaFicheroDeServidorServlet.htm?nombreDocumento=" + remoteDocument,
		dataType: 'text',
		async: false,
		success:
			function(contenido) {
				base64 = contenido;
				jsAjaxStatus.stopAjax();
	  		},
	  	error:
	  		function error(jqXHR, textStatus, errorThrown) {
	  		jsAjaxStatus.stopAjax();
	  	}
	});
	return base64;
}