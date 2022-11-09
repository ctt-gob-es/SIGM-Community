function prepareLoadParameters(primaryKey) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/parameterConfig/load",
		type: 'post',
		dataType: 'html',
		data: "configurationPk=" + primaryKey,
		async: true,
		success: function(model) {
			$("#parameterConfig_modal").html(model);
			setupParameterConfig();
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

function prepareSaveParameterConfig() {
	$('#questionMessageParamConfig').html('Va a actualizar la configuración, ¿está usted seguro?');
	$('#yesButtonParamConfig').attr('onclick','saveParameterConfig();');
	$('#questionParamConfig').show();
}

function saveParameterConfig() {
	$('#questionParamConfig').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/parameterConfig/save",
		type: 'post',
		dataType: 'json',
		data: $("#paramConfig_form").serialize(),
		async: true,
		success:
			function(warning) {
			// Si no hay avisos, se refresca la ventana modal
			if(jQuery.isEmptyObject(warning)) {
				$('#parameterConfig_modal').dialog("close");
			} else {
				// Si se detectan errores, se muestran los avisos en la modal
				$('#paramConfigErrorMessage').html(getErrors(warning));
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

function setupParameterConfig() {
	//Se crea el diálogo de la ventana modal de parametros de configuración
	$("#parameterConfig_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 850,
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
	            	prepareSaveParameterConfig();
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