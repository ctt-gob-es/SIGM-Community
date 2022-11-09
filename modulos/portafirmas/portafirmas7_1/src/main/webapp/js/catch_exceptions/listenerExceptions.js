var accionesEnServidor = {};

function validateNull(objeto){
	return objeto!=null 
	&& objeto!=undefined
	&& objeto!="null"
	&& objeto!="undefined";
}

function enviarErrorAServidor(envio){
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "logServer/logError",
		type: 'post',
		contentType: 'application/json',
		data: JSON.stringify(envio),
        dataType: 'text',
		async: true,
		success:
		function(data) {
			retorno = data;
		},
		error: function error(jqXHR, textStatus, errorThrown) {
        	retorno = null;
		}
	});
	jsAjaxStatus.stopAjax();
}

window.onerror = function(msg, url, line, col, error) {
	
	var accionesEnServidorLocal = accionesEnServidor;
	
	accionesEnServidor= {};

	if(accionesEnServidorLocal!=null 
			&& accionesEnServidorLocal!=undefined 
			&& accionesEnServidorLocal!="null" 
			&& accionesEnServidorLocal!="undefined" 
			&& accionesEnServidorLocal!={} ){
		
		var keys = Object.keys(accionesEnServidorLocal);
		
		if(keys.length!=0){
			var enviarAccionesAServidor = false;
			
			for(var i=0; i<keys.length; ++i){
				var llave = keys[i];
				if(validateNull(accionesEnServidorLocal[llave])){
					var accionEnServidor = accionesEnServidorLocal[llave];
					if(validateNull(accionEnServidor) && validateNull(accionEnServidor.notificarServidor)){
						enviarAccionesAServidor = enviarAccionesAServidor || accionEnServidor.notificarServidor;
						setTimeout(accionEnServidor.accionEnCliente(accionEnServidor.parametrosCliente), 50);
					}
				}
			}
			
			if(enviarAccionesAServidor){
				var objetoEnviar = {};
				objetoEnviar.accionesEnServidor = accionesEnServidorLocal;
				objetoEnviar.mensajeJS = msg;
				objetoEnviar.urlDeError = url;
				objetoEnviar.lineaDeError = line;
				objetoEnviar.columnaDeError = col;
				objetoEnviar.error = error;
				enviarErrorAServidor(objetoEnviar);
			}
			
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	}
};