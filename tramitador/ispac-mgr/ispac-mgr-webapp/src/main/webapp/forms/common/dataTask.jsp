<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>

<ispac:rewrite id="imgcalendar" href="img/calendar/"/>
<ispac:rewrite id="jscalendar" href="../scripts/calendar.js"/>
<ispac:rewrite id="buttoncalendar" href="img/calendar/calendarM.gif"/>

<ispac:calendar-config imgDir='<%= imgcalendar %>' scriptFile='<%= jscalendar %>'/>

<table cellspacing="0" cellpadding="0" align="center" width="90%">

	<tr>
		<td height="10px"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="10px"/></td>
	</tr>
	<tr>
		<td class="textbar">
		
		<!-- [eCenpri-Felipe #467] Se da aspecto "caja_tramite" y se añade imagen de carpeta "begin.gif" delante -->
			<table cellspacing="0" cellpadding="0" border="0" class="caja_tramite">
			
				<tr>
					<td class="textbar">
						<bean:message key="forms.task.task"/>:
					</td>
					<td>
						<img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
					<td class="text11">
						<bean:write name="defaultForm" property="property(SPAC_DT_TRAMITES:NOMBRE)"/>
					</td>
					<td>
						<img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
					<td class="textbar">
						<bean:message key="forms.task.initiated"/>: 
					</td>
					<td>
						<img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
					<td class="text11">
						<bean:write name="defaultForm" property="property(SPAC_DT_TRAMITES:FECHA_INICIO)"/>
					</td>
				</tr>
				
			</table>
			
		</td>
	</tr>
	<tr>
		<td width="100%" valign="bottom" height="5px" style="font-size:4px;">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td valign="top">
		
			<!-- [eCenpri-Felipe #467] Link para mostrar los datos trámite -->
			<a href="javascript:mostrarDatosTramite();">
				<img id="imgDesplegar" src='<ispac:rewrite href="img/desplegar.jpg"/>' border="0" />
				<bean:message key="forms.task.mostrarDatosTramites"/>
			</a>
			
			<div id="datosTramite" style="display:none;"><!-- [eCenpri-Felipe #467] -->
		
				<table class="caja" cellspacing="0" cellpadding="0" width="100%">
					<tr>
						<td height="8px"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
					</tr>
					<tr>
						<td width="8"><img height="1" width="8px" src='<ispac:rewrite href="img/pixel.gif"/>'/>
						</td>
						<td colspan="2">
							<table cellspacing="0" cellpadding="0" width="100%">
								<tr valign="top">
									<td height="20" style="width:180px;" class="formsTitleB">
										<nobr><bean:message key="forms.task.responsibleDept"/>:</nobr>
									</td>
									<td height="20">
										<bean:write name="defaultForm" property="entityApp.property(SPAC_DT_TRAMITES:UND_RESP)"/>
									</td>
								</tr>
								
								<logic:notEmpty name="defaultForm" property="entityApp.property(ID_RESP_NAME)">
									<tr valign="top">
										<td height="20" class="formsTitleB">
											<nobr><bean:message key="forms.task.processDept"/>:</nobr>
										</td>
										<td height="20">
											<bean:write name="defaultForm" property="entityApp.property(ID_RESP_NAME)"/>
										</td>
									</tr>
								</logic:notEmpty>
	
								<tr valign="top">
									<td height="20" class="formsTitleB">
										<nobr><bean:message key="forms.task.remarks"/>:</nobr>
									</td>
									<td height="20">
										<ispac:htmlTextarea property="property(SPAC_DT_TRAMITES:OBSERVACIONES)"
											readonly="false" styleClass="input" rows="2" cols="85" style="color:red"></ispac:htmlTextarea>
										<!-- [eCenpri-Felipe #467] color rojo: rechazos y reparos-->
									</td>
								</tr>
	
							</table>
						</td>
						<td width="8"><img height="1" width="8px" src='<ispac:rewrite href="img/pixel.gif"/>'/>
						</td>
					</tr>
					<tr>
						<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="10px"/></td>
					</tr>
					<tr>
						<td width="8"><img height="1" width="8px" src='<ispac:rewrite href="img/pixel.gif"/>'/>
						</td>
						<td colspan="2">
							<table style="border:0px solid #616EAA;" cellspacing="0" cellpadding="0" width="100%">
								<tr>
									<td height="20" width="25%" class="formsTitleB"><bean:message key="forms.task.term.initDate"/>:<br>
									
										<nobr>
											<ispac:htmlTextCalendar property="property(SPAC_DT_TRAMITES:FECHA_INICIO_PLAZO)" readonly="false" propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" size="12" maxlength="10" image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true" />
										</nobr>
																			
									</td>
									<td height="20" width="10%" class="formsTitleB">
									
										<bean:message key="forms.task.term"/>:<br>
										<ispac:htmlText property="property(SPAC_DT_TRAMITES:PLAZO)" readonly="false" propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" size="4" maxlength="4" />
										
									</td>
									<td height="20" width="40%" class="formsTitleB"><bean:message key="forms.task.term.uds"/>:<br>
	
										<html:hidden property="property(SPAC_DT_TRAMITES:UPLAZO)"/>
										
										<ispac:htmlTextImageFrame property="property(UPLAZO_SPAC_TBL_001:SUSTITUTO)"
																  readonly="true"
																  readonlyTag="false"
																  propertyReadonly="readonly"
																  styleClass="input"
																  styleClassReadonly="inputReadOnly"
																  size="34"
															  	  id="TASK_SELECT_TERM_UNITS"
																  target="workframe"
															  	  action="selectSubstitute.do?entity=SPAC_TBL_001" 
															  	  image="img/search-mg.gif" 
															  	  titleKeyLink="forms.tasks.select.uplazo" 
															  	  showFrame="true">
															  	  
	                                  		<ispac:parameter name="TASK_SELECT_TERM_UNITS" id="property(SPAC_DT_TRAMITES:UPLAZO)" property="VALOR"/>
	              							<ispac:parameter name="TASK_SELECT_TERM_UNITS" id="property(UPLAZO_SPAC_TBL_001:SUSTITUTO)" property="SUSTITUTO"/>
										</ispac:htmlTextImageFrame>
										
									</td>
									<td height="20" width="15%" class="formsTitleB">
										
										<bean:message key="forms.task.dateAlarm"/>:<br>
										<html:text property="property(SPAC_DT_TRAMITES:FECHA_LIMITE)" styleClass="inputReadOnly" size="12" maxlength="12" readonly="true"/>
										
									</td>
									<td width="8"><img height="1" width="8px" src='<ispac:rewrite href="img/pixel.gif"/>'/>
									</td>
								</tr>
								<tr>
									<td height="6px"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="6px"/></td>
								</tr>
								
							</table>
							
						</td>
						<td width="8"><img height="1" width="8px" src='<ispac:rewrite href="img/pixel.gif"/>'/>
						</td>
					</tr>
					<tr>
						<td height="4px"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="4px"/></td>
					</tr>
					
				</table>
			</div> 			<!-- [eCenpri-Felipe #467] Link para mostrar los datos trámite -->			
		</td>
	</tr>
	<tr>
		<td height="4px"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="4px"/></td>
	</tr>
	
</table>

<!-- INICIO [eCenpri-Felipe #467] Función para desplegar los datos de trámite.
	Estos aparecerán por defecto desplegados cuando haya observaciones -->
<script language='JavaScript' type='text/javascript'>

function mostrarDatosTramite() {
	div = document.getElementById('datosTramite');
	img = document.getElementById('imgDesplegar');
	if (div.style.display == 'none'){
		div.style.display = '';
		img.src = '<ispac:rewrite href="img/encoger.jpg"/>';
	}
	else{
		div.style.display = 'none';
		img.src = '<ispac:rewrite href="img/desplegar.jpg"/>';
	}
}

<logic:notEmpty name="defaultForm" property="property(SPAC_DT_TRAMITES:OBSERVACIONES)">
mostrarDatosTramite();
</logic:notEmpty>

</script>
<!-- FIN [eCenpri-Felipe #467] -->