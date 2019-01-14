function showMessage(xhr, status, args) {
    if(args.mostrar) {  
    	dialog.show();
    }  
} 

function handleIntRequest(xhr, status, args) {  
    if(!(args.validationFailed)) {  
        PF('intDialog').hide();  
    }  
}  


function handleTercerosModificarRequest(xhr, status, args) {  
    if(!(args.validationFailed)) {  
    	PF('int_modificar_tercero').hide();  
    }  
} 

function handleTercerosModificarRepresentanteRequest(xhr, status, args) {  
    if(!(args.validationFailed)) {  
    	PF('intDlgModificarTerceroRepresentante').hide();  
    }  
}
