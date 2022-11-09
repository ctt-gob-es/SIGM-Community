function accionDeErrorEnFirmaDeUsuario(parametros){
	$('.cancel_sign').css('display', 'true');
	$('.start_sign').css('display', 'true');
	$('.finish_sign').css('display', 'none');
	$('.cancel_sign').attr('disabled', false);
	$('.start_sign').attr('disabled', false);
	$('.start_sign').attr('style', "");
	$('.cancel_sign').attr('style', "");
	$("#icon_" + $(parametros.request).attr("id")).html("");
}

//contador de la peticiones que se han completado
var nRequestComplete = 0;
//contador de la peticiones que se han lanzado
var nRequestLaunch = 0;
//lista de peticiones a tramitar
var requests;
//Indica si ya se cargaron los comentarios
var isLoadComments = false;

var idRequestToSign = getParameterByName('idRequestToSign');

var errors = $('#errorMessage').val();

//Método que carga los datos de una petición
function loadRequest(requestTagId, requestIndex) {
	jsAjaxStatus.startAjax();
	var requestTagIdParam = "requestTagId=" + requestTagId;
	var requestBarSelectedParam = '&requestBarSelected=' + $('#requestBarSelected').val();
	var currentRequestParam = '&currentRequest=' + (parseInt(requestIndex) + 1);
	var pageSizeParam = '&pageSize=' + $('#pageSize').val();
	var inboxSizeParam = '&inboxSize=' + $('#inboxSize').val();
	var currentPageParam = '&currentPage=' + $('#currentPage').val();
	isLoadComments = false;
	
	var _ctx = $("meta[name='ctx']").attr("content") + "/";

	$.ajaxSetup({cache: false});
	$.ajax({url: _ctx + "inbox/loadRequest",
		dataType: 'html',
		data: requestTagIdParam + requestBarSelectedParam + currentRequestParam + pageSizeParam + inboxSizeParam + currentPageParam,
		async: false,
		success:
			function(model) {
			$('#msgBox').html(model);
			$('#detallePeticion_modal').dialog("open");
			// se invoca la visualización del primer documento
			visualizarDocumento(0);
			$(".row" + requestIndex).removeClass("new");
			$("#msgTable > tbody > tr").removeClass("ui-selected");
			$(".row" + requestIndex).addClass("ui-selected");
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	});

};

//Método que prepara la configuración de las peticiones a firmar
function fireSign(requestsIds) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "fire/getFIReTransaction",
		dataType: 'text',
		data: "requestsIds=" + requestsIds,
		async: false,
		success:
			function(url) {					
				location.href = url;		
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error al firmar con FIRe. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	});
};

//Método que carga los datos de una petición
function loadRequestDocelWeb(url, idElemento, queryString, requestIndex) {
	jsAjaxStatus.startAjax();
	var requestBarSelectedParam = '&requestBarSelected=' + $('#requestBarSelected').val();
	var currentRequestParam = '&currentRequest=' + (parseInt(requestIndex) + 1);
	var pageSizeParam = '&pageSize=' + $('#pageSize').val();
	var inboxSizeParam = '&inboxSize=' + $('#inboxSize').val();
	var currentPageParam = '&currentPage=' + $('#currentPage').val();
	isLoadComments = false;
	$.ajaxSetup({cache: false});
	$.ajax({url: url,
		dataType: 'html',
		data: queryString + requestBarSelectedParam + currentRequestParam + pageSizeParam + inboxSizeParam + currentPageParam,
		async: false,
		success:
			function(model) {
			$("#" + idElemento).html(model);
			$(".row" + requestIndex).removeClass("new");
			$("#msgTable > tbody > tr").removeClass("ui-selected");
			$(".row" + requestIndex).addClass("ui-selected");
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	});

};

/**
 * Método que carga la petición anterior
 */
function loadPreviousRequest() {
	var currentRequestPosition = $('#selectedRequestPosition').val();
	var previousPosition = currentRequestPosition - 2;
	// Se obtiene el código de petición, formato "r_códigoPetición", para ello, se quitan los dos primeros caracteres
	var previousRequestId = $(".row" + previousPosition).attr("id").substring(2) ;
	var currentPage = $('#currentPage').val();

	// Si la siguiente petición está fuera de la página actual se debe paginar y recuperarla
	if (parseInt(previousPosition) < 0) {
		$('#requestPagination').val("previous");
		enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'currentPage', parameterValue: parseInt(currentPage) - 1}]);
	} else {
		$('#requestPagination').val("");
		loadRequest(previousRequestId, previousPosition);
	}
};

/**
 * Método que carga la siguiente petición
 */
function loadNextRequest() {
	var currentRequestPosition = $('#selectedRequestPosition').val();
	var nextPosition = currentRequestPosition;
	// Se obtiene el código de petición, formato "r_códigoPetición", para ello, se quitan los dos primeros caracteres
	var nextRequestId = $(".row" + nextPosition).attr("id").substring(2) ;
	var pageSize = $('#pageSize').val();
	var currentPage = $('#currentPage').val();

	// Si la siguiente petición está fuera de la página actual se debe paginar y recuperarla
	if (parseInt(nextPosition) == parseInt(pageSize)) {
		$('#requestPagination').val("next");
		enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'currentPage', parameterValue: parseInt(currentPage) + 1}]);
	} else {
		$('#requestPagination').val("");
		loadRequest(nextRequestId, nextPosition);
	}
};

function esFirefoxAntiguo() {
	var posicion = navigator.userAgent.toLowerCase().indexOf("firefox");
	var esFirefox = posicion != -1;
    var esAntiguo = false;
    if(esFirefox) {
    	var version = navigator.userAgent.substring(posicion + 8, navigator.userAgent.length) ;
        esAntiguo = parseFloat(version) < 43;
    }
    return esFirefox && esAntiguo;
}

//Método que prepara la configuración de las peticiones a firmar
//Cuando se abra la ventana modal se cargan los datos de las peticiones que se desea firmar
function loadRequestToSign(requestsIds) {
	if (esFirefoxAntiguo()) {
		$('#warning').html('No se permite firmar con Firefox anterior a la versión 43.0');
		$('#warning').show();
		return;
	}
	jsAjaxStatus.startAjax();
	//No cargamos el MiniApplet por tema de licencias no se pueden usar applets de java sin pago
	//MiniApplet.cargarMiniApplet(baseDownloadURL, null);
	MiniApplet.cargarAppAfirma(baseDownloadURL, null);
	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/loadRequestsToSign",
		dataType: 'json',
		data: requestsIds,
		async: false,
		success:
			function(model) {
			// Se cargan las peticiones para que cuando se vayan a firmar, esté disponible la configuración de firma
			requests = model;
			var table = $("#requestsToSignTable > tbody");        			
			$.each(model, function (i, request) {
				table.append("<tr id=" + request.requestTagHash + " class=\"tosign\"></tr>");
				$('#' + request.requestTagHash).append("<td>" + request.subject + "</td>" +
						"<td>" + request.sender + "</td>");
				if (request.signatureConfig.type!="VISTOBUENO"  && request.signatureConfig.documentsConfig[0].xades) {
						$('#' + request.requestTagHash).append("<td><input th:value=\"${request.comentarioXades}\" disabled=\"disabled\"  style=\"visibility:hidden\" id=\"coment_" + request.requestTagHash + "\" class=\"w10\" type=\"text\"/><span id=\"comenticon_" + request.requestTagHash + "\" class=\"mf-icon mf-icon-comment-16\" style=\"visibility:hidden\"></span><a href=\"#\" onclick=\"prepareNewComentarioXades(\'"+ request.requestTagHash +"\',"+i+");\" title=\"Agregar Comentario a firma XADES\"><span class=\"mf-icon mf-icon-add-16\"></span></a></td>");
				} else {
					$('#' + request.requestTagHash).append("<td></td>");
				}
				$('#' + request.requestTagHash).append("<td id=\"icon_" + request.requestTagHash + "\"><span class=\"\"></span></td>" +
						"<td id=\"log_" + request.requestTagHash + "\"></td>");
				if(request.signatureConfig.type == "ERROR") {
					$("#icon_" + request.requestTagHash).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"No se firmará\"/></span>");
					$("#log_" + request.requestTagHash).html(request.signatureConfig.errorMessage);
				}
				if (!request.permitirTrifasica) {
					mostrarClickTrifasica = false;
				} 
				$(".validar").prop('checked', request.paramUser);
			});
			$('#sign_modal').dialog('open');
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	});
};

//Funcion para agregar comentario a una firma XADES
function prepareNewComentarioXades(identificadorRequestParaComentario, numpeticion) {
	var comentarioAnterior = $('#coment_'+identificadorRequestParaComentario).val();
	if (comentarioAnterior!=null){
		$('#comentario_xades').val(comentarioAnterior);	
	} else {
		$('#comentario_xades').val('');		
	}
	$('#comentario_xades_id').val(identificadorRequestParaComentario);
	$('#comentario_xades_num_peticion').val(numpeticion);
	
	$('#comentario_xades_modal').dialog('open');
}

function configComentarioXadesModal() {
	$("#comentario_xades_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    //height:450,
	    width: 500,
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
	  	            	//saveSeat(baseUrl, funcionDeRecarga);
	  	            	//Tengo que actualizar el input de fuera
	  	            	var id = $('#comentario_xades_id').val();
	  	            	var numPeticion = $('#comentario_xades_num_peticion').val();
	  	            	var comentario = $('#comentario_xades').val();
	  	            	$('#coment_'+id).val(comentario);
	  	            	//$('#coment_'+id).css('visibility', 'visible');
	  	            	$('#comenticon_'+id).css('visibility', 'visible');
	  	            	var peticionAModificar = requests[numPeticion];
	  	            	peticionAModificar.comentarioXades = comentario;
	  	            	$(this).dialog("close");
	  	            }
	  	        }
	  	    ],
	  	    
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	    open: function(){	
	    },
	    close:function () {
	      	$('#comentarioXadesErrorMessage').html('');
	    }
	});
}

//Función de retorno para cuando obtenerCertificado va bien
function certificadoCorrecto(signatureB64, certificate) {
	if (obtenerNumeroSerie(certificate)) {
		signRequests();
	} else {
		$(".cancel_sign").attr("disabled", false);
		$("#sign_modal").dialog('close');
	}
}

//Función de retorno para cuando obtenerCertificado va mal
function certificadoError(errorType, errorMsg) {
	var error = ""; 
	if (errorType == 'es.gob.afirma.core.AOCancelledOperationException') {
		error = "Operacion cancelada.";			
	} else if (errorType == 'es.gob.afirma.keystores.common.AOCertificatesNotFoundException') {
		error = "No se ha encontrado el certificado en el almacen";
	} else if (errorType == 'portafirmasException') {
		error = errorMsg;
	} else {
		error = "Error inesperado: " + errorMsg;
	}
	$('#error').html(error);
	$('#error').show();
	$("#sign_modal").dialog('close');
}

