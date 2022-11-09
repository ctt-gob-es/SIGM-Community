// Utilizada para generar un distintivo único para las nuevas filas de e-mail
var i_new = 0;

// Crea una fila en la vista para insertar un nuevo e-mail
function newEmail(notify) {
	$('#emailList').append(
		'<div id="div_new_' + i_new + '">' +
		'	<input class="w200" size="14" value=""  name="" id="demail_new_' + i_new + '"/>' +
		'	<input type="checkbox" value="S" id="lnotify_new_' + i_new + '"/>' +
		'	<label class="dsp_ib w100" style="float:none;">' + notify + '</label>' +
		'	<a href="#!" class="simbutton secondary" onclick="insertEmail(' + i_new + ');" id="modify_' + i_new + '">' +
		'		<span class="mf-icon mf-icon-save-16"></span>' +
		'	</a>' +
		'	<a href="#!" class="simbutton secondary" onclick="cancelNewEmail(' + i_new + ');" id="delete_' + i_new + '">' +
		'		<span class="mf-icon mf-icon-delete-16"></span>' +
		'	</a>' +
		'</div>');
	i_new++;
}

// Elimina una fila que se creó para insertar un e-mail
function cancelNewEmail(i) {
	$('#div_new_' + i).remove();
}

// Ajax para dar de alta a un nuevo e-mail
function insertEmail(i) {
	var demail = $('#demail_new_' + i).val();
	var lnotify = 'N';
	if($('#lnotify_new_' + i).prop('checked')) {
		lnotify = 'S';
	}
	var requestQuery = 'demail=' + demail + '&lnotify=' + lnotify;

	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/insertEmail",
        	dataType: 'json',
        	data: requestQuery,
        	async: false,
        	success:
        		function(model) {
	        		if (model.status == "success") {
	        			$('#div_new_' + i).attr('id','div_' + model.primaryKey);
	        			$('#demail_new_' + i).attr('id','demail_' + model.primaryKey);
	        			$('#lnotify_new_' + i).attr('id','lnotify_' + model.primaryKey);
	        			$('#delete_' + i).attr('onclick','prepareDeleteEmail(' + model.primaryKey + ')');
	        			$('#modify_' + i).attr('onclick','prepareModifyEmail(' + model.primaryKey + ')');
	        			
	        			prepareOkMessage('Se ha guardado el correo electrónico correctamente.');
	        		} else {
	        			showWarning(model.msgError);
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

function prepareOkMessage(msg) {
	$('#okPersonalDataMessage').html(msg);
	$('#okPersonalData').dialog( "option", "buttons", [getAcceptButton()]);
	$('#okPersonalData').dialog('open');
}

// Confirmación para el borrado de un e-mail
function prepareDeleteEmail(primaryKey) {
	$('#confirmPersonalDataMessage').html('Va a borrar un correo electrónico, ¿está usted seguro?');
	$('#confirmPersonalData').dialog( "option", "buttons", [getCloseButton(), getDeleteEmailButton(primaryKey)]);
	$('#confirmPersonalData').dialog('open');
}

//Confirmación para activar las notificaciones Push
function prepareModifyPushNotif(primaryKey) {
	$('#confirmPersonalDataMessage').html('¿Está usted seguro de que desea activar las notificaciones Push?');
	$('#confirmPersonalData').dialog( "option", "buttons", [getCloseButton(), getModifyPushNotifButton(primaryKey)]);
	$('#confirmPersonalData').dialog('open');
}

//Obtiene el objeto que configura la acción de modificar e-mail
function getModifyPushNotifButton(primaryKey) {
	return {
		 text: "Aceptar",
		 "class": 'primary',
		 click: function() {modifyPushNotif(primaryKey);}
	};
}

//Ajax para modificar el flag de notificaciones Push
function modifyPushNotif(primaryKey) {
	$('#confirmPersonalData').dialog('close');
	var lnotify = 'N';
	if($('#lNotifyPush').prop('checked')) {
		lnotify = 'S';
	}
	var requestQuery = "primaryKey=" + primaryKey + '&lnotify=' + lnotify;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/modifyPushNotif",
    	dataType: 'json',
    	data: requestQuery,
    	async: true,
    	success:
    		function(model) {
        		if (model.status == "success") {
        			prepareOkMessage('Se ha modificado el valor de las notificaciones Push correctamente.');
        		} else {
        			showWarning(model.msgError);
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

//Confirmación para el borrado del check de notificaciones Push
function prepareDeletePushNotif(primaryKey) {
	$('#confirmPersonalDataMessage').html('¿Está usted seguro de que desea desactivar las notificaciones Push?');
	$('#confirmPersonalData').dialog( "option", "buttons", [getCloseButton(), getDeletePushNotifButton(primaryKey)]);
	$('#confirmPersonalData').dialog('open');
} 

//Obtiene el objeto que configura la acción de eliminar el flag de notificaciones Push
function getDeletePushNotifButton(primaryKey) {
	return {
		 text: "Aceptar",
		 "class": 'primary',
		 click: function() {deletePushNotif(primaryKey);}
	};
}

//Ajax para borrar el flag de notificaciones Push
function deletePushNotif(primaryKey) {
	$('#confirmPersonalData').dialog('close');
	var requestQuery = "primaryKey=" + primaryKey;
	// Se llama al servidor para poner la etiqueta a la petición
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/deletePushNotif",
    	dataType: 'json',
    	data: requestQuery,
    	async: true,
    	success:
    		function(model) {
        		if (model.status == "success") {
        			$('#lNotifyPush').prop("checked", false);
        		} else {
        			showWarning(model.msgError);
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

//Obtiene el objeto que configura la acción de eliminar e-mail
function getDeleteEmailButton(primaryKey) {
	return {
		 text: "Aceptar",
		 "class": 'primary',
		 click: function() {deleteEmail(primaryKey);}
	};
}


// Ajax para borrar un e-mail
function deleteEmail(primaryKey) {
	$('#confirmPersonalData').dialog('close');
	// Se llama al servidor para poner la etiqueta a la petición
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/deleteEmail",
    	dataType: 'json',
    	data: "primaryKey=" + primaryKey,
    	async: true,
    	success:
    		function(model) {
        		if (model.status == "success") {
        			$('#div_' + primaryKey).remove();
        		} else {
        			showWarning(model.msgError);
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



// Confirmación para modificar un e-mail
function prepareModifyEmail(primaryKey) {
	$('#confirmPersonalDataMessage').html('Va a modificar un correo electrónico, ¿está usted seguro?');
	$('#confirmPersonalData').dialog( "option", "buttons", [getCloseButton(), getModifyEmailButton(primaryKey)]);
	$('#confirmPersonalData').dialog('open');
}



//Obtiene el objeto que configura la acción de modificar e-mail
function getModifyEmailButton(primaryKey) {
	return {
		 text: "Aceptar",
		 "class": 'primary',
		 click: function() {modifyEmail(primaryKey);}
	};
}

// Ajax para modificar un e-mail
function modifyEmail(primaryKey) {
	$('#confirmPersonalData').dialog('close');
	var demail = $('#demail_' + primaryKey).val();
	var lnotify = 'N';
	if($('#lnotify_' + primaryKey).prop('checked')) {
		lnotify = 'S';
	}
	var requestQuery = "primaryKey=" + primaryKey + '&demail=' + demail + '&lnotify=' + lnotify;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/modifyEmail",
    	dataType: 'json',
    	data: requestQuery,
    	async: true,
    	success:
    		function(model) {
        		if (model.status == "success") {
        			prepareOkMessage('Se ha guardado el correo electrónico correctamente.');
        		} else {
        			showWarning(model.msgError);
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

// Prepara el dialogo para guardar el tamaño de página
function prepareSavePageSize() {
	$('#confirmPersonalDataMessage').html('Va a actualizar el tamaño de página, ¿está usted seguro?');
	$('#confirmPersonalData').dialog( "option", "buttons", [getCloseButton(), getSavePageSize()]);
	$('#confirmPersonalData').dialog('open');
}

// Obtiene el objeto que configura la acción de modificar tamaño de página
function getSavePageSize() {
	return {
		 text: "Aceptar",
		 "class": 'primary',
		 click: function() {savePageSize();}
	};
}

// Guarda el tamaño de pagina
function savePageSize() {
	$('#confirmPersonalData').dialog('close');
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/savePageSize",
    	dataType: 'json',
    	data: 'pageSize=' + $('#pageSize').val(),
    	type: 'post',
    	async: true,
    	success: function(errors) {
    		if(jQuery.isEmptyObject(errors)) {
    			prepareOkMessage('El tamaño de página se ha guardado correctamente.');
    		} else {
    			showError(getErrors(errors));
    		}
			jsAjaxStatus.stopAjax();
    	},
      	error: function error(jqXHR, textStatus, errorThrown) {
      		showError(genericMessage);
      		jsAjaxStatus.stopAjax();
      	}
	});
}

//Confirmación para modificar la opción de mostrar firmante anterior junto con el remitente
function prepareSaveMostrarFirmanteAnterior() {
	$('#confirmPersonalDataMessage').html('Va a modificar el parámetro Mostrar firmante anterior, ¿está usted seguro?');
	$('#confirmPersonalData').dialog( "option", "buttons", [getCloseButton(), getModifyFirmanteAnterior()]);
	$('#confirmPersonalData').dialog('open');
}

//Obtiene el objeto que configura la acción de modificar firmante anterior
function getModifyFirmanteAnterior() {
	return {
		 text: "Aceptar",
		 "class": 'primary',
		 click: function() {modifyFirmanteAnterior();}
	};
}

// Ajax para modificar el parametro de usuario Mostrar firmante anterior
function modifyFirmanteAnterior() {
	$('#confirmPersonalData').dialog('close');
	var mostrarFirmanteAnterior = 'N';
	if($('#mostrarFirmanteAnterior').prop('checked')) {
			mostrarFirmanteAnterior = 'S';
	}
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/modifyMostrarFirmanteAnterior",
    	dataType: 'json',
    	data: 'mostrarFirmanteAnterior=' + mostrarFirmanteAnterior,
    	type: 'get',
    	async: true,
    	success: function(errors) {
    		if(jQuery.isEmptyObject(errors)) {
    			prepareOkMessage('La modificación del parámetro se ha guardado correctamente.');
    		} else {
    			showError(getErrors(errors));
    		}
			jsAjaxStatus.stopAjax();
    	},
      	error: function error(jqXHR, textStatus, errorThrown) {
      		showError(genericMessage);
      		jsAjaxStatus.stopAjax();
      	}
	});
}

