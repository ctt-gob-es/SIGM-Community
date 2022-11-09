/*******************************************************************************
 * Ruta al directorio de los instalables. * Si no se establece, supone que estan
 * en el mismo directorio(que el HTML). * Para indicar un directorio en local se
 * debe usar el prefijo "file://", por * ejemplo "file://C:/Instalables". Se
 * debe usar siempre el separador "/" * (nunca "\"). *
 ******************************************************************************/
var baseDownloadURL =  location.protocol + '//' + location.host + baseDownloadURL;

/*******************************************************************************
 * Ruta directorio del instalador. * Si no se establece, supone que est?n en el
 * mismo directorio(que el HTML). * Para indicar un directorio en local se debe
 * usar el prefijo "file://", por * ejemplo "file://C:/Instalador". Se debe usar
 * siempre el separador "/" * (nunca "\"). *
 ******************************************************************************/
var base = baseDownloadURL;

/*******************************************************************************
 * Algoritmo de firma. Puede ser 'SHA1withRSA', 'MD5withRSA' o, salvo que sea *
 * firma XML, MD2withRSA. Se estable al llamar a configuraFirma en firma.js *
 ******************************************************************************/
var signatureAlgorithm = 'SHA1withRSA'; // Valor por defecto

/*******************************************************************************
 * Formato de firma. Puede ser 'CMS', 'XADES', 'XMLDSIGN' o 'NONE'. * Se estable
 * al llamar a configuraFirma en firma.js * Por defecto: CMS. *
 ******************************************************************************/
var signatureFormat = 'CMS'; // Valor por defecto

/*******************************************************************************
 * Mostrar los errores al usuario. Puede ser 'true' o 'false'. * Se estable al
 * llamar a configuraFirma en firma.js * Por defecto: false. *
 ******************************************************************************/
var showErrors = 'false'; // Valor por defecto

/*******************************************************************************
 * Filtro de certificados (expresi?n que determina qu? certificados se le *
 * permite elegir al usuario). Ver la documentaci?n. * Se estable al llamar a
 * configuraFirma en firma.js * * Ejemplos: * - S?lo mostrar certificados de
 * DNIe de firma: * var certFilter = '{ISSUER.DN#MATCHES#{"CN=AC DNIE
 * 00(1|2|3),OU=DNIE,'+ * 'O=DIRECCION GENERAL DE LA
 * POLICIA,C=ES"}&&{SUBJECT.DN#MATCHES#'+ * '{".*(FIRMA).*"}}}'; * * - S?lo
 * mostrar certificados de la FNMT: * var certFilter = '{ISSUER.DN={"OU = FNMT
 * Clase 2 CA,O= FNMT,C = ES"}}'; * * - Mostrar todos los certificados menos el
 * de validaci?n: * var certFilter =
 * '{SUBJECT.DN#NOT_MATCHES#{".*(AUTENTICACI?N).*"}}}' *
 ******************************************************************************/
var certFilter; // Valor por defecto

/*******************************************************************************
 * Directorio donde se instalar� las librer�as del cliente * Por defecto:
 * USER_HOME/afirma.5/ *
 ******************************************************************************/
var installDirectory = "afirma.3.0.2";

/*******************************************************************************
 * Acci�n establecida a realizar cuando durante la instalacion se detecten *
 * versiones antiguas del cliente (v2.4 y anteriores). * Opciones disponibles: * -
 * 1: Preguntar al usuario. * - 2: No eliminar. * - 3: Eliminar sin preguntar. *
 * Por defecto: 1 (Preguntar al usuario). *
 ******************************************************************************/
var oldVersionsAction = 1;

/*******************************************************************************
 * Mostrar los certificados caducados en la listas de seleccion de *
 * certificados *
 ******************************************************************************/
var showExpiratedCertificates = 'true';