//Método que carga el número de serie del certificado del usuario
function obtenerNumeroSerie(certificado) {
	var result = true;
	var certificadoB64 = MiniApplet.getBase64FromText(certificado);
	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/obtenerNumeroSerie",
		type: 'post',
		dataType: 'json',
		data: "certificadoB64=" + certificadoB64,
		async: false,
		success:
			function(model) {
			if (model.status == "error") {
				$('#error').html(model.log);
				$('#error').show();
				result = false;
			} else {
				$('#serialNumber').val(model.serialNumber);
			}
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('No se pudo obtener el numero de serie de su certificado. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			result = false;
		}
	});
	return result;
}

/*
 *  El número máximo de peticiones a firmar asíncrinamente sin que el
 *  el navegador falle y se cierre, es finito, luego vamos a lanzar unas
 *  cuantas peticiones y por cada respuesta recibida de cada petición
 *  ajax se lanza otra que aún esté pendiente.
 *  
 */

//Método que lanza el proceso de firma de las peticiones seleccionadas
function signRequests() {
	$('.cancel_sign').css('display', 'none');
	$('.start_sign').css('display', 'none');
	$('.finish_sign').css('display', 'block');
	$('.finish_sign').attr('disabled', true);

	// Se inician los contadores
	nRequestComplete = 0;
	nRequestLaunch = 0;

	// Fija el firmante que se establezca para ser reutilizado

	/*
	 *  SE HAN ELIMINADO LAS LLAMADAS AJAX PARA OBTENER LA CONFIGURACIÓN DE FIRMA, DE MODO QUE
	 *  CUANDO SE IMPLANTE LA FIRMA TRIFÁSICA, HAY QUE PROBAR A LANZAR MÁS DE 10 A VER SI LO AGUANTA EL NAVEGADOR
	 */
	// Se lanzan las primeras y se lleva la cuenta para lanzar las restantes cuando toque
	while (nRequestLaunch < requests.length && nRequestLaunch < 1) {
		processRequest(requests[nRequestLaunch++]);
	}
}

//Función que procesa una petición
function processRequest(request) {
	// Se pone el icono "en espera"		
	$("#icon_" + request.requestTagHash).html("<span class=\"mf-icon mf-icon-firmando-16\" title=\"En proceso\"></span>");
	// Se obtiene la configuración de firma
	var signatureConfig = request.signatureConfig;
	if (signatureConfig.type == "FIRMA") {
		// Petición de firma
		firmaPeticion(request);
	} else if (signatureConfig.type == "VISTOBUENO") {
		// Petición de visto bueno
		doPass(request);
	} else {
		// Error al obtener la configuración de la petición
		$("#icon_" + request.requestTagHash).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"No firmado\"/></span>");
		$("#log_" + request.requestTagHash).html(request.signatureConfig.errorMessage);
		manageEndRequest(request.requestTagHash, request.paralela);
	}
}

//Método que envía al servidor una firma realizada sobre una petición
function saveSign(firma) {
	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/saveSign", 
		type: 'post',
		dataType: 'json',
		contentType: 'application/json',
		mimeType: 'application/json',
		data: JSON.stringify(firma),
		success: function(model) {
			if (model.status == "success") {
				$("#icon_" + firma.requestTagHash).html("<span class=\"mf-icon mf-icon-firma-correcta-16\" title=\"Firmado\"/></span>");
				$("#log_" + firma.requestTagHash).html("Firma correcta");
				manageEndRequest(firma.requestTagHash, firma.paralela);
			} else {
				var messageError = model.log;
				var statusError = model.status;

				if (statusError == "firmaInvalida") {
					prepareInvalidSign(firma, messageError, $('#warningInvalidSignMessage').val());

				} else if(statusError == "servicioCaido") {
					prepareInvalidSign(firma, messageError, $('#warningAFirmaKOMessage').val());

				} else {        					
					$("#icon_" + firma.requestTagHash).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"Error\"/></span>");
					$("#log_" + firma.requestTagHash).html(model.log);    			
					manageEndRequest(firma.requestTagHash, firma.paralela);
				}

			}
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$("#icon_" + firma.requestTagHash).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"Error\"/></span>");
			$("#log_" + firma.requestTagHash).html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			manageEndRequest(firma.requestTagHash, firma.paralela);
		}
	})
}


//Método que realiza la acción de visto bueno sobre una petición
function doPass(request) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/doPass",
		dataType: 'json',
		data: "requestTagId=" + request.requestTagHash,
		async: true,
		success:
			function(model) {
			if (model.status == "success") {
				$("#icon_" + request.requestTagHash).html("<span class=\"mf-icon mf-icon-firma-correcta-16\" title=\"Visto bueno\"/></span>");
				$("#log_" + request.requestTagHash).html("Visto bueno correcto");
			} else {
				$("#icon_" + request.requestTagHash).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"Error\"/></span>");
				$("#log_" + request.requestTagHash).html(model.log);
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	}).done(function(){
		manageEndRequest(request.requestTagHash, request.paralela);
	});
}

//Se encarga de contabilizar las tandas tramitadas y cargar la siguiente o finalizar, si es el caso
function manageEndRequest(requestTagHash, paralela) {
	desbloquearPeticion(requestTagHash, paralela);
	// Se cuenta la petición completada
	nRequestComplete++;
	// Si no hay más peticiones, entonces esta era la última y se activa el botón de "Finalizar"
	if(nRequestComplete == requests.length) {
		$(".finish_sign").attr('disabled', false);
	} else if(nRequestLaunch < requests.length) {
		// Si quedan peticiones por lanzar, se envia la siguiente y se cuenta
		processRequest(requests[nRequestLaunch++]);
	}
}

function procesarErrorFirmaDocumento(textoError, respuestaJSON) {
	$("#icon_" + objetoFirmaPeticion.request.requestTagHash).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"No firmado\"/></span>");
	$("#log_" + objetoFirmaPeticion.request.requestTagHash).html(textoError);
	manageEndRequest(respuestaJSON.requestTagHash, respuestaJSON.paralela);
}

function desbloquearPeticion(requestTagHash, paralela){
	if(paralela) {
		$.ajaxSetup({cache: false});
		$.ajax({
			url: "sign/desbloquearPeticion?requestTagHash=" + requestTagHash,
			type: 'get',
			dataType: 'text',
			async: true,
			success: function(result){}
		});
	}
}

//Método que realiza la acción de retirar sobre una petición enviada
function doSendToRemove() {
	// Se recuperan las peticiones
	var requests = $('.sendToRemove');

	// Por cada petición se solicita su configuración de firma
	requests.each(function(i, item) {
		// Se recupera el identificador de la petición
		var requestTagId = $(item).attr("id");
		// Se recupera el texto de rechazo
		var sendToRemoveText = $('#sendToRemove_message_' + requestTagId).val();
//		Se pone el icono "en espera"
		$("#sendToRemove_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firmando-16\" title=\"En proceso\"/></span>");
		$.ajaxSetup({cache: false});
		$.ajax({url: "inbox/doSendToRemove",
			dataType: 'json',
			data: "requestTagId=" + requestTagId + "&sendToRemoveText=" + sendToRemoveText,
			async: true,
			success:
				function(model) {
				if (model.status == "success") {
					$("#sendToRemove_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-correcta-16\" title=\"Retirada\"/></span>");
				} else {
	    				if(model.status == "signLinesTerminated"){
					$("#sendToRemove_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"Error\"/></span>");
	    					$("#sendToRemove_modal").dialog("close");
	    					$('input[id="request_checkbox"]').removeAttr("checked");
	    					showError(model.log);	    					
	    				}else{
	    					$("#sendToRemove_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"Error\"/></span>");
	    				}
				}
				return "success";
			},
			error:
				function error(jqXHR, textStatus, errorThrown) {
				$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
				$('#error').show();
				return "error";
			}
		});

	});
}

//Método que realiza la acción de rechazo sobre una petición
function doReject() {
	// Se recuperan las peticiones
	var requests = $('.toreject');

	// Por cada petición se solicita su configuración de firma
	requests.each(function(i, item) {
		// Se recupera el identificador de la petición
		var requestTagId = $(item).attr("id");

		// Se recupera el texto de rechazo
		var rejectionText = $('#reject_message_' + requestTagId).val();

		// Se pone el icono "en espera"
		$("#reject_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firmando-16\" title=\"En proceso\"/></span>");

		$.ajaxSetup({cache: false});
		$.ajax({url: "inbox/doReject",
			dataType: 'json',
			data: "requestTagId=" + requestTagId + "&textRejection=" + rejectionText,
			async: true,
			success:
				function(model) {
				if (model.status == "success") {
					$("#reject_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-correcta-16\" title=\"Rechazada\"/></span>");
				} else {
					$("#reject_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"Error\"/></span>");
				}
				return "success";
			},
			error:
				function error(jqXHR, textStatus, errorThrown) {
				$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
				$('#error').show();
				return "error";
			}
		});
	});
}

//Método que realiza la acción de rechazo sobre una petición
function doRejectDocel() {
	// Se recuperan las peticiones
	var requests = $('.torejectdocel');

	// Por cada petición se solicita su configuración de firma
	requests.each(function(i, item) {
		// Se recupera el identificador de la petición
		var requestTagId = $(item).attr("id");

		// Se recupera el texto de rechazo
		var rejectionText = $('#reject_message_' + requestTagId).val();

		// Se pone el icono "en espera"
		$("#reject_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firmando-16\" title=\"En proceso\"/></span>");

		$.ajaxSetup({cache: false});
		$.ajax({url: "inbox/doSendToRemoveDocelWeb",
			dataType: 'json',
			data: "requestTagId=" + requestTagId,
			async: true,
			success:
				function(model) {
				if (model.status == "success") {
					$("#reject_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-correcta-16\" title=\"Rechazada\"/></span>");
				} else {
					$("#reject_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"Error\"/></span>");
				}
				return "success";
			},
			error:
				function error(jqXHR, textStatus, errorThrown) {
				$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
				$('#error').show();
				return "error";
			}
		});
	});
}

//Método que realiza la acción de validación sobre una petición
function doValidation() {
	// Se recuperan las peticiones
	var requests = $('.tovalidate');

	// Por cada petición se solicita su configuración de firma
	requests.each(function(i, item) {
		// Se recupera el identificador de la petición
		var requestTagId = $(item).attr("id");

		// Se pone el icono "en espera"
		$("#validate_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firmando-16\" title=\"En proceso\"/></span>");

		$.ajaxSetup({cache: false});
		$.ajax({url: "inbox/doValidation",
			dataType: 'json',
			data: "requestTagId=" + requestTagId,
			async: true,
			success:
				function(model) {
				if (model.status == "success") {
					$("#validate_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-correcta-16\" title=\"Validada\"/></span>");
				} else {
					$("#validate_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"Error\"/></span>");
				}
				return "success";
			},
			error:
				function error(jqXHR, textStatus, errorThrown) {
				$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
				$('#error').show();
				return "error";
			}
		});
	});
}

