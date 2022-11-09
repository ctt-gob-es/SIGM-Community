// httpContextPathPortafirmas variable a cargar con thymeleaf en html
var jsAjaxStatus = function () {
	var ajaxStatus; //Variable con el objeto $("#ajaxStatus"). No se puede asignar hasta que se cree el cuadro de diálogo
	
	var _startAjax = function() {
		ajaxStatus.dialog("open");
	}
	var _stopAjax = function() {
		ajaxStatus.dialog("close");
	}
	
	/*********************************************************************************************************************
	 * 													INICIALIZACIÓN
	 *********************************************************************************************************************/
	
	//Función que crea el diálogo AjaxStatus
	$(function() {
		var hostname = window.location.origin;
		ajaxStatus = $("#ajaxStatus");
		ajaxStatus.append('<img src="' + httpContextPathPortafirmas + '/images/morfos/loading.gif" class="textcenter"></img>')
		ajaxStatus.dialog({
			height: 90,
			width: 90,
			autoOpen: false,
			modal: true,
			draggable: false, 
			resizable: false,
			dialogClass: 'ajaxStatus'	
		});
		
	});
	
	//Asociamos la cortinilla al evento submit
//	$(document).ready(function(){
//	    $('#formularioPrincipal').bind('submit', function(event) {
//	    	_startAjax();
//	    });
//	});
	
	/*
	$("#ajaxStatus").bind("ajaxSend", function(){
	 	startAjax();
	 });
	$("#ajaxStatus").bind("ajaxComplete", function(){
		 stopAjax();
	 });
	*/
	
	/*********************************************************************************************************************
	 * 													INTERFAZ
	 *********************************************************************************************************************/
	
	return {
		startAjax : _startAjax,
		stopAjax : _stopAjax
	};
	
} ();