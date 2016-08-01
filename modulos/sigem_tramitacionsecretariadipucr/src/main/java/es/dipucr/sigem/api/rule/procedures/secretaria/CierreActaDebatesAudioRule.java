package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class CierreActaDebatesAudioRule implements IRule {
	/* SECRETARIA_EMAIL_DI_SUBJECT_AUDIOACTA: información de secretaria cuando se manda la convocatoria del orden del dia
	[SIGEM] ${SESION}, email informativo AUDIO ACTA
	*/
	
	/*SECRETARIA_EMAIL_DI_CONTENT_AUDIOACTA: Contenido de la convocatoria
	<font size='3'>
	<b>AUDIO - ACTA DE LA ULTIMA SESIÓN DE ${SESION}</b>
	<br>&nbsp;&nbsp;&nbsp;
		<li>
			<b> Sesión Nº </b>${NUMSESION}.
				&nbsp;&nbsp;&nbsp;
			<li>
				<b> Fecha: </b>${FECHASESION}. 
					&nbsp;&nbsp;&nbsp;
				<li>
					</br>
						</br>
							Ya está disponible el Audio - Acta correspondiente a la sesión celebrada por el/la ${SESION} de la Corporación el día indicado.
							</br>
								Los señores Concejales integrantes del órgano, si disponen de
								firma electrónica, pueden acceder al documento pulsando 
								<a
									http://10.12.200.100:8080/SIGEM_AutenticacionWeb/seleccionEntidad.do?REDIRECCION=ConsultaMiembro&tramiteId=&SESION_ID=&ENTIDAD_ID=001">aquí</a>
							</br>
						</br>
					</br>
				</li>
			</li>
		</li>
	</br>
</font>
*/
	
	protected static final Logger logger = Logger.getLogger(CierreActaDebatesAudioRule.class);
	/**
	 * Variables que se utilizarán para insertar en la bbdd los datos 
	 * sobre el envío correcto o incorrecto del email.
	 * */
	Date fechaEnvío = null;
	String nombreDoc = "";
	boolean enviadoEmail = false;
	String emailNotif = "";
	String descripError = "";
	String descripcionDoc = "";
	private static final String EMAIL_SUBJECT_VAR_NAME = "SECRETARIA_EMAIL_DI_SUBJECT_AUDIOACTA";
	private static final String EMAIL_CONTENT_VAR_NAME = "SECRETARIA_EMAIL_DI_CONTENT_AUDIOACTA";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
    		
    		

    		
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        //Comprobar que el documento esté firmado
			// --> Está ya hecho en ValidateFirmaConvocatoriaRule, ejecutada antes que esta regla

			//Obtener Participantes de la sesion
			String strNumExp = rulectx.getNumExp();
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, strNumExp, "", "ID");
			
			//Obtener el documento Certificado de acuerdos para anexarlo al email 
			int taskId = rulectx.getTaskId();
			String sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA IN ('02','03')";
			IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");

			if (documentos.toList().size() == 1) 
			{
				//Enviar email con la convocatoria adjunta

				// Fichero a adjuntar
				IItem doc = (IItem)documentos.iterator().next();
				
				// Para cada participante seleccionado enviar email
		        logger.warn("Cierre convocatoria. Recorrer participantes");
		     // Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
				if(participantes != null && participantes.toList()!= null && participantes.toList().size() != 0){
					for (int i=0; i<participantes.toList().size(); i++)
					{
						IItem participante = (IItem) participantes.toList().get(i);
						if (participante!=null)
						{
							logger.warn("Cierre convocatoria. Participante: " + participante.getString("NOMBRE"));
							emailNotif = participante.getString("DIRECCIONTELEMATICA");
							/**
							 * INICIO
							 * ##Ticket #147 SIGEM Documento que especifique a los notificados.
							* **/
							nombreDoc = doc.getString("NOMBRE");
							descripcionDoc = doc.getString("DESCRIPCION");
							/**
							 * FIN
							 * ##Ticket #147 SIGEM Documento que especifique a los notificados.
							* **/
							if (emailNotif != null)
							{
								StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
								while (tokens.hasMoreTokens()) 
								{
									String strDestinatario = tokens.nextToken();	
							        logger.warn("Cierre convocatoria. email: " + strDestinatario);
									if (participante!=null)
									{
							        	if (!strDestinatario.equals("")) 
							        	{
									        logger.warn("Enviar correo...");
							        		
							        		//Inserto los parametros del asunto
							        		IItem sesion = SecretariaUtil.getSesion(rulectx, rulectx.getNumExp());
							    			SimpleDateFormat dateformat1 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
							    	        String strFecha = dateformat1.format(sesion.getDate("FECHA"));
							        		
							    	        Map<String, String> variables = new HashMap<String, String>();
							    	        //String area =SecretariaUtil.getNombreAreaSesion(rulectx, rulectx.getNumExp());
							    	        String sesionOrgaCole = SecretariaUtil.getNombreOrganoSesion(rulectx, rulectx.getNumExp());

							    	        //Quitado porque queria Luis que apareciera sólo el nombre de COMISIÓN INFORMATIVA
							    	        //y el área de la comisión
							    	        //if(sesionOrgaCole.equals("COMISIÓN INFORMATIVA")){
							    	        	//sesionOrgaCole += " DE "+ area.toUpperCase();
							    	        //}
							    	        
							    	        variables.put("SESION", sesionOrgaCole);
							    	        variables.put("NUMSESION", sesion.getString("NUMCONV"));
							    	        variables.put("FECHASESION", strFecha);
							    	        variables.put("HORASESION", sesion.getString("HORA"));

							        		MailUtil.enviarCorreoConVariablesUsoExterno(rulectx, strDestinatario, EMAIL_SUBJECT_VAR_NAME, EMAIL_CONTENT_VAR_NAME, null, variables, true);

									        logger.warn("Enviado");
								        }
							        	else{
											descripError = " El email esta  vacío";
											enviadoEmail = false;
										}
								    }
								}
							}
							else{
								descripError = " El email esta vacío";
								enviadoEmail = false;
							}							
						}						
					}
				}
				else{
					descripError = " No existen participantes para este documento.";
					enviadoEmail = false;
				}
				
			}
	        
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	logger.error("Error en el envío del audio acta "+e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido cerrar la convocatoria",e);
        	
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }    
}