//Cuando se abre la ventana modal se cargan los datos de las peticiones que se desea retirar
function prepareDataSendToRemove() {
	jsAjaxStatus.startAjax();
	// Se seleccionan todos los inputs que se llaman "request_checkbox" y están seleccionados;
	var selectedRequests = $('input[id="request_checkbox"]:checked');
	if (selectedRequests.length == 0) {
		$('#warning').html('Debe seleccionar al menos una petición enviada para retirar');
		$('#warning').show();
		jsAjaxStatus.stopAjax();
	} else {
		var table = $("#sendToRemoveTable > tbody");
		selectedRequests.each(function(i, item) {
			// Se recuperan los datos de la petición
			var row = $(item).parent().parent();
			var subject  = row.find('#subject_row').html();
			var remitter = row.find('#remitter_row').html();

			// Se generan las filas de la tabla
			table.append("<tr id=\"" + $(item).val() + "\" class=\"sendToRemove\"></tr>");
			$("#" + $(item).val()).html("<td>" + subject + "</td>" +
					"<td>" + remitter + "</td>" +
					"<td id=\"sendToRemove_icon_" + $(item).val() + "\"><span class=\"\"></span></td>" +
					"<td><input type=\"text\" id=\"sendToRemove_message_" + $(item).val() + "\" class=\"p4  wv90\" /></td>");
		});
		jsAjaxStatus.stopAjax();
		$('#sendToRemove_modal').dialog('open');
	}
}

//Cuando se abre la ventana modal se cargan los datos de las peticiones que se desea firmar
function prepareFIReSign() {
	jsAjaxStatus.startAjax();
	// Se seleccionan todos los inputs que se llaman "request_checkbox" y están seleccionados;
	var selectedRequests = $('input[id="request_checkbox"]:checked');
	if (selectedRequests.length == 0) {
		$('#warning').html('Debe seleccionar al menos una petición para firmar');
		$('#warning').show();
		jsAjaxStatus.stopAjax();
	} else {
		var requestsIds = getCheckIds(selectedRequests);
		fireSign(requestsIds);
	}
}

// Comprueba las peticiones seleccionadas antes de solicitar la configuración de firma, preparar los documentos en disco y most
function prepareDataToSign() {
	// Se seleccionan todos los inputs que se llaman "request_checkbox" y están seleccionados;
	var selectedRequests = $('input[id="request_checkbox"]:checked');
	if (selectedRequests.length == 0) {
		$('#warning').html('Debe seleccionar al menos una petición para firmar');
		$('#warning').show();
		jsAjaxStatus.stopAjax();
	} else {
		var requestsIds = getCheckIds(selectedRequests);
		loadRequestToSign("requestsIds=" + requestsIds);
	}
}

//Concatena ids separados por comas a partir de un array de cadenas
function getCheckIds(selectedRequests) {
	var requestsIds = '';
	selectedRequests.each(function(i, item) {
		requestsIds += $(item).val() + ",";
	});
	return requestsIds.substr(0, requestsIds.length - 1);
}

//Cuando se abre la ventana modal se cargan los datos de las peticiones que se desea rechazar
function prepareDataToReject() {
	var selectedRequests = $('input[id="request_checkbox"]:checked');
	if (selectedRequests.length == 0) {
		$('#warning').html('Debe seleccionar al menos una petición para rechazar');
		$('#warning').show();
		jsAjaxStatus.stopAjax();
	} else {
		var table = $("#requestsToRejectTable > tbody");
		selectedRequests.each(function(i, item) {
			// Se recuperan los datos de la petición
			var row = $(item).parent().parent();
			var subject  = row.find('#subject_row').html();
			var remitter = row.find('#remitter_row').html();

			// Se generan las filas de la tabla
			table.append("<tr id=\"" + $(item).val() + "\" class=\"toreject\"></tr>");
			$("#" + $(item).val()).html("<td>" + subject + "</td>" +
					"<td>" + remitter + "</td>" +
					"<td id=\"reject_icon_" + $(item).val() + "\"><span class=\"\"></span></td>" +
					"<td><input type=\"text\" id=\"reject_message_" + $(item).val() + "\" class=\"p4  wv90\" /></td>");
		});

		$('#reject_modal').dialog('open');
	}
}

/**
 * Método que rechaza la petición seleccionada
 */
function rejectSelectedRequest() {
	var id      = $('#selectedRequestId').val();
	var sender  = $('#selected_request_sender').html();
	var subject = $('#selected_request_subject').html();
	var table   = $("#requestsToRejectTable > tbody");

	// Se generan las filas de la tabla
	table.append("<tr id=\"" + id + "\" class=\"toreject\"></tr>");
	$("#" + id).html("<td>" + subject + "</td>" +
			"<td>" + sender + "</td>" +
			"<td id=\"reject_icon_" + id + "\"><span class=\"\"></span></td>" +
			"<td><input type=\"text\" id=\"reject_message_" + id + "\" class=\"p4  wv90\" /></td>");

	$('#reject_modal').dialog('open');
}

/**
 * Método que rechaza la petición seleccionada de Docel Web
 */
function rejectSelectedRequestDocelWeb() {
	var id      = $('#selectedRequestId').val();
	var subject = $('#selected_request_subject').html();
	var table   = $("#requestsToRejectTable > tbody");

	// Se generan las filas de la tabla
	table.append("<tr id=\"" + id + "\" class=\"torejectdocel\"></tr>");
	$("#" + id).html("<td>" + subject + "</td>" +
			"<td id=\"reject_icon_" + id + "\"><span class=\"\"></span></td>");

	$('#reject_docelweb_modal').dialog('open');
}

/**
 * Método que retira la petición enviada seleccionada
 */
function sendToRemoveSelectedRequest() {
	var id      = $('#selectedRequestId').val();
	var remitter  = $('#selected_request_sender').html();
	var subject = $('#selected_request_subject').html();
	var table   = $("#sendToRemoveTable > tbody");

	// Se generan las filas de la tabla
	table.append("<tr id=\"" + id + "\" class=\"sendToRemove\"></tr>");
	$("#" + id).html("<td>" + subject + "</td>" +
			"<td>" + remitter + "</td>" +
			"<td id=\"sendToRemove_icon_" + id + "\"><span class=\"\"></span></td>" +
			"<td><input type=\"text\" id=\"sendToRemove_message_" + id + "\" class=\"p4  wv90\" /></td>");

	$('#sendToRemove_modal').dialog('open');
}

/**
 * Método que retira la petición enviada seleccionada
 */
function sendToRemoveSelectedRequestDocelWeb() {
	var id      = $('#selectedRequestId').val();
	var subject = $('#selected_request_subject').html();
	var table   = $("#sendToRemoveTable > tbody");

	// Se generan las filas de la tabla
	table.append("<tr id=\"" + id + "\" class=\"sendToRemoveDocelWeb\"></tr>");
	$("#" + id).html("<td>" + subject + "</td>" +
			"<td id=\"sendToRemove_icon_" + id + "\"><span class=\"\"></span></td>");

	$('#sendToRemoveDocelWeb_modal').dialog('open');
}

/**
 * Método que obtiene los datos de las peticiones a reenviar
 */
function prepareDataToForward() {
	var selectedRequests = $('input[id="request_checkbox"]:checked');
	var requestsIds = getCheckIds(selectedRequests);
	$('#requestsToForward').val(requestsIds);
	$('#forward_modal').dialog('open');
}

/**
 * Método que reenvía la petición seleccionada
 */
function forwardSelectedRequest() {
	var selectedRequest = $('#selectedRequestId').val();
	$('#requestsToForward').val(selectedRequest);
	$('#forward_modal').dialog('open');
}

/**
 * Método que valida la petición seleccionada
 */
function validateSelectedRequest() {
	var id      = $('#selectedRequestId').val();
	var sender  = $('#selected_request_sender').html();
	var subject = $('#selected_request_subject').html();
	var table   = $("#requestsToValidateTable > tbody");

	// Se generan las filas de la tabla
	table.append("<tr id=\"" + id + "\" class=\"tovalidate\"></tr>");
	$("#" + id).html("<td>" + subject + "</td>" +
			"<td>" + sender + "</td>" +
			"<td id=\"validate_icon_" + id + "\"><span class=\"\"></span></td>");

	$('#validate_modal').dialog('open');
}


//Cuando se abre la ventana modal se cargan los datos de las peticiones que se desea validar
function prepareDataToValidate() {
	var selectedRequests = $('input[id="request_checkbox"]:checked');
	if (selectedRequests.length == 0) {
		$('#warning').html('Debe seleccionar al menos una petición para validar');
		$('#warning').show();
		jsAjaxStatus.stopAjax();
	} else {
		var table = $("#requestsToValidateTable > tbody");

		selectedRequests.each(function(i, item) {
			// Se recuperan los datos de la petición
			var row = $(item).parent().parent();
			var subject  = row.find('#subject_row').html();
			var remitter = row.find('#remitter_row').html();

			// Se generan las filas de la tabla
			table.append("<tr id=\"" + $(item).val() + "\" class=\"tovalidate\"></tr>");
			$("#" + $(item).val()).html("<td>" + subject + "</td>" +
					"<td>" + remitter + "</td>" +
					"<td id=\"validate_icon_" + $(item).val() + "\"><span class=\"\"></span></td>");
		});

		$('#validate_modal').dialog('open');
	}
}

/* Obtiene los identificadores de etiqueta-petición seleccionados, 
y llama al controlador para obtener el ZIP con los informes
 */
//function getZipReports2 () {	
//jsAjaxStatus.startAjax();
//var selectedRequests = $('input[id="request_checkbox"]:checked');	
//if (selectedRequests.length == 0) {
//$('#warning').html('Debe seleccionar al menos una petición para obtener los informes');
//$('#warning').show();
//jsAjaxStatus.stopAjax();
//} else {		
//var reqTagsId = getCheckIds(selectedRequests);
//location.href='/pf/servlet/DescargaZipVariasPeticionesServlet?idRequestsTagList='+reqTagsId;
//setTimeout(function(){
////do what you need here
//$('#warning').show();
//$('#warning').html('Generando informe ... Espere a que finalice para su descarga.');
//$('#warning').delay(10000).hide(600);
//jsAjaxStatus.stopAjax();
//}, 1000);
//}
//}

