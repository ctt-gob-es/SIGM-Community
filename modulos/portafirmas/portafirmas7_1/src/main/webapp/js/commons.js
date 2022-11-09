function sessionStorageServerGetVariable(nombre){
	return operacionesSessionServidor('getSessionStorageVariable', nombre, null);
}

function sessionStorageServerPutVariable(nombre, valor){
	return operacionesSessionServidor('addSessionStorageVariable', nombre, valor);
}

function operacionesSessionServidor(nombreOperacion, nombreVariable, valor){
	var retorno;
	
	var envio = {};
	
	envio.nombre = nombreVariable;
	envio.valor = valor;
	
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "sessionStorageServer/"+nombreOperacion,
		type: 'post',
		contentType: 'application/json',
		data: JSON.stringify(envio),
        dataType: 'text',
		async: false,
		success:
		function(data) {
			retorno = data;
		},
		error: function error(jqXHR, textStatus, errorThrown) {
        	retorno = null;
		}
	});
	jsAjaxStatus.stopAjax();
	return retorno;
}

/** FUNCIONES COMUNES DE JAVASCRIPT **/

var genericMessage = 'Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador';
var genericMessageSuccess = 'Operación realizada correctamente';

function addAttachFilesModalaceptar(){
	
	var objetoNuevosArchivosAnexos = JSON.parse(sessionStorage.nuevosArchivosAnexos);
	if(objetoNuevosArchivosAnexos==null 
			|| objetoNuevosArchivosAnexos==undefined
			|| objetoNuevosArchivosAnexos=="null"
			|| objetoNuevosArchivosAnexos=="undefined"){
		return;
	}
	
	var llaves = Object.keys(objetoNuevosArchivosAnexos);
	
	var objetoRequest = {};
	
	objetoRequest.peticionAsociada = $('#currentRequestHash').val();
	
	objetoRequest.listArchivoTemporalResponse = [];
	
	for(var i = 0; i<llaves.length; ++i){
		if(llaves[i] != "contador"){
			objetoRequest.listArchivoTemporalResponse.push(objetoNuevosArchivosAnexos[llaves[i]].referenciasEnElServidor);
		}
	}
	
	objetoRequest.comentarioDeAnexos = $('#descripcion_anexos').val(); 
	
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "nuevosAnexosPeticionController/saveFiles",
		type: 'post',
		contentType: 'application/json',
		data: JSON.stringify(objetoRequest),
        dataType: 'json',
		async: false,
		success:
		function(data) {
			$('#addAttachFilesModal').dialog("close");
			var objetoNuevosArchivosAnexos = null;
			objetoNuevosArchivosAnexos = {};
			objetoNuevosArchivosAnexos.contador = 0;

			sessionStorage.setItem("nuevosArchivosAnexos", JSON.stringify(objetoNuevosArchivosAnexos));

			sessionStorage.carpetaTemporalDeArchivosAsignada = null;
			
			var tablaFueraPopUp = $('#tablaDeAnexosSolicitudSeleccionada')[0];
			
			for(var i = 0; i<data.lista.length; ++i ){
				var filaFueraPopUp = tablaFueraPopUp.insertRow(tablaFueraPopUp.rows.length);
				var idFila = tablaFueraPopUp.rows.length -1;
				var idFilaString = 'visualizarAnexo'+idFila+'';
				filaFueraPopUp.id =  idFilaString;
				filaFueraPopUp.setAttribute("onclick","verAnexoRequest('"+data.lista[i].chash+"',"+idFila+");");
				
				var celdaFueraPopUp = filaFueraPopUp.insertCell(0);
				celdaFueraPopUp.innerHTML = '<ul class="buttonbar">'+
					'<li><a th:href="|${#httpServletRequest.getContextPath()}/servlet/DescargaDocumentoServlet?idDocument='+data.lista[i].chash+'" class="dsp_ib "><span '+
					'class="mf-icon mf-icon-downloadfile-16" th:title="#{downloadDocument}"></span> Documento </a></li>';
				if (data.lista[i].esTCNYTCNActivo) {
					celdaFueraPopUp.innerHTML = celdaFueraPopUp.innerHTML +
					'<a th:href="|${#httpServletRequest.getContextPath()}/servlet/DescargaTCNServlet?idDocument='+data.lista[i].chash+'" class="dsp_ib p4"><span '+
					'class="mf-icon mf-icon-downloadfile-16" th:title="#{downloadTCN}"></span> Documento TCN </a></li>';
				}
				if (data.lista[i].esFacturae) {
					celdaFueraPopUp.innerHTML = celdaFueraPopUp.innerHTML +
					'<a target="_blank" th:href="|${#httpServletRequest.getContextPath()}/servlet/DescargaFacturaeServlet?idDocument='+data.lista[i].chash+'" class="dsp_ib "><span '+
					'class="mf-icon mf-icon-downloadfile-16" th:title="#{downloadFacturae}"></span> Documento </a></li>';
				}
				celdaFueraPopUp.innerHTML = celdaFueraPopUp.innerHTML + '</ul>';

				celdaFueraPopUp = filaFueraPopUp.insertCell(0);
				celdaFueraPopUp.innerHTML = data.lista[i].comentarioAnexo;
				celdaFueraPopUp = filaFueraPopUp.insertCell(0);
				celdaFueraPopUp.innerHTML = data.lista[i].usuarioAnexo;
				celdaFueraPopUp = filaFueraPopUp.insertCell(0);
				celdaFueraPopUp.innerHTML = data.lista[i].tipoDeArchivo;
				celdaFueraPopUp = filaFueraPopUp.insertCell(0);
				celdaFueraPopUp.innerHTML = data.lista[i].nombreArchivo;
				visualizarAnexo (idFila);
			}
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$("#erroraddAttachFilesModal")[0].innerHTML = "Ha ocurrido un error enviando el archivo, por favor intentelo de nuevo y en caso que el error persista, contacte al administrador";
        	retorno = null;
		}
	});
	jsAjaxStatus.stopAjax();
}

