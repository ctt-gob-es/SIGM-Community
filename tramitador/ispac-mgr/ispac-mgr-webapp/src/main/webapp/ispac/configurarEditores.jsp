<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>

<script>
	var extension = "";
</script>
<div id="contenido" class="move" >
	<div class="ficha"> 
		<div class="encabezado_ficha">
			<div class="titulo_ficha">
				<h4><label alt = '<bean:message key="configurarEditores.title" />' title = '<bean:message key="configurarEditores.title" />' id="titulo"><bean:message key="configurarEditores.title" /></label></h4>
				<div class="acciones_ficha">

					<a href='#' id="volver" onclick='volver();' class="tdlink" alt = '<bean:message key="configurarEditores.volver" />' title = '<bean:message key="configurarEditores.volver" />' style="display:none"> <img src='<ispac:rewrite href="img/ico_return.gif"/>' class="imgTextBottom" alt = '<bean:message key="configurarEditores.volver" />' title = '<bean:message key="configurarEditores.volver" />'> <bean:message key="configurarEditores.volver" /> </a>

					<a href='#' id="asociarExtension" onclick='asociaExtension();' class="tdlink" alt = '<bean:message key="configurarEditores.asociarExtension" />' title = '<bean:message key="configurarEditores.asociarExtension" />'> <img src='<ispac:rewrite href="img/config.gif"/>' class="imgTextBottom" alt = '<bean:message key="configurarEditores.asociarExtension" />' title = '<bean:message key="configurarEditores.asociarExtension" />'> <bean:message key="configurarEditores.asociarExtension" /> </a>

					<a href="#" id="btnCancel" onclick='javascript:top.ispac_needToConfirm=false;<ispac:hideframe refresh="true"/>' class="btnCancel" alt = '<bean:message key="common.message.close" />' title = '<bean:message key="common.message.close" />'><bean:message key="common.message.close" /></a>
				</div>
			</div>
		</div>
		<div class="cuerpo_ficha">
			<div class="seccion_ficha" id="seccion1">

				<span class="pagebanner" id="totalRegistros">&nbps;</span><span class="pagelinks"></span>

				<table cellspacing="0" cellpadding="5" id="tablaExtensiones" class="tableDisplay">
				</table>
			</div>

			<div class="seccion_ficha" id="seccion2" style=" display: none">
				<table cellspacing="0" cellpadding="5" align="center" width="100%" class="tableDisplay"">
					<thead>
						<tr>
							<th class="sortable sorted order1">
								<label alt = '<bean:message key="seleccionarEditor.openOfficeTitle" />' title = '<bean:message key="seleccionarEditor.openOfficeTitle" />'><bean:message key="seleccionarEditor.openOfficeTitle" /></label>
							</th>
							<!-- <th class="sortable">
								<label alt = '<bean:message key="seleccionarEditor.msOffice" />' title = '<bean:message key="seleccionarEditor.msOffice" />'><bean:message key="seleccionarEditor.msOffice" /></label>
							</th>-->
						</tr>
					</thead>
					<tbody>
						<tr class='odd'>
							<td class="width50percent">
								&nbsp;
								<label alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.openOffice" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.openOffice" />'>
								<img src="<ispac:rewrite href='img/docs/doc_odt.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODT" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODT" />'/>
								<img src="<ispac:rewrite href='img/docs/doc_ods.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODS" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODS" />'/> <bean:message key="seleccionarEditor.openOffice" /> <input id="editorRD" name="editorRD" type="radio" value="sofficeSIGEM: " onclick="seteaValor(this.value);"/></label>
							</td>
							<!-- <td class="width50percent">
								<label alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />'><img src="<ispac:rewrite href='img/docs/doc_doc.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />'/> <bean:message key="seleccionarEditor.word" /><input id="editorRD" name="editorRD" type="radio" value="ms-word:ofe|u|" onclick="seteaValor(this.value);"/></label>
								<br>
							</td>
						</tr>
						<tr class="even">
							<td class="width50percent">&nbsp;</td>
							<td class="width50percent">
								<label alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />'><img src="<ispac:rewrite href='img/docs/doc_xls.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />'/> <bean:message key="seleccionarEditor.excel" /><input id="editorRD" name="editorRD" type="radio" value="ms-excel:ofe|u|" onclick="seteaValor(this.value);"/></label>
							</td>
						</tr>
						<tr class="odd">
							<td class="width50percent">&nbsp;</td>
							<td class="width50percent">
								<label alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />'><img src="<ispac:rewrite href='img/docs/doc_ppt.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />'/> <bean:message key="seleccionarEditor.powerPoint" /><input id="editorRD" name="editorRD" type="radio" value="ms-powerpoint:ofe|u|" onclick="seteaValor(this.value);"/></label>
							</td>
						</tr>
						<tr class="even">
							<td class="width50percent">&nbsp;</td>
							<td class="width50percent">
								<label alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />'><img src="<ispac:rewrite href='img/docs/doc_unknown.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />' title =  '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />'/> <bean:message key="seleccionarEditor.project" /><input id="editorRD" name="editorRD" type="radio" value="ms-project:ofe|u|" onclick="seteaValor(this.value);"/></label>
							</td>
						</tr>
						<tr class="odd">
							<td class="width50percent">&nbsp;</td>
							<td class="width50percent">
								<label alt =  '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />' title =  '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />'><img src="<ispac:rewrite href='img/docs/doc_unknown.gif'/>" class="imgTextBottom" alt =  '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />'/> <bean:message key="seleccionarEditor.access" /><input id="editorRD" name="editorRD" type="radio" value="ms-access:ofe|u|" onclick="seteaValor(this.value);"/></label>								
							</td>-->
						</tr>
					</tbody>										
				</table>
			</div>
		</div>
	</div>
