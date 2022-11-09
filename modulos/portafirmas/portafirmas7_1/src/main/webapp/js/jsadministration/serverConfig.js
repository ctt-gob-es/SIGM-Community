// Indica si ya se cargaron las configuraciones de servidor
var isLoadServerConfig = false;

// Para cargar las configuraciones una única vez
function loadServerConfig() {
	if(!isLoadServerConfig) {
		reLoadServerConfig('');
		isLoadServerConfig = true;
	}
}

// Para recargar las configuraciones
function reLoadServerConfig(searchServerPK) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/serverConfig/load",
		type: 'post',
		dataType: 'html',
		data: "searchServerPk=" + searchServerPK,
		async: true,
		success: function(model) {
			$("#tab8").html(model);
			setupServerConfig();
        	$('#serverConfig_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html(genericMessage);
			$('#error').show();
        	$('#serverConfig_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
};

function setupServerConfig() {
	//Se crea el diálogo de la ventana modal de configuracones de servidor
	$("#serverConfig_modal").dialog({
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
	            	saveServerConfig();
	            }
	        }           
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	    close:function () {
	       //Limpiar campos del formulario del dialog, etc
	    	$('#serverConfigErrorMessage').html('');
	    }
	});
	//Se crea el diálogo de la ventana modal de parametros de configuración
	$("#parameterConfig_modal").dialog({
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
	            },
	        },
	        {                   
	        	text: "Guardar",                   
	            "class": 'primary',
	            click: function() {
	            	saveParameterConfig();
	            }
	        }           
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	    close:function () {
	       //Limpiar campos del formulario del dialog, etc
	    	$('#parameterConfigErrorMessage').html('');
	    }
	});
}

function prepareNewServerConfig(primaryKey) {
	$('#configurationPk').val('');
	$('#cConfiguration').val('');
	$('#serverPk').val($('#searchServerPk').val());
	$('#mainConfig').removeProp("checked");
	$('#serverConfig_modal').dialog("open");
}

function prepareModifyServerConfig(primaryKey) {
	$('#configurationPk').val(primaryKey);
	$('#cConfiguration').val($('#cConfiguration_' + primaryKey).val());
	$('#serverPk').val($('#serverPk_' + primaryKey).val());
	$('#mainConfig').prop("checked",$('#mainConfig_' + primaryKey).val() == 'true');
	$('#serverConfig_modal').dialog("open");
}

function saveServerConfig() {
	$('#questionServerConfig').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/serverConfig/save",
		type: 'post',
		dataType: 'json',
		data: $("#serverConfig_form").serialize(),
		async: true,
		success:
			function(warnings) {
			// Si no hay avisos, se refresca la ventana modal
			if(jQuery.isEmptyObject(warnings)) {
				reLoadServerConfig($('#searchServerPk').val());
			} else {
				// Si se detectan errores, se muestran los avisos en la modal
				$('#serverConfigErrorMessage').html(getErrors(warnings));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#users_jobs_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function prepareDeleteServerConfig(primaryKey, name) {
	$('#questionMessageServerConfig').html('Va a borrar la configuración de servidor "' + name + '", ¿está usted seguro?');
	$('#yesButtonServerConfig').attr('onclick','deleteServerConfig(' + primaryKey + ');');
	$('#questionServerConfig').show();
}

function deleteServerConfig(primaryKey) {
	$('#questionServerConfig').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/serverConfig/delete",
		type: 'post',
		dataType: 'json',
		data: 'configurationPk=' + primaryKey,
		async: true,
		success:
			function(errors) {
			// Si no hay avisos, se refresca la ventana modal
			if(jQuery.isEmptyObject(errors)) {
				reLoadServerConfig($('#searchServerPk').val());
			} else {
				showError(getErrors(errors));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#users_jobs_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function prepareLoadParameters(primaryKey) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/parameterConfig/load",
		type: 'post',
		dataType: 'html',
		data: "searchServerPk=" + searchServerPK,
		async: true,
		success: function(model) {
			$("#parameterConfig_modal").html(model);
			$('#parameterConfig_modal').dialog("open");
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html(genericMessage);
			$('#error').show();
			$('#parameterConfig_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function saveParameterConfig() {
	
}