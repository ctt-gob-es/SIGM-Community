// Indica si ya se cargó la pantalla
var isLoadActualizarDIR3 = false;


//Para cargar los servidores una única vez
function cargaActualizarDIR3() {
	$('#divActualizarDIR3ErrorMessage').hide();
	$('#filtroCodigo').val('');
	$('#messageFStart').val('');
	if(!isLoadActualizarDIR3) {
		reloadActualizarDIR3();
		isLoadActualizarDIR3 = true;
		
	}
}

// Para recargar las últimas actualizaciones de DIR3
function reloadActualizarDIR3() {
	// hay cambios en las actualizaciones y queremos que se reflejen
	// la próxima vez que se visite la pestaña de configuraciones
	isLoadActualizarDIR3 = false;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/administrarDIR3/cargaActualizarDIR3",
		dataType: 'html',
		async: false,
		success: function(model) {
			isLoadActualizarDIR3 = true;
			$("#tab11").html(model);
			$('#divActualizarDIR3ErrorMessage').hide();
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	});
	
	configureCalendars();
};

function doActualizarDIR3() {
	
	jsAjaxStatus.startAjax();
	$('#divActualizarDIR3ErrorMessage').hide();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/administrarDIR3/actualizarDIR3",
		type: 'post',
		dataType: 'json',
		data: $("#actualizarDIR3Form").serialize(),
		async: false,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadActualizarDIR3();
			} else {
				$('#actualizarDIR3ErrorMessage').html(getErrors(data));
				$('#divActualizarDIR3ErrorMessage').show();
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


function configureCalendars() {
	// Se configuran los calendarios
	$( ".datepicker" ).datepicker();
	
	$.mask.definitions['h'] = "[0-2]";
	$.mask.definitions['t'] = "[0-5]";

	$('#messageFStart').mask("99/99/9999");
	
}


function subirNuevoDIR3(inputFicheroUnidades) {
	var inputFiles = $('#' + inputFicheroUnidades);
	
	var reader = new FileReader();
    reader.onload = function(e) {
    	var base64File = e.target.result;
		var indice = base64File.indexOf('base64');
		var archivoCompletoEnBase64 = base64File.substring(indice + 'base64'.length, base64File.length);
		var archivoAEnviarAServidor = {};
		archivoAEnviarAServidor.nombreDeArchivo = "Unidades.xml";
		archivoAEnviarAServidor.contenidoDeArchivo = archivoCompletoEnBase64;
		archivoAEnviarAServidor.carpetaTemporal = "";
		archivoAEnviarAServidor.tipoArchivo = "text/xml";var retorno;
		jsAjaxStatus.startAjax();
		$.ajaxSetup({cache: false});
		$.ajax({url: "administration/administrarDIR3/subirFicheroDIR3AProcesar",
			type: 'post',
			contentType: 'application/json',
			data: JSON.stringify(archivoAEnviarAServidor),
	        dataType: 'json',
			async: false,
			success:
				function(data) {
				if(jQuery.isEmptyObject(data)) {
					reloadActualizarDIR3();
				} else {
					$('#actualizarDIR3ErrorMessage').html(getErrors(data));
					$('#divActualizarDIR3ErrorMessage').show();
				}
				jsAjaxStatus.stopAjax();
			},
			error:
				function error(jqXHR, textStatus, errorThrown) {
				showError(genericMessage);
				jsAjaxStatus.stopAjax();
			}

		});
    };
    reader.readAsDataURL(inputFiles[0].files[0]);
    
	
	
	
	
}