/*******************************************************************************
 * Construccion del cliente que se instalara cuando no se indique explitamente.*
 * Los valores aceptados son: * - 'LITE': Incluye los formatos de firma CMS y
 * CADES. * - 'MEDIA': Incluye los formatos de firma de la LITE + XMLDSIG y
 * XADES. * - 'COMPLETA': Incluye los formatos de firma de la MEDIA + PDF. * Por
 * defecto: 'LITE'. *
 ******************************************************************************/
var defaultBuild = 'COMPLETA';

/*******************************************************************************
 * DEPRECADO. CADA DISTRIBUCION DEBE TENER SU DIRECTORIO DE INSTALACION PROPIO *
 ******************************************************************************/
// var distinctDistroDir;


/*******************************************************************************
 * time.js
 ******************************************************************************/

/**
 * Funciones de tiempo.
 * 
 * whenTry(condicion, comando[, msgErr]) -> Ejecuta el comando cuando se cumpla
 * la condici?n. Si no se cumple transcurrido un tiempo, y se ha especificado
 * msgErr, se muestra un alert con msgErr.
 * 
 * waitFor(condicion, millis) -> Espera un tiempo m?ximo a que se cumpla la
 * condici?n
 */

function whenTry(condicion, comando, msgErr, intento)
{
  var whenTry;
  try
  {
    whenTry = eval(condicion);
  }
  catch(e)
  {
    whenTry = false;
  }

  if(whenTry)
  {
    eval(comando);
  }
  else
  {
    if(intento == undefined)
    {
      intento = 1;
    }
    else 
    {
      if(intento > 100)
      {
        if(msgErr != undefined)
        {
          alert(msgErr);
        }
        //return false;
      }
      else
      {
        intento = intento + 1;
      }
    }
    
    if(msgErr == undefined)
    {
      setTimeout("whenTry(\""+condicion+"\", \""+comando+"\", undefined, "+intento+")", 100);
    }
    else
    {
      setTimeout("whenTry(\""+condicion+"\", \""+comando+"\", \""+msgErr+"\", "+intento+")", 100);
    }
  }
}

function waitFor(_condition, _millis)
{
  var _aux  = new Date().getTime();
  
  var _dif = new Date().getTime() - _aux;
  while( !eval(_condition) && (_dif < _millis) )
  {
    _dif = new Date().getTime() - _aux;
  }
}



/*******************************************************************************
 * time.js
 ******************************************************************************/

/**
 * Depende de deployJava.js, de time.js y de constantes.js [opcional].
 * 
 * Si se ha definido baseDownloadURL se usa como URL base para la descarga de
 * los instalables.
 * 
 * cargarAppletFirma(build): Carga el applet de firma en la variable
 * "clienteFirma". El applet no esta cargado hasta que clienteFirmaCargado=
 * true. La carga del applet verificara que se tiene instalada una construccion
 * igual o superior a la indicada, si no lo esta se carga el instalador e inicia
 * el proceso de instalacion el applet en local.
 * 
 * cargarAppletInstalador(): Carga el applet de carga (BootLoader) en la
 * variable "instalador".
 * 
 * instalar(build): Carga el applet instalador (si no se ha cargado), instala la
 * construccion del cliente en local y devuelve el directorio de instalacion
 * (null si no).
 * 
 * actualizar(baseDownloadUrl): Carga el applet instalador (si no se ha
 * cargado), actualiza la version instalada en local y devuelve true si el
 * proceso finaliza correctamente (false si no).
 * 
 * desinstalar(): Carga el applet instalador (si no se ha cargado), desinstala
 * el cliente de firma de local y devuelve true si el proceso finaliza
 * correctamente (false si no).
 * 
 * isInstalado(build): Carga el applet instalador (si no se ha cargado) y
 * comprueba que este instalada la construccion indicada o una superior. Si no
 * se indica ninguna se comprueba que este instalada la construccion basica del
 * cliente. Devuelve true si la comprobacion fue positiva y false en caso
 * contrario.
 * 
 * isActualizado(): Carga el applet instalador (si no se ha cargado) y devuelve
 * true si el cliente de firma esta actualizado (coinciden las versiones
 * local/remota) en local (false si no).
 * 
 * getDirectorioInstalacion(): Carga el applet instalador (si no se ha cargado)
 * y devuelve la ruta de instalacion del cliente de firma en local.
 * 
 * getVersion(): Carga el applet instalador (si no se ha cargado) y devuelve la
 * version del instalador.
 * 
 * getVersionCliente(): Carga el applet instalador (si no se ha cargado),
 * comprueba que el cliente este instalado y si lo esta devuelve la version del
 * cliente.
 * 
 */ 

