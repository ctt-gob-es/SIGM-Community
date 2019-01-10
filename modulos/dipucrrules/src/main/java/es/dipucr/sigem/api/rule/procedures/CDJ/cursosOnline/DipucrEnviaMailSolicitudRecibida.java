package es.dipucr.sigem.api.rule.procedures.CDJ.cursosOnline;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;

public class DipucrEnviaMailSolicitudRecibida implements IRule {
	protected static final Logger logger = Logger.getLogger(DipucrEnviaMailSolicitudRecibida.class);

	private static final String EMAIL_SUBJECT_VAR_NAME = "CDJ_CUR_ONLINE_EMAIL_SOL_ASUNTO";
	
	/**
	Solicitud de cursos on line recibida.
	<br/> 
	${NOMBRE}
	<br/>
	Curso: ${COD_CURSO} - ${DESCRIPCION_CURSO}.
	**/
	private static final String EMAIL_CONTENT_VAR_NAME = "CDJ_CUR_ONLINE_EMAIL_SOL_CONTENIDO";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		String numexp = "";

		try {
			logger.info("INICIO - " + this.getClass().getName());
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------

			numexp = rulectx.getNumExp();
			
			Map<String,String> variables = new HashMap<String,String>();
			variables.put("NUMEXP", numexp);

			IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);

			if (expediente != null) {
				String nombre = expediente.getString("IDENTIDADTITULAR");
				String correo = expediente.getString("DIRECCIONTELEMATICA");
				String nreg = expediente.getString("NREG");
	        	Date freg = expediente.getDate("FREG");

				IItemCollection solicitudCollection = entitiesAPI.getEntities("DPCR_DAT_CURSO_ONLINE", numexp);
				Iterator<?> solicitudIterator = solicitudCollection.iterator();
				
				if (solicitudIterator.hasNext()) {
					IItem solicitud = (IItem) solicitudIterator.next();
					String cod_curso = solicitud.getString("CURSO");
					String descripcion_curso = solicitud.getString("DESCRIPCION_CURSO");
					
					if (StringUtils.isNotEmpty(correo)) {
						if(StringUtils.isNotEmpty(nombre)){
							variables.put("NOMBRE", nombre);
						}
						if(StringUtils.isNotEmpty(cod_curso)){
							variables.put("COD_CURSO", cod_curso);
						}						
						if(StringUtils.isNotEmpty(descripcion_curso)){
							variables.put("DESCRIPCION_CURSO", descripcion_curso);
						}
						if(freg != null){
		        			variables.put("FECHAREG", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(freg));
		        		}
		        		if(StringUtils.isNotEmpty(nreg)){
		        			variables.put("NREG", nreg);
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
						//String rutaImg = parameters.get(ISPACConfiguration.IMAGES_REPOSITORY_PATH);
						String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
						/**
						 * [Teresa Ticket #59 FIN]Cambio de la ruta de las imágenes 
						 * **/
						Object[] imagen = {rutaImg, new Boolean(true), "logoCabeceraDCJBueno.bmp", "escudo"};
						List<Object[]> imagenes = new ArrayList<Object[]>();
						imagenes.add(imagen);
						try{
							MailUtil.enviarCorreo(rulectx.getClientContext(), correo, subject, content, imagenes);
							logger.info("[DipucrEnviaMailSolicitudRecibida:execute()] Email enviado a: "	+ correo);
						}
						catch(Exception e){
							logger.error(this.getClass().getName()+": Error al enviar e-mail a: "+correo, e);
							content = "ERROR - <br>"+content;
							subject = "ERROR - "+subject;
							try{
								CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
								correo = correoConfig.get("correo_cdj_error");
								MailUtil.enviarCorreo(rulectx.getClientContext(), correo, subject, content, imagenes);
							}catch(Exception e1){
								logger.error(this.getClass().getName()+": Error al enviar e-mail a: "+correo, e1);
							}
						}		     		    	   
					}
				}
			}
			logger.info("FIN - " + this.getClass().getName());
			
		} catch (ISPACException e) {
			logger.error("Error al enviar el correo de documentación incompleta del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
