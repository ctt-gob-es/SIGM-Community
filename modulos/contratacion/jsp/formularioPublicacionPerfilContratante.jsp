<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_PUBLIC_PLACE:CONTRATACION_PUBLIC_PLACE)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_PUBLIC_PLACE"
			style="position: relative; height: 110px; width: 600px">
			<div id="label_CONTRATACION_PUBLIC_PLACE:ANUNCIO_LICITACION"
				style="position: absolute; top: 10px; left: 10px; width: 180px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PUBLIC_PLACE:ANUNCIO_LICITACION)" />
				:
			</div>
			<div id="data_CONTRATACION_PUBLIC_PLACE:ANUNCIO_LICITACION"
				style="position: absolute; top: 10px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_PUBLIC_PLACE:ANUNCIO_LICITACION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_CONTRATACION_PUBLIC_PLACE:ANUNCIO_FORMALIZACION"
				style="position: absolute; top: 60px; left: 10px; width: 180px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PUBLIC_PLACE:ANUNCIO_FORMALIZACION)" />
				:
			</div>
			<div id="data_CONTRATACION_PUBLIC_PLACE:ANUNCIO_FORMALIZACION"
				style="position: absolute; top: 60px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_PUBLIC_PLACE:ANUNCIO_FORMALIZACION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_CONTRATACION_PUBLIC_PLACE:ANUNCIO_ADJUDICACION"
				style="position: absolute; top: 35px; left: 10px; width: 180px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PUBLIC_PLACE:ANUNCIO_ADJUDICACION)" />
				:
			</div>
			<div id="data_CONTRATACION_PUBLIC_PLACE:ANUNCIO_ADJUDICACION"
				style="position: absolute; top: 35px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_PUBLIC_PLACE:ANUNCIO_ADJUDICACION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>