function getZipReports () {
	jsAjaxStatus.startAjax();
	var selectedRequests = $('input[id="request_checkbox"]:checked');	
	if (selectedRequests.length == 0) {
		$('#warning').html('Debe seleccionar al menos una petición para obtener los informes');
		$('#warning').show();
		$('#warning').delay(10000).hide(600);
		jsAjaxStatus.stopAjax();
	} else {		
		var reqTagsId = getCheckIds(selectedRequests);
		var urlDescarga = '/pf/servlet/DescargaZipVariasPeticionesServlet?idRequestsTagList='+reqTagsId;
		var req = new XMLHttpRequest();
		req.open("GET",urlDescarga, true);
		req.responseType = "blob";
		var filename = "InformeFirma_" + (new Date()-new Date('1970-01-01')) + "_.zip";
		req.onreadystatechange = function () {
			if (req.readyState === 4 && req.status === 200) {
				var blob = new Blob([req.response], { type: 'application/zip' });
				jsAjaxStatus.stopAjax();
				if (typeof window.chrome !== 'undefined') {
					// Chrome version
					var link = document.createElement('a');
					link.href = window.URL.createObjectURL(req.response);
					link.download = filename;
					link.click();
				} else if (typeof window.navigator.msSaveBlob !== 'undefined') {
					// IE version
					window.navigator.msSaveOrOpenBlob(blob, filename);
				} else {
					// Firefox version
					var e = document.createElement('a');
					e.style = "display: none";  
					var url = window.URL.createObjectURL(blob);
					e.href = url;
					e.download = filename;
					document.body.appendChild(e);
					e.click();
					setTimeout(function(){
						document.body.removeChild(e);
						window.URL.revokeObjectURL(url);  
					}, 0);
				}
			}else if (req.readyState === 4 && req.status === 500){
				jsAjaxStatus.stopAjax();
				$('#error').show();
				$('#error').html('Ha ocurrido un error al generar el informe.');
			}
		};
		req.send();
	}

}

//Método que aplica una etiqueta a las peticiones seleccionadas
function applyLabel(labelId) {
	// Se recuperan las peticiones seleccionadas
	var selectedRequests = $('input[id="request_checkbox"]:checked');
	if (selectedRequests.length == 0) {
		$('#warning').html('Debe seleccionar al menos una petición para aplicar la etiqueta.');
		$('#warning').show();
		$('#warning').delay(10000).hide(600);
	} else {
		jsAjaxStatus.startAjax();
		var requestsIds = getCheckIds(selectedRequests);

		// Se llama al servidor para poner la etiqueta a la petición
		$.ajaxSetup({cache: false});
		$.ajax({url: "inbox/applyLabel",
			dataType: 'json',
			data: "labelId=" + labelId + "&requestsIds=" + requestsIds,
			async: true,
			success:
				function(model) {
				if (model.status == "error") {
					$('#error').html('Ha ocurrido un error al aplicar la etiqueta');
					$('#error').show();
					jsAjaxStatus.stopAjax();
				} else {
					var requestBarSelected = $('#requestBarSelected').val();
					var currentPage = $('#currentPage').val();
					var searchFilter = $('#searchFilter').val();
					var appFilter = $('#appFilter').val();
					jsAjaxStatus.stopAjax();
					enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue': requestBarSelected},
					                                            {'parameterId':'currentPage', 'parameterValue': currentPage},
					                                            {'parameterId':'searchFilter', 'parameterValue': searchFilter},
					                                            {'parameterId':'appFilter', 'parameterValue': appFilter}]);
					$('input[id="request_checkbox"]').removeProp("checked");
					$('#select_all').removeProp("checked");
				}
			},
			error:
				function error(jqXHR, textStatus, errorThrown) {
				$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
				$('#error').show();
				jsAjaxStatus.stopAjax();
			}
		});
	}
}

//Método que borra la sociación de una etiqueta con una petición
function deleteLabelRequest(labelId, requestId) {
	// Se llama al servidor para poner la etiqueta a la petición
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/unApplyLabel",
		dataType: 'json',
		data: "labelId=" + labelId + "&requestId=" + requestId,
		async: true,
		success:
			function(model) {
			enviarFormulario('inboxForm', 'loadInbox',
					[{'parameterId':'requestBarSelected', 'parameterValue':  $('#requestBarSelected').val()},
					 {'parameterId':'currentPage', 'parameterValue': $('#currentPage').val()},
					 {'parameterId':'searchFilter', 'parameterValue': $('#searchFilter').val()},
					 {'parameterId':'appFilter', 'parameterValue': $('#appFilter').val()}]);
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	});
}

function ocultarEliminarEtiqueta(id) {
	$('#' + id).css('visibility', 'hidden');
}

function mostrarEliminarEtiqueta(id) {
	$('#' + id).css('visibility', 'visible');
}

//Método que reenvía una petición a un destinatario nuevo
function doForward(userPk, selectedRequest) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/doForward",
		dataType: 'json',
		data: 'requestsTagId=' + selectedRequest + '&forwardedUserPk=' + userPk,
		async: true,
		success:
			function(model) {
			jsAjaxStatus.stopAjax();
			$('#forward_modal').dialog("close");
			if (model.status == 'success') {
				// Vamos a la bandeja de pendientes
				enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
				                                            {'parameterId':'currentPage', 'parameterValue':'1'},
				                                            {'parameterId':'searchFilter', 'parameterValue':''},
				                                            {parameterId:'monthFilter', parameterValue: $('#month_filter').val()},
				                                            {parameterId:'yearFilter', parameterValue: $('#year_filter').val()},
				                                            {'parameterId':'appFilter', 'parameterValue':''}]);
			} else {
				showError(model.log);
			}
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			jsAjaxStatus.stopAjax();
			$('#forward_modal').dialog("close");
			showError(genericMessage);
		}
	});
}

//FUNCIONES PARA LA TABLA JMESA //
function createUserTable(queryString) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "userTable/getUserTable",
		dataType: 'html',
		data: queryString,
		async: true,
		success:
			function(model) {
			$('#userTable').html(model);
			$('#userTable > div > table').addClass("data");
			$('#forward_modal').dialog("open");
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	});
}

function userTableCheckbox(checkbox) {
	if ($('#' + checkbox).is(":checked")) {
		$('input[id="request_checkbox"]').removeProp("checked");
		$('#select_all').removeProp("checked");
		$('#' + checkbox).prop("checked", "checked");
	}
}

//Función que ejecuta las acciones sobre la tabla JMesa
function onInvokeAction(id, action) {
	setExportToLimit(id, '');
	var parameterString = createParameterStringForLimit(id);

	// Si la operación es de filtrado se pasa la búsqueda del usuario
	if (action == 'filter') {
		var query = $('#filterQuery').val();
		parameterString += "&userFilter=" + query;
	}
	if (action == 'clear') {
		$('#filterQuery').val("");
	}

	createUserTable(parameterString);
}


function setRequestFilterSelected() {
	var hasValidator = $('#hasValidator').val();
	var viewNoValidate = $('#viewNoValidate').val();
	var requestTypeFilter = $('#requestTypeFilter').val();

	if (requestTypeFilter == 'FIRMA') {
		$('#view_sign').attr("selected", "selected");
	} else if (requestTypeFilter == 'VISTOBUENO') {
		$('#view_pass').attr("selected", "selected");
	} else {
		if (hasValidator == 'true') {
			$('#view_validate').attr("selected", "selected");
		} else if (viewNoValidate == 'true'){
			$('#view_no_validate').attr("selected", "selected");
		}else{
			$('#view_all').attr("selected", "selected");
		}
	}
}

/**
 * Método que aplica el filtro de tipo de petición
 * @param filterValue Valor seleccionado
 */
function onchangeRequestTypeFilter(filterValue) {
	if (filterValue == 'view_all') {
		enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'requestTypeFilter', parameterValue: ''},
		                                            {parameterId:'hasValidator', parameterValue: 'false'},
		                                            {parameterId:'viewNoValidate', parameterValue: 'false'},
		                                            {parameterId:'monthFilter', parameterValue: $('#month_filter').val()},
		                                            {parameterId:'yearFilter', parameterValue: $('#year_filter').val()},
		                                            {parameterId:'currentPage', parameterValue:'1'},]);
	} else if (filterValue == 'view_sign') {
		enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'requestTypeFilter', parameterValue: 'FIRMA'},
		                                            {parameterId:'hasValidator', parameterValue: 'false'},
		                                            {parameterId:'viewNoValidate', parameterValue: 'false'},
		                                            {parameterId:'monthFilter', parameterValue: $('#month_filter').val()},
		                                            {parameterId:'yearFilter', parameterValue: $('#year_filter').val()},
		                                            {parameterId:'currentPage', parameterValue:'1'},]);
	} else if (filterValue == 'view_pass') {
		enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'requestTypeFilter', parameterValue: 'VISTOBUENO'},
		                                            {parameterId:'hasValidator', parameterValue: 'false'},
		                                            {parameterId:'viewNoValidate', parameterValue: 'false'},
		                                            {parameterId:'monthFilter', parameterValue: $('#month_filter').val()},
		                                            {parameterId:'yearFilter', parameterValue: $('#year_filter').val()},
		                                            {parameterId:'currentPage', parameterValue:'1'},]);
	} else if (filterValue == 'view_validate') {
		enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'requestTypeFilter', parameterValue: ''},
		                                            {parameterId:'hasValidator', parameterValue: 'true'},
		                                            {parameterId:'viewNoValidate', parameterValue: 'false'},
		                                            {parameterId:'monthFilter', parameterValue: $('#month_filter').val()},
		                                            {parameterId:'yearFilter', parameterValue: $('#year_filter').val()},
		                                            {parameterId:'currentPage', parameterValue:'1'},]);
	} else if (filterValue == 'view_no_validate') {
		enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'requestTypeFilter', parameterValue: ''},
		                                            {parameterId:'hasValidator', parameterValue: 'false'},
		                                            {parameterId:'viewNoValidate', parameterValue: 'true'},
		                                            {parameterId:'monthFilter', parameterValue: $('#month_filter').val()},
		                                            {parameterId:'yearFilter', parameterValue: $('#year_filter').val()},
		                                            {parameterId:'currentPage', parameterValue:'1'},]);
	}
}


/**
 * Método que aplica el filtro de tamaño de la bandeja de entrada
 * @param pageSize Valor seleccionado
 */
function onchangeInboxPageSize(pageSize) {
	enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'pageSize', parameterValue:pageSize},
	                                            {parameterId:'currentPage', parameterValue:'1'}]);
}

/**
 * Método que elimina una plantilla de redacción del usuario
 * @param templateId Identificador de plantilla
 */
function deleteTemplate(templateId) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "template/deleteTemplate",
		dataType: 'json',
		data: 'templateId=' + templateId,
		async: false,
		success:
			function(model) {
			if (model.status = "success") {
				// Se cierra la ventana modal
				$('#templates_modal').dialog("close");

				// Vamos a la bandeja de pendientes
				enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
				                                            {'parameterId':'currentPage', 'parameterValue':'1'},
				                                            {'parameterId':'searchFilter', 'parameterValue':''},
				                                            {'parameterId':'appFilter', 'parameterValue':''}]);
			} else {
				$('#error').html('No se ha podido eliminar la plantilla. Si el problema persiste, contacte con el administrador');
				$('#error').show();
			}
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
		}
	});
	jsAjaxStatus.stopAjax();
}

