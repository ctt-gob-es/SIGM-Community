<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.File" %>

<c:set var="documentTypeId" value="${requestScope.documentTypeId}"/>

<html>
  <head>
    <link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>'/>
    <link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>'/>
		<!--[if lte IE 5]>
			<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie5.css"/>'/>
		<![endif]-->

		<!--[if IE 6]>
			<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie6.css"/>'>
		<![endif]-->

		<!--[if gte IE 7]>
			<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie7.css"/>'>
		<![endif]-->

	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-1.3.2.min.js"/>'></script>
  	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-ui-1.7.2.custom.min.js"/>'></script>
    <script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js"/>'> </script>
    <ispac:javascriptLanguage/>
    <script type="text/javascript" src='<ispac:rewrite href="../scripts/time.js"/>'> </script>
    <script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery.alerts.js"/>'></script>
    <style>
		#upload {
			padding: 10px 10px 10px 10px;
			text-align: center;
			visibility: hidden;
		}

		#uploadlist {
			border-top: 1px solid blue;
			text-align: left;
		}

		#uploadlist img {
			border: none;
		  	vertical-align: middle;
		}

		.columnTitle {
			background-color: #639ACE;
			color: white;
		}
    </style>

    <script type="text/javascript">

		var files = new Array();

		function init() {
			showFiles();
		}

		function openDirectory() {
			showFiles();
		}

		function showFiles() {		
			clearTbody();
			
			<%
			List<File> listaDocumentos = (ArrayList<File>) request.getAttribute("listaDocumentos");			
			for (int i = 0; i<listaDocumentos.size(); i++){%>

				var name = '<%=listaDocumentos.get(i).getName()%>';
				var path = '<%=listaDocumentos.get(i).getAbsolutePath().replace('\\', '/')%>';
				var lastModified = '<%=listaDocumentos.get(i).lastModified()%>';
				var length = '<%=listaDocumentos.get(i).length()%>';
				
				var doc = [name, path, lastModified, length];

				files[<%=i%>] = doc;
			<%}%>

			for (i = 0; i < files.length; i++)  {				
				addTbodyCell(files[i], i);
			}
			document.getElementById("upload").style.visibility = "visible";

			document.getElementById("deletingFiles").checked = ispacdocapplet.isDeleteFiles();
		}

		function clearTbody() {
			var tbody = document.getElementById("uploadlistbody");
			while (tbody.childNodes.length > 0) {
		  		tbody.removeChild(tbody.firstChild);
			}
		}

		function addTbodyCell(file, i) {

		  	var theTableBody = document.getElementById("uploadlistbody");
	  		var newRow = theTableBody.insertRow(theTableBody.rows.length);
		   	if (i%2!=0) {
	   			newRow.style.background='#EEE';
	   		}

			var fileName = file[0];
			var filePath = file[1];
			var fileDate = file[2];
		   	var size = file[3];

		   	var unit = 'B';

			if(size>1024) {
		 		size=size/1024;
				unit='KB';
			}
			if(size>1024) {
				size=size/1024;
				unit='MB';
			}
			if(size>1024) {
				size=size/1024;
				unit='GB';
			}

			size = Math.round(size*100)/100;

		   	newCell = newRow.insertCell(0);
		   	newCell.align='center';
		   	newCell.innerHTML = "<input type='checkbox' name='multibox' value='" + i + "'/>";
		   	newCell = newRow.insertCell(1);
		   	newCell.align='left';

		   	newCell.innerHTML = "<a target='_blank' href='showDocumentRepoComun.do?documentType=<c:out value="${documentTypeId}"/>&fileName=" + fileName + "'>" + fileName + "</a>";		   	

		   	newCell = newRow.insertCell(2);
		   	newCell.align = 'right';
		  	newCell.innerHTML = "<nobr>" + size + " " + unit + "<nobr>";
		   	newCell = newRow.insertCell(3);
		   	newCell.align='left';
		   	newCell.innerHTML = "<nobr>" + formatDate(fileDate) + "</nobr>";
		}

		function formatDate(time) {
			var date = new Date(parseInt(time));
			return leftPad(date.getDate(), 2) + "/" + leftPad((date.getMonth() + 1), 2) + "/" + date.getFullYear() + " " + leftPad(date.getHours(), 2) + ":" + leftPad(date.getMinutes(), 2) + ":" + leftPad(date.getSeconds(), 2);
		}

		function doUpload() {

			document.body.scrollTop = 0;

			var ids = checkboxElement(document.form1.multibox);
			if (ids == "") {
				jAlert('<bean:message key="upload.from.repository.error.noFiles"/>', '<bean:message key="common.alert"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>');
			} else if ((files != null) && (files.length > 0)) {
				showLayer("wait");
				var fileNames = new Array();
				var tokens = ids.split("-");

				for (var i=0; i < tokens.length; i++) {
					var file = files[tokens[i]];
					fileNames[i] = file[0];
				}				
			   	
				window.location.href = "subirDocumentosRepoComun.do?documentType=<c:out value="${documentTypeId}"/>&fileNames=" + fileNames.join(',');
			}
		}
		
		function volver(){
			window.location.href ="selectDocumentType.do?action=uploadRepoComunFiles.do";
		}

   	</script>
  </head>

  <body onload="javascript:init()">

  <div id="contenido" class="move">
		<div class="ficha">
			<div class="encabezado_ficha">
				<div class="titulo_ficha">
					<h4><nobr><bean:message key="upload.title"/></nobr></h4>
					<div class="acciones_ficha">

