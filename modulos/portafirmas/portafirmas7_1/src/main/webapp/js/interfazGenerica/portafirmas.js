// Para indicar si la pestaña está cargada
var isLoadPortafirmas = false;

// Carga la lista de cargos
function loadPortafirmas() {
	if(!isLoadPortafirmas) {
		reloadPortafirmas('get', null);
		isLoadPortafirmas = true;
	}
}

//Recarga la lista de cargos
function reloadPortafirmas() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		url: 'portafirmas',
		type: 'get',
		dataType: 'html',
		async: false,
		success: function(model) {
        	$('#portafirmas_dialog').dialog("close");
			$("#tab2").html(model);
			setupPortafirmas();
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html(genericMessage);
			$('#error').show();
        	$('#portafirmas_dialog').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function setupPortafirmas() {
	//Se crea el diálogo de la ventana modal
	$("#portafirmas_dialog").dialog({
	    autoOpen: false,
	    resizable: false,
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
	            	savePortafirmas();
	            }
	        }           
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	    close:function () {
	    }
	});	
}

function prepareNewPortafirmas() {
	$('#idPortafirmas').val('');
	$('#portafirmasNombre').val('');
	$('#portafirmasDir3').val('');
	$('#urlInterfaz').val('');
	$('#urlNotificacion').val('');
	$('#cPortafirmas').val('');
	$('#soapVersion').val(1);
	$('#errorMsgPortafirmas').html('');
	$("#portafirmas_dialog").dialog("open");
}

function prepareModifyPortafirmas(idPortafirmas) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "portafirmas/load",
		type: 'get',
		dataType: 'html',
		data: 'idPortafirmas=' + idPortafirmas,
		async: true,
		success:
			function(model) {
			$('#portafirmas_dialog').html(model);
			$('#portafirmas_dialog').dialog('open');
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#portafirmas_dialog').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function savePortafirmas() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "portafirmas/save",
		type: 'post',
		dataType: 'json',
		data: $("#form_portafirmas_modal").serialize(),
		async: true,
		success:
			function(warning) {
			// Si no hay avisos, se refresca la página
			if(jQuery.isEmptyObject(warning)) {
				reloadPortafirmas();
			} else {
				// Si se detectan errores, se muestran los avisos en la modal
				$('#errorMsgPortafirmas').html(getErrors(warning));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#portafirmas_dialog').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function prepareDeletePortafirmas(idPortafirmas, nombre) {
	$('#questionMessagePortafirmas').html('Va a borrar el portafirmas "' + nombre + '", ¿está usted seguro?');
	$('#yesButtonPortafirmas').attr('onclick','deletePortafirmas(' + idPortafirmas + ');');
	$('#questionPortafirmas').show();
}

// Borrado de cargo
function deletePortafirmas(idPortafirmas)  {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "portafirmas/delete",
		type: 'post',
		dataType: 'json',
		data: 'idPortafirmas=' + idPortafirmas,
		async: true,
		success:
			function(response) {
			// Si no hay avisos, es que se ha borrado y refrescamos la página
			if(	   jQuery.isEmptyObject(response.warnings)
				&& jQuery.isEmptyObject(response.isUndelete)
				&& jQuery.isEmptyObject(response.errors)) {
				reloadPortafirmas();
			} else if (!jQuery.isEmptyObject(response.errors)) {
				// Hay errores, se oculta la venta de confimación y se muestra el error
				$('#questionPortafirmas').hide();
				showError(getErrors(response.errors));
			} else if (!jQuery.isEmptyObject(response.warnings)) {
				// Hay avisos, se oculta la venta de confimación y se muestran los avisos
				$('#questionPortafirmas').hide();
				showWarning(getErrors(response.warnings));
			} else if (!jQuery.isEmptyObject(response.isUndelete)) {
				// Hay avisos de que no se puede borrar. Se pregunta si se quiere deshabilitar
				prepareRevokePortafirmas(primaryKey, getErrors(response.isUndelete));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			jsAjaxStatus.stopAjax();
		}
	});
}