function ordenar(campo){
	// Configuración del sentido de la ordenación
	if($('#orderField').val() == campo){
		if($('#order').val() == 'asc'){
			$('#order').val('desc');
		}else{
			$('#order').val('asc');
		}
	}else{
		$('#order').val('asc');
	}
	// Configuracion de campo por el que se ordena
	$('#orderField').val(campo);

	enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'currentPage', 'parameterValue':'1'}]);
}

/**************************************************************************************
 *  		                        Gestión de etiquetas
 **************************************************************************************/
/*
 * Mejoras para la gestión de etiquetas
 * 	 · Sacar el javascript a un fichero a parte, por ejemplo "label.js"
 *   · Sacar el java del "inboxController" a un controlador a parte, por ejemplo "labelController"
 *     y mapeado "inbox/label"
 *   · Sacar la lista de etiquetas de la ventana modal a un html a parte, por ejemplo "label.html"
 *   · Refrescar la lista de etiquetas de la ventana modal con Ajax y Spring MVC en vez de retocar
 *     la que esta cargada en la vista evitando con ello hacer doble mantenimiento.
 */ 

function mostrarPregunta() {
	$('#capaPregunta').show();
	//$('#veloPregunta').show();
}

function ocultarPregunta() {
	$('#capaPregunta').hide();
	//$('#veloPregunta').hide();
}

function newLabel() {
	ocultarPregunta();        		
	$('#table_tags_modal').scrollTop(0);
	$('#new_row').show();
}

function cancelNewLabel() {
	$('#label_0_editing_id').val('');
	$('#new_row').hide();
}

function prepareLabelToEdit(index) {
	ocultarPregunta();        		
	$('#label_' + index).attr('style', 'display: none;');
	$('#label_' + index + '_empty').attr('style', 'display: none;');
	$('#label_' + index + '_actions').attr('style', 'display: none;');
	$('#label_' + index + '_editing').attr('style', 'display: ;');
	$('#label_' + index + '_editing_color').attr('style', 'display: ;');
	$('#label_' + index + '_editing_actions').attr('style', 'display: ;');
	$(".tags_accept").attr("disabled", true);
}

function cancelLabelEdition(index) {
	$('#label_' + index).attr('style', 'display: ;');
	$('#label_' + index + '_empty').attr('style', 'display: ;');
	$('#label_' + index + '_actions').attr('style', 'display: ;');
	$('#label_' + index + '_editing').attr('style', 'display: none;');
	$('#label_' + index + '_editing_color').attr('style', 'display: none;');
	$('#label_' + index + '_editing_actions').attr('style', 'display: none;');
	$(".tags_accept").attr("disabled", false);
}

/**
 * Método que guarda una etiqueta de usuario
 * @param id Identificador de la etiqueta
 */
function insertLabel() {
	var labelCode = $('#label_0_editing_id').encodeValue();
	var labelColorEncode = $('#label_0_editing_color_id').encodeValue();
	var requestQuery = '&labelCode=' +encodeURIComponent(window.btoa(labelCode)) + '&labelColor=' + labelColorEncode;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/insertLabel",
		dataType: 'json',
		data: requestQuery,
		async: false,
		success:
			function(model) {
			if (model.status = "success") {
				reenviarFormulario();
			} else {
				$("#tags_modal").hide();
				$('#error').html('No se ha podido crear la etiqueta. Si el problema persiste, contacte con el administrador');
				$('#error').show();
			}
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$("#tags_modal").hide();
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
		}
	});
	jsAjaxStatus.stopAjax();
}

function confirmModifyLabel(id) {
	$('#pregunta').html('Va a modificar la etiqueta editada, ¿está usted seguro?');
	$('#botonSi').attr('onclick','modifyLabel(' + id + ');');
	mostrarPregunta();
}

/**
 * Método que actualiza una etiqueta de usuario
 * @param id Identificador de la etiqueta
 */
function modifyLabel(id) {
	var labelCode = $('#label_' + id + '_editing_id').val();
	var labelCodeEncode = $('#label_' + id + '_editing_id').encodeValue();
	var labelColor = $('#label_' + id + '_editing_color_id').val();
	var labelColorEncode = $('#label_' + id + '_editing_color_id').encodeValue();
	var requestQuery = 'labelId=' + id + '&labelCode=' + encodeURIComponent(window.btoa(labelCodeEncode)) + '&labelColor=' + labelColorEncode;

	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/modifyLabel",
		dataType: 'json',
		data: requestQuery,
		async: false,
		success:
			function(model) {
			if (model.status = "success") {
				$('#span_' + id).html(labelCode);
				$('#span_' + id).css('background-color', labelColor);
				$('#label_' + id + '_editing_color_id').val(labelColor);
				cancelLabelEdition(id);
				ocultarPregunta();        		
			} else {
				$("#tags_modal").hide();
				$('#error').html('No se ha podido actualizar la etiqueta. Si el problema persiste, contacte con el administrador');
				$('#error').show();
				ocultarPregunta();        		
			}
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$("#tags_modal").hide();
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			ocultarPregunta();        		
		}
	});
	jsAjaxStatus.stopAjax();
}
function confirmDeleteLabel(id, name) {
	$('#pregunta').html('Va a borrar la etiqueta "' + name + '". Todas las peticiones relacionadas a esta etiqueta se desvincularán, ¿está usted seguro?');
	$('#botonSi').attr('onclick','deleteLabel(' + id + ');');
	mostrarPregunta();
}

/**
 * Método que elimina una etiqueta de usuario
 * @param id Identificador de la etiqueta
 */
function deleteLabel(id) {
	// En primer lugar, se resetea el filtro de etiquetas
	$('#labelFilter').val('');
	var labelRow = $('.label_' + id + '_row');
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/deleteLabel",
		dataType: 'json',
		data: 'labelId=' + id,
		async: false,
		success:
			function(model) {
			if (model.status = "success") {
				labelRow.remove();
				$(".tags_accept").attr("disabled", false);
			} else {
				$("#tags_modal").hide();
				$('#error').html('No se ha podido actualizar la etiqueta. Si el problema persiste, contacte con el administrador');
				$('#error').show();
			}
			ocultarPregunta();        		
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#tags_modal').hide();
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			ocultarPregunta();        		
		}
	});
	jsAjaxStatus.stopAjax();
}

/**************************************************************************************
 *  		                        Fin gestión de etiquetas
 **************************************************************************************/

//var fileDownloadCheckTimer;

//Método que muestra el GIF de espera al descargar un documento
//function prepareDocumentDownload() {
////var token = new Date().getTime();
//var token = "10";
//$('#download_token_value_id').val(token);

//// Se bloquea la pantalla
//jsAjaxStatus.startAjax();

//// Se consulta la cookie cada minuto hasta debloquear la pantalla
//fileDownloadCheckTimer = window.setInterval(function () {
//var cookieValue = $.cookie('fileDownloadToken');
//if (cookieValue == token) {
//window.clearInterval(fileDownloadCheckTimer);
//$.removeCookie('fileDownloadToken');
//jsAjaxStatus.stopAjax();
//}
//}, 1000);
//}


function reenviarFormulario() {
	enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
	                                            {'parameterId':'currentPage', 'parameterValue':'1'},
	                                            {'parameterId':'searchFilter', 'parameterValue':''},
	                                            {parameterId:'monthFilter', parameterValue: $('#month_filter').val()},
	                                            {parameterId:'yearFilter', parameterValue: $('#year_filter').val()},
	                                            {'parameterId':'appFilter', 'parameterValue':''}]);
}


function getParameterByName(name, url) {
	if (!url) {
		url = window.location.href;
	}
	name = name.replace(/[\[\]]/g, "\\$&");
	var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
	results = regex.exec(url);
	if (!results) return null;
	if (!results[2]) return '';
	return decodeURIComponent(results[2].replace(/\+/g, " "));
}


