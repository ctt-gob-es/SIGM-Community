// Indica si ya se cargaron los servidores
var isLoadServers = false;

//Prepara la modificación de un servidor
function prepareModifyServer(primaryKey) {
	$('#serverPrimaryKey').val(primaryKey);
	$('#serverCode').val($('#server_code_' + primaryKey).html());
	$('#serverDescription').val($('#server_description_' + primaryKey).html());
	if ($('#isDefault_' + primaryKey).val() == 'true') {
		$('#serverIsDefault').prop("checked",true);
	} else {
		$('#serverIsDefault').prop("checked",false);
	}	
	$('#servers_modal').dialog('open');
}

//Prepara el alta de un servidor
function prepareNewServer() {
	$('#serverPrimaryKey').val('');
	$('#serverCode').val('');
	$('#serverDescription').val('');
	$('#serverIsDefault').prop("checked",false);	
	$('#servers_modal').dialog('open');
}

//Para insertar o modificar una sede
function saveServer() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/server/saveServer",
		type: 'post',
		dataType: 'json',
		data: "primaryKey=" + $('#serverPrimaryKey').val() +
			  "&serverCode=" + $('#serverCode').val() + 
			  "&serverDescription=" + $('#serverDescription').val() +
			  "&serverIsDefault=" + $('#serverIsDefault').prop('checked'),
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadServers();
			} else {
				$('#serverErrorMessage').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#servers_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

//Prepara el borrado de un servidor
function prepareDeleteServer(primaryKey) {
	$('#questionMessageServer').html('Va a borrar un servidor, ¿está usted seguro?');
	$('#yesButtonServer').attr('onclick',"deleteServer('" + primaryKey + "');");
	$('#questionServer').show();
}

//Para borrar una sede
function deleteServer(primaryKey) {
	$('#questionServer').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/server/deleteServer",
		dataType: 'json',
		data: "primaryKey=" + primaryKey,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadServers();
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
function loadServers() {	
	if(!isLoadServers) {
		reloadServers();
		isLoadServers = true;
	}
}

// Para recargar los servidores
function reloadServers() {
	// hay cambios en los servidores y queremos que se reflejen
	// la próxima vez que se visite la pestaña de configuraciones
	isLoadServerConfig = false;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/server/loadServers",
		dataType: 'html',
		async: false,
		success: function(model) {
        	$('#servers_modal').dialog("close");
			$("#tab3").html(model);
			setupServer();
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
        	$('#servers_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
};

function setupServer() {
	$("#servers_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 280,
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
	            	saveServer();
	            }
	        }           
	    ],   
	    create: customModalStyle,
	    close:function () {
	    	$('#serverErrorMessage').html('');
	    }
	});
}