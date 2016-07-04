$(document)
		.keypress(
				function(e) {
					if (e.which == 13) {
						if (document
								.getElementById("accordion:inputRegisterDataForm:btnEnviarM") != null) {
							document
									.getElementById(
											"accordion:inputRegisterDataForm:btnEnviarM")
									.click();
						} else {
							document
									.getElementById(
											"accordion:inputRegisterDataForm:btnEnviarN")
									.click();
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
