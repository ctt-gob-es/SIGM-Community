/*
 * Fichero con las funciones comunes de firma realizada mediante miniapplet
 * por la Diputación de Ciudad Real.
 */

//var HOST = "http://10.12.200.200:8090";
var HOST = "http://comparece.dipucr.es:8080";
//var HOST_HTTPS = "https://10.12.200.200:4443";
var HOST_HTTPS = "https://comparece.dipucr.es:4443";
//var batchPreSignerUrl = HOST + "/agustin_afirma-server-triphase-signer/BatchPresigner";
//var batchPostSignerUrl = HOST + "/agustin_afirma-server-triphase-signer/BatchPostsigner";
var algoritmo = "SHA1withRSA";
var formatXades = "XAdES";
var paramsXades = "filters=nonexpired:\nsigningCert";
		

var metaSignB64 = "";
var firmaRealizada = false;


/**
 * Búsqueda del número de serie de entre los recuperados del usuario
 */ 
function buscarNumSerie(numsSerie,metaSignB64String) {
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
	var e={},i,b=0,c,x,l=0,a,r='',w=String.fromCharCode,L=s.length;
    	var A="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    	for(i=0;i<64;i++){e[A.charAt(i)]=i;}
    	for(x=0;x<L;x++){
       	c=e[s.charAt(x)];b=(b<<6)+c;l+=6;
        	while(l>=8){((a=(b>>>(l-=8))&0xff)||(x<(L-2)))&&(r+=w(a));}
    	}
    	return r;
}

/**
 * 
 * @param datosNotificacion
 * @returns
 */
function firmaBasicaXades(datosFormulario) {

	//alert("Datos a firma... \n"+datosNotificacion);	
	
	//MiniApplet.setStickySignatory(true);
				
	var datosXML = "<?xml version=\"1.0\" encoding=\"windows-1252\"?>\n"; 
	datosXML = datosXML + "<datosFormulario>\n" + datosFormulario + "</datosFormulario>" ;
			      

	var metaB64 = MiniApplet.getBase64FromText(datosXML, "default");

	//alert("metaB64: "+metaB64 );
	
	try{
		//alert(params);
		//var sign = function (dataB64, algorithm, format, params, successCallback, errorCallback) {
		metaSignB64 = MiniApplet.sign(metaB64, algoritmo, formatXades, paramsXades, saveSignatureCallback, showLogCallback);				

	}
	catch (e) {

		alert("ERROR AL REALIZAR LA FIRMA \n1. Borre el hist\u00f3rico del navegador.\n2. Cierre el navegador.\n3. Si le sigue apareciendo este mensaje ponga una incidencia en la siguiente direcci\u00f3n:\nhttps://sede.dipucr.es/soporte\nExplique con detalle el problema. Gracias y perdone las molestias.");
		
		return null;
	}
	
	return metaSignB64;
}


/**
 * Método al que se invoca cuando la firma funciona correctamente
 */
function saveSignatureCallback(signatureB64, certificateB64) {
	
	//alert("signatureB64-->"+signatureB64);
	//alert("certificateB64-->"+certificateB64);

	// Variable global que puede ser útil para saber el estado del proveso de
	// firma
	firmaRealizada = true;	
	document.getElementById('FIRMA').value = signatureB64;
	document.getElementById('certificadoDesc').value = certificateB64;
	document.forms[0].submit();	
	
}

/**
 * Método al que se invoca cuando hay un error en la firma
 */
function showLogCallback(errorType, errorMessage) {

	alert("ERROR AL REALIZAR LA FIRMA:\n"+errorMessage);

}

/**
 * Carga el miniapplet de firma en la dirección especificada
 */
function cargarMiniApplet(){
	//alert("MiniApplet va a cargar");
	//MiniApplet.cargarMiniApplet("http://comparece.dipucr.es:8080/miniapplet13");
		
	MiniApplet.cargarMiniApplet(HOST_HTTPS + "/afirma");	
	//MiniApplet.cargarAppAfirma(HOST_HTTPS + "/afirma", MiniApplet.KEYSTORE_WINDOWS);
	
	//alert("MiniApplet cargado");
}