function funcionAceptarPopUp(idComponente){
	window[idComponente+"aceptar"]();
}

function hideAllPopUps() {
	$("div.popupPortafirmas").each(function(i) {
		
		$('#'+this.id).dialog({
		    autoOpen: false,
		    resizable: false,
		    width: 600,
		    modal: true,       
		    buttons: [
		        {
		        	text: "Cancelar",       
		            "class": 'secondary',
		            click: function() {                   
		            	$(this).dialog("close");
		            },
		        },
		        {                   
		        	text: "Guardar",                   
		            "class": 'primary',
		            click: function() {
		            	funcionAceptarPopUp(this.id);
		            }
		        }           
		    ],   
		    create: customModalStyle,
		    close:function () {
		    	$('#error'+this.id).html('');
		    }
		});
		$('#'+this.id).dialog("close");
	});
}

function enviarFormulario(form, action, parameters) {
	// Se indica la acción a realizar
	$('#action').val(action);

	// Se insertan los valores requeridos en el formulario
	for (var i = 0; i < parameters.length; i++) {
		$('#'+parameters[i].parameterId).val(parameters[i].parameterValue);
	}
	
	// Se envía el formulario
	jQuery("#" + form).submit();

	// Devuelve false para que no se envíe el formulario original en caso de haberlo
	return false;
}

function downloadHandbook(handbookId) {
	jsAjaxStatus.startAjax();
	$('#handbookId').val(handbookId);
	jQuery('#handbookForm').submit();
	jsAjaxStatus.stopAjax();
}


