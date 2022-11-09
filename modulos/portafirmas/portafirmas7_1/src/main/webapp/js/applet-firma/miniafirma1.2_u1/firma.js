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
	$("#signatureToken").val(signature);
	$("#signatureTokenError").val(error);
	return true;
}

/**
 * Devuelve la cadena que habrá que pasarle al applet con el código del algoritmo
 * @param algorithm Código del arlgoritmo
 * @returns {String}
 */
function getAlgorithmParamByAlgorithm (algorithm) {
	var algorithmParam="";
	if (algorithm.indexOf("SHA") != -1) {
		algorithmParam = "SHA1withRSA";		
	}
	return algorithmParam;
}

/**
 * Devuelve la cadena que habrá que pasarle al applet en el parámetro del formato.
 * @param formato
 */
function getFormatParamByFormat (formato) {
	var formatParam = "";
	if (formato.indexOf("XADES IMPLICITO") != -1) {
		formatParam = "XAdES";
	} else if (formato.indexOf("XADES EXPLICITO") != -1) {
		formatParam = "XAdES";
	} else if (formato.indexOf("XADES ENVELOPING") != -1) {
		formatParam = "XAdES";
	} else if (formato.indexOf("XADES ENVELOPED") != -1) {		
		formatParam = "XAdES";
	} else if (formato.indexOf("CADES") != -1) {
		formatParam = "CAdES";
	} else if (formato.indexOf("PDF") != -1) {
		formatParam = "Adobe PDF";
	}
	return formatParam;
}


/**
 * Añade a la cadena "params" más parámetros, dependiendo del argumento "format"
 * @param params
 * @param formato
 * @returns el valor que tenía "params" más los parámetros añadidos.
 */
function addParamsByFormat (params, formato, mime) {
	
	if (formato.indexOf("XADES IMPLICITO") != -1) {		
		params = params + "\n" + "format=XAdES Detached";
		//params = params + "\n" + "mode=implicit";
		params = params + "\n" + "policyIdentifier=urn:oid:2.16.724.1.3.1.1.2.1.9";
		params = params + "\n" + "policyDescription=Política de firma electrónica para la Administración General del Estado";
		params = params + "\n" + "policyIdentifierHash=G7roucf600+f03r/o0bAOQ6WAs0=";
		params = params + "\n" + "policyIdentifierHashAlgorithm=http://www.w3.org/2000/09/xmldsig#sha1";		
		params = params + "\n" + "policyQualifier=https://sede.060.gob.es/politica_de_firma_anexo_1.pdf";
		params = params + "\n" + "mimeType=" + mime;
	} else if (formato.indexOf("XADES EXPLICITO") != -1) {
		params = params + "\n" + "format=XAdES Detached";
		params = params + "\n" + "mode=explicit";
		params = params + "\n" + "policyIdentifier=urn:oid:2.16.724.1.3.1.1.2.1.9";
		params = params + "\n" + "policyDescription=Política de firma electrónica para la Administración General del Estado";
		params = params + "\n" + "policyIdentifierHash=G7roucf600+f03r/o0bAOQ6WAs0=";
		params = params + "\n" + "policyIdentifierHashAlgorithm=http://www.w3.org/2000/09/xmldsig#sha1";
		params = params + "\n" + "policyQualifier=https://sede.060.gob.es/politica_de_firma_anexo_1.pdf";
	} else if (formato.indexOf("XADES ENVELOPING") != -1) {
		params = params + "\n" + "format=XAdES Enveloping";
		params = params + "\n" + "mimeType=" + mime;
		//params = params + "\n" + "mode=implicit";
	} else if (formato.indexOf("XADES ENVELOPED") != -1) {
		params = params + "\n" + "format=XAdES Enveloped";
		/*params = params + "\n" + "policyIdentifier=urn:oid:2.16.724.1.3.1.1.2.1.8";
		params = params + "\n" + "policyIdentifierHash=V8lVVNGDCPen6VELRD1Ja8HARFk=";
		params = params + "\n" + "policyIdentifierHashAlgorithm=http://www.w3.org/2000/09/xmldsig#sha1";
		params = params + "\n" + "policyQualifier=http://administracionelectronica.gob.es/es/ctt/politicafirma/politica_firma_AGE_v1_8.pdf";*/		
		params = params + "\n" + "mimeType=" + mime;
		//params = params + "\n" + "mode=implicit";
	} else if (formato.indexOf("CADES") != -1) {
		params = params + "\n" + "mode=implicit";
		params = params + "\n" + "policyIdentifier=2.16.724.1.3.1.1.2.1.9";
		params = params + "\n" + "policyIdentifierHash=G7roucf600+f03r/o0bAOQ6WAs0=";
		params = params + "\n" + "policyIdentifierHashAlgorithm=1.3.14.3.2.26";
		params = params + "\n" + "policyQualifier=https://sede.060.gob.es/politica_de_firma_anexo_1.pdf";	
		
	} else if (formato.indexOf("PDF") != -1) {		
		params = params + "\n" + "signatureSubFilter=ETSI.CAdES.detached";
		params = params + "\n" + "policyIdentifier=2.16.724.1.3.1.1.2.1.9";
		params = params + "\n" + "policyIdentifierHash=G7roucf600+f03r/o0bAOQ6WAs0=";
		params = params + "\n" + "policyIdentifierHashAlgorithm=1.3.14.3.2.26";		
		params = params + "\n" + "policyQualifier=https://sede.060.gob.es/politica_de_firma_anexo_1.pdf";
	}
	
	return params;	
}


