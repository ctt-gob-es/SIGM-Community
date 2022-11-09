// Para indicar si la pestaña está cargada
var isLoadAuthorizations = false;
	
// Preparación para revocar
function prepareRevoke(primaryKey) {
	showConfirm(primaryKey, 'revocar');
}

//Preparación para rechazar
function prepareReject(primaryKey) {
	showConfirm(primaryKey, 'rechazar');
}

//Preparación para aceptar
function prepareAccept(primaryKey) {
	showConfirm(primaryKey, 'aceptar');
}

// Confirmación para revocar, rechazar y aceptar
function showConfirm(primaryKey, accion) {
	$('#questionMessageAuthorized').html('Va a ' + accion + ' la autorización, ¿está usted seguro?');
	$('#yesButtonAuthorized').attr('onclick',"ajaxAuthorization('" + primaryKey + "','" + accion + "');");
	$('#questionAuthorized').show();
}

// Petición ajax para revocar, rechazar y aceptar
function ajaxAuthorization(primaryKey, accion) {
	$('#questionAuthorized').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/authorization/changeAuthorization",
		dataType: 'json',
		data: "primaryKey=" + primaryKey + "&accion=" + accion,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadAuthorizations();
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

// Preparación de la modificación de una autorización
function prepareModifyAuthorization(primaryKey) {
	$('#authorizationTypeId').val($('#authorizationTypeId_' + primaryKey).val());
	$('#authorizedPk').val($('#authorizedPk_' + primaryKey).val());
	$('#labelFullName').html($('#fullName_' + primaryKey).html());
	$('#modifyPk').val(primaryKey);
	var fRequest = $('#frequest_' + primaryKey).html().split(' ');
	$('#frequest').val(fRequest[0]);
	$('#hrequest').val(fRequest[1]);
	var fRevocation = $('#frevocation_' + primaryKey).html().split(' ');
	$('#frevocation').val(fRevocation[0]);
	if(fRevocation[1] == null || fRevocation[1] == '') {
		$('#hrevocation').val('00:00:00');
	} else {
		$('#hrevocation').val(fRevocation[1]);
	}
	$('#observations').val($('#observations_' + primaryKey).val());
	$('#modify_authorization_modal').dialog('open');
	
	// para ocultar el datapicker al inicio y que siga funcionando
	$('#frequest').blur();
	$('#ui-datepicker-div').hide();
	$('#frequest').focus(function() {
		$('#ui-datepicker-div').show();
	});
}

// Ajax para la modificación
function modifyAuthorization() {
	var primaryKey = $('#modifyPk').val();
	var authorizedPk = $('#authorizedPk').val();
	var authorizationType = $('#authorizationTypeId').val();
	var fRequest = $('#frequest').val();
	var hRequest = $('#hrequest').val();
	var fRevocation = $('#frevocation').val();
	var hRevocation = $('#hrevocation').val();
	var observations = encodeURI($('#observations').val());

	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/authorization/insertAuthorization",
		type: 'post',
		dataType: 'json',
		data: "primaryKey=" + primaryKey + 
			  "&authorizationType=" + authorizationType +
			  "&authorizedPk=" + authorizedPk +
			  "&frequest=" + fRequest + 
			  "&hrequest=" + hRequest + 
			  "&frevocation=" + fRevocation + 
			  "&hrevocation=" + hRevocation + 
			  "&observations=" + observations,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadAuthorizations();
			} else {
				$('#divErrorMessageModify').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#modify_authorization_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

// Preparación para una nueva autorización
function prepareNewAuthorization() {
    $('#divErrorMessage').html('');        	
	$('#new_authorizationType').val('1');
	$('#new_authorized_fullName').val('');
	$('#new_authorized_pk').val('');
	$('#new_frequest').val('');
	$('#new_hrequest').val('00:00:00');
	$('#new_frevocation').val('');
	$('#new_hrevocation').val('00:00:00');
	$('#new_observations').val('');
	$('#new_authorization_modal').dialog('open');
}

// Ajax para la nueva autorización
function newAuthorization() {
   	var authorizationType = $('#new_authorizationType').val();
	var authorizedPk = $('#new_authorized_pk').val();
	var fRequest = $('#new_frequest').val();
	var hRequest = $('#new_hrequest').val();
	var fRevocation = $('#new_frevocation').val();
	var hRevocation =  $('#new_hrevocation').val();
	var observations = encodeURI($('#new_observations').val());

	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/authorization/insertAuthorization",
		type: 'post',
		dataType: 'json',
		data: "primaryKey=" + 
			  "&authorizationType=" + authorizationType + 
			  "&authorizedPk=" + authorizedPk + 
			  "&frequest=" + fRequest + 
			  "&hrequest=" + hRequest + 
			  "&frevocation=" + fRevocation + 
			  "&hrevocation=" + hRevocation + 
			  "&observations=" + observations,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadAuthorizations();
			} else {
				$('#divErrorMessage').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#new_authorization_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

// Carga las autorizaciones
function loadAuthorizations() {
	if(!isLoadAuthorizations) {
		reloadAuthorizations();
		isLoadAuthorizations = true;
	}
};

// Recarga las autorizaciones
function reloadAuthorizations() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/authorization/loadAuthorizations",
		dataType: 'html',
		async: false,
		success: function(model) {
        	$('#modify_authorization_modal').dialog("close");
        	$('#new_authorization_modal').dialog("close");
			$("#tab2").html(model);
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
        	$('#modify_authorization_modal').dialog("close");
        	$('#new_authorization_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}