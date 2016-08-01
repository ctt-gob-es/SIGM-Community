package es.dipucr.sigem.api.rule.procedures.bop.imprenta;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.ownCloud.OwnCloudConfiguration;
import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.OwnCloudUtils;

public class DipucrCreaCarpetaOwnCod implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrCreaCarpetaOwnCod.class);
	
	private static final String EMAIL_SUBJECT_VAR_NAME = "IMPR_SOL_TRAB_ASUNTO";
	
	/**
	<img src='cid:escudo' width='200px'/>
	<p align=justify>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Estimado señor/a:
		<br/>
		<br/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;La Imprenta Provincial ha recibido su solicitud de impresión del trabajo <b>${TRABAJO}</b> el <b>${FECHAREG}</b> y se ha registrado con número <b>${NREG}</b> (expediente asociado a la solicitud con número <b>${NUMEXP}</b>).
		<br/>
		<br/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Coloque el archivo electrónico que contiene el trabajo solicitado en el siguiente enlace: <a href="${URL}" target='_blank'>${URL}</a>
		<br/>
		<br/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Y pulse el botón <img src='cid:imgSubir' width='40px'/>.
		<br/>
		<br/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cuando la impresión haya concluido recibirá un aviso informativo.
		<br/>
		<br/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Un saludo.
		<br/>
		<br/>
	</p>
	<br/>
	<br/>
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
	private static final String EMAIL_CONTENT_VAR_NAME = "IMPR_SOL_TRAB_CONTENIDO";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";

		try {
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			numexp = rulectx.getNumExp();
			
			Map<String,String> variables = new HashMap<String,String>();
			variables.put("NUMEXP", numexp);
						
			OwnCloudConfiguration ownCloudConfig = OwnCloudConfiguration.getInstance();
			
			String username = ownCloudConfig.getProperty(OwnCloudConfiguration.USR_IMPRENTA);
			String password = ownCloudConfig.getProperty(OwnCloudConfiguration.USR_PASSWORD);
							    
			String directorio = numexp.replace("/", "_");
            
            if(OwnCloudUtils.crear(username, password, directorio)){
                        
	            String respuestaCompartir = OwnCloudUtils.compartir(username, password, directorio);
	            
	            if(respuestaCompartir.equals(OwnCloudUtils.COD_ERROR)){
	            	throw new ISPACRuleException("Error al compartir la carpeta al recibir la solicitud de trabajo a imprenta con numexp: " + numexp);
	            }
	            else{
	            	String dirCompartida = (respuestaCompartir.split("<url>"))[1].split("</url>")[0];
			        
			        IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);

			        if(expediente != null){
			        	String nreg = expediente.getString("NREG");
			        	Date freg = expediente.getDate("FREG");
			        	
			        	IItemCollection solicitudCollection = entitiesAPI.getEntities("DPCR_DAT_PET_TRAB_IMPRENTA", numexp);
				        if(solicitudCollection.toList().size() > 0){
				        	IItem solicitud = (IItem) solicitudCollection.iterator().next(); 
				        	String emails = solicitud.getString("EMAILCONTACTO");
				        	String tipo_trabajo = solicitud.getString("TIPO_TRABAJO");
		
				        	if(StringUtils.isNotEmpty(emails)){		        								
				        		if(StringUtils.isNotEmpty(tipo_trabajo)){
				        			variables.put("TRABAJO", tipo_trabajo);
				        		}
				        		if(freg != null){
				        			variables.put("FECHAREG", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(freg));
				        		}
				        		if(StringUtils.isNotEmpty(nreg)){
				        			variables.put("NREG", nreg);
				        		}
				        		if(StringUtils.isNotEmpty(numexp)){
				        			variables.put("NUMEXP", numexp);
				        		}
				        		if(StringUtils.isNotEmpty(dirCompartida)){
									variables.put("URL", dirCompartida );
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
								Object[] imagen1 = {rutaImg, new Boolean(true), "logoCabeceraBOP.bmp", "escudo"};
								Object[] imagen2 = {rutaImg, new Boolean(true), "imgSubir.bmp", "imgSubir"};

								List<Object[]> imagenes = new ArrayList<Object[]>();
								imagenes.add(imagen1);
								imagenes.add(imagen2);
								String[] emailsSplit = emails.split(";");
								for(String email : emailsSplit){
									try{	
										if(StringUtils.isNotEmpty(email)){
											MailUtil.enviarCorreo(rulectx, email.trim(), subject, content, imagenes);
											logger.info("[DipucrEnviaMailSolicitudRecibida:execute()] Email enviado a: "	+ email.trim());
										}
									}
									catch(Exception e){
										logger.error(this.getClass().getName()+": Error al enviar e-mail a: " + email + ". " + e.getMessage(), e);
										content = "ERROR - <br>" + content;
										subject = "ERROR - " + subject;
										try{
											CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
											email = correoConfig.get("correo_imprenta_error");
											MailUtil.enviarCorreo(rulectx, email, subject, content, imagenes);
										}catch(Exception e1){
											logger.error(this.getClass().getName()+": Error al enviar e-mail a: " + email + ". " + e1.getMessage(), e1);
										}
									}
								}		     		    	   
				        	}
				        }	
			        }
		        }	
            }
		} catch (ISPACException e) {
			logger.error("Error al crear la carpeta del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al crear la carpeta del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
