<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>

<%@page contentType="text/html;charset=ISO-8859-1"%>
<html>

	<head>
	<script type="text/javascript" src='<ispac:rewrite href="../../dwr/engine.js"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../../dwr/util.js"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-1.3.2.min.js"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-ui-1.7.2.custom.min.js"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery.alerts.js"/>'></script>
	
	
<link rel="shortcut icon" href='<ispac:rewrite href="img/favicon.ico"/>' type="image/x-icon"/>
<link rel="icon" href='<ispac:rewrite href="img/favicon.ico"/>' type="image/x-icon"/>
<link rel="stylesheet" href='<ispac:rewrite href="css/menus.css"/>'/>
<link rel="stylesheet" href='<ispac:rewrite href="css/nicetabs.css"/>'/>
<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>'/>
<link rel="stylesheet" href='<ispac:rewrite href="css/tablist.css"/>'/>
<link rel="stylesheet" href='<ispac:rewrite href="css/newstyles.css"/>'/>
<link rel="stylesheet" href='<ispac:rewrite href="css/diseniadorGrafico.css"/>'/>

	<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab.css"/>'/>

	<!--[if lte IE 5]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab_ie5.css"/>'/>
	<![endif]-->	

	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab_ie6.css"/>'/>
	<![endif]-->	

	<!--[if gte IE 7]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab_ie7.css"/>'/>
	<![endif]-->	


<!-- ARBOL DE PROCEDIMIENTOS -->
<script type="text/javascript" src='<ispac:rewrite href="../scripts/mktree.js"/>'></script>
<link rel="stylesheet" href='<ispac:rewrite href="css/mktree.css"/>'/>
<!-- FIN ARBOL DE PROCEDIMIENTOS -->

<!-- BARRA DE MENU -->
<link rel="stylesheet" href='<ispac:rewrite href="css/menuDropdown.css"/>'/>
<script type="text/javascript" src='<ispac:rewrite href="../scripts/menuDropdown.js"/>'></script>
<!-- FIN BARRA DE MENU -->

<script type="text/javascript" src='<ispac:rewrite href="../scripts/menus.js"/>'></script>
<script type="text/javascript" src='<ispac:rewrite href="../scripts/sorttable.js"/>'> </script>
<script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js"/>'> </script>
<script type="text/javascript" src='<ispac:rewrite href="../scripts/newutilities.js"/>'> </script>
<script type="text/javascript" src='<ispac:rewrite href="../scripts/ispac-forms.js"/>'> </script>

<!-- AJAX -->
<script type="text/javascript" src='<ispac:rewrite href="../scripts/prototype.js"/>'></script>
<script type="text/javascript" src='<ispac:rewrite href="../scripts/scriptaculous/scriptaculous.js"/>'></script>
<!--
<script type="text/javascript" src='<ispac:rewrite href="../scripts/rico.js"/>'></script>
-->

<!-- Diseñador Gráfico -->
<script type="text/javascript" src='<ispac:rewrite href="../scripts/designer/eventutil.js"/>'></script>
<script type="text/javascript" src='<ispac:rewrite href="../scripts/designer/libreria.js"/>'></script>
<script type="text/javascript" src='<ispac:rewrite href="../scripts/designer/menuflotante.js"/>'></script>

<%-- 		<tiles:insert page="components/head.jsp" ignore="true" flush="false"/> --%>
			
		
		<script>
		//<!--
			window.name="ParentWindow";
		//-->
		</script>
  	</head>

  	<body id="docbody" ><!-- onload="javascript:bodyOnLoad()">-->
  
	  	<!-- Javascripts para generar tooltips, obligatorio despues del body -->
		<tiles:insert page="components/tooltip.jsp" ignore="true" flush="false"/>
		  
		<ispac:keepalive />
	  
		<div id="windowtile">
		
		   	<div id="headertile">
				<tiles:insert attribute="header" ignore="true"/>
			</div>
			<div id="externalbodytile">
				<tiles:insert page="../common/tiles/AppErrorsTile.jsp" ignore="true" flush="false"/>
				<div id="bodytile" style="visibility:visible;">
					<tiles:insert attribute="body" ignore="true" flush="false"/>
				</div>
			</div>
			
			<ispac:layer/>
			<ispac:rewrite id="waitPage" page="wait.jsp"/>
		</div>
	</div>

    <ispac:layer id="waitInProgress" msgKey="msg.layer.downloadDocument" showCloseLink="true" styleClassMsg="messageShowLayer" />
    <ispac:layer id="waitOperationInProgress" msgKey="msg.layer.operationInProgress" styleClassMsg="messageShowLayer" />

    <ispac:layer/>
    <ispac:frame/>
    <ispac:layer id="configLayer"/>
    <ispac:frame id="configFrame"/>
	 <%--ojo no mover el ispac:message ha de ser hijo directo del body para lanzar en jalert al recargar sino IE peta por un bug del propio 
  	navegador--%>
	<ispac:message location="top"/>

	</body>
  
</html>