</div>
<script>
	function getImagen(extension){
		var imagen = document.createElement('IMG');
		imagen.src = '<ispac:rewrite href="img/docs/doc_' + extension + '.gif"/>';
		imagen.alt = '<bean:message key="seleccionarEditor.documento" /> ' + extension;
		imagen.title = '<bean:message key="seleccionarEditor.documento" /> ' + extension;
		imagen.className = 'imgTextBottom';

		return imagen;
	}

	function getEditor(celda, editor){
		var etiqueta = document.createElement("Label");		
		var imagen = document.createElement('IMG');		
		imagen.className = 'imgTextBottom';

		if(editor == 'ms-word:ofe|u|'){
			etiqueta.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />';
			etiqueta.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />';
			etiqueta.innerHTML = ' <bean:message key="configurarEditores.word" />';

			imagen.src = '<ispac:rewrite href="img/docs/doc_doc.gif"/>';
			imagen.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />';
			imagen.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />';
		}
		else if(editor == 'ms-excel:ofe|u|'){
			etiqueta.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />';
			etiqueta.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />';
			etiqueta.innerHTML = ' <bean:message key="configurarEditores.excel" />';

			imagen.src = '<ispac:rewrite href="img/docs/doc_xls.gif"/>';
			imagen.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />';
			imagen.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />';
		}
		else if(editor == 'ms-powerpoint:ofe|u|'){
			etiqueta.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />';
			etiqueta.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />';
			etiqueta.innerHTML = ' <bean:message key="configurarEditores.powerPoint" />';

			imagen.src = '<ispac:rewrite href="img/docs/doc_ppt.gif"/>';
			imagen.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />';
			imagen.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />';
		}
		else if(editor == 'ms-project:ofe|u|'){
			etiqueta.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />';
			etiqueta.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />';
			etiqueta.innerHTML = ' <bean:message key="configurarEditores.project" />';

			imagen.src = '<ispac:rewrite href="img/docs/doc_unknown.gif"/>';
			imagen.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />';
			imagen.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />';
		}
		else if(editor == 'ms-access:ofe|u|'){
			etiqueta.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />';
			etiqueta.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />';
			etiqueta.innerHTML = ' <bean:message key="configurarEditores.access" />';

			imagen.src = '<ispac:rewrite href="img/docs/doc_unknown.gif"/>';
			imagen.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />';
			imagen.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />';
		}
		else{
			etiqueta.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.openOffice" />';
			etiqueta.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.openOffice" />';
			etiqueta.innerHTML = ' <bean:message key="configurarEditores.openOffice" />';

			imagen.src = '<ispac:rewrite href="img/docs/doc_odt.gif"/>';
			imagen.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODT" />';
			imagen.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODT" />';
			celda.appendChild(imagen);
		
			var imagen = document.createElement('IMG');		
			imagen.className = 'imgTextBottom';

			imagen.src = '<ispac:rewrite href="img/docs/doc_ods.gif"/>';
			imagen.alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODS" />';
			imagen.title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODS" />';
		}

		celda.appendChild(imagen);
		celda.appendChild(etiqueta);
	}

	function asociaExtension(){		
		extension=prompt('<bean:message key="configurarEditores.mensajes.introduzcaExtension" />','');
		if (String(extension) != 'null'){
			var existe = localStorage.getItem(extension);
			var reemplazar = true;
			if (String(existe) != 'null'){
				reemplazar = confirm('<bean:message key="configurarEditores.mensajes.mensajeConfirmacion" />');
			}
			if (reemplazar){
				document.getElementById("titulo").innerHTML = '<bean:message key="seleccionarEditor.title" /> ' + extension;
				document.getElementById("titulo").alt = '<bean:message key="seleccionarEditor.titleCorto" /> ' + extension;
				document.getElementById("titulo").title = '<bean:message key="seleccionarEditor.titleCorto" /> ' + extension;
				document.getElementById("seccion1").style.display="none";
				document.getElementById("asociarExtension").style.display="none";
				document.getElementById("seccion2").style.display="";
				document.getElementById("volver").style.display="";
			}
		}
	}

	function volver(){
		document.getElementById("seccion1").style.display="";
		document.getElementById("asociarExtension").style.display="";
		document.getElementById("seccion2").style.display="none";
		document.getElementById("volver").style.display="none";

		document.getElementById("titulo").innerHTML = '<bean:message key="configurarEditores.title" />';
		document.getElementById("titulo").alt = '<bean:message key="configurarEditores.title" />';
		document.getElementById("titulo").title = '<bean:message key="configurarEditores.title" />';
	}

	function seteaValor(editor){
		localStorage.setItem(extension, editor);
		alert('<bean:message key="seleccionarEditor.mensajeCorrecto" />');
		document.getElementById("titulo").innerHTML = '<bean:message key="configurarEditores.title" />';
		document.getElementById("titulo").alt = '<bean:message key="configurarEditores.title" />';
		document.getElementById("titulo").title = '<bean:message key="configurarEditores.title" />';
		document.getElementById("seccion1").style.display="";
		document.getElementById("asociarExtension").style.display="";
		document.getElementById("seccion2").style.display="none";
		document.getElementById("volver").style.display="none";

		document.getElementById('totalRegistros').innerHTML = localStorage.length + ' <bean:message key="configurarEditores.registrosEncontrados" />';

		pintarTabla();
	}

	function borrarExtension(extension){
		localStorage.removeItem( extension);
		pintarTabla();
	}

	function pintarTabla(){
					
		var tabla = document.getElementById("tablaExtensiones");
		while(tabla.rows.length > 0) {
			tabla.deleteRow(0);
		}

		var tHead = document.createElement("THEAD");
		tabla.appendChild(tHead);
		var fila = document.createElement("TR");
		tHead.appendChild(fila);
		var celda = document.createElement("TH");
		celda.className = 'sortable sorted order1';
		var etiqueta = document.createElement("Label");
		etiqueta.alt = '<bean:message key="configurarEditores.extension" />';
		etiqueta.title = '<bean:message key="configurarEditores.extension" />';
		etiqueta.innerHTML = '<bean:message key="configurarEditores.extension" />';
		celda.appendChild(etiqueta);
		fila.appendChild(celda);

		var celda = document.createElement("TH");
		celda.className = "sortable";
		var etiqueta = document.createElement("Label");
		etiqueta.alt = '<bean:message key="configurarEditores.editor" />';
		etiqueta.title = '<bean:message key="configurarEditores.editor" />';
		etiqueta.innerHTML = '<bean:message key="configurarEditores.editor" />';
		celda.appendChild(etiqueta);
		fila.appendChild(celda);

		var celda = document.createElement("TH");;
		celda.className = "headerDisplay";
		fila.appendChild(celda);

		var tBody = document.createElement("TBODY");
		tabla.appendChild(tBody);

		for (var i=0; i<localStorage.length; i++){
			var fila =  document.createElement("TR");
			tBody.appendChild(fila);

			if((i%2)==0){
				fila.className='odd';
			}
			else{
				fila.className='even';
			}
			
			var editor = localStorage[localStorage.key(i)];
			var extension = localStorage.key(i);

			var celda = document.createElement("TD");			
			celda.className = 'width35percent';
			var etiqueta = document.createElement("Label");
			etiqueta.alt = localStorage.key(i);
			etiqueta.title = localStorage.key(i);
			etiqueta.innerHTML = ' ' + localStorage.key(i);
			celda.appendChild(getImagen(localStorage.key(i)));
			celda.appendChild(etiqueta);
			fila.appendChild(celda);


			var celda = document.createElement("TD");
			celda.className = 'width35percent';
			getEditor(celda, editor);
			fila.appendChild(celda);
			
			var celda = document.createElement("TD");
			celda.className = 'width30percent';

			var textoEnlace = document.createTextNode ('<bean:message key="configurarEditores.eliminarRelacion" />');

			var enlace = document.createElement('A');
			enlace.href = 'javascript:window.borrarExtension(\'' + extension + '\');';
			enlace.className = "tdlink";
			enlace.alt = '<bean:message key="configurarEditores.eliminarRelacion" />';
			enlace.title = '<bean:message key="configurarEditores.eliminarRelacion" />';
			enlace.appendChild(textoEnlace);

			var img = document.createElement('IMG');
			img.src = '<ispac:rewrite href="img/borrar.gif"/>';
			img.alt = '<bean:message key="configurarEditores.eliminarRelacion" />';
			img.title = '<bean:message key="configurarEditores.eliminarRelacion" />';
			img.className = 'imgTextBottom';
			enlace.appendChild(img);
			celda.appendChild(enlace);
			
			fila.appendChild(celda);
		}

		document.getElementById('totalRegistros').innerHTML = localStorage.length + ' <bean:message key="configurarEditores.registrosEncontrados" />';

		for (i=0;i<document.getElementsByName('editorRD').length;i++){ 
      	if (document.getElementsByName('editorRD')[i].checked) 
         	document.getElementsByName('editorRD')[i].checked = false;
   		} 
	}
</script>

<script>
	document.getElementById('totalRegistros').innerHTML = localStorage.length + ' <bean:message key="configurarEditores.registrosEncontrados" />';
	document.getElementById('totalRegistros').title = localStorage.length + ' <bean:message key="configurarEditores.registrosEncontrados" />';
	document.getElementById('totalRegistros').alt = localStorage.length + ' <bean:message key="configurarEditores.registrosEncontrados" />';
	
	pintarTabla();
</script>

<script>
positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
</script>
