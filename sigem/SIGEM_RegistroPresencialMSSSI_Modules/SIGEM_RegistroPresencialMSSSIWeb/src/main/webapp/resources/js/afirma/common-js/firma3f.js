/*
 * Fichero con las funciones comunes de firma 3 fases desarrolladas
 * por la Diputación de Ciudad Real.
 * 
 * Parametros globales de invocación al servidor de firma
 */

//var HOST = "http://10.12.200.200:8080";
//var HOST_HTTPS = "https://10.12.200.200:4443";
//var HOST = "http://172.20.1.149:8080";
//var HOST_HTTPS = "https://172.20.1.149:4443";
//var params = "filters=nonexpired:\nsigningCert";
var params = "";
var batchPreSignerUrl = "";
var batchPostSignerUrl = "";
var xmlBase64 = null;
var firmaRealizada = false;
var signResult = null;

/**
 * Carga el miniapplet de firma en la dirección especificada
 */
function cargarMiniApplet3f(host) {

	// alert("MiniApplet va a cargar");
	batchPreSignerUrl = host + "/afirma-server-triphase-signer/BatchPresigner";
	batchPostSignerUrl = host + "/afirma-server-triphase-signer/BatchPostsigner";
	
	MiniApplet.cargarAppAfirma(host + "/afirma", MiniApplet.KEYSTORE_WINDOWS);
	//MiniApplet.cargarAppAfirma(host + "/afirma", MiniApplet.KEYSTORE_WINDOWS);
	
	//QUITAN LA DEPENDENCIA DEL SERVIDOR INTERMEDIO 1/9/2015
	//MiniApplet.setServlets("http://" + HOST_HTTPS
	//		+ "/afirma-signature-storage-lotes/StorageService", "http://" + HOST_HTTPS
	//		+ "/afirma-signature-retriever-lotes/RetrieveService");

}

/**
 * Función que gestiona la llamada al método principal doSign Se añadirá un
 * cierto retraso en la llamada para que aparezca el gif "En proceso ..."
 */
function firmar3f() {

	// Tener en cuenta de que la invocación por protocolo no se produce hasta
	// que no acaba la llamada
	return doSign(document.getElementById('listaDocs').value, document.getElementById('serialNumber').value);

}

/*
 * Realizamos la firma trifásica de una serie de documentos. Obtenemos los
 * identificadores y los metadados de los documentos por parámetro mediante una
 * cadena con la forma:
 * DocId1:EntId1:META1$$DocId2:EntId2:META2$$...Idn:EntIdn:METAn Los DocId y los
 * EntId se pasarán en el paramentro "documentId" separados por ':'. Los META se
 * pasarán como datos adjuntos.
 */
function doSign(xml_lote, serialNumber) {
	
	try{
		// Llamada a la firma
		// signBatch = function (batchB64, batchPreSignerUrl, batchPostSignerUrl, params, successCallback, errorCallback)
		// batchB64, XML con el lote de firma en base 64
		// batchPreSignerUrl, url del despligue del servidor trifasico	
		// batchPostSignerUrl, url del despligue del servidor trifasico
		// params, solo indicar filtro de certificados, las demas opciones en el XML 
		xmlBase64 = MiniApplet.getBase64FromText(xml_lote);
		
		var seriales = serialNumber.split(";");
		
		if(seriales!=null)
		{		
			for (var a = 1; a < seriales.length+1; a++)
			{
				params = params + "filters."+a+"=qualified:" + seriales[a-1] + ";nonexpired:\n";				
			}
		}
		else
		{
			alert("NO TIENE CONFIGURADO EL CERTIFICADOS DE FIRMA, contacte con un administrador del sistema para que realice la configuración.");	
		}
				
		signResult = MiniApplet.signBatch(
															xmlBase64, 
															batchPreSignerUrl, 
															batchPostSignerUrl,
															params, 
															saveSignatureCallback,
															showLogCallback
															);		
		
		//alert("FIN DEL PROCESO DE FIRMA signResult" + signResult);
		return true;

	} catch (e) {
		try {
			alert("Error al firmar, si no puede firmar ponga una incidencia indicando con detalle el problema tiene, describa los mensajes de error. Acceda a la siguiente url para rellenar el formulario de la incidencia: https://sede.dipucr.es/soporte");					
		  return false;
		} catch (ex) {
			alert("Error: " + e);
		}
	}
	
}

/*
 * Método al que se invoca cuando la firma funciona correctamente
 */
function saveSignatureCallback(signatureB64) {

	// Variable global que puede ser útil para saber el estado del proveso de
	// firma
	firmaRealizada = true;

	// Compruebo que tengo datos antes de hacer el submit
	// alert(document.getElementsByName('listaDocs')[0].value);
	// alert("batchSignForm
	// getElement-->"+document.getElementById('codEntidad').value);
	// alert("batchSignForm
	// getElement-->"+document.getElementById('serialNumber').value);
	try {

		document.signForm.submit();

	} catch (ex) {
try{
		document.batchSignForm.submit();
}catch(ex2){
//	document.firmaCorrectaCallback(signatureB64,'');
//alert(signatureB64);
firmaCorrectaCallback(signatureB64,'');
}
	}
	
}


/*
 * Método al que se invoca cuando hay un error en la firma
 */
function showLogCallback(errorType, errorMessage) {

	alert("Error al firmar, si no puede firmar ponga una incidencia indicando con detalle el problema tiene, describa los mensajes de error. Acceda a la siguiente url para rellenar el formulario de la incidencia: https://sede.dipucr.es/soporte");
	//alert("ERROR AL REALIZAR LA FIRMA:" + errorMessage);	

}

/**
 * Función que espera un segundo y realiza comprobación
 */
function esperarAquefirme() {
	while (!firmaRealizada) {
		sleep(1000);
	}
	// document.signForm.submit();
	document.signForm.submit();
	return "success";
}

/**
 * Búsqueda del número de serie de entre los recuperados del usuario
 */
function buscarNumSerie(numsSerie, metaSignB64String) {

	var numsSerieArray = numsSerie.split(";");
	for (var a = 0; a < numsSerieArray.length; a++) {
		var n = metaSignB64String.search(numsSerieArray[a]);
		if (n > -1) {
			return true;
		}
	}
	return false;
}

/**
 * Función para decodificar firma xades
 */
function decode(s) {

	var e = {}, i, b = 0, c, x, l = 0, a, r = '', w = String.fromCharCode, L = s.length;
	var A = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	for (i = 0; i < 64; i++) {
		e[A.charAt(i)] = i;
	}
	for (x = 0; x < L; x++) {
		c = e[s.charAt(x)];
		b = (b << 6) + c;
		l += 6;
		while (l >= 8) {
			((a = (b >>> (l -= 8)) & 0xff) || (x < (L - 2))) && (r += w(a));
		}
	}
	return r;
}

/**
 * Función para poner en espera proceso
 */
function sleep(delay) {
	var start = new Date().getTime();
	while (new Date().getTime() < start + delay)
		;
}