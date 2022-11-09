// Indica si ya se cargaron los totales
var isLoadTotals = false;

// Carga los totales
function loadTotals() {	
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "stats/loadTotals",
		dataType: 'html',
		async: false,
		success: function(model) {
			isLoadTotals = true;
			$("#requestStats").html(model);			
			$('#error').hide();
			jsAjaxStatus.stopAjax();
			$('#requestStats').slideDown("slow");
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html(genericMessage);
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	});
};

// Para exportar el listado que se está visualizando
function exportStat(format){
	$('#format').val(format);
	$('#statForm').attr('action','stats/exportStats');
	$('#statForm').submit();
}

// Valores iniciales de los campos de búsqueda.
function clean() {
	 $("#select_req_sig").val('requests');
	 $("#select_type").val('xu');
	 $("#statsFechaInicio").val('');
	 $("#statsFechaFin").val('');
	 $("#select_seat").val('');
	 $("#select_application").val('');
	 $("#statsUserName").val('');
	 $("#statsUserId").val('');
}

// Cambia el texto del combo según sea peticion o firma
function toggleReqSig(value) {
	var name = 'destinatario';
	if(value == 'requests') {
		name = 'remitente';
	}
	$("#select_type option[value='xu']").text('Por usuario ' + name);
	$("#select_type option[value='xs']").text('Por sede ' + name);
	$("#select_type option[value='xsu']").text('Por sede ' + name + ' y usuario ' + name);
	$("#select_type option[value='xsuf']").text('Por sede ' + name + ', usuario ' + name + ' y fecha');	
	//$("#select_seat option[value='']").text('Sede ' + name);
	$("#statsUserName").attr("placeholder", "Usuario " + name);
}

// Al cargar el documento
$(document).ready(function() {
	toggleReqSig($('#select_req_sig').val());
	
	// Botón para mostrar las estadísticas 
	$('#toggleRequestInfo').click(function(e) {
		e.preventDefault();
		if(isLoadTotals) {
			$('#requestStats').slideToggle("slow");
		} else {
			loadTotals();
		}
	});	

	// Botón para realizar búsquedas
	$("#searchButton").click(function (e) {
		$('#statForm').attr('action','stats');
	});

	// Botón para limpiar los campos de búsqueda
	$("#cleanButton").click(function (e) {
		e.preventDefault();
		clean();
	});

	// Si se seleccionan estadísticas por sede y aplicación se elimina del combo la opción de desglose.
	$("#select_type").change(function() {
		if ($(this).val() == 'xsa' || $(this).val() == 'xsaf') {			
			$("#select_total_desg option[value='desglose']").remove();
		// Si se selecciona cualquier otra opción, se añade la opción al combo (Si no la tuviera).
		} else {
			if ( $("#select_total_desg option[value='desglose']").length == 0 ){				  
				  $('#select_total_desg').append($('<option>', {value:'desglose', text:'Desglose'}));
			}
		}	
	});
	
	// Dependiendo de si se elige peticiones o firmas, cambiará el texto de las opciones de los combos y los filtros.
	$("#select_req_sig").change(function() {
		toggleReqSig($(this).val());
	});
	
	// Autocompletado del campo de filtro de usuario.
	$('#statsUserName').autocomplete({
		 minLength: 3,
		 source: 'stats/autocompleteUser',
		 select: function( event, ui ) {
			 $('#statsUserName').val(ui.item.label);
			 $('#statsUserId').val(ui.item.id);
			 return false;
		 }
	});	
});




