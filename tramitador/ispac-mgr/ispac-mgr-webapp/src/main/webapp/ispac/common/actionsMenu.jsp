<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ page import="ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants" %>
<%@ page import='ieci.tdw.ispac.api.ISPACEntities'%>

<c:set var="ENTITY_TASK" scope="page"><%=ISPACEntities.DT_ID_TRAMITES%></c:set>
<table class="tableMenu" width="100%">
	<tr>
		<td width="470px">
		
<!-- 		<div id="botonera"><p class="menu_bt" ><a href="javascript:menu();" id="imgMenu" class="imgMenuOn">&nbsp;</a></p></div> -->
    	<div id="contenido_menu" class="menu_desplegado">
    		<div id="navegacion">
<!--     			<div class="encabezado_nav"><h3> </h3></div> -->
    			<div class="cuerpo_nav">
    				<div class="contenido_cuerpo_nav">
						<logic:present name="menus">
							<ispac:menuBar id="menu" name="menus" position="cols" styleLevel0="menu0" onlyDrawOptionsMenu="true" styleLevel1="menu1" padding="2px"></ispac:menuBar>
						</logic:present>
						<%-- Tramites de la fase actual (abiertos y cerrados) con stageId activa , exp abierto y Tramites cerrados en otras fases o todos los tramites de un expediente cerrado--%>
						<logic:notPresent name="isSubProcess">
							
								<ispac:menuTask name="SchemeList"/>
							
						</logic:notPresent>
			 
			
						<%-- Expedientes relacionados --%>
						<logic:present name="subExp">
							<ispac:menuExpRelated hijos="subExp" padres="supExp"/>
						</logic:present>
	
						<%-- Aplicaciones de gestion --%>
						<logic:present name="appsGestion">
							<ul class="menuGrupo">
							<li id="itemAppsGestion" class="item_plegado"> 
								<a href="#" onclick="javascript:showItem('AppsGestion');return false;"><bean:message key="menu.appGestion"/></a>
								<ul>
									<logic:iterate id="app" name="appsGestion">
										<c:url value="showExtraTab.do" var="_link">
											<c:param name="url">
												<bean:write name="app" property="property(ENLACE)"/>
											</c:param>
											<c:param name="stageId">
												<bean:write name="Params" property="stageId"/>
											</c:param>
											<c:param name="classId">
												<bean:write name="app" property="property(CLSID)"/>
											</c:param>
											<c:param name="codeBase">
												<bean:write name="app" property="property(CODEBASE)"/>
											</c:param>
										</c:url>
										<li class="menuSelect expRelCerrado">
											 <img src='<ispac:rewrite href="img/exp_hijo_cerrado.gif"/>' />
											<logic:notEqual value="true" property="readonly" name="defaultForm">
												<a href='<c:out value="${_link}"/>'>
								   					<bean:write name="app" property="property(NOMBRE_APLICACION_GESTION)"/>
												</a>
											
											</logic:notEqual>
											<logic:equal value="true" property="readonly" name="defaultForm">
												<a href='#' onclick="javascript:jAlert('<bean:message key="error.users.noSelected"/>', '<bean:message key="common.alert"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>');">
								   					<bean:write name="app" property="property(NOMBRE_APLICACION_GESTION)"/>
												</a>
											
											</logic:equal>
										</li>
									</logic:iterate>
								</ul>
							</li> 
							</ul>
						</logic:present>
	
	
						</div><!-- contenido_cuerpo_nav -->
		 			</div><!-- fin cuerpo_nav -->
				</div> <!-- fin navegacion -->
			</div><!--contenido_menu-->
		</td>
	</tr>
	<!-- INICIO [dipucr-Felipe #1246] Compatibilidad Firma 3 fases -->
	<%@page import = "ieci.tdw.ispac.ispaclib.session.OrganizationUser"%>
	<%@page import = "ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo"%>
	<%@page import = "ieci.tdw.ispac.ispaclib.sign.SignConnectorFactory" %>
	<% 
		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		String entityId = info.getOrganizationId();
		boolean bPortafirmas = SignConnectorFactory.isPortafirmasImplClass(entityId);
		if (bPortafirmas){
	%>
	<tr>
		<td width="470px">	
			<div style="width:80%; margin:0 auto" >
				<br/><br/>
				<a href="https://portafirmas.dipucr.es/pf" target="_blank" >
					<img src='<ispac:rewrite href="img/portafirmas_link.png"/>' style="height:30px;"/> 
				</a>
			</div>			
		</td>		
	</tr>
	<tr>
		<td>
			<div style="width:80%; margin:0 auto" >
				<br/><br/>
				<a href="https://nextcloud.dipucr.es/index.php/s/kbieDxfEa2FpepL" target="_blank" >
						<img src='<ispac:rewrite href="img/manuales.png"/>' style="height:30px;"/> 
				</a>
			</div>
			<div style="width:80%; margin:0 auto" >
				<br/><br/>
				<a href="https://nextcloud.dipucr.es/index.php/s/E4wbK7pksPSL3EB" target="_blank" >
						<img src='<ispac:rewrite href="img/herramientas.png"/>' style="height:30px;"/> 
				</a>
			</div>
		</td>		
	</tr>
	<%
		}
	%>
	<!-- FIN [dipucr-Felipe #1246]  -->
</table>