var instalador;
var instaladorCargado = false;
var clienteFirma;

function cargarAppletFirma(build)
{
  // Comprobamos que el instalador esta cargado
  if(instalador == undefined)
  {
    cargarAppletInstalador();
  }
  
  // Comprobamos que el applet de firma esta instalado
  if( instaladorCargado == false )
  {
    whenTry("instaladorCargado == true", "cargarAppletFirma(build)", "No se ha podido cargar el applet instalador");
    return;
  }

  // Comprobamos que el applet de firma esta instalado
  if( isInstalado(build) == false )
  {
    instalar(build);

    // Si fallo o se cancelo la instalacion, informamos de ello
    if( isInstalado(build) == false )
    {
      alert("No se ha finalizado la instalaci\u00F3n del cliente \u0040firma por lo que no se podr\u00E1n realizar operaciones de firma electr\u00F3nica.");
      return;
    }
  }
    
  // Cargamos el applet de firma
  if(clienteFirma == undefined)
  {
    /* Definici�n de las constantes necesarias */

    var installationDirectory = instalador.getInstallationDirectory();
    var jarArchive = 'file:///' + installationDirectory + '/afirma5_coreV3.jar';

    var attributes = {
      id:'firmaApplet',
      code:'es.gob.afirma.cliente.SignApplet.class',
      archive:jarArchive,
      width:1,
      height:1
    };
    var parameters = {
      userAgent:window.navigator.userAgent,
      appName:window.navigator.appName,
      installDirectory:installDirectory,
      showExpiratedCertificates:showExpiratedCertificates,
      java_arguments:'-Djnlp.packEnabled=true -Xmx512M'
    };
    var version = '1.5';

    deployJava.runApplet(attributes, parameters, version);

    clienteFirma = document.getElementById("firmaApplet");

    /* Realizamos una espera para que de tiempo a cargarse el applet */
    for(var i=0; i<100; i++) {
      try {
        setTimeout("clienteFirma != undefined && clienteFirma.isInitialized()", 100);
        break;
      } catch(e) {
      // Capturamos la excepcion que se produciria si no se hubiese cargado
      // aun el applet, aunque no se lanzaria
      // una vez estuviese cargado aunque no iniciado
      }
    }
  }
}

function cargarAppletInstalador()
{

  if(instalador == undefined)
  {
    /* Definicion de las constantes necesarias */
    var codeBaseVar = '.';
    if(base != undefined) {
      codeBaseVar = base;
    }

    var attributes = {
      id:'instaladorApplet',
      code:'es.gob.afirma.install.AfirmaBootLoader.class',
      archive:codeBaseVar+'/afirmaBootLoader.jar',
      width:1,
      height:1
    };
    var parameters = {
      userAgent:window.navigator.userAgent,
      appName:window.navigator.appName,
      installDirectory:installDirectory,
      oldVersionsAction:oldVersionsAction
    };
    var version = '1.5';

    deployJava.runApplet(attributes, parameters, version);

    instalador = document.getElementById("instaladorApplet");
    
    /* Realizamos una espera para que de tiempo a cargarse el applet */
    whenTry("instalador.isIniciado() == true", "instaladorCargado = true");

    for(var i=0; i<100; i++) {
      try {
        setTimeout("instalador != undefined && instalador.isIniciado()", 100);
        break;
      } catch(e) {
      // Capturamos la excepcion que se produciria si no se hubiese cargado
      // aun el applet, aunque no se lanzaria
      // una vez estuviese cargado aunque no iniciado
      }
    }
  }
}