// Refresca por ajax el contenido de un elemento
// Recibe como parámetro la url para recuperar el contenido, el id del 
// elemento y el queryString
// La url es relativa al contexto de la aplicación (Sin barra al principio)
function refrescaContenidoElemento(url, idElemento, queryString) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: url,
        	dataType: 'html',
        	data: queryString,
        	async: false,
        	success:
        		function(model) {
        			$("#" + idElemento).html(model);
          		},
          	error:
          		function error(jqXHR, textStatus, errorThrown) {
	          		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
	          		$('#error').show();
          		}
			});
	jsAjaxStatus.stopAjax();
}

// Prepara un botón para subir ficheros al servidor en la página en que se carga
function createFileUpload(idElemento, url, button, successFunction, errorFunction) {

	new qq.FineUploader({
		element: $('#' + idElemento)[0],
		request : {
			endpoint : url,
			inputName: 'file',
			forceMultipart: true,
			hideDropzones: false
		},
		text: {
			uploadButton: button
		},
		template: 'qq-template-manual-trigger',
		classes: {
			success: 'alert alert-success',
			fail: 'alert alert-error'
		},			
		debug : false,
		autoUpload: true,
		callbacks: {
			onSubmit: function(id, fileName) {

			},
			onComplete: function(id, fileName, responseJSON) {
				if (responseJSON.success) {
					successFunction(responseJSON.id, fileName);
				} else {
					errorFunction(id, fileName);
				}
			}
		}
	});
}

//Prepara un botón para subir ficheros al servidor y validarlos en la página en que se carga
function createFileUploadValidado(idElemento, url, button, successFunction, errorFunction) {

	new qq.FineUploader({
		element: $('#' + idElemento)[0],
		request : {
			endpoint : url,
			inputName: 'file',
			forceMultipart: true,
			hideDropzones: false
		},
		text: {
			uploadButton: button
		},
		template: 'qq-template-manual-trigger'
			,
		classes: {
			success: 'alert alert-success',
			fail: 'alert alert-error'
		},			
		debug : false,
		autoUpload: true,
		callbacks: {
			onSubmit: function(id, fileName) {

			},
			onComplete: function(id, fileName, responseJSON) {
				$("html, body").animate({ scrollTop: 0 }, 600);
				
				//Recuperamos las validaciones
				
				//Vamos a pintar aquí los mensajes de todas las validaciones
				var messageError = '';
     			var messageWarning = '';
     			var messageSuccess = '';
     			
     			if (responseJSON.applicationError != null) {
     				if (messageError)  {
 						messageError += "<br/>";
 					}
     				messageError += responseJSON.applicationErrorDescription;
     			}		
     			
     			if (responseJSON.validacionTamañoOk != null) {
     				if (responseJSON.validacionTamañoOk == "true") {
     					if (messageSuccess)  {
     						messageSuccess += "<br/>";
     					}
     					messageSuccess += responseJSON.validacionTamanioDescription;
     				} else {
     					if (messageError)  {
     						messageError += "<br/>";
     					}
     					messageError += responseJSON.validacionTamanioDescription;
     				}
     			}
     			
     			if (responseJSON.validacionVersionPDFOk != null) {
     				if (messageWarning)  {
 						messageWarning += "<br/>";
 					}
     				messageWarning += responseJSON.validacionVersionPDFDescripcion;
     			}
     			
     			if (responseJSON.validacionPDFAOk != null) {
     				if ($("#chkbox_1").prop("checked") && responseJSON.validacionPDFAOk == "true") {
     					if (messageSuccess)  {
     						messageSuccess += "<br/>";
     					}
     					messageSuccess += responseJSON.validacionPDFADescription;
     				} else if ($("#chkbox_1").prop("checked")) {
     					if (messageWarning)  {
     						messageWarning += "<br/>";
     					}
     					messageWarning += responseJSON.validacionPDFADescription;
     				}
     			}     			
     			
     			if (responseJSON.validacionFirmaOk != null) {
     				if (responseJSON.validacionFirmaOk == "true") {
     					if (messageSuccess)  {
     						messageSuccess += "<br/>";
     					}
     					messageSuccess += responseJSON.validacionFirmaDescription;
     				} else {
     					if (messageError)  {
     						messageError += "<br/>";
     					}
     					messageError += responseJSON.validacionFirmaDescription;
     				}
     			}
     			
     			if (messageSuccess) {
     				if ($('#success').is(":visible")){
     					var aux = $('#success').html();
     					aux += "<br/>";
     					aux += messageSuccess;
     					$('#success').html(aux);
     				} else {
     					$('#success').html(messageSuccess);
     				}
     				$('#success').show();
     				$('#success').delay(10000).hide(600);
     			}
     			
     			if (messageWarning) {
     				if ($('#warning').is(":visible")){
     					var aux = $('#warning').html();
     					aux += "<br/>";
     					aux += messageWarning;
     					$('#warning').html(aux);
     				} else {
     					$('#warning').html(messageWarning);
     				}
     				$('#warning').show();
     				$('#warning').delay(10000).hide(600);
     			}
     			
     			if (messageError) {
     				if ($('#error').is(":visible")){
     					var aux = $('#error').html();
     					aux += "<br/>";
     					aux += messageError;
     					$('#error').html(aux);
     				} else {
     					$('#error').html(messageError);
     				}
     				$('#error').show();
     				$('#error').delay(10000).hide(600);
     			}
				
				if (responseJSON.success) {
					successFunction(responseJSON.id, fileName);
				} else {
					errorFunction(id, fileName);
				}
			}
		}
	});
}


