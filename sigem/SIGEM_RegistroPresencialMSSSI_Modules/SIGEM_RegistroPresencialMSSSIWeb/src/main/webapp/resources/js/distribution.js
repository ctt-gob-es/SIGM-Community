function decideOpenningRech(xhr, status, args){
    if(args.mostrar) {  
    	dialog.show();
    }  else{
    	dlgRechazar.show();
    }
}

function decideOpenningCambio(xhr, status, args){
    if(args.mostrar) {  
    	dialog.show();
    }  else{
    	dlgCambiar.show();
    }
}
function decideOpenningRedir(xhr, status, args){
    if(args.mostrar) {  
    	dialog.show();
    }  else{
    	dlgRedistribuir.show();
    }
}
function decideOpenningDis(xhr, status, args){
    if(args.mostrar) {  
    	dialog.show();
    }  else{
    	dlgDistribuir.show();
    }
}

function handleDisRequest(xhr, status, args) {
    if(!(args.validationFailed)) {  
        PF('dlgDistribuir').hide();  
    }  
}

function handleRechazarRequest(xhr, status, args) {
    if(!(args.validationFailed)) {  
        PF('dlgRechazar').hide();  
    }  
}

function handleReDisRequest(xhr, status, args) {
    if(!(args.validationFailed)) {  
        PF('dlgRedistribuir').hide();  
    }  
}

function handleCamDesRequest(xhr, status, args) {
    if(!(args.validationFailed)) {  
        PF('dlgCambiar').hide();  
    }  
}

function decideOpenningMoverReg(xhr, status, args){
    if(args.mostrar) {  
    	dialog.show();
    }  else{
    	dlgMoverReg.show();
    }
}

function handleMovRequest(xhr, status, args) {
    if(!(args.validationFailed)) {  
        PF('dlgMoverReg').hide();  
    }  
}

function decideOpenningAccept(xhr, status, args){
    if(args.mostrar) {  
    	dialog.show();
    }  else{
    	dlgAceptar.show();
    }
}

function handleAcceptRequest(xhr, status, args) {
	stopAccept();
    if(!(args.validationFailed)) {
        PF('dlgAceptar').hide();  
    }  
}

function startAccept() {
	PF('statusDialogAccept').show();
}
function stopAccept() {
	PF('statusDialogAccept').hide();
}
