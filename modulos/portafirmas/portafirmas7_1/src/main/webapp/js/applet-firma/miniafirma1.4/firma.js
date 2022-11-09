function bloquearRegistroParaFirma(idDePeticion){
	var retorno;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "sign/validarInicioDeFirma?idRequest="+idDePeticion,
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


function desbloquearRegistroParaFirma(idDePeticion){
	var retorno;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "sign/desbloquearPeticionParaFirma?idRequest="+idDePeticion,
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

function funcionDespuesDeRetornoFirmaPeticion(datosAdicionales){
	
	var i = datosAdicionales.indice;
	var request = datosAdicionales.request;
	
	var resultadoFirma =  eval('(' + datosAdicionales.firmasFirmaJS + ')');
	
	if (resultadoFirma.state == "success") {
		saveSign(resultadoFirma);
		var firmaAutomaticaRedaccion = sessionStorage.firmaAutomaticaRedaccion;
		if(firmaAutomaticaRedaccion!=null && firmaAutomaticaRedaccion!="null" && firmaAutomaticaRedaccion!=undefined && firmaAutomaticaRedaccion!="undefined" &&
				(firmaAutomaticaRedaccion==true || firmaAutomaticaRedaccion=="true")){
			deleteRow("r_"+request.id);
			sessionStorage.setItem("firmaAutomaticaRedaccion",false);
			var urlPath = window.location.protocol + "//" + window.location.host + window.location.pathname;
			window.history.pushState({},"", urlPath);
		}
	} else {
		$("#icon_" + request.id).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"No firmado\"/></span>");
		$("#log_" + request.id).html(resultadoFirma.log);
		manageEndRequest();
	}
}

function finishMethodAfterSignFirmaJS(datosAdicionales){
	
	var i = datosAdicionales.indice;
	var request = datosAdicionales.request;
	
	var datosAFirmar = datosAdicionales.datosAFirmarCallBackAfirma;
	var error = "";
	var log = "";
	
	var errorGeneralCiclo = datosAdicionales.errorGeneralCicloFirmaJS;
	
	var errorWait = datosAdicionales.errorWait;
	
	var cuantosHanTerminado = datosAdicionales.cuantosHanTerminadoFirmaJS;
	
	++cuantosHanTerminado;
	
	datosAdicionales.cuantosHanTerminadoFirmaJS = cuantosHanTerminado;
	
	if( !("" == errorGeneralCiclo || null == errorGeneralCiclo || undefined == errorGeneralCiclo  || "null" == errorGeneralCiclo || "undefined" == errorGeneralCiclo ) ){
		log = datosAdicionales.errorGeneralCiclo;
		error = "error";								
		i = datosAFirmar.length;		
	}
	
	if( !("" == errorWait || null == errorWait || undefined == errorWait  || "null" == errorWait || "undefined" == errorWait ) ){
		log = datosAdicionales.errorWait;
		error = "error";								
		i = datosAFirmar.length;		
	}
	
	var firma = datosAdicionales.signatureWait;
	
	var firmas = datosAdicionales.firmasFirmaJS;
	
	var doc = datosAFirmar[i];
	
	if (("") == error) {
		firma = "{'docHash': '" + doc.documentHash + "', 'format': '" + doc.format + "', 'signature': '" + firma + "'}";
		firmas = firmas + firma;
		
		if (i < datosAFirmar.length-1){
			firmas = firmas + ',';
		}
	} else {
		firmas = "{'state': 'error', 'requestTagId': '" + datosAdicionales.datosPeticion.requestTagId + "', 'log': '" + log + "'}";
	}
	
	datosAdicionales.errorGenerarCicloFirmaJS = error;
	
	datosAdicionales.firmasFirmaJS = firmas;
	
	if(!(cuantosHanTerminado < datosAFirmar.length)){
		if (("") == error) {
			firmas = firmas + "]}";
			datosAdicionales.firmasFirmaJS=firmas;
		}
		
		funcionDespuesDeRetornoFirmaPeticion(datosAdicionales);
		
		desbloquearRegistroParaFirma(datosAdicionales.request.id);

	}
	else{
		firmaPeticion(datosAdicionales.datosPeticion, datosAdicionales.request, cuantosHanTerminado, datosAdicionales);
	}
	
}

/**
 * Permite cargar el applet sin que se quede la página en blanco.
 * Para llamarlo:
 * 
 * docWriteWrapper(function () {MiniApplet.cargarMiniApplet(baseDownloadURL, null);});
 * 
 * 
 * @param func
 */
function docWriteWrapper(func) {	
    var writeTo = document.createElement('del'),
        oldwrite = document.write,
        content = '';
    writeTo.id = "me";
    document.write = function(text) {
        content += text;
    };
    func();
    writeTo.innerHTML += content;
    document.write = oldwrite;
    document.body.appendChild(writeTo);
}

/**
 * Método que recupera un documento del servidor mediante AJAX
 * @param remoteDocument URL del documento remoto
 * @returns Documento
 */
function getRemoteDocument (remoteDocument) {
	var document = null;
	var xhr = getAjaxRequest();
	 
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 /*&& xhr.status == 200*/) {
			document =  xhr.responseText;            
	    }
	};
    xhr.open('GET', remoteDocument, false);
    xhr.send(null);
    return document;
}
// Para sustituir getRemoteDocument
function getRemoteDocumentJQuery(remoteDocument) {
	var document = null;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: remoteDocument,
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
	


/**
 * Obtiene la petición AJAX
 * @returns 
 */
function getAjaxRequest () {
	var xhr = null;
	// Firefox
	if (window.XMLHttpRequest) {
	     xhr = new XMLHttpRequest();
	// IE
	} else if (window.ActiveXObject) {
		xhr =  new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xhr;
}


// Método que recupera los campos de la operación de autenticación por certificado
function setSignatureToken(signature, error) {
	sessionStorageServerPutVariable("tokenSignature",signature);
	sessionStorageServerPutVariable("signatureTokenError",error);
//	$("#signatureToken").val(signature);
//	$("#signatureTokenError").val(error);
	return true;
}

/**
 * Método que realiza el proceso de firma de una petición
 * @param datosPeticion Configuración de firma de la petición y datos a firmar
 */
function firmaPeticion(datosPeticion, request, indice, datosAdicionalesDeRetorno){

	if(indice == null || indice == undefined || indice == "null" || indice == "undefined"){
		indice=0;
	}

	if(datosAdicionalesDeRetorno == null || datosAdicionalesDeRetorno == undefined || datosAdicionalesDeRetorno == "null" || datosAdicionalesDeRetorno == "undefined"){
		datosAdicionalesDeRetorno = {};
	}

	var i = indice;

	var numeroSerie = sessionStorageServerGetVariable('serialNumber');

	var datosAFirmar = datosPeticion.documentsConfig;

	var firmas;

	var log = "";
	var error = datosAdicionalesDeRetorno.errorGenerarCicloFirmaJS;

	if(i==0){


		// Se recuperan los documentos a firmar

		firmas = "{'state': 'success', 'requestTagId': '" + datosPeticion.requestTagId + "', 'signatures': [";

		datosAdicionalesDeRetorno.firmasFirmaJS=firmas;

		log = "";
		error = "";

		datosAdicionalesDeRetorno.cuantosHanTerminadoFirmaJS=0;
	}

	// Se recupera el documento a firmar
	var doc = datosAFirmar[i];

	// Se agrega filtro del certificado
	var paramsParam = doc.signParameter + "\nfilter=qualified:" + numeroSerie;

	// Datos a firmar
	var dataB64;

	// Si es un hash, lo obtenemos directamente. Si no, descargamos el b64 del fichero o de la firma
	if (doc.mode == 'HASH' && doc.operation == 'FIRMA') {
		dataB64 = doc.data;
	} else {
		dataB64 = getRemoteDocument(doc.data);
	}

	if(dataB64 == null) {
		error = "error";
		log = "Se agotó el tiempo para obtener el documento. Por favor, inténtelo de nuevo";
		i = datosAFirmar.length;					
	}

	if (("") == error) {
		var firma = "";		

		datosAdicionalesDeRetorno.datosAFirmarCallBackAfirma = datosAFirmar;
		datosAdicionalesDeRetorno.errorGeneralCicloFirmaJS = error;

		datosAdicionalesDeRetorno.indice = i;
		datosAdicionalesDeRetorno.request = request;
		datosAdicionalesDeRetorno.datosPeticion = datosPeticion;
		datosAdicionalesDeRetorno.finishMethodCallBackAfirma = "finishMethodAfterSignFirmaJS";

		if ((doc.operation == 'sign') || (doc.format.indexOf("PDF") != -1)) {	

			MiniApplet.sign(dataB64, doc.hashAlgorithm, doc.signFormatParameter, paramsParam, signCorrect, signError, datosAdicionalesDeRetorno);	

		} else if (doc.operation == 'countersign') {

			MiniApplet.counterSign(dataB64, doc.hashAlgorithm, 'AUTO', paramsParam, signCorrect, signError, datosAdicionalesDeRetorno);

		} else if (doc.operation == 'cosign') {

			MiniApplet.coSign(dataB64, null, doc.hashAlgorithm, 'AUTO', paramsParam, signCorrect, signError, datosAdicionalesDeRetorno);

		}
	}
}