$(document).ready(function() {

	var maxLength = 22;
	$('#application_filter option').each(function(){
		var text=$(this).text()
		if (text.length>maxLength)
			$(this).text(text.substr(0,maxLength)+'...')
	})

	$("#searchForm").submit(function (e) {
		e.preventDefault();
		var searchFilterValue = $('#searchTextField').val();
		enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'sinceDate', parameterValue: $('#fsince').val()},
		                                            {parameterId:'untilDate', parameterValue: $('#funtil').val()},
		                                            {parameterId:'monthFilter', parameterValue: $('#month_filter').val()},
		                                            {parameterId:'yearFilter', parameterValue: $('#year_filter').val()},
		                                            {parameterId:'currentPage', parameterValue:'1'},
		                                            {parameterId:'searchFilter', parameterValue: searchFilterValue}]);
	});

	// Configuración de las máscaras
	$('#fsince').mask("99/99/9999");
	$('#funtil').mask("99/99/9999");

	// Botón de búsqueda para las fechas
	$('#searchInbox').click(function (e) {
		e.preventDefault();
		var searchFilterValue = $('#searchTextField').val();
		enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'sinceDate', parameterValue: $('#fsince').val()},
		                                            {parameterId:'untilDate', parameterValue: $('#funtil').val()},
		                                            {parameterId:'monthFilter', parameterValue: $('#month_filter').val()},
		                                            {parameterId:'yearFilter', parameterValue: $('#year_filter').val()},
		                                            {parameterId:'currentPage', parameterValue:'1'},
		                                            {parameterId:'searchFilter', parameterValue: searchFilterValue}]);
	});

	// Filtro de aplicaciones
	$("#combo_app_" + $("#appFilter").val()).attr("selected", "selected");

	// Filtro de peticiones
	setRequestFilterSelected();

	// Filtro de etiquetas
	$('#tags_filter').val($('#labelFilter').val());

	$("#select_all").click(function(e){
		if ($(this).is(":checked")) {
			$('input[id="request_checkbox"]').prop("checked", "checked");
		} else {
			$('input[id="request_checkbox"]').removeProp("checked");
		}
	});

	// Filtro de meses
	$("#combo_month_" + $("#monthFilter").val()).attr("selected", "selected");

	// Filtro de años
	$("#combo_year_" + $("#yearFilter").val()).attr("selected", "selected");

	var valorComboMes = $("#combo_month_" + $("#monthFilter").val()).val();
    if (!valorComboMes || valorComboMes == ''){
    	$('#year_filter').attr("disabled", true);
    } else if (valorComboMes == 'last24Hours' || 
		valorComboMes == 'lastWeek' ||
		valorComboMes == 'lastMonth' ||
		valorComboMes == 'all'){
			$('#year_filter').attr("disabled", true);
	} else { 
		$('#year_filter').attr("disabled", false); 
	}

    configComentarioXadesModal();
    
	//Se crea el diálogo de la ventana modal del detalle de petición de firma
	$("#detallePeticion_modal").dialog({
		autoOpen: false,
		resizable: true,
		height: 900,
		width: 1200,
		modal: true,       
	    create:function () {},
	    open: function(){},
	    close:function () {
			$("#msgBox").html("");
	    }
	});

	//Se crea el diálogo de la ventana modal de plantillas
	$("#templates_modal").dialog({
		autoOpen: false,
		resizable: false,
		//height:450,
		width: 500,
		modal: true,       
		buttons: [
		          {
		        	  text: "Cancelar",       
		        	  "class": 'secondary',
		        	  click: function() {                   
		        		  $(this).dialog("close");
		        	  }
		          },
		          {                   
		        	  text: "Aceptar",                   
		        	  "class": 'primary',
		        	  click: function() {

		        	  }
		          }           
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){
		        	  //Cargar combos, listados
		          },
		          close:function () {
		        	  //Limpiar campos del formulario del dialog, etc        	
		          }
	});

	//Se crea el diálogo de la ventana modal para iniciar el proceso de firma
	$("#sign_modal").dialog({
		autoOpen: false,
		resizable: false,
		width: 700,
		modal: true,       
		buttons: [
		          {
		        	  text: "Cancelar",       
		        	  "class": 'secondary cancel_sign',
		        	  click: function() {                   
		        		  $(this).dialog("close");
		        	  }
		          },
		          {                   
		        	  text: "Iniciar firma",                   
		        	  "class": 'primary start_sign',
		        	  click: function() {
		        		  // Se inhabilitan los botones
		        		  $(".cancel_sign").attr("disabled", true);
		        		  $(".start_sign").attr("disabled", true);

		        		  // Se comprueba el número de serie del certificado para saber si ya se cargó el certificado a la sesión
		        		  var numSerieCertificado = $("#serialNumber").val();
		        		  // Si no lo está, se solicita la selección de certificado
		        		  if (numSerieCertificado == null || numSerieCertificado == '') {
		        			  obtenerCertificado();
		        		  } else {
		        			  signRequests();
		        		  }
		        	  }
		          },
		          {                   
		        	  text: "Finalizar",                   
		        	  "class": 'primary finish_sign',
		        	  "style": 'display: none',
		        	  click: function() {
		        		  enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
		        		                                              {'parameterId':'currentPage', 'parameterValue':'1'},
		        		                                              {'parameterId':'searchFilter', 'parameterValue':''},
		        		                                              {'parameterId':'appFilter', 'parameterValue':''}]);
		        	  }
		          }
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){
		        	  jsAjaxStatus.stopAjax();
		        	  $(".cancel_sign").attr("disabled", false);
		        	  $(".start_sign").attr("disabled", false);
		          },
		          close:function () {
		        	  // Se limpia la tabla de peticiones
		        	 $("#requestsToSignTable > tbody").html("");
		        	 if($(".cancel_sign").attr("disabled")) {
		        		  enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
		        		                                              {'parameterId':'currentPage', 'parameterValue':'1'},
		        		                                              {'parameterId':'searchFilter', 'parameterValue':''},
		        		                                              {'parameterId':'appFilter', 'parameterValue':''}]);
		        	 }
		          }
	});

	//Se crea el diálogo de la ventana modal para iniciar el proceso de validación de peticiones
	$("#validate_modal").dialog({
		autoOpen: false,
		resizable: false,
		//height:450,
		width: 700,
		modal: true,       
		buttons: [
		          {
		        	  text: "Cancelar",       
		        	  "class": 'secondary cancel_validation',
		        	  click: function() {                   
		        		  $(this).dialog("close");
		        	  }
		          },
		          {                   
		        	  text: "Validar",                   
		        	  "class": 'primary start_validation',
		        	  click: function() {
		        		  // Se inhabilitan los botones
		        		  $(".cancel_validation").attr("disabled", true);
		        		  $(".start_validation").attr("disabled", true);

		        		  // Se ejecuta la operación de rechazo
		        		  $(".cancel_validation").attr("style", "display: none");
		        		  $(".start_validation").attr("style", "display: none");
		        		  doValidation();
		        		  $(".finish_validation").attr("style", "display: ");
		        	  }
		          },
		          {                   
		        	  text: "Finalizar",                   
		        	  "class": 'primary finish_validation',
		        	  "style": 'display: none',
		        	  click: function() {
		        		  enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
		        		                                              {'parameterId':'currentPage', 'parameterValue':'1'},
		        		                                              {'parameterId':'searchFilter', 'parameterValue':''},
		        		                                              {'parameterId':'appFilter', 'parameterValue':''}]);
		        	  }
		          }
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){
		        	  //Cargar combos, listados
		          },
		          close:function () {
		        	  // Se limpia la tabla de peticiones
		        	  var table = $("#requestsToValidateTable > tbody");
		        	  table.html("");
		          }
	});

	//Se crea el diálogo de la ventana modal de Administracion de Etiquetas
	$("#tags_modal").dialog({
		autoOpen: false,
		resizable: false,
		//height:450,
		width: 500,
		modal: true,       
		buttons: [
//{
//text: "Cancelar",       
//"class": 'secondary tags_cancel',
//click: function() {                   
//$(this).dialog("close");
//enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
//{'parameterId':'currentPage', 'parameterValue':'1'},
//{'parameterId':'searchFilter', 'parameterValue':''},
//{'parameterId':'appFilter', 'parameterValue':''}]);
//}
//},
{                   
	text: "Aceptar",                   
	"class": 'primary tags_accept dsp_n',
	click: function() {
		enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
		                                            {'parameterId':'currentPage', 'parameterValue':'1'},
		                                            {'parameterId':'searchFilter', 'parameterValue':''},
		                                            {'parameterId':'appFilter', 'parameterValue':''}]);
	}
}           
],   
//Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
create:function () {               
	$(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	$(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
},
open: function(){
	//Cargar combos, listados
},
close:function () {
	enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
	                                            {'parameterId':'currentPage', 'parameterValue':'1'},
	                                            {'parameterId':'searchFilter', 'parameterValue':''},
	                                            {'parameterId':'appFilter', 'parameterValue':''}]);
}
	});

//	Se crea el diálogo de la ventana modal de Retirada de peticiones enviadas
	$("#sendToRemove_modal").dialog({
		autoOpen: false,
		resizable: false,
		//height:450,
		width: 700,
		modal: true, 
		buttons: [
		          {
		        	  text: "Cancelar",       
		        	  "class": 'secondary cancel_sendToRemove',

		        	  click: function() {                   
		        		  $(this).dialog("close");
		        	  }
		          },
		          {
		        	  text: "Retirar",
		        	  "class": 'primary start_sendToRemove',
		        	  click: function() {
		        		  // Se inhabilitan los botones
		        		  $(".cancel_sendToRemove").attr("disabled", true);
		        		  $(".start_sendToRemove").attr("disabled", true);

		        		  // Se ejecuta la operación de rechazo
		        		  $(".cancel_sendToRemove").attr("style", "display: none");
		        		  $(".start_sendToRemove").attr("style", "display: none");
		        		  doSendToRemove();
		        		  $(".finish_sendToRemove").attr("style", "display: ");
		        	  }
		          },
		          {                   
		        	  text: "Finalizar",                   
		        	  "class": 'primary finish_sendToRemove',
		        	  "style": 'display: none',
		        	  click: function() {
		        		  enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
		        		                                              {'parameterId':'currentPage', 'parameterValue':'1'},
		        		                                              {'parameterId':'searchFilter', 'parameterValue':''},
		        		                                              {'parameterId':'appFilter', 'parameterValue':''}]);
		        	  }
		          }
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){
		        	  //Cargar combos, listados
		          },
		          close:function () {
		        	  //Limpiar campos del formulario del dialog, etc 
		        	  // Se limpia la tabla de peticiones
		        	  var table = $("#sendToRemoveTable > tbody");
		        	  table.html("");
		          }
	});

//	Se crea el diálogo de la ventana modal de Retirada de peticiones enviadas
	$("#sendToRemoveDocelWeb_modal").dialog({
		autoOpen: false,
		resizable: false,
		//height:450,
		width: 700,
		modal: true, 
		buttons: [
		          {
		        	  text: "Cancelar",       
		        	  "class": 'secondary cancel_sendToRemove',

		        	  click: function() {                   
		        		  $(this).dialog("close");
		        	  }
		          },
		          {
		        	  text: "Retirar",
		        	  "class": 'primary start_sendToRemove',
		        	  click: function() {
		        		  // Se inhabilitan los botones
		        		  $(".cancel_sendToRemove").attr("disabled", true);
		        		  $(".start_sendToRemove").attr("disabled", true);

		        		  // Se ejecuta la operación de rechazo
		        		  $(".cancel_sendToRemove").attr("style", "display: none");
		        		  $(".start_sendToRemove").attr("style", "display: none");
		        		  doSendToRemoveDocelWeb();
		        		  $(".finish_sendToRemove").attr("style", "display: ");
		        	  }
		          },
		          {                   
		        	  text: "Finalizar",                   
		        	  "class": 'primary finish_sendToRemove',
		        	  "style": 'display: none',
		        	  click: function() {
		        		  enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
		        		                                              {'parameterId':'currentPage', 'parameterValue':'1'},
		        		                                              {'parameterId':'searchFilter', 'parameterValue':''},
		        		                                              {'parameterId':'appFilter', 'parameterValue':''}]);
		        	  }
		          }
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){
		        	  //Cargar combos, listados
		          },
		          close:function () {
		        	  //Limpiar campos del formulario del dialog, etc 
		        	  // Se limpia la tabla de peticiones
		        	  var table = $("#sendToRemoveTable > tbody");
		        	  table.html("");
		          }
	});
