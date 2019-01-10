package es.dipucr.sigem.api.rule.procedures.bop.imprenta;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;

public class DipucrMailTrabImprentaAcabado implements IRule {
	
	private static final Logger logger =  Logger.getLogger(DipucrMailTrabImprentaAcabado.class);
	
	private static final String EMAIL_SUBJECT_VAR_NAME = "IMPR_TRAB_ACABADO_ASUNTO";

	/**
	<img src='cid:escudo' width='200px'/>
	<p align=justify>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Estimado señor/a:
		<br/><br/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Me dirijo a usted/es para informarle que el trabajo <b>${TRABAJO}</b> que nos tiene solicitado para su realización en esta Imprenta Provincial ha sido terminado, por lo que a la mayor brevedad posible puede pasarse por estas dependencias para su retirada. Nuestro horario de recogida es de 8,00 a 14,30 de lunes a viernes. Dirección: Ronda del Carmen s/n (junto al antiguo Hospital de El Carmen en Ctra. de Porzuna).
		<br/><br/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;En espera de haber realizado satisfactoriamente el trabajo deseado, reciba nuestra mejor disposición.
		<br/><br/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Le recuerdo, que si su trabajo está registrado con <u>Depósito Legal</u>, tiene la obligación como editor, de presentar 4 ejemplares completos junto con el impreso ANEXO II que les será facilitado en las oficinas de la Consejería de Educación, Cultura y Deportes en C/ Alarcos, 21 de Ciudad Real, o descargárselo por internet en el siguiente enlace: <a target='_blank' href='http://registrovirtual.jccm.es/?SER_ID=JWP'>http://registrovirtual.jccm.es/?SER_ID=JWP</a> (art. 12 de la Ley 23/2011 de 29 de julio. BOE de 30/07/2011).
		<br/><br/>	
	</p>
		<br/><br/>
	<p align=center>
		<b>Teléfono: 926 25 59 50 ext. 543 o 540<b/>
		<br/>
		<b>Fax: 926 25 02 53<b/>
		<br/>
		<b>Ronda del Carmen s/n. 13002 CIUDAD REAL<b/>
		<br/>
		<b><font color='BLUE'>e-mail: imprenta@dipucr.es</font><b/>
		<br/>
	</p>
	**/
	private static final String EMAIL_CONTENT_VAR_NAME = "IMPR_TRAB_ACABADO_CONTENIDO";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
						
			String numexp = rulectx.getNumExp();
			
			Map<String,String> variables = new HashMap<String,String>();
			variables.put("NUMEXP", numexp);

			IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesImprenta.TABLA_DATOS_PETICION_TRABAJO, numexp);
	        if(solicitudCollection.toList().size() > 0){
	        	IItem solicitud = (IItem) solicitudCollection.iterator().next(); 
	        	String emails = solicitud.getString(ConstantesImprenta.COLUMNA_EMAILCONTACTO);
	        	String tipo_trabajo = solicitud.getString(ConstantesImprenta.COLUMNA_TIPO_TRABAJO);

	        	if(StringUtils.isNotEmpty(emails)){	
	        		if(StringUtils.isNotEmpty(tipo_trabajo)){
	        			variables.put("TRABAJO", tipo_trabajo);
	        		}
	        		
					String subject = ConfigurationMgr.getVarGlobal(cct, EMAIL_SUBJECT_VAR_NAME);
					String content = ConfigurationMgr.getVarGlobal(cct, EMAIL_CONTENT_VAR_NAME);

					if (StringUtils.isNotBlank(subject)) {
						subject = StringUtils.replaceVariables(subject, variables);
					}

					if (StringUtils.isNotBlank(content)) {
						content = StringUtils.replaceVariables(content, variables);
					}
					
					/**
					 * [Teresa Ticket #59 INICIO]Cambio de la ruta de las imágenes 
					 * **/
					String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
					/**
					 * [Teresa Ticket #59 FIN]Cambio de la ruta de las imágenes 
					 * **/
					Object[] imagen = {rutaImg, new Boolean(true), "logoCabeceraBOP.bmp", "escudo"};
					List<Object[]> imagenes = new ArrayList<Object[]>();
					imagenes.add(imagen);
					String[] emailsSplit = emails.split(";");
					for(String email : emailsSplit){
						try{	
							if(StringUtils.isNotEmpty(email)){
							MailUtil.enviarCorreo(rulectx.getClientContext(), email, subject, content, imagenes);
							logger.info("[DipucrEnviaMailSolicitudRecibida:execute()] Email enviado a: "	+ email);
							}
						}
						catch(Exception e){
							logger.error(this.getClass().getName() + ": Error al enviar e-mail a: " + email + ". " + e.getMessage(), e);
							content = "ERROR - " + e.getMessage() + " <br>" + content;
							subject = "ERROR - " + e.getMessage() + ". " + subject;
							try{
								CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
								email = correoConfig.get("correo_imprenta_error");
								MailUtil.enviarCorreo(rulectx.getClientContext(), email, subject, content, imagenes);
							}catch(Exception e1){
								logger.error(this.getClass().getName()+": Error al enviar e-mail a: " + email + ". " + e1.getMessage(), e1);
							}
						}
					}		     		    	   
	        	}
	        }
		} catch (Exception e){
			logger.error("Error al insertar el nuevo aviso. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al insertar el nuevo aviso. " + e.getMessage(), e);
		}
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
