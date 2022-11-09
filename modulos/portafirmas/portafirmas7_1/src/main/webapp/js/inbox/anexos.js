function eliminarArchinoDeLosNuevosAnexos(idDeArchivoAnexoDeLosNuevos){
	var objetoNuevosArchivosAnexos = JSON.parse(sessionStorage.nuevosArchivosAnexos);
	if(objetoNuevosArchivosAnexos==null 
			|| objetoNuevosArchivosAnexos==undefined
			|| objetoNuevosArchivosAnexos=="null"
			|| objetoNuevosArchivosAnexos=="undefined"
			|| objetoNuevosArchivosAnexos[idDeArchivoAnexoDeLosNuevos]==null 
			|| objetoNuevosArchivosAnexos[idDeArchivoAnexoDeLosNuevos]==undefined
			|| objetoNuevosArchivosAnexos[idDeArchivoAnexoDeLosNuevos]=="null"
			|| objetoNuevosArchivosAnexos[idDeArchivoAnexoDeLosNuevos]=="undefined"){
		return;
	}
	var retorno;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "nuevosAnexosPeticionController/removeFile",
		type: 'post',
		contentType: 'application/json',
		data: JSON.stringify(objetoNuevosArchivosAnexos[idDeArchivoAnexoDeLosNuevos].referenciasEnElServidor),
        dataType: 'json',
		async: false,
		success:
		function(data) {
			delete objetoNuevosArchivosAnexos[idDeArchivoAnexoDeLosNuevos];
			sessionStorage.setItem("nuevosArchivosAnexos",JSON.stringify(objetoNuevosArchivosAnexos));
			pintarListaDeArchivosNuevos();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$("#erroraddAttachFilesModal")[0].innerHTML = "Ha ocurrido un error removiendo el archivo, por favor intentelo de nuevo y en caso que el error persista, contacte al administrador";
        	retorno = null;
		}
	});
	jsAjaxStatus.stopAjax();
}

function pintarListaDeArchivosNuevos(){
	var objetoNuevosArchivosAnexos = JSON.parse(sessionStorage.nuevosArchivosAnexos);
	if(objetoNuevosArchivosAnexos==null 
			|| objetoNuevosArchivosAnexos==undefined
			|| objetoNuevosArchivosAnexos=="null"
			|| objetoNuevosArchivosAnexos=="undefined"){
		return;
	}
	var llaves = Object.keys(objetoNuevosArchivosAnexos);

	var tabla = document.createElement('table');
	
	var celda;
	var fila;
	
	for(var i = 0; i<llaves.length; ++i){
		if(llaves[i] != "contador"){
			if(objetoNuevosArchivosAnexos[llaves[i]] == null
					|| objetoNuevosArchivosAnexos[llaves[i]] == undefined
					|| objetoNuevosArchivosAnexos[llaves[i]] == "undefined"
					|| objetoNuevosArchivosAnexos[llaves[i]] == "null"
					|| objetoNuevosArchivosAnexos[llaves[i]].referenciasEnElServidor == null
					|| objetoNuevosArchivosAnexos[llaves[i]].referenciasEnElServidor == undefined
					|| objetoNuevosArchivosAnexos[llaves[i]].referenciasEnElServidor == "undefined"
					|| objetoNuevosArchivosAnexos[llaves[i]].referenciasEnElServidor == "null"){
				return;
			}
			fila = tabla.insertRow(0);
			celda = fila.insertCell(0);
			celda.innerHTML = '<a href="javascript:void(0)" onclick="eliminarArchinoDeLosNuevosAnexos('+llaves[i]+');" class="va_top right"><span class="mf-icon mf-icon-delete-16"></span></a>';
			celda = fila.insertCell(0);
			celda.innerHTML = objetoNuevosArchivosAnexos[llaves[i]].filename;
		}
	}
	
	$("#divListaAnexosNuevos")[0].innerHTML="";
	
	$("#divListaAnexosNuevos")[0].appendChild(tabla);
	
}

function enviarNuevoArchivoAnexoDePeticion(archivo){
	var retorno;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "nuevosAnexosPeticionController/addFile",
		type: 'post',
		contentType: 'application/json',
		data: JSON.stringify(archivo),
        dataType: 'json',
		async: false,
		success:
		function(data) {
			sessionStorage.carpetaTemporalDeArchivosAsignada = data.idCarpetaTemporal;
			jsAjaxStatus.stopAjax();
			retorno = data;
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$("#erroraddAttachFilesModal")[0].innerHTML = "Ha ocurrido un error anexando el archivo, por favor intentelo de nuevo y en caso que el error persista, contacte al administrador";
        	jsAjaxStatus.stopAjax();
        	retorno = null;
		}
	});
	return retorno;
}

