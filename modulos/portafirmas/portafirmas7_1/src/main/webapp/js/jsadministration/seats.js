// Indica si ya se cargaron la sedes
var isLoadSeats = false;

// Prepara el alta de una sede
function prepareNewSeat() {
	$('#seatPk').val('');
	$('#seatCode').val('');
	$('#seatDenom').val('');
	$('#seatName').val('');
	$('#seatLongDuration').prop("checked",false);
	$('#seatURLCSV').val('');
	$('#seatLdap').prop("checked",false);
	$('#seat_modal').dialog('open');
	$('#seatCode').autocomplete({
		source: "administration/seats/autocompleteSeats",		
		// Función que se ejecuta al seleccionar un elemento
		select: function( event, ui ) {
			// Se fuerza a que el componente capture el valor
			// seleccionado por el usuario (teclado o ratón)
			$(this).val(ui.item.label);
			$(this).attr('codigo',ui.item.id);
			$(this).attr('denom',ui.item.denom);
			$(this).attr('codigoDIR3',ui.item.codigoDIR3);
			actualizarDenom()
			return false;
		}
	});
}

function actualizarDenom(){
	
	var varSeatCode = $('#seatCode');
	var varSeatDenom = $('#seatDenom');
	var sDenom = varSeatCode.attr('denom');
	var sCodigoDIR3 = varSeatCode.attr('codigoDIR3');
	// Se actualizan input
	$('#seatDenom').val('');
	$('#seatDenom').val(sDenom);
	$('#seatCode').val('');
	$('#seatCode').val(sCodigoDIR3);
	$('#seatName').val('');
	$('#seatName').val(sDenom);
}

// Prepara la modificación de una sede
function prepareModifySeat(primaryKey) {
	$('#seatPk').val(primaryKey);
	$('#seatCode').val($('#seatCode_' + primaryKey).html());
	$('#seatDenom').val($('#seatDenom_' + primaryKey).html());
	$('#seatName').val($('#seatName_' + primaryKey).html());
	if ($('#isLongDuration_' + primaryKey).val() == 'true') {
		$('#seatLongDuration').prop("checked",true);
	} else {
		$('#seatLongDuration').prop("checked",false);
	}
	$('#seatURLCSV').val($('#seatURLCSV_' + primaryKey).html());
	if( $('#seatLongDuration').is(':checked') ){
		$('#seatURLCSV').prop('disabled', true);
	}else{
		$('#seatURLCSV').prop('disabled', false);
	}
	if ($('#isLdap_' + primaryKey).val() == 'true') {
		$('#seatLdap').prop("checked",true);
	} else {
		$('#seatLdap').prop("checked",false);
	}
	$('#seatCode').autocomplete({
		source: "administration/seats/autocompleteSeats",		
		// Función que se ejecuta al seleccionar un elemento
		select: function( event, ui ) {
			// Se fuerza a que el componente capture el valor
			// seleccionado por el usuario (teclado o ratón)
			$(this).val(ui.item.label);
			$(this).attr('codigo',ui.item.id);
			$(this).attr('denom',ui.item.denom);
			$(this).attr('codigoDIR3',ui.item.codigoDIR3);
			actualizarDenom()
			return false;
		}
	});
	$('#seat_modal').dialog('open');
}

// Para insertar o modificar una sede
function saveSeat(baseUrl, funcionRecargaDeSedes) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: baseUrl+"/saveSeat",
		type: 'post',
		dataType: 'json',
		data: "primaryKey=" + $('#seatPk').val() +
			  "&seatCode=" + $('#seatCode').val() + 
			  "&seatName=" + $('#seatName').val() +
			  "&seatLongDuration=" + $('#seatLongDuration').prop('checked') +
			  "&seatURLCSV=" + $('#seatURLCSV').val() +
			  "&seatLdap=" + $('#seatLdap').prop('checked') +
			  "&seatParent=" + $('#seatParent').val(),
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				funcionRecargaDeSedes();
			} else {
				$('#seatErrorMessage').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#seat_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

// Prepara el borrado de una sede
function prepareDeleteSeat(primaryKey, funcionSi) {
	$('#questionMessageSeat').html('Va a borrar una sede, ¿está usted seguro?');
	if(funcionSi==null || funcionSi == undefined || funcionSi == "null" || funcionSi == "undefined"){
		$('#yesButtonSeat').attr('onclick',"deleteSeat('" + primaryKey + "');");
	}
	else{
		$('#yesButtonSeat').attr('onclick',funcionSi+"('" + primaryKey + "');");
	}
	
	$('#questionSeat').show();
}

// Para borrar una sede
function deleteSeat(primaryKey) {
	$('#questionSeat').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/seats/deleteSeat",
		dataType: 'json',
		data: "primaryKey=" + primaryKey,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadSeats();
			} else {
				showError(getErrors(data));
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

// Para cargar las sedes una única vez
function loadSeats() {
	if(!isLoadSeats) {
		reloadSeats();
	}
}

// Para recargar las sedes
function reloadSeats() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/seats/loadSeats",
		dataType: 'html',
		async: false,
		success: function(model) {
			$("#tab4").html(model);
			configSeatModal("administration/seats", reloadSeats);
        	$('#seat_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
        	$('#seat_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
};

//Se crea el diálogo de la ventana modal de sedes
function configSeatModal(baseUrl, funcionDeRecarga) {
	$("#seat_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    //height:450,
	    width: 500,
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
	  	            	saveSeat(baseUrl, funcionDeRecarga);
	  	            }
	  	        }
	  	    ],
	  	    
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	    open: function(){	
	    },
	    close:function () {
	      	$('#seatErrorMessage').html('');
	    }
	});
	isLoadSeats = true;
}

	
function chequear() {
	if( $('#seatLongDuration').is(':checked') ){
		$('#seatURLCSV').prop('disabled', true);
	}else{
		$('#seatURLCSV').prop('disabled', false);
	}
};