/**
 * Añade a la cadena "params" más parámetros, dependiendo del argumento "format"
 * @param params
 * @param formato
 * @returns el valor que tenía "params" más los parámetros añadidos.
 */
function addParamsByFormatPolitica1_8 (params, formato, mime) {
	
	if (formato.indexOf("XADES IMPLICITO") != -1) {
		params = params + "\n" + "format=XAdES Detached";
		//params = params + "\n" + "mode=implicit";
		params = params + "\n" + "policyIdentifier=urn:oid:2.16.724.1.3.1.1.2.1.8";
		params = params + "\n" + "policyIdentifierHash=V8lVVNGDCPen6VELRD1Ja8HARFk=";		
		params = params + "\n" + "policyIdentifierHashAlgorithm=http://www.w3.org/2000/09/xmldsig#sha1";
		params = params + "\n" + "policyQualifier=http://administracionelectronica.gob.es/es/ctt/politicafirma/politica_firma_AGE_v1_8.pdf";
		params = params + "\n" + "mimeType=" + mime;
	} else if (formato.indexOf("XADES EXPLICITO") != -1) {
		params = params + "\n" + "format=XAdES Detached";
		params = params + "\n" + "mode=explicit";
		params = params + "\n" + "policyIdentifier=urn:oid:2.16.724.1.3.1.1.2.1.8";
		params = params + "\n" + "policyIdentifierHash=V8lVVNGDCPen6VELRD1Ja8HARFk=";		
		params = params + "\n" + "policyIdentifierHashAlgorithm=http://www.w3.org/2000/09/xmldsig#sha1";
		params = params + "\n" + "policyQualifier=http://administracionelectronica.gob.es/es/ctt/politicafirma/politica_firma_AGE_v1_8.pdf";
	} else if (formato.indexOf("XADES ENVELOPING") != -1) {
		params = params + "\n" + "format=XAdES Enveloping";
		params = params + "\n" + "mimeType=" + mime;
		//params = params + "\n" + "mode=implicit";
	} else if (formato.indexOf("XADES ENVELOPED") != -1) {
		params = params + "\n" + "format=XAdES Enveloped";
		/*params = params + "\n" + "policyIdentifier=urn:oid:2.16.724.1.3.1.1.2.1.8";
		params = params + "\n" + "policyIdentifierHash=V8lVVNGDCPen6VELRD1Ja8HARFk=";
		params = params + "\n" + "policyIdentifierHashAlgorithm=http://www.w3.org/2000/09/xmldsig#sha1";
		params = params + "\n" + "policyQualifier=http://administracionelectronica.gob.es/es/ctt/politicafirma/politica_firma_AGE_v1_8.pdf";*/		
		params = params + "\n" + "mimeType=" + mime;
		//params = params + "\n" + "mode=implicit";
	} else if (formato.indexOf("CADES") != -1) {
		params = params + "\n" + "mode=implicit";
		params = params + "\n" + "policyIdentifier=2.16.724.1.3.1.1.2.1.8";
		params = params + "\n" + "policyIdentifierHash=7SxX3erFuH31TvAw9LZ70N7p1vA=";
		params = params + "\n" + "policyIdentifierHashAlgorithm=1.3.14.3.2.26";
		params = params + "\n" + "policyQualifier=http://administracionelectronica.gob.es/es/ctt/politicafirma/politica_firma_AGE_v1_8.pdf";	
		
	} else if (formato.indexOf("PDF") != -1) {
	}
	
	return params;	
}

