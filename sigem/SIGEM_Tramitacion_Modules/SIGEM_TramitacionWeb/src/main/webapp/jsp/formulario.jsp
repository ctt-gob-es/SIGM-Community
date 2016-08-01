<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(DIPUCR_SVE:DIPUCR_SVE)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
	<!-- Boton cargar dertificado -->
	<div id="botton" align="center"> 
	<input type="button" value="Comprobar certificado" id="comprobarCertificado" class="buttonSearchAvanced" onclick="document.location.href='/SIGEM_TramitacionWeb/jsp/RedireccionCertificados.jsp'"" />
	
	</div>

	
	
	<!-- DNI -->
		<div id="dataBlock_DIPUCR_SVE"
			style="position: relative; height: 60px; width: 600px">
			<div id="label_DIPUCR_SVE:DNI_FUNCIONARIO"
				style="position: absolute; top: 10px; left: 10px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(DIPUCR_SVE:DNI_FUNCIONARIO)" />:</div>
			<div id="data_DIPUCR_SVE:DNI_FUNCIONARIO"
				style="position: absolute; top: 10px; left: 130px; width: 100%;">
			<ispac:htmlText property="property(DIPUCR_SVE:DNI_FUNCIONARIO)"
				readonly="true" propertyReadonly="readonly" styleClass="input"
				styleClassReadonly="inputReadOnly" size="80" maxlength="9">
			</ispac:htmlText></div>
		</div>
	</ispac:tab>
</ispac:tabs>