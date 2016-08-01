<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>


<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_PETICION:CONTRATACION_PETICION)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_PETICION"
			style="position: relative; height: 160px; width: 600px">
			
			<div id="label_CONTRATACION_PETICION:EMPRESA"
				style="position: absolute; top: 10px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PETICION:EMPRESA)" />:
			</div>
			<div id="data_CONTRATACION_PETICION:EMPRESA"
				style="position: absolute; top: 10px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_PETICION:EMPRESA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="20">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_PETICION:FECHA_PEDIDO"
				style="position: absolute; top: 35px; left: 10px; width: 110px;"
				class="formsTitleB">
					<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:FECHA_PEDIDO)" />:
			</div>
			<div id="data_CONTRATACION_PETICION:FECHA_PEDIDO"
				style="position: absolute; top: 35px; left: 130px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_PETICION:FECHA_PEDIDO)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_PETICION:PRESUPUESTO"
				style="position: absolute; top: 60px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PETICION:PRESUPUESTO)" />:
			</div>
			<div id="data_CONTRATACION_PETICION:PRESUPUESTO"
				style="position: absolute; top: 60px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_PETICION:PRESUPUESTO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="14" maxlength="20">
				</ispac:htmlText>Euros
			</div>
			
			<div id="label_CONTRATACION_PETICION:OBSERVACIONES"
				style="position: absolute; top: 85px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PETICION:OBSERVACIONES)" />:
			</div>
			<div id="data_CONTRATACION_PETICION:OBSERVACIONES"
				style="position: absolute; top: 85px; left: 130px; width: 100%;">
				<ispac:htmlTextarea property="property(CONTRATACION_PETICION:OBSERVACIONES)"
				readonly="false" styleClass="input" rows="5" cols="85" style="color:red" >
				</ispac:htmlTextarea>
									
			</div>
			
		</div>
	</ispac:tab>
</ispac:tabs>