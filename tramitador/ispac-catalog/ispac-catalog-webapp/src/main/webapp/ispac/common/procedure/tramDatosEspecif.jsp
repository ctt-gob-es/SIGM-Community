<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<bean:define id="entapp" name="defaultForm" property="entityApp" />
<bean:define name="pcdId" id="pcdId"/>
<bean:define name="entityId" id="entityId"/>
<bean:define name="regId" id="regId"/>
<bean:define name="keyId" id="keyId"/>

<div class="tabButtons">
	<ul class="tabButtonList">
		<li>
			<a href="javascript:document.getElementById('plantilla_defecto').value = document.getElementsByName('property(SPAC_P_TRAM_DATOSESPECIFICOS:PLANTILLA_DEFECTO)')[0].value;document.getElementById('otros_datos').value = document.getElementsByName('property(SPAC_P_TRAM_DATOSESPECIFICOS:OTROS_DATOS)')[0].value;submit('<c:url value="/storeProcedure.do"/>')">
				<nobr><bean:message key="forms.button.save"/></nobr>
			</a>
		</li>

	</ul>
</div>
<div class="tabContent">


<html:form action='/showProcedureEntity.do'>

	<!-- Identificador de la entidad -->
	<html:hidden property="entity"/>
	<!-- Identificador del registro -->
	<html:hidden property="key"/>
	<!-- Registro de solo lectura-->
	<html:hidden property="readonly"/>

	
	<html:hidden property="property(SPAC_P_TRAMITES:ID_CTTRAMITE)"/>
	
	<input type="hidden" name="method" value="datosEspecif"/>
	<input type="hidden" id="plantilla_defecto" name="plantilla_defecto" value=""/>
	<input type="hidden" id="otros_datos" name="otros_datos" value=""/>
	<input type="hidden" id="id_tram" name="id_tram" value="<%=request.getAttribute("regId")%>"/>	

	<table width="100%" border="0" cellspacing="1" cellpadding="0">
		<tr>
			<td class="blank">
				<table width="100%" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td height="5px" colspan="3"><html:errors/></td>
					</tr>
					<tr>
						<td width="8"><img height="1" width="8px" src='<ispac:rewrite href="img/pixel.gif"/>'/></td>
						<td width="100%">
							<div style="display:block" id="page1" class="formtable">
								<table border="0" cellspacing="0" cellpadding="0" align="center" width="90%">
									<tr>
										<td>
											<table border="0" cellspacing="0" cellpadding="0" width="100%">
												<tr>
													<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="16px"/></td>
												</tr>
												<tr>
													<td class="formsTitle" width="30%">
														<ispac:tooltipLabel labelKey="procedure.tramDatosEspecificos.propertyLabel.plantillaDefecto" tooltipKey="procedure.plazos.propertyLabel.magnitud.tooltip"/>
													</td>
												</tr>
												<tr>
													<td>
														&nbsp;&nbsp;<html:textarea name="entapp" property="property(SPAC_P_TRAM_DATOSESPECIFICOS:PLANTILLA_DEFECTO)" styleClass="input" readonly="false" cols="100" rows="4"
															 />
													</td>
												</tr>
												<tr>
													<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
												</tr>
												<tr>
													<td class="formsTitle">
														<ispac:tooltipLabel labelKey="procedure.tramDatosEspecificos.propertyLabel.otrosDatos" tooltipKey="procedure.plazos.propertyLabel.unidades.tooltip"/>
													</td>
												</tr>
												<tr>
													<td>
														&nbsp;&nbsp;<html:textarea name="entapp" property="property(SPAC_P_TRAM_DATOSESPECIFICOS:OTROS_DATOS)" styleClass="input" readonly="false" cols="100" rows="4"
															 ><%=((String)request.getAttribute("otros_datos"))%></html:textarea>
													</td>
												</tr>												
											</table>											
										</td>
									</tr>
									<tr>
										<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="15px"/></td>
									</tr>
								</table>
								
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
<script>
var plant_defect = '';
if('<%=((String)request.getAttribute("plantilla_defecto"))%>'=='null') plant_defect = '';
else plant_defect = '<%=((String)request.getAttribute("plantilla_defecto"))%>';
document.getElementsByName('property(SPAC_P_TRAM_DATOSESPECIFICOS:PLANTILLA_DEFECTO)')[0].value = plant_defect;
if('<%=((String)request.getAttribute("otros_datos"))%>'=='null') otros_datos = '';
else otros_datos = '<%=((String)request.getAttribute("otros_datos"))%>';
document.getElementsByName('property(SPAC_P_TRAM_DATOSESPECIFICOS:OTROS_DATOS)')[0].value = otros_datos;
</script>
</div>