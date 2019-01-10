package es.dipucr.portafirmas.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
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


public class AvisoEmailParticipantesFirma  implements IRule {
	
	
	private static final Logger logger = Logger.getLogger(AvisoEmailParticipantesFirma.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean terminar = true;
		try
		{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //---------------------------------------------------------------------------------------------- 
			if(StringUtils.isEmpty(ConfigurationMgr.getVarGlobal(cct, "PORTAFIRMAS_ASUNTO")) && StringUtils.isEmpty(ConfigurationMgr.getVarGlobal(cct, "PORTAFIRMAS_CONTENIDO"))){
				terminar = false;
				rulectx.setInfoMessage("Faltan por configurar los campos 'PORTAFIRMAS_ASUNTO', 'PORTAFIRMAS_CONTENIDO'. Contacte con su administrador.");
			}
		}
		catch(ISPACException e) 
		{
			logger.error("Error en el expediente. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el expediente. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
		return terminar;		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		try
		{
			/**
			 * Variables que se utilizarán para insertar en la bbdd los datos 
			 * sobre el envío correcto o incorrecto del email.
			 * */

			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //---------------------------------------------------------------------------------------------- 
	        logger.warn("INICIO TrasladarSubsanacionInforme. ");
        	
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(),  "ROL='INT'", "");
			
			String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
			
			String nombreImagen = ConfigurationMgr.getVarGlobal(cct, "PORTAFIRMAS_EMAIL_NOMBRE_IMAGEN_CABECERA");
			Object[] imagen = {rutaImg, new Boolean(true), nombreImagen, "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);
			
			String cAsunto= ConfigurationMgr.getVarGlobal(cct, "PORTAFIRMAS_ASUNTO")+rulectx.getNumExp();
			String cContenido =  ConfigurationMgr.getVarGlobal(cct, "PORTAFIRMAS_CONTENIDO");
						
			Map<String, String> variables = new HashMap<String, String>();
			variables.put("NOMBREENTIDAD", EntidadesAdmUtil.obtenerNombreLargoEntidadById(cct));
			if (StringUtils.isNotBlank(cContenido)) {
				cContenido = StringUtils.replaceVariables(cContenido, variables);
			}
			
			cContenido = MailUtil.formateContenidoEmail(rulectx, cAsunto, cContenido);
			
			if(participantes != null && participantes.toList()!= null && participantes.toList().size() != 0){
				for (int i=0; i<participantes.toList().size(); i++)
				{					
					IItem participante = (IItem) participantes.toList().get(i);
					String emailNotif = participante.getString("DIRECCIONTELEMATICA");
					
					if (emailNotif != null)
					{
						StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
						while (tokens.hasMoreTokens()) 
						{
							String cCorreoDestino = tokens.nextToken();	
							if (participante!=null)
							{
					        	if (!cCorreoDestino.equals("")) 
					        	{
					        		logger.warn("Numexp. "+rulectx.getNumExp()+" - direccion telematica para el envío del documento. "+cCorreoDestino);
						        	// Confeccionar el email
					        		MailUtil.enviarCorreo(rulectx.getClientContext(), null, cCorreoDestino, cAsunto, cContenido, null, imagenes);
					        	}
							}
						}
					}
				}
			}
			logger.warn("FIN TrasladarSubsanacionInforme. ");
			
			return new Boolean(true);
		}
		catch(ISPACException e) 
		{
			logger.error("Error en el expediente. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el expediente. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
	}
		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
