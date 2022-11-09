// Indica si ya se cargaron las tareas programadas
var isLoadTasks = false;

//Para cargar las las tareas programadas
function loadTasks() {
	if(!isLoadTasks) {
		reloadTasks(null);
		isLoadTasks = true;
	}
}

// Llama al método del controlador encargado de parar la tarea. Si hay éxito recarga la lista.
function stopTask (name, group) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/task/stopTask",
		type: 'post',
		dataType: 'json',
		data: "taskName=" + name +
			  "&taskGroup=" + group,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadTasks($('#searchTaskTextField').val());
			} else {
				//$('#serverErrorMessage').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	//$('#servers_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

//Llama al método del controlador encargado de parar la tarea. Si hay éxito recarga la lista.
function startTask (name, group) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/task/startTask",
		type: 'post',
		dataType: 'json',
		data: "taskName=" + name +
			  "&taskGroup=" + group,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadTasks($('#searchTaskTextField').val());
			} else {
				//$('#serverErrorMessage').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	//$('#servers_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function prepareDeleteTask (name, group) {
	$('#questionMessageTask').html('Va a borrar la tarea "' + name + '", ¿está usted seguro?');
	$('#yesButtonTask').attr('onclick','deleteTask("' + name + '","' + group + '");');
	$('#questionTask').show();
}

//Llama al método del controlador encargado de borrar la tarea. Si hay éxito recarga la lista.
function deleteTask (name, group) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/task/deleteTask",
		type: 'post',
		dataType: 'json',
		data: "taskName=" + name +
			  "&taskGroup=" + group,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadTasks($('#searchTaskTextField').val());
			} else {
				//$('#serverErrorMessage').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	//$('#servers_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

// Para recargar las tareas
function reloadTasks(filter) {
	var data = "taskFilter=";
	if (filter != null) {
		data = "taskFilter=" + filter;		
	}
		
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/task/loadTasks",
		dataType: 'html',
		data: data,
		async: false,
		success: function(model) {
        	//$('#seat_modal').dialog("close");
			$("#tab6").html(model);
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
        	//$('#seat_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
};

// Búsqueda 
function search (event) {
	var key=event.keyCode || event.which;
	if (key==13){ 
		reloadTasks($('#searchTaskTextField').val());
	}
}