function instalar(build)
{
  if(instalador == undefined)
  {
    cargarAppletInstalador();
  }
  
  if(isInstalado(build)) {
    alert("El cliente de @firma 5 v3 ya est\u00E1 instalado");
    return true;
  }

  // Si hay definida una URL desde la que descargar los instalables, la
  // establecemos
  if(baseDownloadURL != undefined) {
    instalador.setBaseDownloadURL(baseDownloadURL);
  }

  // Si no se ha indicado una construccion por parametro ni hay establecida una
  // por defecto en "constantes.js", se instala la 'LITE'
  var confBuild = null;
  if(build == null || build == undefined)
  {
    if(defaultBuild == null || defaultBuild == undefined)
    {
      return instalador.instalar();
    } else {
      confBuild = defaultBuild;
    }
  } else {
    confBuild = build;
  }

  // Si se ha indicado una construccion de alguna manera (por defecto o por
  // parametro), se instala esa

  return instalador.instalar(confBuild);
}

function actualizar(baseDownloadURL)
{
  if(instalador == undefined)
  {
    cargarAppletInstalador();
  }
  
  // Si se ha establecido una URL base, la establecemos para tomar de ahi los
  // paquetes.
  if(baseDownloadURL != undefined)
  {
    instalador.setBaseDownloadURL(baseDownloadURL);
  }

  return instalador.actualizar();
}

function desinstalar()
{
  if(instalador == undefined)
  {
    cargarAppletInstalador();
  }
  return instalador.desinstalar();
}

function isInstalado(build)
{
  if(instalador == undefined)
  {
    cargarAppletInstalador();
  }
  
  // Si no se ha indicado una construccion por parametro ni hay establecida una
  // por defecto en "constantes.js", se comprueba la 'LITE'
  var confBuild = null;
  if(build == null || build == undefined)
  {
    if(defaultBuild == null || defaultBuild == undefined)
    {
      return instalador.isInstalado();
    } else {
      confBuild = defaultBuild;
    }
  } else {
    confBuild = build;
  }

  // Si se ha indicado una construccion de alguna manera (por defecto o por
  // parametro), se comprueba esa

  return instalador.isInstalado(confBuild);
}

function isActualizado()
{
  if(instalador == undefined)
  {
    cargarAppletInstalador();
  }
  
  return instalador.isActualizado();
}

function getDirectorioInstalacion()
{
  if(instalador == undefined)
  {
    cargarAppletInstalador();
  }
  
  return instalador.getInstallationDirectory();
}

function getVersion()
{
  if(instalador == undefined)
  {
    cargarAppletInstalador();
  }
  
  return instalador.getVersion();
}

function getVersionCliente()
{
  if(instalador == undefined)
  {
    cargarAppletInstalador();
  }
  
  return instalador.getClientVersion();
}

/*******************************************************************************
 * firma.js
 ******************************************************************************/

