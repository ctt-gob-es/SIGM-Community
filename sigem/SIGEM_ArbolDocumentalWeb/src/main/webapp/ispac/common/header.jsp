<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<script type="text/javascript"> 
//<!--

	//	Confirma la salida de la aplicación
	function exit() {
		jConfirm('<bean:message key="buscador.exit.msg"/>', '<bean:message key="common.confirm"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>',function(resultado) {
			if(resultado){
				window.location.href = '<c:url value="/exit.do"/>';
			}
		});
	}
	        
//-->
</script>

	<link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>' type="text/css" />

	<div id="cabecera">
   		<div id="logo">
   			<img src="<%=request.getContextPath()%>/resourceServlet/logos/logo.gif" alt="sigem" />
   		</div>
		<div class="salir">
			<img src='<ispac:rewrite href="img/exit.gif"/>' alt="<bean:message key="menu.salir"/>" width="26" height="20" class="icono" />
			<span class="titular">
				<a href='<c:url value="/exit.do"/>'><bean:message key="menu.salir"/></a>
			</span>
		</div>
	</div>
	<div id="usuario">
		<div id="barra_usuario">
			
			<p class="usuario">
		            <bean:message key="aplicacion"/>
			</p>
		</div>
	</div>