//TODO
//	Método que realiza la acción de retirar sobre una petición enviada
	function doSendToRemoveDocelWeb() {
		// Se recuperan las peticiones
		var requests = $('.sendToRemoveDocelWeb');

		// Por cada petición se solicita su configuración de firma
		requests.each(function(i, item) {
			// Se recupera el identificador de la petición
			var requestTagId = $(item).attr("id");
//			Se pone el icono "en espera"
			$("#sendToRemove_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firmando-16\" title=\"En proceso\"/></span>");
			$.ajaxSetup({cache: false});
			$.ajax({url: "inbox/doSendToRemoveDocelWeb",
				dataType: 'json',
				data: "requestTagId=" + requestTagId,
				async: true,
				success:
					function(model) {
					if (model.status == "success") {
						$("#sendToRemove_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-correcta-16\" title=\"Retirada\"/></span>");
					} else {
						$("#sendToRemove_icon_" + requestTagId).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"Error\"/></span>");
					}
					return "success";
				},
				error:
					function error(jqXHR, textStatus, errorThrown) {
					$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
					$('#error').show();
					return "error";
				}
			});		
		});
	}

	//Se crea el diálogo de la ventana modal de Administracion de Etiquetas
	$("#reject_modal").dialog({
		autoOpen: false,
		resizable: false,
		//height:450,
		width: 700,
		modal: true,       
		buttons: [
		          {
		        	  text: "Cancelar",       
		        	  "class": 'secondary cancel_reject',
		        	  click: function() {                   
		        		  $(this).dialog("close");
		        	  }
		          },
		          {
		        	  text: "Aceptar",
		        	  "class": 'primary start_reject',
		        	  click: function() {
		        		  // Se inhabilitan los botones
		        		  $(".cancel_reject").attr("disabled", true);
		        		  $(".start_reject").attr("disabled", true);

		        		  // Se ejecuta la operación de rechazo
		        		  $(".cancel_reject").attr("style", "display: none");
		        		  $(".start_reject").attr("style", "display: none");
		        		  doReject();
		        		  $(".finish_reject").attr("style", "display: ");
		        	  }
		          },
		          {                   
		        	  text: "Finalizar",                   
		        	  "class": 'primary finish_reject',
		        	  "style": 'display: none',
		        	  click: function() {
		        		  enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
		        		                                              {'parameterId':'currentPage', 'parameterValue':'1'},
		        		                                              {'parameterId':'searchFilter', 'parameterValue':''},
		        		                                              {'parameterId':'appFilter', 'parameterValue':''}]);
		        	  }
		          }
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){
		        	  //Cargar combos, listados
		          },
		          close:function () {
		        	  //Limpiar campos del formulario del dialog, etc   
		        	  // Se limpia la tabla de peticiones
		        	  var table = $("#requestsToRejectTable > tbody");
		        	  table.html("");	
		          }
	});

	//Se crea el diálogo de la ventana modal de Administracion de Etiquetas
	$("#reject_docelweb_modal").dialog({
		autoOpen: false,
		resizable: false,
		//height:450,
		width: 700,
		modal: true,       
		buttons: [
		          {
		        	  text: "Cancelar",       
		        	  "class": 'secondary cancel_reject',
		        	  click: function() {                   
		        		  $(this).dialog("close");
		        	  }
		          },
		          {
		        	  text: "Aceptar",
		        	  "class": 'primary start_reject',
		        	  click: function() {
		        		  // Se inhabilitan los botones
		        		  $(".cancel_reject").attr("disabled", true);
		        		  $(".start_reject").attr("disabled", true);

		        		  // Se ejecuta la operación de rechazo
		        		  $(".cancel_reject").attr("style", "display: none");
		        		  $(".start_reject").attr("style", "display: none");
		        		  doRejectDocel();
		        		  $(".finish_reject").attr("style", "display: ");
		        	  }
		          },
		          {                   
		        	  text: "Finalizar",                   
		        	  "class": 'primary finish_reject',
		        	  "style": 'display: none',
		        	  click: function() {
		        		  enviarFormulario('inboxForm', 'loadInbox', [{'parameterId':'requestBarSelected', 'parameterValue':'unresolved'},
		        		                                              {'parameterId':'currentPage', 'parameterValue':'1'},
		        		                                              {'parameterId':'searchFilter', 'parameterValue':''},
		        		                                              {'parameterId':'appFilter', 'parameterValue':''}]);
		        	  }
		          }
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){
		        	  //Cargar combos, listados
		          },
		          close:function () {
		        	  //Limpiar campos del formulario del dialog, etc   
		        	  // Se limpia la tabla de peticiones
		        	  var table = $("#requestsToRejectTable > tbody");
		        	  table.html("");	
		          }
	});

	$("#forward_modal").dialog({
		autoOpen: false,
		resizable: false,
		//height:450,
		//width: 700,
		modal: true,       
		buttons: [
		          {
		        	  text: "Cancelar",       
		        	  "class": 'secondary',
		        	  click: function() {                   
		        		  $(this).dialog("close");
		        	  }
		          },
		          {                   
		        	  text: "Aceptar",                   
		        	  "class": 'primary',
		        	  click: function() {
		        		  $('#forwardErrorMsg').html("");
		        		  var selectedUserPk = $('#forwardToUserPk').val();
		        		  var selectedRequest = $('#requestsToForward').val();
		        		  doForward(selectedUserPk, selectedRequest);
		        	  }
		          }           
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){

		          },
		          close:function () {
		        	  //Limpiar campos del formulario del dialog, etc        	
		          }
	});

	// Redimensiona bandeja de peticiones
	$("#msgTop").resizable({
		handles: {
			"s": "#handler"		
		},	
		alsoResize: "#msgList"
	});

	// El usuario al que se reenvía se busca autocompletando
	$('#userToForward').autocomplete({
		minLength: 3,
		source: "inbox/autocompleteForward",
		// Función que se ejecuta al seleccionar un elemento
		select: function( event, ui ) {
			$(this).val(ui.item.label);
			$('#forwardToUserPk').val(ui.item.id);
			return false;
		}
	});

	// Asociar el icono de espera al evento submit del formulario
	$('#inboxForm').bind('submit', function(event) {
		jsAjaxStatus.startAjax();
	});

	// Se definen los selectores de color
	var colorTags = $(".color_selector");
	colorTags.each(function(i, item) {
		var id = $(item).attr('id').split('_');
		var tagId = id[1];
		var tag = "span_" + tagId;
		var color = hexc($('#' + tag).css('background-color'));
		$(item).spectrum({
			color: color,
			showInput: true,
			className: "full-spectrum",
			showInitial: true,
			showPalette: true,
			showSelectionPalette: true,
			maxPaletteSize: 10,
			preferredFormat: "hex",
			localStorageKey: "spectrum.demo",
			move: function (color) {

			},
			show: function () {

			},
			beforeShow: function () {

			},
			hide: function () {

			},
			change: function(color) {
				$('#label_' + tagId + '_editing_color_id').val(color);
			},
			palette: [
			          ["rgb(0, 0, 0)", "rgb(67, 67, 67)", "rgb(102, 102, 102)",
			           "rgb(204, 204, 204)", "rgb(217, 217, 217)","rgb(255, 255, 255)"],
			           ["rgb(152, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 153, 0)", "rgb(255, 255, 0)", "rgb(0, 255, 0)",
			            "rgb(0, 255, 255)", "rgb(74, 134, 232)", "rgb(0, 0, 255)", "rgb(153, 0, 255)", "rgb(255, 0, 255)"], 
			            ["rgb(230, 184, 175)", "rgb(244, 204, 204)", "rgb(252, 229, 205)", "rgb(255, 242, 204)", "rgb(217, 234, 211)", 
			             "rgb(208, 224, 227)", "rgb(201, 218, 248)", "rgb(207, 226, 243)", "rgb(217, 210, 233)", "rgb(234, 209, 220)", 
			             "rgb(221, 126, 107)", "rgb(234, 153, 153)", "rgb(249, 203, 156)", "rgb(255, 229, 153)", "rgb(182, 215, 168)", 
			             "rgb(162, 196, 201)", "rgb(164, 194, 244)", "rgb(159, 197, 232)", "rgb(180, 167, 214)", "rgb(213, 166, 189)", 
			             "rgb(204, 65, 37)", "rgb(224, 102, 102)", "rgb(246, 178, 107)", "rgb(255, 217, 102)", "rgb(147, 196, 125)", 
			             "rgb(118, 165, 175)", "rgb(109, 158, 235)", "rgb(111, 168, 220)", "rgb(142, 124, 195)", "rgb(194, 123, 160)",
			             "rgb(166, 28, 0)", "rgb(204, 0, 0)", "rgb(230, 145, 56)", "rgb(241, 194, 50)", "rgb(106, 168, 79)",
			             "rgb(69, 129, 142)", "rgb(60, 120, 216)", "rgb(61, 133, 198)", "rgb(103, 78, 167)", "rgb(166, 77, 121)",
			             "rgb(91, 15, 0)", "rgb(102, 0, 0)", "rgb(120, 63, 4)", "rgb(127, 96, 0)", "rgb(39, 78, 19)", 
			             "rgb(12, 52, 61)", "rgb(28, 69, 135)", "rgb(7, 55, 99)", "rgb(32, 18, 77)", "rgb(76, 17, 48)"]
			          ]
		});
	});

	// Si se ordenó paginar la última petición se carga la primera de la nueva página
	var requestPagination = $('#requestPagination').val();
	if (requestPagination.indexOf("next") != -1) {
		var nextRequestId = $(".row0").attr("id");
		loadRequest(nextRequestId, 0);
	} else if (requestPagination.indexOf("previous") != -1) {
		var pageSize = $('#pageSize').val();
		var nextRequestId = $(".row" + (parseInt(pageSize) - 1)).attr("id");
		loadRequest(nextRequestId, (parseInt(pageSize) - 1));
	}


	//Se crea el diálogo de la ventana modal de Administracion de Etiquetas
	$("#message_modal").dialog({
		autoOpen: false,
		resizable: false,
		height: 550,
		width: 680,
		modal: true,       
		buttons: [
		          {
		        	  text: "Aceptar",
		        	  "class": 'primary start_reject',
		        	  click: function() {
		        		  $(this).dialog("close");
		        	  }
		          }
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){

		          },
		          close:function () {

		          }
	});


	//Se crea el diálogo de la ventana modal de mensajes
	$("#messagesUnresolved_modal").dialog({
		autoOpen: false,
		resizable: false,
		//height:450,
		width: 300,
		modal: true,  
		buttons: [
		          {
		        	  text: "Si",
		        	  "class": 'primary start_reject',
		        	  click: function() {
		        		  $(this).dialog("close");
		        		  enviarFormulario('inboxForm', 'loadInbox', [{parameterId:'requestBarSelected', parameterValue:'diffusion'}, {parameterId:'currentPage', parameterValue:'1'}, {parameterId:'searchFilter', parameterValue:''}, {parameterId:'appFilter', parameterValue:''}]);
		        	  }
		          },
		          {
		        	  text: "No",
		        	  "class": 'primary start_reject',
		        	  click: function() {
		        		  $(this).dialog("close");
		        	  }
		          }
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){

		          },
		          close:function () {

		          }
	});

	//Se crea el popup de definir el nombre del reporte de firma
	$("#popUpNombreInformeDeFirma").dialog({
		autoOpen: false,
		resizable: false,
		//height:450,
		width: 300,
		modal: true,  
		buttons: [
		          {
		        	  text: "Guardar",
		        	  "class": 'primary start_reject',
		        	  click: function() {
		        		  jsAjaxStatus.startAjax();
		        		  var urlDescargaInforme = $("#urlDescargaInforme").val();
		        		  var nombreSeleccionado = $("#nombreInformeZip").val();
		        		  if(nombreSeleccionado == null || nombreSeleccionado == ''){
		        			  jsAjaxStatus.stopAjax();
		        			  $("#popUpNombreInformeDeFirma").dialog("close");	
		        			  $('#error').show();
		        			  $('#error').html('Debe introducir un nombre para el informe.');
		        			  $('#error').delay(10000).hide(600);
		        		  }
		        		  else{
		        			  var req = new XMLHttpRequest();
		        			  req.open("POST",urlDescargaInforme+'&nameFileReturn='+nombreSeleccionado, true);
		        			  req.responseType = "blob";
		        			  req.onreadystatechange = function () {
		        				  if (req.readyState === 4 && req.status === 200) {
		        					  jsAjaxStatus.stopAjax();
		        					  var filename = nombreSeleccionado + ".zip";
		        					  var blob = new Blob([req.response], { type: 'application/zip' });
		        					  if (typeof window.chrome !== 'undefined') {
		        						  // Chrome version
		        						  var link = document.createElement('a');
		        						  link.href = window.URL.createObjectURL(req.response);
		        						  link.download = nombreSeleccionado + ".zip";
		        						  link.click();
		        					  } else if (typeof window.navigator.msSaveBlob !== 'undefined') {
		        						  // IE version
		        						  window.navigator.msSaveOrOpenBlob(blob, filename);
		        					  } else {
		        						  // Firefox version
		        						  var e = document.createElement('a');
		        						  e.style = "display: none";  
		        						  var url = window.URL.createObjectURL(blob);
		        						  e.href = url;
		        						  e.download = filename;
		        						  document.body.appendChild(e);
		        						  e.click();
		        						  setTimeout(function(){
		        							  document.body.removeChild(e);
		        							  window.URL.revokeObjectURL(url);  
		        						  }, 0);
		        					  }
		        				  }else if (req.readyState === 4 && req.status === 500){
		        					  jsAjaxStatus.stopAjax();
		        					  $('#error').show();
		        					  $('#error').html('Ha ocurrido un error al generar el informe.');
		        				  }
		        			  };
		        			  req.send();
		        			  $(this).dialog("close");
		        		  }
		        	  }
		          },
		          {
		        	  text: "Cancelar",
		        	  "class": 'primary start_reject',
		        	  click: function() {
		        		  //CancelarFuncion
		        		  $(this).dialog("close");
		        	  }
		          }
		          ],   
		          //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
		          create:function () {               
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
		        	  $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
		          },
		          open: function(){

		          },
		          close:function () {

		          }
	});

	$("#select_all_messages").click(function(e){
		if ($(this).is(":checked")) {
			$('input[id="message_checkbox"]').prop("checked", "checked");
		} else {
			$('input[id="message_checkbox"]').removeProp("checked");
		}
	});


	if ($("#hasMessages").val() == 'true') {
		$("#hasMessages").val('false');
		$("#messagesUnresolved_modal").dialog("open");

	}

