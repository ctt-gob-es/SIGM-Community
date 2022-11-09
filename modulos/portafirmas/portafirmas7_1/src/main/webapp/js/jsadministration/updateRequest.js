
// Indica si ya se cargaron las tareas programadas
var isUpdateRequest = false;

//Para cargar las las tareas programadas
function loadUpdateRequest() {
	if(!isUpdateRequest) {
		reloadUpdateRequest("1");
		isUpdateRequest = true;
	} 
}

//Para recargar las tareas
function reloadUpdateRequest(msm) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "updateRequest/loadUpdateRequest",
		type: 'post',
		dataType: 'html',
		async: false,
		success: function(model) {
			$("#tab7").html(model);
			if(msm == 1){
				$("#mensajeUpdateRequest").html('');
			} else {
				if(msm.indexOf("No ") != -1){//Mensaje de Error
				  $("#mensajeUpdateRequest").html("<p class='capaMensajeError'>" + msm + "</p>");
				}else{//Mensaje de Confirmacion
					$("#mensajeUpdateRequest").html("<p class='capaMensajeConfirmacion'>" + msm + "</p>");
				}
			}
			
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
        	jsAjaxStatus.stopAjax();
		}
	});
};

function mostrarDocumentos(id, fileName) {
	// Si estaba activo el mensaje de error se quita
	var errorMsg = $("#uploadedDocsErrorMessage");
	if (errorMsg != null) {
		errorMsg.remove();
	}
	
	var idDoc = 'document_' + id;
	$("#uploadedDocs > tbody").append('<tr id="' + idDoc + '"><td><span>' + fileName + '</span></td>'+
		  '<td> <a href="javascript:void(0)" onclick="borrarDocumento(\'' + idDoc + '\',\'type=document&id=' + id + '\');" class="va_top right" >' + 
		  '<span class="mf-icon mf-icon-delete-16" ></span></a></td></tr>');
}

function errorDocumentos(id, fileName) {
	showWarning('El documento ' + fileName + " NO cumple con las pautas establecidas");
}

function borrarDocumento(idDoc, queryString) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'updateRequest/deleteDoc',
     	dataType: 'html',
     	data: queryString,
     	async: false,
     	success:
     		function(model) {
     			$("#" + idDoc).remove();
     			jsAjaxStatus.stopAjax();
     	},
       	error:
       		function error(jqXHR, textStatus, errorThrown) {
	       		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
	      		$('#error').show();
	      		jsAjaxStatus.stopAjax();
       		}
	});

	return false;
}


