<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>

<!-- Inicio Cabecera -->
<%@page import="java.util.Locale"%>
<%@page import="ieci.tecdoc.sgm.core.services.idioma.Idioma"%>
<%@page import="es.dipucr.tecdoc.sgm.ct.utilities.Misc"%>

<%
String rutaEstilos = (String)session.getAttribute("PARAMETRO_RUTA_ESTILOS");
if (rutaEstilos == null) rutaEstilos = "";
String rutaImagenes = (String)session.getAttribute("PARAMETRO_RUTA_IMAGENES");
if (rutaImagenes == null) rutaImagenes = "";
%>

	<div id="cabecera">
		<utils-admin:eco-logo/>
		<p class="logoSIGM">					
		<!-- [eCenpri-Manu Ticket#267] + ALSIGM3 Logotipo de la entidad sale feas en registro presencial y tramitador -->
			<!-- <img src="<%=request.getContextPath()%>/resourceServlet/logos/logo.gif" alt="sigem" width="129px" height="56px"/> -->
			<img src="<%=request.getContextPath()%>/resourceServlet/logos/logo.gif" alt="SIGM" style="width: auto; max-width: 200px; height: 59px; max-height: 200px;" />
		<!-- [eCenpri-Manu Ticket#267] + ALSIGM3 Logotipo de la entidad sale feas en registro presencial y tramitador -->
		</p>
		<p class="salir"><a href="Desconectar.jsp"><bean:message key="salir"/></a></p>
	</div>
	<div id="usuario">
		<div id="barra_usuario">
			<h3><bean:message key="consultaExpedientesDe"/> <%=session.getAttribute("nombreUsuario")%></h3>
			<p class="ayuda">

				<%
					Locale locale = (Locale)request.getSession().getAttribute("org.apache.struts.action.LOCALE");
					String strIdioma = locale.getLanguage() + "_" + locale.getCountry();
				%>

				<logic:present name="<%=Misc.IDIOMAS_DISPONIBLES%>">

					<bean:define id="idiomasDesplegable" type="java.util.ArrayList" name="<%=Misc.IDIOMAS_DISPONIBLES%>" />
					<span class="idioma">
						<select id="selIdioma">
						<%
						for(int indIdioma = 	0; indIdioma<idiomasDesplegable.size(); indIdioma++){
							Idioma objIdioma = (Idioma)idiomasDesplegable.get(indIdioma);
						%>
							<option value="<%=objIdioma.getCodigo()%>" <%=(objIdioma.getCodigo().equals(strIdioma) ? "selected" : "")%>><%=objIdioma.getDescripcion()%></option>
						<%}%>
						</select>
						<a href="javascript:cambiarIdioma();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
						<iframe src="blank.html" id="recargarIdioma" style="top: 0px; left: 0px; visibility:hidden; position:absolute" >
						</iframe>
					</span>
				</logic:present>

				<a href="javascript:abrirAyuda('ayuda/ConsultaExpedientes.htm');">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
			</p>
		</div>
	</div>

<script language="javascript">
		function abrirAyuda(url){
		   var opciones="left=100,top=100,width=700,height=450,status=no,menubar=no,scrollbars=yes,status=no,resizable=yes";
			window.open(url,"hija",opciones);
		}
</script>
