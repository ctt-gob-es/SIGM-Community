<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglibs/displaytree.tld" prefix="tree" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>

	<script type="text/javascript" src='<ispac:rewrite href="../scripts/tree/x_core.js"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/tree/utils.js"/>'></script>
	<link rel="shortcut icon" href='<ispac:rewrite href="img/favicon.ico"/>' type="image/x-icon"/>
  	<link rel="icon" href='<ispac:rewrite href="img/favicon.ico"/>' type="image/x-icon"/>
  	
	<link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>'/>
	<link rel="stylesheet" href='<ispac:rewrite href="css/menus.css"/>'/>
	
	<link rel="stylesheet" href='<ispac:rewrite href="css/menus.css"/>'/>
	<link rel="stylesheet" href='<ispac:rewrite href="css/nicetabs.css"/>'/>
	<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>'/>
	<link rel="stylesheet" href='<ispac:rewrite href="css/tablist.css"/>'/>
	<link rel="stylesheet" href='<ispac:rewrite href="css/newstyles.css"/>'/>

	<script type="text/javascript" src='<ispac:rewrite href="../scripts/menus.js"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/sorttable.js"/>'> </script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js"/>'> </script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/newutilities.js"/>'> </script>

	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-1.3.2.min.js"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-ui-1.7.2.custom.min.js"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery.alerts.js"/>'></script>

	<link rel="shortcut icon" href='<ispac:rewrite href="img/favicon.ico"/>' type="image/x-icon"/>
  	<link rel="icon" href='<ispac:rewrite href="img/favicon.ico"/>' type="image/x-icon"/>

	<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab.css"/>'/>
	
	<!--[if lte IE 5]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie5.css"/>'/>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab_ie5.css"/>'/>
	<![endif]-->	

	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie6.css"/>'/>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab_ie6.css"/>'/>
	<![endif]-->	

	<!--[if gte IE 7]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie7.css"/>'/>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab_ie7.css"/>'/>
	<![endif]-->

	<link rel="stylesheet" href='<ispac:rewrite href="css/nicetabsArbol.css'"/>'/>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/forms.js'"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/sorttable.js'"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js'"/>'></script>


<div id="contenido" class="move" >
	<div class="ficha"> 
		<div class="encabezado_ficha">
			<div class="titulo_ficha">
				<h4><label alt = '<bean:message key="head.title" />' title = '<bean:message key="head.title" />' id="titulo"><bean:message key="head.title" /></label></h4>
				<div class="acciones_ficha">
					<a href="#" id="btnCancel" onclick='javascript:top.ispac_needToConfirm=false;<ispac:hideframe refresh="true"/>' class="btnCancel" alt = '<bean:message key="common.message.close" />' title = '<bean:message key="common.message.close" />'><bean:message key="common.message.close" /></a>
				</div>
			</div>
		</div>
		<div class="cuerpo_ficha">
			<div class="seccion_ficha" id="seccion">
				<script type="text/javascript">
					function refreshWindow(selectedNode) {
						var treeManagerForm = document.forms['treeManagerForm'];
						if (treeManagerForm) {
							treeManagerForm.method.value = 'obtenerVista';
							treeManagerForm.openNodes.value = joinArray(trees[0].openNodes, '-');
							treeManagerForm.closedNodes.value = joinArray(trees[0].closedNodes, '-');
							treeManagerForm.selectedNode.value = selectedNode;
							treeManagerForm.target = self.name;
							treeManagerForm.submit();
						}
					}
				</script>
	
				<link rel="stylesheet" href='<ispac:rewrite href="css/frameArbol.css"/>'/>
	
				<script>
					function execConfirm(msg,url){
						window.top.jConfirm(msg, '<bean:message key="common.confirm"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>', function(r) {
							if(r){
								window.document.location.href=url;
							}				
						});	
					}
					
					function execRedirect(url){
						window.document.location.href=url;
					}
				</script>
	
			<!-- [eCenpri-Manu #281] - INICIO - Edición de plantillas desde el tramitador por usuarios -->			
				<script>
					function consultaEdicionTemplate(idTemplate){
								var url = document.location.href + '/showTemplateArbolDocumentalAction.do?template=' + idTemplate;
								url = url.replace('/arbolVistaCuadroProcedimiento.do/', '');
						
								var ventana = window.open(url);
							
								//setTimeout(function cierra(){ventana.close();}, 1000);
					}
				</script>
				
			<!-- [eCenpri-Manu #281] - FIN - Edición de plantillas desde el tramitador por usuarios -->
				
				<c:set var="viewName"><c:out value="${param.viewName}"><c:out value="CUADRO_PROCEDIMIENTO" /></c:out></c:set>
				<jsp:useBean id="viewName" type="java.lang.String" />
	
				<c:set var="scripts_tree_path"><ispac:rewrite href="../scripts/tree/"/></c:set>
				<jsp:useBean id="scripts_tree_path" type="java.lang.String" />
				<c:set var="images_tree_path"><ispac:rewrite href="img/procedureTree/"/></c:set>
				<jsp:useBean id="images_tree_path" type="java.lang.String" />
				
				<div class="divArbol">
	
					<tree:show action="/arbolVistaCuadroProcedimiento.do" name='<%=viewName%>' imagesPath='<%=images_tree_path%>' jsCodeFile='<%=scripts_tree_path+"tree.js"%>' imageConfFile='<%=scripts_tree_path+"entidades_tree_tpl.js"%>' target="ibasefrm">
						function getNodeImage(treeItem) {
							var customIcon = null;
							if (treeItem.n_id == 0 && treeItem.o_root.a_tpl['im_root']){
								customIcon = treeItem.o_root.a_tpl['im_root']
							}
							else if (treeItem.o_root.a_tpl['imgs_icons'] && treeItem.o_root.a_tpl['imgs_icons'][treeItem.a_config[1].TIPO_ELEMENTO]) {
								customIcon = treeItem.o_root.a_tpl['imgs_icons'][treeItem.a_config[1].TIPO_ELEMENTO][(treeItem.a_config[3]?0:4)+(treeItem.a_config[3]&&treeItem.b_opened?2:0)+(treeItem.selected?1:0)];
							}
							return customIcon;
						}
					</tree:show>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
</script>