function prepareNewAttachFile() {

	var objetoNuevosArchivosAnexos = null;
	objetoNuevosArchivosAnexos = {};
	objetoNuevosArchivosAnexos.contador = 0;

	sessionStorage.setItem("nuevosArchivosAnexos", JSON.stringify(objetoNuevosArchivosAnexos));

	sessionStorage.carpetaTemporalDeArchivosAsignada = null;
	
	pintarListaDeArchivosNuevos();
	
	$('#addAttachFilesModal').dialog("open");

}

function subirNuevoAnexo(idComponenteInput) {
	var inputFiles = $('#' + idComponenteInput);
	var listFiles = {};
	var readers = {};
	
	var objetoNuevosArchivosAnexos = JSON.parse(sessionStorage.nuevosArchivosAnexos);
	if(objetoNuevosArchivosAnexos==null 
			|| objetoNuevosArchivosAnexos==undefined
			|| objetoNuevosArchivosAnexos=="null"
			|| objetoNuevosArchivosAnexos=="undefined"){
		objetoNuevosArchivosAnexos = {};
		objetoNuevosArchivosAnexos.contador=0;
	}
	
	for (var i = 0; i < inputFiles[0].files.length; ++i) {
		objetoNuevosArchivosAnexos[objetoNuevosArchivosAnexos.contador] = {};
		
		objetoNuevosArchivosAnexos[objetoNuevosArchivosAnexos.contador].filename = inputFiles[0].files[i].name;
		objetoNuevosArchivosAnexos[objetoNuevosArchivosAnexos.contador].filetype = inputFiles[0].files[i].type;

		var varFile = inputFiles[0].files[i];

		objetoNuevosArchivosAnexos[objetoNuevosArchivosAnexos.contador].reader = new FileReader();
		
		objetoNuevosArchivosAnexos[objetoNuevosArchivosAnexos.contador].reader.indiceFichero = objetoNuevosArchivosAnexos.contador;

		objetoNuevosArchivosAnexos[objetoNuevosArchivosAnexos.contador].reader.onload = function(e) {
			
			var i = e.target.indiceFichero;
			
			var base64File = e.target.result;
			var indice = base64File.indexOf('base64');
			var archivoCompletoEnBase64 = base64File.substring(indice + 'base64'.length, base64File.length);
			if(sessionStorage.nuevosArchivosAnexos==null 
					|| sessionStorage.nuevosArchivosAnexos==undefined
					|| sessionStorage.nuevosArchivosAnexos=="null"
					|| sessionStorage.nuevosArchivosAnexos=="undefined"){
				sessionStorage.setItem("nuevosArchivosAnexos",JSON.stringify({}));
			}
			
			var objetoNuevosArchivosAnexos = JSON.parse(sessionStorage.nuevosArchivosAnexos);
			if(objetoNuevosArchivosAnexos==null 
					|| objetoNuevosArchivosAnexos==undefined
					|| objetoNuevosArchivosAnexos=="null"
					|| objetoNuevosArchivosAnexos=="undefined"){
				objetoNuevosArchivosAnexos = {};
				objetoNuevosArchivosAnexos.contador=0;
			}
			
			var archivoAEnviarAServidor = {};
			archivoAEnviarAServidor.nombreDeArchivo = objetoNuevosArchivosAnexos[i].filename;
			archivoAEnviarAServidor.contenidoDeArchivo = archivoCompletoEnBase64;
			archivoAEnviarAServidor.carpetaTemporal = sessionStorage.carpetaTemporalDeArchivosAsignada;
			archivoAEnviarAServidor.tipoArchivo = objetoNuevosArchivosAnexos[i].filetype;
			
			var referenciasServidor = enviarNuevoArchivoAnexoDePeticion(archivoAEnviarAServidor);
			
			if(referenciasServidor == null){
				$("#erroraddAttachFilesModal").innerHTML = "Hubo un error al procesar el archivo "+objetoNuevosArchivosAnexos[i].filename;
			}
			
			objetoNuevosArchivosAnexos[i].referenciasEnElServidor = referenciasServidor;
			
			sessionStorage.setItem("nuevosArchivosAnexos",JSON.stringify(objetoNuevosArchivosAnexos));
			
			pintarListaDeArchivosNuevos();
			
		};

		objetoNuevosArchivosAnexos[objetoNuevosArchivosAnexos.contador].reader.readAsDataURL(varFile);
		
//		objetoNuevosArchivosAnexos = listFiles;
		
		++objetoNuevosArchivosAnexos.contador;
		
		sessionStorage.setItem("nuevosArchivosAnexos",JSON.stringify(objetoNuevosArchivosAnexos));
		
	}
}
