function inicializarFragmentoUsuarios(){
	
	$('#idInputOrganismo').autocomplete({
		source: 'organismsManagement/queryOrganismos',
		// Función que se ejecuta al seleccionar un elemento
		select: function( event, ui ) {
			$('#idInputOrganismo').val(ui.item.label);
			return false;
		}
	});
}

function actualizarInformacionAutentica(idUsuario){
	
	var cambiar = confirm("Esta seguro que desea actualizar el cargo del usuario con la informacion de autentica?")
	
	if(cambiar){
		jsAjaxStatus.startAjax();
		$.ajaxSetup({cache: false});
		$.ajax({url: "usersManagement/userJob/actualizarUsuarioConAutentica?idUsuario="+idUsuario,
			type: 'get',
			async: false,
			success:
			function(data) {
				var mensaje = 'Ha actualizado el usuario';
				if(data.length>0){
					mensaje = "No se ha podido actualizar por los siguientes errores: \n";
					for(var indice = 0 ; indice<data.length; ++indice){
						mensaje+=data[indice]+"\n";
					}
				}
				alert(mensaje);
				if( !( data.length>0 ) ){
					location.reload();
				}
			},
			error: function error(jqXHR, textStatus, errorThrown) {
				var mensaje = 'Ha ocurrido un error en el llamado';
				alert(mensaje);
			}
		});
		jsAjaxStatus.stopAjax();
	}

}

// Preparación para cargar los cargos del usuario
function prepareLoadUserJob(primaryKey) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/userJob/load",
		type: 'post',
		dataType: 'html',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(model) {
			$('#users_jobs_modal').html(model);
			setupUserJob();
			$('#users_jobs_modal').dialog('open');
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			jsAjaxStatus.stopAjax();
		}
	});
}

// Configura los calendarios y el autocompletado
function setupUserJob() {
	// Configuración de las máscaras
	var dates = $('input[id^="date"]');
	dates.each(function(i, item) {
		$(item).mask("99/99/9999");
	});
	
	// Se configuran los calendarios
	$('.datepicker').datepicker();
	
	// Configuración del autocomplete
	$('#jobName').autocomplete({
		 minLength: 3,
		 source: 'usersManagement/userJob/autocomplete',
		 select: function( event, ui ) {
			 $('#jobName').val(ui.item.label);
			 $('#jobId').val(ui.item.id);
			 return false;
		 }
	});
	// Ventana modal para edición de los cargos de usuario
	$("#users_jobs_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 650,
	    modal: true,       
	    buttons: [
	        {
	        	text: "Cerrar",       
	            "class": 'secondary',
	            click: function() {                
	            	$(this).dialog("close");
	            }
	        }         
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	});
}

// Muestra una nueva fila para asignar un cargo al usuario
function prepareInsertJob() {
	$('#errorMsgUserJob').html('');
	$('#jobName').val('');
	$('#jobId').val('');
	$('#dateStart').val('');
	$('#dateEnd').val('');
	$('#trNewJob').show();
}

// Guarda una relación cargo-usuario
function saveUserJob() {
	$('#questionUserJob').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/userJob/save",
		type: 'post',
		dataType: 'json',
		data: $("#users_jobs_form").serialize(),
		async: true,
		success:
			function(warning) {
			// Si no hay avisos, se refresca la ventana modal
			if(jQuery.isEmptyObject(warning)) {
				prepareLoadUserJob($('#loadUserId').val());
			} else {
				// Si se detectan errores, se muestran los avisos en la modal
				$('#errorMsgUserJob').html(getErrors(warning));
//				jsAjaxStatus.stopAjax();
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

// Muestra el diálogo para modificar
function prepareModifyUserJob(primaryKey, name) {
	$('#errorMsgUserJob').html('');
	$('#questionMessageUserJob').html('Va a modificar el cargo "' + name + '", ¿está usted seguro?');
	$('#yesButtonUserJob').attr('onclick','modifyUserJob(' + primaryKey + ');');
	$('#questionUserJob').show();
}

// Coloca los datos para poder hacer el alta
function modifyUserJob(primaryKey) {
	$('#userJobPk').val(primaryKey);
	$('#jobId').val($('#jobId_' + primaryKey).val());
	$('#dateStart').val($('#dateStart_' + primaryKey).val());
	$('#dateEnd').val($('#dateEnd_' + primaryKey).val());
	$('#trNewJob').hide();
	saveUserJob();
}

// Muestra el diálogo para borrar
function prepareDeleteUserJob(primaryKey, name) {
	$('#errorMsgUserJob').html('');
	$('#questionMessageUserJob').html('Va a eliminar el cargo "' + name + '", ¿está usted seguro?');
	$('#yesButtonUserJob').attr('onclick','deleteUserJob(' + primaryKey + ');');
	$('#questionUserJob').show();
}

// Borra una relación cargo-usuario
function deleteUserJob(primaryKey) {
	$('#questionUserJob').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/userJob/delete",
		type: 'post',
		dataType: 'json',
		data: 'userJobPk=' + primaryKey,
		async: true,
		success:
			function(warnings) {
			// Si no hay avisos, se refresca la ventana modal
			if(jQuery.isEmptyObject(warnings)) {
				prepareLoadUserJob($('#loadUserId').val());
			} else {
				$('#errorMsgUserJob').html(getErrors(warnings));
//				jsAjaxStatus.stopAjax();
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