/*
 * Depende de instalador.js y de constantes.js (opcional)
 * 
 * initialize(): Vuelve el applet a su estado inicial
 * 
 * getEstructuraNodos(): Devuelve una cadena que contiene los nombres de los
 * firmantes de cada firma, co-firma y contra-firma. Los nombres van separados
 * por '\n' y empiezan por tantos '\t' como nivel ocupe el nodo en el arbol. P.
 * ej, para la siguiente estructura de nodos: +---> A | +---> C | +---> D +--->
 * B +---> E El documento est? co-firmado por A, B y E, y la co-firma de A, est?
 * contra-firmada por C y D. La cadena que devolver?a getEstructuraNodos() es la
 * siguiente: "A\n\tC\n\tD\nB".
 * 
 * firmar(), coFirmar(), contraFirmarNodos([cadenaDeIndices]),
 * contraFirmarArbol(), contraFirmarHojas(),
 * contraFirmarFirmantes([cadenaDeFirmantes]): Inician los respectivos procesos
 * de firma -> cadenaDeIndices es una cadena de enteros separados por '\n' que
 * indican qu? nodos contraFirmar. Los indices(0, 1, ...) est?n referidos al
 * resultado de getEstructuraNodos(). Por ejemplo, para firmar los nodos 0 y 4
 * la cadena ser?a '0\n4' -> cadenaDeFirmantes es una cadena de nombres de
 * firmantes separados por '\n' que indican qu? firmantes contrafirmar. Los
 * nombres de los posibles firmantes se obtienen de getEstructuraNodos().
 * 
 * getCertificatesAlias(), getCertificates(): Recuperan los alias de los
 * certificados del keystore activo y los propios certificados codificados en
 * Base 64, respectivamente. Los elementos son devueltos en forma de array.
 */


/**
 * Establece los valores de firma. Ver constantes.js.
 */ 
function configuraFirma()
{
  var command = "";

  if( certFilter != undefined )
    command += "clienteFirma.setCertFilter('"+certFilter+"');";

  if( signatureAlgorithm != undefined )
  {
    command += "clienteFirma.setSignatureAlgorithm('"+signatureAlgorithm+"');";
  }
  if( signatureFormat != undefined )
  {
    command += "clienteFirma.setSignatureFormat('"+signatureFormat+"');";
  }

  eval(command);
}

/**
 * Prepara el cliente para iniciar un proceso de firma.
 */
function initialize()
{
  clienteFirma.initialize();
  clienteFirma.setShowErrors(showErrors=='true');
}

function firmar()
{
  clienteFirma.sign();
}

function coFirmar()
{
  clienteFirma.coSign();
}

function contraFirmarNodos(cadenaDeIndices)
{
  var command = "clienteFirma.counterSignIndexes()";
  if(cadenaDeIndices != undefined)
  {
    command = "clienteFirma.setSignersToCounterSign('"+cadenaDeIndices+"'); " + command;
  }
  
  eval(command);
}

function contraFirmarArbol()
{
  clienteFirma.counterSignTree();
}

function contraFirmarHojas()
{
  clienteFirma.counterSignLeafs();
}

function contraFirmarFirmantes(cadenaDeFirmantes)
{
  var command = "clienteFirma.counterSignSigners()";
  if(cadenaDeFirmantes != undefined)
  {
    command = "clienteFirma.setSignersToCounterSign('"+cadenaDeFirmantes+"'); " + command;
  }
  
  eval(command);
}

function getEstructuraNodos()
{
  return clienteFirma.getSignersStructure();
}

/*
 * Devuelve un array con los alias de los certificados del keystore activo.
 */
function getCertificatesAlias()
{

  /*
   * Ya que en Firefox 2 no funciona la funcion split() de cadenas en
   * JavaScript, hacemos un proceso distinto de la cadena para obtener el mismo
   * resultado que esta funcion.
   */
  if(isFireFox2()) {
    return dividir(clienteFirma.getCertificatesAlias(), clienteFirma.STRING_SEPARATOR);
  } else {
    return clienteFirma.getCertificatesAlias().split(clienteFirma.STRING_SEPARATOR);
  }
}

/*
 * Devuelve un array con los certificados del keystore activo codificados en
 * Base 64.
 */
function getCertificates()
{
  return dividir(clienteFirma.getCertificates(), clienteFirma.STRING_SEPARATOR);
}

/*
 * Comprueba que el navegador Web sea Firefox 2.
 */
