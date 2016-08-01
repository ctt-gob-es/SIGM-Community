<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html">
		<title>Página de búsqueda</title>
		<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>'/>
		 <link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>'/>
  
    <script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js"/>'> </script>
    
    <!--[if lte IE 5]>
 		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie5.css"/>'/>

	<![endif]-->	

	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie6.css"/>'>

	<![endif]-->	

	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie7.css"/>'>
	
	<![endif]-->
	
	 <script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-1.3.2.min.js"/>'></script>
 	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-ui-1.7.2.custom.min.js"/>'></script>
 	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery.alerts.js"/>'></script>
		<script type="text/javascript" src='<ispac:rewrite href="../scripts/sorttable.js"/>'></script>
		
		<%
			String strProcPadre = request.getParameter("procPadre");
			String strProcHijo = request.getParameter("procHijo");
			String strRuleName = request.getParameter("rule");
			String nIdTask = request.getParameter("taskId");
			String numexp = request.getParameter("numexp");
			String entity = request.getParameter("entity");
		%>
		
		<ispac:rewrite id="setValue" action="CreateSearchExpedientAssociate.do"/>
		
		<script language='JavaScript' type='text/javascript'><!--

			function SelectValue(value) {

				document.body.scrollTop = 0;
				showLayer("waitOperationInProgress");
				hideLayer("contenido");
			
				document.selectvalue.action = '<%=setValue%>' + "?method=enter&procedimiento="+value;
				document.selectvalue.submit();
			}
			function cancelForm() {
				document.selectvalue.action = "showTask.do?taskId="+nIdTask+"&numexp="+numexp+"&entity="+entity;
		   		document.selectvalue.submit();
		   	}			
			
		//--></script>
		
	</head>
	
	<body>
	<div id="contenido" class="move" >
	<div class="ficha">
		<div class="encabezado_ficha">
			<div class="titulo_ficha">
				<h4><bean:message key="select.value.title"/></h4>
				<div class="acciones_ficha">
					<a href="#" id="btnCancel" onclick='<ispac:hideframe/>' class="btnCancel">
							<bean:message key="common.message.close" />
					</a>	
				</div><%--fin acciones ficha --%>
			</div><%--fin titulo ficha --%>
		</div><%--fin encabezado ficha --%>

		<div class="cuerpo_ficha">
			<div class="seccion_ficha">
			<form name="selectvalue" method="post">
											<input type="hidden" name="procPadre" value='<%=strProcPadre%>'/>
											<input type="hidden" name="procHijo" value='<%=strProcHijo%>'/>
											<input type="hidden" name="rule" value='<%=strRuleName%>'/>
											<input type="hidden" name="taskId" value='<%=nIdTask%>'/>
											</form>
											
							        		<!-- displayTag con formateador -->
											<bean:define name="Formatter" id="formatter" type="ieci.tdw.ispac.ispaclib.bean.BeanFormatter"/>
							        		
     										<display:table  name="ValueList"
															id="value"
															export='<%=formatter.getExport()%>'
															class='<%=formatter.getStyleClass()%>'
															sort='<%=formatter.getSort()%>'
															pagesize='<%=formatter.getPageSize()%>'
															defaultorder='<%=formatter.getDefaultOrder()%>'
															defaultsort='<%=formatter.getDefaultSort()%>'
															requestURI=''>
												
												<logic:iterate name="Formatter" id="format" type="ieci.tdw.ispac.ispaclib.bean.BeanPropertyFmt">
																   		
													<!-- ENLACE -->
													<logic:equal name="format" property="fieldType" value="LINK">
													
													  	<display:column titleKey='<%=format.getTitleKey()%>'
													  					media='<%=format.getMedia()%>'
													  					headerClass='<%=format.getHeaderClass()%>'
													  					sortable='<%=format.getSortable()%>'
													  					sortProperty='<%=format.getPropertyName()%>'
													  					class='<%=format.getColumnClass()%>'
													  					decorator='<%=format.getDecorator()%>'
													  					comparator='<%=format.getComparator()%>'>
															  				
												  					<a class="element" href="javascript:SelectValue('<%= format.formatProperty(value)%>')">
									              					<%=format.formatProperty(value)%>
												            		</a>
												            													              	
											                
													  	</display:column>
													  	
										   			</logic:equal>
													   		
										   			<!-- DATO DE LA LISTA -->
										   			<logic:equal name="format" property="fieldType" value="LIST">
										   			
														<display:column titleKey='<%=format.getTitleKey()%>'
																	    media='<%=format.getMedia()%>'
													  					headerClass='<%=format.getHeaderClass()%>'
													  					sortable='<%=format.getSortable()%>'
													  					sortProperty='<%=format.getPropertyName()%>'
													  					class='<%=format.getColumnClass()%>'
													  					decorator='<%=format.getDecorator()%>'
													  					comparator='<%=format.getComparator()%>'>
													  					
															<%=format.formatProperty(value)%>
															
														</display:column>
														
										   			</logic:equal>
										   			
												</logic:iterate>
												
											</display:table>
			</div>
		</div>
	</div>
</div>

<div id="waitOperationInProgress" style="display:none;z-index:1000;background:white;filter:alpha(opacity=80);-moz-opacity:.80;opacity:.80;"/><div id="contenido" class="messageShowLayer" ><div class="ficha"><div class="encabezado_ficha"><div class="titulo_ficha"><div class="acciones_ficha"></div></div></div><div class="cuerpo_ficha"><div class="seccion_ficha"><p><label class="popUp"><nobr>Operación en progreso, espere por favor...</nobr></label></p></div></div></div></div></div>

	
	</body>
	
</html>

<script>

positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
</script>