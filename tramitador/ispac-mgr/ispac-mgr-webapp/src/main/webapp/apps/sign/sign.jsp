<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:set var="_listaDocs" value="${signForm.listaDocs}"/>
<jsp:useBean id="_listaDocs" type="java.lang.String"/>
<c:set var="_codEntidad" value="${signForm.codEntidad}"/>
<jsp:useBean id="_codEntidad" type="java.lang.String"/>
<c:set var="_serialNumber" value="${signForm.serialNumber}"/>
<jsp:useBean id="_serialNumber" type="java.lang.String"/>
<c:set var="_documentId" value="${signForm.documentId}"/>
<jsp:useBean id="_documentId" type="java.lang.String"/>


<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@page import="ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants"%>

<html>
	<head>
				<meta http-equiv="Content-Type" content="text/html">
				<title></title>
				<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>'/>
				<link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>'/>
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
				<script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js"/>'></script>
				
				<script type="text/javascript" language="javascript" src="ispac/applets/afirma/common-js/deployJava.js"></script>
				<script type="text/javascript" language="javascript" src="ispac/applets/afirma/common-js/miniapplet.js"></script>
				<script type="text/javascript" language="javascript" src="ispac/applets/afirma/common-js/firma3f.js"></script>					
	</head>
	<body>
		
				<script type="text/javascript">					 
						var secs = 0;
						var timerID = null;
						var timerRunning = false;
						var delay = 1000;
						 
						function Delay()
						{
						    // seteo el tiempo del delay				    	    
						    secs = 1;
						    StopTheClock();
						    StartTheTimer();
						}
						 
						function StopTheClock()
						{
						    if(timerRunning)
						        clearTimeout(timerID);
						    timerRunning = false;
						}
						 
						function StartTheTimer()
						{
						    if (secs==0)
						    {
						        StopTheClock();
						        document.getElementById('etiqueta_applet').style.display='none';
						        document.getElementById('btn_firma').style.display=''; 
						        //alert(clienteFirma)
						        //alert("Grr.")
						    }
						    else
						    {
						        self.status = secs;
						        secs = secs - 1;
						        timerRunning = true;
						        //clienteFirma.echo;
						        //if(clienteFirma!=null)	
						        //		 secs = 0;   
						        timerID = self.setTimeout("StartTheTimer()", delay);
						    }
						}			 
				</script>  
				
				<%@ page import = "es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration"%>
				<%@ page import = "ieci.tdw.ispac.ispaclib.session.OrganizationUser"%>
				<%@ page import = "ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo"%>
				<% 
				   OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
				   String entityId = info.getOrganizationId();
				   FirmaConfiguration fc = FirmaConfiguration.getInstanceNoSingleton(entityId);
				   String host = fc.getProperty("firmar.ip.https");
				%> 
					
				<script type="text/javascript">
								cargarMiniApplet3f("<%=host %>");
				</script> 
			
				<html:form action="/signDocument3Fases">
				<div id="contenido" class="move">
					<div class="ficha">
						<div class="encabezado_ficha">
							<div class="titulo_ficha">
								<div class="acciones_ficha">
									<a href="#" id="btnCancel" onclick='<ispac:hideframe/>' class="btnCancel">
										<bean:message key="common.message.close" />
									</a>
								</div>
							</div>
						</div>
			
						<div class="cuerpo_ficha">
							<div class="seccion_ficha">
								<logic:messagesPresent>
									<div class="infoError">
										<bean:message key="forms.errors.messagesPresent" />
									</div>
								</logic:messagesPresent>
			
								
								<html:hidden property="signCertificate" styleId="signCertificate" />
								<html:hidden property="signs" styleId="sign"/>
									
								<html:hidden property="method" value="sign"/>
									
								<html:hidden property="listaDocs" styleId="listaDocs" value='<%=_listaDocs%>'/>
								<html:hidden property="documentId" styleId="documentId" value='<%=_documentId%>'/>
								<html:hidden property="codEntidad" styleId="codEntidad" value='<%=_codEntidad%>'/>
								<html:hidden property="serialNumber" styleId="serialNumber" value='<%=_serialNumber%>'/>								
								
									
								<div id='etiqueta_applet' style='display:none;text-align:center;color:#0033FF;font-size:13px'>
										<label>Cargando programa de firma...</label>
										<br/><br/>
										<img src="apps/sign/ajax-loader.gif" alt='En proceso...'/>
								</div>								
								
								<div id='etiqueta' style='display:none;text-align:center;color:#0033FF;font-size:13px'>
										<label>Realizando proceso firma...</label>
										<br/><br/>
										<img src="apps/sign/ajax-loader.gif" alt='En proceso...'/>
								</div>	
								
								<div id='btn_firma' style='display:none;' class="firma">
									 	<a href="#" id="btnSubmit"
												styleId="submit"
												class="form_button_white"
												onclick="javascript:this.style.visibility='hidden';
													document.getElementById('etiqueta').style.display='';
													document.getElementById('btnCancel').style.display='none';
													setTimeout('firmar3f()', 100);">
												<bean:message key="forms.button.sign"/>
										</a>					
								</div>
							</div>
				   		</div>
					</div>
				</div>
				</html:form>
	</body>
</html>

<script type="text/javascript" language="javascript">
		//<!--
			positionMiddleScreen('contenido');
			$(document).ready(
				function(){
					$("#contenido").draggable();
				}
			);
		//--!>
</script>
<script>						
		document.getElementById('etiqueta_applet').style.display='';
		Delay();		
</script>