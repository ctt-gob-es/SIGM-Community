// Para indicar si la pestaña está cargada
var isLoadGestores = false;

// Preparación para borrar un gestor
function prepareDeleteGestor(primaryKey) {
	$('#questionMessageGestor').html('Va a borrar un gestor, ¿está usted seguro?');
	$('#yesButtonGestor').attr('onclick','deleteGestor(' + primaryKey + ');');
	$('#questionGestor').show();
}
// Para borrar un gestor
function deleteGestor(primaryKey) {
	$('#questionGestor').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/gestores/deleteGestor",
		dataType: 'json',
		data: "primaryKey=" + primaryKey,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadGestores();
			} else {
				showError(getErrors(data));
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

// Preparación de la ventana para buscar gestores
function prepareInsertGestor() {
	$('#gestores_modal').dialog('open');
}
// Para insertar un gestor
function insertGestor(primaryKey) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/gestores/insertGestor",
		dataType: 'json',
		data: "primaryKey=" + primaryKey,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadGestores();
			} else {
				$('#gestorErrorMessage').html(getErrors(data));
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

// Carga la lista de gestores
function loadGestores() {
	if(!isLoadGestores) {
		reloadGestores();
		isLoadGestores = true;
	}
};

//Recarga la lista de gestores
function reloadGestores() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		type: 'POST',
		url: 'configuration/gestores/loadGestores',
		dataType: 'html',
		async: false,
		success: function(model) {
        	$('#gestores_modal').dialog("close");
			$("#tab4").html(model);
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
        	$('#gestores_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}