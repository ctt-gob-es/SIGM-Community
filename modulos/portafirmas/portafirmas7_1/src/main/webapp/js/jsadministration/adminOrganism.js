
// Prepara el alta de una sede
function prepareNewSeat() {
	$('#seatPk').val('');
	$('#seatCode').val('');
	$('#seatName').val('');
	$('#seat_modal').dialog('open');
}

//Para borrar una sede siendo administrador de organismo
function deleteSedeOrganismo(primaryKey) {
	$('#questionOrganism').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "adminOrganism/deleteSeat",
		dataType: 'json',
		data: "primaryKey=" + primaryKey,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadSedesAdministradorOrganismo();
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

function reloadSedesAdministradorOrganismo() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "adminOrganism/loadOrganism",
		dataType: 'html',
		async: false,
		success: function(model) {
			$("#contenidoDeSedes").html(model);
			configSedeOrganismModal("administration/seats", reloadSedesAdministradorOrganismo);
        	$('#seat_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			jsAjaxStatus.stopAjax();
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
        	$('#seat_modal').dialog("close");
		}
	});
};

//Prepara el borrado de una sede
function prepareDeleteSeat(primaryKey, funcionSi) {
	$('#questionMessageSeat').html('Va a borrar una sede, ¿está usted seguro?');
	if(funcionSi==null || funcionSi == undefined || funcionSi == "null" || funcionSi == "undefined"){
		$('#yesButtonSeat').attr('onclick',"deleteSedeOrganismo('" + primaryKey + "');");
	}
	else{
		$('#yesButtonSeat').attr('onclick',funcionSi+"('" + primaryKey + "');");
	}
	
	$('#questionSeat').show();
}

//Prepara la modificación de una sede
function prepareModifySeat(primaryKey) {
	$('#seatPk').val(primaryKey);
	$('#seatCode').val($('#seatCode_' + primaryKey).html());
	$('#seatName').val($('#seatName_' + primaryKey).html());
	$('#seat_modal').dialog('open');
}

//Para insertar o modificar una sede
function saveSeat(baseUrl, funcionRecargaDeOrganismo) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "adminOrganism/saveSeat",
		type: 'post',
		dataType: 'json',
		data: "primaryKey=" + $('#seatPk').val() +
			  "&seatCode=" + $('#seatCode').val() + 
			  "&seatName=" + $('#seatName').val() +
			  "&seatParent=" + $('#seatParent').val(),
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				funcionRecargaDeOrganismo();
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

//Se crea el diálogo de la ventana modal de organismos
function configSedeOrganismModal(baseUrl, funcionDeRecarga) {
	$("#seat_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    //height:450,
	    width: 500,
	    modal: true,       
	    buttons: [{
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
}