/**
 * Método que divide un campo a partir de un token
 * @param val Token
 */
function split( val ) {
	return val.split( /,\s*/ );
}

/**
 * Método que convierte un código RGB en su equivalente hexadecimal
 * @param colorval Código RGB
 */
function hexc(colorval) {
    var parts = colorval.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
    if (parts != null) {
	    delete(parts[0]);
	    for (var i = 1; i <= 3; ++i) {
	        parts[i] = parseInt(parts[i]).toString(16);
	        if (parts[i].length == 1) parts[i] = '0' + parts[i];
	    }
	    return parts.join('');
	}
    return '';
}

//Muestra el error oculto en el header
function showError(mensaje) {
	$('#error').html(mensaje);
	$("html, body").animate({ scrollTop: "0px" });
	$('#error').fadeIn(800).delay(3000).fadeOut(800);
}

//Muestra el aviso oculto en el header
function showWarning(mensaje) {
	$('#warning').html(mensaje);
	$("html, body").animate({ scrollTop: "0px" });
	$('#warning').fadeIn(800).delay(3000).fadeOut(800);
}

//Muestra el aviso oculto en el header
function showInfo(mensaje) {
	$('#info').html(mensaje);
	$("html, body").animate({ scrollTop: "0px" });
	$('#info').fadeIn(800).delay(3000).fadeOut(800);
}

//Procesa una lista para devolverla como cadena
function getErrors(errors) {
	var errorMessages = '';
	for(var i=0; i < errors.length; i++) {
		errorMessages += errors[i] + '<br/>';
	}
	return errorMessages;
}

function getErrorsFromErrorsAndWarnings(errorsAndWarnings) {
	var errors = errorsAndWarnings.errors
	var errorMessages = '';
	for(var i=0; i < errors.length; i++) {
		errorMessages += errors[i] + '<br/>';
	}
	return errorMessages;
}

function getWarningsFromErrorsAndWarnings(errorsAndWarnings) {
	var warnings = errorsAndWarnings.warnings
	var warningsMessages = '';
	for(var i=0; i < warnings.length; i++) {
		warningsMessages += warnings[i] + '\n';
	}
	return warningsMessages;
}



// Para personalizar los botones de las modales
function customModalStyle() {
    $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
    $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
}

//Obtiene el objeto que configura la acción de cerrar la modal
function getCloseButton() {
	return {
		text: "Cancelar",       
		"class": 'secondary',
		click: function() {$(this).dialog("close");}
	};
}

