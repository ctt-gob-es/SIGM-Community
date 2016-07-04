$(document).keypress(function(e) {
	if (e.which == 13) {
		document.getElementById("form:btnbuscar").click();
		return false;
	}
});
function start() {
	PF('statusDialog').show();
}

function stop() {
	document.getElementById("form:btnReset").click();
	PF('statusDialog').hide();
}

function isGenerateRecibo() {
	if (PF('tableBooks').getSelectedRowsCount() == 0 || PF('tableBooks').getSelectedRowsCount() > 1) {
		PF('dialogRecibo').show();
		return false;
	} else {
		return true;
		
	}
}
function stopOnly() {
	PF('statusDialog').hide();
}

function startExportExcel() {
	PF('statusDialogExportExcel').show();
}

function stopExportExcel() {
	document.getElementById("form:btnReset").click();
	PF('statusDialogExportExcel').hide();
}
function stopOnlyExportExcel() {
	PF('statusDialogExportExcel').hide();
}


function startExportPDF() {
	PF('statusDialogExportPDF').show();
}

function stopExportPDF() {
	document.getElementById("form:btnReset").click();
	PF('statusDialogExportPDF').hide();
}
function stopOnlyExportPDF() {
	PF('statusDialogExportPDF').hide();
}

function isGenerateExport() {
	if (PF('tableBooks').paginator.cfg.rowCount > 2000) {
		PF('dialogExport').show();
		return false;
	} else {
		return true;
		
	}
}

function mostrarfld1(){
	if ($("#form\\:fld1Select_input option:selected").val() != '..'){
		$("#form\\:fld1ValueHasta").hide();
		$("#form\\:fld1ValueHasta").val('');
	}else {
		$("#form\\:fld1ValueHasta").show();
	}
}