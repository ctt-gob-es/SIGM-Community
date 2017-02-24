$(document).keypress(function(e) {
			    if(e.which == 13) {
			    	if (document.getElementById( "accordion:outputRegisterDataForm:btnenviarM" ) != null){
			    		document.getElementById( "accordion:outputRegisterDataForm:btnenviarM" ).click();
			    	}else{
			    		document.getElementById( "accordion:outputRegisterDataForm:btnenviarN" ).click();
			    	}
			        return false;
			    }
			});
function start() {
	PF('statusDialog').show();
}
function stop() {
	PF('statusDialog').hide();
}
function startRec() {
	PF('statusDialogRec').show();
}
function stopRec() {
	PF('statusDialogRec').hide();
	linkRefresh();
	docRefresh();
	histRefresh();
}
function startLabel() {
	PF('statusDialogLabel').show();
}
function stopLabel() {
	PF('statusDialogLabel').hide();
	linkRefresh();
	docRefresh();
	histRefresh();
}

function startNew() {
	PF('statusDialogNew').show();
}
function stopNew() {
	PF('statusDialogNew').hide();
}
