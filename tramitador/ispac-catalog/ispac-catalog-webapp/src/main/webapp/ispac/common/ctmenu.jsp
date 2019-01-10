<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr" %>

<!-- [Manu #814] INICIO - SIGEM Administración - Poner nombre entidad en la que estamos en el Catálogo de Procedimientos. -->

<%@page import="ieci.tecdoc.sgm.core.admin.web.AutenticacionAdministracion" %>
<%@page import="ieci.tecdoc.sgm.core.services.LocalizadorServicios" %>

<% 
	String entidad = AutenticacionAdministracion.obtenerDatosEntidad(request).getIdEntidad();
	String descEntidad = LocalizadorServicios.getServicioEntidades().obtenerEntidad(entidad).getNombreCorto();
%>

<!-- [Manu #814] FIN- SIGEM Administración - Poner nombre entidad en la que estamos en el Catálogo de Procedimientos. -->

<ispac:hasFunction var="inventario" functions="FUNC_INV_PROCEDURES_READ, FUNC_INV_PROCEDURES_EDIT, FUNC_INV_STAGES_READ, FUNC_INV_STAGES_EDIT, FUNC_INV_TASKS_READ, FUNC_INV_TASKS_EDIT, FUNC_INV_DOCTYPES_READ, FUNC_INV_DOCTYPES_EDIT, FUNC_INV_TEMPLATES_READ, FUNC_INV_TEMPLATES_EDIT, FUNC_INV_SUBPROCESS_READ, FUNC_INV_SUBPROCESS_EDIT, FUNC_INV_SIGN_CIRCUITS_READ, FUNC_INV_SIGN_CIRCUITS_EDIT"/>
<ispac:hasFunction var="componentes" functions="FUNC_COMP_ENTITIES_READ, FUNC_COMP_ENTITIES_EDIT, FUNC_COMP_VALIDATION_TABLES_READ, FUNC_COMP_VALIDATION_TABLES_EDIT, FUNC_COMP_HIERARCHICAL_TABLES_READ, FUNC_COMP_HIERARCHICAL_TABLES_EDIT, FUNC_COMP_RULES_READ, FUNC_COMP_RULES_EDIT, FUNC_COMP_SEARCH_FORMS_READ, FUNC_COMP_SEARCH_FORMS_EDIT, FUNC_COMP_CALENDARS_READ, FUNC_COMP_CALENDARS_EDIT, FUNC_COMP_REPORTS_READ, FUNC_COMP_REPORTS_EDIT, FUNC_COMP_SYSTEM_VARS_READ, FUNC_COMP_SYSTEM_VARS_EDIT, FUNC_COMP_HELPS_READ, FUNC_COMP_HELPS_EDIT"/>
<ispac:hasFunction var="publicador" functions="FUNC_PUB_ACTIONS_READ, FUNC_PUB_ACTIONS_EDIT, FUNC_PUB_APPLICATIONS_READ, FUNC_PUB_APPLICATIONS_EDIT, FUNC_PUB_CONDITIONS_READ, FUNC_PUB_CONDITIONS_EDIT, FUNC_PUB_RULES_READ, FUNC_PUB_RULES_EDIT, FUNC_PUB_MILESTONES_READ, FUNC_PUB_MILESTONES_EDIT"/>
<ispac:hasFunction var="permisos" functions="FUNC_PERM_READ, FUNC_PERM_EDIT"/>

<script type="text/javascript"> 
//<!--
	window.onload = function() {
		<c:if test="${inventario}">initializeMenu("inventoryMenu", "inventoryActuator","menutile");</c:if>
		<c:if test="${componentes}">initializeMenu("componentesMenu", "componentesActuator","menutile");</c:if>
		<c:if test="${publicador}">initializeMenu("publisherMenu", "publisherActuator","menutile");</c:if>
		<c:if test="${permisos}">initializeMenu("usersMenu", "usersActuator","menutile");</c:if>
	}
//-->
</script>

<div id="usr">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
	      <td>
		<div id="usuario">
			<div id="barra_usuario">
				<p class="usuario">
					<span style="font-weight:normal">Entidad: <%=descEntidad%></span>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<bean:write name="User"/>
				</p>
			</div>
		</div>
	      </td>
	</tr>
</table>
</div>


<div id="mainMenu">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td>
			<ul id="menuList">
				<c:if test="${inventario}">
				<li class="menubar">
					<a href="#"
            			id="inventoryActuator"
             			class="actuator"
             			accesskey='<bean:message key="menu.inventory.sectiontitle.accesskey"/>'><bean:message key="menu.inventory.sectiontitle"/></a>
	           		<ul id="inventoryMenu" class="menu">
					
						<ispac:hasFunction functions="FUNC_INV_PROCEDURES_READ, FUNC_INV_PROCEDURES_EDIT">
	           			<li>
	           				<html:link action="showCTProceduresTree">
	           					<bean:message key="menu.procedures"/>
	       					</html:link>
	           			</li>
	           			<%--
	           			<li>
	           				<html:link action="showCTProceduresList">
	          					<bean:message key="menu.searchProcedures"/>
	      					</html:link>
	           			</li>
	           			--%>
						</ispac:hasFunction>

						<ispac:hasFunction functions="FUNC_INV_STAGES_READ, FUNC_INV_STAGES_EDIT">
	           			<li>
							<html:link action="showCTStagesList">
								<bean:message key="menu.stages"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
						<ispac:hasFunction functions="FUNC_INV_TASKS_READ, FUNC_INV_TASKS_EDIT">
            			<li>
							<html:link action="showCTTasksList">
								<bean:message key="menu.tasks"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
						<ispac:hasFunction functions="FUNC_INV_DOCTYPES_READ, FUNC_INV_DOCTYPES_EDIT">
            			<li>
							<html:link action="showCTTPDocsList">
								<bean:message key="menu.doctypes"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>

						<ispac:hasFunction functions="FUNC_INV_TEMPLATES_READ, FUNC_INV_TEMPLATES_EDIT">
            			<li>
							<html:link action="showCTTemplatesList">
								<bean:message key="menu.templates"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
						<ispac:hasFunction functions="FUNC_INV_SUBPROCESS_READ, FUNC_INV_SUBPROCESS_EDIT">
            			<li>
							<html:link action="showSubProceduresList">
								<bean:message key="menu.subprocedures"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
		
						<ispac:hasFunction functions="FUNC_INV_SIGN_CIRCUITS_READ, FUNC_INV_SIGN_CIRCUITS_EDIT">
						<c:set var="digitalSignManagementActive" value="${ISPACConfiguration.DIGITAL_SIGN_CONNECTOR_CLASS}"/>
						<c:if test="${!empty digitalSignManagementActive}">
            			<li>
							<html:link action="showSignProcessList">
								<bean:message key="menu.signprocess"/>
				  			</html:link>
            			</li>
            			</c:if>
						</ispac:hasFunction>
            			
	          		</ul>
				</li>
				</c:if>
				
				<c:if test="${componentes}">
				<li class="menubar">
					<a href="#"
             			id="componentesActuator"
             			class="actuator"
             			accesskey='<bean:message key="menu.components.sectiontitle.accesskey"/>'><bean:message key="menu.components.sectiontitle"/></a>
	             	<ul id="componentesMenu" class="menu">
					
						<ispac:hasFunction functions="FUNC_COMP_ENTITIES_READ, FUNC_COMP_ENTITIES_EDIT">
            			<li>
							<html:link action="showCTEntitiesList">
								<bean:message key="menu.entities"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>

						<ispac:hasFunction functions="FUNC_COMP_VALIDATION_TABLES_READ, FUNC_COMP_VALIDATION_TABLES_EDIT">
            			<li>
							<html:link action="showCTValueTablesList">
								<bean:message key="menu.valueTables"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>

						<ispac:hasFunction functions="FUNC_COMP_HIERARCHICAL_TABLES_READ, FUNC_COMP_HIERARCHICAL_TABLES_EDIT">
						<c:set var="hierarchicalTablesManagementActive" value="${ISPACConfiguration.HIERARCHICAL_TABLES_MANAGEMENT_ACTIVE}"/>
						<c:if test="${hierarchicalTablesManagementActive == 'true'}">
            			<li>
							<html:link action="showCTHierarchicalTablesList">
								<bean:message key="menu.hierarchicalTables"/>
				  			</html:link>
            			</li>
						</c:if>
						</ispac:hasFunction>
						
            			<%--
            			<li>
							<html:link action="showCTAplicationsList">
								<bean:message key="menu.applications"/>
							</html:link>
            			</li>
            			--%>
						
						<ispac:hasFunction functions="FUNC_COMP_RULES_READ, FUNC_COMP_RULES_EDIT">
            			<li>
							<html:link action="showCTRulesList">
								<bean:message key="menu.rules"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
						<ispac:hasFunction functions="FUNC_COMP_SEARCH_FORMS_READ, FUNC_COMP_SEARCH_FORMS_EDIT">
            			<li>
							<html:link action="showCTFrmSearchList">
								<bean:message key="menu.searchForms"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>

						<ispac:hasFunction functions="FUNC_COMP_CALENDARS_READ, FUNC_COMP_CALENDARS_EDIT">
            			<li>
							<html:link action="showCTCalendarsList">
								<bean:message key="menu.calendars"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>

						<ispac:hasFunction functions="FUNC_COMP_REPORTS_READ, FUNC_COMP_REPORTS_EDIT">
            			<li>
							<html:link action="showCTReportsList">
								<bean:message key="menu.reports"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>

						<ispac:hasFunction functions="FUNC_COMP_SYSTEM_VARS_READ, FUNC_COMP_SYSTEM_VARS_EDIT">
            			<li>
							<html:link action="showCTVbleSystemList">
								<bean:message key="menu.vbleSystem"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
						<!-- [eCenpri-Manu #120] INICIO - ALSIGM3 Crear opción de menú que devuelva el manual de usuario del procedimento. -->
						<ispac:hasFunction functions="FUNC_COMP_REPORTS_READ, FUNC_COMP_REPORTS_EDIT">
            			<li>
							<html:link action="showCTManualesUsuarioList">
								<bean:message key="menu.manualesUsuario"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
            			<!-- [eCenpri-Manu #120] FIN - ALSIGM3 Crear opción de menú que devuelva el manual de usuario del procedimento. -->
            			
            			<!-- [Dipucr-Manu Ticket #] INICIO - ALSIGM3 Mantenimiento de mensajes. -->
						<ispac:hasFunction functions="FUNC_COMP_REPORTS_READ, FUNC_COMP_REPORTS_EDIT">
            			<li>
							<html:link action="showCTMantenimientoMensajesList">
								<bean:message key="menu.mantenimientoMensajes"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
            			<!-- [Dipucr-Manu Ticket #] FIN - ALSIGM3 Mantenimiento de mensajes. -->
            			
						<ispac:hasFunction functions="FUNC_COMP_HELPS_READ, FUNC_COMP_HELPS_EDIT">
            			<li>
							<html:link action="showCTHelpList">
								<bean:message key="menu.Help"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
	          		</ul>
				</li>
				</c:if>
				
				<c:set var="publisherManagementActive" value="${ISPACConfiguration.PUBLISHER_MANAGEMENT_ACTIVE}"/>
				<c:if test="${publicador && (empty publisherManagementActive || publisherManagementActive != 'false')}">
				<li class="menubar">
					<a href="#"
             			id="publisherActuator"
             			class="actuator"
             			accesskey='<bean:message key="menu.publisher.sectiontitle.accesskey"/>'><bean:message key="menu.publisher.sectiontitle"/></a>
	             	<ul id="publisherMenu" class="menu">
					
						<ispac:hasFunction functions="FUNC_PUB_ACTIONS_READ, FUNC_PUB_ACTIONS_EDIT">
            			<li>
							<html:link action="showPubActionsList">
								<bean:message key="menu.publisher.actions"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
						<ispac:hasFunction functions="FUNC_PUB_APPLICATIONS_READ, FUNC_PUB_APPLICATIONS_EDIT">
            			<li>
							<html:link action="showPubApplicationsList">
								<bean:message key="menu.publisher.applications"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
						<ispac:hasFunction functions="FUNC_PUB_CONDITIONS_READ, FUNC_PUB_CONDITIONS_EDIT">
            			<li>
							<html:link action="showPubConditionsList">
								<bean:message key="menu.publisher.conditions"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
						<ispac:hasFunction functions="FUNC_PUB_RULES_READ, FUNC_PUB_RULES_EDIT">
            			<li>
							<html:link action="showPubRulesGroupList">
								<bean:message key="menu.publisher.rules"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
						<ispac:hasFunction functions="FUNC_PUB_MILESTONES_READ, FUNC_PUB_MILESTONES_EDIT">
            			<li>
							<html:link action="showPubErrorMilestonesList">
								<bean:message key="menu.publisher.milestones.error"/>
				  			</html:link>
            			</li>
						</ispac:hasFunction>
						
	          		</ul>
				</li>
 				</c:if>

				<c:if test="${permisos}">
				<li class="menubar">
					<html:link styleId="usersActuator" styleClass="actuator" action="showInfoEntry">
	           				<bean:message key="menu.users.sectiontitle"/>
	       			</html:link>
				</li>
				</c:if>

			</ul>
		</td>
		<td><nobr>
			
			
			<c:set var="useOdtTemplates"><%=ConfigurationMgr.getVarGlobalBoolean((ieci.tdw.ispac.ispaclib.context.ClientContext)request.getAttribute("ClientContext"), ConfigurationMgr.USE_ODT_TEMPLATES, false)%></c:set>
			
			<div id="configLink">
			<!-- [Manu Ticket #97] INICIO - ALSIGM3 Adapatar AL-SIGM para poder editar documentos sin necesidad de java. -->		
				<!--<ispac:rewrite id="configPage" page="config.jsp"/>
				<a href='javascript:;' onclick="javascript:window.open('<%=configPage%>','','status=no,scrollbars=no,location=no,toolbar=no,');">-->
				<ispac:rewrite id="configPage" page="configurarEditores.jsp"/>
				<a href='javascript:;' onclick="javascript:showFrameConfigurarEditores('<%=configPage%>','configFrame','', '' ,'', false);">
			<!-- [Manu Ticket #97] FIN - ALSIGM3 Adapatar AL-SIGM para poder editar documentos sin necesidad de java. -->		

					<img src='<ispac:rewrite href="img/config.gif"/>' style='vertical-align:middle' border='0'/>
					<bean:message key="title.config"/>
				</a>
			</div>
			<img height="1" width="5px" src="img/pixel.gif"/>
				<c:if test="${useOdtTemplates != 'true'}">
					<script>
						if (!isIEWord()) {
							document.getElementById("configLink").style.display = "block";
						}else{
							document.getElementById("configLink").style.display = "none";
						}
					</script>				
				</c:if>	
			</nobr>
		</td>
		<td>
			<div id="helpLink">
				<ispac:onlinehelp tipoObj="31" image="img/help.gif" titleKey="title.help"/>
			</div>
			<img height="1" width="5px" src="img/pixel.gif"/>
		</td>
	</tr>
</table>
</div>

<!-- [Manu Ticket #97] INICIO - ALSIGM3 Adapatar AL-SIGM para poder editar documentos sin necesidad de java. -->		
<script>
	function showFrameConfigurarEditores(action, target, width, height, msgConfirm, doSubmit, needToConfirm, form) {

		if (needToConfirm != null) {

			if (typeof ispac_needToConfirm != 'undefined') {
				ispac_needToConfirm = needToConfirm;
			}
		}

		if ((msgConfirm != null) && (msgConfirm != ''))
			if (confirm(msgConfirm) == false)
				return;

		if ((form == null) && (document.forms.length > 0)) {
			form = document.forms[0];
		}

		var element;
		/*if (width == null)
			width = 640;
		if (height == null)
			height = 480;*/

			width=getWidthWindow();
			height=getHeightWindow();

		eval('element = document.getElementById("' + target + '")');

		if (element != null) {

			showLayer();

			height='500';
			width='750';

			var x = (document.body.clientWidth - width) / 2;
			//var y = (document.body.clientHeight - height) / 2;
			var y = document.body.scrollTop + (document.body.clientHeight - height) / 2;
			if (y < 10) {
				y = 10;
			}

			element.style.height = height;
			element.style.width = width;
			element.style.position = "absolute";
			element.style.left = x;
			element.style.top = y;
			element.style.visibility = "visible";

			if (document.forms.length > 0) {

				var oldTarget = form.target;
				var oldAction = form.action;

				eval('form.target = "' + target + '"');

				if (action.substring(0,4) == 'http')
					form.action = action;
				else if (action.substring(0,1) != '/')
					form.action = replaceActionForm(form.action, action);
				else
					form.action = action;

				// Hacer un submit al workframe o abrir una url
				if ((doSubmit != null) && (!doSubmit)) {
					window.frames[target].location = form.action;;
				}
				else {
					form.submit();
				}

				form.target = oldTarget;
				form.action = oldAction;
			}
			else {

				eval("frames['" + target + "'].location.href='" + action + "'");

			}
		}
	}

	function showLayer(id){
	  var element;
	  var elements;
	  var i;

	  if (id == null) {
		id = "layer";
	  }

	  element = document.getElementById(id);

	  if (element != null)
	  {
		// Deshabilitar el scroll
		document.body.style.overflow = "hidden";

		element.style.position = "absolute";
		//element.style.height = document.body.clientHeight;
		element.style.height = document.body.scrollHeight + 1200;
		element.style.width = document.body.clientWidth + 1200;
		element.style.left = -600;
		element.style.top = -600;

		element.style.display = "block";

		if (isIE())
		{
		  elements = document.getElementsByTagName("SELECT");

		  for (i = 0; i < elements.length; i++)
		  {
			elements[i].style.visibility = "hidden";
		  }
		}
	  }
	}

	function getWidthWindow(){
		var width= 630;
		if (parseInt(navigator.appVersion)>3) {
			 if (navigator.appName=="Netscape") {
			  width = window.innerWidth;
			 }
			 if (navigator.appName.indexOf("Microsoft")!=-1) {
			  width = document.body.offsetWidth;

			 }
			}
		return width;
	}

	function getHeightWindow(){

		var height = 460;

			if (parseInt(navigator.appVersion)>3) {
			 if (navigator.appName=="Netscape") {
			  height = window.innerHeight;
			 }
			 if (navigator.appName.indexOf("Microsoft")!=-1) {
			  height = document.body.offsetHeight;
			 }
			}
		return height;
	}
</script>

<div id="layer" style="display:none;z-index:1000;background:white;filter:alpha(opacity=80);-moz-opacity:.80;opacity:.80;"/></div>
<iframe src='' id='configFrame' name='configFrame' style='visibility:hidden;z-index:1024;border:none;height:0px' allowtransparency='true' scrolling="no"></iframe>
<!--<iframe src='' id='configFrame' name='configFrame' style='visibility:hidden;width:0px;height:0px;margin:0px;padding:0px;'></iframe>-->
<!-- [Manu Ticket #97] FIN - ALSIGM3 Adapatar AL-SIGM para poder editar documentos sin necesidad de java. -->		
