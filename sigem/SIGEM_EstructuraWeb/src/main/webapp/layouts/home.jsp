<%@ page contentType="text/html;charset=ISO-8859-1" language="java" %>
<%@ taglib uri="/WEB-INF/ieci.tld" prefix="ieci"%>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.Context" %>
<html>
<head>
<ieci:baseInvesDoc/>
<link rel="stylesheet" type="text/css" href="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/include/css/common.css"/>
<link rel="stylesheet" type="text/css" href="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/include/css/estilos.css"/>
</head>
<body>
	<div class="contenedora" align="center">
		<div class="cuerpoEO" style="width:840px;height:494px">
   			<div class="cuerporightEO">
     			<div class="cuerpomidEO"> 				
					<div class="cuadro" style="height: 494px;">


										                    		
           			</div>     				
     			</div>
     		</div>
     	</div>
	</div>
</body>
</html>