//Obtiene el objeto que configura la acción de cerrar la modal
function getNoButton() {
	return {
		text: "No",       
		"class": 'secondary',
		click: function() {$(this).dialog("close");}
	};
}

//Obtiene el objeto que configura la acción de cerrar la modal "aceptando"
function getAcceptButton() {
	return {
		text: "Aceptar",       
		"class": 'secondary',
		click: function() {$(this).dialog("close");}
	};
}

function deleteRow(rowid)  
{   
    var row = document.getElementById(rowid);
    row.parentNode.removeChild(row);
}

function cleanLoginForm(){
	var email = $("#invitedMail").val();
	$('#invitedForm')[0].reset();
	$("#invitedMail").val(email);
	$('#invitedMail').prop('readonly', true);
}

function validateInvitationMail() {
	var result = true;
	var email = $("#invitedMail").val();
	
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'guest/validateInvitationMail',
		type: 'get',
		dataType: 'json',
		data: 'email=' + email,
		async: false,
		success:
			function(retorno) {
				if(retorno.status == "success"){
		    		$('#divDni').css('display', 'block');
		    		$('#divUsername').css('display', 'block');
		    		$('#divSurname1').css('display', 'block');
		    		$('#divSurname2').css('display', 'block');
		    		$('#saveButton').css('display', '');
		    		$('#invitedMail').prop('readonly', true);
		    		$('#validateButton').css('display', 'none');
		    		$('#cleanButton').css('display', '');
				} else {
					$('#error').html(retorno.msgError);
					$('#error').show();
					$('#error').fadeIn(800).delay(3000).fadeOut(800);
					result = false;
				}
				jsAjaxStatus.stopAjax();
			},
	  	error:
	  		function error(jqXHR, textStatus, errorThrown) {
	       		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
	      		$('#error').show();
	      		$('#error').fadeIn(800).delay(3000).fadeOut(800);
	      		jsAjaxStatus.stopAjax();
		      	result = false;
	  		}
	});
	return result;
}

function validateInvitationFields() {
	var result = true;
	var dni = $("#invitedDni").val();
	var name = $("#invitedName").val();
	var surname1 = $("#invitedSurname1").val();
	var surname2 = $("#invitedSurname2").val();
	
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'guest/validateInvitationFields',
		dataType: 'json',
		data: 'dni=' + dni + '&name=' + name + '&surname1=' + surname1 + '&surname2=' + surname2,
		async: false,
		success:
			function(errors) {
				if(jQuery.isEmptyObject(errors)){
//		    		$('#divDni').css('display', 'block');
//		    		$('#divUsername').css('display', 'block');
//		    		$('#divSurname1').css('display', 'block');
//		    		$('#divSurname2').css('display', 'block');
//		    		$('#saveButton').css('display', '');
//		    		$('#invitedMail').prop('readonly', true);
					$('#invitedForm').submit();
				} else {
		    		$('#divDni').css('display', 'block');
		    		$('#divUsername').css('display', 'block');
		    		$('#divSurname1').css('display', 'block');
		    		$('#divSurname2').css('display', 'block');
		    		$('#saveButton').css('display', '');
		    		$('#validateButton').css('display', 'none');
		    		$('#invitedMail').prop('readonly', true);
		    		
		    		//Se muestra el primer error encontrado
					$('#error').html(errors[0]);
					$('#error').show();
					$('#error').fadeIn(800).delay(3000).fadeOut(800);
					result = false;
				}
				jsAjaxStatus.stopAjax();
			},
	  	error:
	  		function error(jqXHR, textStatus, errorThrown) {
	       		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
	      		$('#error').show();
	      		$('#error').fadeIn(800).delay(3000).fadeOut(800);
	      		jsAjaxStatus.stopAjax();
		      	result = false;
	  		}
	});
	return result;
}