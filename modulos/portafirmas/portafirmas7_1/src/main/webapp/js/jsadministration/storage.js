// Indica si ya se cargó la pestaña del histórico
var isLoadStorage = false;

// Para cargar la pestaña del histórico una única vez
function loadStorage() {
	if(!isLoadStorage) {
		reLoadStorage('');
		isLoadStorage = true;
	}
}

// Para recargar la pestaña del histórico
function reLoadStorage(searchServerPK) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'administration/storage/load',
		type: 'get',
		dataType: 'html',
		async: true,
		success: function(model) {
			$('#tab5').html(model);
			setupStorage();
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			manageError();
			jsAjaxStatus.stopAjax();
		}
	});
};

// Configuración al cargar la pestaña
function setupStorage() {	
	// Desactivación del evento submit de los button y configuración del evento al hacer click
	$('#cleanPf').click(function (e) {cleanStorageFilter(e,'Pf');});
	$('#cleanHist').click(function (e) {cleanStorageFilter(e,'Hist');});
	$('#searchPf').click(function (e) {e.preventDefault();goFirst('Pf');searchRequest('Pf');});
	$('#searchHist').click(function (e) {e.preventDefault();goFirst('Hist');searchRequest('Hist');});
	$('#movePf').click(function (e) {prepareMoveRequest(e,'Pf');});
	$('#moveHist').click(function (e) {prepareMoveRequest(e, 'Hist');});
	$('#deleteHist').click(function (e) {prepareDeleteRequest(e);});
	
	// Configuración de los campos de autocompletado
	$('#remitterPf').autocomplete(storageAutocomplete('remitterPf', 'remitterIdPf'));
	$('#signPf').autocomplete(storageAutocomplete('signPf', 'signIdPf'));
	$('#remitterHist').autocomplete(storageAutocomplete('remitterHist', 'remitterIdHist'));
	$('#signHist').autocomplete(storageAutocomplete('signHist', 'signIdHist'));

	//Se crea el diálogo de confirmación 
	$("#questionStorage").dialog(getStoreDialog());
}

// Configura la ventana modal para confirmar operaciones
function getStoreDialog() {
	return {
	    autoOpen: false,
	    resizable: false,
	    width: 320,
	    modal: true,       
	    create: customModalStyle
	};
}

// Resetea los campos de búsqueda
function cleanStorageFilter(e,form) {
	e.preventDefault();
	var today = new Date();
	$('#remitter' + form).val('');
	$('#remitterId' + form).val('');
	$('#application' + form).val('');
	$('#sign' + form).val('');
	$('#signId' + form).val('');
	$('#month' + form).val(today.getMonth() + 1);
	$('#year' + form).val(today.getFullYear());
}

// Configura los campos con autocompletado
function storageAutocomplete(label, id) {
	return {
		minLength: 3,
		source: 'administration/storage/autocomplete',
		select: function( event, ui ) {
			$('#' + label).val(ui.item.label);
		    $('#' + id).val(ui.item.id);
			return false;
		}
	};
}

// Realiza búsquedas de peticiones
function searchRequest(form) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'administration/storage/search',
		type: 'post',
		dataType: 'html',
		data: $('#form' + form).serialize(),
		async: true,
		success: function(model) {
			manageResponse(model, form);
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			manageError();
			jsAjaxStatus.stopAjax();
		}
	});
}

// Dialogo confirmación de para mover peticiones de Portafirmas al histórico y viceversa
function prepareMoveRequest(e,form) {
	e.preventDefault();
	$('#questionMessageStorage').html('Va a mover las solicitudes seleccionadas, ¿está usted seguro?');
	$('#questionStorage').dialog( "option", "buttons", [getCloseButton(), getMoveRequestButton(form)]);
	$('#questionStorage').dialog('open');
}

//Obtiene el objeto que configura la acción de mover peticiones
function getMoveRequestButton(form) {
	return {
		 text: "Aceptar",
		 "class": 'primary',
		 click: function() {moveRequest(form);}
	};
}

// Ajax que mueve peticiones de Portafirmas al histórico y viceversa
function moveRequest(form) {
	$('#questionStorage').dialog('close');
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'administration/storage/move',
		type: 'post',
		dataType: 'html',
		data: $('#form' + form).serialize(),
		async: true,
		success: function(model) {
			manageResponse(model, form);
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			manageError();
			jsAjaxStatus.stopAjax();
		}
	});
}

// Dialogo confirmación de para borrar peticiones del histórico
function prepareDeleteRequest(e) {
	e.preventDefault();
	$('#questionMessageStorage').html('Va a eliminar definitivamente las solicitudes seleccionadas, ¿está usted seguro?');
	$('#questionStorage').dialog( "option", "buttons", [getCloseButton(), getDeleteRequestButton()]);
	$('#questionStorage').dialog('open');
}

// Obtiene el objeto que configura la acción de borrar peticiones
function getDeleteRequestButton() {
	return 	{
		 text: "Aceptar",
		 "class": 'primary',
		 click: function() {deleteRequest();}
	 };
}

// Ajax que borra peticiones del histórico
function deleteRequest() {
	$('#questionStorage').dialog('close');
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'administration/storage/delete',
		type: 'post',
		dataType: 'html',
		data: $('#formHist').serialize(),
		async: true,
		success: function(model) {
			manageResponse(model, 'Hist');
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			manageError();
			jsAjaxStatus.stopAjax();
		}
	});
}

// Gestiona la respuesta ajax; Función callback
function manageResponse(model, form) {
	// se inserta la lista recibida
	$('#requestList' + form).html(model);
	// se configura el evento para seleccionar los checkbox
	$('#requestList' + form + ' #selectAll').click(function () {
		if($(this).is(":checked")) {
			$('#requestList' + form + ' [type=checkbox]').prop("checked", "checked");
		} else {
			$('#requestList' + form + ' [type=checkbox]').removeProp("checked");
		}
	});
	jsAjaxStatus.stopAjax();
}

// Gestiona la respuesta de error del ajax
function manageError() {
	$('#error').html(genericMessage);
	$('#error').show();
	jsAjaxStatus.stopAjax();
}