// Indica si ya se cargaron los mensajes
var isLoadMessages = false;

function cleanMessage() {
	$("#messageErrorMessage").html('');
}

//Prepara la modificación de un mensaje
function prepareModifyMessage(primaryKey) {
	
	cleanMessage();
	
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/message/message",
		type: 'post',
		dataType: 'html',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(model) {
			$('#messages_modal').html(model);
			
			changeScope();
			disableMessageFields(true);
			prepareMessageConfirm();
			
			configureCalendars();
			
			$('#messages_modal').dialog('open');
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#messages_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

//Prepara el alta de un servidor
function prepareNewMessage() {
	cleanMessage();
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; 

	var yyyy = today.getFullYear();
	if(dd<10) {
	    dd='0' + dd;
	} 
	if(mm<10) {
	    mm='0' + mm;
	} 
	var today = dd+'/'+mm+'/'+yyyy;

	$('#messagePrimaryKey').val("");
	$('#messageSubject').val("");
	$('#messageText').val("");
	$('#messageFExpiration').val("");
	$('#messageFStart').val(today);
	$('#userMessage').val("");
	$('#userNameMessage').val("");	
	$('#provinceMessage').val("");
	$('#scopeType').val("");
	$('#userNameMessage').val("");	
	$('#provinceMessage').val("");
	$('#scopeType').val("");
	disableMessageFields(false);
	changeScope();
	prepareMessageConfirm();
	
	configureCalendars();
	
	$('#messages_modal').dialog('open');
}

//Para insertar o modificar una sede
function saveMessage() {
	$('#confirmMessage').dialog("close");
	
	disableMessageFields(false);
	
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/message/saveMessage",
		type: 'post',
		dataType: 'json',
		data: $("#messageForm_modal").serialize(),
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadMessages();
			} else {
				$('#messageErrorMessage').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#messages_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

//Prepara el borrado de un mensaje
function prepareDeleteMessage(primaryKey) {
	$('#questionMessageMessage').html('Va a borrar un mensaje, ¿está usted seguro?');
	$('#yesButtonMessage').attr('onclick',"deleteMessage('" + primaryKey + "');");
	$('#questionMessage').show();
}

//Para borrar una sede
function deleteMessage(primaryKey) {
	$('#questionMessage').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/message/deleteMessage",
		dataType: 'json',
		data: "primaryKey=" + primaryKey,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadMessages();
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


//Para cargar los servidores una única vez
function loadMessages() {	
	if(!isLoadMessages) {
		reloadMessages();
		isLoadMessages = true;
		
	}
	
	prepareMessageConfirm();
}

// Para recargar los mensajes
function reloadMessages() {
	// hay cambios en los mensajes y queremos que se reflejen
	// la próxima vez que se visite la pestaña de configuraciones
	isLoadMessageConfig = false;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/message/loadMessages",
		dataType: 'html',
		async: false,
		success: function(model) {
        	$('#messages_modal').dialog("close");
			$("#tab10").html(model);
			setupMessage();
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
        	$('#messages_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
	
	configureCalendars();
};

function setupMessage() {
	
	loadFieldsAutocomplete();
	
	$("#messages_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 680,
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
	            	prepareSaveMessage();
	            }
	        }           
	    ],   
	    create: customModalStyle,
	    close:function () {
	    	$('#messsageErrorMessage').html('');
	    }
	});
	
}


function loadFieldsAutocomplete() {
	// Configuración de autocompletado para autorizaciones nuevas
	$('#userNameMessage').autocomplete({
		 minLength: 3,
		 source: 'administration/message/autocomplete',
		 select: function( event, ui ) {
			 $('#userNameMessage').val(ui.item.label);
			 $('#userMessage').val(ui.item.id);
			 return false;
		 }
	});
}

function changeScope() {
	var obj = $('#scopeType');
	if (obj.val() == '1') {
		$('#divUserMessage').attr('style', 'display: none;');
		$('#divProvinceMessage').attr('style', 'display: none;');
	} else if (obj.val() == '2') {
		$('#divUserMessage').attr('style', 'display: none;');
		$('#divProvinceMessage').attr('style', 'display: ;');
	} else if (obj.val() == '3') {
		$('#divUserMessage').attr('style', 'display: ;');
		$('#divProvinceMessage').attr('style', 'display: none;');
	} 
}

function disableMessageFields(disabled) {
	$('#scopeType').prop('disabled', disabled);
	$('#userNameMessage').prop('disabled', disabled);
	$('#provinceMessage').prop('disabled', disabled);
	$('#messageFStart').prop('disabled', disabled);

}

function prepareMessageConfirm() {
	//Se crea el diÃ¡logo para confirmaciones
	$("#confirmMessage").dialog({
	    autoOpen: false,
	    resizable: false,
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
	  	            	saveMessage();
	  	            }
	  	        }           
	  	    ],   
	    create: customModalStyle,
	    close:function () {}
	});
}

function prepareSaveMessage() {
	var textMessage = null;
	var scopeType = $('#scopeType');
	
	if (scopeType.val() == '1') {
		textMessage = 'Va a enviar un mensaje a todos los usuarios, ¿está usted seguro?';
	} else if (scopeType.val() == '2') {
		textMessage = 'Va a enviar un mensaje a todos los usuarios de la sede seleccionada, ¿está usted seguro?';
	} else {
		textMessage = 'Va a enviar un mensaje al usuario seleccionado, ¿está usted seguro?';
	}
	
	$('#confirmMessageMessage').html(textMessage);
	$('#confirmMessage').dialog('open');
}

function configureCalendars() {
	// Se configuran los calendarios
	$( ".datepicker" ).datepicker();
	
	$.mask.definitions['h'] = "[0-2]";
	$.mask.definitions['t'] = "[0-5]";

	$('#messageFStart').mask("99/99/9999");
	$('#messageFExpiration').mask("99/99/9999");
}