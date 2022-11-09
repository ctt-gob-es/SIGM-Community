/**
 * Se agrega el campo de cuantosCampos para los casos de parametros que utilizan mas de un componente grafico para ser configurados, como el caso de los jobs
 * @param campo
 * @param mostrar
 * @param cancelar
 * @param salvar
 * @param cuantosCampos
 */
function cambio (campo, mostrar, cancelar, salvar, cuantosCampos){
	$('#' + campo).prop('disabled', false );
	$('#' + mostrar).css('display', 'none');
	$('#' + cancelar).css('display', 'inline-block');
	$('#' + salvar).css('display', 'inline-block');
	
	if(cuantosCampos != undefined && cuantosCampos != null ){
		for(i=2; i<=cuantosCampos; ++i){
			$('#' + campo+i).prop('disabled', false );
		}
	}
	
}
	
/**
 * Se agrega el campo de cuantosCampos para los casos de parametros que utilizan mas de un componente grafico para ser configurados, como el caso de los jobs
 */
function guardar(pk,pk2,cuantosCampos){
	
	var valorEnviar = $('#guardar'+pk).val();
	
	if(cuantosCampos != undefined && cuantosCampos != null ){
		for(i=2; i<=cuantosCampos; ++i){
			valorEnviar = valorEnviar + " " + $('#guardar'+pk+i).val();
		}
	}
	var valorCodficadoEnBase64 = btoa(valorEnviar);
	
	if($('#guardar'+pk).val() == null || $('#guardar'+pk).val() == ''){
		$('#error'+pk).html("Si desea que el valor sea nulo, introduzca el caracter '-'");
		$('#error'+pk).show();
		cancelar(pk, cuantosCampos);
	} else {
		$('#error'+pk).html('');
		jsAjaxStatus.startAjax();
		$.ajaxSetup({cache: false});
		$.ajax({url: "administration/updateField",
			type:'post',
			data:"id=" + pk2+ "&valor=" + valorCodficadoEnBase64,
			dataType: 'json',
			async: false,
			success: function(errors) {
					if(jQuery.isEmptyObject(errors)) {
						//Volvemos al estado inicial
						$('#guardar'+pk).val($('#guardar'+pk).val());
						$('#ocultar'+pk).val($('#guardar'+pk).val());
						$('#guardar'+pk).prop('disabled', true);
						
						if(cuantosCampos != undefined && cuantosCampos != null ){
							for(i=2; i<=cuantosCampos; ++i){
								$('#guardar'+pk+i).prop('disabled', true);
							}
						}
						
						$('#editar'+pk).css('display', 'inline-block');
						$('#cambiar'+pk).css('display', 'none');
						$('#salvar'+pk).css('display', 'none');
						$('#cancelar'+pk).css('display', 'none');
					} else {
						$('#errors').html(getErrors(errors));
						$('#errors').show();
					}
					jsAjaxStatus.stopAjax();
			},
			error: function error(jqXHR, textStatus, errorThrown) {
				$('#error').html(genericMessage);
				$('#error').show();
	        	jsAjaxStatus.stopAjax();
			}
		});
	}
}

/**
 * Se agrega el campo de cuantosCampos para los casos de parametros que utilizan mas de un componente grafico para ser configurados, como el caso de los jobs
 */
function cancelar(pk, cuantosCampos){	
	jsAjaxStatus.startAjax();
	//Volvemos al estado inicial
	
	if(cuantosCampos != undefined && cuantosCampos != null ){
		$('#guardar'+pk).val($('#ocultar'+pk).val().split(" ")[0]);
		for(i=2; i<=cuantosCampos; ++i){
			$('#guardar'+pk+i).val($('#ocultar'+pk).val().split(" ")[i-1]);
			$('#guardar'+pk+i).prop('disabled', true);
		}
	}
	else{
		$('#guardar'+pk).val($('#ocultar'+pk).val());
		
	}
	
	$('#guardar'+pk).prop('disabled', true);
	$('#editar'+pk).css('display', 'inline-block');
	$('#cambiar'+pk).css('display', 'none');
	$('#salvar'+pk).css('display', 'none');
	$('#cancelar'+pk).css('display', 'none');
	jsAjaxStatus.stopAjax();
}


$(document).ready(function() {
	
	if($("#updateRequestMsm").val() == 7) {
		$("#generalOption").removeClass("ui-tabs-selected ui-state-active");
		$("#updateRequestOption").addClass("ui-tabs-selected ui-state-active");
	    $("#tab1").addClass(" ui-tabs-hide");
	    $("#tab7").removeClass(" ui-tabs-hide");
	    reloadUpdateRequest($("#message").val());
	} 
});

