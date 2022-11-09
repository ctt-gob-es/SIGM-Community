// Para indicar si la pestaña está cargada
var isLoadValidators = false;

// Preparación para borrar un validador
function prepareDeleteValidator(primaryKey) {
	$('#questionMessageValidator').html('Va a borrar un validador, ¿está usted seguro?');
	$('#yesButtonValidator').attr('onclick','deleteValidator(' + primaryKey + ');');
	$('#questionValidator').show();
}
// Para borrar un validador
function deleteValidator(primaryKey) {
	$('#questionValidator').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/validators/deleteValidator",
		dataType: 'json',
		data: "primaryKey=" + primaryKey,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadValidators();
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

// Preparación de la ventana para buscar validadores
function prepareInsertValidator() {
	$('#validators_modal').dialog('open');
}
// Para insertar un validador
function insertValidator(primaryKey, apps) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/validators/insertValidator",
		dataType: 'json',
		data: "primaryKey=" + primaryKey
			  + "&apps=" + apps,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadValidators();
			} else {
				$('#validatorErrorMessage').html(getErrors(data));
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

// Carga la lista de validadores
function loadValidators() {

	if(!isLoadValidators) {
		reloadValidators();
		isLoadValidators = true;
	}
};

//Recarga la lista de validadores
function reloadValidators() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		type: 'POST',
		url: 'configuration/validators/loadValidators',
		dataType: 'html',
		async: false,
		success: function(model) {
        	$('#validators_modal').dialog("close");
			$("#tab3").html(model);
			prepareEditValidator();
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
        	$('#validators_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

//Preparación para modificar un validador: Se carga el validador correspondiente en la ventana modal 
function prepareModifyValidator(primaryKey) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/validators/editValidator",
		type: 'post',
		dataType: 'html',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(model) {
			$('#validator_modal').html(model);
			setupValidator();				
			$('#validator_modal').dialog('open');
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#validator_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function unCheckAll(){
    $('#selectAll').prop('checked', false);
}

function checkAll(){
    $('.w50').prop('checked', $('#selectAll').prop('checked'));
}

function setupValidator() {
	
	$("#validator_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 320,
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
	            	saveValidator();
	            }
	        }           
	    ],   
	    create: customModalStyle,
	    close:function () {
	    	$('#validatorErrorMessage').html('');
	    }
	});	
}

//Prepara la edicion del validador
function prepareEditValidator() {
	$('#name').val('');
	$('#primaryKey').val('');
}

//Guarda y modifica al usuario que presenta la ventana modal
function saveValidator() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/validators/saveValidator",
		type: 'post',
		dataType: 'json',
		data: $("#validator_form").serialize(),
		async: true,
		success:
			function(data) {
			$('#warningUserModal').hide();
			// Si no hay avisos, se refresca la página
			if(jQuery.isEmptyObject(data)) {
	        	$('#validator_modal').dialog("close");
			} else {
				// Si se detectan errores, se muestran los avisos en la modal
				$('#validatorErrorMessage').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#validator_modal').dialog("close");
			jsAjaxStatus.stopAjax();
			$('#warningUserModal').hide();
		}
	});
}
