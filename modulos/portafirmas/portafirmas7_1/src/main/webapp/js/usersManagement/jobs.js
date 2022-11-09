// Para indicar si la pestaña está cargada
var isLoadJobs = false;

// Carga la lista de cargos
function loadJobs() {
	if(!isLoadJobs) {
		reloadJobs('get', null);
		isLoadJobs = true;
	}
}

//Recarga la lista de cargos
function reloadJobs(type, data) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		url: 'userManagement/jobs',
		dataType: 'html',
		data: data,
		type: type,
		async: false,
		success: function(model) {
        	$('#jobs_modal').dialog("close");
			$("#tab2").html(model);//removeHTMLTag(model));
			setupJobs();
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html(genericMessage);
			$('#error').show();
        	$('#jobs_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function setupJobs() {
	// Se sobre-escribe el evento submit del formulario
	$('#jobsForm').submit(function (e) {
		e.preventDefault();
		goJobPage(1);
	});
	//Se crea el diálogo de la ventana modal
	$("#jobs_modal").dialog({
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
	            }
	        },
	        {                   
	        	text: "Aceptar",                   
	            "class": 'primary',
	            click: function() {
	            	saveJob();
	            }
	        }           
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	    close:function () {
	    }
	});	
}

function prepareNewJob() {
	$('#codeJob').val('');
	$('#primaryKeyJob').val('');
	$('#descriptionJob').val('');
	$('#provinceJob').val('');
	$('#validJob').prop("checked",true);
	$('#publicoJob').removeProp("checked");
	$('#errorMsgJob').html('');
	$("#jobs_modal").dialog("open");
	$('#codeJob').attr('onblur','aMaysTrim(this);');
	$('#codeJob').attr('onkeyup','aMaysTrim(this)');

}

function prepareModifyJob(primaryKey) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "userManagement/jobs/load",
		type: 'post',
		dataType: 'html',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(model) {
			$('#jobs_modal').html(model);
			$('#jobs_modal').dialog('open');
			$('#codeJob').attr('onblur','aMaysTrim(this);');
			$('#codeJob').attr('onkeyup','aMaysTrim(this)');
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#jobs_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function saveJob() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "userManagement/jobs/save",
		type: 'post',
		dataType: 'json',
		data: $("#jobForm_modal").serialize(),
		async: true,
		success:
			function(warning) {
			// Si no hay avisos, se refresca la página
			if(jQuery.isEmptyObject(warning)) {
				goJobPage(1);
			} else {
				// Si se detectan errores, se muestran los avisos en la modal
				$('#errorMsgJob').html(getErrors(warning));
//				jsAjaxStatus.stopAjax();
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#jobs_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function prepareDeleteJob(primaryKey, name) {
	$('#questionMessageJob').html('Va a borrar el cargo "' + name + '", ¿está usted seguro?');
	$('#yesButtonJob').attr('onclick','deleteJob(' + primaryKey + ');');
	$('#questionJob').show();
}

// Borrado de cargo
function deleteJob(primaryKey)  {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "userManagement/jobs/delete",
		type: 'post',
		dataType: 'json',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(response) {
			// Si no hay avisos, es que se ha borrado y refrescamos la página
			if(	   jQuery.isEmptyObject(response.warnings)
				&& jQuery.isEmptyObject(response.isUndelete)
				&& jQuery.isEmptyObject(response.errors)) {
				goJobPage(1);
			} else if (!jQuery.isEmptyObject(response.errors)) {
				// Hay errores, se oculta la venta de confimación y se muestra el error
				$('#questionJob').hide();
				showError(getErrors(response.errors));
			} else if (!jQuery.isEmptyObject(response.warnings)) {
				// Hay avisos, se oculta la venta de confimación y se muestran los avisos
				$('#questionJob').hide();
				showWarning(getErrors(response.warnings));
			} else if (!jQuery.isEmptyObject(response.isUndelete)) {
				// Hay avisos de que no se puede borrar. Se pregunta si se quiere deshabilitar
				prepareRevokeJob(primaryKey, getErrors(response.isUndelete));
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

//Preparación para deshabilitar al cargo
function prepareRevokeJob(primaryKey, warning) {
	$('#questionMessageJob').html(warning + '¿desea deshabilitar el cargo?');
	$('#yesButtonJob').attr('onclick','revokeJob(' + primaryKey + ');');
	$('#questionJob').show();
}

// Deshabilita a un cargo
function revokeJob(primaryKey) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "userManagement/jobs/revoke",
		type: 'post',
		dataType: 'json',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(errors) {
			// Si no hay errores, se refresca la página
			if(jQuery.isEmptyObject(errors)) {
				goJobPage(1);
			} else {
				// Hay errores, se oculta la venta de confimación y se muestra el error
				$('#questionJob').hide();
				showError(getErrors(errors));
//				jsAjaxStatus.stopAjax();
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

// Configuración de la ordenación
function orderJob(campo){
	// Configuración del sentido de la ordenación
	if($('#orderJobField').val() == campo){
		if($('#orderJob').val() == 'asc'){
			$('#orderJob').val('desc');
		}else{
			$('#orderJob').val('asc');
		}
	}else{
		$('#orderJob').val('asc');
	}
	// Configuracion de campo por el que se ordena
	$('#orderJobField').val(campo);
	
	goJobPage(1);
}

//Refresca el listado de cargos
function goJobPage(page){
	$('#currentJobPage').val(page);
	reloadJobs('post', jQuery('#jobsForm').serialize());
}

function aMaysTrim(e) {
	var textoAux =  e.value; 
	textoAux = textoAux.toUpperCase();
    e.value = textoAux.trim();
}
