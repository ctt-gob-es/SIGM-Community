function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}

function signCorrect(signatureB64, certificate, additionalReturns){
	var error = "";
	var errorType = "";
	var objetoFinishMethodCallBackAfirma = additionalReturns.finishMethodCallBackAfirma;
	//alert("Este es el metodo que se ejecuta cuando la firma termina bien: " + signatureB64);
	
	additionalReturns.signatureWait = signatureB64;
	additionalReturns.errorWait = error;
	additionalReturns.errorTypeWait = errorType;
	additionalReturns.waitAFirma = false;
	
	window[objetoFinishMethodCallBackAfirma](additionalReturns);
}

function signError(errorType, errorMsg, additionalReturns){
	var error = "";
	var signatureB64;
	var objetoFinishMethodCallBackAfirma = additionalReturns.finishMethodCallBackAfirma;
	
	//alert("Este es el metodo que se ejecuta cuando la firma falla: " + errorType + " - " + errorMsg);
	
	additionalReturns.signatureWait = signatureB64;
	additionalReturns.errorWait = errorMsg;
	additionalReturns.errorTypeWait = errorType;
	additionalReturns.waitAFirma = false;

	if (errorType == 'es.gob.afirma.core.AOCancelledOperationException') {
		error = "operationCancelled";			
	} else if (errorType == 'es.gob.afirma.keystores.common.AOCertificatesNotFoundException') {
		error = "No se ha encontrado certificado en el almacen";
	} else {
		error = errorMsg;
	}
	
	additionalReturns.errorWait = error;
	
	window[objetoFinishMethodCallBackAfirma](additionalReturns);
}

// Autenticación mediante certificado
// Se firma un token aleatorio y se obtiene el certificado de la firma
function end_authentication(callback_method, finisMethod, modal)
{
	var format = "XAdES";
	var algorithm = "SHA1withRSA";
	// token aleatorio
	var text = "" +Math.random()*11;
	var signature;
	var error = "";
	
	var errorAfirma;
	var errorType;
	
	try {
		var dataB64 = MiniApplet.getBase64FromText (text, null);
		
		var datosAdicionalesDeRetorno = {};
		
		datosAdicionalesDeRetorno.metodoCallBackAfirma = callback_method;
		datosAdicionalesDeRetorno.finishMethodCallBackAfirma = finisMethod;
		datosAdicionalesDeRetorno.modalCloseAfterSign = modal;
		
		MiniApplet.sign(dataB64, algorithm, format, null, signCorrect, signError, datosAdicionalesDeRetorno);
		//setTimeout(function(){ alert('termina el timeout'); }, 60000);
		//signature = methodSyncSign(dataB64, algorithm, format, null, errorAfirma, errorType);
		
//		sessionStorage.setItem("metodoCallBackAfirma",JSON.stringify(callback_method));
//		sessionStorage.setItem("finishMethodCallBackAfirma",finisMethod);
//		sessionStorage.setItem("modalCloseAfterSign",modal);
	} catch (e) {
		if (MiniApplet.getErrorType() == 'es.gob.afirma.core.AOCancelledOperationException') {
			error = "operationCancelled";			
		} else if (MiniApplet.getErrorType() == 'es.gob.afirma.keystores.common.AOCertificatesNotFoundException') {
			error = "No se ha encontrado certificado en el almacen";
		} else {
//			alert("Por favor, copie salida de la consola de Java en este punto");
			error = MiniApplet.getErrorMessage();
		}
	}
//	eval(callback_method+"(signature,error);");

}


function authenticateEnFirma(numSerieCertificado, callback_method, finisMethod, modal) {
	if (numSerieCertificado == null || numSerieCertificado == '') {
		authenticate(callback_method, finisMethod, modal);
	} else {
		return true;
	}
}

function authenticate(callback_method, finisMethod,modal)
{	
	end_authentication(callback_method,finisMethod,modal);
}

