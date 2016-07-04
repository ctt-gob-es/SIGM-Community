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
  