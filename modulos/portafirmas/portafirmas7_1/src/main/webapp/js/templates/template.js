var listaAccionesFirmante;


function saveTemplates() {
	jsAjaxStatus.startAjax();
	var templateId = $('#templateId').val();
	var	templateName = $('#templateCode').encodeValue();
	$.ajaxSetup({cache: false});
	$.ajax({url: "template/validate",
		dataType: 'json',
     	data: "templateName=" + encodeURIComponent(window.btoa(templateName)) + "&templateId=" + templateId,
		async: false,
		success:
			function(model) {		
				if (model.status == "success") {
					enviarFormulario('templateForm', 'guardar', [{parameterId:'signlinesType', parameterValue: loadSignLineConfig()},{parameterId:'signlinesAccion', parameterValue: loadSignLineAcciones()}]);
				} else {
					var messageError = model.log;
					$('#error').html(messageError);
					$('#error').show();
					jsAjaxStatus.stopAjax();
				}
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	});
};