function isFireFox2()
{
  if(window.navigator.appName == 'Netscape') {
    var userAgent = window.navigator.userAgent;
    
    // posici�n de la cadena que coge la parte de la versi�n de Firefox
    posfinal = userAgent.lastIndexOf('/') + 2; 
    // posicion de la cadena que devuelve si realmente es Firefox
    posinicial=  userAgent.lastIndexOf('/') - 7;
    navigator = userAgent.substring(posinicial, posfinal);
  
    if(navigator== 'Firefox/2') {
      return true;
    }
  }
  return false;
}

/*
 * Divide una cadena en un array de cadenas en donde el criterio de separacion
 * era un delimitador.
 */
function dividir(text, delimitator)
{
  var nDel = 0;
  var tempPos = 0;
  var tempPos2 = 0;

  /* Contamos el numero de cadenas que debemos extraer de la principal. */
  while((tempPos = text.indexOf(delimitator, tempPos)) != -1) {
    nDel++;
    tempPos += delimitator.length();
  }
  
  /* Creamos el array en donde almacenaremos las cadenas. */
  var substrings = new Array(nDel+1);

  /* Recorremos la cadena principal extrayendo las subcadenas. */
  tempPos = 0;
  for(var i=0; i < nDel; i++) {
    tempPos2 = text.indexOf(delimitator, tempPos);
    substrings[i] = text.substring(tempPos, tempPos2);
    tempPos = tempPos2 + delimitator.length();
  }
  substrings[nDel] = text.substring(tempPos);

  return substrings;
}

/*******************************************************************************
 * utils.js
 ******************************************************************************/

function isBlank(x)
{
  return (x==undefined || x=='' || x==null);
}

function isEmpty(x)
{
  return isBlank(x) || x.length<1;
}

function toHex(n, digits)
{
  if(!digits)
  {
    digits = 2;
  }
  var hex = new Number(n).toString(16);
  while(hex.length < digits)
  {
    hex = "0" + hex;
  }
  
  return hex;
}

function containsElement(array, elem)
{
  var containsElement = false;

  if(!isEmpty(array))
  {
    var i;
    for(i=0; i<array.length && !containsElement; i++)
    {
      containsElement= (array[i] == elem);
    }
  }
  
  return containsElement;
}

function escaparExpReg(expresion) 
{
  var escaped= "";
  expresion = "" + expresion;
  
  var i, pos=0;
  for(i=0; i<expresion.length; i++)
  {
    if( isSpecialCharacter(expresion.charAt(i)) )
    {
      escaped += expresion.substring(pos, i);
      escaped += "\\" + expresion.charAt(i);
      pos = i+1;
    }
  }
  escaped += expresion.substring(pos, expresion.length);

  return escaped;
}

function isSpecialCharacter(c)
{
  switch (c)
  {
    case '.':
    case '*':
    case '+':
    case '?':
    case '"':
    case '^':
    case '$':
    case '|':
    case '(':
    case ')':
    case '[':
    case ']':
    case '{':
    case '}':
    case '\\':
      return true;
  }
  return false;
}


/*******************************************************************************
 * ACCEDA
 ******************************************************************************/

function empty (mixed_var) {

  // * example 1: empty(null);
  // * returns 1: true // * example 2: empty(undefined);
  // * returns 2: true
  // * example 3: empty([]);
  // * returns 3: true
  // * example 4: empty({}); // * returns 4: true
  // * example 5: empty({'aFunc' : function () { alert('humpty'); } });
  // * returns 5: false
  
  var key;    
  if (mixed_var === "" ||
    mixed_var === 0 ||
    mixed_var === "0" ||
    mixed_var === null ||        mixed_var === false ||
    typeof mixed_var === 'undefined'
    ){
    return true;
  } 
  if (typeof mixed_var == 'object') {
    for (key in mixed_var) {
      return false;
    }
    return true;
  }

  return false;
}


