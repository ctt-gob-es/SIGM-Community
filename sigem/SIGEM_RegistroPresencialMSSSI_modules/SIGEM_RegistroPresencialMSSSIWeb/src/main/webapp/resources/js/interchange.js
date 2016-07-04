function decideOpenningAceptar(xhr, status, args){
    if(args.mostrar) {  
    	dialog.show();
    }  else{
    	dlgAceptar.show();
    }
}

function decideOpenningRechazar(xhr, status, args){
    if(args.mostrar) {  
    	dialog.show();
    }  else{
    	dlgRechazar.show();
    }
}

function decideOpenningReenviar(xhr, status, args){
    if(args.mostrar) {  
    	dialog.show();
    }  else{
    	dlgReenviar.show();
    }
}

function handleReenIRRequest(xhr, status, args) {  
	 if(!(args.validationFailed)) {  
        PF('dlgReenviar').hide();  
    }  
} 
function handleRechaIRRequest(xhr, status, args) {  
	 if(!(args.validationFailed)) {  
       PF('dlgRechazar').hide();  
   }  
}   

function decideOpenningRectificar(xhr, status, args){
    if(args.mostrar) {  
    	dialog.show();
    }  else{
    	dlgRectificar.show();
    }
}


function handleRecIRRequest(xhr, status, args) {  
	 if(!(args.validationFailed)) {  
        PF('dlgRectificar').hide();  
    }  
} 