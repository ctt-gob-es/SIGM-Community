package es.dipucr.sigem.api.rule.procedures.comunadminelec;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import Saicar.Recursos.Correo.Correo;
import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class TrasladarComAdminis  implements IRule {
	
	private static final Logger logger = Logger.getLogger(TrasladarComAdminis.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	
		boolean terminar = false;
		
		/**
		 * Variables que se utilizarán para insertar en la bbdd los datos 
		 * sobre el envío correcto o incorrecto del email.
		 * */
		String nombreNotif = "";
		Date fechaEnvío = null;
		String nombreDoc = "";
		String descripcionDoc = "";
		boolean enviadoEmail = false;
		String emailNotif = "";
		String descripError = "";
		
		IClientContext cct = rulectx.getClientContext();
		
		try {
			//APIs
			
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			// 2. Obtener Participantes del expediente actual, con relación "Trasladado"
			String numExp = rulectx.getNumExp();
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL= 'TRAS'", "ID");
			
			if (participantes != null && participantes.toList().size() > 0){
				// 3. Obtener el documento decreto para anexarlo al email 
				String sql = "ID_TRAMITE="+rulectx.getTaskId()+" AND NOMBRE='Carta digital'  AND NREG IS NULL";
				IItemCollection documentos = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), sql, "ID DESC");
				
				if (documentos == null || documentos.toList().size()==0){
					throw new ISPACRuleException("No se pueden enviar los traslados: El documento 'Plantilla Carta digital' no está firmado en el trámite.");
				}else if (documentos.toList().size() >= 1) {
					IItem doc = (IItem)documentos.iterator().next();
					if(doc.get("ESTADOFIRMA").equals("02")){
						// 4. Enviar email con la carta digital adjunta				
						String cContenido = "<br/>Adjunto se envía el documento de la Comunicación administrativa electrónica";
						String cAsunto= "[SIGEM] Comunicación administrativa electrónica - Rte. "+EntidadesAdmUtil.obtenerNombreLargoEntidadById(cct);
						
						// Fichero a adjuntar
						//IItem doc = (IItem)documentos.iterator().next();
						String infoPagRde = doc.getString("INFOPAG_RDE");
						if(infoPagRde!=null){
							File file = DocumentosUtil.getFile(cct, infoPagRde, null, null);
							//Se cambia la configuración del correo de ApplicationResources.properties a su archivo de configuración por entidad
							CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
							String cCorreoOrigen = correoConfig.get(CorreoConfiguration.CONV_FROM);
							String nombreImagen = ConfigurationMgr.getVarGlobal(cct, "SECRETARIA_EMAIL_NOMBRE_IMAGEN_CABECERA");
							if(nombreImagen==null || nombreImagen.equals("")){
								nombreImagen = "logoCabecera.gif";
							}
							String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
							
							Object[] imagen = {rutaImg, new Boolean(true), nombreImagen, "escudo"};
							List<Object[]> imagenes = new ArrayList<Object[]>();
							imagenes.add(imagen);
							
							// Para cada participante seleccionado --> enviar email y actualizar el campo DECRETO_TRASLADADO en la BBDD
							for (int i=0; i<participantes.toList().size(); i++){
								
								IItem participante = (IItem) participantes.toList().get(i);
								nombreNotif = participante.getString("NOMBRE");
								emailNotif = participante.getString("DIRECCIONTELEMATICA");
								nombreDoc = doc.getString("NOMBRE");
								descripcionDoc = doc.getString("DESCRIPCION");
								if (emailNotif != null){
									StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
		
									while (tokens.hasMoreTokens()) {
										
										String cCorreoDestino = tokens.nextToken();	
						
										if (participante!=null){
								        	if (!cCorreoDestino.equals("")) {

								        		String dir[]= MailUtil.enviarCorreo(rulectx, cCorreoOrigen, cCorreoDestino, cAsunto, cContenido, file, imagenes);
					        	
									        }
									    }
									}
								}
							}
							file.delete();
						}						
					}
				}
			}			
		} catch (ISPACException e) {
			logger.error("Error: " + rulectx.getNumExp() + " NUMEXP. "+rulectx.getNumExp()+"- . " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		} 
		
		
    	return new Boolean(terminar);
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {	
	}

}
