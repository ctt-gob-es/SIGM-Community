<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
	<link rel="stylesheet" href="css/estilos.css" type="text/css" />
	<script type="text/javascript" language="javascript" src="js/idioma.js"></script>
	
	<script>document.execCommand(ClearAuthenticationCache, false);</script>

	<script language="Javascript">
		function redirigir(){
			//document.forms[0].submit();			
		}		
	</script>	
</head>

<body onload="javascript:redirigir();">
	<!--  [eCenpri-Manu Ticket #125] - INICIO - ALSIGM3 - Modifcar la opción salir para que no lleve a la pantalla del portal de AL-SIGM 
	<p><bean:message key="cargando"/></p>
	<form action="../portal/" method="POST">
	</form>
	-->
	
	<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
		<jsp:include flush="true" page="common/headerSup.jsp"></jsp:include>
	<tr>
		<td height="100%" width="100%" colspan="2" align="center" valign="top">
			<div style="width:721px; padding-top:60px;">
			    <div class="cuerpo">
			      <div class="cuerporight">
			        <div class="cuerpomid">
			          <div class="submenu3">
			          </div>
			          <div class="cuadro">
						<table border="0" cellpadding="0" cellspacing="0" width="707px" height="231px">
							<tr>
							  <td width="100%" height="100%">
							    <table cellpadding="0" cellspacing="0" border="0" align="center" valign="middle">
							      <tr>
							        <td style="width:50%">
							        </td>
							        <td>
							        	<html:errors/>
							        	<!-- [eCenpri-Manu Ticket #125] - INICIO - ALSIGM3 - Modifcar la opción salir para que no lleve a la pantalla del portal de AL-SIGM  -->
						        		<bean:message key="message.cerrarVentana"/>
							        	<!-- [eCenpri-Manu Ticket #125] - FIN - ALSIGM3 - Modifcar la opción salir para que no lleve a la pantalla del portal de AL-SIGM  -->
							        </td>
							      </tr>
							    </table>
							  </td>
							</tr>
						</table>
			          </div>
			        </div>
			      </div>
			    </div>
			    <div class="cuerpobt">
			      <div class="cuerporightbt">
			        <div class="cuerpomidbt"></div>
			      </div>
			    </div>
			</div>
		</td>
	</tr>
</table>
 	 
</body>
</html>