function encode64(input) {
  var output = "";
  var chr1, chr2, chr3;
  var enc1, enc2, enc3, enc4;
  var i = 0;

  var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

  do {
    chr1 = input.charCodeAt(i++);
    chr2 = input.charCodeAt(i++);
    chr3 = input.charCodeAt(i++);

    enc1 = chr1 >> 2;
    enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
    enc4 = chr3 & 63;

    if (isNaN(chr2)) {
      enc3 = enc4 = 64;
    } else if (isNaN(chr3)) {
      enc4 = 64;
    }

    output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2) +
    keyStr.charAt(enc3) + keyStr.charAt(enc4);
  } while (i < input.length);
  
  return output;
}   


/*
 * llama al proceso de firma
 */
function firmaSolicitud( solicitud, formulario, divError ){

  if(empty(clienteFirma)) {
    return false;
  }
  
  // firmamos el documento
  divError.html('');                
  var error = '';             
  try{

    var firma;        
    // Preparamos el cliente para firmar
    clienteFirma.initialize();
    clienteFirma.setShowErrors(false);        
    clienteFirma.setSignatureFormat('CMS');
    clienteFirma.setSignatureMode ("EXPLICIT");       
    clienteFirma.setData( solicitud );

    // Firmamos
    clienteFirma.sign();
    
    // Recogemos el resultado (o el mensaje de error)
    if(!clienteFirma.isError()){          
      firma = clienteFirma.getSignatureBase64Encoded();      
      //throw 'solicitud'+solicitud+' firma'+firma;
      formulario.append('<input type="hidden" name="solicitud" value="'+solicitud+'" />');
      formulario.append('<input type="hidden" name="firma" value="'+firma+'" />');        
    }else{
      throw clienteFirma.getErrorMessage();
    }

                        
  }catch(e){    
      
    // Ocurrio un error al firmar
    if (e.description != null) {
      error=e.description;  
    }else{
      error=e;
    }           
    divError.html('Error: '+error).show();       
    return false;         
  }             
  return true;
}

var cliente_firma_inicializado = false;
function inicializaClienteFirma() {
	if(!cliente_firma_inicializado) {
	clienteFirma.initialize();
	cliente_firma_inicializado = true;
	}
}

