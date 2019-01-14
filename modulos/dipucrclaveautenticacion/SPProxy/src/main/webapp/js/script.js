/******************menu principal activados************/
function menuActivado(ider,menu,clase1,clase2){
// definimos la variable con la que trabajaremos nuestro contenedor del menu
idmenu = document.getElementById(menu);

// Aqui comprobamos con document.all para IE y else para gecko
// enseguida extraemos todas las TAGS div ( en este caso ) que esten dentro de nuestro contenedor
if (document.all) {
ded = idmenu.all.tags("div");
} else {
ded = idmenu.getElementsByTagName('div');
}

// contamos el numero de tags div dentro del contenedor y las tratamos una por una
for (i=0;i<=ded.length;i++) {

// si el tag que estemos tratando en ese momento es igual al que envio la peticion se activa la clase especial
if(ded[i] == ider){ ded[i].className=clase1; }

// en el else se trataran TODOs los elementos del menu, menos el que envio la peticion, se debolvera a la clase base
else{
// lo regresamos a la clase 2 ( de inicio )
ded[i].className=clase2;
}
}
}
// libre de " ver " cualquier error de pagina... �� molesto !
function detenerError(){return true} window.onerror=detenerError;

function showUrl2(){	
	 document.forms[1].pepsUrl2.value = document.forms[1].speps2[document.forms[1].speps2.selectedIndex].value; 
}


function changeUrl(index,toPV) {
	if(toPV) {
		document.forms[index].pepsUrl.value = document.forms[index].pepsUrl.value.replace('ServiceProvider','SPPowerValidation');
	} else {
		document.forms[index].pepsUrl.value = document.forms[index].pepsUrl.value.replace('SPPowerValidation', 'ServiceProvider');
	}
}