<%-- 							<input type="button" value='<bean:message key="upload.from.repository.button.changeDir"/>' class="btnDir" onclick="javascript:openDirectory()"/> --%>
							<a href='#' id="volver" onclick='volver();' class="tdlink" alt = '<bean:message key="configurarEditores.volver" />' title = '<bean:message key="configurarEditores.volver" />'> <img src='<ispac:rewrite href="img/ico_return.gif"/>' class="imgTextBottom" alt = '<bean:message key="configurarEditores.volver" />' title = '<bean:message key="configurarEditores.volver" />'> <bean:message key="configurarEditores.volver" /> </a>

							<input type="button" value='<bean:message key="common.message.cancel"/>' class="btnCancel" onclick='javascript:top.ispac_needToConfirm=false;<ispac:hideframe refresh="true"/>'/>

					</div>
				</div>
			</div><!-- Fin encabezado ficha -->

			<div class="cuerpo_ficha">
				<div class="seccion_ficha">
				<table border="0" cellspacing="2" cellpadding="2" width="95%">
					<tr>
						<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
					</tr>
					<tr>
						<td  class="formsTitle">
							<bean:message key="upload.from.repository.directory.caption"/>
						</td>
					</tr>
					<tr>
						<td >
							<span id="imgDir"></span>
						</td>
					</tr>
					<tr>
						<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
					</tr>
					<tr id="upload">
						<td >
							<form name="form1" method="post">
							  	<table id="uploadlist" border="0" cellpadding="2" cellspacing="2" >
							   		<thead>
							    		<tr class="columnTitle">
											<td width="20px">&nbsp;</td>
								    		<td align="70%">&nbsp;<b><bean:message key="upload.from.repository.table.column.name"/></b></td>
								    		<td width="10%" align="left">&nbsp;<b><bean:message key="upload.from.repository.table.column.fileSize"/></b></td>
											<td width="10%" align="left">&nbsp;<b><bean:message key="upload.from.repository.table.column.date"/></b></td>
								    	</tr>
								   </thead>
								   <tbody id="uploadlistbody">
								   </tbody>
								   <tfooter>
								   		<tr>
								   			<td align="right" colspan="4">
												<hr style="color: grey;">
								    		</td>
								   		</tr>
								   		<tr>
								    		<td align="right" colspan="4">
								    			<table width="100%" border="0" cellpadding="0" cellspacing="0">
								    				<tr>
<!-- 								    					<td> -->
<!-- 										    				<input type="checkbox" name="deletingFiles" id="deletingFiles"/> -->
<!-- 										    				Eliminar ficheros añadidos -->
<!-- 								    					</td> -->
								    					<td align="right">
															<a target="_blank" href="javascript:doUpload();"
																class="rename"><bean:message key="upload.from.repository.confirm.upload"/>
																&nbsp;<img src='<ispac:rewrite href="img/addFile.gif"/>' border="0" /></a>
								    					</td>
								    				</tr>
								    			</table>
								    		</td>
								   		</tr>
								   	</tfooter>
								</table>
							</form>
						</td>
					</tr>
				</table>
			</div>
		</div>
  	</div>
  	<ispac:layer id="wait" msgKey="msg.layer.operationInProgress" styleClassMsg="messageShowLayer" />
  </div>



  </body>

</html>

<script>
positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
</script>