function registro_telematico(formularioweb,popup,sn,prefix){

  $('#message_wait').html( '<img src="/images/icons/loading-icon-2.gif" /> Enviando datos...' );
  $('#message_wait').show();
  $('#submit').hide();
  
  var solicitud = $('#'+prefix+'solicitud').val();
  inicializaClienteFirma();
  clienteFirma.setData( solicitud );
  clienteFirma.setShowErrors(false);  
  clienteFirma.setSignatureFormat("CMS");
  clienteFirma.setSignatureMode ("EXPLICIT");
  //clienteFirma.setShowHashMessage(false);
  
  if(sn) clienteFirma.setMandatoryCertificateCondition('{SUBJECT.SN={"'+sn+'"}}');

  clienteFirma.sign();
            
  if (!clienteFirma.isError()) {
    var firma_solicitud = clienteFirma.getSignatureBase64Encoded();    
    $('#'+prefix+'firma_solicitud').val( firma_solicitud );
  }
  else{
    if (typeof(clienteFirma) != 'undefined') {
      $('#message_wait').html('El applet de firma devolvio el siguiente mensaje de error:' + clienteFirma.getErrorMessage());     
    }else{
      $('#message_wait').html('');
    }
    $('#submit').show();
    return false;
  }

  inicializaClienteFirma();
  clienteFirma.setShowErrors(false);
  clienteFirma.setSignatureFormat("CMS");
  //clienteFirma.setShowHashMessage(false);    
  if(sn) clienteFirma.setMandatoryCertificateCondition('{SUBJECT.SN={"'+sn+'"}}');
  
  if (popup) {    
    formularioweb = formularioweb.replace (/[€]/g, "EUR");
    formularioweb = formularioweb.replace(/[^A-Za-z0-9 áéíóúÁÉÍÓÚäëïöüÄËÏÖÜ\'\¡\ª\!\"\·\$\%\&\/\(\)\=\?\¿\ñ\Ñ\<\,\.\-\>\;\:\_\\\|\@\#\~\½\¬\{\[\]\}\\\~]/g, "");
    clienteFirma.webSign(formularioweb);
  }else{
    clienteFirma.setData(encode64(formularioweb));
    clienteFirma.sign();
  }

  if (!clienteFirma.isError()) {
    var firma_formularioweb = clienteFirma.getSignatureBase64Encoded();
    $('#'+prefix+'firma_formularioweb').val( firma_formularioweb );
    $('#'+prefix+'formularioweb').val( encode64(formularioweb) );
    return true;
  }else{
    
    if(typeof(clienteFirma) != 'undefined')
      $('#message_wait').html('El applet de firma devolvio el siguiente mensaje de error:' + clienteFirma.getErrorMessage()  );
    else{
      $('#message_wait').html('');
    }
      
    $('#submit').show();
    return false;
  }
             
  return false;
}


function comprobarAppletCargado(button){
  
  //button.hide();
  //button.after('<div id="wait_afirma">Espere un momento, por favor. Se estan cargando los componentes de firma...</div>');
  
  if(!empty(clienteFirma)){   
    if (clienteFirma.isInitialized () == true) {      
      $('#wait_afirma').remove();
      //button.show();
      return true;
    } 
  }
  
  setTimeout("comprobarAppletCargado()",1000);
  return false;
}




var browser=(function x(){})[-5]=='x'?'FF3':(function x(){})[-6]=='x'?'FF2':/a/[-1]=='a'?'FF':'\v'=='v'?'IE':/a/.__proto__=='//'?'Saf':/s/.test(/a/.toString)?'Chr':/^function \(/.test([].sort)?'Op':'Unknown';
var jres=deployJava.getJREs();
var errorEnviroment = false;
var errorEnviromentTxt = '';
var ffversion='';
if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)){ //test for Firefox/x.x or Firefox x.x (ignoring remaining digits);
 ffversion=new Number(RegExp.$1) // capture x.x portion and store as a number
}


try{
  // Comprobamos la version del navegador
  if (browser!='FF2' && browser!='IE' && browser!='FF' && browser!='FF3' && ffversion!='3.6' ){
    throw '<br/><br/><b>La versión del navegador que esta usando no es compatible con los componentes de @firma. <br/>Solo puede firmar con Firefox e Internet Explorer 6 o 7.<\/b><\/span><br/><span style="color:red">Versión del navegador detectada: '+browser+' - '+navigator.userAgent+'<br/><br/>';
  }
  
  // Comprobamos que tiene java instalado
  var jres = deployJava.getJREs();

  if(empty(jres)){
    var MsgNoJava='No esta instalado Java. El componente para realizar la firma necesita instalar el plugin de Java en su navegador.';
    var instalarJRE = confirm(MsgNoJava+' Pulsa aceptar si desea ir a la página de descarga de Sun.');
    if(instalarJRE == true){
      deployJava.installLatestJRE();
    }else{
      throw MsgNoJava;
    }
  }
}catch(e){
  // Ocurrio un error
  $('document').ready(function(){
    errorEnviroment=true;
    errorEnviromentTxt =  (empty(e.description)) ? e : e.description;
    $('#submit').hide();
    $("#errorAfirma").html(errorEnviromentTxt).show();
  })
}     


if(!errorEnviroment){
  try{
    // Inicia el Applet
    cargarAppletInstalador();
    cargarAppletFirma();
    comprobarAppletCargado( $('#submit') );
  }catch(e){
    
    // Ocurrio un error        
    errorEnviroment=true
    errorEnviromentTxt =  (empty(e.description)) ? e : e.description;
    $('#submit').hide();    
    $('#submit').after('<div id="errorAfirma">'+errorEnviromentTxt+'</div>');
  }
}