/**
 * Método que realiza el proceso de firma de una petición
 * @param datosPeticion Configuración de firma de la petición y datos a firmar
 */
function firmaPeticion(datosPeticion, validar){

	var numeroSerie = $('#serialNumber').val();
	
	// Se recuperan los documentos a firmar
	var datosAFirmar = datosPeticion.documentsConfig;

	var firmas = "{'signatureToken': '" + $("#signatureToken").val() + "', 'validarFirma': '" + validar+"', 'state': 'success', 'requestTagId': '" + datosPeticion.requestTagId + "', 'signatures': [";
	 
	var log = "";
	var error = "";
	
	
	
	for(var i=0; i < datosAFirmar.length; i++) {

		// Se recupera el documento a firmar
		var doc = datosAFirmar[i];
		
		// Algoritmo de firma
		var algParam = getAlgorithmParamByAlgorithm (doc.hashAlgorithm);
		
		// Formato de la firma
		var formatParam = getFormatParamByFormat (doc.format);

		// Operación de firma
		var operacion = doc.operation;

		// Modo de firma
		var binaryOrHash = doc.mode;

		// Filtro del certificado
		var paramsParam = "filter=qualified:" + numeroSerie + "\n" + "headless=true";

		// Otros parámetros
		paramsParam = addParamsByFormat (paramsParam, doc.format, doc.mime);

		// Datos a firmar
		var dataB64;
		
		// Si es un hash, lo obtenemos directamente. Si no, descargamos el b64 del fichero o de la firma
		if (binaryOrHash == 'HASH' && operacion == 'FIRMA') {
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
		
			if ((operacion == 'FIRMA') || (doc.format.indexOf("PDF") != -1)) {			
				try {
					firma = MiniApplet.sign(dataB64, algParam, formatParam, paramsParam);	
	
				} catch (e) {
					alert("Por favor, copie salida de la consola de Java en este punto");
					log = MiniApplet.getErrorMessage();
					error = "error";								
					i = datosAFirmar.length;
				}
			} else if (operacion == 'CONTRAFIRMA') {
	
				formatParam = "AUTO";
				paramsParam = paramsParam + "\n" + "target:leafs";
				try {
					firma = MiniApplet.counterSign(dataB64, algParam, formatParam, paramsParam);
				} catch (e) {
					alert("Por favor, copie salida de la consola de Java en este punto");
					log = MiniApplet.getErrorMessage();
					error = "error";
					i = datosAFirmar.length;					
				}
				
			} else if (operacion == 'COFIRMA') {
				try {
					formatParam = "AUTO";
					firma = MiniApplet.coSign(dataB64, null, algParam, formatParam, paramsParam);
				} catch (e) {
					alert("Por favor, copie salida de la consola de Java en este punto");
					log = MiniApplet.getErrorMessage();
					error = "error";
					i = datosAFirmar.length;				
				}
			}
		}

		// Si la operación de firma ha ido bien se devuelven las firmas
		if (("") == error) {
			firma = "{'docHash': '" + doc.documentHash + "', 'format': '" + doc.format + "', 'signature': '" + firma + "'}";
			firmas = firmas + firma;
			
			if (i < datosAFirmar.length-1){
				firmas = firmas + ',';
			}
		} else {
			firmas = "{'state': 'error', 'requestTagId': '" + datosPeticion.requestTagId + "', 'log': '" + log + "'}";
		}
	} // end for

	if (("") == error) {
		firmas = firmas + "]}";
	}

	return eval('(' + firmas + ')');
}