package es.dipucr.sigem.api.rule.procedures.actaAudio;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class DipucrEnviaActaAudioRule implements IRule{
	
	/* SECRETARIA_EMAIL_DI_SUBJECT_AUDIOACTA_DIR_DOC: información de secretaria cuando se manda la convocatoria del orden del dia
	[SIGEM] ${SESION} Traslado de Audio acta de Pleno.
	*/
	
	/*SECRETARIA_EMAIL_DI_CONTENT_AUDIOACTA_DIR_DOC: Contenido de la convocatoria
	 * <font size='3'>
			<b>AUDIO - ACTA DE LA ULTIMA SESIÓN DE ${SESION}</b>
			<br>&nbsp;&nbsp;&nbsp;
				<li>
					<b> Sesión Nº </b>${NUMSESION}.
						&nbsp;&nbsp;&nbsp;
					<li>
						<b> Fecha: </b>${FECHASESION}. 
							&nbsp;&nbsp;&nbsp;
							</br>
								</br>
									Ya está disponible el Audio - Acta correspondiente a la sesión celebrada por el/la ${SESION} de la Corporación el día indicado.
									</br>
										Los señores Concejales integrantes del órgano, pueden acceder al documento pulsando										
										<a
											href="http://sei.dipucr.es:8080/SIGEM_BuscadorDocsWeb/getDocument.do?entidad=${ENTIDAD}&doc=${IDDOC}">aquí</a>
											
									</br>
								</br>
								 Un saludo.
							</br> </br> </br>
							SECRETARÍA GENERAL.
					</li>
				</li>
			</br>
		</font>
	 * 
	 */

	private static final Logger logger = Logger.getLogger(DipucrEnviaActaAudioRule.class);
	private static final String EMAIL_SUBJECT_VAR_NAME = "SECRETARIA_EMAIL_DI_SUBJECT_AUDIOACTA_DIR_DOC";
	private static final String EMAIL_CONTENT_VAR_NAME = "SECRETARIA_EMAIL_DI_CONTENT_AUDIOACTA_DIR_DOC";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //----------------------------------------------------------------------------------------------
		
			//Participantes
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(),  "", "ID");

			Iterator<IItem> itemPart = participantes.iterator();
			
			while(itemPart.hasNext()){
				
				IItem participante = itemPart.next();
				
				logger.warn("Cierre convocatoria. Participante: " + participante.getString("NOMBRE"));
				String strDestinatario = participante.getString("DIRECCIONTELEMATICA");
				
				String query = "NUMEXP='"+rulectx.getNumExp()+"' AND ID_TRAMITE="+rulectx.getTaskId()+"";
				logger.warn("query "+query);
				IItemCollection documento = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), query, "");
				int idDoc = 0;
				Iterator<IItem> itDoc = documento.iterator();
				while (itDoc.hasNext()) {
					IItem iDoc = (IItem) itDoc.next();
					idDoc = iDoc.getInt("ID");
					
				}
				//[Ticket #1301#]SIGEM envío de correo del audio acta con debates para los integantes de la sesión
				//Inserto los parametros del asunto
        		IItem sesion = SecretariaUtil.getSesion(rulectx, rulectx.getNumExp());
    			SimpleDateFormat dateformat1 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
    	        String strFecha = dateformat1.format(sesion.getDate("FECHA"));
				
				Map<String, String> variables = new HashMap<String, String>();

		        variables.put("NUMSESION", sesion.getString("NUMCONV"));
		        variables.put("FECHASESION", strFecha);
		        variables.put("HORASESION", sesion.getString("HORA"));
		        variables.put("ENTIDAD", EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()));
		        variables.put("IDDOC", idDoc+"");
				
				MailUtil.enviarCorreoConVariablesUsoExterno(rulectx, strDestinatario, EMAIL_SUBJECT_VAR_NAME, EMAIL_CONTENT_VAR_NAME, null, variables, true);
			}
			
			
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido cerrar el borrador de acta",e);
        }
	
		logger.info("FIN - " + this.getClass().getName());
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
