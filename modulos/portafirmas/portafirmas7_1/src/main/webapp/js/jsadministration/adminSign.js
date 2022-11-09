
// Indica si ya se cargaron las tareas programadas
var isAdminSign = false;

//Para cargar las las tareas programadas
function loadSign() {
	if(!isAdminSign) {
		loadData();
		isAdminSign = true;
	} 
}

function loadData() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		url: "administration/adminSign/loadSign",
		type: 'post',
		dataType: 'html',
		data: $("#adminSignForm").serialize(),
		async: true,
		success: function(model) {
			$("#tab9").html(model);
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			jsAjaxStatus.stopAjax();
		}
	});
}

function reSign() {
	var selectedSigns = $('input[id="sign_checkbox"]:checked');
	if (selectedSigns.length == 0) {
		$('#warning').html('Debe seleccionar al menos una firma para resellar');
		$('#warning').show();
	} else {
		jsAjaxStatus.startAjax();
		var signsIds = getCheckIds(selectedSigns);
		$("#idSign").val(signsIds);
		$.ajaxSetup({cache: false});
		$.ajax({
			url: "administration/adminSign/reSign",
			type: 'post',
			dataType: 'html',
			data: $("#adminSignForm").serialize(),
			async: true,
			success: function(model) {
				$("#tab9").html(model);
				if ($("#mensajeError").val() != '') {
					showError($("#mensajeError").val());
				} else {
					showSuccess(genericMessageSuccess);
				}
				jsAjaxStatus.stopAjax();
			},
			error: function error(jqXHR, textStatus, errorThrown) {
				showError(genericMessage);
				jsAjaxStatus.stopAjax();
			}
		});
	}
}

function showSuccess(mensaje) {
	$('#success').html(mensaje);
	$("html, body").animate({ scrollTop: "0px" });
	$('#success').fadeIn(800).delay(3000).fadeOut(800);
}

//Concatena ids separados por comas a partir de un array de cadenas
function getCheckIds(selectedSigns) {
	var signIds = '';
	selectedSigns.each(function(i, item) {
		signIds += $(item).val() + ";";
	});
	return signIds.substr(0, signIds.length - 1);
}