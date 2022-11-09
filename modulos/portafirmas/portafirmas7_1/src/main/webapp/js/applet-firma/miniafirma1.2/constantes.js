/*******************************************************************************
 * Ruta al directorio de los instalables. * Si no se establece, supone que estan
 * en el mismo directorio(que el HTML). * Para indicar un directorio en local se
 * debe usar el prefijo "file://", por * ejemplo "file://C:/Instalables". Se
 * debe usar siempre el separador "/" * (nunca "\"). *
 ******************************************************************************/
var scripts = document.getElementsByTagName('script');
var index = scripts.length - 1;
var baseDownloadURL  = scripts[index].src.substring(0,scripts[index].src.lastIndexOf('\/')+1);
if(baseDownloadURL.substring(0,4)!='http'){
	baseDownloadURL=location.protocol + '//' + location.host + baseDownloadURL;	
}

/*******************************************************************************
 * Ruta directorio del instalador. * Si no se establece, supone que est?n en el
 * mismo directorio(que el HTML). * Para indicar un directorio en local se debe
 * usar el prefijo "file://", por * ejemplo "file://C:/Instalador". Se debe usar
 * siempre el separador "/" * (nunca "\"). *
 ******************************************************************************/
var base = baseDownloadURL;