//	if(idRequestToSign!=null){
//		fireSign("requestsIds=" + idRequestToSign);
//	}

	if(idRequestToSign!=null){
		loadRequestToSign("requestsIds="+idRequestToSign);
	}

	//Se crea el diálogo para confirmaciones
	$("#confirmInvalidSign").dialog({
		autoOpen : false,
		resizable : false,
		width: 370,
		modal : true,
		create : customModalStyle,
		close : function() {
		}
	});
	
	if (errors != null){
		function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('La firma no se ha realizado correctamente' + errors);
			$('#error').show();
		}
	}
	
});

function ToggleClass(sender){
	if($(sender).hasClass("mf-icon mf-icon-downloadreport-16-black")){
		$(sender).removeClass("mf-icon mf-icon-downloadreport-16-black");
		$(sender).addClass("mf-icon mf-icon-downloadreport-16");
	}	
}

//Método que carga los datos de un mensaje
function loadMessage(messageId, userMessageId) {
	var markRead = false;
	if ($("#r_" + messageId).hasClass("new")) {
		markRead = true;
	}

	jsAjaxStatus.startAjax();

	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/loadMessage",
		dataType: 'html',
		data: 'messageId=' + messageId + '&userMessageId=' + userMessageId,
		async: false,
		success:
			function(data) {
			var mensaje = JSON.parse(data);

			if (markRead) {
				$("#r_" + mensaje.primaryKey).removeClass("new");

				var newMess = eval($('#numMessages')[0].innerHTML);
				if (newMess > 0) {
					newMess = newMess - 1;
					$('#numMessages')[0].innerHTML = newMess;
				}

				$("#r_" + mensaje.primaryKey).find('input[id="message_checkbox"]').val(
						mensaje.primaryKey + "-" + mensaje.userMessagePk);

			}

			$('#messageSubject').val(mensaje.dsubject);
			$('#messageText').val(mensaje.ttext);

			$('#message_modal').dialog('open');
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
		}
	});
	jsAjaxStatus.stopAjax();
};

//Se marcan como leídos todos los mensajes
function prepareMarkReadMessage() {
	// Se seleccionan todos los inputs que se llaman "message_checkbox" y están seleccionados;
	var selectedMessages = $('input[id="message_checkbox"]:checked');
	if (selectedMessages.length == 0) {
		$('#warning').html('Debe seleccionar al menos un mensaje');
		$('#warning').show();
		jsAjaxStatus.stopAjax();
	} else {

		var messagesIds = getCheckIds(selectedMessages);
		jsAjaxStatus.startAjax();
		$.ajaxSetup({cache: false});
		$.ajax({url: "inbox/markRead",
			dataType: 'json',
			data: "messagesIds=" + messagesIds,
			async: false,
			success:
				function(model) {
				// Se cargan los mensajes que se vayan a marcar como leidos
				messages = model;
				var numMessMarkRead = 0;
				$.each(messages, function (i, item) {
					if ($("#r_" + item.primaryKey).hasClass("new")) {
						$("#r_" + item.primaryKey).removeClass("new");
						$("#r_" + item.primaryKey).find('input[id="message_checkbox"]').val(
								item.primaryKey + "-" + item.userMessagePk);
						numMessMarkRead++;	
					}
				});

				var newMess = eval($('#numMessages')[0].innerHTML);
				if (newMess >= numMessMarkRead && newMess - numMessMarkRead >= 0) {
					newMess = newMess - numMessMarkRead;
					$('#numMessages')[0].innerHTML = newMess;
				}

				$('#select_all_messages').removeProp("checked");

			},
			error:
				function error(jqXHR, textStatus, errorThrown) {
				$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
				$('#error').show();
			}
		});
		jsAjaxStatus.stopAjax();
	}
}


//Se marcan como eliminados todos los mensajes
function prepareMarkDeleteMessage() {
	// Se seleccionan todos los inputs que se llaman "message_checkbox" y están seleccionados;
	var selectedMessages = $('input[id="message_checkbox"]:checked');
	if (selectedMessages.length == 0) {
		$('#warning').html('Debe seleccionar al menos un mensaje');
		$('#warning').show();
		jsAjaxStatus.stopAjax();
	} else {

		var messagesIds = getCheckIds(selectedMessages);
		jsAjaxStatus.startAjax();
		$.ajaxSetup({cache: false});
		$.ajax({url: "inbox/markDelete",
			dataType: 'json',
			data: "messagesIds=" + messagesIds,
			async: false,
			success:
				function(model) {
				// Se cargan las peticiones para que cuando se vayan a firmar, esté disponible la configuración de firma
				messages = model;
				var numMessMarkRead = 0;
				$.each(messages, function (i, item) {
					$("#r_" + item.primaryKey).remove();
					numMessMarkRead++;
				});

				var newMess = eval($('#numMessages')[0].innerHTML);
				if (newMess >= numMessMarkRead && newMess - numMessMarkRead >= 0) {
					newMess = newMess - numMessMarkRead;
					$('#numMessages')[0].innerHTML = newMess;
				}

				$('#select_all_messages').removeProp("checked");

			},
			error:
				function error(jqXHR, textStatus, errorThrown) {
				$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
				$('#error').show();
			}
		});
		jsAjaxStatus.stopAjax();
	}
}

function destroyClickedElement(event) {
	document.body.removeChild(event.target);
}
function abrirPopUpNombreDeInformeFirma(urlDescarga, idRequestTag){
	var validacion = validarSiTieneVariosInformesDeFirma(idRequestTag);
	if(validacion == false || validacion == 'false'){	
		jsAjaxStatus.startAjax();
		var req = new XMLHttpRequest();
		req.open("POST","/pf/servlet/DescargaZipServlet?idRequestTag="+idRequestTag, true);
		req.responseType = "blob";
		req.onreadystatechange = function () {
			if (req.readyState === 4 && req.status === 200) {
				jsAjaxStatus.stopAjax();
				var filename = "report_" + new Date().getTime() + ".pdf";
				var blob = new Blob([req.response], { type: 'application/pdf' });		        

				if (typeof window.chrome !== 'undefined') {
					// Chrome version
					var link = document.createElement('a');
					link.href = window.URL.createObjectURL(req.response);
					link.download = "report_" + new Date().getTime() + ".pdf";
					link.click();
				} else if (typeof window.navigator.msSaveBlob !== 'undefined') {
					// IE version
					window.navigator.msSaveOrOpenBlob(blob, filename);
				} else {
					// Firefox version
					var e = document.createElement('a');
					e.style = "display: none";  
					var url = window.URL.createObjectURL(blob);
					e.href = url;
					e.download = filename;
					document.body.appendChild(e);
					e.click();
					setTimeout(function(){
						document.body.removeChild(e);
						window.URL.revokeObjectURL(url);  
					}, 0);
				}
			}else if (req.readyState === 4 && req.status === 500){
				jsAjaxStatus.stopAjax();
				$('#error').show();
				$('#error').html('Ha ocurrido un error al generar el informe.');
			}
		};
		req.send();

	}
	else{
		//Si es un ZIP
		$("#urlDescargaInforme").val ( urlDescarga );
		$("#popUpNombreInformeDeFirma").dialog("open");		
	}
}

function validarSiTieneVariosInformesDeFirma(idRequestTag){
	var retorno;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "inbox/validarSiTieneVariosInformesDeFirma?idRequestTag="+idRequestTag,
		type: 'get',
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



function prepareInvalidSign(firma, error, pregunta) {
	$(window).scrollTop(0);
	$('#confirmInvalidSignMessage').html(error + "</br>" + pregunta);
	$('#confirmInvalidSign').dialog("option", "buttons", [closeInvalidSign(firma, error), saveInvalidSign(firma) ]);
	$('#confirmInvalidSign').dialog('open');
}

function saveInvalidSign(firma) {
	return {
		text : "Sí",
		"class" : 'primary',
		click : function() {
			$(this).dialog("close");
			firma.validarFirma = '0';
			saveSign(firma);
		}
	};
}

function closeInvalidSign(firma, messageError) {
	return {
		text: "No",
		"class": 'primary start_reject',
		click: function() {
			$(this).dialog("close");
			$("#icon_" + firma.requestTagHash).html("<span class=\"mf-icon mf-icon-firma-erronea-16\" title=\"Error\"/></span>");
			$("#log_" + firma.requestTagHash).html(messageError);   
			manageEndRequest(firma.requestTagHash, firma.paralela);
		}
	};

}