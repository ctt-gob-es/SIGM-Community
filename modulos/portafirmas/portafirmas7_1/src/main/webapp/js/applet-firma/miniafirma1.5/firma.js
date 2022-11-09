	
var objetoFirmaPeticion = {indice:0, request:null, respuestaJSON:""};
var validarFirma;

/**
 * Método que realiza el proceso de firma de una petición
 * @param datosPeticion Configuración de firma de la petición y datos a firmar
 */
function firmaPeticion(request){
	
	objetoFirmaPeticion.indice = 0;
	objetoFirmaPeticion.request = request;
	objetoFirmaPeticion.respuestaJSON = "{'validarFirma': '" + 1 + "', 'state': 'success', 'requestId': '" + request.id + "', 'requestTagId': '" + request.signatureConfig.requestTagId + "', 'signatures': [";
		
	firmaDocumento(objetoFirmaPeticion.indice++);	
}

function firmaDocumento(indice){

	// Se recupera el documento a firmar
	var arrayDocumentos = objetoFirmaPeticion.request.signatureConfig.documentsConfig;
	var documento = arrayDocumentos[indice];
	
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
		 procesarErrorFirmaDocumento("Se agotó el tiempo para obtener el documento. Por favor, inténtelo de nuevo");
	} else {
		// Si se ha obtenido el documento, se realiza la operación de firma digital que corresponda
		if ((documento.operation == 'sign') || (documento.format.indexOf("PDF") != -1)) {
			
			if (objetoFirmaPeticion.request.lSignMarked){				
				paramsParam = paramsParam + "\nsignaturePositionOnPageLowerLeftX=5" + "\nsignaturePositionOnPageLowerLeftY=5" + 
				"\nsignaturePositionOnPageUpperRightX=65" + "\nsignaturePositionOnPageUpperRightY=25" + 
				"\nsignaturePage=1" + "\nlayer2Text=Firmado" + "\nlayer2FontSize=10" + "\nlayer2FontStyle=3";
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
		
		objetoFirmaPeticion.respuestaJSON += "{'docHash': '" + documento.documentHash + "', 'format': '" + documento.format + "', 'signature': '" + signatureB64 + "'}";
		
		// Se comprueba si hay que seguir firmando documentos de la petición
		if (objetoFirmaPeticion.indice == arrayDocumentos.length){
			objetoFirmaPeticion.respuestaJSON += "]}"
			saveSign(eval('(' + objetoFirmaPeticion.respuestaJSON + ')'), true);
		} else {
			objetoFirmaPeticion.respuestaJSON += ',';
			firmaDocumento(objetoFirmaPeticion.indice++);	
		}
	} catch (e) {
		procesarErrorFirmaDocumento("Fallo inesperado al guardar la firma generada");
	}
}

function signError(errorType, errorMsg){
	var textoError = "";
	
	if (errorType == 'es.gob.afirma.core.AOCancelledOperationException') {
		textoError = "Operación cancelada.";			
	} else if (errorType == 'es.gob.afirma.keystores.common.AOCertificatesNotFoundException') {
		textoError = "No se ha encontrado certificado en el almacén.";
	} else if (errorType == 'es.gob.afirma.signers.xml.InvalidXMLException') {
		textoError = "Las firmas XAdES Enveloped solo pueden realizarse sobre datos XML.";
	} else if (errorType == 'es.gob.afirma.signers.pades.BadPdfPasswordException') {
		textoError = "El documento a firmar está protegido con contraseña.";
	} else {
		textoError = "Fallo inesperado en el componente de firma: " + errorMsg + ".";
	}
	
	procesarErrorFirmaDocumento(textoError);
}

function procesarErrorFirmaDocumento(textoError) {
	$("#icon_" + objetoFirmaPeticion.request.id).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"No firmado\"/></span>");
	$("#log_" + objetoFirmaPeticion.request.id).html(textoError);

	desbloquearRegistroParaFirma(objetoFirmaPeticion.request.id);
	manageEndRequest();
}

function bloquearRegistroParaFirma(idPeticion){
	var retorno;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "sign/validarInicioDeFirma?idRequest="+idPeticion,
		type: 'get',
		async: false,
		success:
		function(data) {
			retorno = (data == "true");
		},
		error: function error(jqXHR, textStatus, errorThrown) {
        	retorno = false;
		}
	});
	jsAjaxStatus.stopAjax();
	return retorno;
}

function desbloquearRegistroParaFirma(idPeticion){
	var retorno;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "sign/desbloquearPeticionParaFirma?idRequest="+idPeticion,
		type: 'get',
		async: false,
		success:
		function(data) {
			retorno = (data == "true");
			if(retorno){
				quitarNotificacionServidor(llavesDeConfiguracionErroresJS.bloqueoFirma);
			}
		},
		error: function error(jqXHR, textStatus, errorThrown) {
        	retorno = false;
		}
	});
	jsAjaxStatus.stopAjax();
	
	return retorno;
}

function getRemoteDocumentJQuery(remoteDocument) {
	var document = null;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "servlet/DescargaFicheroDeServidorServlet.htm?nombreDocumento=" + remoteDocument,
		dataType: 'html',
		async: false,
		success:
			function(model) {
				document = model;
				jsAjaxStatus.stopAjax();
	  		},
	  	error:
	  		function error(jqXHR, textStatus, errorThrown) {
	  		jsAjaxStatus.stopAjax();
	  	}
	});
	return document;
}