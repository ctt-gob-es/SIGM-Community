<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="ieci.tecdoc.sgm.core.user.web.ConstantesSesionUser"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%

	request.getSession().removeAttribute(ConstantesSesionUser.ID_ENTIDAD);
	request.getSession().removeAttribute(ConstantesSesionUser.ID_SESION);
	request.getSession().removeAttribute(ConstantesSesionUser.IDIOMA);
	request.getSession().removeAttribute(ConstantesSesionUser.LANG);
	request.getSession().removeAttribute(ConstantesSesionUser.TRAMITE_ID);
	
	request.getParameterMap().remove(ConstantesSesionUser.IDIOMA);
	request.getParameterMap().remove(ConstantesSesionUser.ID_ENTIDAD);
	request.getParameterMap().remove(ConstantesSesionUser.ID_SESION);			
%>

<html>
<head>
  <title><bean:message key="head.title"/></title>
  <link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>'/>
  <link rel="shortcut icon" href='<ispac:rewrite href="img/favicon.ico"/>' type="image/x-icon"/>
  <link rel="icon" href='<ispac:rewrite href="img/favicon.ico"/>' type="image/x-icon"/>

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

  <script>
		window.name="ParentWindow";
  </script>
</head>
<body leftMargin="0" rightMargin="0" topMargin="0" marginheight="0" marginwidth="0">
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<jsp:include page="common/header.jsp" />
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
							        	<bean:message key="message.cerrarVentana"/>							        		
										</form>
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